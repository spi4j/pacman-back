/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.business.api.annuaire;
// Start of user code for imports

import fr.spi4j.business.Service_Itf;
import fr.spi4j.exception.Spi4jValidationException;
import java.util.Date;
import java.util.List;

// End of user code

/**
 * Définit le contrat de services spécifiques pour un type 'Personne'.
 * @author safr@n
 */

// Start of user code Annotations Service
// End of user code
public interface PersonneService_Itf extends Service_Itf<Long, PersonneDto>
{


   /**
    * Obtenir la liste d'objets de type 'Personne' associés à l'instance de type 'Personne'.
    * @param p_personneParentDeId
    *           (In)(*) personne
    * @return une liste de PersonneDto ayant p_personneParentDeId = p_personneParentDeId
    */
	
	// Start of user code Annotations ParentDep_personneParentDeId

	// End of user code
   List<PersonneDto> findListParentDeByPersonne (final Long p_personneParentDeId);

   /**
    * 
    * @param p_identifiant
    *           (In)(*) identifiant.
    * @return personne.
	*/
	
	// Start of user code Annotations findPersonneById_Personne_personne_Long_identifiant

	// End of user code
	
    PersonneDto findPersonneById (final Long p_identifiant); 

	

   /**
    * 
    * @param p_nom
    *           (In)(*) nom.
    * @return personne.
	*/
	
	// Start of user code Annotations findPersonneByName_Personne_personne_String_nom

	// End of user code
	
    PersonneDto findPersonneByName (final String p_nom); 

	

   /**
    * 
    * @param p_identifiant
    *           (In)(*) identifiant.
    * @return personne.
	*/
	
	// Start of user code Annotations findPersonneByIdFromRest_Personne_personne_Long_identifiant

	// End of user code
	
    PersonneDto findPersonneByIdFromRest (final Long p_identifiant); 

	

    
    // Start of user code Methodes PersonneService_Itf

    // End of user code
}
