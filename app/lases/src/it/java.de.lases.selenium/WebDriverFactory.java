import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverService;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverFactory {

  public static String LOCALHOST_URL = "http://localhost:8080/lases_war_exploded/";

  public static WebDriver createFirefoxWebDriver() {
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    firefoxOptions.addArguments("--allow-insecure-localhost");

    return new FirefoxDriver(firefoxOptions);
  }
}
