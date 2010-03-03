package com.eclipserunner.views.actions.validator;

import static com.eclipserunner.Messages.Message_catogoryNameNotValid;

import org.eclipse.jface.dialogs.IInputValidator;

public class NotEmptyValidator implements IInputValidator {
	
	public String isValid(String input) {
		if (input == null || input.length() == 0) {
			return Message_catogoryNameNotValid;
		}
		return null;
	}
	
}