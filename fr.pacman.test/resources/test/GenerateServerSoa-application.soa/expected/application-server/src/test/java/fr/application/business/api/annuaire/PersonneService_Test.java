/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.business.api.annuaire;
// Start of user code for imports

import fr.application.business.ApplicationUserBusiness;
import fr.application.matching.ApplicationUserMatching;
import fr.application.persistence.ApplicationParamPersistence;
import fr.application.persistence.ApplicationUserPersistence;
import fr.spi4j.Parameters;
import fr.spi4j.business.dto.DtoUtil;
import fr.spi4j.tua.BeanTester_Abs;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

// End of user code

/**
 * Classe de test du service 'PersonneService_Itf'.
 * @author safr@n
 */

// Start of user code Annotation for class
// End of user code
public class PersonneService_Test  extends BeanTester_Abs
{ 
   /** Le 'UserPersistence' de l'application. */
   private static ApplicationUserPersistence userPersistence;

   /** Le 'PersonneService_Itf' testé. */
   private static PersonneService_Itf service;

   /** L'id du 'PersonneDto' stocké en base. */
   private static Long crudId;

   /**
    * Définition du crudId.
    * @param p_crudId
    *           le crudId
    */
   public static void setCrudId (final Long p_crudId)
   {
      PersonneService_Test.crudId = p_crudId;
   }

   /**
    * Méthode d'initialisation de la classe de tests.
    */
   @BeforeAll
   public static void setUpBeforeClass ()
   {  
      Parameters.setH2Database();
      userPersistence = ApplicationParamPersistence.getUserPersistence ();
      service = ApplicationUserBusiness.getPersonneService ();

      
      // Start of user code set up before class
      // End of user code
   }

   /**
    * Méthode d'initialisation de tests.
    */
   @BeforeEach
   public void setUp ()
   {
	  userPersistence.begin ();

      
      // Start of user code set up
      // End of user code
   }

   /**
    * Test de recherche de toutes les entitys.
    * @throws Throwable
    *            exception
    */
   @Test
   public void testFindAll () throws Throwable
   {
      testCreate ();

      final List<PersonneDto> v_all = service.findAll ();
      assertNotNull(DtoUtil.findInCollectionById (v_all, crudId), 
			"Le dto n'a pas été trouvé dans la liste de tous les dto");

      
      // Start of user code findAll assertions

      // End of user code
   }

   /**
    * Test de création de l'entity.
    * @throws Throwable
    *            exception
    */
   @Test
   public void testCreate () throws Throwable
   {
      final PersonneDto v_PersonneDto = new PersonneDto ();

      
      // Start of user code create
      // TODO renseigner données de test
      v_PersonneDto.set_nom ("Str");
      v_PersonneDto.set_prenom ("Str");
      v_PersonneDto.set_civil (false);
      v_PersonneDto.set_dateNaissance (new Date());
      v_PersonneDto.set_salaire (2D);
      v_PersonneDto.set_grade_id (1L);
      v_PersonneDto.set_marieAvec_id (1L);
      v_PersonneDto.set_personneParentDe_id (1L);

      // End of user code

      final PersonneDto v_createdPersonneDto = service.save (v_PersonneDto);

      setCrudId (v_createdPersonneDto.getId ());

      assertNotNull (v_createdPersonneDto.getId (), "Le dto créé devrait avoir une clé primaire renseignée");

      
      // Start of user code create assertions


      // End of user code
   }

   /**
    * Test de recherche par identifiant de l'entity.
    * @throws Throwable
    *            exception
    */
   @Test
   public void testFindById () throws Throwable
   {
      testCreate ();

      final PersonneDto v_dto = service.findById (crudId);
      assertNotNull (v_dto, "Le dto devrait exister dans le référentiel");
      assertNotNull (v_dto.getId (), "Le dto créé devrait avoir une clé primaire renseignée");
      assertNotNull (v_dto.toString (), "Le dto créé devrait avoir un toString");

      
      // Start of user code findById assertions

      // End of user code
   }

   /**
    * Test de mise à jour de l'entity.
    * @throws Throwable
    *            exception
    */
   @Test
   public void testUpdate () throws Throwable
   {
      testCreate ();

      final PersonneDto v_dto = service.findById (crudId);

      
      // Start of user code update

      // End of user code

      final PersonneDto v_updatedDto = service.save (v_dto);

      assertNotNull (v_updatedDto.getId (), "Le dto mis à jour devrait avoir une clé primaire renseignée");

      
      // Start of user code update assertions

      // End of user code
   }

   /**
    * Test de suppression de l'entity.
    * @throws Throwable
    *            exception
    */
   @Test
   public void testDelete () throws Throwable
   {
      testCreate ();

      final PersonneDto v_dto = service.findById (crudId);

      service.delete(v_dto);
      final List<PersonneDto> v_all = service.findAll ();
      assertNull (DtoUtil.findInCollectionById (v_all, crudId), 
			"Le dto ne devrait plus exister dans la liste de tous les dto");

      
      // Start of user code delete assertions

      // End of user code
   }

   /**
    * Test de la méthode 'getColumn'.
    */
   @Test
   public void testGetColumn ()
   {
      ApplicationUserMatching.getPersonneMatch ().getColumn (PersonneAttributes_Enum.id);
      
      // Start of user code getColumn
      ApplicationUserMatching.getPersonneMatch ().getColumn (PersonneAttributes_Enum.nom);
      // End of user code
   }

   /**
    * Test de l'opération 'findPersonneById'.
    * @throws Throwable
    *            exception
    */
   @Test
   public void testFindPersonneById_Personne_personne_Long_identifiant () throws Throwable
   {
      
      // Start of user code findPersonneById_Personne_personne_Long_identifiant
      // TODO : A Implémenter
      fail ("Test non implémenté");
      // End of user code
   }

   /**
    * Test de l'opération 'findPersonneByName'.
    * @throws Throwable
    *            exception
    */
   @Test
   public void testFindPersonneByName_Personne_personne_String_nom () throws Throwable
   {
      
      // Start of user code findPersonneByName_Personne_personne_String_nom
      // TODO : A Implémenter
      fail ("Test non implémenté");
      // End of user code
   }

   /**
    * Test de l'opération 'findPersonneByIdFromRest'.
    * @throws Throwable
    *            exception
    */
   @Test
   public void testFindPersonneByIdFromRest_Personne_personne_Long_identifiant () throws Throwable
   {
      
      // Start of user code findPersonneByIdFromRest_Personne_personne_Long_identifiant
      // TODO : A Implémenter
      fail ("Test non implémenté");
      // End of user code
   }

   /**
    * Méthode de fin de test : rollback.
    */
   @AfterEach
   public void tearDown ()
   {
      userPersistence.rollback ();

      
      // Start of user code tear down
      // End of user code
   }

   
   // Start of user code specific service test

   // End of user code

}
