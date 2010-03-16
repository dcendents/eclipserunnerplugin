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

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ICategoryNodeChangeListener;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.IModelChangeListener;
import com.eclipserunner.model.IRunnerModel;

/**
 * Class implementing {@link IRunnerModel}
 *
 * @author vachacz
 */
public class RunnerModel implements IRunnerModel, ICategoryNodeChangeListener {

	class ICategoryNodeComparator implements Comparator<ICategoryNode> {
		public int compare(ICategoryNode category1,	ICategoryNode category2) {
			if (RunnerModel.this.getDefaultCategoryNode().equals(category1)) {
				return -1;
			} else if (RunnerModel.this.getDefaultCategoryNode().equals(category2)) {
				return 1;
			} else {
				return category1.getName().compareTo(category2.getName());
			}
		}
	}

	private List<IModelChangeListener> modelChangeListeners = new ArrayList<IModelChangeListener>();
	private Set<ICategoryNode> categoryNodes;

	private ICategoryNode defaultCategoryNode;

	private boolean defaultCategoryNodeVisible;

	public RunnerModel() {

		CategoryNode category = new CategoryNode();
		category.setName(Message_uncategorized);
		category.addCategoryNodeChangeListener(this);
		category.setRemovable(false);
		category.setRenameable(false);

		defaultCategoryNode = category;

		categoryNodes = new TreeSet<ICategoryNode>(new ICategoryNodeComparator());
		categoryNodes.add(defaultCategoryNode);
	}

	public Set<ICategoryNode> getCategoryNodes() {
		return categoryNodes;
	}

	public void addLaunchNode(ILaunchNode launchNode) {
		defaultCategoryNode.add(launchNode);
		// fireModelChangedEvent() not needed because category change triggers an event
	}

	public void removeLaunchNode(ILaunchNode launchNode) {
		for (ICategoryNode categoryNode : categoryNodes) {
			categoryNode.remove(launchNode);
		}
		deleteLaunchConfigurationFile(launchNode.getLaunchConfiguration());
		fireModelChangedEvent();
	}

	public ICategoryNode addCategoryNode(String categoryNodeName) {
		ICategoryNode category = new CategoryNode();
		category.setName(categoryNodeName);
		category.addCategoryNodeChangeListener(this);

		categoryNodes.add(category);
		fireModelChangedEvent();
		return category;
	}

	public void removeCategoryNode(ICategoryNode categoryNode) {
		// Iterator in order to avoid java.util.ConcurrentModificationException
		for (Iterator<ILaunchNode> launchNodeIterator = categoryNode.getLaunchNodes().iterator(); launchNodeIterator.hasNext();) {
			ILaunchNode launchNode = launchNodeIterator.next();
			launchNodeIterator.remove();
			deleteLaunchConfigurationFile(launchNode.getLaunchConfiguration());
		}
		categoryNodes.remove(categoryNode);
		categoryNode.removeCategoryNodeChangeListener(this);
		fireModelChangedEvent();
	}

	public ICategoryNode getDefaultCategoryNode() {
		return defaultCategoryNode;
	}

	public ICategoryNode getCategoryNode(String name) {
		for (ICategoryNode categoryNode : categoryNodes) {
			if (categoryNode.getName().equals(name)) {
				return categoryNode;
			}
		}
		return null;
	}

	public void addModelChangeListener(IModelChangeListener modelChangeListener) {
		modelChangeListeners.add(modelChangeListener);
	}

	public void removeModelChangeListener(IModelChangeListener modelChangeListener) {
		modelChangeListeners.remove(modelChangeListener);
	}

	private void fireModelChangedEvent() {
		for (IModelChangeListener modelChangeListener : modelChangeListeners) {
			modelChangeListener.modelChanged();
		}
	}

	private void deleteLaunchConfigurationFile(ILaunchConfiguration launchConfiguration) {
		if (launchConfiguration != null) {
			try {
				launchConfiguration.delete();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	// for test only
	protected void setLaunchConfigurationCategories(Set<ICategoryNode> categoryNodes) {
		this.categoryNodes = categoryNodes;
	}

	public ILaunchNode findLaunchNodeBy(ILaunchConfiguration launchConfiguration) {
		for (ICategoryNode launchConfigurationCategory : categoryNodes) {
			// TODO LWA BARY: maybe category.contains() could check also ILaunchConfigs ??
			for (ILaunchNode launchConfigurationNode : launchConfigurationCategory.getLaunchNodes()) {
				if (launchConfigurationNode.getLaunchConfiguration().equals(launchConfiguration)) {
					return launchConfigurationNode;
				}
			}
		}
		return null;
	}

	public void categoryNodeChanged() {
		fireModelChangedEvent();
	}

	public boolean isDefaultCategoryNodeVisible() {
		return defaultCategoryNodeVisible;
	}

	public void setDefaultCategoryNodeVisible(boolean visible) {
		this.defaultCategoryNodeVisible = visible;
		fireModelChangedEvent();
	}

}
