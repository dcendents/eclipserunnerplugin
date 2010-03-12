package com.eclipserunner.model;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author vachacz
 */
public interface ILaunchConfigurationNode {

	ILaunchConfiguration getLaunchConfiguration();
	ILaunchConfigurationCategory getLaunchConfigurationCategory();

	boolean isBookmarked();
	public void setBookmarked(boolean state);

	void addLaunchConfigurationChangeListener(ILaunchConfigurationChangeListener listener);
	void removeLaunchConfigurationChangeListener(ILaunchConfigurationChangeListener listener);

}
