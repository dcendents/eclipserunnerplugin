package com.eclipserunner.model;

import java.util.List;

/**
 * @author vachacz
 */
public interface INodeSelection {

	boolean ofSameNodeType();
	boolean ofSingleNode();

	boolean firstNodeHasType(Class<?> clazz);
	<T> T getFirstNodeAs(Class<T> clazz);
	
	List<ILaunchNode> getSelectedLaunchNodes();
	List<ILaunchTypeNode> getSelectedLaunchTypeNodes();
	List<ICategoryNode> getSelectedCategoryNodes();

}
