package com.eclipserunner.model;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewPart;

/**
 * Adapter class adapts IRunnerModel to ITreeContentProvider interface.
 * 
 * @author vachacz
 */
public class RunnerModelTreeAdapter implements ITreeContentProvider {

	private IRunnerModel runnerModel;
	private IViewPart viewPart;

	public RunnerModelTreeAdapter(IRunnerModel runnerModel, IViewPart viewPart) {
		this.runnerModel = runnerModel;
		this.viewPart = viewPart;
	}

	public Object[] getChildren(Object object) {
		if (object instanceof ILaunchConfigurationCategory) {
			ILaunchConfigurationCategory launchConfigrationCategory = (ILaunchConfigurationCategory) object;
			return launchConfigrationCategory.getLaunchConfigurationNodes().toArray();
		}
		return null;
	}

	public Object getParent(Object object) {
		if (object instanceof ILaunchConfigurationNode) {
			return ((ILaunchConfigurationNode) object).getParentCategory();
		}
		return null;
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof ILaunchConfigurationCategory) {
			ILaunchConfigurationCategory launchConfigrationCategory = (ILaunchConfigurationCategory) parent;
			return !launchConfigrationCategory.isEmpty();
		}
		return false;
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(viewPart.getViewSite())) {
			return runnerModel.getLaunchConfigurationCategories().toArray();
		}
		return getChildren(parent);
	}

	public void dispose() {
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

}
