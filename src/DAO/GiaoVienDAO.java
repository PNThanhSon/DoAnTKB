package DAO;

import entities.GiaoVien;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// MÃ NÀY GỒM CÁC PHƯƠNG THỨC ĐỂ THAO TÁC LÊN CSDL

public class GiaoVienDAO {
    public void capNhatThongTin(GiaoVien gv) throws SQLException {
        String sql = "UPDATE GIAOVIEN SET HoGV = ?, TenGV = ?, GioiTinh = ?, ChuyenMon = ?, MaTCM = ?, " +
                "SoTietQuyDinh = ?, SoTietThucHien = ?, SoTietDuThieu = ?, Email = ?, SDT = ?, MatKhau = ?, GhiChu = ? " +
                "WHERE MaGV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gv.getHoGV());
            stmt.setString(2, gv.getTenGV());
            stmt.setString(3, gv.getGioiTinh());
            stmt.setString(4, gv.getChuyenMon());
            stmt.setString(5, gv.getMaTCM());
            stmt.setInt(6, gv.getSoTietQuyDinh());
            stmt.setInt(7, gv.getSoTietThucHien());
            stmt.setInt(8, gv.getSoTietDuThieu());
            stmt.setString(9, gv.getEmail());
            stmt.setString(10, gv.getSdt());
            stmt.setString(11, gv.getMatKhau());
            stmt.setString(12, gv.getGhiChu());
            stmt.setString(13, gv.getMaGV());

            stmt.executeUpdate();
        }
    }


    public void themGiaoVien(GiaoVien gv) throws SQLException {
        String sql = "INSERT INTO GIAOVIEN (HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, " +
                "SoTietQuyDinh, SoTietThucHien, SoTietDuThieu, Email, SDT, MatKhau, GhiChu) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gv.getHoGV());
            stmt.setString(2, gv.getTenGV());
            stmt.setString(3, gv.getGioiTinh());
            stmt.setString(4, gv.getChuyenMon());
            stmt.setString(5, gv.getMaTCM());
            stmt.setInt(6, gv.getSoTietQuyDinh());
            stmt.setInt(7, gv.getSoTietThucHien());
            stmt.setInt(8, gv.getSoTietDuThieu());
            stmt.setString(9, gv.getEmail());
            stmt.setString(10, gv.getSdt());
            stmt.setString(11, gv.getMatKhau());
            stmt.setString(12, gv.getGhiChu());


            stmt.executeUpdate();
        }
    }


    public void xoaGiaoVien(GiaoVien gv) throws SQLException {
        String sql = "DELETE FROM GIAOVIEN WHERE MaGV = ?";
        System.out.println(gv.getMaGV());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gv.getMaGV());
            stmt.executeUpdate();
        }
    }


    public List<GiaoVien> TracuuGiaoVien(String maGV) throws SQLException {
        List<GiaoVien> danhSachGiaoVien = new ArrayList<>();

        String sql = "SELECT MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, " +
                "SoTietThucHien, SoTietDuThieu, Email, SDT, MatKhau, GhiChu " +
                "FROM GIAOVIEN WHERE MaGV LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + maGV + "%");

            if (conn == null) {
                System.err.println("Không thể kết nối đến cơ sở dữ liệu.");
                return null;
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                danhSachGiaoVien.clear(); // Xoá danh sách cũ nếu có

                while (rs.next()) {
                    Integer soTietQD = rs.getObject("SoTietQuyDinh") != null ? rs.getInt("SoTietQuyDinh") : null;
                    Integer soTietTH = rs.getObject("SoTietThucHien") != null ? rs.getInt("SoTietThucHien") : null;
                    Integer soTietDT = rs.getObject("SoTietDuThieu") != null ? rs.getInt("SoTietDuThieu") : null;

                    GiaoVien giaoVien = new GiaoVien(
                            rs.getString("MaGV"),
                            rs.getString("HoGV"),
                            rs.getString("TenGV"),
                            rs.getString("GioiTinh"),
                            rs.getString("ChuyenMon"),
                            rs.getString("MaTCM"),
                            soTietQD,
                            soTietTH,
                            soTietDT,
                            rs.getString("Email"),
                            rs.getString("SDT"),
                            rs.getString("MatKhau"),
                            rs.getString("GhiChu")
                    );

                    danhSachGiaoVien.add(giaoVien);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachGiaoVien;
    }






}
