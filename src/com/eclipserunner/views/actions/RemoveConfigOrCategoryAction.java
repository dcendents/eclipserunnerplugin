package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_removeCategoryConfirm;
import static com.eclipserunner.Messages.Message_removeConfigurationConfirm;
import static com.eclipserunner.Messages.Message_removeConfirm;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeSelection;
import com.eclipserunner.model.IRunnerModel;

/**
 * @author vachacz
 */
public class RemoveConfigOrCategoryAction extends Action {

	private INodeSelection launchConfigurationSelection;
	private IRunnerModel runnerModel;

	public RemoveConfigOrCategoryAction(INodeSelection launchConfigurationSelection,
			IRunnerModel runnerModel) {
		this.launchConfigurationSelection = launchConfigurationSelection;
		this.runnerModel = runnerModel;
	}

	@Override
	// TODO BARY LWA add support for delete many selected items not just first one.
	public void run() {
		Object selectedObject = launchConfigurationSelection.getSelectedObject();

		if (selectedObject instanceof ILaunchNode) {
			removeLaunchConfiguration((ILaunchNode) selectedObject);
		} else if (selectedObject instanceof ICategoryNode) {
			removeLaunchConfigurationCategory((ICategoryNode) selectedObject);
		}
		// else do nothing
	}

	private void removeLaunchConfigurationCategory(ICategoryNode selectedCategory) {
		boolean confirmed = MessageDialog.openConfirm(RunnerPlugin.getRunnerShell(), Message_removeConfirm, Message_removeCategoryConfirm);
		if (confirmed) {
			runnerModel.removeCategoryNode(selectedCategory);
		}
	}

	private void removeLaunchConfiguration(ILaunchNode selectedConfiguration) {
		boolean confirmed = MessageDialog.openConfirm(RunnerPlugin.getRunnerShell(), Message_removeConfirm, Message_removeConfigurationConfirm);
		if (confirmed) {
			runnerModel.removeLaunchNode(selectedConfiguration);
		}
	}

}
