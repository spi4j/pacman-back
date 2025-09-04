package fr.pacman.back.core.convention;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import fr.pacman.back.core.convention.norme.Norme;
import fr.pacman.back.core.service.StringUtils;

/**
 * Définit l'ensemble des méthodes que l'on peut appeler pour la convention de
 * nommage.
 * 
 * @author MINARM
 */
public final class NotationDefinition {

	private static final String c_name_normeCamelCase = "CamelCase";
	private static final String c_name_normeCamelCaseIgnoreFirst = "CamelCaseIgnoreFirst";
	private static final String c_name_normeLanguageC = "LanguageC";
	private static final String c_name_normeUpperFirst = "UpperFirst";
	private static final String c_name_normeLowerFirst = "LowerFirst";
	private static final String c_name_normeUpperAll = "UpperAll";
	private static final String c_name_normeLowerAll = "LowerAll";
	private static final String c_name_normeTrimLowerAll = "TrimLowerAll";

	private final List<String> _tab_allNames = new ArrayList<String>();

	/**
	 * Constructeur par défaut.
	 * 
	 * @param p_name la norme.
	 **/
	public NotationDefinition(final String p_name) {
		if (p_name != null && !p_name.trim().isEmpty()) {
			String nameCour;
			final StringTokenizer st = new StringTokenizer(p_name, ".");
			while (st.hasMoreTokens()) {
				nameCour = (String) st.nextElement();
				_tab_allNames.add(nameCour);
			}
		}
	}

	/**
	 * Retourne une chaîne représentant tous les attributs du bean.
	 * 
	 * @return la chaîne représentant tous les attributs du bean.
	 */
	@Override
	public String toString() {
		boolean first = true;
		final StringBuilder strBuilder = new StringBuilder();
		for (final String name : _tab_allNames) {
			if (first) {
				first = false;
			} else {
				strBuilder.append('.');
			}
			strBuilder.append(name);
		}
		return strBuilder.toString();
	}

	/**
	 * Applique la mise en norme "CamelCase" (ex : "une valeur" --> "uneValeur").
	 * 
	 * @return une instance prenant en compte cette notation.
	 */
	public static NotationDefinition normeCamelCase() {
		return new NotationDefinition(c_name_normeCamelCase);
	}

	/**
	 * Applique la mise en norme "CamelCaseIgnoreFirst" (ex : "une valeur" -->
	 * "uneValeur").
	 * 
	 * @return une instance prenant en compte cette notation.
	 */
	public static NotationDefinition normeCamelCaseIgnoreFirst() {
		return new NotationDefinition(c_name_normeCamelCaseIgnoreFirst);
	}

	/**
	 * Applique la mise en norme "langage C" (ex : "une valeur" --> "une_valeur").
	 * 
	 * @return une instance prenant en compte cette notation.
	 */
	public static NotationDefinition normeLanguageC() {
		return new NotationDefinition(c_name_normeLanguageC);
	}

	/**
	 * Applique la mise en norme "LowerAll" (ex : "une vaLeur" --> "une valeur").
	 * 
	 * @return une instance prenant en compte cette notation.
	 */
	public static NotationDefinition normeLowerAll() {
		return new NotationDefinition(c_name_normeLowerAll);
	}

	/**
	 * Applique la mise en norme "TrimLowerAll" (ex : "une vaLeur" --> "unevaleur").
	 * 
	 * @return une instance prenant en compte cette notation.
	 */
	public static NotationDefinition normeTrimLowerAll() {
		return new NotationDefinition(c_name_normeTrimLowerAll);
	}

	/**
	 * Applique la mise en norme "LowerFirst" (ex : "une Valeur" --> "une Valeur").
	 * 
	 * @return une instance prenant en compte cette notation.
	 */
	public static NotationDefinition normeLowerFirst() {
		return new NotationDefinition(c_name_normeLowerFirst);
	}

	/**
	 * Applique la mise en norme "UpperAll" (ex : "une valeur" --> "une VALEUR").
	 * 
	 * @return une instance prenant en compte cette notation.
	 */
	public static NotationDefinition normeUpperAll() {
		return new NotationDefinition(c_name_normeUpperAll);
	}

	/**
	 * Applique la mise en norme "UpperFirst" (ex : "une Valeur" --> "une Valeur").
	 * 
	 * @return une instance prenant en compte cette notation.
	 */
	public static NotationDefinition normeUpperFirst() {
		return new NotationDefinition(c_name_normeUpperFirst);
	}

	/**
	 * Applique la valeur résolue en tenant compte de la notation désirée.
	 * 
	 * @param p_value La valeur d'origine.
	 * @return La valeur résolue.
	 */
	public String applyNorme(final String p_value) {
		String value = StringUtils.do_normalize(p_value);
		for (String nameNotation : _tab_allNames) {
			final Norme Norme = findNorme(nameNotation);
			value = Norme.applyNorme(value);
		}
		return value;
	}

	/**
	 * Applique la norme.
	 * 
	 * @param p_nameNotation Le nom de la norme à appliquer (ex : "CamelCase").
	 * @return la valeur normée.
	 */
	private Norme findNorme(final String p_nameNotation) {
		final String prefixClassNorme = Norme.class.getPackage().getName() + ".Norme";
		try {
			@SuppressWarnings("unchecked")
			final Class<Norme> normeClass = (Class<Norme>) Class.forName(prefixClassNorme + p_nameNotation);
			return (Norme) normeClass.getDeclaredConstructor().newInstance();
		} catch (final Exception err) {
			throw new IllegalArgumentException(
					"Pas possible d'instancier la norme \"" + prefixClassNorme + p_nameNotation, err);
		}
	}

	/**
	 * Ajoute une norme à la suite de la norme courante
	 * 
	 * @param p_normeToAppend la norme à ajouter
	 * @return la norme concaténée
	 */
	public NotationDefinition appendNorme(final NotationDefinition p_normeToAppend) {
		_tab_allNames.addAll(p_normeToAppend._tab_allNames);
		return this;
	}
}
