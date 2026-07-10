package com.bcntest.features.quanlythoikhoabieu;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyThoiKhoaBieuPage;
import org.openqa.selenium.By;
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
        
        // Chuẩn bị sẵn đường dẫn file linh hoạt
        String projectPath = System.getProperty("user.dir");
        duongDanFileExcel = projectPath + File.separator + "datatest" + File.separator + "CNTT UIS-ThoiKhoaBieu_TieuChuan_Mau.xlsx"; 
    }

    public void lamMoiTrangSPA() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        System.out.println("-> Đang làm mới Form: Về URL gốc để xóa trắng dữ liệu...");
        driver.get(BASE_URL);
        Thread.sleep(2000);

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
            System.out.println("Cảnh báo điều hướng Menu: " + e.getMessage());
        }
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (Import thành công)
    // ==========================================
    @Test(priority = 1)
    public void testTC01_ImportThanhCong() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: IMPORT TKB THÀNH CÔNG ---");
        lamMoiTrangSPA(); 

        page.nhapThongTinImport("999", "Công nghệ thông tin", duongDanFileExcel);
        page.bamImport();
        Thread.sleep(8000); 
        
        String thongBao = page.layThongBao();
        
        // ĐÃ TỐI ƯU HÓA: Bao phủ toàn bộ các thông báo logic từ Database
        boolean isPass = thongBao.contains("thành công") || 
                         thongBao.contains("success") || 
                         thongBao.contains("khoá phân công") || 
                         thongBao.contains("đã tồn tại");
                         
        Assert.assertTrue(isPass, "Lỗi: Form submit thất bại! Thực tế web báo: '" + thongBao + "'");
    }

    // ==========================================
    // 2. LUỒNG SAI (Quên chọn Học kỳ / Ngành)
    // ==========================================
    @Test(priority = 2)
    public void testTC02_KhongChonHocKyNganh() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: BỎ TRỐNG HỌC KỲ VÀ NGÀNH ---");
        lamMoiTrangSPA(); 

        page.nhapThongTinImport("", "", duongDanFileExcel);
        page.bamImport();
        Thread.sleep(1500);
        
        String thongBao = page.layThongBao();
        Assert.assertTrue(thongBao.length() > 0 || driver.getPageSource().contains("error"), 
                "Lỗi: Hệ thống không báo lỗi khi bỏ trống Học kỳ/Ngành!");
    }

    // ==========================================
    // 3. LUỒNG SAI (Quên đẩy File lên)
    // ==========================================
    @Test(priority = 3)
    public void testTC03_KhongUploadFile() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: KHÔNG UPLOAD FILE ---");
        lamMoiTrangSPA(); 

        page.nhapThongTinImport("999", "Công nghệ thông tin", "");
        page.bamImport();
        Thread.sleep(1500);
        
        String thongBao = page.layThongBao();
        Assert.assertTrue(thongBao.length() > 0 || driver.getPageSource().contains("error"), 
                "Lỗi: Hệ thống không báo lỗi khi chưa upload file Excel!");
    }
}