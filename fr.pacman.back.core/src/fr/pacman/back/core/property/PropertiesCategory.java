package fr.pacman.back.core.property;

import fr.pacman.back.core.convention.NotationResolution;

/**
 * Retourne le comportement attendu pour une classe qui doit gérer des
 * propriétés concernées par le gestionnaire interne de propriétés de Pacman.
 * 
 * @author MINARM
 */
public abstract class PropertiesCategory {
	/**
	 * Tableau contenant l'ensemble des propriétés préalablement définies.
	 */
	private final PacmanProperty[] _tabPacmanProperties;

	/**
	 * Initialise une catégorie de propriétés.
	 */
	protected PropertiesCategory() {
		_tabPacmanProperties = initPacmanProperties();
		completePacmanProperties();
	}

	/**
	 * Mise à jour des éléments avec les infos de la catégorie.
	 */
	private void completePacmanProperties() {
		for (int i = 0; i < _tabPacmanProperties.length; i++) {
			_tabPacmanProperties[i].setPropertyFileName(get_propertiesFileName());
		}
	}

	/**
	 * Initialise les propriétés de cette catégorie.
	 * 
	 * @return les propriétés de cette catégorie.
	 */
	protected abstract PacmanProperty[] initPacmanProperties();

	/**
	 * Récupère le nom du fichier de propriétés pour stockage de cette catégorie.
	 * 
	 * @return le nom du fichier de propriétés.
	 */
	protected abstract String get_propertiesFileName();

	/**
	 * Indique si le fichier doit prendre en compte les propriétés additionnelles.
	 * 
	 * @return 'true' si le fichier récupère les propriétés additionnelles du
	 *         développeur.
	 */
	protected abstract boolean is_defaultFileForAdditionalproperties();

	/**
	 * Obtenir la liste des @{@link PacmanProperty} décrivant les différentes
	 * propriétés attendues par Pacman.
	 * 
	 * @return La liste des propriétés.
	 */
	protected PacmanProperty[] getTabPacmanProperties() {
		return _tabPacmanProperties;
	}

	/**
	 * Applique la norme désirée sur la valeur passée en parametre.
	 * 
	 * @param p_value        La valeur originale (ex : "Imputation de gestion").
	 * @param p_propertyName la clé de la propriete
	 * 
	 * @return La valeur respectant la norme.
	 */
	protected static String applyNorme(final String p_value, final String p_propertyName) {
		final NotationResolution notationResolution = new NotationResolution(
				PropertiesHandler.getProperty(p_propertyName));
		return notationResolution.applyNorme(p_value, PropertiesHandler.getProperties());
	}
}
