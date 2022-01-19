package de.lases.selenium.stress;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public class UserCreatesForumAndSubmission implements Callable<List<ResponseTimeEntry>> {

    private static int globalId = 1;

    private final WebDriver driver;
    private final JavascriptExecutor js;

    public UserCreatesForumAndSubmission() {
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        driver = new FirefoxDriver(options);
        js = (JavascriptExecutor) driver;
    }

    @Override
    public List<ResponseTimeEntry> call() {
        try {
            return userCreatesForumAndSubmission(globalId++);
        } finally {
            tearDown();
        }
    }

    private void tearDown() {
        driver.quit();
    }

    private List<ResponseTimeEntry> userCreatesForumAndSubmission(int globalId) {
        List<ResponseTimeEntry> responseTimes = new LinkedList<>();

        driver.get(Stress.URL);
        driver.manage().window().setSize(new Dimension(1536, 832));
        driver.findElement(By.id("login-form:email-itxt")).click();
        driver.findElement(By.id("login-form:email-itxt")).sendKeys("admin@example.com");
        driver.findElement(By.id("login-form:password-iscrt")).sendKeys("admin1!ADMIN");
        driver.findElement(By.id("login-form:login-cbtn")).click();
        driver.findElement(By.id("nav-administration-link")).click();
        driver.findElement(By.id("new-user-link")).click();
        driver.findElement(By.id("register-frm:password-iscrt")).click();
        driver.findElement(By.id("register-frm:password-iscrt")).sendKeys("Password1!");
        driver.findElement(By.id("register-frm:title-itxt")).click();
        driver.findElement(By.id("register-frm:first-name-itxt")).click();
        driver.findElement(By.id("register-frm:first-name-itxt")).sendKeys("StressorAndEditor");
        driver.findElement(By.id("register-frm:first-name-itxt")).click();
        driver.findElement(By.id("register-frm:first-name-itxt")).sendKeys("Stressor");
        driver.findElement(By.id("register-frm:last-name-itxt")).sendKeys("Stressmensch");
        driver.findElement(By.id("register-frm:email-itxt")).click();
        driver.findElement(By.id("register-frm:email-itxt")).sendKeys(globalId + "Stressor.Editor.Admin@sebastianvogt.me");
        driver.findElement(By.id("register-frm:is-admin-cbx")).click();
        driver.findElement(By.id("register-frm:save-btn")).click();
        driver.findElement(By.id("logout-frm:logout-cbtn")).click();
        driver.findElement(By.id("login-form:email-itxt")).click();
        driver.findElement(By.id("login-form:email-itxt")).sendKeys(globalId + "stressor.Editor.Admin@sebastianvogt.me");
        driver.findElement(By.id("login-form:password-iscrt")).sendKeys("Password1!");
        driver.findElement(By.id("login-form:login-cbtn")).click();
        driver.findElement(By.id("nav-administration-link")).click();
        driver.findElement(By.id("new-forum-link")).click();
        driver.findElement(By.id("add-editors-form:email-editor-itxt")).click();
        driver.findElement(By.id("add-editors-form:email-editor-itxt")).sendKeys(globalId + "stressor.editor.admin@sebastianvogt.me");
        driver.findElement(By.id("add-editors-form:add-editor-btn")).click();
        driver.findElement(By.id("create-forum-form:forum-name-itxt")).click();
        driver.findElement(By.id("create-forum-form:forum-name-itxt")).sendKeys(globalId + "Form für Stresstests");
        driver.findElement(By.id("create-forum-form:url-itxt")).click();
        driver.findElement(By.id("create-forum-form:url-itxt")).sendKeys("stress.test.test");
        driver.findElement(By.id("create-forum-form:description-itxt")).click();
        driver.findElement(By.id("create-forum-form:description-itxt")).sendKeys("Lasst uns den Server stressen");
        driver.findElement(By.id("create-forum-form:review-manual-itxt")).sendKeys("Lets stress the server");
        driver.findElement(By.cssSelector(".col")).click();
        driver.findElement(By.id("create-forum-form:save-btn")).click();
        driver.findElement(By.id("new-submission-frm:new-submission-btn")).click();
        driver.findElement(By.id("new-submission-form:submission-title-itxt")).click();
        driver.findElement(By.id("new-submission-form:submission-title-itxt")).sendKeys(globalId + "An Empirical Study about the Stress on Lases on a FIM Computer");

        String fullPathToPdfFile = System.getProperty("user.dir") + "/app/lases/src/it/java/de/lases/selenium/testsuite/paper.pdf";
        WebElement fileInput = driver.findElement(By.id("new-submission-form:pdf-upload-ifile"));
        fileInput.sendKeys(fullPathToPdfFile);

        driver.findElement(By.id("new-submission-form:submitcbtn")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(9));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("revision-required-frm:revision-required-cbtn")));
        driver.findElement(By.id("revision-required-frm:revision-required-cbtn")).click();
        driver.findElement(By.id("revision-required-frm:revision-required-cbtn")).click();
        driver.findElement(By.id("revision-required-frm:revision-deadline-itxt")).click();
        driver.findElement(By.cssSelector(".col-md-8 > .container")).click();

        fileInput = driver.findElement(By.id("upload-file-frm:upload-revision-ifile"));
        fileInput.sendKeys(fullPathToPdfFile);

        js.executeScript("window.scroll(0, 0);");
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.id("upload-file-frm:upload-clnk")));
        actions.perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("upload-file-frm:upload-clnk")));
        driver.findElement(By.id("upload-file-frm:upload-clnk")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id *= '1:release-revision-cbtn']")));
        driver.findElement(By.cssSelector("input[id *= '1:release-revision-cbtn']")).click();
        driver.findElement(By.id("accept-reject-frm:reject-submission-cbtn")).click();
        driver.findElement(By.id("info-submission:forum-lnk")).click();
        driver.findElement(By.id("forum-delete-frm:delete-forum-cbtn")).click();
        driver.findElement(By.id("forum-delete-frm:agree-delete-cbtn")).click();
        driver.findElement(By.id("nav-profile-link")).click();
        driver.findElement(By.id("delete-profile-form:delete-cbtn")).click();
        {
            WebElement element = driver.findElement(By.id("delete-profile-form:delete-cbtn"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("delete-profile-form:delete-really-cbtn")).click();

        return responseTimes;
    }
}
