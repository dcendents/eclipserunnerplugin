package com.eclipserunner.model;

/**
 * @author vachacz
 */
public interface INodeFilterChain {

	void addFilter(INodeFilter filter);
	void removeFilter(INodeFilter filter);

}
