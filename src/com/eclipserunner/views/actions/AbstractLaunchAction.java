package com.eclipserunner.views.actions;

import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationManager;
import org.eclipse.jface.action.Action;

/**
 * Abstract action depending on launch group id
 * 
 * @author vachacz
 */
@SuppressWarnings("restriction")
public abstract class AbstractLaunchAction extends Action {

	private String launchGroupId;

	public AbstractLaunchAction(String launchGroupId) {
		this.launchGroupId = launchGroupId;
	}

	public String getLaunchGroupId() {
		return this.launchGroupId;
	}

	protected LaunchConfigurationManager getLaunchConfigurationManager() {
		return DebugUIPlugin.getDefault().getLaunchConfigurationManager();
	}

}
