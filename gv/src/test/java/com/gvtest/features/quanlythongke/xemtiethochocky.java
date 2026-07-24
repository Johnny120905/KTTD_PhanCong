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

public class xemtiethochocky {

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

        // Login duy nhất một lần
        login.openWebsite();
        login.login();

        Assert.assertTrue(login.isLoginSuccess());

    }

    // ==========================================
    // LUỒNG ĐÚNG
    // ==========================================

    @Test(priority = 1)
    public void TK03_LuongDung() throws Exception {

        thongke.moThongKe();

        thongke.moSoGioCaNhan();

        thongke.chonThongKeTheoHocKy();

        thongke.chonHocKy999();

        Assert.assertTrue(thongke.isHocKy999());

    }

    // ==========================================
    // LUỒNG SAI
    // ==========================================

    @Test(priority = 2)
    public void TK03_LuongSai() throws Exception {

        thongke.moThongKe();

        thongke.moSoGioCaNhan();

        thongke.chonThongKeTheoHocKy();

        // Không chọn học kỳ

        Assert.assertTrue(driver.findElement(
                org.openqa.selenium.By.id("select2-term-container"))
                .getText()
                .contains("Học kỳ"));

    }

    // ==========================================
    // LUỒNG DATA
    // ==========================================

    @Test(priority = 3)
    public void TK03_LuongData() throws Exception {

        thongke.moThongKe();

        thongke.moSoGioCaNhan();

        thongke.chonThongKeTheoHocKy();

        thongke.chonHocKy998();

        Assert.assertTrue(thongke.isNoData());

    }

    @AfterClass
    public void tearDown() {

        if (driver != null)

            driver.quit();

    }

}