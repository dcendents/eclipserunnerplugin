package com.eclipserunner.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.eclipserunner.activator.Activator;
import com.eclipserunner.views.actions.LaunchConfigurationAction;
import com.eclipserunner.views.actions.ShowLaunchConfigurationsDialogAction;

/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SampleView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.eclipserunner.views.SampleView";

	private static final String RUN_CONFIGURATIONS_IMG   = "run.gif";
	private static final String RUN_IMG                  = "run.gif";

	private static final String DEBUG_CONFIGURATIONS_IMG = "run_configuration.gif";
	private static final String DEBUG_IMG                = "run_configuration.gif";

	private TableViewer viewer;

	private Action showRunConfigurationsDialogAction;
	private Action showDebugConfigurationsDialogAction;

	private Action launchRunConfigurationAction;
	private Action launchDebugConfigurationAction;

	private Action aboutAction;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content
	 * (like Task List, for example).
	 */

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			// INFO dummy code ...
			List<ILaunchConfiguration> savedRunConfiguration = new ArrayList<ILaunchConfiguration>();

			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
			try {
				for (ILaunchConfiguration config : manager.getLaunchConfigurations()) {
					savedRunConfiguration.add(config);
				}
			} catch (CoreException e) {
				// TODO LWA
			}

			return savedRunConfiguration.toArray();
		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public SampleView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		this.viewer.setContentProvider(new ViewContentProvider());
		this.viewer.setLabelProvider(DebugUITools.newDebugModelPresentation());
		this.viewer.setSorter(new NameSorter());
		this.viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this.viewer.getControl(), "EclipseRunnerPliugin.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

		addRunConfigurationListener();
	}

	private void addRunConfigurationListener() {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		manager.addLaunchConfigurationListener(new ILaunchConfigurationListener() {

			public void launchConfigurationRemoved(ILaunchConfiguration arg0) {
				SampleView.this.viewer.refresh();
			}

			public void launchConfigurationChanged(ILaunchConfiguration arg0) {
				SampleView.this.viewer.refresh();
			}

			public void launchConfigurationAdded(ILaunchConfiguration arg0) {
				SampleView.this.viewer.refresh();
			}
		});
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(this.viewer.getControl());
		this.viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(this.showRunConfigurationsDialogAction);
		manager.add(this.showDebugConfigurationsDialogAction);
		manager.add(new Separator());
		manager.add(this.aboutAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(this.launchRunConfigurationAction);
		manager.add(this.launchDebugConfigurationAction);
		manager.add(new Separator());
		manager.add(this.showRunConfigurationsDialogAction);
		manager.add(this.showDebugConfigurationsDialogAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(this.showRunConfigurationsDialogAction);
		manager.add(this.showDebugConfigurationsDialogAction);
		manager.add(this.aboutAction);
	}


	protected final void configureAction(Action action, String textKey, String tooltipKey, String imageKey) {
		action.setText(Activator.getResourceString(textKey));
		action.setToolTipText(Activator.getResourceString(tooltipKey));
		// TODO BARY - how to set our own custom icon?
		//action.setImageDescriptor(Activator.getDefault().getImageDescriptor(imageKey));
		action.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	private void makeActions() {
		this.showRunConfigurationsDialogAction = new ShowLaunchConfigurationsDialogAction(this, IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
		configureAction(this.showRunConfigurationsDialogAction, "property.openRunConfigurationsDialogText", "property.openRunConfigurationsDialogToolTipText", RUN_CONFIGURATIONS_IMG);

		this.showDebugConfigurationsDialogAction = new ShowLaunchConfigurationsDialogAction(this, IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
		configureAction(this.showDebugConfigurationsDialogAction, "property.openDebugConfigurationsDialogText", "property.openDebugConfigurationsDialogToolTipText", DEBUG_CONFIGURATIONS_IMG);

		this.launchRunConfigurationAction = new LaunchConfigurationAction(this, IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
		configureAction(this.launchRunConfigurationAction, "property.runConfigurationText", "property.runConfigurationToolTipText", RUN_IMG);

		this.launchDebugConfigurationAction = new LaunchConfigurationAction(this, IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
		configureAction(this.launchDebugConfigurationAction, "property.debugConfigurationText", "property.debugConfigurationToolTipText", DEBUG_IMG);

		this.aboutAction = new Action() {
			@Override
			public void run() {
				showMessage("About action executed!");
			}
		};
		configureAction(this.aboutAction, "About ...", "About ...", null);

	}

	private void hookDoubleClickAction() {
		this.viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				SampleView.this.launchRunConfigurationAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(
				this.viewer.getControl().getShell(),
				"Sample View",
				message
		);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}


	public TableViewer getViewer() {
		return this.viewer;
	}

}