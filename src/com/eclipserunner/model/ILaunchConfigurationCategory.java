package com.eclipserunner.model;

import java.util.Set;


/**
 * Launch configuration category interface.
 * 
 * @author vachacz
 */
public interface ILaunchConfigurationCategory {

	String getName();
	void setName(String name);

	Set<ILaunchConfigurationNode> getLaunchConfigurationNodes();

	void add(ILaunchConfigurationNode launchConfigurationNode);
	void remove(ILaunchConfigurationNode launchConfigurationNode);

	void addCategoryChangeListener(ICategoryChangeListener listener);
	void removeCategoryChangeListener(ICategoryChangeListener listener);

	void setBookmarked(boolean state);

	boolean contains(ILaunchConfigurationNode launchConfigurationNode);
	boolean isEmpty();
	int size();

}
