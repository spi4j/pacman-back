package fr.pacman.core.property.project;

import fr.pacman.core.property.PacmanProperty;
import fr.pacman.core.property.PropertiesCategory;
import fr.pacman.core.property.PropertiesHandler;


public final class Spi4jProperties extends PropertiesCategory {

	private static final String c_idParam_service_abs = "framework.type.service_abs";
	private static final String c_idParam_entity_service_abs = "framework.type.entity.Service_abs";
	private static final String c_idParam_applicationService_itf = "framework.type.applicationService_itf";
	private static final String c_idParam_userBusiness_abs = "framework.type.userBusiness_abs";
	private static final String c_idParam_service_itf = "framework.type.Service_itf";
	private static final String c_idParam_entityService_itf = "framework.type.entity.service_itf";
	private static final String c_idParam_serviceReferentiel_itf = "framework.type.serviceReferentiel_itf";
	private static final String c_idParam_attributesNames_itf = "framework.type.attributeNames_itf";
	private static final String c_idParam_dto_itf = "framework.type.dto_itf";
	private static final String c_idParam_mapper_abs = "framework.type.mapper_abs";
	private static final String c_idParam_entityMapper_abs = "framework.type.entityMapper_abs";
	private static final String c_idParam_mapper_itf = "framework.type.mapper_itf";
	private static final String c_idParam_entityMapper_itf = "framework.type.entityMapper_itf";
	private static final String c_idParam_match_abs = "framework.type.match_abs";
	private static final String c_idParam_match_itf = "framework.type.match_itf";
	private static final String c_idParam_userPersistence_abs = "framework.type.userPersistence_abs";
	private static final String c_idParam_dao_itf = "framework.type.dao_itf";
	private static final String c_idParam_daoJdbc_abs = "framework.type.daoJdbc_abs";
	private static final String c_idParam_columnsNames_itf = "framework.type.columnsNames_itf";
	private static final String c_idParam_entity_itf = "framework.type.entity_itf";
	private static final String c_idParam_beanTester_abs = "framework.type.beanTester_abs";
	private static final String c_idParam_fetchingStrategyTester_abs = "framework.type.fetchingStrategyTester_abs";
	private static final String c_idParam_fetchingStrategyEntityTester_abs = "framework.type.fetchingStrategyEntityTester_abs";
	private static final String c_idParam_xto_itf = "framework.type.xto_itf";
	private static final String c_idParam_xto_rs_itf = "framework.type.xto_rs_itf";
	private static final String c_idParam_binary = "framework.type.Binary";

	@Override
	protected String get_propertiesFileName() {
		return "framework_spi4j.properties";
	}

	@Override
	protected boolean is_defaultFileForAdditionalproperties() {
		return false;
	}

