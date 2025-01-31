package fr.pacman.core.convention;

import java.util.Properties;

/**
 * Classe de résolution d'une convention de nommage.
 * 
 * @author MINARM
 */
public class NotationResolution {

	public static final String c_idPatternStart = "[";
	public static final String c_idPatternEnd = "/]";

	/** Le pattern de resolution (ex : "p_[LowerAll.UpperFirst.LanguageC/]") */
	private String _pattern;

	/**
	 * Le contructeur par défaut.
	 * 
	 * @param p_pattern Le pattern à appliquer.
	 */
	public NotationResolution(final String p_pattern) {
		_pattern = p_pattern;
	}

	/**
	 * Applique le nom résolu : après application du pattern. Avec remplacement des
	 * properties.
	 * 
	 * @param p_value      la valeur.
	 * @param p_properties les properties.
	 * @return Le nom résolu.
	 */
	public String applyNorme(final String p_value, final Properties p_properties) {
		String value;
		value = replaceTagsOfProperties(_pattern, p_properties);
		_pattern = value;
		final String prefixe;
		final String suffixe;

		final NotationDefinition NotationDefinition;

		final int rgDeb = _pattern.indexOf(c_idPatternStart);
		final int rgFin = _pattern.indexOf(c_idPatternEnd);
		if (rgDeb >= 0 && rgFin >= 0 && rgDeb < rgFin) {
			final String valueNorme = _pattern.substring(rgDeb + c_idPatternStart.length(), rgFin);
			NotationDefinition = new NotationDefinition(valueNorme);
			prefixe = _pattern.substring(0, rgDeb);
			suffixe = _pattern.substring(rgFin + c_idPatternEnd.length(), _pattern.length());
		} else {
			prefixe = "";
			suffixe = "";
			NotationDefinition = new NotationDefinition("");
		}
		value = prefixe + NotationDefinition.applyNorme(p_value) + suffixe;
		return value;
	}

	/**
	 * Remplace les références à une autre propriété dans la chaîne à résoudre.
	 * 
	 * @param p_value      la valeur initiale.
	 * @param p_properties les properties.
	 * @return La chaine remplacée.
	 */
	public static String replaceTagsOfProperties(final String p_value, final Properties p_properties) {
		String value = p_value;
		if ((p_value != null) && (p_value.indexOf("{$") >= 0)) {
			for (final String propertyName : p_properties.stringPropertyNames()) {
				if (p_value.indexOf(getVarName(propertyName)) >= 0) {
					value = value.replace("{$" + propertyName + "}",
							replaceTagsOfProperties(p_properties.getProperty(propertyName), p_properties));
				}
			}
		}
		return value;
	}

	/**
	 * Retourne le nom de la propriété formaté avec son $.
	 * 
	 * @param p_propertyName le nom de la propriété à formater.
	 * @return le nom de la propriété formaté avec son $.
	 */
	private static String getVarName(final String p_propertyName) {
		return "{$" + p_propertyName + "}";
	}

	/**
	 * Retourne la chaîne à résoudre.
	 * 
	 * @return La chaîne à résoudre.
	 */
	@Override
	public String toString() {
		return c_idPatternStart + _pattern + c_idPatternEnd;
	}
}
