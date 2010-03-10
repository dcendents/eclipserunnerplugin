package com.eclipserunner.model;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchManager;

public class RunnerModelLaunchConfigurationListenerAdapter implements ILaunchConfigurationListener {

	private IRunnerModel runnerModel;

	public RunnerModelLaunchConfigurationListenerAdapter(IRunnerModel runnerModel) {
		this.runnerModel = runnerModel;
	}

	public void launchConfigurationAdded(ILaunchConfiguration newConfiguration) {
		
	// TODO LWA
		
//		// Find old configuration
//		ILaunchConfiguration oldLaunchConfiguration = getLaunchManager().getMovedFrom(newConfiguration);
//		ILaunchConfigurationCategory oldConfigurationCategory = null;
//		if (oldLaunchConfiguration != null) {
//			oldConfigurationCategory = runnerModel.getLaunchConfigurationCategory(oldLaunchConfiguration);
//		}
//
//		// add new configuration to the same category if possible
//		if (oldConfigurationCategory != null) {
//			oldConfigurationCategory.add(newConfiguration);
//		}
//		else {
			runnerModel.getUncategorizedCategory().add(newConfiguration);
//		}
	}

	public void launchConfigurationChanged(ILaunchConfiguration configuration) {
		// Dont care at this moment. LaunchConfiguration rename fires two events
		// launchConfigurationAdded and launchConfigurationRemoved.
		// Old configuration is deleted and new is created in place of the old one.
	}

	public void launchConfigurationRemoved(ILaunchConfiguration configuration) {
		runnerModel.removeLaunchConfiguration(configuration);
	}

	@SuppressWarnings("unused")
	private ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}
}
