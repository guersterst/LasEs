package de.lases.selenium.testsuite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({TestAdminCreateForum.class, TestAngemelderterNutzerI.class})
public class ProductionTestSuite {
}
