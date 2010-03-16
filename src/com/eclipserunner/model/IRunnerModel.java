package com.eclipserunner.model;

import java.util.Collection;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface IRunnerModel {

	void addLaunchNode(ILaunchNode launchNode);
	void removeLaunchNode(ILaunchNode launchNode);

	ICategoryNode addCategoryNode(String categoryNodeName);
	void removeCategoryNode(ICategoryNode categoryNode);

	// TODO remove
	ICategoryNode getCategoryNode(String categoryNodeName);
	Collection<ICategoryNode> getCategoryNodes();

	void addModelChangeListener(IModelChangeListener modelChangeListener);
	void removeModelChangeListener(IModelChangeListener modelChangeListener);

	// TODO remove
	boolean isDefaultCategoryNodeVisible();

	// TODO remove
	void setDefaultCategoryNodeVisible(boolean visible);
	ICategoryNode getDefaultCategoryNode();

	// TODO remove
	ILaunchNode findLaunchNodeBy(ILaunchConfiguration launchConfiguration);

}
