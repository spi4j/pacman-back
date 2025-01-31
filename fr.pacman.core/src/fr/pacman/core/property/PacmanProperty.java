package fr.pacman.core.property;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import fr.pacman.core.enumeration.PropertyFileAction;
import fr.pacman.core.enumeration.PropertyStatus;

/**
 * Represente un élément de propriété dans le cadre du paramétrage de PacMan.
 * 
 * A utiliser pour avoir la liste des différentes propriétés utilisées par
 * PacMan, et générer des fichiers de propriétés structurés avec les valeurs par
 * défaut pour chaque élément. Cette classe est 'encapsulée' sous forme de liste
 * au niveau de son conteneur {@link PacmanProperties}.
 * 
 * @author MINARM
 */
public class PacmanProperty implements Comparable<PacmanProperty> {

	/**
	 * Compteur incrementé pour l'ordre d'écriture dans les fichiers.
	 */
	private static int _incWriteOrder;

	/**
	 * L'identifiant (clé) de la propriété.
	 */
	private final String _id;

	/**
	 * La liste des valeurs par défaut fixées pour la propriété.
	 */
	private final String[] _defaultValues;

	/**
	 * La valeur a utiliser pour la propriété.
	 */
	private String _value;

	/**
	 * La strategie à appliquer lors de la modification de cette propriété.
	 */
	private final PropertyStrategy _strategy;

	/**
	 * Le statut de référence de la propriété.
	 */
	private final PropertyStatus _defaultStatus;

	/**
	 * Le statut de la propriété.
	 */
	private PropertyStatus _status;

	/**
	 * Le fichier '.properties' pour cette propriété.
	 */
	private String _propertyFileName;

	/**
	 * L'ordre d'affichage dans le fichier '.properties'.
	 */
	private int _order;

	/**
	 * Le commentaire à afficher dans le fichier '.properties'.
	 */
	private final String _comment;

	/**
	 * Action nécessitant la réecriture du fichier de configuration.
	 */
	private PropertyFileAction _fileAction;

	/**
	 * Constructeur privé.
	 * 
	 * @param p_id           la clé pour l'élément.
	 * @param p_defaultValue la valeur par défaut pour l'élément.
	 * @param p_comment      le commentaire à afficher dans le fichier
	 *                       '.properties'.
	 * @param p_status       le statut pour l'élément.
	 * @param p_strategy     la strategie (si elle existe) pour l'élément.
	 */
	private PacmanProperty(final String p_id, final String[] p_defaultValues, final String p_comment,
			final PropertyStatus p_status, final PropertyStrategy p_strategy) {
		_id = p_id;
		_status = p_status;
		_comment = p_comment;
		_strategy = p_strategy;
		_order = _incWriteOrder;
		_defaultStatus = p_status;
		_value = p_defaultValues[0];
		_defaultValues = p_defaultValues;

		// Incrementation du compteur d'ecriture.
		_incWriteOrder++;

		if (null != p_strategy)
			p_strategy.setRefPacmanProperty(this);
	}

	/**
	 * Creation d'une propriété obligatoire.
	 * 
	 * @param p_id           la clé de la propriété.
	 * @param p_defaultValue la valeur par défaut de la propriété.
	 * @param p_comment      le commentaire à afficher pour la propriété.
	 * @return l'instance de la propriété.
	 */
	public static PacmanProperty newRequired(final String p_id, final String p_defaultValue, final String p_comment) {
		return new PacmanProperty(p_id, new String[] { p_defaultValue }, p_comment, PropertyStatus.REQUIRED, null);
	}

	/**
	 * Creation d'une propriété obligatoire.
	 * 
	 * @param p_id            la clé de la propriété.
	 * @param p_defaultValues les valeurs par défaut de la propriété.
	 * @param p_comment       le commentaire à afficher pour la propriété.
	 * @return l'instance de la propriété.
	 */
	public static PacmanProperty newRequired(final String p_id, final String[] p_defaultValues,
			final String p_comment) {
		return new PacmanProperty(p_id, p_defaultValues, p_comment, PropertyStatus.REQUIRED, null);
	}

