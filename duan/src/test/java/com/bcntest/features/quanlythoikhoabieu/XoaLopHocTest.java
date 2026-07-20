package com.bcntest.features.quanlythoikhoabieu;

import com.bcnpages.QuanLyThoiKhoaBieuPage;
import com.bcntest.core.BaseTest;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.List;

public class XoaLopHocTest extends BaseTest {
    QuanLyThoiKhoaBieuPage page;

    @BeforeClass
    public void init() { 
        page = new QuanLyThoiKhoaBieuPage(driver); 
    }

    @BeforeMethod
    public void prepare() throws InterruptedException {
        driver.get(BASE_URL + "Timetable/Assign");
        page.chonHocKyVaNganh("999", "Công nghệ thông tincong nghe");
        try {
            page.waitForTableLoad();
        } catch (Exception e) {
            System.err.println("Cảnh báo: Bảng không tải kịp.");
        }
    }

    // =========================================================================
    // 1. LUỒNG ĐÚNG (HAPPY PATH)
    // =========================================================================
    @Test(priority = 1, description = "Nhập ĐÚNG mã lớp học phần hiển thị trên popup và bấm Xóa thành công")
    public void testTC01_Happy_XoaLopThanhCong() throws InterruptedException {
        List<WebElement> dsDaPhan = page.layCacODaPhanCong();
        Assert.assertTrue(dsDaPhan.size() > 0, "Lỗi: Bảng không có lớp nào đã phân công để test xóa!");

        page.clickODaPhanCong(0);
        page.clickNutThungRacXoa();
        
        // Lấy tự động cái mã trên màn hình
        String maLopChinhXac = page.layMaLopHocPhanTuPopup();
        Assert.assertFalse(maLopChinhXac.isEmpty(), "Lỗi: Không trích xuất được mã lớp học từ câu thông báo!");
        
        page.nhapMaXacNhanXoa(maLopChinhXac);
        page.clickXacNhanXoaTrenPopup();
        
        String msg = page.layThongBao();
        Assert.assertTrue(msg.contains("thành công") || msg.contains("success"), 
            "Lỗi: Không nhận được thông báo Xóa thành công! Web báo: " + msg);
    }

    @Test(priority = 2, description = "Bấm Thùng Rác nhưng sau đó bấm nút Hủy để từ chối xóa")
    public void testTC02_Happy_HuyXoaLopHoc() throws InterruptedException {
        page.clickODaPhanCong(0);
        page.clickNutThungRacXoa();
        
        page.clickHuyXoaTrenPopup();
        Assert.assertFalse(page.isPopupXoaHienThi(), "Lỗi UX: Bấm nút Hủy nhưng Popup Xóa vẫn không đóng!");
    }

    // =========================================================================
    // 2. LUỒNG SAI (SAD PATH)
    // =========================================================================
    @Test(priority = 3, description = "Để trống ô nhập mã xác nhận và bấm Xóa")
    public void testTC03_Sad_BoTrongMaXacNhan() throws InterruptedException {
        page.clickODaPhanCong(0);
        page.clickNutThungRacXoa();
        
        page.nhapMaXacNhanXoa(""); 
        page.clickXacNhanXoaTrenPopup();
        
        String msg = page.layThongBao();
        Assert.assertTrue(msg.contains("lỗi") || msg.contains("không hợp lệ") || msg.contains("vui lòng") || page.isPopupXoaHienThi(), 
            "Lỗi: Form cho phép xóa thành công dù không nhập mã xác nhận!");
            
        if (page.isPopupXoaHienThi()) { page.clickHuyXoaTrenPopup(); }
    }

    @Test(priority = 4, description = "Nhập mã lớp sai/bậy bạ vào ô xác nhận")
    public void testTC04_Sad_NhapSaiMaXacNhan() throws InterruptedException {
        page.clickODaPhanCong(0);
        page.clickNutThungRacXoa();
        
        page.nhapMaXacNhanXoa("MA_TAO_LAO_12345"); 
        page.clickXacNhanXoaTrenPopup();
        
        String msg = page.layThongBao();
        Assert.assertTrue(msg.contains("lỗi") || msg.contains("sai") || msg.contains("không khớp") || page.isPopupXoaHienThi(), 
            "Lỗi: Nhập mã bậy bạ mà hệ thống vẫn báo xóa thành công!");
            
        if (page.isPopupXoaHienThi()) { page.clickHuyXoaTrenPopup(); }
    }

