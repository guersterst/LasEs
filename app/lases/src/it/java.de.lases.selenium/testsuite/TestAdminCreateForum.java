package testsuite;

import factory.WebDriverFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.*;

@Order(10)
public class TestAdminCreateForum {

    WebDriver webDriver = WebDriverFactory.createFirefoxWebDriver();

    @Test()
    @DisplayName("/T010/")
    void adminLoginCreateForum() {
        webDriver.get(WebDriverFactory.LOCALHOST_URL);

        WebElement emailBox = webDriver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = webDriver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys("kirz@fim.uni-passau.de");
        passwordBox.sendKeys("UniDorfen1870!");

        webDriver.findElement(By.id("login-form:login-cbtn")).click();

        webDriver.findElement(By.id("nav-forumlist-link")).click();

        webDriver.findElement(By.id("new-forum-link")).click();

        // Enter details into form as specified by /T010/.

        // add editor.
        WebElement emailField = webDriver.findElement(By.id("add-editors-form:email-editor-itxt"));
        emailField.sendKeys("guerster@fim.uni-passau.de");
        webDriver.findElement(By.id("add-editors-form:add-editor-btn")).click();


        // scroll down manually as buttons are hidden
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].scrollIntoView(true)", webDriver.findElement(By.id("create-forum-form:save-btn")));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webDriver.findElement(By.id("main-content")));
        actions.perform();


        // enter the rest of the data.

        webDriver.findElement(By.id("create-forum-form:forum-name-itxt")).sendKeys("Chemie Tagung");
        // todo: here we added time
        webDriver.findElement(By.id("create-forum-form:deadline-itxt")).sendKeys("30.12.2099, 22:00:00");
        webDriver.findElement(By.id("create-forum-form:url-itxt")).sendKeys("https://ch.em.ie/");
        webDriver.findElement(By.id("create-forum-form:description-itxt")).sendKeys("Es geht um Chemie.");
        webDriver.findElement(By.id("create-forum-form:review-manual-itxt")).sendKeys("Begutachten Sie.");

        webDriver.findElement(By.id("create-forum-form:save-btn")).click();

        assertAll(
                () -> assertTrue(webDriver.getCurrentUrl().contains("scientificForum.xhtml")),
                () -> assertEquals("Chemie Tagung", webDriver.findElement(
                        By.id("forum-information-frm:forum-name-itxt")).getAttribute("value"))
        );
        webDriver.close();
    }

}
