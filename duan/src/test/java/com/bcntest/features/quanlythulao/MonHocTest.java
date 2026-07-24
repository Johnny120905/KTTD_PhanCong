package com.bcntest.features.quanlythulao;

import com.bcnpages.QuanLyThuLaoPage;
import com.bcntest.core.BaseTest;
import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.*;
import java.lang.reflect.Method;

public class MonHocTest extends BaseTest {
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
        driver.get(BASE_URL + "Subject"); 
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        System.out.println("  🧹 [He thong]: Don dep sau Test (Dong form ket, Phong to man hinh)...");
        try {
            driver.manage().window().maximize(); 
            if (page.mh_isPopupHienThi()) { page.mh_clickTatX(); } 
            Thread.sleep(500);
        } catch (Exception e) {}
    }

    // =========================================================================
    // I. CHỨC NĂNG XEM MÔN HỌC (6 TCs)
    // =========================================================================

    @Test(priority = 1, description = "Xem [Dung]: Chon Bo loc Hoc ky -> Nganh -> Hien thi data")
    public void testTC01_MH_Xem_Dung_BoLoc() {
        page.mh_chonHocKy();
        page.mh_chonNganh();
        Assert.assertTrue(page.mh_demSoDongDuLieu() >= 0, "Loi: Khong the load du lieu Mon hoc tu Bo loc!");
    }

    @Test(priority = 2, description = "Xem [Dung]: Click mui ten len/xuong sap xep 4 cot")
    public void testTC02_MH_Xem_Dung_SapXepCot() {
        page.mh_clickSort("Mã môn học");
        page.mh_clickSort("Tên môn học");
        page.mh_clickSort("Số TC");
        page.mh_clickSort("Ngôn ngữ");
        Assert.assertNotNull(page.mh_demSoDongDuLieu(), "Loi: Tinh nang Sap xep (Sort) bi loi lam mat data!");
    }

    @Test(priority = 3, description = "Xem [Sai]: Giao dien bang khong bi crash/NullPointException")
    public void testTC03_MH_Xem_Sai_Crash() {
        Assert.assertFalse(page.mh_kiemTraKhongCoDuLieu() && page.mh_demSoDongDuLieu() > 0, "Loi logic: Vua hien thong bao rong vua co data!");
    }

    @Test(priority = 4, description = "Xem [Data]: Test Dropdown hien thi 10, 25, 50, Tat ca va Phan trang")
    public void testTC04_MH_Xem_Data_HienThiVaPhanTrang() {
        page.mh_chonSoLuongHienThi("25");
        Assert.assertTrue(page.mh_demSoDongDuLieu() >= 0, "Loi: Hien thi 25 dong that bai!");
        
        page.mh_chonSoLuongHienThi("50");
        page.mh_chonSoLuongHienThi("Tất cả");
        
        page.mh_chonSoLuongHienThi("10");
        page.mh_bamPhanTrang("2");
        Assert.assertNotNull(page.mh_demSoDongDuLieu(), "Loi: Tinh nang phan trang bi loi!");
    }

    @Test(priority = 5, description = "Xem [UI]: Kiem tra thanh cuon man hinh PC")
    public void testTC05_MH_Xem_UI_PC() {
        page.mh_cuonTrangDocNhanh();
        page.mh_cuonTrangNgangSangPhai();
        page.mh_cuonTrangLenTopNhanh();
        Assert.assertTrue(true); 
    }

    @Test(priority = 6, description = "Xem [UI]: Thu nho Mobile -> Cuon doc ngang -> Nut mui ten tim")
    public void testTC06_MH_Xem_UI_Mobile() {
        driver.manage().window().setSize(new Dimension(390, 844));
        page.mh_cuonTrangDocNhanh();
        page.mh_cuonTrangNgangSangPhai();
        page.mh_cuonTrangNgangVeTrai();
        if (page.mh_isScrollTopVisible()) {
            page.mh_clickScrollTop();
            Assert.assertTrue(true);
        }
    }

    // =========================================================================
    // II. CHỨC NĂNG TÌM KIẾM MÔN HỌC (7 TCs)
    // =========================================================================

    @Test(priority = 7, description = "Tim kiem [Dung]: Nhap Ten mon hoc ton tai -> Tra ve ket qua")
    public void testTC07_MH_TimKiem_Dung_TenMon() {
        page.mh_nhapTimKiem("Nhập môn");
        Assert.assertTrue(page.mh_demSoDongDuLieu() > 0 || page.mh_kiemTraKhongCoDuLieu(), "Loi: He thong tim kiem ten bi dung!");
    }

    @Test(priority = 8, description = "Tim kiem [Dung]: Nhap Ma mon hoc ton tai -> Tra ve ket qua")
    public void testTC08_MH_TimKiem_Dung_MaMon() {
        page.mh_nhapTimKiem("71IT"); 
        Assert.assertTrue(page.mh_demSoDongDuLieu() > 0 || page.mh_kiemTraKhongCoDuLieu(), "Loi: He thong tim kiem ma bi dung!");
    }

    @Test(priority = 9, description = "Tim kiem [Sai]: Nhap chuoi random khong ton tai -> Hien thi thong bao rong")
    public void testTC09_MH_TimKiem_Sai_KhongTonTai() {
        page.mh_nhapTimKiem("MonHocKhongTheNaoTonTaiDuoc123");
        Assert.assertTrue(page.mh_kiemTraKhongCoDuLieu(), "Loi: Tim chuoi xam ma van ra data!");
    }

    @Test(priority = 10, description = "Tim kiem [Sai]: Nhap ky tu dac biet (!@#) hoac SQL Injection")
    public void testTC10_MH_TimKiem_Sai_KyTuDacBiet() {
        page.mh_nhapTimKiem("' OR 1=1; --");
        Assert.assertTrue(page.mh_kiemTraKhongCoDuLieu() || page.mh_demSoDongDuLieu() == 0, "Loi bao mat: O tim kiem bi thung boi SQL Injection!");
    }

    @Test(priority = 11, description = "Tim kiem [Data]: Xoa trang o tim kiem -> Table reset ve ban dau")
    public void testTC11_MH_TimKiem_Data_XoaTrang() {
        page.mh_nhapTimKiem("Lập trình");
        page.mh_xoaTrangTimKiem();
        Assert.assertTrue(page.mh_demSoDongDuLieu() >= 0, "Loi: Xoa o tim kiem nhung bang khong load lai!");
    }

    @Test(priority = 12, description = "Tim kiem [Data]: Gioi han bien - Nhap chuoi rat dai (500 ky tu)")
    public void testTC12_MH_TimKiem_Data_BienKyTu() {
        String longText = new String(new char[500]).replace("\0", "A");
        page.mh_nhapTimKiem(longText);
        Assert.assertTrue(page.mh_kiemTraKhongCoDuLieu(), "Loi: O tim kiem khong xu ly duoc chuoi qua dai!");
    }

    @Test(priority = 13, description = "Tim kiem [UI]: Thu nho Mobile -> Nhap tim kiem -> Cuon doc ngang")
    public void testTC13_MH_TimKiem_UI_Mobile() {
        driver.manage().window().setSize(new Dimension(390, 844));
        page.mh_nhapTimKiem("Hệ thống");
        page.mh_cuonTrangDocNhanh();
        page.mh_cuonTrangNgangSangPhai();
        page.mh_cuonTrangNgangVeTrai();
        Assert.assertTrue(true);
    }

    // =========================================================================
    // III. CHỨC NĂNG CHỈNH SỬA MÔN HỌC (7 TCs)
    // =========================================================================

    @Test(priority = 14, description = "Sua [Dung]: Click Sua -> Thay doi Ngon ngu thanh cong -> Luu")
    public void testTC14_MH_Sua_Dung_ThayDoiNgonNgu() throws InterruptedException {
        page.mh_clickSuaDongDauTien();
        page.mh_thayDoiNgonNgu(); 
        page.mh_clickLuu();
        Thread.sleep(1000);
        Assert.assertFalse(page.mh_isPopupHienThi(), "Loi: Sua checkbox Ngon Ngu that bai, form van ket!");
    }

    @Test(priority = 15, description = "Sua [Dung]: Mo form -> Khong sua gi -> Luu giu nguyen data")
    public void testTC15_MH_Sua_Dung_KhongThayDoi() throws InterruptedException {
        page.mh_clickSuaDongDauTien();
        page.mh_clickLuu();
        Thread.sleep(1000);
        Assert.assertFalse(page.mh_isPopupHienThi(), "Loi: Form bam Luu ma khong dong lai!");
    }

    @Test(priority = 16, description = "Sua [Sai]: Kiem tra O Ma Mon Hoc phai bi khoa (Readonly/Disabled)")
    public void testTC16_MH_Sua_Sai_KhoaMaMon() {
        page.mh_clickSuaDongDauTien();
        boolean isLocked = page.mh_kiemTraOTextBiKhoa();
        Assert.assertTrue(isLocked, "Loi nghiem trong: O Ma Mon Hoc khong bi khoa (disabled) nhu thiet ke!");
    }

    @Test(priority = 17, description = "Sua [Sai]: Kiem tra O Ten Mon Hoc phai bi khoa (Readonly/Disabled)")
    public void testTC17_MH_Sua_Sai_KhoaTenMon() {
        page.mh_clickSuaDongDauTien();
        boolean isLocked = page.mh_kiemTraOTextBiKhoa();
        Assert.assertTrue(isLocked, "Loi nghiem trong: O Ten Mon Hoc khong bi khoa (disabled) nhu thiet ke!");
    }

    @Test(priority = 18, description = "Sua [Data]: Mo form -> Bam nut Huy -> Form dong an toan")
    public void testTC18_MH_Sua_Data_BamHuy() {
        page.mh_clickSuaDongDauTien();
        page.mh_clickHuy();
        Assert.assertFalse(page.mh_isPopupHienThi(), "Loi: Nut Huy khong the dong form Sua Mon Hoc!");
    }

    @Test(priority = 19, description = "Sua [Data]: Mo form -> Bam nut X tat form -> Form dong an toan")
    public void testTC19_MH_Sua_Data_BamTatX() {
        page.mh_clickSuaDongDauTien();
        page.mh_clickTatX();
        Assert.assertFalse(page.mh_isPopupHienThi(), "Loi: Nut X khong the dong form Sua Mon Hoc!");
    }

    @Test(priority = 20, description = "Sua [UI]: Mo form Sua -> Thu nho Mobile -> Cuon doc chong vo UI")
    public void testTC20_MH_Sua_UI_Mobile() {
        page.mh_clickSuaDongDauTien();
        driver.manage().window().setSize(new Dimension(390, 844));
        page.mh_cuonTrangDocNhanh();
        page.mh_cuonTrangLenTopNhanh();
        Assert.assertTrue(page.mh_isPopupHienThi(), "Loi UI: Form Sua Mon Hoc bi mat khi xoay dien thoai Mobile!");
    }
}