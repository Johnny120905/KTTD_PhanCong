package com.bcnpages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class QuanLyThoiKhoaBieuPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- LOCATORS CŨ ---
    private By btnImportSubmit = By.id("submit-all");
    private By inputFile = By.cssSelector("input[type='file']");
    private By msgThongBao = By.cssSelector("#toast-container .toast-message, .swal2-html-container, .toast-body");
    private By msgLoiDinhDang = By.cssSelector("label.error, .invalid-feedback, .text-danger, #errorLecturers-section");
    private By dropdownHocKy = By.id("select2-term-container");
    private By dropdownNganh = By.id("select2-major-container");
    private By dropzoneArea = By.cssSelector(".dropzone, .upload-area, [for='file']");
    private By btnDownloadTemplate = By.xpath("//img[contains(@src, 'xls') or contains(@src, 'excel')] | //a[contains(@href, 'Template')]");

    // --- LOCATORS MỚI (EXPORT) ---
    private By btnExport = By.cssSelector("button.btn-export");
    private By inputLocGiangVien = By.xpath("//input[@placeholder='Lọc giảng viên']");

    public QuanLyThoiKhoaBieuPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // =========================================================================
    // PHẦN 1: CÁC HÀM IMPORT TKB
    // =========================================================================
    public void chonSelect2(String selectId, String textToSelect) {
        try {
            By select2Container = By.xpath("//span[@aria-labelledby='select2-" + selectId + "-container']");
            wait.until(ExpectedConditions.elementToBeClickable(select2Container)).click();
            Thread.sleep(500);
            By optionXpath = By.xpath("//li[contains(@class, 'select2-results__option') and contains(text(),'" + textToSelect + "')]");
            wait.until(ExpectedConditions.elementToBeClickable(optionXpath)).click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("$('#" + selectId + " option').filter(function() { return $(this).text() === '" + textToSelect + "'; }).prop('selected', true); $('#" + selectId + "').trigger('change');");
        }
    }

    public void nhapThongTinImport(String hocKy, String nganh, String duongDanFile) {
        if(!hocKy.isEmpty()) chonSelect2("term", hocKy);
        if(!nganh.isEmpty()) chonSelect2("major", nganh);
        if(!duongDanFile.isEmpty()) {
            WebElement fileElement = driver.findElement(inputFile);
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='visible'; arguments[0].style.display='block';", fileElement);
            fileElement.sendKeys(duongDanFile);
        }
    }

    public void bamImport() { wait.until(ExpectedConditions.elementToBeClickable(btnImportSubmit)).click(); }
    public void clickDownloadTemplate() {
        try { wait.until(ExpectedConditions.elementToBeClickable(btnDownloadTemplate)).click(); }
        catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(btnDownloadTemplate)); }
    }

    public String layThongBao() {
        StringBuilder thongBaoGop = new StringBuilder();
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement toast = shortWait.until(ExpectedConditions.presenceOfElementLocated(msgThongBao));
            thongBaoGop.append(toast.getText()).append(" ");
        } catch (Exception e) {}
        try {
            List<WebElement> errors = driver.findElements(msgLoiDinhDang);
            for (WebElement err : errors) if (err.isDisplayed()) thongBaoGop.append(err.getText()).append(" ");
        } catch (Exception e) {}
        return thongBaoGop.toString().trim().toLowerCase();
    }

    public boolean isDropdownHocKyDisplayed() { return !driver.findElements(dropdownHocKy).isEmpty(); }
    public boolean isDropdownNganhDisplayed() { return !driver.findElements(dropdownNganh).isEmpty(); }
    public boolean isUploadAreaDisplayed() { return !driver.findElements(dropzoneArea).isEmpty(); }
    public boolean isBtnImportDisplayed() { return driver.findElement(btnImportSubmit).isDisplayed(); }
    public boolean isBtnDownloadTemplateDisplayed() { return !driver.findElements(btnDownloadTemplate).isEmpty(); }

    // =========================================================================
    // PHẦN 2: CÁC HÀM PHÂN CÔNG GIẢNG VIÊN
    // =========================================================================
    public void chonHocKyVaNganh(String hocKy, String nganh) {
        try {
            if(!hocKy.isEmpty()) chonSelect2("term", hocKy);
            if(!nganh.isEmpty()) chonSelect2("major", nganh);
            Thread.sleep(3000); 
        } catch (Exception e) {}
    }

    public void waitForTableLoad() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tblAssign")));
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("button.assign-card")));
            Thread.sleep(1500); 
        } catch (Exception e) {}
    }

    public List<WebElement> layCacOChuaPhanCong() {
        return driver.findElements(By.cssSelector("button.assign-card[class*='unassigned']")); 
    }

    public void clickOChuaPhanCong(int index) throws InterruptedException {
        List<WebElement> buttons = layCacOChuaPhanCong();
        if(buttons.size() <= index) {
            throw new RuntimeException("Lỗi: Không tìm thấy ô 'Chưa phân' ở vị trí " + index + ".");
        }
        WebElement slot = buttons.get(index);
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", slot);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", slot);
        Thread.sleep(1000); 
    }

    public void phanCongGiangVienBangSearch(String tenGV) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("$('div.popover.show select[name=\"lecturer\"]').select2('open');");
        Thread.sleep(1000);

        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.select2-search__field[aria-controls*='lecturer']")));
        searchInput.clear();
        searchInput.sendKeys(tenGV);
        Thread.sleep(1500);

        WebElement resultOption = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.select2-results__option--highlighted")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", resultOption);
        Thread.sleep(500); 
    }

    public void clickLuuPhanCong() throws InterruptedException {
        WebElement btnAssign = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.popover.show button.btn-assign")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnAssign);
        Thread.sleep(1500);
    }

    public void clickLuuPhanCongBlank() {
        WebElement btnAssign = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.popover.show button.btn-assign")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnAssign);
    }

    public String searchGiangVienTraVeLoi(String tenGV) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("$('div.popover.show select[name=\"lecturer\"]').select2('open');");
        Thread.sleep(1000);

        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.select2-search__field[aria-controls*='lecturer']")));
        searchInput.clear();
        searchInput.sendKeys(tenGV);
        Thread.sleep(1500); 

        try {
            return driver.findElement(By.cssSelector("li.select2-results__message")).getText(); 
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isPopoverDayDuUI() {
        try {
            boolean hasSelect = driver.findElement(By.cssSelector("div.popover.show .select2-selection")).isDisplayed();
            boolean hasAssign = driver.findElement(By.cssSelector("div.popover.show button.btn-assign")).isDisplayed();
            return hasSelect && hasAssign;
        } catch (Exception e) { return false; }
    }
    
    public void dongPopoverHienTai(int index) {
        try {
            WebElement slot = layCacOChuaPhanCong().get(index);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", slot);
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

    // =========================================================================
    // PHẦN 3: CÁC HÀM XÓA LỚP HỌC MỚI BỔ SUNG
    // =========================================================================
    public List<WebElement> layCacODaPhanCong() {
        return driver.findElements(By.xpath("//button[contains(@class, 'assign-card') and not(contains(., 'Chưa phân'))]"));
    }

    public void clickODaPhanCong(int index) throws InterruptedException {
        List<WebElement> buttons = layCacODaPhanCong();
        if(buttons.size() <= index) {
            throw new RuntimeException("Lỗi: Không tìm thấy ô 'Đã phân công' ở vị trí " + index + ".");
        }
        WebElement slot = buttons.get(index);
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", slot);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", slot);
        Thread.sleep(1000); 
    }

    public void clickNutThungRacXoa() throws InterruptedException {
        WebElement btnDelete = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.popover.show button.btn-delete")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnDelete);
        Thread.sleep(1000); 
    }

    public String layMaLopHocPhanTuPopup() {
        try {
            WebElement textContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("swal2-html-container")));
            String fullText = textContainer.getText(); 
            return fullText.split(", ")[1].split(" để")[0].trim();
        } catch(Exception e) {
            return "";
        }
    }

    public void nhapMaXacNhanXoa(String maXacNhan) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.swal2-input")));
        input.clear();
        input.sendKeys(maXacNhan);
    }

    public void clickXacNhanXoaTrenPopup() throws InterruptedException {
        WebElement btnConfirm = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.swal2-confirm")));
        btnConfirm.click();
        Thread.sleep(1500); 
    }

    public void clickHuyXoaTrenPopup() throws InterruptedException {
        WebElement btnCancel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.swal2-cancel")));
        btnCancel.click();
        Thread.sleep(1000); 
    }

    public boolean isPopupXoaHienThi() {
        try {
            return driver.findElement(By.cssSelector(".swal2-popup")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // =========================================================================
    // PHẦN 4: CÁC HÀM EXPORT TKB (XUẤT DỮ LIỆU)
    // =========================================================================
    public void clickNhanExport() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnExport));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public boolean isNutExportHienThi() {
        try {
            return driver.findElement(btnExport).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isNutExportKhaDung() {
        try {
            return driver.findElement(btnExport).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void nhapDuLieuLocGiangVien(String tenGiangVien) {
        try {
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputLocGiangVien));
            input.clear();
            input.sendKeys(tenGiangVien);
            Thread.sleep(1000); 
        } catch (Exception e) {
            System.out.println("Không tìm thấy ô Lọc giảng viên");
        }
    }

    // =========================================================================
    // PHẦN 5: CÁC HÀM XEM TKB TOÀN KHOA (MULTI-SELECT & BẢNG)
    // =========================================================================
    public void locDuLieuToanKhoa(String filterId, String keyword) {
        try {
            WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("select#" + filterId + " + span .select2-search__field")));
            searchInput.click();
            searchInput.clear();
            searchInput.sendKeys(keyword);
            Thread.sleep(1500);

            WebElement resultOption = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.select2-results__option--highlighted")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", resultOption);
            Thread.sleep(1000); 
        } catch (Exception e) {}
    }

    public String layThongBaoLoiLocToanKhoa(String filterId, String keyword) {
        try {
            WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("select#" + filterId + " + span .select2-search__field")));
            searchInput.click();
            searchInput.clear();
            searchInput.sendKeys(keyword);
            Thread.sleep(1500); 

            return driver.findElement(By.cssSelector("li.select2-results__message")).getText(); 
        } catch (Exception e) {
            return "";
        }
    }

    public void nhapGioiHanBienLocToanKhoa(String filterId, String text) {
        try {
            WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("select#" + filterId + " + span .select2-search__field")));
            searchInput.click();
            searchInput.clear();
            searchInput.sendKeys(text);
            Thread.sleep(500);
        } catch (Exception e) {}
    }

    public boolean kiemTraBangTKBCoDuLieu() {
        try {
            return driver.findElements(By.cssSelector(".assign-card")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void cuonBangTKB(String chieu) {
        try {
            WebElement tableContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".assign-table-div")));
            if (chieu.equalsIgnoreCase("ngang")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", tableContainer);
            } else if (chieu.equalsIgnoreCase("doc")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", tableContainer);
            }
            Thread.sleep(1000);
        } catch (Exception e) {}
    }
    
    public boolean kiemTraThanhCongCu() {
        return driver.findElement(By.id("term")).isDisplayed() && driver.findElement(By.id("major")).isDisplayed();
    }

    // =========================================================================
    // PHẦN 6: CÁC HÀM XEM TKB CÁ NHÂN (ĐÃ SỬA LẠI CHUẨN THAO TÁC ENTER)
    // =========================================================================
    public void chonGiangVienTKBCaNhan(String tuKhoa) throws InterruptedException {
        // Mở dropdown chọn Giảng viên (ID lecturer)
        ((JavascriptExecutor) driver).executeScript("$('#lecturer').select2('open');");
        Thread.sleep(1000); // Chờ hiệu ứng bung dropdown
        
        // Tìm ô input search của Select2 đang hiện lên
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.select2-search__field")));
        searchInput.clear();
        searchInput.sendKeys(tuKhoa);
        Thread.sleep(1500); // Chờ list kết quả lọc mờ ra đúng người
        
        // Gửi lệnh phím ENTER để tự động chọn người đang được bôi sáng
        searchInput.sendKeys(Keys.ENTER);
        Thread.sleep(2000); // Chờ bảng TKB cá nhân Auto-load render ra dữ liệu
    }

    public String nhapGiangVienTraVeLoi(String tuKhoa) {
        try {
            ((JavascriptExecutor) driver).executeScript("$('#lecturer').select2('open');");
            Thread.sleep(500);
            
            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.select2-search__field")));
            searchInput.clear();
            searchInput.sendKeys(tuKhoa);
            Thread.sleep(1500); 

            return driver.findElement(By.cssSelector("li.select2-results__message")).getText(); 
        } catch (Exception e) {
            return "";
        }
    }

    public boolean kiemTraTrangThaiTrongTKBCaNhan() {
        try {
            return driver.findElement(By.xpath("//div[@id='personalTimetableDiv']//h4[contains(text(), 'Chưa có dữ liệu')]")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean kiemTraHienThiTuanTKBCaNhan() {
        try {
            return driver.findElement(By.cssSelector(".alert-body strong")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean kiemTraBangTKBCaNhanCoDuLieu() {
        try {
            return driver.findElements(By.cssSelector(".theory-class, .practice-class")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void cuonBangTKBCaNhan(String chieu) {
        try {
            WebElement tableContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".personal-timetable-div")));
            if (chieu.equalsIgnoreCase("ngang")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", tableContainer);
            } else if (chieu.equalsIgnoreCase("doc")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", tableContainer);
            }
            Thread.sleep(1000);
        } catch (Exception e) {}
    }
}