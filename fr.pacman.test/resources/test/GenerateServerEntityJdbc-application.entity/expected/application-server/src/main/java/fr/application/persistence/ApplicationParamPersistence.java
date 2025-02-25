/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.persistence; // NOPMD
// Start of user code for imports

import java.io.File;

import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.spi4j.Parameters;
import fr.spi4j.exception.Spi4jRuntimeException;
import fr.spi4j.persistence.ElemResourceManager;
import fr.spi4j.persistence.ParamPersistence_Abs;
import fr.spi4j.persistence.dao.jdbc.DefaultJdbcDao;
import fr.spi4j.persistence.resource.DefaultResourcePhysical;
import fr.spi4j.persistence.resource.ResourcePhysical_Abs;
import fr.spi4j.persistence.resource.ResourceType_Enum;
import fr.spi4j.persistence.resource.jdbc.NonXAJndiResourcePhysical;
import fr.application.ApplicationParamAppli;

// End of user code

/**
 * Implémentation permettant de centraliser le paramétrage de persistance de l'application.
 * @author safr@n
 */
public final class ApplicationParamPersistence extends ParamPersistence_Abs implements ApplicationParamAppli
{
   private static final Logger c_log = LogManager.getLogger(ApplicationParamPersistence.class);

   
   // Start of user code Resources Physiques

   /** Chemin de la base H2 (test ou base embarquee). */
   private static final String c_cheminBaseH2 = "./h2/Application";

   // End of user code

   /** Propriété positionnée à "true" (par maven-surefire-plugin) lorsque l'application est lancée en mode h2. */
   private boolean _h2Mode = Boolean.FALSE;

   /**
    * Le gestionnaire de connexions comprenant des ResourcePhysique et renvoyant le ResourceManager correspondant au mode de fonctionnement de l'appli.
    */
   private final ElemResourceManager _elemResourceManager;

   /**
    * Constructeur.
    */
   public ApplicationParamPersistence ()
   {
      super(c_idAppli);

      
      // Start of user code Constructeur

	  // Positionne l'indicateur à "true" si l'application est lancée en mode h2.
      _h2Mode = checkIfH2Mode();

      // La ressource physique SGBD.
      _elemResourceManager = new ElemResourceManager(c_idAppli, createResourcePhysical());


      // End of user code
   }

	/**
	 * Initialise l'indicateur de mode de lancement pour l'application. Soit on part
	 * sur une base H2 dans le cadre des tests unitaires ou d'une base embarquée,
	 * soit on se connecte sur une base externe. Dans le cas d'une base embarquée, 
	 * on force directement l'indicateur et on se contente de le retourner. 
	 */
   private boolean checkIfH2Mode() {
	
      boolean v_h2Mode;
      try {

         final String v_withH2Bool = Parameters.getParameter(Parameters.c_h2, Boolean.toString(Boolean.FALSE));
         v_h2Mode = v_withH2Bool.equalsIgnoreCase(Boolean.toString(Boolean.TRUE));

	  } catch (final IllegalStateException v_e) {

         v_h2Mode = Boolean.FALSE;
      }
	  return v_h2Mode;
   }

   /**
    * Initialiser les instances du paramétrage de la couche persistance.
    */
   private static synchronized void initInstance ()
   {
      ApplicationParamPersistence v_ParamPersistence = (ApplicationParamPersistence) getParamPersistence(c_idAppli);
      // Si pas d'instance
      if (v_ParamPersistence == null)
      {
         // Instancier 'ParamPersistenceApp'
         v_ParamPersistence = new ApplicationParamPersistence();
         // Mémoriser l'instance 'ParamPersistence'
         setParamPersistence(c_idAppli, v_ParamPersistence);

         // Instancier un 'ApplicationUserPersistence'
         final ApplicationUserPersistence v_UserPersistence = new ApplicationUserPersistence(v_ParamPersistence);
         // Mémoriser l'instance 'UserPersistence'
         setUserPersistence(c_idAppli, v_UserPersistence);

         // Initialiser les éléments du paramétrage
         v_ParamPersistence.initElemParamPersistence();
      }
   }

   /**
    * Permet d'obtenir le 'UserPersistence' de l'application.
    * @return Une instance de 'ApplicationUserPersistence'
    */
   public static ApplicationUserPersistence getUserPersistence ()
   {
      return UserPersistenceStaticHolder.c_userPersistence;
   }

   /**
    * Design pattern "Static Holder": Classe pour initialiser au besoin (c'est-à-dire à la première demande)<br>
    * le userPersistence de l'application sans nécessiter d'ajouter "synchronized" sur la méthode static getUserPersistence().<br>
    * Ajouter "synchronized" pourrait devenir une contention car la méthode est static et est appelée très souvent dans l'application.<br>
    * Le Static Holder permet d'initialiser l'attribut en étant automatiquement synchronisé par l'initialisation de la classe dans le ClassLoader.<br>
    */
   private static final class UserPersistenceStaticHolder
   {
      /** Le 'UserPersistence' de l'application. */
      private static final ApplicationUserPersistence c_userPersistence;

      static
      {
         // Initialiser la couche de persistance
         initInstance();
         // Obtenir le 'UserPersistence' de l'application
         c_userPersistence = (ApplicationUserPersistence) getUserPersistence(c_idAppli);
         // finalise l'initialisation du ParamPersistence
         ((ApplicationParamPersistence) getParamPersistence(c_idAppli)).afterInit();
      }

      /**
       * Constructeur.
       */
      private UserPersistenceStaticHolder ()
      {
         super();
      }
   }

