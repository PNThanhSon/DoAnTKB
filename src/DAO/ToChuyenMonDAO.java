package DAO;

import entities.ToChuyenMon;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ToChuyenMonDAO {
    // Lấy danh sách tất cả Tổ chuyên môn từ DB
    public List<ToChuyenMon> getDanhSachTCM() throws SQLException {
        List<ToChuyenMon> danhSachTCM = new ArrayList<>();
        String sql = "SELECT * FROM TOCHUYENMON";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String maTCM = rs.getString("MaTCM");
                String tenTCM = rs.getString("TenTCM");
                String toTruong = rs.getString("ToTruong");
                String toPho = rs.getString("ToPho");

                ToChuyenMon tcm = new ToChuyenMon(maTCM, tenTCM, toTruong, toPho);
                danhSachTCM.add(tcm);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách TCM: " + e.getMessage());
            throw e;
        }

        return danhSachTCM;
    }
}