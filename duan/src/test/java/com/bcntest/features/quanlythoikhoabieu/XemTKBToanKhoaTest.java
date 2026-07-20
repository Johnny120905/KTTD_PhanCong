package com.bcntest.features.quanlythoikhoabieu;

import com.bcnpages.QuanLyThoiKhoaBieuPage;
import com.bcntest.core.BaseTest;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class XemTKBToanKhoaTest extends BaseTest {
    QuanLyThoiKhoaBieuPage page;

    @BeforeClass
    public void init() { 
        page = new QuanLyThoiKhoaBieuPage(driver); 
    }

    // =========================================================================
    // 1. LUỒNG ĐÚNG (HAPPY PATH) - 2 Kịch bản
    // =========================================================================
    @Test(priority = 1, description = "Chọn Học Kỳ và Ngành -> Bảng TKB hiển thị dữ liệu thành công")
    public void testTC01_Happy_XemTKBThanhCong() throws InterruptedException {
        driver.get(BASE_URL + "Timetable/Assign");
        
        page.chonHocKyVaNganh("999", "Công nghệ thông tincong nghe");
        page.waitForTableLoad();
        
        Assert.assertTrue(page.kiemTraBangTKBCoDuLieu(), "Lỗi: Bảng TKB trống không, không có dữ liệu sau khi chọn đủ Học kỳ & Ngành!");
    }

    @Test(priority = 2, description = "Sử dụng tính năng Lọc Môn học và Lọc Giảng viên để thu hẹp kết quả")
    public void testTC02_Happy_LocDuLieuThanhCong() throws InterruptedException {
        driver.get(BASE_URL + "Timetable/Assign");
        page.chonHocKyVaNganh("999", "Công nghệ thông tincong nghe");
        page.waitForTableLoad();
        
        page.locDuLieuToanKhoa("subjectFilter", "Hệ Quản trị Cơ sở dữ liệu");
        page.locDuLieuToanKhoa("lecturerFilter", "Nguyễn Cao Sâm");
        
        Assert.assertTrue(page.kiemTraBangTKBCoDuLieu(), "Lỗi: Sau khi áp dụng bộ lọc hợp lệ, bảng lại không hiển thị kết quả nào!");
    }

    // =========================================================================
    // 2. LUỒNG SAI (SAD PATH) - 2 Kịch bản
    // =========================================================================
    @Test(priority = 3, description = "Không chọn Học kỳ/Ngành (Mặc định) -> Bảng TKB phải trống")
    public void testTC03_Sad_KhongChonHocKyNganh() throws InterruptedException {
        driver.get(BASE_URL + "Timetable/Assign");
        
        // FIX LỖI CACHE: Ép hệ thống chọn về dòng mặc định để xóa data cũ đang bị lưu cache/session
        page.chonSelect2("term", "---- Chọn học kỳ ----");
        Thread.sleep(2000);
        
        Assert.assertFalse(page.kiemTraBangTKBCoDuLieu(), "Lỗi Sad Path: Đã đưa về trạng thái chưa chọn Học kỳ/Ngành mà bảng TKB vẫn hiện dữ liệu!");
    }

    @Test(priority = 4, description = "Nhập dữ liệu bậy bạ vào ô Lọc môn học -> Trả về Không tìm thấy")
    public void testTC04_Sad_LocSaiDuLieu() throws InterruptedException {
        driver.get(BASE_URL + "Timetable/Assign");
        page.chonHocKyVaNganh("999", "Công nghệ thông tincong nghe");
        
        String thongBaoLoi = page.layThongBaoLoiLocToanKhoa("subjectFilter", "MON_HOC_AOO_123");
        
        Assert.assertTrue(thongBaoLoi.toLowerCase().contains("no results found") || thongBaoLoi.toLowerCase().contains("không tìm thấy"), 
            "Lỗi: Tìm môn học ảo nhưng hệ thống không báo lỗi rỗng!");
    }

    // =========================================================================
    // 3. LUỒNG DỮ LIỆU (DATA PATH) - 2 Kịch bản
    // =========================================================================
    @Test(priority = 5, description = "Thay đổi liên tục các Ngành khác nhau -> Dữ liệu bảng cập nhật linh hoạt")
    public void testTC05_Data_ChuyenDoiNganhLienTuc() throws InterruptedException {
        driver.get(BASE_URL + "Timetable/Assign");
        
        page.chonHocKyVaNganh("999", "Công nghệ thông tincong nghe");
        page.waitForTableLoad();
        boolean coDataLan1 = page.kiemTraBangTKBCoDuLieu();
        
        page.chonHocKyVaNganh("", "Kỹ thuật phần mềm"); // Chuyển ngành mới
        page.waitForTableLoad();
        boolean coDataLan2 = page.kiemTraBangTKBCoDuLieu();
        
        Assert.assertTrue(coDataLan1 || coDataLan2, "Lỗi Data: Load dữ liệu động cho bảng TKB khi chuyển Ngành gặp lỗi!");
    }

    @Test(priority = 6, description = "Multi-select: Chọn cùng lúc nhiều Môn học vào bộ lọc")
    public void testTC06_Data_LocNhieuMonHocCungLuc() throws InterruptedException {
        driver.get(BASE_URL + "Timetable/Assign");
        page.chonHocKyVaNganh("999", "Công nghệ thông tincong nghe");
        page.waitForTableLoad();
        
        // Chọn liên tiếp 2 môn học
        page.locDuLieuToanKhoa("subjectFilter", "Hệ Quản trị");
        page.locDuLieuToanKhoa("subjectFilter", "Thị giác máy tính");
        
        Assert.assertTrue(page.kiemTraBangTKBCoDuLieu(), "Lỗi Data: Tính năng chọn nhiều (Multi-select) của thư viện Select2 gặp lỗi!");
    }

    // =========================================================================
    // 4. LUỒNG GIAO DIỆN (UI/UX) - 3 Kịch bản
    // =========================================================================
    @Test(priority = 7, description = "Giới hạn biên: Nhập 500 ký tự vào ô Lọc Giảng viên để kiểm tra vỡ Layout")
    public void testTC07_UI_GioiHanBien() throws InterruptedException {
        driver.get(BASE_URL + "Timetable/Assign");
        page.chonHocKyVaNganh("999", "Công nghệ thông tincong nghe");
        
        String chuoiSieuDai = "A".repeat(500);
        page.nhapGioiHanBienLocToanKhoa("lecturerFilter", chuoiSieuDai);
        
        Assert.assertTrue(page.kiemTraThanhCongCu(), "Lỗi UI: Nhập quá nhiều ký tự làm vỡ Layout, thanh công cụ biến mất khỏi màn hình!");
    }

    @Test(priority = 8, description = "Responsive: Thay đổi kích thước sang Mobile (390x844), thanh công cụ không bị mất")
    public void testTC08_UI_ResponsiveMobile() throws InterruptedException {
        driver.get(BASE_URL + "Timetable/Assign");
        page.chonHocKyVaNganh("999", "Công nghệ thông tincong nghe");
        
        // Mô phỏng màn hình điện thoại dọc
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        
        Assert.assertTrue(page.kiemTraThanhCongCu(), "Lỗi UI Responsive: Khi thu nhỏ về Mobile, giao diện thanh công cụ bị văng/mất!");
        
        driver.manage().window().maximize();
        Thread.sleep(1000);
    }

    @Test(priority = 9, description = "Thanh cuộn dọc & ngang: Cuộn kịch kim bảng TKB để xem giao diện có rách/vỡ không")
    public void testTC09_UI_ThanhCuonNgangDoc() throws InterruptedException {
        driver.get(BASE_URL + "Timetable/Assign");
        page.chonHocKyVaNganh("999", "Công nghệ thông tincong nghe");
        page.waitForTableLoad();
        
        // Cuộn sang tận ngày Chủ Nhật (Trái sang Phải)
        page.cuonBangTKB("ngang");
        // Cuộn xuống tận Tiết 13-15 (Trên xuống Dưới)
        page.cuonBangTKB("doc");
        
        Assert.assertTrue(page.kiemTraBangTKBCoDuLieu(), "Lỗi UX: Bảng TKB bị lỗi render khi thao tác cuộn trên thanh Scrollbar!");
    }
}