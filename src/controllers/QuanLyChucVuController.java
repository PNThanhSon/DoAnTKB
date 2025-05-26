package controllers;

import entities.ChucVu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import util.DatabaseConnection;

import java.sql.*;

public class QuanLyChucVuController {

    /* ---------- FXML ---------- */
    @FXML private TableView<ChucVu> tableChucVu;
    @FXML private TableColumn<ChucVu,String> colSTT;
    @FXML private TableColumn<ChucVu,String> colMaCV;
    @FXML private TableColumn<ChucVu,String> colTenCV;
    @FXML private TextField txtMaCV;
    @FXML private TextField txtTenCV;
    @FXML private TextField txtSearch;



    /* ---------- INIT ---------- */
    @FXML
    public void initialize() {
        colMaCV.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaCV()));
        colTenCV.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTenCV()));
        colSTT .setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(tableChucVu.getItems().indexOf(c.getValue()) + 1)));

        tableChucVu.setItems(dsChucVu);
        loadData();

        // Khi chọn hàng → đổ vào 2 TextField để tiện sửa
        tableChucVu.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel != null) {
                txtMaCV.setText(sel.getMaCV());
                txtTenCV.setText(sel.getTenCV());
            }
        });
        tableChucVu.setItems(dsChucVu);      // gìn giữ
        loadData();                          // nạp dữ liệu

        /* ---------- Thiết lập bộ lọc ---------- */
        filteredList = new FilteredList<>(dsChucVu, p -> true);

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            String keyword = newVal.toLowerCase().trim();
            if (keyword.isEmpty()) {
                filteredList.setPredicate(p -> true);
            } else {
                filteredList.setPredicate(cv ->
                        cv.getMaCV().toLowerCase().contains(keyword) ||
                                cv.getTenCV().toLowerCase().contains(keyword));
            }
        });

        /* ---------- Cho TableView dùng danh sách đã lọc + sắp xếp ---------- */
        SortedList<ChucVu> sorted = new SortedList<>(filteredList);
        sorted.comparatorProperty().bind(tableChucVu.comparatorProperty());
        tableChucVu.setItems(sorted);
    }

    /* ---------- LOAD ---------- */
    private void loadData() {
        dsChucVu.clear();
        String sql = "SELECT MaCV, TenCV FROM CHUCVU ORDER BY MaCV";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) dsChucVu.add(new ChucVu(rs.getString(1), rs.getString(2)));
        } catch (SQLException e) { error(e); }
    }

    /* ---------- THÊM ---------- */
    @FXML
    private void handleThemChucVu() {
        String ma  = txtMaCV.getText().trim();
        String ten = txtTenCV.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) { warn("Vui lòng nhập cả Mã & Tên."); return; }

        String sql = "INSERT INTO CHUCVU (MaCV, TenCV) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ma); ps.setString(2, ten);
            ps.executeUpdate();
            dsChucVu.add(new ChucVu(ma, ten));  clearFields();
        } catch (SQLIntegrityConstraintViolationException dup) {
            warn("Mã chức vụ đã tồn tại!");          // Trùng khoá chính
        } catch (SQLException e) { error(e); }
    }

    /* ---------- SỬA ---------- */
    @FXML
    private void handleSuaChucVu() {
        ChucVu sel = tableChucVu.getSelectionModel().getSelectedItem();
        if (sel == null) { warn("Chọn 1 hàng để sửa."); return; }

        String newMa  = txtMaCV.getText().trim();
        String newTen = txtTenCV.getText().trim();
        if (newMa.isEmpty() || newTen.isEmpty()) { warn("Không được để trống ô."); return; }

        // Xác nhận
        if (!confirm("Xác nhận sửa?", "Cập nhật chức vụ \""+sel.getMaCV()+"\" thành \""+newMa+"\" ?")) return;

        String sql = "UPDATE CHUCVU SET MaCV = ?, TenCV = ? WHERE MaCV = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newMa); ps.setString(2, newTen); ps.setString(3, sel.getMaCV());
            ps.executeUpdate();

            // Cập nhật trên bảng
            sel.setMaCV(newMa);
            sel.setTenCV(newTen);
            tableChucVu.refresh();
            clearFields();
        } catch (SQLIntegrityConstraintViolationException dup) {
            warn("Mã chức vụ mới đã tồn tại!");      // đổi sang mã đã có
        } catch (SQLException e) { error(e); }
    }

    /* ---------- XOÁ ---------- */
    @FXML
    private void handleXoaChucVu() {
        ChucVu sel = tableChucVu.getSelectionModel().getSelectedItem();
        if (sel == null) { warn("Chọn 1 hàng để xoá."); return; }

        if (!confirm("Xác nhận xoá?", "Bạn chắc muốn xoá chức vụ \""+sel.getMaCV()+"\" ?")) return;

        String sql = "DELETE FROM CHUCVU WHERE MaCV = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sel.getMaCV());
            ps.executeUpdate();
            dsChucVu.remove(sel);  clearFields();
        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState()))   // khoá ngoại ràng buộc
                warn("Không thể xoá: chức vụ đang được dùng ở bảng khác.");
            else error(e);
        }
    }
    // … (các khai báo khác)
    private final ObservableList<ChucVu> dsChucVu = FXCollections.observableArrayList();
    private FilteredList<ChucVu> filteredList;


    /* ---------- HELPER ---------- */
    private void clearFields() { txtMaCV.clear(); txtTenCV.clear(); tableChucVu.getSelectionModel().clearSelection(); }

    private void warn(String msg){ alert(Alert.AlertType.WARNING,"Cảnh báo",msg); }
    private void error(Exception e){ alert(Alert.AlertType.ERROR,"Lỗi",e.getMessage()); e.printStackTrace(); }
    private void alert(Alert.AlertType t, String h, String m){ Alert a=new Alert(t); a.setHeaderText(h); a.setContentText(m); a.showAndWait(); }
    private boolean confirm(String h,String m){
        Alert a=new Alert(Alert.AlertType.CONFIRMATION); a.setHeaderText(h); a.setContentText(m);
        return a.showAndWait().filter(b -> b==ButtonType.OK).isPresent();
    }
}
