package fr.pacman.start.main;

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

import fr.pacman.core.generator.PacmanGeneratorStart;

/**
 * Here no concept of subproject as the creation start at the root path for the
 * project.... Bon anglais ou français ?
 * 
 * @author MINARM
 */
public class GenStart extends PacmanGeneratorStart {

	@Override
	protected Map<String, SelectionType_Enum> getMainTemplates() {
		Map<String, SelectionType_Enum> templates = new HashMap<>();
		templates.put("genRootStart", SelectionType_Enum.ROOT);
		return templates;
	}

	@Override
	public String getSubProjectName() {
		return null;
	}

	@Override
	public String getModuleQualifiedName() {
		return "fr::pacman::start::aql::genStart";
	}

	@Override
	protected Map<String, String> getOptions() {
		Map<String, String> res = new LinkedHashMap<>();
		res.put(AcceleoUtil.LOG_URI_OPTION, c_defaultLogFileName);
		res.put(AcceleoUtil.NEW_LINE_OPTION, c_defaultNewLine);
		return res;
	}

	@Override
	protected List<EObject> getValues(IQualifiedNameQueryEnvironment p_queryEnvironment,
			Map<EClass, List<EObject>> p_valuesCache, TypeLiteral p_type, ResourceSet p_resourceSetForModels,
			Monitor p_monitor) {
		final List<EObject> values = new ArrayList<>();
		values.add(OverviewFactoryImpl.init().createRoot());
		return values;
	}
}
