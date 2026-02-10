package fr.pacman.back.core.ui.util;

/**
 * Utilitaire regroupant les noms fonctionnels des représentations Sirius
 * utilisées par Pacman.
 * 
 * Ces constantes correspondent aux noms des types de représentations
 * (diagrammes, hiérarchies, etc.) tels qu'ils sont définis dans le point de vue
 * Sirius.
 * 
 * L'utilisateur doit pas renommer les diagrammes dans l'interface Eclipse sous
 * peine de ne pas faire fonctionner correctement la récupération des objets
 * suite aux potentielles erreurs de modélisation. Un message d'avertissement
 * est positionné en ce sens dans la documentation Pacman.
 * 
 */
public abstract class RepresentationUtils {

	/** Représentation hiérarchique des namespaces d'entités. */
	public static final String c_entityNH = "Entities Namespaces Hierarchy";

	/** Représentation hiérarchique des namespaces de DTOs. */
	public static final String c_dtoNH = "DTO Namespaces Hierarchy";

	/** Diagramme SOA principal. */
	public static final String c_soaD = "SOA Diagram";

	/** Diagramme des DTOs. */
	public static final String c_dtoD = "DTO Diagram";

	/** Diagramme des contrats de composants. */
	public static final String c_componentD = "Component Contract Diagram";

	/** Diagramme des entités métier. */
	public static final String c_entityD = "Entities Diagram";

	/** Diagramme des règles de gestion. */
	public static final String c_requirementT = "Requirements Table";

	/***/
	public static final String c_entityPN = "EV_Entities_PhysicalNames";

	/***/
	public static final String c_dtoPN = "EV_DTO_PhysicalNames";

	/** Liste des diagrammes de représentation liés à entity. */
	public static final String c_entityR = c_entityNH + c_entityD;

	/** Liste des diagrammes de représentation liés à soa. */
	public static final String c_soaR = c_dtoNH + c_dtoD + c_soaD + c_componentD;
	
	/** Liste des représentations liées à requirement. */
	public static final String c_requirementR = c_requirementT;
}
