package de.lases.selenium.stress;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

public class UserRegistersClicksRandomlyAndDeletesHimself {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    @BeforeEach
    public void setUp() {
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }
    @AfterEach
    public void tearDown() {
        driver.quit();
    }
    public String waitForWindow(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Set<String> whNow = driver.getWindowHandles();
        Set<String> whThen = (Set<String>) vars.get("window_handles");
        if (whNow.size() > whThen.size()) {
            whNow.removeAll(whThen);
        }
        return whNow.iterator().next();
    }
    @Test
    public void userRegistersClicksRandomlyAndDeletesHimself() {
        driver.get("http://localhost:8082/lases_1_0_SNAPSHOT_war/");
        driver.manage().window().maximize();

        driver.findElement(By.id("j_idt33:register-cbtn")).click();
        driver.findElement(By.id("register-frm:first-name-itxt")).click();
        driver.findElement(By.id("register-frm:first-name-itxt")).sendKeys("Stressor");
        driver.findElement(By.id("register-frm:last-name-itxt")).click();
        driver.findElement(By.id("register-frm:last-name-itxt")).sendKeys("Stressmensch");
        driver.findElement(By.id("register-frm:email-itxt")).click();
        driver.findElement(By.id("register-frm:email-itxt")).sendKeys("Stress.Stress@sebastianvogt.me");
        driver.findElement(By.id("register-frm:password-iscrt")).sendKeys("Password1!");
        driver.findElement(By.id("register-frm:register-cbtn")).click();
        vars.put("window_handles", driver.getWindowHandles());
        String url = driver.findElement(By.cssSelector(".alert-info:nth-child(4)")).getText();
        driver.get(url);
        driver.findElement(By.id("go-to-homepage-lnk")).click();
        driver.findElement(By.id("nav-forumlist-link")).click();
        driver.findElement(By.id("nav-profile-link")).click();
        driver.findElement(By.id("global-search-frm:search-btn")).click();
        driver.findElement(By.id("nav-profile-link")).click();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scroll(0, document.documentElement.offsetHeight);");
        driver.findElement(By.id("edit-profile-form:employer-itxt")).click();
        driver.findElement(By.id("edit-profile-form:employer-itxt")).sendKeys("Stressiger Job");
        driver.findElement(By.id("edit-profile-form:password-iscrt")).click();
        driver.findElement(By.id("edit-profile-form:password-iscrt")).sendKeys("Password1!");
        js.executeScript("window.scroll(0, document.documentElement.offsetHeight);");
        driver.findElement(By.id("edit-profile-form:save-cbtn")).click();
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
    }
}
