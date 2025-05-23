package fr.application.ws.impl_server.annuaire;
// Start of user code for imports

import fr.application.business.ApplicationUserBusiness;
import fr.application.mapper.ApplicationUserMapper;
import fr.application.ws.api.annuaire.GradesServiceRSFacade_Itf;
import fr.application.ws.api.ref.GradeXto;
import fr.spi4j.ws.rs.exception.RsNoResultException;
import fr.spi4j.ws.rs.exception.Spi4jValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jvnet.hk2.annotations.Service;

// End of user code

/**
 * Implémentation de la façade du service REST pour le service : grades.
 * @author safr@n
 */
@Service
public class GradesServiceRSFacade implements GradesServiceRSFacade_Itf
{
	// attributs
	// Start of user code attributs

    // End of user code

	/**
	* Constructeur.
	*/
	public GradesServiceRSFacade(){
		super();
	}


    /**
    * 
    * @return grades. 
	*/
   @Override
   public List<GradeXto> findAllGrades ()
   {
		// for findAllGrades_Grade_grades
		// Start of user code for findAllGrades_Grade_grades
		
		
		List<GradeXto> _result = ApplicationUserMapper.getGradeMapper ()
			.convertListDtoToListXto( ApplicationUserBusiness.getGradesService ()
			.findAllGrades ());
		
		if(_result.isEmpty()){
			throw new RsNoResultException();
		}
		return _result;
		// End of user code
   }

   
    /**
    * 
	* @param p_offset :
	*			(In)(*) Numéro de ligne pour débuter la requête dans le cadre de la pagination.
	* @param p_limit :
	*			(In)(*) Nombre d'éléments demandés par page dans le cadre de la pagination.
    * @return grades. 
	*/
   @Override
   public List<GradeXto> findAllPagedGrades (final int p_offset, final int p_limit)
   {
		// for findAllPagedGrades_Grade_grades
		// Start of user code for findAllPagedGrades_Grade_grades
		
		
		List<GradeXto> _result = ApplicationUserMapper.getGradeMapper ()
			.convertListDtoToListXto( ApplicationUserBusiness.getGradesService ()
			.findAllPagedGrades (p_offset, p_limit));
		
		if(_result.isEmpty()){
			throw new RsNoResultException();
		}
		return _result;
		// End of user code
   }

   /**
   * Méthode automatiquement générée pour la pagination de l'opération : findAllPagedGrades
   * @return Le nombre total d'éléments pour l'opération.
   */
   @Override
   public int findAllPagedGradesTotalCount()
   {
   	
   	// Start of user code findAllPagedGrades
   
   	return ApplicationUserBusiness.getGradesService ().findAllPagedGradesTotalCount();
   
   	// End of user code
   }
   
}
