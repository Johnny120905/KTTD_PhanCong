package com.test.chucnang.quanlithongke;

import org.testng.annotations.Test;

import com.test.BoMonCommonTest;

public class F11_14_XemThoiKhoaBieuGiangVienTest extends BoMonCommonTest {
      protected FeatureConfig config() {
        return new FeatureConfig("F11.14", "Xem lịch giảng dạy giảng viên")
                .menu("Thống kê", "Lịch giảng dạy")
                .term("998")
                .week("Tuần 44")
                .expected(
                        "Thống kê lịch giảng dạy",
                        "Tuần 44",
                        "Từ ngày",
                        "THỨ 2",
                        "THỨ 3",
                        "TIẾT"
                )
                .data(
                        "08/10/2020",
                        "14/10/2020",
                        "Lý thuyết",
                        "Thực hành"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - mở lịch giảng dạy, chọn học kỳ 998, tuần 44
    @Test(priority = 1)
    public void F11_14_POS_LuongDung_XemLichGiangDay() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai - chưa đăng nhập bị chặn
    @Test(priority = 2)
    public void F11_14_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data - kiểm tra tuần, ngày, lý thuyết, thực hành
    @Test(priority = 3)
    public void F11_14_DATA_KiemTraDuLieuLichGiangDay() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI - dropdown học kỳ, dropdown tuần, scroll, zoom
    @Test(priority = 4)
    public void F11_14_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
