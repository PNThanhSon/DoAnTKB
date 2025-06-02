package dao;

import entities.*;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XepTKBDAO {

    // ... (Các phương thức khác giữ nguyên) ...

    /**
     * Lấy danh sách các môn học thuộc một Tổ Chuyên Môn (TCM).
     * @param maTCM Mã Tổ Chuyên Môn.
     * @return Danh sách các đối tượng MonHoc.
     */
    public List<MonHoc> getMonHocByTCM(String maTCM) {
        List<MonHoc> monHocList = new ArrayList<>();
        String sql = "SELECT MaMH, TenMH, Khoi, MaTCM FROM MONHOC WHERE MaTCM = ? ORDER BY TenMH, Khoi";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maTCM);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    monHocList.add(new MonHoc(
                            rs.getString("MaMH"),
                            rs.getString("TenMH"),
                            rs.getString("Khoi"),
                            rs.getString("MaTCM")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách Môn Học theo TCM " + maTCM + ": " + e.getMessage());
            e.printStackTrace();
        }
        return monHocList;
    }


    public List<ChiTietTKB> getChiTietTKBByMaTKB(String maTKB) {
        List<ChiTietTKB> chiTietList = new ArrayList<>();
        String sql = "SELECT ct.Thu, ct.Tiet, mh.TenMH, ct.MaLop, gv.MaGV, gv.HoGV || ' ' || gv.TenGV AS HoTenGV, ct.MaMH " +
                "FROM CHITIETTKB ct " +
                "JOIN MONHOC mh ON ct.MaMH = mh.MaMH " +
                "JOIN GIAOVIEN gv ON ct.MaGV = gv.MaGV " +
                "WHERE ct.MaTKB = ? " +
                "ORDER BY ct.Thu, ct.Tiet";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maTKB);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietTKB chiTiet = ChiTietTKB.taoChoXepLichTuDong(
                            rs.getInt("Thu"),
                            rs.getInt("Tiet"),
                            rs.getString("TenMH"),
                            rs.getString("MaLop"),
                            rs.getString("HoTenGV"),
                            rs.getString("MaGV"),
                            rs.getString("MaMH"), // Đảm bảo MaMH được truyền
                            0 // Flag tạm
                    );
                    chiTietList.add(chiTiet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy Chi Tiết TKB cho mã TKB " + maTKB + ": " + e.getMessage());
            e.printStackTrace();
        }
        return chiTietList;
    }

    public List<Lop> getDanhSachLop() {
        List<Lop> danhSach = new ArrayList<>();
        String sql = "SELECT MaLop, TenLop, Khoi, GVCN FROM LOP ORDER BY Khoi, MaLop"; // Sửa lại order by MaLop
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                danhSach.add(new Lop(
                        rs.getString("MaLop"),
                        rs.getString("TenLop"),
                        rs.getString("Khoi"),
                        rs.getString("GVCN")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách Lớp: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSach;
    }

    public Map<String, List<MonHocHoc>> getPhanCongMonHocChoTatCaLop(String maHK) {
        Map<String, List<MonHocHoc>> phanCong = new HashMap<>();
        String sql = "SELECT h.MaLop, h.MaMH, m.TenMH, m.MaTCM, SUM(h.PhanPhoiTiet) AS TongSoTiet " +
                "FROM HOC h " +
                "JOIN MONHOC m ON h.MaMH = m.MaMH " +
                "WHERE h.MaHK = ? " +
                "GROUP BY h.MaLop, h.MaMH, m.TenMH, m.MaTCM " +
                "ORDER BY h.MaLop, m.TenMH";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maHK);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String maLop = rs.getString("MaLop");
                    MonHocHoc monHoc = new MonHocHoc(
                            rs.getString("MaMH"),
                            rs.getString("TenMH"),
                            rs.getInt("TongSoTiet"),
                            rs.getString("MaTCM")
                    );
                    phanCong.computeIfAbsent(maLop, k -> new ArrayList<>()).add(monHoc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy phân công môn học cho các lớp: " + e.getMessage());
            e.printStackTrace();
        }
        return phanCong;
    }

    public int getTongPhanPhoiTietTheoKhoi(String khoi, String maHK) {
        int tongSoTietPhanPhoiTrongKhoi = 0;
        int soLuongLopTrongKhoi = 0;

        String sqlSumTiet = "SELECT SUM(h.PhanPhoiTiet) AS TongTiet " +
                "FROM HOC h " +
                "JOIN LOP l ON h.MaLop = l.MaLop " +
                "WHERE l.Khoi = ? AND h.MaHK = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmtSumTiet = conn.prepareStatement(sqlSumTiet)) {
            pstmtSumTiet.setString(1, khoi);
            pstmtSumTiet.setString(2, maHK);
            try (ResultSet rsSumTiet = pstmtSumTiet.executeQuery()) {
                if (rsSumTiet.next()) {
                    tongSoTietPhanPhoiTrongKhoi = rsSumTiet.getInt("TongTiet");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tính tổng phân phối tiết cho khối " + khoi + ", học kỳ " + maHK + ": " + e.getMessage());
            return 0;
        }

        if (tongSoTietPhanPhoiTrongKhoi == 0) {
            return 0;
        }

        String sqlCountLop = "SELECT COUNT(DISTINCT l.MaLop) AS SoLuongLop " +
                "FROM LOP l " +
                "JOIN HOC h ON l.MaLop = h.MaLop " +
                "WHERE l.Khoi = ? AND h.MaHK = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmtCountLop = conn.prepareStatement(sqlCountLop)) {
            pstmtCountLop.setString(1, khoi);
            pstmtCountLop.setString(2, maHK);
            try (ResultSet rsCountLop = pstmtCountLop.executeQuery()) {
                if (rsCountLop.next()) {
                    soLuongLopTrongKhoi = rsCountLop.getInt("SoLuongLop");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm số lượng lớp cho khối " + khoi + ": " + e.getMessage());
            return 0;
        }

        if (soLuongLopTrongKhoi > 0) {
            return tongSoTietPhanPhoiTrongKhoi / soLuongLopTrongKhoi;
        } else {
            return 0;
        }
    }

    public List<GiaoVien> getDanhSachGiaoVienDayDu() {
        List<GiaoVien> danhSach = new ArrayList<>();
        String sql = "SELECT g.MaGV, g.HoGV, g.TenGV, g.GioiTinh, g.ChuyenMon, g.MaTCM, " +
                "g.SoTietQuyDinh, g.SoTietThucHien, g.SoTietDuThieu, g.Email, g.SDT, g.MatKhau, g.GhiChu " + // Bỏ MaLopCN
                "FROM GIAOVIEN g " +
                "WHERE g.MaGV <> 'ADMIN' ORDER BY g.MaGV"; // Bỏ LEFT JOIN LOP

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                GiaoVien gv = new GiaoVien(
                        rs.getString("MaGV"),
                        rs.getString("HoGV"),
                        rs.getString("TenGV"),
                        rs.getString("GioiTinh"),
                        rs.getString("ChuyenMon"),
                        rs.getString("MaTCM"),
                        rs.getObject("SoTietQuyDinh") != null ? rs.getInt("SoTietQuyDinh") : null,
                        rs.getObject("SoTietThucHien") != null ? rs.getInt("SoTietThucHien") : null,
                        rs.getObject("SoTietDuThieu") != null ? rs.getInt("SoTietDuThieu") : null,
                        rs.getString("Email"),
                        rs.getString("SDT"),
                        rs.getString("MatKhau"),
                        rs.getString("GhiChu")
                );
                danhSach.add(gv);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách giáo viên đầy đủ: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSach;
    }

    public int getSoTietChucVuDaSuDung(String maGV, String maHK) {
        int soTietDaSuDung = 0;
        String sql = "SELECT SUM(SoTiet) AS TongSoTietChucVu FROM GIAOVIEN_CHUCVU WHERE MaGV = ? AND MaHK = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maGV);
            pstmt.setString(2, maHK);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    soTietDaSuDung = rs.getInt("TongSoTietChucVu");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy số tiết chức vụ đã sử dụng cho GV " + maGV + " trong HK " + maHK + ": " + e.getMessage());
        }
        return soTietDaSuDung;
    }

    public boolean luuChiTietTKB(ChiTietTKB chiTiet, String maTKB) {
        String sql = "INSERT INTO CHITIETTKB (MaTKB, Thu, Tiet, MaLop, MaGV, MaMH) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maTKB);
            pstmt.setInt(2, chiTiet.getThu());
            pstmt.setInt(3, chiTiet.getTiet());
            pstmt.setString(4, chiTiet.getMaLop());
            pstmt.setString(5, chiTiet.getMaGV());
            if (chiTiet.getMaMH() == null || chiTiet.getMaMH().trim().isEmpty()) {
                System.err.println("Lỗi nghiêm trọng: MaMH là null hoặc rỗng khi cố gắng lưu ChiTietTKB. MaLop: " + chiTiet.getMaLop() + ", TenMH: " + chiTiet.getTenMonHoc());
                return false;
            }
            pstmt.setString(6, chiTiet.getMaMH());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lưu Chi Tiết TKB (MaTKB: " + maTKB + ", MaLop: " + chiTiet.getMaLop() + ", MaMH: " + chiTiet.getMaMH() + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String taoThoiKhoaBieuMoi(String maHK, String buoiHoc, String nguoiTaoMaGV) {
        String maTKB = "TKB" + System.currentTimeMillis() % 100000;
        String sql = "INSERT INTO THOIKHOABIEU (MaTKB, NgayLap, NgayApDung, Buoi, NguoiTao, MaHK) " +
                "VALUES (?, SYSDATE, SYSDATE, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maTKB);
            pstmt.setString(2, buoiHoc);
            pstmt.setString(3, nguoiTaoMaGV);
            pstmt.setString(4, maHK);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return maTKB;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tạo Thời Khóa Biểu mới: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
