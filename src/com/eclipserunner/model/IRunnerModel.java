package com.eclipserunner.model;

import java.util.Collection;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface IRunnerModel {

	void addLaunchConfigurationNode(ILaunchConfigurationNode launchConfigurationNode);
	void removeLaunchConfigurationNode(ILaunchConfigurationNode launchConfigurationNode);

	ILaunchConfigurationCategory addLaunchConfigurationCategory(String name);
	void removeLaunchConfigurationCategory(ILaunchConfigurationCategory category);

	ILaunchConfigurationCategory getLaunchConfigurationCategory(String name);
	Collection<ILaunchConfigurationCategory> getLaunchConfigurationCategories();

	void addModelChangeListener(IModelChangeListener listener);
	void removeModelChangeListener(IModelChangeListener listener);

	boolean isDefaultCategoryVisible();
	void setDefaultCategoryVisible(boolean checked);
	ILaunchConfigurationCategory getDefaultCategory();

	ILaunchConfigurationNode findLaunchConfigurationNodeBy(ILaunchConfiguration configuration);

}
