package com.eclipserunner.model.filters;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeFilter;
import com.eclipserunner.model.RunnerModelProvider;

public class DefaultCategoryFilter implements INodeFilter {

	public boolean filter(ILaunchNode launchNode) {
		return false;
	}

	public boolean filter(ICategoryNode categoryNode) {
		ICategoryNode defaultCategoryNode = RunnerModelProvider.getInstance().getDefaultModel().getDefaultCategoryNode();
		if (defaultCategoryNode.equals(categoryNode)) {
			return true;
		}
		return false;
	}

}
