package com.test.chucnang.quanlithongke;

import org.testng.annotations.Test;

import com.test.BoMonCommonTest;

public class F11_10_XemSoGioCaNhanTrongHocKyTest extends BoMonCommonTest{
     protected FeatureConfig config() {
        return new FeatureConfig("F11.10", "Xem số giờ cá nhân trong học kỳ")
                .menu("Thống kê", "Số giờ cá nhân")
                .statMode("Học kỳ")
                .term("999")
                .expected(
                        "Thống kê số giờ cá nhân",
                        "Chưa có dữ liệu hệ số cho năm học này",
                        "Học kỳ"
                )
                .data(
                        "999",
                        "Học kỳ",
                        "Chưa có dữ liệu"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - xem số giờ cá nhân theo học kỳ
    @Test(priority = 1)
    public void F11_10_POS_LuongDung_XemSoGioCaNhanTrongHocKy() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai
    @Test(priority = 2)
    public void F11_10_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data
    @Test(priority = 3)
    public void F11_10_DATA_KiemTraSoGioCaNhanTrongHocKy() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI
    @Test(priority = 4)
    public void F11_10_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
