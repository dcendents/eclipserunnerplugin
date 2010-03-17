package com.eclipserunner.model;

import java.util.Collection;

/**
 * Launch configuration category interface.
 *
 * @author vachacz
 */
public interface ICategoryNode {

	String getName();
	void setName(String name);

	Collection<ILaunchNode> getLaunchNodes();

	void add(ILaunchNode launchNode);
	void remove(ILaunchNode launchNode);

	void addCategoryNodeChangeListener(ICategoryNodeChangeListener categoryNodeChangeListener);
	void removeCategoryNodeChangeListener(ICategoryNodeChangeListener categoryNodeChangeListener);

	void setBookmarked(boolean state);

	boolean contains(ILaunchNode launchNode);
	boolean isEmpty();
	int size();

}
