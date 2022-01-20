package de.lases.selenium.security;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InjectionTest {

    private final WebDriver driver = WebDriverFactory.createFirefoxWebDriver();

    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    private static final String ORIGINAL_ADMIN_EMAIL = "admin@example.com";
    private static final String ORIGINAL_ADMIN_PASSWORD = "admin1!ADMIN";
    private static final String TEST_ADMIN_EMAIL = "thomas.kirz2+lasesadmin@gmail.com";
    private static final String TEST_ADMIN_PASSWORD = "Password1!";

    private static final String TEST_TITLE = "\";DROP TABLE user;--";

    /**
     * Prerequisite: Admin 'thomas.kirz2+lasesadmin@gmail.com' w/ Password 'Password1!' exists in the database.
     */
    @Test
    public void injectionTest() {
        // Create Security forum
        createForumAndAddAdmin();

        driver.get(WebDriverFactory.LOCALHOST_URL);

        // Scroll to and click on 'Register' button
        WebElement registerButton = driver.findElement(By.xpath("//div[3]/form/input[2]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)", registerButton);
        Actions actions = new Actions(driver);
        actions.moveToElement(registerButton);
        actions.perform();
        registerButton.click();

        driver.findElement(By.id("register-frm:title-itxt")).clear();
        driver.findElement(By.id("register-frm:title-itxt")).sendKeys(TEST_TITLE);
        driver.findElement(By.id("register-frm:first-name-itxt")).click();
        driver.findElement(By.id("register-frm:first-name-itxt")).clear();
        driver.findElement(By.id("register-frm:first-name-itxt")).sendKeys("Bobby");
        driver.findElement(By.id("register-frm:last-name-itxt")).click();
        driver.findElement(By.id("register-frm:last-name-itxt")).clear();
        driver.findElement(By.id("register-frm:last-name-itxt")).sendKeys("Tables");
        driver.findElement(By.id("register-frm:email-itxt")).click();
        driver.findElement(By.id("register-frm:email-itxt")).clear();
        driver.findElement(By.id("register-frm:email-itxt")).sendKeys("thomas.kirz+injectiontest@gmail.com");
        driver.findElement(By.id("register-frm:password-iscrt")).click();
        driver.findElement(By.id("register-frm:password-iscrt")).clear();
        driver.findElement(By.id("register-frm:password-iscrt")).sendKeys("Password1!");

        // Scroll to and click on 'Register' button
        WebElement registerButton2 = driver.findElement(By.id("register-frm:register-cbtn"));
        js.executeScript("arguments[0].scrollIntoView(true)", registerButton2);
        actions.moveToElement(registerButton2);
        actions.perform();
        registerButton2.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='content']/div/ul/li[7]")));
        String verificationURL = driver.findElement(By.xpath("//div[@id='content']/div/ul/li[7]")).getText();
        driver.get(verificationURL);
        driver.findElement(By.id("go-to-homepage-lnk")).click();
        driver.findElement(By.linkText("Foren")).click();
        driver.findElement(By.linkText("Security Conference")).click();
        driver.findElement(By.xpath("//div[2]/form/input[2]")).click();
        driver.findElement(By.id("new-submission-form:submission-title-itxt")).click();
        driver.findElement(By.id("new-submission-form:submission-title-itxt")).clear();
        driver.findElement(By.id("new-submission-form:submission-title-itxt"))
                .sendKeys("<script>alert('XSS');</script>");

        // upload file
        String fullPathToPdfFile = Paths.get(System.getProperty("user.dir"),
                "/src/it/java/de/lases/selenium/testsuite/paper.pdf").toString();

        driver.findElement(By.id("new-submission-form:pdf-upload-ifile")).sendKeys(fullPathToPdfFile);

        driver.findElement(By.id("new-submission-form:submitcbtn")).click();

        // Submission title should be escaped and displayed correctly
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("info-submission:submission-title-otxt")));
        assertEquals("<script>alert('XSS');</script>",
                driver.findElement(By.id("info-submission:submission-title-otxt")).getText());

        driver.findElement(By.linkText("Profil")).click();

        // Title and name should be displayed correctly
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-profile-form:title-itxt")));
        assertEquals(TEST_TITLE,
                driver.findElement(By.id("edit-profile-form:title-itxt")).getAttribute("value"));
        assertEquals("Bobby",
                driver.findElement(By.id("edit-profile-form:firstname-itxt")).getAttribute("value"));

        // Scroll to and click on 'Delete' button
        WebElement deleteButton = driver.findElement(By.id("delete-profile-form:delete-cbtn"));
        js.executeScript("arguments[0].scrollIntoView(true)", deleteButton);
        deleteButton.click();

        driver.findElement(By.id("delete-profile-form:delete-really-cbtn")).click();

        // Delete Forum
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login-form:email-itxt")));
        deleteForum();

        // Delete Admin
        deleteAdmin();
    }

    private void createForumAndAddAdmin() {
        driver.get(WebDriverFactory.LOCALHOST_URL);

        // Login as original admin
        WebElement emailBox = driver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = driver.findElement(By.id("login-form:password-iscrt"));
        emailBox.sendKeys(ORIGINAL_ADMIN_EMAIL);
        passwordBox.sendKeys(ORIGINAL_ADMIN_PASSWORD);
        driver.findElement(By.id("login-form:login-cbtn")).click();

        // Create second admin
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-administration-link")));
        driver.findElement(By.id("nav-administration-link")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("new-user-link")));
        driver.findElement(By.id("new-user-link")).click();
        driver.findElement(By.id("register-frm:password-iscrt")).click();
        driver.findElement(By.id("register-frm:password-iscrt")).clear();
        driver.findElement(By.id("register-frm:password-iscrt")).sendKeys("Password1!");
        driver.findElement(By.id("register-frm:first-name-itxt")).click();
        driver.findElement(By.id("register-frm:first-name-itxt")).clear();
        driver.findElement(By.id("register-frm:first-name-itxt")).sendKeys("Thomas");
        driver.findElement(By.id("register-frm:last-name-itxt")).click();
        driver.findElement(By.id("register-frm:last-name-itxt")).clear();
        driver.findElement(By.id("register-frm:last-name-itxt")).sendKeys("James II");
        driver.findElement(By.id("register-frm:email-itxt")).click();
        driver.findElement(By.id("register-frm:email-itxt")).clear();
        driver.findElement(By.id("register-frm:email-itxt")).sendKeys(TEST_ADMIN_EMAIL);
        driver.findElement(By.id("register-frm:is-admin-cbx")).click();
        driver.findElement(By.id("register-frm:save-btn")).click();

        // Login as new admin
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-frm:logout-cbtn")));
        driver.findElement(By.id("logout-frm:logout-cbtn")).click();
        driver.findElement(By.id("login-form:email-itxt")).sendKeys(TEST_ADMIN_EMAIL);
        driver.findElement(By.id("login-form:password-iscrt")).sendKeys(TEST_ADMIN_PASSWORD);
        driver.findElement(By.id("login-form:login-cbtn")).click();

        driver.findElement(By.id("nav-forumlist-link")).click();

        driver.findElement(By.id("new-forum-link")).click();

        // add editor.
        WebElement emailField = driver.findElement(By.id("add-editors-form:email-editor-itxt"));
        emailField.sendKeys(TEST_ADMIN_EMAIL);
        driver.findElement(By.id("add-editors-form:add-editor-btn")).click();


        // scroll down manually as buttons are hidden
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)", driver.findElement(By.id("create-forum-form:save-btn")));
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.id("main-content")));
        actions.perform();

        // enter the rest of the data.
        driver.findElement(By.id("create-forum-form:forum-name-itxt")).sendKeys("Security Conference");
        driver.findElement(By.id("create-forum-form:deadline-itxt")).sendKeys("30.12.2099, 22:00:00");
        driver.findElement(By.id("create-forum-form:url-itxt")).sendKeys("https://security.las.es/");
        driver.findElement(By.id("create-forum-form:description-itxt")).sendKeys("Es geht um Sicherheit.");
        driver.findElement(By.id("create-forum-form:review-manual-itxt")).sendKeys("Begutachten Sie.");

        driver.findElement(By.id("create-forum-form:save-btn")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-frm:logout-cbtn")));
        driver.findElement(By.id("logout-frm:logout-cbtn")).click();
    }

    private void deleteForum() {
        WebElement emailBox = driver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = driver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys(TEST_ADMIN_EMAIL);
        passwordBox.sendKeys(TEST_ADMIN_PASSWORD);

        driver.findElement(By.id("login-form:login-cbtn")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-forumlist-link")));
        driver.findElement(By.id("nav-forumlist-link")).click();

        driver.findElement(By.linkText("Security Conference")).click();

        driver.findElement(By.id("forum-delete-frm:delete-forum-cbtn")).click();
        driver.findElement(By.id("forum-delete-frm:agree-delete-cbtn")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-frm:logout-cbtn")));
        driver.findElement(By.id("logout-frm:logout-cbtn")).click();
    }

    private void deleteAdmin() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Log in
        driver.get(WebDriverFactory.LOCALHOST_URL);
        WebElement emailBox = driver.findElement(By.id("login-form:email-itxt"));
        WebElement passwordBox = driver.findElement(By.id("login-form:password-iscrt"));

        emailBox.sendKeys(TEST_ADMIN_EMAIL);
        passwordBox.sendKeys(TEST_ADMIN_PASSWORD);

        driver.findElement(By.id("login-form:login-cbtn")).click();

        driver.findElement(By.linkText("Profil")).click();

        // Scroll to and click on 'Delete' button
        WebElement deleteButton = driver.findElement(By.id("delete-profile-form:delete-cbtn"));
        js.executeScript("arguments[0].scrollIntoView(true)", deleteButton);
        deleteButton.click();
        driver.findElement(By.id("delete-profile-form:delete-really-cbtn")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login-form:email-itxt")));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}

