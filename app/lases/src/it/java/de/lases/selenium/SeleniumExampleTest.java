package de.lases.selenium;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SeleniumExampleTest {

    WebDriver webDriver = WebDriverFactory.createFirefoxWebDriver();

    @Test
    void openLases() {
        webDriver.get(WebDriverFactory.LOCALHOST_URL);

        WebElement emailBox = webDriver.findElement(By.id("j_idt44:email-itxt"));
        WebElement passwordBox = webDriver.findElement(By.id("j_idt44:password-iscrt"));

        emailBox.sendKeys("kirz@fim.uni-passau.de");
        passwordBox.sendKeys("UniDorfen1870!");

        webDriver.findElement(By.id("j_idt44:login-cbtn")).click();

        // webdriver.quit();
    }
}
