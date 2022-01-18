package de.lases.selenium.testsuite;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(200)
public class TestFehlerhafterUndIllegalerZugriff {

    WebDriver webDriver = WebDriverFactory.createFirefoxWebDriver();

    @Test
    @DisplayName("/T200/")
    void testBadAccess() {
        webDriver.get(WebDriverFactory.LOCALHOST_URL);

        WebElement emailBox = webDriver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = webDriver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys("guerster@fim.uni-passau.de");
        passwordBox.sendKeys("SupaDöner1970!");

        webDriver.findElement(By.id("login-form:login-cbtn")).click();

        // Call a random non-existing page.
        webDriver.get(WebDriverFactory.LOCALHOST_URL + "freebitcoin.html");

        assertTrue(webDriver.findElement(By.id("error-message-otxt")).getText().contains("404"));

        webDriver.close();
    }
}
