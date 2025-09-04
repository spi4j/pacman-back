package fr.pacman.back.start.ui.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.dialect.command.CreateRepresentationCommand;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.business.internal.session.danalysis.SaveSessionJob;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.ui.business.api.session.IEditingSession;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;
import org.eclipse.sirius.ui.business.api.viewpoint.ViewpointSelectionCallback;
import org.eclipse.sirius.ui.tools.api.project.ModelingProjectManager;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.ui.IEditorPart;
import org.obeonetwork.dsl.entity.EntityFactory;
import org.obeonetwork.dsl.entity.Root;
import org.obeonetwork.dsl.requirement.Repository;
import org.obeonetwork.dsl.requirement.RequirementFactory;
import org.obeonetwork.dsl.soa.SoaFactory;
import org.obeonetwork.dsl.soa.System;

import fr.pacman.back.start.ui.exception.PacmanInitModelException;

/**
 * Classe utilitaire pour tout ce qui concerne la création d'un projet de
 * modélisation, de ses différents fichiers de modélisation, de leur
 * représentations associées ainsi que les 'viewpoints'.
 * <p>
 * On limite au maximum l'utilisation de constantes quite à faire parfois de la
 * répétition car ce code ne devrait plus jamais être modifié. Voir :
 * {@link AbstractISNewModelWizard}
 * <p>
 * Attention bien notter que les viewpoints sont écrits en dur au niveau du
 * studio, il ne faut donc jamais changer les libellés, sous peine d'engendrer
 * un erreur comme quoi le viewpoi,t n'a pas été trouvé.
 * 
 * @author MINARM
 */
@SuppressWarnings("restriction")
public class SiriusUtil {

	static Map<String, SiriusModelDescriptor> _models;
	static final List<String> _openedRepresentations;

	/**
	 * Initialisation des représentations disponibles.
	 */
	// ISD consolidated view
	static {
		_openedRepresentations = new ArrayList<String>();
		_openedRepresentations.add("Entities Namespaces Hierarchy");
		_openedRepresentations.add("DTO Namespaces Hierarchy");

		_models = new HashMap<String, SiriusModelDescriptor>();
		_models.put("entity",
				new SiriusModelDescriptor(".entity",
						Arrays.asList("Entities Namespaces Hierarchy", "EV_Entities_PhysicalNames"),
						Arrays.asList("org.obeonetwork.dsl.entity.design/Entity Views",
								"org.obeonetwork.is.design/Entity (ISD consolidated view)",
								"org.obeonetwork.dsl.environment.properties/Environment Views")));

		_models.put("requirement",
				new SiriusModelDescriptor(".requirement", Arrays.asList("Requirements Table"),
						Arrays.asList("org.obeonetwork.dsl.environment.properties/Environment Views",
								"org.obeonetwork.dsl.requirement.design/Requirements",
								"org.obeonetwork.graal.design/Requirements (Graal consolidated view)")));

		_models.put("soa",
				new SiriusModelDescriptor(".soa",
						Arrays.asList("SOA Diagram", "DTO Namespaces Hierarchy", "EV_DTO_PhysicalNames"),
						Arrays.asList("org.obeonetwork.is.design/SOA (ISD consolidated view)",
								"org.obeonetwork.dsl.soa.design/SOA Views",
								"org.obeonetwork.dsl.environment.properties/Environment Views")));
	}

	/**
	 * Constructeur privé.
	 */
	private SiriusUtil() {
		super();
	}

