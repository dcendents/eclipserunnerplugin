package com.eclipserunner.model.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeFilter;
import com.eclipserunner.model.INodeFilterChain;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.impl.CategoryNode;

public class RunnerModelFilteringDecorator extends RunnerModelDecoratorAdapter implements INodeFilterChain {

	private List<INodeFilter> nodeFilterList = new ArrayList<INodeFilter>();

	public RunnerModelFilteringDecorator(IRunnerModel runnerModel) {
		super(runnerModel);
	}

	@Override
	public Collection<ICategoryNode> getCategoryNodes() {
		Collection<ICategoryNode> decoratedCategories = new ArrayList<ICategoryNode>();
		for (ICategoryNode categoryNode : runnerModel.getCategoryNodes()) {
			decoratedCategories.add(decorateCategory(categoryNode));
		}
		return decoratedCategories;
	}

	@Override
	public ICategoryNode getDefaultCategoryNode() {
		return decorateCategory(runnerModel.getDefaultCategoryNode());
	}

	private ICategoryNode decorateCategory(ICategoryNode category) {
		Collection<ILaunchNode> filteredNodes = new ArrayList<ILaunchNode>();
		for (ILaunchNode launchNode : category.getLaunchNodes()) {
			boolean filtered = filterLaunchNode(launchNode);
			if (!filtered) {
				filteredNodes.add(launchNode);
			}
		}
		// TODO LWA new method ??
		// we need category node decorator hier, because now drop does not work
		CategoryNode node = new CategoryNode();
		node.setLaunchNodes(filteredNodes);
		node.setName(category.getName());
		return node;
	}

	private boolean filterLaunchNode(ILaunchNode launchNode) {
		for (INodeFilter nodeFilter : nodeFilterList) {
			if (nodeFilter.filter(launchNode)) {
				return true;
			}
		}
		return false;
	}

	public void addFilter(INodeFilter filter) {
		nodeFilterList.add(filter);
	}

	public void removeFilter(INodeFilter filter) {
		nodeFilterList.remove(filter);
	}

}
