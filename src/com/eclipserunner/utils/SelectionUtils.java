package com.eclipserunner.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
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
	
	public static <T> T getFirstSelectedByType(ISelection selection, Class<T> instanceClass) {
		if (selection instanceof IStructuredSelection) {
			return getFirstSelectedByType((IStructuredSelection) selection, instanceClass);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFirstSelectedByType(IStructuredSelection selection, Class<T> instanceClass) {
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Object item = iterator.next();
			if (instanceClass.isInstance(item)) {
				return (T) item;
			}
		}
		return null;
	}
	
	public static <T> List<T> getAllSelectedByType(ISelection selection, Class<T> instanceClass) {
		if (selection instanceof IStructuredSelection) {
			return getAllSelectedByType((IStructuredSelection) selection, instanceClass);
		}
		return new ArrayList<T>();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllSelectedByType(IStructuredSelection selection, Class<T> instanceClass) {
		List<T> selectedOfType = new ArrayList<T>();
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Object item = iterator.next();
			if (instanceClass.isInstance(item)) {
				selectedOfType.add((T) item);
			}
		}
		return selectedOfType;
	}
	
	@SuppressWarnings("unchecked")
	public static List selectionAsList(IStructuredSelection selection) {
		return Arrays.asList(selection.toArray());
	} 
}
