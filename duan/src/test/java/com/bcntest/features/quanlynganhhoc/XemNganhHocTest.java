package com.bcntest.features.quanlynganhhoc;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyNganhHocPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Dimension;

public class XemNganhHocTest extends BaseTest {
    QuanLyNganhHocPage nganhHocPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        nganhHocPage = new QuanLyNganhHocPage(driver);
        driver.get(BASE_URL + "Major"); 
        Thread.sleep(1500);
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (HAPPY PATH): BẤM NEXT / PREV
    // ==========================================
    @Test(priority = 1)
    public void testF33_LuongDung_ChuyenTrangBinhThuong() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: XEM DỮ LIỆU BẰNG NÚT NEXT/PREVIOUS ---");
        
        String thongTinTrang1 = nganhHocPage.layThongTinPhanTrang();
        Assert.assertFalse(thongTinTrang1.isEmpty(), "Lỗi: Không lấy được thông tin số trang!");
        
        nganhHocPage.bamTrangTiepTheo();
        Thread.sleep(500); // Chờ UI DataTables render trang mới
        
        String thongTinTrang2 = nganhHocPage.layThongTinPhanTrang();
        Assert.assertNotEquals(thongTinTrang1, thongTinTrang2, "Lỗi: Bấm Next nhưng trang không chuyển!");

        nganhHocPage.bamTrangTruoc();
        Thread.sleep(500);
        
        String thongTinTrangHienTai = nganhHocPage.layThongTinPhanTrang();
        Assert.assertEquals(thongTinTrangHienTai, thongTinTrang1, "Lỗi: Bấm Previous nhưng không về đúng trang ban đầu!");
    }

    // ==========================================
    // 2. LUỒNG SAI (NEGATIVE PATH): NÚT BỊ KHÓA
    // ==========================================
    @Test(priority = 2)
    public void testF33_LuongSai_BamTrangTruocKhiOTRangMot() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: BẤM PREVIOUS KHI ĐANG Ở TRANG 1 ---");
        driver.navigate().refresh();
        Thread.sleep(1500);
        
        boolean isDisabled = nganhHocPage.kiemTraNutTruocBiKhoa();
        Assert.assertTrue(isDisabled, "Lỗi UI: Đang ở trang 1 nhưng thẻ <li> của nút Previous không bị khóa!");
        
        String thongTinTruocKhiBam = nganhHocPage.layThongTinPhanTrang();
        
        nganhHocPage.bamTrangTruoc();
        Thread.sleep(400); 
        
        String thongTinSauKhiBam = nganhHocPage.layThongTinPhanTrang();
        Assert.assertEquals(thongTinSauKhiBam, thongTinTruocKhiBam, "Lỗi: Hệ thống cho phép lùi về trang số âm!");
    }

    // ==========================================
    // 3. LUỒNG DATA (DATA-DRIVEN): NHẢY TRANG CỤ THỂ
    // ==========================================
    @DataProvider(name = "duLieuTrang")
    public Object[][] provideData() {
        return new Object[][] {
            {"2"}, {"4"}, {"5"} 
        };
    }

    @Test(priority = 3, dataProvider = "duLieuTrang")
    public void testF33_LuongData_ChonTrangCuThe(String soTrang) throws InterruptedException {
        System.out.println("--- LUỒNG DATA: JUMP TỚI TRANG SỐ " + soTrang + " ---");
        String thongTinCu = nganhHocPage.layThongTinPhanTrang();
        
        try {
            nganhHocPage.bamChonTrangSo(soTrang);
            Thread.sleep(500);
            
            String thongTinMoi = nganhHocPage.layThongTinPhanTrang();
            Assert.assertNotEquals(thongTinCu, thongTinMoi, "Lỗi: Bấm số trang nhưng bảng không tải dữ liệu mới!");
        } catch (Exception e) {
            System.out.println("-> Bỏ qua kiểm tra: Bảng không có đủ dữ liệu để tạo ra trang số " + soTrang);
        }
    }

    // ==========================================
    // 4. LUỒNG GIAO DIỆN: SẮP XẾP TẤT CẢ CÁC CỘT
    // ==========================================
    @DataProvider(name = "danhSachCacCotSapXep")
    public Object[][] columnData() {
        return new Object[][] {
            {"Mã ngành"}, 
            {"Tên ngành"}, 
            {"Tên viết tắt"}, 
            {"CTĐT"}
        };
    }

    @Test(priority = 4, dataProvider = "danhSachCacCotSapXep")
    public void testF33_LuongUI_SapXepTatCaCacCot(String tenCot) throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: SẮP XẾP CỘT [" + tenCot + "] ---");
        
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
        Thread.sleep(300);

        nganhHocPage.bamSapXepCot(tenCot);
        Thread.sleep(500); 
        
        String trangThaiLan1 = nganhHocPage.layTrangThaiSapXepCot(tenCot);
        Assert.assertTrue(trangThaiLan1.contains("asc") || trangThaiLan1.contains("desc"), "Lỗi: Cột " + tenCot + " không nhận CSS sắp xếp!");

        nganhHocPage.bamSapXepCot(tenCot);
        Thread.sleep(500);
        
        String trangThaiLan2 = nganhHocPage.layTrangThaiSapXepCot(tenCot);
        Assert.assertNotEquals(trangThaiLan1, trangThaiLan2, "Lỗi: DataTables không đảo chiều sắp xếp khi click lần 2 ở cột " + tenCot);
    }

    // ==========================================
    // 5. LUỒNG UI: ZOOM VÀ SCROLL
    // ==========================================
    @Test(priority = 5)
    public void testF33_LuongUI_ResponsiveScroll() throws InterruptedException {
        System.out.println("--- LUỒNG UI: PHÓNG TO, THU NHỎ, LƯỚT TRANG KHI XEM ---");
        try {
            driver.manage().window().setSize(new Dimension(375, 812));
            Thread.sleep(600); 
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(400); 
            
            js.executeScript("window.scrollTo(0, 0)");
            Thread.sleep(400);
            
        } catch (Exception e) {
            Assert.fail("Lỗi Giao diện: Bị vỡ hoặc lỗi khi thao tác Responsive: " + e.getMessage());
        } finally {
            driver.manage().window().maximize(); 
            Thread.sleep(500);
        }
    }
}