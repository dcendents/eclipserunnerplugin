package com.eclipserunner.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LauchTreeContentProviderTest {

	@Mock
	private IViewPart viewPartMock;
	
	@Mock
	private IViewSite viewSiteMock;
	
	@Mock
	private IModelChangeListener modelListenerMock;

	@Mock
	private ILaunchConfiguration launchConfigurationMock;
	
    @Before 
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        
        when(viewPartMock.getViewSite()).thenReturn(viewSiteMock);
    }
	
	@Test
	public void testAddLaunchConfigurationCategory() {
		LaunchTreeContentProvider contentProvider = new LaunchTreeContentProvider();

		contentProvider.addChangeListener(modelListenerMock);
		contentProvider.addLaunchConfigurationCategory("test");
		
		verify(modelListenerMock).modelChanged();
	}

	@Test
	public void testAddUncategorizedLaunchConfiguration() {
		LaunchTreeContentProvider contentProvider = new LaunchTreeContentProvider();

		contentProvider.addChangeListener(modelListenerMock);
		contentProvider.addLaunchConfiguration(launchConfigurationMock);
		
		verify(modelListenerMock).modelChanged();
	}

	@Test
	public void testGetElementsViewPortParent() {
		LaunchTreeContentProvider contentProvider = new LaunchTreeContentProvider();

		contentProvider.setViewPart(viewPartMock);
		contentProvider.addLaunchConfiguration(launchConfigurationMock);
		
		Object[] elements = contentProvider.getElements(viewSiteMock);
		
		assertEquals(elements.length, 1);
		assertEquals(elements[0], contentProvider.getUncategorizedCategory());
		
		verify(viewPartMock).getViewSite();
	}
	
	@Test
	public void testGetElementsCategory() {
		LaunchTreeContentProvider contentProvider = new LaunchTreeContentProvider();

		contentProvider.setViewPart(viewPartMock);
		contentProvider.addLaunchConfiguration(launchConfigurationMock);
		
		Object[] elements = contentProvider.getElements(contentProvider.getUncategorizedCategory());
		
		assertEquals(elements.length, 1);
		assertEquals(elements[0], launchConfigurationMock);
		
		verify(viewPartMock).getViewSite();
	}
}
