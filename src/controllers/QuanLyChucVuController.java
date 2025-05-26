package controllers;

import entities.ChucVu;
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

    /* ===== FXML ===== */
    @FXML private TableView<ChucVu>         tableChucVu;
    @FXML private TableColumn<ChucVu,String> colSTT, colMaCV, colTenCV;
    @FXML private TextField txtMaCV, txtTenCV, txtSearch;        // dùng cho THÊM
    @FXML private Button    btnThem, btnXoa;                     // (không còn nút Cập nhật)

    /* ===== DATA ===== */
    private final ObservableList<ChucVu> dsChucVu   = FXCollections.observableArrayList();
    private       FilteredList<ChucVu>   filtered   ;

    /* ===== INIT ===== */
    @FXML
    public void initialize() {
        /* --- cấu hình cột --- */
        colMaCV .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaCV()));
        colTenCV.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTenCV()));
        colSTT  .setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(tableChucVu.getItems().indexOf(c.getValue())+1)));

        /* --- cho phép sửa trực tiếp bằng menu chuột phải --- */
        initEditableColumn(colMaCV , "MaCV" );
        initEditableColumn(colTenCV, "TenCV");

        tableChucVu.setEditable(true);
        tableChucVu.setItems(dsChucVu);
        loadData();

        /* --- bộ lọc --- */
        filtered = new FilteredList<>(dsChucVu, p -> true);
        txtSearch.textProperty().addListener((obs,o,n)->{
            String kw = n.toLowerCase().trim();
            filtered.setPredicate(cv ->
                    kw.isEmpty() ||
                            cv.getMaCV().toLowerCase().contains(kw) ||
                            cv.getTenCV().toLowerCase().contains(kw));
        });
        SortedList<ChucVu> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableChucVu.comparatorProperty());
        tableChucVu.setItems(sorted);
    }

    /* ===== CELL FACTORY + UPDATE DB ===== */
    private void initEditableColumn(TableColumn<ChucVu,String> col, String dbCol){
        col.setCellFactory(tc -> {
            TextFieldTableCell<ChucVu,String> cell =
                    new TextFieldTableCell<>(new DefaultStringConverter());

            /* menu chuột phải */
            ContextMenu menu = new ContextMenu();
            MenuItem mien = new MenuItem("Sửa…");
            mien.setOnAction(e -> cell.startEdit());
            menu.getItems().add(mien);
            cell.setContextMenu(menu);
            return cell;
        });

        col.setOnEditCommit(ev -> {
            ChucVu row  = ev.getRowValue();
            String newV = ev.getNewValue();
            if (newV == null || newV.equals(ev.getOldValue())) return;

            // update DB
            if (updateChucVuCell(row.getMaCV(), dbCol, newV)){
                if ("MaCV".equals(dbCol)) row.setMaCV(newV);
                else                      row.setTenCV(newV);
            } else tableChucVu.refresh();    // khôi phục nếu lỗi
        });
    }

    private boolean updateChucVuCell(String maCV, String column, String value){
        String sql = "UPDATE CHUCVU SET "+column+" = ? WHERE MaCV = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,value); ps.setString(2,maCV);
            ps.executeUpdate();  return true;
        } catch (SQLIntegrityConstraintViolationException d){
            warn("Giá trị mới trùng khóa chính!");  // MaCV trùng
        } catch (SQLException ex){ error(ex); }
        return false;
    }

    /* ===== LOAD / THÊM / XOÁ ===== */
    private void loadData(){
        dsChucVu.clear();
        String sql="SELECT MaCV,TenCV FROM CHUCVU ORDER BY MaCV";
        try (Connection c=DatabaseConnection.getConnection();
             Statement  s=c.createStatement();
             ResultSet  r=s.executeQuery(sql)){
            while(r.next()) dsChucVu.add(new ChucVu(r.getString(1),r.getString(2)));
        }catch(SQLException e){ error(e);}
    }

    @FXML private void handleThemChucVu(){
        String ma  = txtMaCV.getText().trim();
        String ten = txtTenCV.getText().trim();
        if(ma.isEmpty()||ten.isEmpty()){ warn("Nhập đủ Mã & Tên"); return; }

        String sql="INSERT INTO CHUCVU(MaCV,TenCV) VALUES(?,?)";
        try(Connection c=DatabaseConnection.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,ma); ps.setString(2,ten);
            ps.executeUpdate();
            dsChucVu.add(new ChucVu(ma,ten));
            txtMaCV.clear(); txtTenCV.clear();
        }catch(SQLIntegrityConstraintViolationException d){ warn("Mã đã tồn tại!"); }
        catch(SQLException e){ error(e);}
    }

    @FXML private void handleXoaChucVu(){
        ChucVu sel = tableChucVu.getSelectionModel().getSelectedItem();
        if(sel==null){ warn("Chọn 1 hàng để xoá"); return; }
        if(!confirm("Xoá?","Bạn chắc muốn xoá \""+sel.getMaCV()+"\" ?")) return;

        String sql="DELETE FROM CHUCVU WHERE MaCV = ?";
        try(Connection c=DatabaseConnection.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,sel.getMaCV()); ps.executeUpdate();
            dsChucVu.remove(sel);
        }catch(SQLException e){
            if("23503".equals(e.getSQLState()))
                warn("Không thể xoá: Đang được tham chiếu!");
            else error(e);
        }
    }

    /* ===== ALERT HELPERS ===== */
    private void warn(String m){ alert(Alert.AlertType.WARNING,"Cảnh báo",m);}
    private void error(Exception e){ alert(Alert.AlertType.ERROR,"Lỗi",e.getMessage()); e.printStackTrace();}
    private void alert(Alert.AlertType t,String h,String m){
        Alert a=new Alert(t); a.setHeaderText(h); a.setContentText(m); a.showAndWait();}
    private boolean confirm(String h,String m){
        Alert a=new Alert(Alert.AlertType.CONFIRMATION); a.setHeaderText(h); a.setContentText(m);
        return a.showAndWait().filter(b->b==ButtonType.OK).isPresent();}
}
