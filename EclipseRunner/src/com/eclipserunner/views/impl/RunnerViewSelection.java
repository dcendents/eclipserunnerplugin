package com.eclipserunner.views.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.eclipserunner.model.IActionEnablement;
import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.ILaunchTypeNode;
import com.eclipserunner.model.INodeSelection;
import com.eclipserunner.utils.SelectionUtils;

public class RunnerViewSelection implements INodeSelection {

	private final TreeViewer treeViewer;

	public RunnerViewSelection(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	@SuppressWarnings("unchecked")
	public <T> T getFirstElementAs(Class<T> clazz) {
		return (T) getSelection().getFirstElement();
	}

	@SuppressWarnings("unchecked")
	public List<Object> asList() {
		return getSelection().toList();
	}

	public boolean ofSameNodeType() {
		return SelectionUtils.isSameTypeNodeSelection(getSelection());
	}

	public boolean ofSingleNode() {
		return SelectionUtils.isSingleNodeSelection(getSelection());
	}

	public <T> List<T> byType(Class<T> type) {
		return SelectionUtils.getAllSelectedItemsByType(getSelection(), type);
	}

	public boolean firstElementHasType(Class<?> clazz) {
		Object firstElement = getSelection().getFirstElement();
		if (firstElement != null) {
			return clazz.isAssignableFrom(getSelection().getFirstElement().getClass());
		}
		return false;
	}
	
	public List<ILaunchNode> getSelectedLaunchNodes() {
		if (ofSameNodeType() && firstElementHasType(ILaunchNode.class)) {
			return byType(ILaunchNode.class);
		}
		return Collections.emptyList();
	}

	public List<ILaunchTypeNode> getSelectedLaunchTypeNodes() {
		if (ofSameNodeType() && firstElementHasType(ILaunchTypeNode.class)) {
			return byType(ILaunchTypeNode.class);
		}
		return Collections.emptyList();
	}
	
	public List<ICategoryNode> getSelectedCategoryNodes() {
		if (ofSameNodeType() && firstElementHasType(ICategoryNode.class)) {
			return byType(ICategoryNode.class);
		}
		return Collections.emptyList();
	}
	
	public ILaunchNode getSelectedLaunchNode() {
		return (ILaunchNode) getSelection().getFirstElement();
	}

	public ICategoryNode getSelectedCategoryNode() {
		return (ICategoryNode) getSelection().getFirstElement();
	}

	public boolean canBeLaunched() {
		return ofSingleNode() && firstElementHasType(ILaunchNode.class);
	}

	public boolean canBeRenamed() {
		if (ofSameNodeType() && ofSingleNode()) {
			Object selectedObject = getSelection().getFirstElement();
			if (selectedObject instanceof IActionEnablement) {
				return ((IActionEnablement) selectedObject).isRenamable();
			}
		}
		return false;
	}

	public boolean canBeRemoved() {
		if (! ofSameNodeType()) {
			return false;
		}
		for (Object selectedObject : asList()) {
			if (selectedObject instanceof IActionEnablement) {
				if (!((IActionEnablement) selectedObject).isRemovable()) {
					return false;
				}
			}
			else {
				return false;
			}
		}
		return true;
	}

	public boolean canBeBookmarked() {
		return ofSameNodeType();
	}

	private IStructuredSelection getSelection() {
		return (IStructuredSelection) treeViewer.getSelection();
	}
	
}
