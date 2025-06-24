package fr.pacman.core.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe utilitaire sur les chaines de caractères.
 * 
 * @author MINARM
 */
public final class StringUtils {

	/**
	 * La liste des caractères authorisés, tous les autres caractères sont
	 * automatiquement supprimés.
	 */
	private static final Pattern c_authorizedChars = Pattern.compile("[^a-zA-Z0-9\s_]");

	/**
	 * 
	 */
	private static final String c_plain_ascii = "AaEeIiOoUu" // grave
			+ "AaEeIiOoUuYy" // acute
			+ "AaEeIiOoUuYy" // circumflex
			+ "AaOoNn" // tilde
			+ "AaEeIiOoUuYy" // umlaut
			+ "Aa" // ring
			+ "Cc" // cedilla
			+ "OoUu" // double acute
			+ "___"; // hyphen

	/**
	 * 
	 */
	private static final String c_unicode = "\u00C0\u00E0\u00C8\u00E8\u00CC\u00EC\u00D2\u00F2\u00D9\u00F9"
			+ "\u00C1\u00E1\u00C9\u00E9\u00CD\u00ED\u00D3\u00F3\u00DA\u00FA\u00DD\u00FD\u00C2\u00E2\u00CA"
			+ "\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177\u00C3\u00E3\u00D5\u00F5\u00D1\u00F1"
			+ "\u00C4\u00E4\u00CB\u00EB\u00CF\u00EF\u00D6\u00F6\u00DC\u00FC\u0178\u00FF\u00C5\u00E5\u00C7"
			+ "\u00E7\u0150\u0151\u0170\u0171\u002D\u2212\u2012";

	// TODO a depacer si multilangues java, php, etc...
	private static final String c_reserved_keywords = "|abstract|assert|boolean|break|byte|case|catch|char|class"
			+ "|continue|default|do|double|else|enum|extends|false|final|finally|float|for|if|implements|import|instanceof"
			+ "|int|interface|long|native|new|null|package|private|protected|public|return|short|static|strictfp|super|switch"
			+ "|synchronized|this|throw|throws|transient|try|true|void|volatile|while";

	/**
	 * Constructeur privé.
	 */
	private StringUtils() {
		// RAS
	}