	/**
	 * Ajout de la nature EMF au projet avec le fichier '.aird'. Activation des
	 * 'view points'et création des fichiers de modélisation ainsi que des
	 * représentations associées. On ajoute la nature Modeling (creation du
	 * 'representations.aird'), on récuère la session pour le nouveau projet et on
	 * crée les ressources de modelisation.
	 *
	 * @param p_monitor         l'objet de monitoring.
	 * @param p_project         l'objet contenant le projet de modélisation.
	 * @param p_applicationName le nom de l'application.
	 * @param p_lstModelCodes   la liste des codes pour les fichiers de modélisation
	 *                          a générer.
	 * @throws CoreException
	 * @throws PacmanInitModelException
	 */
	public static void addModelingResources(final SubMonitor p_monitor, final IProject p_project,
			final String p_applicationName, final List<String> p_lstModelCodes)
			throws CoreException, PacmanInitModelException {
		if (!p_project.exists())
			throw new PacmanInitModelException(
					"Le projet n'a pas été trouvé pour l'ajout des ressources de modélisation !");
		ModelingProjectManager.INSTANCE.convertToModelingProject(p_project, p_monitor.newChild(100));
		final Session v_session = SessionManager.INSTANCE.getSession(getMainRepresentationsUri(p_project), p_monitor);
		if (null == v_session)
			throw new PacmanInitModelException(
					"Aucune session n'a pu être récupérée pour l'ajout des ressources de modélisation !");
		createModelingResources(v_session, p_lstModelCodes, p_project, p_applicationName, p_monitor);
	}

	/**
	 * Creation des différentes ressources de modélisation pour le projet en
	 * fonction des ressources demandées par le développeur lors de la création du
	 * projet.
	 * <p>
	 * L'ouverture des représentations est effectuée dans un deuxieme temps, cela
	 * est uniquement lié à une problématique visuelle lors de la création de
	 * multiples ressources. Si un éditeur est ouvert, il se met à 'clignoter' lors
	 * de la création de nouvelles ressources. On ouvre donc les représentations
	 * uniquement lorsque toutes les ressources sont créées afin d'améliorer le
	 * 'confort visuel' pour l'utilisateur.
	 *
	 * @param p_session         la session Sirius.
	 * @param p_lstModelCodes   la liste des codes pour les fichiers de
	 *                          modélisation.
	 * @param p_applicationName le nom de l'application.
	 * @param p_monitor         l'objet de monitoring.
	 */
	private static void createModelingResources(final Session p_session, final List<String> p_lstModelCodes,
			final IProject p_project, final String p_applicationName, final SubMonitor p_monitor)
			throws PacmanInitModelException {
		Map<String, DRepresentation> v_lstCreatedRepresentations = new HashMap<String, DRepresentation>();
		for (String v_code : p_lstModelCodes) {
			SiriusModelDescriptor v_modelHelper = _models.get(v_code);
			EObject v_eObject = getEObjectForModeling(p_session, v_code, p_applicationName, p_project);
			createModelingFile(p_session, getModelFileUri(p_project, p_applicationName, v_modelHelper), v_modelHelper,
					v_eObject, p_monitor);
			activateViewPoints(p_session, v_modelHelper, p_monitor);
			createRepresentations(p_session, v_modelHelper, v_eObject, p_monitor, p_applicationName,
					v_lstCreatedRepresentations);
		}
		openRepresentations(p_session, p_monitor, v_lstCreatedRepresentations);
	}

	/**
	 * Récupération des paramètres de construction pour les fichiers de modélisation
	 * du projet. (On fonctionne avec un 'if' très basique pour l'instant, pas
	 * nécessaire de mettre en place un pattern plus évolué pour cette
	 * fonctionnalité 'mineure' dans Pacman...).
	 *
	 * @param p_session         la session Sirius.
	 * @param p_code            le code pour le fichier de modélisation à creer.
	 * @param p_applicationName le nom général de l'application.
	 * @param p_project         le projet.
	 * @return les paramètres de construction.
	 * @throws PacmanInitModelException
	 */
	private static EObject getEObjectForModeling(final Session p_session, final String p_code,
			final String p_applicationName, final IProject p_project) throws PacmanInitModelException {
		EObject v_EObject = null;
		if ("entity".equals(p_code)) {
			v_EObject = EntityFactory.eINSTANCE.createRoot();
			((Root) v_EObject).setCreatedOn(new Date());
		}
		if ("requirement".equals(p_code)) {
			v_EObject = RequirementFactory.eINSTANCE.createRepository();
			((Repository) v_EObject).setName(p_project.getName());
		}
		if ("soa".equals(p_code)) {
			v_EObject = SoaFactory.eINSTANCE.createSystem();
			((System) v_EObject).setCreatedOn(new Date());
		}
		if (null == v_EObject)
			throw new PacmanInitModelException("Aucun objet EObject pour le code : " + p_code);
		return v_EObject;
	}

