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
}