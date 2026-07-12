package com.bcnpages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select; 
import java.time.Duration;
import java.util.List;

public class QuanLyHocKyPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- LOCATORS CŨ ---
    private By txtTimKiem = By.cssSelector("input[type='search']"); 
    private By danhSachHocKy = By.xpath("//table/tbody/tr"); 
    private By msgKhongCoDuLieu = By.className("dataTables_empty"); 

    private By btnThemHocKyMoi = By.cssSelector("button.btn-primary.createNew");
    private By btnSuaDongDauTien = By.cssSelector("table tbody tr:first-child a.editRow");
    private By btnXoaDongDauTien = By.cssSelector("table tbody tr:first-child a.deleteRow");
    private By btnSwalXacNhan = By.cssSelector("button.swal2-confirm");
    private By btnSwalHuy = By.cssSelector("button.swal2-cancel");
    private By txtSwalTitle = By.id("swal2-title");
    private By txtSwalContent = By.id("swal2-html-container");
    
    private By txtHocKy = By.id("id"); 
    private By txtTuanBatDau = By.id("start_week");
    private By txtMaxLesson = By.id("max_lesson");
    private By txtMaxClass = By.id("max_class");
    private By btnLuu = By.xpath("//button[@type='submit' and contains(text(), 'Lưu')]");
    private By btnHuy = By.id("btnClose"); 
    private By btnTangTuan = By.xpath("//input[@id='start_week']/parent::div//button[contains(@class, 'bootstrap-touchspin-up')]");
    private By btnGiamTuan = By.xpath("//input[@id='start_week']/parent::div//button[contains(@class, 'bootstrap-touchspin-down')]");
    private By ddlNamBatDauContainer = By.id("select2-start_year-container");
    private By msgThongBao = By.cssSelector(".toast, .swal2-popup:not(.swal2-icon-warning)"); 
    private By msgLoiDinhDang = By.cssSelector("label.error, .invalid-feedback, .text-danger");
    private By ddlHienThiSoLuong = By.name("tblTerm_length"); 

    // --- LOCATORS PHÂN TRANG ---
    private By txtThongTinPhanTrang = By.id("tblTerm_info"); 
    private By btnTrangTiepTheo = By.xpath("//li[@id='tblTerm_next']/a");
    private By btnTrangTruoc = By.xpath("//li[@id='tblTerm_previous']/a");
    private By liTrangTruocContainer = By.id("tblTerm_previous"); 

    // --- LOCATOR MỚI: TOGGLE TRẠNG THÁI ---
    private By toggleTrangThaiDauTien = By.cssSelector("table tbody tr:first-child input.user-status");

    public QuanLyHocKyPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
    }

    public void donDepPopup() {
        ((JavascriptExecutor) driver).executeScript("if(document.querySelector('.swal2-container')) { document.querySelector('.swal2-container').remove(); }");
    }

    // --- TẤT CẢ CÁC HÀM CŨ & MỚI ---
    public int laySoLuongHocKyHienThi() {
        try { return driver.findElements(By.xpath("//table/tbody/tr")).size(); } catch (Exception e) { return 0; }
    }

    public void nhapTuKhoaTimKiem(String tuKhoa) {
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(txtTimKiem));
        searchBox.clear();
        searchBox.sendKeys(tuKhoa);
    }

    public void chonSoLuongHienThi(String giaTriValue) {
        WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(ddlHienThiSoLuong));
        Select select = new Select(selectElement);
        select.selectByValue(giaTriValue);
    }

    public String layMaHocKyDongDauTien() {
        try { return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody tr:first-child td:first-child"))).getText(); } catch (Exception e) { return ""; }
    }

    public String layThongTinPhanTrang() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(txtThongTinPhanTrang)).getText(); } catch (Exception e) { return ""; }
    }

    public void bamTrangTiepTheo() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnTrangTiepTheo));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void bamTrangTruoc() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnTrangTruoc));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void bamChonTrangSo(String soTrang) {
        By locatorTrang = By.xpath("//ul[contains(@class, 'pagination')]//a[text()='" + soTrang + "']");
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(locatorTrang));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public boolean kiemTraNutTruocBiKhoa() {
        WebElement container = wait.until(ExpectedConditions.presenceOfElementLocated(liTrangTruocContainer));
        return container.getAttribute("class").contains("disabled");
    }

    public void bamSapXepCot(String tenCot) {
        By colLocator = By.xpath("//th[contains(@aria-label, '" + tenCot + "')]");
        WebElement col = wait.until(ExpectedConditions.elementToBeClickable(colLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", col);
    }
    
    public String layTrangThaiSapXepCot(String tenCot) {
        By colLocator = By.xpath("//th[contains(@aria-label, '" + tenCot + "')]");
        WebElement col = wait.until(ExpectedConditions.presenceOfElementLocated(colLocator));
        return col.getAttribute("class"); 
    }

    public boolean kiemTraCoHocKyMo() {
        List<WebElement> danhSachMo = driver.findElements(By.xpath("//tr[.//input[contains(@class, 'user-status') and @checked]]"));
        return danhSachMo.size() > 0;
    }

    // ========================================================
    // HÀM MỚI BỔ SUNG CHO TÍNH NĂNG "KHÓA/MỞ KHÓA TRẠNG THÁI"
    // ========================================================
    public boolean kiemTraTrangThaiDongDauTien() {
        WebElement toggle = wait.until(ExpectedConditions.presenceOfElementLocated(toggleTrangThaiDauTien));
        // Kiểm tra xem checkbox có đang được tick (checked) không
        return toggle.isSelected(); 
    }

    public void bamToggleTrangThaiDongDauTien() {
        WebElement toggle = wait.until(ExpectedConditions.presenceOfElementLocated(toggleTrangThaiDauTien));
        // Dùng JS click vì checkbox UI/UX thường bị thẻ span/div đè lên che mất
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
    }
    // ========================================================

    public void bamNutXoaHocKyDangMo() {
        donDepPopup();
        By locatorXoaCuaDongMo = By.xpath("//tr[.//input[contains(@class, 'user-status') and @checked]]//a[contains(@class, 'deleteRow')]");
        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(locatorXoaCuaDongMo));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        } catch (Exception e) { throw new RuntimeException("Không tìm thấy học kỳ 'Mở' để xóa!"); }
    }

    public void bamNutXoaDauTien() { 
        donDepPopup();
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnXoaDongDauTien));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }
    public void xacNhanXoaTrenPopup() { wait.until(ExpectedConditions.elementToBeClickable(btnSwalXacNhan)).click(); }
    public void huyXoaTrenPopup() { wait.until(ExpectedConditions.elementToBeClickable(btnSwalHuy)).click(); }
    public String layMauNutXoaDauTien() { return driver.findElement(btnXoaDongDauTien).getCssValue("color"); }
    public String layTieuDePopupXoa() { return wait.until(ExpectedConditions.visibilityOfElementLocated(txtSwalTitle)).getText(); }
    public String layNoiDungPopupXoa() { return wait.until(ExpectedConditions.visibilityOfElementLocated(txtSwalContent)).getText(); }
    
    public void bamXoaHocKyDauTien() { bamNutXoaDauTien(); }
    public void bamXacNhanXoa() { xacNhanXoaTrenPopup(); }
    public void bamHuyXoaTrongPopup() { huyXoaTrenPopup(); }

    public void bamThemHocKyMoi() { donDepPopup(); wait.until(ExpectedConditions.elementToBeClickable(btnThemHocKyMoi)).click(); }
    public void bamSuaHocKyDauTien() { wait.until(ExpectedConditions.elementToBeClickable(btnSuaDongDauTien)).click(); }
    public void bamHuy() { wait.until(ExpectedConditions.elementToBeClickable(btnHuy)).click(); }
    public void bamLuu() { wait.until(ExpectedConditions.elementToBeClickable(btnLuu)).click(); }

    public boolean kiemTraTruongHocKyBiKhoa() {
        WebElement oHocKy = wait.until(ExpectedConditions.visibilityOfElementLocated(txtHocKy));
        return !oHocKy.isEnabled();
    }

    private void chonGiaTriSelect2(String idTruong, String giaTriCanChon) {
        try {
            WebElement container = wait.until(ExpectedConditions.elementToBeClickable(By.id("select2-" + idTruong + "-container")));
            container.click();
            Thread.sleep(300);
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(@class, 'select2-results__option') and text()='" + giaTriCanChon + "']")));
            option.click();
        } catch (Exception e) {}
    }

    public void nhapThongTinHocKy(String hocKy, String namBD, String namKT, String tuanBD, String ngayBD, String tietToiDa, String lopToiDa) {
        WebElement oHocKy = wait.until(ExpectedConditions.visibilityOfElementLocated(txtHocKy));
        if (oHocKy.isEnabled()) { oHocKy.clear(); oHocKy.sendKeys(hocKy); }
        if (namBD != null && !namBD.isEmpty()) chonGiaTriSelect2("start_year", namBD);
        if (namKT != null && !namKT.isEmpty()) chonGiaTriSelect2("end_year", namKT);
        
        driver.findElement(txtTuanBatDau).clear(); driver.findElement(txtTuanBatDau).sendKeys(tuanBD);
        ((JavascriptExecutor) driver).executeScript("document.getElementById('start_date').value='" + ngayBD + "';");
        
        driver.findElement(txtMaxLesson).clear(); driver.findElement(txtMaxLesson).sendKeys(tietToiDa);
        driver.findElement(txtMaxClass).clear(); driver.findElement(txtMaxClass).sendKeys(lopToiDa);
    }

    public void bamNutTangTuan() { wait.until(ExpectedConditions.elementToBeClickable(btnTangTuan)).click(); }
    public void bamNutGiamTuan() { wait.until(ExpectedConditions.elementToBeClickable(btnGiamTuan)).click(); }
    public String layGiaTriTuanBatDau() { return driver.findElement(txtTuanBatDau).getAttribute("value"); }
    public void bamMoDropdownNamBatDau() { wait.until(ExpectedConditions.elementToBeClickable(ddlNamBatDauContainer)).click(); }
    public boolean kiemTraDropdownNamMoRa() {
        try { return driver.findElement(By.cssSelector("span.select2-container--open")).isDisplayed(); } catch (Exception e) { return false; }
    }

    public String layThongBao() {
        try {
            WebDriverWait fastWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement toast = fastWait.until(ExpectedConditions.presenceOfElementLocated(msgThongBao));
            return toast.getText().trim().toLowerCase();
        } catch (Exception e) { return ""; }
    }
}