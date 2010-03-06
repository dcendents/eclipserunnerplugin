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
public class LaunchTreeContentProvider implements ITreeContentProvider, ICategoryChangeListener {

	private List<IModelChangeListener> modelChangeListeners = new ArrayList<IModelChangeListener>();
	private Set<ILaunchConfigurationCategory> launchConfigrationCategorySet;

	private ILaunchConfigurationCategory uncategorizedCategory;

	private IViewPart viewPart;

	public LaunchTreeContentProvider() {
		uncategorizedCategory = new LaunchConfigurationCategory();
		uncategorizedCategory.setName(Message_uncategorized);
		uncategorizedCategory.addCategoryChangeListener(this);

		launchConfigrationCategorySet = new HashSet<ILaunchConfigurationCategory>();
		launchConfigrationCategorySet.add(uncategorizedCategory);
	}

	public void addUncategorizedLaunchConfiguration(ILaunchConfiguration configuration) {
		uncategorizedCategory.add(configuration);
		fireModelChangedEvent();
	}

	public Object[] getChildren(Object object) {
		if (launchConfigrationCategorySet.contains(object)) {
			ILaunchConfigurationCategory launchConfigrationCategory = (ILaunchConfigurationCategory) object;
			return launchConfigrationCategory.getLaunchConfigurationSet().toArray();
		}
		return null;
	}

	public Object getParent(Object object) {
		for (ILaunchConfigurationCategory category : launchConfigrationCategorySet) {
			if (object instanceof ILaunchConfiguration && category.contains((ILaunchConfiguration) object)) {
				return category;
			}
		}
		return null;
	}

	public boolean hasChildren(Object parent) {
		if (launchConfigrationCategorySet.contains(parent)) {
			ILaunchConfigurationCategory launchConfigrationCategory = (ILaunchConfigurationCategory) parent;
			return !launchConfigrationCategory.isEmpty();
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

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void setViewPart(IViewPart viewPart) {
		this.viewPart = viewPart;
	}

	public ILaunchConfigurationCategory addLaunchConfigurationCategory(String name) {
		ILaunchConfigurationCategory category = new LaunchConfigurationCategory();
		category.setName(name);
		category.addCategoryChangeListener(this);

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

	private void fireModelChangedEvent() {
		for (IModelChangeListener listener : modelChangeListeners) {
			listener.modelChanged();
		}
	}

	public ILaunchConfigurationCategory getUncategorizedCategory() {
		return uncategorizedCategory;
	}

	public void removeCategory(ILaunchConfigurationCategory categoy) {
		launchConfigrationCategorySet.remove(categoy);
	}

	public void categoryChanged() {
		fireModelChangedEvent();
	}

}
