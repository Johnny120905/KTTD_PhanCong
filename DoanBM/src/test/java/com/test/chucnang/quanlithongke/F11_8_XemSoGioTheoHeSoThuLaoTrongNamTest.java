package com.test.chucnang.quanlithongke;
import org.testng.annotations.Test;

import com.test.BoMonCommonTest;

public class F11_8_XemSoGioTheoHeSoThuLaoTrongNamTest extends BoMonCommonTest {
       protected FeatureConfig config() {
        return new FeatureConfig("F11.8", "Xem số giờ quy đổi theo hệ số thù lao trong năm học")
                .menu("Thống kê", "Số giờ quy đổi")
                .statMode("Năm học")
                .term("999")
                .major("Công nghệ thông tincong nghe")
                .expected(
                        "Thống kê số giờ quy đổi giảng viên",
                        "Chưa có dữ liệu hệ số cho năm học này",
                        "Năm học",
                        "Ngành"
                )
                .data(
                        "999",
                        "Công nghệ thông tincong nghe",
                        "Chưa có dữ liệu hệ số"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - mở số giờ quy đổi theo năm học
    @Test(priority = 1)
    public void F11_8_POS_LuongDung_XemSoGioQuyDoiTrongNam() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai
    @Test(priority = 2)
    public void F11_8_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data
    @Test(priority = 3)
    public void F11_8_DATA_KiemTraSoGioQuyDoiTrongNam() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI
    @Test(priority = 4)
    public void F11_8_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
