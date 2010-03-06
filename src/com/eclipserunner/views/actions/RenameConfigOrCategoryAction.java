package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_rename;
import static com.eclipserunner.Messages.Message_renameCategory;
import static com.eclipserunner.Messages.Message_renameLaunchConfiguration;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.LaunchTreeContentProvider;
import com.eclipserunner.views.ILaunchConfigurationSelection;
import com.eclipserunner.views.actions.validator.NotEmptyValidator;

public class RenameConfigOrCategoryAction extends Action {

	private ILaunchConfigurationSelection launchConfigurationSelection;

	@SuppressWarnings("unused")
	private IRunnerModel runnerModel;

	public RenameConfigOrCategoryAction(ILaunchConfigurationSelection launchConfigurationSelection, IRunnerModel runnerModel) {
		this.launchConfigurationSelection = launchConfigurationSelection;
		this.runnerModel = runnerModel;
	}

	@Override
	public void run() {
		Object selectedObject = launchConfigurationSelection.getSelectedObject();

		if (selectedObject instanceof ILaunchConfiguration) {
			renameLaunchConfiguration((ILaunchConfiguration) selectedObject);
		} else if (selectedObject instanceof ILaunchConfigurationCategory) {
			renameLaunchConfigurationCategory((ILaunchConfigurationCategory) selectedObject);
		}
		// else do nothing
	}

	private void renameLaunchConfigurationCategory(ILaunchConfigurationCategory category) {
		// TODO BARY: disable this case in context menu
		if (LaunchTreeContentProvider.getDefault().getUncategorizedCategory() == category) {
			return;
		}

		InputDialog dialog = openRenameDialog(Message_rename, Message_renameCategory, category.getName());
		category.setName(dialog.getValue());
	}

	private void renameLaunchConfiguration(ILaunchConfiguration launchConfiguration) {
		@SuppressWarnings("unused")
		InputDialog dialog = openRenameDialog(Message_rename, Message_renameLaunchConfiguration, launchConfiguration.getName());

		// TODO LWA missing setName in ILaunchConfiguration interface ...
		// selectedObject.setName(dialog.getValue());
		// launchTreeContentProvider.fireModelChangedEvent();
	}

	private InputDialog openRenameDialog(String title, String message, String initialValue) {
		InputDialog dialog = new InputDialog(RunnerPlugin.getShell(), title, message, initialValue, new NotEmptyValidator());
		dialog.open();
		return dialog;
	}

}
