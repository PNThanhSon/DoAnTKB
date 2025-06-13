package controllers;

import entities.GiaoVien; // Đảm bảo import đúng package
import util.DatabaseConnection; // Đảm bảo import đúng package

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProfileController {

    // Labels để hiển thị thông tin (chế độ xem)
    @FXML private Label maGvLabel;
    @FXML private Label hoTenLabel;
    @FXML private Label gioiTinhLabel;
    @FXML private Label emailLabel;
    @FXML private Label sdtLabel;
    @FXML private Label chuyenMonLabel;
    @FXML private Label maTCMLabel;
    @FXML private Label soTietQuyDinhLabel;
    @FXML private Label soTietThucHienLabel;
    @FXML private Label soTietDuThieuLabel;
    @FXML private Label ghiChuLabel;

    // TextFields/TextArea để chỉnh sửa thông tin (chế độ sửa)
    @FXML private TextField emailTextField;
    @FXML private TextField sdtTextField;
    @FXML private TextArea ghiChuTextArea;

    // Buttons
    @FXML private Button suaThongTinButton;
    @FXML private Button luuButton;
    @FXML private Button huyBoButton;

    private GiaoVien currentGiaoVien;
    // private boolean dangCheDoSua = false; // Không cần thiết nếu quản lý bằng visibility của nút

    @FXML
    public void initialize() {
        // Ban đầu, các trường nhập liệu và nút Lưu/Hủy bị ẩn
        thietLapCheDoGiaoDien(false); // false = chế độ xem
    }

    /**
     * Nhận đối tượng GiaoVien và hiển thị thông tin lên các Label.
     * Cũng điền dữ liệu vào các TextField/TextArea để sẵn sàng cho việc sửa.
     * @param giaoVien Đối tượng GiaoVien chứa thông tin người dùng.
     */
    public void khoiTaoDuLieu(GiaoVien giaoVien) {
        this.currentGiaoVien = giaoVien;
        if (giaoVien != null) {
            hienThiThongTinGiaoVien();
        } else {
            clearAllFields();
        }
    }

    private void hienThiThongTinGiaoVien() {
        if (currentGiaoVien == null) return;

        maGvLabel.setText(getOrDefault(currentGiaoVien.getMaGV()));
        hoTenLabel.setText(getOrDefault(currentGiaoVien.getHoGV()) + " " + getOrDefault(currentGiaoVien.getTenGV()));
        gioiTinhLabel.setText(getOrDefault(currentGiaoVien.getGioiTinh()));

        emailLabel.setText(getOrDefault(currentGiaoVien.getEmail()));
        emailTextField.setText(currentGiaoVien.getEmail() != null ? currentGiaoVien.getEmail() : "");

        sdtLabel.setText(getOrDefault(currentGiaoVien.getSdt()));
        sdtTextField.setText(currentGiaoVien.getSdt() != null ? currentGiaoVien.getSdt() : "");

        chuyenMonLabel.setText(getOrDefault(currentGiaoVien.getChuyenMon()));
        maTCMLabel.setText(getOrDefault(currentGiaoVien.getMaTCM()));

        soTietQuyDinhLabel.setText(currentGiaoVien.getSoTietQuyDinh() != null ? String.valueOf(currentGiaoVien.getSoTietQuyDinh()) : "N/A");
        soTietThucHienLabel.setText(currentGiaoVien.getSoTietThucHien() != null ? String.valueOf(currentGiaoVien.getSoTietThucHien()) : "N/A");
        soTietDuThieuLabel.setText(currentGiaoVien.getSoTietDuThieu() != null ? String.valueOf(currentGiaoVien.getSoTietDuThieu()) : "N/A");

        ghiChuLabel.setText(getOrDefault(currentGiaoVien.getGhiChu()));
        ghiChuTextArea.setText(currentGiaoVien.getGhiChu() != null ? currentGiaoVien.getGhiChu() : "");
    }

    private void clearAllFields() {
        String na = "N/A";
        maGvLabel.setText(na);
        hoTenLabel.setText(na);
        gioiTinhLabel.setText(na);
        emailLabel.setText(na); emailTextField.clear();
        sdtLabel.setText(na); sdtTextField.clear();
        chuyenMonLabel.setText(na);
        maTCMLabel.setText(na);
        soTietQuyDinhLabel.setText(na);
        soTietThucHienLabel.setText(na);
        soTietDuThieuLabel.setText(na);
        ghiChuLabel.setText(na); ghiChuTextArea.clear();
    }

    private String getOrDefault(String value) {
        return (value != null && !value.trim().isEmpty()) ? value : "N/A";
    }

    /**
     * Chuyển đổi giao diện giữa chế độ xem và chế độ sửa.
     * @param editing true nếu đang ở chế độ sửa, false nếu ở chế độ xem.
     */
    private void thietLapCheDoGiaoDien(boolean editing) {
        // Email
        emailLabel.setVisible(!editing); emailLabel.setManaged(!editing);
        emailTextField.setVisible(editing); emailTextField.setManaged(editing);

        // SDT
        sdtLabel.setVisible(!editing); sdtLabel.setManaged(!editing);
        sdtTextField.setVisible(editing); sdtTextField.setManaged(editing);

        // GhiChu
        ghiChuLabel.setVisible(!editing); ghiChuLabel.setManaged(!editing);
        ghiChuTextArea.setVisible(editing); ghiChuTextArea.setManaged(editing);

        // Buttons
        suaThongTinButton.setVisible(!editing); suaThongTinButton.setManaged(!editing);
        luuButton.setVisible(editing); luuButton.setManaged(editing);
        huyBoButton.setVisible(editing); huyBoButton.setManaged(editing);
    }

    @FXML
    private void xuLyChuyenSangCheDoSua(ActionEvent event) {
        if (currentGiaoVien != null) {
            // Điền dữ liệu hiện tại vào các ô input một lần nữa để chắc chắn
            emailTextField.setText(currentGiaoVien.getEmail() != null ? currentGiaoVien.getEmail() : "");
            sdtTextField.setText(currentGiaoVien.getSdt() != null ? currentGiaoVien.getSdt() : "");
            ghiChuTextArea.setText(currentGiaoVien.getGhiChu() != null ? currentGiaoVien.getGhiChu() : "");
            thietLapCheDoGiaoDien(true); // Chuyển sang chế độ sửa
        } else {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Không có thông tin giáo viên để sửa.");
        }
    }

    @FXML
    private void xuLyLuuThayDoi(ActionEvent event) {
        if (currentGiaoVien == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không có thông tin giáo viên để lưu.");
            return;
        }

        // Lấy thông tin mới từ các trường nhập liệu
        String newEmail = emailTextField.getText();
        String newSdt = sdtTextField.getText();
        String newGhiChu = ghiChuTextArea.getText();

        // Tạo một đối tượng GiaoVien tạm thời với các thông tin đã cập nhật
        // (chỉ những trường được phép sửa)
        GiaoVien giaoVienDaCapNhat = new GiaoVien(
                currentGiaoVien.getMaGV(), // MaGV không đổi
                currentGiaoVien.getHoGV(), // HoGV không đổi
                currentGiaoVien.getTenGV(), // TenGV không đổi
                currentGiaoVien.getGioiTinh(), // GioiTinh không đổi
                currentGiaoVien.getChuyenMon(), // ChuyenMon không đổi
                currentGiaoVien.getMaTCM(), // MaTCM không đổi
                currentGiaoVien.getSoTietQuyDinh(), // SoTietQuyDinh không đổi
                currentGiaoVien.getSoTietThucHien(), // SoTietThucHien không đổi
                currentGiaoVien.getSoTietDuThieu(), // SoTietDuThieu không đổi
                newEmail, // Email mới
                newSdt,   // SDT mới
                currentGiaoVien.getMatKhau(), // MatKhau không đổi ở form này
                newGhiChu // GhiChu mới
        );

        // Thực hiện cập nhật vào cơ sở dữ liệu
        boolean capNhatThanhCong = thucHienCapNhatCSDL(giaoVienDaCapNhat);

        if (capNhatThanhCong) {
            // Cập nhật đối tượng currentGiaoVien với thông tin mới
            this.currentGiaoVien.setEmail(newEmail);
            this.currentGiaoVien.setSdt(newSdt);
            this.currentGiaoVien.setGhiChu(newGhiChu);

            hienThiThongTinGiaoVien(); // Hiển thị lại thông tin đã cập nhật trên các Label
            thietLapCheDoGiaoDien(false); // Chuyển về chế độ xem
            showAlert(Alert.AlertType.INFORMATION, "Thành Công", "Thông tin cá nhân đã được cập nhật.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Thất Bại", "Không thể cập nhật thông tin cá nhân.\nVui lòng thử lại hoặc kiểm tra kết nối cơ sở dữ liệu.");
            // Giữ nguyên ở chế độ sửa để người dùng có thể thử lại hoặc hủy bỏ
        }
    }

    @FXML
    private void xuLyHuyBo(ActionEvent event) {
        // Đặt lại giá trị các TextField/TextArea về giá trị ban đầu của currentGiaoVien
        // bằng cách gọi lại displayGiaoVienInfo (vì nó cũng cập nhật TextField/TextArea)
        hienThiThongTinGiaoVien();
        thietLapCheDoGiaoDien(false); // Chuyển về chế độ xem
    }

    /**
     * Thực hiện câu lệnh UPDATE vào cơ sở dữ liệu.
     * @param giaoVienDaSua Đối tượng GiaoVien chứa thông tin cần cập nhật.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    private boolean thucHienCapNhatCSDL(GiaoVien giaoVienDaSua) {
        if (giaoVienDaSua == null || giaoVienDaSua.getMaGV() == null) {
            System.err.println("ProfileController/CapNhat: Thông tin giáo viên không hợp lệ.");
            return false;
        }

        String sql = "UPDATE GIAOVIEN SET Email = ?, SDT = ?, GhiChu = ? WHERE MaGV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, giaoVienDaSua.getEmail());
            pstmt.setString(2, giaoVienDaSua.getSdt());
            pstmt.setString(3, giaoVienDaSua.getGhiChu());
            pstmt.setString(4, giaoVienDaSua.getMaGV());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("ProfileController/CapNhat: Cập nhật thành công thông tin cho MaGV: " + giaoVienDaSua.getMaGV());
                return true;
            } else {
                System.out.println("ProfileController/CapNhat: Không có dòng nào được cập nhật cho MaGV: " + giaoVienDaSua.getMaGV() + ". Có thể MaGV không tồn tại.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("ProfileController/CapNhat: Lỗi SQL khi cập nhật thông tin giáo viên (MaGV: " + giaoVienDaSua.getMaGV() + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
