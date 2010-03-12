package com.eclipserunner.views.validators;

import static com.eclipserunner.Messages.Message_catogoryNameNotValid;
import static com.eclipserunner.Messages.Message_errorLaunchConfigurationAlreadyExists;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.dialogs.IInputValidator;

public class LaunchConfigurationNameValidator implements IInputValidator {
	
	private ILaunchManager launchManager = null;
	private String initialValue = null;
	
	public LaunchConfigurationNameValidator(String initialName) {
		this.launchManager = DebugPlugin.getDefault().getLaunchManager();
		this.initialValue = initialName;
	}

	// FOR TESTS ONLY
	protected LaunchConfigurationNameValidator(String initialName, ILaunchManager launchManager) {
		this.launchManager = launchManager;
		this.initialValue = initialName;
	}
	
	public String isValid(String input) {
		String name = input.trim();
		
		if (initialValue.equals(input)) {
			return null;
		}
		else if (name.length() == 0) {
			return Message_catogoryNameNotValid;
		}
		else if (isExistingLaunchConfigurationName(name)) {
			return Message_errorLaunchConfigurationAlreadyExists;
		}
		return null;
	}
	
	private boolean isExistingLaunchConfigurationName(String name) {
		try {
			return launchManager.isExistingLaunchConfigurationName(name);
		} catch (CoreException e) {
			return false;
		}
	}
}