package com.eclipserunner.ui.dnd;

import java.util.Iterator;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

/**
 * Listener for handling drag events.
 * 
 * @author bary
 */
public class RunnerViewDragListener implements DragSourceListener {

	private IStructuredSelection currentSelection;

	private final ISelectionProvider selectionProvider;

	public RunnerViewDragListener(ISelectionProvider selectionProvider) {
		this.selectionProvider = selectionProvider;
	}

	public void dragStart(DragSourceEvent event) {
		ISelection selection = selectionProvider.getSelection();
		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			this.currentSelection = (IStructuredSelection) selection;
			Iterator<?> it = currentSelection.iterator();
			while (it.hasNext()) {
				Object item = it.next();
				if (!(item instanceof ILaunchConfiguration)) {
					event.doit = false;
					return;
				}
			}
		}
		else {
			this.currentSelection = null;
			event.doit = false;
		}
	}

	public void dragSetData(DragSourceEvent event) {
		if (currentSelection == null || currentSelection.isEmpty()) {
			return;
		}

		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.dataType)) {
			LocalSelectionTransfer.getTransfer().setSelection(currentSelection);
		}
	}

	public void dragFinished(DragSourceEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.dataType)) {
			LocalSelectionTransfer.getTransfer().setSelection(null);
		}
	}

}
