package com.eclipserunner.model;

import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface IRunnerModel {

	ILaunchConfigurationCategory addLaunchConfigurationCategory(String name);
	void addLaunchConfigurationNode(ILaunchConfigurationNode launchConfigurationNode);

	void removeLaunchConfiguration(ILaunchConfiguration launchConfiguration);
	void removeLaunchConfigurationNode(ILaunchConfigurationNode launchConfigurationNode);
	void removeLaunchConfigurationCategory(ILaunchConfigurationCategory category);

	ILaunchConfigurationCategory getLaunchConfigurationCategory(String name);
	Set<ILaunchConfigurationCategory> getLaunchConfigurationCategories();

	void addModelChangeListener(IModelChangeListener listener);
	void removeModelChangeListener(IModelChangeListener listener);

	ILaunchConfigurationCategory getUncategorizedCategory();

	ILaunchConfigurationNode findLaunchConfigurationNodeBy(ILaunchConfiguration configuration);

}
