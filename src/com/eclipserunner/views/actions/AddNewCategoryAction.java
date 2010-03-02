package com.eclipserunner.views.actions;

import org.eclipse.jface.action.Action;

import com.eclipserunner.model.LaunchTreeContentProvider;

/**
 * Action creates new empty category in plugin model.
 * 
 * @author vachacz
 */
public class AddNewCategoryAction extends Action {

	private LaunchTreeContentProvider launchTreeContentProvider;
	
	public AddNewCategoryAction(LaunchTreeContentProvider launchTreeContentProvider) {
		this.launchTreeContentProvider = launchTreeContentProvider;
	}

	@Override
	public void run() {
		launchTreeContentProvider.addLaunchConfigurationCategory("Default configuration category");
	}
	
}
