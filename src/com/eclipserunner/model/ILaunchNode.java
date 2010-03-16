package com.eclipserunner.model;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author vachacz
 */
public interface ILaunchNode {

	ILaunchConfiguration getLaunchConfiguration();
	void setLaunchConfiguration(ILaunchConfiguration launchConfiguration);

	ICategoryNode getCategoryNode();
	void setCategoryNode(ICategoryNode categoryNode);

	boolean isBookmarked();
	void setBookmarked(boolean state);

	void addLaunchNodeChangeListener(ILaunchNodeChangeListener launchNodeChangeListener);
	void removeLaunchNodeChangeListener(ILaunchNodeChangeListener launchNodeChangeListener);

}
