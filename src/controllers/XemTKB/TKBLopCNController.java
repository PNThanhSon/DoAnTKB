package controllers.XemTKB;

import dao.ThoiKhoaBieuDAO;
import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;

public class TKBLopCNController {

    @FXML private Label tkbBuoiLabel; // Label mới để hiển thị buổi của TKB
    @FXML private Label tkbLopCNLabel; // Label mới để hiển thị lớp chủ nhiệm
    @FXML private ComboBox<HocKy> hocKyComboBox;
    @FXML private ComboBox<ThoiKhoaBieu> tkbComboBox;
    @FXML private ComboBox<Lop> lopComboBox;
    @FXML private HBox hBoxLop;

    // TableView chính (thay thế cho sangTableView)
    @FXML private TableView<TietHocData> tkbTableView; // Đổi tên từ sangTableView
    @FXML private TableColumn<TietHocData, String> tietColumn; // Đổi tên từ sangTietColumn
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu2Column; // Đổi tên từ sangThu2Column
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu3Column; // Đổi tên
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu4Column; // Đổi tên
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu5Column; // Đổi tên
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu6Column; // Đổi tên
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu7Column; // Đổi tên

    private GiaoVien currentGiaoVien;
    private final ThoiKhoaBieuDAO thoiKhoaBieuDAO;

    // ObservableList để chứa dữ liệu cho TableView
    private ObservableList<TietHocData> tkbDataList;

    private static final int SO_TIET_MOI_BUOI = 5;
    private static final double ROW_HEIGHT = 28.0;
    private static final double ESTIMATED_HEADER_HEIGHT = 30.0;
    private String maLop = "";
    ThoiKhoaBieu selectedTKB = null;

    public TKBLopCNController() {
        thoiKhoaBieuDAO = new ThoiKhoaBieuDAO();
    }

    @FXML
    public void initialize() {
        // Thiết lập cột cho bảng
        setupTableColumns(tietColumn, thu2Column, thu3Column, thu4Column, thu5Column, thu6Column, thu7Column);

        // Khởi tạo ObservableList (giờ chỉ có một)
        tkbDataList = FXCollections.observableArrayList();

        // Khởi tạo các hàng rỗng ban đầu cho bảng
        initializeTableRows(tkbDataList); // Không cần truyền buổi nữa

        // Gán dữ liệu vào TableView
        tkbTableView.setItems(tkbDataList);
        tkbBuoiLabel.setText("Buổi: (chưa chọn TKB)"); // Label thông báo buổi

        tkbTableView.setFixedCellSize(ROW_HEIGHT);
        // Chiều cao = chiều cao header + (số dòng * chiều cao mỗi dòng) + một chút padding nhỏ (2px để tránh lỗi hiển thị thanh cuộn nhỏ)
        double calculatedTableHeight = ESTIMATED_HEADER_HEIGHT + (SO_TIET_MOI_BUOI * ROW_HEIGHT) + 2.0;

        tkbTableView.setPrefHeight(calculatedTableHeight);
        tkbTableView.setMinHeight(calculatedTableHeight); // Ngăn không cho bảng co lại nhỏ hơn
        tkbTableView.setMaxHeight(calculatedTableHeight); // Ngăn không cho bảng giãn ra lớn hơn

        // Tải danh sách học kỳ
        loadHocKyOptions();
        tkbComboBox.setDisable(true);
        lopComboBox.setDisable(true);
    }

    public void initData(GiaoVien giaoVien) {
        this.currentGiaoVien = giaoVien;
        maLop = thoiKhoaBieuDAO.getTenLopCN(currentGiaoVien.getMaGV());
        tkbLopCNLabel.setText("THỜI KHÓA BIỂU LỚP " + maLop);
        hBoxLop.setVisible(false);
        hBoxLop.setManaged(false);
    }

    public void initDataAdmin() {
        tkbLopCNLabel.setText("THỜI KHÓA BIỂU LỚP");
        loadLopOptions();
    }

    private void setupTableColumns(TableColumn<TietHocData, String> tietCol,
                                   TableColumn<TietHocData, ChiTietTKB> colThu2,
                                   TableColumn<TietHocData, ChiTietTKB> colThu3,
                                   TableColumn<TietHocData, ChiTietTKB> colThu4,
                                   TableColumn<TietHocData, ChiTietTKB> colThu5,
                                   TableColumn<TietHocData, ChiTietTKB> colThu6,
                                   TableColumn<TietHocData, ChiTietTKB> colThu7) {
        tietCol.setCellValueFactory(new PropertyValueFactory<>("tiet"));
        colThu2.setCellValueFactory(new PropertyValueFactory<>("thu2"));
        colThu3.setCellValueFactory(new PropertyValueFactory<>("thu3"));
        colThu4.setCellValueFactory(new PropertyValueFactory<>("thu4"));
        colThu5.setCellValueFactory(new PropertyValueFactory<>("thu5"));
        colThu6.setCellValueFactory(new PropertyValueFactory<>("thu6"));
        colThu7.setCellValueFactory(new PropertyValueFactory<>("thu7"));
    }

    private void initializeTableRows(ObservableList<TietHocData> dataList) {
        dataList.clear();
        for (int i = 1; i <= SO_TIET_MOI_BUOI; i++) {
            dataList.add(new TietHocData("Tiết " + i));
        }
    }

