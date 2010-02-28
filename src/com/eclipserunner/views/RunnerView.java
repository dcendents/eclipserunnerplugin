package com.eclipserunner.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.eclipserunner.views.actions.LaunchActionBuilder;

/**
 * Class provides plugin eclipse View UI component.
 * 
 * @author vachacz, bary
 */
public class RunnerView extends ViewPart implements ILaunchConfigurationSelection {

	private TableViewer viewer;

	private Action showRunConfigurationsDialogAction;
	private Action showDebugConfigurationsDialogAction;

	private Action launchRunConfigurationAction;
	private Action launchDebugConfigurationAction;

	private Action aboutAction;

	// TODO LWA replace this code
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			List<ILaunchConfiguration> savedRunConfiguration = new ArrayList<ILaunchConfiguration>();

			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
			try {
				for (ILaunchConfiguration config : manager.getLaunchConfigurations()) {
					savedRunConfiguration.add(config);
				}
			} catch (CoreException e) {
			}

			return savedRunConfiguration.toArray();
		}
	}

	class NameSorter extends ViewerSorter {
	}

	@Override
	public void createPartControl(Composite parent) {
		
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(DebugUITools.newDebugModelPresentation());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getViewerControl(), "EclipseRunnerPliugin.viewer");
		
		setupLaunchActions();
		setupContextMenu();
		setupDoubleClickAction();
		setupActionBars();

		addRunConfigurationListener();
	}

	private void addRunConfigurationListener() {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		manager.addLaunchConfigurationListener(new ILaunchConfigurationListener() {

			public void launchConfigurationRemoved(ILaunchConfiguration configuration) {
				RunnerView.this.viewer.refresh();
			}

			public void launchConfigurationChanged(ILaunchConfiguration configuration) {
				RunnerView.this.viewer.refresh();
			}

			public void launchConfigurationAdded(ILaunchConfiguration configuration) {
				RunnerView.this.viewer.refresh();
			}
		});
	}

	private void setupContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
			menuMgr.setRemoveAllWhenShown(true);
			menuMgr.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {
					RunnerView.this.setupContextMenu(manager);
				}
			});
		
		Menu menu = menuMgr.createContextMenu(getViewerControl());
		getViewerControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.viewer);
	}
	
	private void setupContextMenu(IMenuManager manager) {
		manager.add(this.launchRunConfigurationAction);
		manager.add(this.launchDebugConfigurationAction);
		manager.add(new Separator());
		manager.add(this.showRunConfigurationsDialogAction);
		manager.add(this.showDebugConfigurationsDialogAction);
		manager.add(new Separator());
	}
	
	private void setupActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		setupLocalToolBar(bars.getToolBarManager());
		setupLocalPullDown(bars.getMenuManager());
	}

	private void setupLocalPullDown(IMenuManager manager) {
		manager.add(this.showRunConfigurationsDialogAction);
		manager.add(this.showDebugConfigurationsDialogAction);
		manager.add(new Separator());
		manager.add(this.aboutAction);
	}
	
	private void setupLocalToolBar(IToolBarManager manager) {
		manager.add(this.showRunConfigurationsDialogAction);
		manager.add(this.showDebugConfigurationsDialogAction);
		manager.add(this.aboutAction);
	}

	private void setupLaunchActions() {
		LaunchActionBuilder builder = LaunchActionBuilder.newInstance().withLaunchConfigurationSelection(this);
		
		this.showRunConfigurationsDialogAction   = builder.createShowRunConfigurationDialogAction();
		this.showDebugConfigurationsDialogAction = builder.createShowDebugConfigurationDialogAction();
		this.launchRunConfigurationAction        = builder.createRunConfigurationAction();
		this.launchDebugConfigurationAction      = builder.createDebugConfigurationAction();
		this.aboutAction                         = builder.createAboutAction();
	}
	
	public ILaunchConfiguration getSelectedLaunchConfiguration() {
		return (ILaunchConfiguration) ((IStructuredSelection) getViewer().getSelection()).getFirstElement();
	}

	private void setupDoubleClickAction() {
		this.viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				RunnerView.this.launchRunConfigurationAction.run();
			}
		});
	}

	@Override
	public void setFocus() {
		getViewerControl().setFocus();
	}

	public TableViewer getViewer() {
		return this.viewer;
	}
	
	public Control getViewerControl() {
		return getViewer().getControl();
	}

}