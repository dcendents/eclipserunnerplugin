package com.eclipserunner.model;

import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * Launch configuration category interface.
 * 
 * @author vachacz
 */
public interface ILaunchConfigurationCategory {

	Set<ILaunchConfiguration> getLaunchConfigurationSet();

	void add(ILaunchConfiguration launchConfiguration);
	void remove(ILaunchConfiguration launchConfiguration);

	void addCategoryChangeListener(ICategoryChangeListener listener);
	void removeCategoryChangeListener(ICategoryChangeListener listener);

	boolean contains(ILaunchConfiguration configuration);

	boolean isEmpty();

	String getName();
	void setName(String name);

}
