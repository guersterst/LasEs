package de.lases.selenium.testsuite;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
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
        String fullPathToPdfFile = System.getProperty("user.dir") + "/src/it/java/de/lases/selenium/testsuite/paper.pdf";
        WebElement fileInput = webDriver.findElement(By.id("new-submission-form:pdf-upload-ifile"));
        fileInput.sendKeys(fullPathToPdfFile);

        // Can't read out value in upload box
    }

    @Test()
    @DisplayName("/T045/")
    void test004UserUEnterInvalidData() {
        webDriver.findElement(By.id("new-submission-form:submission-title-itxt")).sendKeys("P != NP");
        webDriver.findElement(By.id("co-authors-form:co-author-firstname-itxt")).sendKeys("Christian");
        webDriver.findElement(By.id("co-authors-form:co-author-lastname-itxt")).sendKeys("Bachmaier");
        webDriver.findElement(By.id("co-authors-form:co-author-email-itxt")).sendKeys("garstenaue");

        webDriver.findElement(By.id("co-authors-form:submit-co-author-cbtn")).click();

        WebElement errorMessage = webDriver.findElement(By.xpath("//*[@id=\"co-authors-form\"]/div[4]"));
        assertTrue(errorMessage.getText().contains("Ungültige E-Mail-Adresse."));
    }

    @Test()
    @DisplayName("/T050/")
    void test005UserSubmitSubmission() {
        webDriver.findElement(By.id("co-authors-form:co-author-email-itxt")).clear();
        webDriver.findElement(By.id("co-authors-form:co-author-email-itxt")).sendKeys("garstenaue@fim.uni-passau.de");

        webDriver.findElement(By.id("co-authors-form:submit-co-author-cbtn")).click();

        WebElement listCoAuthors = webDriver.findElement(By.id("co-authors-form:co-author-list"))
                .findElement(By.tagName("tbody"));

        // find again, because it has been updated.
        listCoAuthors = webDriver.findElement(By.id("co-authors-form:co-author-list"))
                .findElement(By.tagName("tbody"));

        List<WebElement> listEntries = listCoAuthors.findElements(By.tagName("tr"));

        assertAll(
                () -> assertEquals(1, listEntries.size()),
                // Create submission
                () -> {
                    webDriver.findElement(By.id("new-submission-form:submitcbtn")).click();
                    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
                    wait.until(ExpectedConditions.urlContains("submission.xhtml"));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("info-submission:submission-title-otxt")));
                    assertAll(
                            () -> assertTrue(webDriver.getCurrentUrl().contains("submission.xhtml")),
                            () -> assertEquals("P != NP",
                                    webDriver.findElement(By.id("info-submission:submission-title-otxt")).getText())
                    );
                }
        );
    }

    @Test
    @DisplayName("/T060/")
    void test006UserLogout() {
        webDriver.findElement(By.id("logout-frm:logout-cbtn")).click();
        assertTrue(webDriver.getCurrentUrl().contains("welcome.xhtml"));
    }
}