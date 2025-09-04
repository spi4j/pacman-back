package fr.pacman.back.core.ui.service;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import fr.pacman.back.core.service.UriUtils;
import fr.pacman.back.core.ui.plugin.Activator;

public class PlugInUtils extends UriUtils {

	/**
	 * Constructeur privé.
	 */
	private PlugInUtils() {
		// RAS.
	}

	/**
	 * Affiche une information.
	 * 
	 * @param p_title   le titre
	 * @param p_message le message
	 */
	public static void displayInformation(final String p_title, final String p_message) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					// MessageDialog.setDefaultImage(new
					// Image(PlatformUI.getWorkbench().getDisplay(),"icons/cali.png"));
					MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), p_title,
							p_message);
				} catch (final IllegalStateException v_e) {
					MessageDialog.openError(null, p_title, p_message);
				}
			}
		});
	}

	/**
	 * Affichage d'une popup d'erreur.
	 * 
	 * @param p_title   le titre de la popup
	 * @param p_message le message d'erreur
	 */
	public static void displayError(final String p_title, final String p_message) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), p_title,
							p_message);
				} catch (final IllegalStateException v_e) {
					MessageDialog.openError(null, p_title, p_message);
				}
			}
		});
	}

	/**
	 * Permet d'obtenir le répertoire du modèle à partir d'une instance d'EObject.
	 * 
	 * @param p_o L'EObject à prendre en comtpe.
	 * @return La chaîne de caractères représentant le répertoire du modèle.
	 */
	public static String getModelFolderFromEObject(final EObject p_o) {
		return getModelFolderFromUri(getUriFromEObject(p_o));
	}

	/**
	 * Cherche le chemin d'un projet par son nom.
	 * 
	 * @param p_name le nom du projet
	 * @return le chemin vers le projet
	 */
	public static File findProjectByName(final String p_name) {
		String v_path = null;
		try {
			v_path = ResourcesPlugin.getWorkspace().getRoot().getProject(p_name).getLocationURI().getPath();

			return new File(v_path);
		} catch (final Exception v_e) {

			String v_err = " - nom du projet : " + p_name + "\n - path du projet : " + v_path;

			Activator.getDefault().getLog().log(
					new Status(Status.WARNING, Activator.c_pluginId, "Impossible de récupérer le projet  : " + v_err));

			displayError("Impossible de récupérer le projet", v_err);
			return null;
		}
	}
}
