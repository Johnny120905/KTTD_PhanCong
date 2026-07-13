package com.bcntest.features.quanlynganhhoc;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyNganhHocPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Dimension;

public class CapNhatNganhHocTest extends BaseTest {
    QuanLyNganhHocPage nganhHocPage;

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
    public void testF32_LuongDung_CapNhatNganhThanhCong() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: CẬP NHẬT NGÀNH HỌC TỒN TẠI ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        if (nganhHocPage.laySoLuongNganhHocHienThi() == 0) {
            System.out.println("Bảng rỗng, không có dữ liệu để Cập nhật!");
            return;
        }

        nganhHocPage.bamNutSuaDongDauTien();
        Thread.sleep(1000); 
        
        // Xác minh nghiệp vụ: Mã Ngành không được phép sửa
        Assert.assertTrue(nganhHocPage.kiemTraMaNganhBiKhoa(), "Lỗi Nghiệp vụ: Ô Mã Ngành không bị khóa trong form Cập nhật!");
        
        // Thay đổi thông tin
        String tenMoi = "Ngành Cập Nhật " + System.currentTimeMillis() % 1000;
        nganhHocPage.nhapThongTinCapNhatNganh(tenMoi, "UPD", "Tiêu chuẩn");
        Thread.sleep(1000); 
        
        nganhHocPage.bamLuu();
        Thread.sleep(1500); 
        
        String thongBao = nganhHocPage.layThongBao();
        Assert.assertTrue(thongBao.contains("thành công") || thongBao.contains("cập nhật") || thongBao.contains("success"), 
            "Lỗi: Cập nhật không thành công! Hệ thống báo: " + thongBao);
    }

    // ==========================================
    // 2. LUỒNG SAI (NEGATIVE PATH)
    // ==========================================
    @Test(priority = 2)
    public void testF32_LuongSai_BoTrongDuLieuBatBuocKhiSua() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: XÓA TRẮNG DỮ LIỆU ĐANG CÓ VÀ BẤM LƯU ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        if (nganhHocPage.laySoLuongNganhHocHienThi() == 0) return;

        nganhHocPage.bamNutSuaDongDauTien();
        Thread.sleep(1000);
        
        // Cố tình xóa trắng Tên Ngành và Viết tắt
        nganhHocPage.nhapThongTinCapNhatNganh(" ", " ", "Đặc biệt");
        nganhHocPage.bamLuu();
        Thread.sleep(1000);
        
        String thongBaoLoi = nganhHocPage.layThongBao();
        Assert.assertFalse(thongBaoLoi.isEmpty(), "Lỗi Hệ thống: Sửa dữ liệu thành rỗng nhưng không bắt validate!");
        
        nganhHocPage.bamHuy(); 
        Thread.sleep(1000);
    }

    @Test(priority = 3)
    public void testF32_LuongSai_BamNutHuyCapNhat() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: TEST NÚT HỦY KHI ĐANG CẬP NHẬT ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        if (nganhHocPage.laySoLuongNganhHocHienThi() == 0) return;

        nganhHocPage.bamNutSuaDongDauTien();
        Thread.sleep(1000);
        
        // Thay đổi data nhưng không lưu
        nganhHocPage.nhapThongTinCapNhatNganh("Ngành Chuẩn Bị Hủy", "HC", "Tiêu chuẩn");
        
        nganhHocPage.bamHuy();
        Thread.sleep(1500);
        
        String thongBao = nganhHocPage.layThongBao();
        Assert.assertTrue(thongBao.isEmpty(), "Lỗi UI: Bấm hủy form cập nhật nhưng hệ thống văng lỗi!");
    }

    // ==========================================
    // 3. LUỒNG DATA (DATA-DRIVEN)
    // ==========================================
    @DataProvider(name = "duLieuCapNhatNganh")
    public Object[][] provideData() {
        return new Object[][] {
            {"@#$$%^&*()", "KTDB", "Đặc biệt"}, // Tên chứa ký tự đặc biệt
            {"Ngành Hợp Lệ", "@#$", "Tiêu chuẩn"} // Viết tắt chứa ký tự đặc biệt
        };
    }

    @Test(priority = 4, dataProvider = "duLieuCapNhatNganh")
    public void testF32_LuongData_CapNhatVoiNhieuTruongHop(String ten, String vietTat, String ctdt) throws InterruptedException {
        System.out.println("--- LUỒNG DATA: CẬP NHẬT VỚI DỮ LIỆU ĐẶC BIỆT ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        if (nganhHocPage.laySoLuongNganhHocHienThi() == 0) return;

        nganhHocPage.bamNutSuaDongDauTien();
        Thread.sleep(1000);
        
        nganhHocPage.nhapThongTinCapNhatNganh(ten, vietTat, ctdt);
        Thread.sleep(500);
        
        nganhHocPage.bamLuu();
        Thread.sleep(1500);
        
        String thongBao = nganhHocPage.layThongBao();
        Assert.assertFalse(thongBao.isEmpty(), "Lỗi Data: Không có phản hồi khi cập nhật data dị thường!");
        
        nganhHocPage.bamHuy(); // Đóng form để an toàn cho case tiếp theo
    }

    // ==========================================
    // 4. LUỒNG GIAO DIỆN (UI/UX)
    // ==========================================
    @Test(priority = 5)
    public void testF32_LuongUI_ThuNhoPhongToVaLuoTrang() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: RESPONSIVE & SCROLL TRÊN FORM SỬA ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        if (nganhHocPage.laySoLuongNganhHocHienThi() == 0) return;

        try {
            // Giả lập Mobile View
            driver.manage().window().setSize(new Dimension(375, 812));
            Thread.sleep(1500);
            
            nganhHocPage.bamNutSuaDongDauTien();
            Thread.sleep(1500);
            
            // Lướt xuống dưới cùng của form Sửa trên mobile
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(1000);
            
            // Lướt lên
            js.executeScript("window.scrollTo(0, 0)");
            Thread.sleep(1000);
            
            // Đóng form
            nganhHocPage.bamHuy(); 
            Thread.sleep(1000);
            
        } catch (Exception e) {
            Assert.fail("Lỗi UI/UX: Form Cập Nhật bị vỡ Layout khi thu nhỏ màn hình Mobile! " + e.getMessage());
        } finally {
            driver.manage().window().maximize();
            Thread.sleep(1000);
        }
    }
}