package fr.pacman.core.property.project;

import java.util.Map;

import fr.pacman.core.enumeration.PropertyStatus;
import fr.pacman.core.enumeration.PropertyTrigger;
import fr.pacman.core.property.PacmanProperty;
import fr.pacman.core.property.PropertiesCategory;
import fr.pacman.core.property.PropertiesHandler;
import fr.pacman.core.property.PropertyStrategy;

public final class ProjectProperties extends PropertiesCategory {

	private static final String c_noDefaultValue = "";

	/**
	 * Liste des clés de propriétés. Obligé de les mettre en public (par
	 * simplification et éviter de mettre un pattern plus complexe) pour ne pas
	 * avoir de duplication au niveau du projet de création pour une application
	 * cible.
	 */
	// version pacman/spi4j et version spring ?
	static final String c_project_suffixModel = "model";
	static final String c_project_suffixCommon = "domain";
	static final String c_project_suffixServer = "server";
	static final String c_project_suffixUpdate = "update";
	static final String c_project_suffixWebapp = "webapp";

	public static final String c_project_debug = "project.debug.enabled";
	public static final String c_project_version = "project.version";
	public static final String c_project_java_version = "project.java.version";
	public static final String c_project_name = "project.name";
	public static final String c_project_author = "project.author";
	public static final String c_project_package = "project.package";
	public static final String c_project_databases = "project.databases";
	public static final String c_project_ws = "project.ws.enabled";
	public static final String c_project_security = "project.security.enabled";
	public static final String c_project_crud = "project.crud.enabled";
	public static final String c_project_testsCrud = "project.tests.crud.enabled";
	public static final String c_project_fetchingStrategy = "project.fetchingstrategy.enabled";
	public static final String c_project_servicerequirements = "project.servicerequirements.enabled";
	public static final String c_project_validation_config_file = "project.validation.configfile";
	public static final String c_project_validation_init_on_error = "project.validation.init.onerror";
	public static final String c_project_library = "project.library.enabled";
	public static final String c_project_library_rs = "project.library.rs.enabled";
	public static final String c_project_profiler = "project.profiler.enabled";
	public static final String c_project_framework = "project.framework.type";

	// public static final String c_sql_idsuffix = "sql.id.suffix.enabled";
	public static final String c_sql_fields = "sql.table.fields";
	public static final String c_sql_tablePrefix = "sql.table.prefix";
	public static final String c_sql_tableSchema = "sql.table.schema";
	public static final String c_sql_tableXdmaj = "sql.table.field.xdmaj";
	public static final String c_sql_tableXdmajName = "sql.table.field.xdmaj.name";
	public static final String c_sql_tableXdmajType = "sql.table.field.xdmaj.type";
	public static final String c_sql_tableXdmajSize = "sql.table.field.xdmaj.size";
	public static final String c_sql_tableXdmajNotnull = "sql.table.field.xdmaj.notnull";
	public static final String c_sql_tableXdmajComment = "sql.table.field.xdmaj.comment";
	public static final String c_sql_tableXdmajDefault = "sql.table.field.xdmaj.default";
	public static final String c_sql_tableXtopsup = "sql.table.field.xtopsup";
	public static final String c_sql_tableXtopsupName = "sql.table.field.xtopsup.name";
	public static final String c_sql_tableXtopsupType = "sql.table.field.xtopsup.type";
	public static final String c_sql_tableXtopsupDefault = "sql.table.field.xtopsup.default";
	public static final String c_sql_tableXtopsupNotnull = "sql.table.field.xtopsup.notnull";
	public static final String c_sql_tableXtopsupComment = "sql.table.field.xtopsup.comment";
	public static final String c_sql_tableXtopsupSize = "sql.table.field.xtopsup.size";
	public static final String c_sql_oracleIndexTablespace = "sql.oracle.index.tablespace";
	public static final String c_sql_tableXField = "sql.table.field";

