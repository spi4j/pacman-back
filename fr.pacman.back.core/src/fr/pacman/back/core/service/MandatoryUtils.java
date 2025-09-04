package fr.pacman.back.core.service;

import org.eclipse.emf.common.util.EList;
import org.obeonetwork.dsl.entity.Entity;
import org.obeonetwork.dsl.environment.Attribute;
import org.obeonetwork.dsl.environment.MultiplicityKind;
import org.obeonetwork.dsl.environment.Property;
import org.obeonetwork.dsl.environment.Reference;

/**
 * Classe de services pour vérifier la contrainte 'obligatoire' sur les
 * attributs ou les références.
 * 
 * @author MINARM
 */
public final class MandatoryUtils {

	/**
	 * Constructeur privé.
	 */
	private MandatoryUtils() {
		super();
	}

	/**
	 * Vérifie qu'une propriété (au niveau Entity) est obligatoire.
	 * 
	 * @param p_property la propriété.
	 * @return true si la propriété est obligatoire, false sinon.
	 */
	public static boolean is_mandatoryEntity(final Property p_property) {
		if (p_property instanceof Attribute) {
			final Attribute attribute = (Attribute) p_property;
			final MultiplicityKind multiplicity = attribute.getMultiplicity();
			return isAttributeMandatory(multiplicity);
		} else if (p_property instanceof Reference) {
			final Reference reference = (Reference) p_property;
			final MultiplicityKind multiplicity = reference.getMultiplicity();
			final Reference oppositeReference = reference.getOppositeOf();
			final MultiplicityKind oppositeMultiplicity;
			if (oppositeReference == null) {
				oppositeMultiplicity = null;
			} else {
				oppositeMultiplicity = oppositeReference.getMultiplicity();
			}
			return isReferenceMandatory(multiplicity, oppositeMultiplicity);
		} else {
			throw new IllegalArgumentException("Cas non prévu pour p_property=" + p_property);
		}
	}

	/**
	 * Vérifie qu'une propriété (au niveau SOA) est obligatoire.
	 * 
	 * @param p_property la propriété
	 * @return true si la propriété est obligatoire, false sinon
	 */
	public static boolean is_mandatorySoa(final Property p_property) {
		if (p_property instanceof Attribute) {
			final Attribute attribute = (Attribute) p_property;
			final MultiplicityKind multiplicity = attribute.getMultiplicity();
			return isAttributeMandatory(multiplicity);
		} else if (p_property instanceof Reference) {
			final Reference reference = (Reference) p_property;
			final MultiplicityKind multiplicity = reference.getMultiplicity();
			final Reference oppositeReference = reference.getOppositeOf();
			final MultiplicityKind oppositeMultiplicity;
			if (oppositeReference == null) {
				oppositeMultiplicity = null;
			} else {
				oppositeMultiplicity = oppositeReference.getMultiplicity();
			}
			return isReferenceMandatory(multiplicity, oppositeMultiplicity);
		}
		return false;
	}

	/**
	 * Retourne une liste de propriétés concernées par une annotation et vérifie si
	 * toutes les propriétes sont obligatoires. La propriete doit absolument se
	 * presenter sous la forme XXXX:[ASC|DESC].
	 * 
	 * @param p_lstAttributes
	 * @return
	 */
	public static Boolean is_mandatoryAnnotationProperties(final Entity p_entity, final String p_lstStProperties) {
		String[] lstStrProperties = p_lstStProperties.split(",");
		for (String strProperty : lstStrProperties) {
			strProperty = strProperty.substring(0, strProperty.indexOf(":"));
			EList<Property> lstProperties = p_entity.getProperties();
			for (Property property : lstProperties)
				if (property.getName().equals(strProperty))
					if (!is_mandatoryEntity(property))
						return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * Vérifie qu'un attribut est obligatoire.
	 * 
	 * @param p_multiplicity la multiplicité de l'attribut
	 * @return true si l'attribut est obligatoire, false sinon
	 */
	private static boolean isAttributeMandatory(final MultiplicityKind p_multiplicity) {
		return p_multiplicity == MultiplicityKind.ONE_LITERAL || p_multiplicity == MultiplicityKind.ONE_STAR_LITERAL;
	}

	/**
	 * Vérifie qu'une référence est obligatoire.
	 * 
	 * @param p_multiplicity         la multiplicité de la référence
	 * @param p_oppositeMultiplicity la multiplicité de la référence opposée s'il y
	 *                               en a une, false sinon
	 * @return true si la référence est obligatoire, false sinon
	 */
	private static boolean isReferenceMandatory(final MultiplicityKind p_multiplicity,
			final MultiplicityKind p_oppositeMultiplicity) {
		if (p_oppositeMultiplicity == null) {
			return p_multiplicity == MultiplicityKind.ONE_LITERAL
					|| p_multiplicity == MultiplicityKind.ONE_STAR_LITERAL;
		} else {
			return p_multiplicity == MultiplicityKind.ONE_LITERAL
					|| p_oppositeMultiplicity == MultiplicityKind.ONE_LITERAL
					|| p_oppositeMultiplicity == MultiplicityKind.ONE_STAR_LITERAL;
		}
	}
}
