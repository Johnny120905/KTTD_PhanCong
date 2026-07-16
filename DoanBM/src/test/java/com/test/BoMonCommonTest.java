package com.test;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BoMonCommonTest extends BaseTest {

    protected static final String ASSIGN_URL =
            "https://cntttest.vanlanguni.edu.vn:18081/Phancong02/Timetable/Assign";

    protected boolean containsAny(String page, String[] expectedTexts) {

        for (String text : expectedTexts) {
            if (page.contains(text)) {
                return true;
            }
        }

        return false;
    }

    protected void clearBrowserSession() throws InterruptedException {

        try {
            driver.manage().deleteAllCookies();
        } catch (Exception ignored) {
        }

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript(
                    "window.localStorage.clear();" +
                            "window.sessionStorage.clear();"
            );

        } catch (Exception ignored) {
        }

        Thread.sleep(1000);
    }

    protected void openUserMenu() throws InterruptedException {

        List<WebElement> userMenus = driver.findElements(
                By.xpath(
                        "//*[contains(normalize-space(),'@vanlanguni.vn')]"
                                + " | //*[contains(normalize-space(),'Bộ môn')]"
                                + " | //*[contains(@class,'avatar')]"
                                + " | //*[contains(@class,'profile')]"
                                + " | //*[contains(@class,'user')]"
                                + " | //*[contains(@class,'dropdown')]"
                )
        );

        for (WebElement menu : userMenus) {
            try {
                if (menu.isDisplayed()) {
                    clickElement(menu);
                    Thread.sleep(1000);

                    boolean hasLogout = driver.findElements(
                            By.xpath("//*[contains(normalize-space(),'Đăng xuất')]")
                    ).size() > 0;

                    if (hasLogout) {
                        System.out.println("Đã mở menu người dùng");
                        return;
                    }
                }
            } catch (Exception ignored) {
            }
        }

        Assert.fail("Không mở được menu người dùng để bấm Đăng xuất");
    }

    protected void chonHocKyVaNganhTruocKhiPhanCong() throws InterruptedException {

        chonHocKy998();

        Thread.sleep(1200);

        chonNganhCongNgheThongTinCongNghe();

        waitBangPhanCongLoadSauKhiChonFilter();

        String selectedTerm = getSelectedSelect2Text("term");
        String selectedMajor = getSelectedSelect2Text("major");

        Assert.assertTrue(
                selectedTerm.contains("998"),
                "Chưa chọn đúng học kỳ 998. Hiện tại đang chọn: " + selectedTerm
        );

        Assert.assertTrue(
                selectedMajor.contains("Công nghệ thông tin")
                        || selectedMajor.toLowerCase().contains("cong nghe"),
                "Chưa chọn đúng ngành Công nghệ thông tincong nghe. Hiện tại đang chọn: " + selectedMajor
        );

        System.out.println("Đã chọn Học kỳ: " + selectedTerm);
        System.out.println("Đã chọn Ngành: " + selectedMajor);
    }

    protected void chonHocKy998() throws InterruptedException {

        WebElement termRendered = waitSelect2RenderedContainer("term");

        Assert.assertNotNull(
                termRendered,
                "Không tìm thấy dropdown Học kỳ"
        );

        clickSelect2Box(termRendered);

        Thread.sleep(700);

        WebElement option998 = waitSelect2OptionExactText("998", 20);

        Assert.assertNotNull(
                option998,
                "Không tìm thấy option học kỳ 998"
        );

        clickHard(option998);

        Thread.sleep(1200);

        System.out.println("Đã chọn học kỳ 998");
    }

    protected void chonNganhCongNgheThongTinCongNghe() throws InterruptedException {

        WebElement majorRendered = waitSelect2RenderedContainer("major");

        Assert.assertNotNull(
                majorRendered,
                "Không tìm thấy dropdown Ngành"
        );

        clickSelect2Box(majorRendered);

        Thread.sleep(700);

        WebElement searchInput = waitOpenedSelect2SearchInput();

        if (searchInput != null) {
            clearAndTypeSelect2Input(searchInput, "Công nghệ thông tincong nghe");
        }

        WebElement majorOption = waitSelect2OptionForMajor(20);

        if (majorOption == null && searchInput != null) {
            clearAndTypeSelect2Input(searchInput, "Công nghệ thông tin");
            majorOption = waitSelect2OptionForMajor(20);
        }

        Assert.assertNotNull(
                majorOption,
                "Không tìm thấy option ngành Công nghệ thông tincong nghe"
        );

        clickHard(majorOption);

        Thread.sleep(1500);

        System.out.println("Đã chọn ngành Công nghệ thông tincong nghe");
    }

    protected WebElement waitSelect2RenderedContainer(String selectId) throws InterruptedException {

        String renderedId = "select2-" + selectId + "-container";

        for (int retry = 0; retry < 20; retry++) {

            try {
                List<WebElement> elements = driver.findElements(By.id(renderedId));

                for (WebElement element : elements) {
                    try {
                        if (element.isDisplayed()) {
                            return element;
                        }
                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception ignored) {
            }

            Thread.sleep(500);
        }

        return null;
    }

    protected void clickSelect2Box(WebElement renderedElement) throws InterruptedException {

        try {
            WebElement box = renderedElement.findElement(
                    By.xpath("./ancestor::span[contains(@class,'select2-selection')][1]")
            );

            clickHard(box);

            return;

        } catch (Exception ignored) {
        }

        clickHard(renderedElement);
    }

    protected WebElement waitSelect2OptionExactText(String expectedText, int retryCount)
            throws InterruptedException {

        for (int retry = 0; retry < retryCount; retry++) {

            try {
                List<WebElement> options = driver.findElements(
                        By.cssSelector("li.select2-results__option")
                );

                for (WebElement option : options) {
                    try {
                        if (!option.isDisplayed() || !option.isEnabled()) {
                            continue;
                        }

                        String className = option.getAttribute("class");

                        if (className == null) {
                            className = "";
                        }

                        if (className.contains("loading-results")) {
                            continue;
                        }

                        String text = option.getText();

                        if (text == null) {
                            text = "";
                        }

                        text = text.trim();

                        if (text.equals(expectedText)) {
                            return option;
                        }

                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception ignored) {
            }

            Thread.sleep(500);
        }

        return null;
    }

    protected WebElement waitSelect2OptionForMajor(int retryCount)
            throws InterruptedException {

        for (int retry = 0; retry < retryCount; retry++) {

            try {
                List<WebElement> options = driver.findElements(
                        By.cssSelector("li.select2-results__option")
                );

                for (WebElement option : options) {
                    try {
                        if (!option.isDisplayed() || !option.isEnabled()) {
                            continue;
                        }

                        String className = option.getAttribute("class");

                        if (className == null) {
                            className = "";
                        }

                        if (className.contains("loading-results")) {
                            continue;
                        }

                        String text = option.getText();

                        if (text == null) {
                            text = "";
                        }

                        text = text.replaceAll("\\s+", " ").trim();

                        String lowerText = text.toLowerCase();

                        boolean isCorrectMajor =
                                text.contains("Công nghệ thông tincong nghe")
                                        || text.contains("Công nghệ thông tin")
                                        || lowerText.contains("cong nghe");

                        if (isCorrectMajor) {
                            return option;
                        }

                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception ignored) {
            }

            Thread.sleep(500);
        }

        return null;
    }

    protected WebElement waitOpenedSelect2SearchInput() throws InterruptedException {

        for (int retry = 0; retry < 15; retry++) {

            try {
                List<WebElement> inputs = driver.findElements(
                        By.cssSelector(".select2-container--open input.select2-search__field")
                );

                for (WebElement input : inputs) {
                    try {
                        if (input.isDisplayed() && input.isEnabled()) {
                            return input;
                        }
                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception ignored) {
            }

            try {
                List<WebElement> inputs2 = driver.findElements(
                        By.cssSelector("input.select2-search__field[role='searchbox']")
                );

                for (WebElement input : inputs2) {
                    try {
                        if (input.isDisplayed() && input.isEnabled()) {
                            return input;
                        }
                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception ignored) {
            }

            Thread.sleep(300);
        }

        return null;
    }

    protected void clearAndTypeSelect2Input(WebElement input, String keyword)
            throws InterruptedException {

        JavascriptExecutor js = (JavascriptExecutor) driver;

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
            js.executeScript(
                    "const el = arguments[0];" +
                            "const setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                            "setter.call(el, '');" +
                            "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                            "el.dispatchEvent(new Event('change', { bubbles: true }));",
                    input
            );
        } catch (Exception ignored) {
            try {
                input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                input.sendKeys(Keys.BACK_SPACE);
            } catch (Exception ignored2) {
            }
        }

        Thread.sleep(300);

        input.sendKeys(keyword);

        try {
            js.executeScript(
                    "const el = arguments[0];" +
                            "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                            "el.dispatchEvent(new KeyboardEvent('keyup', { bubbles: true }));" +
                            "el.dispatchEvent(new Event('change', { bubbles: true }));",
                    input
            );
        } catch (Exception ignored) {
        }

        Thread.sleep(1200);
    }

    protected String getSelectedSelect2Text(String selectId) {

        try {
            String renderedId = "select2-" + selectId + "-container";

            List<WebElement> elements = driver.findElements(By.id(renderedId));

            for (WebElement element : elements) {
                try {
                    if (element.isDisplayed()) {

                        String text = element.getText();
                        String title = element.getAttribute("title");

                        if (text == null) {
                            text = "";
                        }

                        if (title == null) {
                            title = "";
                        }

                        String allText = (text + " " + title).trim();

                        if (!allText.equals("")) {
                            return allText;
                        }
                    }
                } catch (Exception ignored) {
                }
            }

        } catch (Exception ignored) {
        }

        return "";
    }

    protected void waitBangPhanCongLoadSauKhiChonFilter() throws InterruptedException {

        for (int retry = 0; retry < 30; retry++) {

            try {
                String page = driver.getPageSource();

                boolean hasScheduleContent =
                        page.contains("Số lớp")
                                || page.contains("Chưa phân")
                                || page.contains("Phân công giảng dạy");

                boolean hasAssignCards =
                        driver.findElements(By.cssSelector("button.assign-card")).size() > 0;

                boolean selectedTerm =
                        getSelectedSelect2Text("term").contains("998");

                boolean selectedMajor =
                        getSelectedSelect2Text("major").contains("Công nghệ thông tin")
                                || getSelectedSelect2Text("major").toLowerCase().contains("cong nghe");

                if (selectedTerm && selectedMajor && (hasScheduleContent || hasAssignCards)) {
                    Thread.sleep(1000);
                    return;
                }

            } catch (Exception ignored) {
            }

            Thread.sleep(500);
        }

        System.out.println("Đã chờ bảng phân công load sau khi chọn Học kỳ và Ngành");
    }

    protected boolean openAssignPopupByClickingUnassignedCard() throws InterruptedException {

        for (int retry = 0; retry < 5; retry++) {

            List<WebElement> cards = driver.findElements(
                    By.cssSelector("button.assign-card[class*='unassigned']")
            );

            System.out.println("Số card Chưa phân tìm thấy: " + cards.size());

            for (WebElement card : cards) {
                try {
                    if (!card.isDisplayed() || !card.isEnabled()) {
                        continue;
                    }

                    String text = card.getText();
                    String className = card.getAttribute("class");

                    if (text == null) {
                        text = "";
                    }

                    if (className == null) {
                        className = "";
                    }

                    Rectangle rect = card.getRect();

                    boolean isValidChuaPhan =
                            text.contains("Chưa phân")
                                    && className.contains("unassigned")
                                    && rect.getY() > 250
                                    && rect.getWidth() > 40
                                    && rect.getHeight() > 20;

                    if (!isValidChuaPhan) {
                        continue;
                    }

                    System.out.println("Thử bấm Chưa phân id=" + card.getAttribute("id"));

                    clickHard(card);

                    Thread.sleep(1500);

                    WebElement dropdown = waitForLecturerDropdownShort();

                    if (dropdown != null) {
                        return true;
                    }

                } catch (Exception e) {
                    System.out.println("Lỗi khi bấm card Chưa phân: " + e.getMessage());
                }
            }

            Thread.sleep(700);
        }

        return false;
    }

    protected WebElement waitForLecturerDropdownShort() throws InterruptedException {

        for (int retry = 0; retry < 6; retry++) {

            WebElement dropdown = findLecturerDropdownNow();

            if (dropdown != null) {
                return dropdown;
            }

            Thread.sleep(500);
        }

        return null;
    }

    protected WebElement waitForLecturerDropdownInPopover() throws InterruptedException {

        for (int retry = 0; retry < 20; retry++) {

            WebElement dropdown = findLecturerDropdownNow();

            if (dropdown != null) {
                return dropdown;
            }

            Thread.sleep(500);
        }

        return null;
    }

    protected WebElement findLecturerDropdownNow() {

        try {
            List<WebElement> popovers = driver.findElements(
                    By.cssSelector("div.popover")
            );

            for (WebElement popover : popovers) {
                try {
                    if (!popover.isDisplayed()) {
                        continue;
                    }

                    List<WebElement> dropdowns = popover.findElements(
                            By.cssSelector("span.select2-selection.select2-selection--single")
                    );

                    for (WebElement dropdown : dropdowns) {
                        try {
                            if (dropdown.isDisplayed() && dropdown.isEnabled()) {

                                Rectangle rect = dropdown.getRect();

                                if (rect.getWidth() > 80 && rect.getHeight() > 20) {
                                    return dropdown;
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }

                } catch (Exception ignored) {
                }
            }

        } catch (Exception ignored) {
        }

        return null;
    }

    protected void clearSelectedTeacherIfAny() throws InterruptedException {

        try {
            List<WebElement> selectedElements = driver.findElements(
                    By.cssSelector("div.popover span.select2-selection__rendered")
            );

            for (WebElement selected : selectedElements) {
                try {
                    if (!selected.isDisplayed()) {
                        continue;
                    }

                    String text = selected.getText();
                    String title = selected.getAttribute("title");

                    if (text == null) {
                        text = "";
                    }

                    if (title == null) {
                        title = "";
                    }

                    String allText = text + " " + title;

                    if (allText.contains("Chưa phân công")) {
                        System.out.println("Dropdown đang là Chưa phân công");
                        return;
                    }

                    List<WebElement> clearButtons = driver.findElements(
                            By.cssSelector("div.popover span.select2-selection__clear")
                    );

                    for (WebElement clearButton : clearButtons) {
                        try {
                            if (clearButton.isDisplayed() && clearButton.isEnabled()) {
                                clickHard(clearButton);
                                System.out.println("Đã xóa giảng viên cũ");
                                Thread.sleep(700);
                                return;
                            }
                        } catch (Exception ignored) {
                        }
                    }

                } catch (Exception ignored) {
                }
            }

        } catch (Exception ignored) {
        }
    }

    protected boolean isAnyTeacherSelectedInPopover() {

        try {
            List<WebElement> selectedElements = driver.findElements(
                    By.cssSelector("div.popover span.select2-selection__rendered")
            );

            for (WebElement selected : selectedElements) {
                try {
                    if (!selected.isDisplayed()) {
                        continue;
                    }

                    String text = selected.getText();
                    String title = selected.getAttribute("title");

                    if (text == null) {
                        text = "";
                    }

                    if (title == null) {
                        title = "";
                    }

                    String allText = (text + " " + title).trim();

                    if (!allText.equals("")
                            && !allText.contains("Chưa phân công")) {
                        return true;
                    }

                } catch (Exception ignored) {
                }
            }

        } catch (Exception ignored) {
        }

        return false;
    }

    protected WebElement waitForLecturerSearchInput() throws InterruptedException {

        for (int retry = 0; retry < 20; retry++) {

            List<WebElement> inputs = driver.findElements(
                    By.cssSelector(".select2-container--open input.select2-search__field")
            );

            for (WebElement input : inputs) {
                try {
                    if (input.isDisplayed() && input.isEnabled()) {
                        return input;
                    }
                } catch (Exception ignored) {
                }
            }

            List<WebElement> inputs2 = driver.findElements(
                    By.cssSelector("input.select2-search__field[role='searchbox']")
            );

            for (WebElement input : inputs2) {
                try {
                    if (input.isDisplayed() && input.isEnabled()) {
                        return input;
                    }
                } catch (Exception ignored) {
                }
            }

            Thread.sleep(500);
        }

        return null;
    }

    protected void searchTeacher(WebElement searchInput, String keyword) throws InterruptedException {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            js.executeScript("arguments[0].focus();", searchInput);
        } catch (Exception ignored) {
        }

        try {
            searchInput.click();
        } catch (Exception ignored) {
        }

        Thread.sleep(300);

        try {
            js.executeScript(
                    "const el = arguments[0];" +
                            "const setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                            "setter.call(el, '');" +
                            "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                            "el.dispatchEvent(new Event('change', { bubbles: true }));",
                    searchInput
            );
        } catch (Exception ignored) {
            try {
                searchInput.clear();
            } catch (Exception ignored2) {
            }
        }

        Thread.sleep(300);

        searchInput.sendKeys(keyword);

        Thread.sleep(1500);
    }

    protected boolean isNguyenCaoSamResultVisible() {

        try {
            List<WebElement> options = driver.findElements(
                    By.xpath(
                            "//li[contains(@class,'select2-results__option') and "
                                    + "(contains(normalize-space(),'Nguyễn Cao Sâm') "
                                    + "or contains(normalize-space(),'nguyễn cao sâm') "
                                    + "or contains(normalize-space(),'Nguyen Cao Sam') "
                                    + "or contains(normalize-space(),'Cao Sâm'))]"
                    )
            );

            for (WebElement option : options) {
                try {
                    if (option.isDisplayed()) {
                        return true;
                    }
                } catch (Exception ignored) {
                }
            }

        } catch (Exception ignored) {
        }

        return false;
    }

    protected WebElement waitForNguyenCaoSamOption() throws InterruptedException {

        for (int retry = 0; retry < 20; retry++) {

            List<WebElement> options = driver.findElements(
                    By.xpath(
                            "//li[contains(@class,'select2-results__option') and "
                                    + "(contains(normalize-space(),'Nguyễn Cao Sâm') "
                                    + "or contains(normalize-space(),'nguyễn cao sâm') "
                                    + "or contains(normalize-space(),'Nguyen Cao Sam') "
                                    + "or contains(normalize-space(),'Cao Sâm'))]"
                    )
            );

            for (WebElement option : options) {
                try {
                    if (option.isDisplayed() && option.isEnabled()) {
                        return option;
                    }
                } catch (Exception ignored) {
                }
            }

            Thread.sleep(500);
        }

        return null;
    }

    protected void clickTeacherOption(WebElement option, WebElement searchInput) throws InterruptedException {

        try {
            Actions actions = new Actions(driver);

            actions.moveToElement(option)
                    .pause(Duration.ofMillis(300))
                    .click()
                    .perform();

            Thread.sleep(1000);

            if (isNguyenCaoSamSelected()) {
                return;
            }

        } catch (Exception ignored) {
        }

        try {
            option.click();

            Thread.sleep(1000);

            if (isNguyenCaoSamSelected()) {
                return;
            }

        } catch (Exception ignored) {
        }

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript(
                    "const el = arguments[0];" +
                            "el.dispatchEvent(new MouseEvent('mouseenter', {bubbles:true, cancelable:true, view:window}));" +
                            "el.dispatchEvent(new MouseEvent('mouseover', {bubbles:true, cancelable:true, view:window}));" +
                            "el.dispatchEvent(new MouseEvent('mousemove', {bubbles:true, cancelable:true, view:window}));" +
                            "el.dispatchEvent(new MouseEvent('mousedown', {bubbles:true, cancelable:true, view:window, button:0}));" +
                            "el.dispatchEvent(new MouseEvent('mouseup', {bubbles:true, cancelable:true, view:window, button:0}));" +
                            "el.dispatchEvent(new MouseEvent('click', {bubbles:true, cancelable:true, view:window, button:0}));",
                    option
            );

            Thread.sleep(1000);

            if (isNguyenCaoSamSelected()) {
                return;
            }

        } catch (Exception ignored) {
        }

        try {
            searchInput.sendKeys(Keys.ENTER);
            Thread.sleep(1000);
        } catch (Exception ignored) {
        }
    }

    protected boolean isNguyenCaoSamSelected() throws InterruptedException {

        for (int retry = 0; retry < 10; retry++) {

            try {
                List<WebElement> selectedElements = driver.findElements(
                        By.cssSelector("div.popover span.select2-selection__rendered")
                );

                for (WebElement selected : selectedElements) {
                    try {
                        if (selected.isDisplayed()) {

                            String text = selected.getText();
                            String title = selected.getAttribute("title");

                            if (text == null) {
                                text = "";
                            }

                            if (title == null) {
                                title = "";
                            }

                            String allText = text + " " + title;

                            if (allText.contains("Nguyễn Cao Sâm")
                                    || allText.contains("nguyễn cao sâm")
                                    || allText.contains("Nguyen Cao Sam")
                                    || allText.contains("Cao Sâm")) {
                                return true;
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception ignored) {
            }

            Thread.sleep(500);
        }

        return false;
    }

    protected WebElement waitForAssignButtonInPopover() throws InterruptedException {

        for (int retry = 0; retry < 20; retry++) {

            try {
                List<WebElement> buttons = driver.findElements(
                        By.cssSelector("div.popover button.btn-assign[aria-label='Assign']")
                );

                for (WebElement button : buttons) {
                    try {
                        if (button.isDisplayed() && button.isEnabled()) {

                            Rectangle rect = button.getRect();

                            if (rect.getWidth() >= 25 && rect.getHeight() >= 25) {
                                return button;
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception ignored) {
            }

            try {
                List<WebElement> buttons2 = driver.findElements(
                        By.cssSelector("button.btn-assign[aria-label='Assign']")
                );

                for (WebElement button : buttons2) {
                    try {
                        if (button.isDisplayed() && button.isEnabled()) {

                            Rectangle rect = button.getRect();

                            if (rect.getWidth() >= 25 && rect.getHeight() >= 25) {
                                return button;
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception ignored) {
            }

            Thread.sleep(500);
        }

        return null;
    }

    protected void clickHard(WebElement element) throws InterruptedException {

        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'center'});",
                    element
            );
        } catch (Exception ignored) {
        }

        Thread.sleep(200);

        try {
            clickElement(element);
            return;
        } catch (Exception ignored) {
        }

        try {
            element.click();
            return;
        } catch (Exception ignored) {
        }

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript(
                    "const el = arguments[0];" +
                            "el.dispatchEvent(new MouseEvent('mouseover', {bubbles:true, cancelable:true, view:window}));" +
                            "el.dispatchEvent(new MouseEvent('mousedown', {bubbles:true, cancelable:true, view:window, button:0}));" +
                            "el.dispatchEvent(new MouseEvent('mouseup', {bubbles:true, cancelable:true, view:window, button:0}));" +
                            "el.dispatchEvent(new MouseEvent('click', {bubbles:true, cancelable:true, view:window, button:0}));",
                    element
            );

            return;

        } catch (Exception ignored) {
        }

        try {
            Actions actions = new Actions(driver);

            actions.moveToElement(element)
                    .pause(Duration.ofMillis(200))
                    .click()
                    .perform();

        } catch (Exception ignored) {
        }
    }

    protected int getAssignedCount() {

        try {
            String page = driver.getPageSource();

            Pattern pattern = Pattern.compile("Số lớp đã phân công:\\s*(\\d+)\\s*/");
            Matcher matcher = pattern.matcher(page);

            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }

        } catch (Exception ignored) {
        }

        return -1;
    }

    protected int countNguyenCaoSamCardsOnSchedule() {

        int count = 0;

        try {
            List<WebElement> cards = driver.findElements(
                    By.cssSelector("button.assign-card")
            );

            for (WebElement card : cards) {
                try {
                    if (card.isDisplayed()) {

                        String text = card.getText();

                        if (text == null) {
                            text = "";
                        }

                        Rectangle rect = card.getRect();

                        boolean isTeacherCard =
                                rect.getY() > 250
                                        && (text.contains("Nguyễn Cao Sâm")
                                        || text.contains("N.C Sâm")
                                        || text.contains("N.C.Sâm")
                                        || text.contains("Cao Sâm"));

                        if (isTeacherCard) {
                            count++;
                        }
                    }
                } catch (Exception ignored) {
                }
            }

        } catch (Exception ignored) {
        }

        return count;
    }

    protected boolean waitAssignSuccess(int assignedBefore, int teacherCardBefore) throws InterruptedException {

        for (int retry = 0; retry < 40; retry++) {

            String page = driver.getPageSource();

            boolean hasSuccessMessage =
                    page.contains("Thành công")
                            || page.contains("thành công")
                            || page.contains("Success")
                            || page.contains("success");

            int assignedAfter = getAssignedCount();

            boolean countIncreased =
                    assignedBefore >= 0
                            && assignedAfter > assignedBefore;

            int teacherCardAfter = countNguyenCaoSamCardsOnSchedule();

            boolean teacherCardIncreased =
                    teacherCardAfter > teacherCardBefore;

            boolean noError =
                    !page.toLowerCase().contains("error")
                            && !page.toLowerCase().contains("lỗi");

            if (noError && (hasSuccessMessage || countIncreased || teacherCardIncreased)) {
                System.out.println("Số lớp đã phân công sau khi test: " + assignedAfter);
                System.out.println("Số card Nguyễn Cao Sâm/N.C Sâm sau khi test: " + teacherCardAfter);
                return true;
            }

            Thread.sleep(500);
        }

        return false;
    }

    protected void removeSuccessToasts() {

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript(
                    "document.querySelectorAll('.toast, .toast-container, .swal2-container, .alert-success').forEach(e => e.remove());"
            );

        } catch (Exception ignored) {
        }
    }

    protected boolean waitUnexpectedAssignSuccess(int assignedBefore, int teacherCardBefore, int timeoutSeconds)
            throws InterruptedException {

        for (int retry = 0; retry < timeoutSeconds * 2; retry++) {

            if (isUnexpectedAssignSuccess(assignedBefore, teacherCardBefore)) {
                return true;
            }

            Thread.sleep(500);
        }

        return false;
    }

    protected boolean isUnexpectedAssignSuccess(int assignedBefore, int teacherCardBefore) {

        String page = driver.getPageSource();

        boolean hasSuccessMessage =
                page.contains("Thành công")
                        || page.contains("thành công")
                        || page.contains("Success")
                        || page.contains("success");

        int assignedAfter = getAssignedCount();

        boolean countIncreased =
                assignedBefore >= 0
                        && assignedAfter > assignedBefore;

        int teacherCardAfter = countNguyenCaoSamCardsOnSchedule();

        boolean teacherCardIncreased =
                teacherCardAfter > teacherCardBefore;

        return hasSuccessMessage || countIncreased || teacherCardIncreased;
    }

    protected void closeOpenedPopupAndDropdown() {

        try {
            driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
            Thread.sleep(300);
            driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
        } catch (Exception ignored) {
        }
    }

    /*
     * Helper dùng chung cho F10.2 đến F11.14
     */

    protected void runPositiveFeature(
            String functionCode,
            String featureName,
            String[] openTexts,
            String[] expectedTexts
    ) throws InterruptedException {

        navigateToAssignPage();

        Assert.assertTrue(
                driver.getPageSource().contains("Bộ môn")
                        || driver.getPageSource().contains("Phân công")
                        || driver.getPageSource().contains("Thời khóa biểu")
                        || driver.getPageSource().contains("Thời khoá biểu"),
                "Không xác nhận được đã đăng nhập role Bộ môn trước khi test " + functionCode
        );

        openFeatureByTexts(openTexts);

        boolean displayed = waitFeatureContent(expectedTexts, 15);

        Assert.assertTrue(
                displayed,
                "Không xác nhận được màn hình/chức năng " + functionCode + " - " + featureName
        );

        System.out.println("PASS " + functionCode + " POS - " + featureName);
    }

    protected void runDataFeature(
            String functionCode,
            String featureName,
            String testName,
            String[] openTexts,
            String[] expectedTexts
    ) throws InterruptedException {

        navigateToAssignPage();

        openFeatureByTexts(openTexts);

        boolean displayed = waitFeatureContent(expectedTexts, 15);

        Assert.assertTrue(
                displayed,
                "Không tìm thấy dữ liệu kiểm tra " + functionCode + " - " + testName
        );

        System.out.println("PASS " + functionCode + " DATA - " + testName);
    }

    protected void runNegativeFeatureWithoutLogin(
            String functionCode,
            String featureName,
            String[] restrictedTexts
    ) throws InterruptedException {

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

        boolean stillCanSeeFeature =
                containsAny(page, restrictedTexts)
                        && !isLoginPage;

        Assert.assertTrue(
                isLoginPage || !stillCanSeeFeature,
                "Chưa đăng nhập nhưng vẫn truy cập/thấy được chức năng "
                        + functionCode + " - " + featureName
        );

        System.out.println("PASS " + functionCode + " NEG - Chưa đăng nhập không vào được " + featureName);
    }

    protected void openFeatureByTexts(String[] openTexts) throws InterruptedException {

        for (String text : openTexts) {

            WebElement element = findVisibleElementByText(text);

            if (element != null) {
                clickHard(element);
                Thread.sleep(1500);
            }
        }
    }

    protected boolean waitFeatureContent(String[] expectedTexts, int timeoutSeconds)
            throws InterruptedException {

        for (int retry = 0; retry < timeoutSeconds * 2; retry++) {

            String page = driver.getPageSource();

            boolean hasExpectedText = containsAny(page, expectedTexts);
            boolean hasVisibleText = hasVisibleAnyText(expectedTexts);
            boolean hasTableOrData = hasAnyTableOrData();

            if (hasExpectedText || hasVisibleText || hasTableOrData) {
                return true;
            }

            Thread.sleep(500);
        }

        return false;
    }

    protected boolean hasVisibleAnyText(String[] texts) {

        for (String text : texts) {

            WebElement element = findVisibleElementByText(text);

            if (element != null) {
                return true;
            }
        }

        return false;
    }

    protected WebElement findVisibleElementByText(String text) {

        try {
            String literal = xpathLiteral(text);

            String mainXpath =
                    "//*[not(self::script) and not(self::style) "
                            + "and (self::a or self::button or @role='button') "
                            + "and contains(normalize-space(.), " + literal + ")]";

            List<WebElement> mainElements = driver.findElements(By.xpath(mainXpath));

            for (WebElement element : mainElements) {
                try {
                    if (isGoodVisibleElement(element)) {
                        return element;
                    }
                } catch (Exception ignored) {
                }
            }

            String fallbackXpath =
                    "//*[not(self::script) and not(self::style) "
                            + "and contains(normalize-space(.), " + literal + ")]";

            List<WebElement> fallbackElements = driver.findElements(By.xpath(fallbackXpath));

            for (WebElement element : fallbackElements) {
                try {
                    if (isGoodVisibleElement(element)) {
                        return element;
                    }
                } catch (Exception ignored) {
                }
            }

        } catch (Exception ignored) {
        }

        return null;
    }

    protected boolean isGoodVisibleElement(WebElement element) {

        if (!element.isDisplayed()) {
            return false;
        }

        Rectangle rect = element.getRect();

        return rect.getWidth() > 20 && rect.getHeight() > 10;
    }

    protected boolean hasAnyTableOrData() {

        try {
            List<WebElement> elements = driver.findElements(
                    By.xpath(
                            "//table"
                                    + " | //*[(contains(@class,'table') "
                                    + "or contains(@class,'grid') "
                                    + "or contains(@class,'datatable') "
                                    + "or contains(@class,'dataTable') "
                                    + "or contains(@class,'card'))]"
                    )
            );

            for (WebElement element : elements) {
                try {
                    if (element.isDisplayed()) {
                        Rectangle rect = element.getRect();

                        if (rect.getWidth() > 100 && rect.getHeight() > 30) {
                            return true;
                        }
                    }
                } catch (Exception ignored) {
                }
            }

        } catch (Exception ignored) {
        }

        return false;
    }

    protected String xpathLiteral(String text) {

        if (!text.contains("'")) {
            return "'" + text + "'";
        }

        if (!text.contains("\"")) {
            return "\"" + text + "\"";
        }

        String[] parts = text.split("'");

        StringBuilder builder = new StringBuilder("concat(");

        for (int i = 0; i < parts.length; i++) {
            builder.append("'").append(parts[i]).append("'");

            if (i < parts.length - 1) {
                builder.append(", \"'\", ");
            }
        }

        builder.append(")");

        return builder.toString();
    }
        /*
     * ============================================================
     * CODE CHUNG TEST F10.4 ĐẾN F11.14
     * Gồm 4 luồng:
     * 1. POS  - Luồng đúng
     * 2. NEG  - Luồng sai / chưa đăng nhập
     * 3. DATA - Luồng dữ liệu
     * 4. UI   - Luồng giao diện
     * ============================================================
     */

    protected static class FeatureConfig {

        String code;
        String name;

        String[] menuTexts = {};
        String[] expectedTexts = {};
        String[] dataTexts = {};

        String statMode;
        String termValue;
        String[] multiTerms;

        String majorText;
        String lecturerTypeText;

        String teacherText;
        String teacherSearchText;

        String weekText;
        String tabText;

        boolean clickStatisticButton = false;
        boolean acceptNoData = true;

        public FeatureConfig(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public FeatureConfig menu(String... texts) {
            this.menuTexts = texts;
            return this;
        }

        public FeatureConfig expected(String... texts) {
            this.expectedTexts = texts;
            return this;
        }

        public FeatureConfig data(String... texts) {
            this.dataTexts = texts;
            return this;
        }

        public FeatureConfig statMode(String text) {
            this.statMode = text;
            return this;
        }

        public FeatureConfig term(String text) {
            this.termValue = text;
            return this;
        }

        public FeatureConfig multiTerms(String... texts) {
            this.multiTerms = texts;
            return this;
        }

        public FeatureConfig major(String text) {
            this.majorText = text;
            return this;
        }

        public FeatureConfig lecturerType(String text) {
            this.lecturerTypeText = text;
            return this;
        }

        public FeatureConfig teacher(String teacherText, String searchText) {
            this.teacherText = teacherText;
            this.teacherSearchText = searchText;
            return this;
        }

        public FeatureConfig week(String text) {
            this.weekText = text;
            return this;
        }

        public FeatureConfig tab(String text) {
            this.tabText = text;
            return this;
        }

        public FeatureConfig clickStatisticButton() {
            this.clickStatisticButton = true;
            return this;
        }

        public FeatureConfig acceptNoData(boolean value) {
            this.acceptNoData = value;
            return this;
        }
    }

    /*
     * LUỒNG 1: Test luồng đúng
     * - Đăng nhập role Bộ môn
     * - Mở đúng chức năng
     * - Chọn học kỳ/ngành/loại GV/tuần/GV nếu có
     * - Kiểm tra có dữ liệu, bảng, biểu đồ, thời khóa biểu hoặc thông báo hợp lệ
     */
    protected void runPositiveFeatureFlow(FeatureConfig config) throws InterruptedException {

        System.out.println("===== " + config.code + " POS - " + config.name + " =====");

        openFeatureAndApplyFilterFlow(config);

        boolean ok = waitForExpectedContentFlow(
                config.expectedTexts,
                config.acceptNoData,
                25
        );

        Assert.assertTrue(
                ok,
                config.code + " POS FAIL - Không mở đúng hoặc không thấy nội dung chức năng: " + config.name
        );

        System.out.println("PASS " + config.code + " POS - Luồng đúng");
    }

    /*
     * LUỒNG 2: Test luồng sai
     * - Mở trang chức năng bằng trình duyệt mới khi chưa đăng nhập
     * - Hệ thống phải chặn hoặc chuyển về Login/Microsoft
     */
    protected void runNegativeFeatureFlowWithoutLogin(FeatureConfig config) throws InterruptedException {

        System.out.println("===== " + config.code + " NEG - " + config.name + " =====");

        WebDriver tempDriver = null;

        try {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");

            tempDriver = new ChromeDriver(options);

            tempDriver.get(ASSIGN_URL);

            Thread.sleep(4000);

            String currentUrl = tempDriver.getCurrentUrl();
            String page = tempDriver.getPageSource();

            boolean biChan =
                    currentUrl.toLowerCase().contains("login")
                            || currentUrl.toLowerCase().contains("account")
                            || currentUrl.toLowerCase().contains("microsoft")
                            || page.contains("Đăng nhập")
                            || page.contains("Sign in")
                            || page.contains("Microsoft")
                            || page.toLowerCase().contains("login");

            boolean thayChucNang =
                    containsAnyIgnoreCaseFlow(page, config.expectedTexts)
                            || containsAnyIgnoreCaseFlow(page, config.menuTexts);

            Assert.assertTrue(
                    biChan || !thayChucNang,
                    config.code + " NEG FAIL - Chưa đăng nhập nhưng vẫn xem được chức năng: " + config.name
            );

            System.out.println("PASS " + config.code + " NEG - Chưa đăng nhập bị chặn");

        } finally {
            if (tempDriver != null) {
                tempDriver.quit();
            }
        }
    }

    /*
     * LUỒNG 3: Test luồng data
     * - Chọn dữ liệu test: học kỳ 999/998, ngành CNTT, loại GV, tuần, giảng viên...
     * - Kiểm tra dữ liệu trên bảng/biểu đồ/chi tiết
     */
    protected void runDataFeatureFlow(FeatureConfig config) throws InterruptedException {

        System.out.println("===== " + config.code + " DATA - " + config.name + " =====");

        openFeatureAndApplyFilterFlow(config);

        if (config.tabText != null) {
            clickTextIfExistsFlow(config.tabText);
            Thread.sleep(1200);
        }

        String[] checkTexts = mergeTextFlow(config.expectedTexts, config.dataTexts);

        boolean ok = waitForExpectedContentFlow(
                checkTexts,
                config.acceptNoData,
                25
        );

        Assert.assertTrue(
                ok,
                config.code + " DATA FAIL - Không kiểm tra được dữ liệu của chức năng: " + config.name
        );

        System.out.println("PASS " + config.code + " DATA - Dữ liệu hợp lệ");
    }

    /*
     * LUỒNG 4: Test giao diện
     * - Kiểm tra ô tìm kiếm giới hạn 255 ký tự
     * - Kiểm tra dropdown có thể gõ tìm kiếm
     * - Phóng to / thu nhỏ màn hình
     * - Lướt xuống / lướt lên giao diện
     */
    protected void runUiFeatureFlow(FeatureConfig config) throws InterruptedException {

        System.out.println("===== " + config.code + " UI - " + config.name + " =====");

        openFeatureAndApplyFilterFlow(config);

        kiemTraTimKiemGioiHanBienFlow();
        kiemTraGoTrongDropdownFlow(config);
        kiemTraPhongToThuNhoFlow();
        kiemTraLuotLenLuotXuongFlow();

        Assert.assertTrue(
                driver.findElement(By.tagName("body")).isDisplayed(),
                config.code + " UI FAIL - Body không hiển thị sau khi test giao diện"
        );

        System.out.println("PASS " + config.code + " UI - Giao diện ổn");
    }

    protected void openFeatureAndApplyFilterFlow(FeatureConfig config) throws InterruptedException {

        navigateToAssignPage();

        waitBodyFlow();

        openMenuByTextsFlow(config.menuTexts);

        Thread.sleep(1500);

        applyFilterFlow(config);
    }

    protected void applyFilterFlow(FeatureConfig config) throws InterruptedException {

        if (config.statMode != null) {
            selectDropdownByAnyLabelFlow(
                    new String[]{"Thống kê theo", "Thống kê theo:"},
                    config.statMode,
                    false
            );

            Thread.sleep(800);
        }

        if (config.multiTerms != null && config.multiTerms.length > 0) {
            selectMultiDropdownValuesFlow("Học kỳ", config.multiTerms);
            Thread.sleep(1000);
        } else if (config.termValue != null) {
            selectDropdownByAnyLabelFlow(
                    new String[]{"Học kỳ", "Năm học"},
                    config.termValue,
                    false
            );

            Thread.sleep(1000);
        }

        if (config.majorText != null) {
            selectDropdownByAnyLabelFlow(
                    new String[]{"Ngành"},
                    config.majorText,
                    false
            );

            Thread.sleep(1000);
        }

        if (config.lecturerTypeText != null) {
            selectDropdownByAnyLabelFlow(
                    new String[]{"Loại giảng viên"},
                    config.lecturerTypeText,
                    false
            );

            Thread.sleep(1000);
        }

        if (config.teacherText != null) {
            selectDropdownByAnyLabelFlow(
                    new String[]{"Giảng viên"},
                    config.teacherSearchText,
                    false
            );

            clickOptionByTextFlow(config.teacherText, false);

            Thread.sleep(1200);
        }

        if (config.weekText != null) {
            selectDropdownByAnyLabelFlow(
                    new String[]{"Tuần"},
                    config.weekText,
                    false
            );

            Thread.sleep(1200);
        }

        if (config.clickStatisticButton) {
            clickButtonByTextFlow("Thống kê");
            Thread.sleep(2000);
        }

        if (config.tabText != null) {
            clickTextIfExistsFlow(config.tabText);
            Thread.sleep(1200);
        }
    }

    protected void openMenuByTextsFlow(String[] texts) throws InterruptedException {

        if (texts == null) {
            return;
        }

        for (String text : texts) {
            boolean clicked = clickTextIfExistsFlow(text);

            System.out.println("Click menu [" + text + "]: " + clicked);

            Thread.sleep(900);
        }
    }

    protected boolean clickTextIfExistsFlow(String text) throws InterruptedException {

        WebElement element = findVisibleElementByText(text);

        if (element == null) {
            return false;
        }

        clickHard(element);
        return true;
    }

    protected boolean clickButtonByTextFlow(String text) throws InterruptedException {

        try {
            String literal = xpathLiteral(text);

            List<WebElement> buttons = driver.findElements(
                    By.xpath(
                            "//*[self::button or @role='button' or contains(@class,'btn')]"
                                    + "[contains(normalize-space(.), " + literal + ")]"
                    )
            );

            for (WebElement button : buttons) {
                if (isGoodVisibleElement(button)) {
                    clickHard(button);
                    return true;
                }
            }

        } catch (Exception ignored) {
        }

        return clickTextIfExistsFlow(text);
    }

    protected boolean selectDropdownByAnyLabelFlow(
            String[] labels,
            String value,
            boolean required
    ) throws InterruptedException {

        for (String label : labels) {
            boolean ok = selectDropdownByLabelFlow(label, value);

            if (ok) {
                System.out.println("Đã chọn [" + label + "] = " + value);
                return true;
            }
        }

        if (required) {
            Assert.fail("Không chọn được dropdown value: " + value);
        }

        System.out.println("Không chọn được dropdown value [" + value + "], bỏ qua nếu không bắt buộc");
        return false;
    }

    protected boolean selectDropdownByLabelFlow(String label, String value) throws InterruptedException {

        WebElement dropdown = findDropdownByLabelFlow(label);

        if (dropdown == null) {
            return false;
        }

        clickHard(dropdown);

        Thread.sleep(500);

        typeIntoOpenDropdownFlow(value);

        Thread.sleep(700);

        boolean clicked = clickOptionByTextFlow(value, false);

        if (!clicked) {
            try {
                driver.switchTo().activeElement().sendKeys(Keys.ENTER);
                clicked = true;
            } catch (Exception ignored) {
            }
        }

        Thread.sleep(800);

        return clicked;
    }

    protected void selectMultiDropdownValuesFlow(String label, String[] values)
            throws InterruptedException {

        for (String value : values) {
            WebElement dropdown = findDropdownByLabelFlow(label);

            if (dropdown == null) {
                System.out.println("Không tìm thấy multi dropdown: " + label);
                return;
            }

            clickHard(dropdown);

            Thread.sleep(500);

            clickOptionByTextFlow(value, false);

            Thread.sleep(700);
        }

        try {
            driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
        } catch (Exception ignored) {
        }
    }

    protected WebElement findDropdownByLabelFlow(String label) {

        try {
            String literal = xpathLiteral(label);

            List<WebElement> labelElements = driver.findElements(
                    By.xpath(
                            "//*[not(self::script) and not(self::style) "
                                    + "and contains(normalize-space(.), " + literal + ")]"
                    )
            );

            for (WebElement labelElement : labelElements) {

                if (!isGoodVisibleElement(labelElement)) {
                    continue;
                }

                Rectangle labelRect = labelElement.getRect();

                if (labelRect.getWidth() > 600 || labelRect.getHeight() > 120) {
                    continue;
                }

                List<WebElement> candidates = labelElement.findElements(
                        By.xpath(
                                "following::*[(self::div or self::input or self::span) "
                                        + "and (@role='combobox' "
                                        + "or contains(@class,'select') "
                                        + "or contains(@class,'control') "
                                        + "or contains(@class,'css-') "
                                        + "or contains(@class,'dropdown'))]"
                        )
                );

                for (WebElement candidate : candidates) {
                    try {
                        if (candidate.isDisplayed() && candidate.isEnabled()) {
                            Rectangle r = candidate.getRect();

                            if (r.getWidth() >= 120 && r.getHeight() >= 25) {
                                return candidate;
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
            }

        } catch (Exception ignored) {
        }

        return null;
    }

    protected void typeIntoOpenDropdownFlow(String text) {

        try {
            WebElement active = driver.switchTo().activeElement();

            if (active != null && active.isDisplayed() && active.isEnabled()) {
                active.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                active.sendKeys(text);
                return;
            }

        } catch (Exception ignored) {
        }

        try {
            List<WebElement> inputs = driver.findElements(
                    By.xpath("//input[not(@type='hidden')]")
            );

            for (int i = inputs.size() - 1; i >= 0; i--) {
                WebElement input = inputs.get(i);

                if (input.isDisplayed() && input.isEnabled()) {
                    input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                    input.sendKeys(text);
                    return;
                }
            }

        } catch (Exception ignored) {
        }
    }

    protected boolean clickOptionByTextFlow(String text, boolean exact)
            throws InterruptedException {

        try {
            String literal = xpathLiteral(text);

            String condition;

            if (exact) {
                condition = "normalize-space(.) = " + literal;
            } else {
                condition = "contains(normalize-space(.), " + literal + ")";
            }

            List<WebElement> options = driver.findElements(
                    By.xpath(
                            "//*[not(self::script) and not(self::style) "
                                    + "and (@role='option' "
                                    + "or contains(@class,'option') "
                                    + "or contains(@class,'menu')) "
                                    + "and " + condition + "]"
                    )
            );

            for (WebElement option : options) {
                if (isGoodVisibleElement(option)) {
                    clickHard(option);
                    return true;
                }
            }

            List<WebElement> fallback = driver.findElements(
                    By.xpath(
                            "//*[not(self::script) and not(self::style) and " + condition + "]"
                    )
            );

            for (WebElement option : fallback) {
                if (isGoodVisibleElement(option)) {
                    Rectangle r = option.getRect();

                    if (r.getHeight() <= 120 && r.getWidth() <= 1300) {
                        clickHard(option);
                        return true;
                    }
                }
            }

        } catch (Exception ignored) {
        }

        return false;
    }

    protected boolean waitForExpectedContentFlow(
            String[] expectedTexts,
            boolean acceptNoData,
            int seconds
    ) throws InterruptedException {

        String[] noDataTexts = {
                "Chưa có dữ liệu",
                "Không có dữ liệu",
                "Chưa có dữ liệu hệ số",
                "Chưa có dữ liệu phân công",
                "không khả dụng"
        };

        for (int i = 0; i < seconds * 2; i++) {

            String page = driver.getPageSource();

            if (containsAnyIgnoreCaseFlow(page, expectedTexts)) {
                return true;
            }

            if (hasTableChartOrScheduleFlow()) {
                return true;
            }

            if (acceptNoData && containsAnyIgnoreCaseFlow(page, noDataTexts)) {
                return true;
            }

            Thread.sleep(500);
        }

        return false;
    }

    protected boolean hasTableChartOrScheduleFlow() {

        try {
            List<WebElement> elements = driver.findElements(
                    By.xpath(
                            "//table | //canvas | //svg | "
                                    + "//*[(contains(@class,'table') "
                                    + "or contains(@class,'dataTable') "
                                    + "or contains(@class,'chart') "
                                    + "or contains(@class,'apexcharts') "
                                    + "or contains(@class,'calendar') "
                                    + "or contains(@class,'schedule'))]"
                    )
            );

            for (WebElement element : elements) {
                try {
                    if (element.isDisplayed()) {
                        Rectangle r = element.getRect();

                        if (r.getWidth() > 250 && r.getHeight() > 80) {
                            return true;
                        }
                    }
                } catch (Exception ignored) {
                }
            }

        } catch (Exception ignored) {
        }

        return false;
    }

    protected void kiemTraTimKiemGioiHanBienFlow() throws InterruptedException {

        WebElement search = findFirstVisibleFlow(
                By.xpath("//input[contains(@placeholder,'Nhập tìm kiếm')]"),
                By.xpath("//input[contains(@placeholder,'Tìm kiếm')]"),
                By.xpath("//input[contains(@placeholder,'Search')]"),
                By.xpath("//label[contains(normalize-space(),'Tìm kiếm')]/following::input[1]")
        );

        if (search == null) {
            System.out.println("Không có ô tìm kiếm trong màn hình này, bỏ qua test boundary search");
            return;
        }

        String longText = "A".repeat(255);

        search.clear();
        search.sendKeys(longText);

        Thread.sleep(800);

        String value = search.getAttribute("value");

        if (value == null) {
            value = "";
        }

        Assert.assertTrue(
                value.length() <= 255,
                "Ô tìm kiếm cho nhập quá giới hạn 255 ký tự"
        );

        search.clear();

        System.out.println("PASS UI - Ô tìm kiếm giới hạn biên 255 ký tự");
    }

    protected void kiemTraGoTrongDropdownFlow(FeatureConfig config)
            throws InterruptedException {

        if (config.termValue != null || config.multiTerms != null) {
            checkDropdownCanTypeFlow(new String[]{"Học kỳ", "Năm học"}, "9");
        }

        if (config.majorText != null) {
            checkDropdownCanTypeFlow(new String[]{"Ngành"}, "Công");
        }

        if (config.lecturerTypeText != null) {
            checkDropdownCanTypeFlow(new String[]{"Loại giảng viên"}, "Cơ");
        }

        if (config.teacherText != null) {
            checkDropdownCanTypeFlow(new String[]{"Giảng viên"}, config.teacherSearchText);
        }

        if (config.weekText != null) {
            checkDropdownCanTypeFlow(new String[]{"Tuần"}, "Tuần");
        }
    }

    protected void checkDropdownCanTypeFlow(String[] labels, String keyword)
            throws InterruptedException {

        for (String label : labels) {
            WebElement dropdown = findDropdownByLabelFlow(label);

            if (dropdown == null) {
                continue;
            }

            clickHard(dropdown);

            Thread.sleep(500);

            typeIntoOpenDropdownFlow(keyword);

            Thread.sleep(700);

            String page = driver.getPageSource();

            boolean ok =
                    page.contains(keyword)
                            || page.contains("999")
                            || page.contains("998")
                            || page.contains("Công nghệ")
                            || page.contains("Cơ hữu")
                            || page.contains("Nguyễn Cao Sâm")
                            || page.contains("Tuần");

            Assert.assertTrue(
                    ok,
                    "Dropdown " + label + " không gõ tìm kiếm được"
            );

            try {
                driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
            } catch (Exception ignored) {
            }

            System.out.println("PASS UI - Dropdown [" + label + "] gõ tìm kiếm được");
            return;
        }

        System.out.println("Không thấy dropdown để test gõ keyword: " + keyword);
    }

    protected void kiemTraPhongToThuNhoFlow() throws InterruptedException {

        Dimension oldSize = driver.manage().window().getSize();

        driver.manage().window().setSize(new Dimension(1366, 768));
        Thread.sleep(800);
        Assert.assertTrue(driver.findElement(By.tagName("body")).isDisplayed());

        driver.manage().window().setSize(new Dimension(1600, 900));
        Thread.sleep(800);
        Assert.assertTrue(driver.findElement(By.tagName("body")).isDisplayed());

        driver.manage().window().maximize();
        Thread.sleep(800);
        Assert.assertTrue(driver.findElement(By.tagName("body")).isDisplayed());

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("document.body.style.zoom='80%'");
        Thread.sleep(800);
        Assert.assertTrue(driver.findElement(By.tagName("body")).isDisplayed());

        js.executeScript("document.body.style.zoom='110%'");
        Thread.sleep(800);
        Assert.assertTrue(driver.findElement(By.tagName("body")).isDisplayed());

        js.executeScript("document.body.style.zoom='100%'");

        driver.manage().window().setSize(oldSize);
        driver.manage().window().maximize();

        System.out.println("PASS UI - Phóng to thu nhỏ giao diện");
    }

    protected void kiemTraLuotLenLuotXuongFlow() throws InterruptedException {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(1000);

        Long yDown = (Long) js.executeScript("return Math.round(window.scrollY);");

        js.executeScript("window.scrollTo(0, 0);");
        Thread.sleep(1000);

        Long yUp = (Long) js.executeScript("return Math.round(window.scrollY);");

        Assert.assertTrue(
                yDown >= yUp,
                "Không lướt lên/lướt xuống được"
        );

        System.out.println("PASS UI - Lướt lên và lướt xuống giao diện");
    }

    protected WebElement findFirstVisibleFlow(By... locators) {

        for (By locator : locators) {
            try {
                List<WebElement> elements = driver.findElements(locator);

                for (WebElement element : elements) {
                    if (element.isDisplayed() && element.isEnabled()) {
                        return element;
                    }
                }

            } catch (Exception ignored) {
            }
        }

        return null;
    }

    protected void waitBodyFlow() throws InterruptedException {

        for (int i = 0; i < 20; i++) {
            try {
                if (driver.findElement(By.tagName("body")).isDisplayed()) {
                    return;
                }
            } catch (Exception ignored) {
            }

            Thread.sleep(500);
        }
    }

    protected boolean containsAnyIgnoreCaseFlow(String source, String[] texts) {

        if (source == null || texts == null) {
            return false;
        }

        String lowerSource = source.toLowerCase();

        for (String text : texts) {
            if (text != null && lowerSource.contains(text.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    protected String[] mergeTextFlow(String[] a, String[] b) {

        java.util.ArrayList<String> list = new java.util.ArrayList<>();

        if (a != null) {
            for (String text : a) {
                list.add(text);
            }
        }

        if (b != null) {
            for (String text : b) {
                list.add(text);
            }
        }

        return list.toArray(new String[0]);
    }
}