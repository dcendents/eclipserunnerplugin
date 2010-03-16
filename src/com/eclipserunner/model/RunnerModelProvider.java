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
 * @author vachacz
 */
public class RunnerModelProvider {

	private static INodeFilter bookmarkFilter = new BookmarkFilter();
//	private static INodeFilter defaultCategoryFilter = ...;
//	private static INodeFilter projectFilter = ...;
//	private static INodeFilter workspaceFilter = ...;
	private static INodeFilterChain filterChain = null;

	private static IRunnerModel runnerModel = null;
	private static IRunnerModel filteredRunnerModel = null;
	private static ITreeContentProvider contentProvider = null;

	public static IRunnerModel getDefaultModel() {
		if (runnerModel == null) {
			initializeModel();
		}
		return runnerModel;
	}

	public static IRunnerModel getFilteredModel() {
		if (filteredRunnerModel != null) {
			RunnerModelFilteringDecorator runnerModelDecorator = createRunnerModelFilteringDecorator();

			filterChain = runnerModelDecorator;
			filteredRunnerModel = runnerModelDecorator;
		}
		return filteredRunnerModel;
	}

	private static RunnerModelFilteringDecorator createRunnerModelFilteringDecorator() {
		return new RunnerModelFilteringDecorator(getDefaultModel());
	}

	public static void initializeModel() {
		initializeModel(new RunnerModel());
	}

	public static void initializeModel(IRunnerModel model) {
		runnerModel = model;
	}

	public static void useFlatTreeType() {
		contentProvider = new RunnerModelTreeAdapter(runnerModel);
	}

	public static void useHierarchicalTreeType() {
		contentProvider = new RunnerModelTreeWithTypesAdapter(runnerModel);
	}

	public static void setTreeMode(TreeMode treeMode) {
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

	public static IContentProvider getTreeContentProvider() {
		return contentProvider;
	}

	// TODO LWA ensure that filterChain is not null
	public void useBookmarkFilter(boolean flag) {
		if (flag) {
			filterChain.addFilter(bookmarkFilter);
		} else {
			filterChain.removeFilter(bookmarkFilter);
		}
	}

}
