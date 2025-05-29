// Trong file controllers/TimeTableController.java
package controllers;

import entities.GiaoVien;
import entities.VaiTroGV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane; // Thêm import này

import java.io.IOException;

public class TimeTableController {

    @FXML private Button btnTKBToanTruong;
    @FXML private Button btnTKBLopCN;
    @FXML private Button btnTKBToCM;
    @FXML private Button btnTKBCaNhan;

    private GiaoVien currentGiaoVien;
    private BorderPane mainBorderPane; // Thêm trường này

    @FXML
    public void initialize() {
        setButtonVisibility(VaiTroGV.UNKNOWN);
    }

    // Cập nhật phương thức này hoặc tạo một phương thức init mới
    public void initData(GiaoVien giaoVien, BorderPane mainPane) {
        this.currentGiaoVien = giaoVien;
        this.mainBorderPane = mainPane; // Lưu tham chiếu

        if (this.currentGiaoVien != null) {
            VaiTroGV vaiTroCode = this.currentGiaoVien.getVaiTro();
            System.out.println("TimeTableController nhận GV: " + giaoVien.getMaGV() + " với vai trò code: " + vaiTroCode);
            setButtonVisibility(vaiTroCode);
        } else {
            System.err.println("TimeTableController: Thông tin giáo viên là null.");
            setButtonVisibility(VaiTroGV.UNKNOWN);
        }
    }

    private void setButtonVisibility(VaiTroGV vaiTroCode) {
        btnTKBToanTruong.setVisible(false); btnTKBToanTruong.setManaged(false);
        btnTKBLopCN.setVisible(false);    btnTKBLopCN.setManaged(false);
        btnTKBToCM.setVisible(false);     btnTKBToCM.setManaged(false);
        btnTKBCaNhan.setVisible(false);   btnTKBCaNhan.setManaged(false);

        switch (vaiTroCode) {
            case ADMIN:
                btnTKBToanTruong.setVisible(true); btnTKBToanTruong.setManaged(true);
                btnTKBLopCN.setVisible(true);    btnTKBLopCN.setManaged(true);
                btnTKBToCM.setVisible(true);     btnTKBToCM.setManaged(true);
                btnTKBCaNhan.setVisible(true);   btnTKBCaNhan.setManaged(true);
                break;
            case TCM:
                btnTKBToCM.setVisible(true);     btnTKBToCM.setManaged(true);
                btnTKBCaNhan.setVisible(true);   btnTKBCaNhan.setManaged(true);
                break;
            case GVCN:
                btnTKBLopCN.setVisible(true);    btnTKBLopCN.setManaged(true);
                btnTKBCaNhan.setVisible(true);   btnTKBCaNhan.setManaged(true);
                break;
            case GV:
            default: // Bao gồm cả UNKNOWN, nhưng chỉ hiện nếu đã đăng nhập
                if (currentGiaoVien != null) {
                    btnTKBCaNhan.setVisible(true);   btnTKBCaNhan.setManaged(true);
                }
                break;
        }
    }

    @FXML
    private void handleTKBToanTruong(ActionEvent event) {
        if (currentGiaoVien == null) {
            showAlert("Lỗi", "Chưa có thông tin giáo viên để xem TKB cá nhân.");
            return;
        }
        if (mainBorderPane == null) {
            System.err.println("Lỗi: mainBorderPane chưa được thiết lập trong TimeTableController.");
            showAlert("Lỗi Hệ Thống", "Không thể thay đổi nội dung hiển thị.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/TKBToanTruong.fxml"));
            Parent tkbTTRoot = loader.load();

            TKBToanTruongController tkbTT = loader.getController();
            tkbTT.initData(mainBorderPane);
            mainBorderPane.setCenter(tkbTTRoot); // Đặt nội dung mới vào vùng center của BorderPane chính
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi Hệ Thống", "Không thể tải trang Thời Khóa Biểu Toàn trường.\nChi tiết: " + e.getMessage());
        } catch (Exception e) { // Bắt các lỗi khác có thể xảy ra
            e.printStackTrace();
            showAlert("Lỗi Không Xác Định", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    @FXML
    private void handleTKBLopCN(ActionEvent event) {
        if (currentGiaoVien == null) {
            showAlert("Lỗi", "Chưa có thông tin giáo viên để xem TKB cá nhân.");
            return;
        }
        if (mainBorderPane == null) {
            System.err.println("Lỗi: mainBorderPane chưa được thiết lập trong TimeTableController.");
            showAlert("Lỗi Hệ Thống", "Không thể thay đổi nội dung hiển thị.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/TKBLopCN.fxml"));
            Parent tkbCaNhanRoot = loader.load();

            // Lấy controller
            TKBLopCNController tkbCN = loader.getController();
            tkbCN.initData(currentGiaoVien);

            mainBorderPane.setCenter(tkbCaNhanRoot); // Đặt nội dung mới vào vùng center của BorderPane chính

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi Hệ Thống", "Không thể tải trang Thời Khóa Biểu Cá Nhân.\nChi tiết: " + e.getMessage());
        } catch (Exception e) { // Bắt các lỗi khác có thể xảy ra
            e.printStackTrace();
            showAlert("Lỗi Không Xác Định", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    @FXML
    private void handleTKBToCM(ActionEvent event) {
        if (currentGiaoVien == null) {
            showAlert("Lỗi", "Chưa có thông tin giáo viên để xem TKB cá nhân.");
            return;
        }
        if (mainBorderPane == null) {
            System.err.println("Lỗi: mainBorderPane chưa được thiết lập trong TimeTableController.");
            showAlert("Lỗi Hệ Thống", "Không thể thay đổi nội dung hiển thị.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/TKBTCM.fxml"));
            Parent tkbTCMRoot = loader.load();

            // Lấy controller
            TKBTCMController tkbTCM = loader.getController();
            tkbTCM.initData(currentGiaoVien);

            mainBorderPane.setCenter(tkbTCMRoot);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi Hệ Thống", "Không thể tải trang Thời Khóa Biểu Cá Nhân.\nChi tiết: " + e.getMessage());
        } catch (Exception e) { // Bắt các lỗi khác có thể xảy ra
            e.printStackTrace();
            showAlert("Lỗi Không Xác Định", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    @FXML
    private void handleTKBCaNhan(ActionEvent event) {
        if (currentGiaoVien == null) {
            showAlert("Lỗi", "Chưa có thông tin giáo viên để xem TKB cá nhân.");
            return;
        }
        if (mainBorderPane == null) {
            System.err.println("Lỗi: mainBorderPane chưa được thiết lập trong TimeTableController.");
            showAlert("Lỗi Hệ Thống", "Không thể thay đổi nội dung hiển thị.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/TKBCaNhan.fxml"));
            Parent tkbCaNhanRoot = loader.load();

            // Lấy controller
            TKBCaNhanController tkbCaNhanController = loader.getController();
            tkbCaNhanController.initData(currentGiaoVien);

            mainBorderPane.setCenter(tkbCaNhanRoot); // Đặt nội dung mới vào vùng center của BorderPane chính

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi Hệ Thống", "Không thể tải trang Thời Khóa Biểu Cá Nhân.\nChi tiết: " + e.getMessage());
        } catch (Exception e) { // Bắt các lỗi khác có thể xảy ra
            e.printStackTrace();
            showAlert("Lỗi Không Xác Định", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Thêm hàm showAlert cho lỗi nếu cần
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}