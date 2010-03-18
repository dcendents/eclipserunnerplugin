package com.eclipserunner.model;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.eclipserunner.PrererenceConstants;
import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.adapters.RunnerModelTreeAdapter;
import com.eclipserunner.model.adapters.RunnerModelTreeWithTypesAdapter;
import com.eclipserunner.model.filters.BookmarkFilter;
import com.eclipserunner.model.filters.DefaultCategoryFilter;
import com.eclipserunner.model.filters.ProjectFilter;
import com.eclipserunner.model.filters.RunnerModelFilteringDecorator;
import com.eclipserunner.model.filters.WorkingSetFilter;
import com.eclipserunner.model.impl.RunnerModel;
import com.eclipserunner.views.TreeMode;

/**
 * Access point for all model related instances.
 *
 * @author vachacz, bary
 */
public class RunnerModelProvider {

	private static RunnerModelProvider INSTANCE;

	private IRunnerModel runnerModel;
	private IRunnerModel filteredRunnerModel;

	private INodeFilterChain filterChain;

	private INodeFilter bookmarkFilter;
	private INodeFilter defaultCategoryFilter;
	private INodeFilter projectFilter;
	private INodeFilter workingSetFilter;

	private ITreeContentProvider contentProvider;

	private TreeMode treeMode;

	// Singleton pattern
	private RunnerModelProvider() {
		runnerModel = new RunnerModel();

		RunnerModelFilteringDecorator runnerModelDecorator = new RunnerModelFilteringDecorator(runnerModel);
		filteredRunnerModel = runnerModelDecorator;
		filterChain = runnerModelDecorator;

		bookmarkFilter = new BookmarkFilter();
		defaultCategoryFilter = new DefaultCategoryFilter();
		projectFilter = new ProjectFilter();
		workingSetFilter = new WorkingSetFilter();

		initializeFilterChain();
		initializeTreeModeAdapter();
	}

	private void initializeTreeModeAdapter() {
		String treeModeString = RunnerPlugin.getDefault().getPreferenceStore().getString(PrererenceConstants.TREE_MODE);
		TreeMode treeMode = null;
		try {
			treeMode = TreeMode.valueOf(treeModeString);
		} catch (Exception e) {
			treeMode = TreeMode.FLAT_MODE;
		}
		setTreeMode(treeMode);
	}

	private void initializeFilterChain() {
		boolean defaultCategoryFilter = getPreferenceBoolean(PrererenceConstants.DEFAULT_CATEGORY_VISIBLE);
		useDefaultCategoryFilter(defaultCategoryFilter);

		boolean bookmarkFilter = getPreferenceBoolean(PrererenceConstants.BOOKMARK_FILTER_ENABLE);
		useBookmarkFilter(bookmarkFilter);
	}

	public static RunnerModelProvider getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RunnerModelProvider();
		}
		return INSTANCE;
	}

	public IRunnerModel getDefaultModel() {
		return runnerModel;
	}

	public IRunnerModel getFilteredModel() {
		return filteredRunnerModel;
	}

	public void useFlatTreeType() {
		setTreeMode(TreeMode.FLAT_MODE);
	}

	public void useHierarchicalTreeType() {
		setTreeMode(TreeMode.HIERARCHICAL_MODE);
	}

	public void setTreeMode(TreeMode treeMode) {
		this.treeMode = treeMode;
		switch (treeMode) {
			case FLAT_MODE:
				contentProvider = new RunnerModelTreeAdapter(filteredRunnerModel);
				break;
			case HIERARCHICAL_MODE:
				contentProvider = new RunnerModelTreeWithTypesAdapter(filteredRunnerModel);
				break;
			default:
				break;
		}
		setPreferenceValue(PrererenceConstants.TREE_MODE, treeMode.toString());
	}

	public IContentProvider getTreeContentProvider() {
		return contentProvider;
	}

	public void useBookmarkFilter(boolean useBookmarkFilter) {
		if (useBookmarkFilter) {
			filterChain.addFilter(bookmarkFilter);
		} else {
			filterChain.removeFilter(bookmarkFilter);
		}
		setPreferenceValue(PrererenceConstants.BOOKMARK_FILTER_ENABLE, useBookmarkFilter);
	}

	public void useDefaultCategoryFilter(boolean flag) {
		if (flag) {
			filterChain.addFilter(defaultCategoryFilter);
		} else {
			filterChain.removeFilter(defaultCategoryFilter);
		}
	}

	public void useProjectFilter(boolean flag) {
		if (flag) {
			filterChain.removeFilter(workingSetFilter);
			filterChain.addFilter(projectFilter);
		} else {
			filterChain.removeFilter(projectFilter);
		}
	}

	public void useWorkingSetFilter(boolean flag) {
		if (flag) {
			filterChain.removeFilter(projectFilter);
			filterChain.addFilter(workingSetFilter);
		} else {
			filterChain.removeFilter(workingSetFilter);
		}
	}

	public boolean isBookmarkFilterActive() {
		return filterChain.isFilterActive(bookmarkFilter);
	}

	public boolean isDefaultCategoryFilterActive() {
		return filterChain.isFilterActive(defaultCategoryFilter);
	}

	public TreeMode getCurrentTreeMode() {
		return treeMode;
	}

	private void setPreferenceValue(String property, boolean value) {
		RunnerPlugin.getDefault().getPreferenceStore().setValue(property, value);
	}

	private void setPreferenceValue(String property, String value) {
		RunnerPlugin.getDefault().getPreferenceStore().setValue(property, value);
	}

	private boolean getPreferenceBoolean(String property) {
		return RunnerPlugin.getDefault().getPreferenceStore().getBoolean(property);
	}

}
