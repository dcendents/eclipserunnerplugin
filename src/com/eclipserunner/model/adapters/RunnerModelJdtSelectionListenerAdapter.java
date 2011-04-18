package com.eclipserunner.model.adapters;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.WorkingSet;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

import com.eclipserunner.model.IFilteredRunnerModel;
import com.eclipserunner.model.INodeFilter;
import com.eclipserunner.model.filters.ProjectFilter;
import com.eclipserunner.views.impl.RunnerView;

/**
 * Adapter listening for JDT selection events.
 * 
 * @author bary
 */
@SuppressWarnings("restriction")
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
		if (part instanceof ProjectExplorer) {
			handleProjectExplorerSelection(selection);
		}
		else if (part instanceof PackageExplorerPart) {
			handlePackageExplorerSelection(selection);
		}
		// TODO: Implement or remove
		else {
			System.out.println("DEBUG: Unhandled JDT selection change part:" + part.getClass().getName() + ", selection: " + selection.getClass().getSimpleName());
		}
		
		// Refresh View 
		view.refresh();
	}

	private void handleProjectExplorerSelection(ISelection selection) {
		Object selectedElement = getSelectedElement(selection);
		if (selectedElement instanceof IResource) {
			projectFilter.setFilterProperty("projectName", getProjectName((IResource)selectedElement));
		}
		// TODO: Implement or remove
		else {
			System.out.println("DEBUG: Unhandled ProjectExplorer selection: " + selection.getClass().getSimpleName());
		}
	}
	
	private String getProjectName(IResource resource) {
		while (resource != null) {
			if (IResource.PROJECT == resource.getType()) {
				IProject project = (IProject) resource;
				return project.getName();
			}
			resource = resource.getParent();
		}		
		return null;
	}
	
	private void handlePackageExplorerSelection(ISelection selection) {
		Object selectedElement = getSelectedElement(selection);
		if (selectedElement instanceof WorkingSet) {
			projectFilter.setFilterProperty("projectName", null);   // clear project filter
		}
		else if (selectedElement instanceof JavaProject) {
			projectFilter.setFilterProperty("projectName", getProjectName((JavaProject)selectedElement));			
		}
		// TODO: Implement or remove
		else {
			System.out.println("DEBUG: Unhandled PackageExplorer selection: " + selection.getClass().getSimpleName());
		}		
	}
	
	private String getProjectName(JavaProject javaProject) {
		IProject project = javaProject.getProject();
		return project.getName();	
	}

	private Object getSelectedElement(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			return structuredSelection.getFirstElement();
		}
		return null;
	}
	
}
