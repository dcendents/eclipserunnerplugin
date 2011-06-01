package com.eclipserunner.model;

import java.util.List;

/**
 * @author vachacz
 */
public interface INodeSelection {

	boolean ofSameNodeType();
	boolean ofSingleNode();

	boolean firstElementHasType(Class<?> clazz);
	
	List<ILaunchNode> getSelectedLaunchNodes();
	List<ILaunchTypeNode> getSelectedLaunchTypeNodes();
	List<ICategoryNode> getSelectedCategoryNodes();

	ILaunchNode getSelectedLaunchNode();
	ICategoryNode getSelectedCategoryNode();

}
