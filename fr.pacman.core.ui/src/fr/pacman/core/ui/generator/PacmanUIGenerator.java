package fr.pacman.core.ui.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Logger;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.actions.FormatAllAction;
import org.eclipse.jdt.ui.actions.OrganizeImportsAction;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.obeonetwork.dsl.entity.Entity;
import org.obeonetwork.dsl.environment.Namespace;
import org.obeonetwork.dsl.soa.Component;

import fr.pacman.core.generator.PacmanGenerator;
import fr.pacman.core.property.PropertiesHandler;
import fr.pacman.core.property.project.ProjectProperties;
import fr.pacman.core.ui.plugin.Activator;
import fr.pacman.core.ui.service.PlugInUtils;

/**
 * Classe abstraite pour l'ensemble de générateurs Pacman (au niveau de la
 * couche UI). Cette classe est chargée de l'instanciation et du lancement des
 * différents générateurs internes (hors couche UI) à partir des handlers de la
 * couche UI, handlers eux mêmes activés à partir des fichiers plugin.xml
 * présents dans la couche UI.
 * <p>
 * Tous les générateurs internes des projets de génération au niveau de la
 * couche UI doivent obligatoirement étendre de cette classe abstraite;
 * 
 * @author MINARM
 */
public abstract class PacmanUIGenerator {

	private static final String c_txtErrIncompatiblesOptions = "Les options prises lors de la création "
			+ "de ce projet ne permettent pas l'utilisation de ce générateur. \n\r La génération va être stoppée.";

	/**
	 * Le profiler pour le réglage des performances lors des générations.
	 */
	private PacmanUIAcceleoProfiler _profiler;

	/**
	 * Le chemin racine pour le projet, il est déduit de la ressource qui a été
	 * préalablement sélectionnée par l'utilisateur afin de lancer le générateur UI.
	 * Ce chemin sert de base pour le calcul de l'ensemble des différents chemins
	 * cibles de génération.
	 */
	private File _rootPath;

	/**
	 * La liste des ressources sélectionnées par l'utilisateur pour lancer la
	 * génération. Ces ressources sont ici uniquement des {@link EObject}. Si la
	 * ressource est un fichier, alors cette liste est vide.
	 */
	private List<EObject> _values;

	/**
	 * La liste des ressources sélectionnées par l'utilisateur pour lancer la
	 * génération. Ces resources représentent ici uniquement des ressources de type
	 * fichier. Si la génération. Ces ressources sont ici uniquement des
	 * {@link EObject}. Si la ressource est un fichier, alors cette liste est vide.
	 */
	private List<String> _resources = new ArrayList<>();

	/**
	 * Constructeur pour une sélection par ressource de type 'fichier'. Ce fichier
	 * peut être un fichier de type '.entities', '.soa', '.requirements',
	 * .environment'.
	 * <p>
	 * A ce niveau et pour l'instant on ne prend en compte qu'une seule ressource,
	 * même si le système est prévu à la base pour pouvoir traiter plusieurs
	 * ressources (évolution future si besoin).
	 * 
	 * @param p_selectedResource la ressource sélectionnée par le développeur.
	 */
	public PacmanUIGenerator(IResource p_selectedResource) {
		_resources = Collections.singletonList(p_selectedResource.getLocation().toString());
		_rootPath = new File(p_selectedResource.getLocation().toString()).getParentFile();
		_values = Collections.emptyList();
	}

	/**
	 * Constructeur pour une sélection de ressources de type {@link EObject}. Cette
	 * ressource peut être un {@link Component}, un {@lin DTO}, une {@link Entity},
	 * un {@link Namespace}, etc..
	 * <p>
	 * A ce niveau et pour l'instant on ne prend en compte qu'une seule ressource,
	 * même si le système est prévu à la base pour plusieurs ressources (évolution
	 * future si besoin).
	 * 
	 * @param p_selectedEObject la ressource sélectionnée par le développeur.
	 */
	public PacmanUIGenerator(EObject p_selectedEObject) {
		_values = Collections.singletonList(p_selectedEObject);
		_rootPath = new File(PlugInUtils.getModelFolderFromEObject(p_selectedEObject));
		_resources = Collections.emptyList();
	}

//	/**
//	 * Constructeur partiel pour la création d'un nouveau projet. Ce constructeur
//	 * est juste utilisé pour pouvoir bénéficier (et uniquement dans le cadre de la
//	 * création de projet) de la méthode de post traitement, et éviter ainsi une
//	 * duplication de code.
//	 * 
//	 * @param p_project le projet en cours de création.
//	 */
//	public PacmanUIGenerator(IProject p_project) {
//		_rootPath = new File(p_project.getLocation() + File.separator + p_project.getName() + "-model");
//	}

