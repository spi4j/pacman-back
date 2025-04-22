package fr.pacman.core.property;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import fr.pacman.core.enumeration.PropertyFileAction;
import fr.pacman.core.enumeration.PropertyStatus;

/**
 * Cette classe permet de stocker des propriétés 'enrichies' dans le cadre du
 * gestionnaire interne de propriétés de Pacman. Il s'agit d'un conteneur pour
 * l'ensemble des propriétés de type : {@link PacmanProperty}.
 * 
 * @author MINARM
 */
public class PacmanProperties {
	/**
	 * Stocke l'ensemble des propriétés sous la forme de liste de
	 * {@link PacmanProperty}'.
	 */
	private final Map<String, PacmanProperty> _pacmanProperties;

	/**
	 * Stocke les propriétés dédiées uniquement à la normalisation (normes de
	 * nommage) sous forme de 'properties'.
	 */
	private Properties _normeProperties;

	/**
	 * Liste des propriétés qui demandent la réecriture des fichiers. Si à la fin du
	 * contrôle qui suit la lecture des propriétés, cette liste n'est pas vide,
	 * alors la réecriture du fichier '.properties' concerné est demandée par le
	 * gestionnaire de propriétés {@link PropertiesHandler}.
	 */
	private List<PacmanProperty> _writeProperties;

	/**
	 * Constructeur.
	 * 
	 * @param p_categories la liste des catégories préalablement enregistrées.
	 */
	protected PacmanProperties(final PropertiesCategory[] p_categories) {
		_pacmanProperties = new Hashtable<String, PacmanProperty>();
		initPacmanProperties(p_categories);
	}

	/**
	 * Initialise les propriétés à partir des catégories préalablement enregistrées.
	 * 
	 * @param p_categories la liste des catégories enregistrées.
	 */
	private void initPacmanProperties(final PropertiesCategory[] p_categories) {
		for (PropertiesCategory pacmanPropertiesCategory : p_categories) {
			for (PacmanProperty pacmanProperty : pacmanPropertiesCategory.getTabPacmanProperties()) {
				pacmanProperty.reset();
				_pacmanProperties.put(pacmanProperty.getId(), pacmanProperty);
			}
		}
	}

	/**
	 * Vérifie l'ensemble des propriétés et demande la réécriture si besoin. Pour
	 * l'instant on ne traite pas encore la modification (a voir..).
	 * 
	 * On vérifie en premier lieu les propriétés obligatoires qui ne sont pas
	 * présentes dans le fichier, puis on traite les propriétés conditionnelles qui
	 * ne sont plus utilisées.
	 */
	protected void checkProperties() {
		for (Entry<String, PacmanProperty> v_entry : _pacmanProperties.entrySet()) {
			if (v_entry.getValue().getStatus() == PropertyStatus.REQUIRED) {
				addToWriteProperties(v_entry, PropertyFileAction.ADD);
			}
			if (v_entry.getValue().getStatus() == PropertyStatus.STANDBY) {
				addToWriteProperties(v_entry, PropertyFileAction.REMOVE);
			}
		}
	}

	/**
	 * Mise à jour des valeurs par défaut. Il peut aussi y avoir des propriétés
	 * rajoutées par le développeur dans le fichier '.properties' et qui ne peuvent
	 * pas être connues par avance. Dans ce cas on les enregistre simplement dans la
	 * liste. Pour l'instant les propriétés additionnelles ne peuvent etre remontées
	 * au referentiel, elles sont donc perdues si supprimées du fichier (tout au
	 * moins au niveau des valeurs).
	 * 
	 * On liste l'ensemble des propriétés lues à partir des fichiers, on récupère la
	 * propriété Pacman dans la copie du referentiel, si la propriété est trouvée
	 * dans le référentiel, on met à jour le référentiel avec la valeur qui a été
	 * récupérée. Sinon, on rajoute simplement la propriété dans la liste.
	 * 
	 * Les stratégies sont toujours appliquées en dernier.
	 * 
	 * @param p_props la liste des propriétés récupérées à partir des fichiers
	 *                '.properties'.
	 * @return 'true' si l'affectation de la propriété a bien été effectué.
	 */
	protected boolean setProperties(final Properties p_props) {

		List<PacmanProperty> strategies = new ArrayList<PacmanProperty>();
		if (null == p_props || p_props.size() == 0)
			return false;

		for (String key : p_props.stringPropertyNames()) {

			PacmanProperty pacmanProperty = _pacmanProperties.get(key);
			if (null != pacmanProperty) {
				if (pacmanProperty.hasStrategy())
					strategies.add(pacmanProperty);
				pacmanProperty.setValue((String) p_props.getProperty(key));
			} else {
				setAdditionalUserProperty(key, p_props.getProperty(key));
			}
		}

		for (PacmanProperty pacmanProperty : strategies)
			pacmanProperty.applyStrategy(_pacmanProperties);
		return true;
	}

