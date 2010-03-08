package com.eclipserunner.model;

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
		IRunnerModel runnerModel = RunnerModel.getDefault();

		runnerModel.addChangeListener(modelListenerMock);
		runnerModel.addLaunchConfigurationCategory("test");

		verify(modelListenerMock).modelChanged();
	}

	@Test
	public void testAddUncategorizedLaunchConfiguration() {
		IRunnerModel runnerModel = RunnerModel.getDefault();

		runnerModel.addChangeListener(modelListenerMock);
		runnerModel.addLaunchConfiguration(launchConfigurationMock);

		verify(modelListenerMock).modelChanged();
	}

}
