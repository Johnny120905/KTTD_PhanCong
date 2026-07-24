package com.test.chucnang.quanlithoikhoabieu;

import java.io.File;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.test.BoMonCommonTest;

public class F10_1_XuatThoiKhoaBieuTest extends BoMonCommonTest {

    /*
     * F10.1 - LUỒNG ĐÚNG
     * Xuất thời khóa biểu thành công
     */
    @Test(priority = 1)
    public void F10_1_POS_XuatThoiKhoaBieuThanhCong()
            throws InterruptedException {

        System.out.println("===== F10.1 POS - Xuất thời khóa biểu =====");

        navigateToAssignPage();

        long startTime = System.currentTimeMillis();

        WebElement exportButton = waitClickable(
                By.xpath(
                        "//*[self::button or self::a][contains(normalize-space(),'Export') "
                                + "or contains(normalize-space(),'Xuất')]"
                                + " | //button[.//*[contains(normalize-space(),'Export')]]"
                )
        );

        Assert.assertNotNull(
                exportButton,
                "Không tìm thấy nút Export/Xuất thời khóa biểu"
        );

        clickElement(exportButton);

        System.out.println("Đã bấm nút Export");

        File downloadedFile = waitForNewDownloadedFile(startTime, 30);

        Assert.assertNotNull(
                downloadedFile,
                "Không thấy file được tải về sau khi bấm Export"
        );

        Assert.assertTrue(
                downloadedFile.length() > 0,
                "File tải về bị rỗng"
        );

        System.out.println("PASS F10.1 POS - Xuất thời khóa biểu thành công");
        System.out.println("File tải về: " + downloadedFile.getAbsolutePath());
    }

    /*
     * F10.1 - LUỒNG DATA
     * Xuất thời khóa biểu với nhiều timeout khác nhau
     */
    @DataProvider(name = "F10_1_ExportData")
    public Object[][] F10_1_ExportData() {
        return new Object[][]{
                {"Export với timeout 30 giây", 30},
                {"Export với timeout 45 giây", 45}
        };
    }

    @Test(priority = 2, dataProvider = "F10_1_ExportData")
    public void F10_1_DATA_XuatThoiKhoaBieu(String testName, int timeoutSeconds)
            throws InterruptedException {

        System.out.println("===== F10.1 DATA - " + testName + " =====");

        navigateToAssignPage();

        long startTime = System.currentTimeMillis();

        WebElement exportButton = findExportButton();

        Assert.assertNotNull(
                exportButton,
                "Không tìm thấy nút Export/Xuất thời khóa biểu - " + testName
        );

        clickHard(exportButton);

        System.out.println("Đã bấm Export - " + testName);

        File downloadedFile = waitForNewDownloadedFile(startTime, timeoutSeconds);

        Assert.assertNotNull(
                downloadedFile,
                "Không thấy file được tải về - " + testName
        );

        Assert.assertTrue(
                downloadedFile.length() > 0,
                "File tải về bị rỗng - " + testName
        );

        System.out.println("PASS F10.1 DATA - " + testName);
        System.out.println("File tải về: " + downloadedFile.getAbsolutePath());
    }

    public WebElement findExportButton() {

        try {
            List<WebElement> buttons = driver.findElements(
                    By.xpath(
                            "//*[self::button or self::a][contains(normalize-space(),'Export') "
                                    + "or contains(normalize-space(),'Xuất')]"
                                    + " | //button[.//*[contains(normalize-space(),'Export')]]"
                    )
            );

            for (WebElement button : buttons) {
                try {
                    if (button.isDisplayed() && button.isEnabled()) {
                        return button;
                    }
                } catch (Exception ignored) {
                }
            }

        } catch (Exception ignored) {
        }

        return null;
    }

    public File waitForNewDownloadedFile(long startTime, int timeoutSeconds) {

        File downloadFolder = new File(DOWNLOAD_DIR);

        WebDriverWait fileWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

        return fileWait.until(driver -> {

            File[] files = downloadFolder.listFiles();

            if (files == null || files.length == 0) {
                return null;
            }

            File newestFile = null;

            for (File file : files) {
                String fileName = file.getName().toLowerCase();

                boolean isNewFile = file.lastModified() >= startTime - 1000;

                boolean isCompleted =
                        !fileName.endsWith(".crdownload")
                                && !fileName.endsWith(".tmp");

                boolean hasData = file.length() > 0;

                if (isNewFile && isCompleted && hasData) {
                    if (newestFile == null || file.lastModified() > newestFile.lastModified()) {
                        newestFile = file;
                    }
                }
            }

            return newestFile;
        });
    }
}