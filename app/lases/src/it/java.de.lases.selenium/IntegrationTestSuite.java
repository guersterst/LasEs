import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({FirstTestClass.class, SecondTestClass.class})
public class IntegrationTestSuite {
}
