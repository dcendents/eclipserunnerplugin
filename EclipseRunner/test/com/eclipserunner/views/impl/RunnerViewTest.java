package com.eclipserunner.views.impl;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.eclipserunner.model.impl.CategoryNode;
import com.eclipserunner.model.impl.LaunchNode;
import com.eclipserunner.model.impl.LaunchTypeNode;

public class RunnerViewTest {

	@Mock private TreeViewer treeViewer;
	@Mock private IStructuredSelection selection;
	
	private RunnerView runnerView;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		runnerView = new RunnerView();
		runnerView.setTreeViewerForTesting(treeViewer);
		
		when(treeViewer.getSelection()).thenReturn(selection);
	}
	
	@Test
	public void ensureThatCategoryNodeIsSelected() throws Exception {
		setupSelectionWithFirstElement(new CategoryNode("test"));
		
		assertFalse(runnerView.isLaunchNodeSelected());
		assertTrue(runnerView.isCategoryNodeSelected());
		assertFalse(runnerView.isLaunchTypeNodeSelected());
	}
	
	@Test
	public void ensureThatLaunchTypeNodeIsSelected() throws Exception {
		setupSelectionWithFirstElement(new LaunchTypeNode());
		
		assertFalse(runnerView.isLaunchNodeSelected());
		assertFalse(runnerView.isCategoryNodeSelected());
		assertTrue(runnerView.isLaunchTypeNodeSelected());
	}

	@Test
	public void ensureThatLaunchNodeIsSelected() throws Exception {
		setupSelectionWithFirstElement(new LaunchNode());
		
		assertTrue(runnerView.isLaunchNodeSelected());
		assertFalse(runnerView.isCategoryNodeSelected());
		assertFalse(runnerView.isLaunchTypeNodeSelected());
	}


	private void setupSelectionWithFirstElement(Object selectedNode) {
		when(selection.getFirstElement()).thenReturn(selectedNode);
	}
		
}
