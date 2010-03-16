package com.eclipserunner.model.impl;

import org.eclipse.debug.core.ILaunchConfigurationType;

import com.eclipserunner.model.IActionEnablement;
import com.eclipserunner.model.ICategoryNode;

/**
 * @author vachacz
 */
public class LaunchTypeNode implements IActionEnablement {

	private static final int PRIME_MULTIPLYER = 23;
	private static final int PRIME_BASE       = 133;

	private ICategoryNode categoryNode;
	private ILaunchConfigurationType launchConfigurationType;

	public void setLaunchConfigurationType(ILaunchConfigurationType launchConfigurationType) {
		this.launchConfigurationType = launchConfigurationType;
	}

	public ILaunchConfigurationType getLaunchConfigurationType() {
		return launchConfigurationType;
	}

	public void setCategoryNode(ICategoryNode categoryNode) {
		this.categoryNode = categoryNode;
	}

	public ICategoryNode getCategoryNode() {
		return categoryNode;
	}

	public boolean isRemovable() {
		return false;
	}

	public boolean isRenamable() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LaunchTypeNode) {
			LaunchTypeNode typeNode = (LaunchTypeNode) obj;
			return launchConfigurationType.equals(typeNode.getLaunchConfigurationType()) && categoryNode.equals(typeNode.getCategoryNode());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode () {
		int code = PRIME_BASE;
		code = PRIME_MULTIPLYER * code + launchConfigurationType.hashCode();
		code = PRIME_MULTIPLYER * code + categoryNode.hashCode();
		return code;
	}
}
