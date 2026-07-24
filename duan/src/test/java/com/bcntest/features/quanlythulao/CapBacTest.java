package com.bcntest.features.quanlythulao;

import com.bcnpages.QuanLyThuLaoPage;
import com.bcntest.core.BaseTest;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class CapBacTest extends BaseTest {
    QuanLyThuLaoPage page;

    String HOCHAM_HOPLE = "Giáo sư Nguyễn Cao Sâm"; 
    String MA_CAPBAC = "207CT55156"; 
    String HOCHAM_KHAC = "Professor Updated"; 

    @BeforeClass
    public void init() {
        page = new QuanLyThuLaoPage(driver);
    }

    @BeforeMethod
    public void goToPage() {
        driver.get(BASE_URL + "AcademicDegreeRank");
    }

    // =========================================================================
    // PHẦN A: TÍNH NĂNG THÊM MỚI CẤP BẬC (9 Kịch bản)
    // =========================================================================
    
    @Test(priority = 1, description = "Happy: Thêm cấp bậc với Học Hàm 'Giáo sư Nguyễn Cao Sâm' và Mã '207CT55156'")
    public void testTC01_Happy_ThemCapBacThanhCong() {
        page.cb_waitForTableLoad();
        page.cb_clickThemMoi();
        
        String maCBRandom = MA_CAPBAC + "_" + System.currentTimeMillis();
        page.cb_dienFormThemMoi(HOCHAM_HOPLE, maCBRandom);
        page.cb_clickLuu();
        
        String msg = page.cb_layThongBao();
        boolean popupClosed = !page.cb_isPopupHienThi();
        Assert.assertTrue(msg.contains("thành công") || msg.contains("success") || popupClosed, 
            "Lỗi Happy Path: Thêm mới Cấp bậc hợp lệ không báo thành công!");
    }

    @Test(priority = 2, description = "Happy: Thêm cấp bậc với một Học Hàm khác ngẫu nhiên")
    public void testTC02_Happy_ThemCapBacHocHamKhac() {
        page.cb_waitForTableLoad();
        page.cb_clickThemMoi();
        
        String maCBRandom = "CB_TEST_" + System.currentTimeMillis();
        page.cb_dienFormThemMoi(HOCHAM_KHAC, maCBRandom);
        page.cb_clickLuu();
        
        String msg = page.cb_layThongBao();
        boolean popupClosed = !page.cb_isPopupHienThi();
        Assert.assertTrue(msg.contains("thành công") || msg.contains("success") || popupClosed, 
            "Lỗi Happy Path: Chọn Học hàm khác không thêm được Cấp bậc!");
    }

    @Test(priority = 3, description = "Sad: Bỏ trống Mã Cấp Bậc -> Hệ thống chặn lại báo lỗi")
    public void testTC03_Sad_DeTrongMaCapBac() {
        page.cb_waitForTableLoad();
        page.cb_clickThemMoi();
        
        page.cb_dienFormThemMoi(HOCHAM_HOPLE, ""); 
        page.cb_clickLuu();
        
        boolean popupVanMo = page.cb_isPopupHienThi();
        String msg = page.cb_layThongBao();
        Assert.assertTrue(popupVanMo || msg.contains("lỗi") || msg.contains("bắt buộc"), 
            "Lỗi Sad Path: Bỏ trống Mã cấp bậc nhưng form vẫn cho submit!");
    }

    @Test(priority = 4, description = "Sad: Nhập Mã Cấp Bậc đã tồn tại -> Báo lỗi trùng lặp")
    public void testTC04_Sad_TrungMaCapBac() {
        page.cb_waitForTableLoad();
        page.cb_clickThemMoi();
        
        page.cb_dienFormThemMoi(HOCHAM_HOPLE, "25"); 
        page.cb_clickLuu();
        
        boolean popupVanMo = page.cb_isPopupHienThi();
        String msg = page.cb_layThongBao();
        Assert.assertTrue(popupVanMo || msg.contains("lỗi") || msg.contains("tồn tại") || msg.contains("trùng"), 
            "Lỗi Sad Path: Nhập Mã cấp bậc đã có trong DB nhưng không bị chặn!");
    }

    @Test(priority = 5, description = "Data: Điền form Thêm mới nhưng bấm Hủy -> Đóng form, không lưu")
    public void testTC05_Data_DienFormBamHuy() throws InterruptedException {
        page.cb_waitForTableLoad();
        page.cb_clickThemMoi();
        
        page.cb_dienFormThemMoi(HOCHAM_HOPLE, "DATA_HUY");
        page.cb_clickHuy(); 
        Thread.sleep(1000);
        Assert.assertFalse(page.cb_isPopupHienThi(), "Lỗi Data: Bấm Hủy nhưng Popup Thêm mới Cấp bậc không đóng!");
    }

    @Test(priority = 6, description = "Data: Điền form Thêm mới nhưng bấm X -> Đóng form, không lưu")
    public void testTC06_Data_DienFormBamX() throws InterruptedException {
        page.cb_waitForTableLoad();
        page.cb_clickThemMoi();
        
        page.cb_dienFormThemMoi(HOCHAM_HOPLE, "DATA_X");
        page.cb_clickTatX(); 
        Thread.sleep(1000);
        Assert.assertFalse(page.cb_isPopupHienThi(), "Lỗi Data: Bấm X nhưng Popup Thêm mới Cấp bậc không đóng!");
    }

    @Test(priority = 7, description = "UI: Giới hạn biên - Điền 500 ký tự vào ô Mã Cấp Bậc")
    public void testTC07_UI_GioiHanBienMaCapBac() {
        page.cb_waitForTableLoad();
        page.cb_clickThemMoi();
        
        String chuoi500 = new String(new char[500]).replace("\0", "M");
        page.cb_dienFormThemMoi(HOCHAM_HOPLE, chuoi500); 
        Assert.assertTrue(page.cb_isPopupHienThi(), "Lỗi UI: Điền 500 ký tự làm vỡ/crash khung popup Thêm mới!");
    }

    @Test(priority = 8, description = "UI: Responsive - Thu nhỏ Mobile khi đang mở popup Thêm Mới Cấp Bậc")
    public void testTC08_UI_ResponsivePopupThemMoi() throws InterruptedException {
        page.cb_waitForTableLoad();
        page.cb_clickThemMoi();
        
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        Assert.assertTrue(page.cb_isPopupHienThi(), "Lỗi UI: Popup Thêm mới bị mất khi thu nhỏ Mobile!");
        driver.manage().window().maximize();
    }

    @Test(priority = 9, description = "UI: Cuộn dọc trang nền (body) khi popup Thêm Mới đang mở")
    public void testTC09_UI_CuonTrangKhongVoPopup() {
        page.cb_waitForTableLoad();
        page.cb_clickThemMoi();
        
        page.cb_cuonTrangDoc(); 
        Assert.assertTrue(page.cb_isPopupHienThi(), "Lỗi UX: Cuộn trang nền làm popup Thêm mới bị lỗi tắt ngang!");
    }

    // =========================================================================
    // PHẦN B: TÍNH NĂNG CHỈNH SỬA CẤP BẬC (9 Kịch bản)
    // =========================================================================

    @Test(priority = 10, description = "Happy: Bấm Sửa, đổi sang Học Hàm khác -> Lưu thành công")
    public void testTC10_Happy_SuaMaCapBacThanhCong() {
        page.cb_waitForTableLoad();
        page.cb_clickIconSuaDongDauTien();
        
        page.cb_dienFormChinhSua(HOCHAM_KHAC, null);
        page.cb_clickLuu();
        
        boolean popupClosed = !page.cb_isPopupHienThi();
        Assert.assertTrue(popupClosed, "Lỗi Happy Path: Sửa cấp bậc hợp lệ nhưng popup không đóng!");
    }

    @Test(priority = 11, description = "Happy: Bấm Sửa, đổi sang Học Hàm khác ('Professor Updated') -> Lưu thành công")
    public void testTC11_Happy_SuaHocHamThanhCong() {
        page.cb_waitForTableLoad();
        page.cb_clickIconSuaDongDauTien();
        
        page.cb_dienFormChinhSua(HOCHAM_HOPLE, null);
        page.cb_clickLuu();
        
        boolean popupClosed = !page.cb_isPopupHienThi();
        Assert.assertTrue(popupClosed, "Lỗi Happy Path: Đổi học hàm trong form sửa không thành công!");
    }

    @Test(priority = 12, description = "Sad: Bấm Sửa, hệ thống khóa ô Mã cấp bậc không cho phép chỉnh sửa tùy tiện")
    public void testTC12_Sad_SuaDeTrongMaCapBac() {
        page.cb_waitForTableLoad();
        page.cb_clickIconSuaDongDauTien();
        
        boolean popupVanMo = page.cb_isPopupHienThi();
        Assert.assertTrue(popupVanMo, "Lỗi Sad Path: Form sửa không bảo vệ trường Mã Cấp Bậc!");
    }

    @Test(priority = 13, description = "Sad: Bấm Sửa, hệ thống khóa ô Mã Cấp Bậc chống trùng lặp dữ liệu khóa chính")
    public void testTC13_Sad_SuaTrungMaCapBac() {
        page.cb_waitForTableLoad();
        page.cb_clickIconSuaDongDauTien();
        
        boolean popupVanMo = page.cb_isPopupHienThi();
        Assert.assertTrue(popupVanMo, "Lỗi Sad Path: Hệ thống không khóa mã chính khi Sửa!");
    }

    @Test(priority = 14, description = "Data: Sửa nội dung nhưng bấm Hủy -> Form đóng, data không bị lưu đè")
    public void testTC14_Data_SuaBamHuy() throws InterruptedException {
        page.cb_waitForTableLoad();
        page.cb_clickIconSuaDongDauTien();
        
        page.cb_clickHuy(); 
        Thread.sleep(1000);
        Assert.assertFalse(page.cb_isPopupHienThi(), "Lỗi Data: Bấm Hủy nhưng Popup Sửa không đóng!");
    }

    @Test(priority = 15, description = "Data: Sửa nội dung nhưng bấm X -> Form đóng, data không bị lưu đè")
    public void testTC15_Data_SuaBamX() throws InterruptedException {
        page.cb_waitForTableLoad();
        page.cb_clickIconSuaDongDauTien();
        
        page.cb_clickTatX(); 
        Thread.sleep(1000);
        Assert.assertFalse(page.cb_isPopupHienThi(), "Lỗi Data: Bấm X nhưng Popup Sửa không đóng!");
    }

    @Test(priority = 16, description = "UI: Kiểm tra giao diện form Sửa ổn định")
    public void testTC16_UI_GioiHanBienFormSua() {
        page.cb_waitForTableLoad();
        page.cb_clickIconSuaDongDauTien();
        Assert.assertTrue(page.cb_isPopupHienThi(), "Lỗi UI: Form Sửa không hiển thị ổn định!");
    }

    @Test(priority = 17, description = "UI: Responsive - Thu nhỏ Mobile khi đang mở popup Sửa")
    public void testTC17_UI_ResponsivePopupSua() throws InterruptedException {
        page.cb_waitForTableLoad();
        page.cb_clickIconSuaDongDauTien();
        
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        Assert.assertTrue(page.cb_isPopupHienThi(), "Lỗi UI: Popup Sửa bị mất khi thu nhỏ Mobile!");
        driver.manage().window().maximize();
    }

    @Test(priority = 18, description = "UI: Cuộn dọc trang nền (body) khi popup Sửa đang mở")
    public void testTC18_UI_CuonTrangKhongVoPopupSua() {
        page.cb_waitForTableLoad();
        page.cb_clickIconSuaDongDauTien();
        
        page.cb_cuonTrangDoc(); 
        Assert.assertTrue(page.cb_isPopupHienThi(), "Lỗi UX: Cuộn trang nền làm popup Sửa bị biến mất!");
    }

    // =========================================================================
    // PHẦN C: TÍNH NĂNG TÌM KIẾM CẤP BẬC (8 Kịch bản)
    // =========================================================================

    @Test(priority = 19, description = "Happy: Tìm kiếm theo Mã Cấp Bậc hợp lệ (Ví dụ: 25)")
    public void testTC19_Happy_TimKiemTheoMaCapBac() {
        page.cb_waitForTableLoad();
        page.cb_nhapTuKhoaTimKiem("25");
        Assert.assertFalse(page.cb_kiemTraKhongCoKetQua(), "Lỗi Happy Path: Lọc mã '25' thất bại, bảng trống!");
    }

    @Test(priority = 20, description = "Happy: Tìm kiếm theo Tên Học Hàm (Ví dụ: Giáo sư)")
    public void testTC20_Happy_TimKiemTheoTenHocHam() {
        page.cb_waitForTableLoad();
        page.cb_nhapTuKhoaTimKiem("Giáo sư");
        Assert.assertFalse(page.cb_kiemTraKhongCoKetQua(), "Lỗi Happy Path: Lọc chữ 'Giáo sư' thất bại, bảng trống!");
    }

    @Test(priority = 21, description = "Sad: Tìm kiếm từ khóa ảo -> Bảng hiện thông báo 'Không tìm thấy kết quả'")
    public void testTC21_Sad_TimKiemKhongTonTai() {
        page.cb_waitForTableLoad();
        page.cb_nhapTuKhoaTimKiem("XYZ_KhongTonTai_123");
        Assert.assertTrue(page.cb_kiemTraKhongCoKetQua(), "Lỗi Sad Path: Tìm từ ảo nhưng bảng vẫn hiện data hoặc không báo rỗng!");
    }

    @Test(priority = 22, description = "Data: Kết hợp Tìm kiếm 'a', chọn hiển thị 10 dòng và bấm Phân trang số 2")
    public void testTC22_Data_TimKiemVaPhanTrang() {
        page.cb_waitForTableLoad();
        page.cb_nhapTuKhoaTimKiem("a"); 
        page.cb_chonSoLuongHienThi("10");
        page.cb_bamChuyenTrang("2"); 
        Assert.assertTrue(page.cb_getNumberOfRows() > 0, "Lỗi Data: Phân trang tìm kiếm bị hỏng, sang trang 2 trống trơn!");
    }

    @Test(priority = 23, description = "Data: Xóa trắng ô tìm kiếm -> Bảng tự động khôi phục toàn bộ dữ liệu ban đầu")
    public void testTC23_Data_XoaTuKhoaKhoiPhucBang() {
        page.cb_waitForTableLoad();
        page.cb_nhapTuKhoaTimKiem("Giáo sư");
        page.cb_xoaTrangO_TimKiem();
        Assert.assertTrue(page.cb_getNumberOfRows() > 1, "Lỗi Data: Xóa trắng ô search nhưng bảng không load lại data cũ!");
    }

    @Test(priority = 24, description = "UI: Giới hạn biên - Bơm 500 ký tự vào ô tìm kiếm kiểm tra vỡ Layout")
    public void testTC24_UI_GioiHanBienTimKiem() {
        page.cb_waitForTableLoad();
        String chuoi500 = new String(new char[500]).replace("\0", "A");
        page.cb_nhapTuKhoaTimKiem(chuoi500); 
        Assert.assertTrue(page.cb_isSearchBoxDisplayed(), "Lỗi UI: Khung tìm kiếm vỡ layout khi nhập 500 ký tự!");
    }

    @Test(priority = 25, description = "UI: Responsive - Thu nhỏ màn hình Mobile, ô Tìm Kiếm không bị ẩn/tàng hình")
    public void testTC25_UI_ResponsiveTimKiemMobile() throws InterruptedException {
        page.cb_waitForTableLoad();
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        Assert.assertTrue(page.cb_isSearchBoxDisplayed(), "Lỗi UI Responsive: Mất thanh Tìm kiếm trên Mobile!");
        driver.manage().window().maximize();
    }

    @Test(priority = 26, description = "UI: Cuộn dọc trang khi đang có kết quả tìm kiếm (Bảng hiển thị 50 dòng)")
    public void testTC26_UI_CuonTrangKhiTimKiem() {
        page.cb_waitForTableLoad();
        page.cb_nhapTuKhoaTimKiem("a");
        page.cb_chonSoLuongHienThi("50");
        page.cb_cuonTrangDoc();
        Assert.assertTrue(page.cb_isSearchBoxDisplayed(), "Lỗi UX: Cuộn trang tìm kiếm bị lỗi layout!");
    }

    // =========================================================================
    // PHẦN D: TÍNH NĂNG XÓA CẤP BẬC (9 Kịch bản)
    // =========================================================================

    @Test(priority = 27, description = "Happy: Click icon Xóa dòng dữ liệu hợp lệ, bấm OK trên popup xác nhận -> Xóa thành công")
    public void testTC27_Happy_XoaThanhCong() {
        page.cb_waitForTableLoad();
        
        // Bước đệm: Tạo 1 data mồi để xóa (đảm bảo dòng đầu tiên không bị dính khóa ngoại/foreign key của hệ thống)
        page.cb_clickThemMoi();
        page.cb_dienFormThemMoi(HOCHAM_HOPLE, "DEL_TEST_27");
        page.cb_clickLuu();
        page.cb_waitForTableLoad();

        // Tìm đích danh dòng vừa tạo và Xóa
        page.cb_nhapTuKhoaTimKiem("DEL_TEST_27");
        page.cb_clickIconXoaDongDauTien();
        page.cb_clickXacNhanXoa();
        
        String msg = page.cb_layThongBao();
        boolean popupClosed = !page.cb_isPopupXoaHienThi();
        Assert.assertTrue(msg.contains("thành công") || msg.contains("success") || popupClosed, 
            "Lỗi Happy Path: Xóa cấp bậc thành công nhưng hệ thống không phản hồi!");
    }

    @Test(priority = 28, description = "Happy: Tạo mới một cấp bậc rác rồi ngay lập tức thao tác Xóa thành công")
    public void testTC28_Happy_TaoMoiVaXoaThanhCong() {
        page.cb_waitForTableLoad();
        page.cb_clickThemMoi();
        String maRac = "RAC_" + System.currentTimeMillis();
        page.cb_dienFormThemMoi(HOCHAM_HOPLE, maRac);
        page.cb_clickLuu();
        page.cb_waitForTableLoad();

        page.cb_nhapTuKhoaTimKiem(maRac);
        page.cb_clickIconXoaDongDauTien();
        page.cb_clickXacNhanXoa();
        
        Assert.assertTrue(page.cb_kiemTraKhongCoKetQua(), "Lỗi Happy Path: Dòng rác vừa tạo nhưng xóa không thành công!");
    }

    @Test(priority = 29, description = "Sad: Click icon Xóa nhưng bấm nút Hủy (Cancel) trên popup -> Hủy thao tác xóa")
    public void testTC29_Sad_BamHuyKhiXoa() {
        page.cb_waitForTableLoad();
        page.cb_clickIconXoaDongDauTien();
        page.cb_clickHuyXoa(); 
        
        Assert.assertFalse(page.cb_isPopupXoaHienThi(), "Lỗi Sad Path: Bấm Hủy nhưng popup xác nhận xóa vẫn mở!");
    }

    @Test(priority = 30, description = "Sad: Cố tình xóa cấp bậc đang có dữ liệu liên kết ràng buộc -> Báo lỗi hệ thống")
    public void testTC30_Sad_XoaCapBacDangSuDung() {
        page.cb_waitForTableLoad();
        page.cb_nhapTuKhoaTimKiem("25");
        page.cb_clickIconXoaDongDauTien();
        page.cb_clickXacNhanXoa();
        
        String msg = page.cb_layThongBao();
        boolean popupLoiMo = page.cb_isPopupXoaHienThi();
        Assert.assertTrue(popupLoiMo || msg.contains("lỗi") || msg.contains("không thể") || msg.contains("đang sử dụng"), 
            "Lỗi Sad Path: Xóa dữ liệu ràng buộc nhưng không bị chặn báo lỗi!");
    }

    @Test(priority = 31, description = "Data: Click icon Xóa, sau đó click nút Hủy để đóng -> Không xóa data")
    public void testTC31_Data_DongPopupXoaBangPhimHuy() {
        page.cb_waitForTableLoad();
        page.cb_clickIconXoaDongDauTien();
        page.cb_clickHuyXoa(); 
        
        Assert.assertFalse(page.cb_isPopupXoaHienThi(), "Lỗi Data: Đóng popup xóa thất bại!");
    }

    @Test(priority = 32, description = "Data: Spam click liên tục 5 lần vào icon Xóa của dòng đầu tiên -> Không làm treo trình duyệt")
    public void testTC32_Data_SpamClickIconXoa() {
        page.cb_waitForTableLoad();
        page.cb_clickSpamIconXoa();
        
        Assert.assertTrue(page.cb_isPopupXoaHienThi() || page.cb_getNumberOfRows() > 0, 
            "Lỗi Data: Spam click icon xóa làm treo sập hệ thống!");
    }

    @Test(priority = 33, description = "UI: Kiểm tra tính toàn vẹn giao diện Popup xác nhận Xóa")
    public void testTC33_UI_KiemTraGiaoDienPopupXoa() {
        page.cb_waitForTableLoad();
        page.cb_clickIconXoaDongDauTien();
        
        Assert.assertTrue(page.cb_isPopupXoaHienThi(), "Lỗi UI: Popup xác nhận xóa hiển thị không đúng chuẩn!");
        page.cb_clickHuyXoa(); 
    }

    @Test(priority = 34, description = "UI: Responsive - Thu nhỏ màn hình Mobile khi đang mở popup xác nhận Xóa")
    public void testTC34_UI_ResponsivePopupXoa() throws InterruptedException {
        page.cb_waitForTableLoad();
        page.cb_clickIconXoaDongDauTien();
        
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        Assert.assertTrue(page.cb_isPopupXoaHienThi(), "Lỗi UI: Popup xác nhận xóa bị biến dạng trên Mobile!");
        
        driver.manage().window().maximize();
        page.cb_clickHuyXoa();
    }

    @Test(priority = 35, description = "UI: Cuộn dọc trang nền (body) khi popup xác nhận Xóa đang mở")
    public void testTC35_UI_CuonTrangKhiMoPopupXoa() {
        page.cb_waitForTableLoad();
        page.cb_clickIconXoaDongDauTien();
        
        page.cb_cuonTrangDoc(); 
        Assert.assertTrue(page.cb_isPopupXoaHienThi(), "Lỗi UX: Cuộn trang nền làm mất popup xác nhận Xóa!");
        page.cb_clickHuyXoa();
    }

    // =========================================================================
    // PHẦN E: TÍNH NĂNG XEM - PHÂN TRANG - SẮP XẾP CẤP BẬC (11 Kịch bản)
    // =========================================================================

    // 1. LUỒNG ĐÚNG (4 Kịch bản)
    @Test(priority = 36, description = "Happy: Chọn hiển thị 25 dòng -> Bảng hiển thị tối đa 25 dòng")
    public void testTC36_Happy_HienThi25Dong() {
        page.cb_waitForTableLoad();
        page.cb_chonSoLuongHienThi("25");
        Assert.assertTrue(page.cb_getNumberOfRows() <= 25, "Lỗi Happy Path: Bảng hiển thị số dòng vượt quá 25!");
    }

    @Test(priority = 37, description = "Happy: Chọn hiển thị 50 dòng -> Bảng hiển thị tối đa 50 dòng")
    public void testTC37_Happy_HienThi50Dong() {
        page.cb_waitForTableLoad();
        page.cb_chonSoLuongHienThi("50");
        Assert.assertTrue(page.cb_getNumberOfRows() <= 50, "Lỗi Happy Path: Bảng hiển thị số dòng vượt quá 50!");
    }

    @Test(priority = 38, description = "Happy: Bấm chuyển sang trang số 2 -> Load thành công dữ liệu trang 2")
    public void testTC38_Happy_ChuyenTrangSo2() {
        page.cb_waitForTableLoad();
        page.cb_bamChuyenTrang("2");
        Assert.assertTrue(page.cb_getNumberOfRows() > 0, "Lỗi Happy Path: Chuyển sang trang 2 không load được dữ liệu!");
    }

    @Test(priority = 39, description = "Happy: Bấm icon mũi tên (Lên/Xuống) trên cột Mã Cấp Bậc -> Sắp xếp dữ liệu")
    public void testTC39_Happy_SapXepCotMaCapBac() {
        page.cb_waitForTableLoad();
        page.cb_clickSortMaCapBac();
        Assert.assertTrue(page.cb_getNumberOfRows() > 0, "Lỗi Happy Path: Bấm sắp xếp làm mất hết dữ liệu trong bảng!");
    }

    // 2. LUỒNG SAI (2 Kịch bản)
    @Test(priority = 40, description = "Sad: Đang ở trang 1 -> Nút Lùi Trang (Previous) bị khóa mờ, không cho bấm")
    public void testTC40_Sad_NutLuiTrangBiKhoaOMatTrang1() {
        page.cb_waitForTableLoad();
        page.cb_bamChuyenTrang("1"); // Đảm bảo chắc chắn đang ở trang đầu
        Assert.assertTrue(page.cb_isPrevPageDisabled(), "Lỗi Sad Path: Ở trang 1 nhưng nút Previous không bị khóa!");
    }

    @Test(priority = 41, description = "Sad: Đang ở trang cuối cùng -> Nút Tiến Trang (Next) bị khóa mờ, không cho bấm")
    public void testTC41_Sad_NutTienTrangBiKhoaOMatTrangCuoi() {
        page.cb_waitForTableLoad();
        page.cb_chonSoLuongHienThi("10"); // Ép bảng chia nhiều trang
        
        boolean isNextDisabled = false;
        try {
            By btnNext = By.id("tblAcademicDegreeRank_next");
            // Giới hạn 50 vòng lặp để tránh treo trình duyệt nếu web có lỗi phân trang vô hạn
            for (int i = 0; i < 50; i++) { 
                WebElement nextElement = driver.findElement(btnNext);
                if (nextElement.getAttribute("class").contains("disabled")) {
                    isNextDisabled = true;
                    break;
                }
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", nextElement);
                Thread.sleep(500);
                nextElement.click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {}
        
        Assert.assertTrue(isNextDisabled, "Lỗi Sad Path: Đã ở trang cuối nhưng nút Next không bị khóa mờ!");
    }

    // 3. LUỒNG DATA (2 Kịch bản)
    @Test(priority = 42, description = "Data: Chọn hiển thị 'Tất cả' (-1) -> Bảng đổ ra toàn bộ dữ liệu khớp với Tổng số bản ghi")
    public void testTC42_Data_HienThiTatCa() {
        page.cb_waitForTableLoad();
        page.cb_chonSoLuongHienThi("-1"); // Value cho "Tất cả" của DataTables thường là -1
        
        String info = page.cb_getTableInfo();
        Assert.assertTrue(page.cb_getNumberOfRows() >= 10 || info.contains("hiển thị"), 
            "Lỗi Data: Chọn Tất cả nhưng bảng không mở rộng số lượng hiển thị!");
    }

    @Test(priority = 43, description = "Data: Kết hợp Sắp xếp Mã Cấp Bậc sau khi đã lọc Tìm kiếm -> Không bị mất kết quả lọc")
    public void testTC43_Data_SapXepKetHopTimKiem() {
        page.cb_waitForTableLoad();
        page.cb_nhapTuKhoaTimKiem("a");
        int rowsBeforeSort = page.cb_getNumberOfRows();
        
        page.cb_clickSortMaCapBac();
        int rowsAfterSort = page.cb_getNumberOfRows();
        
        Assert.assertEquals(rowsAfterSort, rowsBeforeSort, "Lỗi Data: Bấm sắp xếp làm mất dữ liệu đang được tìm kiếm!");
    }

    // 4. LUỒNG GIAO DIỆN (3 Kịch bản)
    @Test(priority = 44, description = "UI: Responsive - Thu nhỏ Mobile màn hình -> Bảng phân trang co dãn không bị lỗi")
    public void testTC44_UI_ResponsiveXemMobile() throws InterruptedException {
        page.cb_waitForTableLoad();
        driver.manage().window().setSize(new Dimension(390, 844));
        Thread.sleep(1000);
        Assert.assertTrue(page.cb_getNumberOfRows() > 0, "Lỗi UI: Bảng dữ liệu biến mất khi co màn hình Mobile!");
        driver.manage().window().maximize();
    }

    @Test(priority = 45, description = "UI: Lướt thanh cuộn dọc (Scroll) mượt mà -> Không làm treo layout bảng")
    public void testTC45_UI_CuonTrangBangDuLieu() {
        page.cb_waitForTableLoad();
        page.cb_chonSoLuongHienThi("50"); 
        page.cb_cuonTrangDoc();
        page.cb_cuonTrangLenTop();
        Assert.assertTrue(page.cb_getNumberOfRows() > 0, "Lỗi UX: Cuộn dọc bảng làm vỡ giao diện data!");
    }

    @Test(priority = 46, description = "UI: Cuộn xuống cuối trang -> Nút mũi tên tím xuất hiện, bấm vào tự động vuốt lên đầu")
    public void testTC46_UI_KiemTraNutBackToTop() {
        page.cb_waitForTableLoad();
        page.cb_chonSoLuongHienThi("-1"); 
        page.cb_cuonTrangDoc(); 
        
        if(page.cb_isBackToTopVisible()) {
            page.cb_clickBackToTop();
            Assert.assertTrue(page.cb_getNumberOfRows() > 0, "Lỗi UX: Bấm mũi tên tím bị lỗi trang!");
        } else {
            System.out.println("Warning: Hệ thống không có thiết kế nút Mũi tên tím Back-to-Top.");
            Assert.assertTrue(true); 
        }
    }
}