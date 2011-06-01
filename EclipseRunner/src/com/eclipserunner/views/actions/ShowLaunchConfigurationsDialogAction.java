package com.eclipserunner.views.actions;

import static org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog.LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_LAST_LAUNCHED;
import static org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog.LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_SELECTION;

import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

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

	private INodeSelection selection;

	public ShowLaunchConfigurationsDialogAction(INodeSelection selection, String launchGroupId) {
		super(launchGroupId);
		this.selection = selection;
	}

	@Override
	public void run() {
		LaunchConfigurationsDialog dialog = new LaunchConfigurationsDialog(
			RunnerPlugin.getRunnerShell(),
			getLaunchConfigurationManager().getLaunchGroup(getLaunchGroupId())
		);

		if (selection.hasExactlyOneNode() && selection.firstNodeHasType(ILaunchNode.class)) {
			dialog.setOpenMode(LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_SELECTION);
			dialog.setInitialSelection(
				asStructuredSelection(selection.getFirstNodeAs(ILaunchNode.class).getLaunchConfiguration())
			);
		}
		else {
			dialog.setOpenMode(LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_LAST_LAUNCHED);
		}

		dialog.open();
	}
	
	public static IStructuredSelection asStructuredSelection(Object object) {
		return new StructuredSelection(object);
	}

}
