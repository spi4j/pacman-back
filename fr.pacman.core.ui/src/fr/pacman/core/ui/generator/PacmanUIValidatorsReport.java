package fr.pacman.core.ui.generator;

import java.util.Map.Entry;

import org.eclipse.acceleo.aql.AcceleoUtil;
import org.eclipse.acceleo.query.ast.ASTNode;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;

import fr.pacman.core.generator.PacmanValidatorsReport;
import fr.pacman.core.ui.plugin.Activator;

/**
 * La classe {@code PacmanUIValidatorsReport} est responsable de l'interaction
 * avec le rapport de validation en termes d'interface utilisateur (UI). Elle
 * permet de réinitialiser le rapport et d'enregistrer ou de consigner les
 * erreurs de validation dans les logs. Ellke se contente "d'encapsuler" et
 * d'appeler la classe statique {@Link PacmanValidatorsReport} afin de ne pas
 * créer de dépendance circulaire entre les plugins (core <> core.ui).
 * <p>
 * Cette classe agit comme une couche de présentation en récupérant les erreurs
 * enregistrées dans {@link PacmanValidatorsReport} et en les affichant ou en
 * les loggant à l'aide de la journalisation système d'eclipse (ErrorLog).
 * 
 * @author MINARM
 */
class PacmanUIValidatorsReport {

	/**
	 * Logge les erreurs de validation dans la console et les enregistre dans le
	 * journal du système. Chaque erreur de validation est ajoutée au journal sous
	 * forme de une entrée de type {@link IStatus#ERROR}.
	 * <p>
	 * Si l'objet associé à l'erreur est une instance de {@link ASTNode}, la méthode
	 * {@link AcceleoUtil#getLocation(ASTNode)} est utilisée pour obtenir la
	 * localisation de l'erreur.
	 */
	static void log() {

//		for (Entry<EObject, String> entry : PacmanValidatorsReport.getReport().entrySet()) {
//			System.out.println(">>>>" + entry.getValue());
//			String location = null;
//			EObject o = entry.getKey();
//			if (o instanceof ASTNode) {
//				location = AcceleoUtil.getLocation((ASTNode) o);
//			}
//			Activator.getDefault().getLog()
//					.log(new Status(IStatus.ERROR, "id", location + " : " + entry.getValue(), null));
//		}
	}
}
