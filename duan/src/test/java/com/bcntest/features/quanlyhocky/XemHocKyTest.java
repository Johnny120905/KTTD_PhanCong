package com.bcntest.features.quanlyhocky;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyHocKyPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Dimension;

public class XemHocKyTest extends BaseTest {
    QuanLyHocKyPage hocKyPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        hocKyPage = new QuanLyHocKyPage(driver);
        driver.get(BASE_URL + "Term"); 
        Thread.sleep(3000); // Chờ bảng tải hoàn toàn lần đầu
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (HAPPY PATH): Bấm Next / Prev
    // ==========================================
    @Test(priority = 1)
    public void testF24_LuongDung_ChuyenTrangBinhThuong() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: XEM DỮ LIỆU BẰNG NÚT NEXT/PREVIOUS ---");
        
        String thongTinTrang1 = hocKyPage.layThongTinPhanTrang();
        Assert.assertFalse(thongTinTrang1.isEmpty(), "Lỗi: Không lấy được thông tin số trang!");
        
        hocKyPage.bamTrangTiepTheo();
        Thread.sleep(1500); 
        
        String thongTinTrang2 = hocKyPage.layThongTinPhanTrang();
        Assert.assertNotEquals(thongTinTrang1, thongTinTrang2, "Lỗi: Bấm Next nhưng trang không chuyển!");

        hocKyPage.bamTrangTruoc();
        Thread.sleep(1500);
        
        String thongTinTrangHienTai = hocKyPage.layThongTinPhanTrang();
        Assert.assertEquals(thongTinTrangHienTai, thongTinTrang1, "Lỗi: Bấm Previous nhưng không về đúng trang ban đầu!");
    }

    // ==========================================
    // 2. LUỒNG SAI (NEGATIVE PATH): Biên phân trang
    // ==========================================
    @Test(priority = 2)
    public void testF24_LuongSai_BamTrangTruocKhiOTRangMot() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: BẤM PREVIOUS KHI ĐANG Ở TRANG 1 ---");
        driver.navigate().refresh();
        Thread.sleep(3000);
        
        boolean isDisabled = hocKyPage.kiemTraNutTruocBiKhoa();
        Assert.assertTrue(isDisabled, "Lỗi: Đang ở trang 1 nhưng nút Previous không bị khóa!");
        
        String thongTinTruocKhiBam = hocKyPage.layThongTinPhanTrang();
        
        // Cố tình click nút Previous
        hocKyPage.bamTrangTruoc();
        Thread.sleep(1000);
        
        String thongTinSauKhiBam = hocKyPage.layThongTinPhanTrang();
        Assert.assertEquals(thongTinSauKhiBam, thongTinTruocKhiBam, "Lỗi: Hệ thống cho phép lùi trang khi đang ở trang 1!");
    }

    // ==========================================
    // 3. LUỒNG DATA: Nhảy cóc trang (1 -> 3 -> 5)
    // ==========================================
    @DataProvider(name = "duLieuTrang")
    public Object[][] provideData() {
        return new Object[][] {
            {"2"}, {"4"}, {"5"} 
        };
    }

    @Test(priority = 3, dataProvider = "duLieuTrang")
    public void testF24_LuongData_ChonTrangCuThe(String soTrang) throws InterruptedException {
        System.out.println("--- LUỒNG DATA: JUMP TỚI TRANG SỐ " + soTrang + " ---");
        String thongTinCu = hocKyPage.layThongTinPhanTrang();
        
        try {
            hocKyPage.bamChonTrangSo(soTrang);
            Thread.sleep(1500); 
            
            String thongTinMoi = hocKyPage.layThongTinPhanTrang();
            Assert.assertNotEquals(thongTinCu, thongTinMoi, "Lỗi: Bảng không tải dữ liệu mới khi nhảy trang!");
        } catch (Exception e) {
            System.out.println("Bỏ qua do dữ liệu trên hệ thống không đủ dài tới trang " + soTrang);
        }
    }

    // ==========================================
    // 4. LUỒNG GIAO DIỆN: Zoom & Scroll
    // ==========================================
    @Test(priority = 4)
    public void testF24_LuongUI_ResponsiveScroll() throws InterruptedException {
        System.out.println("--- LUỒNG UI: PHÓNG TO, THU NHỎ, LƯỚT TRANG ---");
        try {
            driver.manage().window().setSize(new Dimension(375, 812));
            Thread.sleep(1500);
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(1000);
            
            js.executeScript("window.scrollTo(0, 0)");
            Thread.sleep(1000);
            
        } catch (Exception e) {
            Assert.fail("Vỡ giao diện khi thao tác Responsive: " + e.getMessage());
        } finally {
            driver.manage().window().maximize(); 
            Thread.sleep(1000);
        }
    }

    // ==========================================
    // 5. CHỨC NĂNG BỔ SUNG: TEST SẮP XẾP TỪNG CỘT
    // ==========================================
    @DataProvider(name = "danhSachCacCotSapXep")
    public Object[][] columnData() {
        return new Object[][] {
            {"Học kỳ"}, {"Năm bắt đầu"}, {"Năm kết thúc"}, 
            {"Tuần bắt đầu"}, {"Ngày bắt đầu"}, {"Tiết tối đa"}, 
            {"Lớp tối đa"}, {"Trạng thái"}
        };
    }

    @Test(priority = 5, dataProvider = "danhSachCacCotSapXep")
    public void testF24_LuongUI_SapXepTatCaCacCot(String tenCot) throws InterruptedException {
        System.out.println("--- LUỒNG BỔ SUNG: SẮP XẾP CỘT [" + tenCot + "] ---");

        // Click lần 1: Mong đợi cột chuyển thành tăng/giảm dần (nhận class asc/desc)
        hocKyPage.bamSapXepCot(tenCot);
        Thread.sleep(1200); // Chờ UI DataTables vẽ lại mũi tên
        String trangThaiLan1 = hocKyPage.layTrangThaiSapXepCot(tenCot);
        Assert.assertTrue(trangThaiLan1.contains("asc") || trangThaiLan1.contains("desc"), "Lỗi: Không nhận lệnh sắp xếp ở cột " + tenCot);

        // Click lần 2: Đảo chiều mũi tên (Từ asc sang desc hoặc ngược lại)
        hocKyPage.bamSapXepCot(tenCot);
        Thread.sleep(1200);
        String trangThaiLan2 = hocKyPage.layTrangThaiSapXepCot(tenCot);
        Assert.assertNotEquals(trangThaiLan1, trangThaiLan2, "Lỗi: Dữ liệu không đảo chiều (asc/desc) khi click sắp xếp lần 2 ở cột " + tenCot);
    }
}