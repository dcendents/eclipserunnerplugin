package com.eclipserunner.utils;

import java.util.ArrayList;
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
	
	public static <T> T getFirstSelectedOfType(ISelection selection, Class<T> instanceClass) {
		if (selection instanceof IStructuredSelection) {
			return getFirstSelectedOfType((IStructuredSelection) selection, instanceClass);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFirstSelectedOfType(IStructuredSelection selection, Class<T> instanceClass) {
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Object item = iterator.next();
			if (instanceClass.isInstance(item)) {
				return (T) item;
			}
		}
		return null;
	}
	
	public static <T> List<T> getAllSelectedOfType(ISelection selection, Class<T> instanceClass) {
		if (selection instanceof IStructuredSelection) {
			return getAllSelectedOfType((IStructuredSelection) selection, instanceClass);
		}
		return new ArrayList<T>();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllSelectedOfType(IStructuredSelection selection, Class<T> instanceClass) {
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
}
