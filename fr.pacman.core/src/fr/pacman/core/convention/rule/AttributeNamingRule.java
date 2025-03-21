package fr.pacman.core.convention.rule;

import fr.pacman.core.convention.NotationDefinition;
import fr.pacman.core.property.PacmanProperty;
import fr.pacman.core.property.PropertiesCategory;

/**
 * Classe des règles de nommage sur les attributs. Placer les propriétés dans
 * l'ordre d'affichage désiré dans le fichier.
 * 
 * @author MINARM
 */
public class AttributeNamingRule extends PropertiesCategory {

	private static final String c_idParam_attributeDefault = "attributeDefault";
	private static final String c_idParam_attributeEnum = "attributeEnum";
	private static final String c_idParam_attributeMultiple = "attributeMultiple";
	private static final String c_idParam_attributeAffectation = "attributeAffectation";
	private static final String c_idParam_attributeStatic = "attributeStatic";
	private static final String c_idParam_attributeFinalStatic = "attributeFinalStatic";
	private static final String c_idParam_attributeFinal = "attributeFinal";

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
		return new PacmanProperty[] { PacmanProperty.newRequired(c_idParam_attributeDefault, new String[] {
				"[" + NotationDefinition.normeCamelCase().appendNorme(NotationDefinition.normeLowerFirst()) + "/]",

				"_[" + NotationDefinition.normeCamelCase().appendNorme(NotationDefinition.normeLowerFirst()) + "/]" },
				"Le nom d'un attribut par defaut"),

				PacmanProperty.newRequired(c_idParam_attributeEnum, new String[] {
						"[" + NotationDefinition.normeCamelCase().appendNorme(NotationDefinition.normeLowerFirst())
								+ "/]",
						"[" + NotationDefinition.normeCamelCase().appendNorme(NotationDefinition.normeLowerFirst())
								+ "/]" },
						"Le nom d'un attribut d'enumeration"),

				PacmanProperty.newRequired(c_idParam_attributeMultiple, new String[] {
						"[" + NotationDefinition.normeCamelCase().appendNorme(NotationDefinition.normeLowerFirst())
								+ "/]",
						"_{$listPrefix}_["
								+ NotationDefinition.normeCamelCase().appendNorme(NotationDefinition.normeLowerFirst())
								+ "/]" },
						"Le nom d'un attribut multiple"),

				PacmanProperty.newRequired(c_idParam_attributeAffectation,
						new String[] { "this.{$" + c_idParam_attributeDefault + "}",
								"this.{$" + c_idParam_attributeDefault + "}" },
						"Le nom d'attribut dans une affectation dans le constructeur"),

				PacmanProperty.newRequired(c_idParam_attributeStatic,
						new String[] {
								"[" + NotationDefinition.normeCamelCase()
										.appendNorme(NotationDefinition.normeLowerFirst()) + "/]",
								"[" + NotationDefinition.normeCamelCaseIgnoreFirst() + "/]" },
						"Le nom d'un attribut static"),

				PacmanProperty.newRequired(c_idParam_attributeFinalStatic,
						new String[] {
								"[" + NotationDefinition.normeCamelCase()
										.appendNorme(NotationDefinition.normeUpperAll()) + "/]",
								"c_[" + NotationDefinition.normeCamelCaseIgnoreFirst() + "/]" },
						"Le nom d'un attribut final static"),

				PacmanProperty.newRequired(c_idParam_attributeFinal, new String[] {
						"{$" + c_idParam_attributeDefault + "}", "{$" + c_idParam_attributeDefault + "}" },
						"Le nom d'un attribut final"), };
	}

	/**
	 * Appliquer la norme 'attributeMultiple' sur la valeur passée en paramètre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_attributeMultiple(final String p_value) {
		return applyNorme(p_value, c_idParam_attributeMultiple);
	}

	/**
	 * Appliquer la norme 'attributeEnum' sur la valeur passée en paramètre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_attributeEnum(final String p_value) {
		return applyNorme(p_value, c_idParam_attributeEnum);
	}

	/**
	 * Appliquer la norme 'attributeStatic' sur la valeur passée en paramètre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_attributeStatic(final String p_value) {
		return applyNorme(p_value, c_idParam_attributeStatic);
	}

	/**
	 * Appliquer la norme 'attributeDefault' sur la valeur passée en paramètre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_attributeDefault(final String p_value) {
		return applyNorme(p_value, c_idParam_attributeDefault);
	}

	/**
	 * Appliquer la norme 'attributeFinalStatic' sur la valeur passée en paramètre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_attributeFinalStatic(final String p_value) {
		return applyNorme(p_value, c_idParam_attributeFinalStatic);
	}

	/**
	 * Appliquer la norme 'attributeFinal' sur la valeur passée en paramètre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_attributeFinal(final String p_value) {
		return applyNorme(p_value, c_idParam_attributeFinal);
	}
}
