package fr.pacman.back.core.ui.generator;

import java.io.IOException;

import org.eclipse.acceleo.common.preference.AcceleoPreferences;
import org.eclipse.acceleo.engine.utils.AcceleoEngineUtils;
import org.eclipse.acceleo.profiler.Profiler;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Classe permettant de surveiller les générateurs et de tracer les performances
 * lors de la génération.
 * 
 * @author MINARM
 */
public class PacmanUIAcceleoProfiler {

	private final Profiler _profiler;
	private static IProject _project;

	/**
	 * Constructeur.
	 */
	PacmanUIAcceleoProfiler() {
		_profiler = new Profiler();
		AcceleoEngineUtils.setProfiler(_profiler);
		AcceleoPreferences.switchProfiler(true);
	}

	/**
	 * Injection du projet à surveiller pour le profiler.
	 * 
	 * @param _project le projet à surveiller
	 */
	public static void set_project(IProject _project) {
		PacmanUIAcceleoProfiler._project = _project;
	}

	/**
	 * Ecriture du rapport pour la surveillance du projet.
	 * 
	 * @throws IOException toute exception éventuellement levée lors de l'exécution
	 *                     du traitement
	 */
	void write() {
		if (isProfilerAvailable()) {
			ResourceSet v_rs = _profiler.getProfileResource().getEntries().get(0).getMonitored().eResource()
					.getResourceSet();

			URI logicalURI = URI.createURI("../../../../../../../../git/pacman/");
			URI physicalURI = URI.createURI("file:/home/vrichard/git/pacman/");
			v_rs.getURIConverter().getURIMap().put(logicalURI, physicalURI);
			try {
				_profiler.save(_project.getLocation().append("profile.mtlp").toString());
			} catch (IOException e) {
				// TODO Voir en quoi on cast.....
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Vérifie si le profiler est disponible.
	 * 
	 * @return la valeur 'true' si le profiler est actif et disponible, sinon
	 *         'false'
	 */
	private boolean isProfilerAvailable() {
		return _profiler != null
				&& _profiler.getProfileResource().getEntries().get(0).getMonitored().eResource() != null;
	}
}