	@Override
	protected PacmanProperty[] initPacmanProperties() {
		return new PacmanProperty[] {
				PacmanProperty.newRequired(c_idParam_service_itf, "fr.spi4j.business.Service_itf",
						"L'interface ancetre des services avec CRUD"),

				PacmanProperty.newRequired(c_idParam_entityService_itf, "fr.spi4j.entity.Service_itf",
						"L'interface ancetre des services avec CRUD (Sans la couche Matching)"),

				PacmanProperty.newRequired(c_idParam_service_abs, "fr.spi4j.business.Service_abs",
						"La classe ancetre des services avec CRUD"),

				PacmanProperty.newRequired(c_idParam_entity_service_abs, "fr.spi4j.entity.Service_abs",
						"La classe ancetre des services avec CRUD (Sans la couche Matching)"),

				PacmanProperty.newRequired(c_idParam_applicationService_itf, "fr.spi4j.business.ApplicationService_itf",
						"La classe ancetre de tous les services"),

				PacmanProperty.newRequired(c_idParam_userBusiness_abs, "fr.spi4j.business.UserBusiness_abs",
						"La classe ancetre de la factory des services"),

				PacmanProperty.newRequired(c_idParam_serviceReferentiel_itf, "fr.spi4j.business.ServiceReferentiel_itf",
						"L'interface marquant un service comme ayant un cache"),

				PacmanProperty.newRequired(c_idParam_attributesNames_itf, "fr.spi4j.business.dto.AttributesNames_itf",
						"L'interface pour les enumerations des attributs"),

				PacmanProperty.newRequired(c_idParam_dto_itf, "fr.spi4j.business.dto.Dto_itf", "L'interface des DTOs"),

				PacmanProperty.newRequired(c_idParam_mapper_itf, "fr.spi4j.mapper.Mapper_itf",
						"L'interface des Mappers (conversion DTO <-> XTO)"),

				PacmanProperty.newRequired(c_idParam_entityMapper_itf, "fr.spi4j.mapper.EntityMapper_itf",
						"L'interface des Mappers (conversion Entity <-> XTO)"),

				PacmanProperty.newRequired(c_idParam_mapper_abs, "fr.spi4j.mapper.Mapper_abs",
						"La classe ancetre des Mappers (conversion DTO <-> XTO)"),

				PacmanProperty.newRequired(c_idParam_entityMapper_abs, "fr.spi4j.mapper.EntityMapper_abs",
						"La classe ancetre des Mappers (conversion Entity <-> XTO)"),

				PacmanProperty.newRequired(c_idParam_match_itf, "fr.spi4j.matching.Match_itf",
						"L'interface des Match (conversion Entity <-> DTO)"),

				PacmanProperty.newRequired(c_idParam_match_abs, "fr.spi4j.matching.Match_abs",
						"La classe ancetre des Match (conversion Entity <-> DTO)"),

				PacmanProperty.newRequired(c_idParam_userPersistence_abs, "fr.spi4j.persistence.UserPersistence_abs",
						"La classe ancetre de la factory des DAOs et Entities"),

				PacmanProperty.newRequired(c_idParam_dao_itf, "fr.spi4j.persistence.dao.Dao_itf",
						"L'interface des DAOs"),

				PacmanProperty.newRequired(c_idParam_daoJdbc_abs, "fr.spi4j.persistence.dao.jdbc.DaoJdbc_abs",
						"La classe ancetre des DAO JDBC"),

				PacmanProperty.newRequired(c_idParam_columnsNames_itf, "fr.spi4j.persistence.entity.ColumnsNames_itf",
						"L'interface pour les enumerations des colonnes"),

				PacmanProperty.newRequired(c_idParam_entity_itf, "fr.spi4j.persistence.entity.Entity_itf",
						"L'interface des Entities"),

				PacmanProperty.newRequired(c_idParam_beanTester_abs, "fr.spi4j.tua.BeanTester_abs",
						"La classe ancetre des testeurs de beans"),

				PacmanProperty.newRequired(c_idParam_fetchingStrategyTester_abs,
						"fr.spi4j.tua.FetchingStrategyTester_abs",
						"La classe ancetre des testeurs de FetchingStrategy"),

				PacmanProperty.newRequired(c_idParam_fetchingStrategyEntityTester_abs,
						"fr.spi4j.tua.FetchingStrategyEntityTester_abs",
						"La classe ancetre des testeurs de FetchingStrategy"),

				PacmanProperty.newRequired(c_idParam_xto_itf, "fr.spi4j.ws.xto.Xto_itf", "L'interface des XTOs"),

				PacmanProperty.newRequired(c_idParam_xto_rs_itf, "fr.spi4j.ws.rs.RsXto_itf",
						"L'interface des XTOs pour les services REST"),

				PacmanProperty.newRequired(c_idParam_binary, "fr.spi4j.persistence.dao.Binary",
						"La classe des Binaires (Binary)") };
	}

	/**
	 * Retourne le nom de la classe d'apres son nom complet (avec package)
	 * 
	 * @param p_qualifedName le nom complet de la classe
	 * @return le nom simple de la classe (sans package)
	 */
	private static String getClassName(final String p_qualifedName) {
		if (p_qualifedName.contains(".")) {
			return p_qualifedName.substring(p_qualifedName.lastIndexOf('.') + 1);
		} else {
			return p_qualifedName;
		}
	}

