package com.eclipserunner.model;

import static org.mockito.Mockito.verify;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LauchTreeContentProviderTest {

	@Mock
	private IModelChangeListener modelListenerMock;

	@Mock
	private ILaunchConfiguration launchConfigurationMock;
	
    @Before 
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
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
		contentProvider.addUncategorizedLaunchConfiguration(launchConfigurationMock);
		
		verify(modelListenerMock).modelChanged();
	}

	
}
