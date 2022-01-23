package de.lases.selenium.factory;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class WebDriverFactory {

    public static String LOCALHOST_URL = "https://sebastianvogt.me/lases-1.0-SNAPSHOT/";
    //public static String LOCALHOST_URL = "http://horizon.fim.uni-passau.de:8002/lases_war_exploded/";

    public static WebDriver createFirefoxWebDriver() {

        //System.setProperty("webdriver.gecko.driver", "/home/guerster/geckodriver");

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--allow-insecure-localhost");
        firefoxOptions.addPreference("intl.accept_languages", "de-DE");
        WebDriver webDriver = new FirefoxDriver(firefoxOptions);

        Dimension dm = new Dimension(1800,950);
        webDriver.manage().window().setSize(dm);

        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

        return webDriver;
    }

    public static WebDriver createFirefoxWebDriverNoCookies() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--allow-insecure-localhost");
        firefoxOptions.addPreference("intl.accept_languages", "de-DE");
        firefoxOptions.addPreference("network.cookie.cookieBehavior", 2);

        return new FirefoxDriver(firefoxOptions);
    }
}
