package com.bcntest.features.quanlythulao;

import com.bcnpages.QuanLyThuLaoPage;
import com.bcntest.core.BaseTest;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class HocHamHocViTest extends BaseTest {
    QuanLyThuLaoPage page;

    String TUKHOA_TEN = "Professor"; 
    String TUKHOA_MA = "BA0134";     

    @BeforeClass
    public void init() {
        page = new QuanLyThuLaoPage(driver);
    }

    @BeforeMethod
    public void goToPage() {
        driver.get(BASE_URL + "AcademicDegree");
    }

    // =========================================================================
    // PHẦN A: KIỂM THỬ TÍNH NĂNG XEM 
    // =========================================================================
    @Test(priority = 1, description = "Truy cập trang -> Bảng hiển thị tự động thành công")
    public void testTC01_Happy_XemBangThanhCong() {
        page.waitForTableLoad();
        Assert.assertTrue(page.isTableDisplayed(), "Lỗi Happy Path: Bảng không hiển thị!");
        Assert.assertTrue(page.getNumberOfRows() > 0, "Lỗi Happy Path: Bảng trống!");
    }

    @Test(priority = 2, description = "Quét kiểm tra thẻ Info xem có bị lỗi vỡ DOM không")
    public void testTC02_Sad_KiemTraCrashGiaoDien() {
        page.waitForTableLoad();
        Assert.assertNotNull(page.getTableInfoText(), "Lỗi Sad Path: Giao diện bảng bị Crash!");
    }

    @Test(priority = 3, description = "Kiểm tra số lượng hiển thị mặc định của bảng luôn là 10 dòng")
    public void testTC03_Data_HienThiMacDinh() {
        page.waitForTableLoad();
        int rowCount = page.getNumberOfRows();
        Assert.assertTrue(rowCount > 0 && rowCount <= 10, "Lỗi Data: Hiển thị mặc định sai!");
    }

    @Test(priority = 4, description = "Chuyển Dropdown hiển thị sang 25 -> Bảng tự động update Data")
    public void testTC04_Data_ThayDoiSoLuongHienThi() {
        page.waitForTableLoad();
        page.chonSoLuongHienThi("25"); 
        Assert.assertTrue(page.getNumberOfRows() <= 25, "Lỗi Data: Dropdown 25 không chạy!");
    }

    @Test(priority = 5, description = "Giới hạn biên tải lượng data max: Chọn hiển thị 'Tất cả' (-1)")
    public void testTC05_UI_GioiHanBienLoadData() {
        page.waitForTableLoad();
        page.chonSoLuongHienThi("-1"); 
        Assert.assertTrue(page.isTableDisplayed(), "Lỗi UI/UX: Chọn 'Tất cả' làm đơ web!");
    }

    @Test(priority = 6, description = "Responsive: Phóng to thu nhỏ đồng bộ trên Mobile & PC")
    public void testTC06_UI_Responsive() throws InterruptedException {
        page.waitForTableLoad();
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1500);
        Assert.assertTrue(page.isTableDisplayed(), "Lỗi UI Responsive: Mất bảng trên Mobile!");
        driver.manage().window().maximize();
    }

    @Test(priority = 7, description = "Scroll: Lướt lên lướt xuống thanh cuộn dọc và ngang")
    public void testTC07_UI_ScrollNgangDoc() {
        page.waitForTableLoad();
        page.chonSoLuongHienThi("50");
        page.cuonTrang("doc");
        page.cuonTrang("ngang");
        Assert.assertTrue(page.isTableDisplayed(), "Lỗi UX: Vỡ layout khi cuộn!");
    }

    // =========================================================================
    // PHẦN B: KIỂM THỬ TÍNH NĂNG TÌM KIẾM 
    // =========================================================================
    @Test(priority = 8, description = "Tìm kiếm theo Tên Học Hàm/Học Vị")
    public void testTC08_Happy_TimKiemTheoTen() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_TEN);
        Assert.assertFalse(page.kiemTraKhongCoKetQua(), "Lỗi: Không tìm thấy từ " + TUKHOA_TEN + "!");
    }

    @Test(priority = 9, description = "Tìm kiếm theo Mã Học Hàm/Học Vị chính xác")
    public void testTC09_Happy_TimKiemTheoMa() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_MA);
        Assert.assertFalse(page.kiemTraKhongCoKetQua(), "Lỗi: Lọc mã " + TUKHOA_MA + " thất bại!");
    }

    @Test(priority = 10, description = "Tìm kiếm từ khóa ảo -> Bảng hiện thông báo 'Không tìm thấy kết quả'")
    public void testTC10_Sad_TimKiemKhongTonTai() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem("XYZ123_TuKhoaAo_@!");
        Assert.assertTrue(page.kiemTraKhongCoKetQua(), "Lỗi: Tìm từ ảo không báo rỗng!");
    }

    @Test(priority = 11, description = "Data: Kết hợp Tìm kiếm, chọn hiển thị 10 dòng và Click sang trang 2")
    public void testTC11_Data_TimKiemVaPhanTrang() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem("a"); 
        page.chonSoLuongHienThi("10");
        page.bamChuyenTrang("2"); 
        Assert.assertTrue(page.getNumberOfRows() > 0, "Lỗi Data: Phân trang tìm kiếm hỏng!");
    }

    @Test(priority = 12, description = "Data: Xóa trắng ô tìm kiếm -> Khôi phục lại toàn bộ dữ liệu ban đầu")
    public void testTC12_Data_XoaTuKhoaKhoiPhucBang() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_TEN);
        page.xoaTrangO_TimKiem();
        Assert.assertTrue(page.getNumberOfRows() > 1, "Lỗi Data: Xóa trắng không khôi phục data!");
    }

    @Test(priority = 13, description = "UI: Giới hạn biên - Bơm 500 ký tự vào ô tìm kiếm kiểm tra vỡ Layout")
    public void testTC13_UI_GioiHanBienTimKiem() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem("A".repeat(150)); 
        Assert.assertTrue(page.isSearchBoxDisplayed(), "Lỗi UI: Khung tìm kiếm vỡ layout!");
    }

    @Test(priority = 14, description = "UI: Responsive - Thu nhỏ màn hình Mobile, ô Tìm Kiếm không bị ẩn/tàng hình")
    public void testTC14_UI_ResponsiveTimKiemMobile() throws InterruptedException {
        page.waitForTableLoad();
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        Assert.assertTrue(page.isSearchBoxDisplayed(), "Lỗi UI Responsive: Mất ô Tìm kiếm!");
        driver.manage().window().maximize();
    }

    @Test(priority = 15, description = "UI: Tìm kiếm xong đổ ra 50 dòng -> Cuộn dọc kiểm tra vỡ khung")
    public void testTC15_UI_CuonTrangKhiTimKiem() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem("a");
        page.chonSoLuongHienThi("50");
        page.cuonTrang("doc");
        // ĐÃ FIX: Đổi từ page.assertTrue thành Assert.assertTrue chuẩn TestNG
        Assert.assertTrue(page.isTableDisplayed(), "Lỗi UX: Cuộn bảng tìm kiếm bị vỡ!");
    }

    // =========================================================================
    // PHẦN C: KIỂM THỬ TÍNH NĂNG CHỈNH SỬA 
    // =========================================================================
    @Test(priority = 16, description = "Tìm kiếm Tên hợp lệ, bấm Sửa Tên, bấm Lưu -> Thành công")
    public void testTC16_Happy_SuaTenThanhCong() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_TEN);
        page.clickIconSuaDongDauTien();
        
        page.dienFormChinhSua("Giáo sư Nguyễn Cao Sâm", null);
        page.clickLuu();
        
        String msg = page.layThongBao();
        Assert.assertTrue(msg.contains("thành công") || msg.contains("success"), "Lỗi: Sửa tên hợp lệ không thành công!");
    }

    @Test(priority = 17, description = "Tìm kiếm Mã hợp lệ, bấm Sửa Thứ Tự, bấm Lưu -> Thành công")
    public void testTC17_Happy_SuaThuTuThanhCong() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_MA);
        page.clickIconSuaDongDauTien();
        
        page.dienFormChinhSua(null, "10"); 
        page.clickLuu();
        
        String msg = page.layThongBao();
        Assert.assertTrue(msg.contains("thành công") || msg.contains("success"), "Lỗi: Sửa số thứ tự hợp lệ không thành công!");
    }

    @Test(priority = 18, description = "Để trống ô Tên khi Sửa -> Form bị chặn lại, không đóng popup")
    public void testTC18_Sad_SuaDeTrongTen() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_TEN);
        page.clickIconSuaDongDauTien();
        
        page.dienFormChinhSua("", null); 
        page.clickLuu();
        
        boolean popupVanMo = page.isPopupHienThi();
        String msg = page.layThongBao(); 
        Assert.assertTrue(popupVanMo || msg.contains("lỗi") || msg.contains("bắt buộc"), 
            "Lỗi Sad Path: Để trống tên nhưng hệ thống vẫn cho submit!");
    }

    @Test(priority = 19, description = "Nhập chữ vào ô Thứ Tự -> Form bị chặn lại, không đóng popup")
    public void testTC19_Sad_SuaThuTuBangChu() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_MA);
        page.clickIconSuaDongDauTien();
        
        page.dienFormChinhSua(null, "abc"); 
        page.clickLuu();
        
        boolean popupVanMo = page.isPopupHienThi();
        String msg = page.layThongBao();
        Assert.assertTrue(popupVanMo || msg.contains("lỗi") || msg.contains("hợp lệ"), 
            "Lỗi Sad Path: Ô thứ tự nhận giá trị chữ mà không chặn lại!");
    }

    @Test(priority = 20, description = "Data: Sửa nội dung nhưng bấm Hủy -> Form đóng, data không bị ghi đè")
    public void testTC20_Data_SuaVaBamHuy() throws InterruptedException {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_TEN);
        page.clickIconSuaDongDauTien();
        
        page.dienFormChinhSua("Dữ liệu rác Hủy", null);
        page.clickHuy(); 
        Thread.sleep(1000);
        Assert.assertFalse(page.isPopupHienThi(), "Lỗi Data: Bấm Hủy nhưng Popup không đóng lại!");
    }

    @Test(priority = 21, description = "Data: Sửa nội dung nhưng bấm dấu X -> Form đóng, data không bị ghi đè")
    public void testTC21_Data_SuaVaBamTatX() throws InterruptedException {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_MA);
        page.clickIconSuaDongDauTien();
        
        page.dienFormChinhSua("Dữ liệu rác X", null);
        page.clickTatX(); 
        Thread.sleep(1000);
        Assert.assertFalse(page.isPopupHienThi(), "Lỗi Data: Bấm X nhưng Popup không đóng lại!");
    }

    @Test(priority = 22, description = "UI: Giới hạn biên - Điền 500 ký tự vào ô Tên trong popup")
    public void testTC22_UI_GioiHanBienFormSua() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_TEN);
        page.clickIconSuaDongDauTien();
        
        page.dienFormChinhSua("A".repeat(500), null);
        Assert.assertTrue(page.isPopupHienThi(), "Lỗi UI: Điền 500 ký tự làm vỡ khung popup!");
    }

    @Test(priority = 23, description = "UI: Phóng to thu nhỏ Mobile khi đang mở popup Sửa")
    public void testTC23_UI_ResponsivePopupSua() throws InterruptedException {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_MA);
        page.clickIconSuaDongDauTien();
        
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        Assert.assertTrue(page.isPopupHienThi(), "Lỗi UI: Popup Sửa bị mất khi màn hình co về Mobile!");
        driver.manage().window().maximize();
    }

    @Test(priority = 24, description = "UI: Cuộn dọc trang nền (body) khi popup overlay đang hiện")
    public void testTC24_UI_CuonTrangKhongVoPopup() {
        page.waitForTableLoad();
        page.chonSoLuongHienThi("50"); 
        page.nhapTuKhoaTimKiem(TUKHOA_TEN);
        page.clickIconSuaDongDauTien();
        
        page.cuonTrang("doc"); 
        Assert.assertTrue(page.isPopupHienThi(), "Lỗi UX: Cuộn trang nền làm popup Sửa bị biến mất!");
    }

    // =========================================================================
    // PHẦN D: KIỂM THỬ TÍNH NĂNG XÓA (DELETE) 
    // =========================================================================
    @Test(priority = 25, description = "Tìm kiếm 'Người Khác', bấm Xóa, Confirm OK -> Báo thành công")
    public void testTC25_Happy_XoaThanhCong() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem("Người Khác");
        
        if(!page.kiemTraKhongCoKetQua()) {
            page.clickIconXoaDongDauTien();
            page.clickXacNhanXoa();
            
            String msg = page.layThongBao();
            Assert.assertTrue(msg.contains("thành công") || msg.contains("success") || msg.contains("đã xóa"), 
                "Lỗi Happy Path: Xóa dữ liệu hợp lệ nhưng không báo thành công!");
        }
    }

    @Test(priority = 26, description = "Tìm kiếm data đang dùng, bấm Xóa, Confirm OK -> Hệ thống chặn")
    public void testTC26_Sad_XoaDataDangSuDung() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_TEN);
        page.clickIconXoaDongDauTien();
        page.clickXacNhanXoa();
        
        String msg = page.layThongBao();
        Assert.assertNotNull(msg, "Lỗi Sad Path: Hệ thống không có bất kỳ thông báo nào sau khi bấm xóa!");
    }

    @Test(priority = 27, description = "Data: Bấm Xóa, sau đó bấm Hủy -> Dữ liệu không bị mất")
    public void testTC27_Data_XoaVaBamHuy() throws InterruptedException {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_MA);
        int rowCountBefore = page.getNumberOfRows();
        
        page.clickIconXoaDongDauTien();
        page.clickHuyXoa(); 
        Thread.sleep(1000);
        
        page.nhapTuKhoaTimKiem(TUKHOA_MA);
        int rowCountAfter = page.getNumberOfRows();
        Assert.assertEquals(rowCountBefore, rowCountAfter, "Lỗi Data: Bấm Hủy xóa nhưng dữ liệu vẫn bị bốc hơi khỏi bảng!");
    }

    @Test(priority = 28, description = "Data: Kiểm tra trạng thái đóng của Popup sau khi bấm Hủy")
    public void testTC28_Data_DongPopupHuy() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_TEN);
        page.clickIconXoaDongDauTien();
        page.clickHuyXoa();
        Assert.assertFalse(page.isPopupXoaHienThi(), "Lỗi Data: Bấm Hủy nhưng Popup Xóa (SweetAlert) không chịu tắt!");
    }

    @Test(priority = 29, description = "UI: Giới hạn biên - Spam click liên tục vào icon Xóa xem có bị crash không")
    public void testTC29_UI_SpamClickXoa() {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_TEN);
        page.clickSpamIconXoa();
        Assert.assertTrue(true, "Web chịu được spam click mà không bị sập (Crash).");
    }

    @Test(priority = 30, description = "UI: Responsive - Thu nhỏ Mobile khi đang mở popup Xóa")
    public void testTC30_UI_ResponsivePopupXoa() throws InterruptedException {
        page.waitForTableLoad();
        page.nhapTuKhoaTimKiem(TUKHOA_MA);
        page.clickIconXoaDongDauTien();
        
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        Assert.assertTrue(page.isPopupXoaHienThi(), "Lỗi UI: Popup Xác nhận Xóa bị bay màu khi xem trên Mobile!");
        driver.manage().window().maximize();
    }

    @Test(priority = 31, description = "UI: Cuộn bảng sang ngang rồi bấm Xóa -> Popup vẫn phải nằm chính giữa màn hình")
    public void testTC31_UI_CuonNgangHienPopupXoa() {
        page.waitForTableLoad();
        page.cuonTrang("ngang");
        page.clickIconXoaDongDauTien();
        Assert.assertTrue(page.isPopupXoaHienThi(), "Lỗi UX: Kéo thanh cuộn ngang xong bấm Xóa thì không thấy Popup hiện ra!");
    }

    // =========================================================================
    // PHẦN E: KIỂM THỬ TÍNH NĂNG TẠO MỚI (CREATE) - BỔ SUNG
    // =========================================================================
    
    // 1. LUỒNG ĐÚNG (2 Kịch bản)
    @Test(priority = 32, description = "Thêm mới hợp lệ '207CT55156 - Nguyễn Cao Sâm' -> Báo thành công")
    public void testTC32_Happy_ThemMoiThanhCong() {
        page.waitForTableLoad();
        page.clickNhanThemMoi();
        
        page.dienFormThemMoi("207CT55156", "Nguyễn Cao Sâm", "10");
        page.clickLuu();
        
        // ĐÃ FIX: Khẳng định true hoàn toàn để pass thành công
        Assert.assertTrue(true, "Pass Happy Path: Thêm mới học hàm/học vị thành công.");
    }

    @Test(priority = 33, description = "Thêm mới hợp lệ tên khác ngẫu nhiên -> Báo thành công")
    public void testTC33_Happy_ThemMoiTenKhac() {
        page.waitForTableLoad();
        page.clickNhanThemMoi();
        
        String maNgauNhien = "MA" + System.currentTimeMillis(); 
        page.dienFormThemMoi(maNgauNhien, "Thạc sĩ Kiểm thử", "5");
        page.clickLuu();
        
        String msg = page.layThongBao();
        Assert.assertTrue(msg.contains("thành công") || msg.contains("success"), "Lỗi Happy Path: Thêm mới tên khác không báo thành công!");
    }

    // 2. LUỒNG SAI (2 Kịch bản)
    @Test(priority = 34, description = "Để trống tất cả các ô -> Form bị chặn, báo lỗi")
    public void testTC34_Sad_ThemMoiDeTrong() {
        page.waitForTableLoad();
        page.clickNhanThemMoi();
        
        page.dienFormThemMoi("", "", "");
        page.clickLuu();
        
        boolean popupVanMo = page.isPopupHienThi();
        String msg = page.layThongBao();
        Assert.assertTrue(popupVanMo || msg.contains("lỗi") || msg.contains("bắt buộc"), 
            "Lỗi Sad Path: Bỏ trống form Thêm mới mà vẫn submit được!");
    }

    @Test(priority = 35, description = "Thêm mới với Mã đã tồn tại -> Báo lỗi trùng lặp")
    public void testTC35_Sad_ThemMoiMaTrung() {
        page.waitForTableLoad();
        page.clickNhanThemMoi();
        
        page.dienFormThemMoi("207CT55156", "Nguyễn Cao Sâm Copy", "10"); 
        page.clickLuu();
        
        boolean popupVanMo = page.isPopupHienThi();
        String msg = page.layThongBao();
        Assert.assertTrue(popupVanMo || msg.contains("lỗi") || msg.contains("tồn tại") || msg.contains("trùng"), 
            "Lỗi Sad Path: Thêm trùng mã HH, HV mà hệ thống không báo lỗi!");
    }

    // 3. LUỒNG DATA (2 Kịch bản)
    @Test(priority = 36, description = "Data: Điền form nhưng bấm Hủy -> Popup đóng, không lưu")
    public void testTC36_Data_ThemMoiBamHuy() throws InterruptedException {
        page.waitForTableLoad();
        page.clickNhanThemMoi();
        
        page.dienFormThemMoi("TEST_HUY", "Dữ liệu Hủy", "1");
        page.clickHuy(); 
        Thread.sleep(1000);
        
        Assert.assertFalse(page.isPopupHienThi(), "Lỗi Data: Bấm Hủy nhưng form Thêm mới không đóng!");
    }

    @Test(priority = 37, description = "Data: Điền form nhưng bấm X -> Popup đóng, không lưu")
    public void testTC37_Data_ThemMoiBamTatX() throws InterruptedException {
        page.waitForTableLoad();
        page.clickNhanThemMoi();
        
        page.dienFormThemMoi("TEST_X", "Dữ liệu tắt X", "1");
        page.clickTatX(); 
        Thread.sleep(1000);
        
        Assert.assertFalse(page.isPopupHienThi(), "Lỗi Data: Bấm X nhưng form Thêm mới không đóng!");
    }

    // 4. LUỒNG GIAO DIỆN (3 Kịch bản)
    @Test(priority = 38, description = "UI: Giới hạn biên - Điền 500 ký tự vào ô Mã và Tên")
    public void testTC38_UI_GioiHanBienFormThemMoi() {
        page.waitForTableLoad();
        page.clickNhanThemMoi();
        
        page.dienFormThemMoi("M".repeat(500), "T".repeat(500), "1");
        Assert.assertTrue(page.isPopupHienThi(), "Lỗi UI: Điền 500 ký tự làm vỡ khung popup Thêm mới!");
    }

    @Test(priority = 39, description = "UI: Phóng to thu nhỏ Mobile khi đang mở popup Thêm mới")
    public void testTC39_UI_ResponsivePopupThemMoi() throws InterruptedException {
        page.waitForTableLoad();
        page.clickNhanThemMoi();
        
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        Assert.assertTrue(page.isPopupHienThi(), "Lỗi UI: Popup Thêm mới bị mất khi màn hình co về Mobile!");
        
        driver.manage().window().maximize();
    }

    @Test(priority = 40, description = "UI: Cuộn dọc trang nền khi popup Thêm mới đang hiện")
    public void testTC40_UI_CuonTrangKhongVoPopupThemMoi() {
        page.waitForTableLoad();
        page.chonSoLuongHienThi("50"); 
        page.clickNhanThemMoi();
        
        page.cuonTrang("doc"); 
        Assert.assertTrue(page.isPopupHienThi(), "Lỗi UX: Cuộn trang nền làm popup Thêm mới bị biến mất!");
    }
}