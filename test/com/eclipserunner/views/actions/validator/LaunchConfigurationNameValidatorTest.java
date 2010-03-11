package com.eclipserunner.views.actions.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.eclipserunner.Messages;

public class LaunchConfigurationNameValidatorTest {

	private static final String EXISTING_NAME = "config";
	private static final String EXISTING_CATEGORY_NAME = "exists";
	private static final String NOT_EXISTING_CATEGORY_NAME = "not_exists";
	
	static {
		Messages.Message_catogoryNameNotValid = "aaaa";
		Messages.Message_errorLaunchConfigurationAlreadyExists = "bbbb";
	}
	
	@Mock
	private ILaunchManager luanchManagerMock;
	
	@Before
	public void initMocks() throws CoreException {
		MockitoAnnotations.initMocks(this);

		when(luanchManagerMock.isExistingLaunchConfigurationName(EXISTING_CATEGORY_NAME)).thenReturn(true);
		when(luanchManagerMock.isExistingLaunchConfigurationName(NOT_EXISTING_CATEGORY_NAME)).thenReturn(false);
	}
	
	@Test
	public void testIsValidEmptyString() throws Exception {
		LaunchConfigurationNameValidator validator = new LaunchConfigurationNameValidator(EXISTING_NAME, luanchManagerMock);
		
		assertFalse(validator.isValid("").isEmpty());
	}
	
	@Test
	public void testIsValidTrimString() throws Exception {
		LaunchConfigurationNameValidator validator = new LaunchConfigurationNameValidator(EXISTING_NAME, luanchManagerMock);
		
		assertFalse(validator.isValid("    ").isEmpty());
	}
	
	@Test
	public void testIsValidConfigurationExists() throws Exception {
		LaunchConfigurationNameValidator validator = new LaunchConfigurationNameValidator(EXISTING_NAME, luanchManagerMock);
		
		assertFalse(validator.isValid(EXISTING_CATEGORY_NAME).isEmpty());
		
		verify(luanchManagerMock).isExistingLaunchConfigurationName(anyString());
	}
	
	@Test
	public void testIsValidSunnyDayScenario() throws Exception {
		LaunchConfigurationNameValidator validator = new LaunchConfigurationNameValidator(EXISTING_NAME, luanchManagerMock);
		
		assertNull(validator.isValid(NOT_EXISTING_CATEGORY_NAME));
		
		verify(luanchManagerMock).isExistingLaunchConfigurationName(anyString());
	}
	
	@Test
	public void testIsValidInitialValue() throws Exception {
		LaunchConfigurationNameValidator validator = new LaunchConfigurationNameValidator(EXISTING_NAME, luanchManagerMock);
		
		assertNull(validator.isValid(EXISTING_NAME));
	}
}
