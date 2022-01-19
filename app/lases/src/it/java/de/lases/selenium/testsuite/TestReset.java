package de.lases.selenium.testsuite;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

@Order(1000)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestReset {

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
    @DisplayName("removeForum")
    void testRemoveForum() {
        webDriver.get(WebDriverFactory.LOCALHOST_URL);

        // login as admin.
        WebElement emailBox = webDriver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = webDriver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys("admin@example.com");
        passwordBox.sendKeys("admin1!ADMIN");

        webDriver.findElement(By.id("login-form:login-cbtn")).click();

        webDriver.findElement(By.id("nav-userlist-link")).click();

        webDriver.findElement(By.id("nav-forumlist-link")).click();


        WebElement tableBody = webDriver.findElement(By.id("sft-tbl:sft-frm:sft-pg:pagination")).findElement(By.tagName("tbody"));
        List<WebElement> elementList = tableBody.findElements(By.tagName("tr"));

        for (int i = 0; i < elementList.size(); i++) {
            WebElement webElement = elementList.get(i).findElement(By.tagName("td")).findElement(By.id("sft-tbl:sft-frm:sft-pg:pagination:"+ i + ":forum-lnk"));
            if (webElement.getText().equals("Chemie Tagung")) {

                WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.elementToBeClickable(By.id("sft-tbl:sft-frm:sft-pg:pagination:"+ i + ":forum-lnk")));
                webElement.click();
                break;
            }
        }

        webDriver.findElement(By.id("forum-delete-frm:delete-forum-cbtn")).click();
        webDriver.findElement(By.id("forum-delete-frm:agree-delete-cbtn")).click();

        assertTrue(webDriver.getCurrentUrl().contains("homepage.xhtml"));
    }

    private static void removeUser(String name) {
        webDriver.findElement(By.id("nav-userlist-link")).click();


        WebElement tableBody = webDriver.findElement(By.id("user-tbl:user-table-frm:user-png:pagination")).findElement(By.tagName("tbody"));
        List<WebElement> elementList = tableBody.findElements(By.tagName("tr"));

        for (int i = 0; i < elementList.size(); i++) {
            WebElement webElement = elementList.get(i).findElements(By.tagName("td")).get(1).findElement(By.id("user-tbl:user-table-frm:user-png:pagination:" + i + ":name-lnk"));
            if (webElement.getText().equals(name)) {

                WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.elementToBeClickable(By.id("user-tbl:user-table-frm:user-png:pagination:" + i + ":name-lnk")));
                webElement.click();
                break;
            }
        }

        WebElement delete = webDriver.findElement(By.id("delete-profile-form:delete-cbtn"));

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-profile-form:delete-cbtn")));
        delete.click();

        webDriver.findElement(By.id("delete-profile-form:delete-really-cbtn")).click();

        assertTrue(webDriver.getCurrentUrl().contains("homepage.xhtml"));
    }

}