	/**
	 * Creation d'une propriété obligatoire avec une stratégie.
	 * 
	 * @param p_id           la clé de la propriété.
	 * @param p_defaultValue la valeur par défaut de la propriété.
	 * @param p_comment      le commentaire à afficher pour la propriété.
	 * @param p_strategy     la stratégie à appliquer en fonction de la valeur de la
	 *                       propriété.
	 * @return l'instance de la propriété.
	 */
	public static PacmanProperty newRequired(final String p_id, final String p_defaultValue, final String p_comment,
			final PropertyStrategy p_strategy) {
		return new PacmanProperty(p_id, new String[] { p_defaultValue }, p_comment, PropertyStatus.REQUIRED,
				p_strategy);
	}

	/**
	 * Creation d'une propriété soumise à condition(s).
	 * 
	 * @param p_id           la clé de la propriété.
	 * @param p_defaultValue la valeur par défaut de la propriété.
	 * @param p_comment      le commentaire à afficher pour la propriété.
	 * @return l'instance de la propriété.
	 */
	public static PacmanProperty newConditional(final String p_id, final String p_defaultValue,
			final String p_comment) {
		return new PacmanProperty(p_id, new String[] { p_defaultValue }, p_comment, PropertyStatus.CONDITIONAL, null);
	}

	/**
	 * Creation d'une propriété soumise a condition.
	 * 
	 * @param p_id           la clé de la propriété.
	 * @param p_defaultValue la valeur par défaut de la propriété.
	 * @param p_comment      le commentaire à afficher pour la propriété.
	 * @return l'instance de la propriété.
	 */
	public static PacmanProperty newConditional(final String p_id, final String[] p_defaultValues,
			final String p_comment) {
		return new PacmanProperty(p_id, p_defaultValues, p_comment, PropertyStatus.CONDITIONAL, null);
	}

	/**
	 * Creation d'une propriété optionnelle, pas de controle d'existence sur cette
	 * propriété .
	 * 
	 * @param p_id           la clé de la propriété.
	 * @param p_defaultValue la valeur par défaut de la propriété.
	 * @param p_comment      le commentaire à afficher pour la propriété.
	 * @return l'instance de la propriété.
	 */
	public static PacmanProperty newOptional(final String p_id, final String p_defaultValue, final String p_comment) {
		return new PacmanProperty(p_id, new String[] { p_defaultValue }, p_comment, PropertyStatus.OPTIONAL, null);
	}

	/**
	 * Creation d'une propriété additionnelle (utilisateur).
	 * 
	 * @param p_id           la clé de la propriété.
	 * @param p_defaultValue la valeur par défaut de la propriété.
	 * @param p_comment      le commentaire à afficher pour la propriété.
	 * @return l'instance de la propriété.
	 */
	public static PacmanProperty newAdditional(final String p_id, final String p_defaultValue, final String p_comment) {
		return new PacmanProperty(p_id, new String[] { p_defaultValue }, p_comment, PropertyStatus.CONDITIONAL, null);
	}

	/**
	 * Creation d'une propriété en mémoire uniquement (pas de stockage dans un
	 * fichier .properties), pas de controle d'existence sur cette propriété .
	 * 
	 * @param p_id           la clé de la propriété.
	 * @param p_defaultValue la valeur par défaut de la propriété.
	 * @param p_comment      le commentaire à afficher pour la propriété.
	 * @return l'instance de la propriété.
	 */
	public static PacmanProperty newMemoryOnly(final String p_id, final String p_defaultValue, final String p_comment) {
		return new PacmanProperty(p_id, new String[] { p_defaultValue }, p_comment, PropertyStatus.MEMORY, null);
	}

