package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_error;
import static com.eclipserunner.Messages.Message_rename;
import static com.eclipserunner.Messages.Message_renameCategory;
import static com.eclipserunner.Messages.Message_renameLaunchConfiguration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeSelection;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.impl.CategoryNode;
import com.eclipserunner.model.impl.LaunchNode;
import com.eclipserunner.views.validators.CategoryNameValidator;
import com.eclipserunner.views.validators.LaunchConfigurationNameValidator;

/**
 * @author vachacz, bary
 */
public class RenameConfigOrCategoryAction extends BaseRunnerAction {

	private INodeSelection nodeSelection;

	@SuppressWarnings("unused")
	private IRunnerModel runnerModel;

	public RenameConfigOrCategoryAction(INodeSelection launchConfigurationSelection, IRunnerModel runnerModel) {
		this.nodeSelection = launchConfigurationSelection;
		this.runnerModel = runnerModel;
	}

	@Override
	public void run() {
		if (nodeSelection.ofSingleNode()) {
			if (nodeSelection.firstElementHasType(LaunchNode.class)) {
				renameLaunchNode(nodeSelection.getSelectedLaunchNode());
			}
			else if (nodeSelection.firstElementHasType(CategoryNode.class)) {
				renameCategoryNode(nodeSelection.getSelectedCategoryNode());
			}
		}
	}

	private void renameLaunchNode(ILaunchNode node) {
		try {
			String initialValue = node.getLaunchConfiguration().getName();
			InputDialog dialog = openInputDialog(Message_rename, Message_renameLaunchConfiguration, initialValue, new LaunchConfigurationNameValidator(initialValue));
			if (dialog.getReturnCode() == Window.OK) {
				ILaunchConfigurationWorkingCopy workingCopy = node.getLaunchConfiguration().getWorkingCopy();
				workingCopy.rename(dialog.getValue().trim());
				workingCopy.doSave();
			}
		} catch (CoreException e) {
			openErrorDialog(Message_error, e.getMessage());
		}
	}

	private void renameCategoryNode(ICategoryNode category) {
		String initialValue = category.getName();
		InputDialog dialog = openInputDialog(Message_rename, Message_renameCategory, initialValue, new CategoryNameValidator(initialValue));
		if (dialog.getReturnCode() == Window.OK) {
			category.setName(dialog.getValue());
		}
	}

}
