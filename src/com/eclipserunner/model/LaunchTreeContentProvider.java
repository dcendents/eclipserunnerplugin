package com.eclipserunner.model;

import static com.eclipserunner.Messages.Message_uncategorized;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewPart;

/**
 * Class implementing {@link ITreeContentProvider} acts as a model for launch configuration tree.
 * By default provides "uncategorized" category. 
 * 
 * @author vachacz
 */
public class LaunchTreeContentProvider implements ITreeContentProvider {

	private List<IModelChangeListener> modelChangeListeners = new ArrayList<IModelChangeListener>();
	private Set<LaunchConfigurationCategory> launchConfigrationCategorySet;
	
	private LaunchConfigurationCategory uncategorizedCategory;

	private IViewPart viewPart;
	
	public LaunchTreeContentProvider() {
		uncategorizedCategory = new LaunchConfigurationCategory();
		uncategorizedCategory.setName(Message_uncategorized);
		
		launchConfigrationCategorySet = new HashSet<LaunchConfigurationCategory>();
		launchConfigrationCategorySet.add(uncategorizedCategory);
	}

	public void addUncategorizedLaunchConfiguration(ILaunchConfiguration configuration) {
		uncategorizedCategory.add(configuration);
		fireModelChangedEvent();
	} 
	
	public Object[] getChildren(Object object) {
		if (launchConfigrationCategorySet.contains(object)) {
			LaunchConfigurationCategory launchConfigrationCategory = (LaunchConfigurationCategory) object;
			return launchConfigrationCategory.toArray();
		}
		return null;
	}

	public Object getParent(Object object) {
		for (LaunchConfigurationCategory category : launchConfigrationCategorySet) {
			if (category.contains(object)) {
				return category;
			}
		}
		return null;
	}

	public boolean hasChildren(Object parent) {
		if (launchConfigrationCategorySet.contains(parent)) {
			LaunchConfigurationCategory launchConfigrationCategory = (LaunchConfigurationCategory) parent;
			return !launchConfigrationCategory.getLaunchConfigurationSet().isEmpty();
		}
		return false;
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(viewPart.getViewSite())) {
			return launchConfigrationCategorySet.toArray();
		}
		return getChildren(parent);
	}

	public void dispose() {
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

	public void setViewPart(IViewPart viewPart) {
		this.viewPart = viewPart;
	}

	public LaunchConfigurationCategory addLaunchConfigurationCategory(String name) {
		LaunchConfigurationCategory category = new LaunchConfigurationCategory();
			category.setName(name);
			
		launchConfigrationCategorySet.add(category);
		fireModelChangedEvent();
		return category;
	}

	public void addChangeListener(IModelChangeListener listener) {
		modelChangeListeners.add(listener);
	}

	public void removeChangeListener(IModelChangeListener listener) {
		modelChangeListeners.remove(listener);
	}
	
	public void fireModelChangedEvent() {
		for (IModelChangeListener listener : modelChangeListeners) {
			listener.modelChanged();
		}
	}

	public LaunchConfigurationCategory getUncategorizedCategory() {
		return uncategorizedCategory;
	}

}
