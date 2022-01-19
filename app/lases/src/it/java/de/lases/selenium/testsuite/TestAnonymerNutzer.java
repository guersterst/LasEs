package de.lases.selenium.testsuite;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Order(110)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestAnonymerNutzer {

    private static WebDriver webDriver;

    private static String verifyLink;

    @BeforeAll
    static void createWebdriver() {
        webDriver = WebDriverFactory.createFirefoxWebDriver();
    }

    @AfterAll
    static void closeWebdriver() {
        webDriver.close();
    }

    @Test()
    @DisplayName("/T110/")
    void test001UnauthenticatedAccessAttempt() {
        webDriver.get(WebDriverFactory.LOCALHOST_URL + "views/authenticated/homepage.xhtml");

        String errorMessage = webDriver.findElement(By.id("global-msgs")).findElement(By.tagName("li")).getText();
        assertEquals("Bitte melden Sie sich zuerst an.", errorMessage);
    }

    @Test()
    @DisplayName("/T120/")
    void test002Register() {
        webDriver.findElement(By.id("register-frm:register-cbtn")).click();

        webDriver.findElement(By.id("register-frm:title-itxt")).sendKeys("Prof. Dr.");
        webDriver.findElement(By.id("register-frm:first-name-itxt")).sendKeys("Christian");
        webDriver.findElement(By.id("register-frm:last-name-itxt")).sendKeys("Bachmaier");
        webDriver.findElement(By.id("register-frm:email-itxt")).sendKeys("garstenaue@fim.uni-passau.de");
        webDriver.findElement(By.id("register-frm:password-iscrt")).sendKeys("einsZwei3!5678");

        webDriver.findElement(By.id("register-frm:register-cbtn")).click();

        List<WebElement> messages = webDriver.findElement(By.id("global-msgs")).findElements(By.tagName("li"));

        for (WebElement message : messages) {
            if (message.getText().contains("http")) {
                verifyLink = message.getText();
            }
        }

        assertNotNull(verifyLink);
    }

    @Test()
    @DisplayName("/T130/")
    void test003Verify() {
        webDriver.get(verifyLink);

        WebElement message = webDriver.findElement(By.id("global-msgs")).findElement(By.tagName("li"));

        assertEquals("Ihre E-Mail-Adresse wurde erfolgreich verifiziert. Sie sind nun angemeldet.", message.getText());

        webDriver.findElement(By.id("go-to-homepage-lnk")).click();

        assertTrue(webDriver.getCurrentUrl().contains("homepage.xhtml"));
    }

    @Test()
    @DisplayName("/T140/")
    void test004SubmissionShown() {
        assertEquals("P != NP",
                webDriver.findElement(By.id("sft-cc:submissiontable-frm:submission-png:pagination:0:submission-lnk")).getText());
    }
}