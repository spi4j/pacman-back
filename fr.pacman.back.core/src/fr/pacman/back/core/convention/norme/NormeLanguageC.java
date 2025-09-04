package fr.pacman.back.core.convention.norme;

import java.util.StringTokenizer;

/**
 * Norme LanguageC.
 * 
 * @author MINARM
 */
public class NormeLanguageC extends Norme {

	/**
	 * Applique la norme 'LanguageC' (ex : 'une valeur' --> 'une_valeur').
	 * 
	 * @param p_value La valeur d'origine.
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
				value = value + "_" + valueCour.substring(0, valueCour.length());
			}
			nbMots++;
		}
		return value;
	}
}
