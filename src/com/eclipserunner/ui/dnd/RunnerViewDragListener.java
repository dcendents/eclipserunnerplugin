package com.eclipserunner.ui.dnd;

import static com.eclipserunner.utils.SelectionUtils.getFirstSelectedByType;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.FileTransfer;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.utils.ImportExportUtil;

/**
 * Listener for handling drag events.
 * 
 * @author bary
 */
public class RunnerViewDragListener implements DragSourceListener {

	private ISelection currentSelection;

	private final ISelectionProvider selectionProvider;

	public RunnerViewDragListener(ISelectionProvider selectionProvider) {
		this.selectionProvider = selectionProvider;
	}

	public void dragStart(DragSourceEvent event) {
		ISelection selection = selectionProvider.getSelection();
		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			
			currentSelection = (IStructuredSelection) selection;
			ILaunchConfigurationCategory category = getFirstSelectedByType(currentSelection, ILaunchConfigurationCategory.class);
			if (category != null) {
				event.doit = false;
			}
		}
		else {
			this.currentSelection = null;
			event.doit = false;
		}
	}

	public void dragSetData(DragSourceEvent event) {
		if (isDragSelectionEmpty()) {
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

	private boolean isDragSelectionEmpty() {
		return currentSelection == null || currentSelection.isEmpty();
	}

}