   @Override
   protected void afterInit ()
   {
      
      // Start of user code afterInit

	  // Uniquement si application en mode h2.
	  if(_h2Mode) {

      	// suppression de l'ancienne base si elle existe encore
      	final File v_dbFile = new File(c_cheminBaseH2 + ".mv.db").getAbsoluteFile();
      	if (v_dbFile.exists())
      	{
        	c_log.info("Suppression de la base existante : " + v_dbFile.getName());
         	if (!v_dbFile.delete())
         	{
            	c_log.warn("La base existante n'a pas pu être supprimée : " + v_dbFile.getName());
         	}
      	}
      	try
      	{
			c_log.info("Création de la base (version usine) : " + v_dbFile.getName());
         	ApplicationH2DatabaseHelper.initializeDatabase();
      	}
      	catch (final Throwable v_t)
      	{
        	c_log.error("Impossible d'initialiser la base de données, "
                  + "vérifier les scripts d'initialisation de la base de données", v_t);
      	}
	  }
      // End of user code
   }

   /**
    * Paramétrage de la persistance.
    */
   // CHECKSTYLE:OFF
   @Override
   // CHECKSTYLE:ON
   protected void initElemParamPersistence () // NOPMD
   {
      // Ajout de différentes ressources au ElemResourceManager
      
      // Start of user code Ajout Resources
      // _elemResourceManager.addResourcePhysical(c_LocalResourcePhysical, fr.spi4j.persistence.Mode_Enum.nomade);
      // End of user code

      
      // Start of user code Default Dao
      // Ajouter le paramétrage pour le DAO par défaut
      setDefaultDao(DefaultJdbcDao.class, _elemResourceManager);
      // End of user code

      
      // Start of user code fr.application.persistence.api.ref.GradeEntity_Itf
      // Ajouter le paramétrage pour l'entité "fr.application.persistence.api.ref.GradeEntity_Itf"
      addElemParamPersistence(fr.application.persistence.api.ref.GradeEntity_Itf.class, new ElemParamPersistence(
               fr.application.persistence.impl_jdbc.ref.GradeEntity.class,
               fr.application.persistence.impl_jdbc.ref.GradeDao.class, _elemResourceManager));
      // End of user code
      
      // Start of user code fr.application.persistence.api.annuaire.CompetenceEntity_Itf
      // Ajouter le paramétrage pour l'entité "fr.application.persistence.api.annuaire.CompetenceEntity_Itf"
      addElemParamPersistence(fr.application.persistence.api.annuaire.CompetenceEntity_Itf.class, new ElemParamPersistence(
               fr.application.persistence.impl_jdbc.annuaire.CompetenceEntity.class,
               fr.application.persistence.impl_jdbc.annuaire.CompetenceDao.class, _elemResourceManager));
      // End of user code
      
      // Start of user code fr.application.persistence.api.annuaire.PersonneEntity_Itf
      // Ajouter le paramétrage pour l'entité "fr.application.persistence.api.annuaire.PersonneEntity_Itf"
      addElemParamPersistence(fr.application.persistence.api.annuaire.PersonneEntity_Itf.class, new ElemParamPersistence(
               fr.application.persistence.impl_jdbc.annuaire.PersonneEntity.class,
               fr.application.persistence.impl_jdbc.annuaire.PersonneDao.class, _elemResourceManager));
      // End of user code
      
      // Start of user code fr.application.persistence.api.annuaire.AdresseEntity_Itf
      // Ajouter le paramétrage pour l'entité "fr.application.persistence.api.annuaire.AdresseEntity_Itf"
      addElemParamPersistence(fr.application.persistence.api.annuaire.AdresseEntity_Itf.class, new ElemParamPersistence(
               fr.application.persistence.impl_jdbc.annuaire.AdresseEntity.class,
               fr.application.persistence.impl_jdbc.annuaire.AdresseDao.class, _elemResourceManager));
      // End of user code
      
      // Start of user code fr.application.persistence.api.annuaire.PaysEntity_Itf
      // Ajouter le paramétrage pour l'entité "fr.application.persistence.api.annuaire.PaysEntity_Itf"
      addElemParamPersistence(fr.application.persistence.api.annuaire.PaysEntity_Itf.class, new ElemParamPersistence(
               fr.application.persistence.impl_jdbc.annuaire.PaysEntity.class,
               fr.application.persistence.impl_jdbc.annuaire.PaysDao.class, _elemResourceManager));
      // End of user code
   }

   /**
    * Création de la ressource physique.
    * @return ResourcePhysical_Abs
    */
   private ResourcePhysical_Abs createResourcePhysical ()
   {

	  
	  // Start of user code Creation resource physique

	  final ResourcePhysical_Abs v_resourcePhysical;
     
      // On regarde si on démarre en mode base h2 embarque ou sur une base externe.
	  if(_h2Mode){

			// Chargement de la base H2.
			c_log.info("Démarrage du serveur/application sur base H2.");
			v_resourcePhysical = new DefaultResourcePhysical("jdbc:h2:" + c_cheminBaseH2, "", "",
                  ResourceType_Enum.ressourceH2NonXA);
	  } else {
			// Récupération du nom jndi pour la ressource.
      		final String v_jndiName = Parameters.getParameter("application.datasource", 
                  "java:comp/env/application/datasource");
			try {

       	 		// Chargement de la ressource via son nom jndi.
         		c_log.info("Démarrage du serveur en utilisant la datasource : " + v_jndiName);
				v_resourcePhysical = new NonXAJndiResourcePhysical(v_jndiName);

			} catch(NamingException v_e) {
        		
				 throw new Spi4jRuntimeException(v_e, "Nom JNDI inconnu : " +  v_jndiName,
                  "Vérifier le nom JNDI de la DataSource");
			}
	  }
      return v_resourcePhysical;
	  // End of user code
   }


   
   // Start of user code Methodes

   // End of user code
}
