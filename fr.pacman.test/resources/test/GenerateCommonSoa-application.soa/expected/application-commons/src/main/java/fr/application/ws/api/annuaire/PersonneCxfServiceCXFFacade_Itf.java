/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.ws.api.annuaire;
// Start of user code for imports

import java.util.List;

// End of user code

/**
 * Definit le contrat de services spécifiques.
 * @author safr@n
 */
public interface PersonneCxfServiceCXFFacade_Itf
{


   /**
    * .
    * @param p_identifiant
    *           (In)(*) identifiant.
    * @return personne.
	*/
    
    // Start of user code Annotations Méthode

    // End of user code
   PersonneXto findPersonneByIdFromCxf (final Long p_identifiant);

}
