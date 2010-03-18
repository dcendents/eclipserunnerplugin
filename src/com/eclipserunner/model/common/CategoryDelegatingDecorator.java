package com.eclipserunner.model.common;

import java.util.Collection;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ICategoryNodeChangeListener;
import com.eclipserunner.model.ILaunchNode;

public class CategoryDelegatingDecorator extends AbstractCategoryNode implements ICategoryNode {

	protected final ICategoryNode category;

	public CategoryDelegatingDecorator(ICategoryNode category) {
		this.category = category;
	}

	public void add(ILaunchNode launchNode) {
		category.add(launchNode);
	}

	public void addCategoryNodeChangeListener(ICategoryNodeChangeListener categoryNodeChangeListener) {
		category.addCategoryNodeChangeListener(categoryNodeChangeListener);
	}

	public boolean contains(ILaunchNode launchNode) {
		return category.contains(launchNode);
	}

	public Collection<ILaunchNode> getLaunchNodes() {
		return category.getLaunchNodes();
	}

	public String getName() {
		return category.getName();
	}

	public boolean isEmpty() {
		return category.isEmpty();
	}

	public void remove(ILaunchNode launchNode) {
		category.remove(launchNode);
	}

	public void removeCategoryNodeChangeListener(ICategoryNodeChangeListener categoryNodeChangeListener) {
		category.removeCategoryNodeChangeListener(categoryNodeChangeListener);
	}

	public void setBookmarked(boolean state) {
		category.setBookmarked(state);
	}

	public void setName(String name) {
		category.setName(name);
	}

	public int size() {
		return category.size();
	}

	public boolean isRemovable() {
		return category.isRemovable();
	}

	public boolean isRenamable() {
		return category.isRenamable();
	}

}
