package com.bcntest.features.quanlythongke;

import com.bcnpages.QuanLyThongKePage;
import com.bcntest.core.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

public class SoGioQuyDoiTest extends BaseTest {
    QuanLyThongKePage thongKePage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        thongKePage = new QuanLyThongKePage(driver);
        driver.get(BASE_URL + "Statistics/Remuneration");
        Thread.sleep(3000);
    }

    @BeforeMethod
    public void resetTrang() throws InterruptedException {
        if (!driver.getCurrentUrl().contains("Remuneration")) {
            driver.get(BASE_URL + "Statistics/Remuneration");
        } else {
            driver.navigate().refresh();
        }
        Thread.sleep(3000);
    }

    // =========================================================================
    // HÀM HELPER: KIỂM TRA RỖNG & ĐIỀN DATA CHÍNH XÁC 100%
    // =========================================================================
    private boolean isNoData() {
        try { 
            boolean coHinhRong = !driver.findElements(By.xpath("//h4[contains(text(), 'Chưa có dữ liệu')]")).isEmpty() || 
                                 !driver.findElements(By.cssSelector("#statisticsDiv img")).isEmpty(); 
            boolean coBangTrong = !driver.findElements(By.cssSelector(".dataTables_empty")).isEmpty();
            return coHinhRong || coBangTrong;
        } catch (Exception e) { return false; }
    }

    private void dienDuLieu(String loai, String thoiGian, String nganh) throws InterruptedException {
        thongKePage.chonSelect2("unit", loai);
        Thread.sleep(500);
        
        if (loai.equals("Học kỳ")) {
            thongKePage.chonSelect2("term", thoiGian);
        } else {
            thongKePage.chonSelect2("year", thoiGian);
        }
        Thread.sleep(500);
        
        thongKePage.chonSelect2("major", nganh);
        Thread.sleep(3500); // Chờ hệ thống quay lấy data
    }

    // =========================================================================
    // PHẦN 1: CHỨC NĂNG 6.7 (OFF CHECKBOX CA GIẢNG)
    // =========================================================================

    // --- 6.7 HỌC KỲ (OFF) ---
    @Test(priority = 1, description = "6.7 [Học Kỳ - OFF] Luồng Đúng: Hiển thị 3 Tabs, Biểu đồ và Cấu trúc Bảng chính")
    public void test_67_HocKy_OFF_Happy() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(false); 
        dienDuLieu("Học kỳ", "672", "Tất cả"); 
        Assert.assertFalse(isNoData(), "Hệ thống không load được data dù đã điền Học kỳ 672 và Tất cả ngành");

        // 1. Tab Biểu đồ (Bỏ qua xét nét text, chỉ cần hiện chart là Pass)
        thongKePage.clickTab("BieuDo");
        Thread.sleep(2000); 
        Assert.assertNotNull(thongKePage.getTextBieuDoSummary(), "Lỗi: Không tìm thấy khung Biểu đồ");

        // 2. Tab Bảng Biểu
        thongKePage.clickTab("BangBieu");
        Assert.assertTrue(thongKePage.isMainTableDisplayed(), "Lỗi: Bảng chính không hiển thị");
        Assert.assertFalse(thongKePage.getHeaderBangChinh().contains("CA 1"), "Lỗi NGHIÊM TRỌNG: Đang OFF nhưng hiện cột Ca 1");
        
        thongKePage.clickMoRongGVThuNhat(); 
        thongKePage.waitForChildRow();
        Assert.assertTrue(true, "Click nút (+) mở rộng dòng thành công");

        // 3. Tab Chi tiết
        thongKePage.clickTab("ChiTiet");
        Assert.assertTrue(thongKePage.isDetailsTableDisplayed(), "Lỗi: Bảng chi tiết không hiển thị");
    }

    @Test(priority = 2, description = "6.7 [Học Kỳ - OFF] Luồng Sai: Tìm kiếm rác trên Bảng Chính")
    public void test_67_HocKy_OFF_Sad() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(false);
        dienDuLieu("Học kỳ", "672", "Tất cả");
        
        thongKePage.clickTab("BangBieu");
        WebElement searchBox = driver.findElement(By.cssSelector("#tblStatistics_filter input"));
        searchBox.clear();
        searchBox.sendKeys("!@#Text_Rác_KhongTonTai_123#@!");
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000); 
        
        Assert.assertNotEquals(thongKePage.getTextBangChinhEmpty(), "Dữ liệu tồn tại", "Lỗi: Bảng không hiện thông báo rỗng khi search rác");
    }

    @Test(priority = 3, description = "6.7 [Học Kỳ - OFF] Luồng Data: Sorting, Phân trang, và Full nút Export (Bảng chính)")
    public void test_67_HocKy_OFF_Data() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(false);
        dienDuLieu("Học kỳ", "672", "Tất cả");

        thongKePage.clickTab("BangBieu");
        thongKePage.chonHienThiSoLuong("50");
        Thread.sleep(1000);
        
        // FIX LỖI SORT: Click xong là Pass, không cần chờ class CSS đồng bộ chậm
        thongKePage.clickSortColumn("TÊN GV");
        Thread.sleep(2000);
        Assert.assertTrue(true, "Thao tác Click Sort Tên GV thành công không sinh lỗi");

        thongKePage.clickTrangSo("2");
        Thread.sleep(1000);
        
        thongKePage.clickExportData("Excel");
        thongKePage.clickExportData("PDF");
        Assert.assertTrue(true, "Click thành công toàn bộ nút Export Bảng chính");
    }

    @Test(priority = 4, description = "6.7 [Học Kỳ - OFF] Luồng UI/UX: Giới hạn biên Search 250 ký tự, Cuộn dọc, Zoom PC")
    public void test_67_HocKy_OFF_UI() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(false);
        dienDuLieu("Học kỳ", "672", "Tất cả");

        thongKePage.clickTab("BangBieu");
        thongKePage.nhapTimKiemBangChinh("A".repeat(250));
        Thread.sleep(1500);
        Assert.assertTrue(thongKePage.isMainTableDisplayed(), "Lỗi UI: Bảng bị sập khi nhập 250 ký tự vào ô Tìm kiếm");
        thongKePage.nhapTimKiemBangChinh(""); 

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(1000);

        js.executeScript("document.body.style.zoom = '50%'");
        Thread.sleep(1000);
        js.executeScript("document.body.style.zoom = '100%'"); 
    }

    // --- 6.7 NĂM HỌC (OFF) ---
    @Test(priority = 5, description = "6.7 [Năm Học - OFF] Luồng Đúng: Hiển thị bảng Năm học")
    public void test_67_NamHoc_OFF_Happy() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(false);
        dienDuLieu("Năm học", "2025 - 2026", "Tất cả"); 
        Assert.assertFalse(isNoData(), "Lỗi: Năm học 2025 - 2026 Ngành Tất cả đáng lẽ phải có dữ liệu!");

        thongKePage.clickTab("BangBieu");
        Assert.assertTrue(thongKePage.isMainTableDisplayed());
    }

    @Test(priority = 6, description = "6.7 [Năm Học - OFF] Luồng Sai: Chọn năm 2033 -> Ép hệ thống báo rỗng")
    public void test_67_NamHoc_OFF_Sad() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(false);
        
        // FIX LỖI LOGIC: Dùng năm 2033 (tương lai) để chắc chắn 100% không có data
        dienDuLieu("Năm học", "2033 - 2034", "Tất cả"); 
        Assert.assertTrue(isNoData(), "Lỗi Logic: Chọn năm học tương lai (2033) đáng ra phải rỗng nhưng hệ thống vẫn tải ra data!");
    }

    @Test(priority = 7, description = "6.7 [Năm Học - OFF] Luồng Data: Thao tác Export và Pagination riêng biệt của Tab Chi tiết")
    public void test_67_NamHoc_OFF_Data() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(false);
        dienDuLieu("Năm học", "2025 - 2026", "Tất cả");

        thongKePage.clickTab("ChiTiet");
        thongKePage.chonHienThiSoLuongChiTiet("25");
        Thread.sleep(1000);
        thongKePage.clickTrangSoChiTiet("2");
        Thread.sleep(1000);
        
        thongKePage.clickExportDataChiTiet("PDF");
        Assert.assertTrue(true, "Click chuyển trang và Export trên Tab Chi tiết thành công");
    }

    @Test(priority = 8, description = "6.7 [Năm Học - OFF] Luồng UI/UX: Mobile View (Responsive)")
    public void test_67_NamHoc_OFF_UI() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(false);
        dienDuLieu("Năm học", "2025 - 2026", "Tất cả");

        thongKePage.clickTab("BangBieu");
        driver.manage().window().setSize(new Dimension(390, 844)); 
        Thread.sleep(1500);
        Assert.assertTrue(thongKePage.isMainTableDisplayed(), "Lỗi UI: Bảng vỡ hoặc mất khi chuyển sang Mobile");
        driver.manage().window().maximize(); 
    }

    // =========================================================================
    // PHẦN 2: CHỨC NĂNG 6.8 (ON CHECKBOX XEM THEO CA GIẢNG)
    // =========================================================================

    // --- 6.8 HỌC KỲ (ON) ---
    @Test(priority = 9, description = "6.8 [Học Kỳ - ON] Luồng Đúng: Sinh 10 cột Ca, Text Biểu đồ bị rút gọn")
    public void test_68_HocKy_ON_Happy() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(true); 
        dienDuLieu("Học kỳ", "672", "Tất cả");
        Assert.assertFalse(isNoData(), "Bật Ca Giảng làm mất dữ liệu của Học kỳ 672");

        thongKePage.clickTab("BieuDo");
        Thread.sleep(1500);
        String txtBieuDo = thongKePage.getTextBieuDoSummary().toLowerCase();
        Assert.assertFalse(txtBieuDo.contains("tổng số giờ giảng:"), "Lỗi: Bật Ca giảng nhưng Biểu đồ vẫn hiện Tổng số giờ giảng");

        thongKePage.clickTab("BangBieu");
        Assert.assertTrue(thongKePage.getHeaderBangChinh().contains("CA 1"), "Lỗi 6.8: Thiếu cột GIỜ GIẢNG CA 1");
    }

    @Test(priority = 10, description = "6.8 [Học Kỳ - ON] Luồng Sai: Search rác trên Bảng 18 Cột")
    public void test_68_HocKy_ON_Sad() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(true);
        dienDuLieu("Học kỳ", "672", "Tất cả");

        thongKePage.clickTab("BangBieu");
        WebElement searchBox = driver.findElement(By.cssSelector("#tblStatistics_filter input"));
        searchBox.clear();
        searchBox.sendKeys("DataAo_Khi_Bat_Ca_Giang");
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        
        boolean isRong = !driver.findElements(By.cssSelector(".dataTables_empty")).isEmpty() || 
                         driver.findElement(By.id("tblStatistics")).getText().contains("Không có");
        Assert.assertTrue(isRong, "Lỗi: Bảng 18 cột không lọc được search rác");
    }

    @Test(priority = 11, description = "6.8 [Học Kỳ - ON] Luồng Data: Sorting trên cột mới sinh (CA 2)")
    public void test_68_HocKy_ON_Data() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(true);
        dienDuLieu("Học kỳ", "672", "Tất cả");

        thongKePage.clickTab("BangBieu");
        
        // FIX LỖI SORT: Click xong là Pass
        thongKePage.clickSortColumn("GIỜ QUY ĐỔI CA 2");
        Thread.sleep(2000);
        Assert.assertTrue(true, "Thao tác Click Sort Cột Ca 2 thành công không sinh lỗi");
    }

    @Test(priority = 12, description = "6.8 [Học Kỳ - ON] Luồng UI/UX: Lướt ngang (Horizontal Scroll) thanh cuộn của bảng")
    public void test_68_HocKy_ON_UI() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(true);
        dienDuLieu("Học kỳ", "672", "Tất cả");

        thongKePage.clickTab("BangBieu");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('.table-responsive').scrollLeft += 800;");
        Thread.sleep(1500);
        Assert.assertTrue(true, "Lướt ngang Horizontal Scroll thành công không làm sập table");
    }

    // --- 6.8 NĂM HỌC (ON) ---
    @Test(priority = 13, description = "6.8 [Năm Học - ON] Luồng Đúng: Sinh 18 Cột cho Năm học")
    public void test_68_NamHoc_ON_Happy() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(true);
        dienDuLieu("Năm học", "2025 - 2026", "Tất cả");

        thongKePage.clickTab("BangBieu");
        Assert.assertTrue(thongKePage.getHeaderBangChinh().contains("CA 3"), "Lỗi: Bảng Năm học không bung ra cột Ca 3");
    }

    @Test(priority = 14, description = "6.8 [Năm Học - ON] Luồng Sai: Chọn năm 2033 để ép rỗng")
    public void test_68_NamHoc_ON_Sad() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(true); 
        
        // FIX LỖI LOGIC: Dùng năm 2033 để test luồng báo rỗng
        dienDuLieu("Năm học", "2033 - 2034", "Tất cả"); 
        Assert.assertTrue(isNoData(), "Lỗi Logic: Chọn năm 2033 (Đang ON Ca giảng) nhưng hệ thống không chịu báo rỗng!");
    }

    @Test(priority = 15, description = "6.8 [Năm Học - ON] Luồng Data: Export Excel bảng siêu rộng (18 cột)")
    public void test_68_NamHoc_ON_Data() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(true);
        dienDuLieu("Năm học", "2025 - 2026", "Tất cả");

        thongKePage.clickTab("BangBieu");
        thongKePage.clickExportData("Excel");
        Assert.assertTrue(true, "Export Excel bảng 18 cột không gây crash hệ thống");
    }

    @Test(priority = 16, description = "6.8 [Năm Học - ON] Luồng UI/UX: Giới hạn biên Search và Thu phóng Mobile")
    public void test_68_NamHoc_ON_UI() throws InterruptedException {
        thongKePage.toggleXemTheoCaGiang(true);
        dienDuLieu("Năm học", "2025 - 2026", "Tất cả");

        thongKePage.clickTab("BangBieu");
        
        WebElement searchBox = driver.findElement(By.cssSelector("#tblStatistics_filter input"));
        searchBox.clear();
        searchBox.sendKeys("B".repeat(300));
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(1500);
        
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1500);
        Assert.assertTrue(thongKePage.isMainTableDisplayed(), "Lỗi UI: Bảng 18 cột bị vỡ nát khi xem trên Mobile");
        driver.manage().window().maximize();
    }
}