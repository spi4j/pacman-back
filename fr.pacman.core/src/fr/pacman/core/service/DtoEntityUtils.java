package fr.pacman.core.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.obeonetwork.dsl.entity.Entity;
import org.obeonetwork.dsl.environment.Attribute;
import org.obeonetwork.dsl.environment.DTO;
import org.obeonetwork.dsl.environment.StructuredType;
import org.obeonetwork.dsl.environment.impl.AttributeImpl;

/**
 * Classe utilitaire pour rechercher des DTOs à partir de la manipulation des
 * objets de type entité.
 * 
 * @author MINARM
 */
public final class DtoEntityUtils {

	/**
	 * Constructeur privé.
	 */
	private DtoEntityUtils() {
		super();
	}

	/**
	 * Retourne les attributs du DTO ainsi que ceux potentiellement hérités par une
	 * entité. On supprime les attributs en doublon (peut être dus à un héritage).
	 * 
	 * @param p_dto le DTO
	 * @return les attributs du DTO (hérités ou non)
	 */
	public static List<Attribute> get_attributes(final DTO p_dto) {

		if ((p_dto != null) && (p_dto.getAssociatedTypes() != null)) {
			for (StructuredType inheritedType : p_dto.getAssociatedTypes()) {
				if (inheritedType instanceof Entity) {
					final Entity entity = (Entity) inheritedType;
					final EList<org.obeonetwork.dsl.environment.Attribute> attributesEntity = entity.getAttributes();
					for (org.obeonetwork.dsl.environment.Attribute entityAttribute : attributesEntity) {
						if (!dtoContainsEntityAttribute(p_dto, entityAttribute) && !entityAttribute.isIsIdentifier()) {
							@SuppressWarnings("unused")
							final Attribute environmentAttribute = new DtoAttributeIntern(p_dto, entityAttribute);
						}
					}
				}
			}
		}
		final EList<Attribute> attributes = p_dto.getAttributes();
		final Set<String> attributesNames = new HashSet<String>();
		final List<Attribute> finalAttributes = new ArrayList<Attribute>();
		for (final Attribute attribute : attributes) {
			final String attributeName = attribute.getName();
			if (!attributesNames.contains(attributeName)) {
				attributesNames.add(attributeName);
				finalAttributes.add(attribute);
			}
		}
		return finalAttributes;
	}

	/**
	 * Retourne les attributs de l'entité ainsi que ceux potentiellement hérités par
	 * une autre entité. Attention, à ce niveau cette méthode est ouverte et ne fait
	 * aucun tri au niveau de la qualité des attributs, elle se contente de renvoyer
	 * la totalité des attributs, qu'il soient calculés, de type identifiant, etc..
	 * 
	 * @param p_entity l'entité pour laquelle retourner l'ensemble des attributs
	 * @return les attributs de l'entité (hérités ou non)
	 */
	@SuppressWarnings("unused")
	public static List<Attribute> get_attributes(final Entity p_entity) {
		final List<Attribute> attributes = new ArrayList<Attribute>(p_entity.getAttributes());
		if (p_entity.getAssociatedTypes() != null) {
			for (StructuredType inheritedType : p_entity.getAssociatedTypes()) {
				final EList<Attribute> inheritedAttributes = inheritedType.getAttributes();
				for (Attribute inheritedAttribute : inheritedAttributes) {
					
				}
			}
		}
		return attributes;
	}

	/**
	 * Retourne la valeur 'true' si l'entité contient déjà cet attribut.
	 * 
	 * @param p_entity          l'entité.
	 * @param p_entityAttribute l'attribut d'entité
	 * @return true si le DTO contient déjà cet attribut d'entité, false sinon
	 */
	@SuppressWarnings("unused")
	private static boolean entityContainsDTOAttribute(final Entity p_entity, final Attribute p_entityAttribute) {
		boolean retour = false;
		for (Attribute entityAttribute : p_entity.getAttributes()) {
			if (entityAttribute instanceof EntityAttributeIntern
					&& ((EntityAttributeIntern) entityAttribute)._dtoAttribute == p_entityAttribute) {
				retour = true;
				break;
			}
		}
		return retour;
	}

	/**
	 * Classe interne de gestion de recopie des attributs d'entité dans DTO
	 * 
	 * @author MINARM
	 */
	private static class EntityAttributeIntern extends AttributeImpl {

		private final org.obeonetwork.dsl.environment.Attribute _dtoAttribute;

		/**
		 * Construit un attribut de Entity à partir d'un attribut de DTO.
		 * 
		 * @param p_entity       le Entity qui contiendra l'attribut
		 * @param p_dtoAttribute l'attribut d'entité
		 */
		@SuppressWarnings("unused")
		public EntityAttributeIntern(final Entity p_entity,
				final org.obeonetwork.dsl.environment.Attribute p_dtoAttribute) {
			super();
			_dtoAttribute = p_dtoAttribute;
			setName(p_dtoAttribute.getName());
			setDescription(p_dtoAttribute.getDescription());
			setMetadatas(p_dtoAttribute.getMetadatas());
			setMultiplicity(p_dtoAttribute.getMultiplicity());
			setType(p_dtoAttribute.getType());
			setContainingType(p_entity);
		}
	}

	/**
	 * Classe interne de gestion pour la recopie des attributs d'entité dans un DTO.
	 * 
	 * @author MINARM
	 */
	private static class DtoAttributeIntern extends AttributeImpl {

		private final org.obeonetwork.dsl.environment.Attribute _entityAttribute;

		/**
		 * Construit un attribut de DTO à partir d'un attribut d'entité.
		 * 
		 * @param p_dto             le DTO qui contiendra l'attribut
		 * @param p_entityAttribute l'attribut d'entité
		 */
		public DtoAttributeIntern(final DTO p_dto, final org.obeonetwork.dsl.environment.Attribute p_entityAttribute) {
			super();
			_entityAttribute = p_entityAttribute;
			setName(p_entityAttribute.getName());
			setDescription(p_entityAttribute.getDescription());
			setMetadatas(p_entityAttribute.getMetadatas());
			setMultiplicity(p_entityAttribute.getMultiplicity());
			setType(p_entityAttribute.getType());
			setContainingType(p_dto);
		}
	}

	/**
	 * Retourne la valeur 'true' si le DTO contient déjà cet attribut d'entité.
	 * 
	 * @param p_dto             le dto
	 * @param p_entityAttribute l'attribut d'entité
	 * @return true si le DTO contient déjà cet attribut d'entité, false sinon
	 */
	private static boolean dtoContainsEntityAttribute(final DTO p_dto,
			final org.obeonetwork.dsl.environment.Attribute p_entityAttribute) {
		boolean found = false;
		for (Attribute dtoAttribute : p_dto.getAttributes()) {
			if (dtoAttribute instanceof DtoAttributeIntern
					&& ((DtoAttributeIntern) dtoAttribute)._entityAttribute == p_entityAttribute) {
				found = true;
				break;
			}
		}
		return found;
	}
}