	/**
	 * Retourne l'import pour Service_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Service_abs
	 */
	public static String getImportForService_absJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_service_abs);
	}

	/**
	 * Retourne l'import pour Service_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Service_abs
	 */
	public static String getImportForEntityService_absJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_entity_service_abs);
	}

	/**
	 * Retourne le nom de la classe pour Service_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Service_abs
	 */
	public static String getClassNameForServiceAbsJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_service_abs));
	}

	/**
	 * Retourne le nom de la classe pour Service_abs (Sans la couche Matching)
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Service_abs
	 */
	public static String getClassNameForEntityServiceAbsJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_entity_service_abs));
	}

	/**
	 * Retourne l'import pour ApplicationService_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour ApplicationService_itf
	 */
	public static String getImportForApplicationServiceitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_applicationService_itf);
	}

	/**
	 * Retourne le nom de la classe pour ApplicationService_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour ApplicationService_itf
	 */
	public static String getClassNameForApplicationServiceitfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_applicationService_itf));
	}

	/**
	 * Retourne l'import pour UserBusiness_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour UserBusiness_abs
	 */
	public static String getImportForUserBusinessAbsJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_userBusiness_abs);
	}

	/**
	 * Retourne le nom de la classe pour UserBusiness_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour UserBusiness_abs
	 */
	public static String getClassNameForUserBusinessAbsJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_userBusiness_abs));
	}

	/**
	 * Retourne l'import pour Service_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Service_itf
	 */
	public static String getImportForServiceitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_service_itf);
	}

	/**
	 * Retourne l'import pour Service_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Service_itf
	 */
	public static String getImportForEntityServiceitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_entityService_itf);
	}

	/**
	 * Retourne le nom de la classe pour Service_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Service_itf
	 */
	public static String getClassNameForServiceItfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_service_itf));
	}

	/**
	 * Retourne le nom de la classe pour Service_itf (Sans la couche Matching)
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Service_itf
	 */
	public static String getClassNameForEntityServiceItfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_entityService_itf));
	}

	/**
	 * Retourne l'import pour ServiceReferentiel_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour ServiceReferentiel_itf
	 */
	public static String getImportForServiceReferentielitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_serviceReferentiel_itf);
	}

	/**
	 * Retourne le nom de la classe pour ServiceReferentiel_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour ServiceReferentiel_itf
	 */
	public static String getClassNameForServiceReferentielitfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_serviceReferentiel_itf));
	}

	/**
	 * Retourne l'import pour AttributeNames_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour AttributeNames_itf
	 */
	public static String getImportForAttributesNamesitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_attributesNames_itf);
	}

	/**
	 * Retourne le nom de la classe pour AttributeNames_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour AttributeNames_itf
	 */
	public static String getClassNameForAttributesNamesitfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_attributesNames_itf));
	}

	/**
	 * Retourne l'import pour Dto_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Dto_itf
	 */
	public static String getImportForDtoitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_dto_itf);
	}

	/**
	 * Retourne le nom de la classe pour Dto_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Dto_itf
	 */
	public static String getClassNameForDtoitfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_dto_itf));
	}

	/**
	 * Retourne l'import pour Mapper_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Mapper_abs
	 */
	public static String getImportForMapperAbsJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_mapper_abs);
	}

	/**
	 * Retourne l'import pour EntityMapper_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour EntityMapper_abs
	 */
	public static String getImportForEntityMapperAbsJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_entityMapper_abs);
	}

	/**
	 * Retourne le nom de la classe pour Mapper_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Mapper_abs
	 */
	public static String getClassNameForMapperAbsJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_mapper_abs));
	}

	/**
	 * Retourne le nom de la classe pour EntityMapper_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour EntityMapper_abs
	 */
	public static String getClassNameForEntityMapperAbsJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_entityMapper_abs));
	}

	/**
	 * Retourne l'import pour Mapper_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Mapper_itf
	 */
	public static String getImportForMapperitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_mapper_itf);
	}

	/**
	 * Retourne l'import pour EntityMapper_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour EntityMapper_itf
	 */
	public static String getImportForEntityMapperitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_entityMapper_itf);
	}

	/**
	 * Retourne le nom de la classe pour Mapper_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Mapper_itf
	 */
	public static String getClassNameForMapperitfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_mapper_itf));
	}

	/**
	 * Retourne le nom de la classe pour EntityMapper_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour EntityMapper_itf
	 */
	public static String getClassNameForEntityMapperitfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_entityMapper_itf));
	}

	/**
	 * Retourne l'import pour Match_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Match_abs
	 */
	public static String getImportForMatchAbsJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_match_abs);
	}

	/**
	 * Retourne le nom de la classe pour Match_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Match_abs
	 */
	public static String getClassNameForMatchAbsJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_match_abs));
	}

	/**
	 * Retourne l'import pour Match_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Match_itf
	 */
	public static String getImportForMatchitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_match_itf);
	}

	/**
	 * Retourne le nom de la classe pour Match_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Match_itf
	 */
	public static String getClassNameForMatchitfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_match_itf));
	}

	/**
	 * Retourne l'import pour UserPersistence_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour UserPersistence_abs
	 */
	public static String getImportForUserPersistenceAbsJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_userPersistence_abs);
	}

	/**
	 * Retourne le nom de la classe pour UserPersistence_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour UserPersistence_abs
	 */
	public static String getClassNameForUserPersistenceAbsJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_userPersistence_abs));
	}

	/**
	 * Retourne l'import pour Dao_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Dao_itf
	 */
	public static String getImportForDaoitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_dao_itf);
	}

	/**
	 * Retourne le nom de la classe pour Dao_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Dao_itf
	 */
	public static String getClassNameForDaoitfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_dao_itf));
	}

	/**
	 * Retourne l'import pour DaoJdbc_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour DaoJdbc_abs
	 */
	public static String getImportForDaoJdbcAbsJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_daoJdbc_abs);
	}

	/**
	 * Retourne le nom de la classe pour DaoJdbc_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour DaoJdbc_abs
	 */
	public static String getClassNameForDaoJdbcAbsJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_daoJdbc_abs));
	}

	/**
	 * Retourne l'import pour ColumnsNames_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour ColumnsNames_itf
	 */
	public static String getImportForColumnsNamesitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_columnsNames_itf);
	}

	/**
	 * Retourne le nom de la classe pour ColumnsNames_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour ColumnsNames_itf
	 */
	public static String getClassNameForColumnsNamesitfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_columnsNames_itf));
	}

	/**
	 * Retourne l'import pour Entity_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Entity_itf
	 */
	public static String getImportForEntityitfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_entity_itf);
	}

	/**
	 * Retourne le nom de la classe pour Entity_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Entity_itf
	 */
	public static String getClassNameForEntityitfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_entity_itf));
	}

	/**
	 * Retourne l'import pour BeanTester_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour BeanTester_abs
	 */
	public static String getImportForBeanTesterAbsJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_beanTester_abs);
	}

	/**
	 * Retourne le nom de la classe pour BeanTester_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour BeanTester_abs
	 */
	public static String getClassNameForBeanTester_absJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_beanTester_abs));
	}

	/**
	 * Retourne l'import pour FetchingStrategyTester_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour FetchingStrategyTester_abs
	 */
	public static String getImportForFetchingStrategyTester_absJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_fetchingStrategyTester_abs);
	}

	/**
	 * Retourne l'import pour FetchingStrategyTester_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour FetchingStrategyTester_abs
	 */
	public static String getImportForFetchingStrategyEntityTester_absJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_fetchingStrategyEntityTester_abs);
	}

	/**
	 * Retourne le nom de la classe pour FetchingStrategyTester_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour FetchingStrategyTester_abs
	 */
	public static String getClassNameForFetchingStrategyTester_absJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_fetchingStrategyTester_abs));
	}

	/**
	 * Retourne le nom de la classe pour FetchingStrategyEntityTester_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * 
	 * @return le nom de la classe pour FetchingStrategyEntityTester_abs
	 */
	public static String getClassNameForFetchingStrategyEntityTester_absJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_fetchingStrategyEntityTester_abs));
	}

	/**
	 * Retourne l'import pour Xto_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Xto_itf
	 */
	public static String getImportForXto_itfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_xto_itf);
	}

	/**
	 * Retourne l'import pour Xto_itf pour RS.
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Xto_itf
	 */
	public static String getImportForRsXto_itfJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_xto_rs_itf);
	}

	/**
	 * Retourne le nom de la classe pour Xto_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Xto_itf
	 */
	public static String getClassNameForXto_itfJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_xto_itf));
	}

	/**
	 * Retourne l'import pour Binary
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Binary
	 */
	public static String getImportForBinaryJavaService(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_binary);
	}

	/**
	 * Retourne le nom de la classe pour Binary
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Binary
	 */
	public static String getClassNameForBinaryJavaService(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_binary));
	}
}
