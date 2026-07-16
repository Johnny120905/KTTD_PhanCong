package com.test.chucnang.quanlithongke;
import org.testng.annotations.Test;

import com.test.BoMonCommonTest;

public class F11_4_XemSoMonHocTrongNamTest extends BoMonCommonTest{
        protected FeatureConfig config() {
        return new FeatureConfig("F11.4", "Xem số môn học trong năm học")
                .menu("Thống kê", "Số giờ giảng viên")
                .statMode("Năm học")
                .term("999")
                .major("Công nghệ thông tincong nghe")
                .lecturerType("Cơ hữu")
                .tab("Bảng biểu")
                .expected(
                        "Thống kê số giờ giảng viên",
                        "MÃ GV",
                        "TÊN GV",
                        "SỐ HP",
                        "SỐ LỚP"
                )
                .data(
                        "Nguyễn Cao Sâm",
                        "Cơ hữu",
                        "Export",
                        "Tìm kiếm"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - xem số môn học trong năm học
    @Test(priority = 1)
    public void F11_4_POS_LuongDung_XemSoMonHocTrongNam() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai
    @Test(priority = 2)
    public void F11_4_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data
    @Test(priority = 3)
    public void F11_4_DATA_KiemTraSoMonHocTrongNam() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI
    @Test(priority = 4)
    public void F11_4_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