    private void loadHocKyOptions() {
        List<HocKy> hocKyList = thoiKhoaBieuDAO.getDanhSachHocKy();
        hocKyComboBox.getItems().clear();
        if (hocKyList != null && !hocKyList.isEmpty()) {
            hocKyComboBox.setItems(FXCollections.observableArrayList(hocKyList));
        } else {
            hocKyComboBox.setPromptText("Không có học kỳ");
            tkbComboBox.setDisable(true);
            lopComboBox.setDisable(true);
        }
    }

    private void loadLopOptions() {
        List<Lop> lopList = thoiKhoaBieuDAO.getDanhSachTatCaLop();
        lopComboBox.getItems().clear();
        if (lopList != null && !lopList.isEmpty()) {
            lopComboBox.setItems(FXCollections.observableArrayList(lopList));
        } else {
            lopComboBox.setPromptText("Không có lớp");
        }
    }

    @FXML
    private void handleLopSelection(ActionEvent event) {
        maLop = lopComboBox.getSelectionModel().getSelectedItem().getMaLop();
        tkbLopCNLabel.setText("THỜI KHÓA BIỂU LỚP " + maLop);
        if (selectedTKB != null) loadThoiKhoaBieuData(selectedTKB, maLop);
    }

    @FXML
    private void handleHocKySelection(ActionEvent event) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        tkbComboBox.getItems().clear();
        tkbComboBox.setDisable(true);
        lopComboBox.setDisable(true);
        tkbBuoiLabel.setText("Buổi: (chưa chọn TKB)");
        clearTableData();

        if (selectedHocKy != null) {
            List<ThoiKhoaBieu> tkbList = thoiKhoaBieuDAO.getDanhSachThoiKhoaBieuTheoHocKy(selectedHocKy.getMaHK());
            if (tkbList != null && !tkbList.isEmpty()) {
                tkbComboBox.setItems(FXCollections.observableArrayList(tkbList));
                tkbComboBox.setDisable(false);
                lopComboBox.setDisable(false);
            } else {
                tkbComboBox.setPromptText("Không có TKB cho học kỳ này");
            }
        } else {
            tkbComboBox.setPromptText("Chọn TKB để xem");
        }
    }

    @FXML
    private void handleTkbSelection(ActionEvent event) {
        selectedTKB = tkbComboBox.getSelectionModel().getSelectedItem();
        clearTableData(); // Xóa dữ liệu bảng cũ

        if (selectedTKB != null) {
            // Cập nhật label buổi
            if (selectedTKB.getBuoi() != null && !selectedTKB.getBuoi().isEmpty()) {
                tkbBuoiLabel.setText("Buổi: " + selectedTKB.getBuoi().toUpperCase());
            } else {
                tkbBuoiLabel.setText("Buổi: (Không xác định)");
            }

            loadThoiKhoaBieuData(selectedTKB, maLop);
        } else {
            tkbBuoiLabel.setText("Buổi: (chưa chọn TKB)");
            if (selectedTKB == null) {
                System.out.println("Không có TKB nào được chọn.");
            }
        }
    }

    private void loadThoiKhoaBieuData(ThoiKhoaBieu selectedTKB, String maLop) {
        // Khởi tạo lại các hàng cho bảng (giờ chỉ có một bảng tkbDataList)
        initializeTableRows(tkbDataList);

        List<ChiTietTKB> chiTietList = thoiKhoaBieuDAO.getChiTietTKBForLopCN(selectedTKB.getMaTKB(), maLop);

        if (chiTietList == null || chiTietList.isEmpty()) {
            System.out.println("Không có chi tiết TKB nào cho " + maLop + " với TKB " + selectedTKB.getMaTKB());
        } else {
            for (ChiTietTKB ct : chiTietList) {
                int tietIndex = ct.getTiet() - 1; // Giả sử tiết 1 là index 0

                if (tietIndex >= 0 && tietIndex < SO_TIET_MOI_BUOI) {
                    TietHocData rowData = tkbDataList.get(tietIndex);
                    switch (ct.getThu()) { // getThu() trả về 2-7
                        case 2: rowData.setThu2(ct); break;
                        case 3: rowData.setThu3(ct); break;
                        case 4: rowData.setThu4(ct); break;
                        case 5: rowData.setThu5(ct); break;
                        case 6: rowData.setThu6(ct); break;
                        case 7: rowData.setThu7(ct); break;
                        default:
                            System.err.println("Thứ không hợp lệ: " + ct.getThu());
                            break;
                    }
                } else {
                    System.err.println("Tiết học không hợp lệ (ngoài khoảng 1-" + SO_TIET_MOI_BUOI + "): Tiết " + ct.getTiet());
                }
            }
        }
        tkbTableView.refresh(); // Refresh TableView để hiển thị dữ liệu mới
    }

    private void clearAllUIData() {
        hocKyComboBox.getItems().clear();
        hocKyComboBox.setPromptText("Chọn học kỳ");
        tkbComboBox.getItems().clear();
        tkbComboBox.setPromptText("Chọn TKB để xem");
        tkbComboBox.setDisable(true);
        tkbBuoiLabel.setText("Buổi: (chưa chọn TKB)");
        clearTableData();
    }

    private void clearTableData() {
        initializeTableRows(tkbDataList); // Chỉ cần khởi tạo lại một danh sách
        tkbTableView.refresh(); // Chỉ cần refresh một bảng
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}