	public static final String c_requirement_prefix = "requirement.prefix";
	public static final String c_requirement_versionningInitial = "requirement.versionning.initial";
	public static final String c_requirement_categoryBaseLevel = "requirement.category.base.level";
	private static final String c_requirement_versionningInitialNone = "none";
	private static final String c_requirement_versionningInitialCurrent = "current";

	public static final String c_paging_mode = "paging.mode";
	public static final String c_paging_totalCount = "paging.total.count.key";
	public static final String c_paging_currentPageIdx = "paging.current.page.idx.key";
	public static final String c_paging_pageCount = "paging.page.count.key";
	public static final String c_paging_currentPageSize = "paging.current.page.size.key";

	// public static final String c_project_libraries =
	// "project.additional.libraries";
	// public static final String c_is_debug = "pacman.debug.enabled";
	// public static final String c_is_wsHk2 = "project.ws.hk2.enabled";

	// public static final String c_is_wms = "project.wms.layer.enabled";

	// public static final String c_is_hashUserCode =
	// "pacman.usercode.hash.enabled";
	// public static final String c_is_displayReport =
	// "pacman.report.display.enabled";
	// public static final String c_is_lazyLoading =
	// "project.entity.lazyLoading.enabled";

	// public static final String c_is_httpEmbeddedServer =
	// "project.tomcat.enabled";
	// public static final String c_is_h2EmbeddedDatabase = "project.h2.enabled";

	public static final String c_is_log4j = "project.log4j.enabled";
	public static final String c_is_spi4jConfig = "config.files.enabled";

	// Tout
	// modifier
	// persistance

	public static final String c_deprecated_soa_usercode_params = "deprecated.soa.usercode.params";
	public static final String c_tests_bdd_enabled = "tests.bdd.enabled";
	public static final String c_rootfiles_generate_enabled = "application.rootfiles.generate";
	public static final String c_ws_security_scheme_id = "ws.security.scheme.spi4id";

	@Override
	protected String get_propertiesFileName() {
		return "project.properties";
	}

	@Override
	protected boolean is_defaultFileForAdditionalproperties() {
		return true;
	}

