package com.eclipserunner.model;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchManager;

public class RunnerModelLaunchListenerAdapter implements ILaunchConfigurationListener {

	private IRunnerModel runnerModel;
	
	public RunnerModelLaunchListenerAdapter(IRunnerModel runnerModel) {
		this.runnerModel = runnerModel;
	}
	
	public void launchConfigurationAdded(ILaunchConfiguration newConfiguration) {
		// Find old configuration
		ILaunchConfiguration oldLaunchConfiguration = getLaunchManager().getMovedFrom(newConfiguration);
		ILaunchConfigurationCategory oldConfigurationCategory = null;
		if (oldLaunchConfiguration != null) {
			oldConfigurationCategory = runnerModel.getLaunchConfigurationCategory(oldLaunchConfiguration);
		}

		// add new configuration to the same category if possible
		if (oldConfigurationCategory != null) {
			oldConfigurationCategory.add(newConfiguration);
		}
		else {
			runnerModel.getUncategorizedCategory().add(newConfiguration);
		}
	}

	public void launchConfigurationChanged(ILaunchConfiguration configuration) {
		// TODO BARY LWA in case of changed name we have to notify model, right ?
	}

	public void launchConfigurationRemoved(ILaunchConfiguration configuration) {
		runnerModel.removeLaunchConfiguration(configuration);
	}
	
	private ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}
}
