package fr.pacman.core.property;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import fr.pacman.core.convention.rule.AttributeNamingRule;
import fr.pacman.core.convention.rule.ClassNamingRule;
import fr.pacman.core.convention.rule.CommonNamingRule;
import fr.pacman.core.convention.rule.MethodNamingRule;
import fr.pacman.core.convention.rule.PackageNamingRule;
import fr.pacman.core.convention.rule.ParameterNamingRule;
import fr.pacman.core.convention.rule.VariableNamingRule;
import fr.pacman.core.enumeration.PropertyStatus;
import fr.pacman.core.property.project.ProjectProperties;
import fr.pacman.core.property.project.Spi4jProperties;
import fr.pacman.core.service.LoggerUtils;

/**
 * Gestionnaire de propriétés pour les générateurs Pacman. Cette classe est
 * chargée d'écrire et de lire les différents fichiers de propriétés nécessaires
 * pour stocker l'ensemble des propriétés (options) de génération. Pour
 * l'instant, ces fichiers de propriété sont stockés simplement sous la forme de
 * fichier '.properties', cela est amplement suffisant dans le cadre d'un outil
 * de génération, d'autant que les fichiers sont automatiquement générés.
 * <p>
 * Afin de pouvoir utiliser facilement le gestionnaire dans l'ensemble des
 * classes Java et des fichiers acceleo, cette classe est utilisée en tant que
 * singleton avec des méthodes statiques.
 * 
 * @author MINARM
 */
public class PropertiesHandler {

	/**
	 * Le singleton pour le gestionnaire de propriétés.
	 */
	private static PropertiesHandler _instance;

	/**
	 * Ensemble des propriétés chargées en mémoire.
	 */
	private final PacmanProperties _pacmanProperties;

	/**
	 * Le chemin courant du projet pour les fichiers '.properties'. Ce chemin dépend
	 * notamment du nom du projet.
	 */
	private final String _currentModelPath;

	/**
	 * Si affecté à la valeur 'true', demande l'affichage d'un message avant de
	 * regénérer le (ou les) fichier(s) de type '.properties'.
	 */
	private static boolean _displayMessage;

	/**
	 * La liste des propriétés pour le model courant (_currentModelPath).
	 */
	private static Map<String, PropertiesCategory[]> _referential = new HashMap<String, PropertiesCategory[]>();

	/**
	 * Constructeur. On initialise la map des propriétés avec le référentiel qui a
	 * été préalablement chargé, on met à jour la map des propriétés avec les
	 * valeurs lues dans les différents fichiers '.properties'. Enfin, on affecte le
	 * chemin pour le fichier de modelisation en cours de // traitement
	 * 
	 * @param p_modelPath   le chemin du model courant.
	 * @param p_properties  la liste des propriétés à mettre à jour dans le
	 *                      gestionnaire de propriétés.
	 * @param p_referential le référentiel à charger.
	 */
	private PropertiesHandler(final String p_modelPath, final Properties p_properties,
			PropertiesCategory[] p_referential) {
		_pacmanProperties = new PacmanProperties(p_referential);
		_pacmanProperties.setProperties(p_properties);
		_currentModelPath = p_modelPath;
	}

	/**
	 * Demande l'initialisation du gestionnaire de propriétés et retourne l'instance
	 * unique du gestionnaire. Cette demande est effectuée au niveau de la couche ui
	 * des générateurs, avant le lancement de ces derniers.
	 * <p>
	 * A ce niveau, il faut donc toujours relire les propriétés qui sont suceptibles
	 * d'avoir été modifiées entre deux générations (on injecte donc la valeur null
	 * au niveau du second paramètre afin de demander expressément la relecture des
	 * fichiers).
	 * 
	 * @param p_modelPath le chemin racine du projet de modélisation afin de
	 *                    pourvoir récupérer et lire les différents fichiers de
	 *                    configuration.
	 */
	public static void init(final String p_modelPath) {
		PropertiesHandler.getInstance(p_modelPath, null);
	}

