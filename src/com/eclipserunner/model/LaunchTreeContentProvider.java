package com.eclipserunner.model;

import static com.eclipserunner.Messages.Message_uncategorized;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.eclipserunner.views.RunnerView;

/**
 * Class implementing {@link ITreeContentProvider} acts as a model for launch configuration tree.
 * By default provides "uncategorized" category. 
 * 
 * @author vachacz
 */
public class LaunchTreeContentProvider implements ITreeContentProvider {

	private Set<LaunchConfigrationCategory> launchConfigrationCategorySet;
	
	private LaunchConfigrationCategory uncategorizedLaunchConfigrationCategory;

	private RunnerView viewer;
	
	public LaunchTreeContentProvider() {
		uncategorizedLaunchConfigrationCategory = new LaunchConfigrationCategory();
		uncategorizedLaunchConfigrationCategory.setCategoryName(Message_uncategorized);
		
		launchConfigrationCategorySet = new HashSet<LaunchConfigrationCategory>();
		launchConfigrationCategorySet.add(uncategorizedLaunchConfigrationCategory);
	}

	public void addUncategorizedLaunchConfiguration(ILaunchConfiguration configuration) {
		uncategorizedLaunchConfigrationCategory.add(configuration);
	} 
	
	public Object[] getChildren(Object object) {
		if (launchConfigrationCategorySet.contains(object)) {
			LaunchConfigrationCategory launchConfigrationCategory = (LaunchConfigrationCategory) object;
			return launchConfigrationCategory.toArray();
		}
		return null;
	}

	public Object getParent(Object object) {
		for (LaunchConfigrationCategory container : launchConfigrationCategorySet) {
			if (container.contains(object)) {
				return container;
			}
		}
		return null;
	}

	public boolean hasChildren(Object parent) {
		if (launchConfigrationCategorySet.contains(parent)) {
			LaunchConfigrationCategory launchConfigrationContainer = (LaunchConfigrationCategory) parent;
			return !launchConfigrationContainer.getLaunchConfigurationSet().isEmpty();
		}
		return false;
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(viewer.getViewSite())) {
			return launchConfigrationCategorySet.toArray();
		}
		return getChildren(parent);
	}

	public void dispose() {
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

	public void setTreeViewer(RunnerView runnerView) {
		this.viewer = runnerView;
	}

	public LaunchConfigrationCategory addLaunchConfigurationCategory(String name) {
		LaunchConfigrationCategory category = new LaunchConfigrationCategory();
			category.setCategoryName(name);
			
		launchConfigrationCategorySet.add(category);
		return category;
	}

}
