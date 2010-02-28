package com.eclipserunner.views.actions;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.views.ILaunchConfigurationSelection;

/**
 * Builder creates Actions related to test launching
 * 
 * @author vachacz
 */
public final class LaunchActionBuilder {

	private static final String IMG_RUN                  = "run.gif";
	private static final String IMG_RUN_CONFIGURATIONS   = "run.gif";
	private static final String IMG_DEBUG                = "run_configuration.gif";
	private static final String IMG_DEBUG_CONFIGURATIONS = "run_configuration.gif";

	private static LaunchActionBuilder builder = new LaunchActionBuilder();

	private ILaunchConfigurationSelection launchConfigurationSelection;

	private LaunchActionBuilder() {
		// singleton pattern
	}

	public static final LaunchActionBuilder newInstance() {
		return builder;
	}

	public LaunchActionBuilder withLaunchConfigurationSelection(ILaunchConfigurationSelection launchConfigurationSelection) {
		this.launchConfigurationSelection = launchConfigurationSelection;
		return builder;
	}

	public Action createShowRunConfigurationDialogAction() {
		Action action = new ShowLaunchConfigurationsDialogAction(this.launchConfigurationSelection, IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
		configureAction(action, "property.openRunConfigurationsDialogText", "property.openRunConfigurationsDialogToolTipText", IMG_RUN_CONFIGURATIONS);
		return action;
	}

	public Action createShowDebugConfigurationDialogAction() {
		Action action = new ShowLaunchConfigurationsDialogAction(this.launchConfigurationSelection, IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
		configureAction(action, "property.openDebugConfigurationsDialogText", "property.openDebugConfigurationsDialogToolTipText", IMG_DEBUG_CONFIGURATIONS);
		return action;
	}

	public Action createRunConfigurationAction() {
		Action action = new LaunchConfigurationAction(this.launchConfigurationSelection, IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
		configureAction(action, "property.runConfigurationText", "property.runConfigurationToolTipText", IMG_RUN);
		return action;
	}

	public Action createDebugConfigurationAction() {
		Action action = new LaunchConfigurationAction(this.launchConfigurationSelection, IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
		configureAction(action, "property.debugConfigurationText", "property.debugConfigurationToolTipText", IMG_DEBUG);
		return action;
	}

	private final void configureAction(Action action, String textKey, String tooltipKey, String imageKey) {
		action.setText(RunnerPlugin.getResourceString(textKey));
		action.setToolTipText(RunnerPlugin.getResourceString(tooltipKey));
		// TODO BARY - how to set our own custom icon?
		//action.setImageDescriptor(Activator.getDefault().getImageDescriptor(imageKey));
		action.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	// TODO LWA BARY
	public Action createAboutAction() {
		Action action = new Action() {
			@Override
			public void run() {
				MessageDialog.openInformation(
					RunnerPlugin.getShell(), "Eclipse Runner View", "About action executed!"
				);
			}
		};
		configureAction(action, "About ...", "About ...", null);
		return action;
	}

}
