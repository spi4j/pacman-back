/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.ws.api.annuaire;
// Start of user code for imports

import fr.spi4j.exception.Spi4jValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// End of user code

/**
 * Definit le contrat de services spécifique pour les services REST.
 * @author safr@n
 */
public interface GradesServiceRSFacade_Itf
{


   /**
    * .
    * @return grades.
	*/
	
    // Start of user code Annotations MethodefindAllGrades

    // End of user code
   List<GradeXto> findAllGrades ();

   

   /**
    * .
	* @param p_offset :
	*			(In)(*) Numéro de ligne pour débuter la requête dans le cadre de la pagination.
	* @param p_limit :
	*			(In)(*) Nombre d'éléments demandés par page dans le cadre de la pagination.
    * @return grades.
	*/
	
    // Start of user code Annotations MethodefindAllPagedGrades

    // End of user code
   List<GradeXto> findAllPagedGrades (final int p_offset, final int p_limit);

   /**
   * Méthode spécifique pour la pagination de l'opération : findAllPagedGrades
   * @return le nombre total d'éléments pour l'opération.
   */
   
   // Start of user code Annotations MethodefindAllPagedGradestotal count
   
   // End of user code
   int findAllPagedGradesTotalCount();
   

}
