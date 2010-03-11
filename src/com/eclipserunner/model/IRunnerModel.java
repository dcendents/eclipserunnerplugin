package com.eclipserunner.model;

import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface IRunnerModel {

	void addLaunchConfigurationNode(ILaunchConfigurationNode configuration);
	void removeLaunchConfigurationNode(ILaunchConfigurationNode configuration);

	ILaunchConfigurationCategory addLaunchConfigurationCategory(String name);
	void removeLaunchConfigurationCategory(ILaunchConfigurationCategory category);

	ILaunchConfigurationCategory getLaunchConfigurationCategory(String name);
	Set<ILaunchConfigurationCategory> getLaunchConfigurationCategories();

	void addModelChangeListener(IModelChangeListener listener);
	void removeModelChangeListener(IModelChangeListener listener);

	ILaunchConfigurationCategory getUncategorizedCategory();
	void removeLaunchConfiguration(ILaunchConfiguration configuration);

	ILaunchConfigurationNode findLaunchConfigurationNodeBy(ILaunchConfiguration configuration);

}
