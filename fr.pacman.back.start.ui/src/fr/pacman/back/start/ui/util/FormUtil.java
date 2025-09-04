package fr.pacman.back.start.ui.util;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * Classe utilitaire pour la vérification et la validation des différents champs
 * de saisie dans le cadre du wizard de création d'un projet Cali.
 * 
 * @author MINARM
 */
public class FormUtil {

	private static final String c_patternNumeric = "[0-9[\b]]";
	private static final String c_patternAlphaNumeric = "[a-zA-Z0-9[\b]]";
	private static final String c_patternProjectKey = "[a-zA-Z0-9[\b\\_\\-]]";
	private static final String c_patternPackageKey = "[a-z0-9[\b\\.]]";
	private static final String c_patternSqlOptionKey = "[a-zA-Z0-9[\b\\_]]";
	private static final String c_patternSqlSchemaKey = "[a-zA-Z0-9[\b\\.]]";
	private static final String c_patternSqlPrefix = "^$|^[a-zA-Z0-9]+(\\_){1}";
	private static final String c_patternSqlSchema = "^$|^[a-zA-Z0-9]+";
	private static final String c_patternLibraryKey = "[a-zA-Z0-9[\b\\-\\_\\.]]";
	private static final String c_patternPackageName = "^[a-z]{2,3}(\\.([a-z0-9\\_]){1,}){1,}";
	private static final String c_patternLibraryVersionKey = "[a-zA-Z0-9[\b\\-\\.]]";

	/**
	 * Constructeur privé pour éviter l'instanciation de la classe.
	 */
	private FormUtil() {
		// EMPTY.
	}

	/**
	 * Vérification de la validité du caractère saisi pour le nommage d'un projet.
	 * 
	 * @param p_c le caractère à vérifier.
	 * @return la valeur 'true' si le caractère est validé pour la saisie.
	 */
	public static boolean checkKeyForProjectName(final Character p_c) {
		return Pattern.matches(c_patternProjectKey, p_c.toString());
	}

	/**
	 * Vérification de la validité du caractère saisi pour le nommage d'une
	 * librairie.
	 * 
	 * @param p_c le caractère à vérifier.
	 * @return la valeur 'true' si le caractère est validé pour la saisie.
	 */
	public static boolean checkKeyForLibraryName(final Character p_c) {
		return Pattern.matches(c_patternLibraryKey, p_c.toString());
	}

	/**
	 * Vérification de la validité du caractère saisi pour le nommage d'un package.
	 * 
	 * @param p_c le caractère à vérifier.
	 * @return la valeur 'true' si le caractère est validé pour la saisie.
	 */
	public static boolean checkKeyForPackageName(final Character p_c) {
		return Pattern.matches(c_patternPackageKey, p_c.toString());
	}

	/**
	 * Verification de la validite du caractere saisi pour un préfixe.
	 * 
	 * @param p_c
	 * @return
	 */
	public static boolean checkKeyForPrefix(final Character p_c) {
		return Pattern.matches(c_patternSqlOptionKey, p_c.toString());
	}

	/**
	 * Verification de la validite du caractere saisi pour un schéma.
	 * 
	 * @param p_c le caractère à vérifier.
	 * @return la valeur 'true' si le caractère est validé pour la saisie.
	 */
	public static boolean checkKeyForSchema(final Character p_c) {
		return Pattern.matches(c_patternSqlSchemaKey, p_c.toString());
	}

	/**
	 * Verification de la validite de la chaine saisie pour un préfixe.
	 * 
	 * @param p_s la chaine de caractères à vérifier.
	 * @return la valeur 'true' si la chaine de caractères est validée pour la
	 *         saisie.
	 */
	public static boolean checkValueForPrefix(final String p_s) {
		return Pattern.matches(c_patternSqlPrefix, p_s);
	}

	/**
	 * Verification de la validite de la chaine saisie pour un schéma.
	 * 
	 * @param p_s la chaine de caractères à vérifier.
	 * @return la valeur 'true' si la chaine de caractères est validée pour la
	 *         saisie.
	 */
	public static boolean checkValueForSchema(final String p_s) {
		return Pattern.matches(c_patternSqlSchema, p_s);
	}

	/**
	 * Verification de la validite de la chaine saisie pour le nommage d'un package.
	 * 
	 * @param p_s la chaine de caractères à vérifier.
	 * @return la valeur 'true' si la chaine de caractères est validée pour la
	 *         saisie.
	 */
	public static boolean checkValueForPackageName(final String p_s) {
		return Pattern.matches(c_patternPackageName, p_s);
	}

