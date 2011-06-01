package com.eclipserunner.views.actions;

import static com.eclipserunner.utils.SelectionUtils.asStructuredSelection;
import static org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog.LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_LAST_LAUNCHED;
import static org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog.LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_SELECTION;

import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeSelection;

/**
 * Action responsible for showing LaunchConfigurationsDialog.
 *
 * @author bary
 */
@SuppressWarnings("restriction")
public class ShowLaunchConfigurationsDialogAction extends AbstractLaunchAction {

	private INodeSelection nodeSelection;

	public ShowLaunchConfigurationsDialogAction(INodeSelection launchConfigurationSelection, String launchGroupId) {
		super(launchGroupId);
		this.nodeSelection = launchConfigurationSelection;
	}

	@Override
	public void run() {
		LaunchConfigurationsDialog dialog = new LaunchConfigurationsDialog(
			RunnerPlugin.getRunnerShell(),
			getLaunchConfigurationManager().getLaunchGroup(getLaunchGroupId())
		);

		if (nodeSelection.ofSingleNode() && nodeSelection.firstElementHasType(ILaunchNode.class)) {
			dialog.setOpenMode(LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_SELECTION);
			dialog.setInitialSelection(asStructuredSelection(nodeSelection.getFirstElementAs(ILaunchNode.class).getLaunchConfiguration()));
		}
		else {
			dialog.setOpenMode(LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_LAST_LAUNCHED);
		}

		dialog.open();
	}

}
