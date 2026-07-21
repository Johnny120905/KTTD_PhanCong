package com.gvpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class quanlythongkepages {

    WebDriver driver;

    public quanlythongkepages(WebDriver driver) {
        this.driver = driver;
    }

    // =========================
    // MENU
    // =========================

    By menuThongKe = By.xpath("//span[contains(text(),'Thống kê')]");
    By menuSoGioCaNhan = By.xpath("//span[contains(text(),'Số giờ cá nhân')]");

    // =========================
    // THỐNG KÊ THEO
    // =========================

    By cboThongKeTheo = By.id("select2-unit-container");

    By thongKeTheoHocKy = By.xpath("//li[contains(text(),'Học kỳ')]");
    By thongKeTheoNam = By.xpath("//li[contains(text(),'Năm học')]");

    // =========================
    // HỌC KỲ
    // =========================

    By cboHocKy = By.id("select2-term-container");

    By hocKy999 = By.xpath("//li[contains(text(),'999')]");
    By hocKy998 = By.xpath("//li[contains(text(),'998')]");

    // =========================
    // NĂM HỌC
    // =========================

    By cboNamHoc = By.id("select2-year-container");

    By namHoc20252026 = By.xpath("//li[contains(text(),'2025 - 2026')]");

    // =========================
    // KHÔNG CÓ DỮ LIỆU
    // =========================

    By imgNoData = By.xpath("//img[contains(@src,'img_no_data.svg')]");

    // =========================
    // KIỂM TRA
    // =========================

    By termSelected = By.id("select2-term-container");
    By yearSelected = By.id("select2-year-container");

// =====================================================
// MENU THỐNG KÊ
// =====================================================

public void moThongKe() throws Exception {

    try {

        if (!driver.findElement(menuSoGioCaNhan).isDisplayed()) {

            driver.findElement(menuThongKe).click();

            Thread.sleep(2000);

        }

    } catch (Exception e) {

        driver.findElement(menuThongKe).click();

        Thread.sleep(2000);

    }

}

    // =====================================================
    // MỞ SỐ GIỜ CÁ NHÂN
    // =====================================================

    public void moSoGioCaNhan() throws Exception {

        driver.findElement(menuSoGioCaNhan).click();

        Thread.sleep(2000);

    }

    // =====================================================
    // CHỌN THỐNG KÊ THEO HỌC KỲ
    // =====================================================

    public void chonThongKeTheoHocKy() throws Exception {

        driver.findElement(cboThongKeTheo).click();

        Thread.sleep(1000);

        driver.findElement(thongKeTheoHocKy).click();

        Thread.sleep(2000);

    }

    // =====================================================
    // CHỌN THỐNG KÊ THEO NĂM
    // =====================================================

    public void chonThongKeTheoNam() throws Exception {

        driver.findElement(cboThongKeTheo).click();

        Thread.sleep(1000);

        driver.findElement(thongKeTheoNam).click();

        Thread.sleep(2000);

    }

    // =====================================================
    // CHỌN HỌC KỲ 998
    // =====================================================

    public void chonHocKy998() throws Exception {

        driver.findElement(cboHocKy).click();

        Thread.sleep(1000);

        driver.findElement(hocKy998).click();

        Thread.sleep(3000);

    }

    // =====================================================
    // CHỌN HỌC KỲ 999
    // =====================================================

    public void chonHocKy999() throws Exception {

        driver.findElement(cboHocKy).click();

        Thread.sleep(1000);

        driver.findElement(hocKy999).click();

        Thread.sleep(3000);

    }

    // =====================================================
    // CHỌN NĂM HỌC 2025-2026
    // =====================================================

    public void chonNamHoc20252026() throws Exception {

        driver.findElement(cboNamHoc).click();

        Thread.sleep(1000);

        driver.findElement(namHoc20252026).click();

        Thread.sleep(3000);

    }

    // =====================================================
    // KIỂM TRA HỌC KỲ 999
    // =====================================================

    public boolean isHocKy999() {

        return driver.findElement(termSelected)
                .getText()
                .equals("999");

    }

    // =====================================================
    // KIỂM TRA HỌC KỲ 998
    // =====================================================

    public boolean isHocKy998() {

        return driver.findElement(termSelected)
                .getText()
                .equals("998");

    }

    // =====================================================
    // KIỂM TRA NĂM HỌC
    // =====================================================

    public boolean isNamHoc20252026() {

        return driver.findElement(yearSelected)
                .getText()
                .equals("2025 - 2026");

    }

    // =====================================================
    // KIỂM TRA KHÔNG CÓ DỮ LIỆU
    // =====================================================

    public boolean isNoData() {

        return driver.findElements(imgNoData).size() > 0;

    }

}