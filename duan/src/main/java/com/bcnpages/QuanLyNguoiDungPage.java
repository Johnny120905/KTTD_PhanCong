package com.bcnpages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class QuanLyNguoiDungPage {
    private WebDriver driver;

    // ==========================================
    // LOCATORS
    // ==========================================
    private By btnThemNguoiDung = By.xpath("//button[@type='button' and contains(@class, 'btn-primary') and not(contains(@class, 'scroll-top'))]"); 
    private By btnSuaDauTien = By.cssSelector("a.editRow.text-success");
    private By btnXoaDauTien = By.cssSelector("a.deleteRow.text-danger"); 
    
    private By txtMaGV = By.id("staff_id");
    private By txtTenGV = By.id("full_name");
    private By txtEmail = By.id("email");
    private By ddlLoai = By.name("type"); 
    private By ddlRole = By.id("role_id"); 
    private By chkQuocTich = By.id("is_vietnamese");
    private By btnLuu = By.xpath("//button[@type='submit']");
    private By btnHuy = By.id("btnClose");
    
    private By txtSearch = By.cssSelector("#tblUser_filter input[type='search']");
    private By ddlFilterRole = By.id("UserRole");
    private By ddlFilterLoai = By.id("UserType");
    private By inputChonCot = By.cssSelector(".select2-search__field"); 
    
    private By tblDuLieu = By.id("tblUser");
    private By danhSachTieuDeCot = By.cssSelector("#tblUser thead th"); 
    private By txtKhongCoDuLieu = By.cssSelector(".dataTables_empty");
    private By ddlHienThi = By.name("tblUser_length"); 
    private By btnTrangTiepTheo = By.cssSelector("#tblUser_next a"); 
    private By txtThongTinPhanTrang = By.cssSelector(".dataTables_info"); 

    private By btnXacNhanXoa = By.xpath("//button[contains(@class, 'swal2-confirm') or text()='Xoá']");
    private By btnHuyXoa = By.xpath("//button[contains(@class, 'swal2-cancel') or text()='Huỷ']");
    private By msgThongBaoToast = By.cssSelector(".toast, .swal2-popup"); 
    private By msgLoiDinhDang = By.cssSelector("label.error");

    public QuanLyNguoiDungPage(WebDriver driver) {
        this.driver = driver;
    }

    // ==========================================
    // ACTIONS: THÊM / SỬA / XÓA
    // ==========================================
    public void bamNutThemNguoiDung() { driver.findElement(btnThemNguoiDung).click(); }
    public void bamNutSuaDauTien() { driver.findElement(btnSuaDauTien).click(); }
    
    public void bamNutXoaDauTien() {
        WebElement btnXoa = driver.findElement(btnXoaDauTien);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", btnXoa);
    }

    public void xacNhanXoaTrenPopup() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement btnXacNhan = wait.until(ExpectedConditions.visibilityOfElementLocated(btnXacNhanXoa));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", btnXacNhan);
    }

    public void huyXoaTrenPopup() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(btnHuyXoa)).click();
    }

    public void nhapThongTinNguoiDung(String maGV, String tenGV, String email, String loai, String role) {
        if (maGV != null) { try { driver.findElement(txtMaGV).clear(); driver.findElement(txtMaGV).sendKeys(maGV); } catch (Exception e) {} }
        if (email != null) { try { driver.findElement(txtEmail).clear(); driver.findElement(txtEmail).sendKeys(email); } catch (Exception e) {} }
        if (tenGV != null) { driver.findElement(txtTenGV).clear(); driver.findElement(txtTenGV).sendKeys(tenGV); }
        if (loai != null && !loai.isEmpty()) { new Select(driver.findElement(ddlLoai)).selectByVisibleText(loai); }
        if (role != null && !role.isEmpty()) { new Select(driver.findElement(ddlRole)).selectByVisibleText(role); }
    }
    
    public void chonQuocTich(boolean laVietNam) {
        WebElement chk = driver.findElement(chkQuocTich);
        if (laVietNam && !chk.isSelected()) chk.click();
        else if (!laVietNam && chk.isSelected()) chk.click();
    }

    public void bamLuu() { driver.findElement(btnLuu).click(); }
    public void bamHuy() { driver.findElement(btnHuy).click(); }
    
    public String layThongBao() {
        StringBuilder thongBaoGop = new StringBuilder();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            try { thongBaoGop.append(wait.until(ExpectedConditions.visibilityOfElementLocated(msgThongBaoToast)).getText()).append(" "); } catch (Exception eToast) {}
            List<WebElement> errors = driver.findElements(msgLoiDinhDang);
            for (WebElement err : errors) { if (err.isDisplayed()) thongBaoGop.append(err.getText()).append(" "); }
        } catch (Exception e) {}
        return thongBaoGop.toString();
    }

    // ==========================================
    // ACTIONS: XEM (VIEW) & KIỂM TRA MẶC ĐỊNH
    // ==========================================
    
    // ĐÃ FIX BỌC THÉP: Tìm thẳng thẻ chứa chữ, sau đó trỏ ngược lên tổ tiên để lấy khối Card an toàn tuyệt đối
    public String laySoLieuThongKe(String tenTheCanTim) {
        try {
            By theChaLocator = By.xpath("//*[normalize-space(text())='" + tenTheCanTim + "']/ancestor::div[contains(@class, 'card') or contains(@class, 'bg-')][1]");
            return driver.findElement(theChaLocator).getText();
        } catch (Exception e) {
            try {
                // Backup plan phòng hờ Dev đổi class thẻ HTML
                return driver.findElement(By.xpath("//*[normalize-space(text())='" + tenTheCanTim + "']/../..")).getText();
            } catch (Exception ex) {
                return "";
            }
        }
    }

    public String layGiaTriDropdownHienThiMacDinh() {
        Select select = new Select(driver.findElement(ddlHienThi));
        return select.getFirstSelectedOption().getText();
    }

    public List<String> layDanhSachTenCotHienThi() {
        List<WebElement> columns = driver.findElements(danhSachTieuDeCot);
        List<String> colNames = new ArrayList<>();
        for (WebElement col : columns) {
            if (!col.getText().trim().isEmpty()) {
                colNames.add(col.getText().trim());
            }
        }
        return colNames;
    }

    public boolean kiemTraMauIconRole(String tenRole, String classCSSMongMuon) {
        try {
            By iconLocator = By.xpath("//td[.//span[contains(text(), '" + tenRole + "')]]//*[local-name()='svg']");
            WebElement icon = driver.findElement(iconLocator);
            return icon.getAttribute("class").contains(classCSSMongMuon);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean kiemTraBadgeLoaiGV(String loaiGV, String classBadgeMongMuon) {
        try {
            By badgeLocator = By.xpath("//td[.//span[contains(text(), '" + loaiGV + "')]]//span[contains(@class, 'badge')]");
            WebElement badge = driver.findElement(badgeLocator);
            return badge.getAttribute("class").contains(classBadgeMongMuon);
        } catch (Exception e) {
            return false;
        }
    }

    // ĐÃ BỔ SUNG: Chức năng test Sắp xếp (Sorting) DataTables
    public void bamTieuDeCotDeSapXep(String tenCot) {
        WebElement th = driver.findElement(By.xpath("//table[@id='tblUser']//th[contains(., '" + tenCot + "')]"));
        th.click();
    }

    public String layTrangThaiSapXepCuaCot(String tenCot) {
        try {
            WebElement th = driver.findElement(By.xpath("//table[@id='tblUser']//th[contains(., '" + tenCot + "')]"));
            return th.getAttribute("aria-sort");
        } catch (Exception e) {
            return null;
        }
    }

    // ==========================================
    // ACTIONS: TÌM KIẾM, BỘ LỌC CỘT & PHÂN TRANG
    // ==========================================
    public void timKiemUser(String keyword) {
        WebElement searchBox = driver.findElement(txtSearch);
        searchBox.clear();
        searchBox.sendKeys(keyword);
        searchBox.sendKeys(Keys.ENTER); 
    }

    public void locTheoRole(String role) { new Select(driver.findElement(ddlFilterRole)).selectByVisibleText(role); }
    public void locTheoLoaiGV(String loai) { new Select(driver.findElement(ddlFilterLoai)).selectByVisibleText(loai); }

    public void tatMoHienThiCot(String tenCot) {
        WebElement inputField = driver.findElement(inputChonCot);
        inputField.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        By cotOption = By.xpath("//ul[contains(@id, 'columnFilter-results')]/li[text()='" + tenCot + "']");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(cotOption));
        option.click();
        inputField.sendKeys(Keys.ESCAPE);
    }

    public boolean kiemTraCotTrenBang(String tenCot) {
        try { return driver.findElement(By.xpath("//table[@id='tblUser']//th[contains(text(), '" + tenCot + "')]")).isDisplayed(); } catch (Exception e) { return false; }
    }

    public void chonSoLuongHienThi(String soLuong) { new Select(driver.findElement(ddlHienThi)).selectByVisibleText(soLuong); }

    public int demSoDongTrenBang() {
        try {
            if (kiemTraBangRong()) return 0;
            List<WebElement> rows = driver.findElements(By.cssSelector("#tblUser tbody tr"));
            return rows.size();
        } catch (Exception e) { return 0; }
    }

    public String layNoiDungBang() { return driver.findElement(tblDuLieu).getText(); }
    public boolean kiemTraBangRong() { try { return driver.findElement(txtKhongCoDuLieu).isDisplayed(); } catch (Exception e) { return false; } }

    public void bamChuyenSangTrang(String soTrang) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebElement element = driver.findElement(By.xpath("//li[contains(@class, 'paginate_button')]/a[text()='" + soTrang + "']"));
        js.executeScript("arguments[0].click();", element);
    }

    public void bamTrangTiepTheo() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebElement element = driver.findElement(btnTrangTiepTheo);
        js.executeScript("arguments[0].click();", element);
    }

    public String layThongTinPhanTrang() { return driver.findElement(txtThongTinPhanTrang).getText(); }

    // ==========================================
    // UI/UX (MÀU SẮC, TRẠNG THÁI & SCROLL BẢN ĐỒ)
    // ==========================================
    public String layMauVienCuaOEmail() { return driver.findElement(txtEmail).getCssValue("border-color"); }
    public String layGiaTriThucTeMaGV() { return driver.findElement(txtMaGV).getAttribute("value"); }
    public String layMauNutSuaDauTien() { return driver.findElement(btnSuaDauTien).getCssValue("color"); }
    public String layMauNutXoaDauTien() { return driver.findElement(btnXoaDauTien).getCssValue("color"); }
    public boolean kiemTraNutXoaHienThi() { return driver.findElement(btnXoaDauTien).isDisplayed(); }

    public void cuonXuongCuoiTrang() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });");
    }

    public void cuonLenDauTrang() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo({ top: 0, behavior: 'smooth' });");
    }
}