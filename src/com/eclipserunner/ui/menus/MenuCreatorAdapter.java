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

	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
	}

	public void run(IAction arg0) {
	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
	}

	public void dispose() {
	}

	public Menu getMenu(Control arg0) {
		return null;
	}

	public Menu getMenu(Menu arg0) {
		return null;
	}

}
