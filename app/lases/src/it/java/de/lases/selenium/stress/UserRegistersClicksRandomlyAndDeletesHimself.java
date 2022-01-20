package de.lases.selenium.stress;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class UserRegistersClicksRandomlyAndDeletesHimself implements Callable<List<ResponseTimeEntry>> {

    private static int globalId = 1000;

    private final WebDriver driver;
    private final JavascriptExecutor js;

    public UserRegistersClicksRandomlyAndDeletesHimself() {
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        driver = new FirefoxDriver(options);
        js = (JavascriptExecutor) driver;
    }

    @Override
    public List<ResponseTimeEntry> call() {
        try {
            List<ResponseTimeEntry> entries = userRegistersClicksRandomlyAndDeletesHimself(globalId++);
            tearDown();
            System.out.println("?????????????????????????????????????????????????");
            System.out.println("Durchgelaufen: " + globalId);
            System.out.println("?????????????????????????????????????????????????");
            return entries;
        } catch (Exception e) {
            System.out.println("?????????????????????????????????????????????????");
            System.out.println("Kaputt");
            System.out.println("??????????????????????????????????????????????????//");
            e.printStackTrace();
            return new LinkedList<>();
        } finally {
            tearDown();
        }
    }

    private void tearDown() {
        driver.quit();
    }

    private List<ResponseTimeEntry> userRegistersClicksRandomlyAndDeletesHimself(int globalId) throws InterruptedException {
        List<ResponseTimeEntry> responseTimes = new LinkedList<>();

        long start = System.currentTimeMillis();
        driver.get(Stress.URL);
        long end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("callPage", end - start));

        TimeUnit.SECONDS.sleep(10);

        driver.manage().window().maximize();
        js.executeScript("window.scroll(0, document.documentElement.offsetHeight);");

        start = System.currentTimeMillis();
        driver.findElement(By.id("register-frm:register-cbtn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:register", end - start));

        TimeUnit.SECONDS.sleep(10);

        driver.findElement(By.id("register-frm:first-name-itxt")).click();
        driver.findElement(By.id("register-frm:first-name-itxt")).sendKeys("Stressor");
        driver.findElement(By.id("register-frm:last-name-itxt")).click();
        driver.findElement(By.id("register-frm:last-name-itxt")).sendKeys("Stressmensch");
        driver.findElement(By.id("register-frm:email-itxt")).click();
        driver.findElement(By.id("register-frm:email-itxt")).sendKeys(globalId +
                "Stress.Stress@sebastianvogt.me");
        driver.findElement(By.id("register-frm:password-iscrt")).sendKeys("Password1!");

        start = System.currentTimeMillis();
        driver.findElement(By.id("register-frm:register-cbtn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:register", end - start));

        TimeUnit.SECONDS.sleep(10);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(9));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(text(), 'verification.xhtml')]")));
        String url = driver.findElement(By.xpath("//li[contains(text(), 'verification.xhtml')]")).getText();

        start = System.currentTimeMillis();
        driver.get(url);
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("callPage", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("go-to-homepage-lnk")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:homepage", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("nav-forumlist-link")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:forumList", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("nav-profile-link")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:profile", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("global-search-frm:search-btn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:search", end - start));

        TimeUnit.SECONDS.sleep(10);

        start = System.currentTimeMillis();
        driver.findElement(By.id("nav-profile-link")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("goto:profile", end - start));

        TimeUnit.SECONDS.sleep(10);

        js.executeScript("window.scroll(0, document.documentElement.offsetHeight);");
        driver.findElement(By.id("edit-profile-form:employer-itxt")).click();
        driver.findElement(By.id("edit-profile-form:employer-itxt")).sendKeys("Stressiger Job");
        driver.findElement(By.id("edit-profile-form:password-iscrt")).click();
        driver.findElement(By.id("edit-profile-form:password-iscrt")).sendKeys("Password1!");
        js.executeScript("window.scroll(0, document.documentElement.offsetHeight);");

        start = System.currentTimeMillis();
        driver.findElement(By.id("edit-profile-form:save-cbtn")).click();
        end = System.currentTimeMillis();
        responseTimes.add(new ResponseTimeEntry("action:profileSave", end - start));

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
