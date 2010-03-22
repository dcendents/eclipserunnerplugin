package com.eclipserunner.model.filters;

import org.eclipse.jface.preference.IPreferenceStore;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.RunnerModelProvider;
import com.eclipserunner.model.common.AbstractFilter;

public class DefaultCategoryFilter extends AbstractFilter {

	public DefaultCategoryFilter(String propery, IPreferenceStore preferenceStore) {
		super(propery, preferenceStore);
	}

	@Override
	public boolean filterWhenActive(ILaunchNode launchNode) {
		return false;
	}

	@Override
	public boolean filterWhenActive(ICategoryNode categoryNode) {
		ICategoryNode defaultCategoryNode = RunnerModelProvider.getInstance().getDefaultModel().getDefaultCategoryNode();
		if (defaultCategoryNode.equals(categoryNode)) {
			return true;
		}
		return false;
	}

}
