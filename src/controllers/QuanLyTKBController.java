package controllers;

import dao.BCTKDAO;
import entities.HocKy;
import entities.ThoiKhoaBieu;
import javafx.beans.binding.Bindings;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class QuanLyTKBController {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final BCTKDAO hkDao = new BCTKDAO();

    @FXML private TableView<ThoiKhoaBieu> tableTKB;
    @FXML private TableColumn<ThoiKhoaBieu,String>  colMaTKB,colBuoi,colNguoiTao,colMaHK;
    @FXML private TableColumn<ThoiKhoaBieu,LocalDate> colNgayLap,colNgayAD;
    @FXML private TextField txtMaTKB,txtNgayAD,txtBuoi,txtNguoiTao,txtSearch;
    @FXML private DatePicker dpNgayLap;
    @FXML private ComboBox<HocKy> cbMaHK;

    private final ObservableList<ThoiKhoaBieu> data   = FXCollections.observableArrayList();
    private final ObservableList<HocKy>        listHK = FXCollections.observableArrayList();

    /* ================= INIT ================= */
    @FXML
    private void initialize() {

        /* map & format */
        colMaTKB  .setCellValueFactory(c->c.getValue().maTKBProperty());
        colNgayLap.setCellValueFactory(c->c.getValue().ngayLapProperty());
        colNgayAD .setCellValueFactory(c->c.getValue().ngayApDungProperty());
        colBuoi   .setCellValueFactory(c->c.getValue().buoiProperty());
        colNguoiTao.setCellValueFactory(c->c.getValue().nguoiTaoProperty());
        colMaHK   .setCellValueFactory(c->c.getValue().maHKProperty());

        TableCell<ThoiKhoaBieu,LocalDate> fmt = new TableCell<>() {
            @Override protected void updateItem(LocalDate d, boolean empty){
                super.updateItem(d,empty);
                setText(empty||d==null?null:FMT.format(d));
            }};
        colNgayLap.setCellFactory(c->fmt); colNgayAD.setCellFactory(c->fmt);

        tableTKB.setEditable(true);

        /* editable + menu ô = Sửa + Xoá */
        initEditableStringColumn(colMaTKB ,"MaTKB");
        initEditableDateColumn  (colNgayLap,"NgayLap");
        initEditableDateColumn  (colNgayAD ,"NgayApDung");
        initEditableStringColumn(colBuoi   ,"Buoi");
        initEditableStringColumn(colNguoiTao,"NguoiTao");
        initEditableStringColumn(colMaHK   ,"MaHK");

        tableTKB.setItems(data);

        /* DatePicker & ComboBox (giữ nguyên) */
        dpNgayLap.setConverter(new StringConverter<>() {
            @Override public String toString(LocalDate d){return d==null?"":FMT.format(d);}
            @Override public LocalDate fromString(String s){return s==null||s.isBlank()?null:LocalDate.parse(s,FMT);}
        });
        dpNgayLap.setPromptText("dd/MM/yyyy");
        dpNgayLap.setValue(LocalDate.now());

        listHK.setAll(hkDao.getAllHocKy());
        cbMaHK.setItems(listHK);
        cbMaHK.setConverter(new StringConverter<>() {
            @Override public String toString(HocKy hk){return hk==null?"":hk.getMaHK()+" - "+hk.getHocKy();}
            @Override public HocKy fromString(String s){return null;}
        });

        /* menu HÀNG -> Xoá (giữ) */
        tableTKB.setRowFactory(tv->{
            TableRow<ThoiKhoaBieu> row=new TableRow<>();
            MenuItem del=new MenuItem("🗑 Xoá");
            del.setOnAction(e->{ tableTKB.getSelectionModel().select(row.getIndex()); handleDelete();});
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty()).then((ContextMenu)null).otherwise(new ContextMenu(del)));
            return row;
        });

        loadData();
    }

    /* ===== editable helpers ===== */

    private ContextMenu cellMenu(TableCell<?,?> cell){
        MenuItem miEdit = new MenuItem("✏ Sửa…");
        miEdit.setOnAction(e -> cell.startEdit());
        MenuItem miDel  = new MenuItem("🗑 Xoá");
        miDel.setOnAction(e -> {
            tableTKB.getSelectionModel().select(cell.getIndex());
            handleDelete();
        });
        return new ContextMenu(miEdit,miDel);
    }

    private void initEditableStringColumn(TableColumn<ThoiKhoaBieu,String> col,String dbCol){
        col.setCellFactory(tc->{
            TextFieldTableCell<ThoiKhoaBieu,String> cell =
                    new TextFieldTableCell<>(new DefaultStringConverter());
            cell.setContextMenu(cellMenu(cell));
            return cell;
        });
        col.setOnEditCommit(ev->{
            ThoiKhoaBieu row=ev.getRowValue();
            String newVal=ev.getNewValue()==null?"":ev.getNewValue().trim();
            if(newVal.equals(ev.getOldValue())) return;
            if(updateDB(row.getMaTKB(),dbCol,newVal)){
                switch(dbCol){
                    case "MaTKB"   -> row.maTKBProperty().set(newVal);
                    case "Buoi"    -> row.buoiProperty().set(newVal);
                    case "NguoiTao"-> row.nguoiTaoProperty().set(newVal);
                    case "MaHK"    -> row.maHKProperty().set(newVal);
                }
            } else tableTKB.refresh();
        });
    }

    private void initEditableDateColumn(TableColumn<ThoiKhoaBieu,LocalDate> col,String dbCol){
        col.setCellFactory(tc->{
            TextFieldTableCell<ThoiKhoaBieu,LocalDate> cell =
                    new TextFieldTableCell<>(new StringConverter<>() {
                        @Override public String toString(LocalDate d){return d==null?"":FMT.format(d);}
                        @Override public LocalDate fromString(String s){return s==null||s.isBlank()?null:LocalDate.parse(s,FMT);}
                    });
            cell.setContextMenu(cellMenu(cell));
            return cell;
        });
        col.setOnEditCommit(ev->{
            ThoiKhoaBieu row=ev.getRowValue();
            LocalDate d=ev.getNewValue();
            if(d==null||d.equals(ev.getOldValue())) return;
            if(updateDB(row.getMaTKB(),dbCol,Date.valueOf(d))){
                if("NgayLap".equals(dbCol)) row.ngayLapProperty().set(d);
                else                        row.ngayApDungProperty().set(d);
            } else tableTKB.refresh();
        });
    }

    private boolean updateDB(String ma,String col,Object val){
        try(Connection c=DatabaseConnection.getConnection();
            PreparedStatement ps=c.prepareStatement("UPDATE THOIKHOABIEU SET "+col+"=? WHERE MaTKB=?")){
            ps.setObject(1,val); ps.setString(2,ma); ps.executeUpdate(); return true;
        }catch(SQLException e){ showError("SQL lỗi!",e); return false;}
    }

    /* === CRUD / search / alert giữ nguyên như phiên bản compile-được gần nhất === */
    /** nạp dữ liệu */
    @FXML
    public void loadData() {
        data.clear();
        String sql = """
                     SELECT MaTKB, NgayLap, NgayApDung, Buoi, NguoiTao, MaHK
                     FROM THOIKHOABIEU
                     ORDER BY NgayLap DESC
                     """;
        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                // Lấy java.sql.Date và kiểm tra null trước khi gọi toLocalDate()
                java.sql.Date sqlNgayLap = rs.getDate("NgayLap");
                LocalDate ngayLap = (sqlNgayLap != null) ? sqlNgayLap.toLocalDate() : null;

                java.sql.Date sqlNgayApDung = rs.getDate("NgayApDung");
                LocalDate ngayApDung = (sqlNgayApDung != null) ? sqlNgayApDung.toLocalDate() : null;

                data.add(new ThoiKhoaBieu(
                        rs.getString("MaTKB"),
                        ngayLap,           // Sử dụng biến đã kiểm tra null
                        ngayApDung,        // Sử dụng biến đã kiểm tra null
                        rs.getString("Buoi"),
                        rs.getString("NguoiTao"),
                        rs.getString("MaHK")));
            }
        }catch(SQLException ex){ showError("Lỗi tải dữ liệu",ex);}
    }
    /** Thêm mới */
    @FXML
    private void handleAdd() {
        HocKy hk = cbMaHK.getValue();
        if (hk == null) { showInfo("Chọn học kỳ!"); return; }
        if (!confirm("Thêm TKB mới?")) return;

        String sql = """
                     INSERT INTO THOIKHOABIEU
                     (MaTKB, NgayLap, NgayApDung, Buoi, NguoiTao, MaHK)
                     VALUES (?,?,?,?,?,?)
                     """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, txtMaTKB.getText().trim());
            ps.setDate  (2, Date.valueOf(dpNgayLap.getValue()));
            ps.setDate  (3, Date.valueOf(LocalDate.parse(txtNgayAD.getText().trim(), FMT)));
            ps.setString(4, txtBuoi.getText().trim());
            ps.setString(5, txtNguoiTao.getText().trim());
            ps.setString(6, hk.getMaHK());

            ps.executeUpdate();
            loadData(); clearForm(); showInfo("Đã thêm!");
        } catch (Exception ex) {
            showError("Không thể thêm!", ex);
        }
    }

    /** Cập nhật */
    private void handleUpdate() {
        ThoiKhoaBieu sel = tableTKB.getSelectionModel().getSelectedItem();
        if (sel == null) { showInfo("Chọn bản ghi!"); return; }
        HocKy hk = cbMaHK.getValue();
        if (hk == null) { showInfo("Chọn học kỳ!"); return; }
        if (!confirm("Cập nhật bản ghi?")) return;

        String sql = """
                     UPDATE THOIKHOABIEU
                     SET NgayLap=?, NgayApDung=?, Buoi=?, NguoiTao=?, MaHK=?
                     WHERE MaTKB=?
                     """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate  (1, Date.valueOf(dpNgayLap.getValue()));
            ps.setDate  (2, Date.valueOf(LocalDate.parse(txtNgayAD.getText().trim(), FMT)));
            ps.setString(3, txtBuoi.getText().trim());
            ps.setString(4, txtNguoiTao.getText().trim());
            ps.setString(5, hk.getMaHK());
            ps.setString(6, sel.getMaTKB());

            ps.executeUpdate();
            loadData(); showInfo("Đã cập nhật!");
        } catch (Exception ex) {
            showError("Không thể cập nhật!", ex);
        }
    }

    /** Xoá */
    private void handleDelete() {
        ThoiKhoaBieu sel = tableTKB.getSelectionModel().getSelectedItem();
        if (sel == null) { showInfo("Chọn bản ghi!"); return; }
        if (!confirm("Xoá TKB '" + sel.getMaTKB() + "' ?")) return;

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps1 = c.prepareStatement("DELETE FROM CHITIETTKB WHERE MaTKB=?");
             PreparedStatement ps2 = c.prepareStatement("DELETE FROM THOIKHOABIEU WHERE MaTKB=?")) {

            ps1.setString(1, sel.getMaTKB());
            ps1.executeUpdate();

            ps2.setString(1, sel.getMaTKB());
            ps2.executeUpdate();

            loadData();
            clearForm();
            showInfo("Đã xoá!");

        } catch (Exception ex) {
            showError("Không thể xoá!", ex);
        }
    }

    /* ====================== Search ====================== */
    @FXML
    private void handleSearch() {
        String key = txtSearch.getText().trim();
        if (key.isEmpty()) { loadData(); return; }

        data.clear();
        // Nên chọn cụ thể các cột thay vì dùng SELECT *
        String sql = "SELECT MaTKB, NgayLap, NgayApDung, Buoi, NguoiTao, MaHK FROM THOIKHOABIEU WHERE MaTKB LIKE ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Lấy java.sql.Date và kiểm tra null trước khi gọi toLocalDate()
                java.sql.Date sqlNgayLap = rs.getDate("NgayLap");
                LocalDate ngayLap = (sqlNgayLap != null) ? sqlNgayLap.toLocalDate() : null;

                java.sql.Date sqlNgayApDung = rs.getDate("NgayApDung");
                LocalDate ngayApDung = (sqlNgayApDung != null) ? sqlNgayApDung.toLocalDate() : null;

                data.add(new ThoiKhoaBieu(
                        rs.getString("MaTKB"),
                        ngayLap,           // Sử dụng biến đã kiểm tra null
                        ngayApDung,        // Sử dụng biến đã kiểm tra null
                        rs.getString("Buoi"),
                        rs.getString("NguoiTao"),
                        rs.getString("MaHK")));
            }
        } catch (Exception ex) { showError("Lỗi tìm kiếm!", ex); }
    }

    /* ====================== Helpers ====================== */
    @FXML
    private void clearForm() {
        txtMaTKB.clear(); txtBuoi.clear(); txtNguoiTao.clear();
        txtNgayAD.clear(); dpNgayLap.setValue(LocalDate.now());
        cbMaHK.getSelectionModel().clearSelection();
        tableTKB.getSelectionModel().clearSelection();
    }

    private boolean confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL);
        a.setHeaderText(null);
        return a.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }
    private void showInfo(String msg) { new Alert(Alert.AlertType.INFORMATION, msg).show(); }
    private void showError(String msg, Exception ex) {
        new Alert(Alert.AlertType.ERROR, msg + "\n" + ex.getMessage()).show();
        ex.printStackTrace();
    }
}
