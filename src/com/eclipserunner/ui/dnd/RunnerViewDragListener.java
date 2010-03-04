package com.eclipserunner.ui.dnd;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.FileTransfer;

import com.eclipserunner.model.LaunchConfigurationCategory;
import com.eclipserunner.views.utils.ImportExportUtil;

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
			currentSelection = (IStructuredSelection) selection;
			Iterator<?> iterator = currentSelection.iterator();
			while (iterator.hasNext()) {
				Object item = iterator.next();
				if (item instanceof LaunchConfigurationCategory) {
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
		// TODO BARY: dummy code
		else if (FileTransfer.getInstance().isSupportedType(event.dataType)) {
			try {
				File file = File.createTempFile("eclipse-", ".launch");
				file.deleteOnExit();
				ImportExportUtil.export(file, currentSelection);

				String[] paths = new String[1];
				paths[0] = file.getAbsolutePath();
				event.data = paths;
			} catch (CoreException e) {
				// TODO BARY: exception handling
				e.printStackTrace();
			} catch (IOException e) {
				// TODO BARY: exception handling
				e.printStackTrace();
			}
		}

	}

	public void dragFinished(DragSourceEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.dataType)) {
			LocalSelectionTransfer.getTransfer().setSelection(null);
		}
	}

}
