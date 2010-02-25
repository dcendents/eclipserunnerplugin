package com.eclipserunner.views.actions;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.eclipserunner.views.SampleView;

/**
 * Action responsible for launching selected configuration.
 * 
 * @author bary
 *
 */
public class LaunchConfigurationAction extends Action {

	private SampleView view;
	private String launchGroupId;


	public LaunchConfigurationAction(SampleView view, String launchGroupId) {
		this.view = view;
		this.launchGroupId = launchGroupId;
	}


	public String getLaunchGroupId() {
		return this.launchGroupId;
	}


	@Override
	public void run() {
		ISelection selection = this.view.getViewer().getSelection();
		ILaunchConfiguration config = (ILaunchConfiguration) ((IStructuredSelection)selection).getFirstElement();

		DebugUITools.launch(config, DebugUIPlugin.getDefault().getLaunchConfigurationManager().getLaunchGroup(this.launchGroupId).getMode());
	}

}
