package fr.pacman.back.core.generator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.acceleo.Module;
import org.eclipse.acceleo.OpenModeKind;
import org.eclipse.acceleo.Template;
import org.eclipse.acceleo.aql.AcceleoUtil;
import org.eclipse.acceleo.aql.evaluation.AcceleoEvaluator;
import org.eclipse.acceleo.aql.evaluation.strategy.DefaultGenerationStrategy;
import org.eclipse.acceleo.aql.evaluation.strategy.DefaultWriterFactory;
import org.eclipse.acceleo.aql.evaluation.strategy.IAcceleoGenerationStrategy;
import org.eclipse.acceleo.aql.evaluation.writer.IAcceleoWriter;
import org.eclipse.acceleo.aql.parser.AcceleoParser;
import org.eclipse.acceleo.aql.parser.ModuleLoader;
import org.eclipse.acceleo.query.AQLUtils;
import org.eclipse.acceleo.query.ast.EClassifierTypeLiteral;
import org.eclipse.acceleo.query.ast.TypeLiteral;
import org.eclipse.acceleo.query.ide.runtime.impl.namespace.OSGiQualifiedNameResolver;
import org.eclipse.acceleo.query.runtime.impl.namespace.ClassLoaderQualifiedNameResolver;
import org.eclipse.acceleo.query.runtime.impl.namespace.JavaLoader;
import org.eclipse.acceleo.query.runtime.namespace.IQualifiedNameQueryEnvironment;
import org.eclipse.acceleo.query.runtime.namespace.IQualifiedNameResolver;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.obeonetwork.dsl.entity.Root;
import org.obeonetwork.dsl.environment.Namespace;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * Couche abstraite pour les générateurs internes de Pacman. C'est cette couche,
 * initialement appelée par la couche UI {@link PacmanUIGenerator}, qui effectue
 * le lien entre l'écosystème Java et le langage Acceleo (les fichiers '.mtl').
 * 
 * Chaque générateur doit indiquer le nom du sous-projet à partir duquel les
 * différents chemins de génération vont être appliqués. Par définition, un
 * générateur ne peut donc générer que dans un et un seul sous projet. Pour
 * générer dans plusieurs sous-projets il faut donc enregistrer plusieurs
 * générateurs au niveau de la couche UI.
 * 
 * Tous les générateurs internes pour les projets de génération doivent
 * obligatoirement étendre de cette classe abstraite;
 * 
 * @author MINARM
 */
public abstract class PacmanGenerator {

	/**
	 * Nom du fichier de log par défaut pour l'écriture des erreurs et des
	 * avertissements Acceleo lors de la génération.
	 */
	protected final static String c_defaultLogFileName = "acceleo.log";

	/**
	 * Le système de retour à la ligne dfinit par défaut.
	 */
	protected final static String c_defaultNewLine = System.lineSeparator();

	/**
	 * Le chemin racine pour le projet, il est déduit de la ressource qui a été
	 * préalablement sélectionnée par l'utilisateur pour lancer le générateur UI. Ce
	 * chemin sert de base pour le calcul de l'ensemble des chemins cibles de
	 * génération.
	 * 
	 * Tout le méchanisme d'écriture des différents fichiers est donc basé sur le
	 * chemin suivant : _rootPath (invariant pour l'ensemble des générateurs du
	 * projet) + getSubProjectName() (en fonction du générateur) + le chemin des
	 * fichiers à générer.
	 */
	private String _rootPath;

	/**
	 * La liste des ressources sélectionnées par l'utilisateur pour lancer la
	 * génération. Ces resources représentent ici uniquement des ressources de type
	 * fichier. Si les ressources sont des EObject, alors la liste est vide.
	 */
	protected List<String> _resources;

	/**
	 * La liste des ressources sélectionnées par l'utilisateur pour lancer la
	 * génération. Ces ressources sont ici uniquement des {@link EObject}. Si la
	 * ressource est un fichier, alors cette liste est vide.
	 */
	protected List<EObject> _values;

	/**
	 * Création d'un {@link ResourceSet} par défaut (vide).
	 * 
	 * @return le {@link ResourceSet} qui vient d'être créé par défaut.
	 * @generated
	 */
	protected ResourceSet createDefaultResourceSet() {
		return new ResourceSetImpl();
	}

