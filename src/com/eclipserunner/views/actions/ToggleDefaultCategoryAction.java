package com.eclipserunner.views.actions;

import com.eclipserunner.RunnerPluginPrererenceConstants;
import com.eclipserunner.model.IRunnerModel;

/**
 * @author vachacz
 */
public class ToggleDefaultCategoryAction extends AbstractToggleAction {

	private final IRunnerModel runnerModel;

	public ToggleDefaultCategoryAction(IRunnerModel runnerModel) {
		this.runnerModel = runnerModel;

		boolean checked = getPreferenceStore().getBoolean(RunnerPluginPrererenceConstants.DEFAULT_CATEGORY_VISIBLE);
		runnerModel.setDefaultCategoryVisible(checked);
		setChecked(checked);
	}

	@Override
	public void run() {
		getPreferenceStore().setValue(RunnerPluginPrererenceConstants.DEFAULT_CATEGORY_VISIBLE, isChecked());
		runnerModel.setDefaultCategoryVisible(isChecked());
	}

}
