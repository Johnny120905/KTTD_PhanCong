package com.bcntest.core;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MasterLayoutTest extends BaseTest {

    @BeforeClass
    public void initPage() throws InterruptedException {
        // Đảm bảo đang đứng ở trang chủ sau khi Login
        driver.get(BASE_URL + "Dashboard");
        Thread.sleep(3000); 
    }

    // ==========================================
    // 1. KỊCH BẢN TEST CHẾ ĐỘ SÁNG / TỐI (Dark Mode)
    // ==========================================
    @Test(priority = 1)
    public void testUI_KiemTraCheDoToi() throws InterruptedException {
        System.out.println("--- LUỒNG UI: KIỂM TRA CHẾ ĐỘ DARK MODE ---");
        
        WebElement btnDarkMode = driver.findElement(By.cssSelector("a.nav-link-style"));
        btnDarkMode.click();
        Thread.sleep(1000); 
        
        WebElement htmlTag = driver.findElement(By.tagName("html"));
        String htmlClasses = htmlTag.getAttribute("class");
        
        Assert.assertTrue(htmlClasses.contains("dark-layout"), 
                "Fail UI: Thẻ HTML không được cập nhật class 'dark-layout'. Hệ thống chưa chuyển sang chế độ tối!");
                
        btnDarkMode.click();
        Thread.sleep(1000);
    }

    // ==========================================
    // 2. KỊCH BẢN TEST THU/PHÓNG THANH MENU (Sidebar Toggle)
    // ==========================================
    @Test(priority = 2)
    public void testUI_KiemTraThuGonMenu() throws InterruptedException {
        System.out.println("--- LUỒNG UI: KIỂM TRA THU GỌN THANH MENU TRÁI ---");
        
        WebElement sidebar = driver.findElement(By.cssSelector(".main-menu"));
        int widthBanDau = sidebar.getSize().width;
        
        WebElement btnToggle = driver.findElement(By.cssSelector(".menu-toggle, a.nav-link.menu-toggle"));
        btnToggle.click();
        Thread.sleep(1000); 
        
        int widthSauKhiThu = sidebar.getSize().width;
        
        Assert.assertTrue(widthSauKhiThu < widthBanDau, 
                "Fail UI: Menu không tự động thụt vào! Độ rộng vẫn là: " + widthSauKhiThu);
                
        btnToggle.click();
        Thread.sleep(1000);
    }

    // ==========================================
    // 3. KỊCH BẢN TEST TOÀN MÀN HÌNH (Fullscreen Mode)
    // ==========================================
    @Test(priority = 3)
    public void testUI_KiemTraToanManHinh() throws InterruptedException {
        System.out.println("--- LUỒNG UI: KIỂM TRA NÚT PHÓNG TOÀN MÀN HÌNH ---");
        
        WebElement btnFullScreen = driver.findElement(By.cssSelector("a.apptogglefullscreen"));
        btnFullScreen.click();
        Thread.sleep(1500); 
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        boolean isFullscreenActived = (Boolean) js.executeScript("return document.fullscreenElement != null;");
        
        Assert.assertTrue(isFullscreenActived, "Fail UI: Giao diện web không phóng to toàn màn hình được!");
        
        btnFullScreen.click();
        Thread.sleep(1000);
    }

    // ==========================================
    // 4. KỊCH BẢN TEST CUỘN TRANG (Scroll Up / Down)
    // ==========================================
    @Test(priority = 4)
    public void testUI_KiemTraCuonTrang() throws InterruptedException {
        System.out.println("--- LUỒNG UI: KIỂM TRA TÍNH NĂNG CUỘN TRANG ---");
        
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // 1. Lấy vị trí thanh cuộn ban đầu (theo trục Y)
        Long viTriBanDau = (Long) js.executeScript("return window.pageYOffset;");
        System.out.println("Vị trí cuộn ban đầu: " + viTriBanDau);

        // 2. Lệnh lướt thẳng xuống cuối cùng của trang web
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(1500); // Chờ trình duyệt xử lý hiệu ứng lướt
        
        // Lấy vị trí sau khi đã lướt xuống
        Long viTriSauKhiLuotXuong = (Long) js.executeScript("return window.pageYOffset;");
        System.out.println("Vị trí sau khi lướt xuống: " + viTriSauKhiLuotXuong);
        
        // Assert: Xác minh vị trí mới phải lớn hơn vị trí ban đầu (tức là đã lướt thành công)
        Assert.assertTrue(viTriSauKhiLuotXuong > viTriBanDau, "Fail UI: Giao diện web đang bị khóa, không thể lướt xuống được!");

        // 3. Lệnh lướt ngược lại lên trên cùng của trang web
        js.executeScript("window.scrollTo(0, 0);");
        Thread.sleep(1500); 
        
        // Lấy vị trí sau khi lướt lên
        Long viTriSauKhiLuotLen = (Long) js.executeScript("return window.pageYOffset;");
        System.out.println("Vị trí sau khi lướt lên đầu trang: " + viTriSauKhiLuotLen);
        
        // Assert: Xác minh thanh cuộn đã quay về đúng mốc số 0 (đỉnh trang)
        Assert.assertEquals(viTriSauKhiLuotLen, Long.valueOf(0), "Fail UI: Trang không thể lướt ngược lên trên cùng!");
    }
}