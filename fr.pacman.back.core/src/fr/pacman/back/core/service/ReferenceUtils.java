package fr.pacman.back.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.obeonetwork.dsl.environment.EnvironmentFactory;
import org.obeonetwork.dsl.environment.MultiplicityKind;
import org.obeonetwork.dsl.environment.NamespacesContainer;
import org.obeonetwork.dsl.environment.Reference;
import org.obeonetwork.dsl.environment.StructuredType;

/**
 * Fournit les utilitaires permettant d'analyser les références d'un modèle et
 * de construire les références de travail associées aux types structurés
 * (Entity / DTO).
 *
 * Les références de travail résultantes prennent en compte la hiérarchie
 * d'héritage des types, les relations unidirectionnelles et bidirectionnelles,
 * ainsi que les relations réflexives et les multiplicités.
 *
 * Les méthodes de cette classe sont destinées à être appelées depuis les
 * générateurs Acceleo afin de notamment faciliter l'exploitation des références
 * résolues (héritage) et la 'complexité' liée à la modélisation des références
 * unidirectionnelles multiples (0,*) lors de la génération de code.
 * 
 * Lorsqu'une référence est héritée, les types contenants et référencés doivent
 * être réaffectés pour correspondre aux sous-types concernés. Cette adaptation
 * nécessite donc de modifier la modélisation au moyen des méthodes
 * {@code setContainingType()} et {@code setReferencedType()} des références.
 * 
 * Il est absolument impératif de ne pas effectuer ces modifications directement
 * sur la référence initiale, car cela altérerait le modèle d'origine et
 * pourrait provoquer des incohérences dans les autres références qui
 * l'utilisent.
 * 
 * Cette classe s'appuie sur le principe de modèle anémique pour représenter les
 * types et les références nécessaires au traitement des références héritées.
 * L'objectif n'est pas de reproduire intégralement le modèle métier d'origine,
 * mais d'en extraire et de conserver uniquement les informations strictement
 * utiles aux traitements réalisés par les méthodes AQL de Pacman.
 * 
 * Les objets manipulés sont ainsi reconstruits de manière totalement
 * indépendante du modèle source, sans reprendre l'ensemble de ses attributs, de
 * ses relations ni de ses mécanismes de contenance. Cette approche limite les
 * dépendances avec le modèle d'origine, évite les effets de bord liés à la
 * manipulation d'objets EMF partagés et garantit que les traitements effectués
 * ici ne modifient pas involontairement le modèle initial.
 * 
 * Envore une fois, il est important de garder à l'esprit que les générateurs
 * Sirius/Acceleo ne sont pas des programmes autonomes dont l'exécution se
 * termine avec la génération des fichiers. Ils sont intégrés à Eclipse sous
 * forme de plugins et suivent donc le cycle de vie de l'environnement Eclipse
 * et de sa JVM.
 * 
 * Le modèle Sirius est conservé en mémoire après l'exécution du générateur. La
 * fin de la génération ne signifie donc ni l'arrêt de la JVM, ni la destruction
 * des objets du modèle. Ceux-ci restent accessibles et peuvent être réutilisés
 * ou manipulés lors d'exécutions ultérieures.
 * 
 * Par conséquent, toute modification directe du modèle initial peut avoir des
 * effets persistants au-delà de l'exécution du générateur et perturber les
 * traitements suivants. Il est donc impératif de privilégier la création
 * d'objets indépendants pour les traitements temporaires et d'éviter toute
 * modification involontaire du modèle source.
 * 
 * La méthode {@code copyReferenceWithOpposite} est conservée dans la classe à
 * titre indicatif et comme trace d'une approche précédemment étudiée. Elle
 * n'est volontairement plus utilisée. La copie d'objets EMF, notamment
 * lorsqu'ils comportent des références croisées, des relations de contenance ou
 * des références bidirectionnelles, présente des risques d'effets de bord
 * difficiles à anticiper et à maîtriser. Son utilisation pourrait notamment
 * entraîner des modifications involontaires du modèle initial.
 * 
 * L'approche retenue repose désormais sur la reconstruction totale et explicite
 * d'objets indépendants, limités aux informations strictement nécessaires au
 * traitement.
 * 
 * ATTENTION : dans les requêtes AQL liées au traitement des références, ne
 * JAMAIS faire appel aux méthodes permettant de récupérer l'entité associée à
 * un DTO (fichier dto.mtl). En présence d'héritage, le modèle est alors
 * anémique et les objets de structure {@code ContainingType} et
 * {@code ReferencedType] ne contiennent plus les informations 
 * permettant de retrouver l'entité d'origine et vont renvoyer null !
 * 
 * @author MINARM
 */
public class ReferenceUtils {

	/**
	 * Ensemble des références de travail résolues à partir du modèle.
	 */
	private static Set<WorkingReference> _workingReferences;

