package com.eclipserunner.views.actions;

import org.eclipse.debug.ui.DebugUITools;

import com.eclipserunner.model.ILaunchConfigurationSelection;

/**
 * Action responsible for launching selected configuration.
 * 
 * @author bary
 */
@SuppressWarnings("restriction")
public class LaunchConfigurationAction extends AbstractLaunchAction {

	private ILaunchConfigurationSelection launchConfigurationSelection;

	public LaunchConfigurationAction(ILaunchConfigurationSelection launchConfigurationSelection, String launchGroupId) {
		super(launchGroupId);
		this.launchConfigurationSelection = launchConfigurationSelection;
	}

	@Override
	public void run() {
		if (launchConfigurationSelection.isLaunchConfigurationSelected()) {
			DebugUITools.launch(
				launchConfigurationSelection.getSelectedLaunchConfiguration(),
				getLaunchConfigurationManager().getLaunchGroup(getLaunchGroupId()).getMode()
			);
		}
	}

}
