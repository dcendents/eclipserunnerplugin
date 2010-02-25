package com.eclipserunner.views.actions;

import org.eclipse.debug.ui.DebugUITools;

import com.eclipserunner.views.IConfigurationSelection;

/**
 * Action responsible for launching selected configuration.
 * 
 * @author bary
 */
@SuppressWarnings("restriction")
public class LaunchConfigurationAction extends AbstractLaunchAction {

	private IConfigurationSelection configurationSelection;

	public LaunchConfigurationAction(IConfigurationSelection configurationSelection, String launchGroupId) {
		super(launchGroupId);
		this.configurationSelection = configurationSelection;
	}

	@Override
	public void run() {
		DebugUITools.launch(
			configurationSelection.getSelectedLaunchConfiguration(), 
			getLaunchConfigurationManager().getLaunchGroup(getLaunchGroupId()).getMode()
		);
	}

}
