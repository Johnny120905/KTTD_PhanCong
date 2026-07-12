package com.gvpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class quanlyTKBpages {

    WebDriver driver;

    public quanlyTKBpages(WebDriver driver) {
        this.driver = driver;
    }

    // =========================
    // MENU
    // =========================

    By menuTKB = By.xpath("//span[contains(text(),'Thời khoá biểu')]");

    // =========================
    // HỌC KỲ
    // =========================

    By cboHocKy = By.id("select2-term-container");

    By hocKy999 = By.xpath("//li[text()='999']");

    By hocKy998 = By.xpath("//li[text()='998']");

    // =========================
    // TUẦN
    // =========================

    By cboTuan = By.id("select2-week-container");

    By tuan9 = By.xpath("//li[text()='Tuần 9']");

    // =========================
    // KIỂM TRA
    // =========================

    By imgSorry = By.xpath("//img[contains(@src,'img_sorry.svg')]");

    By termSelected = By.id("select2-term-container");

    By weekSelected = By.id("select2-week-container");

    // =========================
    // Bấm menu Thời khóa biểu
    // =========================

    public void moTKB() throws InterruptedException {

        driver.findElement(menuTKB).click();

        Thread.sleep(2000);

    }

    // =========================
    // Chọn học kỳ 998
    // =========================

    public void chonHocKy998() throws InterruptedException {

        driver.findElement(cboHocKy).click();

        Thread.sleep(1000);

        driver.findElement(hocKy998).click();

        Thread.sleep(3000);

    }

    // =========================
    // Chọn học kỳ 999
    // =========================

    public void chonHocKy999() throws InterruptedException {

        driver.findElement(cboHocKy).click();

        Thread.sleep(1000);

        driver.findElement(hocKy999).click();

        Thread.sleep(3000);

    }

    // =========================
    // Chọn tuần 9
    // =========================

    public void chonTuan9() throws InterruptedException {

        driver.findElement(cboTuan).click();

        Thread.sleep(1000);

        driver.findElement(tuan9).click();

        Thread.sleep(3000);

    }

    // =========================
    // Kiểm tra hình Sorry
    // =========================

    public boolean isNoData() {

        return driver.findElement(imgSorry).isDisplayed();

    }

    // =========================
    // Kiểm tra học kỳ 999
    // =========================

    public boolean isHocKy999() {

        return driver.findElement(termSelected)
                .getText()
                .equals("999");

    }

    // =========================
    // Kiểm tra tuần 9
    // =========================

    public boolean isWeek9() {

        return driver.findElement(weekSelected)
                .getText()
                .equals("Tuần 9");

    }

}