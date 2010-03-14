package com.eclipserunner.model.adapters;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.ILaunchConfigurationNode;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.impl.LaunchConfigurationTypeNode;

/**
 * @author vachacz
 */
public class TreeWithTypesAdapterTest {

	@Mock IViewPart viewPart;
	@Mock IViewSite viewSite;
	@Mock IRunnerModel model;
	
	@Mock ILaunchConfigurationType type1;
	@Mock ILaunchConfigurationType type2;
	
	@Mock ILaunchConfigurationCategory defaultCategory;
	@Mock ILaunchConfiguration defaultLaunch;
	
	@Mock ILaunchConfigurationCategory category1;
	@Mock ILaunchConfiguration cat1launch1;
	@Mock ILaunchConfiguration cat1launch2;
	@Mock ILaunchConfiguration cat1launch3;
	
	@Mock ILaunchConfigurationCategory category2;
	@Mock ILaunchConfiguration cat2launch1;
	@Mock ILaunchConfiguration cat2launch2;
	
	@Mock ILaunchConfigurationCategory category3;
	
	@Before
	public void setupModel() throws CoreException {
		MockitoAnnotations.initMocks(this);
		
		List<ILaunchConfigurationCategory> categories = 
			Arrays.asList(defaultCategory, category1, category2, category3);
		
		when(viewPart.getViewSite()).thenReturn(viewSite);
		
		when(model.getDefaultCategory()).thenReturn(defaultCategory);
		when(model.getLaunchConfigurationCategories()).thenReturn(categories);
		when(model.isDefaultCategoryVisible()).thenReturn(true);
		
		configureLaunch(defaultLaunch, type1, "c");
		configureLaunch(cat1launch1, type1, "a");
		configureLaunch(cat1launch2, type2, "g");
		configureLaunch(cat1launch3, type2, "w");
		configureLaunch(cat2launch1, type1, "e");
		configureLaunch(cat2launch2, type2, "b");
		
		configureCategory(defaultCategory, defaultLaunch);
		configureCategory(category1, cat1launch1, cat1launch2, cat1launch3);
		configureCategory(category2, cat2launch1, cat2launch2);
		configureCategory(category3);
	}

	// getElements tests
	
	@Test
	public void testGetElementsRoot() throws Exception {
		RunnerModelTreeWithTypesAdapter adapter = new RunnerModelTreeWithTypesAdapter(model, viewPart);
		
		Object[] elements = adapter.getElements(viewSite);
		
		assertArrayEquals(elements, new Object[] {defaultCategory, category1, category2, category3});
	}
	
	@Test
	public void testGetElementsCategory() throws Exception {
		RunnerModelTreeWithTypesAdapter adapter = new RunnerModelTreeWithTypesAdapter(model, viewPart);
		
		Object[] elements = adapter.getElements(category1);
		
		assertEqualsTypeNodeArrays(elements, asTypeNodeArray(category1, type1, type2));
	}
	
	@Test
	public void testGetElementsTypeNode() throws Exception {
		RunnerModelTreeWithTypesAdapter adapter = new RunnerModelTreeWithTypesAdapter(model, viewPart);
		
		Object[] elements = adapter.getElements(asTypeNode(type2, category1));
		
		assertEqualsTypeNodeArray(elements, cat1launch2, cat1launch3);
	}
	
	@Test
	public void testGetElementsLuanchNode() throws Exception {
		RunnerModelTreeWithTypesAdapter adapter = new RunnerModelTreeWithTypesAdapter(model, viewPart);
		
		Object[] elements = adapter.getElements(cat1launch2);
		
		Assert.assertNull(elements);
	}
	
	// hasChildren tests
	
	@Test
	public void testHasChildrenCategory() throws Exception {
		RunnerModelTreeWithTypesAdapter adapter = new RunnerModelTreeWithTypesAdapter(model, viewPart);
		
		boolean hasChildren = adapter.hasChildren(category1);
		
		Assert.assertTrue(hasChildren);
	}
	
	@Test
	public void testHasChildrenCategoryWithoutChildren() throws Exception {
		RunnerModelTreeWithTypesAdapter adapter = new RunnerModelTreeWithTypesAdapter(model, viewPart);
		
		boolean hasChildren = adapter.hasChildren(category3);
		
		Assert.assertFalse(hasChildren);
	}
	
	@Test
	public void testHasChildrenLaunchConfig() throws Exception {
		RunnerModelTreeWithTypesAdapter adapter = new RunnerModelTreeWithTypesAdapter(model, viewPart);
		
		boolean hasChildren = adapter.hasChildren(cat1launch2);
		
		Assert.assertFalse(hasChildren);
	}
	
