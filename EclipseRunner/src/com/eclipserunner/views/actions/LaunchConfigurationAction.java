package com.eclipserunner.views.actions;

import org.eclipse.debug.ui.DebugUITools;

import com.eclipserunner.model.INodeSelection;
import com.eclipserunner.model.impl.LaunchNode;

/**
 * Action responsible for launching selected configuration.
 * 
 * @author bary
 */
@SuppressWarnings("restriction")
public class LaunchConfigurationAction extends AbstractLaunchAction {

	private INodeSelection nodeSelection;

	public LaunchConfigurationAction(INodeSelection launchConfigurationSelection, String launchGroupId) {
		super(launchGroupId);
		this.nodeSelection = launchConfigurationSelection;
	}

	@Override
	public void run() {
		if (nodeSelection.ofSingleNode() && nodeSelection.firstElementHasType(LaunchNode.class)) {
			DebugUITools.launch(
				nodeSelection.getSelectedLaunchNode().getLaunchConfiguration(),
				getLaunchConfigurationManager().getLaunchGroup(getLaunchGroupId()).getMode()
			);
		}
	}

}
