/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.persistence.api.annuaire;

import fr.spi4j.persistence.entity.ColumnsNames_Itf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * L'énumeration définissant les informations de chaque colonne pour le type Competence.
 * @author safr@n
 */
public enum CompetenceColumns_Enum implements ColumnsNames_Itf
{
   /** competence_id. */
   competence_id("_competence_id", "COMPETENCE_ID", Long.class, true, -1, true),
   /** libelle. */
   libelle("libelle", "LIBELLE", String.class, true, -1, false),
   /** typecompetence. */
   typecompetence("typecompetence", "TYPECOMPETENCE", String.class, true, -1, false);

   /**
    * Le nom physique de la table.
    */
   public static final String c_tableName = "AW_COMPETENCE";

   /**
    * Le nom physique de la table de jointure avec Personne.
    */
   public static final String c_tableNameCompetenceDispose = "AW_COMPETENCEDISPOSE";

   /** Le nom logique de la colonne. */
   private final String _logicalColumnName;

   /** Le nom physique de la colonne. */
   private final String _physicalColumnName;

   /** Le type associe a la colonne. */
   private final Class<?> _typeColumn;

   /** Est-ce que la saisie de la valeur est obligatoire pour cette colonne ? */
   private final boolean _mandatory;

   /** La taille de la colonne. */
   private final int _size;

   /** Est-ce que la colonne est la cle primaire ? */
   private final boolean _id;

   /**
    * Constructeur permettant de spécifier le type de la colonne.
    * @param p_logicalColumnName
    *           (In)(*) Le nom logique de la colonne.
    * @param p_physicalColumnName
    *           (In)(*) Le nom physique de la colonne.
    * @param p_ClassType
    *           (In)(*) Le type de la colonne.
    * @param p_mandatory
    *           (In)(*) Est-ce que la saisie de la valeur est obligatoire pour cette colonne?
    * @param p_size
    *           (In)(*) La taille de la colonne
    * @param p_id
    *           (In)(*) Est-ce que la colonne est la clé primaire?
    */
   private CompetenceColumns_Enum (final String p_logicalColumnName, final String p_physicalColumnName,
            final Class<?> p_ClassType, final boolean p_mandatory, final int p_size, final boolean p_id)
   {
      _logicalColumnName = p_logicalColumnName;
      _physicalColumnName = p_physicalColumnName;
      _typeColumn = p_ClassType;
      _mandatory = p_mandatory;
      _size = p_size;
      _id = p_id;
   }

   @Override
   public String getLogicalColumnName ()
   {
      return _logicalColumnName;
   }

   @Override
   public String getPhysicalColumnName ()
   {
      return _physicalColumnName;
   }

   @Override
   public boolean isMandatory ()
   {
      return _mandatory;
   }

   @Override
   public int getSize ()
   {
      return _size;
   }

   @Override
   public boolean isId ()
   {
      return _id;
   }

   @Override
   public Class<?> getTypeColumn ()
   {
      return _typeColumn;
   }

   @Override
   public String toString ()
   {
      return _physicalColumnName;
   }

   @Override
   public String getTableName ()
   {
      return c_tableName;
   }

   @Override
   public String getCompletePhysicalName ()
   {
      return getTableName() + '.' + getPhysicalColumnName ();
   }
}