	/**
	 * Création du {@link ResourceSet} pour les modèles.
	 * 
	 * @param p_generationKey la clé unique de génération
	 * @param p_options       la {@link Map} des options de génération.
	 * @param p_exceptions    la {@link List} des exceptions.
	 * @param p_resourceSet   le {@link ResourceSet} par défaut.
	 * @return le {@link ResourceSet} créé pour les modèles
	 */
	protected ResourceSet createResourceSetForModel(Object p_generationKey, Map<String, String> p_options,
			List<Exception> p_exceptions, ResourceSet p_resourceSet) {
		return AQLUtils.createResourceSetForModels(p_exceptions, p_generationKey, p_resourceSet, p_options);
	}

	/**
	 * Affecte la liste des ressources de type fichier pour la génération de code. A
	 * ce niveau, il s'agit d'une liste (pour évolutions futures) mais au niveau de
	 * la couche UI, la classe {@lin PacmanUIGenerator} n'accepte qu'une seule
	 * ressource.
	 * 
	 * @param p_resources la liste des ressources de type fichier pour la demande de
	 *                    génération.
	 */
	public void setResources(List<String> p_resources) {
		_resources = p_resources;
	}

	/**
	 * Affecte le chemin racine pour la génération de code. A partir de cette
	 * racine, vont être calculés l'ensemble des chemins de génération au travers
	 * des différents sous-projets de l'application, en fonction des options de
	 * génération qui ont été configurées par le développeur de l'application.
	 * 
	 * @param p_rootPath le chemin racine pour la génération de code.
	 */
	public void setRootPath(String p_rootPath) {
		_rootPath = p_rootPath;
	}

	/**
	 * Affecte la liste des ressources de type {@link EObject }
	 * 
	 * @param p_values la liste des ressources de type {@link EObject} (donc toutes
	 *                 les ressources qui ne sont pas des fichiers).
	 */
	public void setValues(List<EObject> p_values) {
		_values = p_values;
	}

	/**
	 * Retourne la liste des templates qui sont à executer au niveau de chaque
	 * générateur, cette liste est dépendante de la sélection initiale du
	 * développeur de l'application. Au nieau du générateur, un template à exécuter
	 * doit obligatoirement avoir l'annotation {@link @Main}
	 * 
	 * @return la liste des templates (points d'entrée dans le fichier '.mtl') à
	 *         exécuter.
	 */
	protected abstract Map<String, SelectionType_Enum> getMainTemplates();

	/**
	 * Retourne le nom du sous-projet impacté par la génération de code. A ce niveau
	 * il ne peut y avoir qu'un seul sous-projet impacté par un générateur. Si
	 * plusieurs sous-projets doivent être impactés, il est nécessaire d'enregistrer
	 * plusieurs générateurs de code au niveau de la couche UI.
	 * 
	 * @return le nom du sous-projet impacté par le générateur de code.
	 */
	public abstract String getSubProjectName();

	/**
	 * Retourne le nom du module à attaquer pour le générateur de code. Le
	 * générateur récupère le module (le fichier '.mtl'), et à l'intérieur de ce
	 * module va attaquer la liste des templates préalablement récupérés.
	 * 
	 * @return le nom du module à attaquer par le générateur de code.
	 */
	public abstract String getModuleQualifiedName();

	/**
	 * Retourne une liste d'options pour la génération si le développeur du
	 * générateur désire ajouter des options supplémentaires aux options déjà
	 * existantes ou plus simplement modifier les options par défaut.
	 * 
	 * @return une liste d'options pour la génération.
	 */
	protected abstract Map<String, String> getOptions();

	/**
	 * Flag d'activation pour la demande de délégation de certaine opérations de
	 * post-traitement. Cette option est aussi présente au niveau de la couche UI.
	 * Ainsi on peut agir de manière plus ou moins fine au niveau de la couche
	 * globale UI qui lance plusieurs générateurs ou au niveau indivuduel de chaque
	 * générateur.
	 * 
	 * Cela est par example partique pour le générateur de validation qui peut être
	 * embarqué au niveau des couches entité, soa, etc.. et qui lui spécifiquement,
	 * n'a pas besoin d'opérations de post traitement.
	 */
	public abstract boolean doPostTreatments();

