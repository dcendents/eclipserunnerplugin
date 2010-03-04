package com.eclipserunner.views;

import org.eclipse.debug.core.ILaunchConfiguration;

import com.eclipserunner.model.LaunchConfigurationCategory;

/**
 * @author vachacz
 */
public interface ILaunchConfigurationSelection {

	public boolean isLaunchConfigurationSelected();
	public Object getSelectedObject();
	public ILaunchConfiguration getSelectedLaunchConfiguration();
	public LaunchConfigurationCategory getSelectedObjectCategory();

}
