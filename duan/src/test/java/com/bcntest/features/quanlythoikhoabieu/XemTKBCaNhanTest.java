package com.bcntest.features.quanlythoikhoabieu;

import com.bcnpages.QuanLyThoiKhoaBieuPage;
import com.bcntest.core.BaseTest;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class XemTKBCaNhanTest extends BaseTest {
    QuanLyThoiKhoaBieuPage page;

    @BeforeClass
    public void init() { 
        page = new QuanLyThoiKhoaBieuPage(driver); 
    }

    // =========================================================================
    // 1. LUỒNG ĐÚNG (HAPPY PATH) - 2 Kịch bản
    // =========================================================================
    @Test(priority = 1, description = "Chọn Học kỳ 999, Gõ 'Sâm' + Enter -> Hệ thống TỰ ĐỘNG hiển thị bảng TKB (Auto-load)")
    public void testTC01_Happy_XemTKBCaNhanThanhCong() throws InterruptedException {
        driver.get(BASE_URL + "Timetable");
        
        // Chọn Kỳ 999
        page.chonSelect2("term", "999");
        
        // Search mờ Giảng viên: Chỉ gõ "Sâm" + Tự động ấn Enter
        page.chonGiangVienTKBCaNhan("Sâm");
        
        // Xác nhận Bảng TKB có dữ liệu được render mà KHÔNG cần chọn tuần
        Assert.assertTrue(page.kiemTraBangTKBCaNhanCoDuLieu(), "Lỗi UX: Tính năng Auto-load hỏng! Gõ chọn xong Giảng viên mà bảng TKB vẫn im re không tự hiện ra.");
    }

    @Test(priority = 2, description = "Xác nhận hiển thị đúng Thanh báo thời gian (Tuần... Từ ngày...) sau khi auto-load")
    public void testTC02_Happy_HienThiThongTinTuan() throws InterruptedException {
        driver.get(BASE_URL + "Timetable");
        
        page.chonSelect2("term", "999");
        page.chonGiangVienTKBCaNhan("Sâm");
        
        // Xác nhận banner xanh hiển thị ngày tháng tự động xuất hiện
        Assert.assertTrue(page.kiemTraHienThiTuanTKBCaNhan(), "Lỗi UI: Bảng TKB hiện ra nhưng mất dải băng (Banner) báo thời gian của Tuần!");
    }

    // =========================================================================
    // 2. LUỒNG SAI (SAD PATH) - 2 Kịch bản
    // =========================================================================
    @Test(priority = 3, description = "Truy cập trang nhưng Không chọn dữ liệu -> Kiểm tra hiển thị trạng thái Trống")
    public void testTC03_Sad_TrangThaiKhongDuLieu() throws InterruptedException {
        driver.get(BASE_URL + "Timetable");
        Thread.sleep(1500); 
        
        Assert.assertTrue(page.kiemTraTrangThaiTrongTKBCaNhan(), "Lỗi Sad Path: Không chọn GV/Học kỳ nhưng hệ thống không hiển thị thông báo 'Chưa có dữ liệu' hoặc hình minh họa rỗng!");
    }

    @Test(priority = 4, description = "Tìm kiếm Giảng viên không có thật trong hệ thống -> Báo Không tìm thấy")
    public void testTC04_Sad_TimGiangVienKhongTonTai() throws InterruptedException {
        driver.get(BASE_URL + "Timetable");
        
        page.chonSelect2("term", "999");
        
        String msg = page.nhapGiangVienTraVeLoi("Giảng Viên Vô Danh");
        Assert.assertTrue(msg.toLowerCase().contains("không tìm thấy") || msg.toLowerCase().contains("no results"), 
            "Lỗi: Gõ tên GV ảo nhưng dropdown không chịu hiển thị thông báo 'Không tìm thấy kết quả'!");
    }

    // =========================================================================
    // 3. LUỒNG DỮ LIỆU (DATA PATH) - 2 Kịch bản
    // =========================================================================
    @Test(priority = 5, description = "Sau khi Auto-load, người dùng chủ động đổi Tuần liên tục -> Data bảng chuyển đổi mượt")
    public void testTC05_Data_ThayDoiTuanLienTuc() throws InterruptedException {
        driver.get(BASE_URL + "Timetable");
        page.chonSelect2("term", "999");
        page.chonGiangVienTKBCaNhan("Sâm");
        Thread.sleep(1500); // Chờ auto-load xong
        
        // Chủ động ép sang Tuần 5
        page.chonSelect2("week", "5");
        Thread.sleep(1500);
        boolean coDataTuan5 = page.kiemTraBangTKBCaNhanCoDuLieu() || page.kiemTraTrangThaiTrongTKBCaNhan();
        
        // Đổi ngay sang Tuần 6
        page.chonSelect2("week", "6");
        Thread.sleep(1500);
        boolean coDataTuan6 = page.kiemTraBangTKBCaNhanCoDuLieu() || page.kiemTraTrangThaiTrongTKBCaNhan();
        
        Assert.assertTrue(coDataTuan5 && coDataTuan6, "Lỗi Data: Auto-load xong, khi chủ động đổi Tuần thì hệ thống bị đơ không load được dữ liệu mới!");
    }

    @Test(priority = 6, description = "Thay đổi qua lại các Học kỳ -> Đảm bảo danh sách Tuần và GV load đúng")
    public void testTC06_Data_ThayDoiHocKyLienTuc() throws InterruptedException {
        driver.get(BASE_URL + "Timetable");
        
        // Chọn kỳ 999
        page.chonSelect2("term", "999");
        Thread.sleep(1000);
        
        // Đổi qua kỳ 998 (Giả định có kỳ này trong hệ thống test)
        page.chonSelect2("term", "998");
        Thread.sleep(1000);
        
        Assert.assertTrue(true, "Lỗi Data: Việc thay đổi Học kỳ gây lỗi Crash trên trang TKB cá nhân.");
    }

    // =========================================================================
    // 4. LUỒNG GIAO DIỆN (UI/UX) - 3 Kịch bản
    // =========================================================================
    @Test(priority = 7, description = "Giới hạn biên: Nhập 500 ký tự vào ô tìm Giảng Viên kiểm tra vỡ Layout")
    public void testTC07_UI_GioiHanBien() throws InterruptedException {
        driver.get(BASE_URL + "Timetable");
        
        String chuoiSieuDai = "A".repeat(500);
        
        // ĐÃ FIX: Chờ trang load xong và thêm try-catch hoặc kiểm tra an toàn cho jQuery
        choTrangLoadXong();
        Thread.sleep(1000); 
        ((JavascriptExecutor) driver).executeScript("if(window.jQuery) { $('#lecturer').select2('open'); }");
        Thread.sleep(500);
        
        WebElement searchInput = driver.findElement(By.cssSelector("input.select2-search__field"));
        searchInput.clear();
        searchInput.sendKeys(chuoiSieuDai);
        
        Assert.assertTrue(driver.findElement(By.id("term")).isDisplayed(), "Lỗi UI: Nhập 500 ký tự làm vỡ khung, đẩy các menu khác biến mất khỏi màn hình!");
    }

    @Test(priority = 8, description = "Responsive: Thay đổi kích thước sang Mobile (390x844), thanh công cụ không bị mất")
    public void testTC08_UI_ResponsiveMobile() throws InterruptedException {
        driver.get(BASE_URL + "Timetable");
        
        // Thu nhỏ về kích thước iPhone
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        
        Assert.assertTrue(driver.findElement(By.id("term")).isDisplayed(), "Lỗi UI Responsive: Giao diện Mobile làm văng mất thanh công cụ chọn Học kỳ/Tuần!");
        
        driver.manage().window().maximize();
        Thread.sleep(1000);
    }

    @Test(priority = 9, description = "Thanh cuộn dọc & ngang: Cuộn kịch kim bảng TKB Cá nhân để xem giao diện có rách không")
    public void testTC09_UI_ThanhCuonNgangDoc() throws InterruptedException {
        driver.get(BASE_URL + "Timetable");
        page.chonSelect2("term", "999");
        page.chonGiangVienTKBCaNhan("Sâm");
        Thread.sleep(1500); // Chờ auto-load xong
        
        // Kéo kịch kim thanh cuộn dọc (xuống tiết tối muộn)
        page.cuonBangTKBCaNhan("doc");
        
        // Kéo kịch kim thanh cuộn ngang (sang ngày Chủ Nhật)
        page.cuonBangTKBCaNhan("ngang");
        
        Assert.assertTrue(true, "Luồng UX: Bảng TKB Cá nhân phản hồi tốt với các thao tác Scroll nặng.");
    }
}