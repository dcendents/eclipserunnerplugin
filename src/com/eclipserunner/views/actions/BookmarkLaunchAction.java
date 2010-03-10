package com.eclipserunner.views.actions;

import org.eclipse.jface.action.Action;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationNode;
import com.eclipserunner.model.ILaunchConfigurationSelection;

/**
 * Bookmark launch configuration.
 * 
 * @author bary
 */
public class BookmarkLaunchAction extends Action {

	private ILaunchConfigurationSelection launchConfigurationSelection;

	public BookmarkLaunchAction(ILaunchConfigurationSelection launchConfigurationSelection) {
		this.launchConfigurationSelection = launchConfigurationSelection;
	}

	@Override
	public void run() {
		if (launchConfigurationSelection.isLaunchConfigurationNodeSelected()) {
			ILaunchConfigurationNode launchConfiguration = launchConfigurationSelection.getSelectedLaunchConfigurationNode();
			launchConfiguration.bookmark();
		}
		if (launchConfigurationSelection.isLaunchConfigurationCategorySelected()) {
			ILaunchConfigurationCategory launchConfigurationCategory = launchConfigurationSelection.getSelectedLaunchConfigurationCategory();
			launchConfigurationCategory.bookmarkAll();
		}
	}

}
