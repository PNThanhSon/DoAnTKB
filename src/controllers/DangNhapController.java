package controllers;

import entities.GiaoVien;
import entities.VaiTroGV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DangNhapController {
    @FXML
    private TextField maGvField;

    @FXML
    private PasswordField matKhauField;

    @FXML
    private Button loginButton; // Đảm bảo fx:id này được gán trong FXML

    @FXML
    private Label statusLabel; // Dùng cho thông báo lỗi trường bắt buộc

    @FXML
    public void initialize() {
        statusLabel.setText("");
    }

    @FXML
    protected void LayThongTinDangNhap(ActionEvent event) {
        String maGV = maGvField.getText();
        String matKhau = matKhauField.getText();

        // Gọi hàm kiểm tra trường bắt buộc
        if (!KiemTraTruongBatBuoc(maGV, matKhau)) {
            return; // Dừng nếu trường bắt buộc không hợp lệ
        }
        // Nếu trường hợp lệ, xóa thông báo lỗi cũ (nếu có)
        statusLabel.setText("");


        System.out.println("Controller: Đang gọi KiemTraDangNhapCSDL cho MaGV: " + maGV);
        Optional<GiaoVien> giaoVienOptional = KiemTraDangNhapCSDL(maGV, matKhau);

        if (giaoVienOptional.isPresent()) {
            GiaoVien loggedInGiaoVien = giaoVienOptional.get();
            System.out.println("Controller: Đăng nhập thành công cho: " + loggedInGiaoVien.getHoGV() + " " + loggedInGiaoVien.getTenGV());

            // Hiển thị thông báo thành công bằng Alert
            ThongBaoDangNhap(true, loggedInGiaoVien.getHoGV() + " " + loggedInGiaoVien.getTenGV());

            try {
                ChuyenGiaoDien(loggedInGiaoVien, event);
            } catch (IOException e) {
                e.printStackTrace();
                // Thông báo lỗi nghiêm trọng hơn nếu không mở được form chính
                showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải trang chính của ứng dụng.\nChi tiết: " + e.getMessage());
            }
        } else {
            System.out.println("Controller: Đăng nhập thất bại cho MaGV: " + maGV);
            // Hiển thị thông báo thất bại bằng Alert
            ThongBaoDangNhap(false, null);

            matKhauField.clear();
            matKhauField.requestFocus();
        }
    }

    private boolean KiemTraTruongBatBuoc(String maGV, String matKhau) {
        if (maGV.trim().isEmpty() || matKhau.isEmpty()) {
            ThongBaoLoi("Mã giáo viên và mật khẩu không được để trống!");
            return false;
        }
        return true;
    }

    private void ThongBaoLoi(String message) {
        statusLabel.setText(message);
        statusLabel.setTextFill(javafx.scene.paint.Color.RED);
        if (maGvField.getText().trim().isEmpty()) {
            maGvField.requestFocus();
        } else {
            matKhauField.requestFocus();
        }
    }

    private void ThongBaoDangNhap(boolean thanhCong, String tenGiaoVien) {
        if (thanhCong) {
            showAlert(Alert.AlertType.INFORMATION, "Đăng Nhập Thành Công",
                    "Chào mừng " + (tenGiaoVien != null ? tenGiaoVien : "") + " đã đăng nhập thành công vào hệ thống!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Đăng Nhập Thất Bại",
                    "Mã giáo viên hoặc mật khẩu không đúng.\nVui lòng kiểm tra lại thông tin đăng nhập.");
        }
    }


    private void ChuyenGiaoDien(GiaoVien giaoVien, ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/MainForm.fxml"));
        Parent mainFormRoot = loader.load();

        MainFormController mainFormController = loader.getController();
        mainFormController.setLoggedInGiaoVien(giaoVien);

        Stage mainStage = new Stage();
        mainStage.setTitle("Trang Chính - Hệ Thống Quản Lý Thời Khóa Biểu Trường Học");
        mainStage.setScene(new Scene(mainFormRoot, 800, 600));
        mainStage.setMinWidth(700);
        mainStage.setMinHeight(500);

        mainStage.setOnCloseRequest(e -> {
            System.out.println("MainForm đang đóng, ứng dụng sẽ thoát.");
            System.exit(0);
        });

        mainStage.show();
        currentStage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Không hiển thị header text
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Optional<GiaoVien> KiemTraDangNhapCSDL(String maGV, String matKhau) {
        String sql = "SELECT MaGV, HoGV, TenGV, GioiTinh, ChuyenMon, MaTCM, SoTietQuyDinh, " +
                "SoTietThucHien, SoTietDuThieu, Email, SDT, MatKhau, GhiChu " +
                "FROM GIAOVIEN WHERE MaGV = ? AND MatKhau = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maGV.trim());
            pstmt.setString(2, matKhau);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
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
                            rs.getString("GhiChu"),
                            xacDinhVaiTro(rs.getString("MaGV"), rs.getString("GhiChu"))
                    );
                    System.out.println("Controller/KiemTraDangNhapCSDL: Xác thực thành công cho MaGV: " + maGV);
                    return Optional.of(giaoVien);
                } else {
                    System.out.println("Controller/KiemTraDangNhapCSDL: Không tìm thấy giáo viên với MaGV: " + maGV + " và mật khẩu cung cấp, hoặc mật khẩu sai.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Controller/KiemTraDangNhapCSDL: Lỗi SQL khi xác thực giáo viên (MaGV: " + maGV + "): " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private VaiTroGV xacDinhVaiTro(String maGV, String ghiChu) {
        if ("ADMIN".equals(maGV) || "phutrachtkb".equals(ghiChu)) {
            return VaiTroGV.ADMIN; // ADMIN hoặc giáo viên phụ trách, có quyền hạn cao
        }

        Connection conn = DatabaseConnection.getConnection();
        String sqlKiemTraToTruongPho = "SELECT COUNT(*) AS SoLuong FROM TOCHUYENMON WHERE TOTRUONG = ? OR TOPHO = ?";
        String sqlKiemTraGVCN = "SELECT COUNT(*) AS SoLuong FROM LOP WHERE GVCN = ?";

        try (PreparedStatement pstmtTCM = conn.prepareStatement(sqlKiemTraToTruongPho)) {
            pstmtTCM.setString(1, maGV);
            pstmtTCM.setString(2, maGV);

            try (ResultSet rsTCM = pstmtTCM.executeQuery()) {
                if (rsTCM.next() && rsTCM.getInt("SoLuong") > 0) {
                    return VaiTroGV.TCM; //Là tổ trưởng hoặc tổ phó chuyên môn
                }
            }
        } catch (SQLException e_tcm) {
            System.err.println("Lỗi khi kiểm tra vai trò Tổ trưởng/phó: " + e_tcm.getMessage());
        }

        try (PreparedStatement pstmtGVCN = conn.prepareStatement(sqlKiemTraGVCN)) {
            pstmtGVCN.setString(1, maGV);

            try (ResultSet rsGVCN = pstmtGVCN.executeQuery()) {
                if (rsGVCN.next() && rsGVCN.getInt("SoLuong") > 0) {
                    return VaiTroGV.GVCN; //Là giáo viên chủ nhiệm
                }
            }
        } catch (SQLException e_tcm) {
            System.err.println("Lỗi khi kiểm tra vai trò Giáo viên chủ nhiệm: " + e_tcm.getMessage());
        }
        return VaiTroGV.GV; //Giáo viên thường
    }
}
