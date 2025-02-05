package fr.pacman.start.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import fr.pacman.config.main.GenCommon;
import fr.pacman.config.main.GenModel;
import fr.pacman.config.main.GenRoot;
import fr.pacman.config.main.GenServer;
import fr.pacman.config.main.GenWebapp;
import fr.pacman.core.generator.PacmanGeneratorStart;
import fr.pacman.core.property.PropertiesHandler;
import fr.pacman.core.property.project.ProjectProperties;
import fr.pacman.start.main.GenStart;
import fr.pacman.start.ui.activator.Activator;
import fr.pacman.start.ui.exception.PacmanInitModelException;
import fr.pacman.start.ui.util.SiriusUtil;
import fr.pacman.start.ui.util.WizardUtil;
import fr.pacman.start.ui.util.WizardUtil.IParametrizedExternalWizard;

/**
 * Wizard pour la création d'un projet de type Cali par le biais du
 * "new->project menu".
 * 
 * @author MINARM.
 */
public class GenerateStartWizard extends Wizard implements INewWizard {

	/**
	 * La page de configuration des différentes options de création pour le projet.
	 */
	private PropertiesWizardStartPage _pageOne;

	/**
	 * La version de spi4J à utiliser.
	 */
	private static final String c_spi4JVersion = "4.6.0";

	@Override
	public void init(IWorkbench p_workbench, IStructuredSelection p_selection) {
		setWindowTitle("Pacman : générateur de code JAVA [version " + c_spi4JVersion + "]");
	}

