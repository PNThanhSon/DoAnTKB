// Giả sử các file nằm trực tiếp trong thư mục src (default package)

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert; // Import Alert

import java.io.IOException;
import java.net.URL;
// import java.sql.Connection; // Cho việc kiểm tra DB ban đầu (tùy chọn)
// import java.sql.SQLException; // Cho việc kiểm tra DB ban đầu (tùy chọn)

public class Main extends Application {

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

            primaryStage.setTitle("Đăng Nhập - Hệ Thống Quản Lý Giáo Viên");
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
        // Tùy chọn: Kiểm tra kết nối CSDL trước khi khởi chạy giao diện
        // Điều này có thể hữu ích để thông báo sớm nếu CSDL không khả dụng.
        // System.out.println("Kiểm tra kết nối CSDL trước khi khởi chạy UI...");
        // Connection connTest = DatabaseConnection.getConnection();
        // if (connTest == null) {
        //     System.err.println("KHÔNG THỂ KẾT NỐI CSDL. Ứng dụng có thể không hoạt động đúng.");
        //     // Bạn có thể hiển thị một Alert ở đây hoặc ghi log.
        //     // Việc quyết định có launch(args) hay không tùy thuộc vào yêu cầu.
        // } else {
        //     System.out.println("Kiểm tra kết nối CSDL thành công.");
        //     try { connTest.close(); } catch (SQLException e) { /* Bỏ qua lỗi đóng kết nối test */ }
        // }
        launch(args);
    }
}
