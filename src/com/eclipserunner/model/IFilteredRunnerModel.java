package com.eclipserunner.model;

import java.util.List;

public interface IFilteredRunnerModel extends IRunnerModel {

	void addFilter(INodeFilter filter);	
	List<INodeFilter> getFilters();
	
}