	/**
	 * Initialise l'ensemble des références de travail à partir des références
	 * présentes dans le système racine.
	 *
	 * @param p_root système racine contenant les éléments du modèle à analyser
	 */
	public static void do_initializeWorkingReferences(final NamespacesContainer p_root) {
		resolveWorkingReferences(collectReferences(p_root.eAllContents()),
				collectStructuredTypes(p_root.eAllContents()));
	}

	/**
	 * Parcourt l'ensemble des éléments contenus dans l'arbre du modèle afin d'en
	 * extraire toutes les références.
	 *
	 * @param p_iterator itérateur parcourant l'ensemble des éléments du modèle
	 * @return ensemble des références détectées, sans doublons et dans leur ordre
	 *         de parcours
	 */
	private static Set<Reference> collectReferences(final TreeIterator<EObject> p_iterator) {
		Set<Reference> v_references = new LinkedHashSet<>();
		while (p_iterator.hasNext()) {
			EObject object = p_iterator.next();
			if (object instanceof Reference reference)
				v_references.add(reference);
		}
		return v_references;
	}

	/**
	 * Parcourt l'ensemble des éléments du modèle afin d'en extraire tous les types
	 * structurés (Entity / DTO).
	 *
	 * @param p_iterator itérateur parcourant l'ensemble des éléments du modèle
	 * @return ensemble des types structurés détectés, sans doublons et dans leur
	 *         ordre de parcours
	 */
	private static Set<StructuredType> collectStructuredTypes(final TreeIterator<EObject> p_iterator) {
		Set<StructuredType> v_structuredTypes = new LinkedHashSet<>();
		while (p_iterator.hasNext()) {
			EObject object = p_iterator.next();
			if (object instanceof StructuredType structuredType)
				v_structuredTypes.add(structuredType);
		}
		return v_structuredTypes;
	}

	/**
	 * Indique si la référence possède une multiplicité multiple (plusieurs éléments
	 * autorisés).
	 *
	 * @param p_reference référence à vérifier
	 * @return {@code true} si la multiplicité est {@code ONE_STAR} ou
	 *         {@code ZERO_STAR}, {@code false} sinon
	 */
	private static boolean isMultiple(final Reference p_reference) {
		return p_reference.getMultiplicity() == MultiplicityKind.ONE_STAR_LITERAL
				|| p_reference.getMultiplicity() == MultiplicityKind.ZERO_STAR_LITERAL;
	}

	/**
	 * Construit les références de travail à partir des références du modèle, en
	 * tenant compte de leur potentiel caractère récursif.
	 *
	 * Pour chaque référence, résout les types hérités associés et génère les
	 * combinaisons de types contenues et référencées. Les références opposées des
	 * relations bidirectionnelles déjà traitées sont ignorées afin d'éviter de
	 * générer deux fois la même relation.
	 * 
	 * Pour une référence réflexive, chaque type concret hérité est associé à
	 * lui-même afin d'éviter de générer des associations croisées entre les
	 * différents types descendants.
	 *
	 * @param p_references      ensemble des références du modèle à traiter
	 * @param p_structuredTypes ensemble des types structurés présents dans le
	 *                          modèle
	 */
	private static void resolveWorkingReferences(final Set<Reference> p_references,
			Set<StructuredType> p_structuredTypes) {

		_workingReferences = new LinkedHashSet<>();

		for (Reference v_reference : p_references) {

			// Récupération des structures (entity/dto) potentiellement héritées.
			List<StructuredType> _containing = resolveContainingTypes(v_reference, p_structuredTypes);
			List<StructuredType> _referenced = resolveReferencedTypes(v_reference, p_structuredTypes);

			// Traitement de la recursivité (évite les associations croisées).
			if (v_reference.getContainingType() == v_reference.getReferencedType()) {
				for (StructuredType v_type : _referenced) {
					addWorkingReference(v_reference, v_type, v_type);
				}
			}
			// Traitement des relations (unidirectionnelles non réflexives et
			// bidirectionnelles).
			else {
				for (StructuredType v_containingType : _containing) {
					for (StructuredType v_referencedType : _referenced) {
						addWorkingReference(v_reference, v_containingType, v_referencedType);
					}
				}
			}
		}
		logWorkingReferences();
	}

	/**
	 * Résout les types contenus associés à une référence en tenant compte de leur
	 * hiérarchie d'héritage.
	 *
	 * @param p_reference       référence dont le type contenant doit être résolu
	 * @param p_structuredTypes ensemble des types structurés présents dans le
	 *                          modèle
	 * @return liste des types contenus les plus spécialisés, sans leurs super-types
	 */
	private static List<StructuredType> resolveContainingTypes(final Reference p_reference,
			final Set<StructuredType> p_structuredTypes) {
		return resolveConcreteTypes(p_reference.getContainingType(), p_structuredTypes);
	}

