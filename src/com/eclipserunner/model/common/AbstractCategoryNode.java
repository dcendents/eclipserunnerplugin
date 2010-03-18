package com.eclipserunner.model.common;

import com.eclipserunner.model.ICategoryNode;

public abstract class AbstractCategoryNode implements ICategoryNode {

	private static final int PRIME_MULTIPLYER = 11;
	private static final int PRIME_BASE       = 17;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ICategoryNode) {
			ICategoryNode categoryNode = (ICategoryNode) obj;
			return getName().equals(categoryNode.getName());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode () {
		int code = PRIME_BASE;
		code = PRIME_MULTIPLYER * code + getName().hashCode();
		return code;
	}

}
