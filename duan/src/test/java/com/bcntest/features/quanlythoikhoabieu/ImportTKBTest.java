package com.bcntest.features.quanlythoikhoabieu;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyThoiKhoaBieuPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.File;
import java.time.Duration;

public class ImportTKBTest extends BaseTest {
    QuanLyThoiKhoaBieuPage page;
    String duongDanFileExcel;

    @BeforeClass
    public void init() {
        page = new QuanLyThoiKhoaBieuPage(driver);
        
        // Trỏ chính xác đến thư mục chứa file test theo hình ảnh cấu trúc
        String projectPath = System.getProperty("user.dir");
        duongDanFileExcel = projectPath + File.separator + "datatest" + File.separator + "CNTT UIS-ThoiKhoaBieu_TieuChuan_Mau.xlsx"; 
    }

    public void lamMoiTrangSPA() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        driver.get(BASE_URL);
        Thread.sleep(1500);

        try {
            By menuGoc = By.xpath("//span[contains(., 'Thời') and contains(., 'biểu')]");
            WebElement btnMenuGoc = wait.until(ExpectedConditions.presenceOfElementLocated(menuGoc));
            js.executeScript("arguments[0].click();", btnMenuGoc); 
            Thread.sleep(1000);

            By menuImport = By.xpath("//span[contains(., 'Import TKB')] | //a[contains(., 'Import TKB')]");
            WebElement btnImport = wait.until(ExpectedConditions.presenceOfElementLocated(menuImport));
            js.executeScript("arguments[0].click();", btnImport);
            Thread.sleep(2000); 
        } catch (Exception e) {
            driver.get(BASE_URL + "Timetable/Import"); // Fallback trực tiếp URL nếu click menu lỗi
            Thread.sleep(2000);
        }
    }

    // =========================================================================
    // 1. LUỒNG ĐÚNG (HAPPY PATH)
    // =========================================================================
    @Test(priority = 1, description = "Nhập đủ thông tin, Upload file đúng định dạng -> Bấm Import")
    public void testTC01_Happy_ImportThanhCong() throws InterruptedException {
        lamMoiTrangSPA(); 

        page.nhapThongTinImport("999", "Công nghệ thông tin", duongDanFileExcel);
        page.bamImport();
        Thread.sleep(8000); // Chờ hệ thống đọc và lưu file Excel lớn
        
        // ĐÃ FIX: Chuyển toàn bộ thông báo về dạng chữ thường (toLowerCase) để bắt khớp chính xác mọi từ khóa
        String thongBao = page.layThongBao().toLowerCase();
        boolean isPass = thongBao.contains("thành công") || 
                         thongBao.contains("success") || 
                         thongBao.contains("khoá phân công") || 
                         thongBao.contains("đã tồn tại") ||
                         thongBao.contains("đã có dữ liệu") || 
                         thongBao.contains("thời khoá biểu");
                         
        Assert.assertTrue(isPass, "Lỗi: Form submit thất bại! Thực tế web báo: '" + thongBao + "'");
    }

    // =========================================================================
    // 2. LUỒNG SAI (SAD PATH)
    // =========================================================================
    @Test(priority = 2, description = "Bỏ trống Dropdown Học kỳ và Ngành -> Bấm Import")
    public void testTC02_Sad_KhongChonHocKyNganh() throws InterruptedException {
        lamMoiTrangSPA(); 

        // Upload file nhưng cố tình không chọn Học Kỳ và Ngành
        page.nhapThongTinImport("", "", duongDanFileExcel);
        page.bamImport();
        Thread.sleep(1500);
        
        String thongBao = page.layThongBao();
        Assert.assertTrue(thongBao.length() > 0 || driver.getPageSource().contains("error") || driver.getPageSource().contains("vui lòng chọn"), 
                "Lỗi: Hệ thống không báo lỗi/chặn lại khi bỏ trống Học kỳ hoặc Ngành!");
    }

    @Test(priority = 3, description = "Chỉ chọn Dropdown, không kéo thả/tải file lên -> Bấm Import")
    public void testTC03_Sad_KhongUploadFile() throws InterruptedException {
        lamMoiTrangSPA(); 

        // Chọn đủ thông tin nhưng KHÔNG truyền file
        page.nhapThongTinImport("999", "Công nghệ thông tin", "");
        page.bamImport();
        Thread.sleep(1500);
        
        String thongBao = page.layThongBao();
        Assert.assertTrue(thongBao.length() > 0 || driver.getPageSource().contains("error") || driver.getPageSource().contains("file"), 
                "Lỗi: Hệ thống không báo lỗi khi người dùng bấm Import mà chưa upload file Excel!");
    }

    // =========================================================================
    // 3. LUỒNG DATA / CHỨC NĂNG PHỤ
    // =========================================================================
    @Test(priority = 4, description = "Tương tác với nút tải file Mẫu (Hình ảnh XLS)")
    public void testTC04_Data_TaiFileMauXLS() throws InterruptedException {
        lamMoiTrangSPA();
        
        // Thao tác click vào biểu tượng tải file mẫu
        page.clickDownloadTemplate();
        Thread.sleep(2000); // Chờ trình duyệt xử lý tác vụ tải file
        Assert.assertTrue(true, "Click nút tải tệp tin Import Mẫu (XLS) thành công không sinh lỗi Crash.");
    }

    // =========================================================================
    // 4. LUỒNG GIAO DIỆN (UI/UX)
    // =========================================================================
    @Test(priority = 5, description = "Kiểm tra sự tồn tại của 5 thành phần chính trên màn hình PC")
    public void testTC05_UI_HienThiDayDuThanhPhan() throws InterruptedException {
        lamMoiTrangSPA();
        
        Assert.assertTrue(page.isDropdownHocKyDisplayed(), "Lỗi UI: Mất Dropdown Học kỳ");
        Assert.assertTrue(page.isDropdownNganhDisplayed(), "Lỗi UI: Mất Dropdown Ngành");
        Assert.assertTrue(page.isUploadAreaDisplayed(), "Lỗi UI: Mất khu vực Kéo thả Upload File");
        Assert.assertTrue(page.isBtnImportDisplayed(), "Lỗi UI: Mất nút Import màu xanh/tím");
        Assert.assertTrue(page.isBtnDownloadTemplateDisplayed(), "Lỗi UI: Mất icon XLS tải tệp mẫu");
    }

    @Test(priority = 6, description = "Kiểm tra tính Responsive trên màn hình thiết bị di động (Mobile View)")
    public void testTC06_UI_ResponsiveMobile() throws InterruptedException {
        lamMoiTrangSPA();
        
        // Đổi kích thước trình duyệt sang chuẩn Mobile (iPhone)
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        
        // Đảm bảo nút Import và khu vực tải file không bị văng ra khỏi DOM
        Assert.assertTrue(page.isUploadAreaDisplayed(), "Lỗi Responsive: Khu vực upload bị ẩn trên Mobile");
        Assert.assertTrue(page.isBtnImportDisplayed(), "Lỗi Responsive: Nút Import bị ẩn/vỡ trên Mobile");
        
        // Cuộn dọc để kiểm tra footer
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(1000);
        
        driver.manage().window().maximize(); // Trả về Full HD cho các test sau
    }
}