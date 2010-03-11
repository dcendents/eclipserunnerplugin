package com.eclipserunner.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewPart;

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
			ILaunchConfigurationCategory launchConfigrationCategory = (ILaunchConfigurationCategory) object;
			return getTypesByCategory(launchConfigrationCategory);
		} else if (object instanceof LaunchConfigurationTypeNode) {
			ILaunchConfigurationCategory category = ((LaunchConfigurationTypeNode) object).getParentCategory();
			return getConfigsByType(category, (LaunchConfigurationTypeNode) object);
		}
		return null;
	}

	public Object getParent(Object object) {
		if (object instanceof ILaunchConfigurationNode) {
			return ((ILaunchConfigurationNode) object).getLaunchConfigurationCategory();
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

	private Object[] getConfigsByType(ILaunchConfigurationCategory category, LaunchConfigurationTypeNode type) {
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
	
	private Object[] getTypesByCategory(ILaunchConfigurationCategory launchConfigrationCategory) {
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
