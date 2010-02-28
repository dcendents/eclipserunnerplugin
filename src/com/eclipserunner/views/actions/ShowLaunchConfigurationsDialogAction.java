package com.eclipserunner.views.actions;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationDialog;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog;
import org.eclipse.jface.viewers.StructuredSelection;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.views.ILaunchConfigurationSelection;

/**
 * Action responsible for showing LaunchConfigurationsDialog.
 * 
 * @author bary
 */
@SuppressWarnings("restriction")
public class ShowLaunchConfigurationsDialogAction extends AbstractLaunchAction {

	private ILaunchConfigurationSelection launchConfigurationSelection;

	public ShowLaunchConfigurationsDialogAction(ILaunchConfigurationSelection launchConfigurationSelection, String launchGroupId) {
		super(launchGroupId);
		this.launchConfigurationSelection = launchConfigurationSelection;
	}

	@Override
	public void run() {
		LaunchConfigurationsDialog dialog = new LaunchConfigurationsDialog(
				RunnerPlugin.getShell(),
				getLaunchConfigurationManager().getLaunchGroup(getLaunchGroupId())
		);

		// if selected then open dialog on selected configuration.
		ILaunchConfiguration selectedLaunchConfiguration = this.launchConfigurationSelection.getSelectedLaunchConfiguration();
		if (selectedLaunchConfiguration != null) {
			dialog.setOpenMode(LaunchConfigurationDialog.LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_SELECTION);
			dialog.setInitialSelection(new StructuredSelection(selectedLaunchConfiguration));
		}
		else {
			dialog.setOpenMode(LaunchConfigurationDialog.LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_LAST_LAUNCHED);
		}

		dialog.open();
	}

}
