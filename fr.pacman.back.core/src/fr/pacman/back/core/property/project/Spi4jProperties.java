package fr.pacman.back.core.property.project;

import fr.pacman.back.core.property.PacmanProperty;
import fr.pacman.back.core.property.PropertiesCategory;
import fr.pacman.back.core.property.PropertiesHandler;


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
				PacmanProperty.newRequired(c_idParam_service_itf, "fr.spi4j.business.Service_Itf",
						"L'interface ancetre des services avec CRUD"),

				PacmanProperty.newRequired(c_idParam_entityService_itf, "fr.spi4j.entity.Service_Itf",
						"L'interface ancetre des services avec CRUD (Sans la couche Matching)"),

				PacmanProperty.newRequired(c_idParam_service_abs, "fr.spi4j.business.Service_Abs",
						"La classe ancetre des services avec CRUD"),

				PacmanProperty.newRequired(c_idParam_entity_service_abs, "fr.spi4j.entity.Service_Abs",
						"La classe ancetre des services avec CRUD (Sans la couche Matching)"),

				PacmanProperty.newRequired(c_idParam_applicationService_itf, "fr.spi4j.business.ApplicationService_Itf",
						"La classe ancetre de tous les services"),

				PacmanProperty.newRequired(c_idParam_userBusiness_abs, "fr.spi4j.business.UserBusiness_Abs",
						"La classe ancetre de la factory des services"),

				PacmanProperty.newRequired(c_idParam_serviceReferentiel_itf, "fr.spi4j.business.ServiceReferentiel_Itf",
						"L'interface marquant un service comme ayant un cache"),

				PacmanProperty.newRequired(c_idParam_attributesNames_itf, "fr.spi4j.business.dto.AttributesNames_Itf",
						"L'interface pour les enumerations des attributs"),

				PacmanProperty.newRequired(c_idParam_dto_itf, "fr.spi4j.business.dto.Dto_Itf", "L'interface des DTOs"),

				PacmanProperty.newRequired(c_idParam_mapper_itf, "fr.spi4j.mapper.Mapper_Itf",
						"L'interface des Mappers (conversion DTO <-> XTO)"),

				PacmanProperty.newRequired(c_idParam_entityMapper_itf, "fr.spi4j.mapper.EntityMapper_Itf",
						"L'interface des Mappers (conversion Entity <-> XTO)"),

				PacmanProperty.newRequired(c_idParam_mapper_abs, "fr.spi4j.mapper.Mapper_Abs",
						"La classe ancetre des Mappers (conversion DTO <-> XTO)"),

				PacmanProperty.newRequired(c_idParam_entityMapper_abs, "fr.spi4j.mapper.EntityMapper_Abs",
						"La classe ancetre des Mappers (conversion Entity <-> XTO)"),

				PacmanProperty.newRequired(c_idParam_match_itf, "fr.spi4j.matching.Match_Itf",
						"L'interface des Match (conversion Entity <-> DTO)"),

				PacmanProperty.newRequired(c_idParam_match_abs, "fr.spi4j.matching.Match_Abs",
						"La classe ancetre des Match (conversion Entity <-> DTO)"),

				PacmanProperty.newRequired(c_idParam_userPersistence_abs, "fr.spi4j.persistence.UserPersistence_Abs",
						"La classe ancetre de la factory des DAOs et Entities"),

				PacmanProperty.newRequired(c_idParam_dao_itf, "fr.spi4j.persistence.dao.Dao_Itf",
						"L'interface des DAOs"),

				PacmanProperty.newRequired(c_idParam_daoJdbc_abs, "fr.spi4j.persistence.dao.jdbc.DaoJdbc_Abs",
						"La classe ancetre des DAO JDBC"),

				PacmanProperty.newRequired(c_idParam_columnsNames_itf, "fr.spi4j.persistence.entity.ColumnsNames_Itf",
						"L'interface pour les enumerations des colonnes"),

				PacmanProperty.newRequired(c_idParam_entity_itf, "fr.spi4j.persistence.entity.Entity_Itf",
						"L'interface des Entities"),

				PacmanProperty.newRequired(c_idParam_beanTester_abs, "fr.spi4j.tua.BeanTester_Abs",
						"La classe ancetre des testeurs de beans"),

				PacmanProperty.newRequired(c_idParam_fetchingStrategyTester_abs,
						"fr.spi4j.tua.FetchingStrategyTester_Abs",
						"La classe ancetre des testeurs de FetchingStrategy"),

				PacmanProperty.newRequired(c_idParam_fetchingStrategyEntityTester_abs,
						"fr.spi4j.tua.FetchingStrategyEntityTester_Abs",
						"La classe ancetre des testeurs de FetchingStrategy"),

				PacmanProperty.newRequired(c_idParam_xto_itf, "fr.spi4j.ws.xto.Xto_Itf", "L'interface des XTOs"),

				PacmanProperty.newRequired(c_idParam_xto_rs_itf, "fr.spi4j.ws.rs.RsXto_Itf",
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
	public static String get_importForServiceAbs(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_service_abs);
	}

	/**
	 * Retourne l'import pour Service_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour ServiceAbs
	 */
	public static String get_importForEntityServiceAbs(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_entity_service_abs);
	}

	/**
	 * Retourne le nom de la classe pour ServiceAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Service_abs
	 */
	public static String get_classNameForServiceAbs(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_service_abs));
	}

	/**
	 * Retourne le nom de la classe pour ServiceAbs (Sans la couche Matching)
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Service_abs
	 */
	public static String get_classNameForEntityServiceAbs(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_entity_service_abs));
	}

	/**
	 * Retourne l'import pour ApplicationService_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour ApplicationService_itf
	 */
	public static String get_importForApplicationServiceItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_applicationService_itf);
	}

	/**
	 * Retourne le nom de la classe pour ApplicationService_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour ApplicationService_itf
	 */
	public static String get_classNameForApplicationServiceItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_applicationService_itf));
	}

	/**
	 * Retourne l'import pour UserBusinessAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour UserBusiness_abs
	 */
	public static String get_importForUserBusinessAbs(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_userBusiness_abs);
	}

	/**
	 * Retourne le nom de la classe pour UserBusiness_abs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour UserBusinessAbs
	 */
	public static String get_classNameForUserBusinessAbs(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_userBusiness_abs));
	}

	/**
	 * Retourne l'import pour Service_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Service_itf
	 */
	public static String get_importForServiceItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_service_itf);
	}

	/**
	 * Retourne l'import pour Service_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Service_itf
	 */
	public static String get_importForEntityServiceItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_entityService_itf);
	}

	/**
	 * Retourne le nom de la classe pour Service_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Service_itf
	 */
	public static String get_classNameForServiceItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_service_itf));
	}

	/**
	 * Retourne le nom de la classe pour Service_itf (Sans la couche Matching)
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Service_itf
	 */
	public static String get_classNameForEntityServiceItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_entityService_itf));
	}

	/**
	 * Retourne l'import pour ServiceReferentiel_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour ServiceReferentiel_itf
	 */
	public static String get_importForServiceReferentielItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_serviceReferentiel_itf);
	}

	/**
	 * Retourne le nom de la classe pour ServiceReferentiel_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour ServiceReferentiel_itf
	 */
	public static String get_classNameForServiceReferentielItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_serviceReferentiel_itf));
	}

	/**
	 * Retourne l'import pour AttributeNames_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour AttributeNames_itf
	 */
	public static String get_importForAttributesNamesItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_attributesNames_itf);
	}

	/**
	 * Retourne le nom de la classe pour AttributeNames_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour AttributeNames_itf
	 */
	public static String get_classNameForAttributesNamesItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_attributesNames_itf));
	}

	/**
	 * Retourne l'import pour Dto_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Dto_itf
	 */
	public static String get_importForDtoItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_dto_itf);
	}

	/**
	 * Retourne le nom de la classe pour Dto_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Dto_itf
	 */
	public static String get_classNameForDtoItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_dto_itf));
	}

	/**
	 * Retourne l'import pour MapperAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Mapper_abs
	 */
	public static String get_importForMapperAbs(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_mapper_abs);
	}

	/**
	 * Retourne l'import pour EntityMapperAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour EntityMapperAbs
	 */
	public static String get_importForEntityMapperAbs(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_entityMapper_abs);
	}

	/**
	 * Retourne le nom de la classe pour MapperAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour MapperAbs
	 */
	public static String get_classNameForMapperAbs(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_mapper_abs));
	}

	/**
	 * Retourne le nom de la classe pour EntityMapperAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour EntityMapperAbs
	 */
	public static String get_classNameForEntityMapperAbs(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_entityMapper_abs));
	}

	/**
	 * Retourne l'import pour Mapper_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Mapper_itf
	 */
	public static String get_importForMapperItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_mapper_itf);
	}

	/**
	 * Retourne l'import pour EntityMapper_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour EntityMapper_itf
	 */
	public static String get_importForEntityMapperItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_entityMapper_itf);
	}

	/**
	 * Retourne le nom de la classe pour Mapper_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Mapper_itf
	 */
	public static String get_classNameForMapperItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_mapper_itf));
	}

	/**
	 * Retourne le nom de la classe pour EntityMapper_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour EntityMapper_itf
	 */
	public static String get_classNameForEntityMapperItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_entityMapper_itf));
	}

	/**
	 * Retourne l'import pour MatchAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour MatchAbs
	 */
	public static String get_importForMatchAbs(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_match_abs);
	}

	/**
	 * Retourne le nom de la classe pour MatchAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour MatchAbs
	 */
	public static String get_classNameForMatchAbs(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_match_abs));
	}

	/**
	 * Retourne l'import pour Match_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Match_itf
	 */
	public static String get_importForMatchItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_match_itf);
	}

	/**
	 * Retourne le nom de la classe pour Match_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Match_itf
	 */
	public static String get_classNameForMatchItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_match_itf));
	}

	/**
	 * Retourne l'import pour UserPersistenceAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour UserPersistenceAbs
	 */
	public static String get_importForUserPersistenceAbs(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_userPersistence_abs);
	}

	/**
	 * Retourne le nom de la classe pour UserPersistenceAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour UserPersistenceAbs
	 */
	public static String get_classNameForUserPersistenceAbs(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_userPersistence_abs));
	}

	/**
	 * Retourne l'import pour Dao_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Dao_itf
	 */
	public static String get_importForDaoItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_dao_itf);
	}

	/**
	 * Retourne le nom de la classe pour Dao_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Dao_itf
	 */
	public static String get_classNameForDaoItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_dao_itf));
	}

	/**
	 * Retourne l'import pour DaoJdbcAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour DaoJdbcAbs
	 */
	public static String get_importForDaoJdbcAbs(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_daoJdbc_abs);
	}

	/**
	 * Retourne le nom de la classe pour DaoJdbcAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour DaoJdbcAbs
	 */
	public static String get_classNameForDaoJdbcAbs(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_daoJdbc_abs));
	}

	/**
	 * Retourne l'import pour ColumnsNames_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour ColumnsNames_itf
	 */
	public static String get_importForColumnsNamesItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_columnsNames_itf);
	}

	/**
	 * Retourne le nom de la classe pour ColumnsNames_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour ColumnsNames_itf
	 */
	public static String get_classNameForColumnsNamesItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_columnsNames_itf));
	}

	/**
	 * Retourne l'import pour Entity_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Entity_itf
	 */
	public static String get_importForEntityItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_entity_itf);
	}

	/**
	 * Retourne le nom de la classe pour Entity_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Entity_itf
	 */
	public static String get_classNameForEntityItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_entity_itf));
	}

	/**
	 * Retourne l'import pour BeanTesterAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour BeanTesterAbs
	 */
	public static String get_importForBeanTesterAbs(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_beanTester_abs);
	}

	/**
	 * Retourne le nom de la classe pour BeanTesterAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour BeanTesterAbs
	 */
	public static String get_classNameForBeanTesterAbs(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_beanTester_abs));
	}

	/**
	 * Retourne l'import pour FetchingStrategyTesterAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour FetchingStrategyTesterAbs
	 */
	public static String get_importForFetchingStrategyTesterAbs(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_fetchingStrategyTester_abs);
	}

	/**
	 * Retourne l'import pour FetchingStrategyTesterAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour FetchingStrategyTesterAbs
	 */
	public static String get_importForFetchingStrategyEntityTesterAbs(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_fetchingStrategyEntityTester_abs);
	}

	/**
	 * Retourne le nom de la classe pour FetchingStrategyTesterAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour FetchingStrategyTesterAbs
	 */
	public static String get_classNameForFetchingStrategyTesterAbs(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_fetchingStrategyTester_abs));
	}

	/**
	 * Retourne le nom de la classe pour FetchingStrategyEntityTesterAbs
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * 
	 * @return le nom de la classe pour FetchingStrategyEntityTesterAbs
	 */
	public static String get_classNameForFetchingStrategyEntityTesterAbs(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_fetchingStrategyEntityTester_abs));
	}

	/**
	 * Retourne l'import pour Xto_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Xto_itf
	 */
	public static String get_importForXtoItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_xto_itf);
	}

	/**
	 * Retourne l'import pour Xto_itf pour RS.
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Xto_itf
	 */
	public static String get_importForRsXtoItf(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_xto_rs_itf);
	}

	/**
	 * Retourne le nom de la classe pour Xto_itf
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Xto_itf
	 */
	public static String get_classNameForXtoItf(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_xto_itf));
	}

	/**
	 * Retourne l'import pour Binary
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return l'import pour Binary
	 */
	public static String get_importForBinary(Object p_object) {
		return PropertiesHandler.getProperty(c_idParam_binary);
	}

	/**
	 * Retourne le nom de la classe pour Binary
	 * 
	 * @param p_object un objet (pour trace acceleo)
	 * @return le nom de la classe pour Binary
	 */
	public static String get_classNameForBinary(Object p_object) {
		return getClassName(PropertiesHandler.getProperty(c_idParam_binary));
	}
}
