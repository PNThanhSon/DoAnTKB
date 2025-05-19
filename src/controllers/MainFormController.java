package controllers;// Giả sử các file nằm trực tiếp trong thư mục src (default package)

import entities.GiaoVien;
import entities.VaiTroGV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font; // Cần import Font
import javafx.geometry.Insets; // Cần import Insets
import javafx.geometry.Pos;   // Cần import Pos
import javafx.stage.Stage;


import java.io.IOException;

public class MainFormController {
    @FXML
    public MenuItem menuDangXuat;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private VBox centerContentArea;

    @FXML
    private Label welcomeLabel;

    @FXML
    private MenuItem menuQLGiaoVien;

    @FXML
    private MenuItem menuXemThongTinCaNhan;

    @FXML
    private MenuItem menuXemCacTKB;

    @FXML
    private MenuItem menuTrangChu;

    private GiaoVien loggedInGiaoVien;

    public void setLoggedInGiaoVien(GiaoVien giaoVien) {
        this.loggedInGiaoVien = giaoVien;
        updateWelcomeMessage();
        applyPermissions();
        // Khi MainForm được tải lần đầu, đảm bảo nội dung center là mặc định
        resetToDefaultCenterContent();
        if (loggedInGiaoVien.getVaiTro() == VaiTroGV.ADMIN) {}
    }

    @FXML
    public void initialize() {
        if (loggedInGiaoVien == null) {
            welcomeLabel.setText("Xin chào! (Chưa có thông tin người dùng)");
        }
        // Đảm bảo centerContentArea được hiển thị khi khởi tạo (nếu nó không phải là null)
        if (mainBorderPane != null && centerContentArea != null && mainBorderPane.getCenter() == null) {
            mainBorderPane.setCenter(centerContentArea);
        }
    }

    private void updateWelcomeMessage() {
        if (loggedInGiaoVien != null) {
            if (loggedInGiaoVien.getGioiTinh().equals("Nam")) {
                welcomeLabel.setText("Xin chào! Thầy " +
                        (loggedInGiaoVien.getHoGV() != null ? loggedInGiaoVien.getHoGV() : "") + " " +
                        (loggedInGiaoVien.getTenGV() != null ? loggedInGiaoVien.getTenGV() : "") +
                        " (Mã GV: " + loggedInGiaoVien.getMaGV() + ")");
            } else if (loggedInGiaoVien.getGioiTinh().equals("Nữ")) {
                welcomeLabel.setText("Xin chào! Cô " +
                        (loggedInGiaoVien.getHoGV() != null ? loggedInGiaoVien.getHoGV() : "") + " " +
                        (loggedInGiaoVien.getTenGV() != null ? loggedInGiaoVien.getTenGV() : "") +
                        " (Mã GV: " + loggedInGiaoVien.getMaGV() + ")");
            } else {
                welcomeLabel.setText("Xin chào! Giáo viên " +
                        (loggedInGiaoVien.getHoGV() != null ? loggedInGiaoVien.getHoGV() : "") + " " +
                        (loggedInGiaoVien.getTenGV() != null ? loggedInGiaoVien.getTenGV() : "") +
                        " (Mã GV: " + loggedInGiaoVien.getMaGV() + ")");
            }
        }
    }

    private void applyPermissions() {
        if (loggedInGiaoVien != null && menuQLGiaoVien != null) {
            menuQLGiaoVien.setVisible("ADMIN".equalsIgnoreCase(loggedInGiaoVien.getMaGV()));
        } else if (menuQLGiaoVien != null) {
            menuQLGiaoVien.setVisible(false);
        }
    }

    @FXML
    private void handleTrangChu(ActionEvent event) { // Xử lí sự kiện khi nhấn vào menuItem Trang chủ
        resetToDefaultCenterContent();
    }

