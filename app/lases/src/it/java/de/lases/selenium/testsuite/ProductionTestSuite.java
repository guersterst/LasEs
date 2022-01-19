package de.lases.selenium.testsuite;

import org.junit.jupiter.api.DisplayName;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@DisplayName("Integration Tests")
@SelectClasses({
        TestSetup.class,
        TestReset.class,
        TestAddReviewer.class,
        TestAdminCreateForum.class,
        TestAngemelderterNutzerI.class,
        TestAngemelderterNutzerII.class,
        TestAnonymerNutzer.class,
        TestEditorII.class,
        TestFehlerhafterUndIllegalerZugriff.class,
        TestGutachter.class})
public class ProductionTestSuite {
}
