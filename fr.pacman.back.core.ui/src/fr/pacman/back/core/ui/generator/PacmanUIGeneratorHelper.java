package fr.pacman.back.core.ui.generator;

import fr.pacman.back.core.ui.service.PlugInUtils;

/**
 * Classe utilitaire pour l'affichage de messages d'erreur ou d'information dans
 * l'interface utilisateur du plugin Pacman.
 *
 * Cette classe fournit des méthodes statiques pour afficher des popups
 * d'alerte, d'information ou des messages d'erreur détaillés à l'utilisateur.
 */
public class PacmanUIGeneratorHelper {

	/**
	 * Affiche une popup d'erreur avec un message détaillé à partir d'une exception.
	 * 
	 * Si l'exception contient une cause, son message est également inclus dans la
	 * popup. Ce message est destiné à informer l'utilisateur qu'une erreur a
	 * interrompu la génération.
	 * 
	 * @param p_e L'exception levée pendant la génération.
	 */
	public static void showErrors(final Exception p_e) {
		String msg = p_e.getMessage();
		if (p_e.getCause() != null) {
			msg = p_e.getCause().getMessage();
		}
		PlugInUtils.displayError("Pacman",
				"Une erreur fonctionnelle ou technique a été détectée lors de la génération : " + p_e
						+ "\n\rCause de l'erreur : " + msg + "\n\rLa génération va être stoppée.");
	}

	/**
	 * Affiche une popup d'alerte contenant le message spécifié.
	 * 
	 * @param p_msg Le message à afficher dans la popup d'alerte.
	 */
	public static void displayPopUpAlert(final String p_msg) {
		PlugInUtils.displayError("Pacman", p_msg);
	}

	/**
	 * Affiche une popup d'alerte contenant le message spécifié.
	 * 
	 * @param p_e L'exception initiale.
	 */
	public static void displayPopUpAlert(final Exception p_e) {
		String msg = p_e.getLocalizedMessage();
		if ((msg == null || msg.isBlank()) && p_e.getCause() != null) {
			msg = p_e.getCause().getLocalizedMessage();
		}
		displayPopUpAlert(msg);
	}

	/**
	 * Affiche une popup d'information contenant le message spécifié.
	 * 
	 * @param p_msg Le message à afficher dans la popup d'information.
	 */
	public static void displayPopUpInfo(final String p_msg) {
		PlugInUtils.displayInformation("Pacman", p_msg);
	}
}
