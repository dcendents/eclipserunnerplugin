package com.eclipserunner.model;

import org.eclipse.debug.core.ILaunchConfiguration;


/**
 * @author vachacz
 */
public interface ILaunchConfigurationSelection {

	boolean isLaunchConfigurationSelected();
	boolean isLaunchConfigurationCategorySelected();
	Object getSelectedObject();
	ILaunchConfiguration getSelectedLaunchConfiguration();
	ILaunchConfigurationCategory getSelectedLaunchConfigurationCategory();

}
