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
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

import com.eclipserunner.model.IActionEnablement;
import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationNode;
import com.eclipserunner.model.ILaunchConfigurationSelection;
import com.eclipserunner.model.IModelChangeListener;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.adapters.RunnerModelJdtSelectionListenerAdapter;
import com.eclipserunner.model.adapters.RunnerModelLaunchConfigurationListenerAdapter;
import com.eclipserunner.model.adapters.RunnerModelTreeWithTypesAdapter;
import com.eclipserunner.model.impl.LaunchTreeLabelProvider;
import com.eclipserunner.model.impl.RunnerModel;
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
	private IRunnerModel runnerModel;

	private TreeViewer viewer;

	private ILaunchConfigurationListener modelLaunchConfigurationListener;
	private ILaunchConfigurationListener viewLaunchConfigurationListener;
	private ISelectionListener selectionListener;

	private Action showRunConfigurationsDialogAction;
	private Action showDebugConfigurationsDialogAction;

	private Action launchRunConfigurationAction;
	private Action launchDebugConfigurationAction;

	private Action addNewCategoryAction;

	private Action collapseAllAction;
	private Action expandAllAction;

	private Action bookmarkAction;
	private Action unbookmarkAction;

	private Action renameAction;
	private Action removeAction;
	private Action aboutAction;

	// we are listening only from this selection providers
	private final String[] selectonProviders = new String[] {
			"org.eclipse.jdt.ui.PackageExplorer",
			"org.eclipse.ui.navigator.ProjectExplorer"
	};

	public RunnerView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		initializeModel();
		initializeViewer(parent);
		initializeSelectionListeners();
		initializeLaunchConfigurationListeners();
		initializeDragAndDrop();

		setupLaunchActions();
		setupContextMenu();
		setupActionBars();
	}

	private void initializeModel() {
		runnerModel = RunnerModel.getDefault();
		runnerModel.addModelChangeListener(this);
	}

	private void initializeViewer(Composite parent) {
		//treeContentProvider = new RunnerModelTreeAdapter(runnerModel, this);
		treeContentProvider = new RunnerModelTreeWithTypesAdapter(runnerModel, this);

		PatternFilter patternFilter = new PatternFilter();
		patternFilter.setIncludeLeadingWildcard(true);

		FilteredTree tree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, patternFilter, true);

		viewer = tree.getViewer();
		viewer.setContentProvider(treeContentProvider);
		viewer.setLabelProvider(new LaunchTreeLabelProvider());
		viewer.addDoubleClickListener(this);
		viewer.setInput(getViewSite());

		// we're cooperative and also provide our selection
		getSite().setSelectionProvider(viewer);
	}

	private void initializeSelectionListeners() {
		selectionListener = new RunnerModelJdtSelectionListenerAdapter(runnerModel, this);
		for (String partId : selectonProviders) {
			getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(partId, selectionListener);
		}
	}

	private void initializeLaunchConfigurationListeners() {
		viewLaunchConfigurationListener = new ILaunchConfigurationListener() {
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

		modelLaunchConfigurationListener = new RunnerModelLaunchConfigurationListenerAdapter(runnerModel);

		getLaunchManager().addLaunchConfigurationListener(viewLaunchConfigurationListener);
		getLaunchManager().addLaunchConfigurationListener(modelLaunchConfigurationListener);
	}

	private void initializeDragAndDrop() {
		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[]{ LocalSelectionTransfer.getTransfer(), FileTransfer.getInstance() };

		getViewer().addDragSupport(operations, transferTypes, new RunnerViewDragListener(getViewer()));
		getViewer().addDropSupport(operations, transferTypes, new RunnerViewDropListener(getViewer()));
	}

	@Override
	public void dispose() {
		super.dispose();

		disposeModel();
		disposeSelectionListeners();
		disposeLaunchConfigurationListeners();
	}

	private void disposeModel() {
		runnerModel.removeModelChangeListener(this);
	}

	private void disposeSelectionListeners() {
		for (String partId : selectonProviders) {
			getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(partId, selectionListener);
		}
	}

	private void disposeLaunchConfigurationListeners() {
		getLaunchManager().removeLaunchConfigurationListener(modelLaunchConfigurationListener);
		getLaunchManager().removeLaunchConfigurationListener(viewLaunchConfigurationListener);
	}

	private void setupLaunchActions() {
		LaunchActionBuilder builder = LaunchActionBuilder.newInstance()
		.withLaunchConfigurationSelection(this)
		.withRunnerModel(runnerModel);

		showRunConfigurationsDialogAction   = builder.createShowRunConfigurationDialogAction();
		showDebugConfigurationsDialogAction = builder.createShowDebugConfigurationDialogAction();
		launchRunConfigurationAction        = builder.createRunConfigurationAction();
		launchDebugConfigurationAction      = builder.createDebugConfigurationAction();
		addNewCategoryAction                = builder.createAddNewCategoryAction();
		collapseAllAction                   = builder.createCollapseAllAction(viewer);
		expandAllAction                     = builder.createExpandAllAction(viewer);
		bookmarkAction                      = builder.createBookmarkAction();
		unbookmarkAction                    = builder.createUnbookmarkAction();
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
		setupLocalPullDown(bars.getMenuManager());
		setupLocalToolBar(bars.getToolBarManager());
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

	// TODO LWA BARY: define which actions should be visible/enable for category and configuration,
	// do we plan some restrictions when user selects multiple items or selects simultaneously catagory and configuration item?
	public void menuAboutToShow(IMenuManager manager) {

		setupMenuItems(manager);
		setupActionEnablement();

	}

	private void setupMenuItems(IMenuManager manager) {
		manager.add(addNewCategoryAction);

		if (isLaunchConfigurationNodeSelected()) {
			manager.add(new Separator());
			manager.add(launchRunConfigurationAction);
			manager.add(launchDebugConfigurationAction);
		}

		manager.add(new Separator());
		manager.add(renameAction);
		manager.add(removeAction);

		manager.add(new Separator());
		manager.add(bookmarkAction);
		manager.add(unbookmarkAction);

		manager.add(new Separator());
		manager.add(showRunConfigurationsDialogAction);
		manager.add(showDebugConfigurationsDialogAction);
	}

	private void setupActionEnablement() {
		IActionEnablement provider = getActionEnablementProvider();
		if (provider != null) {
			renameAction.setEnabled(provider.isRenamable());
			removeAction.setEnabled(provider.isRemovable());
		}
		else {
			renameAction.setEnabled(false);
			removeAction.setEnabled(false);
		}
	}

	private IActionEnablement getActionEnablementProvider() {
		Object selectedObject = getSelectedObject();
		if (getSelectedObject() instanceof IActionEnablement) {
			return (IActionEnablement) selectedObject;
		}
		return null;
	}

	public void doubleClick(DoubleClickEvent event) {
		this.launchRunConfigurationAction.run();
	}

	public Object getSelectedObject() {
		return ((IStructuredSelection) getViewer().getSelection()).getFirstElement();
	}

	public boolean isLaunchConfigurationNodeSelected() {
		if (getSelectedObject() instanceof ILaunchConfigurationNode) {
			return true;
		}
		return false;
	}

	public boolean isLaunchConfigurationCategorySelected() {
		if (getSelectedObject() instanceof ILaunchConfigurationCategory) {
			return true;
		}
		return false;
	}

	public ILaunchConfigurationNode getSelectedLaunchConfigurationNode() {
		return (ILaunchConfigurationNode) getSelectedObject();
	}

	public ILaunchConfigurationCategory getSelectedLaunchConfigurationCategory() {
		Object selectedObject = getSelectedObject();
		ILaunchConfigurationCategory category = null;

		if (selectedObject instanceof ILaunchConfigurationNode) {
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

	public void modelChanged() {
		getViewer().refresh();
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