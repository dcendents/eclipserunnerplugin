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
		
		if (getPreferenceStore().contains(RunnerPluginPrererenceConstants.TREE_MODE)) {
			String treeMode = getPreferenceStore().getDefaultString(RunnerPluginPrererenceConstants.TREE_MODE);
			if (mode.toString().equals(treeMode)) {
				setChecked(true);
			} else {
				setChecked(false);
			}
		} else {
			
		}
	}

	@Override
	public void run() {
		getPreferenceStore().setValue(RunnerPluginPrererenceConstants.TREE_MODE, mode.toString());
		
		runnerView.setTreeMode(this.mode);
		runnerView.refresh();
	}
	
}
