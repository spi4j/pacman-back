package fr.pacman.back.start.ui.util;

import org.eclipse.jface.dialogs.IMessageProvider;

/**
 * 
 * @author MINARM.
 *
 */
public class ValidatorUtil {

	public static ValidatorUtil INSTANCE = new ValidatorUtil();

	private boolean _databaseOK;
	private boolean _applicationNewOK;
	private boolean _applicationOK;
	private boolean _authorOK;
	private boolean _packageOK;
	private boolean _sqlTablePrefixOK;
	private boolean _sqlTableSchemaOK;
	private boolean _requirementPrefixOK;
	private boolean _additionnalFieldsOK;
	private boolean _typeFrameworkOK;

	/**
	 * Constructeur privé pour éviter l'instanciation de la classe.
	 */
	private ValidatorUtil() {
		// EMPTY.
	}

	/**
	 * Calcul de la validité des paramètres en fonction des données saisies.
	 * 
	 * @return boolean
	 */
	public boolean isValid() {

		return _applicationOK 
				&& _databaseOK
				&& _applicationOK
				&& _applicationNewOK 
				&& _sqlTablePrefixOK 
				&& _requirementPrefixOK 
				&& _additionnalFieldsOK
				&& _typeFrameworkOK
				&& _packageOK
				&& _authorOK;
	}

	/**
	 * Retourne le message à afficher à l'utilisateur en cas d'erreur de saisie.
	 * 
	 * @return String
	 */
	public String getMessage() {

		if (!_applicationNewOK)
			return "Le projet existe déjà dans l'espace de travail (ou sur le disque).";

		if (!_applicationOK)
			return "Le nom du projet n'est pas renseigné.";

		if (!_packageOK)
			return "Le package racine de l'application n'est pas renseigné.";
		
		if(! _authorOK)
			return "Le nom de l'auteur ou de l'organisme n'est pas renseigné.";

		if (!_sqlTableSchemaOK)
			return "Le schéma des tables SQL n'est pas valide ( format : xxx ).";

		if (!_sqlTablePrefixOK)
			return "Le préfixe des tables SQL n'est pas valide ( format : xxx_ ).";

		if (!_requirementPrefixOK)
			return "Le préfixe pour les exigences (requirements) n'est pas valide";

		if (!_databaseOK)
			return "Il est impossible de générer pour deux versions différentes d'Oracle.";

		if (!_additionnalFieldsOK)
			return "La rubrique champs automatiques n'est pas correctement renseignée.";
		
		if (!_typeFrameworkOK)
			return "Le type de framework n'est pas encore disponible pour ce type de projet.";

		return null;
	}

	/**
	 * Typologie du message à retourner (pour l'instant toujours ERR.).
	 * 
	 * @return
	 */
	public int getMessageType() {

		return IMessageProvider.INFORMATION;
	}

	public void setApplicationOK(boolean p_applicationOK) {
		_applicationOK = p_applicationOK;
	}

	public void setApplicationNewOk(boolean p_applicationNewOK) {
		_applicationNewOK = p_applicationNewOK;
	}

	public void setPackageOK(boolean p_packageOK) {
		_packageOK = p_packageOK;
	}
	
	public void setAuthorOK(boolean p_authorOK) {
		_authorOK = p_authorOK;
	}

	public void setSqlTablePrefixOk(boolean p_sqlTablePrefixOk) {
		_sqlTablePrefixOK = p_sqlTablePrefixOk;
	}

	public void setSqlTableSchema(boolean p_sqlTableSchemaOk) {
		_sqlTableSchemaOK = p_sqlTableSchemaOk;
	}

	public void setRequirementPrefixOK(boolean p_requirementPrefixOk) {
		_requirementPrefixOK = p_requirementPrefixOk;
	}

	public void setAdditionalFieldsOK(boolean p_additionalFieldsOK) {
		_additionnalFieldsOK = p_additionalFieldsOK;
	}
	
	public void setDatabaseOK(boolean p_databaseOK) {
		_databaseOK = p_databaseOK;
	}

	public void setTypeFrameworkOK(boolean _typeFrameworkOK) {
		this._typeFrameworkOK = _typeFrameworkOK;
	}
}