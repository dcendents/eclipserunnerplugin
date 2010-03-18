package com.eclipserunner.views.actions;

import com.eclipserunner.model.RunnerModelProvider;
import com.eclipserunner.views.IRunnerView;

/**
 * @author vachacz
 */
public class ToggleDefaultCategoryAction extends AbstractToggleAction {

	private final IRunnerView view;

	public ToggleDefaultCategoryAction(IRunnerView view) {
		this.view = view;
		setChecked(
			RunnerModelProvider.getInstance().isDefaultCategoryFilterActive()
		);
	}

	@Override
	public void run() {
		if (isChecked()) {
			RunnerModelProvider.getInstance().useDefaultCategoryFilter(true);
		} else {
			RunnerModelProvider.getInstance().useDefaultCategoryFilter(false);
		}
		view.refresh();
	}

}
