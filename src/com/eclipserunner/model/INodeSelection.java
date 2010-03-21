package com.eclipserunner.model;

/**
 * @author vachacz
 */
public interface INodeSelection {

	boolean isLaunchNodeSelected();
	boolean isLaunchTypeNodeSelected();
	boolean isCategoryNodeSelected();

	Object getSelectedObject();

	ILaunchNode getSelectedLaunchNode();
	ILaunchTypeNode getSelectedLaunchTypeNode();
	ICategoryNode getSelectedCategoryNode();

}
