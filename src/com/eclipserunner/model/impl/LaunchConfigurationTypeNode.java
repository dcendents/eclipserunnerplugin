package com.eclipserunner.model.impl;

import org.eclipse.debug.core.ILaunchConfigurationType;

import com.eclipserunner.model.IActionEnablement;
import com.eclipserunner.model.ILaunchConfigurationCategory;

public class LaunchConfigurationTypeNode implements IActionEnablement {

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

	public boolean isRemovable() {
		return false;
	}

	public boolean isRenamable() {
		return false;
	}
	
}
