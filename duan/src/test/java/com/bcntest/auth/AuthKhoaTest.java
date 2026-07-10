package com.bcntest.auth;

import com.bcntest.core.BaseTest; // Import BaseTest từ thư mục core
import org.testng.annotations.Test;
import org.testng.Assert;

public class AuthKhoaTest extends BaseTest {

    @Test
    public void testQuyTrinhDangNhap() {
        System.out.println("=== BẮT ĐẦU KIỂM TRA QUY TRÌNH ĐĂNG NHẬP ===");
        
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL hiện tại đang đứng: " + currentUrl);
        
        boolean isSuccess = isLoggedIn();
        
        if(isSuccess) {
            System.out.println("=> TUYỆT VỜI! ĐÃ THẤY GIAO DIỆN CỦA BAN CHỦ NHIỆM.");
        } else {
            System.out.println("=> THẤT BẠI! CHƯA VÀO ĐƯỢC HỆ THỐNG.");
        }

        Assert.assertTrue(isSuccess, "Lỗi: Không thể xác nhận giao diện Ban chủ nhiệm!");
    }
}