	@Test
	public void testHasChildrenLaunchType() throws Exception {
		RunnerModelTreeWithTypesAdapter adapter = new RunnerModelTreeWithTypesAdapter(model, viewPart);
		
		boolean hasChildren = adapter.hasChildren(asTypeNode(type2, category1));
		
		Assert.assertTrue(hasChildren);
	}
	
	// getParent tests
	
	@Test
	public void testGetParentLaunch() throws Exception {
		RunnerModelTreeWithTypesAdapter adapter = new RunnerModelTreeWithTypesAdapter(model, viewPart);
		
		Object parent = adapter.getParent(createConfigNode(category1, cat1launch2));
		
		assertTypeNode((LaunchConfigurationTypeNode) parent, asTypeNode(type1, category1));
	}
	
	@Test
	public void testGetParentTypeNode() throws Exception {
		RunnerModelTreeWithTypesAdapter adapter = new RunnerModelTreeWithTypesAdapter(model, viewPart);
		
		Object parent = adapter.getParent(asTypeNode(type2, category1));
		
		Assert.assertEquals(parent, category1);
	}
	
	@Test
	public void testGetParentCategory() throws Exception {
		RunnerModelTreeWithTypesAdapter adapter = new RunnerModelTreeWithTypesAdapter(model, viewPart);
		
		Object parent = adapter.getParent(category1);
		
		Assert.assertNull(parent);
	}
	
	// custom asserts
	
	private void assertEqualsTypeNodeArray(Object[] elements, ILaunchConfiguration ...configs) {
		if (elements.length != configs.length) {
			Assert.fail();
		}
		for (int i = 0; i < elements.length; i++) {
			ILaunchConfigurationNode node = (ILaunchConfigurationNode) elements[i];
			if (!node.getLaunchConfiguration().equals(configs[i])) {
				Assert.fail();
			}
		}
	}

	private void assertEqualsTypeNodeArrays(Object[] elements, Object[] typeNodes) {
		if (elements.length != typeNodes.length) {
			Assert.fail();
		}
		for (int i = 0; i < elements.length; i++) {
			LaunchConfigurationTypeNode node1 = (LaunchConfigurationTypeNode) elements[i];
			LaunchConfigurationTypeNode node2 = (LaunchConfigurationTypeNode) typeNodes[i];
			
			if (!node1.getType().equals(node2.getType())) {
				Assert.fail();
			}
		}
	}
	
	private void assertTypeNode(LaunchConfigurationTypeNode parent, LaunchConfigurationTypeNode typeNode) {
		Assert.assertEquals(parent.getType(), typeNode.getType());
		Assert.assertEquals(parent.getParentCategory(), typeNode.getParentCategory());
	}

	// helpers
	
	private void configureLaunch(ILaunchConfiguration config, ILaunchConfigurationType type, String name) throws CoreException {
		when(config.getType()).thenReturn(type);
		when(config.getName()).thenReturn(name);
	}
	
	private Collection<ILaunchConfigurationNode> createLaunchNodes(ILaunchConfigurationCategory category, ILaunchConfiguration ...configs) {
		List<ILaunchConfigurationNode> nodes = new ArrayList<ILaunchConfigurationNode>(configs.length);
		for (ILaunchConfiguration config : configs) {
			nodes.add(createConfigNode(category, config));
		}
		return nodes;
	}

	private ILaunchConfigurationNode createConfigNode(ILaunchConfigurationCategory category, ILaunchConfiguration config) {
		ILaunchConfigurationNode node = mock(ILaunchConfigurationNode.class);
		when(node.getLaunchConfiguration()).thenReturn(config);
		when(node.getLaunchConfigurationCategory()).thenReturn(category);
		return node;
	}

	private void configureCategory(ILaunchConfigurationCategory category, ILaunchConfiguration ...launches) {
		Collection<ILaunchConfigurationNode> launchNodes = createLaunchNodes(category, launches);
		when(category.getLaunchConfigurationNodes()).thenReturn(launchNodes);
		if (launches.length == 0) {
			when(category.isEmpty()).thenReturn(true);
		} else {
			when(category.isEmpty()).thenReturn(false);
		}
	}
	
	private Object[] asTypeNodeArray(ILaunchConfigurationCategory category, ILaunchConfigurationType ...types) {
		List<LaunchConfigurationTypeNode> typeNode = new ArrayList<LaunchConfigurationTypeNode>();
		for (ILaunchConfigurationType type : types) {
			typeNode.add(asTypeNode(type, category));
		}
		return typeNode.toArray();
	}

	private LaunchConfigurationTypeNode asTypeNode(ILaunchConfigurationType type, ILaunchConfigurationCategory category) {
		LaunchConfigurationTypeNode node = mock(LaunchConfigurationTypeNode.class);
		when(node.getType()).thenReturn(type);
		when(node.getParentCategory()).thenReturn(category);
		return node;
	}
}
