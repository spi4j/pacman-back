package fr.pacman.entity.ui.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.pacman.entity.ui.generators.EntityUIGenerators;

/**
 * Lanceur pour la demande de génération de la couche de persistance à partir
 * d'un fichier de modélisation de type '.entity'.
 * 
 * @author MINARM
 */
public class EntityGeneratorsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent p_event) throws ExecutionException {
		final Iterator<?> v_iterator = HandlerUtil.getCurrentStructuredSelection(p_event).iterator();
		while (v_iterator.hasNext()) {
			final Object v_selected = v_iterator.next();
			if (v_selected instanceof IFile) {
				new EntityUIGenerators((IFile) v_selected).generate();
			}
		}
		return null;
	}
}
