package com.eclipserunner.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * Container of launch configurations presented in RunnerView tree.
 * 
 * @author vachacz
 */
public class LaunchConfigurationCategory implements ILaunchConfigurationCategory {

	private String name;
	private Set<ILaunchConfigurationNode> launchConfigurationNodes = new HashSet<ILaunchConfigurationNode>();
	private Set<ICategoryChangeListener> categoryChangeListeners = new HashSet<ICategoryChangeListener>();

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

	@Override
	public void add(ILaunchConfiguration newConfiguration) {
		
		// TODO LWA builder
		LaunchConfigurationNode node = new LaunchConfigurationNode();
		node.setLaunchConfiguration(newConfiguration);
		node.setLaunchConfigurationCategory(this);
		node.unbookmark();
		
		launchConfigurationNodes.add(node);
	}
	
	public void add(ILaunchConfigurationNode launchConfigurationNode) {
		launchConfigurationNodes.add(launchConfigurationNode);
		fireCategoryChangedEvent();
	}

	public void remove(ILaunchConfigurationNode launchConfiguration) {
		launchConfigurationNodes.remove(launchConfiguration);
		fireCategoryChangedEvent();
	}

	public void addCategoryChangeListener(ICategoryChangeListener listener) {
		categoryChangeListeners.add(listener);
	}

	public void removeCategoryChangeListener(ICategoryChangeListener listener) {
		categoryChangeListeners.remove(listener);
	}

	public void bookmarkAll() {
		for (ILaunchConfigurationNode node : launchConfigurationNodes) {
			node.bookmark();
		}
		fireCategoryChangedEvent();
	}

	public void unbookmarkAll() {
		for (ILaunchConfigurationNode node : launchConfigurationNodes) {
			node.unbookmark();
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

	private void fireCategoryChangedEvent() {
		for (ICategoryChangeListener categoryChangeListener : categoryChangeListeners) {
			categoryChangeListener.categoryChanged();
		}
	}

	@Override
	public void remove(ILaunchConfiguration configuration) {
		// TODO LWA smart equals + tests
		for (ILaunchConfigurationNode node : launchConfigurationNodes) {
			if (node.getLaunchConiguration().equals(configuration)) {
				launchConfigurationNodes.remove(node);
			}
		}		
	}

}