	/**
	 * Creation de l'URI pour le fichier de modélisation.
	 *
	 * @param p_project         le projet en cours.
	 * @param p_applicationName le nom de l'application.
	 * @param p_modelHelper     le conteneur d'informations pour le fichier de
	 *                          modélisation en cours.
	 * @return l'URI du fichier de modélisation.
	 */
	private static URI getModelFileUri(final IProject p_project, final String p_applicationName,
			final SiriusModelDescriptor p_modelHelper) {
		StringBuffer v_strUri = new StringBuffer(File.separator);
		v_strUri.append(p_project.getName()).append(File.separator);
		v_strUri.append(p_applicationName + p_modelHelper.get_modelExt());
		return URI.createPlatformResourceURI(v_strUri.toString(), Boolean.TRUE);
	}

	/**
	 * Creation de l'URI pour le fichier de représentations (.aird).
	 *
	 * @param p_project le projet en cours.
	 * @return l'URI du fichier de representation.
	 */
	private static URI getMainRepresentationsUri(final IProject p_project) {
		StringBuffer v_strUri = new StringBuffer(File.separator);
		v_strUri.append(p_project.getName()).append(File.separator);
		v_strUri.append("representations.aird");
		return URI.createPlatformResourceURI(v_strUri.toString(), Boolean.TRUE);
	}

	/**
	 * Creation de l'URI pour un viewPoint.
	 *
	 * @param p_strViewPointUri une partie de l'URI en chaine de caracteres.
	 * @return l'URI complete.
	 */
	private static URI getViewPointURI(final String p_strViewPointUri) {
		return URI.createURI("viewpoint:/" + p_strViewPointUri);
	}

	/**
	 * Activation des 'viewpoints' en fonction du fichier de modélisation qui a été
	 * demandé. On parcourt la liste des URIs récupérées et on active les
	 * 'viewpoints' concernés pour le modèle.
	 *
	 * @param p_session     la session Sirius.
	 * @param p_modelHelper le conteneur d'informations pour le fichier de
	 *                      modelisation en cours.
	 * @param p_monitor     l'objet de monitoring.
	 */
	private static void activateViewPoints(final Session p_session, final SiriusModelDescriptor p_modelHelper,
			final SubMonitor p_monitor) throws PacmanInitModelException {
		for (String v_viewPointURI : p_modelHelper.get_viewURIs()) {
			activateViewPoint(p_session, getViewPointURI(v_viewPointURI), p_monitor);
		}
	}

	/**
	 * Activation des différents 'viewpoints' en fonction du choix utilisateur.
	 *
	 * @param p_session      la session Sirius.
	 * @param p_viewpointURI l'URI d'un viewpoint dans ceux disponibles dans la
	 *                       registry.
	 * @param p_monitor      l'objet de monitoring.
	 */
	private static void activateViewPoint(final Session p_session, final URI p_viewpointURI, final SubMonitor p_monitor)
			throws PacmanInitModelException {
		final Viewpoint v_viewpoint = ViewpointRegistry.getInstance().getViewpoint(p_viewpointURI);
		if (null == v_viewpoint)
			throw new PacmanInitModelException("Impossible de récupérer le viewPoint : " + p_viewpointURI);
		p_session.getTransactionalEditingDomain().getCommandStack()
				.execute(new RecordingCommand(p_session.getTransactionalEditingDomain()) {
					@Override
					protected void doExecute() {
						final ViewpointSelectionCallback selection = new ViewpointSelectionCallback();
						selection.selectViewpoint(v_viewpoint, p_session, p_monitor);
					}
				});
	}

