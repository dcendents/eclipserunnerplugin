package com.eclipserunner.ui.dnd;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

import com.eclipserunner.model.LaunchConfigurationCategory;
import com.eclipserunner.views.RunnerView;

/**
 * Listener for handling drop events.
 * 
 * @author bary
 */
public class RunnerViewDropListener extends ViewerDropAdapter {

	private final RunnerView runnerView;

	public RunnerViewDropListener(RunnerView runnerView) {
		super(runnerView.getViewer());
		this.runnerView = runnerView;
	}

	@Override
	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		if (target != null && target instanceof LaunchConfigurationCategory) {
			return true;
		}
		return false;
	}


	@Override
	public void drop(DropTargetEvent event) {
		Object target = determineTarget(event);
		if (target != null && target instanceof LaunchConfigurationCategory) {
			super.drop(event);
		}
	}

	@Override
	public boolean performDrop(Object data) {
		if (runnerView.isLaunchConfigurationSelected()) {
			ILaunchConfiguration launchConfiguration = runnerView.getSelectedLaunchConfiguration();

			LaunchConfigurationCategory sourceCategory = (LaunchConfigurationCategory)runnerView.getTreeContentProvider().getParent(launchConfiguration);
			LaunchConfigurationCategory destinationCategory = (LaunchConfigurationCategory)getCurrentTarget();

			// TODO LWA BARY remove is now protected
			// sourceCategory.remove(launchConfiguration);
			destinationCategory.add(launchConfiguration);

			return true;
		}
		return false;
	}

}
