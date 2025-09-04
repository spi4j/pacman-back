package fr.pacman.back.core.service;

import org.eclipse.emf.ecore.EObject;
import org.obeonetwork.dsl.environment.Annotation;
import org.obeonetwork.dsl.environment.MetaData;
import org.obeonetwork.dsl.environment.MetaDataContainer;
import org.obeonetwork.dsl.environment.ObeoDSMObject;

import fr.pacman.back.core.enumeration.AnnotationEnum;

/**
 * Classe utilitaire pour la récupération des annotations dans le modèle. Les
 * annotations sont les métadonnées utilisateur éventuellement présentes dans
 * l'onglet 'METADATAS' de chaque {@link EObject} défini dans le modèle.
 * 
 * @author MINARM
 */
public final class AnnotationUtils {

	/**
	 * Constructeur privé.
	 */
	private AnnotationUtils() {
		// RAS
	}

	/**
	 * Vérifie si une annotation existe.
	 * 
	 * @param p_obj        l'objet annoté
	 * @param p_annotation l'annotation recherchée
	 * @return true si l'annotation existe, false sinon
	 */
	public static boolean is_annotationExistsRecursively(final ObeoDSMObject p_obj, final String p_annotation) {
		return isAnnotationExists(p_obj, p_annotation, true);
	}

	/**
	 * Vérifie si une annotation existe.
	 * 
	 * @param p_obj        l'objet annoté
	 * @param p_annotation l'annotation recherchée
	 * @return true si l'annotation existe, false sinon
	 */
	public static boolean is_annotationExistsRecursively(final ObeoDSMObject p_obj, final AnnotationEnum p_annotation) {
		return isAnnotationExists(p_obj, p_annotation.get_annotationName(), true);
	}

	/**
	 * Vérifie si une annotation existe.
	 * 
	 * @param p_obj        l'objet annoté
	 * @param p_annotation l'annotation recherchée
	 * @return true si l'annotation existe, false sinon
	 */

	public static boolean is_annotationExists(final ObeoDSMObject p_obj, final String p_annotation) {
		return isAnnotationExists(p_obj, p_annotation, false);
	}

	/**
	 * Vérifie si une annotation existe.
	 * 
	 * @param p_obj        l'objet annoté
	 * @param p_annotation l'annotation recherchée
	 * @return true si l'annotation existe, false sinon
	 */

	public static boolean is_annotationExists(final ObeoDSMObject p_obj, final AnnotationEnum p_annotation) {
		return isAnnotationExists(p_obj, p_annotation.get_annotationName(), false);
	}

	/**
	 * Retourne la valeur d'une annotation, en remontant récursivement dans le
	 * modèle à partir d'un objet p_obj.
	 * 
	 * @param p_obj        l'objet source de la recherche.
	 * @param p_annotation le titre de l'annotation recherchée.
	 * @return la valeur de l'annotation trouvée, sinon null
	 */
	public static String get_annotationBodyRecursively(final ObeoDSMObject p_obj, final String p_annotation) {
		return getAnnotationBody(p_obj, p_annotation, true);
	}

	/**
	 * Retourne la valeur d'une annotation, attachée à un objet p_obj.
	 * 
	 * @param p_obj        l'objet contenant l'annotation
	 * @param p_annotation le titre de l'annotation recherchée
	 * @return la valeur de l'annotation trouvée, sinon null.
	 */
	public static String get_annotationBody(final ObeoDSMObject p_obj, final String p_annotation) {
		return getAnnotationBody(p_obj, p_annotation, false);
	}

	/**
	 * Retourne la valeur d'une annotation, attachée à un objet p_obj.
	 * 
	 * @param p_obj        l'objet contenant l'annotation
	 * @param p_annotation l'annotation recherchée
	 * @return la valeur de l'annotation trouvée, sinon null.
	 */
	private static String get_annotationBody(final ObeoDSMObject p_obj, final AnnotationEnum p_annotation) {
		return getAnnotationBody(p_obj, p_annotation.get_annotationName(), false);
	}

