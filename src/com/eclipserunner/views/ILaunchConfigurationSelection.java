package com.eclipserunner.views;

import org.eclipse.debug.core.ILaunchConfiguration;

import com.eclipserunner.model.ILaunchConfigurationCategory;

/**
 * @author vachacz
 */
public interface ILaunchConfigurationSelection {

	boolean isLaunchConfigurationSelected();
	Object getSelectedObject();
	ILaunchConfiguration getSelectedLaunchConfiguration();
	ILaunchConfigurationCategory getSelectedObjectCategory();

}
