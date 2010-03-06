package com.eclipserunner.model;

import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * Launch configuration category interface.
 * 
 * @author vachacz
 */
public interface ILaunchConfigurationCategory {

	public Set<ILaunchConfiguration> getLaunchConfigurationSet();
	
	public void add(ILaunchConfiguration launchConfiguration);
	public void remove(ILaunchConfiguration launchConfiguration);
	
	public void addCategoryChangeListener(ICategoryChangeListener listener);
	public void removeCategoryChangeListener(ICategoryChangeListener listener);

	public boolean contains(ILaunchConfiguration configuration);

	public boolean isEmpty();

	public String getName();
	public void setName(String name);
	
}
