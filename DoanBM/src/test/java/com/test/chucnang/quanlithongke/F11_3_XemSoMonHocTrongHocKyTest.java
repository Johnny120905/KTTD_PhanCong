package com.test.chucnang.quanlithongke;
import org.testng.annotations.Test;
import com.test.BoMonCommonTest;

public class F11_3_XemSoMonHocTrongHocKyTest extends BoMonCommonTest{
    
    protected FeatureConfig config() {
        return new FeatureConfig("F11.3", "Xem số môn học trong học kỳ")
                .menu("Thống kê", "Số giờ giảng viên")
                .statMode("Học kỳ")
                .term("999")
                .major("Công nghệ thông tincong nghe")
                .lecturerType("Cơ hữu")
                .tab("Bảng biểu")
                .expected(
                        "Thống kê số giờ giảng viên",
                        "MÃ GV",
                        "TÊN GV",
                        "SỐ HP",
                        "SỐ LỚP",
                        "SỐ GIỜ GIẢNG"
                )
                .data(
                        "Nguyễn Cao Sâm",
                        "4",
                        "6",
                        "180",
                        "Cơ hữu"
                )
                .acceptNoData(true);
    }

    // LUỒNG 1: Test luồng đúng - mở bảng biểu số môn học trong học kỳ
    @Test(priority = 1)
    public void F11_3_POS_LuongDung_XemSoMonHocTrongHocKy() throws InterruptedException {
        runPositiveFeatureFlow(config());
    }

    // LUỒNG 2: Test luồng sai - chưa đăng nhập bị chặn
    @Test(priority = 2)
    public void F11_3_NEG_ChuaDangNhapBiChan() throws InterruptedException {
        runNegativeFeatureFlowWithoutLogin(config());
    }

    // LUỒNG 3: Test data - kiểm tra cột SỐ HP, SỐ LỚP
    @Test(priority = 3)
    public void F11_3_DATA_KiemTraSoMonHocTrongHocKy() throws InterruptedException {
        runDataFeatureFlow(config());
    }

    // LUỒNG 4: Test UI
    @Test(priority = 4)
    public void F11_3_UI_KiemTraGiaoDien() throws InterruptedException {
        runUiFeatureFlow(config());
    }
}
