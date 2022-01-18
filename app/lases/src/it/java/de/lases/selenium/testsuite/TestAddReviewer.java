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

@Order(80)
public class TestAddReviewer {

    WebDriver webDriver = WebDriverFactory.createFirefoxWebDriver();

    @Test
    @DisplayName("/T080/")
    void testAddReviewer() {
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

                System.out.println("found");
                WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.elementToBeClickable(By.id("sft-cc:submissiontable-frm:submission-png:pagination:"+ i + ":submission-lnk")));
                webElement.click();
                break;
            }
        }

        webDriver.findElement(By.id("add-reviewer-frm:add-reviewer-itxt")).sendKeys("schicho@fim.uni-passau.de");
        webDriver.findElement(By.id("add-reviewer-frm:add-deadline-reviewer-itxt")).sendKeys("31.12.2022, 17:00:00 ");

        webDriver.findElement(By.id("add-reviewer-frm:add-reviewer-cbtn")).click();

        List<WebElement> messages = webDriver.findElement(By.id("global-msgs")).findElements(By.tagName("li"));
        boolean found = false;
        for (WebElement element : messages) {
            if (element.getText().equals("Ein neuer Gutachter wurde zu dieser Einreichung hinzugefügt.")) {
                found = true;
            }
        }

        webDriver.close();
        assertTrue(found);
    }
}
