package com.eclipserunner.model;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface IRunnerModel {

	void addLaunchConfiguration(ILaunchConfiguration configuration);
	ILaunchConfigurationCategory addLaunchConfigurationCategory(String name);
	ILaunchConfigurationCategory getLaunchConfigurationCategory(String name);
	void removeCategory(ILaunchConfigurationCategory category);
	ILaunchConfigurationCategory getParentCategory(ILaunchConfiguration launchConfiguration);

	void addChangeListener(IModelChangeListener listener);
	void removeChangeListener(IModelChangeListener listener);

}
