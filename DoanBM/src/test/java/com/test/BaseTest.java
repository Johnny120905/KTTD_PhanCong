package com.test;

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
            "https://cntttest.vanlanguni.edu.vn:18081/Phancong02";

    protected static final String LOGIN_URL =
            BASE_URL + "/Account/Login";

    protected static final String ASSIGN_URL =
            BASE_URL + "/Timetable/Assign";

    protected static final String PROFILE_DIR =
            "C:\\selenium-profile\\phancong-vlu";

    protected static final String DOWNLOAD_DIR =
            "C:\\selenium-downloads\\phancong-vlu";

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

        // Dùng chung Chrome profile để lưu session/cookie đăng nhập
        options.addArguments("--user-data-dir=" + PROFILE_DIR);
        options.addArguments("--profile-directory=Default");

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

        driver.get(ASSIGN_URL);

        Thread.sleep(3000);

        switchToLastWindow();

        if (isLoggedIn()) {
            System.out.println("Da co session dang nhap, khong can xac minh lai.");
            return;
        }

        System.out.println("Chua dang nhap. Selenium se bam nut Dang nhap neu co.");

        clickLoginButtonIfExists();

        Thread.sleep(5000);

        switchToLastWindow();

        System.out.println("Neu he thong yeu cau xac minh, hay xac minh thu cong tren Chrome.");
        System.out.println("Selenium se tu cho toi da 180 giay.");
        System.out.println("Sau khi ban xac minh xong va vao duoc he thong, Selenium se tu chay tiep.");

        boolean loginSuccess = waitUntilLoggedIn(180);

        Assert.assertTrue(
                loginSuccess,
                "Dang nhap chua thanh cong sau 180 giay hoac chua vao dung role Bo mon"
        );

        driver.get(ASSIGN_URL);

        Thread.sleep(3000);

        Assert.assertTrue(
                isLoggedIn(),
                "Dang nhap chua thanh cong hoac bi da ve trang Login"
        );

        System.out.println("LOGIN PASS - Da dang nhap Bo mon thanh cong.");
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

                if (isLoggedIn()) {
                    return true;
                }

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
                    && (
                    page.contains("Bộ môn")
                            || page.contains("Bo mon")
                            || page.contains("Trang chủ")
                            || page.contains("Trang chu")
                            || page.contains("Thời khóa biểu")
                            || page.contains("Thời khoá biểu")
                            || page.contains("Thoi khoa bieu")
                            || page.contains("Phân công")
                            || page.contains("Phan cong")
                            || page.contains("Đăng xuất")
                            || page.contains("Dang xuat")
            );

        } catch (Exception e) {
            return false;
        }
    }

    public static void clickLoginButtonIfExists() {
        try {
            List<WebElement> loginButtons = driver.findElements(
                    By.xpath(
                            "//button[contains(normalize-space(),'Đăng nhập')]"
                                    + " | //a[contains(normalize-space(),'Đăng nhập')]"
                                    + " | //input[@type='submit' and contains(@value,'Đăng nhập')]"
                                    + " | //button[contains(normalize-space(),'Dang nhap')]"
                                    + " | //a[contains(normalize-space(),'Dang nhap')]"
                    )
            );

            if (!loginButtons.isEmpty()) {
                clickElement(loginButtons.get(0));
                System.out.println("Da bam nut Dang nhap.");
            } else {
                System.out.println("Khong tim thay nut Dang nhap.");
            }

        } catch (Exception e) {
            System.out.println("Khong bam duoc nut Dang nhap: " + e.getMessage());
        }
    }

    public static void navigateToAssignPage() throws InterruptedException {
        driver.get(ASSIGN_URL);
        Thread.sleep(3000);
        switchToLastWindow();

        Assert.assertTrue(
                isLoggedIn(),
                "Chua dang nhap hoac bi da ve trang Login"
        );
    }

    public static void clickElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    element
            );

            Thread.sleep(500);

            element.click();

        } catch (ElementClickInterceptedException e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);

        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
        }
    }

    public static WebElement waitClickable(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public static WebElement waitVisible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void switchToLastWindow() {
        Set<String> windows = driver.getWindowHandles();

        if (windows.isEmpty()) {
            throw new NoSuchWindowException("Khong con cua so Chrome nao.");
        }

        String lastWindow = null;

        for (String window : windows) {
            lastWindow = window;
        }

        driver.switchTo().window(lastWindow);
    }
}