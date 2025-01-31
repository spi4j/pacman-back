package fr.pacman.core.convention.rule;

import fr.pacman.core.convention.NotationDefinition;
import fr.pacman.core.property.PacmanProperty;
import fr.pacman.core.property.PropertiesCategory;

/**
 * Classe des règles de nommage sur les methodes. Placer les propriétés dans
 * l'ordre d'affichage désiré dans le fichier.
 *
 * @author MINARM
 */
public class MethodNamingRule extends PropertiesCategory {

	private static final String c_idParam_methodDefault = "methodDefault";
	private static final String c_idParam_methodMultiple = "methodMultiple";
	private static final String c_idParam_methodGet = "methodGet";
	private static final String c_idParam_methodSet = "methodSet";
	private static final String c_idParam_methodReset = "methodReset";

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
		return new PacmanProperty[] { PacmanProperty.newRequired(c_idParam_methodDefault, new String[] {
				"[" + NotationDefinition.normeCamelCase().appendNorme(NotationDefinition.normeLowerFirst()) + "/]",

				"[" + NotationDefinition.normeCamelCase().appendNorme(NotationDefinition.normeLowerFirst()) + "/]" },
				"Le nom d'une methode par defaut"),

				PacmanProperty.newRequired(c_idParam_methodMultiple,
						new String[] {
								"[" + NotationDefinition.normeCamelCase()
										.appendNorme(NotationDefinition.normeLowerFirst()) + "/]",
								"[" + NotationDefinition.normeLanguageC() + "/]" },
						"Le nom d'une methode multiple"),

				PacmanProperty.newRequired(c_idParam_methodGet,
						new String[] {
								"get[" + NotationDefinition.normeCamelCase()
										.appendNorme(NotationDefinition.normeUpperFirst()) + "/]",
								"get_[" + NotationDefinition.normeCamelCaseIgnoreFirst() + "/]" },
						"Le nom d'une methode get"),

				PacmanProperty.newRequired(c_idParam_methodSet,
						new String[] {
								"set[" + NotationDefinition.normeCamelCase()
										.appendNorme(NotationDefinition.normeUpperFirst()) + "/]",
								"set_[" + NotationDefinition.normeCamelCaseIgnoreFirst() + "/]" },
						"Le nom d'une methode set"),

				PacmanProperty.newRequired(c_idParam_methodReset,
						new String[] {
								"reset[" + NotationDefinition.normeCamelCase()
										.appendNorme(NotationDefinition.normeUpperFirst()) + "/]",
								"reset_[" + NotationDefinition.normeCamelCaseIgnoreFirst() + "/]" },
						"Le nom d'une methode reset"), };
	}

	/**
	 * Appliquer la norme 'methodDefault' sur la valeur passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_methodDefault(final String p_value) {
		return applyNorme(p_value, c_idParam_methodDefault);
	}

	/**
	 * Appliquer la norme 'methodMultiple' sur la valeur passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_methodMultiple(final String p_value) {
		return applyNorme(p_value, c_idParam_methodMultiple);
	}

	/**
	 * Appliquer la norme 'methodGet' sur la valeur passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_methodGet(final String p_value) {
		return applyNorme(p_value, c_idParam_methodGet);
	}

	/**
	 * Appliquer la norme 'methodSet' sur la valeur passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_methodSet(final String p_value) {
		return applyNorme(p_value, c_idParam_methodSet);
	}

	/**
	 * Appliquer la norme 'methodReset' sur la valeur passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_methodReset(final String p_value) {
		return applyNorme(p_value, c_idParam_methodReset);
	}
}
