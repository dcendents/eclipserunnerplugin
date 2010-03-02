package com.eclipserunner.model;

import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.eclipserunner.RunnerPlugin;

/**
 * Launch configuration tree decorator.
 * 
 * @author vachacz
 */
public class LaunchTreeLabelProvider extends LabelProvider {

	private static final String IMG_CATEGORY = "category.gif";
	
	private IDebugModelPresentation debugModelPresentation;

	public LaunchTreeLabelProvider() {
		debugModelPresentation = DebugUITools.newDebugModelPresentation();
	}

	@Override
	public String getText(Object element) {
		if (element instanceof LaunchConfigrationCategory) {
			return ((LaunchConfigrationCategory) element).getName();
		}
		return debugModelPresentation.getText(element);
	}
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof LaunchConfigrationCategory) {
			return RunnerPlugin.getDefault().getImageDescriptor(IMG_CATEGORY).createImage();
		}
		return debugModelPresentation.getImage(element);
	}

}
