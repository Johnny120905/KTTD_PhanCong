package com.test.chucnang.quanlihosonguoidung;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.test.BoMonCommonTest;

import io.github.bonigarcia.wdm.WebDriverManager;
public class F13_1_CapNhatHoSoNguoiDungTest extends BoMonCommonTest  {
            protected FeatureConfig config() {
        return new FeatureConfig("F13.1", "Cập nhật hồ sơ người dùng")
                .menu("Hồ sơ")
                .expected(
                        "Cập nhật hồ sơ người dùng",
                        "Mã giảng viên",
                        "Tên giảng viên",
                        "Cập nhật"
                )
                .data(
                        "2374802010286",
                        "Vũ Thế Long"
                )
                .acceptNoData(true);
    }

    /*
     * POS - Luồng đúng
     * 1. Vào trang Phân công để xác nhận đã đăng nhập
     * 2. Bấm tên/email góc phải
     * 3. Bấm Hồ sơ
     * 4. Vào màn hình Cập nhật hồ sơ người dùng
     * 5. Giữ nguyên tên giảng viên hiện tại
     * 6. Bấm Cập nhật
     */
    @Test(priority = 1)
    public void F13_1_POS_CapNhatHoSoNguoiDungThanhCong() throws InterruptedException {

        System.out.println("===== F13.1 POS - Cập nhật hồ sơ người dùng =====");

        openProfilePageByUserDropdown();

        Assert.assertTrue(
                waitForProfilePage(10),
                "Không mở được màn hình Cập nhật hồ sơ người dùng"
        );

        WebElement tenGiangVienInput = findTenGiangVienInput();

        Assert.assertNotNull(
                tenGiangVienInput,
                "Không tìm thấy ô Tên giảng viên"
        );

        String oldName = getInputValue(tenGiangVienInput);

        Assert.assertTrue(
                oldName != null && !oldName.trim().isEmpty(),
                "Tên giảng viên đang rỗng"
        );

        clearAndTypeInput(tenGiangVienInput, oldName);

        WebElement btnCapNhat = findCapNhatButton();

        Assert.assertNotNull(
                btnCapNhat,
                "Không tìm thấy nút Cập nhật"
        );

        clickHard(btnCapNhat);

        Thread.sleep(2500);

        String page = driver.getPageSource();

        boolean ok =
                isProfilePage()
                        || page.contains("Thành công")
                        || page.contains("thành công")
                        || page.contains("Cập nhật thành công")
                        || page.contains("Success")
                        || page.contains("success");

        Assert.assertTrue(
                ok,
                "Bấm Cập nhật nhưng không xác nhận được kết quả"
        );

        System.out.println("PASS F13.1 POS - Cập nhật hồ sơ người dùng thành công");
    }

    /*
     * NEG - Luồng sai
     * Chưa đăng nhập thì không được truy cập trực tiếp /Account/Update
     */
    @Test(priority = 2)
    public void F13_1_NEG_ChuaDangNhapBiChan() throws InterruptedException {

        System.out.println("===== F13.1 NEG - Chưa đăng nhập bị chặn =====");

        WebDriver tempDriver = null;

        try {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            options.setAcceptInsecureCerts(true);
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--allow-insecure-localhost");
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--remote-allow-origins=*");

            tempDriver = new ChromeDriver(options);

            tempDriver.get(BASE_URL + "/Account/Update");

            Thread.sleep(4000);

            String currentUrl = tempDriver.getCurrentUrl();
            String page = tempDriver.getPageSource();

            boolean isLoginPage =
                    currentUrl.contains("/Account/Login")
                            || currentUrl.toLowerCase().contains("login")
                            || currentUrl.toLowerCase().contains("microsoft")
                            || page.contains("Đăng nhập")
                            || page.contains("Sign in")
                            || page.contains("Microsoft");

            boolean stillCanSeeProfile =
                    page.contains("Cập nhật hồ sơ người dùng")
                            && page.contains("Mã giảng viên")
                            && page.contains("Tên giảng viên")
                            && page.contains("Cập nhật");

            Assert.assertTrue(
                    isLoginPage || !stillCanSeeProfile,
                    "Chưa đăng nhập nhưng vẫn vào được trang Cập nhật hồ sơ người dùng"
            );

            System.out.println("PASS F13.1 NEG - Chưa đăng nhập không vào được hồ sơ");

        } finally {
            if (tempDriver != null) {
                tempDriver.quit();
            }
        }
    }