	/**
	 * Retourne la liste de l'ensemble des propriétés de génération (options) qui
	 * sont incompatibles avec le modèle et le(s) générateur(s) sélectionné(s).
	 * <p>
	 * Si le résultat booléen issu de la récupération de la propriété a la valeur
	 * 'true', cela signifie que la propriété est activée et que le générateur ne
	 * doit pas être lancé.
	 * 
	 * @return la liste des propriétés de génération (options) qui sont
	 *         incompatibles avec le modèle et le(s) générateur(s) sélectionné(s).
	 */
	protected abstract List<String> getIncompatibleOptions();

	/**
	 * Flag d'activation pour la demande de délégation de la gestion des imports à
	 * l'IDE (récupération et organisation automatique des imports pour les classes
	 * Java générées).
	 * <p>
	 * Ici, cette propriété n'a aucune relation avec la demande plus globale du
	 * développeur (par configuration) d'activer ou non l'organisation automatique
	 * des imports. Il s'agit de faire un contrôle plus fin au niveau du générateur
	 * : si l'option d'organisation automatique est activée (de manière globale),
	 * est-ce que ce générateur spécifique permet lui aussi l'organisation
	 * automatique des imports à son niveau.
	 * <p>
	 * Pour exemple, il n'y a aucun intérêt à bénéficier de l'organisation
	 * automatique des imports si le générateur ne crée pas de classe Java.
	 * 
	 * @return positionner à la valeur 'true' pour demander l'organisation
	 *         automatique des imports, sinon mettre à 'false'.
	 */
	protected abstract boolean isOrganizeImports();

	/**
	 * Retourne la liste des générateurs à executer pour la demande de génération de
	 * code. Un générateur de la couche UI peut en effet, appeler un à plusieurs
	 * générateurs internes afin d'effectuer le travail de génération.
	 * 
	 * @return la liste des différents générateurs à executer.
	 */
	protected abstract List<PacmanGenerator> getGenerators();

	/**
	 * Retourne l'identifiant unique du plugin, sous forme de chaîne de caractères.
	 * 
	 * @return l'identifiant unique du plugin.
	 */
	protected abstract String getPluginId();

	/**
	 * Retourne le logger spécifique pour le pugin.
	 * 
	 * @return le logger pour le plugin.
	 */
	protected abstract Logger getLogger();

	/**
	 * Vérifie si le générateur doit doit gérer une vue Eclipse (ErrorLog, Problems,
	 * etc.. ).
	 * 
	 * @return 'true' si le générateur doit gérer une vue Eclipse, sinon retourne la
	 *         valeur 'false'.
	 */
	protected abstract boolean hasView();

	/**
	 * Méthode principale, point d'entrée pour les générateurs au niveau de la
	 * couche UI.
	 * <p>
	 * Lance l'ensemble des générateurs internes qui ont préalablement été
	 * enregistrés auprès du générateur de la couche UI (en l'occurence, l'ensemble
	 * des classes filles de la classe {@link PacmanUIGenerator}).
	 */
	public void generate() {
		final IRunnableWithProgress operation = new IRunnableWithProgress() {
			@Override
			public void run(final IProgressMonitor p_monitor) {
				Monitor monitor = new BasicMonitor();
				PacmanUIAcceleoProfiler.set_project(null);
				PropertiesHandler.init(_rootPath.getPath());
				//PacmanUIGeneratorsReport.reset();
				if (hasSelectionIncompatibilities())
					return;

				if (ProjectProperties.isProfilerEnabled())
					_profiler = new PacmanUIAcceleoProfiler();

				for (PacmanGenerator generator : getGenerators()) {
					generator.setRootPath(_rootPath.getParent());
					generator.setResources(_resources);
					generator.setValues(_values);
					generator.generate(monitor);
				}

				if (ProjectProperties.isProfilerEnabled())
					_profiler.write();
			}
		};

		try {
			PlatformUI.getWorkbench().getProgressService().run(true, true, operation);

		} catch (final Exception p_e) {
			PacmanUIGeneratorHelper.showErrors(p_e);

		} finally {
			try {
				postTreatment();
				//PacmanUIGeneratorsReport.log(Boolean.valueOf(ProjectProperties.getIsDisplayGeneratorReport()));
				PropertiesHandler.exit();

			} catch (Exception p_e) {
				PacmanUIGeneratorHelper.showErrors(p_e);
			}
		}
	}

	/**
	 * Regroupe ici l'ensemble du code de vérification pour les éventuelles
	 * incompatibilités de génération qui ne peuvent être détectées en amont, au
	 * niveau de l'affichage du menu pour le lancement du générateur.
	 * <p>
	 * Il est à noter que l'on pourrait aussi remonter ces informations au niveau de
	 * la classe {@link PacmanUIPropertyTester}. A voir plus tard si on effectue une
	 * évolution en ce sens).
	 * 
	 * @return la valeur 'true' si aun moins une optios de génération est
	 *         incompatible avec le générateur sélectionné.
	 */
	private boolean hasSelectionIncompatibilities() {
		return !_resources.isEmpty() && hasIncompatibleOptions();
	}

