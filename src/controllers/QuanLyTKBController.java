package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import util.DatabaseConnection;

import java.net.URL;
import java.sql.*;
import java.util.*;

/**  Quản lý (xem / thêm / xoá / sửa) Thời Khóa Biểu.
 *   Bảng CHITIETTKB: MaTKB | Thu | Tiet | MaLop | MaGV | MaMH  */
public class QuanLyTKBController implements Initializable {

    /* ============= FXML ============== */
    @FXML private ComboBox<String> cboMaTKB;                // gõ / chọn mã
    @FXML private ComboBox<String> cboKhoi;                 // 10-11-12
    @FXML private TableView<Map<String,StringProperty>> table;

    /* ============= hằng ============== */
    private static final String COL_THU  = "Thứ";
    private static final String COL_TIET = "Tiết";

    /* =========== khởi tạo ============ */
    @Override public void initialize(URL url, ResourceBundle rb) {
        // khối
        cboKhoi.setItems(FXCollections.observableArrayList("10","11","12"));
        cboKhoi.getSelectionModel().selectFirst();

        loadMaTKBList();        // danh sách mã TKB
        table.setEditable(true);
    }

    /* ------------ nạp danh sách mã TKB để chọn ---------- */
    private void loadMaTKBList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT MaTKB FROM THOIKHOABIEU ORDER BY MaTKB";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString(1));
        } catch (SQLException ex) { error(ex); }
        cboMaTKB.setItems(list);
    }

    /* ==================== XEM TKB ======================= */
    @FXML private void handleLoad() {
        String maTKB = cboMaTKB.getEditor().getText().trim();   // dùng ComboBox
        String khoi  = cboKhoi.getValue();

        if (maTKB.isEmpty()) {
            alert(Alert.AlertType.WARNING,"Thiếu dữ liệu","Nhập / chọn mã TKB!"); return;
        }

        try (Connection c = DatabaseConnection.getConnection()) {
            /* 1. DS lớp của khối, A1→A12 */
            List<String> dsLop = getLopTheoKhoi(c, khoi);
            if (dsLop.isEmpty()) {
                alert(Alert.AlertType.INFORMATION,"Không có lớp",
                        "Khối " + khoi + " chưa có lớp."); return;
            }

            /* 2. dựng cột, 3. nạp dữ liệu */
            buildColumns(dsLop);
            table.setItems(loadChiTiet(c, maTKB, khoi, dsLop));

        } catch (SQLException ex) { error(ex); }
    }

    /* ---------- DS lớp theo khối, sắp A1..A12 ---------- */
    private List<String> getLopTheoKhoi(Connection c, String khoi) throws SQLException {
        String sql = """
            SELECT MaLop
            FROM   LOP
            WHERE  Khoi = ?
            ORDER  BY
                   REGEXP_SUBSTR(MaLop,'[A-Za-z]+'),          -- ký tự A/B/…
                   TO_NUMBER(REGEXP_SUBSTR(MaLop,'\\d+$')),   -- số cuối
                   MaLop                                      -- dự phòng
            """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, khoi);
            ResultSet rs = ps.executeQuery();
            List<String> list = new ArrayList<>();
            while (rs.next()) list.add(rs.getString(1));
            return list;
        }
    }

    /* ------------- lấy & pivot dữ liệu ----------------- */
    private ObservableList<Map<String,StringProperty>> loadChiTiet(
            Connection c, String maTKB, String khoi, List<String> dsLop) throws SQLException {

        String sql = """
            SELECT Thu, Tiet, MaLop,
                   MaMH || '-' || MaGV AS MG
            FROM   CHITIETTKB
            WHERE  MaTKB = ?  AND  MaLop LIKE ?
            ORDER  BY Thu, Tiet
            """;

        Map<String,Map<String,StringProperty>> pivot = new LinkedHashMap<>();

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maTKB);
            ps.setString(2, khoi + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int thu  = rs.getInt("Thu");
                int tiet = rs.getInt("Tiet");
                String lop = rs.getString("MaLop");
                String mg  = rs.getString("MG");             // TOAN-MGV01

                String key = thu + "-" + tiet;
                Map<String,StringProperty> row =
                        pivot.computeIfAbsent(key, k -> blankRow(thu,tiet,dsLop));
                row.get(lop).set(mg);
            }
        }
        return FXCollections.observableArrayList(pivot.values());
    }

    private Map<String,StringProperty> blankRow(int thu,int tiet,List<String> dsLop){
        Map<String,StringProperty> m = new HashMap<>();
        m.put(COL_THU , new SimpleStringProperty(String.valueOf(thu)));
        m.put(COL_TIET, new SimpleStringProperty(String.valueOf(tiet)));
        dsLop.forEach(l -> m.put(l,new SimpleStringProperty("")));
        return m;
    }

    /* ------------- dựng cột động ------------------------ */
    private void buildColumns(List<String> dsLop){
        table.getColumns().clear();

        TableColumn<Map<String,StringProperty>,String> colThu = new TableColumn<>(COL_THU);
        colThu.setPrefWidth(45);
        colThu.setCellValueFactory(d -> d.getValue().get(COL_THU));
        table.getColumns().add(colThu);

        TableColumn<Map<String,StringProperty>,String> colTiet = new TableColumn<>(COL_TIET);
        colTiet.setPrefWidth(45);
        colTiet.setCellValueFactory(d -> d.getValue().get(COL_TIET));
        table.getColumns().add(colTiet);

        for (String lop : dsLop) {
            TableColumn<Map<String,StringProperty>,String> c = new TableColumn<>(lop);
            c.setPrefWidth(110);
            c.setCellValueFactory(d -> d.getValue().get(lop));

            // cho phép sửa trực tiếp
            c.setCellFactory(TextFieldTableCell.forTableColumn());
            c.setOnEditCommit(e -> {
                Map<String,StringProperty> row = e.getRowValue();
                row.get(lop).set(e.getNewValue());

                int thu  = Integer.parseInt(row.get(COL_THU ).get());
                int tiet = Integer.parseInt(row.get(COL_TIET).get());
                updateCellDB(cboMaTKB.getEditor().getText().trim(),
                        lop, thu, tiet, e.getNewValue());
            });
            table.getColumns().add(c);
        }
    }

    /* -------------- ghi DB khi sửa ô -------------------- */
    private void updateCellDB(String maTKB,String maLop,int thu,int tiet,String mg){
        String[] p = mg.split("-",2);
        if (p.length<2) return;                      // sai định dạng
        String maMH = p[0].trim(), maGV = p[1].trim();

        try (Connection c = DatabaseConnection.getConnection()){

            boolean existed;
            String chk = "SELECT COUNT(*) FROM CHITIETTKB "
                    + "WHERE MaTKB=? AND MaLop=? AND Thu=? AND Tiet=?";
            try (PreparedStatement ps = c.prepareStatement(chk)){
                ps.setString(1,maTKB); ps.setString(2,maLop);
                ps.setInt(3,thu);      ps.setInt(4,tiet);
                ResultSet rs = ps.executeQuery(); rs.next();
                existed = rs.getInt(1)>0;
            }

            String sql = existed
                    ? "UPDATE CHITIETTKB SET MaMH=?, MaGV=? "
                    + "WHERE MaTKB=? AND MaLop=? AND Thu=? AND Tiet=?"
                    : "INSERT INTO CHITIETTKB(MaTKB,Thu,Tiet,MaLop,MaGV,MaMH) "
                    + "VALUES (?,?,?,?,?,?)";

            try (PreparedStatement ps = c.prepareStatement(sql)){
                ps.setString(1,maMH);
                ps.setString(2,maGV);
                ps.setString(3,maTKB);
                ps.setString(4,maLop);
                ps.setInt   (5,thu);
                ps.setInt   (6,tiet);
                ps.executeUpdate();
            }

        } catch (SQLException ex){ error(ex); }
    }

    /* ================= CRUD header ====================== */

    @FXML private void handleAddTKB() {
        String ma = cboMaTKB.getEditor().getText().trim();
        if (ma.isEmpty()) { alert(Alert.AlertType.WARNING,"Thiếu dữ liệu","Nhập mã TKB!"); return; }

        String sql = """
            INSERT INTO THOIKHOABIEU(MaTKB,NgayLap,NgayApDung,Buoi,NguoiTao,MaHK)
            VALUES(?,SYSDATE,SYSDATE,'Chiều','ADMIN',NULL)
            """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, ma); ps.executeUpdate();
            alert(Alert.AlertType.INFORMATION,"Thành công","Đã tạo TKB "+ma);
            loadMaTKBList();                 // làm mới danh sách
            cboMaTKB.getSelectionModel().select(ma);
            handleLoad();

        } catch (SQLIntegrityConstraintViolationException dup) {
            alert(Alert.AlertType.WARNING,"Trùng mã","Mã TKB đã tồn tại!");
        } catch (SQLException ex) { error(ex); }
    }

    @FXML private void handleDeleteTKB() {
        String ma = cboMaTKB.getEditor().getText().trim();
        if (ma.isEmpty()) return;

        Alert cf = new Alert(Alert.AlertType.CONFIRMATION,
                "Xoá TKB "+ma+" ?", ButtonType.YES,ButtonType.NO);
        cf.setHeaderText("Xác nhận xoá");
        if (cf.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;

        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement d1 = c.prepareStatement(
                    "DELETE FROM CHITIETTKB WHERE MaTKB=?");
                 PreparedStatement d2 = c.prepareStatement(
                         "DELETE FROM THOIKHOABIEU WHERE MaTKB=?")) {
                d1.setString(1,ma); d1.executeUpdate();
                d2.setString(1,ma); d2.executeUpdate();
                c.commit();
            }
            table.getItems().clear(); table.getColumns().clear();
            alert(Alert.AlertType.INFORMATION,"Đã xoá","Đã xoá TKB "+ma);
            loadMaTKBList();

        } catch (SQLException ex) { error(ex); }
    }

    @FXML private void handleUpdateTKB() {
        String ma = cboMaTKB.getEditor().getText().trim();
        if (ma.isEmpty()) return;
        String sql = "UPDATE THOIKHOABIEU SET NgayApDung=SYSDATE WHERE MaTKB=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,ma);
            int n=ps.executeUpdate();
            alert(Alert.AlertType.INFORMATION,"Cập nhật",
                    n>0?"Đã lưu TKB "+ma:"Không tìm thấy TKB!");
        } catch (SQLException ex){ error(ex); }
    }

    /* ============== tiện ích UI ========================= */
    private void alert(Alert.AlertType t,String h,String m){
        Alert a = new Alert(t,m,ButtonType.OK); a.setHeaderText(h); a.showAndWait();
    }
    private void error(Exception ex){
        ex.printStackTrace();
        alert(Alert.AlertType.ERROR,"Lỗi",ex.getMessage());
    }
}
