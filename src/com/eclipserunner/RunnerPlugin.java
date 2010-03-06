package com.eclipserunner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
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

	public static final String PLUGIN_ID         = "com.eclipserunner.plugin";
	public static final String PLUGIN_STATE_FILE = "runner";

	private static RunnerPlugin plugin;

	public static final String ICON_PATH = "icons/";

	private final Map<String, ImageDescriptor> imageDescriptors = new HashMap<String, ImageDescriptor>(13);


	// Callback object responsible for saving the uncomitted state of plugin.
	private class RunnerSaveParticipant implements ISaveParticipant {
		public void prepareToSave(ISaveContext context)	throws CoreException {
			// dont care
		}

		public void saving(ISaveContext context) throws CoreException {
			String newFileName = fileName(context.getSaveNumber());
			File newFile = RunnerPlugin.this.getStateLocation().append(newFileName).toFile();
			RunnerStateExternalizer.writeStateToFile(newFile);
			context.map(new Path(PLUGIN_STATE_FILE), new Path(newFileName));
			context.needSaveNumber();
		}

		public void rollback(ISaveContext context) {
			// dont care
		}

		public void doneSaving(ISaveContext context) {
			String oldFileName = fileName(context.getPreviousSaveNumber());
			File oldFile = RunnerPlugin.this.getStateLocation().append(oldFileName).toFile();
			oldFile.delete();
		}

		private String fileName(int saveNumber) {
			return PLUGIN_STATE_FILE + "-" + Integer.toString(saveNumber) + ".xml";
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		ISavedState savedState = ResourcesPlugin.getWorkspace().addSaveParticipant(this, new RunnerSaveParticipant());
		restoreSavedState(savedState);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);

		if (ResourcesPlugin.getWorkspace() != null) {
			ResourcesPlugin.getWorkspace().removeSaveParticipant(this);
		}
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

	// TODO BARY: find better solution to populate model
	private void restoreSavedState(ISavedState state) throws CoreException {
		if (state != null) {
			try {
				IPath location = state.lookup(new Path(PLUGIN_STATE_FILE));
				if (location != null) {
					File file = getStateLocation().append(location).toFile();
					RunnerStateExternalizer.readStateFromFile(file);
				}
			} catch(CoreException e) {
				e.printStackTrace();
				RunnerStateExternalizer.readDefaultState();
			}
		}
		else {
			RunnerStateExternalizer.readDefaultState();
		}
	}

}
