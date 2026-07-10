package com.test.chucnang.dangnhapdangxuat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.test.BoMonCommonTest;


public class F9_1_BoMonTest  extends BoMonCommonTest  {
     /*
     * F9.1 - LUỒNG ĐÚNG
     * Đăng nhập Bộ môn thành công
     */
    @Test(priority = 1)
    public void F9_1_POS_DangNhapBoMonThanhCong() throws InterruptedException {

        navigateToAssignPage();

        String page = driver.getPageSource();

        Assert.assertTrue(
                page.contains("Bộ môn")
                        || page.contains("Thời khóa biểu")
                        || page.contains("Thời khoá biểu")
                        || page.contains("Phân công"),
                "Không xác nhận được người dùng đã đăng nhập role Bộ môn"
        );

        System.out.println("PASS F9.1 POS - Đăng nhập Bộ môn thành công");
    }

    /*
     * F9.1 - LUỒNG DATA
     * Kiểm tra nhiều dữ liệu xác nhận sau khi đăng nhập
     */
    @DataProvider(name = "F9_1_LoginVerifyData")
    public Object[][] F9_1_LoginVerifyData() {
        return new Object[][]{
                {"Kiểm tra role Bộ môn", new String[]{"Bộ môn"}},
                {"Kiểm tra màn hình Phân công", new String[]{"Phân công", "Phân công giảng dạy"}},
                {"Kiểm tra bộ lọc Học kỳ", new String[]{"Học kỳ", "Học kì"}},
                {"Kiểm tra bộ lọc Ngành", new String[]{"Ngành"}}
        };
    }

    @Test(priority = 2, dataProvider = "F9_1_LoginVerifyData")
    public void F9_1_DATA_DangNhapBoMon(String testName, String[] expectedTexts)
            throws InterruptedException {

        navigateToAssignPage();

        String page = driver.getPageSource();

        Assert.assertTrue(
                containsAny(page, expectedTexts),
                "Không tìm thấy dữ liệu xác nhận đăng nhập Bộ môn: " + testName
        );

        System.out.println("PASS F9.1 DATA - " + testName);
    }

    /*
     * F9.1 - LUỒNG ĐÚNG
     * Đăng xuất Bộ môn thành công
     */
    @Test(priority = 90)
    public void F9_1_POS_DangXuatBoMonThanhCong() throws InterruptedException {

        navigateToAssignPage();

        openUserMenu();

        WebElement logoutButton = waitClickable(
                By.xpath(
                        "//*[contains(normalize-space(),'Đăng xuất') "
                                + "or contains(normalize-space(),'Logout') "
                                + "or contains(normalize-space(),'Sign out')]"
                )
        );

        clickElement(logoutButton);

        Thread.sleep(3000);

        boolean logoutSuccess =
                driver.getCurrentUrl().contains("/Account/Login")
                        || driver.getPageSource().contains("Đăng nhập bằng tài khoản")
                        || driver.getPageSource().contains("Đăng nhập");

        Assert.assertTrue(
                logoutSuccess,
                "Đăng xuất chưa thành công hoặc chưa quay về trang đăng nhập"
        );

        System.out.println("PASS F9.1 POS - Đăng xuất Bộ môn thành công");
    }

    /*
     * F9.1 - LUỒNG SAI
     * Chưa đăng nhập nhưng truy cập trực tiếp trang Phân công
     */
    @Test(priority = 99)
    public void F9_1_NEG_ChuaDangNhapKhongVaoDuocTrangPhanCong()
            throws InterruptedException {

        clearBrowserSession();

        driver.get(ASSIGN_URL);

        Thread.sleep(3000);

        String page = driver.getPageSource();
        String currentUrl = driver.getCurrentUrl();

        boolean isLoginPage =
                currentUrl.contains("/Account/Login")
                        || page.contains("Đăng nhập")
                        || page.contains("Sign in")
                        || page.contains("Microsoft");

        boolean stillInAssignPage =
                page.contains("Phân công giảng dạy")
                        || page.contains("Số lớp đã phân công")
                        || page.contains("Chưa phân");

        Assert.assertTrue(
                isLoginPage || !stillInAssignPage,
                "Chưa đăng nhập nhưng vẫn vào được trang Phân công"
        );

        System.out.println("PASS F9.1 NEG - Chưa đăng nhập thì không truy cập được trang Phân công");
    }
}
