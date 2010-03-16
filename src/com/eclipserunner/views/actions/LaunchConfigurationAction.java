package com.eclipserunner.views.actions;

import org.eclipse.debug.ui.DebugUITools;

import com.eclipserunner.model.INodeSelection;

/**
 * Action responsible for launching selected configuration.
 * 
 * @author bary
 */
@SuppressWarnings("restriction")
public class LaunchConfigurationAction extends AbstractLaunchAction {

	private INodeSelection launchConfigurationSelection;

	public LaunchConfigurationAction(INodeSelection launchConfigurationSelection, String launchGroupId) {
		super(launchGroupId);
		this.launchConfigurationSelection = launchConfigurationSelection;
	}

	@Override
	public void run() {
		if (launchConfigurationSelection.isLaunchNodeSelected()) {
			DebugUITools.launch(
					launchConfigurationSelection.getSelectedLaunchNode().getLaunchConfiguration(),
					getLaunchConfigurationManager().getLaunchGroup(getLaunchGroupId()).getMode()
			);
		}
	}

}
