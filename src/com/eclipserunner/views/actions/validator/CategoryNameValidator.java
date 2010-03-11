package com.eclipserunner.views.actions.validator;

import static com.eclipserunner.Messages.Message_catogoryNameNotValid;

import org.eclipse.jface.dialogs.IInputValidator;

public class CategoryNameValidator implements IInputValidator {

	public String isValid(String input) {
		String name = input.trim();
		if (name.length() == 0) {
			return Message_catogoryNameNotValid;
		} 
		return null;
	}

}
