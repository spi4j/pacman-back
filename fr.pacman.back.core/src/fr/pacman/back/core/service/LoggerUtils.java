package fr.pacman.back.core.service;

import java.util.logging.Logger;

import org.eclipse.core.runtime.Status;

import fr.pacman.back.core.convention.ApplyNorme;
import fr.pacman.back.core.plugin.Activator;
import fr.pacman.back.core.property.PacmanProperty;

/**
 * Petite classe utilitaire pour l'affichage de messages lors de l'utilisation
 * des classes java interne Pacman.
 * 
 * @author MINARM
 */
public class LoggerUtils {

	/**
	 * Affiche un message d'avertissement (dans l'error log).
	 * 
	 * @param p_message le message d'avertissement
	 */
	public static void warn(final String p_message) {
		if (Activator.getDefault() != null) {
			Activator.getDefault().getLog().log(new Status(Status.WARNING, Activator.c_pluginId, p_message));
		} else {
			Logger.getLogger(ApplyNorme.class.getName()).warning(p_message);
		}
	}

	/**
	 * Affiche un message informatif (dans la console).
	 * 
	 * @param p_message le message d'avertissement
	 */
	public static void info(final String p_message) {
		if (Activator.getDefault() != null) {
			Activator.getDefault().getLog().log(new Status(Status.INFO, Activator.c_pluginId, p_message));
		} else {
			Logger.getLogger(ApplyNorme.class.getName()).info(p_message);
		}
	}

	/**
	 * Affiche la clé de la propriété ainsi que sa valeur associée.
	 * 
	 * @param p_property la propriété
	 */
	public static void logProperty(final PacmanProperty p_property) {
		info(p_property.getId() + " = " + p_property.getValue());
	}
}