	/**
	 * Lancement du job de création pour la structure d'un projet de type 'Cali',
	 * une fois l'ensemble des différents paramètres structurants saisis par le
	 * développeur.
	 */
	@Override
	public boolean performFinish() {

		final Map<String, String> startProperties = initProperties();

		Job job = new Job("Creation du projet Cali") {

			@Override
			public IStatus run(IProgressMonitor p_monitor) {
				IProject project = null;
				SubMonitor subMonitor = SubMonitor.convert(p_monitor, 100);
				try {
					subMonitor.setTaskName("Création du projet Cali");
					project = createProject(subMonitor, startProperties);

					subMonitor.setTaskName("Mise à jour du projet vers la norme Cali");
					upgradeProjectWithCali(subMonitor, project, startProperties);

					subMonitor.setTaskName("Ajout de la nature Maven au projet");
					addMavenNatureToProject(subMonitor, project);

					subMonitor.setTaskName("Configuration des sous projets");
					configureSubProjectsWithMaven(project);

					subMonitor.setTaskName("Rafraîchissement des sous projets");
					WizardUtil.refreshProject(subMonitor, project);

					subMonitor.setTaskName("Ajout de la nature EMF au projet de modélisation");
					addEMFNatureToProjectModel(subMonitor, project, 0);

					subMonitor.setTaskName("Finalisation des sous projets");
					updateIDEAfterCodeGeneration(subMonitor, project);
				} catch (Exception p_e) {
					return WizardUtil.sendErrorStatus(p_e, Activator.c_pluginId);
				} finally {
					try {
						WizardUtil.refreshAndSaveProject(subMonitor, project);
					} catch (CoreException e) {
						return WizardUtil.sendErrorStatus(e, Activator.c_pluginId);
					}
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
		return true;
	}

	/**
	 * 
	 * Retourne les informations du formulaire (pour l'instant unique page du
	 * Wizard). Cette méthode ne devrait lancer aucune exception.
	 * 
	 * @return properties
	 */
	private Map<String, String> initProperties() {

		Map<String, String> properties = new HashMap<String, String>();
		properties.put(ProjectProperties.c_project_name, _pageOne.getProjectName());
//		properties.put(GenerateStart.c_PROP_PACKAGE_NAME, _pageOne.getPackageName());
//		properties.put(GenerateStart.c_PROP_NAMING_TYPE, _pageOne.getNamingCode());
//		properties.put(GenerateStart.c_PROP_CLIENT_TYPE, _pageOne.getIhm());
//		properties.put(GenerateStart.c_DATEBASE_TYPES, _pageOne.getDataBasesNames());
//		properties.put(GenerateStart.c_PROP_EJB_SRV, _pageOne.getSrvEjb());
//		properties.put(GenerateStart.c_PROP_REQ_SRV, _pageOne.getSrvReq());
//		properties.put(GenerateStart.c_PROP_CR_FORMAT, _pageOne.getTypeRC());
//		properties.put(GenerateStart.c_PROP_SRV_CRUD_TU, _pageOne.getUnitTests());
//		properties.put(GenerateStart.c_PROP_GEN_WS, _pageOne.getSrvWS());
//		properties.put(GenerateStart.c_PROP_GEN_WMS, _pageOne.getSrvWMS());
//		properties.put(GenerateStart.c_PROP_GEN_MATCHING, _pageOne.getGenerateMatching());
//		properties.put(GenerateStart.c_PROP_USE_CONFIG, _pageOne.getUseConfig());
//		properties.put(GenerateStart.c_PROP_GEN_BDD, _pageOne.getGenBDD());
//		properties.put(GenerateStart.c_PROP_IS_LIBRARY, _pageOne.getIsLibrary());
//		properties.put(GenerateStart.c_PROP_IS_LIBRARY_RS, _pageOne.getIsLibraryRs());
//		properties.put(GenerateStart.c_PROP_LIB_ADD_JAR, _pageOne.getAddLibraries());
//		properties.put(GenerateStart.c_PROP_JAVA_VERSION, _pageOne.getJavaVersion());
//		properties.put(GenerateStart.c_PROP_USE_SECURITY, _pageOne.getUseSecurity());
//		properties.put(GenerateStart.c_PROP_SQL_TABLE_PREFIX, _pageOne.getSqlTablePrefix());
//		properties.put(GenerateStart.c_PROP_SQL_TABLE_SCHEMA, _pageOne.getSqlTableSchema());
//		properties.put(GenerateStart.c_PROP_SQL_TABLESPACE, _pageOne.getSqlTableSpace());
//		properties.put(GenerateStart.c_PROP_SQL_ADD_COLUMNS, _pageOne.getSqlAddColumns());
//		properties.put(GenerateStart.c_PROP_FETCHING_STRATEGY, _pageOne.getFetchingStrategy());
//		properties.put(GenerateStart.c_PROP_REQUIREMENT_LEVEL, _pageOne.getRequirementLevel());
//		properties.put(GenerateStart.c_PROP_REQUIREMENT_PREFIX, _pageOne.getRequirementPrefix());
//		properties.put(GenerateStart.c_PROP_REQUIREMENT_VERSION, _pageOne.getRequirementVersion());
//		properties.put(GenerateStart.c_PROP_HTTP_EMBEDDED_SERVER, _pageOne.getHttpEmbeddedServer());
//		properties.put(GenerateStart.c_PROP_H2_EMBEDDED, _pageOne.getH2EmbeddedDatabase());
//		properties.put(GenerateStart.c_PROP_AUTHOR_NAME, _pageOne.getAuthorName());
//		properties.put(GenerateStart.c_PROP_MODE_DEBUG, _pageOne.getModeDebug());
//		properties.put(GenerateStart.c_PROP_PROJECT_VERSION, "0.0.1-SNAPSHOT");
//		properties.put(GenerateStart.c_PROP_SPI4J_VERSION, c_spi4JVersion);
//		properties.put(GenerateStart.c_PROP_LOG4J, _pageOne.getLog4j());
//		properties.put(GenerateStart.c_PROP_APP_CRUD, _pageOne.getCrud());
//		properties.put(GenerateStart.c_PROP_INJECT_HK2, _pageOne.getHk2());
//		// On rajoute les proprietes additionnelles si elles existent.
//		properties.putAll(_pageOne.getSqlAddColumnsDetail());
//		properties.putAll(_pageOne.getAddLibrariesDetail());
		return properties;
	}

	/**
	 * Création du projet cible Java.
	 * 
	 * @param p_monitor         l'objet de monitoring pour contrôler les fichiers
	 *                          créés sous l'IDE.
	 * @param p_startProperties le tableau des propriétés pour le nouveu projet.
	 * @return le nouveau projet
	 * @throws CoreException une exception éventuellement levée lors de l'exécution
	 *                       du traitement
	 */
	private IProject createProject(final SubMonitor p_monitor, final Map<String, String> p_startProperties)
			throws CoreException {
		final String p_appliName = _pageOne.getProjectName();
		if (null == p_appliName || p_appliName.isEmpty())
			throw new NullPointerException("Le nom du projet n'est pas renseigné.");
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(p_appliName);
		if (!project.exists()) {
			project.create(p_monitor.newChild(50));
			project.open(p_monitor.newChild(50));
		}
		return project;
	}

	/**
	 * Ajout de l'ensemble de la configuration pour un projet de type Cali.
	 * 
	 * @param p_monitor
	 * @param p_project
	 * @param p_properties
	 * @throws CoreException
	 */
	private void upgradeProjectWithCali(final SubMonitor p_monitor, IProject p_project,
			Map<String, String> p_properties) throws CoreException {

		try {
			File file = new File((p_project).getLocation().toString());
			final String modelPath = file.getAbsolutePath() + File.separator + _pageOne.getProjectName() + "-"
					+ ProjectProperties.get_suffixModel(null);
			
			PropertiesHandler.init(modelPath, p_properties);
			Monitor monitor = new BasicMonitor();
			// PacmanUIGeneratorsReport.reset();
			PacmanGeneratorStart generator = new GenStart();
			generator.setModelFile(file);
			generator.generate(monitor);

			generator = new GenModel();
			generator.setModelFile(file);
			generator.generate(monitor);

			generator = new GenCommon();
			generator.setModelFile(file);
			generator.generate(monitor);

			generator = new GenServer();
			generator.setModelFile(file);
			generator.generate(monitor);

			generator = new GenWebapp();
			generator.setModelFile(file);
			generator.generate(monitor);

			generator = new GenRoot();
			generator.setModelFile(file);
			generator.generate(monitor);

		} catch (IOException e) {

			throw new CoreException(WizardUtil.sendErrorStatus(e, Activator.c_pluginId));

		} finally {

			// PacmanUIGeneratorsReport.log(true);
			try {
				PropertiesHandler.exit();

			} catch (IOException e) {

				throw new CoreException(WizardUtil.sendErrorStatus(e, Activator.c_pluginId));
			}
		}
	}

	/**
	 * Ajout de la nature "Maven" au projet principal (projet parent).
	 * 
	 * @param p_monitor
	 * @param p_project
	 * @throws CoreException
	 */
	private void addMavenNatureToProject(final SubMonitor p_monitor, final IProject p_project) throws CoreException {

		IProjectDescription description;
		description = p_project.getDescription();
		String[] natures = description.getNatureIds();
		String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
		newNatures[natures.length] = "org.eclipse.m2e.core.maven2Nature";

		// Validation des natures.
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IStatus status = workspace.validateNatureSet(newNatures);

		// On applique la nature uniquement si la validation est OK.
		if (status.getCode() != IStatus.OK)
			throw new CoreException(status);

		description.setNatureIds(newNatures);
		p_project.setDescription(description, p_monitor.newChild(100));
	}

	/**
	 * 
	 * @throws CoreException
	 */
	private void configureSubProjectsWithMaven(final IProject p_project) throws CoreException {

		final String workspace = p_project.getWorkspace().getRoot().getLocation().toPortableString();

		final int result = WizardUtil.executeExternalWizard(new IParametrizedExternalWizard() {

			@Override
			public void initExternalWizard(IWizard p_wizard) throws Exception {

				Field wizardField = p_wizard.getClass().getDeclaredField("locations");
				wizardField.setAccessible(Boolean.TRUE);
				wizardField.set(p_wizard, Collections.singletonList(workspace));
			}

			@Override
			public String getWizardId() {

				return "org.eclipse.m2e.core.wizards.Maven2ImportWizard";
			}
		});

		if (WizardUtil.c_codeKoExists == result)
			WizardUtil.displayMessageInDialog("ATTENTION",
					"Le plugin Maven d'import de projet n'est pas installé. Ce plugin est nécessaire pour l'utilisation de Pacman.");

		if (WizardUtil.c_codeKo == result)
			WizardUtil.openExternalWizard("org.eclipse.m2e.core.wizards.Maven2ImportWizard", "Pacman start");
	}

	/**
	 * Ajout de la nature EMF au projet de modelisation. Dans quelques rares cas, il
	 * est possible que le systeme n'ait pas encore eu le temps de finaliser
	 * l'écriture des differents projets sur le disque (dépend de la charge
	 * processeur,etc...).
	 * 
	 * On attend donc jusqu'a ce que le projet de modelisation ait été complété puis
	 * on lance l'ajout des données de modélisation. Si cela prend trop de temps, on
	 * stoppe et on affiche un message à l'utilisateur pour finaliser à la main la
	 * configuration du projet de modelisation. Utilisation de seéaphore ?
	 *
	 * @param p_monitor
	 * @param p_project
	 * @throws CoreException
	 * @throws InterruptedException
	 * @throws PacmanInitModelException
	 */
	private void addEMFNatureToProjectModel(final SubMonitor p_monitor, final IProject p_project, int p_cpt)
			throws CoreException, InterruptedException, PacmanInitModelException {

		if (p_cpt == 5) {
			throw new PacmanInitModelException("Impossible de finaliser la configuration du projet de modélisation."
					+ System.getProperty("line.separator") + "Veuillez terminer manuellement la configuration ");
		}

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();
		String projectModelName = p_project.getName() + "-model";
		IProject project = root.getProject(projectModelName);

		// On attend que l'ecriture du projet ait été finalisée sur le disque.
		File pom = new File(project.getLocation() + File.separator + "pom.xml");

		// Boucle tant que le fichier n'est pas trouvé.
		if (!pom.isFile()) {
			p_monitor.setTaskName("Ajout de la nature EMF au projet de modélisation  - Tentative : " + p_cpt + "/6");
			Thread.sleep(10000);
			addEMFNatureToProjectModel(p_monitor, p_project, p_cpt + 1);
		}

		// Ajout de toutes les donnees de modelisation.
		SiriusUtil.addModelingResources(p_monitor, project, _pageOne.getProjectName(), _pageOne.getModelCodes());
	}

	/**
	 * Cette methode est un "pis aller" pour récupérer la couche ui générique des
	 * générateurs et bénéficier ainsi de l'organisation automatique des imports
	 * ainsi que du formattage de l'IDE (factorisation du code). Pour ce faire on
	 * utilise une classe fille {@link GenerateStartUIGenerators} dont l'unique but
	 * est de fournir la liste des sous-projets. Ceci est a rapprocher du generateur
	 * UI de configuration {@link ConfigurationUIGenerators}
	 * 
	 * @param p_monitor
	 * @param p_project
	 * @throws CoreException
	 */
	private void updateIDEAfterCodeGeneration(final SubMonitor p_monitor, final IProject p_project)
			throws CoreException {
		// new GenerateStartUIGenerators(p_project).updateIDEAfterCodeGeneration();
	}

	@Override
	public void addPages() {
		_pageOne = new PropertiesWizardStartPage();
		addPage(_pageOne);
	}
}
