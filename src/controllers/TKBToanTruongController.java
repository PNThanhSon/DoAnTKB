package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane; // Để truyền mainBorderPane

import java.io.IOException;

public class TKBToanTruongController {
    @FXML private Button btnXemTKBGiaoVien; // Đổi tên fx:id cho rõ ràng
    @FXML private Button btnXemTKBLop;
    @FXML private Button btnXemTKBTCM;

    private BorderPane mainBorderPane; // Để load view vào


    public void initData(BorderPane mainPane) {
        this.mainBorderPane = mainPane;
    }

    @FXML
    private void handleXemTKBGiaoVien(ActionEvent event) {
        if (mainBorderPane == null) {
            System.err.println("Lỗi: mainBorderPane chưa được thiết lập trong TimeTableController.");
            showAlert("Lỗi Hệ Thống", "Không thể thay đổi nội dung hiển thị.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/TKBCaNhan.fxml"));
            Parent tkbCaNhanRoot = loader.load();

            TKBCaNhanController tkbGV = loader.getController();
            tkbGV.initDataAdmin();

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
    private void handleXemTKBLop(ActionEvent event) {
        if (mainBorderPane == null) {
            System.err.println("Lỗi: mainBorderPane chưa được thiết lập trong TimeTableController.");
            showAlert("Lỗi Hệ Thống", "Không thể thay đổi nội dung hiển thị.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/TKBLopCN.fxml"));
            Parent tkbLopRoot = loader.load();

            TKBLopCNController tkbLop = loader.getController();
            tkbLop.initDataAdmin();

            mainBorderPane.setCenter(tkbLopRoot); // Đặt nội dung mới vào vùng center của BorderPane chính

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi Hệ Thống", "Không thể tải trang Thời Khóa Biểu Cá Nhân.\nChi tiết: " + e.getMessage());
        } catch (Exception e) { // Bắt các lỗi khác có thể xảy ra
            e.printStackTrace();
            showAlert("Lỗi Không Xác Định", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    @FXML
    private void handleXemTKBTCM(ActionEvent event) {
        if (mainBorderPane == null) {
            System.err.println("Lỗi: mainBorderPane chưa được thiết lập trong TimeTableController.");
            showAlert("Lỗi Hệ Thống", "Không thể thay đổi nội dung hiển thị.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/TKBTCM.fxml"));
            Parent tkbTCMRoot = loader.load();

            TKBTCMController tkbTo = loader.getController();
            tkbTo.initDataAdmin();

            mainBorderPane.setCenter(tkbTCMRoot); // Đặt nội dung mới vào vùng center của BorderPane chính

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi Hệ Thống", "Không thể tải trang Thời Khóa Biểu Cá Nhân.\nChi tiết: " + e.getMessage());
        } catch (Exception e) { // Bắt các lỗi khác có thể xảy ra
            e.printStackTrace();
            showAlert("Lỗi Không Xác Định", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
