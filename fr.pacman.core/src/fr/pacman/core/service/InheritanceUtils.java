package fr.pacman.core.service;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.obeonetwork.dsl.entity.Entity;
import org.obeonetwork.dsl.environment.MultiplicityKind;
import org.obeonetwork.dsl.environment.Reference;

/**
 * Utilitaire lié à la gestion de l'héritage pour les entités du métamodèle.
 * 
 * Cette classe propose des méthodes statiques permettant de manipuler ou
 * dupliquer des objets liés à l'héritage, notamment les références entre
 * entités.
 */
public class InheritanceUtils {

	/**
	 * Crée une copie d'une {@link Reference} en la réassignant à une entité cible.
	 * 
	 * Cette méthode effectue une copie profonde de la référence passée en paramètre
	 * (à l'aide de {@link EcoreUtil#copy(EObject)}), puis :
	 * <ul>
	 * <li>Met à jour son nom en le suffixant avec le nom de l'entité cible.</li>
	 * <li>Réassigne la nouvelle référence à l'entité cible via
	 * {@code setContainingType}.</li>
	 * <li>Si la référence est récursive (autour de la même entité), elle met aussi
	 * à jour {@code referencedType}.</li>
	 * </ul>
	 *
	 * @param p_reference    La référence d'origine à copier.
	 * @param p_targetEntity L'entité dans laquelle sera assignée la nouvelle
	 *                       référence.
	 * @return Une nouvelle instance de {@link Reference}, copiée et réassignée à
	 *         {@code p_targetEntity}.
	 */
	public static Reference get_workingCopyReference(final Reference p_reference, Entity p_targetEntity) {

		Reference p_newReference = EcoreUtil.copy(p_reference);
		p_newReference.setName(p_newReference.getName() + "_" + p_targetEntity.getName());
		Entity p_newTargetEntity = EcoreUtil.copy(p_targetEntity);
		// On remplace le container par l'entité héritée.
		p_newReference.setContainingType(p_newTargetEntity);
		// Si récursif on copie la target dans les deux cas.
		if (p_reference.getContainingType() == p_reference.getReferencedType())
			p_newReference.setReferencedType(p_newTargetEntity);
		return p_newReference;
	}

	/**
	 * Définit la multiplicité de la référence spécifiée à {@code 0..*} (optionnelle
	 * et multi-valuée).
	 * 
	 * Cela signifie que la référence peut contenir zéro ou plusieurs éléments.
	 * Aucun élément n'est requis, et il n'y a pas de limite supérieure.
	 *
	 * @param p_reference la référence métier à modifier (ne doit pas être
	 *                    {@code null})
	 * @return la référence après modification de sa multiplicité
	 *
	 * @throws NullPointerException si {@code p_reference} est {@code null}
	 */
	public static Reference set_multiplicityZeroStar(final Reference p_reference) {
		p_reference.setMultiplicity(MultiplicityKind.ZERO_STAR_LITERAL);
		return p_reference;
	}
}
