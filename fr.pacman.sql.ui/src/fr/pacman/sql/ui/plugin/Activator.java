package fr.pacman.sql.ui.plugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

/**
 * 
 * @author MINARM
 */
public class Activator extends EMFPlugin {

	/**
	 * Plugin's id.
	 */
	public static final String c_pluginId = "fr.pacman.sql.ui";

	/**
	 * The shared instance.
	 */
	public static final Activator INSTANCE = new Activator();

	/**
	 * The implementation plugin for Eclipse.
	 */
	private static Implementation plugin;

	/**
	 * The constructor.
	 */
	public Activator() {
		super(new ResourceLocator[] {});
	}

	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}

	public static Implementation getPlugin() {
		return plugin;
	}

	/**
	 * Class implementing the EclipsePlugin instance, instanciated when the code is
	 * run in an OSGi context.
	 * 
	 * @author cedric
	 */
	public static class Implementation extends EclipsePlugin {

		/**
		 * Create the Eclipse Implementation.
		 */
		public Implementation() {
			super();
			plugin = this;
		}
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance.
	 */
	public static Activator getDefault() {
		return INSTANCE;
	}

	/**
	 * Logs the given exception as error or warning.
	 * 
	 * @param exception The exception to log.
	 * @param blocker   <code>True</code> if the message must be logged as error,
	 *                  <code>False</code> to log it as a warning.
	 */
	public static void log(Exception exception, boolean blocker) {
		final int severity;
		if (blocker) {
			severity = IStatus.ERROR;
		} else {
			severity = IStatus.WARNING;
		}
		Activator.INSTANCE.log(new Status(severity, c_pluginId, exception.getMessage(), exception));
	}

	/**
	 * Puts the given message in the error log view, as error or warning.
	 * 
	 * @param message The message to put in the error log view.
	 * @param blocker <code>True</code> if the message must be logged as error,
	 *                <code>False</code> to log it as a warning.
	 */
	public static void log(String message, boolean blocker) {
		int severity = IStatus.WARNING;
		if (blocker) {
			severity = IStatus.ERROR;
		}
		String errorMessage = message;
		if (errorMessage == null || "".equals(errorMessage)) { //$NON-NLS-1$
			errorMessage = "Logging null message should never happens."; //$NON-NLS-1$
		}
		Activator.INSTANCE.log(new Status(severity, c_pluginId, errorMessage));
	}
}
