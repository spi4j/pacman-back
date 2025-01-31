package fr.pacman.core.service;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Classe utilitaire pour la gestion des URIs.
 * 
 * @author MINARM
 */
public class UriUtils {

	/**
	 * Consructeur.
	 */
	protected UriUtils() {
		// RAS.
	}

	/**
	 * Permet d'obtenir une instance d'URI à partir d''une instance d'EObject.
	 * 
	 * @param p_EObject L'EObject à prendre en comtpe.
	 * @return L'URI désirée.
	 */
	public static URI getUriFromEObject(final EObject p_EObject) {
//		try {
//			if (p_EObject == null) {
//				ErrorGeneration.printMessageFmt("[ERROR] Impossible de trouver le modèle contenant un objet null", "?");
//				return null;
//			}
//			final Resource resource = p_EObject.eResource();
//			if (resource == null) {
//				String objectString = null;
//				try {
//					final Method getNameMethod = p_EObject.getClass().getMethod("getName");
//					objectString = p_EObject.getClass().getSimpleName() + "#" + getNameMethod.invoke(p_EObject);
//				} catch (Exception e) {
//					objectString = p_EObject.toString();
//				}
//				ErrorGeneration.printMessageFmt(
//						"[ERROR] Impossible de trouver le modèle pour l'objet suivant : " + objectString, "?");
//				return null;
//			}
//			final URI uri = resource.getURI();
//			if (uri == null) {
//				ErrorGeneration.printMessageFmt(
//						"[ERROR] Impossible de calculer l'URI de la ressource suivante : " + resource.toString(),
//						"?");
//				return null;
//			}
//			// On a une ressource CDO
//			if ("cdo".equalsIgnoreCase(uri.scheme())) {
//				// Récupération de l'AIRD
//				final Session session = SessionManager.INSTANCE.getSession((EObject) p_EObject);
//				if (session == null) {
//					ErrorGeneration.printMessageFmt("[ERROR] Impossible de récupérer la session CDO", "?");
//					ErrorGeneration.doIfThrowErrorGenerationException();
//					return null;
//				}
//				final Resource aird = session.getSessionResource();
//				return aird.getURI();
//			}
//			return uri;
//		} catch (Throwable t) {
//			ErrorGeneration.printMessageFmt(t, "?");
//			ErrorGeneration.doIfThrowErrorGenerationException();
//			return null;
//		}
		return null;
	}

	/**
	 * Permet d'obtenir répertoire du modèle à partir d'une instance d'URI.
	 * 
	 * @param p_uri L'URI à prendre en comtpe.
	 * @return La chaîne de caractères représentant le répertoire du modèle.
	 */
	public static String getModelFolderFromUri(final URI p_uri) {
		return null;
	}

	/**
	 * Retourne le URI du parent du modèle d'un objet
	 * 
	 * @param p_o l'objet du modèle
	 * @return l'URI du parent
	 */
	public static String getModelFolderFromEObject(final EObject p_eObject) {
		try {
			if (p_eObject == null) {
				return "[ERROR] Impossible de trouver le modèle contenant un objet null";
			}
			final Resource resource = p_eObject.eResource();
			if (resource == null) {
				return "[ERROR] Impossible de trouver le modèle pour l'objet suivant : " + p_eObject.toString();
			}
			final URI uri = resource.getURI();
			if (uri == null) {
				return "[ERROR] Impossible de calculer l'URI de la ressource suivante : " + resource.toString();
			}
			return getModelFolderFromUri(uri);
		} catch (Throwable t) {
			return "[ERROR] " + t.toString() + "\n\nOBJECT = " + p_eObject + "\nRESOURCE = " + p_eObject.eResource()
					+ "\nURI = " + p_eObject.eResource().getURI();
		}
	}
}