	/**
	 * Résout les types référencés associés à une référence en tenant compte de leur
	 * hiérarchie d'héritage.
	 *
	 * @param p_reference       référence dont le type référencé doit être résolu
	 * @param p_structuredTypes ensemble des types structurés présents dans le
	 *                          modèle
	 * @return liste des types référencés les plus spécialisés, sans leurs
	 *         super-types
	 */
	private static List<StructuredType> resolveReferencedTypes(final Reference p_reference,
			final Set<StructuredType> p_structuredTypes) {
		return resolveConcreteTypes(p_reference.getReferencedType(), p_structuredTypes);
	}

	/**
	 * Détermine les types les plus spécialisés à partir d'un type structuré donné.
	 *
	 * Si le type possède des descendants, seuls les descendants terminaux de la
	 * hiérarchie sont conservés. Dans le cas contraire, le type initial est
	 * retourné.
	 *
	 * @param p_type            type structuré à partir duquel effectuer la
	 *                          résolution
	 * @param p_structuredTypes ensemble des types structurés présents dans le
	 *                          modèle
	 * @return liste des types terminaux de la hiérarchie, ou liste vide si le type
	 *         initial est null
	 */
	private static List<StructuredType> resolveConcreteTypes(final StructuredType p_type,
			final Set<StructuredType> p_structuredTypes) {
		return p_type == null ? new ArrayList<>() : resolveSubTypes(p_type, p_structuredTypes);
	}

	/**
	 * Parcourt réflexivement la hiérarchie d'héritage afin de récupérer uniquement
	 * les types terminaux, c'est-à-dire les types ne possédant aucun sous-type.
	 *
	 * Les types intermédiaires et leurs super-types sont ainsi exclus du résultat.
	 *
	 * @param p_type            type structuré à partir duquel rechercher les
	 *                          descendants
	 * @param p_structuredTypes ensemble des types structurés présents dans le
	 *                          modèle
	 * @return liste des types terminaux de la hiérarchie
	 */
	private static List<StructuredType> resolveSubTypes(final StructuredType p_type,
			final Set<StructuredType> p_structuredTypes) {
		final List<StructuredType> v_structuredTypes = new ArrayList<>();
		boolean v_hasSubTypes = false;

		for (StructuredType structuredType : p_structuredTypes) {
			if (structuredType.getSupertype() == p_type) {
				v_hasSubTypes = true;
				v_structuredTypes.addAll(resolveSubTypes(structuredType, p_structuredTypes));
			}
		}
		if (!v_hasSubTypes)
			v_structuredTypes.add(p_type);

		return v_structuredTypes;
	}

	/**
	 * Retourne la référence éventuellement modifiée, correspondant au
	 * {@link WorkingReference}.
	 * 
	 * Lorsque la résolution des types contenus et référencés nécessite une
	 * modification de la référence initiale ({@code requiresNewReference()}), une
	 * nouvelle référence indépendante est créée à l'aide de
	 * {@link #newReferenceWithOpposite(WorkingReference)}. Cette copie permet
	 * notamment de préserver les relations bidirectionnelles ainsi que les
	 * realtions unidirectionnelles multiples sans modifier le modèle initial.
	 * 
	 * Lorsqu'aucune modification n'est nécessaire, la référence initiale est alors
	 * directement retournée.
	 *
	 * @param p_workingReference référence de travail contenant la référence
	 *                           initiale et les types résolus
	 * @return la référence initiale ou sa copie selon que sa modification nécessite
	 *         ou non une nouvelle instance
	 */
	private static Reference getResolvedReference(final WorkingReference p_workingReference) {
		if (p_workingReference.requiresNewReference()) {
			return newReferenceWithOpposite(p_workingReference);
		}
		return p_workingReference.getInitialReference();
	}

