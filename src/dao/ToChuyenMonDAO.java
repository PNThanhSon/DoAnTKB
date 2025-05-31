package dao;

import entities.GiaoVien;
import entities.ToChuyenMon;
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
        String sql = """
                SELECT tcm.matcm, tcm.tentcm, tcm.totruong, tcm.topho, COUNT(gv.magv) AS SLGV
                FROM TOCHUYENMON tcm
                LEFT JOIN GIAOVIEN gv ON tcm.matcm = gv.matcm
                GROUP BY tcm.matcm, tcm.tentcm, tcm.totruong, tcm.topho""";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String maTCM = rs.getString("MaTCM");
                String tenTCM = rs.getString("TenTCM");
                String toTruong = rs.getString("ToTruong");
                String toPho = rs.getString("ToPho");
                Integer slGV = rs.getInt("SLGV");
                ToChuyenMon tcm = new ToChuyenMon(maTCM, tenTCM, toTruong, toPho, slGV);
                danhSachTCM.add(tcm);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách TCM: " + e.getMessage());
            throw e;
        }

        return danhSachTCM;
    }


    public List<ToChuyenMon> tracuuToChuyenMon(String keywork) throws SQLException {
        List<ToChuyenMon> danhSachTCM = new ArrayList<>();
        String sql = "SELECT * FROM TOCHUYENMON WHERE MATCM like ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keywork + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                danhSachTCM.clear();
                while (rs.next()) {
                    String maTCM = rs.getString("MaTCM");
                    String tenTCM = rs.getString("TenTCM");
                    String toTruong = rs.getString("ToTruong");
                    String toPho = rs.getString("ToPho");

                    ToChuyenMon tcm = new ToChuyenMon(maTCM, tenTCM, toTruong, toPho);
                    danhSachTCM.add(tcm);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách TCM: " + e.getMessage());
            throw e;
        }

        return danhSachTCM;
    }

    public void capNhatThongTin(ToChuyenMon TCM) throws SQLException {
        String sql = "UPDATE ToChuyenMon SET tentcm = ?, totruong = ?, topho = ? "+
                "WHERE MaTCM = ?";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, TCM.getTenTCM());
            stmt.setString(2, TCM.getToTruong());
            stmt.setString(3, TCM.getToPho());
            stmt.setString(4, TCM.getMaTCM());

            stmt.executeUpdate();
        }
    }


    public void themToChuyenMon(ToChuyenMon TCM) throws SQLException {
        String sql = "INSERT INTO ToChuyenMon (tenTCM, Totruong, topho)" +
                "VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, TCM.getTenTCM());
            stmt.setString(2, TCM.getToTruong());
            stmt.setString(3, TCM.getToPho());


            stmt.executeUpdate();
        }
    }


    public void xoaToChuyenMon(ToChuyenMon TCM) throws SQLException {
        String sql = "DELETE FROM ToChuyenMon WHERE MaTCM = ?";
        System.out.println(TCM.getMaTCM());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, TCM.getMaTCM());
            stmt.executeUpdate();
        }
    }
}
