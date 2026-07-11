package com.bcntest.features.quanlynguoidung;

import com.bcntest.core.BaseTest;
import com.bcnpages.QuanLyNguoiDungPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class XemNguoiDungTest extends BaseTest {

    QuanLyNguoiDungPage nguoiDungPage;

    @BeforeClass
    public void initPage() throws InterruptedException {
        nguoiDungPage = new QuanLyNguoiDungPage(driver);
        driver.get(BASE_URL + "User"); 
        Thread.sleep(3000); 
    }

    // ==========================================
    // 1. LUỒNG ĐÚNG (Happy Path): TẢI TRANG & XEM DỮ LIỆU CƠ BẢN
    // ==========================================
    @Test(priority = 1)
    public void testF21_LuongDung_KiemTraTheThongKe() throws InterruptedException {
        System.out.println("--- LUỒNG ĐÚNG: KIỂM TRA TẢI 4 THẺ THỐNG KÊ (DASHBOARD) ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        String theKhoa = nguoiDungPage.laySoLieuThongKe("BCN khoa");
        String theBoMon = nguoiDungPage.laySoLieuThongKe("Bộ môn");
        String theGiangVien = nguoiDungPage.laySoLieuThongKe("Giảng viên");
        String theChuaPhanQuyen = nguoiDungPage.laySoLieuThongKe("Chưa phân quyền");
        
        Assert.assertTrue(theKhoa.length() > 5, "Lỗi UI: Thẻ BCN Khoa không hiển thị số liệu!");
        Assert.assertTrue(theBoMon.length() > 5, "Lỗi UI: Thẻ Bộ Môn không hiển thị số liệu!");
        Assert.assertTrue(theGiangVien.length() > 5, "Lỗi UI: Thẻ Giảng Viên không hiển thị số liệu!");
        Assert.assertTrue(theChuaPhanQuyen.length() > 5, "Lỗi UI: Thẻ Chưa phân quyền không hiển thị số liệu!");
    }

    @Test(priority = 2)
    public void testF21_LuongDung_KiemTraBangMacDinh() {
        System.out.println("--- LUỒNG ĐÚNG: KIỂM TRA TRẠNG THÁI LƯỚI DỮ LIỆU MẶC ĐỊNH ---");
        
        String hienThiMacDinh = nguoiDungPage.layGiaTriDropdownHienThiMacDinh();
        Assert.assertEquals(hienThiMacDinh, "10", "Lỗi UX: Vừa vào trang mặc định phải là 10 dòng!");
        
        Assert.assertFalse(nguoiDungPage.kiemTraBangRong(), "Lỗi Data: Vừa load trang mà bảng đã trống rỗng!");
        
        int soDong = nguoiDungPage.demSoDongTrenBang();
        Assert.assertTrue(soDong > 0 && soDong <= 10, "Lỗi UI: Đếm số dòng thực tế không khớp với cấu hình 10 dòng!");
    }

    // ==========================================
    // 2. LUỒNG SAI (Negative Path): KIỂM TRA TRẠNG THÁI LỖI ẨN
    // ==========================================
    @Test(priority = 3)
    public void testF21_LuongSai_KhongHienThiLoiTrenTrang() throws InterruptedException {
        System.out.println("--- LUỒNG SAI: KIỂM TRA KHÔNG CÓ POPUP HAY ERROR NÀO VĂNG RA KHI LOAD TRANG ---");
        driver.navigate().refresh();
        Thread.sleep(3000); 
        
        // Vừa vào trang thì thông báo (Toast/Error) phải trống không
        String thongBao = nguoiDungPage.layThongBao();
        Assert.assertTrue(thongBao.trim().isEmpty(), "Lỗi Nghiệp vụ: Vừa vào trang View mà hệ thống đã văng lỗi: " + thongBao);
    }

    // ==========================================
    // 3. LUỒNG DATA (Data-Driven): QUÉT CSS THEO DỮ LIỆU (ROLE -> MÀU SẮC)
    // ==========================================
    @DataProvider(name = "duLieuMauSacRole")
    public Object[][] provideRoleColors() {
        return new Object[][] {
            {"BCN khoa", "text-primary"},      
            {"Bộ môn", "text-danger"},         
            {"Giảng viên", "text-success"},    
            {"Chưa phân quyền", "text-warning"}
        };
    }

    @Test(priority = 4, dataProvider = "duLieuMauSacRole")
    public void testF21_LuongData_KiemTraRenderMauSacUI(String tenRole, String classCSSMongMuon) {
        System.out.println("--- LUỒNG DATA: KIỂM TRA MÀU SẮC RENDER CỦA ROLE '" + tenRole + "' ---");
        
        String noiDungBang = nguoiDungPage.layNoiDungBang();
        if (noiDungBang.contains(tenRole)) {
            boolean dungMau = nguoiDungPage.kiemTraMauIconRole(tenRole, classCSSMongMuon);
            Assert.assertTrue(dungMau, "Lỗi Data/UI: Render sai màu CSS cho Role " + tenRole + ". Đáng lẽ phải chứa " + classCSSMongMuon);
        } else {
            System.out.println("  -> Bỏ qua kiểm tra màu do Role '" + tenRole + "' không xuất hiện ở trang hiện tại.");
        }
    }

    // ==========================================
    // 4. LUỒNG GIAO DIỆN (UI/UX): CỘT, SORTING, RESPONSIVE, SCROLL
    // ==========================================
    @Test(priority = 5)
    public void testF21_LuongUI_KiemTraCauTrucCot() {
        System.out.println("--- LUỒNG GIAO DIỆN: KIỂM TRA CÁC CỘT HIỂN THỊ MẶC ĐỊNH ---");
        
        List<String> danhSachCot = nguoiDungPage.layDanhSachTenCotHienThi();
        String chuoiCacCot = danhSachCot.toString().toUpperCase();
        
        Assert.assertTrue(chuoiCacCot.contains("TÊN GIẢNG VIÊN"), "Lỗi UI: Thiếu cột Tên Giảng Viên trên bảng!");
        Assert.assertTrue(chuoiCacCot.contains("EMAIL"), "Lỗi UI: Thiếu cột Email trên bảng!");
        Assert.assertTrue(chuoiCacCot.contains("LOẠI"), "Lỗi UI: Thiếu cột Loại trên bảng!");
        Assert.assertTrue(chuoiCacCot.contains("ROLE"), "Lỗi UI: Thiếu cột Role trên bảng!");
    }

    // ĐÃ BỔ SUNG THEO YÊU CẦU: Test chức năng Click vào tiêu đề cột để Sắp Xếp
    @Test(priority = 6)
    public void testF21_LuongUI_KiemTraTinhNangSapXepCot() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: KIỂM TRA SẮP XẾP (SORTING) TRÊN TIÊU ĐỀ CỘT ---");
        driver.navigate().refresh();
        Thread.sleep(2000);
        
        // Bấm vào cột Tên giảng viên 1 lần để kích hoạt Sort
        nguoiDungPage.bamTieuDeCotDeSapXep("Tên giảng viên");
        Thread.sleep(1500);
        
        String trangThaiSort = nguoiDungPage.layTrangThaiSapXepCuaCot("Tên giảng viên");
        Assert.assertNotNull(trangThaiSort, "Lỗi UI/UX: Cột Tên giảng viên không có thuộc tính aria-sort để xác định trạng thái sắp xếp!");
        Assert.assertTrue(trangThaiSort.equals("ascending") || trangThaiSort.equals("descending"), "Lỗi UI/UX: Tính năng Sort của DataTables bị liệt!");
    }

    @Test(priority = 7)
    public void testF21_LuongUI_KiemTraThuNhoPhongTo() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: KIỂM TRA THU NHỎ / PHÓNG TO KÍCH THƯỚC TRANG WEB ---");
        
        org.openqa.selenium.Dimension kichThuocMobile = new org.openqa.selenium.Dimension(375, 812);
        driver.manage().window().setSize(kichThuocMobile);
        Thread.sleep(2000); 
        
        Assert.assertFalse(nguoiDungPage.kiemTraBangRong(), "Lỗi UI Responsive: Thu nhỏ màn hình làm mất bảng dữ liệu!");
        
        driver.manage().window().maximize();
        Thread.sleep(2000);
        
        List<String> danhSachCot = nguoiDungPage.layDanhSachTenCotHienThi();
        Assert.assertTrue(danhSachCot.size() > 0, "Lỗi UI Responsive: Phóng to lại màn hình làm mất cấu trúc cột!");
    }

    @Test(priority = 8)
    public void testF21_LuongUI_KiemTraLuoTrang() throws InterruptedException {
        System.out.println("--- LUỒNG GIAO DIỆN: KIỂM TRA THAO TÁC LƯỚT LÊN VÀ LƯỚT XUỐNG ---");
        
        String theKhoaDauTrang = nguoiDungPage.laySoLieuThongKe("BCN khoa");
        Assert.assertTrue(theKhoaDauTrang.length() > 0, "Lỗi UI Scroll: Mất thẻ thống kê ở đầu trang!");
        
        nguoiDungPage.cuonXuongCuoiTrang();
        Thread.sleep(1500); 
        
        String textPhanTrang = nguoiDungPage.layThongTinPhanTrang();
        Assert.assertTrue(textPhanTrang.contains("Hiển thị"), "Lỗi UI Scroll: Cuộn xuống cuối trang nhưng không thấy phân trang!");
        
        nguoiDungPage.cuonLenDauTrang();
        Thread.sleep(1500);
        
        String theBoMonDauTrang = nguoiDungPage.laySoLieuThongKe("Bộ môn");
        Assert.assertTrue(theBoMonDauTrang.length() > 0, "Lỗi UI Scroll: Cuộn ngược lên đầu trang làm crash UI phía trên!");
    }
}