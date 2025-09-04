package fr.pacman.back.core.enumeration;

/**
 * Liste des différents statuts possibles pour une propriété du gestionnaire de
 * propriétés de Pacman.
 * 
 * @author MINARM
 */
public enum PropertyStatus {
	REQUIRED, // la propriété est obligatoire est automatiquement réecrite dans le fichier
				// '.properties' si elle n'est pas trouvée par le gestionnaire de propriétés.

	CONDITIONAL, // la propriété est soumise à une ou plusieurs conditions en fonction de
					// l'existance ou de la valeur d'une ou plusieurs autres propriétés.

	OPTIONAL, // la propriété est totalement optionnelle et n'est pas soumise à contrôle.

	STANDBY, // la propriété est en attente de complétion (saisie de la valeur).

	FILLED, // la propriété est considérée comme saisie par l'utilisateur.

	MEMORY // la propriété est stockée uniquement en mémoire et non sur un support physique
			// comme un fichier '.properties' par example.
}