	/**
	 * Crée et enregistre une référence de travail à partir d'une référence du
	 * modèle et des types structurés contenant et référencé associés.
	 *
	 * Initialise les informations de la référence de travail, notamment sa
	 * référence d'origine, ses types contenant et référencé, ainsi que ses
	 * caractéristiques unidirectionnelle, réflexive et multiple.
	 *
	 * Une relation bidirectionnelle est représentée dans le modèle par deux
	 * références opposées, chacune portée par l'une des deux extrémités de
	 * l'association. Ces deux références décrivent la même relation, mais dans des
	 * sens de navigation opposés. Les enregistrer toutes les deux conduirait à
	 * représenter deux fois la même association dans les références de travail.
	 * 
	 * Pour éviter cette duplication, seule la référence non navigable est
	 * conservée. Ainsi, lorsque la référence initiale est bidirectionnelle et
	 * navigable, la méthode interrompt immédiatement son traitement, sans créer ni
	 * enregistrer la référence de travail. Au niveau Hibernate on positionne donc
	 * la liste dans le 'containing'.
	 * 
	 * La propriété {@code bidirectional} ne doit jamais être renseignée
	 * explicitement à cet endroit : elle est automatiquement déduite de la
	 * propriété {@code unidirectionnal} lors de l'appel à
	 * {@code setUnidirectionnal()}, qui positionne simultanément
	 * {@code bidirectional} à la valeur inverse.
	 * 
	 * Au niveau AQL,la détection d'une relation bidirectionnelle repose uniquement
	 * sur la présence d'une valeur non nulle dans la propriété {@code oppositeOf}.
	 * Cette règle est suffisante dans le contexte de Pacman, car les relations
	 * bidirectionnelles de cardinalité {@code 1} ou {@code 0..1} sont interdites.
	 * Il n'est donc pas nécessaire de vérifier les cardinalités, la présence de
	 * {@code oppositeOf} suffit à identifier une relation bidirectionnelle.
	 *
	 * Il est à noter qu'une relation à la fois multiple et réflexive est également
	 * considérée comme bidirectionnelle, afin de tenir compte de sa nature
	 * auto-référente.
	 *
	 * Par ailleurs, on inverse les extrémités de la WorkingReference pour les
	 * relations unidirectionnelles multiples (0,*). Dans le modèle initial, la
	 * relation est naturellement définie de l'entité contenant vers l'entité
	 * référencée (A → B), ce qui signifie qu'une instance de A peut être associée à
	 * plusieurs instances de B. Conformément à cette sémantique fonctionnelle,
	 * c'est l'entité B qui doit porter la clé étrangère vers A en base de données
	 * et A contient (pour hibernate) une liste de B.
	 *
	 * Sans cette adaptation, les relations unidirectionnelles imposeraient de gérer
	 * deux conventions différentes dans les requêtes AQL : pour les relations
	 * (0,1), le sens de traitement serait 'containing' vers 'referenced', tandis
	 * que pour les relations (0,*), il faudrait raisonner dans le sens opposé, de
	 * 'referenced' vers 'containing', puisque c'est cette dernière qui porte la clé
	 * étrangère. Cela obligerait à multiplier les requêtes spécifiques ou à
	 * systématiquement doubler les conditions dans de nombreux templates :
	 * génération des tables, des clés étrangères, des contraintes, des index, des
	 * entités Hibernate, des DTO, des XTO, etc.
	 * 
	 * En inversant directement les extrémités de la WorkingReference pour les
	 * relations unidirectionnelles multiples, on uniformise leur représentation
	 * technique et on permet à l'ensemble des traitements de conserver une
	 * convention unique : 'containing' désigne toujours l'entité qui porte la
	 * référence et donc, le cas échéant, la clé étrangère; 'referenced' désigne
	 * toujours l'entité référencée.
	 * 
	 * Cette petite adaptation constitue ainsi une astuce volontaire de
	 * normalisation qui évite de complexifier inutilement les requêtes Acceleo,
	 * réduit leur nombre et améliore leur lisibilité et leur maintenabilité pour le
	 * développeur du générateur, sans pour autant imposer à l'utilisateur une
	 * modélisation contraire à la compréhension naturelle de la relation.
	 *
	 * Le modèle exprime les relations telles que l'utilisateur les comprend. La
	 * WorkingReference les représente telles que le générateur doit les traiter
	 * techniquement.
	 * 
	 * @param p_reference  référence initiale du modèle à traiter
	 * @param p_containing type structuré contenant la référence
	 * @param p_referenced type structuré ciblé par la référence
	 */
	private static void addWorkingReference(final Reference p_reference, final StructuredType p_containing,
			final StructuredType p_referenced) {

		final WorkingReference v_workingReference = new WorkingReference();
		v_workingReference.setInitialReference(p_reference);

		v_workingReference.setUnidirectionnal(p_reference.getOppositeOf() == null);
		if (v_workingReference.isBidirectional() && !p_reference.isNavigable())
			return;

		v_workingReference.setReflexive(p_containing == p_referenced);
		v_workingReference.setMultiple(isMultiple(p_reference));

		// Juste au niveau de cette classe.. il faut refaire le test en aql.
		if (v_workingReference.isMultiple() && v_workingReference.isReflexive())
			v_workingReference.setUnidirectionnal(false);

		// Traitement des références (0,*) pour les unidirectionnelles,on inverse les
		// acteurs pour uniformiser les méthodes aql ('containing' -> 'referenced').
		if (v_workingReference.isUnidirectionnal() && v_workingReference.isMultiple()) {
			v_workingReference.setContainingType(p_referenced);
			v_workingReference.setReferencedType(p_containing);
		} else {
			v_workingReference.setContainingType(p_containing);
			v_workingReference.setReferencedType(p_referenced);
		}

		// Récupération de la veritable référence de travail pour Pacman.
		Reference v_pacmanReference = getResolvedReference(v_workingReference);
		v_workingReference.setPacmanReference(v_pacmanReference);

		// Ajout du descriteur de références dans le set.
		_workingReferences.add(v_workingReference);
	}

