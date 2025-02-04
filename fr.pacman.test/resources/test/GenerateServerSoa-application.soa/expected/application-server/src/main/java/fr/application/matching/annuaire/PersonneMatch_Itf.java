/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.matching.annuaire;
// Start of user code for imports

import fr.application.business.api.annuaire.PersonneAttributes_Enum;
import fr.application.business.api.annuaire.PersonneDto;
import fr.application.persistence.api.annuaire.PersonneColumns_Enum;
import fr.application.persistence.api.annuaire.PersonneEntity_Itf;
import fr.spi4j.matching.Match_Itf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

// End of user code

/**
 * L'interface définissant le contrat pour le Matcher (= persistance <-> business) sur le type 'Personne'.
 * @author safr@n
 */
public interface PersonneMatch_Itf extends Match_Itf<Long, PersonneDto, PersonneEntity_Itf, PersonneColumns_Enum>
{

   /**
    * Pour un matching 1 pour 1, retourne la colonne Entity associée à l'attribut DTO.
    * @param p_attribute
    *           l'attribut DTO
    * @return la colonne Entity
    */
   PersonneColumns_Enum getColumn (final PersonneAttributes_Enum p_attribute);

   
   // Start of user code PersonneMatch_Itf

   // End of user code
}