	@Override
	protected PacmanProperty[] initPacmanProperties() {
		return new PacmanProperty[] {
				PacmanProperty.newRequired(c_project_name, c_noDefaultValue,
						"Le nom de l'application (sert de prefixe pour l'ensemble des projets)"),

				PacmanProperty.newRequired(c_project_java_version, "17",
						"La version du language Java pour la compilation du projet"),

				PacmanProperty.newRequired(c_project_author, System.getProperty("user.name", "MINARM"),
						"L'auteur par defaut pour les fichiers generes"),

				PacmanProperty.newRequired(c_project_package, "com.mycompany.myproject",
						"Le package racine pour les sources du projet"),

				PacmanProperty.newRequired(c_project_profiler, "false",
						"Flag indiquant si le profiling est actif lors des generations"),

				PacmanProperty.newRequired(c_project_framework, "spring",
						"Type de framework pour le projet (Spring par defaut)"),

				PacmanProperty.newRequired(c_project_version, c_noDefaultValue, "La version de l'application"),

				PacmanProperty.newRequired(c_project_databases, "H2",
						"Les types de la base de donnees, parmi Oracle, H2, Mysql, MariaDB, Postgresql (plusieurs possibles, separes par des virgules)"),

				PacmanProperty.newRequired(c_requirement_prefix, "REQ_", "Le prefixe pour les annotations requirement"),

				PacmanProperty.newRequired(c_sql_tablePrefix, c_noDefaultValue,
						"Prefixe pour les tables de l'application ( format : xxx_ )"),

				PacmanProperty.newRequired(c_sql_tableSchema, c_noDefaultValue,
						"Nom du schema pour les tables de l'application ( format : xxx. )"),

				PacmanProperty.newRequired(c_sql_fields, c_noDefaultValue,
						"Champs additionnels pour les tables de l'application"),

				PacmanProperty.newRequired(c_sql_oracleIndexTablespace, "", "Tablespace specifique (si base Oracle)"),

//				PacmanProperty.newRequired(c_is_debug, "false",
//						"Flag indiquant si le mode debug pour PacMan est actif (plus d'informations en cas d'erreur de generation)"),

//				PacmanProperty.newRequired(c_is_displayReport, "true",
//						"Flag indiquant si la generation doit afficher un rapport"),

//				PacmanProperty.newRequired(c_is_hashUserCode, "false",
//						"Flag indiquant si identifiants de balise user code sont en hash"),

//				PacmanProperty.newRequired(c_is_lazyLoading, "false",
//						"Flag indiquant si la generation de la couche de persistance (= Entity) se fait avec les methodes de LazyLoading"),

				PacmanProperty.newRequired(c_project_fetchingStrategy, "false",
						"Flag indiquant si la fetching strategy doit etre generee dans l'application (non actif par defaut)"),

				PacmanProperty.newRequired(c_project_servicerequirements, "true",
						"Flag indiquant si les service requirements doivent etre generes dans l'application (generes par defaut)"),

				PacmanProperty.newRequired(c_is_log4j, "true",
						"Flag indiquant si le log4j2.xml doit etre genere dans l'application (actif par defaut)"),

				PacmanProperty.newRequired(c_project_security, "false",
						"Flag pour la generation de la couche de securite (serveur / client) (non actif par defaut)"),

				PacmanProperty.newRequired(c_project_crud, "false",
						"Flag indiquant si une application des gestion des entites (crud) doit etre generee (non actif par defaut)"),

				PacmanProperty.newRequired(c_tests_bdd_enabled, "false",
						"Flag indiquant si les tests de comportement (JBehave) doivent etre generes (non actif par defaut)"),

				PacmanProperty.newRequired(c_project_library, "false",
						"Flag indiquant si le projet va servir comme librairie"),

				PacmanProperty.newRequired(c_project_library_rs, "false",
						"Flag indiquant si le projet va servir comme librairie avec un import swagger",
						new WSLibraryStrategy()),

				PacmanProperty.newRequired(c_rootfiles_generate_enabled, "false",
						"Flag indiquant si on permet la regeneration des fichiers pom.xml, web.xml et log4j2.xml (non par defaut)"
								+ "\n# ATTENTION : IL S'AGIT D'UNE RESTAURATION D'USINE ! PERTE DES VERSIONS ET DES AJOUTS."),

				PacmanProperty.newRequired(c_project_ws, "false",
						"Flag indiquant si on veut generer des services web (non actif par defaut)",
						new WSProjectStrategy()),

//				PacmanProperty.newRequired(c_is_wms, "false",
//						"Flag indiquant si on veut generer des micro services web (non actif par defaut)"),

//				PacmanProperty.newRequired(c_is_wsHk2, "true",
//						"Flag indiquant si on veut utiliser l'injection pour les services web REST (oui par defaut)"),

				PacmanProperty.newRequired(c_project_debug, "false",
						"Flag indiquant si la generation pour le projet fonctionne en mode debug (non par defaut)"),

				PacmanProperty.newRequired(c_project_testsCrud, "true",
						"Flag indiquant si les tests unitaires sur le crud doivent etre generes (generes par defaut)"),

				PacmanProperty.newRequired(c_requirement_categoryBaseLevel, "0",
						"Niveau de base pour le decoupage des exigences dans le code genere (0 : aucun decoupage)"),

				PacmanProperty.newRequired(c_requirement_versionningInitial, c_requirement_versionningInitialNone,
						"Version initiale mise lors de la premiere genetration pour les tests de versionning d'exigence (\""
								+ "\n#" + c_requirement_versionningInitialNone + "\" : exigence non implementee ou \""
								+ c_requirement_versionningInitialCurrent + "\" pour la version du modele)"),

				PacmanProperty.newRequired(c_project_validation_config_file, "validation.xml",
						"Fichier de configuration des regles de validation"),

				PacmanProperty.newRequired(c_project_validation_init_on_error, "true",
						"Initialisation du fichier de configuration en cas d'erreur de celui-ci"),

				PacmanProperty.newRequired(c_is_spi4jConfig, "false",
						"Utilisation du framework spi4j pour la gestion de fichiers de configuration"),

				PacmanProperty.newRequired(c_deprecated_soa_usercode_params, "true",
						"Utilisation des parametres pour le user code des methodes soa"),

				PacmanProperty.newConditional(c_ws_security_scheme_id, "",
						"Schema de securite a generer si librairie REST et plusieurs schemas (vide par defaut)"),

//				PacmanProperty.newConditional(c_is_httpEmbeddedServer, "",
//						"Mise en place d'un serveur http embarque (jetty, tomcat)"),
//
//				PacmanProperty.newConditional(c_is_h2EmbeddedDatabase, "",
//						"Mise en place d'une base h2 embarque (non actif par defaut)"),

//				PacmanProperty.newRequired(c_project_libraries, "",
//						"Champs additionnels pour les librairies supplémentaires de l'application"),

				PacmanProperty.newRequired(c_sql_fields, "", "Champs additionnels pour les tables SQL de l'application",
						new AdditionalFieldsStrategy()),

				PacmanProperty.newConditional(c_sql_tableXdmajName, "XDMAJ", "Champ additionnel pour les tables SQL"),

				PacmanProperty.newConditional(c_sql_tableXdmajType, "Date", "Champ additionnel pour les tables SQL"),

				PacmanProperty.newConditional(c_sql_tableXdmajSize, c_noDefaultValue,
						"Champ additionnel pour les tables SQL"),

				PacmanProperty.newConditional(c_sql_tableXdmajNotnull, "true", "Champ additionnel pour les tables SQL"),

				PacmanProperty.newConditional(c_sql_tableXdmajDefault, "current_date",
						"Champ additionnel pour les tables SQL"),

				PacmanProperty.newConditional(c_sql_tableXdmajComment, "Date de mise a jour de la ligne.",
						"Champ additionnel pour les tables SQL"),

				PacmanProperty.newConditional(c_sql_tableXtopsupName, "XTOPSUP",
						"Champ additionnel pour les tables SQL"),

				PacmanProperty.newConditional(c_sql_tableXtopsupType, "XtopSup",
						"Champ additionnel pour les tables SQL"),

				PacmanProperty.newConditional(c_sql_tableXtopsupDefault, "0", "Champ additionnel pour les tables SQL"),

				PacmanProperty.newConditional(c_sql_tableXtopsupNotnull, "true",
						"Champ additionnel pour les tables SQL"),

				PacmanProperty.newConditional(c_sql_tableXtopsupComment, "Indicateur de suppression logique.",
						"Champ additionnel pour les tables SQL"),

				PacmanProperty.newConditional(c_sql_tableXtopsupSize, "1", "Champ additionnel pour les tables SQL"),

				PacmanProperty.newRequired(c_paging_mode, c_noDefaultValue,
						"Mode de fonctionnement pour la pagination (auto, user, vide par defaut)",
						new PagingModeStrategy()),

				PacmanProperty.newConditional(c_paging_totalCount, "Resource-Count",
						"Propriete dans l'en-tête pour le stockage du nombre total d'occurences"),

				PacmanProperty.newConditional(c_paging_currentPageIdx, "Current-Page",
						"Propriete dans l'en-tête pour le stockage de l'index de page courante"),

				PacmanProperty.newConditional(c_paging_pageCount, "Page-Count",
						"Propriete dans l'en-tête pour le stockage du nombre de pages"),

				PacmanProperty.newConditional(c_paging_currentPageSize, "Current-Page-Size",
						"Propriete dans l'en-tête pour le stockage du nombre d'occurences pour la page courante") };
	}

