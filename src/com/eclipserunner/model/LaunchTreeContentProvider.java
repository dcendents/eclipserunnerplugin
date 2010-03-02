package com.eclipserunner.model;

import static com.eclipserunner.Messages.Message_uncategorized;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.eclipserunner.views.RunnerView;

/**
 * Class implementing {@link ITreeContentProvider} acts as a model for launch configuration tree.
 * By default provides "uncategorized" category. 
 * 
 * @author vachacz
 */
public class LaunchTreeContentProvider implements ITreeContentProvider {

	private List<IModelChangeListener> modelChangeListeners = new ArrayList<IModelChangeListener>();
	private Set<LaunchConfigrationCategory> launchConfigrationCategorySet;
	
	private LaunchConfigrationCategory uncategorizedCategory;

	private RunnerView runnerView;
	
	public LaunchTreeContentProvider() {
		uncategorizedCategory = new LaunchConfigrationCategory();
		uncategorizedCategory.setName(Message_uncategorized);
		
		launchConfigrationCategorySet = new HashSet<LaunchConfigrationCategory>();
		launchConfigrationCategorySet.add(uncategorizedCategory);
	}

	public void addUncategorizedLaunchConfiguration(ILaunchConfiguration configuration) {
		uncategorizedCategory.add(configuration);
		notifyListenersAboutChange();
	} 
	
	public Object[] getChildren(Object object) {
		if (launchConfigrationCategorySet.contains(object)) {
			LaunchConfigrationCategory launchConfigrationCategory = (LaunchConfigrationCategory) object;
			return launchConfigrationCategory.toArray();
		}
		return null;
	}

	public Object getParent(Object object) {
		for (LaunchConfigrationCategory category : launchConfigrationCategorySet) {
			if (category.contains(object)) {
				return category;
			}
		}
		return null;
	}

	public boolean hasChildren(Object parent) {
		if (launchConfigrationCategorySet.contains(parent)) {
			LaunchConfigrationCategory launchConfigrationCategory = (LaunchConfigrationCategory) parent;
			return !launchConfigrationCategory.getLaunchConfigurationSet().isEmpty();
		}
		return false;
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(runnerView.getViewSite())) {
			return launchConfigrationCategorySet.toArray();
		}
		return getChildren(parent);
	}

	public void dispose() {
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

	public void setTreeViewer(RunnerView runnerView) {
		this.runnerView = runnerView;
	}

	public LaunchConfigrationCategory addLaunchConfigurationCategory(String name) {
		LaunchConfigrationCategory category = new LaunchConfigrationCategory();
			category.setName(name);
			
		launchConfigrationCategorySet.add(category);
		notifyListenersAboutChange();
		return category;
	}

	public void addChangeListener(IModelChangeListener listener) {
		modelChangeListeners.add(listener);
	}

	public void removeChangeListener(IModelChangeListener listener) {
		modelChangeListeners.remove(listener);
	}
	
	private void notifyListenersAboutChange() {
		for (IModelChangeListener listener : modelChangeListeners) {
			listener.modelChanged();
		}
	}

}
