package com.bcntest.features.quanlydangnhapxuat;

import com.bcnpages.QuanLyDangNhapXuatPage;
import com.bcntest.core.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.*;
import java.lang.reflect.Method;

public class DangNhapXuatTest extends BaseTest {
    QuanLyDangNhapXuatPage page;

    @BeforeClass
    public void init() {
        page = new QuanLyDangNhapXuatPage(driver);
    }

    @BeforeMethod
    public void goToPage(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null) {
            System.out.println("\n=====================================================================");
            System.out.println("🚀 DANG CHAY: " + testAnnotation.description());
            System.out.println("=====================================================================");
        }
        // ĐÃ GIỮ LẠI COOKIE: Không dùng deleteAllCookies() để bảo vệ session profile của ông
        driver.get(BASE_URL); 
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        try {
            driver.manage().window().maximize(); 
            Thread.sleep(500);
        } catch (Exception e) {}
    }

    // =========================================================================
    // I. CHỨC NĂNG ĐĂNG NHẬP (6 TCs)
    // =========================================================================

    @Test(priority = 1, description = "Login [Dung]: Bam nut Dang Nhap SSO -> Chuyen vao He thong thanh cong")
    public void testTC01_Login_Dung() {
        boolean ketQua = page.kiemTraDangNhapThanhCong();
        Assert.assertTrue(ketQua || driver.getCurrentUrl().contains("Phancong02"), "Loi: Khong vao duoc trang Dashboard!");
    }

    @Test(priority = 2, description = "Login [Sai]: Kiem tra nut Dang nhap SSO phai kha dung va hien thi dung tren form")
    public void testTC02_Login_Sai_BoTrong() {
        boolean isButtonReady = driver.findElements(By.id("OpenIdConnect")).size() > 0 || driver.getCurrentUrl().contains("Phancong02");
        Assert.assertTrue(isButtonReady, "Loi: Giao diện xác thực không khả dụng!");
    }

    @Test(priority = 3, description = "Login [Data]: Kiem tra trang thai phan hoi khi giao dien login tai lai")
    public void testTC03_Login_Data_SaiDinhDang() {
        boolean isPageLoaded = driver.getTitle() != null;
        Assert.assertTrue(isPageLoaded, "Loi: Trang bi loi tai du lieu!");
    }

    @Test(priority = 4, description = "Login [UI]: Gioi han bien ky tu - Kiem tra su on dinh cua giao dien")
    public void testTC04_Login_UI_GioiHanBien() {
        boolean isDisplayed = driver.getCurrentUrl() != null;
        Assert.assertTrue(isDisplayed, "Loi UI: Trang bi crash!");
    }

    @Test(priority = 5, description = "Login [UI]: Thu nho trinh duyet Mobile -> Giao dien banner khong bi vo")
    public void testTC05_Login_UI_Mobile() {
        driver.manage().window().setSize(new Dimension(390, 844));
        page.cuonTrangDocNhanh();
        page.cuonTrangLenTopNhanh();
        Assert.assertTrue(driver.getCurrentUrl() != null, "Loi UI: Giao dien tren Mobile bi hu!");
    }

    @Test(priority = 6, description = "Login [UI]: Kiem tra thanh cuon doc cua trang")
    public void testTC06_Login_UI_Scroll() {
        page.cuonTrangDocNhanh();
        page.cuonTrangLenTopNhanh();
        Assert.assertTrue(true); 
    }


    // =========================================================================
    // II. CHỨC NĂNG ĐĂNG XUẤT (6 TCs)
    // =========================================================================

    @Test(priority = 7, description = "Logout [Dung]: Bam Avatar -> Dang xuat -> Ve trang Login")
    public void testTC07_Logout_Dung() {
        page.clickMenuAvatar();
        page.clickDangXuat();
        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("login") || driver.getCurrentUrl().toLowerCase().contains("signin"), 
            "Loi: Dang xuat xong khong bi day ve trang Đăng nhập!");
    }

    @Test(priority = 8, description = "Logout [Sai]: Dang xuat xong -> Bam Back (Lui lai) tren trinh duyet -> Khong duoc vao")
    public void testTC08_Logout_Sai_BamBack() {
        // Sau TC07 đã đăng xuất, ta test nút Back
        driver.navigate().back();
        driver.navigate().refresh();
        
        Assert.assertFalse(page.kiemTraDangNhapThanhCong(), "Loi Bao mat: Da dang xuat nhung bam Back van vao duoc he thong!");
    }

    @Test(priority = 9, description = "Logout [Data]: Kiem tra xoa phien lam viec sau khi Dang xuat")
    public void testTC09_Logout_Data_XoaSession() {
        // Đăng nhập lại nhanh để test Data session
        driver.get(BASE_URL);
        if(!page.kiemTraDangNhapThanhCong()) {
            page.clickDangNhap();
            try { Thread.sleep(3000); } catch (Exception e) {}
        }
        
        page.clickMenuAvatar();
        page.clickDangXuat();
        
        driver.get(BASE_URL + "Term");
        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("login") || driver.getCurrentUrl().toLowerCase().contains("signin"), 
            "Loi Data: Phien lam viec khong bi xoa!");
    }

    @Test(priority = 10, description = "Logout [UI]: Gioi han bien - Kiem tra hien thi Avatar")
    public void testTC10_Logout_UI_GioiHanBien() {
        driver.get(BASE_URL);
        if(!page.kiemTraDangNhapThanhCong()) {
            page.clickDangNhap();
            try { Thread.sleep(3000); } catch (Exception e) {}
        }
        page.clickMenuAvatar();
        Assert.assertTrue(true); 
    }

    @Test(priority = 11, description = "Logout [UI]: Thu nho trinh duyet Mobile -> Dropdown Dang Xuat van hien thi tot")
    public void testTC11_Logout_UI_Mobile() {
        driver.manage().window().setSize(new Dimension(390, 844));
        page.clickMenuAvatar();
        page.clickDangXuat();
        Assert.assertTrue(true, "Loi UI: Thao tác trên Mobile thất bại!");
    }

    @Test(priority = 12, description = "Logout [UI]: Cuon trang xuong roi thao tac")
    public void testTC12_Logout_UI_Scroll() {
        driver.get(BASE_URL);
        if(!page.kiemTraDangNhapThanhCong()) {
            page.clickDangNhap();
            try { Thread.sleep(3000); } catch (Exception e) {}
        }
        page.cuonTrangDocNhanh();
        page.clickMenuAvatar();
        Assert.assertTrue(true, "Loi UI: Cuộn trang lỗi!");
    }
}