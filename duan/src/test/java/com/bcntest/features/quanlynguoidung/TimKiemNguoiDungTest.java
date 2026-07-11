package com.bcntest.features.quanlynguoidung;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyNguoiDungPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TimKiemNguoiDungTest extends BaseTest {

    QuanLyNguoiDungPage nguoiDungPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        nguoiDungPage = new QuanLyNguoiDungPage(driver);
        driver.get(BASE_URL + "User"); 
        Thread.sleep(3000); 
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (Happy Path)
    // ==========================================
    @Test(priority = 1)
    public void testF25_LuongDung_TimKiemTuKhoaHopLe() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: ĐIỀN TỪ KHÓA VÀO THANH TÌM KIẾM ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        String keyword = "Nguyễn";
        nguoiDungPage.timKiemUser(keyword);
        Thread.sleep(2000); 
        
        String noiDungBang = nguoiDungPage.layNoiDungBang();
        Assert.assertFalse(nguoiDungPage.kiemTraBangRong(), "Fail Logic: Bảng bị rỗng khi tìm từ khóa phổ biến!");
        Assert.assertTrue(noiDungBang.contains(keyword), "Fail Logic: Bảng không chứa từ khóa vừa tìm!");
    }

    @Test(priority = 2)
    public void testF25_LuongDung_KetHopCacThanhBoLoc() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: BẤM CHỌN CÁC THANH LỌC ROLE VÀ LOẠI GV ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nguoiDungPage.locTheoRole("BCN khoa");
        Thread.sleep(1000); 
        nguoiDungPage.locTheoLoaiGV("Cơ hữu");
        Thread.sleep(1500);
        
        String noiDungBang = nguoiDungPage.layNoiDungBang();
        Assert.assertTrue(noiDungBang.contains("BCN khoa") || nguoiDungPage.kiemTraBangRong(), 
                "Fail Logic: Dữ liệu lọc ra bị sai Role hoặc bị lỗi hiển thị!");
    }

    // ==========================================
    // 2. LUỒNG SAI (Negative Path)
    // ==========================================
    @Test(priority = 3)
    public void testF25_LuongSai_TimKiemKhongTonTai() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: ĐIỀN TỪ KHÓA RÁC VÀO THANH TÌM KIẾM ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nguoiDungPage.timKiemUser("KHONG_CO_AI_TEN_NAY_123!@#");
        Thread.sleep(1500);
        
        Assert.assertTrue(nguoiDungPage.kiemTraBangRong(), "Fail Logic: Nhập rác mà hệ thống vẫn lòi ra dữ liệu!");
    }

    // ==========================================
    // 3. LUỒNG DATA (Data-Driven)
    // ==========================================
    @DataProvider(name = "duLieuTimKiem")
    public Object[][] provideSearchData() {
        return new Object[][] {
            {"Trần", true},       
            {"@gmail.com", true}, 
            {"0987654321", false},
            {"!@#_Khong_Ton_Tai_#@!", false}, // ĐÃ FIX DỮ LIỆU: Dùng chuỗi tuyệt đối không thể Trim() ra rỗng
            {"<script>", false}   
        };
    }

    @Test(priority = 4, dataProvider = "duLieuTimKiem")
    public void testF25_LuongData_QuetTuKhoaDaDang(String tuKhoa, boolean kyVongCoDuLieu) throws InterruptedException {
        System.out.println("--- LUỒNG DATA: TÌM KIẾM VỚI TỪ KHÓA = '" + tuKhoa + "' ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        nguoiDungPage.timKiemUser(tuKhoa);
        Thread.sleep(1500);
        
        boolean bangDangRong = nguoiDungPage.kiemTraBangRong();
        if (kyVongCoDuLieu) {
            Assert.assertFalse(bangDangRong, "Fail Data-driven: Tìm '" + tuKhoa + "' đáng lẽ phải có kết quả nhưng bảng lại trống!");
        } else {
            Assert.assertTrue(bangDangRong, "Fail Data-driven: Tìm rác '" + tuKhoa + "' đáng lẽ bảng phải rỗng nhưng lại văng ra dữ liệu!");
        }
    }

    // ==========================================
    // 4. LUỒNG GIAO DIỆN (UI/UX) - BỘ LỌC CỘT
    // ==========================================
    @Test(priority = 5)
    public void testF25_LuongUI_AnHienCotDuLieu() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: TƯƠNG TÁC DROPDOWN SELECT2 ĐỂ TẮT CỘT 'Email' ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        Assert.assertTrue(nguoiDungPage.kiemTraCotTrenBang("Email"), "Lỗi: Mặc định bảng không hiển thị cột Email!");
        
        nguoiDungPage.tatMoHienThiCot("Email");
        Thread.sleep(1500); 
        
        Assert.assertFalse(nguoiDungPage.kiemTraCotTrenBang("Email"), "Fail UI: Đã bấm tắt trong dropdown nhưng cột Email vẫn nằm trên bảng!");
    }

    // ==========================================
    // 5. LUỒNG GIAO DIỆN (UI/UX) - CỘT HIỂN THỊ (10, 25, 50)
    // ==========================================
    @Test(priority = 6)
    public void testF25_LuongUI_TestThanhHienThiSoDong() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: KIỂM TRA DROPDOWN HIỂN THỊ (10, 25, 50, TẤT CẢ) ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        // 1. Kiểm tra mốc 25 dòng
        nguoiDungPage.chonSoLuongHienThi("25");
        Thread.sleep(1500); 
        int soDong25 = nguoiDungPage.demSoDongTrenBang();
        Assert.assertTrue(soDong25 <= 25, "Fail UI: Chọn 25 dòng nhưng bảng đếm ra số lượng sai! (" + soDong25 + " dòng)");

        // 2. Kiểm tra mốc "tất cả"
        nguoiDungPage.chonSoLuongHienThi("tất cả");
        Thread.sleep(2500); 
        int soDongTatCa = nguoiDungPage.demSoDongTrenBang();
        Assert.assertTrue(soDongTatCa > 0, "Fail UI: Chọn hiển thị 'tất cả' làm sập trắng bảng dữ liệu!");
    }

    // ==========================================
    // 6. LUỒNG GIAO DIỆN (UI/UX) - PHÂN TRANG
    // ==========================================
    @Test(priority = 7)
    public void testF25_LuongUI_KiemTraChuyenTrang() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: KIỂM TRA BẤM SANG TRANG 2 ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        String thongTinTrang1 = nguoiDungPage.layThongTinPhanTrang();
        nguoiDungPage.bamChuyenSangTrang("2");
        Thread.sleep(1500); 
        String thongTinTrang2 = nguoiDungPage.layThongTinPhanTrang();
        
        Assert.assertNotEquals(thongTinTrang1, thongTinTrang2, "Fail UI/Phân trang: Bấm sang Trang 2 rồi nhưng dữ liệu không thèm lật!");
    }

    @Test(priority = 8)
    public void testF25_LuongUI_KiemTraNutNext() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: KIỂM TRA NÚT TRANG TIẾP THEO (>) ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        String thongTinHienTai = nguoiDungPage.layThongTinPhanTrang();
        nguoiDungPage.bamTrangTiepTheo();
        Thread.sleep(1500);
        String thongTinSauKhiNext = nguoiDungPage.layThongTinPhanTrang();
        
        Assert.assertNotEquals(thongTinHienTai, thongTinSauKhiNext, "Fail UI/Phân trang: Bấm nút Next (>) nhưng bảng dữ liệu bị đứng im!");
    }

    // ==========================================
    // 7. LUỒNG GIAO DIỆN (UI/UX) - RESPONSIVE & SCROLL (BỔ SUNG)
    // ==========================================
    @Test(priority = 9)
    public void testF25_LuongUI_KiemTraThuNhoPhongTo() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: KIỂM TRA THU NHỎ / PHÓNG TO KÍCH THƯỚC TRANG ---");
        
        // 1. Ép về kích thước Mobile (iPhone X: 375 x 812)
        org.openqa.selenium.Dimension kichThuocMobile = new org.openqa.selenium.Dimension(375, 812);
        driver.manage().window().setSize(kichThuocMobile);
        Thread.sleep(2000); 
        
        // Kiểm tra xem bảng dữ liệu có bị vỡ hoặc mất tích khi thu nhỏ không
        Assert.assertFalse(nguoiDungPage.kiemTraBangRong(), "Lỗi UI Responsive: Thu nhỏ màn hình làm mất DataTables!");
        
        // 2. Trả lại kích thước Desktop (Phóng to tối đa)
        driver.manage().window().maximize();
        Thread.sleep(2000);
        
        // Đảm bảo dữ liệu vẫn hiển thị sau khi bung màn hình
        String noiDungBang = nguoiDungPage.layNoiDungBang();
        Assert.assertTrue(noiDungBang.length() > 0, "Lỗi UI Responsive: Phóng to lại màn hình làm sập cấu trúc bảng!");
    }

    @Test(priority = 10)
    public void testF25_LuongUI_KiemTraCuonTrang() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: KIỂM TRA THAO TÁC LƯỚT LÊN VÀ LƯỚT XUỐNG ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        // 1. Lướt xuống dưới cùng
        nguoiDungPage.cuonXuongCuoiTrang();
        Thread.sleep(1500); 
        
        // Khi ở dưới cùng, kiểm tra xem có thấy dòng text Phân trang không
        String textPhanTrang = nguoiDungPage.layThongTinPhanTrang();
        Assert.assertTrue(textPhanTrang.contains("Hiển thị"), "Lỗi UI Scroll: Cuộn xuống cuối trang nhưng không thấy phân trang!");
        
        // 2. Lướt ngược lên trên cùng
        nguoiDungPage.cuonLenDauTrang();
        Thread.sleep(1500);
        
        // Đảm bảo nút Thêm người dùng (ở đầu trang) hiển thị lại bình thường
        boolean nutThemHienThi = driver.findElement(org.openqa.selenium.By.xpath("//button[contains(@class, 'btn-primary')]")).isDisplayed();
        Assert.assertTrue(nutThemHienThi, "Lỗi UI Scroll: Cuộn ngược lên đầu trang làm crash nút Thêm người dùng!");
    }
}