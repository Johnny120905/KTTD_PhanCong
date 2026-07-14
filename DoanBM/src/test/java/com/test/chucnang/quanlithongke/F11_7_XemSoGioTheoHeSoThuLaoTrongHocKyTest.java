package com.test.chucnang.quanlithongke;
import org.testng.annotations.Test;

import com.test.BoMonCommonTest;
public class F11_7_XemSoGioTheoHeSoThuLaoTrongHocKyTest extends BoMonCommonTest {
     protected FeatureConfig config() {
        return new FeatureConfig("F11.7", "Xem số giờ quy đổi theo hệ số thù lao trong học kỳ")
                .menu("Thống kê", "Số giờ quy đổi")
                .statMode("Học kỳ")
                .term("999")
                .major("Công nghệ thông tincong nghe")
                .expected(
                        "Thống kê số giờ quy đổi giảng viên",
                        "Chưa có dữ liệu hệ số cho năm học này",
                        "Học kỳ",
                        "Ngành"
                )
                .data(
                        "999",
                        "Công nghệ thông tincong nghe",
                        "Chưa có dữ liệu hệ số"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - mở số giờ quy đổi theo học kỳ
    @Test(priority = 1)
    public void F11_7_POS_LuongDung_XemSoGioQuyDoiTrongHocKy() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai
    @Test(priority = 2)
    public void F11_7_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data - chọn học kỳ 999, ngành CNTT
    @Test(priority = 3)
    public void F11_7_DATA_KiemTraSoGioQuyDoiTrongHocKy() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI
    @Test(priority = 4)
    public void F11_7_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
