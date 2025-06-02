package fr.pacman.core.service;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.obeonetwork.dsl.environment.MultiplicityKind;
import org.obeonetwork.dsl.environment.Reference;
import org.obeonetwork.dsl.environment.StructuredType;

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
	 * @param p_reference La référence d'origine à copier.
	 * @param p_target    La structure dans laquelle sera assignée la nouvelle
	 *                    référence.
	 * @return Une nouvelle instance de {@link Reference}, copiée et réassignée à
	 *         {@code p_target}.
	 */
	private static Reference getWorkingCopyReference(final Reference p_reference, StructuredType p_target) {
		StructuredType p_newTarget = EcoreUtil.copy(p_target);
		// On remplace le container par type hérité.
		p_reference.setContainingType(p_newTarget);
		// Si récursif on copie la target dans les deux cas.
		if (p_reference.getContainingType() == p_reference.getReferencedType())
			p_reference.setReferencedType(p_newTarget);
		return p_reference;
	}

	public static Reference downgradeReference(final Reference p_reference, StructuredType p_target) {
		return getWorkingCopyReference(EcoreUtil.copy(p_reference), p_target);
	}

	/**
	 * Définit la multiplicité de la référence spécifiée à {@code 0..*} (optionnelle
	 * et multi-valuée).
	 * 
	 * Cette méthode est appelée dans le cas des références FK inverses, comme avec
	 * l'héritage on redescent la relation au niveau de chaque table qui hérite, il
	 * n'y a plus une seule clé mais n clés en fonction du nombre d'entités. Une
	 * seule clé ne peut être renseignée, il est donc nécessaire de forcer toutes
	 * les clés à null même si au niveau de la modélisation la cardinalité initiale
	 * est à (1,*).
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
