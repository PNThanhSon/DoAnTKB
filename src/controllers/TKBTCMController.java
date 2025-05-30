package controllers;

import dao.ThoiKhoaBieuDAO;
import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane; // Thêm ScrollPane
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TKBTCMController {

    @FXML private Label tkbBuoiLabel;
    @FXML private Label tkbTCMLabel;
    @FXML private ComboBox<HocKy> hocKyComboBox;
    @FXML private ComboBox<ThoiKhoaBieu> tkbComboBox;
    @FXML private ComboBox<NhomMonHocDisplay> monHocComboBox;
    @FXML private ScrollPane tkbScrollPane; // Để chứa GridPane
    @FXML private HBox hBoxTo;
    @FXML private ComboBox<ToChuyenMon> toComboBox;
    private GridPane tkbGridPane;

    private GiaoVien currentGiaoVien;
    private final ThoiKhoaBieuDAO thoiKhoaBieuDAO;

    // Danh sách này không trực tiếp bind vào TableView nữa, mà dùng để build GridPane
    private final ObservableList<GiaoVienLichDayData> danhSachLichDayGV;

    private static final int SO_TIET_MOI_BUOI = 5;
    private static final String[] THU_TRONG_TUAN_LABEL = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
    private static final int[] THU_TRONG_TUAN_DBVAL = {2, 3, 4, 5, 6, 7}; // Giá trị Thứ trong CSDL
    private String maTCM = "";

    public TKBTCMController() {
        thoiKhoaBieuDAO = new ThoiKhoaBieuDAO();
        danhSachLichDayGV = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        tkbBuoiLabel.setText("Buổi: (chưa chọn TKB)");
        tkbTCMLabel.setText("TKB Tổ Chuyên Môn: (chưa có thông tin)");

        // Khởi tạo GridPane và đặt nó vào ScrollPane
        tkbGridPane = new GridPane();
        tkbGridPane.setGridLinesVisible(true);
        tkbGridPane.getStyleClass().add("tkb-grid-tcm"); // Thêm style class để tùy chỉnh CSS
        tkbGridPane.setAlignment(Pos.CENTER); // Căn giữa GridPane trong ScrollPane

        // Đặt GridPane làm content của ScrollPane
        if (tkbScrollPane != null) {
            tkbScrollPane.setContent(tkbGridPane);
            tkbScrollPane.setFitToWidth(true); // Quan trọng để GridPane co giãn theo chiều rộng ScrollPane
        } else {
            System.err.println("Lỗi: tkbScrollPane chưa được inject từ FXML.");
        }


        loadHocKyOptions();
        tkbComboBox.setDisable(true);
        monHocComboBox.setDisable(true);
        toComboBox.setDisable(true);
    }

    public void initData(GiaoVien giaoVien) {
        this.currentGiaoVien = giaoVien;
        maTCM = currentGiaoVien.getMaTCM();
        if (currentGiaoVien != null && currentGiaoVien.getMaTCM() != null) {
            String maTCM = currentGiaoVien.getMaTCM();
            String tenTCM = thoiKhoaBieuDAO.getTenTCMByMaTCM(maTCM); // Dùng hàm mới
            tkbTCMLabel.setText("THỜI KHÓA BIỂU " + (tenTCM != null ? tenTCM.toUpperCase() : maTCM.toUpperCase()));
            hBoxTo.setVisible(false);
            hBoxTo.setManaged(false);
            loadMonHocOptions(maTCM);
        } else {
            tkbTCMLabel.setText("TKB Tổ Chuyên Môn: Lỗi - Không có thông tin GV hoặc TCM");
            monHocComboBox.setDisable(true);
            hocKyComboBox.setDisable(true); // Cũng vô hiệu hóa nếu không có TCM
            tkbComboBox.setDisable(true);
        }
    }

    public void initDataAdmin() {
        tkbTCMLabel.setText("THỜI KHÓA BIỂU TỔ CHUYÊN MÔN");
        loadToChuyenMonOptions();
    }

    private void loadToChuyenMonOptions() {
        List<ToChuyenMon> toList = thoiKhoaBieuDAO.getDanhSachTatCaTCM();
        toComboBox.getItems().clear();
        if (toList != null && !toList.isEmpty()) {
            toComboBox.setItems(FXCollections.observableArrayList(toList));
        } else {
            toComboBox.setPromptText("Không có Tổ Chuyên Môn");
        }
    }

    @FXML
    private void handleToSelection(ActionEvent event) {
        ToChuyenMon selectedTo = toComboBox.getSelectionModel().getSelectedItem();
        maTCM = selectedTo.getMaTCM();
        String tenTCM = thoiKhoaBieuDAO.getTenTCMByMaTCM(maTCM);
        tkbTCMLabel.setText("THỜI KHÓA BIỂU " + (tenTCM != null ? tenTCM.toUpperCase() : maTCM.toUpperCase()));
        loadMonHocOptions(maTCM);
        monHocComboBox.setDisable(false);
    }

    private void loadHocKyOptions() {
        List<HocKy> hocKyList = thoiKhoaBieuDAO.getDanhSachHocKy();
        hocKyComboBox.getItems().clear();
        if (hocKyList != null && !hocKyList.isEmpty()) {
            hocKyComboBox.setItems(FXCollections.observableArrayList(hocKyList));
        } else {
            hocKyComboBox.setPromptText("Không có học kỳ");
        }
    }

    private void loadMonHocOptions(String maTCM) {
        List<NhomMonHocDisplay> nhomMonHocList = thoiKhoaBieuDAO.getNhomMonHocByTCM(maTCM);
        monHocComboBox.getItems().clear();
        if (nhomMonHocList != null && !nhomMonHocList.isEmpty()) {
            monHocComboBox.setItems(FXCollections.observableArrayList(nhomMonHocList));
        } else {
            monHocComboBox.setPromptText("TCM không có môn học");
            monHocComboBox.setDisable(true);
        }
    }

    @FXML
    private void handleHocKySelection(ActionEvent event) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        tkbComboBox.getItems().clear();
        tkbComboBox.setDisable(true);
        toComboBox.setDisable(true);
        tkbBuoiLabel.setText("Buổi: (chưa chọn TKB)");
        clearTKBDisplay();

        if (selectedHocKy != null) {
            List<ThoiKhoaBieu> tkbList = thoiKhoaBieuDAO.getDanhSachThoiKhoaBieuTheoHocKy(selectedHocKy.getMaHK());
            if (tkbList != null && !tkbList.isEmpty()) {
                tkbComboBox.setItems(FXCollections.observableArrayList(tkbList));
                tkbComboBox.setDisable(false);
            } else {
                tkbComboBox.setPromptText("Không có TKB cho học kỳ này");
            }
        } else {
            tkbComboBox.setPromptText("Chọn TKB");
        }
    }

    @FXML
    private void handleTkbSelection(ActionEvent event) {
        ThoiKhoaBieu selectedTKB = tkbComboBox.getSelectionModel().getSelectedItem();
        clearTKBDisplay();

        if (selectedTKB != null) {
            tkbBuoiLabel.setText("Buổi: " + (selectedTKB.getBuoi() != null ? selectedTKB.getBuoi().toUpperCase() : "N/A"));
            monHocComboBox.setDisable(false);
            toComboBox.setDisable(false);

        } else {
            tkbBuoiLabel.setText("Buổi: (chưa chọn TKB)");
            monHocComboBox.setDisable(true);
            toComboBox.setDisable(true);
        }
    }

    @FXML
    private void handleMonHocSelection(ActionEvent event) {
        NhomMonHocDisplay selectedNhomMonHoc = monHocComboBox.getSelectionModel().getSelectedItem();
        ThoiKhoaBieu selectedTKB = tkbComboBox.getSelectionModel().getSelectedItem();
        clearTKBDisplay();

        if (selectedNhomMonHoc != null && selectedTKB != null) {
            loadAndDisplayThoiKhoaBieuForTCM(selectedTKB, selectedNhomMonHoc);
        }
    }

    private void loadAndDisplayThoiKhoaBieuForTCM(ThoiKhoaBieu selectedTKB, NhomMonHocDisplay selectedNhomMonHoc) {
        danhSachLichDayGV.clear();
        List<String> cacMaMHCuaNhom = selectedNhomMonHoc.getDanhSachMaMH();
        if (cacMaMHCuaNhom.isEmpty()) {
            showAlert("Lỗi", "Nhóm môn học không chứa mã môn cụ thể.");
            buildTKBGrid(new ArrayList<>());
            return;
        }

        List<ChiTietTKB> tatCaChiTiet = thoiKhoaBieuDAO.getChiTietTKBForTCM(
                selectedTKB.getMaTKB(),
                cacMaMHCuaNhom,
                maTCM
        );

        if (tatCaChiTiet.isEmpty()) {
            showAlert("Thông báo", "Không có lịch dạy cho môn '" + selectedNhomMonHoc.getTenMonHocChung() + "' trong TKB này cho tổ chuyên môn.");
            buildTKBGrid(new ArrayList<>());
            return;
        }

        Map<String, List<ChiTietTKB>> lichDayTheoMaGV = tatCaChiTiet.stream()
                .filter(ct -> ct.getMaGV() != null)
                .collect(Collectors.groupingBy(ChiTietTKB::getMaGV));

        List<GiaoVienLichDayData> tempDataList = new ArrayList<>();
        for (Map.Entry<String, List<ChiTietTKB>> entry : lichDayTheoMaGV.entrySet()) {
            String maGV = entry.getKey();
            List<ChiTietTKB> lichCuaMotGV = entry.getValue();
            String hoTenGV = lichCuaMotGV.getFirst().getHoTenGV(); // Lấy tên từ chi tiết đầu tiên

            GiaoVienLichDayData gvData = new GiaoVienLichDayData(maGV, hoTenGV);
            for (ChiTietTKB ct : lichCuaMotGV) {
                gvData.setChiTiet(ct.getThu(), ct.getTiet(), ct);
            }
            tempDataList.add(gvData);
        }
        // Sắp xếp danh sách giáo viên theo tên để hiển thị nhất quán
        tempDataList.sort((gv1, gv2) -> gv1.getHoTenGV().compareToIgnoreCase(gv2.getHoTenGV()));
        danhSachLichDayGV.setAll(tempDataList);
        buildTKBGrid(danhSachLichDayGV);
    }

    private void buildTKBGrid(List<GiaoVienLichDayData> data) {
        tkbGridPane.getChildren().clear();
        tkbGridPane.getColumnConstraints().clear();
        tkbGridPane.getRowConstraints().clear();
        tkbGridPane.setPadding(new Insets(5));
        tkbGridPane.setHgap(1); // Khoảng cách ngang giữa các ô
        tkbGridPane.setVgap(1); // Khoảng cách dọc giữa các ô

        // --- Hàng 0: Tiêu đề Thứ (colspan=SO_TIET_MOI_BUOI) ---
        // Ô trống đầu tiên (cho cột tên GV)
        Label gvHeaderLabel = createHeaderLabel("Giáo viên");
        gvHeaderLabel.setMinHeight(50); // Đảm bảo đủ chỗ cho 2 dòng tiêu đề tiết
        tkbGridPane.add(gvHeaderLabel, 0, 0);
        GridPane.setRowSpan(gvHeaderLabel, 2); // Chiếm 2 hàng

        for (int i = 0; i < THU_TRONG_TUAN_LABEL.length; i++) {
            Label thuLabel = createHeaderLabel(THU_TRONG_TUAN_LABEL[i]);
            thuLabel.setStyle(thuLabel.getStyle() + "-fx-font-size: 14px;"); // Tăng size cho Thứ
            tkbGridPane.add(thuLabel, i * SO_TIET_MOI_BUOI + 1, 0);
            GridPane.setColumnSpan(thuLabel, SO_TIET_MOI_BUOI);
        }

        // --- Hàng 1: Tiêu đề Tiết (1 đến 5 cho mỗi Thứ) ---
        for (int i = 0; i < THU_TRONG_TUAN_LABEL.length; i++) {
            for (int tiet = 1; tiet <= SO_TIET_MOI_BUOI; tiet++) {
                Label tietLabel = createHeaderLabel(String.valueOf(tiet));
                tietLabel.setFont(Font.font("System", FontWeight.NORMAL, 10));
                tkbGridPane.add(tietLabel, i * SO_TIET_MOI_BUOI + tiet, 1);
            }
        }

        // --- Hàng Dữ liệu Giáo viên ---
        int currentGridRow = 2; // Bắt đầu từ hàng 2
        if (data.isEmpty()){
            Label noDataLabel = new Label("Không có dữ liệu lịch dạy để hiển thị.");
            noDataLabel.setPadding(new Insets(20));
            tkbGridPane.add(noDataLabel, 0, currentGridRow);
            GridPane.setColumnSpan(noDataLabel, THU_TRONG_TUAN_LABEL.length * SO_TIET_MOI_BUOI + 1);
            GridPane.setHalignment(noDataLabel, HPos.CENTER);
        } else {
            for (GiaoVienLichDayData gvData : data) {
                Label tenGvLabel = createDataCellLabel(gvData.getHoTenGV());
                tenGvLabel.setAlignment(Pos.CENTER_LEFT);
                tenGvLabel.setPadding(new Insets(0,0,0,5));
                tkbGridPane.add(tenGvLabel, 0, currentGridRow);

                for (int i = 0; i < THU_TRONG_TUAN_DBVAL.length; i++) {
                    int thuDbValue = THU_TRONG_TUAN_DBVAL[i];
                    for (int tiet = 1; tiet <= SO_TIET_MOI_BUOI; tiet++) {
                        ChiTietTKB chiTiet = gvData.getChiTiet(thuDbValue, tiet);
                        // toString() của ChiTietTKB đã được sửa để trả về MaLop
                        String cellText = (chiTiet != null) ? chiTiet.toString() : "";
                        Label dataCell = createDataCellLabel(cellText);
                        tkbGridPane.add(dataCell, i * SO_TIET_MOI_BUOI + tiet, currentGridRow);
                    }
                }
                currentGridRow++;
            }
        }


        // Cấu hình constraints cho cột
        // Cột tên GV
        ColumnConstraints colGVConstraints = new ColumnConstraints();
        colGVConstraints.setPrefWidth(150); // Chiều rộng cố định hoặc ưu tiên
        colGVConstraints.setMinWidth(100);
        colGVConstraints.setHgrow(Priority.NEVER);
        tkbGridPane.getColumnConstraints().add(colGVConstraints);

        // Các cột tiết (cho mỗi tiết trong mỗi thứ)
        for (int i = 0; i < THU_TRONG_TUAN_LABEL.length * SO_TIET_MOI_BUOI; i++) {
            ColumnConstraints colTietConstraints = new ColumnConstraints();
            colTietConstraints.setHgrow(Priority.SOMETIMES); // Cho phép co giãn
            colTietConstraints.setMinWidth(50); // Chiều rộng tối thiểu
            colTietConstraints.setPrefWidth(70); // Chiều rộng ưu tiên
            tkbGridPane.getColumnConstraints().add(colTietConstraints);
        }
    }

    private Label createHeaderLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 12));
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Để label chiếm hết ô
        label.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5px; -fx-border-color: #cccccc; -fx-border-width: 0.5px;");
        return label;
    }

    private Label createDataCellLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.NORMAL, 11));
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.setWrapText(true); // Cho phép xuống dòng nếu nội dung dài
        label.setStyle("-fx-padding: 5px; -fx-border-color: #e0e0e0; -fx-border-width: 0.5px;");
        return label;
    }

    private void clearTKBDisplay() {
        if (tkbGridPane != null) {
            tkbGridPane.getChildren().clear();
            tkbGridPane.getColumnConstraints().clear();
            tkbGridPane.getRowConstraints().clear();
        }
        danhSachLichDayGV.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
