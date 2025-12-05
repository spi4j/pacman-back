package fr.pacman.back.entity.ui.generators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.Logger;

import fr.pacman.back.core.generator.PacmanGenerator;
import fr.pacman.back.core.ui.generator.PacmanUIGenerator;
import fr.pacman.back.entity.main.GenPopulation;
import fr.pacman.back.entity.ui.plugin.Activator;

public class EntityPopulationUIGenerators extends PacmanUIGenerator {

	/**
	 * Constructeur.
	 * 
	 * @param p_selected la ressource sélectionnée (ici obligatoirement un fichier).
	 */
	public EntityPopulationUIGenerators(final IFile p_selected) {
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
		return true;
	}

	@Override
	protected List<PacmanGenerator> getGenerators() {
		final List<PacmanGenerator> v_generators = new ArrayList<>();
		v_generators.add(new GenPopulation());
		return v_generators;
	}

	@Override
	protected boolean hasView() {
		return false;
	}
}
