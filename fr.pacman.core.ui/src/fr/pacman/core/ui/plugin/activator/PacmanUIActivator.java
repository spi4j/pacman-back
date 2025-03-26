package fr.pacman.core.ui.plugin.activator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

public abstract class PacmanUIActivator extends EMFPlugin {


	
	public PacmanUIActivator(ResourceLocator[] delegateResourceLocators) {
		super(delegateResourceLocators);
	}

	/**
	 * Logs the given exception as error or warning.
	 * 
	 * @param exception The exception to log.
	 * @param blocker   <code>True</code> if the message must be logged as error,
	 *                  <code>False</code> to log it as a warning.
	 */
	public static void log(Exception exception, boolean blocker) {
		@SuppressWarnings("unused")
		final int severity;
		if (blocker) {
			severity = IStatus.ERROR;
		} else {
			severity = IStatus.WARNING;
		}
		//Activator.INSTANCE.log(new Status(severity, Activator.INSTANCE.getPluginId(),
		// exception.getMessage(), exception));
	}

	/**
	 * Puts the given message in the error log view, as error or warning.
	 * 
	 * @param message The message to put in the error log view.
	 * @param blocker <code>True</code> if the message must be logged as error,
	 *                <code>False</code> to log it as a warning.
	 */
	public static void log(String message, boolean blocker) {
		@SuppressWarnings("unused")
		int severity = IStatus.WARNING;
		if (blocker) {
			severity = IStatus.ERROR;
		}
		String errorMessage = message;
		if (errorMessage == null || "".equals(errorMessage)) { //$NON-NLS-1$
			errorMessage = "Logging null message should never happens."; //$NON-NLS-1$
		}
		//Activator.INSTANCE.log(new Status(severity, Activator.INSTANCE.getPluginId(), errorMessage));
	}
}
