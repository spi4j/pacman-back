package fr.pacman.soa.ui.generators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.Logger;

import fr.pacman.core.generator.PacmanGenerator;
import fr.pacman.core.ui.generator.PacmanUIGenerator;
import fr.pacman.core.ui.generator.PacmanUIGeneratorHelper;
import fr.pacman.soa.main.GenValidation;
import fr.pacman.soa.ui.plugin.Activator;

/**
 * UI Generateur pour la validation des modèles.
 * 
 * Se reporter à la classe {@link PacmanUIGenerator} pour l'explication des
 * différentes méthodes.
 * 
 * @author MINARM
 */
public class SoaValidationUIGenerators extends PacmanUIGenerator {

	/**
	 * Constructeur.
	 * 
	 * @param p_selected la ressource sélectionnée (ici obligatoirement un fichier).
	 */
	public SoaValidationUIGenerators(final IFile p_selected) {
		super(p_selected);
	}

	@Override
	protected String getPluginId() {
		return Activator.c_pluginId;
	}

	@Override
	protected Logger getLogger() {
		return Activator.getDefault().getPluginLogger();
	}

	@Override
	protected List<String> getIncompatibleOptions() {
		return null;
	}

	@Override
	protected boolean doPostTreatments() {
		PacmanUIGeneratorHelper
				.displayPopUpInfo("Le fichier de modélisation pour la couche de service est valide.");
		return false;
	}

	@Override
	protected List<PacmanGenerator> getGenerators() {
		final List<PacmanGenerator> v_generators = new ArrayList<>();
		v_generators.add(new GenValidation());
		return v_generators;
	}

	@Override
	protected boolean hasView() {
		return false;
	}
}
