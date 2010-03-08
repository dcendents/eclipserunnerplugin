package com.eclipserunner.matchers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.eclipserunner.model.RunnerModelTest;
import com.eclipserunner.utils.SelectionUtilsTest;

@RunWith(Suite.class)
@SuiteClasses(value = {
	RunnerModelTest.class,
	SelectionUtilsTest.class
})
public class SuiteAllRunnerTests {

}
