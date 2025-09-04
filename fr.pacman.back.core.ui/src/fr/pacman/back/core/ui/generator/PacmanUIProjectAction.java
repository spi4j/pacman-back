package fr.pacman.back.core.ui.generator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.actions.FormatAllAction;
import org.eclipse.jdt.ui.actions.OrganizeImportsAction;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import fr.pacman.back.core.property.project.ProjectProperties;

/**
 * 
 * @author MINARM
 */
public abstract class PacmanUIProjectAction {

	/**
	 * Vérifie si le générateur doit doit gérer une vue Eclipse (ErrorLog, Problems,
	 * etc.. ).
	 * 
	 * @return 'true' si le générateur doit gérer une vue Eclipse, sinon retourne la
	 *         valeur 'false'.
	 */
	protected abstract boolean hasView();

	/**
	 * Retourne la fenêtre de travail active. Si aucune fenêtre n'est active,
	 * retourne la première fenêtre trouvée dans la liste des fenêtres disponibles.
	 * 
	 * @return la fenêtre de travail courant.
	 */
	protected IWorkbenchWindow getWorkbenchWindow() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (hasView())
			return workbench.getActiveWorkbenchWindow();
		if (workbench.getWorkbenchWindowCount() > 0)
			return workbench.getWorkbenchWindows()[0];
		return null;
	}

	/**
	 * Si l'option est activée, demande l'organisation et le formattage automatique
	 * des imports Java par l'IDE.
	 * 
	 * @param p_targetWorkspaceContainer le conteneur de ressources.
	 * @param p_targetSite               interface entre ....
	 * @throws CoreException une exception levée pendant le traitement.
	 */
	protected void doImportsAction(final IContainer p_targetWorkspaceContainer, final IWorkbenchPartSite p_targetSite)
			throws CoreException {
		if (!ProjectProperties.isModeDebug() && p_targetSite != null && PacmanUIGeneratorsReport.getNbFiles() >= 0)
			runDispatchAction(p_targetWorkspaceContainer.getProject(), new OrganizeImportsAction(p_targetSite));
	}

	/**
	 * Si l'option est activée, demande le formattage automatique des classes Java
	 * par l'IDE.
	 * 
	 * @param p_targetWorkspaceContainer le conteneur de ressources.
	 * @param p_targetSite               interface entre .....
	 * @throws CoreException une exception levée pendant le traitement.
	 */
	protected void doFormatAction(final IContainer p_targetWorkspaceContainer, final IWorkbenchPartSite p_targetSite)
			throws CoreException {
		if (!ProjectProperties.isModeDebug() && p_targetSite != null && PacmanUIGeneratorsReport.getNbFiles() >= 0)
			runDispatchAction(p_targetWorkspaceContainer.getProject(), new FormatAllAction(p_targetSite));
	}

	/**
	 * Permet de passer une commande de type {@link SelectionDispatchAction} à
	 * l'IDE. Cela permet de bénéficier de l'ensemble des commandes existantes de
	 * l'IDE (Eclipse) qui sont accessibles soit pas menu soit par séquence de
	 * touche. Cette commande ne doit impacter que les sous-projets de type Java.
	 * 
	 * Pour l'instant on utilise déjà l'organisation des imports (CTRL + SHIFT + O)
	 * et la demande de formattage automatique (CRL + SHIFT + F).
	 * 
	 * @param p_project le projet cible.
	 * @param p_action  l'action à executer pour le projet cible.
	 * @throws CoreException une exception levée lors de l'exécution du traitement.
	 */
	private void runDispatchAction(final IProject p_project, final SelectionDispatchAction p_actionToExcute)
			throws CoreException {
		Runnable job = new Runnable() {
			@Override
			public void run() {
				try {
					IJavaProject prj = null;
					if (p_project.exists() && p_project.hasNature(JavaCore.NATURE_ID)) {
						prj = JavaCore.create(p_project);
						IStructuredSelection selection = new StructuredSelection(prj);
						p_actionToExcute.run(selection);
					}
				} catch (CoreException ce) {
					ce.printStackTrace();
				}
			}
		};
		getWorkbenchWindow().getShell().getDisplay().syncExec(job);
	}

	/**
	 * Retourne l'interface principale ...
	 * 
	 * @return The target site.
	 */
	protected IWorkbenchPartSite getTargetSite() {
		IWorkbenchWindow window = getWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IWorkbenchPart activePart = page.getActivePart();
				if (activePart != null) {
					return activePart.getSite();
				}
			}
		}
		throw new RuntimeException("No active part available.");
	}
}
