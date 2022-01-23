package de.lases.selenium.security;

import de.lases.selenium.factory.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IDORTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String TEST_PASSWORD = "Password1!";
    private static final String ORIGINAL_ADMIN_EMAIL = "admin@example.com";
    private static final String ORIGINAL_ADMIN_PASSWORD = "admin1!ADMIN";
    private static final String TEST_ADMIN_EMAIL = "thomas.kirz2+lasesadmin@gmail.com";
    private static final String AUTHOR_EMAIL = "thomas.kirz+author@gmail.com";
    private static final String ATTACKER_EMAIL = "thomas.kirz+sus@gmail.com";

    @BeforeEach
    public void setUp() {
        driver = WebDriverFactory.createFirefoxWebDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void testNotAuthenticated() {
        createForumAndAddAdmin();
        registerAuthor();
        uploadSubmission();

        // Get submission id from url
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("info-submission:submission-title-otxt")));
        Pattern pattern = Pattern.compile(".*" + "id=(\\d+).*");
        Matcher matcher = pattern.matcher(driver.getCurrentUrl());
        assertTrue(matcher.find());
        String submissionId = matcher.group(1);

        driver.findElement(By.id("logout-frm:logout-cbtn")).click();

        registerAttacker();

        // Try to access the submission
        driver.get(WebDriverFactory.LOCALHOST_URL + "views/authenticated/submission.xhtml?id=" + submissionId);

        // Should be redirected to 404 page
        assertTrue(driver.getCurrentUrl().contains("views/anonymous/errorPage.xhtml"));

        // Log out
        driver.get(WebDriverFactory.LOCALHOST_URL);
        driver.findElement(By.id("logout-frm:logout-cbtn")).click();

        deleteUsers();
        deleteForum();
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
        driver.findElement(By.id("login-form:password-iscrt")).sendKeys(TEST_PASSWORD);
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

        emailBox.sendKeys(ORIGINAL_ADMIN_EMAIL);
        passwordBox.sendKeys(ORIGINAL_ADMIN_PASSWORD);

        driver.findElement(By.id("login-form:login-cbtn")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-forumlist-link")));
        driver.findElement(By.id("nav-forumlist-link")).click();

        driver.findElement(By.linkText("Security Conference")).click();

        driver.findElement(By.id("forum-delete-frm:delete-forum-cbtn")).click();
        driver.findElement(By.id("forum-delete-frm:agree-delete-cbtn")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-frm:logout-cbtn")));
        driver.findElement(By.id("logout-frm:logout-cbtn")).click();
    }

    private void registerAuthor() {
        // Scroll to and click on 'Register' button
        WebElement registerButton = driver.findElement(By.xpath("//div[3]/form/input[2]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)", registerButton);
        Actions actions = new Actions(driver);
        actions.moveToElement(registerButton);
        actions.perform();
        registerButton.click();

        driver.findElement(By.id("register-frm:first-name-itxt")).click();
        driver.findElement(By.id("register-frm:first-name-itxt")).clear();
        driver.findElement(By.id("register-frm:first-name-itxt")).sendKeys("Andy");
        driver.findElement(By.id("register-frm:last-name-itxt")).click();
        driver.findElement(By.id("register-frm:last-name-itxt")).clear();
        driver.findElement(By.id("register-frm:last-name-itxt")).sendKeys("Author");
        driver.findElement(By.id("register-frm:email-itxt")).click();
        driver.findElement(By.id("register-frm:email-itxt")).clear();
        driver.findElement(By.id("register-frm:email-itxt")).sendKeys(AUTHOR_EMAIL);
        driver.findElement(By.id("register-frm:password-iscrt")).click();
        driver.findElement(By.id("register-frm:password-iscrt")).clear();
        driver.findElement(By.id("register-frm:password-iscrt")).sendKeys(TEST_PASSWORD);

        // Scroll to and click on 'Register' button
        WebElement registerButton2 = driver.findElement(By.id("register-frm:register-cbtn"));
        js.executeScript("arguments[0].scrollIntoView(true)", registerButton2);
        actions.moveToElement(registerButton2);
        actions.perform();
        registerButton2.click();

        // Verify email
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='content']/div/ul/li[7]")));
        String verificationURL = driver.findElement(By.xpath("//div[@id='content']/div/ul/li[7]")).getText();
        driver.get(verificationURL);
        driver.findElement(By.id("go-to-homepage-lnk")).click();
    }

    private void uploadSubmission() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Foren")));
        driver.findElement(By.linkText("Foren")).click();
        driver.findElement(By.linkText("Security Conference")).click();
        driver.findElement(By.xpath("//div[2]/form/input[2]")).click();
        driver.findElement(By.id("new-submission-form:submission-title-itxt")).click();
        driver.findElement(By.id("new-submission-form:submission-title-itxt")).clear();
        driver.findElement(By.id("new-submission-form:submission-title-itxt"))
                .sendKeys("Steelo - You can't style on a stylist");

        // upload file
        String fullPathToPdfFile = Paths.get(System.getProperty("user.dir"),
                "/src/it/java/de/lases/selenium/testsuite/paper.pdf").toString();

        driver.findElement(By.id("new-submission-form:pdf-upload-ifile")).sendKeys(fullPathToPdfFile);

        driver.findElement(By.id("new-submission-form:submitcbtn")).click();
    }

    private void registerAttacker() {
        // Scroll to and click on 'Register' button
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[3]/form/input[2]")));
        WebElement registerButton = driver.findElement(By.xpath("//div[3]/form/input[2]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)", registerButton);
        Actions actions = new Actions(driver);
        actions.moveToElement(registerButton);
        actions.perform();
        registerButton.click();

        driver.findElement(By.id("register-frm:first-name-itxt")).click();
        driver.findElement(By.id("register-frm:first-name-itxt")).clear();
        driver.findElement(By.id("register-frm:first-name-itxt")).sendKeys("Peter");
        driver.findElement(By.id("register-frm:last-name-itxt")).click();
        driver.findElement(By.id("register-frm:last-name-itxt")).clear();
        driver.findElement(By.id("register-frm:last-name-itxt")).sendKeys("Pagan");
        driver.findElement(By.id("register-frm:email-itxt")).click();
        driver.findElement(By.id("register-frm:email-itxt")).clear();
        driver.findElement(By.id("register-frm:email-itxt")).sendKeys(ATTACKER_EMAIL);
        driver.findElement(By.id("register-frm:password-iscrt")).click();
        driver.findElement(By.id("register-frm:password-iscrt")).clear();
        driver.findElement(By.id("register-frm:password-iscrt")).sendKeys(TEST_PASSWORD);

        // Scroll to and click on 'Register' button
        WebElement registerButton2 = driver.findElement(By.id("register-frm:register-cbtn"));
        js.executeScript("arguments[0].scrollIntoView(true)", registerButton2);
        actions.moveToElement(registerButton2);
        actions.perform();
        registerButton2.click();

        // Verify email
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='content']/div/ul/li[7]")));
        String verificationURL = driver.findElement(By.xpath("//div[@id='content']/div/ul/li[7]")).getText();
        driver.get(verificationURL);
        driver.findElement(By.id("go-to-homepage-lnk")).click();
    }

    private void deleteUsers() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        List.of(AUTHOR_EMAIL, ATTACKER_EMAIL, TEST_ADMIN_EMAIL).forEach(email -> {
            // Log in
            driver.get(WebDriverFactory.LOCALHOST_URL);
            WebElement emailBox = driver.findElement(By.id("login-form:email-itxt"));
            WebElement passwordBox = driver.findElement(By.id("login-form:password-iscrt"));

            emailBox.sendKeys(email);
            passwordBox.sendKeys(TEST_PASSWORD);

            driver.findElement(By.id("login-form:login-cbtn")).click();

            driver.findElement(By.linkText("Profil")).click();

            // Scroll to and click on 'Delete' button
            WebElement deleteButton = driver.findElement(By.id("delete-profile-form:delete-cbtn"));
            js.executeScript("arguments[0].scrollIntoView(true)", deleteButton);
            deleteButton.click();
            driver.findElement(By.id("delete-profile-form:delete-really-cbtn")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("login-form:email-itxt")));
        });
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
