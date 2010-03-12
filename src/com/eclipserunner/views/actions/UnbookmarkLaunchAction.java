package com.eclipserunner.views.actions;

import org.eclipse.jface.action.Action;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationNode;
import com.eclipserunner.model.ILaunchConfigurationSelection;

/**
 * Unbookmark launch configuration.
 * 
 * @author bary
 */
public class UnbookmarkLaunchAction extends Action {

	private ILaunchConfigurationSelection launchConfigurationSelection;

	public UnbookmarkLaunchAction(ILaunchConfigurationSelection launchConfigurationSelection) {
		this.launchConfigurationSelection = launchConfigurationSelection;
	}

	@Override
	public void run() {
		if (launchConfigurationSelection.isLaunchConfigurationNodeSelected()) {
			ILaunchConfigurationNode launchConfiguration = launchConfigurationSelection.getSelectedLaunchConfigurationNode();
			launchConfiguration.setBookmarked(false);
		}
		if (launchConfigurationSelection.isLaunchConfigurationCategorySelected()) {
			ILaunchConfigurationCategory launchConfigurationCategory = launchConfigurationSelection.getSelectedLaunchConfigurationCategory();
			launchConfigurationCategory.unbookmarkAll();
		}
	}

}
