package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_removeCategoryConfirm;
import static com.eclipserunner.Messages.Message_removeConfigurationConfirm;
import static com.eclipserunner.Messages.Message_removeConfirm;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.LaunchTreeContentProvider;
import com.eclipserunner.views.ILaunchConfigurationSelection;

public class RemoveConfigOrCategoryAction extends Action {

	private ILaunchConfigurationSelection launchConfigurationSelection;
	private IRunnerModel runnerModel;

	public RemoveConfigOrCategoryAction(ILaunchConfigurationSelection launchConfigurationSelection,
			IRunnerModel runnerModel) {
		this.launchConfigurationSelection = launchConfigurationSelection;
		this.runnerModel = runnerModel;
	}

	@Override
	public void run() {
		Object selectedObject = launchConfigurationSelection.getSelectedObject();

		if (selectedObject instanceof ILaunchConfiguration) {
			removeLaunchConfiguration((ILaunchConfiguration) selectedObject);
		} else if (selectedObject instanceof ILaunchConfigurationCategory) {
			removeLaunchConfigurationCategory((ILaunchConfigurationCategory) selectedObject);
		}
		// else do nothing
	}

	private void removeLaunchConfigurationCategory(ILaunchConfigurationCategory selectedCategory) {
		// TODO BARY: disable this case in context menu
		if (LaunchTreeContentProvider.getDefault().getUncategorizedCategory() == selectedCategory) {
			return;
		}

		boolean confirmed = MessageDialog.openConfirm(RunnerPlugin.getRunnerShell(), Message_removeConfirm, Message_removeCategoryConfirm);
		if (confirmed) {
			runnerModel.removeLaunchConfigurationCategory(selectedCategory);
		}
	}

	private void removeLaunchConfiguration(ILaunchConfiguration selectedConfiguration) {
		boolean confirmed = MessageDialog.openConfirm(RunnerPlugin.getRunnerShell(), Message_removeConfirm, Message_removeConfigurationConfirm);
		if (confirmed) {
			ILaunchConfigurationCategory category = launchConfigurationSelection.getSelectedObjectCategory();
			category.remove(selectedConfiguration);
		}
	}

}
