package com.eclipserunner.model.impl;

import static com.eclipserunner.Messages.Message_uncategorized;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import com.eclipserunner.model.ICategoryChangeListener;
import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationNode;
import com.eclipserunner.model.IModelChangeListener;
import com.eclipserunner.model.IRunnerModel;

/**
 * Class implementing {@link IRunnerModel}
 * 
 * @author vachacz
 */
public class RunnerModel implements IRunnerModel, ICategoryChangeListener {

	class ILaunchConfigurationCategoryComparator implements Comparator<ILaunchConfigurationCategory> {
		public int compare(ILaunchConfigurationCategory category1,	ILaunchConfigurationCategory category2) {
			if (RunnerModel.this.getDefaultCategory().equals(category1)) {
				return 1;
			} else if (RunnerModel.this.getDefaultCategory().equals(category2)) {
				return 1;
			} else {
				return category1.getName().compareTo(category2.getName());
			}
		}
	}

	private static RunnerModel runnerModel = new RunnerModel();

	private List<IModelChangeListener> modelChangeListeners = new ArrayList<IModelChangeListener>();
	private Set<ILaunchConfigurationCategory> launchConfigurationCategories;

	private ILaunchConfigurationCategory defaultCategory;

	protected RunnerModel() {
		// TODO LWA builder
		LaunchConfigurationCategory category = new LaunchConfigurationCategory();
		category.setName(Message_uncategorized);
		category.addCategoryChangeListener(this);
		category.setRemovable(false);
		category.setRenameable(false);

		defaultCategory = category;

		launchConfigurationCategories = new TreeSet<ILaunchConfigurationCategory>(new ILaunchConfigurationCategoryComparator());
		launchConfigurationCategories.add(defaultCategory);
	}

	public static IRunnerModel getDefault() {
		return runnerModel;
	}

	public Set<ILaunchConfigurationCategory> getLaunchConfigurationCategories() {
		return launchConfigurationCategories;
	}

	public void addLaunchConfigurationNode(ILaunchConfigurationNode launchConfigurationNode) {
		defaultCategory.add(launchConfigurationNode);
		// fireModelChangedEvent() not needed because category change triggers an event
	}

	public void removeLaunchConfigurationNode(ILaunchConfigurationNode launchConfigurationNode) {
		for (ILaunchConfigurationCategory launchConfigurationCategory : launchConfigurationCategories) {
			launchConfigurationCategory.remove(launchConfigurationNode);
		}
		deleteLaunchConfigurationFile(launchConfigurationNode.getLaunchConfiguration());
		fireModelChangedEvent();
	}

	public ILaunchConfigurationCategory addLaunchConfigurationCategory(String name) {
		ILaunchConfigurationCategory category = new LaunchConfigurationCategory();
		category.setName(name);
		category.addCategoryChangeListener(this);

		launchConfigurationCategories.add(category);
		fireModelChangedEvent();
		return category;
	}

	public void removeLaunchConfigurationCategory(ILaunchConfigurationCategory category) {
		// Iterator in order to avoid java.util.ConcurrentModificationException
		for (Iterator<ILaunchConfigurationNode> launchConfigurationNodeIterator = category.getLaunchConfigurationNodes().iterator(); launchConfigurationNodeIterator.hasNext();) {
			ILaunchConfigurationNode launchConfigurationNode = launchConfigurationNodeIterator.next();
			launchConfigurationNodeIterator.remove();
			deleteLaunchConfigurationFile(launchConfigurationNode.getLaunchConfiguration());
		}
		launchConfigurationCategories.remove(category);
		category.removeCategoryChangeListener(this);
		fireModelChangedEvent();
	}

	public ILaunchConfigurationCategory getDefaultCategory() {
		return defaultCategory;
	}

	public ILaunchConfigurationCategory getLaunchConfigurationCategory(String name) {
		for (ILaunchConfigurationCategory launchConfigurationCategory : launchConfigurationCategories) {
			if (launchConfigurationCategory.getName().equals(name)) {
				return launchConfigurationCategory;
			}
		}
		return null;
	}

	public void addModelChangeListener(IModelChangeListener listener) {
		modelChangeListeners.add(listener);
	}

	public void removeModelChangeListener(IModelChangeListener listener) {
		modelChangeListeners.remove(listener);
	}

	private void fireModelChangedEvent() {
		for (IModelChangeListener listener : modelChangeListeners) {
			listener.modelChanged();
		}
	}

	private void deleteLaunchConfigurationFile(ILaunchConfiguration configuration) {
		if (configuration != null) {
			try {
				configuration.delete();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	// for test only
	protected void setLaunchConfigurationCategories(Set<ILaunchConfigurationCategory> launchConfigurationCategories) {
		this.launchConfigurationCategories = launchConfigurationCategories;
	}

	public ILaunchConfigurationNode findLaunchConfigurationNodeBy(ILaunchConfiguration configuration) {
		for (ILaunchConfigurationCategory launchConfigurationCategory : launchConfigurationCategories) {
			// TODO LWA BARY: maybe category.contains() could check also ILaunchConfigs ??
			for (ILaunchConfigurationNode launchConfigurationNode : launchConfigurationCategory.getLaunchConfigurationNodes()) {
				if (launchConfigurationNode.getLaunchConfiguration().equals(configuration)) {
					return launchConfigurationNode;
				}
			}
		}
		return null;
	}

	public void categoryChanged() {
		fireModelChangedEvent();
	}

}
