package com.eclipserunner.model;

import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * Launch configuration category interface.
 * 
 * @author vachacz
 */
public interface ILaunchConfigurationCategory {

	String getName();
	void setName(String name);

	Set<ILaunchConfigurationNode> getLaunchConfigurationNodes();

	void add(ILaunchConfiguration launchConfiguration);
	void add(ILaunchConfigurationNode launchConfigurationNode);
	
	void remove(ILaunchConfiguration launchConfiguration);
	void remove(ILaunchConfigurationNode launchConfigurationNode);

	void addCategoryChangeListener(ICategoryChangeListener listener);
	void removeCategoryChangeListener(ICategoryChangeListener listener);

	boolean contains(ILaunchConfigurationNode launchConfigurationNode);
	
	void bookmarkAll();
	void unbookmarkAll();

	boolean isEmpty();
	int size();
	
}
