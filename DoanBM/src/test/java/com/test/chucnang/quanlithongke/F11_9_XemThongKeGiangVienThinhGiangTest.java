package com.test.chucnang.quanlithongke;

import org.testng.annotations.Test;

import com.test.BoMonCommonTest;

public class F11_9_XemThongKeGiangVienThinhGiangTest extends BoMonCommonTest {
    protected FeatureConfig config() {
        return new FeatureConfig("F11.9", "Xem thống kê giảng viên thỉnh giảng")
                .menu("Thống kê", "GV thỉnh giảng")
                .multiTerms("999", "998")
                .clickStatisticButton()
                .expected(
                        "Thống kê giảng viên thỉnh giảng",
                        "MÃ GVTG",
                        "HỌ TÊN GVTG",
                        "CẤP BẬC",
                        "999",
                        "998"
                )
                .data(
                        "CNTTK28",
                        "Nhập môn Công nghệ thông tin",
                        "Lập trình hướng đối tượng",
                        "Lập trình ứng dụng Web"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - chọn 2 học kỳ 999 và 998 rồi bấm Thống kê
    @Test(priority = 1)
    public void F11_9_POS_LuongDung_XemThongKeGiangVienThinhGiang() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai
    @Test(priority = 2)
    public void F11_9_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data - kiểm tra bảng thỉnh giảng
    @Test(priority = 3)
    public void F11_9_DATA_KiemTraThongKeGiangVienThinhGiang() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI - dropdown multi học kỳ, search, export, scroll
    @Test(priority = 4)
    public void F11_9_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