	/**
	 * Retourne la liste des {@link EObject} qui vont être manipulés par le
	 * générateur. Par défaut cette méthode récupère l'ensemble des objets contenus
	 * dans le fichier de modélisation.
	 * 
	 * Si la ressource sélectionnée par le développeur de l'application n'est pas
	 * une resource de type fichier mais directment un {@link EObject} alors ce
	 * dernier est directement positionné dans la liste.
	 *
	 * Cette méthode est toutefois laissée au niveau des classes filles afin que le
	 * développeur puisse modifier l'algorithme de récupération des valeurs si
	 * besoin.
	 * 
	 * @param p_queryEnvironment
	 * @param p_valuesCache
	 * @param p_type
	 * @param p_resourceSetForModels
	 * @param p_monitor
	 * @return la liste des valeurs {} récupérée à partir de la ressource
	 *         initialement sélectionnée par le développeur de l'application cible.
	 */
	protected abstract List<EObject> getValues(IQualifiedNameQueryEnvironment p_queryEnvironment,
			final Map<EClass, List<EObject>> p_valuesCache, TypeLiteral p_type, ResourceSet p_resourceSetForModels,
			Monitor p_monitor);

	/**
	 * Retourne la liste des différents templates à exécuter au sein du module
	 * (fichier '.mtl'). Pour l'instant, aucune action n'est prévue si la liste est
	 * vide (voir selon les besoins futurs).
	 * 
	 * Pour l'instant, au niveau obéo il ,n'a pas été prévu dans AQL8 de scanner les
	 * templates @{link @Main} au niveau des modules parents. Pour remédier à cette
	 * problématique et pouvoir aussi mettre des templates @{link @Main} au niveau
	 * des modules parents, cette méthode est récursive et c'est elle qui se charge
	 * aussi du scan pour le module parent si ce dernier existe. (On pourrait aussi
	 * surcharger le code de la classe {@link AcceleoUtil}).
	 * 
	 * ATTENTION (15/01/2025) : Pour l'instant ne pas encore positionner les
	 * annotations au niveau du module parent, cela engendre des effets de bords et
	 * les corps des fichiers ne sont plus générés (vide) !
	 * 
	 * @param p_module             le module acceleo.
	 * @param p_templatesToExecute la liste des templates à exécuter (au départ
	 *                             cette liste des vide).
	 * @param p_resolver           la classe de résolution des modules (dans le cas
	 *                             d'un héritage)!, afin de pouvoir résoudre lee
	 *                             module parent)
	 * @return la liste des templates à exécuter au sein du module. Le contenu de
	 *         cette liste peut dépendre de la ressource initialement sélectionnée
	 *         par le développeur de l'application cible.
	 */
	protected List<Template> getTemplatesToExecute(Module p_module, final List<Template> p_templatesToExecute,
			final IQualifiedNameResolver p_resolver) {
		SelectionType_Enum selectionType = getSelectiontTypeForTemplates();
		for (Template template : AcceleoUtil.getMainTemplates(p_module)) {
			for (Entry<String, SelectionType_Enum> entry : getMainTemplates().entrySet()) {
				if (entry.getKey().equals(template.getName()))
					if (entry.getValue() == selectionType)
						p_templatesToExecute.add(template);
			}
		}
		if (p_module.getExtends() != null) {
			String parentModuleQualifiedName = p_module.getExtends().getQualifiedName();
			Module moduleParent = (Module) p_resolver.resolve(parentModuleQualifiedName);
			getTemplatesToExecute((Module) moduleParent, p_templatesToExecute, p_resolver);
		}
		return p_templatesToExecute;
	}

	/**
	 * Retourne le type pour la sélection du (ou des) {@link Template} a executer.
	 * Il s'agit ici de véritablement pouvoir distinguer (de manière plus précise)
	 * le type de la resource (ou de l'{@link EObject}) afin de pouvoir retourner le
	 * ou les {@link Template} correspondants.
	 * 
	 * La valeur par défaut (si impossible à déterminer) est {@link Root}. Cette
	 * valeur est utilisée notamment par le plugin de création d'un nouveau projet
	 * ou par définition, aucune ressource n'a été préalablement sélectionnée par le
	 * développeur.
	 * 
	 * Compléter cette méthode ainsi que l'énumération en fonction du besoin.
	 * 
	 * @return le type de la sélection initiale.
	 */
	private SelectionType_Enum getSelectiontTypeForTemplates() {
		SelectionType_Enum selectionType;
		if (null != _resources && !_resources.isEmpty()) {
			selectionType = SelectionType_Enum.FILE;
		} else if (null != _values && !_values.isEmpty()) {
			EObject obj = _values.get(0);
			if (obj instanceof Namespace) {
				selectionType = SelectionType_Enum.NAMESPACE;
			} else {
				selectionType = SelectionType_Enum.EOBJECT;
			}
		} else {
			selectionType = SelectionType_Enum.ROOT;
		}
		return selectionType;
	}

