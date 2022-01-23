package de.lases.selenium.security;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionFixationTest {

    private WebDriver driver;

    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_PASSWORD = "admin1!ADMIN";

    private static final String SESSION_ID_IDENTIFIER = "sessionid";

    @BeforeEach
    public void setUp() {
        driver = WebDriverFactory.createFirefoxWebDriverNoCookies();
    }

    @Test
    public void testNotAuthenticated() {
        driver.get(WebDriverFactory.LOCALHOST_URL);

        // Click on logo so session id is written in url
        driver.findElement(By.xpath("//nav/a")).click();

        Pattern pattern = Pattern.compile(".*" + SESSION_ID_IDENTIFIER + "=([\\w]*).*");
        Matcher matcher = pattern.matcher(driver.getCurrentUrl());

        assertTrue(matcher.find());

        String sessionId = matcher.group(1);

        login();

        driver.get(WebDriverFactory.LOCALHOST_URL + "views/authenticated/homepage.xhtml" + ";" +
                SESSION_ID_IDENTIFIER + "=" + sessionId);

        assertTrue(driver.getCurrentUrl().startsWith(WebDriverFactory.LOCALHOST_URL + "views/anonymous/welcome.xhtml"));
    }

    private void login() {
        WebElement emailBox = driver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = driver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys(ADMIN_EMAIL);
        passwordBox.sendKeys(ADMIN_PASSWORD);

        driver.findElement(By.id("login-form:login-cbtn")).click();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
