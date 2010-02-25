package com.eclipserunner.activator;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "EclipseRunnerPliugin";

	// The shared instance
	private static Activator plugin;

	// Icons path
	public static final String ICON_PATH = "icons/";

	// Map containing preloaded ImageDescriptors
	private final Map<String, ImageDescriptor> imageDescriptors = new HashMap<String, ImageDescriptor>(13);

	// Resource bundle
	private ResourceBundle resourceBundle;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// initialize resource strings
		try {
			this.resourceBundle = ResourceBundle.getBundle("com.eclipserunner.messages");
		}
		catch (MissingResourceException x) {
			this.resourceBundle = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path (cached version)
	 *
	 * @param id the id of the image descriptor or relative icon path if icon is inside of default icons folder
	 * @return the image descriptor
	 */
	public ImageDescriptor getImageDescriptor(String id) {
		ImageDescriptor imageDescriptor = this.imageDescriptors.get(id);
		if (imageDescriptor == null) {
			imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, ICON_PATH + id);
			this.imageDescriptors.put(id, imageDescriptor);
		}
		return imageDescriptor;
	}


	public static String getResourceString(String key) {
		ResourceBundle bundle = Activator.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		}
		catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return this.resourceBundle;
	}

}
