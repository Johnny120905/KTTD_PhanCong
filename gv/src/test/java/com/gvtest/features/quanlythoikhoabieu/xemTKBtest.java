package com.gvtest.features.quanlythoikhoabieu;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.gvpages.loginpages;
import com.gvpages.quanlyTKBpages;

import io.github.bonigarcia.wdm.WebDriverManager;

public class xemTKBtest {

    WebDriver driver;

    loginpages login;

    quanlyTKBpages tkb;

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

        tkb = new quanlyTKBpages(driver);

    }

    @Test

    public void TKB01_XemThoiKhoaBieu() throws Exception {

        // Login

        login.openWebsite();

        login.login();

        Assert.assertTrue(login.isLoginSuccess());

        // Mở TKB

        tkb.moTKB();

        // Chọn học kỳ 998

        tkb.chonHocKy998();

        Assert.assertTrue(tkb.isNoData(),
                "Học kỳ 998 phải hiển thị chưa có dữ liệu.");

        // Chọn lại học kỳ 999

        tkb.chonHocKy999();

        Assert.assertTrue(tkb.isHocKy999(),
                "Không quay lại học kỳ 999.");

        // Chọn tuần 9

        tkb.chonTuan9();

        Assert.assertTrue(tkb.isWeek9(),
                "Không chuyển sang tuần 9.");

    }

    @AfterMethod

    public void tearDown() {

        if (driver != null)

            driver.quit();

    }

}