	/**
	 * Réalise une copie indépendante d'une référence et de sa référence opposée
	 * éventuelle.
	 * 
	 * La copie est effectuée à l'aide d'un {@link Copier} afin de conserver la
	 * cohérence des relations entre les objets copiés. Lorsque la référence
	 * initiale possède un {@code oppositeOf}, les deux références sont ajoutées aux
	 * objets à copier.
	 * 
	 * Une attention particulière doit être portée à l'ordre des opérations.En
	 * effet, ces modifications peuvent entraîner la suppression de la propriété
	 * {@code oppositeOf}, aussi bien sur la référence copiée que sur la référence
	 * initiale lorsque des objets sont encore partagés entre les deux modèles.
	 * 
	 * Après la copie, les types contenant et référencé sont réaffectés sur la
	 * nouvelle référence à partir des informations résolues dans le
	 * {@link WorkingReference}.
	 * 
	 * Cette opération doit impérativement précéder l'appel à
	 * {@link Copier#copyReferences()}, car les méthodes de réaffectation des types
	 * peuvent modifier les liens {@code oppositeOf} lorsque les références copiées
	 * partagent encore des objets avec le modèle initial.
	 * 
	 * L'appel final à {@code copyReferences()} rétablit les références croisées
	 * entre les objets copiés. La nouvelle référence conserve ainsi son
	 * {@code oppositeOf}, qui pointe vers la copie de la référence opposée, tandis
	 * que la référence initiale et son propre {@code oppositeOf} restent inchangés.
	 *
	 * @param p_workingReference référence de travail contenant la référence
	 *                           initiale et les types résolus
	 * @return copie indépendante de la référence initiale, avec sa référence
	 *         opposée éventuellement copiée et correctement reliée
	 */
	@SuppressWarnings("unused")
	private static Reference copyReferenceWithOpposite(final WorkingReference p_workingReference) {

		Reference v_initialReference = p_workingReference.getInitialReference();
		Collection<EObject> v_objects = new ArrayList<>();

		// Reference est copiée indépendamment
		v_objects.add(v_initialReference);

		// Sa référence oppositeOf est également copiée.
		if (v_initialReference.getOppositeOf() != null)
			v_objects.add(v_initialReference.getOppositeOf());

		Copier v_copier = new Copier();
		v_copier.copyAll(v_objects);

		// Reconstruit les liens entre les objets copiés.
		v_copier.copyReferences();

		Reference v_newReference = (Reference) v_copier.get(v_initialReference);

		// Affecte les véritables types du modèle initial à la nouvelle référence.
		v_newReference.setContainingType(p_workingReference.getContainingType());
		v_newReference.setReferencedType(p_workingReference.getReferencedType());
		return v_newReference;
	}

