package com.eclipserunner.views.actions;

import com.eclipserunner.RunnerPluginPrererenceConstants;
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

		boolean checked = getPreferenceStore().getBoolean(RunnerPluginPrererenceConstants.BOOKMARK_FILTER_ENABLE);
		setChecked(checked);
	}

	@Override
	public void run() {
		getPreferenceStore().setValue(RunnerPluginPrererenceConstants.BOOKMARK_FILTER_ENABLE, isChecked());

		// TODO BARY launchers filtering
		// runnerView.
		runnerView.refresh();
	}

}