	/**
	 * La méthode principale de la classe.
	 * 
	 * Pour chaque ressource, on récupère l'ensemble des {@link Template} et à
	 * partir de chaque {@link Template} on génère le code à l'aide des différents
	 * fichiers aql8.
	 * 
	 * Chaque générateur de la couche UI lance donc un ou plusieurs générateurs
	 * internes, chaque générateur interne récupère la liste de ses {@link Template}
	 * à exécuter, pour chaque {@link Template}, la liste des {@link EObject} est
	 * retournée au générateur Acceleo ce dernier se charge alors de la génération
	 * et de l'écriture des différents fichiers.
	 * 
	 * @param p_monitor l'objet de monitoring pour la tâche à effectuer
	 */
	public void generate(final Monitor p_monitor) {
		final Map<String, String> options = getOptions();
		final Object generationKey = new Object();
		final ResourceSet resourceSetForModels = createResourceSetForModel(generationKey, options, new ArrayList<>(),
				createDefaultResourceSet());
		loadResources(resourceSetForModels, _resources);
		final IQualifiedNameResolver resolver = createResolver();
		final IQualifiedNameQueryEnvironment queryEnvironment = createAcceleoQueryEnvironment(options, resolver,
				resourceSetForModels);
		AcceleoEvaluator evaluator = createAcceleoEvaluator(resolver, queryEnvironment);
		final IAcceleoGenerationStrategy strategy = createGenerationStrategy(resourceSetForModels);

		final Module module = (Module) resolver.resolve(getModuleQualifiedName());
		final URI targetURI = getTargetUri();
		final URI logURI = AcceleoUtil.getlogURI(targetURI, options.get(AcceleoUtil.LOG_URI_OPTION));

		try {
			final Map<EClass, List<EObject>> valuesCache = new LinkedHashMap<>();
			for (Template template : getTemplatesToExecute(module, new ArrayList<>(), resolver)) {
				final EClassifierTypeLiteral eClassifierTypeLiteral = (EClassifierTypeLiteral) template.getParameters()
						.get(0).getType().getAst();
				final List<EObject> values = getValuesFromInitialSelection(queryEnvironment, valuesCache,
						eClassifierTypeLiteral, resourceSetForModels, p_monitor);
				final String parameterName = template.getParameters().get(0).getName();
				Map<String, Object> variables = new LinkedHashMap<>();
				for (EObject value : values) {
					variables.put(parameterName, value);

					System.out.println("Génération dans : " + targetURI.toString());

					AcceleoUtil.generate(template, variables, evaluator, queryEnvironment, strategy, targetURI, logURI,
							p_monitor);
				}
			}
		} finally {
			// PacmanGeneratorsReport.addGenerationResult(evaluator.getGenerationResult(),
			// getSubProjectName());
			AQLUtils.cleanResourceSetForModels(generationKey, resourceSetForModels);
			AcceleoUtil.cleanServices(queryEnvironment, resourceSetForModels);
		}
	}

	/**
	 * Construction du chemin racine cible de génération. Si aucun nom de sous
	 * projet n'a été préalablement renseigné au niveau du générateur, alors il
	 * s'agit d'un générateur qui doit agir au niveau de la globalité du projet.
	 * 
	 * @return le chemin racine cible de génération pour le générateur.
	 */
	private URI getTargetUri() {
		StringBuffer strUri = new StringBuffer(_rootPath + File.separator);
		if (null != getSubProjectName() && !getSubProjectName().isEmpty())
			strUri.append(getSubProjectName() + File.separator);
		return URI.createFileURI(strUri.toString());
	}

	/**
	 * Retourne l'ensemble des {@link EObject} concernés par la sélection initiale
	 * du développeur.
	 * 
	 * @param p_queryEnvironment
	 * @param p_valuesCache
	 * @param p_type
	 * @param p_resourceSetForModels
	 * @param p_moniror
	 * @return
	 */
	private List<EObject> getValuesFromInitialSelection(IQualifiedNameQueryEnvironment p_queryEnvironment,
			final Map<EClass, List<EObject>> p_valuesCache, TypeLiteral p_type, ResourceSet p_resourceSetForModels,
			Monitor p_monitor) {
		if (null != _values && !_values.isEmpty()) {
			return _values;
		} else {
			return getValues(p_queryEnvironment, p_valuesCache, p_type, p_resourceSetForModels, p_monitor);
		}
	}

