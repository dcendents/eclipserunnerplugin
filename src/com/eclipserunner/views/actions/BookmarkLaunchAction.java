package com.eclipserunner.views.actions;

import org.eclipse.jface.action.Action;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeSelection;

/**
 * Bookmark launch configuration.
 * 
 * @author bary
 */
public class BookmarkLaunchAction extends Action {

	private INodeSelection launchConfigurationSelection;

	public BookmarkLaunchAction(INodeSelection launchConfigurationSelection) {
		this.launchConfigurationSelection = launchConfigurationSelection;
	}

	@Override
	public void run() {
		if (launchConfigurationSelection.isLaunchNodeSelected()) {
			ILaunchNode launchConfiguration = launchConfigurationSelection.getSelectedLaunchNode();
			launchConfiguration.setBookmarked(true);
		}
		if (launchConfigurationSelection.isCategoryNodeSelected()) {
			ICategoryNode launchConfigurationCategory = launchConfigurationSelection.getSelectedCategoryNode();
			launchConfigurationCategory.setBookmarked(true);
		}
	}

}
