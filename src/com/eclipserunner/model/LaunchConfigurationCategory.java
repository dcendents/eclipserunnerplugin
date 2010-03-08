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
	private Set<ILaunchConfiguration> launchConfigurationSet = new HashSet<ILaunchConfiguration>();
	private Set<ICategoryChangeListener> listeners = new HashSet<ICategoryChangeListener>();

	public final Set<ILaunchConfiguration> getLaunchConfigurationSet() {
		return launchConfigurationSet;
	}

	public void add(ILaunchConfiguration launchConfiguration) {
		launchConfigurationSet.add(launchConfiguration);
		fireCategoryChangedEvent();
	}

	public void remove(ILaunchConfiguration launchConfiguration) {
		launchConfigurationSet.remove(launchConfiguration);
		fireCategoryChangedEvent();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		fireCategoryChangedEvent();
	}

	public int size() {
		return launchConfigurationSet.size();
	}
	
	public boolean contains(ILaunchConfiguration configuration) {
		return launchConfigurationSet.contains(configuration);
	}
	
	public boolean isEmpty() {
		return launchConfigurationSet.isEmpty();
	}

	public void addCategoryChangeListener(ICategoryChangeListener listener) {
		listeners.add(listener);
	}

	public void removeCategoryChangeListener(ICategoryChangeListener listener) {
		listeners.remove(listener);
	}
	
	private void fireCategoryChangedEvent() {
		for (ICategoryChangeListener listener : listeners) {
			listener.categoryChanged();
		}
	}

}
