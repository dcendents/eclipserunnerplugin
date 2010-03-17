package com.eclipserunner.model;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.RunnerPluginPrererenceConstants;
import com.eclipserunner.model.adapters.RunnerModelTreeAdapter;
import com.eclipserunner.model.adapters.RunnerModelTreeWithTypesAdapter;
import com.eclipserunner.model.filters.BookmarkFilter;
import com.eclipserunner.model.filters.DefaultCategoryFilter;
import com.eclipserunner.model.filters.RunnerModelFilteringDecorator;
import com.eclipserunner.model.impl.RunnerModel;
import com.eclipserunner.views.TreeMode;

/**
 * Access point for all model related instances.
 *
 * @author vachacz, bary
 */
public class RunnerModelProvider {

	private static final RunnerModelProvider INSTANCE = new RunnerModelProvider();

	private IRunnerModel runnerModel;
	private IRunnerModel filteredRunnerModel;

	private INodeFilterChain filterChain;

	private INodeFilter bookmarkFilter;
	private INodeFilter defaultCategoryFilter;
	private INodeFilter projectFilter;
	private INodeFilter workingSetFilter;

	private ITreeContentProvider contentProvider;

	// Singleton pattern
	private RunnerModelProvider() {
		runnerModel = new RunnerModel();

		RunnerModelFilteringDecorator runnerModelDecorator = new RunnerModelFilteringDecorator(runnerModel);
		filteredRunnerModel = runnerModelDecorator;
		filterChain = runnerModelDecorator;

		bookmarkFilter = new BookmarkFilter();
		defaultCategoryFilter = new DefaultCategoryFilter();

		// TODO BARY LWA filter implementation
		projectFilter = new INodeFilter() {
			public boolean filter(ILaunchNode launchNode) {
				return false;
			}
			public boolean filter(ICategoryNode categoryNode) {
				return false;
			}
		};

		// TODO BARY LWA filter implementation
		workingSetFilter = new INodeFilter() {
			public boolean filter(ILaunchNode launchNode) {
				return false;
			}
			public boolean filter(ICategoryNode categoryNode) {
				return false;
			}
		};
	}

	public static RunnerModelProvider getInstance() {
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
		boolean treeModeState = false;
		switch (treeMode) {
			case FLAT_MODE:
				contentProvider = new RunnerModelTreeAdapter(filteredRunnerModel);
				treeModeState = false;
				break;
			case HIERARCHICAL_MODE:
				contentProvider = new RunnerModelTreeWithTypesAdapter(filteredRunnerModel);
				treeModeState = true;
				break;
			default:
				break;
		}
		setPreferenceValue(RunnerPluginPrererenceConstants.DEFAULT_CATEGORY_VISIBLE, treeModeState);
	}

	public IContentProvider getTreeContentProvider() {
		return contentProvider;
	}

	public void useBookmarkFilter(boolean flag) {
		if (flag) {
			filterChain.addFilter(bookmarkFilter);
		} else {
			filterChain.removeFilter(bookmarkFilter);
		}
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

	private void setPreferenceValue(String property, boolean value) {
		RunnerPlugin.getDefault().getPreferenceStore().setValue(RunnerPluginPrererenceConstants.DEFAULT_CATEGORY_VISIBLE, value);
	}

}
