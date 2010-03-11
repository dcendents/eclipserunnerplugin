package com.eclipserunner.model;

import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.eclipserunner.RunnerPlugin;

/**
 * Launch configuration tree decorator.
 * 
 * @author vachacz, bary
 */
public class LaunchTreeLabelProvider extends LabelProvider {

	private static final String IMG_CATEGORY = "category.gif";
	private static final String IMG_PIN      = "pin.gif";

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
		return debugModelPresentation.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof ILaunchConfigurationCategory) {
			return getCategoryImage();
		}
		else if (element instanceof ILaunchConfigurationNode) {
			ILaunchConfigurationNode launchConfiguration = (ILaunchConfigurationNode) element;
			Image image = debugModelPresentation.getImage(launchConfiguration.getLaunchConfiguration());

			if (launchConfiguration.isBookmarked()) {
				return getDecoratedImage(image, IMG_PIN);
			}

			return image;
		}
		else {
			return ImageDescriptor.getMissingImageDescriptor().createImage();
		}
	}

	private Image getCategoryImage() {
		return RunnerPlugin.getDefault().getImageDescriptor(IMG_CATEGORY).createImage();
	}

	private Image getDecoratedImage(Image image, String decoration) {
		ImageDescriptor decorationDescriptor = RunnerPlugin.getDefault().getImageDescriptor(decoration);
		DecorationOverlayIcon decoratedImageDescriptor = new DecorationOverlayIcon(image, decorationDescriptor, IDecoration.TOP_RIGHT);
		return decoratedImageDescriptor.createImage();
	}

}
