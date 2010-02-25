package com.eclipserunner.views.actions;

import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Action responsible for showing LaunchConfigurationsDialog.
 * 
 * @author bary
 */
@SuppressWarnings("restriction")
public class ShowLaunchConfigurationsDialogAction extends AbstractLaunchAction {

	public ShowLaunchConfigurationsDialogAction(String launchGroupId) {
		super(launchGroupId);
	}

	@Override
	public void run() {
		LaunchConfigurationsDialog dialog = new LaunchConfigurationsDialog(
				Display.getCurrent().getActiveShell(),
				getLaunchConfigurationManager().getLaunchGroup(getLaunchGroupId())
		);
		dialog.open();
	}

}
