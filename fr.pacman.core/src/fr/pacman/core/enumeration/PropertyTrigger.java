package fr.pacman.core.enumeration;

/**
 * Liste des différentes temporalités pour l'application d'une stratégie sur une
 * propriété du gestionnaire de propriétés de Pacman.
 * 
 * @author MINARM
 */
public enum PropertyTrigger {
	ONSTART, // la stratégie est appliquée uniquement au démarrage initial du générateur
				// (premier lancement).

	ONCHANGE, // la stratégie est appliquée uniquement sur détection du changement de valeur
				// pour la propriété.

	ONCHAIN, //

	ALWAYS, // la stratégie est toujours appliquée (à chaque demande de génération).

	ONCREATE_CHANGE //
}
