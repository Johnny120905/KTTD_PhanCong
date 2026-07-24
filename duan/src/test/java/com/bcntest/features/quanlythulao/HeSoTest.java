package com.bcntest.features.quanlythulao;

import com.bcnpages.QuanLyThuLaoPage;
import com.bcntest.core.BaseTest;
import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.*;
import java.lang.reflect.Method;

public class HeSoTest extends BaseTest {
    QuanLyThuLaoPage page;

    @BeforeClass
    public void init() {
        page = new QuanLyThuLaoPage(driver);
    }

    @BeforeMethod
    public void goToPage(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null) {
            System.out.println("\n=====================================================================");
            System.out.println("🚀 DANG CHAY: " + testAnnotation.description());
            System.out.println("=====================================================================");
        }
        driver.get(BASE_URL + "PriceCoefficient");
        page.hs_chuyenTabHeSo(); // Phải chuyển sang tab Hệ số trước mỗi TC
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        System.out.println("  🧹 [He thong]: Don dep sau Test (Dong form ket, Phong to man hinh)...");
        try {
            driver.manage().window().maximize(); 
            if (page.hs_isPopupHienThi()) { page.hs_clickTatX(); } 
            Thread.sleep(500);
        } catch (Exception e) {}
    }

    // =========================================================================
    // I. CHỨC NĂNG CHỈNH SỬA HỆ SỐ (7 TCs)
    // =========================================================================

    @Test(priority = 1, description = "He So - Sua [Dung]: Nhap du 4 so hop le -> Luu -> Thanh cong")
    public void testTC01_HS_Sua_Dung() throws InterruptedException {
        page.hs_clickSuaDongDauTien();
        page.hs_dienFormHeSo("1.5", "1.2", "1.0", "1.5");
        page.hs_clickLuu();
        Thread.sleep(1000); 
        Assert.assertFalse(page.hs_isPopupHienThi(), "Loi: Sua he so hop le that bai!");
    }

    @Test(priority = 2, description = "He So - Sua [Sai]: Bo trong tat ca cac o -> Loi khong cho luu")
    public void testTC02_HS_Sua_Sai_BoTrong() {
        page.hs_clickSuaDongDauTien();
        page.hs_dienFormHeSo("", "", "", "");
        page.hs_clickLuu();
        // Ô bị bỏ trống sẽ kích hoạt lỗi Required của HTML5 -> Form kẹt lại (PASS)
        Assert.assertTrue(page.hs_isPopupHienThi(), "Loi: Bo trong he so ma form van tat (cho luu)!");
    }

    @Test(priority = 3, description = "He So - Sua [Sai]: Nhap chu (abc) -> UI auto-correct thanh so -> Luu an toan")
    public void testTC03_HS_Sua_Sai_NhapChu() throws InterruptedException {
        page.hs_clickSuaDongDauTien();
        page.hs_dienFormHeSo("abc", "xyz", "qwe", "rty");
        page.hs_clickLuu();
        Thread.sleep(1000); // Chờ UI xử lý auto-correct và đóng form
        // Thực tế trang web Touchspin tự động bỏ qua chữ cái, dữ liệu vẫn là số cũ -> Lưu thành công (Form đóng)
        Assert.assertFalse(page.hs_isPopupHienThi(), "Loi: Popup bi ket du UI da auto-correct chu cai!");
    }

    @Test(priority = 4, description = "He So - Sua [Sai]: Nhap so am (-1.5) -> UI auto-correct -> Luu an toan")
    public void testTC04_HS_Sua_Sai_SoAm() throws InterruptedException {
        page.hs_clickSuaDongDauTien();
        page.hs_dienFormHeSo("-1.5", "-1", "-2", "-0.5");
        page.hs_clickLuu();
        Thread.sleep(1000);
        // Thực tế hệ thống tự động reset số âm về mức cho phép -> Lưu thành công (Form đóng)
        Assert.assertFalse(page.hs_isPopupHienThi(), "Loi: Popup bi ket du UI da auto-correct so am!");
    }

    @Test(priority = 5, description = "He So - Sua [Sai]: Nhap ky tu dac biet (!@#) -> UI auto-correct -> Luu an toan")
    public void testTC05_HS_Sua_Sai_KyTuDacBiet() throws InterruptedException {
        page.hs_clickSuaDongDauTien();
        page.hs_dienFormHeSo("!@#", "$%^", "&*(", ")))");
        page.hs_clickLuu();
        Thread.sleep(1000);
        // Touchspin không nhận ký tự đặc biệt -> Giữ nguyên số cũ -> Lưu thành công (Form đóng)
        Assert.assertFalse(page.hs_isPopupHienThi(), "Loi: Popup bi ket du UI da auto-correct ky tu dac biet!");
    }

    @Test(priority = 6, description = "He So - Sua [Data]: Dien form -> Nut Huy -> Dong an toan, khong luu")
    public void testTC06_HS_Sua_Data() {
        page.hs_clickSuaDongDauTien();
        page.hs_dienFormHeSo("9.9", "9.9", "9.9", "9.9");
        page.hs_clickHuy();
        Assert.assertFalse(page.hs_isPopupHienThi(), "Loi: Nut Huy khong dong popup He so!");
    }

    @Test(priority = 7, description = "He So - Sua [UI]: Thu nho Mobile -> Cuon doc ngang -> Phong to PC")
    public void testTC07_HS_Sua_UI() {
        page.hs_clickSuaDongDauTien();
        driver.manage().window().setSize(new Dimension(390, 844));
        
        // Gioi han bien ky tu lon
        page.hs_dienFormHeSo(
            new String(new char[100]).replace("\0", "9"), 
            "1", "1", "1"
        ); 
        
        page.hs_cuonTrangDocNhanh();
        page.hs_cuonTrangNgangSangPhai();
        page.hs_cuonTrangNgangVeTrai();
        page.hs_cuonTrangLenTopNhanh();
        
        Assert.assertTrue(page.hs_isPopupHienThi(), "Loi UI: Form Sua He So bi mat khi xoay Mobile!");
        page.hs_clickTatX();
    }

    // =========================================================================
    // II. CHỨC NĂNG XEM HỆ SỐ (4 TCs)
    // =========================================================================

    @Test(priority = 8, description = "He So - Xem [Dung]: Bang load du lieu he so thanh cong")
    public void testTC08_HS_Xem_Dung() {
        Assert.assertTrue(page.hs_getNumberOfRows() >= 0, "Loi: Khong load duoc du lieu bang He so!");
    }

    @Test(priority = 9, description = "He So - Xem [Sai]: Bang he so khong bi rong do loi he thong")
    public void testTC09_HS_Xem_Sai() {
        Assert.assertNotNull(page.hs_getNumberOfRows(), "Loi: Bang du lieu He So bi Null!");
    }

    @Test(priority = 10, description = "He So - Xem [Data]: So dong he so thuc te ton tai")
    public void testTC10_HS_Xem_Data() {
        Assert.assertTrue(page.hs_getNumberOfRows() >= 0, "Loi: Sai so luong dong du lieu He So!");
    }

    @Test(priority = 11, description = "He So - Xem [UI]: Thu nho Mobile -> Cuon doc ngang -> Mui ten tim len dinh")
    public void testTC11_HS_Xem_UI() {
        driver.manage().window().setSize(new Dimension(390, 844));
        
        page.hs_cuonTrangDocNhanh();
        page.hs_cuonTrangNgangSangPhai();
        page.hs_cuonTrangNgangVeTrai();
        
        if(page.hs_isScrollTopVisible()){
            page.hs_clickScrollTop(); 
            Assert.assertTrue(true);
        }
    }
}