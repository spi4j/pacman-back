package fr.pacman.sql.ui.generators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.Logger;

import fr.pacman.core.generator.PacmanGenerator;
import fr.pacman.core.ui.generator.PacmanUIGenerator;
import fr.pacman.core.validation.main.GenValidation;
import fr.pacman.sql.main.GenServer;
import fr.pacman.sql.ui.plugin.Activator;

/**
 * UI Generator for JDBC based on Entity model file. Il est à noter qu'il
 * n'existe pas de génération partielle au niveau de la couche SQL.
 * <p>
 * Se reporter à la classe {@link PacmanUIGenerator} pour l'explication des
 * différentes méthodes.
 * 
 * @author MINARM
 */
public class SqlUIGenerators extends PacmanUIGenerator {

	/**
	 * Constructeur.
	 * 
	 * @param p_selected la ressource sélectionnée (ici obligatoirement un fichier).
	 */
	public SqlUIGenerators(final IFile p_selected) {
		super(p_selected);
	}

	@Override
	protected List<PacmanGenerator> getGenerators() {
		final List<PacmanGenerator> v_generators = new ArrayList<>();
		v_generators.add(new GenValidation());
		v_generators.add(new GenServer());
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
		// return Arrays.asList(ProjectProperties.getIsLibraryRs());
		return null;
	}

	@Override
	protected boolean hasPostTreatments() {
		return false;
	}

	@Override
	protected boolean hasView() {
		return false;
	}
}
