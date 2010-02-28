package com.eclipserunner.views;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author vachacz
 */
public interface ILaunchConfigurationSelection {

	boolean isLaunchConfigurationSelected();
	ILaunchConfiguration getSelectedLaunchConfiguration();

}
