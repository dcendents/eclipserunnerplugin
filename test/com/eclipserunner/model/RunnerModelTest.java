package com.eclipserunner.model;

import static com.eclipserunner.matchers.RunnerMatchers.anyLaunchConfigurationCotegory;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RunnerModelTest {

	@Mock
	private IViewPart viewPartMock;

	@Mock
	private IViewSite viewSiteMock;

	@Mock
	private IModelChangeListener modelListenerMock;

	@Mock
	private ILaunchConfiguration launchConfigurationMock;

	@Mock
	private ILaunchConfigurationCategory launchConfigurationCategoryMock;

	@Mock
	private Set<ILaunchConfigurationCategory> launchConfigurationSetMock;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);

		when(viewPartMock.getViewSite()).thenReturn(viewSiteMock);
		when(launchConfigurationSetMock.iterator()).thenReturn(new ArrayList<ILaunchConfigurationCategory>().iterator());
	}

	@Test
	public void testAddLaunchConfigurationCategory() {
		RunnerModel runnerModel = new RunnerModel();

		// setup
		runnerModel.addModelChangeListener(modelListenerMock);
		runnerModel.setLaunchConfigurationCategories(launchConfigurationSetMock);

		// test
		runnerModel.addLaunchConfigurationCategory("test");

		verify(launchConfigurationSetMock).add(anyLaunchConfigurationCotegory());
		verify(modelListenerMock).modelChanged();
	}

	@Test
	public void testAddUncategorizedLaunchConfiguration() {
		RunnerModel runnerModel = new RunnerModel();

		// setup
		runnerModel.addModelChangeListener(modelListenerMock);
		runnerModel.setLaunchConfigurationCategories(launchConfigurationSetMock);

		// test
		runnerModel.addLaunchConfiguration(launchConfigurationMock);

		verify(modelListenerMock).modelChanged();
	}

	@Test
	public void testRemoveLaunchConfiguration() {
		RunnerModel runnerModel = new RunnerModel();

		// setup
		runnerModel.addModelChangeListener(modelListenerMock);
		runnerModel.setLaunchConfigurationCategories(launchConfigurationSetMock);

		// test
		runnerModel.removeLaunchConfiguration(launchConfigurationMock);

		verify(modelListenerMock).modelChanged();
	}

	@Test
	public void testRemoveLaunchConfigurationCategory() {
		RunnerModel runnerModel = new RunnerModel();

		// setup
		runnerModel.addModelChangeListener(modelListenerMock);
		runnerModel.setLaunchConfigurationCategories(launchConfigurationSetMock);

		// test
		runnerModel.removeLaunchConfigurationCategory(launchConfigurationCategoryMock);

		verify(launchConfigurationSetMock).remove(anyLaunchConfigurationCotegory());
		verify(modelListenerMock).modelChanged();
	}

}
