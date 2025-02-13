package fr.pacman.start.ui.util;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;

import fr.pacman.start.ui.GenerateStartWizardAction;
import fr.pacman.start.ui.activator.Activator;
import fr.pacman.start.ui.exception.WizardNotFoundException;

/**
 * Classe utilitaire pour le wizard de création d'un nouveau projet.
 * 
 * @author MINARM
 */
public class WizardUtil {

	/**
	 * La liste des codes de retour pour l'exécution d'un wizard.
	 */
	public static final int c_codeOk = 0;
	public static final int c_codeKo = 1;
	public static final int c_codeKoExists = 2;

	/**
	 * Constructeur privé.
	 */
	private WizardUtil() {
		super();
	}

	/**
	 * Rechargement complet du workspace.
	 * 
	 * @param p_monitor un moniteur de progression qui utilise les données de
	 *                  travail d'un moniteur parent.
	 * @throws CoreException une exception suceptible d'être levée pendant
	 *                       l'exécution de la méthode.
	 */
	public static void refreshWorkspace(final SubMonitor p_monitor) throws CoreException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.getRoot().refreshLocal(IResource.DEPTH_INFINITE, p_monitor);
	}

	/**
	 * Demande le rechargement d'un projet dans le workspace, demande la
	 * réorganisation automatique des imports, le formattage automatique du code et
	 * effectue les éventuelles sauvegardes si des éditeurs sont encore ouverts. On
	 * se raccorde sur la classe d'action {@link GenerateStartWizardAction} afin
	 * d'effectuer la majorité de ces demandes.
	 * 
	 * @param p_projectName     le nom du projet en cours de création.
	 * @param p_monitor         un moniteur de progression qui utilise les données
	 *                          de travail d'un moniteur parent. Il s'agit d'une
	 *                          alternative plus sûre et plus facile à utiliser que
	 *                          le SubProgressMonitor.
	 * @param p_subProjectNames le nom des différents sous-projets pour le projet
	 *                          conteneur.
	 * @throws CoreException une exception suceptible d'être levée pendant
	 *                       l'exécution de la méthode.
	 */
	public static void postTreatment(final SubMonitor p_monitor, final IProject p_project,
			final List<String> p_subProjectNames) throws CoreException {
		if (null != p_project && p_project.exists()) {
			new GenerateStartWizardAction().postTreatment(p_project, p_subProjectNames);
			saveAllEditors();
		}
	}

	/**
	 * Demande le rechargement d'un projet dans le workspace.
	 * 
	 * @param p_projectName le nom du projet à recharger.
	 * @param p_monitor     un moniteur de progression qui utilise les données de
	 *                      travail d'un moniteur parent.
	 * @throws CoreException une exception suceptible d'être levée pendant
	 *                       l'exécution de la méthode.
	 */
	public static void refreshProject(final SubMonitor p_monitor, final IProject p_project) throws CoreException {
		if (null != p_project && p_project.exists())
			p_project.refreshLocal(IResource.DEPTH_INFINITE, p_monitor.newChild(100));
	}

	/**
	 * Sauvegarde automatique de l'ensemble des éditeurs qui sont en état 'dirty'
	 * (donc à sauvegarder). Pour l'instant pas d'utilité pour une fenêtre de
	 * confirmation, à voir pus tard si besoin.
	 */
	private static void saveAllEditors() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				if (null != workbench) {
					workbench.saveAllEditors(false);
					// workbench.restart(Boolean.TRUE);
				}
			}
		});
	}

	/**
	 * Retourne un IStatus directement dans une CoreException.
	 * 
	 * @param p_e l'exception
	 * @return status le statut
	 */
	public static IStatus sendErrorStatus(Exception p_e, final String p_pluginId) {
		IStatus status = new Status(IStatus.ERROR, p_pluginId, p_e.getMessage(), p_e);
		Activator.getDefault().getLog().log(status);
		return status;
	}

	/**
	 * Demande le chargement d'un Wizard présent (plugin existant) sur la
	 * plate-forme. On cherche dans les différentes Registry et si on trouve le
	 * Wizard, il est alors chargé à partir de son descripteur. On recherche dans
	 * les différents catégories (new, import, export).
	 * 
	 * @param p_id l'identifiant unique du wizard à charger.
	 * @return le wizard à exécuter.
	 * @throws CoreException une exception suceptible d'être levée pendant
	 *                       l'exécution de la méthode.
	 */
	private static IWorkbenchWizard loadExternalWizard(final String p_id)
			throws CoreException, WizardNotFoundException {
		IWizardDescriptor descriptor = PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(p_id);
		if (descriptor == null)
			descriptor = PlatformUI.getWorkbench().getImportWizardRegistry().findWizard(p_id);
		if (descriptor == null)
			descriptor = PlatformUI.getWorkbench().getExportWizardRegistry().findWizard(p_id);
		if (descriptor == null)
			throw new WizardNotFoundException("Impossible de charger le wizard");
		return descriptor.createWizard();
	}

	/**
	 * Execute un Wizard présent (plugin existant) sur la plate-forme. Ce wizard est
	 * exécuté dans son propre processus.
	 * 
	 * @param p_configurator interface pour le paramétrage éventuel d'un wizard.
	 * @return le code de retour pour l'exécution du wizard.
	 */
	public static int executeExternalWizard(final IParametrizedExternalWizard p_configurator) {

		final int[] result = new int[1];
		result[0] = c_codeOk;
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					boolean perform = executeExternalWizardInDialogBox(p_configurator);
					if (!perform)
						result[0] = c_codeOk;
				} catch (CoreException e) {
					result[0] = c_codeKo;
				} catch (WizardNotFoundException e) {
					result[0] = c_codeKoExists;
				}
			}
		});
		return result[0];
	}

	/**
	 * Charge un wizard sans l'afficher (en sous-main) et lance sa méthode
	 * "performFinish". Ainsi le wizard effectue son action sans aucune action
	 * utilisateur (l'IHM n'est pas affichée mais on pilote le wiezrd de manière
	 * programmatique).
	 * <p>
	 * Ce type de fonctionnement n'est pas optima mais l permet pour l'instant juste
	 * d'effectuer le travail demandé en attendant de trouver une solution plus
	 * élégante et pérenne.
	 * 
	 * @param p_configurator interface pour le paramétrage éventuel du wizard.
	 * @return le code de retour pour l'exécution du wizard.
	 */
	private static boolean executeExternalWizardInDialogBox(final IParametrizedExternalWizard p_configurator)
			throws CoreException, WizardNotFoundException {
		boolean perform = false;
		try {
			final IWorkbenchWizard wizard = loadExternalWizard(p_configurator.getWizardId());
			if (wizard != null) {
				p_configurator.initExternalWizard(wizard);
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				WizardDialog wd = new WizardDialog(shell, wizard);
				wd.create();
				perform = wd.getCurrentPage().getWizard().performFinish();
				wd = null;
			}
		} catch (WizardNotFoundException e) {
			throw e; // Pour l'instant.
		} catch (Exception e) {
			throw new CoreException(sendErrorStatus(e, p_configurator.getWizardId()));
		}
		return perform;
	}

	/**
	 * Ouvre un wizard disponible (plugin existant) sur la plate-forme et l'affiche
	 * pour l'utilisateur.
	 * 
	 * @param p_id    l'identifiant du wizard.
	 * @param p_title le titre à afficher pour la fenêtre.
	 * @throws CoreException une exception suceptible d'être levée pendant
	 *                       l'exécution de la méthode.
	 */
	public static int openExternalWizard(final String p_id, final String p_title) throws CoreException {
		final int[] result = new int[1];
		result[0] = c_codeOk;
		try {
			final IWorkbenchWizard wizard = loadExternalWizard(p_id);
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {

					if (wizard != null) {
						Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
						WizardDialog wd = new WizardDialog(shell, wizard);
						wd.create();
						wd.setTitle(p_title);
						wd.open();
					}
				}
			});
		} catch (WizardNotFoundException e) {
			result[0] = c_codeKoExists;
		}
		return result[0];
	}

	/**
	 * Affiche un message dans une boite de dialogue.
	 * 
	 * @param p_title   le titre de la boite de dialogue.
	 * @param p_message le message à afficher dans la boite de dialogue.
	 */
	public static void displayMessageInDialog(final String p_title, final String p_message) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), p_title,
						p_message);
			}
		});
	}

	/**
	 * Interface pour le parametrage éventuel d'un wizard que l'on désire activer
	 * sans passer par l' interface utilisateur.
	 * 
	 * @author MINARM.
	 */
	public interface IParametrizedExternalWizard {
		public abstract String getWizardId();

		public abstract void initExternalWizard(IWizard p_wizard) throws Exception;
	}
}