	/**
	 * Definition des strategies pour les proprietes.
	 */

	/**
	 * Stratégie pour la
	 */
	private class WSLibraryStrategy extends PropertyStrategy {
		@Override
		protected PropertyTrigger getStrategyTrigger() {
			return PropertyTrigger.ALWAYS;
		}

		@Override
		protected void updateProperty(PacmanProperty p_pacmanProperty) {
			p_pacmanProperty.setStatus(PropertyStatus.REQUIRED);
		}

		@Override
		protected void doStrategy(Map<String, PacmanProperty> p_pacmanProperties) {
			if (Boolean.valueOf(getRefValue()))
				updateProperty(p_pacmanProperties.get(c_ws_security_scheme_id));
		}
	}

	/**
	 * Stratégie pour la webapp.
	 */
	private class WSProjectStrategy extends PropertyStrategy {
		@Override
		protected PropertyTrigger getStrategyTrigger() {
			return PropertyTrigger.ALWAYS;
		}

		@Override
		protected void updateProperty(PacmanProperty p_pacmanProperty) {
			p_pacmanProperty.setStatus(PropertyStatus.REQUIRED);
		}

		@Override
		protected void doStrategy(Map<String, PacmanProperty> p_pacmanProperties) {
			if (Boolean.parseBoolean(getRefValue())) {
//				updateProperty(p_pacmanProperties.get(c_is_httpEmbeddedServer));
//				updateProperty(p_pacmanProperties.get(c_is_h2EmbeddedDatabase));
			}
		}
	}

