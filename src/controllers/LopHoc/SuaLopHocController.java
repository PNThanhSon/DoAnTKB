package controllers.LopHoc;

import DAO.GiaoVienDAO;
import DAO.LopHocDAO;
import entities.GiaoVien;
import entities.LopHoc;
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

public class SuaLopHocController {

    @FXML private Text txtMaLop; // Hiển thị Mã lớp, không cho sửa
    @FXML private TextField txtTenLop;
    @FXML private ComboBox<String> comboKhoi;
    @FXML private ComboBox<GiaoVien> comboGVCN;

    @FXML private Text tbTenLop;
    @FXML private Text tbKhoi;
    @FXML private Text tbGVCN;

    private Stage dialogStage;
    private Runnable onSuccess; // Callback khi sửa thành công
    private LopHoc lopHocToEdit; // Đối tượng Lớp học đang được sửa
    private final LopHocDAO lopHocDAO = new LopHocDAO();
    private final GiaoVienDAO giaoVienDAO = new GiaoVienDAO(); // Sử dụng GiaoVienDAO bạn cung cấp

    @FXML
    public void initialize() {
        loadGiaoVienData(); // Tải danh sách giáo viên cho ComboBox

        // Custom display cho ComboBox Giáo viên
        comboGVCN.setConverter(new StringConverter<GiaoVien>() {
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

    // Nhận đối tượng LopHoc cần sửa và điền thông tin vào form
    public void setLopHocToEdit(LopHoc lopHoc) {
        this.lopHocToEdit = lopHoc;
        populateForm();
    }

    // Điền thông tin của lopHocToEdit vào các trường trên form
    private void populateForm() {
        if (lopHocToEdit != null) {
            txtMaLop.setText(lopHocToEdit.getMaLop());
            txtTenLop.setText(lopHocToEdit.getTenLop());
            comboKhoi.setValue(lopHocToEdit.getKhoi());

            // Chọn giáo viên chủ nhiệm hiện tại trong ComboBox
            String maGVCNToSelect = lopHocToEdit.getMaGVCN();
            if (maGVCNToSelect != null) {
                GiaoVien gvcnSelected = null;
                // Tìm đối tượng GiaoVien trong danh sách của ComboBox có MaGV khớp
                for (GiaoVien gv : comboGVCN.getItems()) {
                    if (maGVCNToSelect.equals(gv.getMaGV())) {
                        gvcnSelected = gv;
                        break;
                    }
                }
                comboGVCN.setValue(gvcnSelected); // Đặt giáo viên tìm được làm giá trị được chọn
            } else {
                comboGVCN.getSelectionModel().clearSelection(); // Nếu không có GVCN, xóa lựa chọn
            }
        }
    }

    @FXML
    private void handleLuu() {
        if (validateInputs()) { // Kiểm tra dữ liệu nhập
            // Cập nhật thông tin cho đối tượng lopHocToEdit từ form
            lopHocToEdit.setTenLop(txtTenLop.getText().trim());
            lopHocToEdit.setKhoi(comboKhoi.getValue());

            GiaoVien selectedGVCN = comboGVCN.getValue();
            if (selectedGVCN != null) {
                lopHocToEdit.setMaGVCN(selectedGVCN.getMaGV());
            } else {
                lopHocToEdit.setMaGVCN(null); // Cho phép MaGVCN là null nếu CSDL cho phép
            }

            try {
                lopHocDAO.capNhatLopHoc(lopHocToEdit); // Gọi DAO để cập nhật CSDL
                if (onSuccess != null) {
                    onSuccess.run(); // Thực thi callback nếu có
                }
                dialogStage.close(); // Đóng dialog sau khi cập nhật thành công
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể cập nhật lớp học: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleHuy() {
        dialogStage.close(); // Đóng dialog khi hủy
    }

    private boolean validateInputs() {
        resetErrorMessages(); // Xóa các thông báo lỗi cũ
        boolean isValid = true;

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

        // Kiểm tra GVCN (tùy chọn)
        // if (comboGVCN.getValue() == null) {
        //     tbGVCN.setText("Vui lòng chọn giáo viên chủ nhiệm.");
        //     isValid = false;
        // }
        return isValid;
    }

    // Đặt lại các Text thông báo lỗi
    private void resetErrorMessages() {
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
