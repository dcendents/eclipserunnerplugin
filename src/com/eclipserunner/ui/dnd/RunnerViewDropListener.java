package com.eclipserunner.ui.dnd;

import static com.eclipserunner.utils.SelectionUtils.getAllSelectedItemsByType;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;

/**
 * Listener for handling drop events.
 *
 * @author bary
 */
public class RunnerViewDropListener extends ViewerDropAdapter {

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
			if (getCurrentTarget() instanceof ICategoryNode) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean performDrop(Object data) {
		List<ILaunchNode> launchNodesToMove = new ArrayList<ILaunchNode>();

		if (localTransfer) {
			ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
			launchNodesToMove.addAll(
					getAllSelectedItemsByType(selection, ILaunchNode.class)
			);
		}

		Object currentTarget = getCurrentTarget();
		if (currentTarget instanceof ICategoryNode && getCurrentLocation() == LOCATION_ON) {
			for (ILaunchNode launchNode : launchNodesToMove) {

				ICategoryNode sourceCategoryNode = launchNode.getCategoryNode();
				ICategoryNode destinationCategoryNode = (ICategoryNode) getCurrentTarget();

				sourceCategoryNode.remove(launchNode);
				destinationCategoryNode.add(launchNode);
			}
		}

		if (launchNodesToMove.size() == 1) {
			getViewer().setSelection(new StructuredSelection(launchNodesToMove.get(0)));
		}

		return true;
	}

}
