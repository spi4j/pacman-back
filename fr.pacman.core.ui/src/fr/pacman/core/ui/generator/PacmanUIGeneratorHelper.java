package fr.pacman.core.ui.generator;

import fr.pacman.core.ui.service.PlugInUtils;

public class PacmanUIGeneratorHelper {

	/**
	 * 
	 * @param p_e
	 * @param p_statusCode
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

	public static void displayPopUpAlert(final String p_msg) {
		PlugInUtils.displayError("Pacman", p_msg);
	}
}
