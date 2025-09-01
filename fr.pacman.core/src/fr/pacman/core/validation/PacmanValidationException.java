package fr.pacman.core.validation;

/**
 * Exception spécifique au plugin Pacman, déclenchée lorsqu'une validation
 * échoue.
 * <p>
 * Cette exception hérite de {@link RuntimeException} afin de pouvoir être
 * propagée sans être déclarée explicitement dans la signature des méthodes.
 * Elle est généralement utilisée pour signaler une erreur de validation métier
 * ou technique détectée lors de l'exécution d'un générateur ou d'un validateur.
 * 
 * @author MINARM
 */
public class PacmanValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 991581068891927356L;

	/**
	 * Crée une nouvelle exception de validation avec le message d'erreur fourni.
	 *
	 * @param p_msg le détail de l'erreur de validation
	 */
	public PacmanValidationException(final String p_msg) {
		super(p_msg);
	}

	/**
	 * Retourne uniquement le message d'erreur sans le nom de la classe d'exception.
	 * Cela permet d'afficher un message plus clair dans les boîtes de dialogue ou
	 * les journaux destinés à l'utilisateur.
	 *
	 * @return le message d'erreur de validation
	 */
	@Override
	public String toString() {
		return getLocalizedMessage();
	}

}
