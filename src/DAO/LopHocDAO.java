package DAO;

import entities.LopHoc;
import util.DatabaseConnection; // Đảm bảo bạn đã có class này

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LopHocDAO {

    // Thêm một lớp học mới
    public void themLopHoc(LopHoc lh) throws SQLException {
        // Bảng LOP có các cột: MaLop, TenLop, Khoi, GVCN
        String sql = "INSERT INTO LOP (MaLop, TenLop, Khoi, GVCN) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lh.getMaLop());
            stmt.setString(2, lh.getTenLop());
            stmt.setString(3, lh.getKhoi());
            stmt.setString(4, lh.getMaGVCN()); // GVCN là MaGVCN trong LopHoc.java
            stmt.executeUpdate();
        }
    }

    // Cập nhật thông tin một lớp học
    public void capNhatLopHoc(LopHoc lh) throws SQLException {
        String sql = "UPDATE LOP SET TenLop = ?, Khoi = ?, GVCN = ? WHERE MaLop = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lh.getTenLop());
            stmt.setString(2, lh.getKhoi());
            stmt.setString(3, lh.getMaGVCN());
            stmt.setString(4, lh.getMaLop());
            stmt.executeUpdate();
        }
    }

    // Xóa một lớp học
    public void xoaLopHoc(String maLop) throws SQLException {
        String sql = "DELETE FROM LOP WHERE MaLop = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maLop);
            stmt.executeUpdate();
        }
    }

    // Lấy thông tin một lớp học bằng Mã Lớp
    public LopHoc getLopHocById(String maLop) throws SQLException {
        String sql = "SELECT MaLop, TenLop, Khoi, GVCN FROM LOP WHERE MaLop = ?";
        LopHoc lopHoc = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLop);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    lopHoc = new LopHoc(
                            rs.getString("MaLop"),
                            rs.getString("TenLop"),
                            rs.getString("Khoi"),
                            rs.getString("GVCN") // Cột GVCN trong CSDL
                    );
                }
            }
        }
        return lopHoc;
    }

    // Tra cứu lớp học
    public List<LopHoc> tracuuLopHoc(String keyword, String khoiFilter, String maGVCNFilter) throws SQLException {
        List<LopHoc> danhSachLopHoc = new ArrayList<>();
        // Giả sử bạn muốn join với bảng GIAOVIEN để lấy tên GVCN hiển thị
        // Nếu không, câu SELECT sẽ đơn giản hơn: "SELECT MaLop, TenLop, Khoi, GVCN FROM LOP WHERE 1=1"
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT l.MaLop, l.TenLop, l.Khoi, l.GVCN " +
                        // опционально: ", gv.HoGV || ' ' || gv.TenGV AS TenGVCN " + // Nếu muốn lấy tên GV
                        "FROM LOP l " +
                        // опционально: "LEFT JOIN GIAOVIEN gv ON l.GVCN = gv.MaGV " + // Nếu muốn lấy tên GV
                        "WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sqlBuilder.append(" AND (UPPER(l.MaLop) LIKE UPPER(?) OR UPPER(l.TenLop) LIKE UPPER(?))");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        if (khoiFilter != null && !khoiFilter.trim().isEmpty() && !khoiFilter.equalsIgnoreCase("Tất cả")) {
            sqlBuilder.append(" AND l.Khoi = ?");
            params.add(khoiFilter);
        }
        if (maGVCNFilter != null && !maGVCNFilter.trim().isEmpty() && !maGVCNFilter.equalsIgnoreCase("Tất cả")) {
            // Giả sử maGVCNFilter là MaGV thực sự, không phải đối tượng GiaoVien
            sqlBuilder.append(" AND l.GVCN = ?");
            params.add(maGVCNFilter);
        }

        sqlBuilder.append(" ORDER BY l.Khoi ASC, l.TenLop ASC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LopHoc lopHoc = new LopHoc(
                            rs.getString("MaLop"),
                            rs.getString("TenLop"),
                            rs.getString("Khoi"),
                            rs.getString("GVCN")
                    );
                    // Nếu bạn lấy TenGVCN từ join, bạn có thể set nó vào đối tượng LopHoc
                    // (cần thêm thuộc tính và getter/setter cho tenGVCN trong LopHoc.java)
                    // Ví dụ: lopHoc.setTenGVCN(rs.getString("TenGVCN"));
                    danhSachLopHoc.add(lopHoc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn lớp học: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return danhSachLopHoc;
    }

    // Lấy tất cả lớp học
    public List<LopHoc> getAllLopHoc() throws SQLException {
        return tracuuLopHoc(null, null, null);
    }
}
