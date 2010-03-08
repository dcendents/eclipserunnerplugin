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
	private Set<ILaunchConfiguration> launchConfigurations = new HashSet<ILaunchConfiguration>();
	private Set<ICategoryChangeListener> categoryChangeListeners = new HashSet<ICategoryChangeListener>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		fireCategoryChangedEvent();
	}

	public final Set<ILaunchConfiguration> getLaunchConfigurations() {
		return launchConfigurations;
	}

	public void add(ILaunchConfiguration launchConfiguration) {
		launchConfigurations.add(launchConfiguration);
		fireCategoryChangedEvent();
	}

	public void remove(ILaunchConfiguration launchConfiguration) {
		launchConfigurations.remove(launchConfiguration);
		fireCategoryChangedEvent();
	}

	public void addCategoryChangeListener(ICategoryChangeListener listener) {
		categoryChangeListeners.add(listener);
	}

	public void removeCategoryChangeListener(ICategoryChangeListener listener) {
		categoryChangeListeners.remove(listener);
	}

	public boolean contains(ILaunchConfiguration configuration) {
		return launchConfigurations.contains(configuration);
	}

	public boolean isEmpty() {
		return launchConfigurations.isEmpty();
	}

	public int size() {
		return launchConfigurations.size();
	}

	private void fireCategoryChangedEvent() {
		for (ICategoryChangeListener categoryChangeListener : categoryChangeListeners) {
			categoryChangeListener.categoryChanged();
		}
	}

}
