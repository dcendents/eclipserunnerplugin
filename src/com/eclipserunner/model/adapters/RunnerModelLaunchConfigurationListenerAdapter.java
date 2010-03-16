package com.eclipserunner.model.adapters;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchManager;

import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.impl.LaunchNode;

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
		LaunchNode launchNode = null;
		ILaunchConfiguration oldLaunchConfiguration = getLaunchManager().getMovedFrom(newConfiguration);
		if (oldLaunchConfiguration != null) {

			launchNode = (LaunchNode) runnerModel.findLaunchNodeBy(oldLaunchConfiguration);
			if (launchNode != null) {
				launchNode.setLaunchConfiguration(newConfiguration);
				return;
			}
		}

		launchNode = new LaunchNode();
		launchNode.setLaunchConfiguration(newConfiguration);
		runnerModel.getDefaultCategoryNode().add(launchNode);
	}

	public void launchConfigurationChanged(ILaunchConfiguration configuration) {
		// Dont care at this moment. LaunchConfiguration rename fires two events
		// launchConfigurationAdded and launchConfigurationRemoved.
		// Old configuration is deleted and new is created in place of the old one.
	}

	public void launchConfigurationRemoved(ILaunchConfiguration oldConfiguration) {
		LaunchNode launchNode = (LaunchNode) runnerModel.findLaunchNodeBy(oldConfiguration);
		if (launchNode != null) {
			runnerModel.removeLaunchNode(launchNode);
		}
	}

	private ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}

}