	/**
	 * Creates the {@link IQualifiedNameResolver}.
	 * 
	 * @return the created {@link IQualifiedNameResolver}
	 * @generated
	 */
	protected IQualifiedNameResolver createResolver() {
		if (EMFPlugin.IS_OSGI_RUNNING) {
			final Bundle bundle = FrameworkUtil.getBundle(this.getClass());
			return new OSGiQualifiedNameResolver(bundle, AcceleoParser.QUALIFIER_SEPARATOR);
		} else {
			return new ClassLoaderQualifiedNameResolver(this.getClass().getClassLoader(),
					AcceleoParser.QUALIFIER_SEPARATOR);
		}
	}

	/**
	 * 
	 * @param resourceSetForModels
	 * @return
	 */
	protected IAcceleoGenerationStrategy createGenerationStrategy(ResourceSet resourceSetForModels) {
		final IAcceleoGenerationStrategy strategy = new DefaultGenerationStrategy(
				resourceSetForModels.getURIConverter(), new DefaultWriterFactory()) {
			@Override
			public IAcceleoWriter createWriterFor(URI uri, OpenModeKind openMode, Charset charset, String lineDelimiter)
					throws IOException {
				// System.out.println(uri.toString());
				return super.createWriterFor(uri, openMode, charset, lineDelimiter);
			}
		};
		return strategy;
	}

	/**
	 * Registers the given {@link EPackage} in the given
	 * {@link IQualifiedNameQueryEnvironment} recursively.
	 * 
	 * @param environment the {@link IQualifiedNameQueryEnvironment}
	 * @param ePackage    the {@link EPackage}
	 * @generated
	 */
	public static void registerEPackage(IQualifiedNameQueryEnvironment environment, EPackage ePackage) {
		environment.registerEPackage(ePackage);
		for (EPackage child : ePackage.getESubpackages()) {
			registerEPackage(environment, child);
		}
	}

	/**
	 * 
	 * @param resourceSetForModels
	 * @param resources
	 */
	protected void loadResources(ResourceSet resourceSetForModels, List<String> resources) {
		for (String resource : resources) {
			resourceSetForModels.getResource(URI.createFileURI(resource), true);
		}
	}

	/**
	 * Creates the {@link IQualifiedNameQueryEnvironment}.
	 * 
	 * @param options              the {@link Map} of options
	 * @param resolver             the {@link IQualifiedNameResolver}
	 * @param resourceSetForModels the {@link ResourceSet} for models
	 * @return the created {@link IQualifiedNameQueryEnvironment}
	 * @generated
	 */
	protected IQualifiedNameQueryEnvironment createAcceleoQueryEnvironment(Map<String, String> options,
			IQualifiedNameResolver resolver, ResourceSet resourceSetForModels) {
		final IQualifiedNameQueryEnvironment queryEnvironment = AcceleoUtil.newAcceleoQueryEnvironment(options,
				resolver, resourceSetForModels, false);
		for (String nsURI : new ArrayList<String>(EPackage.Registry.INSTANCE.keySet())) {
			registerEPackage(queryEnvironment, EPackage.Registry.INSTANCE.getEPackage(nsURI));
		}
		return queryEnvironment;
	}

	/**
	 * Creates the {@link AcceleoEvaluator}
	 * 
	 * @param resolver         the {@link IQualifiedNameResolver}
	 * @param queryEnvironment the {@link IQualifiedNameQueryEnvironment}
	 * @return le {@link AcceleoEvaluator} qui vient d'être créé
	 */
	protected AcceleoEvaluator createAcceleoEvaluator(IQualifiedNameResolver resolver,
			IQualifiedNameQueryEnvironment queryEnvironment) {
		AcceleoEvaluator evaluator = new AcceleoEvaluator(queryEnvironment.getLookupEngine(), System.lineSeparator());
		resolver.addLoader(new ModuleLoader(new AcceleoParser(), evaluator));
		resolver.addLoader(new JavaLoader(AcceleoParser.QUALIFIER_SEPARATOR, false));
		return evaluator;
	}

	/**
	 * Enumération interne pour connaitre le type de ressource qui a été
	 * préalabalement sélectionnée par l'utilisateur au niveau de la couche UI.
	 * 
	 * Ici il ne faut pas s'attacher ici à une vision stricte et puriste de la
	 * sémantique. Par exemple, {@link Namespace} et {@link Root} sont tous les deux
	 * des {@link EObject} mais, dans le cadre de la génération, il est toutefois
	 * nécessaire de pouvoir les différencier.
	 * 
	 * Au fur et à mesure du besoin, compléter cette énumération et éventuellement
	 * sortir les éléments de la notion plus abstraite de {@link EObject}.
	 */
	public enum SelectionType_Enum {
		FILE, NAMESPACE, ROOT, COMPONENT, EOBJECT, SERVICE
	}
}