	/**
	 * Retourne la valeur d'une annotation, récursivement ou non.
	 * 
	 * @param p_obj        l'objet source de la recherche
	 * @param p_annotation le titre de l'annotation recherchée
	 * @param p_recursive  true si la recherche doit être récursive, false si la
	 *                     recherche doit se faire seulement sur l'objet p_obj
	 * @return la valeur de l'annotation trouvée, sinon null.
	 */
	private static String getAnnotationBody(final ObeoDSMObject p_obj, final String p_annotation,
			final boolean p_recursive) {
		if (p_obj == null)
			return null;

		final MetaDataContainer metadataContainer = p_obj.getMetadatas();
		final String body = get_annotationBody(metadataContainer, p_annotation);
		if (body != null)
			return body;

		if (p_recursive) {
			final EObject containerObject = p_obj.eContainer();
			if (containerObject instanceof ObeoDSMObject) {
				return getAnnotationBody((ObeoDSMObject) containerObject, p_annotation, p_recursive);
			}
		}
		return null;
	}

	/**
	 * Retourne la valeur d'une annotation dans un MetadataContainer.
	 * 
	 * @param p_metadataContainer le container de métadonnées
	 * @param p_annotation        l'annotation recherchée
	 * @return la valeur de l'annotation trouvée, sinon null.
	 */
	public static String get_annotationBody(final MetaDataContainer p_metadataContainer,
			final AnnotationEnum p_annotation) {
		return get_annotationBody(p_metadataContainer, p_annotation.get_annotationName());
	}

	/**
	 * Retourne la valeur d'une annotation dans un MetadataContainer.
	 * 
	 * @param p_metadataContainer le container de métadonnées
	 * @param p_annotation        l'annotation recherchée
	 * @return la valeur de l'annotation trouvée, sinon null.
	 */
	public static String get_annotationBody(final MetaDataContainer p_metadataContainer, final String p_annotation) {
		if (p_metadataContainer != null) {
			for (final MetaData m : p_metadataContainer.getMetadatas()) {
				if (m instanceof Annotation) {
					final Annotation anno = (Annotation) m;
					if (anno != null && anno.getTitle() != null
							&& anno.getTitle().trim().equalsIgnoreCase(p_annotation.trim()) && anno.getBody() != null
							&& !anno.getBody().trim().isEmpty()) {
						return anno.getBody();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Vérifie si une annotation existe.
	 * 
	 * @param p_obj        l'objet annoté
	 * @param p_annotation l'annotation recherchée
	 * @param p_recursive  true pour chercher récursivement parmi les parents
	 * @return true si l'annotation existe, false sinon
	 */
	private static boolean isAnnotationExists(final ObeoDSMObject p_obj, final String p_annotation,
			final boolean p_recursive) {
		if (p_obj == null) {
			return false;
		}
		final MetaDataContainer metadataContainer = p_obj.getMetadatas();
		if (metadataContainer != null) {
			for (final MetaData m : metadataContainer.getMetadatas()) {
				if (m instanceof Annotation) {
					final Annotation anno = (Annotation) m;
					if (anno != null && anno.getTitle() != null
							&& anno.getTitle().trim().equalsIgnoreCase(p_annotation.trim())) {
						return true;
					}
				}
			}
		}
		if (p_recursive) {
			final EObject containerObject = p_obj.eContainer();
			if (containerObject instanceof ObeoDSMObject) {
				return isAnnotationExists((ObeoDSMObject) containerObject, p_annotation, p_recursive);
			}
		}
		return false;
	}

	public static boolean is_metaUser(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.USER);
	}

	public static boolean has_metaPhysicalName(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.PHYSICAL_NAME);
	}

	public static boolean has_metaPhysicalShortname(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.PHYSICAL_SHORT_NAME);
	}

	public static boolean is_metaReferential(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.REFERENTIAL);
	}

	public static boolean has_metaPhysicalSize(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.PHYSICAL_SIZE);
	}