	/**
	 * Creation des fichiers de modélisation en fonction du choix utilisateur.
	 *
	 * @param p_session     la session Sirius.
	 * @param p_modelhelper le conteneur d'informations pour le fichier de
	 *                      modélisation en cours.
	 * @param p_eObject     l'objet racine pour la ressource.
	 * @param p_monitor     l'objet de monitoring.
	 * @return la resource qui a été créée.
	 */
	private static Resource createModelingFile(final Session p_session, final URI p_uri,
			final SiriusModelDescriptor p_modelhelper, final EObject p_eObject, final SubMonitor p_monitor) {
		final Resource v_resource = p_session.getTransactionalEditingDomain().getResourceSet().createResource(p_uri);
		p_session.getTransactionalEditingDomain().getCommandStack()
				.execute(new RecordingCommand(p_session.getTransactionalEditingDomain()) {
					@Override
					protected void doExecute() {
						v_resource.getContents().add(p_eObject);
						p_session.addSemanticResource(p_uri, p_monitor);
					}
				});
		p_session.save(p_monitor);
		return v_resource;
	}

	/**
	 * Création des représentations associées à un fichier de modélisation.
	 * <p>
	 * On teste si on a bien récupéré l'ensemble des descriptions pour les //
	 * représentations, on récupère la description pour la représentation et on la
	 * créée.
	 *
	 * @param p_session                   la session Sirius.
	 * @param p_modelHelper               le conteneur d'informations pour le
	 *                                    fichier de modélisation en cours.
	 * @param p_eObject                   l'objet racine pour la resource.
	 * @param p_monitor                   l'objet de monitoring.
	 * @param p_applicationName           le nom du projet.
	 * @param p_lstCreatedRepresentations la liste de représentations créées.
	 */
	private static void createRepresentations(final Session p_session, final SiriusModelDescriptor p_modelHelper,
			final EObject p_eObject, final SubMonitor p_monitor, String p_applicationName,
			Map<String, DRepresentation> p_lstCreatedRepresentations) throws PacmanInitModelException {
		if (null == p_modelHelper.get_descIDs() || p_modelHelper.get_descIDs().isEmpty())
			throw new PacmanInitModelException("Impossible de recuperer les ids de description !");

		for (String v_descID : p_modelHelper.get_descIDs()) {
			RepresentationDescription v_desc = getRepresentationDescription(p_session, p_eObject, v_descID);

			if (null == v_desc)
				throw new PacmanInitModelException("Impossible de trouver la description : " + v_descID);
			DRepresentation v_newRepresentation = createRepresentation(p_session, v_desc,
					getRepresentationName(p_applicationName + " - " + v_descID), p_eObject, p_monitor);
			if (null != v_newRepresentation)
				p_lstCreatedRepresentations.put(v_descID, v_newRepresentation);
		}
	}

	/**
	 * Récupération du fix du [08/10/20] à partir du code ISD pour le namespace qui
	 * n'apparait pas dans le diagramme (de manière aléatoire).
	 */
	private static void ensureNoCDOSaveInProgress() {
		// Ensure that there is no save in progress.
		// Otherwise, when the representation will be added to the resource
		// (createRepresentation-->CreateRepresentationCommand) can be problematic.
		// Indeed, during the save, at a specific time
		// (ResourceSaveDiagnose.hasDifferentSerialization),
		// the eSetDeliver is disabled. So in this condition, no adapter is added to the
		// added representation.
		try {
			Job.getJobManager().join(SaveSessionJob.FAMILY, new NullProgressMonitor());
		} catch (OperationCanceledException | InterruptedException e) {
			// Ignore these exceptions. The join is just here to avoid to have a save in
			// progress.
		}
	}

	/**
	 * Vide les identifiants de "EV_" (si besoin) afin de créer un nom plus lisible
	 * pour la représentation (l'utilisateur pourra toujours modifier le nom plus
	 * tard).
	 *
	 * @param p_name le nom de la representation.
	 * @return le nom modifié
	 */
	private static String getRepresentationName(final String p_name) {
		if (p_name.indexOf("EV_") == -1)
			return p_name;
		String v_name = p_name.replace("EV_", "");
		v_name = v_name.replaceAll("_", " ");
		return v_name;
	}

