package fr.pacman.start.ui;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPartSite;

import fr.pacman.core.ui.generator.PacmanUIProjectAction;

/**
 * 
 * @author MINARM
 */
public class GenerateStartWizardAction extends PacmanUIProjectAction {

	@Override
	protected boolean hasView() {
		return false;
	}

	/**
	 * Ne fonctionne pas si on veut effectuer le traitement dierctement à la racine,
	 * il faut itérer sur chaque sous-projet.
	 * 
	 * @param p_subProjectNames
	 * @throws CoreException
	 */
	public void postTreatment(final IProject p_project, final List<String> p_subProjectNames) throws CoreException {
		for (String subProjectName : p_subProjectNames) {
			final File targetFolder = new File(
					p_project.getLocation() + File.separator + subProjectName);
			final IContainer targetWorkspaceContainer = ResourcesPlugin.getWorkspace().getRoot()
					.getContainerForLocation(new Path(targetFolder.getAbsolutePath()));

			if (targetWorkspaceContainer != null && targetWorkspaceContainer.getProject().exists()) {
				targetWorkspaceContainer.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
				final IWorkbenchPartSite targetSite = getTargetSite();
				doImportsAction(targetWorkspaceContainer, targetSite);
				doFormatAction(targetWorkspaceContainer, targetSite);
			}
		}
	}
}
