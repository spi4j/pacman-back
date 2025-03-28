/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
// CHECKSTYLE:OFF
package fr.application.requirement;
// CHECKSTYLE:ON

import org.junit.jupiter.api.Test;

/**
 * Tests de vérification de version pour chaque exigence entre la modèlisation et l'implémentation courante.<br>
 * Toutes les exigences du projet.
 * @author safr@n
 */
// Annotation for class
// Start of user code Annotation for class
// End of user code
public class RequirementVersion_Test
{
   /**
    * Vérifier que la version de l'exigence REQ_FCT_PERS_01 est la même entre la modèlisation et l'implémentation courante.
    * @see Requirement_Enum#REQ_FCT_PERS_01
    */
   @Test
   public void testVersion_REQ_FCT_PERS_01 ()
   {
      // Version de la modélisation : "1"
      // Start of user code testVersion_REQ_FCT_PERS_01
      // TODO Affecter le No de la version d'implémentation lorsque celle-ci sera conforme avec celle de la modélisation
      Requirement_Enum.REQ_FCT_PERS_01.set_versionImplem();
      // End of user code
   }

   /**
    * Vérifier que la version de l'exigence REQ_TEC_PERS_02 est la même entre la modèlisation et l'implémentation courante.
    * @see Requirement_Enum#REQ_TEC_PERS_02
    */
   @Test
   public void testVersion_REQ_TEC_PERS_02 ()
   {
      // Version de la modélisation : "1"
      // Start of user code testVersion_REQ_TEC_PERS_02
      // TODO Affecter le No de la version d'implémentation lorsque celle-ci sera conforme avec celle de la modélisation
      Requirement_Enum.REQ_TEC_PERS_02.set_versionImplem();
      // End of user code
   }

}
