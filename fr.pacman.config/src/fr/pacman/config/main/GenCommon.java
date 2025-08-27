package fr.pacman.config.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.acceleo.aql.AcceleoUtil;
import org.eclipse.acceleo.query.ast.TypeLiteral;
import org.eclipse.acceleo.query.runtime.namespace.IQualifiedNameQueryEnvironment;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.obeonetwork.dsl.overview.impl.OverviewFactoryImpl;

import fr.pacman.core.generator.PacmanGenerator;
import fr.pacman.core.generator.PacmanGeneratorStart;
import fr.pacman.core.property.project.ProjectProperties;

/**
 * Générateur pour la couche de configuration.
 * 
 * Ce générateur est aussi susceptible d'être lancé directement par le plugin
 * fr.pacman.start, dans ce cas, il n'existe aucune ressource prééxistante pour
 * la modélisation. C'est pourquoi ce générateur hérite de la classe
 * {@link PacmanGeneratorStart} et non uniquement, comme la plupart des autres
 * générateurs, de la classe {@link PacmanGenerator}.
 * 
 * Se reporter à la classe {@link PacmanGenerator} pour l'explication des
 * différentes méthodes.
 * 
 * @author MINARM
 */
public class GenCommon extends PacmanGeneratorStart {

	@Override
	public String getSubProjectName() {
		return ProjectProperties.get_projectCommonName(null);
	}

	@Override
	public String getModuleQualifiedName() {
		
		if (ProjectProperties.isServerType()) {
			if (ProjectProperties.isSpring())
				return "fr::pacman::config::aql::genCommonSpring";
			return "fr::pacman::config::aql::genCommonSpi4j";
		}
		
		if (ProjectProperties.isClientType()) {
			if (ProjectProperties.isSpring())
				return "fr::pacman::config::aql::client::genCommonSpring";
			return "fr::pacman::config::aql::client::genCommonSpi4j";
		}
		
		throw new RuntimeException("Impossible de récupérer le type de projet pour la génération.");
	}

	@Override
	protected Map<String, String> getOptions() {
		Map<String, String> options = new LinkedHashMap<>();
		options.put(AcceleoUtil.LOG_URI_OPTION, c_defaultLogFileName);
		options.put(AcceleoUtil.NEW_LINE_OPTION, c_defaultNewLine);
		return options;
	}

	@Override
	protected List<EObject> getValues(IQualifiedNameQueryEnvironment p_queryEnvironment,
			Map<EClass, List<EObject>> p_valuesCache, TypeLiteral p_type, ResourceSet p_resourceSetForModels,
			Monitor p_monitor) {

		if (isFromStarter()) {
			final List<EObject> values = new ArrayList<>();
			values.add(OverviewFactoryImpl.init().createRoot());
			return values;
		}

		final List<EObject> values = AcceleoUtil.getValues(p_type, p_queryEnvironment,
				p_resourceSetForModels.getResources(), p_valuesCache, p_monitor);
		return values;
	}

	@Override
	protected Map<String, SelectionType_Enum> getMainTemplates() {
		Map<String, SelectionType_Enum> templates = new HashMap<>();
		templates.put("genCommonConfig", SelectionType_Enum.ROOT);
		return templates;
	}

	@Override
	public boolean doPostTreatments() {
		return true;
	}
}
