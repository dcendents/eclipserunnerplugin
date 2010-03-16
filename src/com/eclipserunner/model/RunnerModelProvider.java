package com.eclipserunner.model;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.eclipserunner.model.adapters.RunnerModelTreeAdapter;
import com.eclipserunner.model.adapters.RunnerModelTreeWithTypesAdapter;
import com.eclipserunner.model.impl.RunnerModel;
import com.eclipserunner.views.TreeMode;

public class RunnerModelProvider {

	private static IRunnerModel runnerModel = null;
	private static ITreeContentProvider contentProvider = null;

	public static IRunnerModel getDefaultModel() {
		if (runnerModel == null) {
			initializeModel();
		}
		return runnerModel;
	}

	public static void initializeModel() {
		runnerModel = new RunnerModel();
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

}
