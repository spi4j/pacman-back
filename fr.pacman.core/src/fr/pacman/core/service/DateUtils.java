package fr.pacman.core.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitaire pour les dates.
 * 
 * @author MINARM
 */
public class DateUtils {

	/**
	 * Constructeur privé.
	 */
	private DateUtils() {
		// RAS.
	}

	/**
	 * Retourne le timestamp au format p_format.
	 * 
	 * @param p_format le format attendu pour le timestamp.
	 * @return le timestamp formaté avec p_format (sous forme de chaîne de
	 *         caractères).
	 */
	public static String get_dateTime(final String p_format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(p_format);
		return LocalDateTime.now().format(formatter);
	}

	/**
	 * Retourne la date du jour au format p_format.
	 * 
	 * @param p_format le format attendu de la date
	 * @return la date du jour formatée avec p_format (sous forme de chaîne de
	 *         caractères).
	 */
	public static String get_date(final String p_format) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(p_format);
		return LocalDate.now().format(formatter);
	}
}
