package com.eclipserunner.views.actions;

import com.eclipserunner.PrererenceConstants;
import com.eclipserunner.views.IRunnerView;
import com.eclipserunner.views.TreeMode;

/**
 * @author vachacz
 */
public class ToggleTreeModeAction extends AbstractToggleAction {

	private final IRunnerView runnerView;
	private final TreeMode mode;

	public ToggleTreeModeAction(IRunnerView runnerView, TreeMode mode) {
		this.mode = mode;
		this.runnerView = runnerView;

		String treeMode = getPreferenceStore().getString(PrererenceConstants.TREE_MODE);
		if (mode.toString().equals(treeMode)) {
			setChecked(true);
		} else {
			setChecked(false);
		}
	}

	@Override
	public void run() {
		// TODO LWA implement like toggle actions
		getPreferenceStore().setValue(PrererenceConstants.TREE_MODE, mode.toString());
		runnerView.setTreeMode(mode);
	}

}
