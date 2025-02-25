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
 * L'énumération définissant les informations de chaque attribut pour le type 'Adresse'.
 * @author safr@n
 */
public enum AdresseAttributes_Enum implements AttributesNames_Itf
{
   /** id. */
   id("id", "id", Long.class, true, -1),
   /** rue. */
   rue("rue", "", String.class, true, -1),
   /** ville. */
   ville("ville", "", String.class, true, -1),
   /** codePostal. */
   codePostal("codePostal", "", String.class, true, 5),
   /** personneAdresses_id. */
   personneAdresses_id("personneAdresses_id", "", Long.class, false, -1);

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
   private AdresseAttributes_Enum (final String p_name, final String p_description, final Class<?> p_ClassType, final boolean p_mandatory, final int p_size)
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
         _getterMethod = DtoAttributeHelper.getInstance().getGetterMethodForAttribute(AdresseDto.class, getName());
      }
      return _getterMethod;
   }

   @Override
   public Method getSetterMethod ()
   {
      if (_setterMethod == null)
      {
         _setterMethod = DtoAttributeHelper.getInstance().getSetterMethodForAttribute(AdresseDto.class, getName(), getType());
      }
      return _setterMethod;
   }

   @Override
   public String toString ()
   {
      return _description;
   }
}