    // =========================================================================
    // 3. LUỒNG DỮ LIỆU (DATA PATH)
    // =========================================================================
    @Test(priority = 5, description = "Nhập ký tự đặc biệt vào ô xác nhận")
    public void testTC05_Data_KyTuDacBiet() throws InterruptedException {
        page.clickODaPhanCong(0);
        page.clickNutThungRacXoa();
        
        page.nhapMaXacNhanXoa("!@#$%^&*()_+{}|:<>?"); 
        page.clickXacNhanXoaTrenPopup();
        
        Assert.assertTrue(page.isPopupXoaHienThi() || page.layThongBao().contains("lỗi"), 
            "Lỗi: Form không chặn ký tự đặc biệt!");
            
        if (page.isPopupXoaHienThi()) { page.clickHuyXoaTrenPopup(); }
    }

    @Test(priority = 6, description = "Kiểm tra bảo mật: Nhập mã XSS/HTML Injection")
    public void testTC06_Data_HTMLInjection() throws InterruptedException {
        page.clickODaPhanCong(0);
        page.clickNutThungRacXoa();
        
        page.nhapMaXacNhanXoa("<script>alert('Hacked');</script>"); 
        page.clickXacNhanXoaTrenPopup();
        
        Assert.assertTrue(page.isPopupXoaHienThi() || page.layThongBao().contains("lỗi"), 
            "Lỗi: Lỗ hổng bảo mật XSS, hệ thống cho phép submit thẻ HTML!");
            
        if (page.isPopupXoaHienThi()) { page.clickHuyXoaTrenPopup(); }
    }

    // =========================================================================
    // 4. LUỒNG GIAO DIỆN (UI/UX)
    // =========================================================================
    @Test(priority = 7, description = "Mở popover lớp ĐÃ CÓ GIẢNG VIÊN, bấm Thùng Rác kiểm tra UI Popup Xóa")
    public void testTC07_UI_KiemTraPopupXoa() throws InterruptedException {
        page.clickODaPhanCong(0);
        page.clickNutThungRacXoa();

        Assert.assertTrue(page.isPopupXoaHienThi(), "Lỗi UI: Popup xác nhận Xóa không hiển thị!");
        page.clickHuyXoaTrenPopup(); 
    }

    @Test(priority = 8, description = "Giới hạn biên: Nhập chuỗi siêu dài (500 ký tự) kiểm tra vỡ layout")
    public void testTC08_UI_GioiHanBienKyTu() throws InterruptedException {
        page.clickODaPhanCong(0);
        page.clickNutThungRacXoa();
        
        String chuoiSieuDai = "A".repeat(500);
        page.nhapMaXacNhanXoa(chuoiSieuDai);
        
        boolean nutXoaConHienThi = driver.findElement(By.cssSelector("button.swal2-confirm")).isDisplayed();
        Assert.assertTrue(nutXoaConHienThi, "Lỗi UI: Nhập văn bản quá dài làm vỡ layout Popup Xóa!");
        page.clickHuyXoaTrenPopup(); 
    }

    @Test(priority = 9, description = "Responsive: Test chức năng xóa trên màn hình Mobile (390x844)")
    public void testTC09_UI_ResponsiveMobile() throws InterruptedException {
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        
        page.clickODaPhanCong(0);
        page.clickNutThungRacXoa();
        
        Assert.assertTrue(page.isPopupXoaHienThi(), "Lỗi UI (Mobile): Popup xác nhận Xóa không hiển thị!");
        page.clickHuyXoaTrenPopup(); 
        driver.manage().window().maximize();
    }

    @Test(priority = 10, description = "Thanh cuộn dọc: Xóa lớp học ở vị trí cuối cùng của bảng")
    public void testTC10_UI_ThanhCuonDoc() throws InterruptedException {
        List<WebElement> dsDaPhan = page.layCacODaPhanCong();
        int viTriCuoiCung = dsDaPhan.size() - 1;
        
        page.clickODaPhanCong(viTriCuoiCung);
        page.clickNutThungRacXoa();
        
        Assert.assertTrue(page.isPopupXoaHienThi(), "Lỗi UX: Cuộn xuống cuối bảng thì Popup Xóa bị kẹt/mất dạng!");
        page.clickHuyXoaTrenPopup(); 
    }
}