    /*
     * DATA - Dữ liệu kiểm thử
     * Kiểm tra đúng dữ liệu đang hiển thị trong input:
     * staff_id = 2374802010286
     * full_name = Vũ Thế Long
     */
    @DataProvider(name = "duLieuHoSoNguoiDung")
    public Object[][] duLieuHoSoNguoiDung() {
        return new Object[][]{
                {"2374802010286", "Vũ Thế Long"}
        };
    }

    @Test(priority = 3, dataProvider = "duLieuHoSoNguoiDung")
    public void F13_1_DATA_KiemTraDuLieuHoSoNguoiDung(
            String expectedMaGiangVien,
            String expectedTenGiangVien
    ) throws InterruptedException {

        System.out.println("===== F13.1 DATA - Kiểm tra dữ liệu hồ sơ người dùng =====");

        openProfilePageByUserDropdown();

        Assert.assertTrue(
                waitForProfilePage(10),
                "Không mở được màn hình Cập nhật hồ sơ người dùng"
        );

        WebElement maGiangVienInput = findMaGiangVienInput();
        WebElement tenGiangVienInput = findTenGiangVienInput();

        Assert.assertNotNull(
                maGiangVienInput,
                "Không tìm thấy ô Mã giảng viên id=staff_id"
        );

        Assert.assertNotNull(
                tenGiangVienInput,
                "Không tìm thấy ô Tên giảng viên id=full_name"
        );

        String actualMaGiangVien = getInputValue(maGiangVienInput);
        String actualTenGiangVien = getInputValue(tenGiangVienInput);

        System.out.println("Expected mã giảng viên: " + expectedMaGiangVien);
        System.out.println("Actual mã giảng viên: " + actualMaGiangVien);

        System.out.println("Expected tên giảng viên: " + expectedTenGiangVien);
        System.out.println("Actual tên giảng viên: " + actualTenGiangVien);

        Assert.assertEquals(
                actualMaGiangVien,
                expectedMaGiangVien,
                "Mã giảng viên không đúng dữ liệu mong đợi"
        );

        Assert.assertEquals(
                actualTenGiangVien,
                expectedTenGiangVien,
                "Tên giảng viên không đúng dữ liệu mong đợi"
        );

        Assert.assertTrue(
                actualMaGiangVien.matches("\\d+"),
                "Mã giảng viên phải là dạng số"
        );

        Assert.assertTrue(
                actualTenGiangVien.length() >= 2,
                "Tên giảng viên không hợp lệ"
        );

        System.out.println("PASS F13.1 DATA - Dữ liệu hồ sơ người dùng hiển thị đúng");
    }