	/**
	 * Verification de la validite du caractere saisi pour un numero de version.
	 * 
	 * @param p_c le caractère à vérifier.
	 * @return la valeur 'true' si le caractère est validé pour la saisie.
	 */
	public static boolean checkKeyForLibraryVersion(final Character p_c) {
		return Pattern.matches(c_patternLibraryVersionKey, p_c.toString());
	}

	/**
	 * Verification de la validite du caractere saisi une valeur numerique.
	 * 
	 * @param p_c le caractère à vérifier.
	 * @return la valeur 'true' si le caractère est validé pour la saisie.
	 */
	public static boolean checkKeyForNumericValue(final Character p_c) {
		return Pattern.matches(c_patternNumeric, p_c.toString());
	}

	/**
	 * Verification de la validite du caractere saisi une valeur alphanumerique.
	 * 
	 * @param p_c le caractère à vérifier.
	 * @return la valeur 'true' si le caractère est validé pour la saisie.
	 */
	public static boolean checkKeyForAlphaNumericValue(final Character p_c) {
		return Pattern.matches(c_patternAlphaNumeric, p_c.toString());
	}

	/**
	 * Verification de la validite du caractere saisi pour le nommage du tablespace
	 * des contraintes.
	 * 
	 * @param p_c le caractère à vérifier.
	 * @return la valeur 'true' si le caractère est validé pour la saisie.rn
	 */
	public static boolean checkKeyForSqlTableSpace(final Character p_c) {
		return Pattern.matches(c_patternSqlOptionKey, p_c.toString());
	}

	/**
	 * Verification de la validite du caractere saisi pour le nom d'une colonne SQL.
	 * 
	 * @param p_c le caractère à vérifier.
	 * @return la valeur 'true' si le caractère est validé pour la saisie.
	 */
	public static boolean checkKeyForSqlColumnName(final Character p_c) {
		return Pattern.matches(c_patternSqlOptionKey, p_c.toString());
	}

	/**
	 * Vérifie que les deux versions Oracles ne peuvent pas être cochées en même
	 * temps.
	 * 
	 * @param _bdNames
	 * @return
	 */
	public static boolean checkForOracleVersion(final List<String> _bdNames) {
		if (_bdNames.contains("Oracle") && _bdNames.contains("Oracle_32"))
			return false;
		return true;
	}

	/**
	 * Verifie la coherence pour la selection des services web.
	 * 
	 * @param p_ws
	 * @param p_wms
	 * @return
	 */
	public static boolean checkForWs(final boolean p_ws, final boolean p_wms) {
		return (p_ws && !p_wms) || (p_ws && p_wms) || (!p_ws && !p_wms);
	}

	/**
	 * Verifie si le projet existe deja. On ne se base pas sur la fonction exists()
	 * du IProject qui n'est pas complete mais directement sur la presence du
	 * fichier dans le repertoire. Cela permet ainsi de detecter les projets effaces
	 * mais toujour presents sur le disque..
	 * 
	 * @param p_appliNames
	 * @return
	 */
	public static boolean checkForNewProject(final String p_appliName) {

		IWorkspaceRoot v_root = ResourcesPlugin.getWorkspace().getRoot();

		if (null == p_appliName || p_appliName.isEmpty())
			return true;

		File v = new File(v_root.getLocationURI().getPath() + File.separator + p_appliName);

		if (v.exists())
			return false;

		return true;
	}

	/**
	 * Verifie si une base de donnees est bien selectionnee (en fonction des autres
	 * options). Tres simple, si pas d'utilisation de bdd, alors la liste est vide,
	 * si utilisation alors la liste contient au moins la base H2, si elle ne
	 * contient que la base H2, alors c'est qu'aucune autre base n'a ete
	 * selectionnee.
	 * 
	 * @param p_databases
	 * @return
	 */
	public static boolean checkForDatabase(final String p_databases) {
		String databases = p_databases.replaceFirst("oracle", "");
		if (databases.indexOf("oracle") != -1)
			return false;
		return true;
	}

	/**
	 * Dans le cas d'une base embarquee, verifie qu'aucune base de donnees autre que
	 * H2 n'a ete selectionnee.
	 * 
	 * @param p_databases
	 * @param p_h2Embedded
	 * @return
	 */
	public static boolean checkForEmbeddedDatabase(final List<String> p_databases, final String p_h2Embedded) {

		if (Boolean.valueOf(p_h2Embedded) && p_databases.size() > 1)
			return false;

		return true;
	}
}
