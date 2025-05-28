package controllers;

import entities.GiaoVien; // Cần GiaoVien để truyền thông tin Admin (nếu cần)
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

    @FXML
    private void handleXemTKBGiaoVien(ActionEvent event) {
    }

    @FXML
    private void handleXemTKBLop(ActionEvent event) {
    }

    @FXML
    private void handleXemTKBTCM(ActionEvent event) {
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
