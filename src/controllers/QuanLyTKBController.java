package controllers;

import entities.ThoiKhoaBieu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty; // Để hiển thị giá trị rỗng cho cột không dùng
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat; // Đã sửa lỗi cú pháp

/**
 * Quản lý Thời khóa biểu – đã sửa lỗi SimpleDateFormat và xử lý FXML có trường không khớp Entity.
 */
public class QuanLyTKBController {

    private static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    private final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN); // Sửa lỗi SimpleDateFormat_

    /* ==== FXML binding (bao gồm cả các trường không có trong entity để FXML không lỗi) ==== */
    @FXML private TableView<ThoiKhoaBieu> tableTKB;
    @FXML private TableColumn<ThoiKhoaBieu, String> colMaTKB;
    @FXML private TableColumn<ThoiKhoaBieu, Date> colNgayAD;
    @FXML private TableColumn<ThoiKhoaBieu, String> colBuoi;
    @FXML private TableColumn<ThoiKhoaBieu, String> colMaHK;

    // Các trường này có trong FXML nhưng không có trong Entity ThoiKhoaBieu.java
    // Chúng sẽ được khai báo để FXML load được, nhưng sẽ không có chức năng đầy đủ.
    @FXML private TableColumn<ThoiKhoaBieu, String> colNgayLap;
    @FXML private TableColumn<ThoiKhoaBieu, String> colNguoiTao;

    @FXML private TextField txtMaTKB;
    @FXML private TextField txtNgayAD;
    @FXML private TextField txtBuoi;
    @FXML private TextField txtMaHK;
    @FXML private TextField txtSearch;

    // Các trường này có trong FXML nhưng không có trong Entity ThoiKhoaBieu.java
    @FXML private TextField txtNgayLap;
    @FXML private TextField txtNguoiTao;

    private final ObservableList<ThoiKhoaBieu> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colMaTKB.setCellValueFactory(new PropertyValueFactory<>("maTKB"));
        colNgayAD.setCellValueFactory(new PropertyValueFactory<>("ngayApDung"));
        colBuoi.setCellValueFactory(new PropertyValueFactory<>("buoi"));
        colMaHK.setCellValueFactory(new PropertyValueFactory<>("maHK"));

        // Định dạng hiển thị cho cột Ngày Áp Dụng
        colNgayAD.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(sdf.format(item)); // sdf đã được sửa
                }
            }
        });

        // Xử lý các cột không có trong entity ThoiKhoaBieu
        colNgayLap.setCellValueFactory(cellData -> new SimpleStringProperty("")); // Luôn hiển thị rỗng
        colNguoiTao.setCellValueFactory(cellData -> new SimpleStringProperty("")); // Luôn hiển thị rỗng


        tableTKB.setItems(data);

        tableTKB.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, sel) -> {
                    if (sel != null) {
                        txtMaTKB.setText(sel.getMaTKB());
                        txtMaTKB.setDisable(true);

                        if (sel.getNgayApDung() != null) {
                            txtNgayAD.setText(sdf.format(sel.getNgayApDung())); // sdf đã được sửa
                        } else {
                            txtNgayAD.clear();
                        }
                        txtBuoi.setText(sel.getBuoi());
                        txtMaHK.setText(sel.getMaHK());

                        // Xử lý các trường không có trong entity
                        txtNgayLap.clear();
                        txtNgayLap.setDisable(true); // Vô hiệu hóa vì không dùng
                        txtNguoiTao.clear();
                        txtNguoiTao.setDisable(true); // Vô hiệu hóa vì không dùng

                    } else {
                        clearForm();
                    }
                });

        loadData();
    }

    @FXML
    public void loadData() {
        data.clear();
        String sql = "SELECT MaTKB, NgayApDung, Buoi, MaHK FROM THOIKHOABIEU ORDER BY MaTKB";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(new ThoiKhoaBieu(
                        rs.getString("MaTKB"),
                        rs.getDate("NgayApDung"),
                        rs.getString("Buoi"),
                        rs.getString("MaHK")));
            }
        } catch (SQLException ex) {
            showError("Lỗi tải dữ liệu", ex);
        }
    }

    @FXML
    private void handleAdd() {
        if (txtMaTKB.isDisabled()) {
            showError("Vui lòng làm mới form (Nhấn nút 'Làm mới') trước khi thêm TKB mới.", null);
            return;
        }
        if (validateInput(true)) {
            return;
        }

        if (confirm("Thêm TKB mới?")) return;

        String sql = "INSERT INTO THOIKHOABIEU (MaTKB, NgayApDung, Buoi, MaHK) VALUES (?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Date ngayAD = parseDateFromString(txtNgayAD.getText().trim(), "Ngày Áp Dụng");
            if (ngayAD == null && !txtNgayAD.getText().trim().isEmpty()) return;

            ps.setString(1, txtMaTKB.getText().trim());
            if (ngayAD != null) {
                ps.setDate(2, new java.sql.Date(ngayAD.getTime()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }
            ps.setString(3, txtBuoi.getText().trim());
            ps.setString(4, txtMaHK.getText().trim());
            // Dữ liệu từ txtNgayLap và txtNguoiTao sẽ bị bỏ qua vì không có trong entity/DB table

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                loadData();
                clearForm();
                showInfo("Đã thêm thành công!");
            } else {
                showError("Thêm thất bại, không có bản ghi nào được thêm.", null);
            }
        } catch (SQLException ex) {
            showError("Không thể thêm! Có thể MaTKB đã tồn tại hoặc dữ liệu không hợp lệ.", ex);
        }
    }

    @FXML
    private void handleUpdate() {
        ThoiKhoaBieu sel = tableTKB.getSelectionModel().getSelectedItem();
        if (sel == null) { showInfo("Chọn bản ghi cần sửa trước!"); return; }

        if (validateInput(false)) {
            return;
        }

        if (confirm("Cập nhật bản ghi?")) return;

        String sql = "UPDATE THOIKHOABIEU SET NgayApDung=?, Buoi=?, MaHK=? WHERE MaTKB=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Date ngayAD = parseDateFromString(txtNgayAD.getText().trim(), "Ngày Áp Dụng");
            if (ngayAD == null && !txtNgayAD.getText().trim().isEmpty()) return;

            if (ngayAD != null) {
                ps.setDate(1, new java.sql.Date(ngayAD.getTime()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }
            ps.setString(2, txtBuoi.getText().trim());
            ps.setString(3, txtMaHK.getText().trim());
            ps.setString(4, sel.getMaTKB());
            // Dữ liệu từ txtNgayLap và txtNguoiTao sẽ bị bỏ qua

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                // Cập nhật lại đối tượng trong danh sách data để bảng hiển thị đúng nếu có thay đổi
                // mà không cần load lại toàn bộ từ DB, giúp giữ lựa chọn tốt hơn.
                int index = data.indexOf(sel);
                if (index != -1) {
                    // Tạo một đối tượng mới hoặc cập nhật đối tượng sel rồi thay thế trong data
                    // Ở đây, để đơn giản, chúng ta load lại data và cố gắng chọn lại
                    loadData(); // Load lại để đảm bảo dữ liệu nhất quán
                    // Cố gắng chọn lại dòng vừa sửa (có thể cần logic tìm kiếm phức tạp hơn nếu MaTKB thay đổi)
                    tableTKB.getItems().stream()
                            .filter(item -> item.getMaTKB().equals(sel.getMaTKB()))
                            .findFirst()
                            .ifPresent(tableTKB.getSelectionModel()::select);
                } else {
                    loadData(); // Fallback
                }
                showInfo("Đã cập nhật thành công!");
            } else {
                showError("Cập nhật thất bại, không tìm thấy bản ghi hoặc không có gì thay đổi.", null);
            }
        } catch (SQLException ex) {
            showError("Không thể cập nhật!", ex);
        }
    }

    @FXML
    private void handleDelete() {
        ThoiKhoaBieu sel = tableTKB.getSelectionModel().getSelectedItem();
        if (sel == null) { showInfo("Chọn bản ghi cần xoá trước!"); return; }
        if (confirm("Xoá TKB '" + sel.getMaTKB() + "' ?")) return;

        String sql = "DELETE FROM THOIKHOABIEU WHERE MaTKB=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, sel.getMaTKB());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                loadData();
                clearForm();
                showInfo("Đã xoá thành công!");
            } else {
                showError("Xóa thất bại, không tìm thấy bản ghi.", null);
            }
        } catch (SQLException ex) {
            showError("Không thể xoá!", ex);
        }
    }

    @FXML
    private void clearForm() {
        txtMaTKB.clear();
        txtMaTKB.setDisable(false);
        txtNgayAD.clear();
        txtBuoi.clear();
        txtMaHK.clear();
        txtSearch.clear();

        // Xóa và kích hoạt các trường không dùng đến (để FXML không lỗi)
        txtNgayLap.clear();
        txtNgayLap.setDisable(false); // Cho phép nhập nhưng sẽ không được dùng
        txtNguoiTao.clear();
        txtNguoiTao.setDisable(false); // Cho phép nhập nhưng sẽ không được dùng

        tableTKB.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSearch() {
        String key = txtSearch.getText().trim();
        if (key.isEmpty()) {
            loadData();
            return;
        }

        data.clear();
        String sql = "SELECT MaTKB, NgayApDung, Buoi, MaHK FROM THOIKHOABIEU WHERE MaTKB LIKE ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.add(new ThoiKhoaBieu(
                        rs.getString("MaTKB"),
                        rs.getDate("NgayApDung"),
                        rs.getString("Buoi"),
                        rs.getString("MaHK")));
            }
        } catch (SQLException ex) {
            showError("Lỗi tìm kiếm!", ex);
        }
    }

    private boolean validateInput(boolean isAdding) {
        String maTKB = txtMaTKB.getText().trim();
        String ngayADStr = txtNgayAD.getText().trim();
        String buoi = txtBuoi.getText().trim();
        String maHK = txtMaHK.getText().trim();

        if (isAdding && maTKB.isEmpty()) {
            showError("Mã Thời Khóa Biểu không được để trống.", null);
            txtMaTKB.requestFocus();
            return true;
        }
        if (!ngayADStr.isEmpty()) {
            if (parseDateFromString(ngayADStr, "Ngày Áp Dụng") == null) {
                txtNgayAD.requestFocus();
                return true;
            }
        }
        if (buoi.isEmpty()) {
            showError("Buổi không được để trống.", null);
            txtBuoi.requestFocus();
            return true;
        }
        if (maHK.isEmpty()) {
            showError("Mã Học Kỳ không được để trống.", null);
            txtMaHK.requestFocus();
            return true;
        }
        // Không validate txtNgayLap, txtNguoiTao vì chúng không được sử dụng
        return false;
    }

    private Date parseDateFromString(String dateString, String fieldName) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            // sdf.setLenient(false); // Để kiểm tra chặt chẽ hơn nếu muốn
            return sdf.parse(dateString.trim()); // sdf đã được sửa
        } catch (ParseException e) {
            showError("Định dạng ngày tháng không hợp lệ ở trường '" + fieldName +
                    "'. Vui lòng nhập theo định dạng " + DATE_FORMAT_PATTERN + " (ví dụ: 25/12/2024).", null);
            return null;
        }
    }

    private boolean confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,msg,ButtonType.OK,ButtonType.CANCEL);
        a.setHeaderText(null); return a.showAndWait().filter(bt -> bt == ButtonType.OK).isEmpty();
    }
    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION,msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
    private void showError(String msg, Exception ex) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Lỗi");
        a.setHeaderText(msg);
        if (ex != null) {
            a.setContentText("Chi tiết: " + ex.getMessage() + (ex.getCause() != null ? "\nNguyên nhân: " + ex.getCause().getMessage() : ""));
            ex.printStackTrace();
        }
        a.showAndWait();
    }
}