package com.eclipserunner.model;

import static com.eclipserunner.Messages.Message_uncategorized;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewPart;

/**
 * Class implementing {@link ITreeContentProvider} acts as a model for launch configuration tree.
 * By default provides "uncategorized" category.
 * 
 * @author vachacz
 */
public class LaunchTreeContentProvider implements ITreeContentProvider, ICategoryChangeListener, IRunnerModel, ILaunchConfigurationListener {

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
			return getLaunchConfigurationCategory((ILaunchConfiguration) object);
		}
		return null;
	}

	public ILaunchConfigurationCategory getLaunchConfigurationCategory(ILaunchConfiguration launchConfiguration) {
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

	public void removeLaunchConfiguration(ILaunchConfiguration configuration) {
		for (ILaunchConfigurationCategory launchConfigurationCategory : launchConfigurationCategorySet) {
			for(ILaunchConfiguration launchConfiguration : launchConfigurationCategory.getLaunchConfigurationSet()) {
				if (launchConfiguration.equals(configuration)) {
					launchConfigurationCategory.remove(launchConfiguration);
					return;
				}
			}
		}
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

	public void removeLaunchConfigurationCategory(ILaunchConfigurationCategory category) {
		// TODO/FIXME: BARY LWA java.util.ConcurrentModificationException
		//for(ILaunchConfiguration launchConfiguration : category.getLaunchConfigurationSet()) {
		//	category.remove(launchConfiguration);
		//	uncategorizedCategory.add(launchConfiguration);
		//}
		launchConfigurationCategorySet.remove(category);
		category.removeCategoryChangeListener(this);
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


	// ILaunchConfigurationListener
	public void launchConfigurationAdded(ILaunchConfiguration newConfiguration) {
		// Find old configuration
		ILaunchConfiguration oldLaunchConfiguration = DebugPlugin.getDefault().getLaunchManager().getMovedFrom(newConfiguration);
		ILaunchConfigurationCategory oldConfigurationCategory = null;
		if (oldLaunchConfiguration != null) {
			oldConfigurationCategory = getLaunchConfigurationCategory(oldLaunchConfiguration);
		}

		// add new configuration to the same category if possible
		if (oldConfigurationCategory != null) {
			oldConfigurationCategory.add(newConfiguration);
		}
		else {
			getUncategorizedCategory().add(newConfiguration);
		}
	}

	// ILaunchConfigurationListener
	public void launchConfigurationChanged(ILaunchConfiguration configuration) {
	}

	// ILaunchConfigurationListener
	public void launchConfigurationRemoved(ILaunchConfiguration configuration) {
		removeLaunchConfiguration(configuration);
	}

}
