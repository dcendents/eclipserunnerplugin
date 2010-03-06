package com.eclipserunner.model;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface IRunnerModel {

	public void addUncategorizedLaunchConfiguration(ILaunchConfiguration configuration);
	public ILaunchConfigurationCategory addLaunchConfigurationCategory(String name);
	public void removeCategory(ILaunchConfigurationCategory categoy);
	public ILaunchConfigurationCategory getParentCategory(ILaunchConfiguration launchConfiguration);
	
	public void addChangeListener(IModelChangeListener listener);
	public void removeChangeListener(IModelChangeListener listener);
	
}
