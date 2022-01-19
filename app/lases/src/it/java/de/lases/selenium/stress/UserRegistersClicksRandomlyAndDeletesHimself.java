package de.lases.selenium.stress;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public class UserRegistersClicksRandomlyAndDeletesHimself implements Callable<List<Long>> {

    private WebDriver driver;
    private JavascriptExecutor js;

    public UserRegistersClicksRandomlyAndDeletesHimself() {
//        FirefoxBinary firefoxBinary = new FirefoxBinary();
        FirefoxOptions options = new FirefoxOptions();
//        options.setBinary(firefoxBinary);
//        options.setHeadless(true);
        driver = new FirefoxDriver(options);
        js = (JavascriptExecutor) driver;
    }

    @Override
    public List<Long> call() {
        return userRegistersClicksRandomlyAndDeletesHimself(hashCode());
    }

    public void tearDown() {
        driver.quit();
    }

    public List<Long> userRegistersClicksRandomlyAndDeletesHimself(int threadIdentifier) {
        List<Long> responseTimes = new LinkedList<>();

        long start = System.currentTimeMillis();

        driver.get("http://localhost:8082/lases_1_0_SNAPSHOT_war/");
        driver.manage().window().maximize();
        js.executeScript("window.scroll(0, document.documentElement.offsetHeight);");
        driver.findElement(By.id("register-frm:register-cbtn")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

        driver.findElement(By.id("register-frm:first-name-itxt")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

        driver.findElement(By.id("register-frm:first-name-itxt")).sendKeys("Stressor");

        driver.findElement(By.id("register-frm:last-name-itxt")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

        driver.findElement(By.id("register-frm:last-name-itxt")).sendKeys("Stressmensch");
        driver.findElement(By.id("register-frm:email-itxt")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

        driver.findElement(By.id("register-frm:email-itxt")).sendKeys(threadIdentifier +
                "Stress.Stress@sebastianvogt.me");
        driver.findElement(By.id("register-frm:password-iscrt")).sendKeys("Password1!");
        driver.findElement(By.id("register-frm:register-cbtn")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(9));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-info:nth-child(4)")));
        responseTimes.add(start - (start = System.currentTimeMillis()));

        String url = driver.findElement(By.cssSelector(".alert-info:nth-child(4)")).getText();
        driver.get(url);
        driver.findElement(By.id("go-to-homepage-lnk")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

        driver.findElement(By.id("nav-forumlist-link")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

        driver.findElement(By.id("nav-profile-link")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

        driver.findElement(By.id("global-search-frm:search-btn")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

        driver.findElement(By.id("nav-profile-link")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

        js.executeScript("window.scroll(0, document.documentElement.offsetHeight);");
        driver.findElement(By.id("edit-profile-form:employer-itxt")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

        driver.findElement(By.id("edit-profile-form:employer-itxt")).sendKeys("Stressiger Job");
        driver.findElement(By.id("edit-profile-form:password-iscrt")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

        driver.findElement(By.id("edit-profile-form:password-iscrt")).sendKeys("Password1!");
        js.executeScript("window.scroll(0, document.documentElement.offsetHeight);");
        driver.findElement(By.id("edit-profile-form:save-cbtn")).click();
        responseTimes.add(start - (start = System.currentTimeMillis()));

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
        responseTimes.add(start - (start = System.currentTimeMillis()));

        return responseTimes;
    }
}
