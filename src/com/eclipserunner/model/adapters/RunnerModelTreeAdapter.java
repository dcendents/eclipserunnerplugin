package com.eclipserunner.model.adapters;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewPart;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationNode;
import com.eclipserunner.model.IRunnerModel;

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
			return ((ILaunchConfigurationNode) object).getLaunchConfigurationCategory();
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
			Collection<ILaunchConfigurationCategory> categories = runnerModel.getLaunchConfigurationCategories();
			Object[] objects = categories.toArray();
			if (! runnerModel.isDefaultCategoryVisible()) {
				objects = Arrays.asList(objects).subList(1, objects.length).toArray();
			}
			return objects;
		}
		return getChildren(parent);
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
