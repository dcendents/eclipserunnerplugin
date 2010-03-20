package com.eclipserunner.views.actions;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.views.IRunnerView;

public class ToggleFilterAction extends AbstractToggleAction {

	private final String preferenceProperty;
	private final IRunnerView runnerView;

	public ToggleFilterAction(String preferenceProperty, IRunnerView runnerView) {
		this.preferenceProperty = preferenceProperty;
		this.runnerView = runnerView;

		boolean active = RunnerPlugin.getDefault().getPreferenceStore().getBoolean(preferenceProperty);
		setChecked(active);
	}

	@Override
	public void run() {
		RunnerPlugin.getDefault().getPreferenceStore().setValue(preferenceProperty, isChecked());

		runnerView.refresh();
	}

}
