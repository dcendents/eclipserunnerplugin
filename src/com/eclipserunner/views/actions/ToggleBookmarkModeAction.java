package com.eclipserunner.views.actions;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.action.Action;

import com.eclipserunner.RunnerPluginPrererenceConstants;
import com.eclipserunner.views.IRunnerView;

@SuppressWarnings("restriction")
public class ToggleBookmarkModeAction extends Action {

	private final IRunnerView runnerView;

	public ToggleBookmarkModeAction(IRunnerView runnerView) {
		this.runnerView = runnerView;

		boolean checked = JavaPlugin.getDefault().getPreferenceStore().getBoolean(RunnerPluginPrererenceConstants.BOOKMARK_FILTER_ENABLE);
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
			JavaPlugin.getDefault().getPreferenceStore().setValue(RunnerPluginPrererenceConstants.BOOKMARK_FILTER_ENABLE, checked);
		}
	}

}
