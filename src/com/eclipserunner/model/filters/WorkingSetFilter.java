package com.eclipserunner.model.filters;

import org.eclipse.debug.internal.ui.launchConfigurations.WorkingSetsFilter;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.common.AbstractFilter;

@SuppressWarnings("restriction")
public class WorkingSetFilter extends AbstractFilter {

	public WorkingSetFilter(String propery) {
		super(propery);
	}

	@Override
	public boolean filterWhenActive(ILaunchNode launchNode) {
		return !new WorkingSetsFilter().select(null, null, launchNode.getLaunchConfiguration());
	}

	@Override
	public boolean filterWhenActive(ICategoryNode categoryNode) {
		return false;
	}

}
