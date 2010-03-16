package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_error;
import static com.eclipserunner.Messages.Message_rename;
import static com.eclipserunner.Messages.Message_renameCategory;
import static com.eclipserunner.Messages.Message_renameLaunchConfiguration;
import static com.eclipserunner.RunnerPlugin.getRunnerShell;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeSelection;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.views.validators.LaunchConfigurationNameValidator;

/**
 * @author vachacz
 */
public class RenameConfigOrCategoryAction extends Action {

	private INodeSelection launchConfigurationSelection;

	@SuppressWarnings("unused")
	private IRunnerModel runnerModel;

	public RenameConfigOrCategoryAction(INodeSelection launchConfigurationSelection, IRunnerModel runnerModel) {
		this.launchConfigurationSelection = launchConfigurationSelection;
		this.runnerModel = runnerModel;
	}

	@Override
	public void run() {
		Object selectedObject = launchConfigurationSelection.getSelectedObject();

		if (selectedObject instanceof ILaunchNode) {
			renameLaunchConfiguration((ILaunchNode) selectedObject);
		} else if (selectedObject instanceof ICategoryNode) {
			renameLaunchConfigurationCategory((ICategoryNode) selectedObject);
		}
		// else do nothing
	}

	private void renameLaunchConfigurationCategory(ICategoryNode category) {
		InputDialog dialog = openRenameDialog(Message_rename, Message_renameCategory, category.getName());
		if (dialog.getReturnCode() == Window.OK) {
			category.setName(dialog.getValue());
		}
	}

	private void renameLaunchConfiguration(ILaunchNode node) {
		try {
			InputDialog dialog = openRenameDialog(Message_rename, Message_renameLaunchConfiguration, node.getLaunchConfiguration().getName());
			if (dialog.getReturnCode() == Window.OK) {
				ILaunchConfigurationWorkingCopy workingCopy = node.getLaunchConfiguration().getWorkingCopy();
				workingCopy.rename(dialog.getValue().trim());
				workingCopy.doSave();
			}
		} catch (CoreException e) {
			MessageDialog.openError(getRunnerShell(), Message_error, e.getMessage());
		}
	}

	private InputDialog openRenameDialog(String title, String message, String initialValue) {
		InputDialog dialog = new InputDialog(getRunnerShell(), title, message, initialValue, new LaunchConfigurationNameValidator(initialValue));
		dialog.open();
		return dialog;
	}


}