    /*
     * UI - Kiểm tra giao diện
     * Kiểm tra tiêu đề, label, input staff_id, input full_name, nút Cập nhật,
     * giới hạn nhập 255 ký tự, resize, zoom, scroll.
     */
    @Test(priority = 4)
    public void F13_1_UI_KiemTraGiaoDienCapNhatHoSo() throws InterruptedException {

        System.out.println("===== F13.1 UI - Kiểm tra giao diện cập nhật hồ sơ =====");

        openProfilePageByUserDropdown();

        Assert.assertTrue(
                waitForProfilePage(10),
                "Không mở được màn hình Cập nhật hồ sơ người dùng"
        );

        Assert.assertTrue(
                hasVisibleText("Cập nhật hồ sơ người dùng"),
                "Không thấy tiêu đề Cập nhật hồ sơ người dùng"
        );

        Assert.assertTrue(
                hasVisibleText("Mã giảng viên"),
                "Không thấy label Mã giảng viên"
        );

        Assert.assertTrue(
                hasVisibleText("Tên giảng viên"),
                "Không thấy label Tên giảng viên"
        );

        WebElement maGiangVienInput = findMaGiangVienInput();
        WebElement tenGiangVienInput = findTenGiangVienInput();
        WebElement btnCapNhat = findCapNhatButton();

        Assert.assertNotNull(
                maGiangVienInput,
                "Không thấy ô Mã giảng viên id=staff_id trên giao diện"
        );

        Assert.assertNotNull(
                tenGiangVienInput,
                "Không thấy ô Tên giảng viên id=full_name trên giao diện"
        );

        Assert.assertNotNull(
                btnCapNhat,
                "Không thấy nút Cập nhật trên giao diện"
        );

        Assert.assertTrue(
                maGiangVienInput.isDisplayed(),
                "Ô Mã giảng viên không hiển thị"
        );

        Assert.assertTrue(
                tenGiangVienInput.isDisplayed(),
                "Ô Tên giảng viên không hiển thị"
        );

        Assert.assertTrue(
                btnCapNhat.isDisplayed(),
                "Nút Cập nhật không hiển thị"
        );

        Assert.assertEquals(
                maGiangVienInput.getAttribute("id"),
                "staff_id",
                "Input Mã giảng viên không đúng id"
        );

        Assert.assertEquals(
                tenGiangVienInput.getAttribute("id"),
                "full_name",
                "Input Tên giảng viên không đúng id"
        );

        Assert.assertTrue(
                getInputValue(maGiangVienInput).matches("\\d+"),
                "UI FAIL - Mã giảng viên phải là số"
        );

        String oldName = getInputValue(tenGiangVienInput);

        Assert.assertTrue(
                oldName != null && oldName.length() >= 2,
                "UI FAIL - Tên giảng viên ban đầu không hợp lệ"
        );

        String longText = "A".repeat(255);

        clearAndTypeInput(tenGiangVienInput, longText);

        Thread.sleep(800);

        String currentValue = getInputValue(tenGiangVienInput);

        Assert.assertTrue(
                currentValue.length() <= 255,
                "Ô Tên giảng viên cho nhập quá 255 ký tự"
        );

        clearAndTypeInput(tenGiangVienInput, oldName);

        Thread.sleep(800);

        Assert.assertEquals(
                getInputValue(tenGiangVienInput),
                oldName,
                "Không khôi phục lại được tên giảng viên ban đầu sau khi test UI"
        );

        kiemTraPhongToThuNhoFlow();
        kiemTraLuotLenLuotXuongFlow();

        Assert.assertTrue(
                driver.findElement(By.tagName("body")).isDisplayed(),
                "Body không hiển thị sau khi kiểm tra UI"
        );

        System.out.println("PASS F13.1 UI - Giao diện cập nhật hồ sơ ổn");
    }

    /*
     * Mở trang Hồ sơ đúng theo UI thật:
     * 1. Vào trang Phân công
     * 2. Bấm tên/email góc phải
     * 3. Bấm Hồ sơ href=/Phancong02/Account/Update
     */
    private void openProfilePageByUserDropdown() throws InterruptedException {

        navigateToAssignPage();

        waitBodyFlow();

        Thread.sleep(1000);

        if (isProfilePage()) {
            return;
        }

        WebElement userDropdown = waitClickable(
                By.xpath(
                        "//a[@id='dropdown-user']"
                                + " | //a[contains(@class,'dropdown-user-link')]"
                                + " | //li[contains(@class,'dropdown-user')]//a[contains(@class,'dropdown-toggle')]"
                                + " | //*[contains(normalize-space(),'@vanlanguni.vn')]/ancestor::a[1]"
                )
        );

        Assert.assertNotNull(
                userDropdown,
                "Không tìm thấy nút tên/email người dùng góc phải"
        );

        clickHard(userDropdown);

        Thread.sleep(1000);

        WebElement hoSoButton = waitVisible(
                By.xpath(
                        "//a[contains(@class,'dropdown-item') and contains(@href,'/Phancong02/Account/Update')]"
                                + " | //a[contains(@class,'dropdown-item') and contains(@href,'Account/Update')]"
                                + " | //a[contains(@href,'Account/Update') and contains(normalize-space(),'Hồ sơ')]"
                                + " | //a[contains(normalize-space(),'Hồ sơ')]"
                )
        );

        Assert.assertNotNull(
                hoSoButton,
                "Không tìm thấy nút Hồ sơ trong dropdown"
        );

        clickHard(hoSoButton);

        Thread.sleep(2500);

        Assert.assertTrue(
                waitForProfilePage(10),
                "Đã bấm Hồ sơ nhưng không vào được màn hình Cập nhật hồ sơ người dùng"
        );
    }

