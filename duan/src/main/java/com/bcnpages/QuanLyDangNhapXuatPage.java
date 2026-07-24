package com.bcnpages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class QuanLyDangNhapXuatPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public QuanLyDangNhapXuatPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // =========================================================================
    // LOCATORS
    // =========================================================================
    
    // Locators Đăng nhập (Dựa theo HTML: id="OpenIdConnect")
    private By btnDangNhapSSO = By.id("OpenIdConnect");
    private By inputTaiKhoan = By.id("username"); // Cấu hình giả định nếu có form nội bộ
    private By inputMatKhau = By.id("password");  // Cấu hình giả định nếu có form nội bộ
    private By txtLoiDangNhap = By.cssSelector(".text-danger, .validation-summary-errors");

    // Locators Layout / Dashboard (Sau khi đăng nhập)
    private By menuTrangChu = By.xpath("//ul[@id='main-menu-navigation']//span[text()='Trang chủ']");

    // Locators Đăng xuất (Dựa theo HTML: dropdown-user, logoutForm)
    private By btnAvatarUser = By.id("dropdown-user");
    private By btnDangXuat = By.xpath("//a[contains(@href, 'logoutForm')]"); 
    private By formDangXuat = By.id("logoutForm");

    // =========================================================================
    // HÀM TƯƠNG TÁC: ĐĂNG NHẬP
    // =========================================================================

    public void nhapThongTinDangNhap(String tk, String mk) {
        try {
            // Khối này dùng để test giới hạn biên / luồng data theo yêu cầu
            if(driver.findElements(inputTaiKhoan).size() > 0) {
                WebElement oTK = wait.until(ExpectedConditions.visibilityOfElementLocated(inputTaiKhoan));
                oTK.clear();
                oTK.sendKeys(tk);
                
                WebElement oMK = driver.findElement(inputMatKhau);
                oMK.clear();
                oMK.sendKeys(mk);
            }
        } catch (Exception e) {}
    }

    public void clickDangNhap() {
        try {
            System.out.println("  [Nguoi dung]: Bam nut Đăng nhập (OpenIdConnect)");
            WebElement btnLogin = wait.until(ExpectedConditions.elementToBeClickable(btnDangNhapSSO));
            btnLogin.click();
            Thread.sleep(2000); 
        } catch (Exception e) {
            System.out.println("  [Loi]: Khong tim thay nut dang nhap.");
        }
    }

    public boolean kiemTraLoiDangNhap() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(txtLoiDangNhap)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean kiemTraDangNhapThanhCong() {
        System.out.println("  [He thong]: Kiem tra thanh menu Trang chu xua hien...");
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(menuTrangChu)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // =========================================================================
    // HÀM TƯƠNG TÁC: ĐĂNG XUẤT
    // =========================================================================

    public void clickMenuAvatar() {
        try {
            System.out.println("  [Nguoi dung]: Bam vao Avatar goc phai tren cung");
            WebElement avatar = wait.until(ExpectedConditions.elementToBeClickable(btnAvatarUser));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", avatar);
            Thread.sleep(500);
            avatar.click();
            Thread.sleep(1000); // Chờ dropdown xổ xuống
        } catch (Exception e) {
            System.out.println("  [Loi]: Khong the click Avatar.");
        }
    }

    public void clickDangXuat() {
        try {
            System.out.println("  [Nguoi dung]: Bam nut Đăng xuất trong menu dropdown");
            WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(btnDangXuat));
            logoutBtn.click();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("  [Loi]: Khong the click Dang xuat.");
        }
    }

    // =========================================================================
    // CÁC HÀM UI (GIAO DIỆN)
    // =========================================================================
    
    public void cuonTrangDocNhanh() {
        try { 
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);"); 
            Thread.sleep(500); 
        } catch (Exception e) {}
    }

    public void cuonTrangLenTopNhanh() {
        try { 
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);"); 
            Thread.sleep(500); 
        } catch (Exception e) {}
    }
}