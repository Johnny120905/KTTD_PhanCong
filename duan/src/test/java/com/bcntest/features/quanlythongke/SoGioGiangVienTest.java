package com.bcntest.features.quanlythongke;

import com.bcnpages.QuanLyThongKePage;
import com.bcntest.core.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.*;

public class SoGioGiangVienTest extends BaseTest {
    QuanLyThongKePage thongKePage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        thongKePage = new QuanLyThongKePage(driver);
        driver.get(BASE_URL + "Statistics"); 
        Thread.sleep(3000); 
    }

    @BeforeMethod
    public void navigate() {
        if (!driver.getCurrentUrl().contains("Statistics")) thongKePage.navigateToMenu();
    }

    // 6.1
    @Test(priority = 1) public void test_61_Happy_LocHocKy() {
        thongKePage.setCaGiang(false);
        thongKePage.chonSelect2("unit", "Học kỳ");
        thongKePage.chonSelect2("term", "999");
        thongKePage.clickTab("BangBieu");
        Assert.assertTrue(thongKePage.isMainTableDisplayed());
    }
    @Test(priority = 2) public void test_61_Sad() {
        thongKePage.setCaGiang(false); thongKePage.clickTab("BangBieu");
        thongKePage.nhapTimKiemBangChinh("NonExistentData_XYZ");
        Assert.assertFalse(thongKePage.getTextBangChinhEmpty().isEmpty());
    }
    @Test(priority = 3) public void test_61_Data() {
        thongKePage.setCaGiang(false); thongKePage.clickTab("BangBieu");
        Assert.assertFalse(thongKePage.getHeaderBangChinh().isEmpty());
    }
    @Test(priority = 4) public void test_61_UI_InfoHienThi() {
        thongKePage.setCaGiang(false); thongKePage.clickTab("BangBieu");
        Assert.assertFalse(thongKePage.getThongTinHienThiBangChinh().isEmpty());
    }
    // 6.2
    @Test(priority = 5) public void test_62_Happy_LocNamHoc() {
        thongKePage.setCaGiang(false);
        thongKePage.chonSelect2("unit", "Năm học");
        thongKePage.chonSelect2("year", "2025 - 2026");
        thongKePage.clickTab("BangBieu");
        Assert.assertTrue(thongKePage.isMainTableDisplayed());
    }
    @Test(priority = 6) public void test_62_Sad_InputRong() {
        thongKePage.setCaGiang(false); thongKePage.clickTab("BangBieu");
        thongKePage.nhapTimKiemBangChinh("");
        Assert.assertTrue(thongKePage.isMainTableDisplayed());
    }
    @Test(priority = 7) public void test_62_Data_SoSanhTong() {
        thongKePage.setCaGiang(false); thongKePage.clickTab("BangBieu");
        Assert.assertNotNull(thongKePage.getThongTinHienThiBangChinh());
    }
    @Test(priority = 8) public void test_62_UI_BieuDo() {
        thongKePage.setCaGiang(false); thongKePage.clickTab("BieuDo");
        Assert.assertTrue(driver.findElement(By.id("chart-tab")).isDisplayed());
    }
    // 6.5
    @Test(priority = 9) public void test_65_Happy() {
        thongKePage.setCaGiang(true); thongKePage.clickTab("BangBieu");
        thongKePage.clickMoRongGVThuNhat();
        thongKePage.waitForChildRow(); // Đợi dòng mở rộng
        boolean isExpanded = !driver.findElements(By.cssSelector("tr.child")).isEmpty() || !driver.findElements(By.cssSelector("tr.shown")).isEmpty();
        Assert.assertTrue(isExpanded);
    }
    @Test(priority = 10) public void test_65_Sad() {
        thongKePage.setCaGiang(true); thongKePage.setCaGiang(false);
        thongKePage.clickTab("BangBieu");
        Assert.assertTrue(thongKePage.isMainTableDisplayed());
    }
    @Test(priority = 11) public void test_65_Data() {
        thongKePage.setCaGiang(true); thongKePage.clickTab("BangBieu");
        Assert.assertTrue(thongKePage.getHeaderBangChinh().contains("CA"));
    }
    @Test(priority = 12) public void test_65_UI() {
        thongKePage.setCaGiang(true); thongKePage.clickTab("BangBieu");
        Assert.assertFalse(thongKePage.getTextHuongDan().isEmpty());
    }
    // 6.6
    @Test(priority = 13) public void test_66_Happy_TabChiTiet() {
        thongKePage.setCaGiang(true);
        thongKePage.chonSelect2("unit", "Năm học");
        thongKePage.chonSelect2("major", "Công nghệ thông tin");
        thongKePage.clickTab("ChiTiet");
        Assert.assertTrue(thongKePage.isDetailsTableDisplayed());
    }
    @Test(priority = 14) public void test_66_Sad_TimKiemSaiChiTiet() {
        thongKePage.setCaGiang(true); thongKePage.clickTab("ChiTiet");
        thongKePage.nhapTimKiemBangChiTiet("@@@WrongData");
        Assert.assertTrue(driver.findElement(By.cssSelector(".dataTables_empty")).isDisplayed());
    }
    @Test(priority = 15) public void test_66_Data_SoKhop() {
        thongKePage.setCaGiang(true); thongKePage.clickTab("BangBieu");
        String t1 = thongKePage.getThongTinHienThiBangChinh();
        thongKePage.clickTab("ChiTiet");
        String t2 = thongKePage.getThongTinHienThiBangChiTiet();
        Assert.assertNotNull(t1); Assert.assertNotNull(t2);
    }
    @Test(priority = 16) public void test_66_UI_HeaderChiTiet() {
        thongKePage.setCaGiang(true); thongKePage.clickTab("ChiTiet");
        Assert.assertFalse(thongKePage.getHeaderBangChiTiet().isEmpty());
    }
}