package fr.pacman.core.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 
 * @author MINARM
 */
public class IdUtils {

	/**
	 * Constructeur privé.
	 */
	private IdUtils() {
		// RAS.
	}

	/**
	 * 
	 * @param p_str
	 * @return
	 */
	public static String createFixedUserCodeId(final String p_str) {
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

	/**
	 * Génère un UUID à partir d'un nom.
	 * 
	 * @param p_str le nom
	 * @return l'UUID du nom
	 */
	public static String name(final String p_str) {
		return UUID.nameUUIDFromBytes(p_str.getBytes()).toString();
	}

	/**
	 * Génére un UUID aléatoire.
	 * 
	 * @return un UUID aléatoire
	 */
	public static String unique() {
		return UUID.randomUUID().toString();
	}
}
