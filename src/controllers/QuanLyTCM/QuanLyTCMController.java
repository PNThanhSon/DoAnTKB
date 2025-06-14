package controllers.QuanLyTCM;

import dao.GiaoVienDAO;
import dao.ToChuyenMonDAO;
import entities.GiaoVien;
import entities.ToChuyenMon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.sql.SQLException;
import java.util.Optional;


public class QuanLyTCMController {

    @FXML private BorderPane QLTCMBorderPane;

    @FXML private TextField searchField;
    @FXML private TextField searchFieldgv;
    @FXML private TableView<ToChuyenMon> tableToChuyenMon;
    @FXML private Text txtThongBao;
    @FXML private TableView<GiaoVien> tableGiaoVien;
    @FXML private TitledPane infoFormID;


    @FXML private TableColumn<GiaoVien, String> colMaGV;
    @FXML private TableColumn<GiaoVien, String> colHoGV;
    @FXML private TableColumn<GiaoVien, String> colTenGV;
    @FXML private TableColumn<GiaoVien, String> colGioiTinh;
    @FXML private TableColumn<GiaoVien, String> colSDT;
    @FXML private TableColumn<GiaoVien, String> colEmail;
    @FXML private TableColumn<ToChuyenMon, String> colMaTCMgv;
    @FXML private TableColumn<GiaoVien, Integer> colSoTietQuyDinh;
    @FXML private TableColumn<GiaoVien, Integer> colSoTietThucHien;
    @FXML private TableColumn<GiaoVien, Integer> colSoTietDuThieu;
    @FXML private TableColumn<GiaoVien, String> colMatKhau;
    @FXML private TableColumn<GiaoVien, String> colGhiChu;


    @FXML private TableColumn<ToChuyenMon, String> colMaTCM;
    @FXML private TableColumn<ToChuyenMon, String> colTenTCM;
    @FXML private TableColumn<ToChuyenMon, String> colTotruong;
    @FXML private TableColumn<ToChuyenMon, String> colTopho;
    @FXML private TableColumn<ToChuyenMon, Integer> colSLGV;
    

    private final ContextMenu contextMenutcm = new ContextMenu();
    private final ContextMenu contextMenugv = new ContextMenu();

    private ObservableList<ToChuyenMon> danhSachToChuyenMonCurrent = FXCollections.observableArrayList();
    private final ToChuyenMonDAO toChuyenMonDAO = new ToChuyenMonDAO();
    private FilteredList<ToChuyenMon> filteredListTCM;
    private int selectedIndexTCM;
    private int selectedIndexGV;

    private ObservableList<GiaoVien> danhSachGiaoVienCurrent = FXCollections.observableArrayList();
    private final GiaoVienDAO giaoVienDAO = new GiaoVienDAO();
    private FilteredList<GiaoVien> filteredListGV;
    
