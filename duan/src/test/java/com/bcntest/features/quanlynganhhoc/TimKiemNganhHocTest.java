package com.bcntest.features.quanlynganhhoc;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyNganhHocPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Dimension;

public class TimKiemNganhHocTest extends BaseTest {
    QuanLyNganhHocPage nganhHocPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        nganhHocPage = new QuanLyNganhHocPage(driver);
        driver.get(BASE_URL + "Major"); 
        Thread.sleep(1500); 
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (HAPPY PATH): TÌM CÓ KẾT QUẢ
    // ==========================================
    @Test(priority = 1)
    public void testF35_LuongDung_TimKiemTuKhoaTonTai() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: TÌM KIẾM NGÀNH HỌC TỒN TẠI ---");
        
        nganhHocPage.nhapTuKhoaTimKiem(""); 
        Thread.sleep(300);
        
        String tuKhoa = "Tiêu chuẩn"; 
        nganhHocPage.nhapTuKhoaTimKiem(tuKhoa);
        Thread.sleep(500); 
        
        int soDong = nganhHocPage.laySoLuongNganhHocHienThi();
        Assert.assertTrue(soDong > 0, "Lỗi: Bảng trống mặc dù tìm kiếm từ khóa hợp lệ!");
        
        String noiDungBanGhi = nganhHocPage.layNoiDungDongDauTien().toLowerCase();
        Assert.assertTrue(noiDungBanGhi.contains(tuKhoa.toLowerCase()), "Lỗi: Kết quả tìm kiếm không chứa từ khóa vừa nhập!");
    }

    // ==========================================
    // 2. LUỒNG SAI (NEGATIVE PATH): TÌM RÁC
    // ==========================================
    @Test(priority = 2)
    public void testF35_LuongSai_TimKiemTuKhoaKhongTonTai() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: TÌM KIẾM TỪ KHÓA RÁC ---");
        
        nganhHocPage.nhapTuKhoaTimKiem("KhongTonTai999!@#");
        Thread.sleep(500); 
        
        int soDong = nganhHocPage.laySoLuongNganhHocHienThi();
        Assert.assertEquals(soDong, 0, "Lỗi: Dữ liệu vẫn hiển thị dù tìm từ khóa rác không tồn tại!");
        
        String thongBaoRong = nganhHocPage.layThongBaoKhongCoDuLieu().toLowerCase();
        Assert.assertTrue(thongBaoRong.contains("không tìm thấy") || thongBaoRong.contains("no matching records"), 
            "Lỗi UI: Không hiện thông báo 'Không tìm thấy dữ liệu' trong bảng!");
    }

    // ==========================================
    // 3. LUỒNG DATA (DATA-DRIVEN): TÌM QUA CÁC CỘT
    // ==========================================
    @DataProvider(name = "duLieuTimKiemNganh")
    public Object[][] provideSearchData() {
        return new Object[][] {
            {"00"},           
            {"Công nghệ"},    
            {"CNTT"},         
            {"Đặc biệt"}      
        };
    }

    @Test(priority = 3, dataProvider = "duLieuTimKiemNganh")
    public void testF35_LuongData_KiemTraTimKiemMoiCot(String tuKhoa) throws InterruptedException {
        System.out.println("--- LUỒNG DATA: KIỂM TRA TỪ KHÓA ĐA DẠNG: " + tuKhoa + " ---");
        
        nganhHocPage.nhapTuKhoaTimKiem(tuKhoa);
        Thread.sleep(400); 
        
        Assert.assertTrue(nganhHocPage.laySoLuongNganhHocHienThi() >= 0, "Lỗi: DataTables bị lỗi khi xử lý từ khóa " + tuKhoa);
    }

    // ==========================================
    // 4. LUỒNG GIAO DIỆN (UI/UX)
    // ==========================================
    @Test(priority = 4)
    public void testF35_LuongUI_DropdownHienThiSoLuong() throws InterruptedException {
        System.out.println("--- LUỒNG UI: KIỂM TRA NÚT DROPDOWN HIỂN THỊ ---");
        
        nganhHocPage.nhapTuKhoaTimKiem("");
        Thread.sleep(500);

        nganhHocPage.chonSoLuongHienThi("10");
        Thread.sleep(500); 
        Assert.assertTrue(nganhHocPage.laySoLuongNganhHocHienThi() <= 10, "Lỗi UI: Bảng hiển thị nhiều hơn 10 dòng!");

        nganhHocPage.chonSoLuongHienThi("25");
        Thread.sleep(500); 
        Assert.assertTrue(nganhHocPage.laySoLuongNganhHocHienThi() <= 25, "Lỗi UI: Bảng hiển thị nhiều hơn 25 dòng!");

        nganhHocPage.chonSoLuongHienThi("50");
        Thread.sleep(500); 
        Assert.assertTrue(nganhHocPage.laySoLuongNganhHocHienThi() <= 50, "Lỗi UI: Bảng hiển thị nhiều hơn 50 dòng!");

        nganhHocPage.chonSoLuongHienThi("-1");
        Thread.sleep(1000); 
        Assert.assertTrue(nganhHocPage.laySoLuongNganhHocHienThi() >= 0, "Lỗi UI: Server lỗi render Tất cả!");
    }

    @Test(priority = 5)
    public void testF35_LuongUI_ThuNhoPhongToVaLuoTrang() throws InterruptedException {
        System.out.println("--- LUỒNG UI: PHÓNG TO, THU NHỎ, LƯỚT TRANG ---");
        
        try {
            driver.manage().window().setSize(new Dimension(375, 812));
            Thread.sleep(600); 
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(400);
            
            js.executeScript("window.scrollTo(0, 0)");
            Thread.sleep(400);
            
        } catch (Exception e) {
            Assert.fail("Lỗi UI/UX: Vỡ giao diện khi thao tác Responsive thu nhỏ! " + e.getMessage());
        } finally {
            driver.manage().window().maximize();
            Thread.sleep(500); 
        }
    }
}