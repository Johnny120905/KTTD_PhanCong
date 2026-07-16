package com.test.chucnang.quanlithoikhoabieu;
import org.testng.annotations.Test;

import com.test.BoMonCommonTest;
public class F10_4_XemThoiKhoaBieuTest extends BoMonCommonTest {
     protected FeatureConfig config() {
        return new FeatureConfig("F10.4", "Xem thời khóa biểu cá nhân")
                .menu("Thời khoá biểu", "Thời khóa biểu", "Xem TKB")
                .term("999")
                .teacher("Nguyễn Cao Sâm", "sâm")
                .week("Tuần 5")
                .expected(
                        "Thời khoá biểu cá nhân",
                        "Thời khóa biểu cá nhân",
                        "Nguyễn Cao Sâm",
                        "Tuần 5",
                        "Mã LHP",
                        "Lập trình",
                        "Tiết"
                )
                .data(
                        "Lập trình hướng đối tượng",
                        "Lập trình Hệ thống nhúng và mạng kết nối vạn vật",
                        "Thiết kế giao diện người dùng",
                        "Phòng",
                        "Tuần BD/KT"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - mở Xem TKB, chọn học kỳ 999, GV Nguyễn Cao Sâm, tuần 5
    @Test(priority = 1)
    public void F10_4_POS_LuongDung_XemThoiKhoaBieuCaNhan() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai - chưa đăng nhập thì không được xem chức năng
    @Test(priority = 2)
    public void F10_4_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test dữ liệu - kiểm tra có môn học, phòng, tuần, tiết trên TKB
    @Test(priority = 3)
    public void F10_4_DATA_KiemTraDuLieuThoiKhoaBieu() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test giao diện - dropdown, phóng to/thu nhỏ, lướt lên/lướt xuống
    @Test(priority = 4)
    public void F10_4_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
