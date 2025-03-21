package fr.pacman.soa.ui.plugin;

import org.eclipse.emf.common.util.ResourceLocator;

import fr.pacman.core.ui.plugin.activator.PacmanUIActivator;

/**
 * 
 * @author MINARM
 */
public class Activator extends PacmanUIActivator {

	/**
	 * Plugin's id.
	 */
	public static final String c_pluginId = "fr.pacman.soa.ui";

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
}
