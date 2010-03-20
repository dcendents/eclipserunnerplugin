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
			ILaunchNode launchNode = launchConfigurationSelection.getSelectedLaunchNode();
			launchNode.setBookmarked(!launchNode.isBookmarked());
		}
		if (launchConfigurationSelection.isCategoryNodeSelected()) {
			ICategoryNode categoryNode = launchConfigurationSelection.getSelectedCategoryNode();

			boolean allLaunchNodesBookmarked = areAllLaunchNodesBookmarked(categoryNode);
			if (allLaunchNodesBookmarked) {
				categoryNode.setBookmarked(false);
			} else {
				categoryNode.setBookmarked(true);
			}
		}
	}

	private boolean areAllLaunchNodesBookmarked(ICategoryNode categoryNode) {
		boolean allNodesSelected = true;
		for (ILaunchNode launchNode : categoryNode.getLaunchNodes()) {
			if (!launchNode.isBookmarked()) {
				allNodesSelected = false;
			}
		}
		return allNodesSelected;
	}

}
