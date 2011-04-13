package com.eclipserunner.model.adapters;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;

import com.eclipserunner.model.IRunnerModel;

/**
 * Adapter listening for JDT selection events.
 * 
 * @author bary
 */
public class RunnerModelJdtSelectionListenerAdapter implements ISelectionListener {

	@SuppressWarnings("unused")
	private IRunnerModel runnerModel;
	private IViewPart viewPart;

	public RunnerModelJdtSelectionListenerAdapter(IRunnerModel runnerModel, IViewPart viewPart) {
		this.runnerModel = runnerModel;
		this.viewPart = viewPart;
	}

	// TODO BARY
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// we ignore our own selections
		if (part != viewPart) {
			System.out.println("TODO: handle JDT selection change part:" + part.getClass().getSimpleName() + ", selection: " + selection.getClass().getSimpleName());
		}
	}

}
