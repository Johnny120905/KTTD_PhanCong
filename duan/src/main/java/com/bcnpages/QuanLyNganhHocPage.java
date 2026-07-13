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

public class QuanLyNganhHocPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- 1. LOCATORS: XEM & TÌM KIẾM ---
    private By txtTimKiem = By.cssSelector("input[type='search']"); 
    private By danhSachNganhHoc = By.xpath("//table/tbody/tr"); 
    private By msgKhongCoDuLieu = By.className("dataTables_empty"); 
    private By ddlHienThiSoLuong = By.name("tblMajor_length"); 

    // --- 2. LOCATORS: THÊM NGÀNH MỚI ---
    private By btnThemHocKyMoi = By.cssSelector("button.btn-primary.createNew"); 
    private By txtHocKy = By.id("id"); 
    private By txtNgayBatDau = By.id("start_date");
    private By txtMaxLesson = By.id("max_lesson");
    private By txtMaxClass = By.id("max_class");
    private By txtMaNganh = By.id("id"); 
    private By txtTenNganh = By.id("name");
    private By txtVietTat = By.id("abbreviation");
    private By btnLuu = By.xpath("//button[@type='submit']");
    private By btnHuy = By.id("btnClose"); 
    private By msgThongBao = By.cssSelector(".toast, .swal2-popup:not(.swal2-icon-warning)");
    private By msgLoiDinhDang = By.cssSelector("label.error, .invalid-feedback, .text-danger");

    // --- 3. LOCATORS: CẬP NHẬT (SỬA) ---
    private By btnSuaDongDauTien = By.cssSelector("table tbody tr:first-child a.editRow");

    // --- 4. LOCATORS: XÓA ---
    private By btnXoaDongDauTien = By.cssSelector("table tbody tr:first-child a.deleteRow");
    private By btnSwalXacNhan = By.cssSelector("button.swal2-confirm");
    private By btnSwalHuy = By.cssSelector("button.swal2-cancel");
    private By txtSwalTitle = By.id("swal2-title");
    private By txtSwalContent = By.id("swal2-html-container");

    // --- 5. LOCATORS: PHÂN TRANG (PAGINATION) ---
    private By txtThongTinPhanTrang = By.id("tblMajor_info"); 
    private By btnTrangTiepTheo = By.xpath("//li[@id='tblMajor_next']/a");
    private By btnTrangTruoc = By.xpath("//li[@id='tblMajor_previous']/a");
    private By liTrangTruocContainer = By.id("tblMajor_previous"); 

    public QuanLyNganhHocPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void donDepPopup() {
        ((JavascriptExecutor) driver).executeScript("if(document.querySelector('.swal2-container')) { document.querySelector('.swal2-container').remove(); }");
    }

    // =========================================
    // CÁC HÀM XEM & TÌM KIẾM
    // =========================================
    public void nhapTuKhoaTimKiem(String tuKhoa) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(txtTimKiem));
        searchBox.clear();
        searchBox.sendKeys(tuKhoa);
    }

    public void chonSoLuongHienThi(String giaTriValue) {
        WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(ddlHienThiSoLuong));
        Select select = new Select(selectElement);
        select.selectByValue(giaTriValue);
    }

    public String layNoiDungDongDauTien() {
        try { 
            return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody tr:first-child"))).getText(); 
        } catch (Exception e) { return ""; }
    }

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
        donDepPopup();
        try {
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnThemHocKyMoi));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        } catch (Exception e) {
            WebElement btnBackup = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.btn-primary")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnBackup);
        }
    }

    public void chonCTDT(String textToSelect) {
        By select2Container = By.id("select2-program_type-container");
        WebElement container = wait.until(ExpectedConditions.elementToBeClickable(select2Container));
        container.click(); 
        
        try { Thread.sleep(500); } catch (Exception e) {} 
        
        By optionXpath = By.xpath("//ul[@id='select2-program_type-results']/li[contains(text(),'" + textToSelect + "')]");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionXpath));
        option.click();
    }

    public void nhapThongTinNganh(String maNganh, String tenNganh, String vietTat, String ctdt) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(txtMaNganh)).clear();
        driver.findElement(txtMaNganh).sendKeys(maNganh);
        
        driver.findElement(txtTenNganh).clear();
        driver.findElement(txtTenNganh).sendKeys(tenNganh);
        
        driver.findElement(txtVietTat).clear();
        driver.findElement(txtVietTat).sendKeys(vietTat);
        
        if (ctdt != null && !ctdt.isEmpty()) {
            chonCTDT(ctdt);
        }
    }

    public void bamLuu() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnLuu));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }
    
    public void bamHuy() {
        try {
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnHuy));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        } catch (Exception e) {
            System.out.println("Nút Hủy không tồn tại trên DOM, bỏ qua thao tác đóng form.");
        }
    }

    public String layThongBao() {
        StringBuilder thongBaoGop = new StringBuilder();
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
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

    // =========================================
    // CÁC HÀM CẬP NHẬT (SỬA)
    // =========================================
    public void bamNutSuaDongDauTien() {
        donDepPopup();
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnSuaDongDauTien));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public boolean kiemTraMaNganhBiKhoa() {
        WebElement inputMaNganh = wait.until(ExpectedConditions.presenceOfElementLocated(txtMaNganh));
        return inputMaNganh.getAttribute("disabled") != null;
    }

    public void nhapThongTinCapNhatNganh(String tenNganh, String vietTat, String ctdt) {
        WebElement txtTen = wait.until(ExpectedConditions.visibilityOfElementLocated(txtTenNganh));
        txtTen.clear();
        if (tenNganh != null && !tenNganh.isEmpty()) {
            txtTen.sendKeys(tenNganh);
        }
        
        WebElement txtVT = driver.findElement(txtVietTat);
        txtVT.clear();
        if (vietTat != null && !vietTat.isEmpty()) {
            txtVT.sendKeys(vietTat);
        }
        
        if (ctdt != null && !ctdt.isEmpty()) {
            chonCTDT(ctdt);
        }
    }

    // =========================================
    // CÁC HÀM XÓA
    // =========================================
    public void bamNutXoaDauTien() { 
        donDepPopup(); 
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnXoaDongDauTien));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void xacNhanXoaTrenPopup() { 
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnSwalXacNhan));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public void huyXoaTrenPopup() { 
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnSwalHuy));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public String layMauNutXoaDauTien() { 
        return driver.findElement(btnXoaDongDauTien).getCssValue("color"); 
    }

    public String layTieuDePopupXoa() { 
        return wait.until(ExpectedConditions.visibilityOfElementLocated(txtSwalTitle)).getText(); 
    }

    public String layNoiDungPopupXoa() { 
        return wait.until(ExpectedConditions.visibilityOfElementLocated(txtSwalContent)).getText(); 
    }

    // =========================================
    // CÁC HÀM PHÂN TRANG & SẮP XẾP
    // =========================================
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
        By colLocator = By.xpath("//table[@id='tblMajor']//th[contains(@aria-label, '" + tenCot + "') or contains(., '" + tenCot + "')]");
        WebElement col = wait.until(ExpectedConditions.elementToBeClickable(colLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", col);
    }
    
    public String layTrangThaiSapXepCot(String tenCot) {
        By colLocator = By.xpath("//table[@id='tblMajor']//th[contains(@aria-label, '" + tenCot + "') or contains(., '" + tenCot + "')]");
        WebElement col = wait.until(ExpectedConditions.presenceOfElementLocated(colLocator));
        return col.getAttribute("class"); 
    }
}