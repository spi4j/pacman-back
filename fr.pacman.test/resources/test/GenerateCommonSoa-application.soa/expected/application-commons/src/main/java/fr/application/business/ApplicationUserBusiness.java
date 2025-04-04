/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
// CHECKSTYLE:OFF
package fr.application.business; // NOPMD
// CHECKSTYLE:ON
// Start of user code for imports

import fr.application.business.api.annuaire.AdresseService_Itf;
import fr.application.business.api.annuaire.CompetenceService_Itf;
import fr.application.business.api.annuaire.GradesService_Itf;
import fr.application.business.api.annuaire.PaysService_Itf;
import fr.application.business.api.annuaire.PersonneCxfService_Itf;
import fr.application.business.api.annuaire.PersonneService_Itf;
import fr.application.business.api.ref.GradeService_Itf;
import fr.spi4j.business.UserBusiness_Abs;
import fr.spi4j.exception.Spi4jRuntimeException;
import fr.spi4j.persistence.UserPersistence_Abs;

// End of user code

/**
 * Classe permettant de centraliser les factories business de l'application.
 * @author safr@n
 */
public final class ApplicationUserBusiness extends UserBusiness_Abs
{

   /** Singleton. */
   private static ApplicationUserBusiness singleton = new ApplicationUserBusiness();

   /**
    * Constructeur privé.
    */
   private ApplicationUserBusiness ()
   {
      super();
   }

   /**
    * Obtenir la façade de services 'AdresseService_Itf'.
    * @return L'instance désirée.
    */
   public static AdresseService_Itf getAdresseService ()
   {
      return singleton.getBinding(AdresseService_Itf.class);
   }

   /**
    * Obtenir la façade de services 'CompetenceService_Itf'.
    * @return L'instance désirée.
    */
   public static CompetenceService_Itf getCompetenceService ()
   {
      return singleton.getBinding(CompetenceService_Itf.class);
   }

   /**
    * Obtenir la façade de services 'GradeService_Itf'.
    * @return L'instance désirée.
    */
   public static GradeService_Itf getGradeService ()
   {
      return singleton.getBinding(GradeService_Itf.class);
   }

   /**
    * Obtenir la façade de services 'PaysService_Itf'.
    * @return L'instance désirée.
    */
   public static PaysService_Itf getPaysService ()
   {
      return singleton.getBinding(PaysService_Itf.class);
   }

   /**
    * Obtenir la façade de services 'PersonneService_Itf'.
    * @return L'instance désirée.
    */
   public static PersonneService_Itf getPersonneService ()
   {
      return singleton.getBinding(PersonneService_Itf.class);
   }


   /**
    * Obtenir la façade de services 'GradesService_Itf'.
    * @return L'instance désirée.
    */
   public static GradesService_Itf getGradesService ()
   {
      return singleton.getBinding(GradesService_Itf.class);
   }

   /**
    * Obtenir la façade de services 'PersonneCxfService_Itf'.
    * @return L'instance désirée.
    */
   public static PersonneCxfService_Itf getPersonneCxfService ()
   {
      return singleton.getBinding(PersonneCxfService_Itf.class);
   }

   /**
    * @return le singleton de cette factory
    */
   public static ApplicationUserBusiness getSingleton ()
   {
      return singleton;
   }

   @Override
   protected UserPersistence_Abs getUserPersistence ()
   {
      // dans cette factory de la partie commune entre un client et le serveur
      // on ne référence pas les classes d'implémentations "serveur" des services
      // pour ne pas avoir d'erreur de compilation (et pour ne pas avoir ClassNotFoundException à l'exécution)

      try
      {
         return (UserPersistence_Abs) Class.forName("fr.application.persistence.ApplicationParamPersistence")
                  .getMethod("getUserPersistence").invoke(null);
      }
      catch (final Exception v_ex)
      {
         // ne devrait jamais arriver grâce à la génération de code
         throw new Spi4jRuntimeException(v_ex, v_ex.toString(), "???");
      }
   }

   // CHECKSTYLE:OFF
   @Override
   // CHECKSTYLE:ON
   public void initBindings () // NOPMD
   {
      // dans cette factory de la partie commune entre un client et le serveur
      // on ne référence pas les classes d'implémentations "serveur" des services
      // pour ne pas avoir d'erreur de compilation (et pour ne pas avoir ClassNotFoundException à l'exécution)
      final AdresseService_Itf v_AdresseService = wrapService(AdresseService_Itf.class, "fr.application.business.impl_server.annuaire.AdresseService");
      bind(AdresseService_Itf.class, v_AdresseService);

      final CompetenceService_Itf v_CompetenceService = wrapService(CompetenceService_Itf.class, "fr.application.business.impl_server.annuaire.CompetenceService");
      bind(CompetenceService_Itf.class, v_CompetenceService);

      final GradeService_Itf v_GradeService = wrapService(GradeService_Itf.class, "fr.application.business.impl_server.ref.GradeService");
      bind(GradeService_Itf.class, v_GradeService);

      final PaysService_Itf v_PaysService = wrapService(PaysService_Itf.class, "fr.application.business.impl_server.annuaire.PaysService");
      bind(PaysService_Itf.class, v_PaysService);

      final PersonneService_Itf v_PersonneService = wrapService(PersonneService_Itf.class, "fr.application.business.impl_server.annuaire.PersonneService");
      bind(PersonneService_Itf.class, v_PersonneService);

      final GradesService_Itf v_GradesService = wrapService(GradesService_Itf.class, "fr.application.business.impl_server.annuaire.GradesService");
      bind(GradesService_Itf.class, v_GradesService);

      final PersonneCxfService_Itf v_PersonneCxfService = wrapService(PersonneCxfService_Itf.class, "fr.application.business.impl_server.annuaire.PersonneCxfService");
      bind(PersonneCxfService_Itf.class, v_PersonneCxfService);

   }
}
