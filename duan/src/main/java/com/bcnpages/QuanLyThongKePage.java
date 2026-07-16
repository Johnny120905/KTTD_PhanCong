package com.bcnpages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class QuanLyThongKePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators cũ
    private By txtUsername = By.xpath("//input[contains(@name, 'UserName') or contains(@id, 'UserName') or contains(@name, 'username')]");
    private By txtPassword = By.xpath("//input[contains(@name, 'Password') or contains(@id, 'Password') or contains(@name, 'password')]");
    private By btnLogin = By.xpath("//button[@type='submit']");
    private By menuThongKe = By.xpath("//*[self::a or self::span or self::div][contains(normalize-space(),'Thống kê')]");
    private By subMenuSoGioGV = By.xpath("//a[contains(@href, '/Phancong02/Statistics')]");
    private By loader = By.id("loader");
    private By checkboxCaGiang = By.id("isLesson");
    private By tabBieuDo = By.id("chart-tab");
    private By tabBangBieu = By.id("table-tab");
    private By tabChiTiet = By.id("details-tab");
    private By tableMain = By.id("tblStatistics");
    private By searchMain = By.cssSelector("#tblStatistics_filter input");
    private By infoMain = By.id("tblStatistics_info");
    private By tableDetails = By.id("tblStatisticsDetails");
    private By searchDetails = By.cssSelector("#tblStatisticsDetails_filter input");
    private By infoDetails = By.id("tblStatisticsDetails_info");
    private By alertHuongDan = By.cssSelector("div.alert-body");
    private By btnExpandRow = By.cssSelector("button.viewInfo"); 

    private By menuLichGiangDay = By.xpath("//a[@href='/Phancong02/Statistics/Timetable']");
    private By tableLichGiangDay = By.id("tblStatistics");
    private By btnLichGiang = By.cssSelector("button.class-card"); 
    private By alertTuanHoc = By.cssSelector("div.alert-body strong"); 

    private By menuGVThinhGiang = By.xpath("//a[contains(@href, '/Phancong02/Statistics/VisitingLecturer')]");
    private By btnSubmitThongKe = By.id("submit-all");
    private By inputHocKyThinhGiang = By.xpath("//input[@placeholder='---- Chọn học kỳ ----']");
    private By txtChonHocKyPlaceholder = By.xpath("//h3[contains(text(), 'Chọn học kỳ để xem thống kê')]");
    private By imgPlaceholder = By.cssSelector("#statisticsDiv img");
    private By btnChonTatCa = By.xpath("//button[contains(@class, 'filter-button') and contains(normalize-space(), 'Chọn tất cả')]");
    private By btnBoChonTatCa = By.xpath("//button[contains(@class, 'filter-button') and contains(normalize-space(), 'Bỏ chọn tất cả')]");
    private By selectHienThi = By.name("tblStatistics_length");
    private By btnExportCollection = By.cssSelector("button.buttons-collection");
    private By btnExportExcel = By.cssSelector("button.buttons-excel");
    private By btnExportPDF = By.cssSelector("button.buttons-pdf");
    private By btnExportCopy = By.cssSelector("button.buttons-copy");
    private By btnExportPrint = By.cssSelector("button.buttons-print");
    private By paginateNext = By.id("tblStatistics_next");
    private By paginatePrev = By.id("tblStatistics_previous");

    public QuanLyThongKePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void donDepPopup() { try { ((JavascriptExecutor) driver).executeScript("if(document.querySelector('.swal2-container')) { document.querySelector('.swal2-container').remove(); }"); } catch (Exception e) {} }
    public void login(String u, String p) { wait.until(ExpectedConditions.visibilityOfElementLocated(txtUsername)).sendKeys(u); wait.until(ExpectedConditions.presenceOfElementLocated(txtPassword)).sendKeys(p); wait.until(ExpectedConditions.elementToBeClickable(btnLogin)).click(); }
    public void navigateToMenu() { if (driver.getCurrentUrl().contains("Statistics")) return; donDepPopup(); wait.until(ExpectedConditions.elementToBeClickable(menuThongKe)).click(); wait.until(ExpectedConditions.elementToBeClickable(subMenuSoGioGV)).click(); }
    public void navigateToLichGiangDay() { donDepPopup(); wait.until(ExpectedConditions.elementToBeClickable(menuLichGiangDay)).click(); wait.until(ExpectedConditions.urlContains("Timetable")); }

    public void chonSelect2(String selectId, String textToSelect) {
        try {
            By select2Container = By.xpath("//span[@aria-labelledby='select2-" + selectId + "-container']");
            wait.until(ExpectedConditions.elementToBeClickable(select2Container)).click();
            By optionXpath = By.xpath("//li[contains(@class, 'select2-results__option') and contains(text(),'" + textToSelect + "')]");
            wait.until(ExpectedConditions.elementToBeClickable(optionXpath)).click();
            try { wait.until(ExpectedConditions.invisibilityOfElementLocated(loader)); } catch (Exception e) {}
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("$('#" + selectId + "').val('" + textToSelect + "').trigger('change');");
        }
    }

    public void chonMultiSelectTheoPlaceholder(String placeholder, String textToSearch) {
        try {
            By inputLocator = By.xpath("//input[@placeholder='" + placeholder + "']");
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
            input.click();
            input.sendKeys(textToSearch);
            Thread.sleep(1000); 
            input.sendKeys(Keys.ENTER);
            try { wait.until(ExpectedConditions.invisibilityOfElementLocated(loader)); } catch (Exception e) {}
        } catch (Exception e) {}
    }

    public List<WebElement> getAllLichButtons() { return driver.findElements(btnLichGiang); }
    public void clickLichByIndex(int index) { List<WebElement> buttons = getAllLichButtons(); if (index < buttons.size()) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", buttons.get(index)); } }

    public boolean isLichGiangDayDisplayed() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(tableLichGiangDay)).isDisplayed(); } catch (Exception e) { return false; } }
    public String getThongTinTuanHoc() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(alertTuanHoc)).getText(); } catch (Exception e) { return ""; } }
    public String getHeaderBangChinh() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#tblStatistics thead"))).getText(); } catch (Exception e) { return ""; } }
    public void setCaGiang(boolean check) { WebElement cb = wait.until(ExpectedConditions.presenceOfElementLocated(checkboxCaGiang)); if (cb.isSelected() != check) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cb); wait.until(ExpectedConditions.invisibilityOfElementLocated(loader)); } }
    public void clickTab(String tabName) { if (tabName.equalsIgnoreCase("BangBieu")) wait.until(ExpectedConditions.elementToBeClickable(tabBangBieu)).click(); else if (tabName.equalsIgnoreCase("ChiTiet")) wait.until(ExpectedConditions.elementToBeClickable(tabChiTiet)).click(); else if (tabName.equalsIgnoreCase("BieuDo")) wait.until(ExpectedConditions.elementToBeClickable(tabBieuDo)).click(); }
    public void clickMoRongGVThuNhat() { List<WebElement> buttons = driver.findElements(btnExpandRow); if (!buttons.isEmpty()) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", buttons.get(0)); } }
    public void waitForChildRow() { wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr.child")), ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr.shown")))); }
    
    public boolean isMainTableDisplayed() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(tableMain)).isDisplayed(); } catch (Exception e) { return false; } }
    public boolean isDetailsTableDisplayed() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(tableDetails)).isDisplayed(); } catch (Exception e) { return false; } }
    public String getTextBangChinhEmpty() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#tblStatistics td.dataTables_empty"))).getText(); } catch (Exception e) { return "Dữ liệu tồn tại"; } }
    public String getHeaderBangChiTiet() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#tblStatisticsDetails thead"))).getText(); } catch(Exception e){ return ""; } }
    public String getThongTinHienThiBangChinh() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(infoMain)).getText(); } catch(Exception e){ return ""; } }
    public String getThongTinHienThiBangChiTiet() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(infoDetails)).getText(); } catch(Exception e){ return ""; } }
    public String getTextHuongDan() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(alertHuongDan)).getText(); } catch(Exception e){ return ""; } }
    
    public void nhapTimKiemBangChinh(String k) { 
        try { WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchMain)); input.clear(); input.sendKeys(k); } catch(Exception e) { }
    }
    public void nhapTimKiemBangChiTiet(String k) { 
        try { WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchDetails)); input.clear(); input.sendKeys(k); } catch(Exception e) { }
    }

    public void navigateToGVThinhGiang() { donDepPopup(); if (!driver.getCurrentUrl().contains("Statistics")) { wait.until(ExpectedConditions.elementToBeClickable(menuThongKe)).click(); } wait.until(ExpectedConditions.elementToBeClickable(menuGVThinhGiang)).click(); wait.until(ExpectedConditions.urlContains("VisitingLecturer")); }
    public void nhapHocKyThinhGiang(String term) { try { WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputHocKyThinhGiang)); input.click(); input.sendKeys(term); Thread.sleep(1000); input.sendKeys(Keys.ENTER); input.sendKeys(Keys.ESCAPE); } catch (Exception e) {} }
    public void moDropdownHocKyThinhGiang() { try { WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputHocKyThinhGiang)); ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input); Thread.sleep(1000); } catch (Exception e) {} }
    public void clickChonTatCaHocKy() { try { moDropdownHocKyThinhGiang(); WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnChonTatCa)); ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn); Thread.sleep(1500); wait.until(ExpectedConditions.elementToBeClickable(inputHocKyThinhGiang)).sendKeys(Keys.ESCAPE); } catch (Exception e) {} }
    public void clickBoChonTatCaHocKy() { try { moDropdownHocKyThinhGiang(); WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(btnBoChonTatCa)); ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn); Thread.sleep(1000); wait.until(ExpectedConditions.elementToBeClickable(inputHocKyThinhGiang)).sendKeys(Keys.ESCAPE); } catch (Exception e) {} }
    public void clickNutThongKe() { try { WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnSubmitThongKe)); btn.click(); try { wait.until(ExpectedConditions.invisibilityOfElementLocated(loader)); } catch (Exception e) {} } catch (Exception e) {} }
    public void chonHienThiSoLuong(String value) { try { WebElement selectElement = wait.until(ExpectedConditions.elementToBeClickable(selectHienThi)); Select select = new Select(selectElement); select.selectByValue(value); wait.until(ExpectedConditions.invisibilityOfElementLocated(loader)); } catch (Exception e) {} }
    
    public void clickExportData(String type) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnExportCollection)).click();
            Thread.sleep(500);
            if (type.equalsIgnoreCase("Excel")) { wait.until(ExpectedConditions.elementToBeClickable(btnExportExcel)).click(); } 
            else if (type.equalsIgnoreCase("PDF")) { wait.until(ExpectedConditions.elementToBeClickable(btnExportPDF)).click(); } 
            else if (type.equalsIgnoreCase("Copy")) { wait.until(ExpectedConditions.elementToBeClickable(btnExportCopy)).click(); } 
            else if (type.equalsIgnoreCase("Print")) { wait.until(ExpectedConditions.elementToBeClickable(btnExportPrint)).click(); }
        } catch (Exception e) {}
    }

    public boolean isChonHocKyPlaceholderDisplayed() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(txtChonHocKyPlaceholder)).isDisplayed(); } catch (Exception e) { return false; } }
    public boolean isThongKeButtonDisplayed() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(btnSubmitThongKe)).isDisplayed(); } catch (Exception e) { return false; } }
    public boolean isPlaceholderImageDisplayed() { try { return wait.until(ExpectedConditions.visibilityOfElementLocated(imgPlaceholder)).isDisplayed(); } catch (Exception e) { return false; } }

    public void clickSortColumn(String columnName) {
        try {
            By colXPath = By.xpath("//table[@id='tblStatistics']//th[contains(normalize-space(), '" + columnName + "')]");
            WebElement thElement = wait.until(ExpectedConditions.presenceOfElementLocated(colXPath));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", thElement);
            Thread.sleep(500); 
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", thElement);
        } catch (Exception e) {}
    }

    public String waitForTrangThaiSapXep(String columnName, String expectedState) {
        By colXPath = By.xpath("//table[@id='tblStatistics']//th[contains(normalize-space(), '" + columnName + "')]");
        String classCanTim = expectedState.equals("TANG_DAN") ? "sorting_asc" : "sorting_desc";
        try {
            wait.until(ExpectedConditions.attributeContains(colXPath, "class", classCanTim));
            return expectedState; 
        } catch (TimeoutException e) { return "MAC_DINH"; }
    }

    public void clickTrangTiepTheo() { try { wait.until(ExpectedConditions.elementToBeClickable(paginateNext)).click(); } catch (Exception e) {} }
    public void clickTrangTruocDo() { try { wait.until(ExpectedConditions.elementToBeClickable(paginatePrev)).click(); } catch (Exception e) {} }
    public void clickTrangSo(String pageNumber) { try { By pageXPath = By.xpath("//div[@id='tblStatistics_paginate']//a[text()='" + pageNumber + "']"); wait.until(ExpectedConditions.elementToBeClickable(pageXPath)).click(); } catch (Exception e) {} }

    // =========================================================================
    // LOCATORS & METHODS ĐÃ TỐI ƯU - SỐ GIỜ CÁ NHÂN (6.10, 6.11, 6.12, 6.13)
    // =========================================================================
    private By menuSoGioCaNhan = By.xpath("//a[contains(@href, '/Phancong02/Statistics/Personal') or contains(normalize-space(), 'Số giờ cá nhân')]");
    private By txtChuaCoDuLieu = By.xpath("//h4[contains(text(), 'Chưa có dữ liệu')]");
    private By chartCanvas = By.tagName("canvas");
    
    // Locator tĩnh cho 2 ô Select2 (Cấu trúc HTML an toàn nhất)
    private By selectThongKeTheo = By.xpath("(//span[contains(@class, 'select2-selection--single')])[1]");
    private By selectGiaTriThoiGian = By.xpath("//div[@id='termDiv' or @id='yearDiv'][not(contains(@style, 'none'))]//span[contains(@class, 'select2-selection--single')]");
    
    // *** QUAN TRỌNG: Chỉ tìm input ĐANG HIỂN THỊ ***
    private By inputSearchSelect2 = By.cssSelector("input.select2-search__field");
    private By cbxXemTheoCaGiang = By.xpath("//input[@type='checkbox' and following-sibling::label[contains(text(), 'Xem theo ca giảng')]]");

    public void navigateToSoGioCaNhan() {
        donDepPopup();
        if (!driver.getCurrentUrl().contains("Statistics")) { wait.until(ExpectedConditions.elementToBeClickable(menuThongKe)).click(); }
        wait.until(ExpectedConditions.elementToBeClickable(menuSoGioCaNhan)).click();
        wait.until(ExpectedConditions.urlContains("Personal"));
        try { Thread.sleep(1000); } catch(Exception e){}
    }

    public boolean isChuaCoDuLieuDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(txtChuaCoDuLieu)).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public String getTextBieuDoSummary() {
        try {
            WebElement chartArea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("chart-tab-pane")));
            return chartArea.getText();
        } catch (Exception e) { return ""; }
    }

    public boolean isChartCaNhanDisplayed() {
        try { return wait.until(ExpectedConditions.visibilityOfElementLocated(chartCanvas)).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public boolean kiemTraCotTonTai(String columnName) {
        try { return getHeaderBangChinh().contains(columnName); } catch (Exception e) { return false; }
    }

    // --- CÁC HÀM XỬ LÝ DỮ LIỆU ĐỘNG ĐÃ FIX ---

    // Hàm click an toàn: Thử click thường, nếu vướng animation thì dùng JS click
    private void clickSafe(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            el.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    public void chonLoaiThongKeCaNhan(String loai) {
        try {
            clickSafe(selectThongKeTheo);
            Thread.sleep(500);
            // DÙNG VISIBILITY ĐỂ KHÔNG TÓM NHẦM Ô ẨN
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputSearchSelect2));
            input.sendKeys(loai);
            Thread.sleep(500);
            input.sendKeys(Keys.ENTER);
            try { wait.until(ExpectedConditions.invisibilityOfElementLocated(loader)); } catch (Exception e) {}
        } catch (Exception e) { System.out.println("Lỗi chọn loại thống kê: " + e.getMessage()); }
    }

    public void chonGiaTriThoiGian(String giaTri) {
        try {
            clickSafe(selectGiaTriThoiGian);
            Thread.sleep(500);
            // DÙNG VISIBILITY ĐỂ KHÔNG TÓM NHẦM Ô ẨN
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputSearchSelect2));
            input.sendKeys(giaTri);
            Thread.sleep(500);
            input.sendKeys(Keys.ENTER);
            try { wait.until(ExpectedConditions.invisibilityOfElementLocated(loader)); } catch (Exception e) {}
        } catch (Exception e) { System.out.println("Lỗi chọn giá trị thời gian: " + e.getMessage()); }
    }

    public void toggleXemTheoCaGiang(boolean isCheck) {
        try {
            WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(cbxXemTheoCaGiang));
            if (checkbox.isSelected() != isCheck) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
                Thread.sleep(1500); 
            }
        } catch (Exception e) { System.out.println("Lỗi click Checkbox Ca Giảng: " + e.getMessage());}
    }

    public boolean kiemTraSelect2BaoLoi(String giaTriAo) {
        try {
            clickSafe(selectGiaTriThoiGian);
            Thread.sleep(500);
            
            // DÙNG VISIBILITY ĐỂ KHÔNG TÓM NHẦM Ô ẨN
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputSearchSelect2));
            input.sendKeys(giaTriAo);
            Thread.sleep(1000);
            
            boolean isLoi = !driver.findElements(By.cssSelector("li.select2-results__message")).isEmpty();
            input.sendKeys(Keys.ESCAPE); 
            return isLoi;
        } catch (Exception e) {
            System.out.println("Debug - Lỗi trong kiemTraSelect2BaoLoi: " + e.getMessage());
            return false;
        }
    }
}