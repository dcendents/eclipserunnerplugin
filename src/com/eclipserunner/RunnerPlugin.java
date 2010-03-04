package com.eclipserunner;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Eclipse runner plugin activator class.
 * 
 * @author bary, vachacz
 */
public class RunnerPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "EclipseRunnerPlugin";

	private static RunnerPlugin plugin;

	public static final String ICON_PATH = "icons/";

	private final Map<String, ImageDescriptor> imageDescriptors = new HashMap<String, ImageDescriptor>(13);

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance.
	 */
	public static RunnerPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path (cached version).
	 *
	 * @param imageId Image file name.
	 * @return the image descriptor.
	 */
	public ImageDescriptor getImageDescriptor(String imageFileName) {
		ImageDescriptor imageDescriptor = imageDescriptors.get(imageFileName);
		if (imageDescriptor == null) {
			imageDescriptor = imageDescriptorFromPlugin(getDefault().getBundle().getSymbolicName(), ICON_PATH + imageFileName);
			imageDescriptors.put(imageFileName, imageDescriptor);
		}
		return imageDescriptor;
	}

	/**
	 * Returns the SWT active Shell.
	 * 
	 * @return SWT active Shell.
	 */
	public static Shell getShell() {
		return Display.getCurrent().getActiveShell();
	}

}
