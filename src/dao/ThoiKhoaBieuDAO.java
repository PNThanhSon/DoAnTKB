package dao;

import entities.*; // Đầy đủ các entities
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ThoiKhoaBieuDAO {
    public List<GiaoVien> getDanhSachGiaoVien() {
        List<GiaoVien> danhSach = new ArrayList<>();
        String sql = "SELECT MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, " +
                "SoTietThucHien, SoTietDuThieu, Email, SDT, MatKhau, GhiChu " +
                "FROM GIAOVIEN ORDER BY MaGV";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(sql)) {
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
                    danhSach.add(giaoVien);
                }
            }
        } catch (SQLException e) {
            // Thay thế bằng logger trong ứng dụng thực tế
            System.err.println("Lỗi SQL khi lấy danh sách giáo viên: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<Lop> getDanhSachTatCaLop() {
        List<Lop> danhSach = new ArrayList<>();
        String sql = "SELECT MaLop, TenLop, Khoi, GVCN FROM LOP ORDER BY Khoi, TenLop";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn == null) {
                System.err.println("DAO: Không thể kết nối CSDL để lấy danh sách Lớp.");
                return danhSach;
            }
            while (rs.next()) {
                danhSach.add(new Lop(
                        rs.getString("MaLop"),
                        rs.getString("TenLop"),
                        rs.getString("Khoi"),
                        rs.getString("GVCN")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách tất cả Lớp: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<ToChuyenMon> getDanhSachTatCaTCM() {
        List<ToChuyenMon> danhSach = new ArrayList<>();
        String sql = "SELECT MaTCM, TenTCM, ToTruong, ToPho FROM TOCHUYENMON ORDER BY TenTCM";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn == null) {
                System.err.println("DAO: Không thể kết nối CSDL để lấy danh sách TCM.");
                return danhSach;
            }
            while (rs.next()) {
                danhSach.add(new ToChuyenMon(
                        rs.getString("MaTCM"),
                        rs.getString("TenTCM"),
                        rs.getString("ToTruong"),
                        rs.getString("ToPho")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách tất cả TCM: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSach;
    }

    public String getTenLopCN(String maGV) {
        if (maGV == null || maGV.trim().isEmpty()) return null;
        String sql = "SELECT l.MaLop FROM LOP l WHERE l.GVCN = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maGV);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getString("MaLop");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy lớp chủ nhiệm cho GV " + maGV + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getTenTCMByMaTCM(String maTCM) {
        if (maTCM == null || maTCM.trim().isEmpty()) return null;
        String sql = "SELECT TenTCM FROM TOCHUYENMON WHERE MaTCM = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maTCM);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getString("TenTCM");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy tên TCM với MaTCM " + maTCM + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public List<HocKy> getDanhSachHocKy() {
        List<HocKy> danhSach = new ArrayList<>();
        String sql = "SELECT MaHK, HocKy, NamHoc FROM HOCKY ORDER BY NamHoc DESC, SUBSTR(HocKy, INSTR(HocKy, ' ') + 1) DESC, MaHK DESC";
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
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<ThoiKhoaBieu> getDanhSachThoiKhoaBieuTheoHocKy(String maHK) {
        List<ThoiKhoaBieu> danhSach = new ArrayList<>();
        String sql = "SELECT MaTKB, NgayApDung, Buoi FROM THOIKHOABIEU WHERE MaHK = ? ORDER BY NgayApDung DESC, MaTKB ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maHK);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(new ThoiKhoaBieu(
                            rs.getString("MaTKB"),
                            rs.getDate("NgayApDung"),
                            rs.getString("Buoi"),
                            maHK
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<MonHoc> getDanhSachMonHocTheoTCM(String maTCM) {
        List<MonHoc> danhSach = new ArrayList<>();
        String sql = "SELECT MaMH, TenMH, Khoi, MaTCM FROM MONHOC WHERE MaTCM = ? ORDER BY TenMH, Khoi";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maTCM);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(new MonHoc(
                            rs.getString("MaMH"),
                            rs.getString("TenMH"),
                            rs.getString("Khoi"),
                            rs.getString("MaTCM")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<NhomMonHocDisplay> getNhomMonHocByTCM(String maTCM) {
        List<NhomMonHocDisplay> ketQua = new ArrayList<>();
        List<MonHoc> tatCaMonHocCuaTCM = getDanhSachMonHocTheoTCM(maTCM);
        if (tatCaMonHocCuaTCM.isEmpty()) return ketQua;

        Map<String, List<MonHoc>> groupedByTenMH = tatCaMonHocCuaTCM.stream()
                .collect(Collectors.groupingBy(MonHoc::getTenMH));

        for (Map.Entry<String, List<MonHoc>> entry : groupedByTenMH.entrySet()) {
            String tenMonHocChung = entry.getKey();
            List<String> danhSachMaMH = entry.getValue().stream()
                    .map(MonHoc::getMaMH)
                    .distinct() // Đảm bảo mã môn học là duy nhất trong nhóm
                    .collect(Collectors.toList());
            String maTCMCuaNhom = entry.getValue().getFirst().getMaTCM(); // Lấy MaTCM từ môn đầu tiên
            ketQua.add(new NhomMonHocDisplay(tenMonHocChung, danhSachMaMH, maTCMCuaNhom));
        }
        ketQua.sort((n1, n2) -> n1.getTenMonHocChung().compareToIgnoreCase(n2.getTenMonHocChung()));
        return ketQua;
    }

    // Hàm này dùng cho TKB cá nhân của giáo viên
    public List<ChiTietTKB> getChiTietTKBForGiaoVien(String maTKB, String maGV) {
        List<ChiTietTKB> chiTietList = new ArrayList<>();
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
                    chiTietList.add(ChiTietTKB.taoChoTKBCaNhan( // Sử dụng factory method
                            rs.getInt("Thu"),
                            rs.getInt("Tiet"),
                            rs.getString("TenMH"),
                            rs.getString("MaLop"),
                            2
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chiTietList;
    }

    // Hàm này dùng cho TKB của Lớp chủ nhiệm
    public List<ChiTietTKB> getChiTietTKBForLopCN(String maTKB, String maGV) {
        List<ChiTietTKB> chiTietList = new ArrayList<>();

        String sql = "SELECT ct.Thu, ct.Tiet, mh.TenMH, ct.MaLop, gv.MaGV, gv.HoGV || ' ' || gv.TenGV AS HoTenGV " +
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
                    chiTietList.add(ChiTietTKB.taoChoTKBLopCN(
                            rs.getInt("Thu"),
                            rs.getInt("Tiet"),
                            rs.getString("TenMH"),
                            rs.getString("HoTenGV"),
                            rs.getString("MaGV"),
                            3
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy chi tiết TKB cho Lớp Chủ Nhiệm, TKB " + maTKB + ": " + e.getMessage());
            e.printStackTrace();
        }
        return chiTietList;
    }


    /**
     * Lấy chi tiết TKB cho Tổ Chuyên Môn, dựa trên TKB, danh sách Mã Môn Học, và MaTCM.
     */
    public List<ChiTietTKB> getChiTietTKBForTCM(String maTKB, List<String> danhSachMaMH, String maTCM) {
        List<ChiTietTKB> chiTietList = new ArrayList<>();
        if (danhSachMaMH == null || danhSachMaMH.isEmpty() || maTKB == null || maTCM == null) {
            return chiTietList;
        }

        String maMhPlaceholders = danhSachMaMH.stream().map(maMH -> "?").collect(Collectors.joining(", "));

        // Lấy MaGV, HoTenGV, MaLop, Thu, Tiet, TenMH (thực tế trong TKB)
        // Lọc theo các MaMH thuộc nhóm môn đã chọn và GV thuộc TCM
        String sql = "SELECT ct.Thu, ct.Tiet, ct.MaLop, gv.MaGV, gv.HoGV || ' ' || gv.TenGV AS HoTenGV, mh.TenMH AS TenMonHocTrongTKB " +
                "FROM CHITIETTKB ct " +
                "JOIN GIAOVIEN gv ON ct.MaGV = gv.MaGV " +
                "JOIN MONHOC mh ON ct.MaMH = mh.MaMH " +
                "WHERE ct.MaTKB = ? AND ct.MaMH IN (" + maMhPlaceholders + ") AND gv.MaTCM = ? " +
                "ORDER BY gv.TenGV, gv.HoGV, ct.Thu, ct.Tiet";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            pstmt.setString(paramIndex++, maTKB);
            for (String maMH : danhSachMaMH) {
                pstmt.setString(paramIndex++, maMH);
            }
            pstmt.setString(paramIndex, maTCM); // Lọc giáo viên theo MaTCM

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    chiTietList.add(ChiTietTKB.taoChoTKBTCM(
                            rs.getInt("Thu"),
                            rs.getInt("Tiet"),
                            rs.getString("TenMonHocTrongTKB"),
                            rs.getString("MaLop"),
                            rs.getString("HoTenGV"),
                            rs.getString("MaGV"),
                            1
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy chi tiết TKB cho TCM (MaTCM: " + maTCM + ", TKB: " + maTKB + "): " + e.getMessage());
            e.printStackTrace();
        }
        return chiTietList;
    }
}
