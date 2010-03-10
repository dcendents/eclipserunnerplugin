package com.eclipserunner.model;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author vachacz
 */
public interface ILaunchConfigurationNode {

	ILaunchConfiguration getLaunchConiguration();
	ILaunchConfigurationCategory getParentCategory();

	void bookmark();
	void unbookmark();
	
	boolean isBookmarked();
	
}
