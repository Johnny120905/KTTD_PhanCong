package com.test.chucnang.quanlithoikhoabieu;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.test.BoMonCommonTest;
public class F10_3_PhanCongGiangVienTest extends BoMonCommonTest{
    
    /*
     * F10.3 - LUỒNG ĐÚNG
     * Chọn học kỳ 998, ngành Công nghệ thông tincong nghe,
     * sau đó phân công Nguyễn Cao Sâm
     */
    @Test(priority = 1)
    public void F10_3_POS_PhanCongNguyenCaoSamThanhCong()
            throws InterruptedException {

        navigateToAssignPage();

        Assert.assertTrue(
                driver.getPageSource().contains("Phân công giảng dạy")
                        || driver.getPageSource().contains("Phân công"),
                "Không vào được màn hình Phân công giảng dạy"
        );

        chonHocKyVaNganhTruocKhiPhanCong();

        int assignedBefore = getAssignedCount();
        int teacherCardBefore = countNguyenCaoSamCardsOnSchedule();

        System.out.println("Số lớp đã phân công trước khi test: " + assignedBefore);
        System.out.println("Số card Nguyễn Cao Sâm/N.C Sâm trước khi test: " + teacherCardBefore);

        boolean openedPopup = openAssignPopupByClickingUnassignedCard();

        Assert.assertTrue(
                openedPopup,
                "Không bấm mở được popup phân công từ ô Chưa phân"
        );

        WebElement lecturerDropdown = waitForLecturerDropdownInPopover();

        Assert.assertNotNull(
                lecturerDropdown,
                "Không tìm thấy dropdown Chưa phân công trong popup"
        );

        clearSelectedTeacherIfAny();

        Thread.sleep(700);

        lecturerDropdown = waitForLecturerDropdownInPopover();

        Assert.assertNotNull(
                lecturerDropdown,
                "Không tìm thấy lại dropdown Chưa phân công"
        );

        clickHard(lecturerDropdown);

        Thread.sleep(1000);

        WebElement searchInput = waitForLecturerSearchInput();

        Assert.assertNotNull(
                searchInput,
                "Không tìm thấy ô search giảng viên trong dropdown"
        );

        searchTeacher(searchInput, "sâm");

        WebElement nguyenCaoSamOption = waitForNguyenCaoSamOption();

        if (nguyenCaoSamOption == null) {
            searchTeacher(searchInput, "Nguyễn Cao Sâm");
            nguyenCaoSamOption = waitForNguyenCaoSamOption();
        }

        Assert.assertNotNull(
                nguyenCaoSamOption,
                "Không tìm thấy option Nguyễn Cao Sâm"
        );

        clickTeacherOption(nguyenCaoSamOption, searchInput);

        Thread.sleep(1500);

        Assert.assertTrue(
                isNguyenCaoSamSelected(),
                "Chưa thấy Nguyễn Cao Sâm được chọn trong dropdown"
        );

        WebElement assignButton = waitForAssignButtonInPopover();

        Assert.assertNotNull(
                assignButton,
                "Không tìm thấy nút tích màu tím Assign"
        );

        clickHard(assignButton);

        boolean success = waitAssignSuccess(assignedBefore, teacherCardBefore);

        Assert.assertTrue(
                success,
                "Chưa xác nhận được phân công thành công"
        );

        System.out.println("PASS F10.3 POS - Phân công giảng viên Nguyễn Cao Sâm thành công");
    }

