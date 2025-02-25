/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.business.impl_server.annuaire;
// Start of user code for imports

import fr.application.business.api.annuaire.PersonneDto;
import fr.application.business.api.annuaire.PersonneService_Itf;
import fr.application.business.api.ref.GradeDto;
import fr.application.business.impl_server.annuaire.PersonneServiceRequirements;
import fr.application.matching.ApplicationUserMatching;
import fr.application.matching.annuaire.PersonneMatch_Itf;
import fr.application.persistence.api.annuaire.PersonneColumns_Enum;
import fr.spi4j.business.Service_Abs;
import fr.spi4j.exception.Spi4jValidationException;
import java.util.Date;
import java.util.List;

// End of user code

/**
 * Implémentation du contrat de services spécifiques pour un type 'Personne'. <br>
 * Pour rappel, les services sont sans état.
 * @author safr@n
 */

// Start of user code annotations service
// End of user code
public class PersonneService extends Service_Abs<Long, PersonneDto, PersonneColumns_Enum> implements PersonneService_Itf
{

   // Rappel : les services sont sans état.
   
   // Start of user code attributs

   // End of user code

   @SuppressWarnings("all")
   private final PersonneServiceRequirements _requirements = new PersonneServiceRequirements (); // NOPMD

   /**
    * Obtenir l'instance de matching sur le type 'Personne'.
    * @return L'instance désirée.
    */
   @Override
   protected PersonneMatch_Itf getMatch ()
   {
      return ApplicationUserMatching.getPersonneMatch ();
   }


   @Override
   public List<PersonneDto> findListParentDeByPersonne (final Long p_personneParentDeId)
   {
      return findByColumn (PersonneColumns_Enum.personneParentDe_id, p_personneParentDeId);
   }

   /**
    * 
    * @param p_identifiant
    *           (In)(*) identifiant.

    * @return personne.
    */
   @Override
   public PersonneDto findPersonneById (final Long p_identifiant)
   {

      // Appel des exigences en provenance de la modélisation

      
      // Start of user code findPersonneById_Personne_personne_Long_identifiant
      // Appel des exigences
      // TODO Méthode à implémenter
      throw new UnsupportedOperationException ();

      // End of user code
   }

	
   /**
    * 
    * @param p_nom
    *           (In)(*) nom.

    * @return personne.
    */
   @Override
   public PersonneDto findPersonneByName (final String p_nom)
   {

      // Appel des exigences en provenance de la modélisation

      
      // Start of user code findPersonneByName_Personne_personne_String_nom
      // Appel des exigences
      // TODO Méthode à implémenter
      throw new UnsupportedOperationException ();

      // End of user code
   }

	
   /**
    * 
    * @param p_identifiant
    *           (In)(*) identifiant.

    * @return personne.
    */
   @Override
   public PersonneDto findPersonneByIdFromRest (final Long p_identifiant)
   {

      // Appel des exigences en provenance de la modélisation

      
      // Start of user code findPersonneByIdFromRest_Personne_personne_Long_identifiant
      // Appel des exigences
      // TODO Méthode à implémenter
      throw new UnsupportedOperationException ();

      // End of user code
   }

	

   
   // Start of user code PersonneService

   // End of user code
}
