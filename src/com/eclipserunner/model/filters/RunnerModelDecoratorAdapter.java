package com.eclipserunner.model.filters;

import java.util.Collection;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.IModelChangeListener;
import com.eclipserunner.model.IRunnerModel;

/**
 * Base class for all IRunnerModel decorators. Implements all IRunnerModel methods as
 * delegating calls to decorated runner model instance.
 *
 * @author vachacz
 */
public class RunnerModelDecoratorAdapter implements IRunnerModel {

	protected final IRunnerModel runnerModel;

	public RunnerModelDecoratorAdapter(IRunnerModel runnerModel) {
		this.runnerModel = runnerModel;
	}

	public ICategoryNode addCategoryNode(String categoryNodeName) {
		return runnerModel.addCategoryNode(categoryNodeName);
	}

	public void addLaunchNode(ILaunchNode launchNode) {
		runnerModel.addLaunchNode(launchNode);
	}

	public void addModelChangeListener(IModelChangeListener modelChangeListener) {
		runnerModel.addModelChangeListener(modelChangeListener);
	}

	public Collection<ICategoryNode> getCategoryNodes() {
		return runnerModel.getCategoryNodes();
	}

	public ICategoryNode getDefaultCategoryNode() {
		return runnerModel.getDefaultCategoryNode();
	}

	public boolean isDefaultCategoryNodeVisible() {
		return runnerModel.isDefaultCategoryNodeVisible();
	}

	public void removeCategoryNode(ICategoryNode categoryNode) {
		runnerModel.removeCategoryNode(categoryNode);
	}

	public void removeLaunchNode(ILaunchNode launchNode) {
		runnerModel.removeLaunchNode(launchNode);
	}

	public void removeModelChangeListener(IModelChangeListener modelChangeListener) {
		runnerModel.removeModelChangeListener(modelChangeListener);
	}

	public void setDefaultCategoryNodeVisible(boolean visible) {
		runnerModel.setDefaultCategoryNodeVisible(visible);
	}

}
