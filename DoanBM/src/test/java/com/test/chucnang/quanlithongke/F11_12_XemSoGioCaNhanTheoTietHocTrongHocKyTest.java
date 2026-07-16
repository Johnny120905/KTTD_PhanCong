package com.test.chucnang.quanlithongke;

import org.testng.annotations.Test;

import com.test.BoMonCommonTest;
public class F11_12_XemSoGioCaNhanTheoTietHocTrongHocKyTest  extends BoMonCommonTest{
   protected FeatureConfig config() {
        return new FeatureConfig("F11.12", "Xem số giờ cá nhân theo tiết học trong học kỳ")
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

    // LUỒNG 1: Test luồng đúng - xem số giờ cá nhân theo tiết trong học kỳ
    @Test(priority = 1)
    public void F11_12_POS_LuongDung_XemSoGioCaNhanTheoTietHocTrongHocKy() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai
    @Test(priority = 2)
    public void F11_12_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data
    @Test(priority = 3)
    public void F11_12_DATA_KiemTraSoGioCaNhanTheoTietHocTrongHocKy() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI
    @Test(priority = 4)
    public void F11_12_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
