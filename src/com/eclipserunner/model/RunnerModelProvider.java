package com.eclipserunner.model;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.eclipserunner.model.adapters.RunnerModelTreeAdapter;
import com.eclipserunner.model.adapters.RunnerModelTreeWithTypesAdapter;
import com.eclipserunner.model.filters.BookmarkFilter;
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
		
		// TODO BARY LWA filter implementation
		defaultCategoryFilter = new INodeFilter() {
			public boolean filter(ILaunchNode launchNode) {
				return false;
			}
			public boolean filter(ICategoryNode categoryNode) {
				return false;
			}
		};
		
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
		contentProvider = new RunnerModelTreeAdapter(runnerModel);
	}

	public void useHierarchicalTreeType() {
		contentProvider = new RunnerModelTreeWithTypesAdapter(runnerModel);
	}

	public void setTreeMode(TreeMode treeMode) {
		switch (treeMode) {
			case FLAT_MODE:
				contentProvider = new RunnerModelTreeAdapter(runnerModel);
				break;
			case HIERARCHICAL_MODE:
				contentProvider = new RunnerModelTreeWithTypesAdapter(runnerModel);
				break;
			default:
				break;
		}
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
	
}
