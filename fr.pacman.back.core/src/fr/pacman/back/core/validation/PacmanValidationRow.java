package fr.pacman.back.core.validation;

import org.eclipse.emf.ecore.EObject;

/**
 * Représente une ligne de validation dans le rapport.
 * 
 * Chaque ligne contient :
 * <ul>
 * <li>La règle métier concernée</li>
 * <li>Le nom de l'objet EMF concerné</li>
 * <li>L'erreur détectée</li>
 * <li>Une solution ou recommandation</li>
 * <li>Une référence directe à l'élément {@link EObject} concerné</li>
 * </ul>
 *
 * Cette classe est utilisée pour alimenter la vue
 * {@code PacmanUIValidationView}.
 *
 * @author MINARM
 */
public class PacmanValidationRow {

	/** Libellé de la règle métier violée. */
	public String _rule;

	/** Nom de l’objet métier ou technique concerné. */
	public String _objectName;

	/** Message d'erreur décrivant le problème identifié. */
	public String _error;

	/** Recommandation ou solution potentielle pour corriger l’erreur. */
	public String _solution;

	/** Référence EMF vers l'objet concerné par cette ligne. */
	public org.eclipse.emf.ecore.EObject _emfElement;

	/**
	 * Construit une nouvelle ligne de rapport de validation.
	 *
	 * @param p_rule       la règle métier concernée
	 * @param p_objectName le nom de l’objet concerné
	 * @param p_error      le message d’erreur ou de problème
	 * @param p_solution   la solution proposée ou la recommandation
	 * @param p_emfElement l’élément EMF lié à cette ligne
	 */
	public PacmanValidationRow(String p_rule, String p_objectName, String p_error, String p_solution, EObject p_emfElement) {
		this._rule = p_rule;
		this._objectName = p_objectName;
		this._error = p_error;
		this._solution = p_solution;
		this._emfElement = p_emfElement;
	}
}
