package com.eclipserunner.model;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author vachacz
 */
public class LaunchConfigurationNode implements ILaunchConfigurationNode {

	private ILaunchConfigurationCategory launchConfigurationCategory;
	private ILaunchConfiguration launchConfiguration;
	private boolean bookmarked;
	
	
	@Override
	public void bookmark() {
		bookmarked = true;
	}

	@Override
	public void unbookmark() {
		bookmarked = false;
	}
	
	@Override
	public boolean isBookmarked() {
		return bookmarked;
	}
	
	@Override
	public ILaunchConfiguration getLaunchConiguration() {
		return getLaunchConfiguration();
	}

	@Override
	public ILaunchConfigurationCategory getParentCategory() {
		return getLaunchConfigurationCategory();
	}

	public void setLaunchConfigurationCategory(
			ILaunchConfigurationCategory launchConfigurationCategory) {
		this.launchConfigurationCategory = launchConfigurationCategory;
	}

	public ILaunchConfigurationCategory getLaunchConfigurationCategory() {
		return launchConfigurationCategory;
	}

	public void setLaunchConfiguration(ILaunchConfiguration launchConfiguration) {
		this.launchConfiguration = launchConfiguration;
	}

	public ILaunchConfiguration getLaunchConfiguration() {
		return launchConfiguration;
	}

}
