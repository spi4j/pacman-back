package fr.pacman.back.core.convention.norme;

import java.util.StringTokenizer;

/**
 * Norme TrimLowerAll.
 * 
 * @author MINARM
 */
public class NormeTrimLowerAll extends Norme {

	/**
	 * Applique la norme 'TrimLowerAll' (ex : 'UNe VAleur' --> 'unevaleur').
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
				value = valueCour.toLowerCase();
			} else {
				value = value + valueCour.toLowerCase();
			}
			nbMots++;
		}
		return value;
	}
}
