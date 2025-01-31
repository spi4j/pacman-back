package fr.pacman.core.convention.rule;

import fr.pacman.core.convention.NotationDefinition;
import fr.pacman.core.property.PacmanProperty;
import fr.pacman.core.property.PropertiesCategory;

/**
 * Classe des règles de nommage sur les classes. Placer les propriétés dans
 * l'ordre d'affichage désiré dans le fichier.
 *
 * @author MINARM
 */
public class ClassNamingRule extends PropertiesCategory {

	private static final String c_idParam_classDefault = "classDefault";
	private static final String c_idParam_classAnnotation = "classAnnotation";
	private static final String c_idParam_classAbstract = "classAbstract";
	private static final String c_idParam_classInterface = "classInterface";
	private static final String c_idParam_classImplem = "classImplem";
	private static final String c_idParam_classEnum = "classEnum";
	private static final String c_idParam_classTest = "classTest";

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
				PacmanProperty.newRequired(c_idParam_classDefault,
						new String[] { "[" + NotationDefinition.normeCamelCase() + "/]",
								"[" + NotationDefinition.normeCamelCase() + "/]" },
						"La norme a appliquer pour une classe par defaut"),

				PacmanProperty.newRequired(c_idParam_classAnnotation,
						new String[] { "[" + NotationDefinition.normeCamelCase() + "/]",
								"[" + NotationDefinition.normeCamelCase() + "/]" },
						"La norme a appliquer pour une annotation Java"),

				PacmanProperty.newRequired(c_idParam_classAbstract,
						new String[] { "[" + NotationDefinition.normeCamelCase() + "/]Abs",
								"[" + NotationDefinition.normeCamelCase() + "/]_Abs" },
						"La norme a appliquer pour une classe abstraite"),

				PacmanProperty.newRequired(c_idParam_classInterface,
						new String[] { "[" + NotationDefinition.normeCamelCase() + "/]",
								"[" + NotationDefinition.normeCamelCase() + "/]_Itf" },
						"La norme a appliquer pour une interface"),

				PacmanProperty.newRequired(c_idParam_classImplem,
						new String[] { "[" + NotationDefinition.normeCamelCase() + "/]Impl",
								"[" + NotationDefinition.normeCamelCase() + "/]" },
						"La norme a appliquer pour une classe d'implementation"),

				PacmanProperty.newRequired(c_idParam_classEnum,
						new String[] { "[" + NotationDefinition.normeCamelCase() + "/]Enum",
								"[" + NotationDefinition.normeCamelCase() + "/]_Enum" },
						"La norme a appliquer pour une enumeration"),

				PacmanProperty.newRequired(c_idParam_classTest,
						new String[] { "[" + NotationDefinition.normeCamelCase() + "/]Test",
								"[" + NotationDefinition.normeCamelCase() + "/]_Test" },
						"La norme a appliquer pour une classe de test"), };
	}

	/**
	 * Appliquer la norme 'classAbstract' sur la valeur passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_classAbstract(final String p_value) {
		return applyNorme(p_value, c_idParam_classAbstract);
	}

	/**
	 * Appliquer la norme 'classInterface' sur la valeur passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_classInterface(final String p_value) {
		return applyNorme(p_value, c_idParam_classInterface);
	}

	/**
	 * Appliquer la norme 'classImplem' sur la valeur passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_classImplem(final String p_value) {
		return applyNorme(p_value, c_idParam_classImplem);
	}

	/**
	 * Appliquer la norme 'classEnum' sur la valeur passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_classEnum(final String p_value) {
		return applyNorme(p_value, c_idParam_classEnum);
	}

	/**
	 * Appliquer la norme 'classDefault' sur la valeur passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_classDefault(final String p_value) {
		return applyNorme(p_value, c_idParam_classDefault);
	}

	/**
	 * Appliquer la norme 'classAnnotation' (i.e. annotation Java) sur la valeur
	 * passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_classAnnotation(final String p_value) {
		return applyNorme(p_value, c_idParam_classAnnotation);
	}

	/**
	 * Appliquer la norme 'classTest' sur la valeur passée en parametre.
	 * 
	 * @param p_value     la valeur originale (ex : "Imputation de gestion").
	 * @param p_modelFile les properties.
	 * @return La valeur respectant la norme.
	 */
	public static String applyNorme_classTest(final String p_value) {
		return applyNorme(p_value, c_idParam_classTest);
	}
}
