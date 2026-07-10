package com.bcnpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class QuanLyNguoiDungPage {
    private WebDriver driver;

    private By btnThemNguoiDung = By.xpath("//button[@type='button' and contains(@class, 'btn-primary') and not(contains(@class, 'scroll-top'))]"); 
    
    private By txtMaGV = By.id("staff_id");
    private By txtTenGV = By.id("full_name");
    private By txtEmail = By.id("email");
    private By ddlLoai = By.name("type"); 
    private By ddlRole = By.id("role_id"); 
    
    private By btnLuu = By.xpath("//button[@type='submit']");
    private By btnHuy = By.id("btnClose");
    
    private By msgThongBaoToast = By.cssSelector(".toast, .swal2-popup"); 
    private By msgLoiDinhDang = By.cssSelector("label.error");

    public QuanLyNguoiDungPage(WebDriver driver) {
        this.driver = driver;
    }

    public void bamNutThemNguoiDung() {
        driver.findElement(btnThemNguoiDung).click();
    }

    public void nhapThongTinNguoiDung(String maGV, String tenGV, String email, String loai, String role) {
        driver.findElement(txtMaGV).clear();
        driver.findElement(txtMaGV).sendKeys(maGV);

        driver.findElement(txtTenGV).clear();
        driver.findElement(txtTenGV).sendKeys(tenGV);

        driver.findElement(txtEmail).clear();
        driver.findElement(txtEmail).sendKeys(email);

        if (!loai.isEmpty()) {
            Select selectLoai = new Select(driver.findElement(ddlLoai));
            selectLoai.selectByVisibleText(loai);
        }
        if (!role.isEmpty()) {
            Select selectRole = new Select(driver.findElement(ddlRole));
            selectRole.selectByVisibleText(role);
        }
    }

    public void bamLuu() {
        driver.findElement(btnLuu).click();
    }

    public void bamHuy() {
        driver.findElement(btnHuy).click();
    }

    public String layThongBao() {
        StringBuilder thongBaoGop = new StringBuilder();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            try {
                WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(msgThongBaoToast));
                thongBaoGop.append(toast.getText()).append(" ");
            } catch (Exception eToast) {
            }
            
            List<WebElement> errors = driver.findElements(msgLoiDinhDang);
            for (WebElement err : errors) {
                if (err.isDisplayed()) {
                    thongBaoGop.append(err.getText()).append(" ");
                }
            }
        } catch (Exception e) {
        }
        return thongBaoGop.toString();
    }
}