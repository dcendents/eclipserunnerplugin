package com.eclipserunner.views.actions;

import static com.eclipserunner.utils.SelectionUtils.asStructuredSelection;
import static org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog.LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_LAST_LAUNCHED;
import static org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog.LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_SELECTION;

import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.views.ILaunchConfigurationSelection;

/**
 * Action responsible for showing LaunchConfigurationsDialog.
 * 
 * @author bary
 */
@SuppressWarnings("restriction")
public class ShowLaunchConfigurationsDialogAction extends AbstractLaunchAction {

	private ILaunchConfigurationSelection selection;

	public ShowLaunchConfigurationsDialogAction(ILaunchConfigurationSelection launchConfigurationSelection, String launchGroupId) {
		super(launchGroupId);
		this.selection = launchConfigurationSelection;
	}

	@Override
	public void run() {
		LaunchConfigurationsDialog dialog = new LaunchConfigurationsDialog(
				RunnerPlugin.getRunnerShell(),
				getLaunchConfigurationManager().getLaunchGroup(getLaunchGroupId())
		);

		if (selection.isLaunchConfigurationSelected()) {
			dialog.setOpenMode(LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_SELECTION);
			dialog.setInitialSelection(asStructuredSelection(selection.getSelectedLaunchConfiguration()));
		}
		else {
			dialog.setOpenMode(LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_LAST_LAUNCHED);
		}

		dialog.open();
	}

}