	/**
	 * Ajoute une propriété additionnelle dans le référentiel.
	 * 
	 * @param p_key   la clé de la propriété.
	 * @param p_value la valeur de la propriété.
	 */
	protected void setAdditionalUserProperty(final String p_key, final String p_value) {

		PacmanProperty newProperty = PacmanProperty.newAdditional(p_key, p_value,
				"Champ additionnel, specifique utilisateur.");
		newProperty.setPropertyFileName("");
		_pacmanProperties.put(p_key, newProperty);
	}

	/**
	 * Verifie l'ensemble des propriétés et demande la réecriture si besoin. Pour
	 * l'instant on ne traite pas encore la modification. On vérifie donc les
	 * propriétés obligatoires mais non présentes dans le fichier et on demande la
	 * suppression des propriétés conditionnelles qui ne sont éventuellement plus
	 * utilisées.
	 */
	protected void validateProperties() {
		for (Entry<String, PacmanProperty> entry : _pacmanProperties.entrySet()) {
			if (entry.getValue().getStatus() == PropertyStatus.REQUIRED) {
				addToWriteProperties(entry, PropertyFileAction.ADD);
			}
			if (entry.getValue().getStatus() == PropertyStatus.STANDBY) {
				addToWriteProperties(entry, PropertyFileAction.REMOVE);
			}
		}
	}

	/**
	 * Ajoute la propriété à écrire dans la liste des propriétés qui nécessitent la
	 * réecriture des fichiers.
	 * 
	 * @param p_entry      : la propriété qui nécessite la réecriture des fichiers
	 *                     de propriété.
	 * 
	 * @param p_fileAction : action à effectuer dans le fichier de configuration.
	 */
	private void addToWriteProperties(final Entry<String, PacmanProperty> p_entry,
			final PropertyFileAction p_fileAction) {
		if (null == _writeProperties) {
			_writeProperties = new ArrayList<>();
		}
		p_entry.getValue().setFileAction(p_fileAction);
		_writeProperties.add(p_entry.getValue());
	}

	/**
	 * Obtenir la propriété à partir de sa clé.
	 * 
	 * @param p_key la clé de la propriété à récuperer.
	 * 
	 * @return la propriété au format Pacman.
	 * 
	 * @TODO : gérer les nulls
	 */
	protected PacmanProperty getPacmanProperty(final String p_key) {
		return ((PacmanProperty) _pacmanProperties.get(p_key));
	}

	/**
	 * Obtenir l'ensemble des propriétés sous format Pacman.
	 * 
	 * @return la liste de l'ensemble des propriétés (format Pacman).
	 */
	protected Map<String, PacmanProperty> getPacmanProperties() {
		return _pacmanProperties;
	}

	/**
	 * Obtenir uniquement les propriétés liées à la norme pour le nommage des
	 * différentes classes à générer.
	 * 
	 * @return la liste des propriétés liées au nommage.
	 */
	protected Properties getNormeProperties() {
		if (null == _normeProperties) {
			_normeProperties = new Properties();
			for (Entry<String, PacmanProperty> entry : _pacmanProperties.entrySet())
				_normeProperties.put(entry.getKey(), entry.getValue().getValue());
		}
		return _normeProperties;
	}

	/**
	 * Retourne au manager de propriété {@link PropertiesHandler} la liste
	 * éventuelle des propriétés qui nécessitent la réecriture des fichiers.
	 * 
	 * @return la liste des propriétés qui nécessitent la réecriture des fichiers.
	 */
	protected List<PacmanProperty> getWriteProperties() {
		return _writeProperties;
	}
}
