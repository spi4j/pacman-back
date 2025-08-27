package fr.pacman.core.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * La classe {@code PacmanValidatorsReport} centralise la gestion des erreurs de
 * validation rencontrées lors de l'analyse ou du traitement des modèles dans
 * l'outil Pacman.
 * 
 * Elle fournit un rapport global (statique) permettant :
 * <ul>
 * <li>d'ajouter des erreurs de validation associées à un objet métier
 * ({@link EObject}),</li>
 * <li>de réinitialiser complètement le rapport,</li>
 * <li>de vérifier la présence d'erreurs,</li>
 * <li>et de récupérer l'ensemble des erreurs enregistrées sous forme de
 * {@link PacmanValidationRow}.</li>
 * </ul>
 * 
 * @author MINARM
 */
public class PacmanValidatorsReport {

	/**
	 * Le rapport des erreurs de validation dans le cas ou on veut afficher les
	 * erreurs dans une vue.
	 */
	private static List<PacmanValidationRow> _report = new ArrayList<>();

	/**
	 * Ajoute une erreur de validation au rapport.
	 * 
	 * @param p_object L'objet pour lequel l'erreur de validation est signalée.
	 * @param p_msg    Le message décrivant l'erreur de validation.
	 */
	public static void add(final EObject p_object, final String p_msg) {
		String[] errCols = p_msg.split("@");
		_report.add(new PacmanValidationRow(errCols[0], errCols[1], errCols[2], errCols[3], p_object));
	}

	/**
	 * Réinitialise le rapport d'erreurs en le vidant. Cette méthode efface toutes
	 * les erreurs enregistrées dans le rapport.
	 */
	public static void reset() {
		_report.clear();
	}

	/**
	 * Indique si un rapport est présent.Cette méthode vérifie si la collection ou
	 * structure de données interne {@code _report} contient au moins un élément.
	 *
	 * @return {@code true} si le rapport n'est pas vide, {@code false} sinon
	 */
	public static boolean hasReport() {
		return !_report.isEmpty();
	}

	/**
	 * Obtient le rapport complet des erreurs de validation pour le fichier.
	 * 
	 * @return Une simple {@link List} contenant les messages d'erreur.
	 */
	public static List<PacmanValidationRow> get() {
		return _report;
	}
}