	/**
	 * Ensemble des opérations supplémentaires à effectuer suite à la génération.
	 * <p>
	 * Ici, on demande de rafraîchissement pour l'ensemble des sous-projets qui ont
	 * été préalablement définis dans le cadre du générateur cible.
	 * <p>
	 * Si la liste des sous-projets est vide ou si elle est nulle, la globalité du
	 * projet doit alors être rafraichie (peut importe le nombre de sous-projets).
	 * On part alors du nom de l'application pour demander le rafraîchissement.
	 * <p>
	 * Par ailleurs, on en profite pour lire les différents options supplémentaires
	 * au demandées niveau de l'IDE. Pour l'instant il s'agit de la demande
	 * d'organisation automatique des imports ainsi que le formattage automatique
	 * des fichiers générés. A compléter selon les besoins.
	 * 
	 * @throws CoreException une exception levée lors de l'exécution du traitement.
	 */
	protected void postTreatment() throws CoreException {
		for (PacmanGenerator generator : getGenerators()) {
			final File targetFolder = new File(_rootPath.getParent() + File.separator + generator.getSubProjectName());
			final IContainer targetWorkspaceContainer = ResourcesPlugin.getWorkspace().getRoot()
					.getContainerForLocation(new Path(targetFolder.getAbsolutePath()));

			if (targetWorkspaceContainer != null && targetWorkspaceContainer.getProject().exists()) {
				try {
					targetWorkspaceContainer.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
					final IWorkbenchPartSite targetSite = getTargetSite();
					doImportsAction(targetWorkspaceContainer, targetSite);
					doFormatAction(targetWorkspaceContainer, targetSite);

				} catch (CoreException p_e) {
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, getPluginId(),
							"Impossible de rafraîchir " + targetWorkspaceContainer.getFullPath(), p_e));
				}
			}
		}
	}

	/**
	 * Si l'option est activée, demande l'organisation et le formattage automatique
	 * des imports Java par l'IDE.
	 * 
	 * @param p_targetWorkspaceContainer le conteneur de ressources.
	 * @param p_targetSite               interface entre ....
	 * @throws CoreException une exception levée pendant le traitement.
	 */
	private void doImportsAction(final IContainer p_targetWorkspaceContainer, final IWorkbenchPartSite p_targetSite)
			throws CoreException {
		if (!ProjectProperties.isModeDebug() && p_targetSite != null
				&& PacmanUIGeneratorsReport.getNbFiles() >= 0)
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
	private void doFormatAction(final IContainer p_targetWorkspaceContainer, final IWorkbenchPartSite p_targetSite)
			throws CoreException {
		if (!ProjectProperties.isModeDebug() && p_targetSite != null
				&& PacmanUIGeneratorsReport.getNbFiles() >= 0)
			runDispatchAction(p_targetWorkspaceContainer.getProject(), new FormatAllAction(p_targetSite));
	}

	/**
	 * Vérifie si toutes les options sélectionnées par le développeur sont
	 * compatibles avec la demande de génération.
	 * <p>
	 * La couche UI envoie une liste de valeurs booléennes. Si une seule de ces
	 * valeurs est à 'true', une des options incompatibles avec le générateur est
	 * alors considérée comme active et le générateur doit être stoppé.
	 * 
	 * @return 'true' si une seule (au moins) des valeurs retournées par la liste
	 *         des options incompatibles à la valeur 'true', sinon retourne la
	 *         valeur 'false.
	 */
	protected boolean hasIncompatibleOptions() {
		if (null == getIncompatibleOptions() || getIncompatibleOptions().isEmpty())
			return false;
		for (String valueOfProperty : getIncompatibleOptions()) {
			if (Boolean.valueOf(valueOfProperty)) {
				PacmanUIGeneratorHelper.displayPopUpAlert(c_txtErrIncompatiblesOptions);
				return true;
			}
		}
		return false;
	}

	/**
	 * Permet de passer une commande de type {@link SelectionDispatchAction} à
	 * l'IDE. Cela permet de bénéficier de l'ensemble des commandes existantes de
	 * l'IDE (Eclipse) qui sont accessibles soit pas menu soit par séquence de
	 * touche. Cette commande ne doit impacter que les sous-projets de type Java.
	 * <p>
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
	 * Retourne la fenêtre de travail active. Si aucune fenêtre n'est active,
	 * retourne la première fenêtre trouvée dans la liste des fenêtres disponibles.
	 * 
	 * @return la fenêtre de travail courant.
	 */
	private IWorkbenchWindow getWorkbenchWindow() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (hasView())
			return workbench.getActiveWorkbenchWindow();
		if (workbench.getWorkbenchWindowCount() > 0)
			return workbench.getWorkbenchWindows()[0];
		return null;
	}

	/**
	 * Retourne l'interface principale ...
	 * 
	 * @return The target site.
	 */
	private IWorkbenchPartSite getTargetSite() {
		if (null != getWorkbenchWindow())
			return getWorkbenchWindow().getPartService().getActivePart().getSite();
		throw new RuntimeException("xxx");
	}
}
