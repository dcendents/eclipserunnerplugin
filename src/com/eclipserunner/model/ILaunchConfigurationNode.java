package com.eclipserunner.model;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author vachacz
 */
public interface ILaunchConfigurationNode {

	ILaunchConfiguration getLaunchConfiguration();
	ILaunchConfigurationCategory getLaunchConfigurationCategory();

	void bookmark();
	void unbookmark();
	boolean isBookmarked();

	void addLaunchConfigurationChangeListener(ILaunchConfigurationChangeListener listener);
	void removeLaunchConfigurationChangeListener(ILaunchConfigurationChangeListener listener);

}
