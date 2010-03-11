package com.eclipserunner.model;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Adapter listening for JDT selection events.
 * 
 * @author bary
 *
 */
public class RunnerModelJdtSelectionListenerAdapter implements ISelectionListener {

	@SuppressWarnings("unused")
	private IRunnerModel runnerModel;

	public RunnerModelJdtSelectionListenerAdapter(IRunnerModel runnerModel) {
		this.runnerModel = runnerModel;
	}

	// TODO BARY
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		System.out.println("TODO: handle JDT selection change part:" + part.getClass().getSimpleName() + ", selection: " + selection.getClass().getSimpleName());

	}

}
