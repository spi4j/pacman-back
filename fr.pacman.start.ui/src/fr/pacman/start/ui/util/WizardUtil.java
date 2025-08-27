package fr.pacman.start.ui.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
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
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.wizards.IWizardDescriptor;

import fr.pacman.core.ui.validation.PacmanUIValidationView;
import fr.pacman.start.ui.GenerateStartWizardAction;
import fr.pacman.start.ui.activator.Activator;
import fr.pacman.start.ui.exception.WizardNotFoundException;
import fr.pacman.start.ui.views.HtmlViewerView;

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

	private static final String c_view_log = "org.eclipse.pde.runtime.LogView";
	private static final String c_view_problem = "org.eclipse.ui.views.ProblemView";
	private static final String c_view_properties = "org.eclipse.ui.views.PropertySheet";
	private static final String c_view_junit = "org.eclipse.jdt.junit.ResultView";
	private static final String c_view_html = "fr.pacman.start.ui.views.HtmlViewerView";
	private static final String c_view_progress = "org.eclipse.ui.views.ProgressView";
	private static final String c_view_validation = PacmanUIValidationView.VALIDATION_VIEW_ID;

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
	 * Demande le rechargement d'un projet dans le workspace.
	 * 
	 * Demande la réorganisation automatique des imports, le formattage automatique
	 * du code et effectue les éventuelles sauvegardes si des éditeurs sont encore
	 * ouverts. Par ailleurs on rend la main sur le contrôle de la vue des erreurs
	 * et on demande le chargement de la vue concernat les problèmes et la vue de
	 * propriété. Ainsi le développeur est prêt pour travailler.
	 * 
	 * Pour finir, on force le focus sur le navigateur interne pour le récapitulatif
	 * de la création de projet.
	 * 
	 * On se raccorde sur la classe d'action {@link GenerateStartWizardAction} afin
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
		// Restaure le comportement standard pour les vues.
		Display.getDefault().syncExec(() -> {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window != null) {
				ProgressViewUtils.stopMonitoring(window);
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					IViewPart view = null;
					try {
						// IViewPart view = page.showView("org.eclipse.ui.views.ProgressView");
						// page.setPartState(page.getReference(view), IWorkbenchPage.STATE_RESTORED);
						view = page.showView(c_view_validation);
						view = page.showView(c_view_log);
						view = page.showView(c_view_problem);
						view = page.showView(c_view_properties);
						view = page.showView(c_view_junit);
						view = page.showView(c_view_html);

						page.hideView(view); // Sinon de rafraichit pas.
						view = page.showView(c_view_html);

					} catch (PartInitException e) {
						// RAS (pas grave à ce niveau, on ne vas pas polluer la création)
					} finally {
						if (view != null) {
							view.setFocus();
						}
					}
				}
			}
		});
	}

	/**
	 * Initialise les vues dans la fenêtre de travail active, en masquant toutes les
	 * vues ouvertes et en affichant la vue de progression des tâches. Cette méthode
	 * est exécutée de manière asynchrone sur le thread de l'UI.
	 * 
	 * Elle supprime toutes les vues actuellement ouvertes dans la page de travail
	 * active, puis affiche la vue de progression des tâches et commence à
	 * surveiller les tâches en cours avec
	 * {@link ProgressViewUtils#monitor(IWorkbenchWindow)}.
	 *
	 * Cette méthode est principalement utilisée avant de lancer un job ou une série
	 * de tâches asynchrones, afin de réinitialiser les vues et d'afficher un
	 * indicateur de progression. En cas d'erreur lors de l'affichage de la vue, une
	 * exception est lancée.
	 * 
	 * @param p_monitor Un objet {@link SubMonitor} utilisé pour suivre la
	 *                  progression de l'opération. Il est utilisé pour la gestion
	 *                  de la progression dans les tâches asynchrones (bien que non
	 *                  directement utilisé dans cette méthode, il pourrait être
	 *                  intégré pour la gestion des sous-tâches).
	 * 
	 * @throws RuntimeException Si une erreur se produit lors de l'affichage de la
	 *                          vue de progression, notamment en cas
	 *                          d'initialisation échouée de la vue ou d'une
	 *                          exception dans la méthode
	 *                          {@link IWorkbenchPage#showView(String)}.
	 */
	public static void initViews(final SubMonitor p_monitor) {
		Display.getDefault().syncExec(() -> {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					try {
						page.showView(c_view_progress);
						ProgressViewUtils.monitor(window);
					} catch (PartInitException e) {
						throw new RuntimeException("Impossible d'afficher la vue pour la progression des tâches", e);
					}
				}
			}
		});
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

	/**
	 * Affiche une page HTML à partir d'un chemin de fichier. Si un navigateur
	 * interne est disponible, il est utilisé, sinon un navigateur externe est
	 * ouvert.
	 *
	 * @param p_path Le chemin absolu du fichier HTML à afficher.
	 */
	public static void showHtml(String p_path) {
		Display.getDefault().syncExec(() -> {
			try {
				File file = new File(p_path);
				if (file.exists()) {
					URL fileUrl = file.toURI().toURL();
					boolean browserAvailable = tryOpenInternalBrowser(fileUrl.toString());
					if (!browserAvailable) {
						openExternalBrowser(fileUrl);
					}
				} else {
					// RAS (pas grave dans notre cas)
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Essaie d'ouvrir une page HTML dans un navigateur interne (dans une vue
	 * Eclipse).
	 *
	 * @param p_url L'URL de la page HTML à afficher.
	 * @return true si le navigateur interne a été ouvert avec succès, false sinon.
	 */
	private static boolean tryOpenInternalBrowser(String p_url) {
		try {
			HtmlViewerView.setHtmlUrlToLoad(p_url);
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			page.showView(HtmlViewerView.c_id);
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	/**
	 * Ouvre un fichier HTML dans un navigateur externe (par exemple, le navigateur
	 * par défaut).
	 *
	 * @param p_url L'URL de la page HTML à afficher.
	 */
	private static void openExternalBrowser(URL p_url) {
		try {
			IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
			IWebBrowser browser = browserSupport.getExternalBrowser();
			browser.openURL(p_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convertit un fichier Markdown en HTML et l'affiche dans un navigateur interne
	 * ou externe.
	 *
	 * @param p_markdownPath Le chemin du fichier Markdown à convertir et afficher.
	 */
	public static void showHtmlFromMarkdown(String p_markdownPath) {
		try {

			String markdown = new String(Files.readAllBytes(Paths.get(p_markdownPath)), "UTF-8");
			Parser parser = Parser.builder().build();
			HtmlRenderer renderer = HtmlRenderer.builder().build();
			String htmlContent = renderer.render(parser.parse(markdown));

			String htmlWithCharset = "<!DOCTYPE html>\n<html lang=\"fr\">\n<head>\n<meta charset=\"UTF-8\">"
					+ "\n<title>Markdown Converti</title>\n</head>\n<body>\n" + htmlContent + "\n</body>\n</html>";

			File tempHtmlFile = File.createTempFile("converted", ".html");
			tempHtmlFile.deleteOnExit();
			Files.write(tempHtmlFile.toPath(), htmlWithCharset.getBytes("UTF-8"));
			showHtml(tempHtmlFile.getAbsolutePath());

		} catch (IOException e) {
			// RAS (pas grave dans notre cas)
		}
	}
}
