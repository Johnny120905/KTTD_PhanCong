package com.bcntest.features.quanlyhocky;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyHocKyPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XemHocKyTest extends BaseTest {

    QuanLyHocKyPage hocKyPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        hocKyPage = new QuanLyHocKyPage(driver);
        driver.get(BASE_URL + "Term"); // Chuyển hướng tới trang Quản lý Học kỳ
        Thread.sleep(3000); 
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (Happy Path) - Tải trang và có dữ liệu
    // ==========================================
    @Test(priority = 1)
    public void testF_XemHocKy_TC01_HienThiDanhSach() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: KIỂM TRA HIỂN THỊ DANH SÁCH ---");
        
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        // Kiểm tra xem bảng có hiển thị dữ liệu không (Số dòng > 0)
        int soLuong = hocKyPage.laySoLuongHocKyHienThi();
        Assert.assertTrue(soLuong > 0, "Lỗi: Danh sách học kỳ không hiển thị dữ liệu hoặc bảng bị trống!");
    }

    // ==========================================
    // 2. LUỒNG SAI (Negative Path) - Tìm kiếm từ khóa rác
    // ==========================================
    @Test(priority = 2)
    public void testF_XemHocKy_TC02_TimKiemKhongTonTai() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: TÌM KIẾM DỮ LIỆU KHÔNG TỒN TẠI ---");
        
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        // Cố tình nhập một từ khóa không có thật
        String tuKhoaRac = "XYZ_KhongTonTai_9999";
        hocKyPage.nhapTuKhoaTimKiem(tuKhoaRac);
        Thread.sleep(1500); // Đợi bảng filter dữ liệu
        
        // Kiểm tra số lượng dòng phải bằng 0
        int soLuong = hocKyPage.laySoLuongHocKyHienThi();
        Assert.assertEquals(soLuong, 0, "Lỗi: Tìm kiếm từ khóa rác nhưng bảng vẫn hiện dữ liệu!");
        
        // Kiểm tra thông báo có xuất hiện hay không
        String thongBao = hocKyPage.layThongBaoKhongCoDuLieu().toLowerCase();
        Assert.assertTrue(thongBao.contains("không tìm thấy") || thongBao.contains("không có") || thongBao.contains("no data"), 
                "Lỗi: Không hiển thị đúng thông báo rỗng. Thực tế thông báo: '" + thongBao + "'");
    }

    // ==========================================
    // 3. LUỒNG DATA (Data-Driven) - Quét nhiều từ khóa chuẩn
    // ==========================================
    @DataProvider(name = "duLieuTimKiem")
    public Object[][] provideData() {
        // Dựa vào ảnh ông chụp, tôi lấy vài dữ liệu thực tế đang có trên bảng
        return new Object[][] {
            {"999"},  // Tìm theo Tên Học Kỳ
            {"2025"}, // Tìm theo Năm
            {"52"}    // Tìm theo Tuần
        };
    }

    @Test(priority = 3, dataProvider = "duLieuTimKiem")
    public void testF_XemHocKy_TC03_TimKiemDaDang(String tuKhoa) throws InterruptedException {
        System.out.println("--- LUỒNG DATA: TÌM KIẾM VỚI TỪ KHÓA '" + tuKhoa + "' ---");
        
        driver.navigate().refresh();
        Thread.sleep(2000);

        hocKyPage.nhapTuKhoaTimKiem(tuKhoa);
        Thread.sleep(1500); // Chờ UI lọc xong
        
        // Kỳ vọng là với dữ liệu có thật, bảng phải hiển thị ít nhất 1 dòng
        int soLuong = hocKyPage.laySoLuongHocKyHienThi();
        Assert.assertTrue(soLuong > 0, "Fail Data-driven: Nhập từ khóa đúng '" + tuKhoa + "' nhưng không tìm thấy dữ liệu nào!");
    }
}