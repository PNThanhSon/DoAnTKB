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

public class ThemMonHocController {

    @FXML private TextField txtMaMH;
    @FXML private TextField txtTenMH;
    @FXML private ComboBox<String> comboKhoi;
    @FXML private ComboBox<ToChuyenMon> comboToChuyenMon; // Use ToChuyenMon object

    @FXML private Text tbMaMH;
    @FXML private Text tbTenMH;
    @FXML private Text tbKhoi;
    @FXML private Text tbMaTCM;

    private Stage dialogStage;
    private Runnable onSuccess;
    private final MonHocDAO monHocDAO = new MonHocDAO();
    private final ToChuyenMonDAO toChuyenMonDAO = new ToChuyenMonDAO();

    @FXML
    public void initialize() {
        loadToChuyenMonData();
        comboKhoi.getSelectionModel().selectFirst(); // Select default Khoi
    }

    private void loadToChuyenMonData() {
        try {
            List<ToChuyenMon> tcmList = toChuyenMonDAO.getDanhSachTCM();
            comboToChuyenMon.setItems(FXCollections.observableArrayList(tcmList));
            if (!tcmList.isEmpty()) {
                comboToChuyenMon.getSelectionModel().selectFirst();
            }
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

    @FXML
    private void xuLyLuuMoi() {
        if (kiemTraHopLe()) {
            MonHoc newMonHoc = new MonHoc(txtMaMH.getText().trim(), txtTenMH.getText().trim(), comboKhoi.getValue(), null);
            ToChuyenMon selectedTCM = comboToChuyenMon.getValue();
            if (selectedTCM != null) {
                newMonHoc.setMaTCM(selectedTCM.getMaTCM());
            } else {
                // This case should ideally be prevented by validation
                tbMaTCM.setText("Tổ chuyên môn không được trống.");
                return;
            }


            try {
                // Check if MaMH already exists
                if (monHocDAO.getMonHocById(newMonHoc.getMaMH()) != null) {
                    tbMaMH.setText("Mã môn học đã tồn tại.");
                    return;
                }

                monHocDAO.themMonHoc(newMonHoc);
                if (onSuccess != null) {
                    onSuccess.run();
                }
                dialogStage.close();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể thêm môn học: " + e.getMessage());
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

        if (txtMaMH.getText() == null || txtMaMH.getText().trim().isEmpty()) {
            tbMaMH.setText("Mã môn học không được trống.");
            isValid = false;
        } else if (txtMaMH.getText().trim().length() > 10) {
            tbMaMH.setText("Mã MH tối đa 10 ký tự.");
            isValid = false;
        }


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
        tbMaMH.setText("");
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