	/**
	 * Demande l'initialisation du gestionnaire de propriétés et retourne l'instance
	 * unique du gestionnaire.
	 * 
	 * @param p_modelPath  le chemin racine du projet de modélisation afin de
	 *                     pourvoir récupérer et lire les différents fichiers de
	 *                     configuration.
	 * @param p_properties la liste de propriétés à injecter directement dans le
	 *                     gestionnaire, ces propriétés ont déjà été chargées par
	 *                     une source quelconque.
	 */
	public static void init(final String p_modelPath, final Properties p_properties) {
		PropertiesHandler.getInstance(p_modelPath, p_properties);
	}

	/**
	 * Demande l'initialisation du gestionnaire de propriétés et retourne l'instance
	 * unique du gestionnaire, dans le cadre de la création d'un nouveau projet
	 * Pacman.
	 * 
	 * @param p_modelPath  le chemin racine du projet de modélisation afin de
	 *                     pourvoir récupérer et lire les différents fichiers de
	 *                     configuration.
	 * @param p_properties la liste des propriétés à injecter directement dans le
	 *                     gestionnaire, ces propriétés sont issues du formulaire de
	 *                     création pour un nouveau projet Pacman.
	 */
	public static void init(String p_modelPath, final Map<String, String> p_properties) {
		Properties properties = new Properties();
		properties.putAll(p_properties);
		init(p_modelPath, properties);
	}

	/**
	 * Retourne l'instance unique du gestionnaire de propriétés pour l'ensemble des
	 * générateurs. On écrase la précédente si elle existe. Si des propriétés
	 * obligatoires sont manquantes dans les différents fichiers (ou si un ou
	 * plusieurs fichiers sont manquants) alors, les fichiers sont automatiquement
	 * réécrits par le gestionnaire de propriétés.
	 * 
	 * @param p_modelPath  le chemin racine pour le stockage des fichiers de
	 *                     propriétés.
	 * @param p_properties la liste de propriétés (si elles existent déjà, par
	 *                     exemple dans le cadre de la création d'un nouveau projet
	 *                     ou les propriétés ont été initialisées par le formulaire
	 *                     de création en fonction du choix utilisateur).
	 * @return l'instance unique du gestionnaire de propriétés
	 */
	private static synchronized PropertiesHandler getInstance(final String p_modelPath, Properties p_properties) {
		if (null == p_properties)
			p_properties = PropertiesUtils.loadPropertyFiles(p_modelPath);
		if (null == _referential.get(p_modelPath))
			_referential.put(p_modelPath, createReferential());
		_instance = new PropertiesHandler(p_modelPath, p_properties, _referential.get(p_modelPath));
		_instance.checkProperties();
		return _instance;
	}

	/**
	 * Creation de la liste des propriétés qui sont potentiellement utilisables par
	 * les différents générateurs. Cela comprend la liste des propriétés pour le
	 * projet et celles qui sont utilisée pour les normes de nommage.
	 * 
	 * @return le référentiel pour le model courant.
	 */
	protected static PropertiesCategory[] createReferential() {
		return new PropertiesCategory[] {

				new ProjectProperties(), new Spi4jProperties(), new AttributeNamingRule(), new MethodNamingRule(),
				new ClassNamingRule(), new VariableNamingRule(), new ParameterNamingRule(), new PackageNamingRule(),
				new CommonNamingRule() };
	}

	/**
	 * Vérification sur l'ensemble des catégories, des propriétés non remplies / non
	 * trouvées au niveau des fichiers de type '.properties'.
	 */
	private void checkProperties() {
		_pacmanProperties.checkProperties();
	}

	/**
	 * Effectue les différentes opérations nécessaires à la clôture du gestionnaire
	 * de propriétés. Va écrire les différents fichiers de propriétés si le projet
	 * est en phase de création ou réécrire un fichier de propriété si une ou
	 * plusieurs propriétés obligatoires sont manquantes dans le(s) fichier(s).
	 * 
	 * @throws IOException
	 */
	public static void exit() throws IOException {
		List<PacmanProperty> writeProperties = _instance._pacmanProperties.getWriteProperties();
		if (null != writeProperties && !writeProperties.isEmpty()) {
			if (_displayMessage) {
			}
			_instance.writePropertiesFile();
		}
	}

