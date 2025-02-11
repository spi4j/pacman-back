package fr.pacman.core.service;

import java.util.ArrayList;
import java.util.List;

import org.obeonetwork.dsl.soa.Component;
import org.obeonetwork.dsl.soa.ExpositionKind;
import org.obeonetwork.dsl.soa.Operation;
import org.obeonetwork.dsl.soa.Service;

/**
 * 
 * @author MINARM
 */
public class RestUtils {

	/**
	 * Analyse l'ensemble des services et retourne la liste des diféfrentes URIs à
	 * sécuriser pour l'ensemble des opérations de type rest. Effectue des
	 * regroupements si besoin (si toutes les opérations du service sont sécurisées
	 * alors on ne crée qu'une seule URI pour l'opération, sinon on ajoute chaque
	 * URI d'opération dans la liste, à condition que cela ne vérouille pas une URI
	 * en amont, par exemple '/*' ou '/api/*').
	 * 
	 * @param p_services la liste des services (tous services confondus), pour
	 *                   l'ensemble du système(root).
	 * @return la liste des URIs à sécuriser.
	 */
	public static List<String> get_securedURIs(List<Service> p_services) {

		int nbOperations;
		List<String> securedURIs = new ArrayList<>();
		List<String> tmpSecuredURIs = new ArrayList<>();

		for (Service service : p_services) {
			if (!service.getSecuritySchemes().isEmpty() && isValid(service.getURI())) {
				securedURIs.add(buildURI(service, "*"));
			} else {
				nbOperations = 0;
				tmpSecuredURIs.clear();
				for (Operation operation : service.getOwnedInterface().getOwnedOperations()) {
					nbOperations++;
					if (operation.getExposition() == ExpositionKind.REST)
						if (!operation.getSecuritySchemes().isEmpty())
							tmpSecuredURIs.add(buildURI(service, operation.getURI()));
				}
				if (nbOperations == tmpSecuredURIs.size() && isValid(service.getURI())) {
					securedURIs.add(buildURI(service, "*"));
				} else {
					securedURIs.addAll(tmpSecuredURIs);
				}
			}
		}
		return securedURIs;
	}

	/**
	 * Vérifie si un fragment d'URI existe, on ne vérifie pas ici la validité du
	 * fragment mais uniquement son existence.
	 * 
	 * @param p_uriFragment le fragement d'URI à vérifier.
	 * @return la valeur 'true ' si le fragment d'URI existe.
	 */
	private static boolean isValid(final String p_uriFragment) {
		return p_uriFragment != null && !p_uriFragment.isEmpty() && !p_uriFragment.isBlank();
	}

	/**
	 * Construction de l'URI à partir des différents fragments d'URI.
	 * 
	 * @param p_service         le service en cours de traitement (celui sur lequel
	 *                          on liste les opérations).
	 * @param p_lastUriFragment le fernier fragment d'URI (normallement celui dédié
	 *                          à l'opération).
	 * @return
	 */
	private static String buildURI(final Service p_service, String p_lastUriFragment) {
		String baseURI = ((Component) p_service.eContainer()).getURI();
		return (uriFragment(baseURI).concat(uriFragment(p_service.getURI())).concat(uriFragment(p_lastUriFragment))
				.replaceAll("/+", "/"));
	}

	/**
	 * Retourne un fragment d'URI 'normalisé' pour le besoin de la méthode
	 * appelante.
	 * 
	 * @param p_uri le fragment d'URI.
	 * @return le fragment d'URI normalisé.
	 */
	private static String uriFragment(String p_uri) {
		if (null == p_uri) {
			return "/";
		}
		return "/" + p_uri.trim();
	}
}
