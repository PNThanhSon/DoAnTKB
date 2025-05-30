package DAO;

import entities.MonHoc;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MonHocDAO {

    // Add a new subject
    public void themMonHoc(MonHoc mh) throws SQLException {
        String sql = "INSERT INTO MONHOC (MaMH, TenMH, Khoi, MaTCM) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mh.getMaMH());
            stmt.setString(2, mh.getTenMH());
            stmt.setString(3, mh.getKhoi());
            stmt.setString(4, mh.getMaTCM());
            stmt.executeUpdate();
        }
    }

    // Update an existing subject
    public void capNhatMonHoc(MonHoc mh) throws SQLException {
        String sql = "UPDATE MONHOC SET TenMH = ?, Khoi = ?, MaTCM = ? WHERE MaMH = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mh.getTenMH());
            stmt.setString(2, mh.getKhoi());
            stmt.setString(3, mh.getMaTCM());
            stmt.setString(4, mh.getMaMH());
            stmt.executeUpdate();
        }
    }

    // Delete a subject
    public void xoaMonHoc(String maMH) throws SQLException {
        String sql = "DELETE FROM MONHOC WHERE MaMH = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maMH);
            stmt.executeUpdate();
        }
    }

    // Get a subject by its ID
    public MonHoc getMonHocById(String maMH) throws SQLException {
        String sql = "SELECT MaMH, TenMH, Khoi, MaTCM FROM MONHOC WHERE MaMH = ?";
        MonHoc monHoc = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maMH);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    monHoc = new MonHoc(
                            rs.getString("MaMH"),
                            rs.getString("TenMH"),
                            rs.getString("Khoi"),
                            rs.getString("MaTCM")
                    );
                }
            }
        }
        return monHoc;
    }

    // Search for subjects (by MaMH or TenMH)
    // You can extend this to search by Khoi or MaTCM if needed
    public List<MonHoc> tracuuMonHoc(String keyword, String khoiFilter, String maTCMFilter) throws SQLException {
        List<MonHoc> danhSachMonHoc = new ArrayList<>();
        // Base query
        StringBuilder sqlBuilder = new StringBuilder("SELECT MaMH, TenMH, Khoi, MaTCM FROM MONHOC WHERE 1=1");

        // Append conditions dynamically
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sqlBuilder.append(" AND (UPPER(MaMH) LIKE UPPER(?) OR UPPER(TenMH) LIKE UPPER(?))");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        if (khoiFilter != null && !khoiFilter.trim().isEmpty() && !khoiFilter.equals("Tất cả")) {
            sqlBuilder.append(" AND Khoi = ?");
            params.add(khoiFilter);
        }
        if (maTCMFilter != null && !maTCMFilter.trim().isEmpty() && !maTCMFilter.equals("Tất cả")) {
            sqlBuilder.append(" AND MaTCM = ?");
            params.add(maTCMFilter);
        }

        sqlBuilder.append(" ORDER BY MaMH ASC"); // Optional: order results

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MonHoc monHoc = new MonHoc(
                            rs.getString("MaMH"),
                            rs.getString("TenMH"),
                            rs.getString("Khoi"),
                            rs.getString("MaTCM")
                    );
                    danhSachMonHoc.add(monHoc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn môn học: " + e.getMessage());
            e.printStackTrace(); // For debugging
            throw e; // Re-throw the exception to be handled by the caller
        }
        return danhSachMonHoc;
    }


    // Get all subjects
    public List<MonHoc> getAllMonHoc() throws SQLException {
        return tracuuMonHoc(null, null, null); // Call the search function with no filters
    }
}
