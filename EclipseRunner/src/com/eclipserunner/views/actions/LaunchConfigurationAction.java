package com.eclipserunner.views.actions;

import org.eclipse.debug.ui.DebugUITools;

import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeSelection;

/**
 * Action responsible for launching selected configuration.
 * 
 * @author bary
 */
@SuppressWarnings("restriction")
public class LaunchConfigurationAction extends AbstractLaunchAction {

	private INodeSelection selection;

	public LaunchConfigurationAction(INodeSelection selection, String launchGroupId) {
		super(launchGroupId);
		this.selection = selection;
	}

	@Override
	public void run() {
		if (selection.hasExactlyOneNode() && selection.firstNodeHasType(ILaunchNode.class)) {
			DebugUITools.launch(
				selection.getFirstNodeAs(ILaunchNode.class).getLaunchConfiguration(),
				getLaunchConfigurationManager().getLaunchGroup(getLaunchGroupId()).getMode()
			);
		}
	}

}