	public static boolean has_metaPhysicalDefault(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.PHYSICAL_DEFAULT);
	}

	public static boolean has_metaPhysicalCheck(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.PHYSICAL_CHECK);
	}

	public static boolean has_metaPhysicalUnique(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.PHYSICAL_UNIQUE);
	}
	
	public static boolean has_metaCascadeType(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.CASCADE_TYPE);
	}

	public static boolean has_metaVersion(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.VERSION);
	}
	
	public static boolean has_metaPermission(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.PERMISSIONS);
	}
	
	public static boolean has_metaPermissionOr(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.PERMISSIONS_OR);
	}
	
	public static boolean has_metaPermissionAnd(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.PERMISSIONS_AND);
	}

	public static boolean is_metaComputed(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.COMPUTED);
	}
	
	public static boolean is_metaFetchLazy(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.FETCH_LAZY);
	}

	public static boolean has_metaAuthToken(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.AUTH_TOKEN);
	}
	
	public static String get_metaUser(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.USER);
	}

	public static String get_metaPhysicalName(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PHYSICAL_NAME);
	}

	public static String get_metaPhysicalShortname(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PHYSICAL_SHORT_NAME);
	}

	public static String get_metaLibraryName(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.LIBRARY_NAME);
	}

	public static String get_metaReferential(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.REFERENTIAL);
	}

	public static String get_metaPhysicalSize(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PHYSICAL_SIZE);
	}

	public static String get_metaPhysicalDefault(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PHYSICAL_DEFAULT);
	}

	public static String get_metaPhysicalCheck(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PHYSICAL_CHECK);
	}

	public static String get_metaPhysicalUnique(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PHYSICAL_UNIQUE);
	}

	public static String get_metaVersion(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.VERSION);
	}
	
	public static String get_metaPermission(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PERMISSIONS);
	}
	
	public static String get_metaPermissionOr(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PERMISSIONS_OR);
	}
	
	public static String get_metaPermissionAnd(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PERMISSIONS_AND);
	}
	
	public static String get_metaComputed(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.COMPUTED);
	}

	public static String get_metaPermissions(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PERMISSIONS);
	}

	public static String get_metaPermissionsOr(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PERMISSIONS_OR);
	}

	public static String get_metaPersmissionsAnd(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PERMISSIONS_AND);
	}

	public static String get_metaAcceptUnauth(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.ACCEPT_UNAUTHENTIFIED);
	}

	public static String get_metaVolNbRows(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.VOL_NB_ROWS);
	}

	public static String get_metaVolMinOccurs(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.VOL_MIN_OCCURS);
	}

	public static String get_metaVolMaxOccurs(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.VOL_MAX_OCCURS);
	}

	public static String get_metaPageSize(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.PAGE_SIZE);
	}

	public static String get_metaAuthToken(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.AUTH_TOKEN);
	}

	public static String get_metaApiVersion(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.API_VERSION);
	}

	public static String get_metaLiteralParamValues(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.LITERAL_PARAM_VALUES);
	}

	public static String get_metaLiteralParamsStruct(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.LITERALS_PARAMS_STRUCT);
	}
	
	public static String get_metaLiteralCascadeType(final ObeoDSMObject p_object) {
		return get_annotationBody(p_object, AnnotationEnum.CASCADE_TYPE);
	}
	
	public static boolean has_metaEntityManager(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.ENTITY_MANAGER);
	}
	
	public static boolean has_metaSkipEmptyValues(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.SKIP_EMPTY_VALUES);
	}
	

	public static boolean has_metaLiteralParamsStruct(final ObeoDSMObject p_object) {
		return is_annotationExists(p_object, AnnotationEnum.LITERALS_PARAMS_STRUCT)
				&& get_annotationBody(p_object, AnnotationEnum.LITERALS_PARAMS_STRUCT) != null
				&& !get_annotationBody(p_object, AnnotationEnum.LITERALS_PARAMS_STRUCT).isEmpty();
	}
}
