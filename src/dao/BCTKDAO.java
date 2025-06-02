package dao; // Hoặc package DAO bạn đang dùng

import entities.GiaoVien;
import entities.HocKy;
import entities.ThoiKhoaBieu;
import util.DatabaseConnection; // Sử dụng class DatabaseConnection bạn cung cấp

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class BCTKDAO {

    /**
     * Lấy danh sách tất cả Học Kỳ từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng HocKy.
     */
    public List<HocKy> getAllHocKy() {
        List<HocKy> hocKyList = new ArrayList<>();
        String sql = "SELECT MaHK, HocKy, NamHoc FROM HOCKY ORDER BY NamHoc DESC, SUBSTR(HocKy, INSTR(HocKy, ' ') + 1) DESC, MaHK DESC"; // Sắp xếp để hiển thị tốt hơn

        // Sử dụng try-with-resources để đảm bảo kết nối được đóng đúng cách
        try (Connection conn = DatabaseConnection.getConnection(); // Sử dụng class của bạn
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String maHK = rs.getString("MaHK");
                String tenHocKy = rs.getString("HocKy");
                String namHoc = rs.getString("NamHoc");
                hocKyList.add(new HocKy(maHK, tenHocKy, namHoc));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách Học Kỳ: " + e.getMessage());
            e.printStackTrace();
        }
        return hocKyList;
    }

    /**
     * Lấy danh sách Thời Khóa Biểu cho một MaHK cụ thể.
     * @param maHK Mã Học Kỳ để lọc Thời Khóa Biểu.
     * @return Danh sách các đối tượng ThoiKhoaBieu.
     */
    public List<ThoiKhoaBieu> getThoiKhoaBieuByMaHK(String maHK) {
        List<ThoiKhoaBieu> tkbList = new ArrayList<>();
        String sql = "SELECT MaTKB, NgayApDung, Buoi, MaHK FROM THOIKHOABIEU WHERE MaHK = ? ORDER BY NgayApDung DESC, Buoi";

        try (Connection conn = DatabaseConnection.getConnection(); // Sử dụng class của bạn
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHK);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String maTKB = rs.getString("MaTKB");
                    Date ngayApDung = rs.getDate("NgayApDung"); // java.sql.Date
                    String buoi = rs.getString("Buoi");
                    String maHKResult = rs.getString("MaHK");
                    tkbList.add(new ThoiKhoaBieu(maTKB, (java.sql.Date) ngayApDung, buoi, maHKResult));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách Thời Khóa Biểu cho MaHK " + maHK + ": " + e.getMessage());
            e.printStackTrace();
        }
        return tkbList;
    }

    /**
     * Tính toán thống kê Số Tiết Dư Thiếu cho tất cả giáo viên dựa trên Học Kỳ và Thời Khóa Biểu cụ thể.
     * SoTietThucHien = (số tiết trong CHITIETTKB của MaTKB) + (SoTiet trong GIAOVIEN_CHUCVU của MaHK)
     * SoTietDuThieu = SoTietThucHien - SoTietQuyDinh (từ bảng GIAOVIEN)
     *
     * @param selectedMaHK Mã Học Kỳ đã chọn.
     * @param selectedMaTKB Mã Thời Khóa Biểu đã chọn.
     * @return Một Map với key là các danh mục Số Tiết Dư Thiếu và value là số lượng giáo viên.
     */
    public Map<String, Integer> getSoTietDuThieuStats(String selectedMaHK, String selectedMaTKB) {
        Map<String, Integer> stats = new HashMap<>();
        // Khởi tạo các danh mục theo yêu cầu mới
        stats.put("ThieuNhieu", 0); // <= -3 tiết
        stats.put("Thieu", 0);      // == -1 và -2 tiết
        stats.put("Du", 0);         // == 0 tiết (Đủ)
        stats.put("DuVua", 0);      // == 1 tiết và 2 tiết (Đổi tên key để rõ nghĩa hơn)
        stats.put("DuNhieu", 0);    // >= 3 tiết

        // Câu SQL để lấy MaGV và SoTietQuyDinh từ bảng GIAOVIEN
        String sqlGiaoVien = "SELECT MaGV, SoTietQuyDinh FROM GIAOVIEN WHERE MaGV <> 'ADMIN'";

        try (Connection conn = DatabaseConnection.getConnection(); // Sử dụng class của bạn
             PreparedStatement pstmtGV = conn.prepareStatement(sqlGiaoVien);
             ResultSet rsGV = pstmtGV.executeQuery()) {

            while (rsGV.next()) {
                String maGV = rsGV.getString("MaGV");
                int soTietQuyDinh = rsGV.getInt("SoTietQuyDinh");
                if (rsGV.wasNull()) { // Kiểm tra nếu SoTietQuyDinh là NULL
                    soTietQuyDinh = 0; // Mặc định là 0 nếu NULL
                }

                // 1. Đếm số tiết giáo viên đó dạy trong Thời Khóa Biểu cụ thể (CHITIETTKB)
                int soTietDayTKB = 0;
                String sqlChiTietTKB = "SELECT COUNT(*) AS TongSoTietDay FROM CHITIETTKB WHERE MaGV = ? AND MaTKB = ?";
                try (PreparedStatement pstmtChiTiet = conn.prepareStatement(sqlChiTietTKB)) {
                    pstmtChiTiet.setString(1, maGV);
                    pstmtChiTiet.setString(2, selectedMaTKB);
                    try (ResultSet rsChiTiet = pstmtChiTiet.executeQuery()) {
                        if (rsChiTiet.next()) {
                            soTietDayTKB = rsChiTiet.getInt("TongSoTietDay");
                        }
                    }
                }

                // 2. Tính tổng số tiết của giáo viên đó trong bảng GIAOVIEN_CHUCVU theo Học Kỳ đã chọn
                int soTietChucVu = 0;
                String sqlGVChucVu = "SELECT SUM(SoTiet) AS TongSoTietChucVu FROM GIAOVIEN_CHUCVU WHERE MaGV = ? AND MaHK = ?";
                try (PreparedStatement pstmtChucVu = conn.prepareStatement(sqlGVChucVu)) {
                    pstmtChucVu.setString(1, maGV);
                    pstmtChucVu.setString(2, selectedMaHK);
                    try (ResultSet rsChucVu = pstmtChucVu.executeQuery()) {
                        if (rsChucVu.next()) {
                            // SUM có thể trả về NULL nếu không có dòng nào, getInt sẽ coi NULL là 0
                            soTietChucVu = rsChucVu.getInt("TongSoTietChucVu");
                        }
                    }
                }

                int soTietThucHien = soTietDayTKB + soTietChucVu;
                int soTietDuThieu = soTietThucHien - soTietQuyDinh;

                // Phân loại SoTietDuThieu theo yêu cầu mới
                if (soTietDuThieu <= -3) {
                    stats.put("ThieuNhieu", stats.get("ThieuNhieu") + 1);
                } else if (soTietDuThieu == -2 || soTietDuThieu == -1) {
                    stats.put("Thieu", stats.get("Thieu") + 1);
                } else if (soTietDuThieu == 0) {
                    stats.put("Du", stats.get("Du") + 1);
                } else if (soTietDuThieu == 1 || soTietDuThieu == 2) {
                    stats.put("DuVua", stats.get("DuVua") + 1);
                } else { // soTietDuThieu >= 3
                    stats.put("DuNhieu", stats.get("DuNhieu") + 1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tính toán thống kê Số Tiết Dư Thiếu: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }

    // Bên trong class BCTKDAO

    /**
     * Lấy danh sách giáo viên cùng với thông tin chi tiết về số tiết quy định,
     * thực hiện và dư thiếu cho một Học Kỳ và Thời Khóa Biểu cụ thể.
     *
     * @param selectedMaHK Mã Học Kỳ.
     * @param selectedMaTKB Mã Thời Khóa Biểu.
     * @return Danh sách các đối tượng GiaoVien đã được cập nhật thông tin số tiết.
     */
    public List<GiaoVien> getDanhSachGiaoVienVoiSoTietChiTiet(String selectedMaHK, String selectedMaTKB) {
        List<GiaoVien> danhSachGiaoVienDayDu = new ArrayList<>();
        // Câu SQL để lấy thông tin cơ bản của giáo viên, loại trừ 'ADMIN'
        String sqlGiaoVienInfo = "SELECT MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, Email, SDT, GhiChu " +
                "FROM GIAOVIEN WHERE MaGV <> 'ADMIN' ORDER BY MaGV"; // Lấy các trường cần thiết

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmtGVInfo = conn.prepareStatement(sqlGiaoVienInfo);
             ResultSet rsGVInfo = pstmtGVInfo.executeQuery()) {

            while (rsGVInfo.next()) {
                String maGV = rsGVInfo.getString("MaGV");
                String hoGV = rsGVInfo.getString("HoGV");
                String tenGV = rsGVInfo.getString("TenGV");
                int soTietQuyDinh = rsGVInfo.getInt("SoTietQuyDinh");
                if (rsGVInfo.wasNull()) {
                    soTietQuyDinh = 0;
                }

                // 1. Đếm số tiết giáo viên đó dạy trong Thời Khóa Biểu cụ thể (CHITIETTKB)
                int soTietDayTKB = 0;
                String sqlChiTietTKB = "SELECT COUNT(*) AS TongSoTietDay FROM CHITIETTKB WHERE MaGV = ? AND MaTKB = ?";
                try (PreparedStatement pstmtChiTiet = conn.prepareStatement(sqlChiTietTKB)) {
                    pstmtChiTiet.setString(1, maGV);
                    pstmtChiTiet.setString(2, selectedMaTKB);
                    try (ResultSet rsChiTiet = pstmtChiTiet.executeQuery()) {
                        if (rsChiTiet.next()) {
                            soTietDayTKB = rsChiTiet.getInt("TongSoTietDay");
                        }
                    }
                }

                // 2. Tính tổng số tiết của giáo viên đó trong bảng GIAOVIEN_CHUCVU theo Học Kỳ đã chọn
                int soTietChucVu = 0;
                String sqlGVChucVu = "SELECT SUM(SoTiet) AS TongSoTietChucVu FROM GIAOVIEN_CHUCVU WHERE MaGV = ? AND MaHK = ?";
                try (PreparedStatement pstmtChucVu = conn.prepareStatement(sqlGVChucVu)) {
                    pstmtChucVu.setString(1, maGV);
                    pstmtChucVu.setString(2, selectedMaHK);
                    try (ResultSet rsChucVu = pstmtChucVu.executeQuery()) {
                        if (rsChucVu.next()) {
                            soTietChucVu = rsChucVu.getInt("TongSoTietChucVu");
                        }
                    }
                }

                int soTietThucHien = soTietDayTKB + soTietChucVu;
                int soTietDuThieu = soTietThucHien - soTietQuyDinh;

                // Tạo đối tượng GiaoVien và set các giá trị đã tính toán
                // Sử dụng constructor phù hợp hoặc tạo một constructor mới nếu cần
                // Ở đây tôi dùng constructor không có MatKhau và VaiTro để đơn giản cho báo cáo
                GiaoVien gv = new GiaoVien(
                        maGV,
                        hoGV,
                        tenGV,
                        rsGVInfo.getString("GioiTinh"),
                        rsGVInfo.getString("ChuyenMon"),
                        rsGVInfo.getString("MaTCM"),
                        soTietQuyDinh,       // Set giá trị đã lấy
                        soTietThucHien,      // Set giá trị đã tính
                        soTietDuThieu,       // Set giá trị đã tính
                        rsGVInfo.getString("Email"),
                        rsGVInfo.getString("SDT"),
                        null, // MatKhau không cần cho báo cáo này
                        rsGVInfo.getString("GhiChu")
                        // Nếu entity GiaoVien của bạn không có constructor này,
                        // bạn có thể tạo mới hoặc dùng constructor mặc định và các setter
                );
                // Hoặc nếu bạn đã có đối tượng GiaoVien từ một nguồn khác và muốn cập nhật:
                // gv.setSoTietQuyDinh(soTietQuyDinh); // Đảm bảo GiaoVien.java có setter này
                // gv.setSoTietThucHien(soTietThucHien); // Đảm bảo GiaoVien.java có setter này
                // gv.setSoTietDuThieu(soTietDuThieu); // Đảm bảo GiaoVien.java có setter này

                danhSachGiaoVienDayDu.add(gv);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách giáo viên với số tiết chi tiết: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachGiaoVienDayDu;
    }
}
