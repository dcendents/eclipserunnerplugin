package com.eclipserunner.ui.dnd;

import static com.eclipserunner.utils.SelectionUtils.getAllSelectedByType;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationNode;

/**
 * Listener for handling drop events.
 * 
 * @author bary
 */
public class RunnerViewDropListener extends ViewerDropAdapter {

	// TODO BARY : what is "localTransfer"? we could change name to something better
	private boolean localTransfer;

	public RunnerViewDropListener(Viewer viewer) {
		super(viewer);
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
		List<ILaunchConfigurationNode> launchConfigurationToMove = new ArrayList<ILaunchConfigurationNode>();

		if (localTransfer) {
			ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
			launchConfigurationToMove.addAll(
					getAllSelectedByType(selection, ILaunchConfigurationNode.class)
			);
		}

		Object currentTarget = getCurrentTarget();
		if (currentTarget instanceof ILaunchConfigurationCategory && getCurrentLocation() == LOCATION_ON) {
			for (ILaunchConfigurationNode launchConfiguration : launchConfigurationToMove) {

				ILaunchConfigurationCategory sourceCategory = launchConfiguration.getLaunchConfigurationCategory();
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
