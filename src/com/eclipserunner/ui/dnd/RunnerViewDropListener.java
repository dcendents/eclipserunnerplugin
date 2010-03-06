package com.eclipserunner.ui.dnd;

import static com.eclipserunner.utils.SelectionUtils.getAllSelectedByType;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.IRunnerModel;

/**
 * Listener for handling drop events.
 * 
 * @author bary
 */
public class RunnerViewDropListener extends ViewerDropAdapter {

	private boolean localTransfer;
	private IRunnerModel runnerModel;

	public RunnerViewDropListener(Viewer viewer, IRunnerModel runnerModel) {
		super(viewer);
		this.runnerModel = runnerModel;
		setFeedbackEnabled(true);
	}

	@Override
	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		localTransfer = false;
		if (LocalSelectionTransfer.getTransfer().isSupportedType(transferType)) {
			localTransfer = true;
			if (getCurrentTarget() instanceof ILaunchConfigurationCategory) {
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
			launchConfigurationToMove.addAll(
				getAllSelectedByType(selection, ILaunchConfiguration.class)
			);
		}

		Object currentTarget = getCurrentTarget();
		if (currentTarget instanceof ILaunchConfigurationCategory && getCurrentLocation() == LOCATION_ON) {
			for (ILaunchConfiguration launchConfiguration : launchConfigurationToMove) {
				
				ILaunchConfigurationCategory sourceCategory = runnerModel.getParentCategory(launchConfiguration);
				ILaunchConfigurationCategory destinationCategory = (ILaunchConfigurationCategory) getCurrentTarget();

				sourceCategory.remove(launchConfiguration);
				destinationCategory.add(launchConfiguration);
			}
		}

		if (launchConfigurationToMove.size() == 1) {
			getViewer().setSelection(new StructuredSelection(launchConfigurationToMove.get(0)));
			getViewer().refresh();
		}

		return true;
	}


}
