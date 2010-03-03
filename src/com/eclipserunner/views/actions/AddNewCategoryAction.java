package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_categoryDialogMessage;
import static com.eclipserunner.Messages.Message_categoryDialogTitle;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.LaunchTreeContentProvider;
import com.eclipserunner.views.actions.validator.NotEmptyValidator;

/**
 * Action creates new empty category in plugin model.
 * 
 * @author vachacz
 */
public class AddNewCategoryAction extends Action {

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
