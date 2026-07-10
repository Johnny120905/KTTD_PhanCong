package com.bcnpages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class QuanLyThoiKhoaBieuPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- LOCATORS ---
    private By btnImportSubmit = By.id("submit-all");
    private By inputFile = By.cssSelector("input[type='file']");
    private By msgThongBao = By.cssSelector(".toast, .swal2-popup");
    private By msgLoiDinhDang = By.cssSelector("label.error, .invalid-feedback, .text-danger, #errorLecturers-section");

    public QuanLyThoiKhoaBieuPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // ĐÃ TỐI ƯU HÓA: Hàm chọn Select2 bất hoại (kết hợp Click UI và Javascript)
    public void chonSelect2(String selectId, String textToSelect) {
        try {
            // Thử click bằng giao diện trước
            By select2Container = By.xpath("//span[@aria-labelledby='select2-" + selectId + "-container']");
            wait.until(ExpectedConditions.elementToBeClickable(select2Container)).click();
            Thread.sleep(500); // Chờ 0.5s cho hiệu ứng thả xuống
            
            By optionXpath = By.xpath("//li[contains(@class, 'select2-results__option') and contains(text(),'" + textToSelect + "')]");
            wait.until(ExpectedConditions.elementToBeClickable(optionXpath)).click();
        } catch (Exception e) {
            // Nếu giao diện bị lag hoặc bị che, lập tức dùng Javascript để bơm dữ liệu
            System.out.println("Giao diện lag, chuyển sang ép chọn " + textToSelect + " bằng Javascript...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String script = "$('#" + selectId + " option').filter(function() { return $(this).text() === '" + textToSelect + "'; }).prop('selected', true); $('#" + selectId + "').trigger('change');";
            js.executeScript(script);
        }
    }

    public void nhapThongTinImport(String hocKy, String nganh, String duongDanFile) {
        if(!hocKy.isEmpty()) {
            chonSelect2("term", hocKy);
        }
        
        if(!nganh.isEmpty()) {
            chonSelect2("major", nganh);
        }
        
        if(!duongDanFile.isEmpty()) {
            WebElement fileElement = driver.findElement(inputFile);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.visibility='visible'; arguments[0].style.display='block';", fileElement);
            fileElement.sendKeys(duongDanFile);
        }
    }

    public void bamImport() {
        wait.until(ExpectedConditions.elementToBeClickable(btnImportSubmit)).click();
    }

    public String layThongBao() {
        StringBuilder thongBaoGop = new StringBuilder();
        try {
            // Chờ tối đa 5 giây cho các popup chậm
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement toast = shortWait.until(ExpectedConditions.visibilityOfElementLocated(msgThongBao));
            thongBaoGop.append(toast.getText()).append(" ");
        } catch (Exception e) {}
        
        try {
            List<WebElement> errors = driver.findElements(msgLoiDinhDang);
            for (WebElement err : errors) {
                if (err.isDisplayed()) thongBaoGop.append(err.getText()).append(" ");
            }
        } catch (Exception e) {}
        
        return thongBaoGop.toString().trim().toLowerCase();
    }
}