package com.test.chucnang.quanlithongke;
import org.testng.annotations.Test;
import com.test.BoMonCommonTest;

public class F11_2_XemSoGioTrongNamTest extends BoMonCommonTest{
    protected FeatureConfig config() {
    return new FeatureConfig("F11.2", "Xem số giờ giảng viên trong năm học")
            .menu("Thống kê", "Số giờ giảng viên")
             .statMode("Năm học")
            .term("2033 - 2034")
            .major("Công nghệ thông tincong nghe")
            .lecturerType("Cơ hữu")
            .tab("Biểu đồ")
            .expected(
                    "Thống kê số giờ giảng viên",
                    "Năm học",
                    "Công nghệ thông tincong nghe",
                    "Cơ hữu",
                    "Chưa có dữ liệu"
            )
            .data(
                    "Năm học",
                    "2033 - 2034",
                    "Cơ hữu",
                    "Công nghệ thông tincong nghe",
                    "Chưa có dữ liệu"
            )
            .acceptNoData(true);
}

    // LUỒNG 1: Test luồng đúng - xem số giờ GV theo năm học
    @Test(priority = 1)
    public void F11_2_POS_LuongDung_XemSoGioTrongNam() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai - chưa đăng nhập bị chặn
    @Test(priority = 2)
    public void F11_2_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data - kiểm tra dữ liệu năm học
    @Test(priority = 3)
    public void F11_2_DATA_KiemTraSoGioTrongNam() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI
    @Test(priority = 4)
    public void F11_2_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
