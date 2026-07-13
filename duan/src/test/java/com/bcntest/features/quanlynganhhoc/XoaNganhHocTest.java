package com.bcntest.features.quanlynganhhoc;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyNganhHocPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Dimension;

public class XoaNganhHocTest extends BaseTest {
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
    public void testF34_LuongDung_XoaNganhHoc() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: XÓA NGÀNH HỌC (CHẤP NHẬN CẢ VIỆC BỊ CHẶN BỞI RÀNG BUỘC) ---");
        driver.navigate().refresh();
        Thread.sleep(2000);

        int soDongBanDau = nganhHocPage.laySoLuongNganhHocHienThi();
        if (soDongBanDau == 0) {
            System.out.println("Bảng rỗng, không có dữ liệu để xóa!");
            return;
        }

        // Bấm nút xóa (thùng rác) dòng đầu tiên
        nganhHocPage.bamNutXoaDauTien();
        Thread.sleep(1000);

        // Kiểm tra nội dung popup SweetAlert
        String tieuDe = nganhHocPage.layTieuDePopupXoa();
        String noiDung = nganhHocPage.layNoiDungPopupXoa();
        Assert.assertEquals(tieuDe, "Thông báo", "Lỗi UI: Tiêu đề popup xóa không đúng!");
        Assert.assertTrue(noiDung.contains("chắc muốn xoá"), "Lỗi UI: Nội dung cảnh báo xóa không đúng!");

        // Xác nhận xóa
        nganhHocPage.xacNhanXoaTrenPopup();
        Thread.sleep(2000); // Đợi API Server xử lý xóa

        String thongBao = nganhHocPage.layThongBao();
        System.out.println("Hệ thống phản hồi: " + thongBao);
        
        // Phân nhánh logic dựa trên ràng buộc Database
        boolean isSuccess = thongBao.contains("thành công") || thongBao.contains("success") || thongBao.contains("xoá");
        boolean isBlockedByData = thongBao.contains("đã có dữ liệu") || thongBao.contains("không thể xoá");

        Assert.assertTrue(isSuccess || isBlockedByData, "Lỗi: Hệ thống trả về thông báo lạ sau khi bấm xóa! Nội dung: " + thongBao);

        driver.navigate().refresh();
        Thread.sleep(2000);

        if (isBlockedByData) {
            System.out.println("-> Ngành học đang có dữ liệu liên quan, hệ thống chặn xóa là chính xác.");
            Assert.assertEquals(nganhHocPage.laySoLuongNganhHocHienThi(), soDongBanDau, "Lỗi Data: Hệ thống báo lỗi nhưng dòng vẫn bị xóa mất!");
        } else {
            System.out.println("-> Không có ràng buộc, xóa thành công.");
            Assert.assertEquals(nganhHocPage.laySoLuongNganhHocHienThi(), soDongBanDau - 1, "Lỗi Data: Hệ thống báo thành công nhưng số lượng dòng không giảm!");
        }
    }

    // ==========================================
    // 2. LUỒNG SAI (NEGATIVE PATH)
    // ==========================================
    @Test(priority = 2)
    public void testF34_LuongSai_HuyThaoTacXoa() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: BẤM HỦY TRÊN POPUP XÁC NHẬN ---");
        driver.navigate().refresh(); 
        Thread.sleep(2000);
        
        int soDongBanDau = nganhHocPage.laySoLuongNganhHocHienThi();
        if (soDongBanDau == 0) return;

        nganhHocPage.bamNutXoaDauTien();
        Thread.sleep(1000);
        
        // Bấm nút Hủy màu đỏ
        nganhHocPage.huyXoaTrenPopup();
        Thread.sleep(1500); 
        
        driver.navigate().refresh();
        Thread.sleep(2000);
        Assert.assertEquals(nganhHocPage.laySoLuongNganhHocHienThi(), soDongBanDau, "Lỗi Nghiệp vụ: Bấm Hủy nhưng dữ liệu vẫn bị xóa mất!");
    }

    // ==========================================
    // 3. LUỒNG DATA (DATA-DRIVEN / LIÊN TỤC)
    // ==========================================
    @Test(priority = 3)
    public void testF34_LuongData_ThaoTacXoaLienTuc() throws InterruptedException {
        System.out.println("--- LUỒNG DATA: XÓA TIẾP DÒNG KHÁC (STRESS TEST CƠ BẢN) ---");
        driver.navigate().refresh(); 
        Thread.sleep(2000);
        
        int soDongBanDau = nganhHocPage.laySoLuongNganhHocHienThi();
        if (soDongBanDau < 2) {
            System.out.println("Bảng không đủ 2 dòng dữ liệu để test luồng xóa liên tục!");
            return;
        }

        // Xóa dòng đầu tiên hiện tại
        nganhHocPage.bamNutXoaDauTien();
        Thread.sleep(1000);
        nganhHocPage.xacNhanXoaTrenPopup();
        Thread.sleep(2000);
        
        String thongBao = nganhHocPage.layThongBao();
        boolean isBlockedByData = thongBao.contains("đã có dữ liệu") || thongBao.contains("không thể");
        
        driver.navigate().refresh(); 
        Thread.sleep(2000);
        
        if (isBlockedByData) {
            Assert.assertEquals(nganhHocPage.laySoLuongNganhHocHienThi(), soDongBanDau);
        } else {
            Assert.assertEquals(nganhHocPage.laySoLuongNganhHocHienThi(), soDongBanDau - 1);
        }
    }

    // ==========================================
    // 4. LUỒNG GIAO DIỆN (UI/UX)
    // ==========================================
    @Test(priority = 4)
    public void testF34_LuongUI_KiemTraGiaoDienResponsive() throws InterruptedException {
        System.out.println("--- LUỒNG UI: KIỂM TRA MÀU SẮC, RESPONSIVE, SCROLL ---");
        driver.navigate().refresh(); 
        Thread.sleep(2000);
        
        if (nganhHocPage.laySoLuongNganhHocHienThi() > 0) {
             String mauNut = nganhHocPage.layMauNutXoaDauTien();
             // Nút xóa thường có class text-danger, mã màu trả về là rgba
             Assert.assertNotNull(mauNut, "Lỗi UI: Nút xóa (thùng rác) không tồn tại hoặc bị mất định dạng CSS màu sắc!");
        }
        
        try {
            // Giả lập Mobile
            driver.manage().window().setSize(new Dimension(375, 812));
            Thread.sleep(1500);
            
            // Lướt xuống dưới cùng
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(1000);
            
            // Lướt lên đầu
            js.executeScript("window.scrollTo(0, 0)");
            Thread.sleep(1000);
            
            // Có thể click xóa thử trên giao diện mobile để đảm bảo popup không bị vỡ layout
            if (nganhHocPage.laySoLuongNganhHocHienThi() > 0) {
                nganhHocPage.bamNutXoaDauTien();
                Thread.sleep(1000);
                nganhHocPage.huyXoaTrenPopup(); // Hủy để an toàn data
            }
            
        } catch (Exception e) {
            Assert.fail("Lỗi UI/UX: Vỡ giao diện hoặc mất phần tử khi tương tác trên màn hình Mobile thu nhỏ! " + e.getMessage());
        } finally {
            driver.manage().window().maximize();
            Thread.sleep(1000);
        }
    }
}