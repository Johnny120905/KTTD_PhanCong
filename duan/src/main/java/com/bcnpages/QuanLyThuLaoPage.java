package com.bcnpages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class QuanLyThuLaoPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public QuanLyThuLaoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // =========================================================================
    // HÀM ÉP CLICK BẰNG JAVASCRIPT (CHỐNG ĐƠ / CHE KHUẤT)
    // =========================================================================
    private void forceClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private void smartClick(By locator, String actionName) {
        try {
            System.out.println("  [Nguoi dung]: " + actionName);
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", element);
            Thread.sleep(300);
            try {
                element.click();
            } catch (Exception e) {
                forceClick(element);
            }
            Thread.sleep(500);
        } catch (Exception e) { System.out.println("  [Loi]: Khong the thuc hien -> " + actionName); }
    }

    // =========================================================================
    // PHẦN 1: QUẢN LÝ HỌC HÀM, HỌC VỊ (Giữ nguyên)
    // =========================================================================
   
    private By tableHocHam = By.id("tblAcademicDegree");
    private By tableRows = By.cssSelector("#tblAcademicDegree tbody tr:not(.odd > td.dataTables_empty)");
    private By selectLength = By.name("tblAcademicDegree_length");
    private By tableInfo = By.id("tblAcademicDegree_info");
    private By wrapperTable = By.id("tblAcademicDegree_wrapper");
    private By inputSearch = By.cssSelector("input[type='search'][aria-controls='tblAcademicDegree']");
    private By emptyRowMessage = By.cssSelector(".dataTables_empty");
    private By btnThemMoi = By.cssSelector("button.createNew");
    private By btnEditFirstRow = By.cssSelector("table#tblAcademicDegree tbody tr:first-child a.editRow");
    private By popupDialog = By.cssSelector(".ui-dialog");
    private By inputMaHH = By.id("id");    
    private By inputTenHH = By.id("name");  
    private By inputThuTu = By.id("level");
    private By btnLuu = By.cssSelector("form#academicdegree-form button[type='submit']");
    private By btnHuy = By.id("btnClose");
    private By btnXClose = By.cssSelector(".ui-dialog-titlebar-close");
    private By btnDeleteFirstRow = By.cssSelector("table#tblAcademicDegree tbody tr:first-child a.deleteRow");
    private By swalPopup = By.cssSelector(".swal2-popup");
    private By btnSwalConfirm = By.cssSelector("button.swal2-confirm");
    private By btnSwalCancel = By.cssSelector("button.swal2-cancel");  
    private By toastMessage = By.cssSelector("#toast-container .toast-message, .swal2-html-container, .invalid-feedback");

    public void waitForTableLoad() {
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(tableHocHam)); Thread.sleep(1000); } catch (Exception e) {}
    }
    public boolean isTableDisplayed() {
        try { return driver.findElement(tableHocHam).isDisplayed(); } catch (Exception e) { return false; }
    }
    public int getNumberOfRows() {
        System.out.println("  [He thong]: Dem so dong du lieu hien thi tren bang");
        return driver.findElements(tableRows).size();
    }
    public void chonSoLuongHienThi(String value) {
        try {
            System.out.println("  [Nguoi dung]: Chon hien thi " + value + " dong");
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(selectLength));
            Select select = new Select(dropdown);
            select.selectByValue(value); Thread.sleep(1500);
        } catch (Exception e) {}
    }
    public String getTableInfoText() {
        try { return driver.findElement(tableInfo).getText(); } catch (Exception e) { return null; }
    }
    public void cuonTrang(String chieu) {
        try {
            System.out.println("  [Nguoi dung]: Cuon trang theo chieu " + chieu);
            if (chieu.equalsIgnoreCase("doc")) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            } else if (chieu.equalsIgnoreCase("ngang")) {
                WebElement wrapper = driver.findElement(wrapperTable);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", wrapper);
            }
            Thread.sleep(1000);
        } catch (Exception e) {}
    }
    public void nhapTuKhoaTimKiem(String tuKhoa) {
        try {
            System.out.println("  [Nguoi dung]: Nhap tu khoa tim kiem: '" + tuKhoa + "'");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(inputSearch));
            searchBox.clear(); searchBox.sendKeys(tuKhoa); Thread.sleep(1500);
        } catch (Exception e) {}
    }
    public void xoaTrangO_TimKiem() {
        try {
            System.out.println("  [Nguoi dung]: Xoa trang o Tim kiem");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(inputSearch));
            searchBox.sendKeys(Keys.CONTROL + "a"); searchBox.sendKeys(Keys.BACK_SPACE); Thread.sleep(1500);
        } catch (Exception e) {}
    }
    public boolean kiemTraKhongCoKetQua() {
        try { return driver.findElement(emptyRowMessage).isDisplayed(); } catch (Exception e) { return false; }
    }
    public void bamChuyenTrang(String soTrang) {
        try {
            System.out.println("  [Nguoi dung]: Bam chuyen sang trang so " + soTrang);
            WebElement pageLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='" + soTrang + "' and @aria-controls='tblAcademicDegree']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", pageLink);
            Thread.sleep(500); pageLink.click(); Thread.sleep(1500);
        } catch (Exception e) {}
    }
    public boolean isSearchBoxDisplayed() {
        try { return driver.findElement(inputSearch).isDisplayed(); } catch (Exception e) { return false; }
    }
    public void clickNhanThemMoi() {
        try {
            System.out.println("  [Nguoi dung]: Bam nut Them Moi");
            WebElement btnAdd = wait.until(ExpectedConditions.elementToBeClickable(btnThemMoi));
            btnAdd.click(); wait.until(ExpectedConditions.visibilityOfElementLocated(popupDialog)); Thread.sleep(500);
        } catch (Exception e) {}
    }
    public void clickIconSuaDongDauTien() {
        try {
            System.out.println("  [Nguoi dung]: Click Chinh Sua dong dau tien");
            WebElement btnEdit = wait.until(ExpectedConditions.elementToBeClickable(btnEditFirstRow));
            btnEdit.click(); wait.until(ExpectedConditions.visibilityOfElementLocated(popupDialog)); Thread.sleep(500);
        } catch (Exception e) {}
    }
    public void dienFormThemMoi(String ma, String ten, String thuTu) {
        try {
            System.out.println("  [Nguoi dung]: Dien thong tin vao form Hoc Ham/Hoc vi");
            if (ma != null) { WebElement inputMa = wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaHH)); inputMa.clear(); inputMa.sendKeys(ma); }
            if (ten != null) { WebElement inputTen = driver.findElement(inputTenHH); inputTen.clear(); inputTen.sendKeys(ten); }
            if (thuTu != null) { WebElement inputSo = driver.findElement(inputThuTu); inputSo.clear(); inputSo.sendKeys(thuTu); }
            Thread.sleep(500);
        } catch (Exception e) {}
    }
    public void dienFormChinhSua(String tenMoi, String thuTuMoi) {
        try {
            System.out.println("  [Nguoi dung]: Dien cap nhat du lieu form Chinh Sua");
            if (tenMoi != null) { WebElement inputTen = wait.until(ExpectedConditions.visibilityOfElementLocated(inputTenHH)); inputTen.clear(); inputTen.sendKeys(tenMoi); }
            if (thuTuMoi != null) { WebElement inputSo = driver.findElement(inputThuTu); inputSo.clear(); inputSo.sendKeys(thuTuMoi); }
            Thread.sleep(500);
        } catch (Exception e) {}
    }
    public void clickLuu() {
        System.out.println("  [Nguoi dung]: Bam nut Luu");
        driver.findElement(btnLuu).click();
    }
    public void clickHuy() {
        System.out.println("  [Nguoi dung]: Bam nut Huy");
        driver.findElement(btnHuy).click();
    }
    public void clickTatX() {
        System.out.println("  [Nguoi dung]: Bam nut Tat (X)");
        driver.findElement(btnXClose).click();
    }
    public boolean isPopupHienThi() {
        System.out.println("  [He thong]: Kiem tra trang thai Popup Form...");
        try { return driver.findElement(popupDialog).isDisplayed(); } catch (Exception e) { return false; }
    }
    public String layThongBao() {
        System.out.println("  [He thong]: Doc noi dung thong bao Alert/Toast...");
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessage)).getText().toLowerCase(); } catch (Exception e) { return ""; }
    }
    public void clickIconXoaDongDauTien() {
        try {
            System.out.println("  [Nguoi dung]: Bam Thung rac de Xoa dong dau tien");
            WebElement btnDelete = wait.until(ExpectedConditions.elementToBeClickable(btnDeleteFirstRow));
            btnDelete.click(); wait.until(ExpectedConditions.visibilityOfElementLocated(swalPopup)); Thread.sleep(500);
        } catch (Exception e) {}
    }
    public boolean isPopupXoaHienThi() {
        System.out.println("  [He thong]: Kiem tra trang thai Popup xac nhan Xoa...");
        try { return driver.findElement(swalPopup).isDisplayed(); } catch (Exception e) { return false; }
    }
    public void clickXacNhanXoa() {
        try {
            System.out.println("  [Nguoi dung]: Bam Xac nhan Xoa");
            driver.findElement(btnSwalConfirm).click(); Thread.sleep(1000);
        } catch (Exception e) {}
    }
    public void clickHuyXoa() {
        try {
            System.out.println("  [Nguoi dung]: Bam Huy xoa");
            driver.findElement(btnSwalCancel).click(); Thread.sleep(500);
        } catch (Exception e) {}
    }
    public void clickSpamIconXoa() {
        try {
            System.out.println("  [Nguoi dung]: Spam click nut Thung rac 5 lan");
            WebElement btnDelete = wait.until(ExpectedConditions.elementToBeClickable(btnDeleteFirstRow));
            for(int i = 0; i < 5; i++) { btnDelete.click(); }
        } catch (Exception e) {}
    }

    // =========================================================================
    // PHẦN 2: QUẢN LÝ CẤP BẬC (Giữ nguyên)
    // =========================================================================
   
    private By cb_tableCapBac = By.id("tblAcademicDegreeRank");
    private By cb_btnThemMoi = By.cssSelector("button[aria-controls='tblAcademicDegreeRank'].createNew");
    private By cb_btnEditFirstRow = By.cssSelector("table#tblAcademicDegreeRank tbody tr:not(.group) a.editRow");
    private By cb_popupDialog = By.cssSelector(".ui-dialog");
    private By cb_select2Container = By.id("select2-academic_degree_id-container");
    private By cb_inputMaCapBac = By.id("id");
    private By cb_btnLuu = By.cssSelector("form#academicdegreerank-form button[type='submit']");
    private By cb_btnHuy = By.id("btnClose");
    private By cb_btnXClose = By.cssSelector(".ui-dialog-titlebar-close");
    private By cb_toastMessage = By.cssSelector("#toast-container .toast-message, .swal2-html-container, .invalid-feedback");

    private By cb_btnDeleteFirstRow = By.cssSelector("table#tblAcademicDegreeRank tbody tr:not(.group) a.deleteRow");
    private By cb_swalPopup = By.cssSelector(".swal2-popup");
    private By cb_btnSwalConfirm = By.cssSelector("button.swal2-confirm");
    private By cb_btnSwalCancel = By.cssSelector("button.swal2-cancel");  

    private By cb_inputSearch = By.cssSelector("input[type='search'][aria-controls='tblAcademicDegreeRank']");
    private By cb_emptyRowMessage = By.cssSelector("#tblAcademicDegreeRank .dataTables_empty");
    private By cb_selectLength = By.name("tblAcademicDegreeRank_length");
    private By cb_tableRows = By.cssSelector("#tblAcademicDegreeRank tbody tr:not(.group)");
    private By cb_tableInfo = By.id("tblAcademicDegreeRank_info");

    private By cb_colMaCapBac = By.xpath("//table[@id='tblAcademicDegreeRank']//th[contains(@class, 'sorting') and contains(text(), 'Mã')]");
    private By cb_btnPrevPage = By.id("tblAcademicDegreeRank_previous");
    private By cb_btnNextPage = By.id("tblAcademicDegreeRank_next");
    private By cb_btnBackToTop = By.cssSelector(".back-to-top, #back-to-top, .scroll-to-top, i.fa-arrow-up");

    public void cb_waitForTableLoad() {
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(cb_tableCapBac)); Thread.sleep(1000); } catch (Exception e) {}
    }

    public void cb_nhapTuKhoaTimKiem(String tuKhoa) {
        try {
            System.out.println("  [Nguoi dung]: Nhap tu khoa tim kiem Cap Bac: '" + tuKhoa + "'");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(cb_inputSearch));
            searchBox.clear();
            searchBox.sendKeys(tuKhoa);
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public void cb_xoaTrangO_TimKiem() {
        try {
            System.out.println("  [Nguoi dung]: Xoa trang du lieu o Tim Kiem");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(cb_inputSearch));
            searchBox.sendKeys(Keys.CONTROL + "a");
            searchBox.sendKeys(Keys.BACK_SPACE);
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public boolean cb_kiemTraKhongCoKetQua() {
        try { return driver.findElement(cb_emptyRowMessage).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public boolean cb_isSearchBoxDisplayed() {
        try { return driver.findElement(cb_inputSearch).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public int cb_getNumberOfRows() {
        System.out.println("  [He thong]: Dem so dong hien thi cua Cap Bac");
        try { return driver.findElements(cb_tableRows).size(); }
        catch (Exception e) { return 0; }
    }

    public void cb_chonSoLuongHienThi(String value) {
        try {
            System.out.println("  [Nguoi dung]: Chon hien thi " + value + " dong Cap Bac");
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(cb_selectLength));
            new Select(dropdown).selectByValue(value);
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public void cb_bamChuyenTrang(String soTrang) {
        try {
            System.out.println("  [Nguoi dung]: Bam chuyen Cap Bac sang trang " + soTrang);
            WebElement pageLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='" + soTrang + "' and @aria-controls='tblAcademicDegreeRank']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", pageLink);
            Thread.sleep(500);
            pageLink.click();
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public String cb_getTableInfo() {
        try { return driver.findElement(cb_tableInfo).getText(); }
        catch (Exception e) { return ""; }
    }

    public boolean cb_isPrevPageDisabled() {
        try {
            WebElement prevBtn = driver.findElement(cb_btnPrevPage);
            return prevBtn.getAttribute("class").contains("disabled");
        } catch (Exception e) { return false; }
    }
   
    public boolean cb_isNextPageDisabled() {
        try {
            WebElement nextBtn = driver.findElement(cb_btnNextPage);
            return nextBtn.getAttribute("class").contains("disabled");
        } catch (Exception e) { return false; }
    }

    public void cb_clickSortMaCapBac() {
        try {
            System.out.println("  [Nguoi dung]: Bam sap xep len/xuong o cot Ma Cap Bac");
            WebElement colHeader = wait.until(ExpectedConditions.elementToBeClickable(cb_colMaCapBac));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", colHeader);
            Thread.sleep(500);
            colHeader.click();
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public boolean cb_isBackToTopVisible() {
        try { return driver.findElement(cb_btnBackToTop).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public void cb_clickBackToTop() {
        try {
            System.out.println("  [Nguoi dung]: Click nut Back-To-Top (Mui ten tim)");
            WebElement btnTop = wait.until(ExpectedConditions.elementToBeClickable(cb_btnBackToTop));
            btnTop.click();
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

    public void cb_clickThemMoi() {
        try {
            System.out.println("  [Nguoi dung]: Click Them Moi Cap Bac");
            WebElement btnAdd = wait.until(ExpectedConditions.elementToBeClickable(cb_btnThemMoi));
            btnAdd.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(cb_popupDialog));
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

    public void cb_dienFormThemMoi(String tenHocHamHocVi, String maCapBac) {
        try {
            System.out.println("  [Nguoi dung]: Dien Form Them Cap Bac");
            Actions actions = new Actions(driver);

            if (tenHocHamHocVi != null) {
                WebElement selectBox = wait.until(ExpectedConditions.elementToBeClickable(cb_select2Container));
                actions.moveToElement(selectBox).click().perform();
                Thread.sleep(1500);

                By optionLocator = By.xpath("//ul[contains(@class, 'select2-results__options')]//li[contains(text(), '" + tenHocHamHocVi + "')]");
                WebElement optionToSelect = wait.until(ExpectedConditions.presenceOfElementLocated(optionLocator));
               
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", optionToSelect);
                Thread.sleep(1500);

                actions.moveToElement(optionToSelect).click().perform();
                Thread.sleep(1000);
            }
           
            if (maCapBac != null) {
                System.out.println("  [Nguoi dung]: Nhap ma cap bac la '" + maCapBac + "'");
                WebElement inputMa = wait.until(ExpectedConditions.visibilityOfElementLocated(cb_inputMaCapBac));
                ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled'); arguments[0].removeAttribute('readonly');", inputMa);
               
                actions.moveToElement(inputMa).click().perform();
                Thread.sleep(800);
                inputMa.clear();
                inputMa.sendKeys(maCapBac);
                Thread.sleep(1500);
            }
        } catch (Exception e) {
            System.out.println("\n========== ERROR: LỖI KHI TƯƠNG TÁC FORM CẤP BẬC ==========");
        }
    }

    public void cb_clickIconSuaDongDauTien() {
        try {
            System.out.println("  [Nguoi dung]: Click Chinh sua dong Cap Bac dau tien");
            WebElement btnEdit = wait.until(ExpectedConditions.elementToBeClickable(cb_btnEditFirstRow));
            btnEdit.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(cb_popupDialog));
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

    public void cb_dienFormChinhSua(String tenHocHamMoi, String maCapBacMoi) {
        cb_dienFormThemMoi(tenHocHamMoi, maCapBacMoi);
    }

    public void cb_clickIconXoaDongDauTien() {
        try {
            System.out.println("  [Nguoi dung]: Click Xoa dong Cap Bac dau tien");
            WebElement btnDelete = wait.until(ExpectedConditions.elementToBeClickable(cb_btnDeleteFirstRow));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btnDelete);
            Thread.sleep(500);
            btnDelete.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(cb_swalPopup));
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Lỗi: Không thể click icon Xóa Cấp Bậc.");
        }
    }

    public boolean cb_isPopupXoaHienThi() {
        System.out.println("  [He thong]: Kiem tra hien thi Popup Xoa Cap Bac...");
        try { return driver.findElement(cb_swalPopup).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public void cb_clickXacNhanXoa() {
        try {
            System.out.println("  [Nguoi dung]: Bam nut Xac nhan Xoa");
            WebElement btnConfirm = wait.until(ExpectedConditions.elementToBeClickable(cb_btnSwalConfirm));
            btnConfirm.click();
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public void cb_clickHuyXoa() {
        try {
            System.out.println("  [Nguoi dung]: Bam nut Huy thao tac Xoa");
            WebElement btnCancel = wait.until(ExpectedConditions.elementToBeClickable(cb_btnSwalCancel));
            btnCancel.click();
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

    public void cb_clickSpamIconXoa() {
        try {
            System.out.println("  [Nguoi dung]: Spam click icon Xoa nhieu lan");
            WebElement btnDelete = wait.until(ExpectedConditions.elementToBeClickable(cb_btnDeleteFirstRow));
            for(int i = 0; i < 5; i++) {
                btnDelete.click();
                Thread.sleep(200);
            }
        } catch (Exception e) {}
    }

    public void cb_clickLuu() {
        try {
            System.out.println("  [Nguoi dung]: Bam nut Luu Cap Bac");
            wait.until(ExpectedConditions.elementToBeClickable(cb_btnLuu)).click(); Thread.sleep(1500);
        } catch (Exception e) {}
    }
    public void cb_clickHuy() {
        try {
            System.out.println("  [Nguoi dung]: Bam Huy Form Cap Bac");
            driver.findElement(cb_btnHuy).click();
        } catch (Exception e) {}
    }
    public void cb_clickTatX() {
        try {
            System.out.println("  [Nguoi dung]: Tắt X form Cấp Bậc");
            driver.findElement(cb_btnXClose).click();
        } catch (Exception e) {}
    }
    public boolean cb_isPopupHienThi() {
        try { return driver.findElement(cb_popupDialog).isDisplayed(); } catch (Exception e) { return false; }
    }
   
    public String cb_layThongBao() {
        System.out.println("  [He thong]: Doc noi dung Thong Bao...");
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(cb_toastMessage)).getText().toLowerCase(); } catch (Exception e) { return ""; }
    }

    public void cb_cuonTrangDoc() {
        try {
            System.out.println("  [Nguoi dung]: Lướt cuộn dọc trang Cấp Bậc xuống");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
        } catch (Exception e) {}
    }
   
    public void cb_cuonTrangLenTop() {
        try {
            System.out.println("  [Nguoi dung]: Lướt cuộn dọc trang Cấp Bậc lên Đỉnh");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

    // =========================================================================
    // PHẦN 3: ĐƠN GIÁ (Giữ nguyên)
    // =========================================================================
   
    private By dg_tabTieuChuan = By.id("standard-tab");
    private By dg_tabDacBiet = By.id("special-tab");
    private By dg_tabNuocNgoai = By.id("foreign-tab");
   
    private By dg_btnSuaDongDau = By.xpath("(//div[contains(@class,'active')]//tbody//a[.//i[contains(@class,'feather-edit')] or contains(@class,'edit')])[1]");
    private By dg_btnXoaDongDau = By.xpath("(//div[contains(@class,'active')]//tbody//a[contains(@class,'deleteRow') or contains(@class,'text-danger')])[1]");
    private By dg_btnSuaTatCa = By.cssSelector(".tab-pane.active .editAll");
    private By dg_tableRows = By.cssSelector(".tab-pane.active tbody tr");
    private By dg_tableWrapper = By.cssSelector(".tab-pane.active .table-responsive");
    private By dg_btnScrollTop = By.cssSelector(".scroll-top");

    private By dg_popupForm = By.cssSelector(".ui-dialog");
    private By dg_inputPrice = By.id("price");
    private By dg_btnSave = By.cssSelector("form#unitprice-form button[type='submit']");
    private By dg_btnCancel = By.id("btnClose");
    private By dg_btnXClose = By.cssSelector(".ui-dialog-titlebar-close");
   
    private By dg_swalPopup = By.cssSelector(".swal2-popup");
    private By dg_btnSwalConfirm = By.cssSelector(".swal2-confirm");
    private By dg_btnSwalCancel = By.cssSelector(".swal2-cancel");
    private By dg_toastMessage = By.cssSelector("#toast-container .toast-message, .swal2-html-container, .invalid-feedback");

    public void dg_chuyenTab(String tenTab) {
        By targetTab = dg_tabTieuChuan;
        if (tenTab.equalsIgnoreCase("Đặc biệt")) targetTab = dg_tabDacBiet;
        else if (tenTab.equalsIgnoreCase("Nước ngoài")) targetTab = dg_tabNuocNgoai;
        smartClick(targetTab, "Click chuyen sang tab Don Gia: [" + tenTab + "]");
    }

    public void dg_clickSuaDongDauTien() {
        smartClick(dg_btnSuaDongDau, "Click Chinh Sua don gia (Dau tien tim thay)");
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(dg_popupForm)); } catch(Exception e){}
    }

    public void dg_clickSuaTatCa() {
        smartClick(dg_btnSuaTatCa, "Click nut Sua Tat Ca Don Gia");
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(dg_popupForm)); } catch(Exception e){}
    }

    public void dg_clickXoaDongDauTien() {
        smartClick(dg_btnXoaDongDau, "Click Thung rac de Xoa don gia");
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(dg_swalPopup)); } catch(Exception e){}
    }

    public void dg_dienFormDonGia(String donGia) {
        try {
            System.out.println("  [Nguoi dung]: Nhap don gia la: '" + donGia + "'");
            WebElement inputPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(dg_inputPrice));
            inputPrice.clear();
            if(donGia != null && !donGia.isEmpty()){
                inputPrice.sendKeys(donGia);
            }
            inputPrice.sendKeys(Keys.TAB);
            Thread.sleep(500);
        } catch (Exception e) { System.out.println("  [Loi]: O nhap don gia khong tim thay."); }
    }

    public void dg_clickLuu() { smartClick(dg_btnSave, "Bam nut Luu form"); }
    public void dg_clickHuy() { smartClick(dg_btnCancel, "Bam nut Huy form"); }
    public void dg_clickTatX() { smartClick(dg_btnXClose, "Bam Tat (X) form"); }
    public void dg_xacNhanXoa() { smartClick(dg_btnSwalConfirm, "Bam Xac nhan Dong Y Xoa"); }
    public void dg_huyXoa() { smartClick(dg_btnSwalCancel, "Bam Huy lenh Xoa"); }

    public boolean dg_isPopupHienThi() {
        try { return driver.findElement(dg_popupForm).isDisplayed(); } catch (Exception e) { return false; }
    }
    public boolean dg_isPopupXoaHienThi() {
        try { return driver.findElement(dg_swalPopup).isDisplayed(); } catch (Exception e) { return false; }
    }
    public String dg_layThongBao() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(dg_toastMessage)).getText().toLowerCase(); } catch (Exception e) { return ""; }
    }
    public int dg_getNumberOfRows() {
        try { return driver.findElements(dg_tableRows).size(); } catch (Exception e) { return 0; }
    }
    public void dg_cuonTrangDocNhanh() {
        try { ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);"); Thread.sleep(500); } catch (Exception e) {}
    }
    public void dg_cuonTrangLenTopNhanh() {
        try { ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);"); Thread.sleep(500); } catch (Exception e) {}
    }
    public void dg_cuonTrangNgangSangPhai() {
        try { WebElement wrapper = driver.findElement(dg_tableWrapper); ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", wrapper); Thread.sleep(500); } catch (Exception e) {}
    }
    public void dg_cuonTrangNgangVeTrai() {
        try { WebElement wrapper = driver.findElement(dg_tableWrapper); ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = 0;", wrapper); Thread.sleep(500); } catch (Exception e) {}
    }
    public boolean dg_isScrollTopVisible() {
        try { return driver.findElement(dg_btnScrollTop).isDisplayed(); } catch (Exception e) { return false; }
    }
    public void dg_clickScrollTop() {
        smartClick(dg_btnScrollTop, "Click Mui ten tim ve Dau trang");
    }

    // =========================================================================
    // PHẦN 4: HỆ SỐ (Giữ nguyên)
    // =========================================================================
   
    private By hs_tabHeSo = By.id("coefficient-tab");
    private By hs_tableHeSo = By.id("tblCoefficient");
    private By hs_tableRows = By.cssSelector("#tblCoefficient tbody tr");
    private By hs_btnSuaDongDau = By.cssSelector("#tblCoefficient tbody tr:first-child a.editRow");
   
    private By hs_popupForm = By.cssSelector(".ui-dialog");
    private By hs_inputTiengViet = By.id("vietnamese_coefficient");
    private By hs_inputTiengAnh = By.id("foreign_coefficient");
    private By hs_inputLyThuyet = By.id("theoretical_coefficient");
    private By hs_inputThucHanh = By.id("practice_coefficient");
   
    private By hs_btnSave = By.cssSelector("form#coefficient-form button[type='submit']");
    private By hs_btnCancel = By.id("btnClose");
    private By hs_btnXClose = By.cssSelector(".ui-dialog-titlebar-close");
   
    private By hs_tableWrapper = By.cssSelector("#coefficient .table-responsive");
    private By hs_btnScrollTop = By.cssSelector(".scroll-top");

    public void hs_chuyenTabHeSo() {
        smartClick(hs_tabHeSo, "Click chuyen sang tab He So");
    }

    public void hs_clickSuaDongDauTien() {
        smartClick(hs_btnSuaDongDau, "Click Chinh Sua He so dong dau tien");
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(hs_popupForm)); } catch(Exception e){}
    }

    public void hs_dienFormHeSo(String tiengViet, String tiengAnh, String lyThuyet, String thucHanh) {
        try {
            System.out.println("  [Nguoi dung]: Dien Form He So (TV: " + tiengViet + ", TA: " + tiengAnh + ", LT: " + lyThuyet + ", TH: " + thucHanh + ")");
           
            if (tiengViet != null) {
                WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(hs_inputTiengViet));
                input.sendKeys(Keys.CONTROL + "a"); input.sendKeys(Keys.BACK_SPACE);
                if (!tiengViet.isEmpty()) input.sendKeys(tiengViet);
            }
            if (tiengAnh != null) {
                WebElement input = driver.findElement(hs_inputTiengAnh);
                input.sendKeys(Keys.CONTROL + "a"); input.sendKeys(Keys.BACK_SPACE);
                if (!tiengAnh.isEmpty()) input.sendKeys(tiengAnh);
            }
            if (lyThuyet != null) {
                WebElement input = driver.findElement(hs_inputLyThuyet);
                input.sendKeys(Keys.CONTROL + "a"); input.sendKeys(Keys.BACK_SPACE);
                if (!lyThuyet.isEmpty()) input.sendKeys(lyThuyet);
            }
            if (thucHanh != null) {
                WebElement input = driver.findElement(hs_inputThucHanh);
                input.sendKeys(Keys.CONTROL + "a"); input.sendKeys(Keys.BACK_SPACE);
                if (!thucHanh.isEmpty()) input.sendKeys(thucHanh);
                input.sendKeys(Keys.TAB);
            }
            Thread.sleep(500);
        } catch (Exception e) { System.out.println("  [Loi]: Khong the dien form He So."); }
    }

    public void hs_clickLuu() { smartClick(hs_btnSave, "Bam nut Luu form He So"); }
    public void hs_clickHuy() { smartClick(hs_btnCancel, "Bam nut Huy form He So"); }
    public void hs_clickTatX() { smartClick(hs_btnXClose, "Bam Tat (X) form He So"); }

    public boolean hs_isPopupHienThi() {
        try { return driver.findElement(hs_popupForm).isDisplayed(); } catch (Exception e) { return false; }
    }

    public int hs_getNumberOfRows() {
        try { return driver.findElements(hs_tableRows).size(); } catch (Exception e) { return 0; }
    }

    public void hs_cuonTrangDocNhanh() {
        try { ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);"); Thread.sleep(500); } catch (Exception e) {}
    }
    public void hs_cuonTrangLenTopNhanh() {
        try { ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);"); Thread.sleep(500); } catch (Exception e) {}
    }
    public void hs_cuonTrangNgangSangPhai() {
        try { WebElement wrapper = driver.findElement(hs_tableWrapper); ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", wrapper); Thread.sleep(500); } catch (Exception e) {}
    }
    public void hs_cuonTrangNgangVeTrai() {
        try { WebElement wrapper = driver.findElement(hs_tableWrapper); ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = 0;", wrapper); Thread.sleep(500); } catch (Exception e) {}
    }
    public boolean hs_isScrollTopVisible() {
        try { return driver.findElement(hs_btnScrollTop).isDisplayed(); } catch (Exception e) { return false; }
    }
    public void hs_clickScrollTop() {
        smartClick(hs_btnScrollTop, "Click Mui ten tim ve Dau trang");
    }

    // =========================================================================
    // PHẦN 5: MÔN HỌC (SUBJECT) - Giữ nguyên
    // =========================================================================

    private By mh_selectTerm = By.id("term");
    private By mh_selectMajor = By.id("major");
    private By mh_selectLength = By.name("tblSubject_length");
    private By mh_inputSearch = By.cssSelector("input[type='search'][aria-controls='tblSubject']");
    private By mh_tableRows = By.cssSelector("#tblSubject tbody tr:not(.odd > td.dataTables_empty)");
    private By mh_emptyRowMsg = By.cssSelector("#tblSubject .dataTables_empty");
    private By mh_tableWrapper = By.cssSelector("#tblSubject_wrapper .table-responsive");
    private By mh_btnScrollTop = By.cssSelector(".scroll-top"); 

    private By mh_btnSuaDongDau = By.xpath("(//*[@id='tblSubject']//tbody//a[contains(@class,'editRow')])[1]");
    private By mh_popupForm = By.cssSelector(".ui-dialog");
    private By mh_inputMaMonHoc = By.id("subject_id");
    private By mh_inputTenMonHoc = By.id("name");
    private By mh_checkboxNgonNgu = By.id("is_vietnamese");
    private By mh_btnLuu = By.cssSelector("form#subject-form button[type='submit']");
    private By mh_btnHuy = By.cssSelector("form#subject-form #btnClose");
    private By mh_btnXClose = By.cssSelector(".ui-dialog-titlebar-close");

    private void xulySelect2An(By locatorSelect, String label) {
        try {
            System.out.println("  [Nguoi dung]: Chon dropdown " + label);
            WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(locatorSelect));
            ((JavascriptExecutor) driver).executeScript("arguments[0].className = arguments[0].className.replace('select2-hidden-accessible', '');", selectElement);
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", selectElement);
            Thread.sleep(500);
           
            Select select = new Select(selectElement);
            if (select.getOptions().size() > 1) {
                select.selectByIndex(1);
            }
            Thread.sleep(1500);
        } catch (Exception e) { System.out.println("  [Loi]: Khong the thao tac voi Select2 " + label); }
    }

    public void mh_chonHocKy() { xulySelect2An(mh_selectTerm, "Hoc Ky"); }
    public void mh_chonNganh() { xulySelect2An(mh_selectMajor, "Nganh"); }

    public void mh_chonSoLuongHienThi(String textValue) {
        try {
            System.out.println("  [Nguoi dung]: Chon Hien thi [" + textValue + "] du lieu");
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(mh_selectLength));
            Select select = new Select(dropdown);
            if (textValue.equalsIgnoreCase("Tất cả")) {
                select.selectByValue("-1");
            } else {
                select.selectByVisibleText(textValue);
            }
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public void mh_bamPhanTrang(String soTrang) {
        try {
            System.out.println("  [Nguoi dung]: Bam Phan trang so [" + soTrang + "]");
            WebElement pageLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@aria-controls='tblSubject' and text()='" + soTrang + "']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", pageLink);
            Thread.sleep(500);
            pageLink.click();
            Thread.sleep(1500);
        } catch (Exception e) { System.out.println("  [Loi]: Khong tim thay trang " + soTrang); }
    }

    public void mh_clickSort(String tenCot) {
        try {
            System.out.println("  [Nguoi dung]: Click mui ten sap xep cot [" + tenCot + "]");
            By thLocator = By.xpath("//table[@id='tblSubject']//th[contains(text(), '" + tenCot + "')]");
            WebElement thElement = wait.until(ExpectedConditions.elementToBeClickable(thLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", thElement);
            Thread.sleep(500);
            thElement.click();
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public void mh_nhapTimKiem(String text) {
        try {
            System.out.println("  [Nguoi dung]: Nhap tim kiem Mon hoc: '" + text + "'");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(mh_inputSearch));
            searchBox.clear();
            searchBox.sendKeys(text);
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public void mh_xoaTrangTimKiem() {
        try {
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(mh_inputSearch));
            searchBox.sendKeys(Keys.CONTROL + "a");
            searchBox.sendKeys(Keys.BACK_SPACE);
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public int mh_demSoDongDuLieu() {
        try { return driver.findElements(mh_tableRows).size(); } catch (Exception e) { return 0; }
    }

    public boolean mh_kiemTraKhongCoDuLieu() {
        try { return driver.findElement(mh_emptyRowMsg).isDisplayed(); } catch (Exception e) { return false; }
    }

    public void mh_clickSuaDongDauTien() {
        smartClick(mh_btnSuaDongDau, "Click icon Chinh Sua Mon hoc");
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(mh_popupForm)); } catch(Exception e){}
    }

    public boolean mh_kiemTraOTextBiKhoa() {
        try {
            WebElement inputMa = wait.until(ExpectedConditions.presenceOfElementLocated(mh_inputMaMonHoc));
            WebElement inputTen = driver.findElement(mh_inputTenMonHoc);
            return (inputMa.getAttribute("disabled") != null && inputTen.getAttribute("disabled") != null);
        } catch (Exception e) { return false; }
    }

    public void mh_thayDoiNgonNgu() {
        try {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(mh_checkboxNgonNgu));
            checkbox.click();
            Thread.sleep(500);
        } catch (Exception e) {}
    }

    public void mh_clickLuu() { smartClick(mh_btnLuu, "Bam nut Luu form Mon Hoc"); }
    public void mh_clickHuy() { smartClick(mh_btnHuy, "Bam nut Huy form Mon Hoc"); }
    public void mh_clickTatX() { smartClick(mh_btnXClose, "Bam nut X tat form Mon Hoc"); }

    public boolean mh_isPopupHienThi() {
        try { return driver.findElement(mh_popupForm).isDisplayed(); } catch (Exception e) { return false; }
    }

    public void mh_cuonTrangDocNhanh() {
        try { ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);"); Thread.sleep(500); } catch (Exception e) {}
    }
    public void mh_cuonTrangLenTopNhanh() {
        try { ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);"); Thread.sleep(500); } catch (Exception e) {}
    }
    public void mh_cuonTrangNgangSangPhai() {
        try { WebElement wrapper = driver.findElement(mh_tableWrapper); ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", wrapper); Thread.sleep(500); } catch (Exception e) {}
    }
    public void mh_cuonTrangNgangVeTrai() {
        try { WebElement wrapper = driver.findElement(mh_tableWrapper); ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = 0;", wrapper); Thread.sleep(500); } catch (Exception e) {}
    }
    public boolean mh_isScrollTopVisible() {
        try { return driver.findElement(mh_btnScrollTop).isDisplayed(); } catch (Exception e) { return false; }
    }
    public void mh_clickScrollTop() {
        smartClick(mh_btnScrollTop, "Click mui ten tim ve dau trang");
    }

    // =========================================================================
    // PHẦN 6: CẤP BẬC GIẢNG VIÊN (Giữ nguyên)
    // =========================================================================

    private By cbgv_selectTerm = By.id("term");
    private By cbgv_selectLength = By.name("tblLecturerRank_length");
    private By cbgv_inputSearch = By.cssSelector("input[type='search'][aria-controls='tblLecturerRank']");
    private By cbgv_tableRows = By.cssSelector("#tblLecturerRank tbody tr:not(.odd > td.dataTables_empty)");
    private By cbgv_emptyRowMsg = By.cssSelector("#tblLecturerRank .dataTables_empty");
    private By cbgv_tableWrapper = By.cssSelector("#tblLecturerRank_wrapper .table-responsive");
    private By cbgv_btnScrollTop = By.cssSelector(".scroll-top");

    private By cbgv_btnSuaTatCa = By.cssSelector(".dt-button.editAll");
    private By cbgv_btnSuaDongDau = By.xpath("(//*[@id='tblLecturerRank']//tbody//a[contains(@class,'editRow')])[1]");
    private By cbgv_popupForm = By.cssSelector(".ui-dialog");
    private By cbgv_inputMaGV = By.id("lecturer_staff_id");
    private By cbgv_inputTenGV = By.id("lecturer_full_name");
   
    private By cbgv_selectCapBac = By.id("academic_degree_rank_id");
   
    private By cbgv_btnLuu = By.cssSelector("button[type='submit']");
    private By cbgv_btnHuy = By.id("btnClose");
    private By cbgv_btnXClose = By.cssSelector(".ui-dialog-titlebar-close");
    private By cbgv_toastMessage = By.cssSelector("#toast-container .toast-message, .swal2-html-container, .invalid-feedback, .alert-danger");

    private void xulySelect2An_CapBac(By locatorSelect, int indexToSelect, String label) {
        try {
            System.out.println("  [Nguoi dung]: Chon dropdown " + label + " tai vi tri index = " + indexToSelect);
            WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(locatorSelect));
            ((JavascriptExecutor) driver).executeScript("arguments[0].className = arguments[0].className.replace('select2-hidden-accessible', '');", selectElement);
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", selectElement);
            Thread.sleep(500);
           
            Select select = new Select(selectElement);
            if (select.getOptions().size() > indexToSelect) {
                select.selectByIndex(indexToSelect);
            }
            Thread.sleep(1000);
        } catch (Exception e) { System.out.println("  [Loi]: Khong the thao tac voi Select2 " + label); }
    }

    public void cbgv_chonHocKy() { xulySelect2An_CapBac(cbgv_selectTerm, 1, "Hoc Ky"); }

    public void cbgv_chonSoLuongHienThi(String textValue) {
        try {
            System.out.println("  [Nguoi dung]: Chon Hien thi [" + textValue + "] du lieu Cấp Bậc GV");
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(cbgv_selectLength));
            Select select = new Select(dropdown);
            if (textValue.equalsIgnoreCase("Tất cả")) {
                select.selectByValue("-1");
            } else {
                select.selectByVisibleText(textValue);
            }
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public void cbgv_bamPhanTrang(String soTrang) {
        try {
            System.out.println("  [Nguoi dung]: Bam Phan trang so [" + soTrang + "]");
            WebElement pageLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@aria-controls='tblLecturerRank' and text()='" + soTrang + "']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", pageLink);
            Thread.sleep(500); 
            pageLink.click();
            Thread.sleep(1500);
        } catch (Exception e) { System.out.println("  [Loi]: Khong tim thay trang " + soTrang); }
    }

    public void cbgv_clickSort(String tenCot) {
        try {
            System.out.println("  [Nguoi dung]: Click mui ten sap xep cot [" + tenCot + "]");
            By thLocator = By.xpath("//table[@id='tblLecturerRank']//th[contains(text(), '" + tenCot + "')]");
            WebElement thElement = wait.until(ExpectedConditions.elementToBeClickable(thLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", thElement);
            Thread.sleep(500);
            thElement.click();
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("  [Loi]: Khong the click sap xep cot " + tenCot);
        }
    }

    public void cbgv_nhapTimKiem(String text) {
        try {
            System.out.println("  [Nguoi dung]: Nhap tim kiem Cap Bac GV: '" + text + "'");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(cbgv_inputSearch));
            searchBox.clear();
            searchBox.sendKeys(text);
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public void cbgv_xoaTrangTimKiem() {
        try {
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(cbgv_inputSearch));
            searchBox.sendKeys(Keys.CONTROL + "a");
            searchBox.sendKeys(Keys.BACK_SPACE);
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public int cbgv_demSoDongDuLieu() {
        try { return driver.findElements(cbgv_tableRows).size(); } catch (Exception e) { return 0; }
    }

    public boolean cbgv_kiemTraKhongCoDuLieu() {
        try { return driver.findElement(cbgv_emptyRowMsg).isDisplayed(); } catch (Exception e) { return false; }
    }

    public void cbgv_clickSuaDongDauTien() {
        smartClick(cbgv_btnSuaDongDau, "Click icon Chinh Sua dong GV dau tien");
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(cbgv_popupForm)); } catch(Exception e){}
    }

    public void cbgv_clickSuaTatCa() {
        smartClick(cbgv_btnSuaTatCa, "Click nut Sua Tat Ca Cap Bac GV");
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(cbgv_popupForm)); } catch(Exception e){}
    }

    public boolean cbgv_kiemTraOTextBiKhoa() {
        try {
            WebElement inputMa = wait.until(ExpectedConditions.presenceOfElementLocated(cbgv_inputMaGV));
            WebElement inputTen = driver.findElement(cbgv_inputTenGV);
            return (inputMa.getAttribute("disabled") != null && inputTen.getAttribute("disabled") != null);
        } catch (Exception e) { return false; }
    }

    public void cbgv_thayDoiCapBac(boolean isHopLe) {
        if(isHopLe) {
            xulySelect2An_CapBac(cbgv_selectCapBac, 2, "Cấp bậc trong Form");
        } else {
            xulySelect2An_CapBac(cbgv_selectCapBac, 0, "Cấp bậc rỗng trong Form");
        }
    }

    public void cbgv_clickLuu() { smartClick(cbgv_btnLuu, "Bam nut Luu form Cap Bac GV"); }
    public void cbgv_clickHuy() { smartClick(cbgv_btnHuy, "Bam nut Huy form Cap Bac GV"); }
    public void cbgv_clickTatX() { smartClick(cbgv_btnXClose, "Bam nut X tat form Cap Bac GV"); }

    public boolean cbgv_isPopupHienThi() {
        try { return driver.findElement(cbgv_popupForm).isDisplayed(); } catch (Exception e) { return false; }
    }

    public String cbgv_layThongBao() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(cbgv_toastMessage)).getText().toLowerCase(); } catch (Exception e) { return ""; }
    }

    public void cbgv_cuonTrangDocNhanh() {
        try { ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);"); Thread.sleep(500); } catch (Exception e) {}
    }
    public void cbgv_cuonTrangLenTopNhanh() {
        try { ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);"); Thread.sleep(500); } catch (Exception e) {}
    }
    public void cbgv_cuonTrangNgangSangPhai() {
        try { WebElement wrapper = driver.findElement(cbgv_tableWrapper); ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", wrapper); Thread.sleep(500); } catch (Exception e) {}
    }
    public void cbgv_cuonTrangNgangVeTrai() {
        try { WebElement wrapper = driver.findElement(cbgv_tableWrapper); ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = 0;", wrapper); Thread.sleep(500); } catch (Exception e) {}
    }
    public boolean cbgv_isScrollTopVisible() {
        try { return driver.findElement(cbgv_btnScrollTop).isDisplayed(); } catch (Exception e) { return false; }
    }
    public void cbgv_clickScrollTop() {
        smartClick(cbgv_btnScrollTop, "Click mui ten tim ve dau trang");
    }

    // =========================================================================
    // PHẦN 7: THÙ LAO GIẢNG VIÊN (TRÙM CUỐI) - BỔ SUNG MỚI
    // =========================================================================

    // Locators Giao diện chính Thù Lao GV
    private By tlgv_selectTerm = By.id("term");
    private By tlgv_selectLength = By.name("tblRemuneration_length");
    private By tlgv_inputSearch = By.cssSelector("input[type='search'][aria-controls='tblRemuneration']");
    private By tlgv_tableRows = By.cssSelector("#tblRemuneration tbody tr:not(.odd > td.dataTables_empty)");
    private By tlgv_emptyRowMsg = By.cssSelector("#tblRemuneration .dataTables_empty");
    private By tlgv_tableWrapper = By.cssSelector("#tblRemuneration_wrapper .table-responsive");
    private By tlgv_btnScrollTop = By.cssSelector(".scroll-top");

    // Locators Nút Export và Menu Export
    private By tlgv_btnExportMenu = By.cssSelector(".dt-button.buttons-collection");
    private By tlgv_btnExportPrint = By.cssSelector(".buttons-print");
    private By tlgv_btnExportExcel = By.cssSelector(".buttons-excel");
    private By tlgv_btnExportPDF = By.cssSelector(".buttons-pdf");
    private By tlgv_btnExportCopy = By.cssSelector(".buttons-copy");

    // ===== HÀM HỖ TRỢ XỬ LÝ DROPDOWN SELECT2 =====
    private void xulySelect2An_ThuLao(By locatorSelect, String label) {
        try {
            System.out.println("  [Nguoi dung]: Chon dropdown " + label);
            WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(locatorSelect));
            ((JavascriptExecutor) driver).executeScript("arguments[0].className = arguments[0].className.replace('select2-hidden-accessible', '');", selectElement);
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", selectElement);
            Thread.sleep(500);
           
            Select select = new Select(selectElement);
            if (select.getOptions().size() > 1) {
                select.selectByIndex(1); // Chọn học kỳ hợp lệ
            }
            Thread.sleep(1500);
        } catch (Exception e) { System.out.println("  [Loi]: Khong the thao tac voi Select2 " + label); }
    }

    // ===== CÁC HÀM TƯƠNG TÁC THÙ LAO GV =====

    public void tlgv_chonHocKy() { xulySelect2An_ThuLao(tlgv_selectTerm, "Hoc Ky Thù Lao"); }

    public void tlgv_chonSoLuongHienThi(String textValue) {
        try {
            System.out.println("  [Nguoi dung]: Chon Hien thi [" + textValue + "] du lieu Thù Lao");
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(tlgv_selectLength));
            Select select = new Select(dropdown);
            if (textValue.equalsIgnoreCase("Tất cả")) {
                select.selectByValue("-1");
            } else {
                select.selectByVisibleText(textValue);
            }
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public void tlgv_bamPhanTrang(String soTrang) {
        try {
            System.out.println("  [Nguoi dung]: Bam Phan trang so [" + soTrang + "]");
            WebElement pageLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@aria-controls='tblRemuneration' and text()='" + soTrang + "']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", pageLink);
            Thread.sleep(500);
            pageLink.click();
            Thread.sleep(1500);
        } catch (Exception e) { System.out.println("  [Loi]: Khong tim thay trang " + soTrang); }
    }

    public void tlgv_clickSort(String tenCot) {
        try {
            System.out.println("  [Nguoi dung]: Click mui ten sap xep cot [" + tenCot + "]");
            By thLocator = By.xpath("//table[@id='tblRemuneration']//th[contains(text(), '" + tenCot + "')]");
            WebElement thElement = wait.until(ExpectedConditions.elementToBeClickable(thLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", thElement);
            Thread.sleep(500);
            thElement.click();
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("  [Loi]: Khong the click sap xep cot " + tenCot);
        }
    }

    public void tlgv_nhapTimKiem(String text) {
        try {
            System.out.println("  [Nguoi dung]: Nhap tim kiem Thù Lao GV: '" + text + "'");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(tlgv_inputSearch));
            searchBox.clear();
            searchBox.sendKeys(text);
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public void tlgv_xoaTrangTimKiem() {
        try {
            System.out.println("  [Nguoi dung]: Xóa text ô tìm kiếm Thù Lao");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(tlgv_inputSearch));
            searchBox.sendKeys(Keys.CONTROL + "a");
            searchBox.sendKeys(Keys.BACK_SPACE);
            Thread.sleep(1500);
        } catch (Exception e) {}
    }

    public int tlgv_demSoDongDuLieu() {
        System.out.println("  [He thong]: Dem so dong hien thi tren bang Thù Lao");
        try { return driver.findElements(tlgv_tableRows).size(); } catch (Exception e) { return 0; }
    }

    public boolean tlgv_kiemTraKhongCoDuLieu() {
        try { return driver.findElement(tlgv_emptyRowMsg).isDisplayed(); } catch (Exception e) { return false; }
    }

    // --- HÀM XỬ LÝ EXPORT ---
    public void tlgv_moMenuExport() {
        smartClick(tlgv_btnExportMenu, "Bấm mở Menu Export (Xuất báo cáo)");
    }

    public void tlgv_xuatFile(String loaiFile) {
        By locator = null;
        switch (loaiFile.toLowerCase()) {
            case "in ấn": locator = tlgv_btnExportPrint; break;
            case "excel": locator = tlgv_btnExportExcel; break;
            case "pdf": locator = tlgv_btnExportPDF; break;
            case "sao chép": locator = tlgv_btnExportCopy; break;
        }
        if (locator != null) {
            smartClick(locator, "Bấm xuất file định dạng: " + loaiFile);
        }
    }

    public boolean tlgv_kiemTraNutExportTonTai() {
        try { return driver.findElement(tlgv_btnExportMenu).isDisplayed(); } catch (Exception e) { return false; }
    }

    // --- CÁC HÀM UI ---
    public void tlgv_cuonTrangDocNhanh() {
        try { ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);"); Thread.sleep(500); } catch (Exception e) {}
    }
    public void tlgv_cuonTrangLenTopNhanh() {
        try { ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);"); Thread.sleep(500); } catch (Exception e) {}
    }
    public void tlgv_cuonTrangNgangSangPhai() {
        try { WebElement wrapper = driver.findElement(tlgv_tableWrapper); ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", wrapper); Thread.sleep(500); } catch (Exception e) {}
    }
    public void tlgv_cuonTrangNgangVeTrai() {
        try { WebElement wrapper = driver.findElement(tlgv_tableWrapper); ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = 0;", wrapper); Thread.sleep(500); } catch (Exception e) {}
    }
    public boolean tlgv_isScrollTopVisible() {
        try { return driver.findElement(tlgv_btnScrollTop).isDisplayed(); } catch (Exception e) { return false; }
    }
    public void tlgv_clickScrollTop() {
        smartClick(tlgv_btnScrollTop, "Click mui ten tim ve dau trang Thù Lao");
    }
}