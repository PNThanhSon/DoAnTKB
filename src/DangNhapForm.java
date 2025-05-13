import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert; // Import Alert

import java.io.IOException;
import java.net.URL;

public class DangNhapForm extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlUrl = getClass().getResource("DangNhapForm.fxml");

            if (fxmlUrl == null) {
                System.err.println("LỖI NGHIÊM TRỌNG: Không tìm thấy file DangNhapForm.fxml.");
                showAlert(Alert.AlertType.ERROR, "Lỗi Khởi Tạo Ứng Dụng",
                        "Không thể tải giao diện đăng nhập (DangNhapForm.fxml không tìm thấy).\nVui lòng liên hệ bộ phận hỗ trợ.");
                return;
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root);

            primaryStage.setTitle("Đăng Nhập - Hệ Thống Quản Lý Thời Khóa Biểu Trường Học");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Lỗi IOException khi tải FXML: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Khởi Tạo Giao Diện",
                    "Có lỗi xảy ra khi tải giao diện người dùng.\nChi tiết: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi khởi chạy ứng dụng: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Không Xác Định",
                    "Đã có lỗi không mong muốn xảy ra trong quá trình khởi chạy.\nVui lòng thử lại hoặc liên hệ hỗ trợ.");
        }
    }

    // Phương thức tiện ích để hiển thị Alert từ phương thức start (nếu cần)
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
