package com.bcntest.features.quanlyhocky;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyHocKyPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Dimension;

public class XoaHocKyTest extends BaseTest {
    QuanLyHocKyPage hocKyPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        hocKyPage = new QuanLyHocKyPage(driver);
        driver.get(BASE_URL + "Term"); 
        Thread.sleep(3000); // Chờ trang tải hoàn toàn lần đầu
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG: XÓA DỮ LIỆU ĐANG MỞ
    // ==========================================
    @Test(priority = 1)
    public void testF24_LuongDung_XoaThanhCong() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: XÓA HỌC KỲ ĐANG MỞ ---");
        
        if (!hocKyPage.kiemTraCoHocKyMo()) {
            System.out.println("Hệ thống không có dữ liệu học kỳ đang mở để xóa! Bỏ qua Test.");
            return; 
        }

        int soDongBanDau = hocKyPage.laySoLuongHocKyHienThi();
        
        hocKyPage.bamNutXoaHocKyDangMo();
        Thread.sleep(1000);
        hocKyPage.xacNhanXoaTrenPopup();
        Thread.sleep(2000); 
        
        String thongBao = hocKyPage.layThongBao();
        
        // Phân nhánh logic kiểm tra dựa trên phản hồi của hệ thống
        boolean isSuccess = thongBao.contains("thành công") || thongBao.contains("success") || thongBao.contains("xóa");
        boolean isBlockedByData = thongBao.contains("đã có dữ liệu");
        
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        Assert.assertTrue(isSuccess || isBlockedByData, "Hệ thống trả về thông báo lạ! Nội dung: " + thongBao);

        if (isBlockedByData) {
            System.out.println("-> Hệ thống chặn xóa hợp lệ do ràng buộc dữ liệu.");
            Assert.assertEquals(hocKyPage.laySoLuongHocKyHienThi(), soDongBanDau, "Báo lỗi có dữ liệu nhưng dòng vẫn bị xóa mất!");
        } else {
            System.out.println("-> Xóa thành công, không có ràng buộc.");
            Assert.assertEquals(hocKyPage.laySoLuongHocKyHienThi(), soDongBanDau - 1, "Số dòng trên bảng không giảm sau khi xóa thành công!");
        }
    }

    // ==========================================
    // 2. LUỒNG SAI: BẤM HỦY TRÊN POPUP
    // ==========================================
    @Test(priority = 2)
    public void testF24_LuongSai_HuyXoa() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: HỦY XÓA ---");
        driver.navigate().refresh(); 
        Thread.sleep(2000);
        
        if (!hocKyPage.kiemTraCoHocKyMo()) return;

        int soDongBanDau = hocKyPage.laySoLuongHocKyHienThi();

        hocKyPage.bamNutXoaHocKyDangMo();
        Thread.sleep(1000);
        hocKyPage.huyXoaTrenPopup();
        Thread.sleep(1500); 
        
        Assert.assertEquals(hocKyPage.laySoLuongHocKyHienThi(), soDongBanDau, "Lỗi: Dữ liệu bị xóa mất khi bấm Hủy!");
    }

    // ==========================================
    // 3. LUỒNG DATA: XÓA TIẾP DÒNG KHÁC
    // ==========================================
    @Test(priority = 3)
    public void testF24_LuongData_XoaTiep() throws InterruptedException {
        System.out.println("--- LUỒNG DATA: XÓA TIẾP DÒNG KHÁC ---");
        driver.navigate().refresh(); 
        Thread.sleep(2000);
        
        if (!hocKyPage.kiemTraCoHocKyMo()) return;

        int soDongBanDau = hocKyPage.laySoLuongHocKyHienThi();
        
        hocKyPage.bamNutXoaHocKyDangMo();
        Thread.sleep(1000);
        hocKyPage.xacNhanXoaTrenPopup();
        Thread.sleep(2000);
        
        String thongBao = hocKyPage.layThongBao();
        boolean isBlockedByData = thongBao.contains("đã có dữ liệu");
        
        driver.navigate().refresh(); 
        Thread.sleep(2000);
        
        if (isBlockedByData) {
            Assert.assertEquals(hocKyPage.laySoLuongHocKyHienThi(), soDongBanDau);
        } else {
            Assert.assertEquals(hocKyPage.laySoLuongHocKyHienThi(), soDongBanDau - 1);
        }
    }

    // ==========================================
    // 4. LUỒNG GIAO DIỆN: RESPONSIVE & SCROLL
    // ==========================================
    @Test(priority = 4)
    public void testF24_LuongUI_GiaoDien() throws InterruptedException {
        System.out.println("--- LUỒNG UI: KIỂM TRA MÀU, RESPONSIVE, SCROLL ---");
        driver.navigate().refresh(); 
        Thread.sleep(2000);
        
        if (hocKyPage.laySoLuongHocKyHienThi() > 0) {
             String mauNut = hocKyPage.layMauNutXoaDauTien();
             Assert.assertNotNull(mauNut, "Nút xóa không tồn tại hoặc mất màu!");
        }
        
        try {
            driver.manage().window().setSize(new Dimension(375, 812));
            Thread.sleep(1500);
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(1000);
            js.executeScript("window.scrollTo(0, 0)");
            Thread.sleep(1000);
            
        } catch (Exception e) {
            Assert.fail("Lỗi khi tương tác UI: " + e.getMessage());
        } finally {
            driver.manage().window().maximize();
            Thread.sleep(1000);
        }
    }
}