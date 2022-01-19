package de.lases.selenium.example;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({FirstTestClass.class, SecondTestClass.class, AAAAATEst.class})
public class ExampleTestSuite {
}
