package com.test.chucnang.quanlithulao;
import org.testng.annotations.Test;

import com.test.BoMonCommonTest;
public class F12_1_XemDanhSachThuLaoGiangVienTest extends BoMonCommonTest {
     protected FeatureConfig config() {
        return new FeatureConfig("F12.1", "Xem danh sách thù lao của giảng viên")
                .menu("Thù lao")
                .term("999")
                .expected(
                        "Thù lao giảng viên",
                        "Thù lao",
                        "Học kỳ",
                        "999",
                        "Chưa có dữ liệu hệ số cho năm học này",
                        "Chưa có dữ liệu hệ số"
                )
                .data(
                        "999",
                        "Học kỳ",
                        "Chưa có dữ liệu hệ số cho năm học này"
                )
                .acceptNoData(true);
    }

    @Test(priority = 1)
    public void F12_1_POS_XemDanhSachThuLaoGiangVien() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    @Test(priority = 2)
    public void F12_1_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    @Test(priority = 3)
    public void F12_1_DATA_KiemTraDuLieuThuLaoGiangVien() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    @Test(priority = 4)
    public void F12_1_UI_KiemTraGiaoDienThuLaoGiangVien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
