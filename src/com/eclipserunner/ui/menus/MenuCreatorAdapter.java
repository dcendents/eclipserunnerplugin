package com.eclipserunner.ui.menus;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This class provides empty implementation of {@link IObjectActionDelegate} and 
 * {@link IMenuCreator} interfaces. It's used to avoid implementation of unnecessary methods.
 * 
 * Leave all methods not implemented.
 * 
 * @author vachacz
 */
public class MenuCreatorAdapter implements IObjectActionDelegate, IMenuCreator {

	public void setActivePart(IAction action, IWorkbenchPart workbenchPart) {
	}

	public void run(IAction action) {
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void dispose() {
	}

	public Menu getMenu(Control control) {
		return null;
	}

	public Menu getMenu(Menu menu) {
		return null;
	}

}