	/**
	 * Creation de la ligne d'écriture pour l'élément.
	 */
	@Override
	public String toString() {
		String value;
		if (null != _comment && _comment.trim().length() > 0) {
			value = "# " + _comment + "\n";
		} else {
			value = "";
		}
		return value + _id + " = " + _value + "\n";
	}

	/**
	 * Met en forme l'élément pour un fichier '.properties' ex : #identifiant de
	 * l'application idAppli = appwhite1.
	 * 
	 * @return La propriété mise en forme pour les '.properties' directement sous le
	 *         bon format "ISO-8859-1".
	 * 
	 * @throws UnsupportedEncodingException une exception d'encodage levée lors de
	 *                                      traitement.
	 */
	protected byte[] toStringProperties() throws UnsupportedEncodingException {
		return toString().getBytes("ISO-8859-1");
	}

	/**
	 * Permet de trier les différentes propriétés pour dans le cadre de l'écriture
	 * du fichier '.properties'.
	 */
	@Override
	public int compareTo(PacmanProperty p_o) {
		return (_order - p_o._order);
	}

	/**
	 * Execute la stratégie de l'élément.
	 * 
	 * @param p_pacmanProperties la liste globale des propriétés.
	 */
	protected void applyStrategy(final Map<String, PacmanProperty> p_pacmanProperties) {
		if (hasStrategy())
			_strategy.applyStrategy(p_pacmanProperties);
	}

	/**
	 * Affecte le statut de l'élément. Cette méthode est le pilier pour le
	 * fonctionnement du {@link PropertiesHandler}.
	 * 
	 * On ne fait que demander le positionnement d'un statut avec différentes règles
	 * de gestion qui sont appliquées pour définir ce statut. Ne modifier qu'en
	 * véritable connaissance de cause !
	 * 
	 * @param p_status le statut (enumeration) de l'élément.
	 */
	public void setStatus(PropertyStatus p_status) {
		if (PropertyStatus.FILLED == p_status) {
			if (PropertyStatus.REQUIRED == getDefaultStatus())
				_status = p_status;

			if (PropertyStatus.CONDITIONAL == getDefaultStatus())
				_status = PropertyStatus.STANDBY;
		}
		if (PropertyStatus.REQUIRED == p_status) {
			if (PropertyStatus.STANDBY == _status)
				_status = PropertyStatus.FILLED;

			if (PropertyStatus.CONDITIONAL == _status)
				_status = p_status;
		}
	}

	/**
	 * Remise à blanc pour le statut courant.
	 */
	protected void reset() {
		_status = _defaultStatus;
	}

	/**
	 * Affecte le nom du fichier '.properties' dans lequel est doit etre stockée la
	 * propriété.
	 * 
	 * @param _propertyFileName le nom du fichier '.properties' pour la propriété.
	 */
	protected void setPropertyFileName(String p_propertyFileName) {
		_propertyFileName = p_propertyFileName;
	}

	/**
	 * Affecte la valeur a utiliser pour la propriété. Si la propriété est soumise à
	 * une stratégie alors on positionne de suite sa valeur dans la stratégie afin
	 * de pouvoir correctement gérer le trigger dans le cas d'un ONCHANGE...
	 * 
	 * @param _value la nouvelle valeur pour la propriété.
	 */
	public void setValue(final String p_value) {
		_value = p_value;
		setStatus(PropertyStatus.FILLED);
		if (hasStrategy())
			getStrategy().updateWithOldRefValue(_value);
	}

	/**
	 * Modifie une partie de la valeur (cas des WildCards).
	 * 
	 * @param p_value la nouvelle valeur pour la propriété.
	 */
	public void updateValue(final String p_value) {
		_value = p_value;
	}

