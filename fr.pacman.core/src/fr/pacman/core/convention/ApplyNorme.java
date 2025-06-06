package fr.pacman.core.convention;

import fr.pacman.core.convention.rule.AttributeNamingRule;
import fr.pacman.core.convention.rule.ClassNamingRule;
import fr.pacman.core.convention.rule.MethodNamingRule;
import fr.pacman.core.convention.rule.PackageNamingRule;
import fr.pacman.core.convention.rule.ParameterNamingRule;
import fr.pacman.core.convention.rule.VariableNamingRule;

/**
 * Classe qui sert de point d'entrée pour toutes les 'queries' acceleo en ce qui
 * concerne les conventions de nommage pour l'ensemble de l'application cible.
 * 
 * @author MINARM
 */
public final class ApplyNorme {

	/**
	 * Constructeur privé.
	 */
	private ApplyNorme() {
		super();
	}

	/**
	 * Applique la norme pour le package de persistence.
	 * 
	 * Attention exception, ici on retourne juste le nom du package on ne peut pas
	 * définir le chemin complet à l'avance.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_packagePersistence(final String p_value) {
		return PackageNamingRule.applyNorme_packagePersistence(p_value);
	}

	/**
	 * Retourne le package d'implémentation jdbc.
	 * 
	 * Attention exception, ici on retourne juste le nom du package.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_packageImplemJdbc(final String p_value) {
		return PackageNamingRule.applyNorme_packageImplemJdbc(p_value);
	}

	/**
	 * Retourne le package d'implémentation server.
	 * 
	 * Attention exception, ici on retourne juste le nom du package.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_packageImplemServer(final String p_value) {
		return PackageNamingRule.applyNorme_packageImplemServer(p_value);
	}

	/**
	 * Retourne le package de matching.
	 * 
	 * Attention exception, ici on retourne juste le nom du package
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_packageMatching(final String p_value) {
		return PackageNamingRule.applyNorme_packageMatching(p_value);
	}

	/**
	 * Retourne le package d'api.
	 * 
	 * Attention exception, ici on retourne juste le nom du package.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_packageApi(final String p_value) {
		return PackageNamingRule.applyNorme_packageApi(p_value);
	}

	/**
	 * Retourne le package de business.
	 * 
	 * Attention exception, ici on retourne juste le nom du package.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_packageBusiness(final String p_value) {
		return PackageNamingRule.applyNorme_packageBusiness(p_value);
	}

	/**
	 * Retourne le package de Requirement.
	 * 
	 * Requirement Attention exception, ici on retourne juste le nom du package.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_packageRequirement(final String p_value) {
		return PackageNamingRule.applyNorme_packageRequirement(p_value);
	}

	/**
	 * Applique la convention de nommage d'un attributeDefault.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_attributeDefault(final String p_value) {
		return AttributeNamingRule.applyNorme_attributeDefault(p_value);
	}

	/**
	 * Applique la convention de nommage d'un attributeEnum.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_attributeEnum(final String p_value) {
		return AttributeNamingRule.applyNorme_attributeEnum(p_value);
	}

	/**
	 * Applique la convention de nommage d'un attributeMultiple.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_attributeMultiple(final String p_value) {
		return AttributeNamingRule.applyNorme_attributeMultiple(p_value);
	}

	/**
	 * Applique la convention de nommage d'un attributeFinal.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_attributeFinal(final String p_value) {
		return AttributeNamingRule.applyNorme_attributeFinal(p_value);
	}

	/**
	 * Applique la convention de nommage d'un attributeFinalStatic.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_attributeFinalStatic(final String p_value) {
		return AttributeNamingRule.applyNorme_attributeFinalStatic(p_value);
	}

	/**
	 * Applique la convention de nommage d'un attributeStatic.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_attributeStatic(final String p_value) {
		return AttributeNamingRule.applyNorme_attributeStatic(p_value);
	}

	/**
	 * Applique la convention de nommage d'un classAbstract.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_classAbstract(final String p_value) {
		return ClassNamingRule.applyNorme_classAbstract(p_value);
	}

	/**
	 * Applique la convention de nommage d'un classDefault.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_classDefault(final String p_value) {
		return ClassNamingRule.applyNorme_classDefault(p_value);
	}
	
	/**
	 * Applique la convention de nommage d'un classAnnotation (i.e. annotation
	 * Java).
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_classAnnotation(final String p_value) {
		return ClassNamingRule.applyNorme_classAnnotation(p_value);
	}

	/**
	 * Applique la convention de nommage d'un classEnum.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_classEnum(final String p_value) {
		return ClassNamingRule.applyNorme_classEnum(p_value);
	}

	/**
	 * Applique la convention de nommage d'un classImplem.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_classImplem(final String p_value) {
		return ClassNamingRule.applyNorme_classImplem(p_value);
	}

	/**
	 * Applique la convention de nommage d'un classInterface.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_classInterface(final String p_value) {
		return ClassNamingRule.applyNorme_classInterface(p_value);
	}

	/**
	 * Applique la convention de nommage d'un classTest.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_classTest(final String p_value) {
		return ClassNamingRule.applyNorme_classTest(p_value);
	}

	/**
	 * Applique la convention de nommage d'un methodDefault.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_methodDefault(final String p_value) {
		return MethodNamingRule.applyNorme_methodDefault(p_value);
	}

	/**
	 * Applique la convention de nommage d'un methodMultiple.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_methodMultiple(final String p_value) {
		return MethodNamingRule.applyNorme_methodMultiple(p_value);
	}

	/**
	 * Applique la convention de nommage d'un methodGet.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_methodGet(final String p_value) {
		return MethodNamingRule.applyNorme_methodGet(p_value);
	}

	/**
	 * Applique la convention de nommage d'un methodReset.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_methodReset(final String p_value) {
		return MethodNamingRule.applyNorme_methodReset(p_value);
	}

	/**
	 * Applique la convention de nommage d'un methodSet.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_methodSet(final String p_value) {
		return MethodNamingRule.applyNorme_methodSet(p_value);
	}

	/**
	 * Applique la convention de nommage d'un parameterMethodClassicIn.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_parameterMethodClassicIn(final String p_value) {
		return ParameterNamingRule.applyNorme_parameterMethodClassicIn(p_value);
	}

	/**
	 * Applique la convention de nommage d'un parameterMethodMultiple.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_parameterMethodMultiple(final String p_value) {
		return ParameterNamingRule.applyNorme_parameterMethodMultiple(p_value);
	}

	/**
	 * Applique la convention de nommage d'un parameterMethodMultipleMap.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_parameterMethodMultipleMap(final String p_value) {
		return ParameterNamingRule.applyNorme_parameterMethodMultipleMap(p_value);
	}

	/**
	 * Applique la convention de nommage d'un parameterMethodObjectIn.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_parameterMethodObjectIn(final String p_value) {
		return ParameterNamingRule.applyNorme_parameterMethodObjectIn(p_value);
	}

	/**
	 * Applique la convention de nommage d'un parameterMethodClassicInOut.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_parameterMethodClassicInOut(final String p_value) {
		return ParameterNamingRule.applyNorme_parameterMethodClassicInOut(p_value);
	}

	/**
	 * Applique la convention de nommage d'un parameterMethodObjectInOut.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_parameterMethodObjectInOut(final String p_value) {
		return ParameterNamingRule.applyNorme_parameterMethodObjectInOut(p_value);
	}

	/**
	 * Applique la convention de nommage d'un parameterMethodClassicOut.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_parameterMethodClassicOut(final String p_value) {
		return ParameterNamingRule.applyNorme_parameterMethodClassicOut(p_value);
	}

	/**
	 * Applique la convention de nommage d'un parameterMethodObjectOut.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_parameterMethodObjectOut(final String p_value) {
		return ParameterNamingRule.applyNorme_parameterMethodObjectOut(p_value);
	}

	/**
	 * Applique la convention de nommage d'un parameterMethodReset.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_parameterMethodReset(final String p_value) {
		return ParameterNamingRule.applyNorme_parameterMethodReset(p_value);
	}

	/**
	 * Applique la convention de nommage d'une variableDefault.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_variableDefault(final String p_value) {
		return VariableNamingRule.applyNorme_variableDefault(p_value);
	}

	/**
	 * Applique la convention de nommage d'une variableMultipleDefault.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_variableMultipleDefault(final String p_value) {
		return VariableNamingRule.applyNorme_variableMultipleDefault(p_value);
	}

	/**
	 * Applique la convention de nommage d'une variableMultipleDefault.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_variableMultipleMap(final String p_value) {
		return VariableNamingRule.applyNorme_variableMultipleMap(p_value);
	}

	/**
	 * Applique la convention de nommage d'une variableFinal.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_variableFinal(final String p_value) {
		return VariableNamingRule.applyNorme_variableFinal(p_value);
	}

	/**
	 * Applique la convention de nommage d'une variableObjectDefault.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_variableObjectDefault(final String p_value) {
		return VariableNamingRule.applyNorme_variableObjectDefault(p_value);
	}

	/**
	 * Applique la convention de nommage d'une variableObjectFinal.
	 * 
	 * @param p_value la valeur du modèle à normer.
	 * 
	 * @return la valeur normée.
	 */
	public static String norme_variableObjectFinal(final String p_value) {
		return VariableNamingRule.applyNorme_variableObjectFinal(p_value);
	}
}
