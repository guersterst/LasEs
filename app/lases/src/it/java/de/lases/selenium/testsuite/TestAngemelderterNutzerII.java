package de.lases.selenium.testsuite;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@Order(150)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestAngemelderterNutzerII {

    private static WebDriver webDriver;

    @BeforeAll
    static void createWebdriver() {
        webDriver = WebDriverFactory.createFirefoxWebDriver();
    }

    @AfterAll
    static void closeWebdriver() {
        webDriver.close();
    }

    @Test()
    @DisplayName("/T150/")
    void test001DownloadReview() {
        webDriver.get(WebDriverFactory.LOCALHOST_URL);

        WebElement emailBox = webDriver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = webDriver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys("vogt@fim.uni-passau.de");
        passwordBox.sendKeys("TSVDorfen2002!");
        webDriver.findElement(By.id("login-form:login-cbtn")).click();

        // open the submission
        webDriver.findElement(By.id("sft-cc:submissiontable-frm:submission-png:pagination:0:submission-lnk")).click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("review-pg-frm:review-pg:pagination:0:download-review-btn")));

        WebElement downloadButton = webDriver.findElement(By.id("review-pg-frm:review-pg:pagination:0:download-review-btn"));
        assertTrue(downloadButton.isEnabled());
    }

    @Test()
    @DisplayName("/T160/")
    void test002DeleteSelf() {
        webDriver.findElement(By.id("nav-profile-link")).click();

        By deleteButton = By.id("delete-profile-form:delete-cbtn");
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton));

        webDriver.findElement(deleteButton).click();

        // confirm deletion
        webDriver.findElement(By.id("delete-profile-form:delete-really-cbtn")).click();

        wait.until(ExpectedConditions.urlContains("welcome"));
        assertTrue(webDriver.getCurrentUrl().contains("welcome"));
    }

    @Test()
    @DisplayName("/T170/")
    void test003CheckForSubmission() {
        webDriver.get(WebDriverFactory.LOCALHOST_URL);

        WebElement emailBox = webDriver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = webDriver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys("kirz@fim.uni-passau.de");
        passwordBox.sendKeys("UniDorfen1870!");
        webDriver.findElement(By.id("login-form:login-cbtn")).click();

        assertEquals("Keine Einträge",
                webDriver.findElement(By.id("sft-cc:submissiontable-frm:submission-png:no-entries-msg-otxt")).getText());
    }
}