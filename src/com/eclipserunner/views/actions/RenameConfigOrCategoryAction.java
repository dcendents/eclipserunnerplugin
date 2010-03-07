package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_error;
import static com.eclipserunner.Messages.Message_errorLaunchConfigurationAlreadyExists;
import static com.eclipserunner.Messages.Message_rename;
import static com.eclipserunner.Messages.Message_renameCategory;
import static com.eclipserunner.Messages.Message_renameLaunchConfiguration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

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
		if (dialog.getReturnCode() == Window.OK) {
			category.setName(dialog.getValue());
		}
	}

	private void renameLaunchConfiguration(ILaunchConfiguration launchConfiguration) {
		try {
			InputDialog dialog = openRenameDialog(Message_rename, Message_renameLaunchConfiguration, launchConfiguration.getName());
			if (dialog.getReturnCode() == Window.OK) {

				String newName = dialog.getValue().trim();
				if (launchConfigurationNameAlreadyExists(newName)) {
					MessageDialog.openError(
							RunnerPlugin.getShell(), Message_error, Message_errorLaunchConfigurationAlreadyExists
					);
					return;
				}

				ILaunchConfigurationWorkingCopy launchConfigurationWorkingCopy = launchConfiguration.getWorkingCopy();
				launchConfigurationWorkingCopy.rename(newName);
				launchConfigurationWorkingCopy.doSave();

			}
		} catch (CoreException e) {
			MessageDialog.openError(
					RunnerPlugin.getShell(), Message_error, e.getMessage()
			);
			return;
		}
	}

	private InputDialog openRenameDialog(String title, String message, String initialValue) {
		InputDialog dialog = new InputDialog(RunnerPlugin.getShell(), title, message, initialValue, new NotEmptyValidator());
		dialog.open();
		return dialog;
	}

	private boolean launchConfigurationNameAlreadyExists(String name) throws CoreException {
		for (ILaunchConfiguration tmpLaunchConfiguration : DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations()) {
			if (tmpLaunchConfiguration.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
