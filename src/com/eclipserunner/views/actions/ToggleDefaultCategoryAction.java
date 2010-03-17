package com.eclipserunner.views.actions;

import com.eclipserunner.RunnerPluginPrererenceConstants;
import com.eclipserunner.model.RunnerModelProvider;

/**
 * @author vachacz
 */
public class ToggleDefaultCategoryAction extends AbstractToggleAction {

	public ToggleDefaultCategoryAction() {

		// TODO LWA BARY replace with one RunnerModelProvider getter method
		boolean checked = getPreferenceStore().getBoolean(RunnerPluginPrererenceConstants.DEFAULT_CATEGORY_VISIBLE);
		RunnerModelProvider.getInstance().useDefaultCategoryFilter(checked);

		setChecked(checked);
	}

	@Override
	public void run() {
		if (isChecked()) {
			RunnerModelProvider.getInstance().useDefaultCategoryFilter(true);
		} else {
			RunnerModelProvider.getInstance().useDefaultCategoryFilter(false);
		}
	}

}
