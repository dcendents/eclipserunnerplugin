package com.eclipserunner.views.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.impl.CategoryNode;
import com.eclipserunner.model.impl.LaunchNode;
import com.eclipserunner.model.impl.LaunchTypeNode;

public class RunnerViewSelectionTest {

	@Mock private TreeViewer treeViewer;
	@Mock private IStructuredSelection strucuredSelection;
	private RunnerViewSelection selection;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		when(treeViewer.getSelection()).thenReturn(strucuredSelection);
		
		selection = new RunnerViewSelection(treeViewer);
	}
	
	@Test
	public void shouldGetFirstElement() throws Exception {
		String expected = "first element";
		
		when(strucuredSelection.getFirstElement()).thenReturn(expected);
		
		Object actual = selection.getFirstElement();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void shouldReturnAllSelectedElements() throws Exception {
		List<String> expected = Arrays.asList( "first element", "second element" );
		
		when(strucuredSelection.toList()).thenReturn(expected);
		
		List<Object> actual = selection.asList();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void ensureThatAllElementsAreOfTheSameType() throws Exception {
		List<String> expected = Arrays.asList( "first element", "second element" );
		
		when(strucuredSelection.iterator()).thenReturn(expected.iterator());
		
		boolean actual = selection.ofSameNodeType();
		
		assertTrue(actual);
	}

	@Test
	public void ensureThatAllElementsAreNotOfTheSameType() throws Exception {
		List<Object> expected = Arrays.asList( (Object) "first element", "second element", new Integer(1) );
		
		when(strucuredSelection.iterator()).thenReturn(expected.iterator());
		
		boolean actual = selection.ofSameNodeType();
		
		assertFalse(actual);
	}

	@Test
	public void ensureThatSelectionContainsOnlyOneTyp() throws Exception {
		when(strucuredSelection.size()).thenReturn(1);
		
		boolean actual = selection.ofSingleNode();
		
		assertTrue(actual);
	}
	
	@Test
	public void ensureThatSelectionContainsDifferentTypes() throws Exception {
		when(strucuredSelection.size()).thenReturn(2);
		
		boolean actual = selection.ofSingleNode();
		
		assertFalse(actual);
	}
	
	@Test
	public void shouldQuerySelectedElementsByStringType() throws Exception {
		List<Object> expected = Arrays.asList( 
			(Object) "first element", "second element", new Integer(1) 
		);
		when(strucuredSelection.iterator()).thenReturn(expected.iterator());

		List<String> actual = selection.byType(String.class);
		
		assertEquals("first element", actual.get(0));
		assertEquals("second element", actual.get(1));
		assertEquals(2, actual.size());
	}
	
	@Test
	public void shouldQuerySelectedElementsByIntegerType() throws Exception {
		List<Object> expected = Arrays.asList( 
			(Object) "first element", "second element", new Integer(1) 
		);
		when(strucuredSelection.iterator()).thenReturn(expected.iterator());

		List<Integer> actual = selection.byType(Integer.class);
		
		assertEquals(new Integer(1), actual.get(0));
		assertEquals(1, actual.size());
	}
	
	@Test
	public void ensureThatLaunchNodeIsSelected() throws Exception {
		when(strucuredSelection.getFirstElement()).thenReturn(new LaunchNode());
		
		boolean launchNodeSelected = selection.firstElementHasType(LaunchNode.class);
		
		assertTrue(launchNodeSelected); 
	}
	
	@Test
	public void ensureThatLaunchNodeIsNotSelected() throws Exception {
		when(strucuredSelection.getFirstElement()).thenReturn("cannot be launched");
		
		boolean launchNodeSelected = selection.firstElementHasType(LaunchNode.class);
		
		assertFalse(launchNodeSelected); 
	}
	
	@Test
	public void ensureThatLaunchTypeNodeIsSelected() throws Exception {
		when(strucuredSelection.getFirstElement()).thenReturn(new LaunchTypeNode());
		
		boolean launchNodeSelected = selection.firstElementHasType(LaunchTypeNode.class);
		
		assertTrue(launchNodeSelected); 
	}
	
	@Test
	public void ensureThatLaunchTypeNodeIsNotSelected() throws Exception {
		when(strucuredSelection.getFirstElement()).thenReturn("cannot be launched");
		
		boolean launchNodeSelected = selection.firstElementHasType(LaunchTypeNode.class);
		
		assertFalse(launchNodeSelected); 
	}
	
	@Test
	public void ensureThatCategoryNodeIsSelected() throws Exception {
		when(strucuredSelection.getFirstElement()).thenReturn(new CategoryNode("test"));
		
		boolean launchNodeSelected = selection.firstElementHasType(CategoryNode.class);
		
		assertTrue(launchNodeSelected); 
	}
	
	@Test
	public void ensureThatCategoryNodeIsNotSelected() throws Exception {
		when(strucuredSelection.getFirstElement()).thenReturn("cannot be launched");
		
		boolean launchNodeSelected = selection.firstElementHasType(CategoryNode.class);
		
		assertFalse(launchNodeSelected); 
	}
	
	@Test
	public void shouldReturnLaunchNodesWhenNothingElseIsSelected() throws Exception {
		ILaunchConfiguration configuration = mock(ILaunchConfiguration.class);
		
		LaunchNode launchNode = new LaunchNode();
		launchNode.setLaunchConfiguration(configuration);
		
		List<Object> expected = Arrays.asList( 
			(Object) launchNode
		);
		
		when(strucuredSelection.getFirstElement()).thenReturn(launchNode);
		when(strucuredSelection.iterator()).thenReturn(expected.iterator(), expected.iterator());
		
		List<ILaunchNode> nodes = selection.getSelectedLaunchNodes();
		
		assertEquals(nodes.size(), 1);
		assertEquals(nodes.get(0), launchNode);
	}
	
	@Test
	public void shouldReturnLaunchNodesWhenMoreTypesOrSelected() throws Exception {
		ILaunchConfiguration configuration = mock(ILaunchConfiguration.class);
		
		LaunchNode launchNode = new LaunchNode();
		launchNode.setLaunchConfiguration(configuration);
		
		List<Object> expected = Arrays.asList( 
			(Object) launchNode, "aaaa"
		);
		
		when(strucuredSelection.getFirstElement()).thenReturn(launchNode);
		when(strucuredSelection.iterator()).thenReturn(expected.iterator(), expected.iterator());
		
		List<ILaunchNode> nodes = selection.getSelectedLaunchNodes();

		assertEquals(nodes.size(), 0);
	}
	
	@Test
	public void ensureThatSelectionCanBeLaunched() throws Exception {
		LaunchNode launchNode = new LaunchNode();
		
		when(strucuredSelection.getFirstElement()).thenReturn(launchNode);
		when(strucuredSelection.size()).thenReturn(1);
		
		boolean canBeLaunched = selection.canBeLaunched();

		assertTrue(canBeLaunched);
	}
	
	@Test
	public void ensureThatSelectionCannotBeLaunchedWhenMoreElementsAreSelected() throws Exception {
		LaunchNode launchNode = new LaunchNode();
		
		when(strucuredSelection.getFirstElement()).thenReturn(launchNode);
		when(strucuredSelection.size()).thenReturn(2);
		
		boolean canBeLaunched = selection.canBeLaunched();

		assertFalse(canBeLaunched);
	}
	
	@Test
	public void ensureThatSelectionCannotBeLaunchedWhenFirstElementIsNotLaunchable() throws Exception {
		CategoryNode categoryNode = new CategoryNode("aaa");
		
		when(strucuredSelection.getFirstElement()).thenReturn(categoryNode);
		when(strucuredSelection.size()).thenReturn(1);
		
		boolean canBeLaunched = selection.canBeLaunched();

		assertFalse(canBeLaunched);
	}
	
	@Test
	public void ensureThatSelectionCanBeRenamed() throws Exception {
		LaunchNode launchNode = new LaunchNode();
		
		List<Object> expected = Arrays.asList( 
			(Object) launchNode
		);
			
		when(strucuredSelection.iterator()).thenReturn(expected.iterator());
		when(strucuredSelection.getFirstElement()).thenReturn(launchNode);
		when(strucuredSelection.size()).thenReturn(1);
		
		boolean canBeRenamed = selection.canBeRenamed();

		assertTrue(canBeRenamed);
	}
	
	@Test
	public void ensureThatSelectionCannotBeRenamedWhenTwoTypesAreSelected() throws Exception {
		LaunchNode launchNode = new LaunchNode();
		CategoryNode categoryNode = new CategoryNode("aaa");
		
		List<Object> expected = Arrays.asList( 
			(Object) launchNode, categoryNode
		);
			
		when(strucuredSelection.iterator()).thenReturn(expected.iterator());
		when(strucuredSelection.getFirstElement()).thenReturn(launchNode);
		when(strucuredSelection.size()).thenReturn(1);
		
		boolean canBeRenamed = selection.canBeRenamed();

		assertFalse(canBeRenamed);
	}
	
	@Test
	public void ensureThatSelectionCannotBeRenamedWhenOneSelectedElementIsNotRenamable() throws Exception {
		LaunchTypeNode typeNode = new LaunchTypeNode();
		
		List<Object> expected = Arrays.asList( 
			(Object) typeNode
		);
			
		when(strucuredSelection.iterator()).thenReturn(expected.iterator());
		when(strucuredSelection.getFirstElement()).thenReturn(typeNode);
		when(strucuredSelection.size()).thenReturn(1);
		
		boolean canBeRenamed = selection.canBeRenamed();

		assertFalse(canBeRenamed);
	}
	
//	getSelectedLaunchNode()
//	getSelectedCategoryNode()
//	canBeRemoved()
//	canBeBookmarked()
		
}