	/**
	 * Récupère la description (et donc le meta-model) de la représentation.
	 *
	 * @param p_session   la session Sirius.
	 * @param p_object    l'objet sur lequel porte la representation.
	 * @param p_repDescID l'identifiant de la representation.
	 * @return l'objet de description pour la representation.
	 */
	private static RepresentationDescription getRepresentationDescription(final Session p_session,
			final EObject p_object, final String p_repDescID) {
		for (final RepresentationDescription v_representation : DialectManager.INSTANCE
				.getAvailableRepresentationDescriptions(p_session.getSelectedViewpoints(Boolean.FALSE), p_object)) {
			if (p_repDescID.equals(v_representation.getName())) {
				return v_representation;
			}
		}
		return null;
	}

	/**
	 * Envoi la commande de création pour la représentation. 'Fix' de la fiche
	 * SAFRAN-1047 + 'Fix' pour le message aléatoire de session nulle mais qui crée
	 * toutefois les représentation (pas d'impact sur le fonctionnement voulu de
	 * l'application à partir du momment ou l'exception est en catch).
	 * 
	 * @param p_session     la session Sirius.
	 * @param p_description la description (avec le meta-model).
	 * @param p_name        le nom a afficher pour la representation.
	 * @param p_object      l'objet auquel est associe la representation.
	 * @param p_monitor     l'objet de monitoring.
	 * @return la représentation qui vient d'être créée.
	 */
	private static DRepresentation createRepresentation(final Session p_session,
			final RepresentationDescription p_description, final String p_name, final EObject p_object,
			final IProgressMonitor p_monitor) {
		DRepresentation v_representation = null;
		try {
			ensureNoCDOSaveInProgress();
			CreateRepresentationCommand v_cmd = new CreateRepresentationCommand(p_session, p_description, p_object,
					p_name, p_monitor);
			p_session.getTransactionalEditingDomain().getCommandStack().execute(v_cmd);
			v_representation = v_cmd.getCreatedRepresentation();
		} catch (RuntimeException p_e) {
			// RAS.
		}
		return v_representation;
	}

	/**
	 * Demande l'ouverture de toutes les représentations qui sont dans la liste des
	 * représentations à ouvrir et qui ont été créées en fonction des fichiers de
	 * modélisation demandés par le développeur.
	 *
	 * @param p_session            la session Sirius.
	 * @param p_monitor            l'objet de monitoring
	 * @param p_lstRepresentations la map des représentations qui ont été créées.
	 */
	private static void openRepresentations(final Session p_session, final IProgressMonitor p_monitor,
			final Map<String, DRepresentation> p_lstRepresentations) {
		for (Entry<String, DRepresentation> v_entry : p_lstRepresentations.entrySet()) {
			if (_openedRepresentations.contains(v_entry.getKey())) {
				openRepresentation(p_session, (DRepresentation) v_entry.getValue(), p_monitor);
			}
		}
	}

	/**
	 * Commande d'ouverture de la représentation.
	 *
	 * @param p_session        la session Sirius.
	 * @param p_representation la representation
	 * @param p_monitor        l'objet de monitoring
	 */
	private static void openRepresentation(final Session p_session, final DRepresentation p_representation,
			final IProgressMonitor p_monitor) {
		final IEditingSession v_editingEdition = SessionUIManager.INSTANCE.getUISession(p_session);
		final IEditorPart v_editorPart = DialectUIManager.INSTANCE.openEditor(p_session, p_representation, p_monitor);
		if (v_editorPart != null && v_editingEdition != null)
			v_editingEdition.attachEditor((DialectEditor) v_editorPart);
	}

	/**
	 * Classe utilitaire interne pour le stockage des informations dans le cadre de
	 * la génération des modélisations, des 'viewpoints' et des représentations pour
	 * Sirius.
	 * 
	 * @author MINARM
	 */
	static class SiriusModelDescriptor {

		String _modelExt;
		List<String> _viewpointURIs;
		List<String> _descIDs;

		SiriusModelDescriptor(final String p_modelExt, List<String> p_lstDescIDs, final List<String> p_lstViewURIs) {
			_modelExt = p_modelExt;
			_descIDs = p_lstDescIDs;
			_viewpointURIs = p_lstViewURIs;
		}

		public List<String> get_viewURIs() {
			return _viewpointURIs;
		}

		public String get_modelExt() {
			return _modelExt;
		}

		public List<String> get_descIDs() {
			return _descIDs;
		}
	}
}
