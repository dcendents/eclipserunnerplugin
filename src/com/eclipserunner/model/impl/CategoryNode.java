package com.eclipserunner.model.impl;

import java.util.HashSet;
import java.util.Set;

import com.eclipserunner.model.IActionEnablement;
import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ICategoryNodeChangeListener;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.ILaunchNodeChangeListener;
import com.eclipserunner.model.common.AbstractCategoryNode;

/**
 * Container of launch configurations presented in RunnerView tree.
 *
 * @author vachacz
 */
public class CategoryNode extends AbstractCategoryNode implements ICategoryNode, ILaunchNodeChangeListener, IActionEnablement {

	private String name;
	private Set<ILaunchNode> launchNodes = new HashSet<ILaunchNode>();
	private Set<ICategoryNodeChangeListener> categoryNodeChangeListeners = new HashSet<ICategoryNodeChangeListener>();

	private boolean removable  = true;
	private boolean renameable = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		fireCategoryNodeChangedEvent();
	}

	public final Set<ILaunchNode> getLaunchNodes() {
		return launchNodes;
	}

	public void add(ILaunchNode launchNode) {
		launchNode.setCategoryNode(this);
		launchNode.addLaunchNodeChangeListener(this);

		launchNodes.add(launchNode);
		fireCategoryNodeChangedEvent();
	}

	public void remove(ILaunchNode launchNode) {
		launchNodes.remove(launchNode);
		fireCategoryNodeChangedEvent();
	}

	public void setBookmarked(boolean state) {
		for (ILaunchNode launchNode : launchNodes) {
			launchNode.setBookmarked(state);
		}
		fireCategoryNodeChangedEvent();
	}

	public void addCategoryNodeChangeListener(ICategoryNodeChangeListener categoryNodeChangeListener) {
		categoryNodeChangeListeners.add(categoryNodeChangeListener);
	}

	public void removeCategoryNodeChangeListener(ICategoryNodeChangeListener categoryNodeChangeListener) {
		categoryNodeChangeListeners.remove(categoryNodeChangeListener);
	}

	private void fireCategoryNodeChangedEvent() {
		for (ICategoryNodeChangeListener categoryNodeChangeListener : categoryNodeChangeListeners) {
			categoryNodeChangeListener.categoryNodeChanged();
		}
	}

	public void launchNodeChanged() {
		fireCategoryNodeChangedEvent();
	}

	public boolean isRemovable() {
		return removable;
	}

	public boolean isRenamable() {
		return renameable;
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	public void setRenameable(boolean renameable) {
		this.renameable = renameable;
	}

}
