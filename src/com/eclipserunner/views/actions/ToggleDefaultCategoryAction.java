package com.eclipserunner.views.actions;

import org.eclipse.jface.action.Action;

import com.eclipserunner.views.IRunnerView;

public class ToggleDefaultCategoryAction extends Action {

	private final IRunnerView runnerView;

	public ToggleDefaultCategoryAction(IRunnerView runnerView) {
		this.runnerView = runnerView;
	}

	@Override
	public void run() {
		setChecked(true);
	}
	
}
