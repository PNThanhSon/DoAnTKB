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
    @FXML public void loadData(){ /* ... */ }
    @FXML private void handleAdd(){ /* ... */ }
    private void handleDelete(){ /* ... */ }
    @FXML private void handleSearch(){ /* ... */ }
    @FXML private void clearForm(){ /* ... */ }
    private boolean confirm(String m){ Alert a=new Alert(Alert.AlertType.CONFIRMATION,m,ButtonType.OK,ButtonType.CANCEL);
        a.setHeaderText(null); return a.showAndWait().filter(b->b==ButtonType.OK).isPresent();}
    private void showInfo(String m){ new Alert(Alert.AlertType.INFORMATION,m).show();}
    private void showError(String m,Exception e){ new Alert(Alert.AlertType.ERROR,m+"\n"+e.getMessage()).show(); e.printStackTrace();}
}
