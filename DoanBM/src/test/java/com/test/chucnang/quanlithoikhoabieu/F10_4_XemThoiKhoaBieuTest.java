package com.test.chucnang.quanlithoikhoabieu;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.test.BoMonCommonTest;
public class F10_4_XemThoiKhoaBieuTest extends BoMonCommonTest {
      /*
     * F10.4 - LUỒNG ĐÚNG
     * Xem thời khóa biểu cá nhân thành công
     */
    @Test(priority = 1)
    public void F10_4_POS_XemThoiKhoaBieuCaNhanThanhCong()
            throws InterruptedException {

        navigateToAssignPage();

        WebElement xemTKB = waitClickable(
                By.xpath(
                        "//*[contains(normalize-space(),'Xem TKB') "
                                + "or contains(normalize-space(),'Xem thời khóa biểu') "
                                + "or contains(normalize-space(),'Xem thời khoá biểu')]"
                )
        );

        Assert.assertNotNull(
                xemTKB,
                "Không tìm thấy nút Xem TKB"
        );

        clickElement(xemTKB);

        Thread.sleep(3000);

        String page = driver.getPageSource();

        Assert.assertTrue(
                page.contains("Thời khóa biểu")
                        || page.contains("Thời khoá biểu")
                        || page.contains("TKB")
                        || page.contains("Lịch")
                        || page.contains("Bộ môn"),
                "Không thấy nội dung thời khóa biểu cá nhân"
        );

        System.out.println("PASS F10.4 POS - Xem thời khóa biểu cá nhân thành công");
        System.out.println("URL hiện tại: " + driver.getCurrentUrl());
    }

    /*
     * F10.4 - LUỒNG DATA
     * Kiểm tra nhiều dữ liệu xác nhận nội dung sau khi bấm Xem TKB
     */
    @DataProvider(name = "F10_4_TKBVerifyData")
    public Object[][] F10_4_TKBVerifyData() {
        return new Object[][]{
                {"Kiểm tra chữ Thời khóa biểu", new String[]{"Thời khóa biểu", "Thời khoá biểu"}},
                {"Kiểm tra chữ TKB", new String[]{"TKB", "Thời khóa biểu", "Thời khoá biểu"}},
                {"Kiểm tra chữ Lịch", new String[]{"Lịch", "Thời khóa biểu", "Thời khoá biểu"}},
                {"Kiểm tra chữ Bộ môn", new String[]{"Bộ môn", "Thời khóa biểu", "Thời khoá biểu"}}
        };
    }

    @Test(priority = 2, dataProvider = "F10_4_TKBVerifyData")
    public void F10_4_DATA_XemThoiKhoaBieuCaNhan(String testName, String[] expectedTexts)
            throws InterruptedException {

        navigateToAssignPage();

        WebElement xemTKB = waitClickable(
                By.xpath(
                        "//*[contains(normalize-space(),'Xem TKB') "
                                + "or contains(normalize-space(),'Xem thời khóa biểu') "
                                + "or contains(normalize-space(),'Xem thời khoá biểu')]"
                )
        );

        Assert.assertNotNull(
                xemTKB,
                "Không tìm thấy nút Xem TKB - " + testName
        );

        clickElement(xemTKB);

        Thread.sleep(3000);

        String page = driver.getPageSource();

        Assert.assertTrue(
                containsAny(page, expectedTexts),
                "Không thấy dữ liệu xác nhận thời khóa biểu cá nhân: " + testName
        );

        System.out.println("PASS F10.4 DATA - " + testName);
        System.out.println("URL hiện tại: " + driver.getCurrentUrl());
    }

    /*
     * F10.4 - LUỒNG SAI
     * Chưa đăng nhập thì không được xem thời khóa biểu cá nhân
     */
    @Test(priority = 99)
    public void F10_4_NEG_ChuaDangNhapKhongXemDuocThoiKhoaBieuCaNhan()
            throws InterruptedException {

        clearBrowserSession();

        driver.get(ASSIGN_URL);

        Thread.sleep(3000);

        boolean hasXemTKBButton = hasVisibleXemTKBButton();

        Assert.assertFalse(
                hasXemTKBButton,
                "Chưa đăng nhập nhưng vẫn thấy nút Xem TKB"
        );

        String page = driver.getPageSource();

        boolean hasPersonalTimetable =
                page.contains("Thời khóa biểu cá nhân")
                        || page.contains("Thời khoá biểu cá nhân")
                        || page.contains("Xem TKB cá nhân");

        Assert.assertFalse(
                hasPersonalTimetable,
                "Chưa đăng nhập nhưng vẫn thấy nội dung thời khóa biểu cá nhân"
        );

        System.out.println("PASS F10.4 NEG - Chưa đăng nhập thì không xem được thời khóa biểu cá nhân");
    }

    public boolean hasVisibleXemTKBButton() {

        try {
            List<WebElement> buttons = driver.findElements(
                    By.xpath(
                            "//*[contains(normalize-space(),'Xem TKB') "
                                    + "or contains(normalize-space(),'Xem thời khóa biểu') "
                                    + "or contains(normalize-space(),'Xem thời khoá biểu')]"
                    )
            );

            for (WebElement button : buttons) {
                try {
                    if (button.isDisplayed() && button.isEnabled()) {
                        return true;
                    }
                } catch (Exception ignored) {
                }
            }

        } catch (Exception ignored) {
        }

        return false;
    }
}
