package com.eclipserunner.views.actions;

import org.eclipse.jface.action.Action;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeSelection;

/**
 * Unbookmark launch configuration.
 * 
 * @author bary
 */
public class UnbookmarkLaunchAction extends Action {

	private INodeSelection launchConfigurationSelection;

	public UnbookmarkLaunchAction(INodeSelection launchConfigurationSelection) {
		this.launchConfigurationSelection = launchConfigurationSelection;
	}

	@Override
	public void run() {
		if (launchConfigurationSelection.isLaunchNodeSelected()) {
			ILaunchNode launchConfiguration = launchConfigurationSelection.getSelectedLaunchNode();
			launchConfiguration.setBookmarked(false);
		}
		if (launchConfigurationSelection.isCategoryNodeSelected()) {
			ICategoryNode launchConfigurationCategory = launchConfigurationSelection.getSelectedCategoryNode();
			launchConfigurationCategory.setBookmarked(false);
		}
	}

}
