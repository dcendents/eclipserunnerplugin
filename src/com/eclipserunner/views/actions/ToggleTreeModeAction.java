package com.eclipserunner.views.actions;

import org.eclipse.jface.action.Action;

import com.eclipserunner.views.IRunnerView;
import com.eclipserunner.views.TreeMode;

public class ToggleTreeModeAction extends Action {

	private final IRunnerView runnerView;
	private final TreeMode mode;

	public ToggleTreeModeAction(IRunnerView runnerView, TreeMode mode, boolean enabled) {
		this.mode = mode;
		this.runnerView = runnerView;
		setChecked(enabled);
	}

	@Override
	public void run() {
		runnerView.setTreeMode(this.mode);
	}
	
}
