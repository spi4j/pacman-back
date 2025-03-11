package fr.pacman.core.convention.rule;

import fr.pacman.core.property.PacmanProperty;
import fr.pacman.core.property.PropertiesCategory;
import fr.pacman.core.property.PropertiesHandler;

/**
 * Classe des règles de nommage sur les packages. Placer les propriétés dans
 * l'ordre d'affichage desiré dans le fichier.
 *
 * 
 * @author MINARM
 */
public class PackageNamingRule extends PropertiesCategory {

	private static final String c_idParam_packagePersistence = "packagePersistence";
	private static final String c_idParam_packageImplemJdbc = "packageImplemJdbc";
	private static final String c_idParam_packageApi = "packageApi";
	private static final String c_idParam_packageOverload = "packageOverload";
	private static final String c_idParam_packageReferentiel = "packageReferentiel";
	private static final String c_idParam_packageImplemServer = "packageImplemServer";
	private static final String c_idParam_packageMatching = "packageMatching";
	private static final String c_idParam_packageBusiness = "packageBusiness";
	private static final String c_idParam_packageRequirement = "packageRequirement";
	private static final String c_idParam_packagePgeSwing = "packagePgeSwing";
	private static final String c_idParam_packagePgeGwt = "packagePgeGwt";
	private static final String c_idParam_packagePgeJsf = "packagePgeJsf";

	@Override
	protected String get_propertiesFileName() {
		return "nommage.properties";
	}

	@Override
	protected boolean is_defaultFileForAdditionalproperties() {
		return false;
	}

	@Override
	protected PacmanProperty[] initPacmanProperties() {
		return new PacmanProperty[] {
				PacmanProperty.newRequired(c_idParam_packagePersistence, new String[] { "persistence", "persistence" },
						"Le nom du package de persistence"),

				PacmanProperty.newRequired(c_idParam_packageImplemJdbc, new String[] { "jdbc", "impl_jdbc" },
						"Le nom du package d'implementation jdbc"),

				PacmanProperty.newRequired(c_idParam_packageApi, new String[] { "api", "api" },
						"Le nom du package d'api"),

				PacmanProperty.newRequired(c_idParam_packageOverload, new String[] { "dbpopulate", "dbpopulate" },
						"Le nom du package pour la montee en charge"),

				PacmanProperty.newRequired(c_idParam_packageReferentiel,
						new String[] { "dbreferentiel", "dbreferentiel" },
						"Le nom du package pour l import de referentiel"),

				PacmanProperty.newRequired(c_idParam_packageImplemServer, new String[] { "server", "impl_server" },
						"Le nom du package d'implementation server"),

				PacmanProperty.newRequired(c_idParam_packageMatching, new String[] { "matching", "matching" },
						"Le nom du package de matching"),

				PacmanProperty.newRequired(c_idParam_packageBusiness, new String[] { "business", "business" },
						"Le nom du package du business"),

				PacmanProperty.newRequired(c_idParam_packageRequirement, new String[] { "requirement", "requirement" },
						"Le nom du package de requirement"),

				PacmanProperty.newRequired(c_idParam_packagePgeSwing,
						new String[] { "client.ui.swing.widgets", "client.ui.swing.widgets" },
						"Le nom du package pour les composants PGE Swing"),

				PacmanProperty.newRequired(c_idParam_packagePgeGwt,
						new String[] { "client.ui.gwt.widgets", "client.ui.gwt.widgets" },
						"Le nom du package pour les composants PGE GWT"), };
	}

	/**
	 * Appliquer la norme 'packagePersistence' sur la valeur passée en parametre.
	 * 
	 * @param p_value      la valeur originale (ex : "Imputation de gestion").
	 * @param p_properties les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_packagePersistence(final String p_value) {
		return applyNorme(p_value, c_idParam_packagePersistence);
	}

	/**
	 * Appliquer la norme 'packageImplemJdbc' sur la valeur passée en parametre.
	 * 
	 * @param p_value      la valeur originale (ex : "Imputation de gestion").
	 * @param p_properties les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_packageImplemJdbc(final String p_value) {
		return applyNorme(p_value, c_idParam_packageImplemJdbc);
	}

	/**
	 * Appliquer la norme 'packageApi' sur la valeur passée en parametre.
	 * 
	 * @param p_value      la valeur originale (ex : "Imputation de gestion").
	 * @param p_properties les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_packageApi(final String p_value) {
		return applyNorme(p_value, c_idParam_packageApi);
	}

	/**
	 * Appliquer la norme 'packageImplemServer' sur la valeur passée en parametre.
	 * 
	 * @param p_value      la valeur originale (ex : "Imputation de gestion").
	 * @param p_properties les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_packageImplemServer(final String p_value) {
		return applyNorme(p_value, c_idParam_packageImplemServer);
	}

	/**
	 * Appliquer la norme 'packageMatching' sur la valeur passée en parametre.
	 * 
	 * @param p_value      la valeur originale (ex : "Imputation de gestion").
	 * @param p_properties les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_packageMatching(final String p_value) {
		return applyNorme(p_value, c_idParam_packageMatching);
	}

	/**
	 * Appliquer la norme 'packageBusiness' sur la valeur passée en parametre.
	 * 
	 * @param p_value      la valeur originale (ex : "Imputation de gestion").
	 * @param p_properties les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_packageBusiness(final String p_value) {
		return applyNorme(p_value, c_idParam_packageBusiness);
	}

	/**
	 * Appliquer la norme 'packageRequirement' sur la valeur passée en parametre.
	 * 
	 * @param p_value      la valeur originale (ex : "Imputation de gestion").
	 * @param p_properties les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_packageRequirement(final String p_value) {
		return applyNorme(p_value, c_idParam_packageRequirement);
	}

	/**
	 * Bien que la classe soit nommee "PackageRule" elle est aussi utilisée pour
	 * récupérer directement les noms de package.
	 */
	public static String get_packagePersistence(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packagePersistence);
	}

	public static String get_packageImplemJdbc(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packageImplemJdbc);
	}

	public static String get_packageApi(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packageApi);
	}

	public static String get_packageOverload(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packageOverload);
	}

	public static String get_packageReferentiel(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packageReferentiel);
	}

	public static String get_packageImplemServer(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packageImplemServer);
	}

	public static String get_packagePgeJsf(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packagePgeJsf);
	}

	public static String get_packageMatching(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packageMatching);
	}

	public static String get_packageBusiness(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packageBusiness);
	}

	public static String get_packageRequirement(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packageRequirement);
	}

	public static String get_packagePgeSwing(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packagePgeSwing);
	}

	public static String get_packagePgeGwt(Object object) {
		return PropertiesHandler.getProperty(c_idParam_packagePgeGwt);
	}
}