	/**
	 * Stratégie pour la pagination.
	 */
	private class PagingModeStrategy extends PropertyStrategy {

		@Override
		protected PropertyTrigger getStrategyTrigger() {
			return PropertyTrigger.ALWAYS;
		}

		@Override
		protected void updateProperty(PacmanProperty p_pacmanProperty) {
			p_pacmanProperty.setStatus(PropertyStatus.REQUIRED);
		}

		@Override
		protected void doStrategy(Map<String, PacmanProperty> p_pacmanProperties) {
			if (!getRefValue().isEmpty()) {
				updateProperty(p_pacmanProperties.get(c_paging_currentPageIdx));
				updateProperty(p_pacmanProperties.get(c_paging_currentPageSize));
				updateProperty(p_pacmanProperties.get(c_paging_pageCount));
				updateProperty(p_pacmanProperties.get(c_paging_totalCount));
			}
		}
	}

	/**
	 * Fonctionnement particulier pour les proprietes additionnelles. On se contente
	 * de lancer une exception si non trouvee.
	 * 
	 * @author MINARM.
	 */
	private class AdditionalFieldsStrategy extends PropertyStrategy {

		@Override
		protected PropertyTrigger getStrategyTrigger() {
			return PropertyTrigger.ALWAYS;
		}

		@Override
		protected void updateProperty(PacmanProperty p_pacmanProperty) {
			p_pacmanProperty.setStatus(PropertyStatus.REQUIRED);
		}

		@Override
		protected void doStrategy(Map<String, PacmanProperty> p_pacmanProperties) {
			if (null != getRefValue() && !getRefValue().isEmpty()) {
				for (String key : getRefValue().split(",")) {
					if (c_sql_tableXdmaj.equals(key)) {
						updateProperty(p_pacmanProperties.get(c_sql_tableXdmajName));
						updateProperty(p_pacmanProperties.get(c_sql_tableXdmajComment));
						updateProperty(p_pacmanProperties.get(c_sql_tableXdmajNotnull));
						updateProperty(p_pacmanProperties.get(c_sql_tableXdmajSize));
						updateProperty(p_pacmanProperties.get(c_sql_tableXdmajType));
						updateProperty(p_pacmanProperties.get(c_sql_tableXdmajDefault));
					}

					if (c_sql_tableXtopsup.equals(key)) {
						updateProperty(p_pacmanProperties.get(c_sql_tableXtopsupName));
						updateProperty(p_pacmanProperties.get(c_sql_tableXtopsupComment));
						updateProperty(p_pacmanProperties.get(c_sql_tableXtopsupNotnull));
						updateProperty(p_pacmanProperties.get(c_sql_tableXtopsupSize));
						updateProperty(p_pacmanProperties.get(c_sql_tableXtopsupType));
						updateProperty(p_pacmanProperties.get(c_sql_tableXtopsupDefault));
					}
				}
			}
		}
	}

