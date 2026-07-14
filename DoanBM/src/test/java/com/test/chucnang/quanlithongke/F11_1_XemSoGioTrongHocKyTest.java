package com.test.chucnang.quanlithongke;
import org.testng.annotations.Test;
import com.test.BoMonCommonTest;

public class F11_1_XemSoGioTrongHocKyTest extends BoMonCommonTest {
    
    protected FeatureConfig config() {
        return new FeatureConfig("F11.1", "Xem số giờ giảng viên trong học kỳ")
                .menu("Thống kê", "Số giờ giảng viên")
                .statMode("Học kỳ")
                .term("999")
                .major("Công nghệ thông tincong nghe")
                .lecturerType("Cơ hữu")
                .tab("Biểu đồ")
                .expected(
                        "Thống kê số giờ giảng viên",
                        "Số giảng viên",
                        "Tổng số giờ",
                        "Nguyễn Cao Sâm",
                        "HK999"
                )
                .data(
                        "180",
                        "660",
                        "Phạm Hoài Phúc",
                        "Ngô Thanh Hiền",
                        "Cơ hữu"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - mở thống kê số giờ GV theo học kỳ 999
    @Test(priority = 1)
    public void F11_1_POS_LuongDung_XemSoGioTrongHocKy() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai - chưa đăng nhập bị chặn
    @Test(priority = 2)
    public void F11_1_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data - kiểm tra biểu đồ, tổng giờ, tên GV
    @Test(priority = 3)
    public void F11_1_DATA_KiemTraSoGioTrongHocKy() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI - tìm kiếm, dropdown, phóng to/thu nhỏ, scroll
    @Test(priority = 4)
    public void F11_1_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
