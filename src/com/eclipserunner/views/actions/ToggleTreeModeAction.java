package com.eclipserunner.views.actions;

import com.eclipserunner.RunnerPluginPrererenceConstants;
import com.eclipserunner.views.IRunnerView;
import com.eclipserunner.views.TreeMode;

public class ToggleTreeModeAction extends AbstractToggleAction {

	private final IRunnerView runnerView;
	private final TreeMode mode;

	public ToggleTreeModeAction(IRunnerView runnerView, TreeMode mode) {
		this.mode = mode;
		this.runnerView = runnerView;
		
		String treeMode = getPreferenceStore().getString(RunnerPluginPrererenceConstants.TREE_MODE);
		if (mode.toString().equals(treeMode)) {
			setChecked(true);
		} else {
			setChecked(false);
		}
	}

	@Override
	public void run() {
		getPreferenceStore().setValue(RunnerPluginPrererenceConstants.TREE_MODE, mode.toString());
		runnerView.setTreeMode(mode);
	}
	
}
