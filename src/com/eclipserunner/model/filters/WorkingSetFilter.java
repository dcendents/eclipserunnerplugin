package com.eclipserunner.model.filters;

import org.eclipse.debug.internal.ui.launchConfigurations.WorkingSetsFilter;

import com.eclipserunner.PrererenceConstants;
import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.INodeFilter;

@SuppressWarnings("restriction")
public class WorkingSetFilter implements INodeFilter {

	public boolean filter(ILaunchNode launchNode) {
		boolean active = RunnerPlugin.getDefault().getPreferenceStore().getBoolean(PrererenceConstants.ACTIVE_WORKING_SET_FILTER);

		if (active) {
			return !new WorkingSetsFilter().select(null, null, launchNode.getLaunchConfiguration());
		} else {
			return false;
		}
	}

	public boolean filter(ICategoryNode categoryNode) {
		return false;
	}

}