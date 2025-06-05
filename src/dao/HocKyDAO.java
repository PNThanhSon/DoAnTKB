package dao;

import entities.HocKy;
import util.DatabaseConnection; // Giả sử bạn có một lớp tiện ích để kết nối CSDL

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HocKyDAO {

    /**
     * Lấy tất cả các bản ghi HocKy từ cơ sở dữ liệu.
     *
     * @return Danh sách các đối tượng HocKy.
     */
    public List<HocKy> getAllHocKy() {
        List<HocKy> dsHocKy = new ArrayList<>();
        // Truy vấn SQL để lấy dữ liệu từ bảng HOCKY. Cột "HocKy" trong DB tương ứng với "tenHocKy" trong entity.
        String sql = "SELECT MaHK, HocKy, NamHoc FROM HOCKY ORDER BY NamHoc DESC, HocKy ASC";

        try (Connection conn = DatabaseConnection.getConnection(); // Mở kết nối CSDL
             Statement stmt = conn.createStatement(); // Tạo đối tượng Statement
             ResultSet rs = stmt.executeQuery(sql)) { // Thực thi truy vấn và nhận ResultSet

            // Lặp qua từng dòng kết quả trong ResultSet
            while (rs.next()) {
                String maHK = rs.getString("MaHK");
                String tenHocKy = rs.getString("HocKy"); // Tên cột trong CSDL là "HocKy"
                String namHoc = rs.getString("NamHoc");
                // Tạo đối tượng HocKy từ dữ liệu đọc được và thêm vào danh sách
                dsHocKy.add(new HocKy(maHK, tenHocKy, namHoc));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách học kỳ: " + e.getMessage());
            e.printStackTrace();
        }
        return dsHocKy; // Trả về danh sách học kỳ
    }

    /**
     * Lấy thông tin một HocKy cụ thể bằng MaHK.
     *
     * @param maHK MaHK của HocKy cần lấy.
     * @return Đối tượng HocKy nếu tìm thấy, ngược lại trả về null.
     */
    public HocKy getHocKyByMaHK(String maHK) {
        String sql = "SELECT MaHK, HocKy, NamHoc FROM HOCKY WHERE MaHK = ?";
        HocKy hocKy = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHK); // Thiết lập tham số cho PreparedStatement
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String tenHocKy = rs.getString("HocKy");
                    String namHoc = rs.getString("NamHoc");
                    hocKy = new HocKy(maHK, tenHocKy, namHoc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy học kỳ theo MaHK '" + maHK + "': " + e.getMessage());
            e.printStackTrace();
        }
        return hocKy;
    }

    /**
     * Thêm một HocKy mới vào cơ sở dữ liệu.
     *
     * @param hocKy Đối tượng HocKy cần thêm.
     * @return true nếu thao tác thành công, false nếu ngược lại.
     */
    public boolean addHocKy(HocKy hocKy) {
        // Kiểm tra xem MaHK đã tồn tại chưa bằng cách sử dụng ràng buộc khóa chính của CSDL
        // Nếu CSDL báo lỗi (ví dụ: SQLException với mã lỗi cho duplicate key), đó là dấu hiệu MaHK đã tồn tại.
        String sql = "INSERT INTO HOCKY (MaHK, HocKy, NamHoc) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hocKy.getMaHK());
            pstmt.setString(2, hocKy.getTenHocKy()); // Entity sử dụng getTenHocKy()
            pstmt.setString(3, hocKy.getNamHoc());

            int affectedRows = pstmt.executeUpdate(); // Thực thi lệnh INSERT
            return affectedRows > 0; // Trả về true nếu có dòng nào được thêm
        } catch (SQLException e) {
            // Kiểm tra mã lỗi cụ thể cho vi phạm khóa chính (ví dụ ORA-00001 cho Oracle)
            if (e.getErrorCode() == 1 || e.getMessage().toLowerCase().contains("unique constraint") || e.getMessage().toLowerCase().contains("duplicate key")) { // Thêm kiểm tra chung
                System.err.println("Lỗi thêm học kỳ: MaHK '" + hocKy.getMaHK() + "' đã tồn tại.");
            } else {
                System.err.println("Lỗi khi thêm học kỳ: " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Cập nhật thông tin một HocKy đã có trong cơ sở dữ liệu.
     * MaHK không được phép thay đổi qua phương thức này.
     *
     * @param hocKy Đối tượng HocKy với thông tin đã được cập nhật (MaHK phải khớp với bản ghi hiện có).
     * @return true nếu thao tác thành công, false nếu ngược lại.
     */
    public boolean updateHocKy(HocKy hocKy) {
        String sql = "UPDATE HOCKY SET HocKy = ?, NamHoc = ? WHERE MaHK = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hocKy.getTenHocKy()); // Cập nhật tên học kỳ
            pstmt.setString(2, hocKy.getNamHoc());   // Cập nhật năm học
            pstmt.setString(3, hocKy.getMaHK());     // Điều kiện WHERE theo MaHK

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Cập nhật thất bại: Không tìm thấy học kỳ với MaHK '" + hocKy.getMaHK() + "'.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật học kỳ '" + hocKy.getMaHK() + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xoá một HocKy khỏi cơ sở dữ liệu dựa vào MaHK.
     *
     * @param maHK MaHK của HocKy cần xoá.
     * @return true nếu thao tác thành công, false nếu ngược lại.
     */
    public boolean deleteHocKy(String maHK) {
        String sql = "DELETE FROM HOCKY WHERE MaHK = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHK);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Xóa thất bại: Không tìm thấy học kỳ với MaHK '" + maHK + "'.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            // Bắt lỗi vi phạm ràng buộc khoá ngoại (ví dụ: ORA-02292 trong Oracle)
            // Các mã lỗi có thể khác nhau tùy theo CSDL (SQLServer, MySQL, PostgreSQL)
            if (e.getErrorCode() == 2292 || e.getMessage().toLowerCase().contains("foreign key constraint")) { // Ví dụ Oracle và kiểm tra chung
                System.err.println("Không thể xoá Học kỳ '" + maHK + "' vì đang được tham chiếu bởi các bản ghi khác (ví dụ: Thời Khóa Biểu, Phân Công,...).");
            } else {
                System.err.println("Lỗi khi xóa học kỳ '" + maHK + "': " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Kiểm tra xem một MaHK đã tồn tại trong bảng HOCKY hay chưa.
     *
     * @param maHK MaHK cần kiểm tra.
     * @return true nếu MaHK tồn tại, false nếu không.
     */
    public boolean maHKExists(String maHK) {
        String sql = "SELECT 1 FROM HOCKY WHERE MaHK = ?"; // Truy vấn kiểm tra sự tồn tại
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maHK);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Nếu có kết quả (rs.next() là true), nghĩa là MaHK tồn tại
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra sự tồn tại của MaHK '" + maHK + "': " + e.getMessage());
            e.printStackTrace();
            // Coi đây là trạng thái lỗi, có thể trả về true để ngăn chặn việc chèn
            // hoặc throw một exception tùy thuộc vào cách xử lý lỗi mong muốn
            return true; // An toàn dự phòng, giả sử nó tồn tại nếu có lỗi CSDL
        }
    }
}
