package com.eclipserunner.model;

import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface IRunnerModel {

	void addLaunchConfigurationNode(ILaunchConfigurationNode launchConfigurationNode);
	void removeLaunchConfigurationNode(ILaunchConfigurationNode launchConfigurationNode);

	ILaunchConfigurationCategory addLaunchConfigurationCategory(String name);
	void removeLaunchConfigurationCategory(ILaunchConfigurationCategory category);

	ILaunchConfigurationCategory getLaunchConfigurationCategory(String name);
	Set<ILaunchConfigurationCategory> getLaunchConfigurationCategories();

	void addModelChangeListener(IModelChangeListener listener);
	void removeModelChangeListener(IModelChangeListener listener);

	ILaunchConfigurationCategory getDefaultCategory();

	ILaunchConfigurationNode findLaunchConfigurationNodeBy(ILaunchConfiguration configuration);

}
