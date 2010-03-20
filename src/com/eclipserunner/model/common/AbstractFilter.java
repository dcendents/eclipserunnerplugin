package com.eclipserunner.model.common;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeFilter;

public abstract class AbstractFilter implements INodeFilter {

	private final String propery;

	public abstract boolean filterWhenActive(ILaunchNode launchNode);
	public abstract boolean filterWhenActive(ICategoryNode categoryNode);

	public AbstractFilter(String propery) {
		this.propery = propery;
	}

	public boolean filter(ILaunchNode launchNode) {
		if (isActive()) {
			return filterWhenActive(launchNode);
		}
		return false;
	}

	public boolean filter(ICategoryNode categoryNode) {
		if (isActive()) {
			return filterWhenActive(categoryNode);
		}
		return false;
	}

	private boolean isActive() {
		return RunnerPlugin.getDefault().getPreferenceStore().getBoolean(propery);
	}

}
