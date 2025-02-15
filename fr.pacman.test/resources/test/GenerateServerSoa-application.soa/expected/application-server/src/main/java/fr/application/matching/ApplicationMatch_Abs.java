/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.matching;
// Start of user code for imports

import fr.application.persistence.api.ApplicationAutoFieldsEntity_Itf;
import fr.spi4j.business.dto.Dto_Itf;
import fr.spi4j.exception.Spi4jRuntimeException;
import fr.spi4j.exception.Spi4jValidationException;
import fr.spi4j.matching.Match_Abs;
import fr.spi4j.persistence.DatabaseLineStatus_Enum;
import fr.spi4j.persistence.dao.OperatorLogical_Enum;
import fr.spi4j.persistence.dao.TableCriteria;
import fr.spi4j.persistence.entity.ColumnsNames_Itf;
import fr.spi4j.type.XtopSup;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

// End of user code

/**
 * Classe abstraite de Matching entre le DTO et l'entity spécifique à l'application en particulier pour 
 * gérer les attributs SQL additionnels tels que xtopsup et xdmaj (par exemple).
 * Cette classe intermédiaire n'est générée que lors de l'ajout de champs automatiques !
 *
 * Dans le cadre du champ automatique de suppression logique les valeurs possibles sont : 
 * 
 * - 0 'active' : La ligne est disponible pour l'ensemble des opérations SQL.
 * - 1 'deletedForNewReference : La ligne n'est plus disponible pour des créations y faisant référence.
 * - 2 'deletedForAll' : La ligne n'est plus disponible, elle est considérée comme supprimée.
 * - 3 'deletedForTrash' : La ligne est à supprimer physiquement de la base (batch, etc...).
 * 
 * @author safr@n
 * @param <TypeId>
 *           Type de l'identifiant du DTO
 * @param <TypeDto>
 *           Type de DTO
 * @param <TypeEntity>
 *           Type d'entity
 * @param <Columns_Enum>
 *           Type de l'énumération des colonnes
 */