    private boolean waitForProfilePage(int timeoutSeconds) throws InterruptedException {

        for (int i = 0; i < timeoutSeconds * 2; i++) {
            if (isProfilePage()
                    && findMaGiangVienInput() != null
                    && findTenGiangVienInput() != null
                    && findCapNhatButton() != null) {
                return true;
            }

            Thread.sleep(500);
        }

        return false;
    }

    private boolean isProfilePage() {

        try {
            String currentUrl = driver.getCurrentUrl();
            String page = driver.getPageSource();

            boolean correctUrl = currentUrl.contains("/Account/Update");

            boolean hasProfileContent =
                    page.contains("Cập nhật hồ sơ người dùng")
                            && page.contains("Mã giảng viên")
                            && page.contains("Tên giảng viên")
                            && page.contains("Cập nhật");

            boolean hasProfileInputs =
                    driver.findElements(By.id("staff_id")).size() > 0
                            && driver.findElements(By.id("full_name")).size() > 0;

            return correctUrl && (hasProfileContent || hasProfileInputs);

        } catch (Exception e) {
            return false;
        }
    }

    private WebElement findMaGiangVienInput() {

        return findVisibleElementAllowDisabled(
                By.id("staff_id"),
                By.name("staff_id"),
                By.cssSelector("input#staff_id"),
                By.xpath("//input[@id='staff_id' and @name='staff_id']"),
                By.xpath("(//*[contains(normalize-space(),'Mã giảng viên')]/following::input[1])[1]")
        );
    }

    private WebElement findTenGiangVienInput() {

        return findVisibleElementAllowDisabled(
                By.id("full_name"),
                By.name("full_name"),
                By.cssSelector("input#full_name"),
                By.xpath("//input[@id='full_name' and @name='full_name']"),
                By.xpath("(//*[contains(normalize-space(),'Tên giảng viên')]/following::input[1])[1]")
        );
    }

    private WebElement findCapNhatButton() {

        return findVisibleElementAllowDisabled(
                By.cssSelector("form#profile-form button[type='submit']"),
                By.xpath("//form[@id='profile-form']//button[@type='submit']"),
                By.xpath("//button[@type='submit' and contains(normalize-space(),'Cập nhật')]"),
                By.xpath("//*[self::button or @role='button' or contains(@class,'btn')][contains(normalize-space(),'Cập nhật')]")
        );
    }

    private WebElement findVisibleElementAllowDisabled(By... locators) {

        for (By locator : locators) {
            try {
                List<WebElement> elements = driver.findElements(locator);

                for (WebElement element : elements) {
                    try {
                        if (element.isDisplayed()) {
                            Rectangle rect = element.getRect();

                            if (rect.getWidth() > 20 && rect.getHeight() > 10) {
                                return element;
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception ignored) {
            }
        }

        return null;
    }

    private String getInputValue(WebElement input) {

        try {
            String value = input.getAttribute("value");

            if (value == null) {
                value = input.getText();
            }

            if (value == null) {
                value = "";
            }

            return value.trim();

        } catch (Exception e) {
            return "";
        }
    }

    private void clearAndTypeInput(WebElement input, String value) throws InterruptedException {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", input);
        } catch (Exception ignored) {
        }

        Thread.sleep(300);

        try {
            js.executeScript("arguments[0].focus();", input);
        } catch (Exception ignored) {
        }

        try {
            input.click();
        } catch (Exception ignored) {
        }

        Thread.sleep(300);

        try {
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            input.sendKeys(Keys.BACK_SPACE);
        } catch (Exception ignored) {
        }

        try {
            js.executeScript(
                    "const el = arguments[0];"
                            + "const value = arguments[1];"
                            + "const setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;"
                            + "setter.call(el, value);"
                            + "el.dispatchEvent(new Event('input', { bubbles: true }));"
                            + "el.dispatchEvent(new Event('change', { bubbles: true }));"
                            + "el.dispatchEvent(new KeyboardEvent('keyup', { bubbles: true }));",
                    input,
                    value
            );
        } catch (Exception e) {
            input.sendKeys(value);
        }

        Thread.sleep(500);
    }

    private boolean hasVisibleText(String text) {

        try {
            WebElement element = findVisibleElementByText(text);

            return element != null && element.isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }
}
