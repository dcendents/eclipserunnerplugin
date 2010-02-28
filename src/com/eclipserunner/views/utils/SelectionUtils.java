package com.eclipserunner.views.utils;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Utility methods related to object selection.
 * 
 * @author vachacz
 */
public class SelectionUtils {

	public static IStructuredSelection asStructuredSelection(Object object) {
		return new StructuredSelection(object);
	}
	
}
