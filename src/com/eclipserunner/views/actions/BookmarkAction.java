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
	private INodeSelection nodeSelection;

	public BookmarkAction(INodeSelection launchConfigurationSelection, boolean state) {
		this.nodeSelection = launchConfigurationSelection;
		this.state = state;
	}

	@Override
	public void run() {
		if (nodeSelection.isSameTypeNodeSelection()) {
			if (nodeSelection.isLaunchNodeSelected()) {
				for (ILaunchNode launchNode : nodeSelection.getSelectedLaunchNodes()) {
					launchNode.setBookmarked(state);
				}
			}
			else if (nodeSelection.isLaunchTypeNodeSelected()) {
				for (ILaunchTypeNode launchTypeNode : nodeSelection.getSelectedLaunchTypeNodes()) {
					launchTypeNode.setBookmarked(state);
				}
			}
			else if (nodeSelection.isCategoryNodeSelected()) {
				for (ICategoryNode categoryNode : nodeSelection.getSelectedCategoryNodes()) {
					categoryNode.setBookmarked(state);
				}
			}
		}
	}

}
