package com.bcntest.features.quanlyhocky;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyHocKyPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Dimension;

public class KhoaHocKyTest extends BaseTest {
    QuanLyHocKyPage hocKyPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        hocKyPage = new QuanLyHocKyPage(driver);
        driver.get(BASE_URL + "Term"); 
        Thread.sleep(3000); // Chờ trang tải hoàn toàn lần đầu
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (HAPPY PATH): Đổi trạng thái 1 lần
    // ==========================================
    @Test(priority = 1)
    public void testF24_LuongDung_ThayDoiTrangThaiThanhCong() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: THAY ĐỔI TRẠNG THÁI (KHÓA / MỞ KHÓA) ---");
        
        if (hocKyPage.laySoLuongHocKyHienThi() == 0) {
            System.out.println("Bảng trống, không có dữ liệu để test!");
            return;
        }

        // Lấy trạng thái hiện tại (true = Mở, false = Đóng)
        boolean trangThaiBanDau = hocKyPage.kiemTraTrangThaiDongDauTien();
        System.out.println("Trạng thái ban đầu: " + (trangThaiBanDau ? "Mở" : "Khóa"));

        // Click để đảo ngược trạng thái
        hocKyPage.bamToggleTrangThaiDongDauTien();
        Thread.sleep(1500); // Đợi gọi API editStatus
        
        // Kiểm tra thông báo (Toast/Swal)
        String thongBao = hocKyPage.layThongBao();
        boolean isSuccess = thongBao.contains("thành công") || thongBao.contains("cập nhật") || thongBao.contains("success");
        Assert.assertTrue(isSuccess, "Lỗi: Thông báo đổi trạng thái không hiển thị hoặc báo lỗi! Text: " + thongBao);

        // Kiểm tra checkbox đã thực sự đổi chưa
        boolean trangThaiMoi = hocKyPage.kiemTraTrangThaiDongDauTien();
        Assert.assertNotEquals(trangThaiBanDau, trangThaiMoi, "Lỗi: Bấm đổi trạng thái nhưng checkbox không thay đổi!");

        // TRẢ LẠI TRẠNG THÁI CŨ ĐỂ KHÔNG LÀM HỎNG DỮ LIỆU
        hocKyPage.bamToggleTrangThaiDongDauTien();
        Thread.sleep(1500);
    }

    // ==========================================
    // 2. LUỒNG DATA (STRESS TEST): Click liên tục
    // ==========================================
    @Test(priority = 2)
    public void testF24_LuongData_ClickLienTucNhieuLan() throws InterruptedException {
        System.out.println("--- LUỒNG DATA: CLICK LIÊN TỤC VÀO NÚT TRẠNG THÁI ---");
        driver.navigate().refresh();
        Thread.sleep(3000);

        if (hocKyPage.laySoLuongHocKyHienThi() == 0) return;

        boolean trangThaiBanDau = hocKyPage.kiemTraTrangThaiDongDauTien();

        // Spam click 3 lần (nếu ban đầu là Mở -> Click 3 lần sẽ thành Khóa)
        for (int i = 0; i < 3; i++) {
            hocKyPage.bamToggleTrangThaiDongDauTien();
            Thread.sleep(500); // Khoảng nghỉ cực ngắn để test API có handle kịp không
        }

        Thread.sleep(2000); // Đợi server xử lý xong các request dồn dập
        
        boolean trangThaiSauKhiSpam = hocKyPage.kiemTraTrangThaiDongDauTien();
        // Do click lẻ lần (3 lần), trạng thái cuối cùng phải khác ban đầu
        Assert.assertNotEquals(trangThaiBanDau, trangThaiSauKhiSpam, "Lỗi: Hệ thống không ghi nhận đúng trạng thái khi click liên tục!");

        // Trả lại trạng thái cũ bằng 1 click nữa (để chẵn 4 lần click = về ban đầu)
        hocKyPage.bamToggleTrangThaiDongDauTien();
        Thread.sleep(1500);
    }

    // ==========================================
    // 3. LUỒNG GIAO DIỆN (UI/UX): Zoom & Scroll
    // ==========================================
    @Test(priority = 3)
    public void testF24_LuongUI_ResponsiveScroll() throws InterruptedException {
        System.out.println("--- LUỒNG UI: PHÓNG TO, THU NHỎ, LƯỚT TRANG ---");
        
        try {
            // Thu nhỏ (Giả lập màn hình Mobile)
            driver.manage().window().setSize(new Dimension(375, 812));
            Thread.sleep(1500);
            
            // Lướt xuống
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(1000);
            
            // Lướt lên
            js.executeScript("window.scrollTo(0, 0)");
            Thread.sleep(1000);
            
            // Phóng to (Desktop)
            driver.manage().window().maximize();
            Thread.sleep(1500);
            
        } catch (Exception e) {
            Assert.fail("Vỡ giao diện khi thao tác Responsive: " + e.getMessage());
        } finally {
            driver.manage().window().maximize(); 
        }
    }
}