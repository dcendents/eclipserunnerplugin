package com.eclipserunner.model;

import java.util.Collection;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface IRunnerModel {

	void addLaunchNode(ILaunchNode launchNode);
	void removeLaunchNode(ILaunchNode launchNode);

	ICategoryNode addCategoryNode(String categoryNodeName);
	void removeCategoryNode(ICategoryNode categoryNode);

	ICategoryNode getCategoryNode(String categoryNodeName);
	Collection<ICategoryNode> getCategoryNodes();

	void addModelChangeListener(IModelChangeListener modelChangeListener);
	void removeModelChangeListener(IModelChangeListener modelChangeListener);

	boolean isDefaultCategoryNodeVisible();
	void setDefaultCategoryNodeVisible(boolean visible);
	ICategoryNode getDefaultCategoryNode();

	ILaunchNode findLaunchNodeBy(ILaunchConfiguration launchConfiguration);

}
