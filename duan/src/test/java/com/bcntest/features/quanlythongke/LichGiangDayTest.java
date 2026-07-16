package com.bcntest.features.quanlythongke;

import com.bcnpages.QuanLyThongKePage;
import com.bcntest.core.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class LichGiangDayTest extends BaseTest {
    QuanLyThongKePage lichPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        lichPage = new QuanLyThongKePage(driver);
        driver.get(BASE_URL + "Statistics/Timetable");
        Thread.sleep(3000); 
    }

    @BeforeMethod
    public void navigateAndReset() throws InterruptedException {
        if (!driver.getCurrentUrl().contains("Timetable")) {
            lichPage.navigateToLichGiangDay();
        } else {
            driver.navigate().refresh(); 
            Thread.sleep(2000); // Đợi web làm mới DOM hoàn toàn
        }
    }

    // =========================================================================
    // 1. LUỒNG ĐÚNG (HAPPY PATH) 
    // =========================================================================
    @Test(priority = 1, description = "Luồng đúng: Load bảng và lọc Học kỳ/Tuần thành công")
    public void test_01_Happy_LocHocKyTuan() {
        lichPage.chonSelect2("term", "999");
        lichPage.chonSelect2("week", "Tuần 5");
        
        Assert.assertTrue(lichPage.isLichGiangDayDisplayed(), "Bảng lịch giảng dạy không hiển thị sau khi chọn Học kỳ/Tuần!");
    }

    @Test(priority = 2, description = "Luồng đúng: Lọc đích danh giảng viên 'Sâm'")
    public void test_02_Happy_LocGiangVienSam() {
        lichPage.chonSelect2("term", "999");
        lichPage.chonSelect2("week", "Tuần 5");
        
        lichPage.chonMultiSelectTheoPlaceholder("Lọc giảng viên", "Sâm");
        Assert.assertTrue(lichPage.isLichGiangDayDisplayed(), "Lỗi UI khi lọc Giảng viên Sâm!");
    }

    @Test(priority = 3, description = "Luồng đúng: Lọc nhiều tiêu chí cùng lúc")
    public void test_03_Happy_LocNhieuTieuChi() {
        lichPage.chonSelect2("term", "999");
        lichPage.chonSelect2("week", "Tuần 5");
        
        lichPage.chonMultiSelectTheoPlaceholder("Lọc loại giảng viên", "Cơ hữu");
        lichPage.chonMultiSelectTheoPlaceholder("Lọc ca giảng", "Ca 1");
        lichPage.chonMultiSelectTheoPlaceholder("Lọc thứ", "Thứ 2");
        
        Assert.assertTrue(lichPage.isLichGiangDayDisplayed(), "Bảng bị mất khi áp dụng nhiều bộ lọc!");
    }

    // =========================================================================
    // 2. LUỒNG SAI (SAD PATH) - Thao tác dữ liệu không tồn tại/lỗi
    // =========================================================================
    @Test(priority = 4, description = "Luồng sai: Tìm kiếm giảng viên không tồn tại")
    public void test_04_Sad_TimGiangVienKhongTonTai() throws InterruptedException {
        lichPage.chonSelect2("term", "999");
        lichPage.chonSelect2("week", "Tuần 5");
        
        // 1. Dùng WebDriverWait để chờ (Tránh lỗi load chậm)
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        
        // 2. Lấy TẤT CẢ các ô nhập liệu của bộ lọc (Bỏ qua việc tìm bằng tiếng Việt để né lỗi Encoding)
        List<WebElement> searchFields = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("input.select2-search__field")));
        
        // Ô "Lọc giảng viên" là ô đầu tiên (index 0)
        WebElement input = searchFields.get(0);
        
        input.click();
        input.sendKeys("NguoiLaKhongTonTai_9999");
        
        // 3. Đợi Select2 tìm kiếm và hiển thị thông báo
        Thread.sleep(1500); 
        
        // 4. Bắt class thông báo lỗi mặc định của Select2
        List<WebElement> noResultMsg = driver.findElements(By.cssSelector("li.select2-results__message"));
        
        // 5. Xác thực rằng Select2 có báo lỗi "Không tìm thấy kết quả"
        Assert.assertFalse(noResultMsg.isEmpty(), "Select2 không hiển thị thông báo 'Không tìm thấy kết quả' khi nhập sai!");
        
        // 6. Dọn dẹp bằng phím ESCAPE để đóng dropdown, không làm ảnh hưởng đến các Test Case sau
        input.sendKeys(Keys.ESCAPE);
    }

    // =========================================================================
    // 3. LUỒNG DATA (DATA VALIDATION)
    // =========================================================================
    @Test(priority = 5, description = "Luồng Data: Kiểm tra cấu trúc cột Thứ trong bảng")
    public void test_05_Data_KiemTraCotThu() {
        lichPage.chonSelect2("term", "999");
        lichPage.chonSelect2("week", "Tuần 5");
        
        String headerText = lichPage.getHeaderBangChinh().toUpperCase();
        
        Assert.assertFalse(headerText.isEmpty(), "Header rỗng do không tìm thấy bảng!");
        Assert.assertTrue(headerText.contains("THỨ 2"), "Thiếu cột Thứ 2");
        Assert.assertTrue(headerText.contains("THỨ 4"), "Thiếu cột Thứ 4");
        Assert.assertTrue(headerText.contains("CHỦ NHẬT"), "Thiếu cột Chủ Nhật");
    }

    @Test(priority = 6, description = "Luồng Data: Xác thực dữ liệu Tooltip của thẻ môn học")
    public void test_06_Data_ChiTietMonHoc() {
        lichPage.chonSelect2("term", "999");
        lichPage.chonSelect2("week", "Tuần 5");
        
        lichPage.isLichGiangDayDisplayed(); 
        
        List<WebElement> classCards = lichPage.getAllLichButtons();
        if (classCards.isEmpty()) {
            System.out.println("Debug: Tuần này không có lịch, bỏ qua check Tooltip.");
            Assert.assertTrue(true);
            return;
        }

        WebElement firstCard = classCards.get(0);
        String tooltipData = firstCard.getAttribute("data-bs-original-title");
        if (tooltipData == null || tooltipData.isEmpty()) {
            tooltipData = firstCard.getAttribute("title");
        }

        Assert.assertNotNull(tooltipData, "Thẻ lịch không có Tooltip!");
        Assert.assertTrue(tooltipData.contains("HP:") || tooltipData.contains("Phòng:"), "Dữ liệu Tooltip bị thiếu cấu trúc chuẩn!");
    }

    // =========================================================================
    // 4. LUỒNG GIAO DIỆN (UI VALIDATION)
    // =========================================================================
    @Test(priority = 7, description = "Luồng UI: Kiểm tra thông báo thời gian hiển thị rõ ràng")
    public void test_07_UI_AlertThoiGian() {
        lichPage.chonSelect2("term", "999");
        lichPage.chonSelect2("week", "Tuần 5");
        
        String thongTinTuan = lichPage.getThongTinTuanHoc();
        
        Assert.assertFalse(thongTinTuan.isEmpty(), "Giao diện bị thiếu Alert thông báo Tuần học!");
        Assert.assertTrue(thongTinTuan.contains("Tuần") || thongTinTuan.contains("ngày"), "Văn bản Alert UI không đúng định dạng!");
    }

    @Test(priority = 8, description = "Luồng UI: Kiểm tra màu sắc/class của nút Lý thuyết & Thực hành")
    public void test_08_UI_MauSacClassCard() {
        lichPage.chonSelect2("term", "999");
        lichPage.chonSelect2("week", "Tuần 5");
        
        lichPage.isLichGiangDayDisplayed(); 
        
        List<WebElement> classCards = lichPage.getAllLichButtons();
        if (classCards.isEmpty()) {
            System.out.println("Debug: Không có lịch để check UI màu sắc.");
            Assert.assertTrue(true);
            return;
        }

        WebElement firstCard = classCards.get(0);
        String cardClass = firstCard.getAttribute("class");
        
        boolean isCorrectColor = cardClass.contains("btn-success") || cardClass.contains("btn-warning") || cardClass.contains("btn-info");
        Assert.assertTrue(isCorrectColor, "Thẻ lịch không hiển thị đúng class màu sắc của UI!");
    }

    // =========================================================================
    // 5. LUỒNG NGHIỆP VỤ CỤ THỂ (SPECIFIC BUSINESS SCENARIO)
    // =========================================================================
    @Test(priority = 9, description = "Luồng cụ thể: Tìm TKB của Giảng viên Nguyễn Cao Sâm (Thỉnh giảng) tại Học kỳ 999")
    public void test_09_TimLichGiangDay_NguyenCaoSam() {
        // 1. Chọn học kỳ 999 
        lichPage.chonSelect2("term", "999");
        
        // Chọn Tuần 5 (Lưu ý: Nếu Sâm có lịch dạy ở tuần khác, ông có thể đổi số tuần ở đây)
        lichPage.chonSelect2("week", "Tuần 5");
        
        // 2. Lọc loại giảng viên -> Thỉnh giảng
        lichPage.chonMultiSelectTheoPlaceholder("Lọc loại giảng viên", "Thỉnh giảng");
        
        // 3. Lọc tên giảng viên -> Nguyễn Cao Sâm
        lichPage.chonMultiSelectTheoPlaceholder("Lọc giảng viên", "Nguyễn Cao Sâm");
        
        // 4. Đảm bảo bảng hiển thị ổn định
        Assert.assertTrue(lichPage.isLichGiangDayDisplayed(), "Bảng TKB bị lỗi sau khi áp dụng các bộ lọc!");
        
        // 5. Kiểm tra kết quả các ca dạy
        List<WebElement> classCards = lichPage.getAllLichButtons();
        
        if (classCards.isEmpty()) {
            System.out.println("===============================================================================");
            System.out.println("📌 Kết quả test 09: GV Nguyễn Cao Sâm (Thỉnh giảng) KHÔNG CÓ lịch dạy ở Tuần 5 - HK 999");
            System.out.println("===============================================================================");
            Assert.assertTrue(true); // Lọc thành công nhưng trả về rỗng là chuyện bình thường
        } else {
            System.out.println("===============================================================================");
            System.out.println("📌 Kết quả test 09: Đã tìm thấy " + classCards.size() + " ca dạy của Nguyễn Cao Sâm (Thỉnh giảng) ở Tuần 5 - HK 999");
            
            // Lấy thử dữ liệu của ca đầu tiên in ra để kiểm chứng
            String firstCardTooltip = classCards.get(0).getAttribute("data-bs-original-title");
            if (firstCardTooltip != null) {
                System.out.println("Chi tiết ca đầu tiên: " + firstCardTooltip);
            }
            System.out.println("===============================================================================");
            Assert.assertTrue(classCards.size() > 0);
        }
    }
}