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
public class LaunchTreeContentProvider implements ITreeContentProvider, ICategoryChangeListener, IRunnerModel {

	private static LaunchTreeContentProvider model = new LaunchTreeContentProvider();

	private List<IModelChangeListener> modelChangeListeners = new ArrayList<IModelChangeListener>();
	private Set<ILaunchConfigurationCategory> launchConfigurationCategorySet;

	private ILaunchConfigurationCategory uncategorizedCategory;

	private IViewPart viewPart;

	private LaunchTreeContentProvider() {
		uncategorizedCategory = new LaunchConfigurationCategory();
		uncategorizedCategory.setName(Message_uncategorized);
		uncategorizedCategory.addCategoryChangeListener(this);

		launchConfigurationCategorySet = new HashSet<ILaunchConfigurationCategory>();
		launchConfigurationCategorySet.add(uncategorizedCategory);
	}

	public static LaunchTreeContentProvider getDefault() {
		return model;
	}

	public Set<ILaunchConfigurationCategory> getLaunchConfigurationCategorySet() {
		return launchConfigurationCategorySet;
	}

	public void addLaunchConfiguration(ILaunchConfiguration configuration) {
		uncategorizedCategory.add(configuration);
		fireModelChangedEvent();
	}

	public Object[] getChildren(Object object) {
		if (launchConfigurationCategorySet.contains(object)) {
			ILaunchConfigurationCategory launchConfigrationCategory = (ILaunchConfigurationCategory) object;
			return launchConfigrationCategory.getLaunchConfigurationSet().toArray();
		}
		return null;
	}

	public Object getParent(Object object) {
		if (object instanceof ILaunchConfiguration) {
			return getParentCategory((ILaunchConfiguration) object);
		}
		return null;
	}

	public ILaunchConfigurationCategory getParentCategory(ILaunchConfiguration launchConfiguration) {
		for (ILaunchConfigurationCategory category : launchConfigurationCategorySet) {
			if (category.contains(launchConfiguration)) {
				return category;
			}
		}
		return null;
	}

	public boolean hasChildren(Object parent) {
		if (launchConfigurationCategorySet.contains(parent)) {
			ILaunchConfigurationCategory launchConfigrationCategory = (ILaunchConfigurationCategory) parent;
			return !launchConfigrationCategory.isEmpty();
		}
		return false;
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(viewPart.getViewSite())) {
			return launchConfigurationCategorySet.toArray();
		}
		return getChildren(parent);
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void setViewPart(IViewPart viewPart) {
		this.viewPart = viewPart;
	}

	public ILaunchConfigurationCategory addLaunchConfigurationCategory(String name) {
		ILaunchConfigurationCategory category = new LaunchConfigurationCategory();
		category.setName(name);
		category.addCategoryChangeListener(this);

		launchConfigurationCategorySet.add(category);
		fireModelChangedEvent();
		return category;
	}

	public void addChangeListener(IModelChangeListener listener) {
		modelChangeListeners.add(listener);
	}

	public void removeChangeListener(IModelChangeListener listener) {
		modelChangeListeners.remove(listener);
	}

	private void fireModelChangedEvent() {
		for (IModelChangeListener listener : modelChangeListeners) {
			listener.modelChanged();
		}
	}

	public ILaunchConfigurationCategory getUncategorizedCategory() {
		return uncategorizedCategory;
	}

	public void removeCategory(ILaunchConfigurationCategory category) {
		// TODO/FIXME: BARY LWA java.util.ConcurrentModificationException
		//for(ILaunchConfiguration launchConfiguration : category.getLaunchConfigurationSet()) {
		//	category.remove(launchConfiguration);
		//	uncategorizedCategory.add(launchConfiguration);
		//}
		launchConfigurationCategorySet.remove(category);
		fireModelChangedEvent();
	}

	public void categoryChanged() {
		fireModelChangedEvent();
	}

	public ILaunchConfigurationCategory getLaunchConfigurationCategory(String name) {
		for (ILaunchConfigurationCategory launchConfigurationCategory : launchConfigurationCategorySet) {
			if (launchConfigurationCategory.getName().equals(name)) {
				return launchConfigurationCategory;
			}
		}
		return null;
	}

}
