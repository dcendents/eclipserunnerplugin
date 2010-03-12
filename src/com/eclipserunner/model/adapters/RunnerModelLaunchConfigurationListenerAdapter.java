package com.eclipserunner.model.adapters;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchManager;

import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.impl.LaunchConfigurationNode;

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
		LaunchConfigurationNode launchConfigurationNode = null;
		ILaunchConfiguration oldLaunchConfiguration = getLaunchManager().getMovedFrom(newConfiguration);
		if (oldLaunchConfiguration != null) {

			// TODO BARY: verify this
			// better: runnerModel.remove(oldLaunchConfiguration);
			//   Reply from bary: NO because you need to find original node when renaming configuration or you end up in uncategorized category

			launchConfigurationNode = (LaunchConfigurationNode) runnerModel.findLaunchConfigurationNodeBy(oldLaunchConfiguration);
			if (launchConfigurationNode != null) {
				launchConfigurationNode.setLaunchConfiguration(newConfiguration);
				return;
			}
		}

		launchConfigurationNode = new LaunchConfigurationNode();
		launchConfigurationNode.setLaunchConfiguration(newConfiguration);
		runnerModel.getUncategorizedCategory().add(launchConfigurationNode);
	}

	public void launchConfigurationChanged(ILaunchConfiguration configuration) {
		// Dont care at this moment. LaunchConfiguration rename fires two events
		// launchConfigurationAdded and launchConfigurationRemoved.
		// Old configuration is deleted and new is created in place of the old one.
	}

	public void launchConfigurationRemoved(ILaunchConfiguration oldConfiguration) {
		LaunchConfigurationNode launchConfigurationNode = (LaunchConfigurationNode) runnerModel.findLaunchConfigurationNodeBy(oldConfiguration);
		if (launchConfigurationNode != null) {
			runnerModel.removeLaunchConfigurationNode(launchConfigurationNode);
		}
	}

	private ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}

}