	/**
	 * Crée une nouvelle référence en reconstruisant ses types contenant et
	 * référencé à partir des informations portées par la {@link WorkingReference}.
	 * Cette reconstruction permet de matérialiser une référence héritée dans le
	 * contexte des types concernés, indépendamment de la référence d'origine.
	 * 
	 * Les objets {@link StructuredType} contenant et référencé sont créés comme des
	 * instances indépendantes du modèle d'origine. Seul leur nom est recopié.
	 * 
	 * Il ne faut surtout pas recopier leur propriété {@code typeDefinition} :
	 * celle-ci est une référence de contenance EMF dont l'affectation rattacherait
	 * implicitement les nouveaux objets au modèle source. Cela modifierait le
	 * contenu du {@code Root} et provoquerait une accumulation d'entités lors des
	 * exécutions successives du traitement.
	 * 
	 * Les deux types reconstruits restent ainsi autonomes et leur création ne
	 * modifie pas la structure du modèle d'origine. Le cas particulier d'une
	 * référence réflexive, pour laquelle le type contenant et le type référencé
	 * sont identiques, doit également être pris en compte lors de cette
	 * reconstruction.
	 * 
	 * Pour le bidirectionnel, La multiplicité est volontairement forcée à (0,*)
	 * pour les besoins des tests AQL. La distinction entre (0,*) et (1,*) n'a
	 * aucune importance pour ces traitements : seule l'identification de la
	 * référence opposée comme référence multiple est nécessaire.
	 * 
	 * ... et encore comme vu précédemment cette notion devrait disparaitre, seule
	 * la présence de 'oppositeOf est maintenant nécessaire.
	 * 
	 * Conservé à titre indicatif :
	 * 
	 * StructuredType v_containingType = EntityFactory.eINSTANCE.createEntity();
	 * StructuredType v_referencedType = EntityFactory.eINSTANCE.createEntity();
	 *
	 * @param p_workingReference référence de travail décrivant la référence
	 *                           modifiée
	 * @return nouvelle référence reconstruite
	 */
	private static Reference newReferenceWithOpposite(final WorkingReference p_workingReference) {

		Reference v_reference = EnvironmentFactory.eINSTANCE.createReference();

		v_reference.setName(p_workingReference.getInitialReference().getName());
		v_reference.setDescription(p_workingReference.getInitialReference().getDescription());
		v_reference.setMultiplicity(p_workingReference.getInitialReference().getMultiplicity());
		v_reference.setMetadatas(p_workingReference.getInitialReference().getMetadatas());
		v_reference.setContainingType(newContainingType(p_workingReference));
		v_reference.setReferencedType(newReferencedType(p_workingReference));

		// Juste pour avoir égalité en aql.
		if (p_workingReference.isReflexive()) {
			v_reference.setReferencedType(v_reference.getContainingType());
		}

		// Juste pour repérer une bidirectionnelle en aql.
		if (p_workingReference.isBidirectional() && !p_workingReference.isReflexive()) {
			Reference v_oppositeReference = EnvironmentFactory.eINSTANCE.createReference();
			v_oppositeReference.setMultiplicity(MultiplicityKind.ZERO_STAR_LITERAL);
			v_oppositeReference.setName(p_workingReference.getInitialReference().getOppositeOf().getName());
			v_reference.setOppositeOf(v_oppositeReference);
		}
		return v_reference;
	}

	/**
	 * Crée une nouvelle instance de {@link StructuredType} à partir du type
	 * contenant de la {@link WorkingReference}, en conservant sa classe Ecore et
	 * son nom.
	 *
	 * Une nouvelle instance est créée plutôt que de réutiliser directement le type
	 * d'origine afin de disposer d'un objet indépendant, qui pourra être utilisé
	 * dans la reconstruction des références sans modifier le modèle initial ni
	 * conserver de lien direct avec celui-ci.
	 *
	 * @param p_workingReference référence de travail contenant le type à reproduire
	 * @return nouvelle instance de {@link StructuredType}
	 */
	private static StructuredType newContainingType(final WorkingReference p_workingReference) {
		StructuredType v_containingType = (StructuredType) EcoreUtil
				.create(p_workingReference.getContainingType().eClass());
		v_containingType.setName(p_workingReference.getContainingType().getName());
		return v_containingType;
	}

	/**
	 * Crée une nouvelle instance de {@link StructuredType} à partir du type
	 * référencé de la {@link WorkingReference}, en conservant sa classe Ecore et
	 * son nom.
	 * 
	 * Une nouvelle instance est créée plutôt que de réutiliser directement le type
	 * d'origine afin de disposer d'un objet indépendant, qui pourra être utilisé
	 * dans la reconstruction des références sans modifier le modèle initial ni
	 * conserver de lien direct avec celui-ci.
	 *
	 * @param p_workingReference référence de travail contenant le type à reproduire
	 * @return nouvelle instance de {@link StructuredType}
	 */
	private static StructuredType newReferencedType(final WorkingReference p_workingReference) {
		StructuredType v_referencedType = (StructuredType) EcoreUtil
				.create(p_workingReference.getReferencedType().eClass());
		v_referencedType.setName(p_workingReference.getReferencedType().getName());
		return v_referencedType;
	}

	/**
	 * Retourne l'ensemble des références unidirectionnelles dont le type contenant
	 * correspond au type structuré fourni. Pour chaque référence de travail
	 * correspondante, la méthode récupère la référence du modèle associée.
	 * 
	 * Si les types résolus par héritage diffèrent de ceux de la référence initiale,
	 * une copie adaptée est retournée afin de préserver le modèle d'origine ;
	 * sinon, la référence initiale est directement réutilisée.
	 * 
	 * Il s'agit de l'ensemble des realtions non multiples (0,1) qui vont se
	 * traduire par une instance de B au niveau du conteneur A de la relation
	 * (A->B).
	 *
	 * @param p_structuredType type structuré pour lequel rechercher les références
	 *                         unidirectionnelles contenues
	 * @return ensemble des références unidirectionnelles correspondant au type
	 *         fourni
	 */
	public static Set<Reference> get_unidirectionnals(final StructuredType p_structuredType) {
		Set<Reference> v_references = new HashSet<>();
		for (WorkingReference workingReference : _workingReferences) {
			if (workingReference.isUnidirectionnal() && p_structuredType == workingReference.getContainingType()) {
				v_references.add(workingReference.getPacmanReference());
			}
		}
		return v_references;
	}