	/**
	 * Supprime les caractères spéciaux et les remplace par leurs équivalents.
	 * 
	 * @param p_s la chaine de caractère à normaliser
	 * @return la chaine normalisée
	 */
	public static String do_normalize(final String p_str) {
		if (p_str == null)
			return null;

		final StringBuilder sb = new StringBuilder();
		final int n = p_str.length();
		for (int i = 0; i < n; i++) {
			final char c = p_str.charAt(i);
			final int pos = c_unicode.indexOf(c);
			if (pos > -1) {
				sb.append(c_plain_ascii.charAt(pos));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Retourne la notation CamelCase d'une chaine de caractères.
	 * 
	 * @param p_str la chaine à écrire
	 * @return la notation CamelCase de p_s
	 */
	public static String do_camelCase(final String p_str) {
		if (p_str == null)
			return null;

		final StringBuilder sb = new StringBuilder();
		final String[] splits = p_str.split("\\s");
		for (final String split : splits) {
			if (split.length() > 0) {
				sb.append(split.substring(0, 1).toUpperCase());
			}
			if (split.length() > 1) {
				sb.append(split.substring(1));
			}
		}
		return sb.toString();
	}

	/**
	 * Supprime tous les caractères interdits à la fois pour java et pour les bases
	 * de données (sql). Afin de transformer les caractères accentués, il est
	 * nécessaire de passer au préalable par la méthode de normalisation
	 * (do_normalize) sinon, tous les caractères accentués seront automatiquement
	 * supprimés.
	 * 
	 * @param p_str p_s la chaine à écrire
	 * @return la chaine expurgée de l'ensemble des caractères spéciaux.
	 */
	public static String do_sanitize(String p_str) {
		Matcher match = c_authorizedChars.matcher(p_str);
		while (match.find()) {
			String s = match.group();
			p_str = p_str.replaceAll("\\" + s, "");
		}
		return p_str;
	}

	/**
	 * Ecriture d'une ligne au format /* ABCDEF \*\/
	 * 
	 * @param p_str  la chaine à écrire à la place de ABCDEF
	 * @param p_size la taille de la ligne à générer
	 * @return la ligne générée
	 */
	public static String do_slashLine(final String p_str, final Integer p_size) {
		final StringBuilder sb = new StringBuilder("/* ");
		if (p_str != null) {
			sb.append(p_str);
		}
		while (sb.length() < p_size) {
			sb.append(' ');
		}
		sb.append(" */");
		return sb.toString();
	}

	/**
	 * Ecriture d'une ligne au format /* ABCDEF \*\/
	 * 
	 * @param p_str  la chaine à écrire à la place de ABCDEF
	 * @param p_size la taille de la ligne à générer
	 * @return la ligne générée
	 */
	public static String do_tabCell(String p_str, final Integer p_size, final String p_separator) {

		if (p_str.length() > p_size - 1)
			p_str = p_str.substring(0, p_size - 4) + "...";

		final StringBuilder sb = new StringBuilder(" ");
		if (p_str != null) {
			sb.append(p_str);
		}
		while (sb.length() < p_size) {
			sb.append(' ');
		}
		sb.append(p_separator);
		return sb.toString();
	}

	/**
	 * Echappement des caractères spéciaux d'une chaine, tels que les guillemets,
	 * les retours chariots et les tabulations.
	 * 
	 * @param p_str la chaine à échapper
	 * @return la chaine échappée
	 */
	public static String do_escape(final String p_str) {
		if (p_str == null) {
			return "";
		}
		return p_str.replaceAll("\"", "\\\\\"").replaceAll("\r\n", "\\\\n").replaceAll("\n", "\\\\n").replaceAll("\t",
				"   ");
	}

	/**
	 * Découpe une longue chaine de caractères en positionnant un retour à la ligne
	 * en fonction de la demande de césure à p_nbChars. Si le paramètre p_char est
	 * renseigné, alors une fois la césure atteinte, on continue jusqu'a rencontrer
	 * ce char afin de ne pas effectuer la césure n'importe comment et rendre la
	 * chaine de caractères illisible.
	 * 
	 * @param p_str     la chaine à découper
	 * @param p_nbChars le nombre de caractères par ligne
	 * @param p_cut     le caractère de retour à la ligne à affecter pour chaque
	 *                  césure
	 * @param p_char    si renseigné, on effectue la césure uniquement une fois ce
	 *                  caractère atteint, même si on a dépassé le nombre de
	 *                  caractères à l aligne
	 * @return la chaine découpée en plusieurs lignes (grâce au caractère de retour
	 *         à la ligne)
	 */
	public static String do_cut(final String p_str, final int p_nbChars, final String p_cut, final String p_char) {
		int pos = 0;
		StringBuilder strBuild = new StringBuilder();
		char[] charArray = p_str.toCharArray();
		for (char c : charArray) {
			strBuild.append(c);
			if (pos < p_nbChars) {
				pos++;
			} else {
				if (p_char != null && p_char.trim().length() == 1 && c != p_char.charAt(0))
					continue;
				strBuild.append(p_cut);
				pos = 0;
			}
		}
		return strBuild.toString();
	}

	/**
	 * Inverse une chaine de caractères, selon un séparateur :
	 * reverse("abc.def.ghi", "\\.") --> "ghi.def.abc"
	 * 
	 * @param p_str               la chaine originale
	 * @param p_patternSeparation le pattern de séparation
	 * @param p_nouveauSeparateur la nouvelle chaine de séparation
	 * @return la chaine inversée
	 */
	public static String do_reverse(final String p_str, final String p_patternSeparation,
			final String p_nouveauSeparateur) {
		final String[] split = p_str.split(p_patternSeparation);
		final StringBuilder builder = new StringBuilder(p_str.length());
		for (int i = split.length - 1; i >= 0; i--) {
			builder.append(split[i]).append(p_nouveauSeparateur);
		}
		if (builder.length() >= p_nouveauSeparateur.length()) {
			builder.setLength(builder.length() - p_nouveauSeparateur.length());
		}
		return builder.toString();
	}

	/**
	 * Ajoute "_1" au nom de la variable si cette dernière est un mot clé pour le
	 * langage cible de génération. Cette méthode est utilisé lors de la fabrication
	 * du client pour la librairie REST. Comme le fournisseur de ressources peut
	 * utiliser un autre langage, il existe des cas ou ces noms de variable ne
	 * conviennent pas dans le cadre de la génération pour le langage cible.
	 * 
	 * @param p_var
	 * @return
	 */
	public static String do_sanitizeKeywords(final String p_var) {
		if (c_reserved_keywords.indexOf("|" + p_var.toLowerCase() + "|") > 0) {
			return p_var + "_1";
		}
		return do_camelCase(p_var);
	}

	/**
	 * Retourne le hashcode d'une chaine de caractères (identifiant) pour une zone
	 * protégée de type user code.
	 * 
	 * @param p_str l'identifiant de la zone user code
	 * @return l'identifiant de la zone user code sous forme de hash (toujours la
	 *         même taille, quelle que soit la taille initiale de l'identifiant pass
	 *         en paramètre)
	 */
	public static String get_userCodeId(final String p_str) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(p_str.getBytes());
			byte[] messageDigestMD5 = messageDigest.digest();
			StringBuffer stringBuffer = new StringBuffer();
			for (byte bytes : messageDigestMD5) {
				stringBuffer.append(String.format("%02x", bytes & 0xff));
			}
			return stringBuffer.toString();
		} catch (NoSuchAlgorithmException exception) {
			return "";
		}
	}
}
