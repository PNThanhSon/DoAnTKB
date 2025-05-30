package controllers.QuanLyGiaoVien;


import dao.GiaoVienDAO;
import dao.ToChuyenMonDAO;
import entities.GiaoVien;
import entities.ToChuyenMon;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SuaThongtinGVController {

    @FXML
    private Text txtMaGV;

    @FXML private TextField txtHoGV, txtTenGV, txtChuyenMon;
    @FXML private TextField txtSoTietQD, txtSoTietTH, txtSoTietDT, txtEmail, txtSDT;
    @FXML private PasswordField txtMatKhau;
    @FXML private ComboBox<String> comboToChuyenMon, comboGioiTinh;
    @FXML private TextArea txtGhiChu;
    @FXML private Text tbHoGV, tbTenGV, tbGioiTinh, tbChuyenMon, tbMaTCM;
    @FXML private Text tbSoTietQD, tbSoTietTH, tbSoTietDT, tbEmail, tbSDT, tbMatKhau, tbGhiChu;

    private Stage dialogStage;

    private Runnable onSuccess;

    private GiaoVien giaoVien;

    private GiaoVienDAO giaoVienDAO = new GiaoVienDAO();

    private ToChuyenMonDAO tcmDAO = new ToChuyenMonDAO();

    private List<ToChuyenMon> danhSachTCM = new ArrayList<>();


    public void initialize() throws SQLException {
        //
        danhSachTCM.clear();
        danhSachTCM = tcmDAO.getDanhSachTCM();
        for (ToChuyenMon tcm : danhSachTCM) {
            comboToChuyenMon.getItems().add(tcm.getTenTCM());
        }
        comboToChuyenMon.getSelectionModel().selectFirst(); // chọn dòng đầu
        //
        comboGioiTinh.getItems().addAll("Nam", "Nữ","Khác");
        comboGioiTinh.getSelectionModel().selectFirst();
    }
    // điền dữ liệu hiện tại vào form sửa
    public void getGiaoVien(GiaoVien gv) {
        this.giaoVien = gv;
        txtMaGV.setText(gv.getMaGV());
        txtHoGV.setText(gv.getHoGV());
        txtTenGV.setText(gv.getTenGV());
        comboGioiTinh.setValue(gv.getGioiTinh());
        txtChuyenMon.setText(gv.getChuyenMon());
        for (ToChuyenMon tcm : danhSachTCM) {
            if (tcm.getMaTCM().equals(gv.getMaTCM())) {
                comboToChuyenMon.setValue(tcm.getTenTCM());
            }
        }
        txtSoTietQD.setText(String.valueOf(gv.getSoTietQuyDinh()));
        txtSoTietTH.setText(String.valueOf(gv.getSoTietThucHien()));
        txtSoTietDT.setText(String.valueOf(gv.getSoTietDuThieu()));
        txtEmail.setText(gv.getEmail());
        txtSDT.setText(gv.getSdt());
        txtMatKhau.setText(gv.getMatKhau());
        txtGhiChu.setText(gv.getGhiChu());
    }

    @FXML
    private void handleLuu() throws SQLException {

        if (!validateInputs()) {
            return;
        }
        // cập nhật lại thông tin
        giaoVien.setHoGV(txtHoGV.getText());
        giaoVien.setTenGV(txtTenGV.getText());
        giaoVien.setGioiTinh(comboGioiTinh.getValue());
        giaoVien.setChuyenMon(txtChuyenMon.getText());
        for (ToChuyenMon tcm : danhSachTCM) {
            if (tcm.getTenTCM().equals(comboToChuyenMon.getValue())) {
                giaoVien.setMaTCM(tcm.getMaTCM());
            }
        }
        giaoVien.setSoTietQuyDinh(Integer.valueOf(txtSoTietQD.getText()));
        giaoVien.setSoTietThucHien(Integer.valueOf(txtSoTietTH.getText()));
        giaoVien.setSoTietDuThieu(Integer.valueOf(txtSoTietDT.getText()));
        giaoVien.setEmail(txtEmail.getText());
        giaoVien.setSdt(txtSDT.getText());
        giaoVien.setMatKhau(txtMatKhau.getText());
        giaoVien.setGhiChu(txtGhiChu.getText());

        // gọi service hoặc DAO để lưu vào database nếu cần
        giaoVienDAO.capNhatThongTin(giaoVien);

        // gọi callback về controller gốc
        if (onSuccess != null) {
            dialogStage.close();
            onSuccess.run();
        }
        // đóng cửa sổ

    }

    @FXML
    private void handleHuy() {
        dialogStage.close();
    }


    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    // để callback
    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }


    public boolean validateInputs() {
        boolean isValid = true;

        // Reset lỗi
        tbHoGV.setText("");
        tbTenGV.setText("");
        tbGioiTinh.setText("");
        tbChuyenMon.setText("");
        tbMaTCM.setText("");
        tbSoTietQD.setText("");
        tbSoTietTH.setText("");
        tbSoTietDT.setText("");
        tbEmail.setText("");
        tbSDT.setText("");
        tbMatKhau.setText("");
        tbGhiChu.setText("");

        isValid = true;

        if (isEmptyField(txtHoGV, tbHoGV, "Không được để trống")) isValid = false;
        if (isEmptyField(txtTenGV, tbTenGV, "Không được để trống")) isValid = false;
        if (isEmptyField(txtChuyenMon, tbChuyenMon, "Không được để trống")) isValid = false;

        if (!isIntegerField(txtSoTietQD, tbSoTietQD, "Không được để trống", "Phải là số")) isValid = false;
        if (!isIntegerField(txtSoTietTH, tbSoTietTH, "Không được để trống", "Phải là số")) isValid = false;
        if (!isIntegerField(txtSoTietDT, tbSoTietDT, "Không được để trống", "Phải là số")) isValid = false;

        if (!isMinos(txtSoTietTH, tbSoTietTH, "Không được âm")) isValid = false;
        if (!isMinos(txtSoTietQD, tbSoTietQD, "Không được âm")) isValid = false;

        if (isEmptyField(txtEmail, tbEmail, "Không được để trống")) {
            isValid = false;
        } else {
            String email = txtEmail.getText().trim();
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                tbEmail.setText("Email không hợp lệ");
                isValid = false;
            }
        }

        if (isEmptyField(txtSDT, tbSDT, "Không được để trống")) {
            isValid = false;
        } else {
            String sdt = txtSDT.getText().trim();
            if (!sdt.matches("\\d{9,11}")) {
                tbSDT.setText("SĐT phải là số và từ 9-11 chữ số");
                isValid = false;
            }
        }

        if (isEmptyField(txtMatKhau, tbMatKhau, "Không được để trống")) isValid = false;
//        if (isEmptyField(txtGhiChu, tbGhiChu, "Không được để trống")) isValid = false;



        return isValid;
    }

    private boolean isEmptyField(TextField field, Text errorLabel, String errorMessage) {
        if (field.getText() == null || field.getText().trim().isEmpty()) {
            errorLabel.setText(errorMessage);
            return true;
        }
        return false;
    }
    private boolean isMinos(TextField field, Text errorLabel, String errorMessage) {
        if (field.getText().contains("-")) {
            errorLabel.setText(errorMessage);
            return true;
        }
        return false;
    }

    private boolean isIntegerField(TextField field, Text errorLabel, String emptyMsg, String invalidMsg) {
        String text = field.getText();
        if (text == null || text.trim().isEmpty()) {
            errorLabel.setText(emptyMsg);
            return false;
        }
        try {
            Integer.parseInt(text.trim());
            return true;
        } catch (NumberFormatException e) {
            errorLabel.setText(invalidMsg);
            return false;
        }
    }
}
