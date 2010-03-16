package com.eclipserunner.model.adapters;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewPart;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
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
		if (object instanceof ICategoryNode) {
			ICategoryNode categoryNode = (ICategoryNode) object;
			return categoryNode.getLaunchNodes().toArray();
		}
		return null;
	}

	public Object getParent(Object object) {
		if (object instanceof ILaunchNode) {
			return ((ILaunchNode) object).getCategoryNode();
		}
		return null;
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof ICategoryNode) {
			ICategoryNode categoryNode = (ICategoryNode) parent;
			return !categoryNode.isEmpty();
		}
		return false;
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(viewPart.getViewSite())) {
			Collection<ICategoryNode> categoryNodes = runnerModel.getCategoryNodes();
			Object[] objects = categoryNodes.toArray();
			if (! runnerModel.isDefaultCategoryNodeVisible()) {
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
