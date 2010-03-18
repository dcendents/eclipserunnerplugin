package com.eclipserunner.model.filters;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeFilter;

// TODO LWA BARY
public class ProjectFilter implements INodeFilter {

	public boolean filter(ILaunchNode launchNode) {
		return false;
	}

	public boolean filter(ICategoryNode categoryNode) {
		return false;
	}

}