	/**
	 * Ecriture des différents fichiers de propriétés. La seule et unique référence
	 * étant au niveau des propriétés présentes dans les classes filles de
	 * 'PropertiesCategory', on va écrire les propriétés dans des fichiers de type
	 * '.properties' à partir des différentes instances de 'PacmanProperty' qui ont
	 * été mises à jour.
	 */
	public void writePropertiesFile() throws IOException {
		List<PacmanProperty> v_additionalProperties = new ArrayList<>();
		for (PropertiesCategory v_pacmanPropertiesCategory : _referential.get(_currentModelPath)) {
			List<PacmanProperty> v_propertiesToFile = new ArrayList<PacmanProperty>();
			String v_propertiesFileName = v_pacmanPropertiesCategory.get_propertiesFileName();
			for (Entry<String, PacmanProperty> v_entry : getPacmanProperties().entrySet()) {
				if (v_propertiesFileName.equals(v_entry.getValue().getPropertyFileName())
						&& v_entry.getValue().is_writeToFile())
					v_propertiesToFile.add(v_entry.getValue());

				else if (v_entry.getValue().getPropertyFileName().isEmpty())
					v_additionalProperties.add(v_entry.getValue());
			}
			if (v_pacmanPropertiesCategory.is_defaultFileForAdditionalproperties())
				v_propertiesToFile.addAll(v_additionalProperties);

			File v_propertiesDirpath = PropertiesUtils.getPropertiesDirectory(_currentModelPath);
			PropertiesHandler.writeProperties(new File(v_propertiesDirpath.getPath()), v_propertiesFileName,
					v_propertiesToFile);
		}
	}

	/**
	 * Construction et écriture d'un fichier '.properties', avec les différents
	 * attributs ainsi que leurs valeurs par défaut.
	 * 
	 * @param p_directory
	 * @param p_fileName
	 * @param p_properties
	 * @throws IOException
	 */
	private static void writeProperties(final File p_directory, final String p_fileName,
			final List<PacmanProperty> p_properties) throws IOException {
		FileOutputStream fos = null;
		try {
			String fileName = p_fileName;
			fos = new FileOutputStream(p_directory.getPath() + File.separator + fileName, false);
			Collections.sort(p_properties);

			for (PacmanProperty property : p_properties) {
				if (PropertyStatus.MEMORY != property.getDefaultStatus())
					fos.write(property.toStringProperties());
			}
		} catch (IOException e) {
			LoggerUtils.warn(e.getMessage());
			throw e;
		} finally {
			try {
				if (null != fos)
					fos.close();
			} catch (IOException e) {
				// RAS.
			}
		}
	}

	/**
	 * Retourne la valeur de la propriété en fonction de sa clé.
	 * 
	 * @param p_key la clé unique de la propriété.
	 * @return la valeur de la propriété, toujours sous forme de string.
	 */
	private String getPacmanProperty(final String p_key) {
		final PacmanProperty property = _pacmanProperties.getPacmanProperty(p_key);
		if (null == property) {
			LoggerUtils.warn("La propriété : " + p_key + " n'a pas été trouvée dans les fichiers de configuration");
			return null;
		}
		return property.getValue().toString();
	}

	/**
	 * Retourne la valeur d'une propriété en fonction de sa clé.
	 * 
	 * @param p_key la clé unique de la propriété.
	 * @return la valeur de la propriété, toujours sous forme de string.
	 */
	public static String getProperty(final String p_key) {
		return _instance.getPacmanProperty(p_key);
	}

	/**
	 * Retourne la liste complète des propriétés au format Pacman.
	 * 
	 * @return _properties la liste complète des propriétés sous format Pacman.
	 */
	public Map<String, PacmanProperty> getPacmanProperties() {
		return _pacmanProperties.getPacmanProperties();
	}

	/**
	 * Retourne la liste de toutes les proprietes qui sont utilisées exclusivement
	 * dans le cadre de la normalisation.
	 *
	 * @return la liste de toutes les propriétés liées à la normalisation.
	 */
	public static Properties getProperties() {
		return _instance._pacmanProperties.getNormeProperties();
	}
}
