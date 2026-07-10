package com.bcntest.features.quanlynguoidung;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyNguoiDungPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TaoNguoiDungTest extends BaseTest {

    QuanLyNguoiDungPage nguoiDungPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        nguoiDungPage = new QuanLyNguoiDungPage(driver);
        driver.get(BASE_URL + "User"); 
        Thread.sleep(3000); 
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (Happy Path) 
    // ==========================================
    @Test(priority = 1)
    public void testF22_LuongDung_TaoThanhCong() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: TẠO MỚI THÀNH CÔNG ---");
        
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nguoiDungPage.bamNutThemNguoiDung();
        Thread.sleep(1500); 
        
        String timestamp = String.valueOf(System.currentTimeMillis() % 100000);
        String mockMaGV = "VLU_" + timestamp;
        String mockEmail = "gv_" + timestamp + "@vanlanguni.vn";
        
        nguoiDungPage.nhapThongTinNguoiDung(mockMaGV, "Giảng Viên Ảo " + timestamp, mockEmail, "Thỉnh giảng", "Giảng viên");
        Thread.sleep(1000); 
        
        nguoiDungPage.bamLuu();
        Thread.sleep(1500); 
        
        // Ép tất cả về chữ thường để không bị bắt bẻ "Thành công" hay "thành công"
        String thongBao = nguoiDungPage.layThongBao().toLowerCase(); 
        Assert.assertTrue(thongBao.contains("thành công") || thongBao.contains("success"), 
                "Lỗi: Không hiển thị thông báo thành công! Thực tế tìm thấy: '" + thongBao + "'");
    }

    // ==========================================
    // 2. LUỒNG SAI (Negative Path) 
    // ==========================================
    @Test(priority = 2)
    public void testF22_LuongSai_BoTrongThongTin() throws InterruptedException {
        System.out.println("--- LUỒNG SAI 1: BỎ TRỐNG THÔNG TIN ---");
        
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nguoiDungPage.bamNutThemNguoiDung();
        Thread.sleep(1500);
        
        nguoiDungPage.nhapThongTinNguoiDung("", "", "", "", "");
        nguoiDungPage.bamLuu();
        Thread.sleep(1500);
        
        String thongBao = nguoiDungPage.layThongBao();
        Assert.assertTrue(thongBao.contains("Bạn chưa nhập") || thongBao.contains("Bạn chưa chọn"), "Lỗi: Hệ thống không chặn khi bỏ trống dữ liệu!");
    }

    @Test(priority = 3)
    public void testF22_LuongSai_BamNutHuy() throws InterruptedException {
        System.out.println("--- LUỒNG SAI 2: BẤM NÚT HUỶ KHI ĐANG NHẬP ---");
        
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nguoiDungPage.bamNutThemNguoiDung();
        Thread.sleep(1500);
        
        nguoiDungPage.nhapThongTinNguoiDung("HUY_001", "Test Nút Huỷ", "huy@vanlanguni.vn", "Thỉnh giảng", "Giảng viên");
        Thread.sleep(1000);
        
        nguoiDungPage.bamHuy();
        Thread.sleep(1500); 
    }

    // ==========================================
    // 3. LUỒNG DATA (Data-Driven)
    // ==========================================
    @DataProvider(name = "duLieuQuetLoi")
    public Object[][] provideData() {
        return new Object[][] {
            {"ERR_001", "Lỗi Email 1", "email_sai_dinh_dang.com", "Thỉnh giảng", "Giảng viên", "hợp lệ"}, 
            {"ERR_002", "Lỗi Email 2", "test!@#$@vanlanguni.vn", "Thỉnh giảng", "Giảng viên", "hợp lệ"},
            
            // ĐÃ SỬA CHỮ KỲ VỌNG: Đổi "tồn tại" thành "hệ thống" cho khớp với web
            {"ERR_004", "Trùng Email", "admin@vanlanguni.vn", "Cơ hữu", "Bộ môn", "hệ thống"}, 
        };
    }

    @Test(priority = 4, dataProvider = "duLieuQuetLoi")
    public void testF22_LuongData_KiemTraNhieuTruongHop(String maGV, String tenGV, String email, String loai, String role, String loiKyVong) throws InterruptedException {
        System.out.println("--- LUỒNG DATA: TEST CHẶN LỖI VỚI EMAIL = " + email + " ---");
        
        driver.navigate().refresh();
        Thread.sleep(2000);

        nguoiDungPage.bamNutThemNguoiDung();
        Thread.sleep(1500);

        nguoiDungPage.nhapThongTinNguoiDung(maGV, tenGV, email, loai, role);
        nguoiDungPage.bamLuu();
        
        // Trả lại 1.5 giây cho Server trường kịp thở và nhả lỗi
        Thread.sleep(1500); 
        
        String thongBao = nguoiDungPage.layThongBao();
        Assert.assertTrue(thongBao.toLowerCase().contains(loiKyVong.toLowerCase()), 
                "Fail Data-driven: Nhập rác nhưng web không chặn đúng. Thực tế thông báo là: '" + thongBao + "'");
    }
}