package com.eclipserunner.model.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewPart;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationNode;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.impl.LaunchConfigurationTypeNode;

/**
 * Development code ... before refactoring.
 * 
 * @author vachacz
 */
public class RunnerModelTreeWithTypesAdapter implements ITreeContentProvider {

	private IRunnerModel runnerModel;
	private IViewPart viewPart;

	public RunnerModelTreeWithTypesAdapter(IRunnerModel runnerModel, IViewPart viewPart) {
		this.runnerModel = runnerModel;
		this.viewPart = viewPart;
	}

	public Object[] getChildren(Object object) {
		if (object instanceof ILaunchConfigurationCategory) {
			return getChildrenByCategory((ILaunchConfigurationCategory) object);
		} 
		else if (object instanceof LaunchConfigurationTypeNode) {
			return getChildrenByType((LaunchConfigurationTypeNode) object);
		}
		return null;
	}

	public Object getParent(Object object) {
		if (object instanceof ILaunchConfigurationNode) {
			return getParentByLaunchConfiguration((ILaunchConfigurationNode) object); 
		}
		else if (object instanceof LaunchConfigurationTypeNode) {
			return ((LaunchConfigurationTypeNode) object).getParentCategory();
		}
		return null;
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof ILaunchConfigurationCategory) {
			ILaunchConfigurationCategory launchConfigrationCategory = (ILaunchConfigurationCategory) parent;
			return !launchConfigrationCategory.isEmpty();
		} 
		else if (parent instanceof LaunchConfigurationTypeNode) {
			return true;
		} 
		return false;
	}

	// TODO LWA make it better
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

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

	// TODO LWA make it better and easier
	private Object getParentByLaunchConfiguration(ILaunchConfigurationNode launchConfigurationNode) {
		ILaunchConfigurationType type;
		try {
			type = launchConfigurationNode.getLaunchConfiguration().getType();
		} catch (CoreException e) {
			return null;
		}
		Object[] children = getChildren(launchConfigurationNode.getLaunchConfigurationCategory());
		for (int i = 0; i > children.length; i++) {
			if (((LaunchConfigurationTypeNode) children[i]).getType() == type) {
				return children[i];
			}
		}
		return null;
	}
	
	private Object[] getChildrenByType(LaunchConfigurationTypeNode type) {
		ILaunchConfigurationCategory category = type.getParentCategory();
		
		List<ILaunchConfigurationNode> nodes = new ArrayList<ILaunchConfigurationNode>();
		for (ILaunchConfigurationNode node : category.getLaunchConfigurationNodes()) {
			try {
				if (type.getType().equals(node.getLaunchConfiguration().getType())) {
					nodes.add(node);
				}
			} catch (CoreException e) {
			}
		}
		return nodes.toArray();
	}
	
	private Object[] getChildrenByCategory(ILaunchConfigurationCategory launchConfigrationCategory) {
		List<LaunchConfigurationTypeNode> types = new ArrayList<LaunchConfigurationTypeNode>();
		for (ILaunchConfigurationNode node : launchConfigrationCategory.getLaunchConfigurationNodes()) {
			try {
				ILaunchConfigurationType type = node.getLaunchConfiguration().getType();
				if (!isTypeOnTheList(type, types)) {
					LaunchConfigurationTypeNode typeNode = new LaunchConfigurationTypeNode();
					typeNode.setParentCategory(launchConfigrationCategory);
					typeNode.setType(type);
					
					types.add(typeNode);
				}
			} catch (CoreException e) {
			}
		}
		return types.toArray();
	}

	private boolean isTypeOnTheList(ILaunchConfigurationType type,	List<LaunchConfigurationTypeNode> types) {
		for (LaunchConfigurationTypeNode node : types) {
			if (node.getType().equals(type)) {
				return true;
			}
		}
		return false;
	}
	
}