	public static String get_projectName(final Object p_object) {
		return PropertiesHandler.getProperty(c_project_name);
	}

	public static String get_projectPackage(final Object p_object) {
		return PropertiesHandler.getProperty(c_project_package);
	}

	public static String get_dataBasesNames(final Object p_object) {
		return "H2,MySQL,MariaDB,PostgreSQL,Oracle,Oracle_32";
		// return PropertiesHandler.getProperty(c_project_databaseTypes);
	}

	public static boolean get_useServiceRequirements(final Object p_object) {
		return Boolean.valueOf(PropertiesHandler.getProperty(c_project_servicerequirements));
	}

	public static boolean get_useTestsCRUD() {
		return Boolean.valueOf(PropertiesHandler.getProperty(c_project_testsCrud));
	}

	public static boolean is_appCRUD() {
		// return PacmanPropertiesHandler.getProperty(c_appli);
		return false;
	}

	public static String get_useSpi4jSecurity() {
		return PropertiesHandler.getProperty(c_project_security);
	}

	public static String get_useTestBDD() {
		return PropertiesHandler.getProperty(c_tests_bdd_enabled);
	}

	public static boolean is_library() {
		return Boolean.valueOf(PropertiesHandler.getProperty(c_project_library));
	}

	public static boolean isLibraryRs() {
		return Boolean.valueOf(PropertiesHandler.getProperty(c_project_library_rs));
	}

	public static boolean is_generateRootFiles() {
		return Boolean.valueOf(PropertiesHandler.getProperty(c_rootfiles_generate_enabled));
	}

	public static boolean get_useWS() {
		return Boolean.valueOf(PropertiesHandler.getProperty(c_project_ws));
	}

	public static boolean isModeDebug() {
		return Boolean.valueOf(PropertiesHandler.getProperty(c_project_debug));
	}

//	public static String get_useWMS() {
//		return PropertiesHandler.getProperty(c_is_wms);
//	}

//	public static String get_useWsServiceInjection() {
//		return PropertiesHandler.getProperty(c_is_wsHk2);
//	}

	public static String get_wsSecuritySchemeId() {
		return PropertiesHandler.getProperty(c_ws_security_scheme_id);
	}

	public static String getVersion() {
		return PropertiesHandler.getProperty(c_project_version);
	}

	public static String get_validationConfigFile() {
		return PropertiesHandler.getProperty(c_project_validation_config_file);
	}

	public static String is_validationInitOnError() {
		return PropertiesHandler.getProperty(c_project_validation_init_on_error);
	}

	public static String get_requirementCategoryBaseLevel() {
		return PropertiesHandler.getProperty(c_requirement_categoryBaseLevel);
	}

	public static String get_persistenceAutoFields(final Object p_object) {
		return "";
		// return PropertiesHandler.getProperty(c_sql_fields);
	}

//	public static String getLibrariesAdditionalJars(Object p_object) {
//		return PropertiesHandler.getProperty(c_project_libraries);
//	}

	public static final boolean is_applicationCrud(final Object p_object) {
		return true;
		// return PropertiesHandler.getProperty(c_is_crud);
	}

	public static final String get_useConfigFilesSpi4jFrwk(Object p_object) {
		return "false";
		// return PropertiesHandler.getProperty(c_is_spi4jConfig);
	}

	public static String get_useLog4J(Object p_object) {
		return "false";
		// return PropertiesHandler.getProperty(c_is_log4j);
	}

	public static String get_authorName(Object p_object) {
		return PropertiesHandler.getProperty(c_project_author);
	}

	public static String get_requirementPrefix() {
		return PropertiesHandler.getProperty(c_requirement_prefix);
	}

	public static boolean use_idSQLSuffixForReferences(final Object p_object) {
		return true;
		// return PropertiesHandler.getProperty(c_is_sqlIdsuffix);
	}

	public static String get_SQLTablePrefix(final Object p_object) {
		// return "";
		return "TBL";
		// return PropertiesHandler.getProperty(c_sql_tablePrefix);
	}

