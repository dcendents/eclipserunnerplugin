package com.eclipserunner.views.actions;

import static com.eclipserunner.Messages.Message_debugConfiguration;
import static com.eclipserunner.Messages.Message_debugConfigurationTooltip;
import static com.eclipserunner.Messages.Message_openDebugConfigurationsDialog;
import static com.eclipserunner.Messages.Message_openDebugConfigurationsDialogTooltip;
import static com.eclipserunner.Messages.Message_openRunConfigurationsDialog;
import static com.eclipserunner.Messages.Message_openRunConfigurationsDialogTooltip;
import static com.eclipserunner.Messages.Message_runConfiguration;
import static com.eclipserunner.Messages.Message_runConfigurationTooltip;

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
		configureAction(action, Message_openRunConfigurationsDialog, Message_openRunConfigurationsDialogTooltip, IMG_RUN_CONFIGURATIONS);
		return action;
	}

	public Action createShowDebugConfigurationDialogAction() {
		Action action = new ShowLaunchConfigurationsDialogAction(this.launchConfigurationSelection, IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
		configureAction(action, Message_openDebugConfigurationsDialog, Message_openDebugConfigurationsDialogTooltip, IMG_DEBUG_CONFIGURATIONS);
		return action;
	}

	public Action createRunConfigurationAction() {
		Action action = new LaunchConfigurationAction(this.launchConfigurationSelection, IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
		configureAction(action, Message_runConfiguration, Message_runConfigurationTooltip, IMG_RUN);
		return action;
	}

	public Action createDebugConfigurationAction() {
		Action action = new LaunchConfigurationAction(this.launchConfigurationSelection, IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
		configureAction(action, Message_debugConfiguration, Message_debugConfigurationTooltip, IMG_DEBUG);
		return action;
	}

	private final void configureAction(Action action, String title, String tooltip, String imageKey) {
		action.setText(title);
		action.setToolTipText(tooltip);
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
