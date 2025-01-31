package fr.pacman.core.service;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.obeonetwork.dsl.entity.Entity;

/**
 * Classe utilitaire pour trouver le nom de la librairie à laquelle appartient
 * éventuellement une entité. Dans une application , il est possible que le
 * modèle ait des relations avec des modèles externes issus de librairies
 * préalablement développées.
 * <p>
 * Afin de pouvoir récupérer le bon nom pour la couche de persistance (cette
 * entité n'est pas gérée par la couche de persistance par défaut de
 * l'application mais par la couche de persistance de la librairie), il est
 * nécessaire de récupérer le nom de l'application (la librairie) afin de
 * pouvoir recomposer le bon nom du gestionnaire de persistance.
 * 
 * @author MINARM
 */
public class ResourceUtils {

	/**
	 * Recuperation du nom de la librairie (en fait le nom du fichier ".entity" ou
	 * est stockee l'entité. Comme le nom du fichier est obligatoirement celui de la
	 * librairie...)
	 * <p>
	 * On recherche l'entité dans tous les fichiers de librairie externe. Si elle
	 * est trouvée, on retourne le nom du fichier qui la contient. ATTENTION, pour
	 * cette premiere version on ne détecte pas les doublons, il ne faut donc pas
	 * qu'une entité ait le meme nom dans deux projets differents !
	 * 
	 * @param p_entity      : L'entité pour laquelle essayer de récuperer le nom du
	 *                      projet (librairie).
	 * @param p_projectName : Le nom du projet principal (celui en cours).
	 * @return
	 */
	private static String getLibraryName(final Entity p_entity, final String p_projectName) {
		for (Resource v_resource : p_entity.eResource().getResourceSet().getResources()) {
			String v_lastFragment = v_resource.getURI().lastSegment();
			if (null != v_lastFragment && !v_lastFragment.isEmpty())
				v_lastFragment = v_lastFragment.replace(".entity", "");

			// Par gagner du temps on ne scanne que les librairies.
			if (!v_lastFragment.equals(p_projectName)) {
				TreeIterator<EObject> v_eObjects = v_resource.getAllContents();
				while (v_eObjects.hasNext()) {
					EObject v_eObject = v_eObjects.next();
					if (v_eObject instanceof Entity && p_entity.getName().equals(((Entity) v_eObject).getName()))
						return v_lastFragment;
				}
			}
		}
		return p_projectName;
	}

	/**
	 * Verifie si une entité appartient à une librairie externe. Cela est le cas a
	 * partir du moment ou le nom du fichier qui contient l'entité est different de
	 * celui du projet en cours.
	 * 
	 * @param p_entity      : L'entité pour laquelle essayer de recuperer le nom du
	 *                      projet (librairie).
	 * @param p_projectName : Le nom du projet principal (celui en cours).
	 * @return
	 */
	public static boolean is_libraryEntity(final Entity p_entity, final String p_projectName) {
		return !getLibraryName(p_entity, p_projectName).equals(p_projectName);
	}
}
