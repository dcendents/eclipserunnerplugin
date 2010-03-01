package com.eclipserunner.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * Container of launch configurations presented in RunnerView tree.
 * 
 * @author vachacz
 */
public class LaunchConfigrationCategory {

	private String categoryName;
	private Set<ILaunchConfiguration> launchConfigurationSet = new HashSet<ILaunchConfiguration>();

	public void setLaunchConfigurationSet(Set<ILaunchConfiguration> launchConfigurationList) {
		this.launchConfigurationSet = launchConfigurationList;
	}

	public Set<ILaunchConfiguration> getLaunchConfigurationSet() {
		return launchConfigurationSet;
	}

	public void add(ILaunchConfiguration launchConfiguration) {
		launchConfigurationSet.add(launchConfiguration);
	}

	public Object[] toArray() {
		return launchConfigurationSet.toArray();
	}

	public boolean contains(Object arg0) {
		return launchConfigurationSet.contains(arg0);
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
