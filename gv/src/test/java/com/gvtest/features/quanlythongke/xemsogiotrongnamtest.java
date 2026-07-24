package com.gvtest.features.quanlythongke;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.gvpages.loginpages;
import com.gvpages.quanlythongkepages;

import io.github.bonigarcia.wdm.WebDriverManager;

public class xemsogiotrongnamtest {

    WebDriver driver;

    loginpages login;

    quanlythongkepages thongke;

    @BeforeMethod
    public void setUp() {

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

        thongke = new quanlythongkepages(driver);

    }

    @Test
    public void TK02_XemSoGioCaNhanTrongNamHoc() throws Exception {

        // Login

        login.openWebsite();

        login.login();

        Assert.assertTrue(login.isLoginSuccess());

        // Mở Thống kê

        thongke.moThongKe();

        // Mở Số giờ cá nhân

        thongke.moSoGioCaNhan();

        // Chọn Thống kê theo Năm học

        thongke.chonThongKeTheoNam();

        // Chọn Năm học 2025 - 2026

        thongke.chonNamHoc20252026();

        Assert.assertTrue(thongke.isNamHoc20252026(),
                "Không chuyển sang năm học 2025 - 2026.");

    }

    @AfterMethod
    public void tearDown() {

        if (driver != null)

            driver.quit();

    }

}