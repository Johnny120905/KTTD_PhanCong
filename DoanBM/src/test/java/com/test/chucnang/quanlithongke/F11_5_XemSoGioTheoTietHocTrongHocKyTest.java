package com.test.chucnang.quanlithongke;

import org.testng.annotations.Test;
import com.test.BoMonCommonTest;

public class F11_5_XemSoGioTheoTietHocTrongHocKyTest extends BoMonCommonTest{
    
    
    protected FeatureConfig config() {
        return new FeatureConfig("F11.5", "Xem số giờ theo tiết học trong học kỳ")
                .menu("Thống kê", "Số giờ giảng viên")
                .statMode("Học kỳ")
                .term("999")
                .major("Công nghệ thông tincong nghe")
                .lecturerType("Cơ hữu")
                .tab("Chi tiết")
                .expected(
                        "Thống kê số giờ giảng viên",
                        "MÃ GV",
                        "TÊN GV",
                        "SỐ LỚP",
                        "CHI TIẾT LỚP"
                )
                .data(
                        "Nguyễn Cao Sâm",
                        "Lập trình hướng đối tượng",
                        "Thiết kế giao diện người dùng",
                        "30 tiết"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - xem chi tiết số giờ theo tiết trong học kỳ
    @Test(priority = 1)
    public void F11_5_POS_LuongDung_XemSoGioTheoTietHocTrongHocKy() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai
    @Test(priority = 2)
    public void F11_5_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data - kiểm tra chi tiết lớp, môn học, số tiết
    @Test(priority = 3)
    public void F11_5_DATA_KiemTraSoGioTheoTietHocTrongHocKy() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI
    @Test(priority = 4)
    public void F11_5_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
