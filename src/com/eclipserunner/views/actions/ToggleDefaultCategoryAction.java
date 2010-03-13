package com.eclipserunner.views.actions;

import org.eclipse.jface.action.Action;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.RunnerPluginPrererenceConstants;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.views.IRunnerView;

public class ToggleDefaultCategoryAction extends Action {

	private final IRunnerView runnerView;
	private final IRunnerModel runnerModel;

	public ToggleDefaultCategoryAction(IRunnerView runnerView, IRunnerModel runnerModel) {
		this.runnerView = runnerView;
		this.runnerModel = runnerModel;
		
		setChecked(RunnerPlugin.getDefault().getPreferenceStore().contains(
				RunnerPluginPrererenceConstants.DEFAULT_CATEGORY_VISIBLE));
	}

	@Override
	public void run() {
		RunnerPlugin.getDefault().getPreferenceStore().setValue(
				RunnerPluginPrererenceConstants.DEFAULT_CATEGORY_VISIBLE, isChecked());
		
		runnerModel.setDefaultCategoryVisible(isChecked());
		
		runnerView.refresh();
	}
	
}
