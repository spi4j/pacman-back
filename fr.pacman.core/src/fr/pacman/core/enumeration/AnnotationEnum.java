package fr.pacman.core.enumeration;

/**
 * Enumération contenant l'ensemble des méta-données possibles dans les
 * différents modèles.
 * 
 * @author MINARM
 */
public enum AnnotationEnum {

	PHYSICAL_NAME("PHYSICAL_NAME", ModelEnum.DSL_ENTITY, true),

	PHYSICAL_SHORT_NAME("PHYSICAL_SHORT_NAME", ModelEnum.DSL_ENTITY, true),

	PHYSICAL_DEFAULT("PHYSICAL_DEFAULT", ModelEnum.DSL_ENTITY, true),

	PHYSICAL_CHECK("PHYSICAL_CHECK", ModelEnum.DSL_ENTITY, false),

	PHYSICAL_UNIQUE("PHYSICAL_UNIQUE", ModelEnum.DSL_ENTITY, true),

	PHYSICAL_SIZE("PHYSICAL_SIZE", ModelEnum.DSL_ENTITY, true),

	VERSION("VERSION", ModelEnum.DSL_ENTITY, false),

	REFERENTIAL("REFERENTIAL", ModelEnum.DSL_SOA_DTO, false),

	COMPUTED("COMPUTED", ModelEnum.DSL_SOA_DTO, false),

	USER("USER", ModelEnum.DSL_SOA_DTO, false),

	PERMISSIONS("PERMISSIONS", ModelEnum.DSL_SOA_SERVICE, true),

	PERMISSIONS_OR("PERMISSIONS_OR", ModelEnum.DSL_SOA_SERVICE, true),

	PERMISSIONS_AND("PERMISSIONS_AND", ModelEnum.DSL_SOA_SERVICE, true),

	VOL_NB_ROWS("VOL_NB_ROWS", ModelEnum.DSL_ENTITY, true),

	VOL_MIN_OCCURS("VOL_MIN_OCCURS", ModelEnum.DSL_ENTITY, true),

	VOL_MAX_OCCURS("VOL_MAX_OCCURS", ModelEnum.DSL_ENTITY, true),

	ACCEPT_UNAUTHENTIFIED("ACCEPT_UNAUTHENTIFIED", ModelEnum.DSL_SOA_SERVICE, false),

	PAGE_SIZE("PAGE_SIZE", ModelEnum.DSL_SOA_SERVICE, true),

	AUTH_TOKEN("AUTH_TOKEN", ModelEnum.DSL_SOA_SERVICE, true),

	API_VERSION("API_VERSION", ModelEnum.DSL_SOA_SERVICE, true),

	LIBRARY_NAME("LIBRARY_NAME", ModelEnum.DSL_SOA_DTO, true),

	LITERAL_PARAM_VALUES("LITERAL_PARAM_VALUES", ModelEnum.DSL_ENTITY, true),

	LITERALS_PARAMS_STRUCT("LITERALS_PARAMS_STRUCT", ModelEnum.DSL_ENTITY, true),
	
	CASCADE_TYPE("CASCADE_TYPE", ModelEnum.DSL_ENTITY, true), 
	
	FETCH_LAZY("FETCH_LAZY", ModelEnum.DSL_ENTITY, false);

	/** Le nom de l'annotation */
	private String _annotationName;

	/** Le type de modèle DSM sur laquelle on l'a trouve */
	// private ModelEnum[] _tab_AnotationModel;
	private ModelEnum _tab_annotationModel;

	/** le boolean qui indique si la métadonnées a un contenu */
	private boolean _content;

	/**
	 * Constructeur avec le nom et les DSM de l'annotation.
	 * 
	 * @param p_annotationName      Le nom de l'annotation.
	 * @param p_tab_annotationModel Le (ou les) DSM sur lesquelles elle s'applique.
	 * @param p_content             'true' si l'annotation possède un contenu.
	 */
	private AnnotationEnum(final String p_annotationName, final ModelEnum p_tab_annotationModel,
			final boolean p_content) {
		_annotationName = p_annotationName;
		_tab_annotationModel = p_tab_annotationModel;
		_content = p_content;
	}

	/**
	 * Retourne le nom de l'annotation.
	 * 
	 * @return Le nom de l'annotation.
	 */
	public String get_annotationName() {
		return _annotationName;
	}

	/**
	 * Boolean qui indique si la métadonnée a un contenu.
	 * 
	 * @return boolean : 'true' si l'annotation doit avoir un cootenu, sinon,
	 *         retourne la valeur 'false'.
	 */
	public boolean hasBody() {
		return _content;
	}

	/**
	 * Retourne Le (ou les) DSM sur lesquels l'annotation s'applique.
	 * 
	 * @return Le tableau contenant les DSM potentiels.
	 */
	public ModelEnum get_tabAnnotationModel() {
		return _tab_annotationModel;
	}

	/**
	 * Retourne une annotation à partir de son titre
	 * 
	 * @param p_titre Le titre de l'annotation
	 * @return Le tableau contenant les types de DSM potentiels.
	 */
	public static AnnotationEnum findByName(final String p_titre) {
		AnnotationEnum a = null;
		for (AnnotationEnum ae : AnnotationEnum.values()) {
			if (ae.get_annotationName().equalsIgnoreCase(p_titre)) {
				a = ae;
			}
		}
		return a;
	}
}