    @FXML
    private void handleXemThongTinCaNhan(ActionEvent event) {
        if (loggedInGiaoVien == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa Đăng Nhập", "Không có thông tin người dùng để hiển thị.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/ProfileForm.fxml"));
            Parent profileRoot = loader.load();

            ProfileController profileController = loader.getController();
            profileController.setGiaoVienData(loggedInGiaoVien);

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(profileRoot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: mainBorderPane chưa được inject. Kiểm tra fx:id trong MainForm.fxml.");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị thông tin cá nhân trong trang chính.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải trang thông tin cá nhân.\nChi tiết: " + e.getMessage());
        }
    }

    @FXML
    private void handleXemCacTKB(ActionEvent event) {
        if (loggedInGiaoVien == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa Đăng Nhập", "Không có gì để hiển thị.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/TimeTableForm.fxml"));
            Parent profileRoot = loader.load();

            TimeTableController TTControl = loader.getController();
            TTControl.initData(loggedInGiaoVien, mainBorderPane);

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(profileRoot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: mainBorderPane chưa được inject. Kiểm tra fx:id trong MainForm.fxml.");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị TimeTableForm trong trang chính.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải trang TimeTableForm.\nChi tiết: " + e.getMessage());
        }
    }

    /**
     * Đặt lại nội dung vùng center về giao diện chào mừng mặc định.
     * Giao diện này được định nghĩa bởi VBox có fx:id="centerContentArea" trong MainForm.fxml.
     * Nếu centerContentArea không được tìm thấy, sẽ tạo một Label chào mừng đơn giản.
     */
    private void resetToDefaultCenterContent() {
        if (mainBorderPane != null) {
            if (centerContentArea != null) {
                mainBorderPane.setCenter(centerContentArea);
            } else {
                // Fallback nếu centerContentArea không được inject (ví dụ: fx:id bị thiếu trong FXML)
                // Hoặc nếu bạn muốn tạo nội dung mặc định động
                System.out.println("Thông báo: centerContentArea không được inject, tạo nội dung mặc định.");
                Label defaultLabel = new Label("Chào mừng đến với hệ thống!");
                defaultLabel.setFont(new Font("System", 18)); // Sử dụng tên font hợp lệ
                Label subLabel = new Label("Sử dụng menu để điều hướng các chức năng.");
                subLabel.setFont(new Font("System", 14));

                VBox defaultVBox = new VBox(20, defaultLabel, subLabel); // Khoảng cách giữa các label là 20
                defaultVBox.setAlignment(Pos.CENTER); // Căn giữa nội dung của VBox
                defaultVBox.setPadding(new Insets(30)); // Thêm padding
                mainBorderPane.setCenter(defaultVBox);
            }
        } else {
            System.err.println("Lỗi: mainBorderPane chưa được inject trong resetToDefaultCenterContent.");
        }
    }


    @FXML
    private void handleDangXuat(ActionEvent event) {
        Stage currentStage = (Stage) mainBorderPane.getScene().getWindow();
        if (currentStage != null) {
            currentStage.close();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/DangNhapForm.fxml"));
            Parent loginRoot = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Đăng Nhập Hệ Thống");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setResizable(false);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể quay lại trang đăng nhập.");
        }
    }

    @FXML
    private void handleQuanLyGiaoVien(ActionEvent event) {
        if (loggedInGiaoVien != null && "ADMIN".equalsIgnoreCase(loggedInGiaoVien.getMaGV())) {
            // Ví dụ: loadViewIntoCenter("QuanLyGiaoVienForm.fxml");
            showAlert(Alert.AlertType.INFORMATION, "Chức Năng", "Mở /form/chức năng Quản lý Giáo Viên.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Từ Chối Truy Cập", "Bạn không có quyền truy cập chức năng này.");
        }
    }

    private void loadViewIntoCenter(String fxmlFileName) { // Hàm ví dụ để load một view form vào vùng center
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent viewRoot = loader.load();

            // Object controller = loader.getController();
            // if (controller instanceof SomeFeatureController && loggedInGiaoVien != null) {
            //    ((SomeFeatureController) controller).initData(loggedInGiaoVien); // Ví dụ truyền dữ liệu
            // }

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(viewRoot);
            } else {
                System.err.println("Lỗi: mainBorderPane chưa được inject.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Tải Giao Diện", "Không thể tải: " + fxmlFileName);
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
