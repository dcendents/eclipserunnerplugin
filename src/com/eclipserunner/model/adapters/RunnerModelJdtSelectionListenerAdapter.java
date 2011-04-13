package com.eclipserunner.model.adapters;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import com.eclipserunner.model.IFilteredRunnerModel;
import com.eclipserunner.model.INodeFilter;
import com.eclipserunner.model.filters.ProjectFilter;
import com.eclipserunner.views.impl.RunnerView;

/**
 * Adapter listening for JDT selection events.
 * 
 * @author bary
 */
public class RunnerModelJdtSelectionListenerAdapter implements ISelectionListener {

	@SuppressWarnings("unused")
	private IFilteredRunnerModel model;
	private RunnerView view;
	private INodeFilter projectFilter;

	public RunnerModelJdtSelectionListenerAdapter(IFilteredRunnerModel model, RunnerView view) {
		this.model = model;
		this.view = view;
		// Find ProjectFilter filter ;)
		for (INodeFilter filter : model.getFilters()) {
			if (filter instanceof ProjectFilter) {
				projectFilter = filter;
				break;
			}
		}

	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// we ignore our own selections
		if (part != view) {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				Object selectedElement = structuredSelection.getFirstElement();
				if (selectedElement != null) {
					if (selectedElement instanceof IResource) {
						System.out.println("DEBUG: handle JDT selection selectedElement:" + selectedElement.getClass().getSimpleName());
						IResource resource = (IResource) selectedElement;
						while (resource != null) {
							if (IResource.PROJECT == resource.getType()) {
								IProject project = (IProject) resource;
								projectFilter.setFilterProperty("projectName", project.getName());
								view.refresh();
							}
							resource = resource.getParent();
						}
					}
					else {
						// TODO: BARY How to get IProject from selectedElement other than IResource?
						System.out.println("DEBUG: TODO handle JDT selection selectedElement:" + selectedElement.getClass().getSimpleName());
					}
				}
			}
			else {
				// TODO: BARY How to get IProject from selection other than IStructuredSelection?
				System.out.println("DEBUG: TODO handle JDT selection change part:" + part.getClass().getSimpleName() + ", selection: " + selection.getClass().getSimpleName());
			}
		}
	}

}
