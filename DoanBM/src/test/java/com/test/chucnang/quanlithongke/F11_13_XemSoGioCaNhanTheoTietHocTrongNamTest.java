package com.test.chucnang.quanlithongke;

import org.testng.annotations.Test;

import com.test.BoMonCommonTest;
public class F11_13_XemSoGioCaNhanTheoTietHocTrongNamTest extends BoMonCommonTest {
    
    protected FeatureConfig config() {
        return new FeatureConfig("F11.13", "Xem số giờ cá nhân theo tiết học trong năm học")
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

    // LUỒNG 1: Test luồng đúng - xem số giờ cá nhân theo tiết trong năm học
    @Test(priority = 1)
    public void F11_13_POS_LuongDung_XemSoGioCaNhanTheoTietHocTrongNam() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai
    @Test(priority = 2)
    public void F11_13_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data
    @Test(priority = 3)
    public void F11_13_DATA_KiemTraSoGioCaNhanTheoTietHocTrongNam() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI
    @Test(priority = 4)
    public void F11_13_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
