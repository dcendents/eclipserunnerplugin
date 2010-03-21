package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_removeConfirmPrompt;
import static com.eclipserunner.Messages.Message_removeConfirmTitle;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.ILaunchTypeNode;
import com.eclipserunner.model.INodeSelection;
import com.eclipserunner.model.IRunnerModel;

/**
 * @author vachacz, bary
 */
public class RemoveConfigOrCategoryAction extends Action {

	private INodeSelection nodeSelection;
	private IRunnerModel runnerModel;

	public RemoveConfigOrCategoryAction(INodeSelection launchConfigurationSelection, IRunnerModel runnerModel) {
		this.nodeSelection = launchConfigurationSelection;
		this.runnerModel = runnerModel;
	}

	@Override
	public void run() {
		if (nodeSelection.isSameTypeNodeSelection()) {
			if (nodeSelection.isLaunchNodeSelected()) {
				removeLaunchNodes(nodeSelection.getSelectedLaunchNodes());
			}
			else if (nodeSelection.isLaunchTypeNodeSelected()) {
				removeLaunchTypeNodes(nodeSelection.getSelectedLaunchTypeNodes());
			}
			else if (nodeSelection.isCategoryNodeSelected()) {
				removeCategoryNodes(nodeSelection.getSelectedCategoryNodes());
			}
		}
	}

	private void removeLaunchNodes(List<ILaunchNode> launchNodes) {
		boolean confirmed = MessageDialog.openConfirm(RunnerPlugin.getRunnerShell(), Message_removeConfirmTitle, Message_removeConfirmPrompt);
		if (confirmed) {
			for (ILaunchNode launchNode : launchNodes) {
				runnerModel.removeLaunchNode(launchNode);
			}
		}
	}

	private void removeLaunchTypeNodes(List<ILaunchTypeNode> launchTypeNodes) {
		boolean confirmed = MessageDialog.openConfirm(RunnerPlugin.getRunnerShell(), Message_removeConfirmTitle, Message_removeConfirmPrompt);
		if (confirmed) {
			for (ILaunchTypeNode launchTypeNode : launchTypeNodes) {
				for (ILaunchNode launchNode : launchTypeNode.getLaunchNodes()) {
					runnerModel.removeLaunchNode(launchNode);
				}
			}
		}
	}

	private void removeCategoryNodes(List<ICategoryNode> categoryNodes) {
		boolean confirmed = MessageDialog.openConfirm(RunnerPlugin.getRunnerShell(), Message_removeConfirmTitle, Message_removeConfirmPrompt);
		if (confirmed) {
			for (ICategoryNode categoryNode : categoryNodes) {
				runnerModel.removeCategoryNode(categoryNode);
			}
		}
	}

}
