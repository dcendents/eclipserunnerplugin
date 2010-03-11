package com.eclipserunner.model;

import org.eclipse.debug.core.ILaunchConfigurationType;

public class LaunchConfigurationTypeNode {

	private ILaunchConfigurationCategory parentCategory;
	private ILaunchConfigurationType type;

	public void setType(ILaunchConfigurationType type) {
		this.type = type;
	}

	public ILaunchConfigurationType getType() {
		return type;
	}

	public void setParentCategory(ILaunchConfigurationCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

	public ILaunchConfigurationCategory getParentCategory() {
		return parentCategory;
	}
	
}
