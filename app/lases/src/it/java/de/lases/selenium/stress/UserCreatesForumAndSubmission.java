package de.lases.selenium.stress;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class UserCreatesForumAndSubmission implements Callable<List<ResponseTimeEntry>> {

    private static int globalId = 1;

    private final WebDriver driver;
    private final JavascriptExecutor js;

    public UserCreatesForumAndSubmission() {
        FirefoxOptions options = new FirefoxOptions();
//        options.setHeadless(true);
        driver = new FirefoxDriver(options);
        js = (JavascriptExecutor) driver;
    }

    @Override
    public List<ResponseTimeEntry> call() {
        try {
            List<ResponseTimeEntry> entries = userCreatesForumAndSubmission(globalId++);
            tearDown();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Durchgelaufen: " + globalId);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return entries;
        } catch (Exception e) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Kaputt");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
            return new LinkedList<>();
        } finally {
            tearDown();
        }
    }

    private void tearDown() {
        driver.quit();
    }

    private List<ResponseTimeEntry> userCreatesForumAndSubmission(int globalId) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<ResponseTimeEntry> responseTimes = new LinkedList<>();

        long start = System.currentTimeMillis();
        driver.get(Stress.URL);
        long end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("callPage", end - start));

        TimeUnit.SECONDS.sleep(10);

        driver.manage().window().setSize(new Dimension(1536, 832));
        driver.findElement(By.id("login-form:email-itxt")).click();
        driver.findElement(By.id("login-form:email-itxt")).sendKeys("admin@example.com");
        driver.findElement(By.id("login-form:password-iscrt")).sendKeys("admin1!ADMIN");

        start = System.currentTimeMillis();
        driver.findElement(By.id("login-form:login-cbtn")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-administration-link")));
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:login", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("nav-administration-link")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:administration", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("new-user-link")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:newUser", end - start));

        TimeUnit.SECONDS.sleep(10);

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

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("register-frm:save-btn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:saveNewUser", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("logout-frm:logout-cbtn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:logOut", end - start));

        TimeUnit.SECONDS.sleep(10);

        driver.findElement(By.id("login-form:email-itxt")).click();
        driver.findElement(By.id("login-form:email-itxt")).sendKeys(globalId + "stressor.Editor.Admin@sebastianvogt.me");
        driver.findElement(By.id("login-form:password-iscrt")).sendKeys("Password1!");

        start = System.currentTimeMillis();
        driver.findElement(By.id("login-form:login-cbtn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:login", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("nav-administration-link")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:administration", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("new-forum-link")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:newForum", end - start));

        TimeUnit.SECONDS.sleep(10);

        driver.findElement(By.id("add-editors-form:email-editor-itxt")).click();
        driver.findElement(By.id("add-editors-form:email-editor-itxt")).sendKeys(globalId + "stressor.editor.admin@sebastianvogt.me");

        start = System.currentTimeMillis();
        driver.findElement(By.id("add-editors-form:add-editor-btn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:newForumAddEditor", end - start));

        TimeUnit.SECONDS.sleep(10);

        driver.findElement(By.id("create-forum-form:forum-name-itxt")).click();
        driver.findElement(By.id("create-forum-form:forum-name-itxt")).sendKeys(globalId + "Form für Stresstests");
        driver.findElement(By.id("create-forum-form:url-itxt")).click();
        driver.findElement(By.id("create-forum-form:url-itxt")).sendKeys("stress.test.test");
        driver.findElement(By.id("create-forum-form:description-itxt")).click();
        driver.findElement(By.id("create-forum-form:description-itxt")).sendKeys("Lasst uns den Server stressen");
        driver.findElement(By.id("create-forum-form:review-manual-itxt")).sendKeys("Lets stress the server");
        driver.findElement(By.cssSelector(".col")).click();

        start = System.currentTimeMillis();
        driver.findElement(By.id("create-forum-form:save-btn")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("new-submission-frm:new-submission-btn")));
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:newForumSave", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("new-submission-frm:new-submission-btn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:newSubmission", end - start));

        TimeUnit.SECONDS.sleep(10);

        driver.findElement(By.id("new-submission-form:submission-title-itxt")).click();
        driver.findElement(By.id("new-submission-form:submission-title-itxt")).sendKeys(globalId + "An Empirical Study about the Stress on Lases on a FIM Computer");

        String fullPathToPdfFile = Paths.get(System.getProperty("user.dir"),
                "/src/it/java/de/lases/selenium/testsuite/paper.pdf").toString();
        WebElement fileInput = driver.findElement(By.id("new-submission-form:pdf-upload-ifile"));
        fileInput.sendKeys(fullPathToPdfFile);

        start = System.currentTimeMillis();
        driver.findElement(By.id("new-submission-form:submitcbtn")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("revision-required-frm:revision-required-cbtn")));
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:newSubmissionSubmit", end - start));

        TimeUnit.SECONDS.sleep(10);

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

        start = System.currentTimeMillis();
        driver.findElement(By.id("upload-file-frm:upload-clnk")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id *= '1:release-revision-cbtn']")));
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:newSubmissionRevision", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.cssSelector("input[id *= '1:release-revision-cbtn']")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:newSubmissionReleaseRevision", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("accept-reject-frm:reject-submission-cbtn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:newSubmissionRejectRevision", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("info-submission:forum-lnk")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:forum", end - start));

        TimeUnit.SECONDS.sleep(10);

        driver.findElement(By.id("forum-delete-frm:delete-forum-cbtn")).click();

        start = System.currentTimeMillis();
        driver.findElement(By.id("forum-delete-frm:agree-delete-cbtn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:forumDelete", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("nav-profile-link")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:profile", end - start));

        TimeUnit.SECONDS.sleep(10);

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

        start = System.currentTimeMillis();
        driver.findElement(By.id("delete-profile-form:delete-really-cbtn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:profileDelete", end - start));

        return responseTimes;
    }
}
