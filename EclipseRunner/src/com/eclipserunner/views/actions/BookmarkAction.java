package com.eclipserunner.views.actions;

import java.util.List;

import org.eclipse.jface.action.Action;

import com.eclipserunner.model.IBookmarkable;
import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.ILaunchTypeNode;
import com.eclipserunner.model.INodeSelection;

/**
 * Bookmark launch configuration.
 *
 * @author bary, vachacz
 */
public class BookmarkAction extends Action {

	private boolean state;
	private INodeSelection selection;

	public BookmarkAction(INodeSelection launchConfigurationSelection, boolean state) {
		this.selection = launchConfigurationSelection;
		this.state = state;
	}

	@Override
	public void run() {
		if (selection.ofSameNodeType()) {
			if (selection.firstElementHasType(ILaunchNode.class)) {
				updateBookmark(selection.getSelectedLaunchNodes());
			}
			else if (selection.firstElementHasType(ILaunchTypeNode.class)) {
				updateBookmark(selection.getSelectedLaunchTypeNodes());
			}
			else if (selection.firstElementHasType(ICategoryNode.class)) {
				updateBookmark(selection.getSelectedCategoryNodes());
			}
		}
	}

	private <T extends IBookmarkable> void updateBookmark(List<T> bookmarkables) {
		for (IBookmarkable bookmarkable : bookmarkables) {
			bookmarkable.setBookmarked(state);
		}
	}

}
