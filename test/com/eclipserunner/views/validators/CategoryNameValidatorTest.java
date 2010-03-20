package com.eclipserunner.views.validators;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.eclipserunner.Messages;
import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.impl.CategoryNode;

public class CategoryNameValidatorTest {

	private static final String INITIAL_CATEGORY_NAME = "category";
	private static final String EXISTING_CATEGORY_NAME = "exists";
	private static final String NOT_EXISTING_CATEGORY_NAME = "not_exists";

	static {
		Messages.Message_errorCatogoryEmptyName = "aaaa";
		Messages.Message_errorCategoryAlreadyExists = "bbbb";
	}

	@Mock
	private IRunnerModel runnerModelMock;

	@Before
	public void initMocks() throws CoreException {
		MockitoAnnotations.initMocks(this);

		Collection<ICategoryNode> categoryNodes = new HashSet<ICategoryNode>();
		categoryNodes.add(new CategoryNode(EXISTING_CATEGORY_NAME));
		when(runnerModelMock.getCategoryNodes()).thenReturn(categoryNodes);
	}


	@Test
	public void testIsValidEmptyString() throws Exception {
		CategoryNameValidator validator = new CategoryNameValidator(INITIAL_CATEGORY_NAME, runnerModelMock);

		assertTrue(Messages.Message_errorCatogoryEmptyName.equals(validator.isValid("")));
	}

	@Test
	public void testIsValidWhitespaceString() throws Exception {
		CategoryNameValidator validator = new CategoryNameValidator(INITIAL_CATEGORY_NAME, runnerModelMock);

		assertTrue(Messages.Message_errorCatogoryEmptyName.equals(validator.isValid("    ")));
	}

	@Test
	public void testIsValidCategoryNameExists() throws Exception {
		CategoryNameValidator validator = new CategoryNameValidator(INITIAL_CATEGORY_NAME, runnerModelMock);

		assertTrue(Messages.Message_errorCategoryAlreadyExists.equals(validator.isValid(EXISTING_CATEGORY_NAME)));
		verify(runnerModelMock).getCategoryNodes();
	}

	@Test
	public void testIsValidCategoryNameNotExists() throws Exception {
		CategoryNameValidator validator = new CategoryNameValidator(INITIAL_CATEGORY_NAME, runnerModelMock);

		assertNull(validator.isValid(NOT_EXISTING_CATEGORY_NAME));
		verify(runnerModelMock).getCategoryNodes();
	}

	@Test
	public void testIsValidInitialValue() throws Exception {
		CategoryNameValidator validator = new CategoryNameValidator(INITIAL_CATEGORY_NAME, runnerModelMock);

		assertNull(validator.isValid(INITIAL_CATEGORY_NAME));
	}

}
