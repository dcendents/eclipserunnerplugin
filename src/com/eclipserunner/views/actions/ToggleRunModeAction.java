package com.eclipserunner.views.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;

import com.eclipserunner.RunnerPlugin;

/**
 * @author tonyq
 */
public class ToggleRunModeAction extends Action {

	private final String preferenceProperty;

	public ToggleRunModeAction(String preferenceProperty) {
		this.preferenceProperty = preferenceProperty;
		boolean active = getPreferenceStore().getBoolean(preferenceProperty);
		setChecked(active);
	}

	@Override
	public void run() {
		getPreferenceStore().setValue(preferenceProperty, isChecked());
	}

	private IPreferenceStore getPreferenceStore() {
		return RunnerPlugin.getDefault().getPreferenceStore();
	}

}
