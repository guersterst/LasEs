package de.lases.selenium.testsuite;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Order(85)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestGutachter {

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
    @DisplayName("/T085/")
    void test001ReviewerLoginAndAccept() {
        webDriver.get(WebDriverFactory.LOCALHOST_URL);

        WebElement emailBox = webDriver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = webDriver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys("schicho@fim.uni-passau.de");
        passwordBox.sendKeys("TSVDorfen2001!");
        webDriver.findElement(By.id("login-form:login-cbtn")).click();

        // view own reviews
        webDriver.findElement(By.id("tabs:reviewer-tab-cmdlnk")).click();

        // click on submission
        WebElement tableBody = webDriver.findElement(By.id("sft-cc:submissiontable-frm:submission-png:pagination"))
                .findElement(By.tagName("tbody"));
        List<WebElement> tableEntries = tableBody.findElements(By.tagName("tr"));
        tableEntries.get(0).findElement(By.tagName("a")).click();

        // accept
        By acceptBtn = By.id("review-request-frm:review-accept-cbtn");
        (new WebDriverWait(webDriver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(acceptBtn));
        webDriver.findElement(acceptBtn).click();

        assertEquals("Ihre Änderungen wurden erfolgreich gespeichert.",
                webDriver.findElement(By.id("global-msgs")).findElement(By.tagName("li")).getText());
    }

    @Test()
    @DisplayName("/T090/")
    void test002ReviewerUploadReview() {
        // click to upload.
        webDriver.findElement(By.id("review-request-frm:new-review-cbtn")).click();

        // upload file
        WebElement reviewUploadFileDialog = webDriver.findElement(By.id("upload-review-form:pdf-upload-ifile"));
        String fullPath = Paths.get(System.getProperty("user.dir"), "/src/it/java/de/lases/selenium/testsuite/gutachten.pdf").toString();

        reviewUploadFileDialog.sendKeys(fullPath);

        // submit
        webDriver.findElement(By.id("upload-review-form:upload-cbtn")).click();

        // wait for long page load
        // review-pg-frm:review-pg:pagination
        By table = By.id("review-pg-frm:review-pg:pagination");
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(table));

        List<WebElement> reviewList = webDriver.findElement(table).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

        assertEquals(1, reviewList.size());
    }
}