package com.eclipserunner.model.filters;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.common.AbstractFilter;

public class BookmarkFilter extends AbstractFilter {

	public BookmarkFilter(String propery) {
		super(propery);
	}

	@Override
	public boolean filterWhenActive(ILaunchNode launchNode) {
		return !launchNode.isBookmarked();
	}

	@Override
	public boolean filterWhenActive(ICategoryNode categoryNode) {
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
