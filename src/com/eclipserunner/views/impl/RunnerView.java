package com.eclipserunner.views.impl;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.util.PropertyChangeEvent;
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
import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.IModelChangeListener;
import com.eclipserunner.model.INodeSelection;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.RunnerModelProvider;
import com.eclipserunner.model.adapters.RunnerModelJdtSelectionListenerAdapter;
import com.eclipserunner.model.adapters.RunnerModelLaunchConfigurationListenerAdapter;
import com.eclipserunner.ui.dnd.RunnerViewDragListener;
import com.eclipserunner.ui.dnd.RunnerViewDropListener;
import com.eclipserunner.views.IRunnerView;
import com.eclipserunner.views.TreeMode;
import com.eclipserunner.views.actions.LaunchActionBuilder;

/**
 * Class provides plugin eclipse View UI component.
 *
 * @author vachacz, bary
 */
@SuppressWarnings("restriction")
public class RunnerView extends ViewPart implements INodeSelection, IMenuListener, IDoubleClickListener, IModelChangeListener, IRunnerView {

	private IRunnerModel runnerModel;

	private TreeViewer viewer;

	private ILaunchConfigurationListener modelLaunchConfigurationListener;
	private ILaunchConfigurationListener viewLaunchConfigurationListener;
	private ISelectionListener selectionListener;

	private IPropertyChangeListener propertyChangeListener;

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

	private Action toggleFlatModeAction;
	private Action toggleTypeModeAction;
	private Action toggleDefaultCategoryAction;

	private Action toggleBookmarkModeAction;

	// we are listening only from this selection providers
	private final String[] selectonProviders = new String[] {
			"org.eclipse.jdt.ui.PackageExplorer",
			"org.eclipse.ui.navigator.ProjectExplorer"
	};

	@Override
	public void createPartControl(Composite parent) {
		initializeModel();
		initializeViewer(parent);
		initializeSelectionListeners();
		initializeLaunchConfigurationListeners();
		initializePropertyChangeListeners();
		initializeDragAndDrop();

		setupLaunchActions();
		setupContextMenu();
		setupActionBars();
	}

	private void initializeModel() {
		runnerModel = RunnerModelProvider.getInstance().getFilteredModel();
		runnerModel.addModelChangeListener(this);
	}

	private void initializeViewer(Composite parent) {
		PatternFilter patternFilter = new PatternFilter();
		patternFilter.setIncludeLeadingWildcard(true);

		FilteredTree tree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, patternFilter, true);

		viewer = tree.getViewer();
		setupTreeContentProvider();

		viewer.setLabelProvider(new LaunchTreeLabelProvider(runnerModel));
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
				refresh();
			}
			public void launchConfigurationChanged(ILaunchConfiguration configuration) {
				refresh();
			}
			public void launchConfigurationAdded(ILaunchConfiguration configuration) {
				refresh();
			}
		};

		modelLaunchConfigurationListener = new RunnerModelLaunchConfigurationListenerAdapter(runnerModel);

		getLaunchManager().addLaunchConfigurationListener(viewLaunchConfigurationListener);
		getLaunchManager().addLaunchConfigurationListener(modelLaunchConfigurationListener);
	}

	// TODO do we need this ?
	private void initializePropertyChangeListeners() {
		propertyChangeListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				System.out.println("RunnerView.RunnerView().new IPropertyChangeListener() {...}.propertyChange()");
			}
		};
		JavaPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(propertyChangeListener);
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
		disposePropertyChangeListeners();
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

	private void disposePropertyChangeListeners() {
		if (propertyChangeListener != null) {
			JavaPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(propertyChangeListener);
			propertyChangeListener= null;
		}
	}

	private void setupLaunchActions() {
		LaunchActionBuilder builder = LaunchActionBuilder.newInstance()
		.withLaunchConfigurationSelection(this)
		.withRunnerModel(runnerModel)
		.withRunnerView(this);

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
		toggleFlatModeAction                = builder.createToggleFlatModeAction();
		toggleTypeModeAction                = builder.createToggleTypeModeAction();
		toggleDefaultCategoryAction         = builder.createToggleDefaultCategoryAction();
		toggleBookmarkModeAction            = builder.createToggleBookmarkModeAction();
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
		manager.add(toggleFlatModeAction);
		manager.add(toggleTypeModeAction);
		manager.add(new Separator());
		manager.add(showRunConfigurationsDialogAction);
		manager.add(showDebugConfigurationsDialogAction);
		manager.add(new Separator());
		manager.add(aboutAction);
	}

	private void setupLocalToolBar(IToolBarManager manager) {
		manager.add(toggleBookmarkModeAction);
		manager.add(toggleDefaultCategoryAction);
		manager.add(new Separator());
		manager.add(addNewCategoryAction);
		manager.add(new Separator());
		manager.add(collapseAllAction);
		manager.add(expandAllAction);
	}

	// TODO LWA BARY: define which actions should be visible/enable for category and configuration,
	// do we plan some restrictions when user selects multiple items or selects simultaneously catagory and configuration item?
	public void menuAboutToShow(IMenuManager manager) {

		setupMenuItems(manager);
		setupActionEnablement();

	}

	private void setupMenuItems(IMenuManager manager) {
		manager.add(addNewCategoryAction);

		if (isLaunchNodeSelected()) {
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

	public boolean isLaunchNodeSelected() {
		if (getSelectedObject() instanceof ILaunchNode) {
			return true;
		}
		return false;
	}

	public boolean isCategoryNodeSelected() {
		if (getSelectedObject() instanceof ICategoryNode) {
			return true;
		}
		return false;
	}

	public ILaunchNode getSelectedLaunchNode() {
		return (ILaunchNode) getSelectedObject();
	}

	public ICategoryNode getSelectedCategoryNode() {
		Object selectedObject = getSelectedObject();
		ICategoryNode category = null;

		if (selectedObject instanceof ILaunchNode) {
			category = (ICategoryNode) getTreeContentProvider().getParent(selectedObject);
		}
		else if (selectedObject instanceof ICategoryNode) {
			category = (ICategoryNode) selectedObject;
		}
		else {
			assert true; // unreachable code
		}
		return category;
	}

	public void modelChanged() {
		refresh();
	}

	@Override
	public void setFocus() {
		getViewerControl().setFocus();
	}

	public TreeViewer getViewer() {
		return viewer;
	}

	public ITreeContentProvider getTreeContentProvider() {
		return (ITreeContentProvider) getViewer().getContentProvider();
	}

	private Control getViewerControl() {
		return getViewer().getControl();
	}

	private ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}

	public void setTreeMode(TreeMode mode) {
		setupTreeModeActions(mode);
		setupTreeContentProvider();
		refresh();
	}

	protected void setupTreeModeActions(TreeMode mode) {
		if (mode.isHierarchical()) {
			toggleFlatModeAction.setChecked(false);
			toggleTypeModeAction.setChecked(true);
		} else {
			toggleFlatModeAction.setChecked(true);
			toggleTypeModeAction.setChecked(false);
		}
	}

	protected void setupTreeContentProvider() {
		viewer.setContentProvider(
				RunnerModelProvider.getInstance().getTreeContentProvider()
		);
	}

	public void refresh() {
		getViewer().refresh();
	}

}