package fr.pacman.start.ui.util;

import org.eclipse.jface.dialogs.IMessageProvider;

/**
 * 
 * @author MINARM.
 *
 */
public class ValidatorUtil {

	public static ValidatorUtil INSTANCE = new ValidatorUtil();

	private boolean _databaseOK;
	private boolean _eDatabaseOK;
	private boolean _applicationNewOK;
	private boolean _applicationOK;
	private boolean _authorOK;
	private boolean _packageOK;
	private boolean _spi4JOK;
	private boolean _useLibrariesOK;
	private boolean _sqlTablePrefixOK;
	private boolean _sqlTableSchemaOK;
	private boolean _requirementPrefixOK;
	private boolean _additionnalFieldsOK;
	private boolean _oracleOK;
	private boolean _wsOK;

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
				&& _eDatabaseOK
				&& _useLibrariesOK
				&& _applicationOK
				&& _applicationNewOK 
				&& _sqlTablePrefixOK 
				&& _requirementPrefixOK 
				&& _additionnalFieldsOK
				&& _packageOK 
				&& _authorOK
				&& _spi4JOK 
				&& _oracleOK
				&& _wsOK;
	}

	/**
	 * Retourne le message à afficher à l'utilisateur en cas d'erreur de saisie.
	 * 
	 * @return String
	 */
	public String getMessage() {

		if (!_useLibrariesOK)
			return "Une (ou plusieurs) rubrique(s) dans l'ajout des librairies n'est pas renseignée ou n'est pas valide.";

		if (!_applicationNewOK)
			return "Le projet existe déjà dans l'espace de travail (ou sur le disque).";

		if (!_applicationOK)
			return "Le nom du projet n'est pas renseigné.";

		if (!_packageOK)
			return "Le package de l'application n'est pas renseigné.";
		
		if(! _authorOK)
			return "Le nom de l'auteur pour les classes n'est pas renseigné.";

		if (!_sqlTableSchemaOK)
			return "Le schéma des tables SQL n'est pas valide ( format : xxx. ).";

		if (!_sqlTablePrefixOK)
			return "Le préfixe des tables SQL n'est pas valide ( format : xxx_ ).";

		if (!_requirementPrefixOK)
			return "Le préfixe pour les requirements n'est pas valide";

		if (!_oracleOK)
			return "Il est impossible de générer pour deux versions différentes d'Oracle.";

		if (!_additionnalFieldsOK)
			return "Veuillez remplir le nom pour les champs SQL additionnels.";
		
		if(!_wsOK)
			return "Microservices, la génération des services Web doit être cochée.";
		
		if(!_databaseOK)
			return "Le projet utilise une base de données mais aucune base n'a été cochée (H2 ne fonctionne qu'en mode embarqué)";
		
		if(!_eDatabaseOK)
			return "Pour l'option base embarquée, seule la base de type H2 est authorisée.";

		return null;
	}

	/**
	 * Typologie du message à retourner (pour l'instant toujours ERR.).
	 * 
	 * @return
	 */
	public int getMessageType() {

		return IMessageProvider.ERROR;
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

	public void setSpi4JOK(boolean p_spi4JOK) {
		_spi4JOK = p_spi4JOK;
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

	public void setUseLibrariesOK(boolean p_useLibrariesOK) {
		_useLibrariesOK = p_useLibrariesOK;
	}

	public void setOracleOK(boolean p_oracleOK) {
		_oracleOK = p_oracleOK;
	}
	
	public void setWsOk(boolean p_wsOK) {
		_wsOK = p_wsOK;
	}
	
	public void setDatabaseOK(boolean p_databaseOK) {
		_databaseOK = p_databaseOK;
	}
	
	public void setEmbeddedDatabaseOK(boolean p_eDatabaseOK) {
		_eDatabaseOK = p_eDatabaseOK;
	}
}