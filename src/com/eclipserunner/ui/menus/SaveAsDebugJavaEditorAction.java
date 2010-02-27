package com.eclipserunner.ui.menus;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

/**
 * @author vachacz
 */
public class SaveAsDebugJavaEditorAction extends Action implements IEditorActionDelegate {

	private IEditorPart editorPart;
	
	public void setActiveEditor(IAction action, IEditorPart editorPart) {
		this.editorPart = editorPart;
	}

	public void run(IAction arg0) {
		// TODO LWA dummy
		MessageDialog.openInformation(
			editorPart.getEditorSite().getShell(),
			"Eclipse Runner Action", "add debug configuration to bookmarks");
	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
	}

}