	/**
	 * Retourne l'ensemble des références unidirectionnelles multiples dont le type
	 * référencé correspond au type structuré fourni. Cette recherche permet
	 * notamment d'identifier les références unidirectionnelles qui pointent vers le
	 * type courant et qui doivent être prises en compte dans le traitement inverse
	 * des associations multiples.
	 * 
	 * Pour chaque référence de travail correspondante, la méthode récupère la
	 * référence du modèle associée, en créant une copie adaptée aux types résolus
	 * par héritage lorsque cela est nécessaire, afin de préserver le modèle
	 * d'origine.
	 * 
	 * Il s'agit donc des relation référencées dans le cadre des relations
	 * unidirectionnelles multiples (0,*) qui vont se traduire par des listes
	 * (List<A>) au niveau de l'entité référencée B par la relation (A->B).
	 *
	 * @param p_structuredType type structuré recherché parmi les types référencés
	 * @return ensemble des références unidirectionnelles multiples pointant vers le
	 *         type fourni
	 */
	public static Set<Reference> get_unidirectionnalsInverse(final StructuredType p_structuredType) {
		Set<Reference> v_references = new HashSet<>();
		for (WorkingReference workingReference : _workingReferences) {
			if (workingReference.isUnidirectionnal() && p_structuredType == workingReference.getReferencedType()
					&& workingReference.isMultiple()) {
				v_references.add(workingReference.getPacmanReference());
			}
		}
		return v_references;
	}

	/**
	 * Retourne l'ensemble des références de travail considérées comme
	 * bidirectionnelles. Chaque référence de travail correspondante est convertie
	 * en référence du modèle ; une copie adaptée aux types contenant et référencé
	 * résolus par héritage est créée lorsque cela est nécessaire, afin de ne pas
	 * modifier la référence initiale du modèle.
	 *
	 * @param p_root conteneur des espaces de noms du modèle, non utilisé par la
	 *               méthode
	 * @return ensemble des références considérées comme bidirectionnelles
	 */
	public static Set<Reference> get_bidirectionnals(final NamespacesContainer p_root) {
		Set<Reference> v_references = new HashSet<>();
		for (WorkingReference workingReference : _workingReferences) {
			if (workingReference.isBidirectional()) {
				v_references.add(workingReference.getPacmanReference());
			}
		}
		return v_references;
	}

	/**
	 * Retourne l'ensemble des références considérées comme bidirectionnelles dont
	 * le type contenant correspond au type structuré fourni. Pour chaque référence
	 * de travail correspondante, la méthode récupère la référence du modèle
	 * associée, en créant une copie adaptée aux types contenant et référencé
	 * résolus par héritage lorsque cela est nécessaire, afin de préserver le modèle
	 * d'origine.
	 *
	 * @param p_structuredType type structuré pour lequel rechercher les références
	 *                         bidirectionnelles contenues
	 * @return ensemble des références bidirectionnelles correspondant au type
	 *         fourni
	 */
	public static Set<Reference> get_bidirectionnals(final StructuredType p_structuredType) {
		Set<Reference> v_references = new HashSet<>();
		for (WorkingReference workingReference : _workingReferences) {
			if (workingReference.isBidirectional() && (workingReference.getContainingType() == p_structuredType)) {
				v_references.add(workingReference.getPacmanReference());
			}
		}
		return v_references;
	}

	/**
	 * Point d'entrée destiné à permettre l'appel de la journalisation des
	 * {@link WorkingReference} depuis les templates AQL. Le paramètre
	 * {@code p_object} est uniquement présent pour assurer la traçabilité et
	 * permettre l'utilisation de cette méthode dans les expressions AQL.
	 * 
	 * Par défaut, cette journalisation est effectuée une seule fois, à la fin de la
	 * constitution des {@link WorkingReference} et avant le début de la création
	 * des nouvelles références. Cette méthode permet toutefois de déclencher le
	 * même affichage à d'autres endroits du générateur si nécessaire, notamment
	 * pour faciliter si besoin le diagnostic des différentes étapes du traitement.
	 *
	 * @param p_object objet utilisé comme point d'entrée dans les templates AQL
	 */
	public static void logWorkingReferences(final EObject p_object) {
		logWorkingReferences();
	}

