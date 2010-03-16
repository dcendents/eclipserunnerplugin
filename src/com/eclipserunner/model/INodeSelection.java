package com.eclipserunner.model;

/**
 * @author vachacz
 */
public interface INodeSelection {

	boolean isLaunchNodeSelected();
	boolean isCategoryNodeSelected();
	
	Object getSelectedObject();
	
	ILaunchNode getSelectedLaunchNode();
	ICategoryNode getSelectedCategoryNode();

}
