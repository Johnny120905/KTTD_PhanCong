package com.test.chucnang.quanlithongke;

import org.testng.annotations.Test;

import com.test.BoMonCommonTest;
public class F11_6_XemSoGioTheoTietHocTrongNamTest extends BoMonCommonTest{
    protected FeatureConfig config() {
        return new FeatureConfig("F11.6", "Xem số giờ theo tiết học trong năm học")
                .menu("Thống kê", "Số giờ giảng viên")
                .statMode("Năm học")
                .term("999")
                .major("Công nghệ thông tincong nghe")
                .lecturerType("Cơ hữu")
                .tab("Chi tiết")
                .expected(
                        "Thống kê số giờ giảng viên",
                        "CHI TIẾT LỚP",
                        "MÃ GV",
                        "TÊN GV"
                )
                .data(
                        "Nguyễn Cao Sâm",
                        "Lập trình",
                        "30 tiết",
                        "Phòng"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - xem chi tiết số giờ theo tiết trong năm học
    @Test(priority = 1)
    public void F11_6_POS_LuongDung_XemSoGioTheoTietHocTrongNam() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai
    @Test(priority = 2)
    public void F11_6_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data
    @Test(priority = 3)
    public void F11_6_DATA_KiemTraSoGioTheoTietHocTrongNam() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI
    @Test(priority = 4)
    public void F11_6_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
