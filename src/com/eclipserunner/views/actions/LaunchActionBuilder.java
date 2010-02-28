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

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.views.ILaunchConfigurationSelection;

/**
 * Builder creates Actions related to test launching
 * 
 * @author vachacz
 */
public final class LaunchActionBuilder {

	private static final String IMG_RUN                  = "run.gif";
	private static final String IMG_RUN_CONFIGURATIONS   = "run_configuration.gif";
	private static final String IMG_DEBUG                = "debug.gif";
	private static final String IMG_DEBUG_CONFIGURATIONS = "run_configuration.gif";

	private ILaunchConfigurationSelection launchConfigurationSelection;

	private LaunchActionBuilder() {
		// use factory method instead 
	}

	public static final LaunchActionBuilder newInstance() {
		return new LaunchActionBuilder();
	}

	public LaunchActionBuilder withLaunchConfigurationSelection(ILaunchConfigurationSelection launchConfigurationSelection) {
		this.launchConfigurationSelection = launchConfigurationSelection;
		return this;
	}

	public Action createShowRunConfigurationDialogAction() {
		Action action = new ShowLaunchConfigurationsDialogAction(launchConfigurationSelection, IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
		configureAction(action, Message_openRunConfigurationsDialog, Message_openRunConfigurationsDialogTooltip, IMG_RUN_CONFIGURATIONS);
		return action;
	}

	public Action createShowDebugConfigurationDialogAction() {
		Action action = new ShowLaunchConfigurationsDialogAction(launchConfigurationSelection, IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
		configureAction(action, Message_openDebugConfigurationsDialog, Message_openDebugConfigurationsDialogTooltip, IMG_DEBUG_CONFIGURATIONS);
		return action;
	}

	public Action createRunConfigurationAction() {
		Action action = new LaunchConfigurationAction(launchConfigurationSelection, IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
		configureAction(action, Message_runConfiguration, Message_runConfigurationTooltip, IMG_RUN);
		return action;
	}

	public Action createDebugConfigurationAction() {
		Action action = new LaunchConfigurationAction(launchConfigurationSelection, IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
		configureAction(action, Message_debugConfiguration, Message_debugConfigurationTooltip, IMG_DEBUG);
		return action;
	}

	private final void configureAction(Action action, String text, String tooltip, String imageKey) {
		action.setText(text);
		action.setToolTipText(tooltip);
		action.setImageDescriptor(RunnerPlugin.getDefault().getImageDescriptor(imageKey));
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
