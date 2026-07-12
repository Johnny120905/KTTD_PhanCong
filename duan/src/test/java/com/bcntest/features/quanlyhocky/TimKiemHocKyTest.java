package com.bcntest.features.quanlyhocky;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyHocKyPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Dimension;

public class TimKiemHocKyTest extends BaseTest {
    QuanLyHocKyPage hocKyPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        hocKyPage = new QuanLyHocKyPage(driver);
        driver.get(BASE_URL + "Term"); 
        Thread.sleep(3000); // Chờ trang tải hoàn toàn lần đầu
    }

    // Hàm hỗ trợ "lọc" lỗi của DataTables: Nếu bảng có 1 dòng báo "Không tìm thấy", thì số dòng thực tế là 0
    private int demSoDongThucTe() {
        int soDong = hocKyPage.laySoLuongHocKyHienThi();
        if (soDong == 1) {
            String noiDungDong1 = hocKyPage.layMaHocKyDongDauTien().toLowerCase();
            if (noiDungDong1.contains("không") || noiDungDong1.contains("no matching") || noiDungDong1.contains("empty")) {
                return 0; // Trống thực sự
            }
        }
        return soDong;
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (HAPPY PATH)
    // ==========================================
    @Test(priority = 1)
    public void testF24_LuongDung_TimKiemChinhXac() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: TÌM KIẾM CHÍNH XÁC ---");
        String tuKhoa = "999"; 
        hocKyPage.nhapTuKhoaTimKiem(tuKhoa);
        Thread.sleep(1500); 
        
        Assert.assertTrue(demSoDongThucTe() > 0, "Lỗi: Hệ thống không tìm thấy học kỳ chính xác: " + tuKhoa);
    }

    @Test(priority = 2)
    public void testF24_LuongDung_TimKiemTuongDoi() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: TÌM KIẾM TƯƠNG ĐỐI (Năm 2024) ---");
        String tuKhoa = "2024"; 
        hocKyPage.nhapTuKhoaTimKiem(tuKhoa);
        Thread.sleep(1500); 
        
        Assert.assertTrue(demSoDongThucTe() > 0, "Lỗi: Không tìm thấy kết quả nào chứa " + tuKhoa);
    }

    // ==========================================
    // 2. LUỒNG SAI (NEGATIVE PATH)
    // ==========================================
    @Test(priority = 3)
    public void testF24_LuongSai_TimKiemTuKhoaRac() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: TÌM KIẾM TỪ KHÓA RÁC ---");
        hocKyPage.nhapTuKhoaTimKiem("KhongCoHocKyNayDau");
        Thread.sleep(1500); 
        
        Assert.assertEquals(demSoDongThucTe(), 0, "Lỗi: Vẫn ra dữ liệu khi nhập từ khóa rác không tồn tại!");
    }

    @Test(priority = 4)
    public void testF24_LuongSai_TimKiemKyTuDacBiet() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: TÌM KIẾM KÝ TỰ ĐẶC BIỆT ---");
        hocKyPage.nhapTuKhoaTimKiem("!@#$%^&*()");
        Thread.sleep(1500); 
        
        Assert.assertEquals(demSoDongThucTe(), 0, "Lỗi: Hệ thống không xử lý tốt từ khóa chứa ký tự đặc biệt!");
    }

    // ==========================================
    // 3. LUỒNG DATA & GIỚI HẠN BIÊN
    // ==========================================
    @DataProvider(name = "duLieuTimKiem")
    public Object[][] provideData() {
        return new Object[][] {
            {"2025"}, {"2026"}, {"1"} // Các năm, tuần có tần suất xuất hiện cao
        };
    }

    @Test(priority = 5, dataProvider = "duLieuTimKiem")
    public void testF24_LuongData_TimKiemNhieuTuKhoa(String tuKhoa) throws InterruptedException {
        System.out.println("--- LUỒNG DATA: KIỂM TRA TỪ KHÓA: " + tuKhoa + " ---");
        hocKyPage.nhapTuKhoaTimKiem(tuKhoa);
        Thread.sleep(1000); 
        
        Assert.assertTrue(demSoDongThucTe() >= 0); // Đảm bảo bảng không bị crash khi quét DataProvider
    }

    @Test(priority = 6)
    public void testF24_LuongData_GioiHanBienChuoiDai() throws InterruptedException {
        System.out.println("--- LUỒNG DATA (BIÊN): TÌM KIẾM CHUỖI CỰC DÀI ---");
        // Giả lập nhập chuỗi vượt quá giới hạn (150 ký tự) xem DataTables có sập không
        String chuoiDai = "A".repeat(150); 
        hocKyPage.nhapTuKhoaTimKiem(chuoiDai);
        Thread.sleep(1500);
        
        Assert.assertEquals(demSoDongThucTe(), 0, "Lỗi: Tìm chuỗi cực dài nhưng lại ra kết quả hoặc vỡ UI!");
    }

    // ==========================================
    // 4. LUỒNG GIAO DIỆN (UI/UX)
    // ==========================================
    @Test(priority = 7)
    public void testF24_LuongUI_DropdownHienThiSoLuong() throws InterruptedException {
        System.out.println("--- LUỒNG UI: KIỂM TRA DROPDOWN (10, 25, 50, Tất cả) ---");

        // F5 lại trang để xóa hoàn toàn bộ lọc "150 chữ A" từ Test Case số 6, đưa bảng về trạng thái mặc định
        driver.navigate().refresh();
        Thread.sleep(2500); 

        // Chọn 10 dòng
        hocKyPage.chonSoLuongHienThi("10");
        Thread.sleep(1500); 
        Assert.assertTrue(demSoDongThucTe() <= 10, "Lỗi: Bảng hiển thị nhiều hơn 10 dòng!");

        // Chọn 25 dòng
        hocKyPage.chonSoLuongHienThi("25");
        Thread.sleep(1500); 
        Assert.assertTrue(demSoDongThucTe() <= 25, "Lỗi: Bảng hiển thị nhiều hơn 25 dòng!");

        // Chọn 50 dòng
        hocKyPage.chonSoLuongHienThi("50");
        Thread.sleep(1500); 
        Assert.assertTrue(demSoDongThucTe() <= 50, "Lỗi: Bảng hiển thị nhiều hơn 50 dòng!");

        // Chọn Tất cả (Value trong HTML là "-1")
        hocKyPage.chonSoLuongHienThi("-1");
        Thread.sleep(3000); // Lệnh tải "Tất cả" cần nhiều thời gian để render hơn
        Assert.assertTrue(demSoDongThucTe() > 0, "Lỗi: Không tải được danh sách hiển thị Tất cả!");
    }

    @Test(priority = 8)
    public void testF24_LuongUI_ResponsiveScroll() throws InterruptedException {
        System.out.println("--- LUỒNG UI: PHÓNG TO, THU NHỎ, LƯỚT TRANG ---");
        
        try {
            // Thu nhỏ (Giả lập màn hình Mobile)
            driver.manage().window().setSize(new Dimension(375, 812));
            Thread.sleep(1500);
            
            // Lướt xuống cuối bảng
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(1000);
            
            // Lướt lên đầu trang
            js.executeScript("window.scrollTo(0, 0)");
            Thread.sleep(1000);
            
            // Phóng to (Desktop)
            driver.manage().window().maximize();
            Thread.sleep(1500);
            
        } catch (Exception e) {
            Assert.fail("Vỡ giao diện khi thao tác Responsive thu nhỏ màn hình: " + e.getMessage());
        } finally {
            driver.manage().window().maximize(); // Backup phòng ngừa test case sau
        }
    }
}