package com.eclipserunner.model.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;

import com.eclipserunner.model.IActionEnablement;
import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNodeChangeListener;
import com.eclipserunner.model.ILaunchNode;

/**
 * @author vachacz
 */
public class LaunchNode implements ILaunchNode, IActionEnablement {

	private ILaunchConfiguration launchConfiguration;
	private ICategoryNode categoryNode;
	private boolean bookmarked;

	private Set<ILaunchNodeChangeListener> launchNodeChangeListeners = new HashSet<ILaunchNodeChangeListener>();

	public LaunchNode() {
	}

	public void setLaunchConfiguration(ILaunchConfiguration launchConfiguration) {
		this.launchConfiguration = launchConfiguration;
	}

	public ILaunchConfiguration getLaunchConfiguration() {
		return launchConfiguration;
	}

	public void setCategoryNode(ICategoryNode categoryNode) {
		this.categoryNode = categoryNode;
	}

	public ICategoryNode getCategoryNode() {
		return categoryNode;
	}

	public void setBookmarked(boolean state) {
		this.bookmarked = state;
		fireLaunchNodeChangedEvent();
	}

	public boolean isBookmarked() {
		return bookmarked;
	}

	public void addLaunchNodeChangeListener(ILaunchNodeChangeListener launchNodeChangeListener) {
		launchNodeChangeListeners.add(launchNodeChangeListener);
	}

	public void removeLaunchNodeChangeListener(ILaunchNodeChangeListener launchNodeChangeListener) {
		launchNodeChangeListeners.remove(launchNodeChangeListener);
	}

	private void fireLaunchNodeChangedEvent() {
		for (ILaunchNodeChangeListener launchNodeChangeListener : launchNodeChangeListeners) {
			launchNodeChangeListener.launchNodeChanged();
		}
	}

	public boolean isRemovable() {
		return true;
	}

	public boolean isRenamable() {
		return true;
	}

}
