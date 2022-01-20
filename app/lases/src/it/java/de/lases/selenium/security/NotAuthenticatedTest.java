package de.lases.selenium.security;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotAuthenticatedTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = WebDriverFactory.createFirefoxWebDriver();
    }

    @Test
    public void testNotAuthenticated() {
        driver.get(WebDriverFactory.LOCALHOST_URL + "views/authenticated/homepage.xhtml");

        assertTrue(driver.getCurrentUrl().startsWith(WebDriverFactory.LOCALHOST_URL + "views/anonymous/welcome.xhtml"));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
