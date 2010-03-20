package com.eclipserunner.model.filters;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.RunnerModelProvider;
import com.eclipserunner.model.common.AbstractFilter;

public class DefaultCategoryFilter extends AbstractFilter {

	public DefaultCategoryFilter(String propery) {
		super(propery);
	}

	@Override
	public boolean filterWhenActive(ILaunchNode launchNode) {
		return false;
	}

	@Override
	public boolean filterWhenActive(ICategoryNode categoryNode) {
		ICategoryNode defaultCategoryNode = RunnerModelProvider.getInstance().getDefaultModel().getDefaultCategoryNode();
		if (defaultCategoryNode.equals(categoryNode)) {
			return true;
		}
		return false;
	}

}
