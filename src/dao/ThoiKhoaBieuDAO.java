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
import java.util.Date;
import java.util.List;

public class ThoiKhoaBieuDAO {

    /**
     * Lấy danh sách tất cả các Học Kỳ.
     * Sắp xếp theo năm học mới nhất, rồi đến học kỳ mới nhất.
     * @return Danh sách các đối tượng HocKy.
     */
    public List<HocKy> getDanhSachHocKy() {
        List<HocKy> danhSach = new ArrayList<>();
        String sql = "SELECT MaHK, HocKy, NamHoc FROM HOCKY ORDER BY NamHoc DESC, HocKy DESC"; // Giả sử HocKy có thể là '1', '2', 'Hè'

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn == null) {
                System.err.println("TKB DAO: Không thể kết nối CSDL để lấy danh sách Học Kỳ.");
                return danhSach;
            }

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

    /**
     * Lấy danh sách các Thời Khóa Biểu thuộc một Học Kỳ cụ thể.
     * Sắp xếp theo ngày áp dụng mới nhất.
     * @param maHK Mã học kỳ.
     * @return Danh sách các đối tượng ThoiKhoaBieu.
     */
    public List<ThoiKhoaBieu> getDanhSachThoiKhoaBieuTheoHocKy(String maHK) {
        List<ThoiKhoaBieu> danhSach = new ArrayList<>();
        // Bảng THOIKHOABIEU có MaTKB, NgayApDung, Buoi, MaHK
        String sql = "SELECT MaTKB, NgayApDung, Buoi FROM THOIKHOABIEU WHERE MaHK = ? ORDER BY NgayApDung DESC, MaTKB ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("TKB DAO: Không thể kết nối CSDL để lấy danh sách TKB theo học kỳ.");
                return danhSach;
            }
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

    /**
     * Lấy chi tiết thời khóa biểu cho một giáo viên cụ thể và một MaTKB cụ thể.
     * @param maTKB Mã thời khóa biểu đã chọn.
     * @param maGV Mã giáo viên.
     * @return Danh sách các đối tượng ChiTietDayHoc.
     */
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

            if (conn == null) {
                System.err.println("TKB DAO: Không thể kết nối CSDL để lấy chi tiết TKB.");
                return chiTietList; // Trả về danh sách rỗng nếu không có kết nối
            }

            pstmt.setString(1, maTKB);
            pstmt.setString(2, maGV);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    chiTietList.add(new ChiTietTKB(
                            rs.getInt("Thu"),
                            rs.getInt("Tiet"),
                            rs.getString("TenMH"),
                            rs.getString("MaLop")
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
