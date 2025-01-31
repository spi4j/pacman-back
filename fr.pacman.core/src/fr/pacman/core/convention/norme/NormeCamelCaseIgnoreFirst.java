package fr.pacman.core.convention.norme;

import java.util.StringTokenizer;

/**
 * Norme CamelCaseIgnoreFirst.
 * 
 * @author MINARM
 */
public class NormeCamelCaseIgnoreFirst extends Norme {

	/**
	 * Applique la norme 'CamelCase' (ex : 'une valeur' --> 'uneValeur').
	 * 
	 * @param p_value la valeur d'origine.
	 * @return la valeur prennant en compte cette notation.
	 */
	@Override
	public String applyNorme(final String p_value) {
		String value = "";
		final StringTokenizer st = new StringTokenizer(p_value, " ");
		String valueCour;
		int nbMots = 0;
		while (st.hasMoreTokens() == true) {
			valueCour = (String) st.nextElement();
			if (nbMots == 0) {
				value = valueCour;
			} else {
				value = value + valueCour.toUpperCase().charAt(0) + valueCour.substring(1, valueCour.length());
			}
			nbMots++;
		}
		return value;
	}
}
