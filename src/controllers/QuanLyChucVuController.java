package controllers;

import entities.ThoiKhoaBieu;
import entities.HocKy;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class QuanLyTKBController {

    /* ==== FXML binding ==== */
    @FXML private TableView<ThoiKhoaBieu> tableTKB;
    @FXML private TableColumn<ThoiKhoaBieu,String>  colMaTKB,colBuoi,colNguoiTao,colMaHK;
    @FXML private TableColumn<ThoiKhoaBieu,LocalDate> colNgayLap,colNgayAD;
    @FXML private TextField txtNgayAD,txtBuoi,txtNguoiTao,txtSearch;
    @FXML private DatePicker dpNgayLap;
    @FXML private TextField txtMaTKB;               // vẫn là text
    @FXML private ComboBox<HocKy> cbMaHK;           // **mới**

    private final ObservableList<ThoiKhoaBieu> data  = FXCollections.observableArrayList();
    private final ObservableList<HocKy>        listHK= FXCollections.observableArrayList();

    /* ==== Định dạng ngày dd/MM/yyyy ==== */
    private static final DateTimeFormatter DD_MM_YYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /* ====================== Khởi tạo ====================== */
    @FXML
    private void initialize() {

        /* ------- map cột -> property ------- */
        colMaTKB   .setCellValueFactory(c -> c.getValue().maTKBProperty());
        colNgayLap .setCellValueFactory(c -> c.getValue().ngayLapProperty());
        colNgayAD  .setCellValueFactory(c -> c.getValue().ngayApDungProperty());
        colBuoi    .setCellValueFactory(c -> c.getValue().buoiProperty());
        colNguoiTao.setCellValueFactory(c -> c.getValue().nguoiTaoProperty());
        colMaHK    .setCellValueFactory(c -> c.getValue().maHKProperty());

        /* ------- format dd/MM/yyyy cho cột Ngày lập ------- */
        colNgayLap.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : DD_MM_YYYY.format(item));
            }
        });

        tableTKB.setItems(data);

        /* ------- DatePicker – định dạng + default today ------- */
        dpNgayLap.setConverter(new StringConverter<>() {
            @Override public String toString(LocalDate date) {
                return date == null ? "" : DD_MM_YYYY.format(date);
            }
            @Override public LocalDate fromString(String s) {
                return (s == null || s.isBlank()) ? null : LocalDate.parse(s, DD_MM_YYYY);
            }
        });
        dpNgayLap.setValue(LocalDate.now());     // default

        /* ------- ComboBox HocKy ------- */
        loadHocKy();
        cbMaHK.setItems(listHK);
        cbMaHK.setConverter(new StringConverter<>() {          // hiển thị MaHK (HọcKỳ/NămHọc)
            @Override public String toString(HocKy hk) {
                if (hk == null) return "";
                return hk.getMaHK() + " - HK" + hk.getHocKy() + " ("+ hk.getNamHoc()+")";
            }
            @Override public HocKy fromString(String s) {      // không dùng
                return null;
            }
        });

        /* -------- Listener chọn dòng -> đổ form -------- */
        tableTKB.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, sel) -> {
                    if (sel != null) {
                        txtMaTKB.setText(sel.getMaTKB());
                        dpNgayLap.setValue(sel.getNgayLap());
                        txtNgayAD.setText(sel.getNgayApDung().toString());
                        txtBuoi.setText(sel.getBuoi());
                        txtNguoiTao.setText(sel.getNguoiTao());
                        // chọn HK tương ứng
                        listHK.stream()
                                .filter(hk -> hk.getMaHK().equals(sel.getMaHK()))
                                .findFirst().ifPresent(cbMaHK::setValue);
                    }
                });

        /* -------- Context-menu Sửa / Xóa -------- */
        tableTKB.setRowFactory(tv -> {
            TableRow<ThoiKhoaBieu> row = new TableRow<>();
            MenuItem miEdit   = new MenuItem("✏  Sửa");
            MenuItem miDelete = new MenuItem("🗑  Xóa");
            miEdit  .setOnAction(e -> { tableTKB.getSelectionModel().select(row.getIndex()); handleUpdate(); });
            miDelete.setOnAction(e -> { tableTKB.getSelectionModel().select(row.getIndex()); handleDelete(); });
            ContextMenu cm = new ContextMenu(miEdit, miDelete);
            row.contextMenuProperty().bind(Bindings.when(row.emptyProperty())
                    .then((ContextMenu) null)
                    .otherwise(cm));
            return row;
        });

        loadData();
    }

    /* ====================== CRUD ====================== */

    @FXML
    public void loadData() {
        data.clear();
        String sql = """
                     SELECT MaTKB, NgayLap, NgayApDung, Buoi, NguoiTao, MaHK
                     FROM THOIKHOABIEU
                     ORDER BY NgayLap DESC
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

        HocKy hk = cbMaHK.getValue();
        if (hk == null) { showInfo("Chọn học kỳ!"); return; }

        String sql = """
                     INSERT INTO THOIKHOABIEU (MaTKB,NgayLap,NgayApDung,Buoi,NguoiTao,MaHK)
                     VALUES (?,?,?,?,?,?)
                     """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, txtMaTKB.getText().trim());
            ps.setDate  (2, Date.valueOf(dpNgayLap.getValue())); // <-- DatePicker
            ps.setDate  (3, Date.valueOf(txtNgayAD.getText().trim()));
            ps.setString(4, txtBuoi.getText().trim());
            ps.setString(5, txtNguoiTao.getText().trim());
            ps.setString(6, hk.getMaHK());

            ps.executeUpdate();
            loadData();
            clearForm();
            showInfo("Đã thêm thành công!");
        } catch (SQLException ex) {
            showError("Không thể thêm!", ex);
        }
    }

    /** Cập nhật */
    private void handleUpdate() {
        ThoiKhoaBieu sel = tableTKB.getSelectionModel().getSelectedItem();
        if (sel == null) { showInfo("Chọn bản ghi cần sửa trước!"); return; }
        if (!confirm("Cập nhật bản ghi?")) return;

        HocKy hk = cbMaHK.getValue();
        if (hk == null) { showInfo("Chọn học kỳ!"); return; }

        String sql = """
                     UPDATE THOIKHOABIEU
                     SET NgayLap=?, NgayApDung=?, Buoi=?, NguoiTao=?, MaHK=?
                     WHERE MaTKB=?
                     """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate  (1, Date.valueOf(dpNgayLap.getValue()));
            ps.setDate  (2, Date.valueOf(txtNgayAD.getText().trim()));
            ps.setString(3, txtBuoi.getText().trim());
            ps.setString(4, txtNguoiTao.getText().trim());
            ps.setString(5, hk.getMaHK());
            ps.setString(6, sel.getMaTKB());

            ps.executeUpdate();
            loadData();
            showInfo("Đã cập nhật thành công!");
        } catch (SQLException ex) {
            showError("Không thể cập nhật!", ex);
        }
    }

    /** Xóa */
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

    /* ====================== Học kỳ ====================== */
    private void loadHocKy() {
        listHK.clear();
        String sql = """
                     SELECT MaHK, HocKy, NamHoc
                     FROM HOCKY
                     ORDER BY NamHoc DESC, SUBSTR(HocKy,1,1) DESC
                     """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                listHK.add(new HocKy(
                        rs.getString("MaHK"),
                        rs.getString("HocKy"),
                        rs.getString("NamHoc")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ====================== Tiện ích UI ====================== */
    @FXML
    private void clearForm() {
        txtMaTKB.clear();
        dpNgayLap.setValue(LocalDate.now());     // reset today
        txtNgayAD.clear();
        txtBuoi.clear();
        txtNguoiTao.clear();
        cbMaHK.getSelectionModel().clearSelection();
        tableTKB.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSearch() { /* ... giữ nguyên ... */ }

    private boolean confirm(String msg) { /* ... giữ nguyên ... */ }
    private void showInfo(String msg) { /* ... giữ nguyên ... */ }
    private void showError(String msg, Exception ex) { /* ... giữ nguyên ... */ }
}
