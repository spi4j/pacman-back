package fr.pacman.core.convention.rule;

import fr.pacman.core.property.PacmanProperty;
import fr.pacman.core.property.PropertiesCategory;
import fr.pacman.core.property.PropertiesHandler;

/**
 * Classe des règles communes pour le nommage. Placer les propriétés dans
 * l'ordre d'affichage désiré dans le fichier.
 *
 * @author MINARM
 */
public class CommonNamingRule extends PropertiesCategory {

	private static final String c_idParam_prefixList = "prefixList";
	private static final String c_idParam_prefixMap = "prefixMap";
	private static final String c_idParam_suffixEntity = "suffixEntity";
	private static final String c_idParam_suffixService = "suffixService";
	private static final String c_idParam_suffixDto = "suffixDto";

	@Override
	protected String get_propertiesFileName() {
		return "nommage.properties";
	}

	@Override
	protected boolean is_defaultFileForAdditionalproperties() {
		return false;
	}

	@Override
	protected PacmanProperty[] initPacmanProperties() {
		return new PacmanProperty[] {

				PacmanProperty.newRequired(c_idParam_prefixList, new String[] { "", "tab" }, "Le prefixe des listes"),

				PacmanProperty.newRequired(c_idParam_prefixMap, new String[] { "", "map" }, "Le prefixe des map"),

				PacmanProperty.newRequired(c_idParam_suffixEntity, new String[] { "entity", "" },
						"Le suffixe des entites"),

				PacmanProperty.newRequired(c_idParam_suffixDto, new String[] { "record", "" },
						"Le suffixe des objets métier"),

				PacmanProperty.newRequired(c_idParam_suffixService, new String[] { "service", "" },
						"Le suffixe des services soa"), };
	}

	public static String get_entitySuffix() {
		return PropertiesHandler.getProperty(c_idParam_suffixEntity);
	}

	public static String get_entitySuffix(Object object) {
		return get_entitySuffix();
	}

	public static String get_serviceSuffix() {
		return PropertiesHandler.getProperty(c_idParam_suffixService);
	}

	public static String get_serviceSuffix(Object object) {
		return get_serviceSuffix();
	}

	public static String get_dtoSuffix() {
		return PropertiesHandler.getProperty(c_idParam_suffixDto);
	}

	public static String get_dtoSuffix(Object object) {
		return get_dtoSuffix();
	}

	public static String get_listPrefix() {
		return PropertiesHandler.getProperty(c_idParam_prefixList);
	}

	public static String get_listPrefix(Object object) {
		return get_listPrefix();
	}

	public static String get_mapPrefix() {
		return PropertiesHandler.getProperty(c_idParam_prefixMap);
	}

	public static String get_mapPrefix(Object object) {
		return get_mapPrefix();
	}
}
