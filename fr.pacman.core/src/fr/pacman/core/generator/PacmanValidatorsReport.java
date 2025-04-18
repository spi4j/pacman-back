package fr.pacman.core.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * La classe {@code PacmanValidatorsReport} permet de gérer un rapport d'erreurs
 * de validation pour des objets issus de la modélisation utilisateur. Elle
 * stocke les erreurs sous forme de paires clé-valeur, où la clé est un objet de
 * type {@link EObject} et la valeur est un message d'erreur associé.
 * <p>
 * Cette classe offre des méthodes pour ajouter des erreurs, réinitialiser le
 * rapport et obtenir le rapport complet. Très simple (jsute le stockage d'une
 * map), elle sert de 'passe-plat' entre la couche AQL et la couche Java, elle
 * est récupérée au niveau de la couche UI afin d'afficher les erreurs de
 * validation de modèle au niveau de l'error log.
 * 
 * @author MINARM
 */
public class PacmanValidatorsReport {

	/**
	 * Le rapport des erreurs de validation.
	 */
	private static List<String> _report = new ArrayList<>();

	/**
	 * Ajoute une erreur de validation au rapport.
	 * 
	 * @param p_object L'objet pour lequel l'erreur de validation est signalée.
	 * @param p_msg    Le message décrivant l'erreur de validation.
	 */
	public static void add(final EObject p_object, final String p_msg) {
		_report.add(p_msg);

	}

	/**
	 * Réinitialise le rapport d'erreurs en le vidant. Cette méthode efface toutes
	 * les erreurs enregistrées dans le rapport.
	 */
	public static void reset() {
		_report.clear();
	}

	/**
	 * Obtient le rapport complet des erreurs de validation.
	 * 
	 * @return Une simple {@link List} contenant les messages d'erreur.
	 */
	public static List<String> get_report(EObject p_object) {
		return _report;
	}

	/**
	 * Vérifie si le rapport généré par le validateur Pacman est vide. Cette méthode
	 * renvoie un résultat indiquant si le rapport est vide ou non.
	 * 
	 * @return true si le rapport est vide, sinon false
	 */
	public static boolean hasReport() {
		return !_report.isEmpty();
	}
}
