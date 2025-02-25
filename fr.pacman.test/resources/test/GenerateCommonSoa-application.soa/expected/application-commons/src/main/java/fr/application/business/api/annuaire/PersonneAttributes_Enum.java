/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.business.api.annuaire;
// Start of user code for imports

import fr.application.business.api.annuaire.PersonneDto;
import fr.application.business.api.ref.GradeDto;
import fr.spi4j.business.dto.AttributesNames_Itf;
import fr.spi4j.business.dto.DtoAttributeHelper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

// End of user code

/**
 * L'énumération définissant les informations de chaque attribut pour le type 'Personne'.
 * @author safr@n
 */
public enum PersonneAttributes_Enum implements AttributesNames_Itf
{
   /** id. */
   id("id", "id", Long.class, true, -1),
   /** nom. */
   nom("nom", "", String.class, true, 30),
   /** prenom. */
   prenom("prenom", "", String.class, true, -1),
   /** civil. */
   civil("civil", "", Boolean.class, true, -1),
   /** dateNaissance. */
   dateNaissance("dateNaissance", "", Date.class, true, -1),
   /** salaire. */
   salaire("salaire", "", Double.class, true, -1),
   /** tab_adresses. */
   tab_adresses("tab_adresses", "", List.class, false, -1),
   /** grade_id. */
   grade_id("grade_id", "", Long.class, false, -1),
   /** grade. */
   grade("grade", "", GradeDto.class, false, -1),
   /** marieAvec_id. */
   marieAvec_id("marieAvec_id", "", Long.class, false, -1),
   /** marieAvec. */
   marieAvec("marieAvec", "", PersonneDto.class, false, -1),
   /** tab_parentDe. */
   tab_parentDe("tab_parentDe", "", List.class, false, -1),
   /** personneParentDe_id. */
   personneParentDe_id("personneParentDe_id", "", Long.class, true, -1),
   /** tab_pays. */
   tab_pays("tab_pays", "", List.class, false, -1);

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
   private PersonneAttributes_Enum (final String p_name, final String p_description, final Class<?> p_ClassType, final boolean p_mandatory, final int p_size)
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
         _getterMethod = DtoAttributeHelper.getInstance().getGetterMethodForAttribute(PersonneDto.class, getName());
      }
      return _getterMethod;
   }

   @Override
   public Method getSetterMethod ()
   {
      if (_setterMethod == null)
      {
         _setterMethod = DtoAttributeHelper.getInstance().getSetterMethodForAttribute(PersonneDto.class, getName(), getType());
      }
      return _setterMethod;
   }

   @Override
   public String toString ()
   {
      return _description;
   }
}
