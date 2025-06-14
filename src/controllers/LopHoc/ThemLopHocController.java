package controllers.LopHoc;

import dao.GiaoVienDAO;
import dao.LopHocDAO;
import entities.GiaoVien;
import entities.Lop;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.sql.SQLException;
import java.util.List;

public class ThemLopHocController {

    @FXML private TextField txtMaLop;
    @FXML private TextField txtTenLop;
    @FXML private ComboBox<String> comboKhoi;
    @FXML private ComboBox<GiaoVien> comboGVCN;

    @FXML private Text tbMaLop;
    @FXML private Text tbTenLop;
    @FXML private Text tbKhoi;
    @FXML private Text tbGVCN;

    private Stage dialogStage;
    private Runnable onSuccess; // Callback khi thêm thành công
    private final LopHocDAO lopHocDAO = new LopHocDAO();
    private final GiaoVienDAO giaoVienDAO = new GiaoVienDAO(); // Sử dụng GiaoVienDAO bạn cung cấp

    @FXML
    public void initialize() {
        loadGiaoVienData(); // Tải danh sách giáo viên cho ComboBox
        comboKhoi.getSelectionModel().selectFirst(); // Chọn giá trị mặc định cho Khối

        // Custom display cho ComboBox Giáo viên
        comboGVCN.setConverter(new StringConverter<>() {
            @Override
            public String toString(GiaoVien gv) {
                // Hiển thị "Họ Tên (Mã GV)" hoặc null nếu gv là null
                return gv == null ? null : (gv.getHoGV() + " " + gv.getTenGV() + " (" + gv.getMaGV() + ")");
            }

            @Override
            public GiaoVien fromString(String string) {
                // Không cần cho ComboBox không cho phép nhập tự do
                return null;
            }
        });
        comboGVCN.setPromptText("Chọn giáo viên chủ nhiệm"); // Gợi ý cho người dùng
    }

    private void loadGiaoVienData() {
        try {
            // Lấy tất cả giáo viên từ DAO
            List<GiaoVien> gvList = giaoVienDAO.TracuuGiaoVien("");
            comboGVCN.setItems(FXCollections.observableArrayList(gvList));
            // Không chọn mặc định, để người dùng tự chọn hoặc để trống nếu GVCN có thể null
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách giáo viên: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    @FXML
    private void xuLyLuuMoi() {
        if (kiemTraHopLe()) { // Kiểm tra dữ liệu nhập
            Lop newLopHoc = new Lop(txtMaLop.getText().trim().toUpperCase(), txtTenLop.getText().trim(), comboKhoi.getValue(), null);

            GiaoVien selectedGVCN = comboGVCN.getValue();
            if (selectedGVCN != null) {
                newLopHoc.setGvcn(selectedGVCN.getMaGV());
            } else {
                newLopHoc.setGvcn(null); // Cho phép MaGVCN là null nếu CSDL thiết kế vậy
            }

            try {
                // Kiểm tra xem Mã lớp đã tồn tại chưa
                if (lopHocDAO.getLopHocById(newLopHoc.getMaLop()) != null) {
                    tbMaLop.setText("Mã lớp học này đã tồn tại.");
                    return; // Dừng lại nếu mã lớp trùng
                }

                lopHocDAO.themLopHoc(newLopHoc); // Gọi DAO để thêm vào CSDL
                if (onSuccess != null) {
                    onSuccess.run(); // Thực thi callback nếu có
                }
                dialogStage.close(); // Đóng dialog sau khi thêm thành công
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể thêm lớp học: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void xuLyHuy() {
        dialogStage.close(); // Đóng dialog khi hủy
    }

    private boolean kiemTraHopLe() {
        resetErrorMessages(); // Xóa các thông báo lỗi cũ
        boolean isValid = true;

        // Kiểm tra Mã lớp
        if (txtMaLop.getText() == null || txtMaLop.getText().trim().isEmpty()) {
            tbMaLop.setText("Mã lớp không được để trống.");
            isValid = false;
        } else if (txtMaLop.getText().trim().length() > 10) { // Theo CSDL VARCHAR2(10 BYTE)
            tbMaLop.setText("Mã lớp tối đa 10 ký tự.");
            isValid = false;
        }

        // Kiểm tra Tên lớp
        if (txtTenLop.getText() == null || txtTenLop.getText().trim().isEmpty()) {
            tbTenLop.setText("Tên lớp không được để trống.");
            isValid = false;
        } else if (txtTenLop.getText().trim().length() > 50) { // Theo CSDL VARCHAR2(50 CHAR)
            tbTenLop.setText("Tên lớp tối đa 50 ký tự.");
            isValid = false;
        }

        // Kiểm tra Khối
        if (comboKhoi.getValue() == null || comboKhoi.getValue().isEmpty()) {
            tbKhoi.setText("Vui lòng chọn khối cho lớp.");
            isValid = false;
        }

        // Kiểm tra GVCN (tùy chọn, nếu GVCN là bắt buộc thì bỏ comment)
        // if (comboGVCN.getValue() == null) {
        //     tbGVCN.setText("Vui lòng chọn giáo viên chủ nhiệm.");
        //     isValid = false;
        // }

        return isValid;
    }

    // Đặt lại các Text thông báo lỗi
    private void resetErrorMessages() {
        tbMaLop.setText("");
        tbTenLop.setText("");
        tbKhoi.setText("");
        tbGVCN.setText("");
    }

    // Hiển thị Alert dialog
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
