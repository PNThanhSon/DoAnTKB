package controllers;

import entities.ChucVu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import util.DatabaseConnection;

import java.sql.*;

public class QuanLyChucVuController {

    /* ======= FXML phần hiển thị ======= */
    @FXML private TableView<ChucVu>      tableChucVu;
    @FXML private TableColumn<ChucVu,String> colSTT;   // hiển thị chỉ số
    @FXML private TableColumn<ChucVu,String> colMaCV;
    @FXML private TableColumn<ChucVu,String> colTenCV;

    /* ======= FXML phần nhập liệu ======= */
    @FXML private TextField txtMaCV;
    @FXML private TextField txtTenCV;

    private final ObservableList<ChucVu> dsChucVu = FXCollections.observableArrayList();

    /* ========== initialize ========== */
    @FXML
    public void initialize() {
        // map cột với thuộc tính
        colMaCV.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMaCV()));
        colTenCV.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTenCV()));
        // cột STT = chỉ số hàng
        colSTT.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(tableChucVu.getItems().indexOf(c.getValue()) + 1)));

        tableChucVu.setItems(dsChucVu);
        loadData();          // nạp lúc mở form
    }

    /* ========== NẠP DỮ LIỆU ========== */
    private void loadData() {
        dsChucVu.clear();
        String sql = "SELECT MaCV, TenCV FROM CHUCVU ORDER BY MaCV";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st   = con.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            while (rs.next()) {
                dsChucVu.add(new ChucVu(rs.getString("MaCV"), rs.getString("TenCV")));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi DB", e.getMessage());
            e.printStackTrace();
        }
    }

    /* ========== XỬ LÝ NÚT THÊM ========== */
    @FXML
    private void handleThemChucVu() {
        String ma = txtMaCV.getText().trim();
        String ten = txtTenCV.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Vui lòng nhập đủ Mã & Tên chức vụ!");
            return;
        }

        String insert = "INSERT INTO CHUCVU (MaCV, TenCV) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(insert)) {

            ps.setString(1, ma);
            ps.setString(2, ten);
            ps.executeUpdate();

            // cập nhật TableView
            dsChucVu.add(new ChucVu(ma, ten));
            txtMaCV.clear(); txtTenCV.clear();

        } catch (SQLIntegrityConstraintViolationException dup) {
            showAlert(Alert.AlertType.ERROR, "Trùng mã", "Mã chức vụ đã tồn tại!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi DB", e.getMessage());
            e.printStackTrace();
        }
    }

    /* ========== tiện ích ========== */
    private void showAlert(Alert.AlertType type, String header, String msg){
        Alert a = new Alert(type); a.setHeaderText(header); a.setContentText(msg); a.showAndWait();
    }
}
