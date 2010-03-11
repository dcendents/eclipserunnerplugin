package com.eclipserunner.model;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchManager;

/**
 * Adapter listening for Launch manager event.
 * 
 * @author bary
 *
 */
public class RunnerModelLaunchConfigurationListenerAdapter implements ILaunchConfigurationListener {

	private IRunnerModel runnerModel;

	public RunnerModelLaunchConfigurationListenerAdapter(IRunnerModel runnerModel) {
		this.runnerModel = runnerModel;
	}

	public void launchConfigurationAdded(ILaunchConfiguration newConfiguration) {
		// TODO LWA verify
		ILaunchConfiguration oldLaunchConfiguration = getLaunchManager().getMovedFrom(newConfiguration);
		if (oldLaunchConfiguration != null) {
			
			// TODO BARY: verify this
			// better: runnerModel.remove(oldLaunchConfiguration);
			
			LaunchConfigurationNode launchConfigurationNode = (LaunchConfigurationNode) runnerModel.findLaunchConfigurationNodeBy(oldLaunchConfiguration);
			if (launchConfigurationNode != null) {
				launchConfigurationNode.setLaunchConfiguration(newConfiguration);
				return;
			}
		}

		runnerModel.getUncategorizedCategory().add(newConfiguration);
	}

	public void launchConfigurationChanged(ILaunchConfiguration configuration) {
		// Dont care at this moment. LaunchConfiguration rename fires two events
		// launchConfigurationAdded and launchConfigurationRemoved.
		// Old configuration is deleted and new is created in place of the old one.
	}

	public void launchConfigurationRemoved(ILaunchConfiguration configuration) {
		runnerModel.removeLaunchConfiguration(configuration);
	}

	private ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}

}
