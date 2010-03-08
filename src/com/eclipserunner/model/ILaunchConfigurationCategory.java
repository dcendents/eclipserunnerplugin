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

	Set<ILaunchConfiguration> getLaunchConfigurations();

	void add(ILaunchConfiguration launchConfiguration);
	void remove(ILaunchConfiguration launchConfiguration);

	void addCategoryChangeListener(ICategoryChangeListener listener);
	void removeCategoryChangeListener(ICategoryChangeListener listener);

	boolean contains(ILaunchConfiguration configuration);

	boolean isEmpty();
	int size();

}
