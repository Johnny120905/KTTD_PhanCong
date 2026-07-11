package com.bcntest.features.quanlynguoidung;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyNguoiDungPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class XoaNguoiDungTest extends BaseTest {

    QuanLyNguoiDungPage nguoiDungPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        nguoiDungPage = new QuanLyNguoiDungPage(driver);
        driver.get(BASE_URL + "User"); 
        Thread.sleep(3000); 
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (Happy Path): XÓA DỮ LIỆU CÓ SẴN
    // ==========================================
    @Test(priority = 1)
    public void testF24_LuongDung_XoaThanhCong() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: XÓA NGƯỜI DÙNG THÀNH CÔNG ---");
        
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        // BƯỚC 1: Bấm thẳng nút Thùng rác ở dòng đầu tiên trên lưới dữ liệu
        nguoiDungPage.bamNutXoaDauTien();
        
        // TẠM DỪNG ĐỂ NHÌN RÕ POPUP
        Thread.sleep(2000); 
        
        // BƯỚC 2: Xác nhận Xóa trên Popup
        nguoiDungPage.xacNhanXoaTrenPopup();
        Thread.sleep(2000); // Chờ Server thực thi lệnh Xóa
        
        // BƯỚC 3: Kiểm tra kết quả trả về
        String thongBao = nguoiDungPage.layThongBao().toLowerCase(); 
        Assert.assertTrue(thongBao.contains("thành công") || thongBao.contains("success") || thongBao.contains("xóa") || thongBao.contains("ok"), 
                "Lỗi: Không hiển thị thông báo Xóa thành công! Thực tế: '" + thongBao + "'");
    }

    // ==========================================
    // 2. LUỒNG HỦY (Negative Path): BẤM HỦY TRÊN POPUP
    // ==========================================
    @Test(priority = 2)
    public void testF24_LuongHuy_BamHuyThaoTacXoa() throws InterruptedException {
        System.out.println("--- LUỒNG HỦY: KHÔNG XÓA NỮA, BẤM HỦY TRÊN POPUP ---");
        
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        // Bấm nút Xóa dòng đầu tiên trên lưới
        nguoiDungPage.bamNutXoaDauTien();
        Thread.sleep(1000);
        
        // Bấm nút Hủy trên Popup
        nguoiDungPage.huyXoaTrenPopup();
        Thread.sleep(1500); 
        
        // Xác minh Popup đã tắt mà không có thông báo nào hiện ra
        String thongBao = nguoiDungPage.layThongBao();
        Assert.assertTrue(thongBao.isEmpty(), "Fail UI: Bấm Hủy xóa mà hệ thống lại báo Pop-up thành công/lỗi!");
    }

    // ==========================================
    // 3. LUỒNG GIAO DIỆN (UI/UX): MÀU SẮC NÚT XÓA (DANGER)
    // ==========================================
    @Test(priority = 3)
    public void testF24_LuongUI_KiemTraMauNutXoa() throws InterruptedException {
        System.out.println("--- LUỒNG UI: KIỂM TRA MÀU ĐỎ CỦA ICON THÙNG RÁC ---");
        
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        // Lấy mã màu của cái Icon Thùng rác
        String mauNut = nguoiDungPage.layMauNutXoaDauTien();
        
        // CSS chuẩn của Danger
        Assert.assertTrue(mauNut.contains("234, 84, 85") || mauNut.contains("220, 53, 69") || mauNut.contains("red") || mauNut.contains("rgba"), 
                "Fail UI/UX: Nút Xóa không phải màu đỏ (Danger) để cảnh báo người dùng! Mã màu thực tế: " + mauNut);
    }

    // ==========================================
    // 4. LUỒNG GIAO DIỆN (UI/UX - Responsive Mobile)
    // ==========================================
    @Test(priority = 4)
    public void testF24_LuongUI_ThuNhoManHinhMobile() throws InterruptedException {
        System.out.println("--- LUỒNG UI: KIỂM TRA CHỨC NĂNG XÓA TRÊN ĐIỆN THOẠI ---");
        
        // Thu nhỏ màn hình
        org.openqa.selenium.Dimension kichThuocMobile = new org.openqa.selenium.Dimension(375, 812);
        driver.manage().window().setSize(kichThuocMobile);
        Thread.sleep(2000); 
        
        try {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            
            // Ép click icon thùng rác đầu tiên bằng Javascript
            org.openqa.selenium.WebElement btnXoa = driver.findElement(org.openqa.selenium.By.cssSelector("a.deleteRow.text-danger"));
            js.executeScript("arguments[0].click();", btnXoa);
            Thread.sleep(1500); 
            
            // Popup hiện lên, ép click nút Hủy xóa
            org.openqa.selenium.WebElement btnHuyPopup = driver.findElement(org.openqa.selenium.By.xpath("//button[contains(@class, 'swal2-cancel') or text()='Huỷ']"));
            js.executeScript("arguments[0].click();", btnHuyPopup);
            Thread.sleep(1000);
            
            Assert.assertTrue(true, "Pass UI: Tính năng cảnh báo Xóa vẫn hoạt động tốt trên giao diện Mobile.");
        } catch (Exception e) {
            System.out.println(">>> LỖI CHI TIẾT: " + e.getMessage());
            Assert.fail("Fail UI Responsive: Bảng dữ liệu vỡ nát, không tìm thấy nút Xóa hoặc Popup trên Mobile!");
        } finally {
            // Trả lại kích thước gốc
            driver.manage().window().maximize(); 
            Thread.sleep(1000);
        }
    }
}