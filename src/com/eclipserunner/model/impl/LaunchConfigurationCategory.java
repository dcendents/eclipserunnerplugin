package com.eclipserunner.model.impl;

import java.util.HashSet;
import java.util.Set;

import com.eclipserunner.model.IActionEnablement;
import com.eclipserunner.model.ICategoryChangeListener;
import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationChangeListener;
import com.eclipserunner.model.ILaunchConfigurationNode;

/**
 * Container of launch configurations presented in RunnerView tree.
 * 
 * @author vachacz
 */
public class LaunchConfigurationCategory implements ILaunchConfigurationCategory, ILaunchConfigurationChangeListener, IActionEnablement {

	private String name;
	private Set<ILaunchConfigurationNode> launchConfigurationNodes = new HashSet<ILaunchConfigurationNode>();
	private Set<ICategoryChangeListener> categoryChangeListeners = new HashSet<ICategoryChangeListener>();

	private boolean removable  = true;
	private boolean renameable = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		fireCategoryChangedEvent();
	}

	public final Set<ILaunchConfigurationNode> getLaunchConfigurationNodes() {
		return launchConfigurationNodes;
	}

	public void add(ILaunchConfigurationNode launchConfigurationNode) {
		launchConfigurationNode.setLaunchConfigurationCategory(this);
		launchConfigurationNode.setBookmarked(false);
		launchConfigurationNode.addLaunchConfigurationChangeListener(this);

		launchConfigurationNodes.add(launchConfigurationNode);
		fireCategoryChangedEvent();
	}

	public void remove(ILaunchConfigurationNode launchConfigurationNode) {
		launchConfigurationNodes.remove(launchConfigurationNode);
		fireCategoryChangedEvent();
	}

	public void setBookmarked(boolean state) {
		for (ILaunchConfigurationNode node : launchConfigurationNodes) {
			node.setBookmarked(state);
		}
		fireCategoryChangedEvent();
	}

	public boolean contains(ILaunchConfigurationNode launchConfigurationNode) {
		return launchConfigurationNodes.contains(launchConfigurationNode);
	}

	public boolean isEmpty() {
		return launchConfigurationNodes.isEmpty();
	}

	public int size() {
		return launchConfigurationNodes.size();
	}

	public void addCategoryChangeListener(ICategoryChangeListener listener) {
		categoryChangeListeners.add(listener);
	}

	public void removeCategoryChangeListener(ICategoryChangeListener listener) {
		categoryChangeListeners.remove(listener);
	}

	private void fireCategoryChangedEvent() {
		for (ICategoryChangeListener categoryChangeListener : categoryChangeListeners) {
			categoryChangeListener.categoryChanged();
		}
	}

	public void launchConfigurationChanged() {
		fireCategoryChangedEvent();
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
