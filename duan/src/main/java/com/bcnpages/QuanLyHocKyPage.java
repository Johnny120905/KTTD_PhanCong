package com.bcnpages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class QuanLyHocKyPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By txtTimKiem = By.cssSelector("input[type='search']"); 
    private By danhSachHocKy = By.xpath("//table/tbody/tr"); 
    private By msgKhongCoDuLieu = By.className("dataTables_empty"); 

    private By btnThemHocKyMoi = By.cssSelector("button.btn-primary");
    private By txtHocKy = By.id("id"); 
    private By txtTuanBatDau = By.id("start_week");
    private By txtMaxLesson = By.id("max_lesson");
    private By txtMaxClass = By.id("max_class");
    
    private By btnLuu = By.xpath("//button[@type='submit']");
    private By btnHuy = By.xpath("//button[@type='button' and (contains(@class, 'btn-light') or contains(@class, 'btn-secondary'))]");
    
    private By msgThongBao = By.cssSelector(".toast, .swal2-popup");
    private By msgLoiDinhDang = By.cssSelector("label.error, .invalid-feedback, .text-danger");

    public QuanLyHocKyPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void nhapTuKhoaTimKiem(String tuKhoa) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(txtTimKiem));
        searchBox.clear();
        searchBox.sendKeys(tuKhoa);
    }

    public int laySoLuongHocKyHienThi() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(danhSachHocKy));
            List<WebElement> rows = driver.findElements(danhSachHocKy);
            for (WebElement row : rows) {
                if (row.getText().toLowerCase().contains("no data") || 
                    row.findElements(By.className("dataTables_empty")).size() > 0) return 0;
            }
            return rows.size();
        } catch (Exception e) { return 0; }
    }

    public String layThongBaoKhongCoDuLieu() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(msgKhongCoDuLieu)).getText();
        } catch (Exception e) { return ""; }
    }

    public void bamThemHocKyMoi() {
        wait.until(ExpectedConditions.elementToBeClickable(btnThemHocKyMoi)).click();
    }

    public void nhapThongTinHocKy(String hocKy, String namBD, String namKT, String tuanBD, String ngayBD, String tietToiDa, String lopToiDa) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(txtHocKy)).clear();
        driver.findElement(txtHocKy).sendKeys(hocKy);
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // 1. Ép Chọn Select2 bằng jQuery (Bỏ qua thao tác click dễ lỗi)
        js.executeScript("$('#start_year').val('" + namBD + "').trigger('change');");
        js.executeScript("$('#end_year').val('" + namKT + "').trigger('change');");
        
        // 2. Nhập Tuần
        driver.findElement(txtTuanBatDau).clear();
        driver.findElement(txtTuanBatDau).sendKeys(tuanBD);
        
        // 3. TUYỆT CHIÊU: Bơm dữ liệu ngày tháng thẳng vào thẻ ẨN
        js.executeScript("document.getElementById('start_date').value='" + ngayBD + "';");
        // Bơm thêm vào thẻ hiển thị để mắt thường thấy được cho an tâm
        js.executeScript("var els = document.querySelectorAll('input.flatpickr-input[type=\"text\"]'); if(els.length > 0) els[0].value='" + ngayBD + "';");

        // 4. Nhập Tiết và Lớp
        driver.findElement(txtMaxLesson).clear();
        driver.findElement(txtMaxLesson).sendKeys(tietToiDa);
        driver.findElement(txtMaxClass).clear();
        driver.findElement(txtMaxClass).sendKeys(lopToiDa);
    }

    public void bamLuu() {
        wait.until(ExpectedConditions.elementToBeClickable(btnLuu)).click();
    }
    
    public void bamHuy() {
        driver.findElement(btnHuy).click();
    }

    public String layThongBao() {
        StringBuilder thongBaoGop = new StringBuilder();
        try {
            // Tăng thời gian chờ lên 5 giây (nhiều khi web trường load lưu dữ liệu chậm)
            WebDriverWait waitThongBao = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement toast = waitThongBao.until(ExpectedConditions.visibilityOfElementLocated(msgThongBao));
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