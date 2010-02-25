package com.eclipserunner.views.actions;

import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

import com.eclipserunner.views.SampleView;


/**
 * Action responsible for showing LaunchConfigurationsDialog.
 * 
 * @author bary
 *
 */
public class ShowLaunchConfigurationsDialogAction extends Action {

	private SampleView view;
	private String launchGroupId;

	public ShowLaunchConfigurationsDialogAction(SampleView view, String launchGroupId) {
		this.view = view;
		this.launchGroupId = launchGroupId;
	}

	@Override
	public void run() {
		LaunchConfigurationsDialog dialog = new LaunchConfigurationsDialog(
				Display.getCurrent().getActiveShell(),
				DebugUIPlugin.getDefault().getLaunchConfigurationManager().getLaunchGroup(this.launchGroupId)
		);
		dialog.open();
	}

}
