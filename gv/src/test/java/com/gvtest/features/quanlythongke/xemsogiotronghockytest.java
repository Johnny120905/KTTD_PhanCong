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

public class xemsogiotronghockytest {

    WebDriver driver;

    loginpages login;

    quanlythongkepages thongke;

    @BeforeClass
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
    public void TK01_XemSoGioHocKy() throws Exception {

        // Login

        login.openWebsite();

        login.login();

        Assert.assertTrue(login.isLoginSuccess());

        // Mở Thống kê

        thongke.moThongKe();

        // Mở Số giờ cá nhân

        thongke.moSoGioCaNhan();

        // Chọn Thống kê theo Học kỳ

        thongke.chonThongKeTheoHocKy();

        // Chọn Học kỳ 999

        thongke.chonHocKy999();

        Assert.assertTrue(thongke.isHocKy999(),
                "Không chuyển sang học kỳ 999.");

    }
    @Test
    public void TK02_KhongChonHocKy() throws Exception {
    
        thongke.moThongKe();
    
        thongke.moSoGioCaNhan();
    
        thongke.chonThongKeTheoHocKy();
    
        // Không chọn học kỳ
    
        Assert.assertTrue(driver.getCurrentUrl().contains("Statistics"));
    
    }
    @Test
public void TK03_HocKyKhongCoDuLieu() throws Exception {

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