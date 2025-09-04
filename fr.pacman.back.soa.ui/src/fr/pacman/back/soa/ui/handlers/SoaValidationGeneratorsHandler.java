package fr.pacman.back.soa.ui.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.pacman.back.soa.ui.generators.SoaValidationUIGenerators;

/**
 * Lanceur pour la demande de validation d'un modèle soa.
 * 
 * @author MINARM
 */
public class SoaValidationGeneratorsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent p_event) throws ExecutionException {
		final Iterator<?> v_iterator = HandlerUtil.getCurrentStructuredSelection(p_event).iterator();
		while (v_iterator.hasNext()) {
			final Object v_selected = v_iterator.next();
			if (v_selected instanceof IFile) {
				new SoaValidationUIGenerators((IFile) v_selected).generate();
			}
		}
		return null;
	}
}