    /*
     * F10.3 - LUỒNG SAI 1
     * Search giảng viên không tồn tại
     */
    @Test(priority = 2)
    public void F10_3_NEG_SearchGiangVienKhongTonTai()
            throws InterruptedException {

        navigateToAssignPage();

        Assert.assertTrue(
                driver.getPageSource().contains("Phân công giảng dạy")
                        || driver.getPageSource().contains("Phân công"),
                "Không vào được màn hình Phân công giảng dạy"
        );

        chonHocKyVaNganhTruocKhiPhanCong();

        boolean openedPopup = openAssignPopupByClickingUnassignedCard();

        Assert.assertTrue(
                openedPopup,
                "Không mở được popup phân công từ ô Chưa phân"
        );

        WebElement lecturerDropdown = waitForLecturerDropdownInPopover();

        Assert.assertNotNull(
                lecturerDropdown,
                "Không tìm thấy dropdown Chưa phân công trong popup"
        );

        clearSelectedTeacherIfAny();

        Thread.sleep(700);

        lecturerDropdown = waitForLecturerDropdownInPopover();

        Assert.assertNotNull(
                lecturerDropdown,
                "Không tìm thấy lại dropdown Chưa phân công"
        );

        clickHard(lecturerDropdown);

        Thread.sleep(1000);

        WebElement searchInput = waitForLecturerSearchInput();

        Assert.assertNotNull(
                searchInput,
                "Không tìm thấy ô search giảng viên"
        );

        String invalidKeyword = "zzzzzz999999";

        searchTeacher(searchInput, invalidKeyword);

        boolean hasNguyenCaoSam = isNguyenCaoSamResultVisible();

        Assert.assertFalse(
                hasNguyenCaoSam,
                "Search keyword sai nhưng vẫn thấy Nguyễn Cao Sâm"
        );

        closeOpenedPopupAndDropdown();

        System.out.println("PASS F10.3 NEG - Search giảng viên không tồn tại không trả về Nguyễn Cao Sâm");
    }

    /*
     * F10.3 - LUỒNG SAI 2
     * Không chọn giảng viên nhưng bấm Assign
     */
    @Test(priority = 3)
    public void F10_3_NEG_BamAssignKhiChuaChonGiangVien()
            throws InterruptedException {

        navigateToAssignPage();

        Assert.assertTrue(
                driver.getPageSource().contains("Phân công giảng dạy")
                        || driver.getPageSource().contains("Phân công"),
                "Không vào được màn hình Phân công giảng dạy"
        );

        chonHocKyVaNganhTruocKhiPhanCong();

        int assignedBefore = getAssignedCount();
        int teacherCardBefore = countNguyenCaoSamCardsOnSchedule();

        boolean openedPopup = openAssignPopupByClickingUnassignedCard();

        Assert.assertTrue(
                openedPopup,
                "Không mở được popup phân công từ ô Chưa phân"
        );

        WebElement lecturerDropdown = waitForLecturerDropdownInPopover();

        Assert.assertNotNull(
                lecturerDropdown,
                "Không tìm thấy dropdown Chưa phân công trong popup"
        );

        clearSelectedTeacherIfAny();

        Thread.sleep(1000);

        boolean teacherStillSelected = isAnyTeacherSelectedInPopover();

        Assert.assertFalse(
                teacherStillSelected,
                "Dropdown vẫn đang có giảng viên được chọn, không thể test luồng sai chưa chọn giảng viên"
        );

        removeSuccessToasts();

        WebElement assignButton = waitForAssignButtonInPopover();

        Assert.assertNotNull(
                assignButton,
                "Không tìm thấy nút Assign để test luồng sai"
        );

        clickHard(assignButton);

        boolean unexpectedSuccess = waitUnexpectedAssignSuccess(assignedBefore, teacherCardBefore, 6);

        Assert.assertFalse(
                unexpectedSuccess,
                "Hệ thống vẫn phân công thành công dù chưa chọn giảng viên"
        );

        closeOpenedPopupAndDropdown();

        System.out.println("PASS F10.3 NEG - Không chọn giảng viên thì không phân công thành công");
    }

    /*
     * F10.3 - LUỒNG DATA HỢP LỆ
     */
    @DataProvider(name = "F10_3_ValidTeacherSearchData")
    public Object[][] F10_3_ValidTeacherSearchData() {
        return new Object[][]{
                {"sâm", "Nguyễn Cao Sâm"},
                {"Nguyễn Cao Sâm", "Nguyễn Cao Sâm"},
                {"Cao Sâm", "Nguyễn Cao Sâm"}
        };
    }

