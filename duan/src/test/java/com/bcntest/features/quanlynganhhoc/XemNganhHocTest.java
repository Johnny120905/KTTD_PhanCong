package com.bcntest.features.quanlynganhhoc;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyNganhHocPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XemNganhHocTest extends BaseTest {

    QuanLyNganhHocPage page;

    @BeforeClass
    public void init() throws InterruptedException {
        page = new QuanLyNganhHocPage(driver);
        
        // LƯU Ý CHO ÔNG NGỌC: 
        // Ông nhớ check lại trên web xem đường dẫn (URL) vào trang Quản lý Ngành là gì nhé.
        // Ví dụ nếu là "/Major" thì điền vào đây.
        driver.get(BASE_URL + "Major"); 
        Thread.sleep(3000); 
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG: Tải trang hiển thị danh sách
    // ==========================================
    @Test(priority = 1)
    public void testTC01_XemDanhSachNganhHoc() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: KIỂM TRA HIỂN THỊ DANH SÁCH ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        int soLuong = page.laySoLuongNganhHocHienThi();
        Assert.assertTrue(soLuong > 0, "Lỗi: Danh sách ngành học không hiển thị hoặc bảng bị trống!");
    }

    // ==========================================
    // 2. LUỒNG SAI: Tìm từ khóa không tồn tại
    // ==========================================
    @Test(priority = 2)
    public void testTC02_TimKiemKhongTonTai() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: TÌM KIẾM DỮ LIỆU KHÔNG TỒN TẠI ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        String tuKhoaRac = "NganhHocX_9999";
        page.nhapTuKhoaTimKiem(tuKhoaRac);
        Thread.sleep(1500); // Đợi bảng filter
        
        // Kiểm tra số lượng dòng bằng 0
        int soLuong = page.laySoLuongNganhHocHienThi();
        Assert.assertEquals(soLuong, 0, "Lỗi: Tìm từ khóa rác nhưng bảng vẫn hiện dữ liệu!");
        
        // Kiểm tra thông báo
        String thongBao = page.layThongBaoKhongCoDuLieu().toLowerCase();
        Assert.assertTrue(thongBao.contains("không tìm thấy") || thongBao.contains("không có") || thongBao.contains("no data"), 
                "Lỗi: Không hiển thị đúng thông báo rỗng. Thực tế thông báo: '" + thongBao + "'");
    }

    // ==========================================
    // 3. LUỒNG DATA: Tìm kiếm đa dạng theo Cột
    // ==========================================
    @DataProvider(name = "duLieuTimKiemNganh")
    public Object[][] provideData() {
        return new Object[][] {
            {"000001"},     // Tìm theo Mã Ngành
            {"Kiểm Thử"},   // Tìm theo Tên Ngành
            {"CNTT"}        // Tìm theo Tên Viết Tắt
        };
    }

    @Test(priority = 3, dataProvider = "duLieuTimKiemNganh")
    public void testTC03_TimKiemDataDriven(String tuKhoa) throws InterruptedException {
        System.out.println("--- LUỒNG DATA: TÌM KIẾM VỚI TỪ KHÓA '" + tuKhoa + "' ---");
        driver.navigate().refresh();
        Thread.sleep(2000);

        page.nhapTuKhoaTimKiem(tuKhoa);
        Thread.sleep(1500); 
        
        int soLuong = page.laySoLuongNganhHocHienThi();
        Assert.assertTrue(soLuong > 0, "Fail Data-driven: Nhập từ khóa đúng '" + tuKhoa + "' nhưng không tìm thấy dữ liệu!");
    }
}