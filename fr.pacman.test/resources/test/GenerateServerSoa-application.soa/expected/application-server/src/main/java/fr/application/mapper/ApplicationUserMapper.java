/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.mapper; // NOPMD

// Start of user code for imports

import fr.application.mapper.annuaire.AdresseMapper_Itf;
import fr.application.mapper.annuaire.AdresseMapper;
import fr.application.mapper.annuaire.CompetenceMapper_Itf;
import fr.application.mapper.annuaire.CompetenceMapper;
import fr.application.mapper.ref.GradeMapper_Itf;
import fr.application.mapper.ref.GradeMapper;
import fr.application.mapper.annuaire.PaysMapper_Itf;
import fr.application.mapper.annuaire.PaysMapper;
import fr.application.mapper.annuaire.PersonneMapper_Itf;
import fr.application.mapper.annuaire.PersonneMapper;

// End of user code

/**
 * Factory permettant de récupérer les instances de classes de mapper.
 * @author safr@n
 */
public final class ApplicationUserMapper
{

   /**
    * Constructeur privé.
    */
   private ApplicationUserMapper ()
   {
      super();
   }

   /**
    * Obtenir la façade de services 'AdresseMapper_Itf'.
    * @return L'instance désirée.
    */
   public static AdresseMapper_Itf getAdresseMapper ()
   {
      return new AdresseMapper();
   }

   /**
    * Obtenir la façade de services 'CompetenceMapper_Itf'.
    * @return L'instance désirée.
    */
   public static CompetenceMapper_Itf getCompetenceMapper ()
   {
      return new CompetenceMapper();
   }

   /**
    * Obtenir la façade de services 'GradeMapper_Itf'.
    * @return L'instance désirée.
    */
   public static GradeMapper_Itf getGradeMapper ()
   {
      return new GradeMapper();
   }

   /**
    * Obtenir la façade de services 'PaysMapper_Itf'.
    * @return L'instance désirée.
    */
   public static PaysMapper_Itf getPaysMapper ()
   {
      return new PaysMapper();
   }

   /**
    * Obtenir la façade de services 'PersonneMapper_Itf'.
    * @return L'instance désirée.
    */
   public static PersonneMapper_Itf getPersonneMapper ()
   {
      return new PersonneMapper();
   }

}
