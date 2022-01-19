package de.lases.selenium.testsuite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
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
