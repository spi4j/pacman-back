/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.persistence.api.annuaire;
// Start of user code for imports

import fr.application.annuaire.TypeCompetence_Enum;
import fr.application.persistence.ApplicationParamPersistence;
import fr.application.persistence.ApplicationUserPersistence;
import fr.spi4j.exception.Spi4jValidationException;
import fr.spi4j.persistence.dao.Operator_Enum;
import fr.spi4j.persistence.dao.TableCriteria;
import fr.spi4j.persistence.entity.EntityUtil;
import fr.spi4j.tua.BeanTester_Abs;
import fr.spi4j.type.XtopSup;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

// End of user code

/**
 * Classe de test du dao 'PaysDao_Itf'.
 * @author safr@n
 */

// Start of user code Annotation for class
// End of user code
public class PaysDao_Test extends BeanTester_Abs
{

   /** Le 'UserPersistence' de l'application. */
   private static ApplicationUserPersistence userPersistence;

   /** Le 'PaysDao_Itf' teste. */
   private static PaysDao_Itf dao;

   /** L'id du 'PaysEntity_Itf' stocke en base. */
   private static Long crudId;

   /**
    * Definition du crudId.
    * @param p_crudId
    *           le crudId
    */
   public static void setCrudId (final Long p_crudId)
   {
      PaysDao_Test.crudId = p_crudId;
   }

   /**
    * Methode d'initialisation de la classe de tests.
    */
   @BeforeAll
   public static void setUpBeforeClass ()
   {
      userPersistence = ApplicationParamPersistence.getUserPersistence();
      dao = userPersistence.getPaysDao();

      
      // Start of user code set up before class
      // End of user code
   }

   /**
    * Methode d'initialisation de tests.
    */
   @BeforeEach
   public void setUp ()
   {
      userPersistence.begin();

      
      // Start of user code set up
      // End of user code
   }

   /**
    * Test de recherche de toutes les entitys.
    * @throws Throwable
    *            si erreur de creation de l'entity.
    */
   @Test
   public void testFindAll () throws Throwable
   {
      testCreate();

      final List<PaysEntity_Itf> v_all = dao.findAll();
      assertNotNull(EntityUtil.findInCollectionById(v_all, crudId), 
			"L'entity n'a pas ete trouvee dans la liste de toutes les entitys");

      
      // Start of user code findAll assertions

      // End of user code
   }

   /**
    * Test de creation de l'entity.
    * @throws Throwable
    *            si erreur de creation de l'entity.
    */
   @Test
   public void testCreate () throws Throwable
   {
      final PaysEntity_Itf v_entity = userPersistence.getPaysEntity();

      
      // Start of user code create
      // TODO renseigner donnees de test
      v_entity.set_nom("t");
      v_entity.set_capitale("t");
      v_entity.set_xdmaj(new Date());
      v_entity.set_xtopsup(new XtopSup(DatabaseLineStatus_Enum.active));
      v_entity.set_personnePays_id(1L);
      // End of user code

	  dao.create(v_entity);
	  setCrudId(v_entity.getId());
	  assertNotNull(v_entity.getId(), "L'entity creee devrait avoir une cle primaire renseignee");

      
      // Start of user code create assertions

      // End of user code
   }

   /**
    * Test de (non) creation de l'entity avec 
    * des valeurs nulles sur des champs obligatoires.
    * @throws Throwable
    *            si erreur .
    */
   @Test
   public void testCreateWithNullOnMandatoryFields () throws Throwable
   {
      final PaysEntity_Itf v_entity = userPersistence.getPaysEntity();

      
      // Start of user code create null on mandatory
      // TODO renseigner donnees de test
      v_entity.set_nom(null);
      v_entity.set_capitale(null);
      v_entity.set_xdmaj(new Date());
      v_entity.set_xtopsup(new XtopSup(DatabaseLineStatus_Enum.active));
      v_entity.set_personnePays_id(1L);
      // End of user code    

      try{
        dao.create(v_entity);
        setCrudId(v_entity.getId());
		assertNull(v_entity.getId(), "L'entity ne devrait pas avoir été créé");
      }
      catch(Spi4jValidationException p_e){
           assertTrue(Boolean.TRUE);
      }
   }

