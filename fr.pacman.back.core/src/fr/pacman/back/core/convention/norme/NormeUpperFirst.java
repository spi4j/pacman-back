package fr.pacman.back.core.convention.norme;

import java.util.StringTokenizer;

/**
 * Norme UpperFirst.
 * 
 * @author MINARM
 */
public class NormeUpperFirst extends Norme {

	/**
	 * Applique la norme 'UpperFirst' (ex : 'uNe vaLEur' --> 'UNe VaLEur').
	 * 
	 * @param p_value la valeur d'origine.
	 * @return une instance prenant en compte cette notation.
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
				value = valueCour.toUpperCase().charAt(0) + valueCour.substring(1, valueCour.length());
			} else {
				value += " " + valueCour.toUpperCase().charAt(0) + valueCour.substring(1, valueCour.length());
			}
			nbMots++;
		}
		return value;
	}
}
