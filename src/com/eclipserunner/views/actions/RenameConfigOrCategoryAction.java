package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_error;
import static com.eclipserunner.Messages.Message_errorLaunchConfigurationAlreadyExists;
import static com.eclipserunner.Messages.Message_rename;
import static com.eclipserunner.Messages.Message_renameCategory;
import static com.eclipserunner.Messages.Message_renameLaunchConfiguration;
import static com.eclipserunner.RunnerPlugin.getRunnerShell;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationSelection;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.RunnerModel;
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
		if (RunnerModel.getDefault().getUncategorizedCategory() == category) {
			return;
		}

		InputDialog dialog = openRenameDialog(Message_rename, Message_renameCategory, category.getName());
		if (dialog.getReturnCode() == Window.OK) {
			category.setName(dialog.getValue());
		}
	}

	private void renameLaunchConfiguration(ILaunchConfiguration launchConfiguration) {
		try {
			InputDialog dialog = openRenameDialog(Message_rename, Message_renameLaunchConfiguration, launchConfiguration.getName());
			if (dialog.getReturnCode() == Window.OK) {

				String newName = dialog.getValue().trim();
				if (isExistingLaunchConfigurationName(newName)) {
					MessageDialog.openError(
							getRunnerShell(), Message_error, Message_errorLaunchConfigurationAlreadyExists
					);
				} else {
					ILaunchConfigurationWorkingCopy workingCopy = launchConfiguration.getWorkingCopy();
					workingCopy.rename(newName);
					workingCopy.doSave();
				}
			}
		} catch (CoreException e) {
			MessageDialog.openError(getRunnerShell(), Message_error, e.getMessage());
		}
	}

	private InputDialog openRenameDialog(String title, String message, String initialValue) {
		InputDialog dialog = new InputDialog(getRunnerShell(), title, message, initialValue, new NotEmptyValidator());
		dialog.open();
		return dialog;
	}

	private boolean isExistingLaunchConfigurationName(String name) throws CoreException {
		return DebugPlugin.getDefault().getLaunchManager().isExistingLaunchConfigurationName(name);
	}
}
