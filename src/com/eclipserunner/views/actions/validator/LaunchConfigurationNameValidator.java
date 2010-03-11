package com.eclipserunner.views.actions.validator;

import static com.eclipserunner.Messages.Message_catogoryNameNotValid;
import static com.eclipserunner.Messages.Message_errorLaunchConfigurationAlreadyExists;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jface.dialogs.IInputValidator;

public class LaunchConfigurationNameValidator implements IInputValidator {
	
	public String isValid(String input) {
		String name = input.trim();
		if (name.length() == 0) {
			return Message_catogoryNameNotValid;
		} 
		else if (isExistingLaunchConfigurationName(name)) {
			return Message_errorLaunchConfigurationAlreadyExists;
		}
		return null;
	}
	
	private boolean isExistingLaunchConfigurationName(String name) {
		try {
			return DebugPlugin.getDefault().getLaunchManager().isExistingLaunchConfigurationName(name);
		} catch (CoreException e) {
			return false;
		}
	}
}