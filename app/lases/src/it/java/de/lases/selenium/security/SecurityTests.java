package de.lases.selenium.security;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Security Test Suite")
@SelectPackages("de.lases.selenium.security")
@IncludeClassNamePatterns({"^.*Test$"})
public class SecurityTests {
}
