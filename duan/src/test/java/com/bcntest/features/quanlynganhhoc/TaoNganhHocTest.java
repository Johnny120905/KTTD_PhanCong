package com.bcntest.features.quanlynganhhoc;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyNganhHocPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Dimension;

public class TaoNganhHocTest extends BaseTest {
    QuanLyNganhHocPage nganhHocPage;
    String maNganhAuto = "AUTO" + System.currentTimeMillis() % 10000;

    @BeforeClass
    public void initPage() throws InterruptedException {
        nganhHocPage = new QuanLyNganhHocPage(driver);
        driver.get(BASE_URL + "Major"); 
        Thread.sleep(3000); 
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (HAPPY PATH)
    // ==========================================
    @Test(priority = 1)
    public void testF31_LuongDung_ThemNganhMoiThanhCong() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: THÊM NGÀNH HỌC MỚI ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nganhHocPage.bamThemNganhMoi();
        Thread.sleep(1000); 
        
        nganhHocPage.nhapThongTinNganh(maNganhAuto, "Ngành Kỹ Thuật Auto", "KTA", "Tiêu chuẩn");
        Thread.sleep(1000); 
        
        nganhHocPage.bamLuu();
        Thread.sleep(1500); 
        
        // Chỉ cần kiểm tra Toast Message báo thành công là đủ điều kiện Pass cho chức năng Tạo
        String thongBao = nganhHocPage.layThongBao();
        Assert.assertTrue(thongBao.contains("thành công") || thongBao.contains("success"), 
            "Lỗi: Không hiện thông báo thành công! Thực tế: " + thongBao);
    }

    // ==========================================
    // 2. LUỒNG SAI (NEGATIVE PATH)
    // ==========================================
    @Test(priority = 2)
    public void testF31_LuongSai_BoTrongDuLieuBatBuoc() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: BỎ TRỐNG FORM VÀ BẤM LƯU ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nganhHocPage.bamThemNganhMoi();
        Thread.sleep(1000);
        
        nganhHocPage.nhapThongTinNganh("", "", "", "");
        nganhHocPage.bamLuu();
        Thread.sleep(1000);
        
        String thongBaoLoi = nganhHocPage.layThongBao();
        Assert.assertFalse(thongBaoLoi.isEmpty(), "Lỗi Hệ thống: Bỏ trống form nhưng không bắt validate (không hiện lỗi)!");
        
        nganhHocPage.bamHuy(); 
        Thread.sleep(1000);
    }

    @Test(priority = 3)
    public void testF31_LuongSai_NhapTrungMaNganh() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: NHẬP TRÙNG MÃ NGÀNH ĐÃ TỒN TẠI ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nganhHocPage.bamThemNganhMoi();
        Thread.sleep(1000);
        
        nganhHocPage.nhapThongTinNganh(maNganhAuto, "Ngành Clone", "CLN", "Đặc biệt");
        Thread.sleep(1000); 
        
        nganhHocPage.bamLuu();
        Thread.sleep(1500);
        
        String thongBao = nganhHocPage.layThongBao();
        Assert.assertTrue(thongBao.contains("tồn tại") || thongBao.contains("trùng") || thongBao.contains("đã có"), 
            "Lỗi Nghiệp vụ: Cho phép nhập trùng Mã Ngành! Hệ thống báo: " + thongBao);
            
        nganhHocPage.bamHuy();
        Thread.sleep(1000);
    }

    @Test(priority = 4)
    public void testF31_LuongSai_BamNutHuy() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: TEST NÚT HỦY ĐÓNG FORM ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nganhHocPage.bamThemNganhMoi();
        Thread.sleep(1000);
        
        String maHuy = "HUY" + System.currentTimeMillis();
        nganhHocPage.nhapThongTinNganh(maHuy, "Ngành Bị Hủy", "NBH", "Tiêu chuẩn");
        
        // Bấm nút Hủy, form phải được đóng lại mà không sinh ra lỗi gì
        nganhHocPage.bamHuy();
        Thread.sleep(1500);
        
        String thongBao = nganhHocPage.layThongBao();
        Assert.assertTrue(thongBao.isEmpty(), "Lỗi UI: Bấm hủy form nhưng hệ thống lại văng ra lỗi hoặc thông báo lạ!");
    }

    // ==========================================
    // 3. LUỒNG DATA (DATA-DRIVEN)
    // ==========================================
    @DataProvider(name = "duLieuNganhHoc")
    public Object[][] provideData() {
        return new Object[][] {
            {"@#$$%", "Tên Ký Tự Đặc Biệt", "!@#", "Tiêu chuẩn"}, 
            {"CODE1", " ", "VT", "Đặc biệt"}, 
            {"CODE2", "Tên Ngành", "", "Tiêu chuẩn"} 
        };
    }

    @Test(priority = 5, dataProvider = "duLieuNganhHoc")
    public void testF31_LuongData_KiemTraNhieuTruongHop(String ma, String ten, String vietTat, String ctdt) throws InterruptedException {
        System.out.println("--- LUỒNG DATA: TEST BỘ DỮ LIỆU [Mã: " + ma + "] ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nganhHocPage.bamThemNganhMoi();
        Thread.sleep(1000);
        
        nganhHocPage.nhapThongTinNganh(ma, ten, vietTat, ctdt);
        Thread.sleep(500);
        
        nganhHocPage.bamLuu();
        Thread.sleep(1500);
        
        String thongBao = nganhHocPage.layThongBao();
        Assert.assertFalse(thongBao.isEmpty(), "Lỗi Data: Hệ thống không phản hồi gì khi nhập bộ dữ liệu bất thường!");
        
        nganhHocPage.bamHuy();
    }

    // ==========================================
    // 4. LUỒNG GIAO DIỆN (UI/UX)
    // ==========================================
    @Test(priority = 6)
    public void testF31_LuongUI_GioiHanBienChuoiDai() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: GIỚI HẠN BIÊN (NHẬP CHUỖI CỰC DÀI) ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nganhHocPage.bamThemNganhMoi();
        Thread.sleep(1000);
        
        String chuoiSieuDai = "A".repeat(500); 
        nganhHocPage.nhapThongTinNganh("TEST_MAX", chuoiSieuDai, "MAX", "Tiêu chuẩn");
        nganhHocPage.bamLuu();
        Thread.sleep(1500);
        
        String thongBao = nganhHocPage.layThongBao();
        Assert.assertNotNull(thongBao, "Lỗi UI: Giao diện vỡ hoặc crash server khi nhập chuỗi 500 ký tự!");
        
        nganhHocPage.bamHuy();
    }

    @Test(priority = 7)
    public void testF31_LuongUI_ThuNhoPhongToVaLuoTrang() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: RESPONSIVE (MOBILE VIEW) & SCROLL TRÊN POPUP ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        try {
            driver.manage().window().setSize(new Dimension(375, 812));
            Thread.sleep(1500);
            
            nganhHocPage.bamThemNganhMoi();
            Thread.sleep(1500);
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(1000);
            
            js.executeScript("window.scrollTo(0, 0)");
            Thread.sleep(1000);
            
            nganhHocPage.bamHuy(); 
            Thread.sleep(1000);
            
        } catch (Exception e) {
            Assert.fail("Lỗi UI/UX: Form Thêm Mới bị vỡ Layout khi thu nhỏ màn hình Mobile! " + e.getMessage());
        } finally {
            driver.manage().window().maximize();
            Thread.sleep(1000);
        }
    }
}