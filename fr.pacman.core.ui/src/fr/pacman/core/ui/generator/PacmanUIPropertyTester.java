package fr.pacman.core.ui.generator;

import java.io.File;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IResource;

import fr.pacman.core.property.PropertiesHandler;

/**
 * Classe abstraite de base pour la définition de {@link PropertyTester} Eclipse
 * dans le contexte du plugin Pacman.
 * <p>
 * Cette implémentation fournit une logique commune permettant :
 * <ul>
 * <li>de récupérer le chemin absolu du projet modèle associé à une ressource
 * sélectionnée,</li>
 * <li>d'initialiser le {@link PropertiesHandler} une seule fois par chemin de
 * modèle,</li>
 * <li>de déléguer l'évaluation de la propriété spécifique à une sous-classe via
 * la méthode abstraite
 * {@link #testProperty(Object, String, Object[], Object)}.</li>
 * </ul>
 *
 * <p>
 * Le cycle est le suivant :
 * <ol>
 * <li>Lorsqu'une ressource {@link IResource} est reçue en paramètre, son chemin
 * absolu est résolu.</li>
 * <li>Si le chemin du modèle a changé depuis le dernier appel, le
 * {@link PropertiesHandler} est réinitialisé avec ce nouveau chemin.</li>
 * <li>La méthode {@code testProperty(...)} est appelée pour exécuter la logique
 * métier spécifique du test.</li>
 * </ol>
 *
 * <p>
 * <b>Notes :</b>
 * <ul>
 * <li>Le champ {@code _initialized} garantit que l'initialisation du
 * {@link PropertiesHandler} n'a lieu qu'une seule fois par chemin de
 * modèle.</li>
 * <li>Les sous-classes doivent uniquement se concentrer sur la logique métier
 * propre à la propriété testée.</li>
 * </ul>
 *
 * @author MINARM
 */
public abstract class PacmanUIPropertyTester extends PropertyTester {

	/**
	 * Indique si le PropertiesHandler a déjà été initialisé pour le modèle courant.
	 */
	static boolean _initialialized = false;

	/**
	 * Chemin du modèle actuellement utilisé pour l'initialisation.
	 */
	static String _modelPath;

	/**
	 * Méthode à implémenter par les sous-classes pour tester une propriété
	 * spécifique.
	 *
	 * @param p_receiver      l'objet cible du test (souvent un {@link IResource})
	 * @param p_property      le nom de la propriété à évaluer
	 * @param p_args          arguments supplémentaires éventuels
	 * @param p_expectedValue valeur attendue, éventuellement {@code null}
	 * @return {@code true} si la propriété est validée, {@code false} sinon
	 */
	public abstract boolean testProperty(Object p_receiver, String p_property, Object[] p_args, Object p_expectedValue);

	/**
	 * Implémentation générique du test de propriété.
	 * <p>
	 * Si le receiver est une ressource Eclipse ({@link IResource}), le chemin du
	 * projet modèle est récupéré et utilisé pour initialiser le
	 * {@link PropertiesHandler}. Ensuite, la logique spécifique est déléguée à
	 * {@link #testProperty(Object, String, Object[], Object)}.
	 *
	 * @param p_receiver      objet cible (souvent un {@link IResource})
	 * @param p_property      propriété à tester
	 * @param p_args          éventuels arguments supplémentaires
	 * @param p_expectedValue valeur attendue
	 * @return résultat du test
	 */
	@Override
	public boolean test(Object p_receiver, String p_property, Object[] p_args, Object p_expectedValue) {
		if (p_receiver instanceof IResource resource) {
			File modelProject = new File(resource.getLocation().toOSString());
			String modelPath = modelProject.getParentFile().getPath();
			if (!_initialialized || !modelPath.equals(_modelPath)) {
				PropertiesHandler.init(modelPath);
				_modelPath = modelPath;
				_initialialized = true;
			}
		}
		return testProperty(p_receiver, p_property, p_args, p_expectedValue);
	}
}
