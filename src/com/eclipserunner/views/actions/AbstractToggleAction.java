package com.eclipserunner.views.actions;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Abstract toggle action.
 * 
 * @author vachacz
 */
@SuppressWarnings("restriction")
public class AbstractToggleAction extends Action {

	protected IPreferenceStore getPreferenceStore() {
		return JavaPlugin.getDefault().getPreferenceStore();
	}

}
