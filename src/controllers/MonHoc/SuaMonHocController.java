package controllers.MonHoc;

import dao.MonHocDAO;
import dao.ToChuyenMonDAO;
import entities.MonHoc;
import entities.ToChuyenMon;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class SuaMonHocController {

    @FXML private Text txtMaMH; // Display only, not editable
    @FXML private TextField txtTenMH;
    @FXML private ComboBox<String> comboKhoi;
    @FXML private ComboBox<ToChuyenMon> comboToChuyenMon; // Use ToChuyenMon object

    @FXML private Text tbTenMH;
    @FXML private Text tbKhoi;
    @FXML private Text tbMaTCM;

    private Stage dialogStage;
    private Runnable onSuccess;
    private MonHoc monHocToEdit;
    private final MonHocDAO monHocDAO = new MonHocDAO();
    private final ToChuyenMonDAO toChuyenMonDAO = new ToChuyenMonDAO();

    @FXML
    public void initialize() {
        loadToChuyenMonData();
    }

    private void loadToChuyenMonData() {
        try {
            List<ToChuyenMon> tcmList = toChuyenMonDAO.getDanhSachTCM();
            comboToChuyenMon.setItems(FXCollections.observableArrayList(tcmList));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách tổ chuyên môn: " + e.getMessage());
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void khoiTaoDuLieuSua(MonHoc monHoc) {
        this.monHocToEdit = monHoc;
        populateForm();
    }

    private void populateForm() {
        if (monHocToEdit != null) {
            txtMaMH.setText(monHocToEdit.getMaMH());
            txtTenMH.setText(monHocToEdit.getTenMH());
            comboKhoi.setValue(monHocToEdit.getKhoi());

            // Select the correct ToChuyenMon in the ComboBox
            String maTCMToSelect = monHocToEdit.getMaTCM();
            if (maTCMToSelect != null) {
                for (ToChuyenMon tcm : comboToChuyenMon.getItems()) {
                    if (maTCMToSelect.equals(tcm.getMaTCM())) {
                        comboToChuyenMon.setValue(tcm);
                        break;
                    }
                }
            }
        }
    }

    @FXML
    private void xuLyLuuThayDoi() {
        if (kiemTraHopLe()) {
            monHocToEdit.setTenMH(txtTenMH.getText().trim());
            monHocToEdit.setKhoi(comboKhoi.getValue());
            ToChuyenMon selectedTCM = comboToChuyenMon.getValue();
            if (selectedTCM != null) {
                monHocToEdit.setMaTCM(selectedTCM.getMaTCM());
            } else {
                tbMaTCM.setText("Tổ chuyên môn không được trống.");
                return;
            }

            try {
                monHocDAO.capNhatMonHoc(monHocToEdit);
                if (onSuccess != null) {
                    onSuccess.run();
                }
                dialogStage.close();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể cập nhật môn học: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void xuLyHuy() {
        dialogStage.close();
    }

    private boolean kiemTraHopLe() {
        resetErrorMessages();
        boolean isValid = true;

        if (txtTenMH.getText() == null || txtTenMH.getText().trim().isEmpty()) {
            tbTenMH.setText("Tên môn học không được trống.");
            isValid = false;
        } else if (txtTenMH.getText().trim().length() > 100) {
            tbTenMH.setText("Tên MH tối đa 100 ký tự.");
            isValid = false;
        }


        if (comboKhoi.getValue() == null || comboKhoi.getValue().isEmpty()) {
            tbKhoi.setText("Vui lòng chọn khối.");
            isValid = false;
        }

        if (comboToChuyenMon.getValue() == null) {
            tbMaTCM.setText("Vui lòng chọn tổ chuyên môn.");
            isValid = false;
        }
        return isValid;
    }

    private void resetErrorMessages() {
        tbTenMH.setText("");
        tbKhoi.setText("");
        tbMaTCM.setText("");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
