package fr.pacman.back.core.service;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.obeonetwork.dsl.entity.Entity;
import org.obeonetwork.dsl.environment.Attribute;
import org.obeonetwork.dsl.environment.ObeoDSMObject;
import org.obeonetwork.dsl.environment.Reference;

public class EObjectUtils {

	/**
	 * Retourne la racine du modèle à partir d'un élément du modèle.
	 * 
	 * @param p_obj un objet de type {@link EObject}.
	 * @return la racine du modèle
	 */
	public static EObject get_root(final EObject p_obj) {
		return EcoreUtil.getRootContainer(p_obj);
	}

	/**
	 * Vérifie si une entité a des commentaires au niveau de ses attributs et/ou de
	 * ses références.
	 * 
	 * @param p_obj l'entité
	 * @return la valeur 'true' si au moins un commentaire a été saisi au niveau de
	 *         l'entité, d'un de ses attributs et ou d'une de ses références, sinon,
	 *         retourne la valeur 'false'.
	 */
	public static boolean has_comments(final Entity p_obj) {
		if (has_comment(p_obj))
			return true;
		for (Attribute attribut : p_obj.getAttributes()) {
			if (has_comment(attribut))
				return true;
		}
		for (Reference reference : p_obj.getReferences()) {
			if (has_comment(reference))
				return true;
		}
		return false;
	}

	/**
	 * Vérifie si un objet a un commentaire.
	 * 
	 * @param p_obj l'objet à vérifier.
	 * @return la valeur 'true' si un commentaire a été saisi au niveau de l'objet,
	 *         sinon, retourne la valeur 'false'.
	 */
	public static boolean has_comment(final ObeoDSMObject p_obj) {
		if (p_obj.getDescription() != null && !p_obj.getDescription().trim().isEmpty())
			return true;
		return false;
	}
}
