package com.bcnpages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class QuanLyNganhHocPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- 1. LOCATORS: XEM & TÌM KIẾM ---
    private By txtTimKiem = By.cssSelector("input[type='search']"); 
    private By danhSachNganhHoc = By.xpath("//table/tbody/tr"); 
    private By msgKhongCoDuLieu = By.className("dataTables_empty"); 

    // --- 2. LOCATORS: THÊM NGÀNH MỚI ---
    private By btnThemHocKyMoi = By.cssSelector("button.btn-primary");
    private By txtHocKy = By.id("id"); 
    
    private By txtNgayBatDau = By.id("start_date");
    private By txtMaxLesson = By.id("max_lesson");
    private By txtMaxClass = By.id("max_class");
    private By txtMaNganh = By.id("id"); 
    private By txtTenNganh = By.id("name");
    private By txtVietTat = By.id("abbreviation");
    
    private By btnLuu = By.xpath("//button[@type='submit']");
    private By btnHuy = By.id("btnClose"); 
    
    private By msgThongBao = By.cssSelector(".toast, .swal2-popup");
    private By msgLoiDinhDang = By.cssSelector("label.error, .invalid-feedback, .text-danger");

    public QuanLyNganhHocPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // =========================================
    // CÁC HÀM XEM & TÌM KIẾM
    // =========================================
    public void nhapTuKhoaTimKiem(String tuKhoa) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(txtTimKiem));
        searchBox.clear();
        searchBox.sendKeys(tuKhoa);
    }

    // ĐÃ SỬA TÊN HÀM: Thêm chữ "Hoc" vào cho khớp với file Test
    public int laySoLuongNganhHocHienThi() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(danhSachNganhHoc));
            List<WebElement> rows = driver.findElements(danhSachNganhHoc);
            
            for (WebElement row : rows) {
                if (row.getText().toLowerCase().contains("no data") || 
                    row.getText().toLowerCase().contains("không tìm thấy") ||
                    row.findElements(By.className("dataTables_empty")).size() > 0) {
                    return 0; 
                }
            }
            return rows.size();
        } catch (Exception e) { return 0; }
    }

    public String layThongBaoKhongCoDuLieu() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(msgKhongCoDuLieu)).getText();
        } catch (Exception e) { return ""; }
    }

    // =========================================
    // CÁC HÀM THÊM MỚI & HỦY
    // =========================================
    public void bamThemNganhMoi() {
        wait.until(ExpectedConditions.elementToBeClickable(btnThemHocKyMoi)).click();
    }

    public void chonSelect2(String selectId, String textToSelect) {
        By select2Container = By.xpath("//span[@aria-labelledby='select2-" + selectId + "-container']");
        wait.until(ExpectedConditions.elementToBeClickable(select2Container)).click();
        
        By optionXpath = By.xpath("//li[contains(@class, 'select2-results__option') and contains(text(),'" + textToSelect + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(optionXpath)).click();
    }

    public void nhapThongTinNganh(String maNganh, String tenNganh, String vietTat, String ctdt) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(txtMaNganh)).clear();
        driver.findElement(txtMaNganh).sendKeys(maNganh);
        
        driver.findElement(txtTenNganh).clear();
        driver.findElement(txtTenNganh).sendKeys(tenNganh);
        
        driver.findElement(txtVietTat).clear();
        driver.findElement(txtVietTat).sendKeys(vietTat);
        
        if (!ctdt.isEmpty()) {
            chonSelect2("program_type", ctdt);
        }
    }

    public void bamLuu() {
        wait.until(ExpectedConditions.elementToBeClickable(btnLuu)).click();
    }
    
    public void bamHuy() {
        wait.until(ExpectedConditions.elementToBeClickable(btnHuy)).click();
    }

    public String layThongBao() {
        StringBuilder thongBaoGop = new StringBuilder();
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement toast = shortWait.until(ExpectedConditions.visibilityOfElementLocated(msgThongBao));
            thongBaoGop.append(toast.getText()).append(" ");
        } catch (Exception e) {}
        
        try {
            List<WebElement> errors = driver.findElements(msgLoiDinhDang);
            for (WebElement err : errors) {
                if (err.isDisplayed()) thongBaoGop.append(err.getText()).append(" ");
            }
        } catch (Exception e) {}
        
        return thongBaoGop.toString().trim().toLowerCase();
    }
}