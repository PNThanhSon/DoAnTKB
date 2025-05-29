package DAO;

import entities.MonHoc;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonHocDAO {

    public List<MonHoc> layDanhSachMonHoc() throws SQLException {
        List<MonHoc> danhSach = new ArrayList<>();
        String sql = "SELECT MaMH, TenMH, Khoi, MaTCM FROM MONHOC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MonHoc monHoc = new MonHoc(
                        rs.getString("MaMH"),
                        rs.getString("TenMH"),
                        rs.getString("Khoi"),
                        rs.getString("MaTCM")
                );
                danhSach.add(monHoc);
            }
        }
        return danhSach;
    }

    public void themMonHoc(MonHoc monHoc) throws SQLException {
        String sql = "INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, monHoc.getMaMH());
            stmt.setString(2, monHoc.getTenMH());
            stmt.setString(3, monHoc.getKhoi());
            stmt.setString(4, monHoc.getMaTCM());

            stmt.executeUpdate();
        }
    }

    public void xoaMonHoc(String maMH) throws SQLException {
        String sql = "DELETE FROM MONHOC WHERE MaMH = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maMH);
            stmt.executeUpdate();
        }
    }

    public void capNhatMonHoc(MonHoc monHoc) throws SQLException {
        String sql = "UPDATE MONHOC SET TenMH = ?, Khoi = ?, MaTCM = ? WHERE MaMH = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, monHoc.getTenMH());
            stmt.setString(2, monHoc.getKhoi());
            stmt.setString(3, monHoc.getMaTCM());
            stmt.setString(4, monHoc.getMaMH());

            stmt.executeUpdate();
        }
    }

    public List<MonHoc> timKiemMonHoc(String keyword) throws SQLException {
        List<MonHoc> danhSach = new ArrayList<>();
        String sql = "SELECT MaMH, TenMH, Khoi, MaTCM FROM MONHOC WHERE TenMH LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MonHoc monHoc = new MonHoc(
                            rs.getString("MaMH"),
                            rs.getString("TenMH"),
                            rs.getString("Khoi"),
                            rs.getString("MaTCM")
                    );
                    danhSach.add(monHoc);
                }
            }
        }
        return danhSach;
    }
}
