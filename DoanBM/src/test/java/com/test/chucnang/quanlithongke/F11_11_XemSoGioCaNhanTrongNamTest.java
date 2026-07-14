package com.test.chucnang.quanlithongke;

import org.testng.annotations.Test;

import com.test.BoMonCommonTest;
public class F11_11_XemSoGioCaNhanTrongNamTest  extends BoMonCommonTest{
       protected FeatureConfig config() {
        return new FeatureConfig("F11.11", "Xem số giờ cá nhân trong năm học")
                .menu("Thống kê", "Số giờ cá nhân")
                .statMode("Năm học")
                .term("999")
                .expected(
                        "Thống kê số giờ cá nhân",
                        "Chưa có dữ liệu hệ số cho năm học này",
                        "Năm học"
                )
                .data(
                        "999",
                        "Năm học",
                        "Chưa có dữ liệu"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - xem số giờ cá nhân theo năm học
    @Test(priority = 1)
    public void F11_11_POS_LuongDung_XemSoGioCaNhanTrongNam() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai
    @Test(priority = 2)
    public void F11_11_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data
    @Test(priority = 3)
    public void F11_11_DATA_KiemTraSoGioCaNhanTrongNam() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI
    @Test(priority = 4)
    public void F11_11_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