    @Test(priority = 4, dataProvider = "F10_3_ValidTeacherSearchData")
    public void F10_3_DATA_SearchGiangVienHopLe(String keyword, String expectedTeacher)
            throws InterruptedException {

        navigateToAssignPage();

        Assert.assertTrue(
                driver.getPageSource().contains("Phân công giảng dạy")
                        || driver.getPageSource().contains("Phân công"),
                "Không vào được màn hình Phân công giảng dạy"
        );

        chonHocKyVaNganhTruocKhiPhanCong();

        boolean openedPopup = openAssignPopupByClickingUnassignedCard();

        Assert.assertTrue(
                openedPopup,
                "Không mở được popup phân công từ ô Chưa phân"
        );

        WebElement lecturerDropdown = waitForLecturerDropdownInPopover();

        Assert.assertNotNull(
                lecturerDropdown,
                "Không tìm thấy dropdown Chưa phân công trong popup"
        );

        clearSelectedTeacherIfAny();

        Thread.sleep(700);

        lecturerDropdown = waitForLecturerDropdownInPopover();

        Assert.assertNotNull(
                lecturerDropdown,
                "Không tìm thấy lại dropdown Chưa phân công"
        );

        clickHard(lecturerDropdown);

        Thread.sleep(1000);

        WebElement searchInput = waitForLecturerSearchInput();

        Assert.assertNotNull(
                searchInput,
                "Không tìm thấy ô search giảng viên"
        );

        searchTeacher(searchInput, keyword);

        WebElement teacherOption = waitForNguyenCaoSamOption();

        Assert.assertNotNull(
                teacherOption,
                "Không tìm thấy Nguyễn Cao Sâm với keyword: " + keyword
        );

        String optionText = teacherOption.getText();

        Assert.assertTrue(
                optionText.contains(expectedTeacher)
                        || optionText.contains("Cao Sâm")
                        || optionText.contains("Nguyễn Cao Sâm"),
                "Kết quả search không đúng giảng viên mong muốn"
        );

        closeOpenedPopupAndDropdown();

        System.out.println("PASS F10.3 DATA - Keyword '" + keyword + "' tìm thấy Nguyễn Cao Sâm");
    }

    /*
     * F10.3 - LUỒNG DATA KHÔNG HỢP LỆ
     */
    @DataProvider(name = "F10_3_InvalidTeacherSearchData")
    public Object[][] F10_3_InvalidTeacherSearchData() {
        return new Object[][]{
                {"zzzzzz999999"},
                {"khongcogiangvien"},
                {"abcxyz123456"}
        };
    }

    @Test(priority = 5, dataProvider = "F10_3_InvalidTeacherSearchData")
    public void F10_3_DATA_SearchGiangVienKhongHopLe(String keyword)
            throws InterruptedException {

        navigateToAssignPage();

        Assert.assertTrue(
                driver.getPageSource().contains("Phân công giảng dạy")
                        || driver.getPageSource().contains("Phân công"),
                "Không vào được màn hình Phân công giảng dạy"
        );

        chonHocKyVaNganhTruocKhiPhanCong();

        boolean openedPopup = openAssignPopupByClickingUnassignedCard();

        Assert.assertTrue(
                openedPopup,
                "Không mở được popup phân công từ ô Chưa phân"
        );

        WebElement lecturerDropdown = waitForLecturerDropdownInPopover();

        Assert.assertNotNull(
                lecturerDropdown,
                "Không tìm thấy dropdown Chưa phân công trong popup"
        );

        clearSelectedTeacherIfAny();

        Thread.sleep(700);

        lecturerDropdown = waitForLecturerDropdownInPopover();

        Assert.assertNotNull(
                lecturerDropdown,
                "Không tìm thấy lại dropdown Chưa phân công"
        );

        clickHard(lecturerDropdown);

        Thread.sleep(1000);

        WebElement searchInput = waitForLecturerSearchInput();

        Assert.assertNotNull(
                searchInput,
                "Không tìm thấy ô search giảng viên"
        );

        searchTeacher(searchInput, keyword);

        boolean hasNguyenCaoSam = isNguyenCaoSamResultVisible();

        Assert.assertFalse(
                hasNguyenCaoSam,
                "Keyword sai nhưng vẫn tìm thấy Nguyễn Cao Sâm: " + keyword
        );

        closeOpenedPopupAndDropdown();

        System.out.println("PASS F10.3 DATA - Keyword sai '" + keyword + "' không trả về Nguyễn Cao Sâm");
    }
}
