package dao;

import entities.YKien;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// MÃ NÀY GỒM CÁC PHƯƠNG THỨC ĐỂ THAO TÁC LÊN CSDL

public class YKienDAO {

    // Thêm ý kiến mới
    public void themYKien(YKien yk) throws SQLException {
        String sql = "INSERT INTO YKien (NoiDung, NgayGui, AnDanh, MaGV) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // truyen
            stmt.setString(1, yk.getNoiDung());
            stmt.setDate(2, Date.valueOf(yk.getNgayGui())); // LocalDate -> java.sql.Date
            stmt.setBoolean(3, yk.getAnDanh());
            stmt.setString(4, yk.getMaGV());

            stmt.executeUpdate();
        }
    }

    // Truy xuất tất cả ý kiến
    public List<YKien> TracuuYKien(String MaGV) throws SQLException {
        List<YKien> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM YKien WHERE MaGV = ? ORDER BY NgayGui DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, MaGV);  // Gán tham số MaGV

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String Trangthai = "";
                    if (rs.getDate("NgayGui").toLocalDate().equals(LocalDate.now())) {
                        Trangthai = "Mới";
                    }

                    YKien yk = new YKien(
                            rs.getString("MaYK"),
                            rs.getString("NoiDung"),
                            rs.getDate("NgayGui").toLocalDate(),
                            rs.getBoolean("AnDanh"),
                            rs.getString("MaGV"),
                            Trangthai
                    );
                    danhSach.add(yk);
                }
            }
        }
        return danhSach;
    }

    public List<YKien> TimKiemYKien(String keywork) throws SQLException {
        List<YKien> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM YKien WHERE MaGV LIKE ? ORDER BY NgayGui DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keywork + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // set trang thai, cung ngay thì MỚI
                    String Trangthai = "";
                    if (rs.getDate("NgayGui").toLocalDate().equals(LocalDate.now())) {
                        Trangthai = "Mới";
                    }
                    // Bỏ các mẫu ẩn danh nếu không phải của người đăng  nhập
//                    String maGV = rs.getString("MaGV");
//                    if (rs.getBoolean("AnDanh") && !(maGV.equals(giaoviencurrent.getMaGV()))) {
//                        continue;
//                    }
                    // truyen
                    YKien yk = new YKien(
                            rs.getString("MaYK"),
                            rs.getString("NoiDung"),
                            rs.getDate("NgayGui").toLocalDate(),
                            rs.getBoolean("AnDanh"),
                            rs.getString("MaGV"),
                            Trangthai
                    );
                    danhSach.add(yk);
                }
            }
        }
        return danhSach;
    }



    // Xóa ý kiến
    public void xoaYKien(YKien YK) throws SQLException {
        String sql = "DELETE FROM YKien WHERE MaYK = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, YK.getMaYK());
            stmt.executeUpdate();
        }
    }

    public void suaYKien(YKien yk) throws SQLException {
        String sql = "UPDATE YKIEN SET NoiDung = ?, NgayGui = ?, AnDanh = ?, MaGV = ? WHERE MaYK = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setString(1, yk.getNoiDung());
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setBoolean(3, yk.getAnDanh());
            stmt.setString(4, yk.getMaGV());
            stmt.setString(5, yk.getMaYK());

            stmt.executeUpdate();
        }
    }

}
