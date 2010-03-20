package com.eclipserunner.model.filters;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeFilter;

public class BookmarkFilter implements INodeFilter {

	public boolean filter(ILaunchNode launchNode) {
		return !launchNode.isBookmarked();
	}

	public boolean filter(ICategoryNode categoryNode) {
		boolean filterCategoryNode = true;
		for(ILaunchNode launchNode : categoryNode.getLaunchNodes()) {
			if (launchNode.isBookmarked()) {
				filterCategoryNode = false;
				break;
			}
		}
		return filterCategoryNode;
	}

}