   /**
    * Test de recherche par identifiant de l'entity.
    * @throws Throwable
    *            si erreur de creation de l'entity.
    */
   @Test
   public void testFindById () throws Throwable
   {
      testCreate();

      final PaysEntity_Itf v_entity = dao.findById(crudId);
      assertNotNull(v_entity, "L'entity devrait exister dans le referentiel");
      assertNotNull(v_entity.getId(), "L'entity creee devrait avoir une cle primaire renseignee");
      assertNotNull(v_entity.toString(), "L'entity creee devrait avoir un toString");

      
      // Start of user code findById assertions

      // End of user code
   }

   /**
    * Test d egalite entre les champs de l entite pre et post insertion.
    * @throws Throwable
    *            si erreur de creation de l'entity.
    */
   @Test
   public void testAllFieldInserted () throws Throwable
   {

      final PaysEntity_Itf v_entityInsert = userPersistence.getPaysEntity();

      
      // Start of user code findAllFieldInserted create
      // TODO renseigner donnees de test
      v_entityInsert.set_nom("t");
      v_entityInsert.set_capitale("t");
      v_entityInsert.set_xdmaj(new Date());
      v_entityInsert.set_xtopsup(new XtopSup(DatabaseLineStatus_Enum.active));
      v_entityInsert.set_personnePays_id(1L);
      // End of user code

      dao.create(v_entityInsert);
      setCrudId(v_entityInsert.getId());
      final PaysEntity_Itf v_entityRead = dao.findById(crudId);

      
      // Start of user code findAllFieldInserted assertions
      HashCodeBuilder v_hashCodeBuilderEntityInsert = new HashCodeBuilder();

      v_hashCodeBuilderEntityInsert.append(v_entityInsert.get_nom());
      v_hashCodeBuilderEntityInsert.append(v_entityInsert.get_capitale());
      v_hashCodeBuilderEntityInsert.append(v_entityInsert.get_xdmaj());
      v_hashCodeBuilderEntityInsert.append(v_entityInsert.get_xtopsup().get_value());
      v_hashCodeBuilderEntityInsert.append(v_entityInsert.get_personnePays_id());

      int  v_hashCodeEntityInsert = v_hashCodeBuilderEntityInsert.toHashCode();
      HashCodeBuilder v_hashCodeBuilderEntityRead = new HashCodeBuilder();

      v_hashCodeBuilderEntityRead.append(v_entityRead.get_nom());
      v_hashCodeBuilderEntityRead.append(v_entityRead.get_capitale());
      v_hashCodeBuilderEntityRead.append(v_entityRead.get_xdmaj());
      v_hashCodeBuilderEntityRead.append(v_entityRead.get_xtopsup().get_value());
      v_hashCodeBuilderEntityRead.append(v_entityRead.get_personnePays_id());

      int  v_hashCodeEntityRead = v_hashCodeBuilderEntityRead.toHashCode();
      EqualsBuilder v_equalsBuilder = new EqualsBuilder();

      v_equalsBuilder.append(v_entityInsert.get_nom(), v_entityRead.get_nom());
      v_equalsBuilder.append(v_entityInsert.get_capitale(), v_entityRead.get_capitale());
      v_equalsBuilder.append(v_entityInsert.get_xdmaj(), v_entityRead.get_xdmaj());
      v_equalsBuilder.append(v_entityInsert.get_xtopsup().get_value(), v_entityRead.get_xtopsup().get_value());
      v_equalsBuilder.append(v_entityInsert.get_personnePays_id(), v_entityRead.get_personnePays_id());

      assertEquals(v_hashCodeEntityInsert,v_hashCodeEntityRead);
      assertTrue(v_equalsBuilder.isEquals());

      // End of user code
   }

   /**
    * Test de recherche par colonne.
    * @throws Throwable
    *            si erreur de creation de l'entity.
    */
   @Test
   public void testFindByColumn () throws Throwable
   {
      testCreate();

      final List<PaysEntity_Itf> v_entitys = dao.findByColumn(PaysColumns_Enum.pays_id, crudId);
      assertEquals(1, v_entitys.size(), "Il ne devrait exister qu'une entity");
      final PaysEntity_Itf v_entity = v_entitys.get(0);
      assertNotNull(v_entity.getId(), "L'entity creee devrait avoir une cle primaire renseignee");

      
      // Start of user code findByColumn assertions

      // End of user code
   }

