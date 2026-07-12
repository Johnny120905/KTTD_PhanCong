package com.gvtest.features.quanlydangnhap_xuat;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.gvpages.loginpages;

import io.github.bonigarcia.wdm.WebDriverManager;

public class dangnhap {

    WebDriver driver;
    loginpages login;

    @BeforeClass
    public void setUp() {

        System.out.println("\n===============================================");
        System.out.println("KHỞI TẠO MÔI TRƯỜNG KIỂM THỬ ĐĂNG NHẬP");
        System.out.println("===============================================\n");

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--user-data-dir=C:/New folder/testlogin_logout/testlogin_logout/selenium_profile");
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);

        options.setExperimentalOption("excludeSwitches",
                new String[] { "enable-automation" });

        options.setExperimentalOption("useAutomationExtension", false);

        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new ChromeDriver(options);

        ((JavascriptExecutor) driver).executeScript(
                "Object.defineProperty(navigator,'webdriver',{get:()=>undefined});");

        driver.manage().window().maximize();

        login = new loginpages(driver);

    }

    // ==========================
    // LG01 - Login thành công
    // ==========================

    @Test(priority = 1)
    public void LG01_LoginSuccess() throws Exception {

        System.out.println("LG01 - ĐĂNG NHẬP THÀNH CÔNG");

        login.openWebsite();

        login.login();

        Assert.assertTrue(
                login.isLoginSuccess(),
                "Đăng nhập thất bại.");

        System.out.println("KẾT QUẢ: PASS\n");

    }

    // ==========================
    // LG02 - Truy cập Timetable
    // ==========================

    @Test(priority = 2, dependsOnMethods = "LG01_LoginSuccess")
    public void LG02_OpenTimetable() {

        System.out.println("LG02 - TRUY CẬP TRANG THỜI KHÓA BIỂU");

        login.accessTimetableDirectly();

        Assert.assertTrue(
                login.isLoginSuccess(),
                "Không thể truy cập Timetable sau khi đăng nhập.");

        System.out.println("KẾT QUẢ: PASS\n");

    }

    // ==========================
    // LG03 - Kiểm tra Session
    // ==========================

    @Test(priority = 3, dependsOnMethods = "LG02_OpenTimetable")
    public void LG03_RefreshSession() {

        System.out.println("LG03 - KIỂM TRA SESSION SAU KHI REFRESH");

        driver.navigate().refresh();

        Assert.assertTrue(
                login.isLoginSuccess(),
                "Session đăng nhập không được giữ sau khi Refresh.");

        System.out.println("KẾT QUẢ: PASS\n");

    }

    @AfterClass
    public void tearDown() {

        if (driver != null) {

            driver.quit();

        }

        System.out.println("HOÀN THÀNH KIỂM THỬ CHỨC NĂNG ĐĂNG NHẬP");

    }

}