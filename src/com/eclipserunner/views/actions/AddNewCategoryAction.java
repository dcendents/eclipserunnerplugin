package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_categoryDialogMessage;
import static com.eclipserunner.Messages.Message_categoryDialogTitle;
import static com.eclipserunner.Messages.Message_catogoryNameNotValid;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.LaunchTreeContentProvider;

/**
 * Action creates new empty category in plugin model.
 * 
 * @author vachacz
 */
public class AddNewCategoryAction extends Action {

	class NotEmptyValidator implements IInputValidator {
		public String isValid(String input) {
			if (input == null || input.length() == 0) {
				return Message_catogoryNameNotValid;
			}
			return null;
		}
	}
	
	private LaunchTreeContentProvider launchTreeContentProvider;
	
	public AddNewCategoryAction(LaunchTreeContentProvider launchTreeContentProvider) {
		this.launchTreeContentProvider = launchTreeContentProvider;
	}

	@Override
	public void run() {
		InputDialog dialog = new InputDialog(RunnerPlugin.getShell(), 
			Message_categoryDialogTitle, Message_categoryDialogMessage, "", new NotEmptyValidator());
		
		dialog.open();
		
		launchTreeContentProvider.addLaunchConfigurationCategory(dialog.getValue());
	}
	
}
