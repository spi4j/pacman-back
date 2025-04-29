package fr.pacman.start.ui.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

/**
 * Utilitaire pour surveiller l'activité des vues dans Eclipse et forcer
 * l'affichage de la vue "Progress" à la place de la vue "Error Log".
 * 
 * Lorsqu'un utilisateur (ou une erreur) tente d'afficher la vue "Error Log",
 * celle-ci est automatiquement masquée, et la vue "Progress" est affichée à la
 * place.
 * 
 * Idéal pour les traitements automatisés ou les assistants (wizards) qui
 * souhaitent garder la vue de progression visible sans interférence avec le log
 * d'erreurs.
 */
class ProgressViewUtils {

	/**
	 * Constructeur privé.
	 */
	private ProgressViewUtils() {
		super();
	}

	/** ID Eclipse de la vue "Progress" */
	private static final String c_progress_view_id = "org.eclipse.ui.views.ProgressView";

	/** ID Eclipse de la vue "Error Log" */
	private static final String c_error_log_view_id = "org.eclipse.pde.runtime.LogView";

	/** Listener courant installé sur la fenêtre de travail */
	private static IPartListener2 _currentListener;

	/**
	 * Active le monitoring sur la fenêtre de travail donnée. Si la vue "Error Log"
	 * est activée, elle sera immédiatement masquée et remplacée par la vue
	 * "Progress".
	 *
	 * @param window la fenêtre de travail Eclipse à surveiller
	 */
	public static void monitor(IWorkbenchWindow p_window) {
		IWorkbenchPage page = p_window.getActivePage();
		if (page == null)
			return;

		IPartListener2 listener = new IPartListener2() {
			@Override
			public void partActivated(IWorkbenchPartReference partRef) {
				if (c_error_log_view_id.equals(partRef.getId())) {
					Display.getDefault().asyncExec(() -> {
						IViewPart logView = page.findView(c_error_log_view_id);
						if (logView != null) {
							page.hideView(logView);
						}
						try {
							page.showView(c_progress_view_id);
						} catch (PartInitException e) {
							// RAS
						}
					});
				}
			}

			@Override
			public void partBroughtToTop(IWorkbenchPartReference ref) {
			}

			@Override
			public void partClosed(IWorkbenchPartReference ref) {
			}

			@Override
			public void partDeactivated(IWorkbenchPartReference ref) {
			}

			@Override
			public void partHidden(IWorkbenchPartReference ref) {
			}

			@Override
			public void partInputChanged(IWorkbenchPartReference ref) {
			}

			@Override
			public void partOpened(IWorkbenchPartReference ref) {
			}

			@Override
			public void partVisible(IWorkbenchPartReference ref) {
			}
		};

		page.addPartListener(listener);
		_currentListener = listener;
	}

	/**
	 * Désactive le monitoring précédemment activé via
	 * {@link #monitor(IWorkbenchWindow)}. Cela restaure le comportement par défaut
	 * d'Eclipse concernant la vue "Error Log".
	 *
	 * @param window la fenêtre de travail Eclipse concernée
	 */
	public static void stopMonitoring(IWorkbenchWindow p_window) {
		if (_currentListener != null && p_window.getActivePage() != null) {
			p_window.getActivePage().removePartListener(_currentListener);
			_currentListener = null;
		}
	}
}
