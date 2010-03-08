package com.eclipserunner.model;

import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface IRunnerModel {

	void addLaunchConfiguration(ILaunchConfiguration configuration);
	void removeLaunchConfiguration(ILaunchConfiguration configuration);

	ILaunchConfigurationCategory addLaunchConfigurationCategory(String name);
	void removeLaunchConfigurationCategory(ILaunchConfigurationCategory category);

	ILaunchConfigurationCategory getLaunchConfigurationCategory(String name);
	ILaunchConfigurationCategory getLaunchConfigurationCategory(ILaunchConfiguration launchConfiguration);
	Set<ILaunchConfigurationCategory> getLaunchConfigurationCategorySet();

	void addChangeListener(IModelChangeListener listener);
	void removeChangeListener(IModelChangeListener listener);
	
	ILaunchConfigurationCategory getUncategorizedCategory();

}
