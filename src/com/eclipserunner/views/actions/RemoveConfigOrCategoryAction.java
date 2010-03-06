package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_removeCategoryConfirm;
import static com.eclipserunner.Messages.Message_removeConfigurationConfirm;
import static com.eclipserunner.Messages.Message_removeConfirm;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.LaunchTreeContentProvider;
import com.eclipserunner.views.ILaunchConfigurationSelection;

public class RemoveConfigOrCategoryAction extends Action {

	private ILaunchConfigurationSelection launchConfigurationSelection;
	private LaunchTreeContentProvider launchTreeContentProvider;
	
	public RemoveConfigOrCategoryAction(ILaunchConfigurationSelection launchConfigurationSelection,
			LaunchTreeContentProvider launchTreeContentProvider) {
		this.launchConfigurationSelection = launchConfigurationSelection;
		this.launchTreeContentProvider = launchTreeContentProvider;
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

	private void removeLaunchConfigurationCategory(ILaunchConfigurationCategory selectedObject) {
		boolean confirmed = MessageDialog.openConfirm(RunnerPlugin.getShell(), Message_removeConfirm, Message_removeCategoryConfirm);
		if (confirmed) {
			ILaunchConfigurationCategory categoy = (ILaunchConfigurationCategory) launchConfigurationSelection.getSelectedObject();
			launchTreeContentProvider.removeCategory(categoy);
		}
	}

	private void removeLaunchConfiguration(ILaunchConfiguration selectedObject) {
		boolean confirmed = MessageDialog.openConfirm(RunnerPlugin.getShell(), Message_removeConfirm, Message_removeConfigurationConfirm);
		if (confirmed) {
			ILaunchConfiguration configuration = (ILaunchConfiguration) launchConfigurationSelection.getSelectedObject();
			
			ILaunchConfigurationCategory category = launchConfigurationSelection.getSelectedObjectCategory();
			category.remove(configuration);
		}
	}
	
}
