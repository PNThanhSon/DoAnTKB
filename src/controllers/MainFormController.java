package controllers;// Giả sử các file nằm trực tiếp trong thư mục src (default package)

import controllers.PhanHoiYKien.PhanHoiYKienController;
import controllers.QuanLyGiaoVien.QuanLyGiaoVienController;
import controllers.QuanLyTCM.QuanLyTCMController;
import controllers.XemTKB.TimeTableController;
import entities.GiaoVien;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font; // Cần import Font
import javafx.geometry.Insets; // Cần import Insets
import javafx.geometry.Pos;   // Cần import Pos
import javafx.stage.Stage;
import controllers.MonHoc.*; // Wildcard import for MonHoc controllers
import controllers.LopHoc.*; // Wildcard import for LopHoc controllers


import java.io.IOException;
import java.sql.SQLException;

public class MainFormController {
    @FXML
    public MenuItem menuDangXuat;

    @FXML
    public Menu menuQuanLyMain;
    @FXML
    public MenuItem menuXepTKBTuDong;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private VBox centerContentArea; // VBox ban đầu trong vùng center

    @FXML
    private Label welcomeLabel;

    @FXML
    private MenuItem menuQLGiaoVien;

    @FXML
    private MenuItem menuXemThongTinCaNhan;

    @FXML
    private MenuItem menuQuanLyMonHoc;

    @FXML
    private MenuItem menuQuanLyLopHoc;

    @FXML
    private MenuItem menuPhanHoiYKien;

    // THAY ĐỔI Ở ĐÂY: Khai báo MenuItem cho Trang chủ
    @FXML
    private MenuItem menuXemCacTKB;

    @FXML
    private MenuItem menuTrangChu;
    // Khai báo menu item cho tính năng quản lí chức vụ
    @FXML
    private MenuItem menuQLChucVu;


    @FXML
    private MenuItem menuBCTK;

    private GiaoVien loggedInGiaoVien;