   /**
    * Test de recherche par critere.
    * @throws Throwable
    *            si erreur de recherche de l'entity.
    */
   @Test
   public void testFindByCriteria () throws Throwable
   {
      testCreate();

      final TableCriteria<PaysColumns_Enum> v_table = new TableCriteria<>(
               "Test 'find by criteria' en cherchant sur l'id");
      v_table.addCriteria(PaysColumns_Enum.pays_id, Operator_Enum.equals, crudId);
      v_table.addOrderByDesc(PaysColumns_Enum.pays_id);
      final List<PaysEntity_Itf> v_entitys = dao.findByCriteria(v_table);
      assertEquals(1, v_entitys.size(), "Il ne devrait exister qu'une entity");
      final PaysEntity_Itf v_entity = v_entitys.get(0);
      assertNotNull(v_entity.getId(), "L'entity creee devrait avoir une cle primaire renseignee");

      
      // Start of user code findByCriteria assertions

      // End of user code
   }

   /**
    * Test de mise a jour de l'entity.
    * @throws Throwable
    *            si erreur de mise a jour de l'entity.
    */
   @Test
   public void testUpdate () throws Throwable
   {
      testCreate();

      final PaysEntity_Itf v_entity = dao.findById(crudId);

      
      // Start of user code update

      // End of user code

      dao.update(v_entity);

      assertNotNull(v_entity.getId(), "L'entity mise a jour devrait avoir une cle primaire renseignee");

      
      // Start of user code update assertions

      // End of user code
   }

   /**
    * Test de suppression de l'entity.
    * @throws Throwable
    *            si erreur de creation de l'entity.
    */
   @Test
   public void testDelete () throws Throwable
   {
      testCreate();

      final PaysEntity_Itf v_entity = dao.findById(crudId);

      dao.delete(v_entity);
      final List<PaysEntity_Itf> v_all = dao.findAll();
      assertNull(EntityUtil.findInCollectionById(v_all, crudId), 
			"L'entity ne devrait plus exister dans la liste de toutes les entitys");

      
      // Start of user code delete assertions

      // End of user code
   }

   /**
    * Test de l'enumeration des colonnes de l'entity.
    */
   @Test
   public void testColumns ()
   {
      for (final PaysColumns_Enum v_column : PaysColumns_Enum.values())
      {
         assertNotNull(v_column.toString(), "name");
         assertNotNull(v_column.getLogicalColumnName(), "logicalColumnName");
         assertNotNull(v_column.getCompletePhysicalName(), "completePhysicalName");
         assertNotNull(v_column.getSize(), "size");
         assertNotNull(v_column.getTypeColumn(), "typeColumn");
         assertNotNull(v_column.isMandatory(), "mandatory");
         assertNotNull(v_column.isId(), "id");
      }
   }

   /**
    * Test de validation d'une entity.
    */
   @Test
   public void testValidate ()
   {
      // generation des champs
      final Date v_xdmaj = getRandomDate();
      final XtopSup v_xtopsup = getRandomXtopSup();
      final String v_nom = getRandomString();
      final String v_capitale = getRandomString();
      final Long v_personnePays = getRandomLong();

      final PaysEntity_Itf v_entity = userPersistence.getPaysEntity();
      validate(v_entity);
      v_entity.set_xdmaj(v_xdmaj);
      validate(v_entity);
      v_entity.set_xtopsup(v_xtopsup);
      validate(v_entity);
      v_entity.set_nom(v_nom);
      validate(v_entity);
      v_entity.set_capitale(v_capitale);
      validate(v_entity);
      v_entity.set_personnePays_id(v_personnePays);
      validate(v_entity);

      
      // Start of user code test validate

      // End of user code

      // derniere validation avec entity valide
      try
      {
         v_entity.validate();
      }
      catch (final Spi4jValidationException v_e)
      {
         fail(v_e.toString());
      }
   }

   /**
    * Validation de l'entity.
    * @param p_entity
    *           l'entity a valider
    */
   private void validate (final PaysEntity_Itf p_entity)
   {
      try
      {
         p_entity.validate();
      }
      catch (final Spi4jValidationException v_e)
      {
         assertTrue(v_e.getMessage().startsWith("Champ(s)"), "Message incorrect");
      }
   }

   /**
    * Methode de fin de test : rollback.
    */
   @AfterEach
   public void tearDown ()
   {
      userPersistence.rollback();

      
      // Start of user code tear down
      // End of user code
   }


   
   // Start of user code specific service test

   // End of user code

}
