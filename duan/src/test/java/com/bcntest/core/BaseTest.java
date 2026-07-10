package com.bcntest.core;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

    protected static WebDriver driver;
    protected static WebDriverWait wait;

    protected static final String BASE_URL =
            "https://cntttest.vanlanguni.edu.vn:18081/Phancong02/";

    protected static final String PROFILE_DIR =
            "C:\\selenium-profile\\bcn-phancong-vlu";
    protected static final String PROFILE_NAME = 
            "Default"; 

    protected static final String DOWNLOAD_DIR =
            "C:\\selenium-downloads\\bcn-phancong-vlu";

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() throws InterruptedException {
        new File(DOWNLOAD_DIR).mkdirs();
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        options.addArguments("--user-data-dir=" + PROFILE_DIR);
        options.addArguments("--profile-directory=" + PROFILE_NAME);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", DOWNLOAD_DIR);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get(BASE_URL);
        Thread.sleep(3000);
        switchToLastWindow();

        if (isLoggedIn()) {
            System.out.println("Đã có session đăng nhập, không cần xác minh lại.");
            return;
        }

        System.out.println("Chưa đăng nhập. Selenium sẽ bấm nút Đăng nhập nếu có.");
        clickLoginButtonIfExists();
        Thread.sleep(5000);
        switchToLastWindow();

        System.out.println("Nếu hệ thống yêu cầu xác minh, hãy xác minh thủ công trên Chrome.");
        System.out.println("Selenium sẽ tự chờ tối đa 180 giây.");

        boolean loginSuccess = waitUntilLoggedIn(180);
        Assert.assertTrue(loginSuccess, "Đăng nhập chưa thành công sau 180 giây");

        driver.get(BASE_URL);
        Thread.sleep(3000);
        Assert.assertTrue(isLoggedIn(), "Đăng nhập chưa thành công hoặc bị đá về trang Login");
        System.out.println("LOGIN PASS - Đã đăng nhập Ban chủ nhiệm thành công.");
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static boolean waitUntilLoggedIn(int timeoutSeconds) throws InterruptedException {
        int count = 0;
        while (count < timeoutSeconds) {
            try {
                switchToLastWindow();
                if (isLoggedIn()) return true;
                Thread.sleep(1000);
                count++;
            } catch (Exception e) {
                Thread.sleep(1000);
                count++;
            }
        }
        return false;
    }

    public static boolean isLoggedIn() {
        try {
            String url = driver.getCurrentUrl();
            String page = driver.getPageSource();

            return !url.contains("/Account/Login")
                    && (page.contains("Ban chủ nhiệm")
                            || page.contains("Ban chu nhiem")
                            || page.contains("Học kỳ và ngành")
                            || page.contains("Người dùng")
                            || page.contains("Trang chủ")
                            || page.contains("Đăng xuất"));
        } catch (Exception e) {
            return false;
        }
    }

    public static void clickLoginButtonIfExists() {
        try {
            List<WebElement> loginButtons = driver.findElements(
                    By.xpath("//button[contains(normalize-space(),'Đăng nhập')]"
                            + " | //a[contains(normalize-space(),'Đăng nhập')]"
                            + " | //input[@type='submit' and contains(@value,'Đăng nhập')]")
            );
            if (!loginButtons.isEmpty()) {
                clickElement(loginButtons.get(0));
                System.out.println("Đã bấm nút Đăng nhập.");
            }
        } catch (Exception e) {
            System.out.println("Không bấm được nút Đăng nhập: " + e.getMessage());
        }
    }

    public static void clickElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
            Thread.sleep(500);
            element.click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
        }
    }

    public static void switchToLastWindow() {
        Set<String> windows = driver.getWindowHandles();
        if (windows.isEmpty()) throw new NoSuchWindowException("Không còn cửa sổ Chrome nào.");
        String lastWindow = null;
        for (String window : windows) {
            lastWindow = window;
        }
        driver.switchTo().window(lastWindow);
    }
}