package com.eclipserunner.views;

public enum TreeMode {

	FLAT_MODE,
	TYPE_MODE;

	public boolean isFlat() {
		return this.equals(FLAT_MODE);
	}

	public boolean isType() {
		return this.equals(TYPE_MODE);
	}

	public static TreeMode fromString(String treeMode) {
		if (TYPE_MODE.toString().equals(treeMode)) {
			return TYPE_MODE;
		}
		return FLAT_MODE;
	}
}
