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

@Order(1)
public class TestSetup {

    private static WebDriver webDriver;

    @BeforeAll
    static void createWebdriver() {
        webDriver = WebDriverFactory.createFirefoxWebDriver();
    }

    @AfterAll
    static void closeWebdriver() {
        //webDriver.close();
    }

    @Test()
    void testSetup() {
        webDriver.get(WebDriverFactory.LOCALHOST_URL);

        registerAndLogout("kirz@fim.uni-passau.de", "","Johanna", "Mayer", "UniDorfen1870!");
        registerAndLogout("schicho@fim.uni-passau.de", "","Franz", "Huber", "TSVDorfen2001!");
        registerAndLogout("vogt@fim.uni-passau.de", "","Petra", "Müller", "TSVDorfen2002!");
        registerAndLogout("guerster@fim.uni-passau.de", "","Tuti", "Aslan", "SupaDöner1970!");

        // kirz@fim.uni-passau.de is admin:

        WebElement emailBox = webDriver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = webDriver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys("admin@example.com");
        passwordBox.sendKeys("admin1!ADMIN");

        webDriver.findElement(By.id("login-form:login-cbtn")).click();

        webDriver.findElement(By.id("nav-userlist-link")).click();

        // Find user in list.
        WebElement tableBody = webDriver.findElement(By.id("user-tbl:user-table-frm:user-png:pagination")).findElement(By.tagName("tbody"));
        List<WebElement> elementList = tableBody.findElements(By.tagName("tr"));

        for (int i = 0; i < elementList.size(); i++) {
            WebElement webElement = elementList.get(i).findElements(By.tagName("td")).get(1).findElement(By.id("user-tbl:user-table-frm:user-png:pagination:" + i + ":name-lnk"));
            if (webElement.getText().equals("Johanna Mayer")) {

                System.out.println("found");
                WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.elementToBeClickable(By.id("user-tbl:user-table-frm:user-png:pagination:" + i + ":name-lnk")));
                webElement.click();
                break;
            }
        }

        webDriver.findElement(By.id("edit-admin-form:is-admin-cbx")).click();
        webDriver.findElement(By.id("edit-admin-form:is-admin-cbtn")).click();

        // Enter admin password
        WebElement passwordAdmin = webDriver.findElement(By.id("edit-admin-form:password-admin-iscrt"));
        passwordAdmin.sendKeys("admin1!ADMIN");

        webDriver.findElement(By.id("edit-admin-form:admin-save-cbtn")).click();
    }

    private static void registerAndLogout(String email, String title, String firstName, String lastName, String password) {
        webDriver.findElement(By.id("register-frm:register-cbtn")).click();

        webDriver.findElement(By.id("register-frm:title-itxt")).sendKeys(title);
        webDriver.findElement(By.id("register-frm:first-name-itxt")).sendKeys(firstName);
        webDriver.findElement(By.id("register-frm:last-name-itxt")).sendKeys(lastName);
        webDriver.findElement(By.id("register-frm:email-itxt")).sendKeys(email);
        webDriver.findElement(By.id("register-frm:password-iscrt")).sendKeys(password);

        webDriver.findElement(By.id("register-frm:register-cbtn")).click();

        List<WebElement> messages = webDriver.findElement(By.id("global-msgs")).findElements(By.tagName("li"));

        String verifyLink = null;

        for (WebElement message : messages) {
            if (message.getText().contains("http")) {
                verifyLink = message.getText();
            }
        }

        assertNotNull(verifyLink);
        webDriver.get(verifyLink);

        WebElement message = webDriver.findElement(By.id("global-msgs")).findElement(By.tagName("li"));

        assertEquals("Ihre E-Mail-Adresse wurde erfolgreich verifiziert. Sie sind nun angemeldet.", message.getText());

        webDriver.findElement(By.id("go-to-homepage-lnk")).click();

        assertTrue(webDriver.getCurrentUrl().contains("homepage.xhtml"));

        webDriver.findElement(By.id("logout-frm:logout-cbtn")).click();
        assertTrue(webDriver.getCurrentUrl().contains("welcome.xhtml"));
    }
}
