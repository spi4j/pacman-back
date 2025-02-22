/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.persistence.api.annuaire;
// Start of user code for imports

import fr.application.annuaire.TypeCompetence_Enum;
import fr.application.persistence.api.ApplicationAutoFieldsEntity_Itf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

// End of user code

/**
 * L'interface définissant le contrat de persistance pour le type Personne.
 * @author safr@n
 */
public interface PersonneEntity_Itf extends ApplicationAutoFieldsEntity_Itf<Long>
{
   // CONSTANTES

   
   // Start of user code Constantes PersonneEntity_Itf

   // End of user code

   // METHODES ABSTRAITES

   /**
    * Obtenir nom.
    * @return nom.
    */
   String get_nom ();

   /**
    * Affecter nom.
    * @param p_nom
    *           (In)(*) nom.
    */
   void set_nom (final String p_nom);

   /**
    * Obtenir prenom.
    * @return prenom.
    */
   String get_prenom ();

   /**
    * Affecter prenom.
    * @param p_prenom
    *           (In)(*) prenom.
    */
   void set_prenom (final String p_prenom);

   /**
    * Obtenir civil.
    * @return civil.
    */
   Boolean get_civil ();

   /**
    * Affecter civil.
    * @param p_civil
    *           (In)(*) civil.
    */
   void set_civil (final Boolean p_civil);

   /**
    * Obtenir dateNaissance.
    * @return dateNaissance.
    */
   Date get_dateNaissance ();

   /**
    * Affecter dateNaissance.
    * @param p_dateNaissance
    *           (In)(*) dateNaissance.
    */
   void set_dateNaissance (final Date p_dateNaissance);

   /**
    * Obtenir salaire.
    * @return salaire.
    */
   Double get_salaire ();

   /**
    * Affecter salaire.
    * @param p_salaire
    *           (In)(*) salaire.
    */
   void set_salaire (final Double p_salaire);

   /**
    * Obtenir Grade.
    * @return Grade.
    */
   Long get_Grade_id ();

   /**
    * Affecter Grade.
    * @param p_grade_id
    *           (In) Grade.
    */
   void set_Grade_id (final Long p_grade_id);

   /**
    * Obtenir MarieAvec.
    * @return MarieAvec.
    */
   Long get_MarieAvec_id ();

   /**
    * Affecter MarieAvec.
    * @param p_marieAvec_id
    *           (In) MarieAvec.
    */
   void set_MarieAvec_id (final Long p_marieAvec_id);

   /**
    * Obtenir personne.
    * @return personne.
    */
   Long get_personneParentDe_id ();

   /**
    * Affecter personne.
    * @param p_personneParentDe_id
    *           (In)(*) personne.
    */
   void set_personneParentDe_id (final Long p_personneParentDe_id);



   
   // Start of user code Methodes PersonneEntity_Itf

   // End of user code

}
