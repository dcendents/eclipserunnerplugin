package com.eclipserunner.model;

import org.eclipse.jface.preference.IPreferenceStore;
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

	private static final RunnerModelProvider INSTANCE = new RunnerModelProvider();

	private IRunnerModel runnerModel;
	private IRunnerModel filteredRunnerModel;

	private ITreeContentProvider contentProvider;

	private TreeMode treeMode;

	private RunnerModelFilteringDecorator decorator;

	// Singleton pattern
	private RunnerModelProvider() {
		runnerModel = new RunnerModel();
		
		decorator = new RunnerModelFilteringDecorator(runnerModel);
		decorator.addFilter(new ClosedProjectsFilter(PreferenceConstants.FILTER_CLOSED_PROJECT, getPreferenceStore()));
		decorator.addFilter(new DeletedProjectsFilter(PreferenceConstants.FILTER_DELETED_PROJECT, getPreferenceStore()));
		decorator.addFilter(new WorkingSetFilter(PreferenceConstants.FILTER_ACTIVE_WORKING_SET, getPreferenceStore()));
		decorator.addFilter(new BookmarkFilter(PreferenceConstants.FILTER_BOOKMARKED, getPreferenceStore()));
		decorator.addFilter(new DefaultCategoryFilter(PreferenceConstants.FILTER_DEFAULT_CATEGORY, runnerModel, getPreferenceStore()));
		decorator.addFilter(new ProjectFilter());
		
		filteredRunnerModel = decorator;

		initializeTreeModeAdapter();
	}

	private void initializeTreeModeAdapter() {
		TreeMode treeMode = null;
		try {
			treeMode = TreeMode.valueOf(getPreferenceValue(PreferenceConstants.TREE_MODE));
		} catch (Exception e) {
			treeMode = TreeMode.FLAT_MODE;
		}
		setTreeMode(treeMode);
	}

	public void setTreeMode(TreeMode treeMode) {
		this.treeMode = treeMode;
		this.contentProvider = createContentProviderForTreeType(treeMode);
		setPreferenceValue(PreferenceConstants.TREE_MODE, treeMode.toString());
	}

	private ITreeContentProvider createContentProviderForTreeType(TreeMode treeMode) {
		switch (treeMode) {
			case HIERARCHICAL_MODE:
				return new RunnerModelTreeWithTypesAdapter(filteredRunnerModel);
			default: // FLAT_MODE
				return new RunnerModelTreeAdapter(filteredRunnerModel);
		}
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

	public IContentProvider getTreeContentProvider() {
		return contentProvider;
	}

	public TreeMode getCurrentTreeMode() {
		return treeMode;
	}

	private void setPreferenceValue(String property, String value) {
		getPreferenceStore().setValue(property, value);
	}
	
	private String getPreferenceValue(String preferenceValue) {
		return getPreferenceStore().getString(preferenceValue);
	}

	private IPreferenceStore getPreferenceStore() {
		return RunnerPlugin.getDefault().getPreferenceStore();
	}
	
}
