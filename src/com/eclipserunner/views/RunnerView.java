package com.eclipserunner.views;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.IModelChangeListener;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.LaunchTreeContentProvider;
import com.eclipserunner.model.LaunchTreeLabelProvider;
import com.eclipserunner.ui.dnd.RunnerViewDragListener;
import com.eclipserunner.ui.dnd.RunnerViewDropListener;
import com.eclipserunner.views.actions.LaunchActionBuilder;

/**
 * Class provides plugin eclipse View UI component.
 * 
 * @author vachacz, bary
 */
public class RunnerView extends ViewPart implements ILaunchConfigurationSelection, IMenuListener, IDoubleClickListener, IModelChangeListener {

	private ITreeContentProvider treeContentProvider;
	private IRunnerModel model;

	private TreeViewer viewer;

	private ILaunchConfigurationListener launchConfigurationListener;

	private Action showRunConfigurationsDialogAction;
	private Action showDebugConfigurationsDialogAction;

	private Action launchRunConfigurationAction;
	private Action launchDebugConfigurationAction;

	private Action addNewCategoryAction;

	private Action collapseAllAction;

	private Action expandAllAction;

	private Action renameAction;
	private Action removeAction;
	private Action aboutAction;

	public RunnerView() {
		LaunchTreeContentProvider launchTreeContentProvider = LaunchTreeContentProvider.getDefault();
		launchTreeContentProvider.setViewPart(this);

		treeContentProvider = launchTreeContentProvider;
		model = launchTreeContentProvider;
	}

	@Override
	public void createPartControl(Composite parent) {

		initializeModel();
		initializeViewer(parent);
		initDragAndDrop();

		setupLaunchActions();
		setupContextMenu();
		setupActionBars();

		addRunConfigurationListener();
	}

	@Override
	public void dispose() {
		super.dispose();

		model.removeChangeListener(this);
		getLaunchManager().removeLaunchConfigurationListener(launchConfigurationListener);
	}

	private void initializeModel() {
		model.addChangeListener(this);
	}

	private void initializeViewer(Composite parent) {

		PatternFilter patternFilter = new PatternFilter();
		patternFilter.setIncludeLeadingWildcard(true);

		FilteredTree tree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, patternFilter, true);

		viewer = tree.getViewer();
		viewer.setContentProvider(treeContentProvider);
		viewer.setLabelProvider(new LaunchTreeLabelProvider());
		viewer.setSorter(new ViewerSorter());
		viewer.addDoubleClickListener(this);
		viewer.setInput(getViewSite());
	}

	private void initDragAndDrop() {
		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer(), FileTransfer.getInstance() };

		getViewer().addDragSupport(operations, transferTypes, new RunnerViewDragListener(getViewer()));
		getViewer().addDropSupport(operations, transferTypes, new RunnerViewDropListener(getViewer(), model));
	}

	private void setupLaunchActions() {
		LaunchActionBuilder builder = LaunchActionBuilder.newInstance()
			.withLaunchConfigurationSelection(this)
			.withRunnerModel(model);

		showRunConfigurationsDialogAction   = builder.createShowRunConfigurationDialogAction();
		showDebugConfigurationsDialogAction = builder.createShowDebugConfigurationDialogAction();
		launchRunConfigurationAction        = builder.createRunConfigurationAction();
		launchDebugConfigurationAction      = builder.createDebugConfigurationAction();
		addNewCategoryAction                = builder.createAddNewCategoryAction();
		collapseAllAction                   = builder.createCollapseAllAction(viewer);
		expandAllAction                     = builder.createExpandAllAction(viewer);
		renameAction                        = builder.createRenameAction();
		removeAction                        = builder.createRemoveAction();
		aboutAction                         = builder.createAboutAction();
	}

	private void setupContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(this);

		Menu menu = menuMgr.createContextMenu(getViewerControl());
		getViewerControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void setupActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		setupLocalToolBar(bars.getToolBarManager());
		setupLocalPullDown(bars.getMenuManager());
	}

	private void setupLocalPullDown(IMenuManager manager) {
		manager.add(addNewCategoryAction);
		manager.add(new Separator());
		manager.add(showRunConfigurationsDialogAction);
		manager.add(showDebugConfigurationsDialogAction);
		manager.add(new Separator());
		manager.add(aboutAction);
	}

	private void setupLocalToolBar(IToolBarManager manager) {
		manager.add(addNewCategoryAction);
		manager.add(new Separator());
		manager.add(collapseAllAction);
		manager.add(expandAllAction);
		manager.add(new Separator());
		manager.add(showRunConfigurationsDialogAction);
		manager.add(showDebugConfigurationsDialogAction);
	}

	private void addRunConfigurationListener() {
		launchConfigurationListener = new ILaunchConfigurationListener() {

			public void launchConfigurationRemoved(ILaunchConfiguration configuration) {
				getViewer().refresh();
			}

			public void launchConfigurationChanged(ILaunchConfiguration configuration) {
				getViewer().refresh();
			}

			public void launchConfigurationAdded(ILaunchConfiguration configuration) {
				getViewer().refresh();
			}
		};

		getLaunchManager().addLaunchConfigurationListener(launchConfigurationListener);
	}

	public void modelChanged() {
		getViewer().refresh();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager manager) {

		manager.add(addNewCategoryAction);

		if (isLaunchConfigurationSelected()) {
			manager.add(new Separator());
			manager.add(launchRunConfigurationAction);
			manager.add(launchDebugConfigurationAction);
		}

		manager.add(new Separator());
		manager.add(renameAction);
		manager.add(removeAction);
		manager.add(new Separator());
		manager.add(showRunConfigurationsDialogAction);
		manager.add(showDebugConfigurationsDialogAction);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
	public void doubleClick(DoubleClickEvent event) {
		this.launchRunConfigurationAction.run();
	}

	public boolean isLaunchConfigurationSelected() {
		if (getSelectedObject() instanceof ILaunchConfiguration) {
			return true;
		}
		return false;
	}

	public ILaunchConfiguration getSelectedLaunchConfiguration() {
		return (ILaunchConfiguration) getSelectedObject();
	}

	public Object getSelectedObject() {
		return ((IStructuredSelection) getViewer().getSelection()).getFirstElement();
	}

	public ILaunchConfigurationCategory getSelectedObjectCategory() {
		Object selectedObject = getSelectedObject();
		ILaunchConfigurationCategory category = null;

		if (selectedObject instanceof ILaunchConfiguration) {
			category = (ILaunchConfigurationCategory) treeContentProvider.getParent(selectedObject);
		}
		else if (selectedObject instanceof ILaunchConfigurationCategory) {
			category = (ILaunchConfigurationCategory) selectedObject;
		}
		else {
			assert true; // unreachable code
		}
		return category;
	}

	@Override
	public void setFocus() {
		getViewerControl().setFocus();
	}

	public TreeViewer getViewer() {
		return viewer;
	}

	public ITreeContentProvider getTreeContentProvider() {
		return treeContentProvider;
	}

	private Control getViewerControl() {
		return getViewer().getControl();
	}

	private ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}

}