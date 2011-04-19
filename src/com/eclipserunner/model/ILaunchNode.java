package com.eclipserunner.model;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author vachacz
 */
public interface ILaunchNode extends IBookmarkable {

	ILaunchConfiguration getLaunchConfiguration();
	void setLaunchConfiguration(ILaunchConfiguration launchConfiguration);

	ICategoryNode getCategoryNode();
	void setCategoryNode(ICategoryNode categoryNode);

	void addLaunchNodeChangeListener(ILaunchNodeChangeListener launchNodeChangeListener);
	void removeLaunchNodeChangeListener(ILaunchNodeChangeListener launchNodeChangeListener);

}
