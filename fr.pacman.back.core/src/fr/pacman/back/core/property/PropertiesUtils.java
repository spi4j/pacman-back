package fr.pacman.back.core.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import fr.pacman.back.core.service.LoggerUtils;

/**
 * Classe utilitaire pour la manipulation des différents fichiers de propriétés.
 * 
 * @author MINARM
 */
public class PropertiesUtils {

	/**
	 * Le nom par défaut du répertoire pour le stockage des fichiers '.properties'.
	 */
	private final static String c_dirProperties = "pacman-properties";

	/**
	 * Extension à rechercher pour les fichiers présents dans le répertoire de
	 * stockage. Pour l'instant toutes les propriétés sont stockées sous la frome de
	 * fichiers '.properties'.
	 */
	private final static String c_fileExtension = ".properties";

	/**
	 * Charge et retourne l'ensemble des propriétés lues à partir des différents
	 * fichiers de configuration. Ces fichiers sont pour l'instant, toujours stockés
	 * sour la forme de fichiers '.properties'.
	 * 
	 * On récupère le répertoire ou sont stockés les fichiers '.properties', on
	 * récupère la liste des fichiers puis on demande le chargement de l'ensemble
	 * des propriétés.
	 * 
	 * @param p_modelPath le chemin racine pour le stockage des différents fichiers
	 *                    de propriétés.
	 * @return le contenu (clé/valeur) de l'ensemble des fichiers de propriétés.
	 */
	static Properties loadPropertyFiles(final String p_modelPath) {
		Properties properties = new Properties();
		try {
			File currentDirectory = getPropertiesDirectory(p_modelPath);
			String[] lstPropertiesFiles = getPropertyFiles(currentDirectory);
			properties = loadProperties(lstPropertiesFiles, currentDirectory);

		} catch (final FileNotFoundException err) {
			throw new IllegalArgumentException("Problème pour ouvrir un fichier : ", err);
		} catch (final IOException err) {
			throw new IllegalArgumentException(
					"Problème lors de l'écriture d'une propriété : ou de la fermeture du fichier", err);
		} catch (final IllegalArgumentException err) {
			throw new IllegalArgumentException("Problème lors du getInstance()" + err);
		}
		return properties;
	}

	/**
	 * Retourne la liste des chemins pour les différents fichiers '.properties' qui
	 * sont présents dans le répertoire qui a été passé en paramètre.
	 * 
	 * @param p_repertoire le répertoire ou l'on va tenter de chercher les
	 *                     différents fichiers '.properties'.
	 * @return la liste des chemins pour les fichiers de propriété stockés dans le
	 *         répertoire
	 */
	private static String[] getPropertyFiles(final File p_directory) {
		return p_directory.list(new FilenameFilter() {
			@Override
			public boolean accept(final File p_dir, final String p_name) {
				boolean v_isPropertyFile = false;
				if (p_name.endsWith(c_fileExtension)) {
					v_isPropertyFile = true;
				}
				return v_isPropertyFile;
			}
		});
	}

	/**
	 * Chargement des propriétés à partir des fichiers de type '.properties'. On
	 * itère sur la liste des fichiers, chaque fichier est lu et les propriétés sont
	 * stockées sous la forme traditionelle de pair (clé/valeur). voir
	 * {@link Properties}).
	 * 
	 * On en profite pour vérifier si des doublons sont présents dans la liste des
	 * propriétés, pour l'instant seul un avertissement est levé pour les logs.
	 * 
	 * @param p_listFiles la liste des chemins pour les différents fichiers de
	 *                    propriété.
	 * @param p_directory le répertoire de stockage.
	 * @returnl'ensemble des propriétés sous forme de paire (clé/valeur).
	 * @throws IOException une éventuelle exception soulevée lors du traitement
	 */
	private static Properties loadProperties(final String[] p_listFiles, final File p_directory) throws IOException {

		// _displayMessage = Boolean.TRUE;
		final Properties properties = new Properties();
		FileInputStream fis;
		for (final String file : p_listFiles) {
			final File propertyFile = new File(p_directory, file);
			final Properties tempFile = new Properties();
			final FileInputStream fisTemp = new FileInputStream(propertyFile);
			fis = new FileInputStream(propertyFile);
			try {
				tempFile.load(fisTemp);
				for (final Object propertyKey : tempFile.keySet()) {
					if (properties.containsKey(propertyKey))
						LoggerUtils.warn("Propriété en doublon dans les fichiers : " + propertyKey);
				}
				properties.load(fis);
			} finally {
				if (null != fisTemp) {
					fisTemp.close();
				}

				if (null != fis) {
					fis.close();
				}
			}
		}
		return properties;
	}

	/**
	 * Retourne le répertoire pour le stockage des fichiers '.properties'. Si le
	 * répertoire n'est pas trouvé du premier coup, on tente trois itérations en
	 * parcourant l'arbre.
	 * 
	 * @param p_modelPath le chemin racine pour le stockage des différents fichiers
	 *                    de propriétés
	 * @return le répertoire de stockage pour les différents fichiers de
	 *         configuration
	 */
	public static File getPropertiesDirectory(final String p_modelPath) {
		final int nbMaxIterations = 3;
		int iteration = 1;
		File currentDirectory = new File(p_modelPath);
		String pathDirProperty = getSubDirectoryProperties(currentDirectory);
		while ((pathDirProperty == null) && (currentDirectory != null) && iteration < nbMaxIterations) {
			currentDirectory = currentDirectory.getParentFile();
			pathDirProperty = getSubDirectoryProperties(currentDirectory);
			iteration++;
		}
		if (null != pathDirProperty)
			currentDirectory = new File(pathDirProperty);
		return currentDirectory;
	}

	/**
	 * Retourne le sous-répertoire (si il existe et si les fichiers n'ont pas été
	 * trouvés dans le répertoire initialement passé en paramètre) de stockage des
	 * fichiers '.properties' pour le répertoire passé en paramètre.
	 * 
	 * @param p_directory le répertoire ou l'on cherche initialement les fichiers de
	 *                    propriétés
	 * @return la liste des path de proprietes du repertoire
	 */
	private static String getSubDirectoryProperties(final File p_directory) {
		final String[] dirFiles = p_directory.list(new FilenameFilter() {
			@Override
			public boolean accept(final File p_dir, final String p_name) {
				boolean isDirectoryProperty = false;
				if (p_name.contains(c_dirProperties) && (p_dir.isDirectory()))
					isDirectoryProperty = true;
				return isDirectoryProperty;
			}
		});
		if (dirFiles.length > 0)
			return p_directory + File.separator + dirFiles[0];
		return null;
	}
}
