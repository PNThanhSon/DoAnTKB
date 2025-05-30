package controllers.PhanHoiYKien;

import dao.YKienDAO;
import entities.YKien;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

public class SuaYKienController {

    @FXML
    private Text txtNguoiYKien;

    @FXML
    private TextArea txtNoiDung;

    @FXML
    private CheckBox chkAnDanh;

    @FXML
    private Button btnLuu;



    private Stage dialogStage;
    private Runnable onSuccess;
    private YKien yKien;

    private final YKienDAO yKienDAO = new YKienDAO();

    // điền dữ liệu hiện tại vào form sửa
    public void getYKientoForm(YKien yKien) {
        this.yKien = yKien;
        txtNoiDung.setText(yKien.getNoiDung());
        txtNguoiYKien.setText(yKien.getMaGV());
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }
    // để callback
    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    @FXML
    private void handleLuu() throws SQLException {
        // Cập nhật thông tin ý kiến
        yKien.setNoiDung(txtNoiDung.getText());
        yKien.setAnDanh(chkAnDanh.isSelected());

        yKienDAO.suaYKien(yKien);

        if (onSuccess != null) {
            dialogStage.close();
            onSuccess.run();
        }
    }

//    @FXML
//    private void handleHuy() {
//        dialogStage.close();
//    }
}
