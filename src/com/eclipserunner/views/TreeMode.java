package com.eclipserunner.views;

public enum TreeMode {

	FLAT_MODE,
	HIERARCHICAL_MODE;

	public boolean isFlat() {
		return this.equals(FLAT_MODE);
	}

	public boolean isHierarchical() {
		return this.equals(HIERARCHICAL_MODE);
	}

	public static TreeMode fromString(String treeMode) {
		if (HIERARCHICAL_MODE.toString().equals(treeMode)) {
			return HIERARCHICAL_MODE;
		}
		return FLAT_MODE;
	}
}
