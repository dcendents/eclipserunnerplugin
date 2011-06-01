package com.eclipserunner.views.actions;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.ILaunchTypeNode;
import com.eclipserunner.model.INodeSelection;
import com.eclipserunner.model.impl.CategoryNode;
import com.eclipserunner.model.impl.LaunchNode;
import com.eclipserunner.model.impl.LaunchTypeNode;

/**
 * BookmarkAction tests.
 * 
 * @author lwachowi
 */
public class BookmarkActionTest {

	@Mock INodeSelection selection;
	
	@Mock ILaunchNode     launchNode;
	@Mock ILaunchTypeNode launchTypeNode;
	@Mock ICategoryNode   categoryNode;
	
	private BookmarkAction action;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		action = new BookmarkAction(selection, false);
	}
	
	@Test
	public void shouldNotBookmarkWhenSelectionHasMultipleElements() throws Exception {
		when(selection.ofSameNodeType()).thenReturn(false);
		
		action.run();
	}
	
	@Test
	public void shouldBookmarkLaunchNode() throws Exception {
		when(selection.ofSameNodeType()).thenReturn(true);
		
		when(selection.firstElementHasType(LaunchNode.class)).thenReturn(true);
		when(selection.firstElementHasType(LaunchTypeNode.class)).thenReturn(false);
		when(selection.firstElementHasType(CategoryNode.class)).thenReturn(false);
		
		when(selection.getSelectedLaunchNodes()).thenReturn(Arrays.asList(launchNode));
		
		action.run();
		
		verify(launchNode).setBookmarked(false);
	}

	@Test
	public void shouldBookmarkLaunchTypeNode() throws Exception {
		when(selection.ofSameNodeType()).thenReturn(true);

		when(selection.firstElementHasType(LaunchNode.class)).thenReturn(false);
		when(selection.firstElementHasType(LaunchTypeNode.class)).thenReturn(true);
		when(selection.firstElementHasType(CategoryNode.class)).thenReturn(false);
		
		when(selection.getSelectedLaunchTypeNodes()).thenReturn(Arrays.asList(launchTypeNode));
		
		action.run();
		
		verify(launchTypeNode).setBookmarked(false);
	}

	@Test
	public void shouldBookmarkCategoryNode() throws Exception {
		when(selection.ofSameNodeType()).thenReturn(true);

		when(selection.firstElementHasType(LaunchNode.class)).thenReturn(false);
		when(selection.firstElementHasType(LaunchTypeNode.class)).thenReturn(false);
		when(selection.firstElementHasType(CategoryNode.class)).thenReturn(true);
		
		when(selection.getSelectedCategoryNodes()).thenReturn(Arrays.asList(categoryNode));
		
		action.run();
		
		verify(categoryNode).setBookmarked(false);
	}

}
