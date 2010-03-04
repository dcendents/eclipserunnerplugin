package com.eclipserunner.ui.dnd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;

import com.eclipserunner.model.LaunchConfigurationCategory;
import com.eclipserunner.model.LaunchTreeContentProvider;

/**
 * Listener for handling drop events.
 * 
 * @author bary
 */
public class RunnerViewDropListener extends ViewerDropAdapter {

	private boolean localTransfer;
	private LaunchTreeContentProvider model;

	public RunnerViewDropListener(Viewer viewer, LaunchTreeContentProvider launchTreeContentProvider) {
		super(viewer);
		this.model = launchTreeContentProvider;
		setFeedbackEnabled(true);
	}

	@Override
	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		localTransfer = false;
		if (LocalSelectionTransfer.getTransfer().isSupportedType(transferType)) {
			localTransfer = true;
			if (getCurrentTarget() instanceof LaunchConfigurationCategory) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean performDrop(Object data) {
		List<ILaunchConfiguration> launchConfigurationToMove = new ArrayList<ILaunchConfiguration>();

		if (localTransfer) {
			ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
			if (selection instanceof IStructuredSelection) {
				Iterator<?> iterator = ((IStructuredSelection) selection).iterator();
				while (iterator.hasNext()) {
					Object item = iterator.next();
					if (item instanceof ILaunchConfiguration) {
						launchConfigurationToMove.add((ILaunchConfiguration) item);
					}
				}
			}
		}

		Object currentTarget = getCurrentTarget();
		if (currentTarget instanceof LaunchConfigurationCategory && getCurrentLocation() == LOCATION_ON) {
			for (ILaunchConfiguration launchConfiguration : launchConfigurationToMove) {
				
				LaunchConfigurationCategory sourceCategory = (LaunchConfigurationCategory) model.getParent(launchConfiguration);
				LaunchConfigurationCategory destinationCategory = (LaunchConfigurationCategory)getCurrentTarget();

				model.removeConfigurationInCategory(sourceCategory, launchConfiguration);
				model.addConfigurationInCategory(destinationCategory, launchConfiguration);
			}
		}

		if (launchConfigurationToMove.size() == 1) {
			getViewer().setSelection(new StructuredSelection(launchConfigurationToMove.get(0)));
			getViewer().refresh();
		}

		return true;
	}

}
