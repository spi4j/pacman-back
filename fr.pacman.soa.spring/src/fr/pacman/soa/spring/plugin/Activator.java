package fr.pacman.soa.spring.plugin;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends Plugin {

	public static final String c_pluginId = "fr.pacman.soa.spring";
	private static Activator plugin;

	@Override
	public void start(final BundleContext p_context) throws Exception {
		super.start(p_context);
		plugin = this;
	}

	@Override
	public void stop(final BundleContext p_context) throws Exception {
		plugin = null;
		super.stop(p_context);
	}

	public static Activator getDefault() {
		return plugin;
	}
}
