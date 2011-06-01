package com.eclipserunner.model;

import java.util.List;

/**
 * @author vachacz
 */
public interface INodeSelection {

	boolean ofSameNodeType();
	boolean ofSingleNode();

	boolean firstElementHasType(Class<?> clazz);
	<T> T getFirstElementAs(Class<T> clazz);
	
	List<ILaunchNode> getSelectedLaunchNodes();
	List<ILaunchTypeNode> getSelectedLaunchTypeNodes();
	List<ICategoryNode> getSelectedCategoryNodes();

}
