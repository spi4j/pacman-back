package fr.pacman.core.ui.generator;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EObject;
import org.obeonetwork.dsl.entity.Root;
import org.obeonetwork.dsl.environment.Namespace;
import org.obeonetwork.dsl.environment.NamespacesContainer;
import org.obeonetwork.dsl.soa.System;

/**
 * Classe de vérification des ressources sélectionnées (fichiers, eobject,
 * etc..) pour affichage des différents menus de génération en fonction des
 * ressources sélectionnées par le développeur afin de lancer un générateur.
 * Permet d'effectuer des contrôles plus poussés qui seraient impossibles à
 * réaliser directement dans le fichier plugin.xml avec les commandes
 * disponibles par défaut.
 * 
 * @author MINARM
 */
public class PacmanUIPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object p_receiver, String p_property, Object[] p_args, Object p_expectedValue) {
		if ("testNamespaceSoa".equals(p_property) || "testNamespaceEntity".equals(p_property)) {
			if (!(p_receiver instanceof Namespace) || p_receiver instanceof org.obeonetwork.dsl.soa.System)
				return false;
			return checkNameSpace(p_property, (EObject) p_receiver);
		}
		return false;
	}

	/**
	 * Vérifie si l'OEbject est une racine pour le {@link Namespace}. Un namespace
	 * étant soit un namespace pour la couche 'entities', soit pour la couche 'SOA',
	 * la racine est donc soit un {@link Root}, soit un {@link System}.
	 * 
	 * @param p_receiver le {@link EObject} à vérifier.
	 * @return 'true' si le propriétaire du {@link EObject} est bien de l'instance
	 *         que l'on désire vérifier, sinon la valeur 'false'.
	 */
	private boolean hasOwner(final EObject p_receiver) {
		if (p_receiver instanceof Root)
			return false;
		if (p_receiver instanceof org.obeonetwork.dsl.soa.System)
			return false;
		return true;
	}

	/**
	 * Pour tester le type du {@link Namespace} qui a été sélectionné par le
	 * développeur, on remonte jusqu'à la racine (si le namespace est lui même dans
	 * un autre namespace, qui est lui même...etc..). On obtient alors le
	 * {@link Root} (cas d'un namespace pour les entités) ou un {@link System} dans
	 * le cas d'un namespace pour la couche SOA.
	 * <p>
	 * C'est donc une manière simple de distinguer un namespace pour la couche
	 * 'entities' d'un namespace pour la couche 'SOA';
	 * 
	 * @param p_type     le type de {@link Namespace} que la sélection doit avoir
	 *                   pour afficher le menu adéquat.
	 * @param p_receiver la sélection initiale du développeur (namespace / EObject).
	 * @return la valeur 'true' si le menu peut être affiché en fonction de la
	 *         sélection du développeur, sinon, la valeur 'false'.
	 */
	private boolean checkNameSpace(final String p_type, final EObject p_receiver) {
		if (null == p_receiver)
			return false;

		NamespacesContainer owner = ((Namespace) p_receiver).getOwner();
		while (hasOwner(owner)) {
			checkNameSpace(p_type, owner);
		}
		if ("testNamespaceSoa".equals(p_type) && owner instanceof org.obeonetwork.dsl.soa.System)
			return true;
		if ("testNamespaceEntity".equals(p_type) && owner instanceof Root)
			return true;

		return false;
	}

}
