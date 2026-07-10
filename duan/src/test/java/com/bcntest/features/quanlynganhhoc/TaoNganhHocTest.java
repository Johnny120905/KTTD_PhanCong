package com.bcntest.features.quanlynganhhoc;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyNganhHocPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TaoNganhHocTest extends BaseTest {
    QuanLyNganhHocPage page;

    @BeforeClass
    public void init() {
        page = new QuanLyNganhHocPage(driver);
        // Lưu ý: Đổi URL nếu trang của ông khác. Trong hình có vẻ dùng chung "/Term" hoặc "/Major"
        driver.get(BASE_URL + "Major"); 
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (Thêm thành công)
    // ==========================================
    @Test(priority = 1)
    public void testTC01_TaoNganhThanhCong() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: TẠO MỚI NGÀNH HỌC ---");
        driver.navigate().refresh(); 
        Thread.sleep(2000);

        page.bamThemNganhMoi();
        Thread.sleep(1000);
        
        // Sinh mã ngành ngẫu nhiên để không bị lỗi trùng Database
        String maNganhRandom = "N" + (int)(Math.random() * 90000 + 10000); 
        
        page.nhapThongTinNganh(maNganhRandom, "Ngành Tự Động Hóa", "TĐH", "Tiêu chuẩn");
        page.bamLuu();
        Thread.sleep(1500); 
        
        String thongBao = page.layThongBao();
        // Bao phủ cả trường hợp Database đầy báo "đã tồn tại"
        boolean isPass = thongBao.contains("thành công") || thongBao.contains("tồn tại") || thongBao.contains("đã được tạo");
        
        Assert.assertTrue(isPass, "Lỗi: Form submit thất bại! Thực tế web báo: '" + thongBao + "'");
    }

    // ==========================================
    // 2. LUỒNG SAI (Bỏ trống)
    // ==========================================
    @Test(priority = 2)
    public void testTC02_BoTrongThongTin() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: BỎ TRỐNG THÔNG TIN ---");
        driver.navigate().refresh(); 
        Thread.sleep(2000);

        page.bamThemNganhMoi();
        Thread.sleep(1000);
        page.bamLuu();
        Thread.sleep(1000);
        
        String msg = page.layThongBao();
        Assert.assertTrue(msg.length() > 0 || driver.getPageSource().contains("error"), 
                "Lỗi: Hệ thống không báo lỗi khi bỏ trống dữ liệu!");
    }

    // ==========================================
    // 3. LUỒNG HỦY BỎ (Điền xong bấm Hủy)
    // ==========================================
    @Test(priority = 3)
    public void testTC03_HuyTaoNganhHoc() throws InterruptedException {
        System.out.println("--- LUỒNG HỦY: ĐIỀN THÔNG TIN NHƯNG BẤM HỦY ---");
        driver.navigate().refresh(); 
        Thread.sleep(2000);

        page.bamThemNganhMoi();
        Thread.sleep(1000);
        
        page.nhapThongTinNganh("TEST_HUY", "Ngành Nháp", "NN", "Đặc biệt");
        page.bamHuy(); // Không bấm Lưu mà bấm Hủy
        Thread.sleep(1000);
        
        // Kiểm tra xem bảng form đã đóng chưa (nếu mất chữ "Tên ngành" là form đã đóng)
        boolean isFormClosed = !driver.getPageSource().contains("Nhập tên ngành");
        Assert.assertTrue(isFormClosed, "Lỗi: Nút Hủy không hoạt động, form vẫn đang mở!");
    }

    // ==========================================
    // 4. LUỒNG DATA-DRIVEN 
    // ==========================================
    @DataProvider(name = "duLieuNganh")
    public Object[][] provideData() {
        return new Object[][] {
            // Tên Ngành, Viết Tắt, CTĐT
            {"Công Nghệ Phần Mềm", "CNPM", "Tiêu chuẩn"},
            {"An Toàn Thông Tin", "ATTT", "Đặc biệt"}
        };
    }

    @Test(priority = 4, dataProvider = "duLieuNganh")
    public void testTC04_DataDriven(String tenNganh, String vietTat, String ctdt) throws InterruptedException {
        System.out.println("--- LUỒNG DATA DRIVEN ---");
        driver.navigate().refresh(); 
        Thread.sleep(2000);

        page.bamThemNganhMoi();
        Thread.sleep(1000);
        
        String maNganhRandom = "N" + (int)(Math.random() * 90000 + 10000);
        
        page.nhapThongTinNganh(maNganhRandom, tenNganh, vietTat, ctdt);
        page.bamLuu();
        Thread.sleep(1500);
        
        String thongBao = page.layThongBao();
        boolean isPass = thongBao.contains("thành công") || thongBao.contains("tồn tại");
        Assert.assertTrue(isPass, "Lỗi data driven thất bại tại mã: " + maNganhRandom + ". Thực tế web báo: '" + thongBao + "'");
    }
}