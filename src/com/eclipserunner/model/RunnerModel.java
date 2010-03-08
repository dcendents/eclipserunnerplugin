package com.eclipserunner.model;

import static com.eclipserunner.Messages.Message_uncategorized;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * Class implementing {@link ITreeContentProvider} acts as a model for launch configuration tree.
 * By default provides "uncategorized" category.
 * 
 * @author vachacz
 */
public class RunnerModel implements ICategoryChangeListener, IRunnerModel {

	private static RunnerModel runnerModel = new RunnerModel();

	private List<IModelChangeListener> modelChangeListeners = new ArrayList<IModelChangeListener>();
	private Set<ILaunchConfigurationCategory> launchConfigurationCategorySet;

	private ILaunchConfigurationCategory uncategorizedCategory;

	protected RunnerModel() {
		uncategorizedCategory = new LaunchConfigurationCategory();
		uncategorizedCategory.setName(Message_uncategorized);
		uncategorizedCategory.addCategoryChangeListener(this);

		launchConfigurationCategorySet = new HashSet<ILaunchConfigurationCategory>();
		launchConfigurationCategorySet.add(uncategorizedCategory);
	}

	public static IRunnerModel getDefault() {
		return runnerModel;
	}

	public Set<ILaunchConfigurationCategory> getLaunchConfigurationCategorySet() {
		return launchConfigurationCategorySet;
	}

	public void addLaunchConfiguration(ILaunchConfiguration configuration) {
		uncategorizedCategory.add(configuration);
		// fireModelChangedEvent() not needed because category change triggers an event
	}

	public ILaunchConfigurationCategory getLaunchConfigurationCategory(ILaunchConfiguration launchConfiguration) {
		for (ILaunchConfigurationCategory category : launchConfigurationCategorySet) {
			if (category.contains(launchConfiguration)) {
				return category;
			}
		}
		return null;
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
			launchConfigurationCategory.remove(configuration);
		}
		fireModelChangedEvent();
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

	public ILaunchConfigurationCategory getLaunchConfigurationCategory(String name) {
		for (ILaunchConfigurationCategory launchConfigurationCategory : launchConfigurationCategorySet) {
			if (launchConfigurationCategory.getName().equals(name)) {
				return launchConfigurationCategory;
			}
		}
		return null;
	}
	
	public void categoryChanged() {
		fireModelChangedEvent();
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

	// for test only
	protected void setLaunchConfigurationCategorySet(Set<ILaunchConfigurationCategory> launchConfigurationCategorySet) {
		this.launchConfigurationCategorySet = launchConfigurationCategorySet;
	}

}
