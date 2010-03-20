package com.eclipserunner.model;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.eclipserunner.PreferenceConstants;
import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.adapters.RunnerModelTreeAdapter;
import com.eclipserunner.model.adapters.RunnerModelTreeWithTypesAdapter;
import com.eclipserunner.model.filters.BookmarkFilter;
import com.eclipserunner.model.filters.ClosedProjectsFilter;
import com.eclipserunner.model.filters.DefaultCategoryFilter;
import com.eclipserunner.model.filters.DeletedProjectsFilter;
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

	private ITreeContentProvider contentProvider;

	private TreeMode treeMode;

	// Singleton pattern
	private RunnerModelProvider() {
		runnerModel = new RunnerModel();

		RunnerModelFilteringDecorator runnerModelDecorator = new RunnerModelFilteringDecorator(runnerModel);
		filteredRunnerModel = runnerModelDecorator;
		filterChain = runnerModelDecorator;
		filterChain.addFilter(new ClosedProjectsFilter(PreferenceConstants.FILTER_CLOSED_PROJECT));
		filterChain.addFilter(new DeletedProjectsFilter(PreferenceConstants.FILTER_DELETED_PROJECT));
		filterChain.addFilter(new WorkingSetFilter(PreferenceConstants.FILTER_ACTIVE_WORKING_SET));
		filterChain.addFilter(new BookmarkFilter(PreferenceConstants.FILTER_BOOKMARKED));
		filterChain.addFilter(new DefaultCategoryFilter(PreferenceConstants.FILTER_DEFAULT_CATEGORY));
		filterChain.addFilter(new ProjectFilter());

		initializeTreeModeAdapter();
	}

	private void initializeTreeModeAdapter() {
		String treeModeString = RunnerPlugin.getDefault().getPreferenceStore().getString(PreferenceConstants.TREE_MODE);
		TreeMode treeMode = null;
		try {
			treeMode = TreeMode.valueOf(treeModeString);
		} catch (Exception e) {
			treeMode = TreeMode.FLAT_MODE;
		}
		setTreeMode(treeMode);
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
		setPreferenceValue(PreferenceConstants.TREE_MODE, treeMode.toString());
	}

	public IContentProvider getTreeContentProvider() {
		return contentProvider;
	}

	public TreeMode getCurrentTreeMode() {
		return treeMode;
	}

	private void setPreferenceValue(String property, String value) {
		RunnerPlugin.getDefault().getPreferenceStore().setValue(property, value);
	}

}
