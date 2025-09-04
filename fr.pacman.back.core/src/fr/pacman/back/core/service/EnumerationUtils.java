package fr.pacman.back.core.service;

import org.obeonetwork.dsl.environment.Enumeration;
import org.obeonetwork.dsl.environment.Literal;

/**
 * 
 * @author MINARM
 */
public class EnumerationUtils {

	/**
	 * Constructeur privé.
	 */
	private EnumerationUtils() {
		// RAS.
	}

	/**
	 * 
	 * @param p_enum
	 * @return
	 */
	public static String get_maxSqlSizeForEnumeration(final Enumeration p_enum) {
		int maxSize = 0;
		for (Literal el : p_enum.getLiterals()) {
			if (maxSize < el.getName().length())
				maxSize = el.getName().length();
		}
		return Integer.toString(maxSize);
	}
}
