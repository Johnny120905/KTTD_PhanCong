package com.test.chucnang.quanlithoikhoabieu;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.test.BoMonCommonTest;
public class F10_2_XemThoiKhoaBieuToanKhoaTest extends BoMonCommonTest {
    
    private final String functionCode = "F10.2";
    private final String featureName = "Xem thời khóa biểu toàn khoa";

    private final String[] openTexts = {
            "Quản lý thời khóa biểu",
            "Xem thời khóa biểu toàn khoa",
            "Xem thời khoá biểu toàn khoa",
            "Toàn khoa"
    };

    private final String[] expectedTexts = {
            "Thời khóa biểu toàn khoa",
            "Thời khoá biểu toàn khoa",
            "Toàn khoa",
            "Tuần",
            "Khoa"
    };

    @Test(priority = 1)
    public void F10_2_POS_XemThoiKhoaBieuToanKhoaThanhCong() throws InterruptedException {
        runPositiveFeature(functionCode, featureName, openTexts, expectedTexts);
    }

   
    @DataProvider(name = "F10_2_Data")
    public Object[][] F10_2_Data() {
        return new Object[][]{
                {"Kiểm tra tiêu đề toàn khoa", new String[]{"Thời khóa biểu toàn khoa", "Thời khoá biểu toàn khoa", "Toàn khoa"}},
                {"Kiểm tra xem theo tuần", new String[]{"Tuần", "week", "Week"}},
                {"Kiểm tra dữ liệu thời khóa biểu", new String[]{"Khoa", "Lớp", "Môn học", "Giảng viên"}}
        };
    }

    @Test(priority = 3, dataProvider = "F10_2_Data")
    public void F10_2_DATA_XemThoiKhoaBieuToanKhoa(String testName, String[] dataTexts)
            throws InterruptedException {
        runDataFeature(functionCode, featureName, testName, openTexts, dataTexts);
    }
}
