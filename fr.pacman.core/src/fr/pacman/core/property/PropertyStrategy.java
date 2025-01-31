package fr.pacman.core.property;

import java.util.Map;

import fr.pacman.core.enumeration.PropertyTrigger;

/**
 * Une stratégie peut éventuellement être associée à un élément et permet de
 * déclancher des actions à réception de cet élément. Elle est lancée selon une
 * fréquence d'activation (trigger), voir la classe {@link PropertyTrigger}.
 * 
 * @author MINARM
 */
public abstract class PropertyStrategy {
	/**
	 * La propriété de référence (qui a déclanché la stratégie).
	 */
	private PacmanProperty _refPacmanProperty;

	/**
	 * Ancienne valeur de la propriété de référence.
	 */
	private String _refOldValue;

	/**
	 * Permet de savoir si la stratégie a été activée au moins une fois.
	 */
	private boolean _triggered = false;

	/**
	 * Vérifie si il faut lancer la stratégie pour cette propriété. Pour l'instant
	 * ONCHAIN n'est pas encore implémenté.
	 * 
	 * @return 'true' si la stratégie doit être lancée, sinon 'false'.
	 */
	private boolean canTriggerStrategy() {
		if (PropertyTrigger.ALWAYS == getStrategyTrigger())
			return true;

		if ((PropertyTrigger.ONSTART == getStrategyTrigger() || PropertyTrigger.ONCREATE_CHANGE == getStrategyTrigger())
				&& !_triggered)
			return true;

		if ((PropertyTrigger.ONCHANGE == getStrategyTrigger()
				|| PropertyTrigger.ONCREATE_CHANGE == getStrategyTrigger())
				&& !_refPacmanProperty.getValue().equals(_refOldValue))
			return true;

		return false;
	}

	/**
	 * Met à jour la valeur de la propriété de référence à partir des fichiers de
	 * type '.properties' et demande le lancement éventuel de la stratégie.
	 * 
	 * @param p_properties la liste des propriétés préalablement chargées.
	 */
	void applyStrategy(final Map<String, PacmanProperty> p_pacmanProperties) {

		if (canTriggerStrategy()) {
			applyChainedStrategy(p_pacmanProperties);
			doStrategy(p_pacmanProperties);
			_refOldValue = _refPacmanProperty.getValue();
			_triggered = true;
		}
	}

	/**
	 * Dans le cas ou la stratégie est basée sur un trigger de type ONCHANGE, on
	 * initialise de suite la valeur au premier démarrage du générateur afin de
	 * pouvoir comparer correctement les valeurs. Ceci UNIQUEMENT SI LA VALEUR EST
	 * NULLE (donc _triggered = false)!
	 * 
	 * @param p_value : la valeur de la propriété.
	 */
	protected void updateWithOldRefValue(final String p_value) {
		if (PropertyTrigger.ONCHANGE == getStrategyTrigger() && !_triggered) {
			_refOldValue = p_value;
			_triggered = true;
		}
	}

	/**
	 * Ce cas devrait être rare mais il est possible de chaîner les stratégies.
	 * 
	 * @param p_pacmanProperties la liste des propriétés.
	 */
	private void applyChainedStrategy(final Map<String, PacmanProperty> p_pacmanProperties) {
		if (null == getChainedStrategy())
			return;
		PacmanProperty pacmanProperty = p_pacmanProperties.get(getChainedStrategy());
		pacmanProperty.getStrategy().doStrategy(p_pacmanProperties);
	}

	/**
	 * Stratégie à appliquer pour la propriété de référence.
	 * 
	 * @param p_properties la liste des propriétés préalablement chargées.
	 */
	protected abstract void doStrategy(final Map<String, PacmanProperty> p_pacmanProperties);

	/**
	 * Permet d'appliquer la stratégie de mise à jour pour l'élément.
	 * 
	 * @param p_pacmanProperty la propriété à mettre à jour.
	 */
	protected abstract void updateProperty(final PacmanProperty p_pacmanProperty);

	/**
	 * Retourne la fréquence d'activation de la stratégie.
	 * 
	 * @return la demande d'activation pour la stratégie.
	 */
	protected abstract PropertyTrigger getStrategyTrigger();

	/**
	 * Retourne la clé de la propriété qui déclanche la stratégie.
	 * 
	 * @return _refId la clé pour l'élément maître (référence).
	 */
	protected String getRefId() {
		return _refPacmanProperty.getId();
	}

	/**
	 * Retourne la valeur de la propriété qui déclanche la stratégie.
	 * 
	 * @return _refValue la valeur pour l'élément maitre (référence).
	 */
	protected String getRefValue() {
		return _refPacmanProperty.getValue();
	}

	/**
	 * Affecte la propriété pour la mettre à disposition dans la méthode doStrategy.
	 * 
	 * @param p_refPacmanProperty la propriété de référence.
	 */
	protected void setRefPacmanProperty(PacmanProperty p_refPacmanProperty) {
		_refPacmanProperty = p_refPacmanProperty;
	}

	/**
	 * Retourne la clé de l'élément pour appliquer sa stratégie. Pour mettre en
	 * place une stratégie chainée, implementer cette méthode dans la stratégie de
	 * l'élément de référence.
	 * 
	 * @return la clé de l'élément.
	 */
	protected String getChainedStrategy() {
		return null;
	}
}
