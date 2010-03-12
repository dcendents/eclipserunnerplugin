package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_categoryDialogMessage;
import static com.eclipserunner.Messages.Message_categoryDialogTitle;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.views.validators.CategoryNameValidator;

/**
 * Action creates new empty category in plugin model.
 * 
 * @author vachacz
 */
public class AddNewCategoryAction extends Action {

	private IRunnerModel runnerModel;

	public AddNewCategoryAction(IRunnerModel runnerModel) {
		this.runnerModel = runnerModel;
	}

	@Override
	public void run() {
		InputDialog dialog = new InputDialog(RunnerPlugin.getRunnerShell(), Message_categoryDialogTitle, Message_categoryDialogMessage, "", new CategoryNameValidator());

		dialog.open();
		if (dialog.getReturnCode() == Window.OK) {
			runnerModel.addLaunchConfigurationCategory(dialog.getValue());
		}
	}

}