    public void setLoggedInGiaoVien(GiaoVien giaoVien) {
        this.loggedInGiaoVien = giaoVien;
        updateWelcomeMessage();
        applyPermissions();
        // Khi MainForm được tải lần đầu, đảm bảo nội dung center là mặc định
        resetToDefaultCenterContent();
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
        if (loggedInGiaoVien != null && loggedInGiaoVien.isAdmin()) {
            menuQuanLyMain.setVisible(true);
            menuBCTK.setVisible(true);
            menuXepTKBTuDong.setVisible(true);
        } else {
            menuQuanLyMain.setVisible(false);
            menuBCTK.setVisible(false);
            menuXepTKBTuDong.setVisible(false);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/XemTKB/TimeTableForm.fxml"));
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
    private void handleQuanLyMonHoc(ActionEvent event) {
        // điều phối màn hình mới
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/MHform/QuanLyMonHocForm.fxml"));
            Parent QLMonHocRoot = loader.load();

            QuanLyMonHocController QLMonHocController = loader.getController(); // các bước load data đã có sẵn trong init của controller này

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(QLMonHocRoot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: mainBorderPane chưa được inject. Kiểm tra fx:id trong .fxml.");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị quản lý môn học trong trang chính.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải trang quản lý môn học.\nChi tiết: " + e.getMessage());
        }
    }

    @FXML
    private void handleQuanLyLopHoc(ActionEvent event) {
        // điều phối màn hình mới
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/LHform/QuanLyLopHocForm.fxml"));
            Parent QLLopHocRoot = loader.load(); // Sửa tên biến để tránh nhầm lẫn

            // Lấy controller của màn hình quản lý lớp học nếu cần
            QuanLyLopHocController qlLopHocController = loader.getController();

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(QLLopHocRoot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: mainBorderPane chưa được inject. Kiểm tra fx:id trong .fxml.");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị quản lý lớp học trong trang chính.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải trang quản lý lớp học.\nChi tiết: " + e.getMessage());
        }
    }
    @FXML
    private void handleQuanLyGiaoVien(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/QuanLyGiaoVien/QuanLyGiaoVienForm.fxml"));
            Parent QLGiaoVienRoot = loader.load();

            QuanLyGiaoVienController QLGiaoVienController = loader.getController(); // các bước load data đã có sẵn trong init của controller này

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(QLGiaoVienRoot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi:  chưa được inject. Kiểm tra fx:id trong .fxml.");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị quản lý giáo viên trong trang chính.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải trang quản lý giáo viên.\nChi tiết: " + e.getMessage());
        }
    }

    @FXML
    private void handlePhanHoiYKien(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/PhanHoiYKien/PhanHoiYKienForm.fxml"));
            Parent PhanHoiYKienRoot = loader.load();

            PhanHoiYKienController phanHoiYKienController = loader.getController();
            phanHoiYKienController.getGiaoVienData(loggedInGiaoVien);

            // Ẩn các chức năng không được quyền, bảo mật fontend
            if (!loggedInGiaoVien.isAdmin()) {
                phanHoiYKienController.ishideFuntion();
            }

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(PhanHoiYKienRoot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: chưa được inject. Kiểm tra fx:id ");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải phan hoi y kien.\nChi tiết: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleBCTK(ActionEvent event) {
        if (loggedInGiaoVien == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa Đăng Nhập", "Không có gì để hiển thị.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/BCTKForm.fxml"));
            Parent bctkRoot = loader.load();

            //BCTKController TTControl = loader.getController();

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(bctkRoot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: mainBorderPane chưa được inject. Kiểm tra fx:id trong MainForm.fxml.");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị TimeTableForm trong trang chính.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải trang TimeTableForm.\nChi tiết: " + e.getMessage());
        }
    }

    @FXML
    private void handleQuanLyChucVu(ActionEvent event) {
        loadViewIntoCenter("/form/QuanLiChucVuForm.fxml");
    }
    @FXML
    private void handleQuanLyTKB(ActionEvent event) {
        loadViewIntoCenter("/form/QuanLyTKBForm.fxml");
    }

    @FXML
    private void handleQuanLyHocKy(ActionEvent e) {
        loadViewIntoCenter("/form/QuanLyHocKyForm.fxml");
    }

    private void loadViewIntoCenter(String fxmlFileName) { // Hàm ví dụ để load một view form vào vùng center
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent viewRoot = loader.load();

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
    public void handleQuanLyTCM() {
        if (loggedInGiaoVien == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa Đăng Nhập", "Không có gì để hiển thị.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/ToChuyenMon/QLTCMForm.fxml"));
            Parent Root = loader.load();

            QuanLyTCMController qltcmControl = loader.getController();
            if (mainBorderPane != null) {
                mainBorderPane.setCenter(Root); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: mainBorderPane chưa được inject. Kiểm tra fx:id trong MainForm.fxml.");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị TimeTableForm trong trang chính.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải trang TimeTableForm.\nChi tiết: " + e.getMessage());
        }
    }

    @FXML
    public void handleXepTKB(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/XepTKBTuDong/ChuanBiForm.fxml"));
            Parent mainFormRoot = loader.load();

            // Tạo và cấu hình stage mới
            Stage mainStage = new Stage();
            mainStage.setTitle("Xếp TKB tự động - Chuẩn bị");
            mainStage.setScene(new Scene(mainFormRoot, 800, 600));
            mainStage.setMinWidth(700);
            mainStage.setMinHeight(800);

            // Thiết lập các thuộc tính bổ sung cho stage
            mainStage.setResizable(true);
            // Hiển thị stage
            mainStage.show();
            // Đưa window lên foreground
            mainStage.toFront();

        } catch (IOException e) {
            // Xử lý lỗi khi load FXML
            System.err.println("Lỗi khi load FXML file: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở cửa sổ xếp thời khóa biểu" + e.getMessage());
        } catch (Exception e) {
            // Xử lý các lỗi khác
            System.err.println("Lỗi không xác định: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi không xác định" + e.getMessage());
        }
    }
}