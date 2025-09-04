package fr.pacman.back.soaclient.ui.testers;

import fr.pacman.back.core.property.project.ProjectProperties;
import fr.pacman.back.core.ui.generator.PacmanUIPropertyTester;

/**
 * Implémentation concrète d'un {@link PacmanUIPropertyTester} utilisée
 * pour déterminer si le contexte courant correspond à un projet de type "client".
 *
 * <p>
 * Ce tester est référencé dans le plugin via la propriété
 * {@code fr.pacman.back.soaclient.canShowMenu} et permet de contrôler
 * l'affichage conditionnel d'éléments d'UI (menus, commandes, etc.)
 * en fonction du type de projet.
 * </p>
 *
 * <p>
 * La logique métier se limite ici à vérifier que la propriété
 * {@link ProjectProperties#get_type(Object)} retourne la chaîne
 * {@code "client"}. Dans ce cas, la condition est validée
 * et le menu/commande associé(e) devient disponible.
 * </p>
 *
 * <h3>Exemple d'usage dans le plugin.xml</h3>
 * <pre>{@code
 * <test
 *    property="fr.pacman.back.soaclient.canShowMenu"
 *    forcePluginActivation="true"/>
 * }</pre>
 *
 * <p>
 * Cet exemple montre comment utiliser ce tester pour afficher un menu
 * uniquement si le projet courant est identifié comme un projet client.
 * </p>
 *
 * @see PacmanUIPropertyTester
 * @see ProjectProperties
 */
public class PacmanUIClientPropertyTester extends PacmanUIPropertyTester {

    /**
     * Vérifie si le projet courant est de type "client".
     *
     * @param receiver      l'objet cible du test (ignoré dans cette implémentation)
     * @param property      le nom de la propriété à tester (ignoré ici)
     * @param args          éventuels arguments supplémentaires (non utilisés)
     * @param expectedValue valeur attendue (non utilisée)
     * @return {@code true} si le type du projet est "client", {@code false} sinon
     */
	@Override
	public boolean testProperty(Object receiver, String property, Object[] args, Object expectedValue) {
		if (ProjectProperties.get_type(null).equals("client"))
			return true;
		return false;
	}
}