	public static String get_SQLTableSchema(final Object p_object) {
		// return "";
		return "SCH";
		// return PropertiesHandler.getProperty(c_sql_tableSchema);
	}

	public static String get_SQLOracleIndexTableSpace(final Object p_object) {
		return "TORA";
		// return PropertiesHandler.getProperty(c_sql_oracleIndexTablespace);
	}

	public static String get_useDeprecatedSOAUserCodeParams() {
		return PropertiesHandler.getProperty(c_deprecated_soa_usercode_params);
	}

	public static String use_fetchingStrategy() {
		return PropertiesHandler.getProperty(c_project_fetchingStrategy);
	}

	public static String get_javaVersion(final Object p_object) {
		return PropertiesHandler.getProperty(c_project_java_version);
	}

	public static String get_pagingMode() {
		return PropertiesHandler.getProperty(c_paging_mode);
	}

	public static String get_pagingTotalCountKey() {
		return PropertiesHandler.getProperty(c_paging_totalCount);
	}

	public static String get_pagingCurrentPageSizeKey() {
		return PropertiesHandler.getProperty(c_paging_currentPageSize);
	}

	public static String get_pagingCurrentPageIdxKey() {
		return PropertiesHandler.getProperty(c_paging_currentPageIdx);
	}

	public static String get_javaPagingCount() {
		return PropertiesHandler.getProperty(c_paging_pageCount);
	}

//	public static String isDebugMode() {
//		return PropertiesHandler.getProperty(c_is_debug);
//	}

//	public static String get_httpEmbeddedServer() {
//		return PropertiesHandler.getProperty(c_is_httpEmbeddedServer);
//	}

//	public static String is_hashUserCode() {
//		return PropertiesHandler.getProperty(c_is_hashUserCode);
//	}

//	public static String get_H2EmbeddedDatabase() {
//		return PropertiesHandler.getProperty(c_is_h2EmbeddedDatabase);
//	}

	public static String get_XtoSupName() {
		return PropertiesHandler.getProperty(c_sql_tableXtopsupName);
	}

	public static String get_XdMajName() {
		return PropertiesHandler.getProperty(c_sql_tableXdmajName);
	}

//	public static String is_displayGeneratorReport() {
//		return PropertiesHandler.getProperty(c_is_displayReport);
//	}

	public static String get_framework(final Object p_object) {
		return PropertiesHandler.getProperty(c_project_framework);
	}

	public static boolean is_spring(final Object p_object) {
		return get_framework(p_object).indexOf("spring") != -1;
	}

	public static boolean isProfilerEnabled() {
		return Boolean.valueOf(PropertiesHandler.getProperty(c_project_profiler));
	}

	public static String get_SQLAdditionalFields() {
		return null;
		// return
		// PacmanPropertiesHandler.getProperty(c_server_sql_table_add_fields);
	}

	public static String get_XtoSupKey() {
		return c_sql_tableXtopsup;
	}

	public static String get_XdMajKey() {
		return c_sql_tableXdmaj;
	}

	public static String get_projectServerName(final Object p_object) {
		return get_projectName(p_object) + "-" + c_project_suffixServer;
	}

	public static String get_projectCommonName(final Object p_object) {
		return get_projectName(p_object) + "-" + c_project_suffixCommon;
	}

	public static String get_projectWebappName(final Object p_object) {
		return get_projectName(p_object) + "-" + c_project_suffixWebapp;
	}

	public static String get_projectModelName(final Object p_object) {
		return get_projectName(p_object) + "-" + c_project_suffixModel;
	}

	public static String get_suffixModel(final Object p_object) {
		return c_project_suffixModel;
	}

	public static String get_suffixCommon(final Object p_object) {
		return c_project_suffixCommon;
	}

	public static String get_suffixServer(final Object p_object) {
		return c_project_suffixServer;
	}

	public static String get_suffixWebapp(final Object p_object) {
		return c_project_suffixWebapp;
	}

	public static String get_suffixUpdate(final Object p_object) {
		return c_project_suffixUpdate;
	}
}
