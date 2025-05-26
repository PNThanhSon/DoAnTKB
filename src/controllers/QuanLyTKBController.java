package controllers;

import entities.ThoiKhoaBieu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

/**
 * Quản lý Thời khóa biểu – trực tiếp JDBC, không tách DAO.
 */
public class QuanLyTKBController {

    /* ==== FXML binding ==== */
    @FXML private TableView<ThoiKhoaBieu> tableTKB;
    @FXML private TableColumn<ThoiKhoaBieu,String> colMaTKB,colBuoi,colNguoiTao,colMaHK;
    @FXML private TableColumn<ThoiKhoaBieu,LocalDate> colNgayLap,colNgayAD;
    @FXML private TextField txtMaTKB,txtNgayLap,txtNgayAD,txtBuoi,txtNguoiTao,txtMaHK,txtSearch;

    private final ObservableList<ThoiKhoaBieu> data = FXCollections.observableArrayList();

    /* ====================== Khởi tạo bảng ====================== */
    @FXML
    private void initialize() {
        // map cột -> thuộc tính entity
        colMaTKB.setCellValueFactory(c -> c.getValue().maTKBProperty());
        colNgayLap.setCellValueFactory(c -> c.getValue().ngayLapProperty());
        colNgayAD.setCellValueFactory(c -> c.getValue().ngayApDungProperty());
        colBuoi.setCellValueFactory(c -> c.getValue().buoiProperty());
        colNguoiTao.setCellValueFactory(c -> c.getValue().nguoiTaoProperty());
        colMaHK.setCellValueFactory(c -> c.getValue().maHKProperty());

        tableTKB.setItems(data);

        // Khi chọn 1 dòng -> đổ xuống form
        tableTKB.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, sel) -> {
                    if (sel != null) {
                        txtMaTKB.setText(sel.getMaTKB());
                        txtNgayLap.setText(sel.getNgayLap().toString());
                        txtNgayAD.setText(sel.getNgayApDung().toString());
                        txtBuoi.setText(sel.getBuoi());
                        txtNguoiTao.setText(sel.getNguoiTao());
                        txtMaHK.setText(sel.getMaHK());
                    }
                });

        loadData();
    }

    /* ====================== CRUD ====================== */

    /** Nạp toàn bộ dữ liệu */
    @FXML
    public void loadData() {
        data.clear();
        String sql = """
                     SELECT MaTKB, NgayLap, NgayApDung, Buoi, NguoiTao, MaHK
                     FROM THOIKHOABIEU ORDER BY NgayLap DESC
                     """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(new ThoiKhoaBieu(
                        rs.getString("MaTKB"),
                        rs.getDate("NgayLap").toLocalDate(),
                        rs.getDate("NgayApDung").toLocalDate(),
                        rs.getString("Buoi"),
                        rs.getString("NguoiTao"),
                        rs.getString("MaHK")));
            }
        } catch (SQLException ex) {
            showError("Lỗi tải dữ liệu", ex);
        }
    }

    /** Thêm mới */
    @FXML
    private void handleAdd() {
        if (!confirm("Thêm TKB mới?")) return;

        String sql = "INSERT INTO THOIKHOABIEU (MaTKB,NgayLap,NgayApDung,Buoi,NguoiTao,MaHK) " +
                "VALUES (?,?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, txtMaTKB.getText().trim());
            ps.setDate(2, Date.valueOf(txtNgayLap.getText().trim()));
            ps.setDate(3, Date.valueOf(txtNgayAD.getText().trim()));
            ps.setString(4, txtBuoi.getText().trim());
            ps.setString(5, txtNguoiTao.getText().trim());
            ps.setString(6, txtMaHK.getText().trim());

            ps.executeUpdate();
            loadData();
            clearForm();
            showInfo("Đã thêm thành công!");
        } catch (SQLException ex) {
            showError("Không thể thêm!", ex);
        }
    }

    /** Cập nhật */
    @FXML
    private void handleUpdate() {
        ThoiKhoaBieu sel = tableTKB.getSelectionModel().getSelectedItem();
        if (sel == null) { showInfo("Chọn bản ghi cần sửa trước!"); return; }
        if (!confirm("Cập nhật bản ghi?")) return;

        String sql = """
                     UPDATE THOIKHOABIEU
                     SET NgayLap=?, NgayApDung=?, Buoi=?, NguoiTao=?, MaHK=?
                     WHERE MaTKB=?
                     """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(txtNgayLap.getText().trim()));
            ps.setDate(2, Date.valueOf(txtNgayAD.getText().trim()));
            ps.setString(3, txtBuoi.getText().trim());
            ps.setString(4, txtNguoiTao.getText().trim());
            ps.setString(5, txtMaHK.getText().trim());
            ps.setString(6, sel.getMaTKB());

            ps.executeUpdate();
            loadData();
            showInfo("Đã cập nhật thành công!");
        } catch (SQLException ex) {
            showError("Không thể cập nhật!", ex);
        }
    }

    /** Xóa */
    @FXML
    private void handleDelete() {
        ThoiKhoaBieu sel = tableTKB.getSelectionModel().getSelectedItem();
        if (sel == null) { showInfo("Chọn bản ghi cần xoá trước!"); return; }
        if (!confirm("Xoá TKB '" + sel.getMaTKB() + "' ?")) return;

        String sql = "DELETE FROM THOIKHOABIEU WHERE MaTKB=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, sel.getMaTKB());
            ps.executeUpdate();
            loadData();
            clearForm();
            showInfo("Đã xoá thành công!");
        } catch (SQLException ex) {
            showError("Không thể xoá!", ex);
        }
    }

    /* ====================== Tiện ích UI ====================== */

    @FXML
    private void clearForm() {
        txtMaTKB.clear(); txtNgayLap.clear(); txtNgayAD.clear();
        txtBuoi.clear(); txtNguoiTao.clear(); txtMaHK.clear();
        tableTKB.getSelectionModel().clearSelection();
    }

    /** Lọc theo MaTKB (LIKE) */
    @FXML
    private void handleSearch() {
        String key = txtSearch.getText().trim();
        if (key.isEmpty()) { loadData(); return; }

        data.clear();
        String sql = "SELECT * FROM THOIKHOABIEU WHERE MaTKB LIKE ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.add(new ThoiKhoaBieu(
                        rs.getString("MaTKB"),
                        rs.getDate("NgayLap").toLocalDate(),
                        rs.getDate("NgayApDung").toLocalDate(),
                        rs.getString("Buoi"),
                        rs.getString("NguoiTao"),
                        rs.getString("MaHK")));
            }
        } catch (SQLException ex) {
            showError("Lỗi tìm kiếm!", ex);
        }
    }

    /* ==== Hộp thoại gọn ==== */
    private boolean confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,msg,ButtonType.OK,ButtonType.CANCEL);
        a.setHeaderText(null); return a.showAndWait().filter(bt->bt==ButtonType.OK).isPresent();
    }
    private void showInfo(String msg) { new Alert(Alert.AlertType.INFORMATION,msg).show(); }
    private void showError(String msg,Exception ex) {
        Alert a=new Alert(Alert.AlertType.ERROR,msg+"\n"+ex.getMessage());
        a.show(); ex.printStackTrace();
    }
}
