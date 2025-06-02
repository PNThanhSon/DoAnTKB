package controllers;

import entities.ChucVu;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import util.DatabaseConnection;

import java.sql.*;

public class QuanLyChucVuController {

    @FXML private TableView<ChucVu>          tableChucVu;
    @FXML private TableColumn<ChucVu,String> colSTT,colMaCV,colTenCV;
    @FXML private TextField txtMaCV,txtTenCV,txtSearch;
    @FXML private Button btnThem;

    private final ObservableList<ChucVu> dsChucVu = FXCollections.observableArrayList();
    private FilteredList<ChucVu> filtered;

    @FXML
    public void initialize() {
        colMaCV .setCellValueFactory(c->new SimpleStringProperty(c.getValue().getMaCV()));
        colTenCV.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getTenCV()));
        colSTT  .setCellValueFactory(c->new SimpleStringProperty(
                String.valueOf(tableChucVu.getItems().indexOf(c.getValue())+1)));

        initEditableColumn(colMaCV ,"MaCV");
        initEditableColumn(colTenCV,"TenCV");

        tableChucVu.setEditable(true);

        /* menu hàng Xoá */
        tableChucVu.setRowFactory(tv->{
            TableRow<ChucVu> row=new TableRow<>();
            MenuItem del=new MenuItem("🗑 Xoá");
            del.setOnAction(e->{ tableChucVu.getSelectionModel().select(row.getIndex()); handleXoaChucVu();});
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty()).then((ContextMenu)null).otherwise(new ContextMenu(del)));
            return row;
        });

        loadData();

        filtered=new FilteredList<>(dsChucVu,p->true);
        txtSearch.textProperty().addListener((o,oldV,n)->{
            String kw=n.toLowerCase().trim();
            filtered.setPredicate(cv ->
                    kw.isEmpty()||
                            cv.getMaCV().toLowerCase().contains(kw)||
                            cv.getTenCV().toLowerCase().contains(kw));
        });
        SortedList<ChucVu> sorted=new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableChucVu.comparatorProperty());
        tableChucVu.setItems(sorted);
    }

    /* ===== editable cell: Sửa + Xoá ===== */
    private void initEditableColumn(TableColumn<ChucVu,String> col,String dbCol){
        col.setCellFactory(tc->{
            TextFieldTableCell<ChucVu,String> cell =
                    new TextFieldTableCell<>(new DefaultStringConverter());

            MenuItem miEdit=new MenuItem("✏ Sửa…");
            miEdit.setOnAction(e->cell.startEdit());
            MenuItem miDel =new MenuItem("🗑 Xoá");
            miDel.setOnAction(e->{
                tableChucVu.getSelectionModel().select(cell.getIndex());
                handleXoaChucVu();
            });
            cell.setContextMenu(new ContextMenu(miEdit,miDel));
            return cell;
        });

        col.setOnEditCommit(ev->{
            ChucVu row=ev.getRowValue();
            String newVal=ev.getNewValue()==null?"":ev.getNewValue().trim();
            if(newVal.equals(ev.getOldValue())) return;
            if(updateDB(row.getMaCV(),dbCol,newVal)){
                if("MaCV".equals(dbCol)) row.setMaCV(newVal); else row.setTenCV(newVal);
            } else tableChucVu.refresh();
        });
    }

    private boolean updateDB(String key,String col,String val){
        String sql="UPDATE CHUCVU SET "+col+"=? WHERE MaCV=?";
        try(Connection c=DatabaseConnection.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,val); ps.setString(2,key); ps.executeUpdate(); return true;
        }catch(SQLException e){ error(e); return false;}
    }

    /* ===== load / add / delete (giữ nguyên) ===== */
    private void loadData(){ /* ... */ }
    @FXML private void handleThemChucVu(){ /* ... */ }
    private void handleXoaChucVu(){ /* ... */ }

    /* ===== alert helpers (giữ nguyên) ===== */
    private void warn(String m){ alert(Alert.AlertType.WARNING,"Cảnh báo",m);}
    private void error(Exception e){ alert(Alert.AlertType.ERROR,"Lỗi",e.getMessage()); e.printStackTrace();}
    private void alert(Alert.AlertType t,String h,String m){
        Alert a=new Alert(t); a.setHeaderText(h); a.setContentText(m); a.showAndWait();}
    private boolean confirm(String h,String m){
        Alert a=new Alert(Alert.AlertType.CONFIRMATION); a.setHeaderText(h); a.setContentText(m);
        return a.showAndWait().filter(b->b==ButtonType.OK).isPresent();}
}
