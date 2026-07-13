package com.gvpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class loginpages {

    WebDriver driver;

    public loginpages(WebDriver driver) {
        this.driver = driver;
    }

    // ==========================
    // Locator
    // ==========================

    By btnDangNhap = By.xpath("//button[contains(text(),'Đăng nhập')]");
    By account = By.xpath("//span[contains(text(),'@vanlanguni.vn')]");

    // ==========================
    // Mở website
    // ==========================

    public void openWebsite() {

        driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");

    }

    // ==========================
    // Login thành công
    // ==========================

    public void login() throws InterruptedException {

        driver.findElement(btnDangNhap).click();

        Thread.sleep(8000);

        driver.findElement(account).click();

        Thread.sleep(5000);

    }

    // ==========================
    // Login thất bại
    // ==========================

// ==========================
// Truy cập trực tiếp Timetable
// ==========================

public void accessTimetableDirectly() {

    driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/Timetable");

}
    // ==========================
    // Kiểm tra Login thành công
    // ==========================

public boolean isLoginSuccess() {

    return driver.getCurrentUrl().contains("/Timetable");

}

// ==========================
// Kiểm tra bị chuyển về Login
// ==========================

public boolean isRedirectToLogin() {

    return driver.getCurrentUrl().contains("Login")
            || driver.getPageSource().contains("Đăng nhập");

}

}