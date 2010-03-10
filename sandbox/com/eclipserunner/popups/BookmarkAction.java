package com.eclipserunner.popups;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.contextlaunching.LaunchingResourceManager;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsMessages;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchShortcutExtension;
import org.eclipse.debug.internal.ui.stringsubstitution.SelectedResourceManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.RunnerModel;

@SuppressWarnings("restriction")
public class BookmarkAction extends Action {

	private LaunchShortcutExtension launchShortcut;
	private String launchMode;
	
	public BookmarkAction(String launchMode, LaunchShortcutExtension launchShortcut) {
		super(launchMode + " \"" + launchShortcut.getLabel() + "\"", launchShortcut.getImageDescriptor());
		this.launchShortcut = launchShortcut;
		this.launchMode = launchMode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		LaunchingResourceManager resourceManeger = DebugUIPlugin.getDefault().getLaunchingResourceManager();
		IStructuredSelection selection = SelectedResourceManager.getDefault().getCurrentSelection();
		
		List<LaunchShortcutExtension> shortcuts = Arrays.asList(launchShortcut);
		
		IResource resource = SelectedResourceManager.getDefault().getSelectedResource();
		if (resource == null) {
			resource = resourceManeger.getLaunchableResource(shortcuts, selection);
		}

		@SuppressWarnings("unused")
		IRunnerModel runnerModel = RunnerModel.getDefault();
		List configs = resourceManeger.getParticipatingLaunchConfigurations(selection, resource, shortcuts, launchMode);
		if (configs.size() > 0) {
			// TODO LWA
			// runnerModel.addLaunchConfiguration((ILaunchConfiguration) configs.get(0));
		} else {
			Set types = launchShortcut.getAssociatedConfigurationTypes();
			ILaunchConfigurationType type = getLaunchManager().getLaunchConfigurationType((String) types.toArray()[0]);

			try {
				ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, getLaunchManager().generateUniqueLaunchConfigurationNameFrom(LaunchConfigurationsMessages.CreateLaunchConfigurationAction_New_configuration_2));
				@SuppressWarnings("unused")
				ILaunchConfiguration launchConfiguration = workingCopy.doSave();
				
				// TODO LWA
				// runnerModel.addLaunchConfiguration(launchConfiguration);
			} catch (CoreException e) {
			}
		}
	}

	private ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}
	
}
