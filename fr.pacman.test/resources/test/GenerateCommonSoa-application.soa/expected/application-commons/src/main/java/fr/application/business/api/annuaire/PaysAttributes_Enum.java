/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.business.api.annuaire;
// Start of user code for imports

import fr.spi4j.business.dto.AttributesNames_Itf;
import fr.spi4j.business.dto.DtoAttributeHelper;
import java.lang.reflect.Method;

// End of user code

/**
 * L'énumération définissant les informations de chaque attribut pour le type 'Pays'.
 * @author safr@n
 */
public enum PaysAttributes_Enum implements AttributesNames_Itf
{
   /** id. */
   id("id", "id", Long.class, true, -1),
   /** nom. */
   nom("nom", "", String.class, true, -1),
   /** capitale. */
   capitale("capitale", "", String.class, true, -1),
   /** personnePays_id. */
   personnePays_id("personnePays_id", "", Long.class, false, -1);

   /** Le nom de l'attribut. */
   private final String _name;

   /** La description de l'attribut. */
   private final String _description;

   /** Le type associé à l'attribut. */
   private final Class<?> _type;

   /** Est-ce que la saisie de la valeur est obligatoire pour cet attribut ? */
   private final boolean _mandatory;

   /** La taille de l'attribut. */
   private final int _size;

   /** La méthode du getter. */
   private transient Method _getterMethod;

   /** La méthode du setter. */
   private transient Method _setterMethod;

   /**
    * Constructeur.
    * @param p_name
    *           (In)(*) Le nom de l'attribut.
    * @param p_description
    *           (In)(*) La description de l'attribut.
    * @param p_ClassType
    *           (In)(*) Le type de l'attribut.
    * @param p_mandatory
    *           (In)(*) Est-ce que la saisie de la valeur est obligatoire pour cette colonne?
    * @param p_size
    *           (In)(*) La taille de la colonne
    */
   private PaysAttributes_Enum (final String p_name, final String p_description, final Class<?> p_ClassType, final boolean p_mandatory, final int p_size)
   {
      _name = p_name;
      _description = p_description;
      _type = p_ClassType;
      _mandatory = p_mandatory;
      _size = p_size;
   }


   @Override
   public String getName ()
   {
      return _name;
   }

   @Override
   public String getDescription ()
   {
      return _description;
   }

   @Override
   public Class<?> getType ()
   {
      return _type;
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
   public Method getGetterMethod ()
   {
      if (_getterMethod == null)
      {
         _getterMethod = DtoAttributeHelper.getInstance().getGetterMethodForAttribute(PaysDto.class, getName());
      }
      return _getterMethod;
   }

   @Override
   public Method getSetterMethod ()
   {
      if (_setterMethod == null)
      {
         _setterMethod = DtoAttributeHelper.getInstance().getSetterMethodForAttribute(PaysDto.class, getName(), getType());
      }
      return _setterMethod;
   }

   @Override
   public String toString ()
   {
      return _description;
   }
}
