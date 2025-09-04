package fr.pacman.back.core.convention.norme;

/**
 * Définition d'une norme au sens large.
 * 
 * @author MINARM
 */
public abstract class Norme {

	/**
	 * Appliquer la norme à la valeur spécifiée.
	 * 
	 * @param p_value La valeur à résoudre.
	 * @return La valeur résolue (norme appliquée).
	 */
	abstract public String applyNorme(String p_value);
}
