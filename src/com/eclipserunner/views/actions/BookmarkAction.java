package com.eclipserunner.views.actions;

import org.eclipse.jface.action.Action;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.ILaunchTypeNode;
import com.eclipserunner.model.INodeSelection;

/**
 * Bookmark launch configuration.
 *
 * @author bary
 */
public class BookmarkAction extends Action {

	private boolean state;
	private INodeSelection launchConfigurationSelection;

	public BookmarkAction(INodeSelection launchConfigurationSelection, boolean state) {
		this.launchConfigurationSelection = launchConfigurationSelection;
		this.state = state;
	}

	@Override
	public void run() {
		if (launchConfigurationSelection.isLaunchNodeSelected()) {
			ILaunchNode launchNode = launchConfigurationSelection.getSelectedLaunchNode();
			launchNode.setBookmarked(state);
		}
		else if (launchConfigurationSelection.isLaunchTypeNodeSelected()) {
			ILaunchTypeNode launchTypeNode = launchConfigurationSelection.getSelectedLaunchTypeNode();
			launchTypeNode.setBookmarked(state);
		}
		else if (launchConfigurationSelection.isCategoryNodeSelected()) {
			ICategoryNode categoryNode = launchConfigurationSelection.getSelectedCategoryNode();
			categoryNode.setBookmarked(state);
		}
	}

}
