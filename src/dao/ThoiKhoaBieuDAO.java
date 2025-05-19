package dao;

import entities.ChiTietTKB;
import entities.HocKy;
import entities.ThoiKhoaBieu;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ThoiKhoaBieuDAO {

    public String getTenLopCN(String maGV) {
        String sql = "SELECT l.MaLop FROM LOP l " +
                "WHERE l.GVCN = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maGV);

            try (ResultSet rs = pstmt.executeQuery()) { // Đưa ResultSet vào try-with-resources
                if (rs.next()) {
                    return rs.getString("MaLop");
                } else {
                    // Không tìm thấy lớp nào, có thể ghi log hoặc không làm gì cả và trả về null
                    System.out.println("Không tìm thấy lớp chủ nhiệm cho GV: " + maGV);
                    return null;
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy lớp chủ nhiệm cho GV " + maGV + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<HocKy> getDanhSachHocKy() {
        List<HocKy> danhSach = new ArrayList<>();
        String sql = "SELECT MaHK, HocKy, NamHoc FROM HOCKY ORDER BY NamHoc DESC, HocKy DESC"; // Giả sử HocKy có thể là '1', '2', 'Hè'

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                danhSach.add(new HocKy(
                        rs.getString("MaHK"),
                        rs.getString("HocKy"),
                        rs.getString("NamHoc")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách Học Kỳ: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<ThoiKhoaBieu> getDanhSachThoiKhoaBieuTheoHocKy(String maHK) {
        List<ThoiKhoaBieu> danhSach = new ArrayList<>();
        // Bảng THOIKHOABIEU có MaTKB, NgayApDung, Buoi, MaHK
        String sql = "SELECT MaTKB, NgayApDung, Buoi FROM THOIKHOABIEU WHERE MaHK = ? ORDER BY NgayApDung DESC, MaTKB ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHK);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(new ThoiKhoaBieu(
                            rs.getString("MaTKB"),
                            rs.getDate("NgayApDung"), // java.sql.Date
                            rs.getString("Buoi"),
                            maHK // Truyền lại maHK để đối tượng ThoiKhoaBieu biết nó thuộc HK nào
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách TKB cho MaHK " + maHK + ": " + e.getMessage());
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<ChiTietTKB> getChiTietTKBForGiaoVien(String maTKB, String maGV) {
        List<ChiTietTKB> chiTietList = new ArrayList<>();
        // Bảng CHITIETTKB: MaTKB, Thu, Tiet, MaLop, MaGV, MaMH
        // Bảng MONHOC: MaMH, TenMH
        // Câu SQL này đã đúng để lấy TenMH từ bảng MONHOC
        String sql = "SELECT ct.Thu, ct.Tiet, mh.TenMH, ct.MaLop " +
                "FROM CHITIETTKB ct " +
                "JOIN MONHOC mh ON ct.MaMH = mh.MaMH " +
                "WHERE ct.MaTKB = ? AND ct.MaGV = ? " +
                "ORDER BY ct.Thu, ct.Tiet";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maTKB);
            pstmt.setString(2, maGV);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    chiTietList.add(new ChiTietTKB(
                            rs.getInt("Thu"),
                            rs.getInt("Tiet"),
                            rs.getString("TenMH"),
                            rs.getString("MaLop"),
                            null // Không cần tên giáo viên ở đây
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy chi tiết TKB cho GV " + maGV + ", TKB " + maTKB + ": " + e.getMessage());
            e.printStackTrace(); // Nên dùng logger thay vì printStackTrace trong ứng dụng thực tế
        }
        return chiTietList;
    }

    public List<ChiTietTKB> getChiTietTKBForLopCN(String maTKB, String maGV) {
        List<ChiTietTKB> chiTietList = new ArrayList<>();
        // Bảng CHITIETTKB: MaTKB, Thu, Tiet, MaLop, MaGV, MaMH
        // Bảng MONHOC: MaMH, TenMH
        // Câu SQL này đã đúng để lấy TenMH từ bảng MONHOC
        String sql = "SELECT ct.Thu, ct.Tiet, mh.TenMH, gv.HoGV, gv.TenGV " +
                "FROM CHITIETTKB ct " +
                "JOIN MONHOC mh ON ct.MaMH = mh.MaMH " +
                "JOIN GIAOVIEN gv ON ct.MaGV = gv.MaGV " +
                "JOIN LOP l ON ct.MaLop = l.MaLop " +
                "WHERE ct.MaTKB = ? AND l.GVCN = ? " +
                "ORDER BY ct.Thu, ct.Tiet";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maTKB);
            pstmt.setString(2, maGV);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    chiTietList.add(new ChiTietTKB(
                            rs.getInt("Thu"),
                            rs.getInt("Tiet"),
                            rs.getString("TenMH"),
                            null, // Không cần mã lớp ở đây
                            rs.getString("HoGV") + " " + rs.getString("TenGV")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy chi tiết TKB cho GV " + maGV + ", TKB " + maTKB + ": " + e.getMessage());
            e.printStackTrace(); // Nên dùng logger thay vì printStackTrace trong ứng dụng thực tế
        }
        return chiTietList;
    }
}
