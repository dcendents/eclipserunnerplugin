package com.eclipserunner.views.actions;

import org.eclipse.jface.action.Action;

import com.eclipserunner.RunnerPlugin;

/**
 * @author tonyq
 */
public class ToggleRunModeAction extends Action {

	private final String preferenceProperty;

	public ToggleRunModeAction(String preferenceProperty) {
		this.preferenceProperty = preferenceProperty;
		boolean active = RunnerPlugin.getDefault().getPreferenceStore().getBoolean(preferenceProperty);
		setChecked(active);
	}

	@Override
	public void run() {
		RunnerPlugin.getDefault().getPreferenceStore().setValue(preferenceProperty, isChecked());
	}

}
