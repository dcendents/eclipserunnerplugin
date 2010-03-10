package com.eclipserunner.model;

/**
 * @author vachacz
 */
public interface ILaunchConfigurationSelection {

	boolean isLaunchConfigurationNodeSelected();
	boolean isLaunchConfigurationCategorySelected();
	
	Object getSelectedObject();
	
	ILaunchConfigurationNode getSelectedLaunchConfigurationNode();
	ILaunchConfigurationCategory getSelectedLaunchConfigurationCategory();

}