	/**
	 * Modifie la valeur de l'élément avec une valeur par défaut. On force le
	 * statut. Si la valeur est correcte, il est inutile de la modifier, par
	 * ailleurs si on est sur stratégie de type 'ONCHANGE', cela empêche l'écriture
	 * au démarrage du studio.
	 * 
	 * @param p_defaultValueIdx l'index dans le tableau des valeurs par défaut.
	 */
	public void setValueFromIndexedDefaultValue(int p_idx) {
		if (p_idx <= _defaultValues.length) {
			if (_value.equals(_defaultValues[p_idx]))
				return;
			_value = _defaultValues[p_idx];
			reset();
		}
	}

	/**
	 * Les propriétés à écrire dans les fichiers sont toutes les propriétés
	 * obligatoires non remplies et toutes les propriétés remplies.
	 * 
	 * @return un indicateur pour savoir si la propriété doit être écrite dans le
	 *         fichier de propriétés.
	 */
	protected boolean is_writeToFile() {
		if (PropertyStatus.REQUIRED == _status)
			return Boolean.TRUE;

		if (PropertyStatus.FILLED == _status)
			return Boolean.TRUE;

		return Boolean.FALSE;
	}

	/**
	 * Recupere le nom du fichier '.properties' dans lequel est stockee la
	 * propriété.
	 * 
	 * @return _propertyFileName le nom du fichier de propriété.
	 */
	public String getPropertyFileName() {
		return _propertyFileName;
	}

	/**
	 * Obtenir l'ordre d'affichage de la propriété dans le fichier '.properties'.
	 * 
	 * @return _order l'ordre d'affichage pour la propriété.
	 */
	protected int getOrder() {
		return _order;
	}

	/**
	 * Obtenir le statut par défaut de l'élément (il s'agit du statut de référence).
	 * 
	 * @return _defaultStatus le statut de référence.
	 */
	protected PropertyStatus getDefaultStatus() {
		return _defaultStatus;
	}

	/**
	 * Obtenir la statut courant de la propriété.
	 * 
	 * @return _status le statut courant de la propriété.
	 */
	protected PropertyStatus getStatus() {
		return _status;
	}

	/**
	 * Obtenir la valeur de l'élément.
	 * 
	 * @return _value la valeur à utiliser
	 */
	public String getValue() {
		return _value;
	}

	/**
	 * Obtenir la cle de l'élément.
	 * 
	 * @return _id la cle de l'élément.
	 */
	public String getId() {
		return _id;
	}

	/**
	 * Obetnir le commentaire pour l'élément.
	 * 
	 * @return _comment le commentaire pour l'élément.
	 */
	protected String getComment() {
		return _comment;
	}

	/**
	 * Les propriétés à écrire dans les fichiers sont toutes les propriétés
	 * obligatoires non remplies et toutes les propriétés remplies.
	 * 
	 * @return _writeToFile indicateur pour savoir si la propriété doit être écrite
	 *         dans le fichier '.properties'.
	 */
	protected boolean isWriteToFile() {
		if (PropertyStatus.REQUIRED == _status)
			return true;
		if (PropertyStatus.FILLED == _status)
			return true;
		return false;
	}

	/**
	 * Obtenir la strategie conjointe a appliquer.
	 * 
	 * @return la strategie (si elle existe).
	 */
	protected PropertyStrategy getStrategy() {
		return _strategy;
	}

	/**
	 * Raccourci pour savoir si l'élement a une stratégie associée.
	 * 
	 * @return 'true' si l'élément à une stratégie associée.
	 */
	protected boolean hasStrategy() {
		return (null != _strategy);
	}

	/**
	 * Retourne l'action qui nécessite la réecriture du fichier dans lequel est
	 * stockée cette propriété.
	 * 
	 * @return l'action à effectuer pour le fichier de configuration.
	 */
	public PropertyFileAction getFileAction() {
		return _fileAction;
	}

	/**
	 * Affecte l'action qui nécessite la réecriture du fichier dans lequel est
	 * stockée cette propriété.
	 * 
	 * @param p_fileAction : l'action à effectuer pour le fichier de configuration.
	 */
	public void setFileAction(PropertyFileAction p_fileAction) {
		_fileAction = p_fileAction;
	}
}
