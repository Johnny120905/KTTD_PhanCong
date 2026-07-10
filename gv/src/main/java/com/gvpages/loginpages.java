package com.gvpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class loginpages {

    WebDriver driver;

    public loginpages(WebDriver driver) {   // <-- sửa ở đây
        this.driver = driver;
    }

    By btnDangNhap = By.xpath("//button[contains(text(),'Đăng nhập')]");
    By account = By.xpath("//span[contains(text(),'@vanlanguni.vn')]");
    By btnDangXuat = By.xpath("//a[contains(text(),'Đăng xuất')]");

    public void openWebsite() {
        driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");
    }

    public void login() throws InterruptedException {

        driver.findElement(btnDangNhap).click();

        Thread.sleep(8000);

        driver.findElement(account).click();
    }

    public void logout() throws InterruptedException {

        Thread.sleep(2000);

        driver.findElement(btnDangXuat).click();
    }

    public boolean isLogoutSuccess() {

        return driver.getCurrentUrl().contains("Login")
                || driver.getPageSource().contains("Đăng nhập");
    }
}
