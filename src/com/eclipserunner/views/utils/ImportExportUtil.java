package com.eclipserunner.views.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ImportExportUtil {

	// TODO BARY dummy code
	public static void export(File file, IStructuredSelection selection) throws CoreException {
		try {
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("TODO: How to persist launch configuration to file");
			out.close();
		}
		catch (IOException e) {
			// TODO BARY
			e.printStackTrace();
		}
	}
}
