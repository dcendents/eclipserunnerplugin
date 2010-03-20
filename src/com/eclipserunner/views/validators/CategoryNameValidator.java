package com.eclipserunner.views.validators;

import static com.eclipserunner.Messages.Message_errorCategoryAlreadyExists;
import static com.eclipserunner.Messages.Message_errorCatogoryEmptyName;

import org.eclipse.jface.dialogs.IInputValidator;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.RunnerModelProvider;

/**
 * 
 * @author vachacz
 */
public class CategoryNameValidator implements IInputValidator {

	private String initialValue;

	public CategoryNameValidator() {
		this.initialValue = null;
	}

	public CategoryNameValidator(String initialName) {
		this.initialValue = initialName;
	}

	public String isValid(String input) {
		String name = input.trim();

		if (initialValue != null && initialValue.equals(input)) {
			return null;
		}
		else if (name.length() == 0) {
			return Message_errorCatogoryEmptyName;
		}
		else if (isExistingCategoryName(name)) {
			return Message_errorCategoryAlreadyExists;
		}
		return null;
	}

	private boolean isExistingCategoryName(String categoryName) {
		IRunnerModel runnerModel = RunnerModelProvider.getInstance().getDefaultModel();
		for (ICategoryNode categoryNode : runnerModel.getCategoryNodes()) {
			if (categoryNode.getName().equals(categoryName)) {
				return true;
			}
		}
		return false;
	}

}
