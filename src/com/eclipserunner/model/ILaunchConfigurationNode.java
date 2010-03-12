package com.eclipserunner.model;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author vachacz
 */
public interface ILaunchConfigurationNode {

	ILaunchConfiguration getLaunchConfiguration();
	void setLaunchConfiguration(ILaunchConfiguration configuration);

	ILaunchConfigurationCategory getLaunchConfigurationCategory();
	void setLaunchConfigurationCategory(ILaunchConfigurationCategory category);

	boolean isBookmarked();
	void setBookmarked(boolean state);

	void addLaunchConfigurationChangeListener(ILaunchConfigurationChangeListener listener);
	void removeLaunchConfigurationChangeListener(ILaunchConfigurationChangeListener listener);

}
