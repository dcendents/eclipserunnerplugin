package com.eclipserunner.views.actions;

import java.util.List;

import org.eclipse.jface.action.Action;

import com.eclipserunner.model.IBookmarkable;
import com.eclipserunner.model.INodeSelection;

/**
 * Bookmark launch configuration.
 *
 * @author bary, vachacz
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
		if (nodeSelection.isSelectionOfOneClass()) {
			if (nodeSelection.isLaunchNodeSelected()) {
				updateBookmark(nodeSelection.getSelectedLaunchNodes());
			}
			else if (nodeSelection.isLaunchTypeNodeSelected()) {
				updateBookmark(nodeSelection.getSelectedLaunchTypeNodes());
			}
			else if (nodeSelection.isCategoryNodeSelected()) {
				updateBookmark(nodeSelection.getSelectedCategoryNodes());
			}
		}
	}

	private <T extends IBookmarkable> void updateBookmark(List<T> bookmarkables) {
		for (IBookmarkable bookmarkable : bookmarkables) {
			bookmarkable.setBookmarked(state);
		}
	}

}