public abstract class ApplicationMatch_Abs<TypeId, TypeDto extends Dto_Itf<TypeId>, TypeEntity extends ApplicationAutoFieldsEntity_Itf<TypeId>, Columns_Enum extends ColumnsNames_Itf>
         extends Match_Abs<TypeId, TypeDto, TypeEntity, Columns_Enum>
{

   
   // Start of user code attributs

   // End of user code

   @Override
   public void create (final TypeDto p_dto) throws Spi4jValidationException
   {
      
      // Start of user code create

	  final TypeEntity v_entity = convertDtoToEntity(p_dto);
      v_entity.set_xtopsup(new XtopSup(DatabaseLineStatus_Enum.active));
      v_entity.set_xdmaj(new Date());
      getDao().create(v_entity);
      refreshDtoFromEntity(v_entity, p_dto);

      // End of user code
   }

   @Override
   public TypeDto findById (final TypeId p_id)
   {
      
      // Start of user code findById

      final TypeEntity v_entityFound = getDao().findById(p_id);
      // Si le xtopsup est < 3 on renvoie une exception comme si la donnée n'avait pas été trouvée.
      if (!DatabaseLineStatus_Enum.forSelect(v_entityFound.get_xtopsup().get_value()))
         throw new Spi4jRuntimeException("Donnée non trouvée (id=" + p_id + ')', "???");
     
      return convertEntityToDto(v_entityFound);

      // End of user code
   }

   @Override
   public List<TypeDto> findAll ()
   {
      
      // Start of user code findAll

	  final Map<String, String> v_xtopsupStatus = new HashMap<>();
      v_xtopsupStatus.put("xtopsup-param1", DatabaseLineStatus_Enum.active.get_value());
	  v_xtopsupStatus.put("xtopsup-param2", DatabaseLineStatus_Enum.deletedForNewReference.get_value());

      // On recherche toutes les lignes dont le xtopsup est < 3.
      final List<TypeEntity> v_tab_entity = getDao().findByCriteria("where " 
          + ApplicationAutoFieldsColumns_Enum.xtopsup.getPhysicalColumnName() + " = :xtopsup-param1 or " 
          + ApplicationAutoFieldsColumns_Enum.xtopsup.getPhysicalColumnName() + " = :xtopsup-param2",
               v_xtopsupStatus);

      return convertListEntityToListDto(v_tab_entity);

      // End of user code
   }

   @Override
   public List<TypeDto> findAll (final Columns_Enum p_orderByColumn)
   {
      
      // Start of user code findAllOrberByColumn
 	  
      final TableCriteria<ColumnsNames_Itf> v_tableCriteria = new TableCriteria<>("");

      // On recherche toutes les lignes dont le xtopsup est < 3.
      v_tableCriteria.addCriteria(ApplicationAutoFieldsColumns_Enum.xtopsup, 
			Operator_Enum.equals, DatabaseLineStatus_Enum.active.get_value());
      v_tableCriteria.addCriteria(OperatorLogical_Enum.or, ApplicationAutoFieldsColumns_Enum.xtopsup, 
			Operator_Enum.equals, DatabaseLineStatus_Enum.deletedForNewReference.get_value());
      v_tableCriteria.addOrderByAsc(p_orderByColumn);

      @SuppressWarnings({"rawtypes", "unchecked" })
      final List<TypeEntity> v_tab_entity = getDao().findByCriteria((TableCriteria) v_tableCriteria);
      return convertListEntityToListDto(v_tab_entity);

      // End of user code
   }

   @Override
   public void update (final TypeDto p_dto) throws Spi4jValidationException
   {
      
      // Start of user code update

	  // Si la ligne n'est pas active, on rejete la demande. 
	  final TypeEntity v_entityFound = getDao().findById(p_dto.getId());
	  if(null == v_entityFound || !DatabaseLineStatus_Enum.forUpdate(v_entityFound.get_xtopsup().get_value()))
			throw new Spi4jRuntimeException("Cette donnée a été supprimée par un autre utilisateur.", "???");

	  // On effectue la mise à jour.
      final TypeEntity v_entity = convertDtoToEntity(p_dto);
      v_entity.set_xdmaj(new Date());
      v_entity.set_xtopsup(new XtopSup(DatabaseLineStatus_Enum.active));
      getDao().update(v_entity);
      refreshDtoFromEntity(v_entity, p_dto);

      // End of user code
   }

   @Override
   public void delete (final TypeDto p_dto) throws Spi4jValidationException
   {
      
      // Start of user code delete

      final TypeEntity v_entity = convertDtoToEntity(p_dto);
      final TypeEntity v_entityFound = getDao().findById(v_entity.getId());

      // Si v_entityFound est null ou que l'entité à supprimer a déjà le xtopsup > 1 
      // alors on renvoie une exception indiquant que la donnée a déjà été supprimée.
	  if (null == v_entityFound || !DatabaseLineStatus_Enum.forDelete(v_entityFound.get_xtopsup().get_value()))
         throw new Spi4jRuntimeException("Cette donnée a été supprimée par un autre utilisateur.", "???");
     
 	  v_entity.set_xtopsup(new XtopSup(DatabaseLineStatus_Enum.deletedForAll));
      v_entity.set_xdmaj(new Date());
      getDao().update(v_entity);
      refreshDtoFromEntity(v_entity, p_dto);

      // End of user code
   }

   @Override
   public int deleteAll ()
   {
      
      // Start of user code deleteAll

      // Attention ici on veut probablement faire un vrai deleteAll() et non pas mettre à 1 la colonne xtopsup
      return super.deleteAll();

      // End of user code
   }

   @Override
   public int deleteByCriteria (final TableCriteria<Columns_Enum> p_criteria)
   {
      
      // Start of user code deleteByCriteria

      @SuppressWarnings("unchecked")
      final TableCriteria<ColumnsNames_Itf> v_tableCriteria = (TableCriteria<ColumnsNames_Itf>) p_criteria.clone();
      
      // Si entityFound est null ou que l'entité à supprimer a déjà le xtopsup > 1 
      // alors on renvoie une exception indiquant que la donnée a déjà été supprimée.
      v_tableCriteria.addCriteria(OperatorLogical_Enum.and, ApplicationAutoFieldsColumns_Enum.xtopsup, 
			Operator_Enum.equals, DatabaseLineStatus_Enum.active.get_value());
      v_tableCriteria.addCriteria(OperatorLogical_Enum.or, ApplicationAutoFieldsColumns_Enum.xtopsup, 
			Operator_Enum.equals, DatabaseLineStatus_Enum.deletedForNewReference.get_value());
      
      final String v_criteriaSql = v_tableCriteria.getCriteriaSql();
      final String v_tableName = getDao().getTableName();
      final String v_query = "update " + v_tableName + " set " + ApplicationAutoFieldsColumns_Enum.xtopsup + " = :xtopsupValue "
               + v_criteriaSql;
      final Map<String, Object> v_map_valueByName = new HashMap<>(v_tableCriteria.getMapValue());
      v_map_valueByName.put("xtopsupValue", DatabaseLineStatus_Enum.deletedForAll.get_value());

      return getDao().executeUpdate(v_query, v_map_valueByName);
     
      // End of user code
   }

   @Override
   public List<TypeDto> findByColumn (final Columns_Enum p_column, final Object p_value)
   {
      
      // Start of user code findByColumn

      final TableCriteria<ColumnsNames_Itf> v_tableCriteria = new TableCriteria<>("");

      // On recherche toutes les lignes dont le xtopsup est < 3.
      v_tableCriteria.addCriteria(p_column, Operator_Enum.equals, p_value);
      v_tableCriteria.addCriteria(OperatorLogical_Enum.and, ApplicationAutoFieldsColumns_Enum.xtopsup, 
			Operator_Enum.equals, DatabaseLineStatus_Enum.active.get_value());
      v_tableCriteria.addCriteria(OperatorLogical_Enum.or, ApplicationAutoFieldsColumns_Enum.xtopsup, 
			Operator_Enum.equals, DatabaseLineStatus_Enum.deletedForNewReference.get_value());
      
	  @SuppressWarnings({"rawtypes", "unchecked" })
      final List<TypeEntity> v_tab_entity = getDao().findByCriteria((TableCriteria) v_tableCriteria);
      return convertListEntityToListDto(v_tab_entity);
      
      // End of user code
   }

   @Override
   public List<TypeDto> findByCriteria (final String p_criteria,
            final Map<String, ? extends Object> p_map_valueName)
   {
      
      // Start of user code findByCriteriaSql

      // On doit ajouter le critère xtopsup < 3 devant un éventuel order by avant d'appeler findByCriteria
      final String v_sqlCriteria = addXtopsupCriteria(p_criteria); // critère sql modifié
      final Map<String, Object> v_map_valueByName;
      if (p_map_valueName == null)
      {
         v_map_valueByName = new HashMap<>();
      } else {
         v_map_valueByName = new HashMap<>(p_map_valueName);
      }
      
  	  // On recherche uniquement les lignes dont le xtopsup est < 3.
	  v_map_valueByName.put("xtopsup-param1", DatabaseLineStatus_Enum.active.get_value());
 	  v_map_valueByName.put("xtopsup-param2", DatabaseLineStatus_Enum.deletedForNewReference.get_value());

      final List<TypeEntity> v_tab_entity = getDao().findByCriteria(v_sqlCriteria, v_map_valueByName);
      return convertListEntityToListDto(v_tab_entity);
  
      // End of user code
   }

   @Override
   public List<TypeDto> findByCriteria (final String p_criteria,
            final Map<String, ? extends Object> p_map_valueName, final int p_nbLignesMax, final int p_nbLignesStart)
   {
      
      // Start of user code findByCriteriaSqlNbLignes

      // On doit ajouter le critère xtopsup < 3 devant un éventuel order by avant d'appeler findByCriteria
      final String v_sqlCriteria = addXtopsupCriteria(p_criteria); // critère sql modifié
      final Map<String, Object> v_map_valueByName;
      if (p_map_valueName == null)
      {
         v_map_valueByName = new HashMap<>();
      } else {
         v_map_valueByName = new HashMap<>(p_map_valueName);
      }

	  // On recherche uniquement les lignes dont le xtopsup est > 3.
	  v_map_valueByName.put("xtopsup-param1", DatabaseLineStatus_Enum.active.get_value());
 	  v_map_valueByName.put("xtopsup-param2", DatabaseLineStatus_Enum.deletedForNewReference.get_value());

      final List<TypeEntity> v_tab_entity = getDao().findByCriteria(v_sqlCriteria, v_map_valueByName, p_nbLignesMax,
               p_nbLignesStart);
      return convertListEntityToListDto(v_tab_entity);
     
      // End of user code
   }

   @Override
   public List<TypeDto> findByCriteria (final TableCriteria<Columns_Enum> p_criteria)
   {
      
      // Start of user code findByCriteria

      @SuppressWarnings("unchecked")
      final TableCriteria<ColumnsNames_Itf> v_tableCriteria = (TableCriteria<ColumnsNames_Itf>) p_criteria.clone();
      
	  // On recherche uniquement les lignes dont le xtopsup est < 3.
	  v_tableCriteria.addCriteria(ApplicationAutoFieldsColumns_Enum.xtopsup, 
			Operator_Enum.equals, DatabaseLineStatus_Enum.active.get_value());
      v_tableCriteria.addCriteria(OperatorLogical_Enum.or, ApplicationAutoFieldsColumns_Enum.xtopsup, 
			Operator_Enum.equals, DatabaseLineStatus_Enum.deletedForNewReference.get_value());
      
 	  @SuppressWarnings({"rawtypes", "unchecked" })
      final List<TypeEntity> v_tab_entity = getDao().findByCriteria((TableCriteria) v_tableCriteria);
      return convertListEntityToListDto(v_tab_entity);
   
      // End of user code
   }

   
   // Start of user code autres methodes

   /**
    * @param p_criteria
    *           le critère sql auquel il faut ajouter le xtopsup < 3
    * @return le critère auquel on a ajouté le xtopsup < 3
    */
   private String addXtopsupCriteria (final String p_criteria)
   {
      // On doit ajouter le critère xtopsup < 3 devant un éventuel order by avant d'appeler findByCriteria
      String v_xtopsupCriterium; // critère à ajouter
      final String v_tableName = getDao().getTableName();
      if (p_criteria.contains("where"))
      {
         v_xtopsupCriterium = " and " + v_tableName + '.' + ApplicationAutoFieldsColumns_Enum.xtopsup.getPhysicalColumnName() + " = :xtopsup-param1 or " 
         + v_tableName + '.' + ApplicationAutoFieldsColumns_Enum.xtopsup.getPhysicalColumnName() + " = :xtopsup-param2 ";
      }
      else
      {
         v_xtopsupCriterium = " where " + v_tableName + '.' + ApplicationAutoFieldsColumns_Enum.xtopsup.getPhysicalColumnName() + " = :xtopsup-param1 and "
         + v_tableName + '.' + ApplicationAutoFieldsColumns_Enum.xtopsup.getPhysicalColumnName() + " = :xtopsup-param2 ";
      }
      String v_strCriteria; // critère sql modifié
      // On sépare le critère sql selon la chaine "order by" pour avoir dans v_tab_sqlCriteria[0] la partie avant le order by et
      // dans un éventuel v_tab_sqlCriteria[1] la partie après le order by s'il existe
      final String[] v_tab_sqlCriteria = p_criteria.split("order by");
      // On ajoute ici le critère xtopsup avant l'éventuel order by
      v_strCriteria = v_tab_sqlCriteria[0] + v_xtopsupCriterium;
      // si v_tab_sqlCriteria.length > 1 c'est qu'il y avait un order by dans le critère initial : alors on le rajoute à la fin de v_sqlCriteria
      if (v_tab_sqlCriteria.length > 1)
      {
         v_strCriteria += " order by " + v_tab_sqlCriteria[1];
      }
      return v_strCriteria;
   }

   // End of user code
}