	/**
	 * Affiche dans la console le détail des références de travail collectées. Pour
	 * chaque référence, les informations suivantes sont présentées : le nom de la
	 * référence initiale, les types contenant et référencé, son caractère
	 * unidirectionnel, récursif ou bidirectionnel, ainsi que la présence d'une
	 * référence opposée dans le modèle d'origine.
	 * <p>
	 * Cette méthode est destinée au diagnostic et à la vérification du résultat du
	 * traitement des références héritées. Elle n'effectue aucune modification sur
	 * les objets manipulés.
	 */
	private static void logWorkingReferences() {

		for (WorkingReference v_ref : _workingReferences) {
			System.out.println(String.format(
					"Reference : [%-20s] Containing : [%-15s] Referenced : [%-15s] ForeignKey : [%-5s] "
							+ " Reflexive : [%-5s] Bidirectionnal : [%-5s] Opposite : [%-5s] NewReference : [%-5s]",
					v_ref.getInitialReference().getName(), v_ref.getContainingType().getName(),
					v_ref.getReferencedType().getName(), v_ref.isUnidirectionnal(), v_ref.isReflexive(),
					v_ref.isBidirectional(), v_ref.getInitialReference().getOppositeOf() != null,
					v_ref.requiresNewReference()));
		}
	}

	/**
	 * Représente une référence de travail construite à partir d'une référence
	 * initiale du modèle et des types concrets contenant et référencé retenus après
	 * résolution de l'éventuel héritage.
	 * 
	 * Cette classe conserve les informations nécessaires aux traitements ultérieurs
	 * sans modifier directement le modèle d'origine : la référence initiale, les
	 * deux types associés et les caractéristiques de l'association
	 * (unidirectionnelle, bidirectionnelle, réflexive et multiple).
	 * 
	 * Les indicateurs unidirectionnel et bidirectionnel sont complémentaires :
	 * {@code setUnidirectionnal()} renseigne systématiquement les deux avec des
	 * valeurs inverses.
	 * 
	 * Conserve surtout (c'est le but final) la référence Pacman résolue,
	 * éventuellement reconstruite pour tenir compte des adaptations liées à
	 * l'héritage ou la gestion de (0,*).
	 * 
	 * La méthode {@code requiresNewReference()} indique si les types résolus
	 * diffèrent de ceux de la référence initiale et nécessitent donc la création
	 * d'une copie adaptée.
	 * 
	 * Enfin, {@code equals()} et {@code hashCode()} identifient une référence de
	 * travail à partir du triplet constitué de la référence initiale, du type
	 * contenant et du type référencé. Cette règle permet notamment d'éviter
	 * l'enregistrement de plusieurs références de travail strictement équivalentes
	 * dans un ensemble.
	 */
	private static class WorkingReference {

		private boolean _unidirectionnal;
		private boolean _bidirectional;
		private boolean _reflexive;
		private boolean _multiple;

		private Reference _initialReference;
		private Reference _pacmanReference;
		private StructuredType _containing;
		private StructuredType _referenced;

		/**
		 * Initialise une référence de travail avec ses indicateurs à {@code false}.
		 */
		WorkingReference() {
			_unidirectionnal = false;
			_bidirectional = false;
			_reflexive = false;
			_multiple = false;
		}

		public Boolean isMultiple() {
			return _multiple;
		}

		public void setMultiple(boolean p_multiple) {
			this._multiple = p_multiple;
		}

		public boolean isUnidirectionnal() {
			return _unidirectionnal;
		}

		public void setUnidirectionnal(boolean p_unidirectionnal) {
			this._unidirectionnal = p_unidirectionnal;
			this._bidirectional = !p_unidirectionnal;
		}

		public Boolean isReflexive() {
			return _reflexive;
		}

		public void setReflexive(boolean p_reflexive) {
			this._reflexive = p_reflexive;
		}

		public Boolean isBidirectional() {
			return _bidirectional;
		}

		public Reference getInitialReference() {
			return _initialReference;
		}

		public void setInitialReference(Reference _initialReference) {
			this._initialReference = _initialReference;
		}

		public StructuredType getContainingType() {
			return _containing;
		}

		public void setContainingType(StructuredType p_containing) {
			this._containing = p_containing;
		}

		public StructuredType getReferencedType() {
			return _referenced;
		}

		public void setReferencedType(StructuredType p_referenced) {
			this._referenced = p_referenced;
		}

		public Reference getPacmanReference() {
			return _pacmanReference;
		}

		public void setPacmanReference(Reference _pacmanReference) {
			this._pacmanReference = _pacmanReference;
		}

		public boolean requiresNewReference() {
			return _containing != _initialReference.getContainingType()
					|| _referenced != _initialReference.getReferencedType();
		}

		@Override
		public int hashCode() {
			return Objects.hash(_initialReference, _containing, _referenced);
		}

		@Override
		public boolean equals(Object p_object) {
			if (this == p_object)
				return true;
			if (p_object == null || getClass() != p_object.getClass())
				return false;

			WorkingReference v_other = (WorkingReference) p_object;
			return Objects.equals(_initialReference, v_other._initialReference)
					&& Objects.equals(_containing, v_other._containing)
					&& Objects.equals(_referenced, v_other._referenced);
		}
	}
}
