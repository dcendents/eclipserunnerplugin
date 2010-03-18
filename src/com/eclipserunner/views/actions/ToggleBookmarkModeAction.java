package com.eclipserunner.views.actions;

import com.eclipserunner.model.RunnerModelProvider;
import com.eclipserunner.views.IRunnerView;

/**
 * Switch bookmark mode.
 *
 * @author bary
 */
public class ToggleBookmarkModeAction extends AbstractToggleAction {

	private final IRunnerView runnerView;

	public ToggleBookmarkModeAction(IRunnerView runnerView) {
		this.runnerView = runnerView;
		setChecked(
			RunnerModelProvider.getInstance().isBookmarkFilterActive()
		);
	}

	@Override
	public void run() {
		if (isChecked()) {
			RunnerModelProvider.getInstance().useBookmarkFilter(true);
		} else {
			RunnerModelProvider.getInstance().useBookmarkFilter(false);
		}
		runnerView.refresh();
	}

}
