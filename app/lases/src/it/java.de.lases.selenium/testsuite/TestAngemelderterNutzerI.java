package testsuite;

import factory.WebDriverFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Order(20)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestAngemelderterNutzerI {

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
    @DisplayName("/T020/")
    void test001User1LoginAndNavigateToForum() {
        webDriver.get(WebDriverFactory.LOCALHOST_URL);

        WebElement emailBox = webDriver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = webDriver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys("vogt@fim.uni-passau.de");
        passwordBox.sendKeys("TSVDorfen2002!");
        webDriver.findElement(By.id("login-form:login-cbtn")).click();

        webDriver.findElement(By.id("global-search-frm:search-itxt")).sendKeys("Chemie Tagung");
        webDriver.findElement(By.id("global-search-frm:search-btn")).click();

        WebElement tableBody = webDriver.findElement(By.id("sft-cc:sft-frm:sft-pg:pagination")).findElement(By.tagName("tbody"));

        List<WebElement> tableEntries = tableBody.findElements(By.tagName("tr"));

        assertAll(
                () -> assertEquals(1, tableEntries.size()),
                () -> assertEquals("Chemie Tagung", tableEntries.get(0).findElement(By.tagName("a")).getText())
        );
    }

    @Test()
    @DisplayName("/T030/")
    void test002GoToUploadSubmission() {
        // Click on entry
        WebElement tableBody = webDriver.findElement(By.id("sft-cc:sft-frm:sft-pg:pagination")).findElement(By.tagName("tbody"));
        tableBody.findElements(By.tagName("tr")).get(0).findElement(By.tagName("a")).click();

        webDriver.findElement(By.id("new-submission-frm:new-submission-btn")).click();

        assertAll(
                () -> assertEquals("Chemie Tagung", webDriver.findElement(By.id("new-submission-form:forum-name-itxt")).getAttribute("value")),
                () -> assertTrue(webDriver.getCurrentUrl().contains("newSubmission.xhtml"))
        );
    }

    @Test()
    @DisplayName("/T040/")
    void test003UserUploadFile() {
        // upload file
    }

}
