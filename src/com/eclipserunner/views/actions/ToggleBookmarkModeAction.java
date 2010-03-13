package com.eclipserunner.views.actions;

import com.eclipserunner.RunnerPluginPrererenceConstants;
import com.eclipserunner.views.IRunnerView;

public class ToggleBookmarkModeAction extends AbstractToggleAction {

	private final IRunnerView runnerView;

	public ToggleBookmarkModeAction(IRunnerView runnerView) {
		this.runnerView = runnerView;

		boolean checked = getPreferenceStore().getBoolean(RunnerPluginPrererenceConstants.BOOKMARK_FILTER_ENABLE);
		valueChanged(checked, false);
	}

	@Override
	public void run() {
		valueChanged(isChecked(), true);

		// TODO BARY launchers filtering
		// runnerView.
		runnerView.refresh();
	}

	private void valueChanged(final boolean checked, boolean storeProperty) {
		setChecked(checked);
		if (storeProperty) {
			getPreferenceStore().setValue(RunnerPluginPrererenceConstants.BOOKMARK_FILTER_ENABLE, checked);
		}
	}

}
