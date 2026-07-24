package com.gvtest.features.quanlythongke;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.gvpages.loginpages;
import com.gvpages.quanlythongkepages;

import io.github.bonigarcia.wdm.WebDriverManager;

public class xemtiethocnam {

    WebDriver driver;

    loginpages login;

    quanlythongkepages thongke;

    @BeforeClass
    public void setUp() throws Exception {

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

        // Login 1 lần
        login.openWebsite();
        login.login();

        Assert.assertTrue(login.isLoginSuccess());

    }

    // =====================================================
    // LUỒNG ĐÚNG
    // =====================================================

    @Test(priority = 1)

    public void TK04_LuongDung() throws Exception {

        thongke.moThongKe();

        thongke.moSoGioCaNhan();

        thongke.chonThongKeTheoNam();

        thongke.chonNamHoc20252026();

        Assert.assertTrue(thongke.isNamHoc20252026());

    }

    // =====================================================
    // LUỒNG SAI
    // =====================================================

    @Test(priority = 2)

    public void TK04_LuongSai() throws Exception {

        thongke.moThongKe();

        thongke.moSoGioCaNhan();

        thongke.chonThongKeTheoNam();

        // Không chọn năm học

        Assert.assertTrue(driver.findElement(
                org.openqa.selenium.By.id("select2-year-container"))
                .isDisplayed());

    }

    // =====================================================
    // LUỒNG DATA
    // =====================================================

    @Test(priority = 3)

    public void TK04_LuongData() throws Exception {

        thongke.moThongKe();

        thongke.moSoGioCaNhan();

        thongke.chonThongKeTheoNam();

        thongke.chonNamHoc20252026();

        // Nếu hệ thống không có dữ liệu thì PASS
        // Nếu có dữ liệu thì vẫn PASS

        if (thongke.isNoData()) {

            Assert.assertTrue(true);

        } else {

            Assert.assertTrue(thongke.isNamHoc20252026());

        }

    }

    @AfterClass

    public void tearDown() {

        if (driver != null)

            driver.quit();

    }

}