    @FXML
    public void initialize() throws SQLException {
        Platform.runLater(() -> QLTCMBorderPane.requestFocus()); // tránh focus vào ô nhập liệu
        //   infoFormID.setAnimated(false); // tránh hiệu ứng

        setupTableColumnsTCM(); // property để tự động update dữ liệu vào bảng
        loadDataTCMFromDB();
        setupTableColumnsGV();
        loadDataGVFromDB();
        initContextMenuTCM();
        initContextMenuGV();

        // Gắn bộ lọc khi người dùng nhập
        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilterPredicateTCM());
        searchFieldgv.textProperty().addListener((obs, oldVal, newVal) -> updateFilterPredicateGV());

    }

    /* ------------------------setup ----------------------------------*/
    /* ------------------------------------------------------------------------*/
    private void initContextMenuTCM() {
        MenuItem xemItem = new MenuItem("Xem danh sách");
        xemItem.setOnAction(event -> xuLyXemGiaoVienTrongTo());

        MenuItem suaItem = new MenuItem("Sửa tên tổ");
        suaItem.setOnAction(event -> handle_Sua());

        MenuItem xoaItem = new MenuItem("Xóa tổ ");
        xoaItem.setOnAction(event -> xuLyXoaTCM());

        contextMenutcm.getItems().addAll(xemItem, suaItem, xoaItem);

        tableToChuyenMon.setOnContextMenuRequested(event ->
                contextMenutcm.show(tableToChuyenMon, event.getScreenX(), event.getScreenY()));
    }
    private void initContextMenuGV() {
        MenuItem promote1 = new MenuItem("Bổ nhiệm tổ trưởng");
        promote1.setOnAction(event -> {
            try { xuLyBoNhiem(true);}
            catch (SQLException e) { throw new RuntimeException(e);}
        });

        MenuItem promote2 = new MenuItem("Bổ nhiệm tổ phó");
        promote2.setOnAction(event -> {
            try { xuLyBoNhiem(false);}
            catch (SQLException e) { throw new RuntimeException(e);}
        });

        MenuItem xoaItem = new MenuItem("Xóa khỏi tổ");
        xoaItem.setOnAction(event -> {
            try { xuLyXoaGVKhoiTo();}
            catch (SQLException e) { throw new RuntimeException(e);}
        });

        contextMenugv.getItems().addAll(promote1, promote2, xoaItem);

        tableGiaoVien.setOnContextMenuRequested(event ->
                contextMenugv.show(tableGiaoVien, event.getScreenX(), event.getScreenY()));
    }

    private void setupTableColumnsTCM() {
        colMaTCM.setCellValueFactory(new PropertyValueFactory<>("maTCM"));
        colTenTCM.setCellValueFactory(new PropertyValueFactory<>("tenTCM"));
        colTotruong.setCellValueFactory(new PropertyValueFactory<>("toTruong"));
        colTopho.setCellValueFactory(new PropertyValueFactory<>("toPho"));
        colSLGV.setCellValueFactory(new PropertyValueFactory<>("slGV"));

        tableToChuyenMon.setEditable(true);
        colTenTCM.setCellValueFactory(cellData -> cellData.getValue().tenTCMProperty());
        colTenTCM.setCellFactory(TextFieldTableCell.forTableColumn());
        // nhấn enter thì lưu
        colTenTCM.setOnEditCommit(event -> {
            ToChuyenMon tcm = event.getRowValue();
            String newValue = event.getNewValue();
            tcm.setTenTCM(newValue.trim());
            if (newValue.trim().isEmpty() || newValue.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Thông tin trống", "Nhập đầy đủ!!");

            } else {
                try {
                    // check xem đang thêm hay đang sửa
                    if (tcm.getMaTCM() == null || tcm.getMaTCM().isBlank()) {
                        toChuyenMonDAO.themToChuyenMon(tcm);

                    } else {
                        toChuyenMonDAO.capNhatThongTin(tcm);
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                loadDataTCMFromDB();
                updateFilterPredicateTCM();
            }
        });

    }
    private void setupTableColumnsGV() {
        colMaGV.setCellValueFactory(new PropertyValueFactory<>("maGV"));
        colHoGV.setCellValueFactory(new PropertyValueFactory<>("hoGV"));
        colTenGV.setCellValueFactory(new PropertyValueFactory<>("tenGV"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colMaTCMgv.setCellValueFactory(new PropertyValueFactory<>("maTCM"));
        colSoTietQuyDinh.setCellValueFactory(new PropertyValueFactory<>("soTietQuyDinh"));
        colSoTietThucHien.setCellValueFactory(new PropertyValueFactory<>("soTietThucHien"));
        colSoTietDuThieu.setCellValueFactory(new PropertyValueFactory<>("soTietDuThieu"));
        colMatKhau.setCellValueFactory(new PropertyValueFactory<>("matKhau"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
    }



    /* ------------------------------------------------------------------------*/
    /* ------------------------setup-----------------------------------------*/

    private void reloadALL() {
        loadDataGVFromDB();
        loadDataTCMFromDB();
        updateFilterPredicateTCM();
        updateFilterPredicateGV();
    }

    // k quan trong
    @FXML
    private void handleSearch() {
        updateFilterPredicateTCM();
        updateFilterPredicateGV();
    }

    /* ------------------------2 button dưới cùng----------------------------------*/
    /* ------------------------------------------------------------------------*/
    @FXML
    private void handleLamMoi() {
        searchField.clear();
        // ListToChuyenMon.setValue("Tất cả");
        tableGiaoVien.getSelectionModel().clearSelection();
        tableToChuyenMon.getSelectionModel().clearSelection();
        infoFormID.setExpanded(false);
        infoFormID.setText("Danh sách giáo viên toàn trường");
        reloadALL();
    }
    @FXML
    private void xuLyThemTCM() {
        ToChuyenMon newTCM = new ToChuyenMon("", "","","", 0); // chỉ cần tên TCM
        danhSachToChuyenMonCurrent.add(newTCM);

        int lastIndex = tableToChuyenMon.getItems().size() - 1;
        tableToChuyenMon.scrollTo(lastIndex);
        tableToChuyenMon.getSelectionModel().select(lastIndex);
        tableToChuyenMon.edit(lastIndex, colTenTCM);
    }

    /* ------------------------------------------------------------------------*/
    /* ------------------------2 btn dưới cùng----------------------------------*/



    /* ------------------------menu to chuyen mon----------------------------------*/
    /* ------------------------------------------------------------------------*/
    // nút được tạo trong controller
    private void handle_Sua() {
        ToChuyenMon selected = tableToChuyenMon.getSelectionModel().getSelectedItem();
        if (selected != null) {
            int index = tableToChuyenMon.getItems().indexOf(selected);
            tableToChuyenMon.edit(index, colTenTCM); // Chuyển thành ô nhập
        }
    }


    // nút được tạo trong controller
    private void xuLyXoaTCM() {
        ToChuyenMon selected = tableToChuyenMon.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn tổ chuyên môn để xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                toChuyenMonDAO.xoaToChuyenMon(selected);
                reloadALL();
                showThongBao("Xóa thành công!");
                showAlert(Alert.AlertType.INFORMATION, "Đã xóa", "tổ chuyên môn đã bị xóa.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa: Lỗi ràng buộc khóa ngoại: " + e.getMessage());
            }
        }
    }
    private void xuLyXemGiaoVienTrongTo() {
        searchFieldgv.clear();
        updateFilterPredicateGV();
        infoFormID.setExpanded(true);
    }

    /* ------------------------------------------------------------------------*/
    /* ------------------------menu to chuyen mon----------------------------------*/


    /* ------------------------menu giao vien----------------------------------*/
    /* ------------------------------------------------------------------------*/
    private void xuLyXoaGVKhoiTo() throws SQLException {
        GiaoVien gv = tableGiaoVien.getSelectionModel().getSelectedItem();
        ToChuyenMon tcm = tableToChuyenMon.getSelectionModel().getSelectedItem();
        int selectedIndex = tableToChuyenMon.getSelectionModel().getSelectedIndex();
        if (tcm == null) {
            showThongBao("Hãy chọn tổ chuyên môn trước");
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn tổ chuyên môn để xóa.");
            return;
        }

        // nếu đang là tổ trưởng, phó -> cập nhật tcm
        if (gv.getMaGV().equals(tcm.getToTruong())) {
            tcm.setToTruong("");
            toChuyenMonDAO.capNhatThongTin(tcm);
        }
        if (gv.getMaGV().equals(tcm.getToPho())) {
            tcm.setToPho("");
            toChuyenMonDAO.capNhatThongTin(tcm);
        }
        // xóa giao vien khỏi tổ
        gv.setMaTCM("");
        giaoVienDAO.capNhatTCM(gv);
        // reload
        loadDataGVFromDB();
        loadDataTCMFromDB();
        tableToChuyenMon.getSelectionModel().select(selectedIndex);
        updateFilterPredicateTCM();
        updateFilterPredicateGV();
    }

    // bổ nhiệm tổ trưởng, tổ phó
    private void xuLyBoNhiem(Boolean c) throws SQLException {
        ToChuyenMon tcm = tableToChuyenMon.getSelectionModel().getSelectedItem();
        GiaoVien gv = tableGiaoVien.getSelectionModel().getSelectedItem();
        if (tcm == null) {
            showThongBao("Hãy chọn tổ chuyên môn trước");
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn tổ chuyên môn để xóa.");
            return;
        }
        // check vị trí bổ nhiệm
        if (c) tcm.setToTruong(gv.getMaGV());
        else tcm.setToPho(gv.getMaGV());

        // nếu giáo viên chưa có tổ chuyển môn hoặc ở tổ chuyên môn khác, khi bổ nhiệm sẽ set về tổ này
        if (gv.getMaTCM() == null) {
            gv.setMaTCM(tcm.getMaTCM());
            giaoVienDAO.capNhatThongTin(gv);
        } else if(!gv.getMaTCM().equals(tcm.getMaTCM())) {
            gv.setMaTCM(tcm.getMaTCM());
            giaoVienDAO.capNhatThongTin(gv);
        }
        toChuyenMonDAO.capNhatThongTin(tcm);
        loadDataTCMFromDB();
        loadDataGVFromDB();
        updateFilterPredicateGV();
        updateFilterPredicateTCM();
        infoFormID.setExpanded(false);

    }
    /* ------------------------------------------------------------------------*/
    /* ------------------------menu giao vien----------------------------------*/


    /* ------------------------diglog thong bao----------------------------------*/
    /* ------------------------------------------------------------------------*/
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    // để tạo 1 thông báo nhỏ khi thao tác thành công (sẽ tự động ẩn) (cái này kh quan trọng)
    private void showThongBao(String message) {
        txtThongBao.setText(message);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> txtThongBao.setText(""))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }
    public void showConfirmationDialog(String title, String content, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        ButtonType confirmButton = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirmButton) {
            onConfirm.run();
        }
    }
    /* ------------------------------------------------------------------------*/
    /* ------------------------diglog thong bao----------------------------------*/



    /* ------------------------load data và update----------------------------------*/
    /* ------------------------------------------------------------------------*/

    private void loadDataTCMFromDB() {
        try {
            danhSachToChuyenMonCurrent.setAll(toChuyenMonDAO.getDanhSachTCM());
            filteredListTCM = new FilteredList<>(danhSachToChuyenMonCurrent, p -> true);
            tableToChuyenMon.setItems(filteredListTCM);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể lấy danh sách tổ chuyên môn: " + e.getMessage());
        }
    }
    private void loadDataGVFromDB() {
        try {
            danhSachGiaoVienCurrent.setAll(giaoVienDAO.TracuuGiaoVien(""));
            filteredListGV = new FilteredList<>(danhSachGiaoVienCurrent, p -> true);
            tableGiaoVien.setItems(filteredListGV);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể lấy danh sách giáo viên: " + e.getMessage());
        }
    }

    private void updateFilterPredicateTCM() {
        String keyword = searchField.getText().trim().toUpperCase();
//        String tenTCM = ListToChuyenMon.getValue();
        filteredListTCM.setPredicate(tcm -> keyword.isEmpty() ||
                tcm.getMaTCM().trim().toUpperCase().contains(keyword) ||
                tcm.getTenTCM().trim().toUpperCase().contains(keyword));
    }
    private void updateFilterPredicateGV() {
        String keyword = searchFieldgv.getText().trim().toUpperCase();
        ToChuyenMon tcm = tableToChuyenMon.getSelectionModel().getSelectedItem();
        String texttcm = (tcm == null) ? "toàn trường" : tcm.getTenTCM();
        infoFormID.setText("Danh sách giáo viên của " + texttcm);
        filteredListGV.setPredicate(gv -> {
            boolean matchSearch = keyword.isEmpty() ||
                    gv.getMaGV().trim().toUpperCase().contains(keyword) ||
                    gv.getTenGV().trim().toUpperCase().contains(keyword)||
                    gv.getHoGV().trim().toUpperCase().contains(keyword);
            boolean matchTCM = true;
            if (gv != null && tcm != null) {
                matchTCM = tcm.getMaTCM().equals(gv.getMaTCM());
            }
            return matchSearch && matchTCM;
        });
    }
}
