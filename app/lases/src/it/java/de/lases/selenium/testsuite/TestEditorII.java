package de.lases.selenium.testsuite;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(100)
public class TestEditorII {

    WebDriver webDriver = WebDriverFactory.createFirefoxWebDriver();

    @Test
    @DisplayName("/T100/")
    void testReleaseReview() {
        webDriver.get(WebDriverFactory.LOCALHOST_URL);

        WebElement emailBox = webDriver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = webDriver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys("guerster@fim.uni-passau.de");
        passwordBox.sendKeys("SupaDöner1970!");

        webDriver.findElement(By.id("login-form:login-cbtn")).click();

        webDriver.findElement(By.id("tabs:editor-tab-cmdlnk")).click();

        WebElement tableBody = webDriver.findElement(By.id("sft-cc:submissiontable-frm:submission-png:pagination")).findElement(By.tagName("tbody"));
        List<WebElement> elementList = tableBody.findElements(By.tagName("tr"));

        for (int i = 0; i < elementList.size(); i++) {
            WebElement webElement = elementList.get(i).findElement(By.tagName("td")).findElement(By.id("sft-cc:submissiontable-frm:submission-png:pagination:"+ i + ":submission-lnk"));
            if (webElement.getText().equals("P != NP")) {

                //System.out.println("found");
                WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.elementToBeClickable(By.id("sft-cc:submissiontable-frm:submission-png:pagination:"+ i + ":submission-lnk")));
                webElement.click();
                break;
            }
        }

        // release review, wait for long page load
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        By releaseButton = By.id("review-pg-frm:review-pg:pagination:0:set-visible-cbtn");
        wait.until(ExpectedConditions.elementToBeClickable(releaseButton));

        webDriver.findElement(releaseButton).click();

        List<WebElement> messages = webDriver.findElement(By.id("global-msgs")).findElements(By.tagName("li"));

        assertTrue(messages.size() > 1);
        webDriver.close();
    }
}
