package fr.pacman.core.validation.ui.generators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.Logger;

import fr.pacman.core.generator.PacmanGenerator;
import fr.pacman.core.generator.PacmanValidatorsReport;
import fr.pacman.core.ui.generator.PacmanUIGenerator;
import fr.pacman.core.ui.generator.PacmanUIGeneratorHelper;
import fr.pacman.core.validation.main.GenValidation;
import fr.pacman.core.validation.ui.plugin.Activator;

/**
 * UI Generateur pour la validation des models.
 * <p>
 * Se reporter à la classe {@link PacmanUIGenerator} pour l'explication des
 * différentes méthodes.
 * 
 * @author MINARM
 */
public class ValidationUIGenerators extends PacmanUIGenerator {

	/**
	 * Constructeur.
	 * 
	 * @param p_selected la ressource sélectionnée (ici obligatoirement un fichier).
	 */
	public ValidationUIGenerators(final IFile p_selected) {
		super(p_selected);
	}

	@Override
	protected List<PacmanGenerator> getGenerators() {
		final List<PacmanGenerator> v_generators = new ArrayList<>();
		v_generators.add(new GenValidation());
		return v_generators;
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

	/**
	 * Pour ce générateur, traitement particulier post génération.
	 */
	@Override
	protected boolean hasPostTreatments() {

		if (PacmanValidatorsReport.hasReport())
			PacmanUIGeneratorHelper.displayPopUpAlert("Le rapport a remonté des erreurs de validation, "
					+ "\n consultez le fichier de log ([nom de l'application]-validation.log) "
					+ " au niveau du projet de modélisation.");
		else
			PacmanUIGeneratorHelper.displayPopUpInfo("Le fichier de modélisation est valide.");
		return false;
	}

	@Override
	protected boolean hasView() {
		return false;
	}
}
