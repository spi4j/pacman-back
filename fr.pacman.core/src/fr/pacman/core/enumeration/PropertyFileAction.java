package fr.pacman.core.enumeration;

/**
 * Liste des actions disponibles pour une stratégie concernant une ou plusieurs
 * propriétés du gestionnaire de propriétés de Pacman.
 * 
 * @author MINARM
 */
public enum PropertyFileAction {
	ADD("Ajout"), MODIFY("Modification"), REMOVE("Suppression");

	private final String _textToDisplay;

	/**
	 * Constructeur.
	 */
	PropertyFileAction(final String p_textToDisplay) {

		_textToDisplay = p_textToDisplay;
	}

	public String get_textToDisplay() {
		return _textToDisplay;
	}
}
