package com.eclipserunner.model.impl;

import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.eclipserunner.RunnerPlugin;
import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationNode;

/**
 * Launch configuration tree decorator.
 * 
 * @author vachacz, bary
 */
public class LaunchTreeLabelProvider extends LabelProvider {

	private static final String IMG_CATEGORY   = "category.gif";
	private static final String IMG_DECORATION = "bookmark_star.gif";

	private IDebugModelPresentation debugModelPresentation;

	public LaunchTreeLabelProvider() {
		debugModelPresentation = DebugUITools.newDebugModelPresentation();
	}

	@Override
	public String getText(Object element) {
		if (element instanceof ILaunchConfigurationCategory) {
			return ((ILaunchConfigurationCategory) element).getName();
		}
		else if (element instanceof ILaunchConfigurationNode) {
			ILaunchConfigurationNode launchConfiguration = (ILaunchConfigurationNode) element;
			return debugModelPresentation.getText(launchConfiguration.getLaunchConfiguration());
		}
		else if (element instanceof LaunchConfigurationTypeNode) {
			return debugModelPresentation.getText(((LaunchConfigurationTypeNode) element).getType());
		}
		return debugModelPresentation.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof ILaunchConfigurationCategory) {
			return createImage(IMG_CATEGORY);
		}
		else if (element instanceof ILaunchConfigurationNode) {
			ILaunchConfigurationNode launchConfiguration = (ILaunchConfigurationNode) element;
			Image image = debugModelPresentation.getImage(launchConfiguration.getLaunchConfiguration());

			if (launchConfiguration.isBookmarked()) {
				return overlyImage(image, IMG_DECORATION);
			}
			return image;
		}
		else if (element instanceof LaunchConfigurationTypeNode) {
			return debugModelPresentation.getImage(((LaunchConfigurationTypeNode) element).getType());
		}
		return ImageDescriptor.getMissingImageDescriptor().createImage();
	}

	private Image createImage(String image) {
		return RunnerPlugin.getDefault().getImageDescriptor(image).createImage();
	}

	private Image overlyImage(Image image, String decoration) {
		ImageDescriptor decorationDescriptor = RunnerPlugin.getDefault().getImageDescriptor(decoration);
		return new DecorationOverlayIcon(image, decorationDescriptor, IDecoration.TOP_RIGHT).createImage();
	}

}
