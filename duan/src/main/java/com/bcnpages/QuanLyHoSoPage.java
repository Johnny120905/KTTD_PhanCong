package com.bcnpages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class QuanLyHoSoPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By txtMaGV = By.id("staff_id");
    private By txtTenGV = By.id("full_name");
    private By btnCapNhat = By.cssSelector("button[type='submit']");
    private By dropdownUser = By.id("dropdown-user");
    private By linkHoSo = By.linkText("Hồ sơ");
    // Giả định toast thông báo thành công hoặc lỗi có class chung
    private By msgThongBao = By.cssSelector("#toast-container, .swal2-html-container");

    public QuanLyHoSoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void truyCapHoSo() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(dropdownUser)).click();
        Thread.sleep(800);
        wait.until(ExpectedConditions.elementToBeClickable(linkHoSo)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(txtMaGV));
    }

    public void capNhatHoSo(String maGV, String tenGV) {
        WebElement elMa = wait.until(ExpectedConditions.visibilityOfElementLocated(txtMaGV));
        elMa.clear();
        elMa.sendKeys(maGV);
        
        WebElement elTen = driver.findElement(txtTenGV);
        elTen.clear();
        elTen.sendKeys(tenGV);
        
        driver.findElement(btnCapNhat).click();
    }

    public String layThongBaoKetQua() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(msgThongBao)).getText().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    public String layGiaTriMaGV() { return driver.findElement(txtMaGV).getAttribute("value"); }
    public String layGiaTriTenGV() { return driver.findElement(txtTenGV).getAttribute("value"); }
}