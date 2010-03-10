package com.eclipserunner.views.actions;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.action.Action;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationSelection;
import com.eclipserunner.model.RunnerModel;

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
		if (launchConfigurationSelection.isLaunchConfigurationSelected()) {
			ILaunchConfiguration launchConfiguration = launchConfigurationSelection.getSelectedLaunchConfiguration();
			ILaunchConfigurationCategory launchConfigurationCategory = RunnerModel.getDefault().getLaunchConfigurationCategory(launchConfiguration);
			launchConfigurationCategory.bookmark(launchConfiguration);
		}
		if (launchConfigurationSelection.isLaunchConfigurationCategorySelected()) {
			ILaunchConfigurationCategory launchConfigurationCategory = launchConfigurationSelection.getSelectedLaunchConfigurationCategory();
			launchConfigurationCategory.bookmarkAll();
		}
	}

}
