package com.eclipserunner.matchers;

import static org.mockito.Matchers.argThat;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.mockito.ArgumentMatcher;

import com.eclipserunner.model.ILaunchConfigurationCategory;

public class RunnerMatchers {

	static class IsILaunchConfiguration extends ArgumentMatcher<ILaunchConfiguration> {
		@Override
		public boolean matches(Object object) {
			return (object instanceof ILaunchConfiguration);
		}
	}
	
	static class IsILaunchConfigurationCategory extends ArgumentMatcher<ILaunchConfigurationCategory> {
		@Override
		public boolean matches(Object object) {
			return (object instanceof ILaunchConfigurationCategory);
		}
	}
	
	public static ILaunchConfiguration anyLaunchConfiguration() {
		return argThat(new IsILaunchConfiguration());
	}
	
	public static ILaunchConfigurationCategory anyLaunchConfigurationCotegory() {
		return argThat(new IsILaunchConfigurationCategory());
	}
}
