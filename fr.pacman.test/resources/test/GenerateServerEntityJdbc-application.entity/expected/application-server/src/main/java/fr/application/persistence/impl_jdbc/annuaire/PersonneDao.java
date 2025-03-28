/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.persistence.impl_jdbc.annuaire;
// Start of user code for imports

import fr.application.annuaire.TypeCompetence_Enum;
import fr.application.persistence.api.ApplicationAutoFieldsColumns_Enum;
import fr.application.persistence.api.annuaire.PersonneColumns_Enum;
import fr.application.persistence.api.annuaire.PersonneDao_Itf;
import fr.application.persistence.api.annuaire.PersonneEntity_Itf;
import fr.spi4j.persistence.dao.jdbc.DaoJdbc_Abs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// End of user code

/**
 * Implémentation JDBC du DAO Personne.
 * @author safr@n
 */
public class PersonneDao extends DaoJdbc_Abs<PersonneEntity_Itf, PersonneColumns_Enum> implements PersonneDao_Itf
{
   // CONSTANTES

   
   // Start of user code Constantes PersonneDao

   // End of user code

   // ATTRIBUTS

   
   // Start of user code Attributs PersonneDao

   // End of user code

   // METHODES

   /**
    * Constructeur par défaut.
    */
   public PersonneDao ()
   {
      super (PersonneColumns_Enum.c_tableName, PersonneColumns_Enum.values (), ApplicationAutoFieldsColumns_Enum.values ());
   }

   @Override
   protected Map<String, Object> getMapValueByLogicalNameFromEntity (final PersonneEntity_Itf p_Entity)
   {
      final Map<String, Object> v_map_valueByColumnName = new LinkedHashMap<>();
      v_map_valueByColumnName.put(PersonneColumns_Enum.personne_id.getLogicalColumnName(), p_Entity.getId());
      v_map_valueByColumnName.put(PersonneColumns_Enum.nom.getLogicalColumnName(), p_Entity.get_nom());
      v_map_valueByColumnName.put(PersonneColumns_Enum.prenom.getLogicalColumnName(), p_Entity.get_prenom());
      v_map_valueByColumnName.put(PersonneColumns_Enum.civil.getLogicalColumnName(), p_Entity.get_civil());
      v_map_valueByColumnName.put(PersonneColumns_Enum.dateNaissance.getLogicalColumnName(), p_Entity.get_dateNaissance());
      v_map_valueByColumnName.put(PersonneColumns_Enum.salaire.getLogicalColumnName(), p_Entity.get_salaire());
      v_map_valueByColumnName.put(PersonneColumns_Enum.grade_id.getLogicalColumnName(), p_Entity.get_Grade_id());
      v_map_valueByColumnName.put(PersonneColumns_Enum.marieAvec_id.getLogicalColumnName(), p_Entity.get_MarieAvec_id());
      v_map_valueByColumnName.put(PersonneColumns_Enum.personneParentDe_id.getLogicalColumnName(), p_Entity.get_personneParentDe_id());
      v_map_valueByColumnName.put(ApplicationAutoFieldsColumns_Enum.xdmaj.getLogicalColumnName(), p_Entity.get_xdmaj());
      v_map_valueByColumnName.put(ApplicationAutoFieldsColumns_Enum.xtopsup.getLogicalColumnName(), p_Entity.get_xtopsup().get_value());
      return v_map_valueByColumnName;
   }

   @Override
   protected PersonneEntity_Itf getEntityFromMapValueByLogicalName (final Map<String, Object> p_map_valueByColumnName)
   {
      final PersonneEntity_Itf v_entity = new PersonneEntity ();
      v_entity.setId((Long) p_map_valueByColumnName.get(PersonneColumns_Enum.personne_id.getLogicalColumnName()));
      v_entity.set_nom((String) p_map_valueByColumnName.get(PersonneColumns_Enum.nom.getLogicalColumnName ()));
      v_entity.set_prenom((String) p_map_valueByColumnName.get(PersonneColumns_Enum.prenom.getLogicalColumnName ()));
      v_entity.set_civil((Boolean) p_map_valueByColumnName.get(PersonneColumns_Enum.civil.getLogicalColumnName ()));
      v_entity.set_dateNaissance((Date) p_map_valueByColumnName.get(PersonneColumns_Enum.dateNaissance.getLogicalColumnName ()));
      v_entity.set_salaire((Double) p_map_valueByColumnName.get(PersonneColumns_Enum.salaire.getLogicalColumnName ()));
      v_entity.set_Grade_id((Long) p_map_valueByColumnName.get(PersonneColumns_Enum.grade_id.getLogicalColumnName()));
      v_entity.set_MarieAvec_id((Long) p_map_valueByColumnName.get(PersonneColumns_Enum.marieAvec_id.getLogicalColumnName()));
      v_entity.set_personneParentDe_id((Long) p_map_valueByColumnName.get(PersonneColumns_Enum.personneParentDe_id.getLogicalColumnName()));
      v_entity.set_xdmaj((Date) p_map_valueByColumnName.get(ApplicationAutoFieldsColumns_Enum.xdmaj.getLogicalColumnName()));
      v_entity.set_xtopsup((XtopSup) p_map_valueByColumnName.get(ApplicationAutoFieldsColumns_Enum.xtopsup.getLogicalColumnName()));
      return v_entity;
   }

   
   // Start of user code Methodes PersonneDao

   // End of user code

}
