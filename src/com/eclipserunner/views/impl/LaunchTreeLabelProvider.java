package com.eclipserunner.views.impl;

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
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.impl.LaunchConfigurationTypeNode;

/**
 * Launch configuration tree decorator.
 * 
 * @author vachacz, bary
 */
public class LaunchTreeLabelProvider extends LabelProvider {

	private static final String IMG_CATEGORY         = "category.gif";
	private static final String IMG_DEFAULT_CATEGORY = "category-archive.gif";
	private static final String IMG_DECORATION       = "bookmark_star.gif";

	private IDebugModelPresentation debugModelPresentation;
	private final IRunnerModel runnerModel;

	public LaunchTreeLabelProvider(IRunnerModel runnerModel) {
		this.debugModelPresentation = DebugUITools.newDebugModelPresentation();
		this.runnerModel = runnerModel;
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
			return getCategoryImage((ILaunchConfigurationCategory) element);
		}
		else if (element instanceof ILaunchConfigurationNode) {
			return getLaunchConfigurationImage((ILaunchConfigurationNode) element);
		}
		else if (element instanceof LaunchConfigurationTypeNode) {
			return getLaunchConfigurationTypeImage((LaunchConfigurationTypeNode) element);
		}
		return ImageDescriptor.getMissingImageDescriptor().createImage();
	}

	private Image getCategoryImage(ILaunchConfigurationCategory launchConfigurationCategory) {
		if (runnerModel.getDefaultCategory() == launchConfigurationCategory) {
			return createImage(IMG_DEFAULT_CATEGORY);
		} else {
			return createImage(IMG_CATEGORY);
		}
	}

	private Image getLaunchConfigurationImage(ILaunchConfigurationNode launchConfiguration) {
		Image image = debugModelPresentation.getImage(launchConfiguration.getLaunchConfiguration());
		if (launchConfiguration.isBookmarked()) {
			return overlyBookmarkIcon(image, IMG_DECORATION);
		}
		return image;
	}
	
	private Image getLaunchConfigurationTypeImage(LaunchConfigurationTypeNode typeNode) {
		return debugModelPresentation.getImage(typeNode.getType());
	}
	
	private Image createImage(String image) {
		return RunnerPlugin.getDefault().getImageDescriptor(image).createImage();
	}

	private Image overlyBookmarkIcon(Image image, String decoration) {
		ImageDescriptor decorationDescriptor = RunnerPlugin.getDefault().getImageDescriptor(decoration);
		return new DecorationOverlayIcon(image, decorationDescriptor, IDecoration.TOP_RIGHT).createImage();
	}

}
