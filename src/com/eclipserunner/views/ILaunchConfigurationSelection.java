package com.eclipserunner.views;

import org.eclipse.debug.core.ILaunchConfiguration;

import com.eclipserunner.model.ILaunchConfigurationCategory;

/**
 * @author vachacz
 */
public interface ILaunchConfigurationSelection {

	public boolean isLaunchConfigurationSelected();
	public Object getSelectedObject();
	public ILaunchConfiguration getSelectedLaunchConfiguration();
	public ILaunchConfigurationCategory getSelectedObjectCategory();

}
