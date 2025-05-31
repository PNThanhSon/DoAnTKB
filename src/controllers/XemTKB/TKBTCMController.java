package controllers.XemTKB;

import dao.ThoiKhoaBieuDAO;
import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Import cho Apache POI (nếu chưa có hoặc cần bổ sung)
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle; // Import BorderStyle
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;    // Import RegionUtil để sửa viền ô gộp

// Import cho FileChooser và File I/O (đảm bảo đã có)
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TKBTCMController {

    @FXML private Label tkbBuoiLabel;
    @FXML private Label tkbTCMLabel;
    @FXML private ComboBox<HocKy> hocKyComboBox;
    @FXML private ComboBox<ThoiKhoaBieu> tkbComboBox;
    @FXML private ComboBox<NhomMonHocDisplay> monHocComboBox;
    @FXML private ScrollPane tkbScrollPane; // Để chứa GridPane
    @FXML private HBox hBoxTo;
    @FXML private ComboBox<ToChuyenMon> toComboBox;
    @FXML private Button btnXuat; // Nút xuất Excel
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
            showAlert("Lỗi", "Nhóm môn học không chứa mã môn cụ thể.", Alert.AlertType.ERROR);
            buildTKBGrid(new ArrayList<>());
            return;
        }

        List<ChiTietTKB> tatCaChiTiet = thoiKhoaBieuDAO.getChiTietTKBForTCM(
                selectedTKB.getMaTKB(),
                cacMaMHCuaNhom,
                maTCM
        );

        if (tatCaChiTiet.isEmpty()) {
            showAlert("Thông báo", "Không có lịch dạy cho môn '" + selectedNhomMonHoc.getTenMonHocChung() + "' trong TKB này cho tổ chuyên môn.", Alert.AlertType.INFORMATION);
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

    // ======================= PHẦN CODE MỚI CHO EXCEL EXPORT =========================

    /**
     * Hàm tiện ích để tạo CellStyle chung.
     * Bạn có thể tùy chỉnh thêm các thuộc tính khác cho style nếu muốn.
     */
    private CellStyle createCellStyle(Workbook workbook, org.apache.poi.ss.usermodel.Font font,
                                      HorizontalAlignment halign, VerticalAlignment valign,
                                      boolean wrapText, boolean bordered) {
        CellStyle style = workbook.createCellStyle();
        if (font != null) {
            style.setFont(font);
        }
        style.setAlignment(halign);
        style.setVerticalAlignment(valign);
        style.setWrapText(wrapText);
        if (bordered) {
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
        }
        return style;
    }

    private void createCellWithFormattedData(Row row, int cellIndex, String text, CellStyle style) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(text);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private void createMergedInfoCell(Sheet sheet, Row row, int cellIndex, String text, CellStyle style,
                                      int firstColMerge, int lastColMerge) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(text);
        if (style != null) {
            cell.setCellStyle(style);
        }
        if (lastColMerge > firstColMerge) {
            CellRangeAddress mergedRegion = new CellRangeAddress(row.getRowNum(), row.getRowNum(), firstColMerge, lastColMerge);
            sheet.addMergedRegion(mergedRegion);
            // Áp dụng viền cho vùng gộp nếu style có viền (hoặc có thể bỏ qua nếu style đã định nghĩa)
            // Để đảm bảo viền đẹp cho các ô thông tin này, ta có thể không cần RegionUtil ở đây nếu CellStyle đã có border.
            // Nếu muốn viền chỉ bao quanh vùng gộp, thì dùng RegionUtil như các header Thứ.
        }
    }

    private void applyBordersToMergedRegion(CellRangeAddress region, Sheet sheet) {
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
    }

    @FXML
    private void handleXuat(ActionEvent event) {
        ThoiKhoaBieu selectedTKBValue = tkbComboBox.getSelectionModel().getSelectedItem();
        NhomMonHocDisplay selectedNhomMonHocValue = monHocComboBox.getSelectionModel().getSelectedItem();

        if (selectedTKBValue == null) {
            showAlert("Chưa chọn TKB", "Vui lòng chọn một thời khóa biểu để xuất.", Alert.AlertType.WARNING);
            return;
        }
        if (maTCM == null || maTCM.isEmpty()) {
            showAlert("Chưa có thông tin Tổ Chuyên Môn", "Vui lòng chọn Tổ Chuyên Môn để xuất TKB.", Alert.AlertType.WARNING);
            return;
        }
        if (selectedNhomMonHocValue == null) {
            showAlert("Chưa chọn Nhóm Môn học", "Vui lòng chọn một nhóm môn học để xuất TKB.", Alert.AlertType.WARNING);
            return;
        }
        if (danhSachLichDayGV.isEmpty()) {
            showAlert("Không có dữ liệu", "Không có dữ liệu thời khóa biểu của tổ chuyên môn cho môn học này để xuất.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file Excel TKB Tổ Chuyên Môn (.xlsx)");
        // Tên file theo dạng TKB_MaTKB_MaTCM.xls
        String defaultFileName = "TKB_" + selectedTKBValue.getMaTKB() + "_" + maTCM + ".xlsx";
        defaultFileName = defaultFileName.replaceAll("[^a-zA-Z0-9._-]", "_").replaceAll("_+", "_");
        fileChooser.setInitialFileName(defaultFileName);

        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));
        File file = fileChooser.showSaveDialog(btnXuat.getScene().getWindow()); // Sử dụng tkbScrollPane hoặc một Node khác để lấy Window

        if (file != null) {
            Workbook workbook = null;
            try {
                workbook = new XSSFWorkbook(); // Tạo workbook cho file .xls
                Sheet sheet = workbook.createSheet("TKB_TCM");

                // --- Định nghĩa Fonts ---
                org.apache.poi.ss.usermodel.Font timesNewRomanFont = workbook.createFont();
                timesNewRomanFont.setFontName("Times New Roman");
                timesNewRomanFont.setFontHeightInPoints((short) 11);

                org.apache.poi.ss.usermodel.Font timesNewRomanBoldFont = workbook.createFont();
                timesNewRomanBoldFont.setFontName("Times New Roman");
                timesNewRomanBoldFont.setFontHeightInPoints((short) 12); // Size lớn hơn cho tiêu đề chính
                timesNewRomanBoldFont.setBold(true);

                org.apache.poi.ss.usermodel.Font timesNewRomanHeaderFont = workbook.createFont(); // Font cho header bảng
                timesNewRomanHeaderFont.setFontName("Times New Roman");
                timesNewRomanHeaderFont.setFontHeightInPoints((short) 10);
                timesNewRomanHeaderFont.setBold(true);


                // --- Định nghĩa CellStyles ---
                // Style cho các label thông tin chung (Tổ, Môn, Buổi) - BOLD
                CellStyle infoLabelStyle = createCellStyle(workbook, timesNewRomanBoldFont, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, false, false); // Không viền, căn trái

                // Style cho header của bảng (Giáo viên, Thứ, Tiết) - BOLD, có viền
                CellStyle mainHeaderStyle = createCellStyle(workbook, timesNewRomanHeaderFont, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true, true);

                // Style cho ô dữ liệu (Mã Lớp) - REGULAR, có viền
                CellStyle dataCellStyle = createCellStyle(workbook, timesNewRomanFont, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true, true);

                // Style cho tên giáo viên trong bảng - REGULAR, có viền, căn trái
                CellStyle gvNameStyle = createCellStyle(workbook, timesNewRomanFont, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, true, true);
                gvNameStyle.setIndention((short)1); // Thụt lề một chút cho tên GV


                // --- Ghi thông tin chung ---
                int currentRowIndex = 0;
                int lastColIndexForInfoMerge = THU_TRONG_TUAN_LABEL.length * SO_TIET_MOI_BUOI; // Cột cuối cùng của bảng TKB

                createMergedInfoCell(sheet, sheet.createRow(currentRowIndex++), 0, tkbTCMLabel.getText(), infoLabelStyle, 0, lastColIndexForInfoMerge);
                createMergedInfoCell(sheet, sheet.createRow(currentRowIndex++), 0, "Môn học: " + selectedNhomMonHocValue.getTenMonHocChung(), infoLabelStyle, 0, lastColIndexForInfoMerge);
                createMergedInfoCell(sheet, sheet.createRow(currentRowIndex++), 0, tkbBuoiLabel.getText(), infoLabelStyle, 0, lastColIndexForInfoMerge);

                currentRowIndex++; // Dòng trống

                // --- Tạo hàng tiêu đề cho bảng TKB (Thứ và Tiết) - 2 dòng ---
                Row thuHeaderRow = sheet.createRow(currentRowIndex++);
                Row tietHeaderRow = sheet.createRow(currentRowIndex++);
                thuHeaderRow.setHeightInPoints(20f);
                tietHeaderRow.setHeightInPoints(20f);

                // Ô "Giáo viên" (gộp 2 dòng)
                CellRangeAddress mergedGvHeaderRegion = new CellRangeAddress(thuHeaderRow.getRowNum(), tietHeaderRow.getRowNum(), 0, 0);
                createCellWithFormattedData(thuHeaderRow, 0, "Giáo viên", mainHeaderStyle); // Cell được tạo ở thuHeaderRow
                // Cell ở tietHeaderRow tại cột 0 sẽ được ngầm hiểu là một phần của vùng merge, POI sẽ tự xử lý
                // hoặc bạn có thể tạo một cell rỗng với style nếu muốn kiểm soát hoàn toàn
                Cell emptyCellForGvMerge = tietHeaderRow.createCell(0);
                emptyCellForGvMerge.setCellStyle(mainHeaderStyle); // Áp style để có viền
                sheet.addMergedRegion(mergedGvHeaderRegion);
                applyBordersToMergedRegion(mergedGvHeaderRegion, sheet); // Áp dụng viền cho vùng gộp

                // Các ô "Thứ X" (gộp SO_TIET_MOI_BUOI cột) và "Tiết Y"
                int currentCellIndexInExcel = 1;
                for (String thu : THU_TRONG_TUAN_LABEL) {
                    CellRangeAddress mergedThuHeaderRegion = new CellRangeAddress(thuHeaderRow.getRowNum(), thuHeaderRow.getRowNum(), currentCellIndexInExcel, currentCellIndexInExcel + SO_TIET_MOI_BUOI - 1);
                    createCellWithFormattedData(thuHeaderRow, currentCellIndexInExcel, thu, mainHeaderStyle);
                    // Tạo các cell trống bên dưới cho vùng merge để đảm bảo style và viền được áp dụng
                    for(int k=0; k < SO_TIET_MOI_BUOI; ++k) {
                        if (k==0) continue; // Ô đầu tiên đã được tạo
                        Cell emptyCellInThuMerge = thuHeaderRow.createCell(currentCellIndexInExcel + k);
                        emptyCellInThuMerge.setCellStyle(mainHeaderStyle);
                    }
                    sheet.addMergedRegion(mergedThuHeaderRegion);
                    applyBordersToMergedRegion(mergedThuHeaderRegion, sheet);

                    for (int tiet = 1; tiet <= SO_TIET_MOI_BUOI; tiet++) {
                        createCellWithFormattedData(tietHeaderRow, currentCellIndexInExcel + tiet - 1, "Tiết " + tiet, mainHeaderStyle);
                    }
                    currentCellIndexInExcel += SO_TIET_MOI_BUOI;
                }

                // --- Ghi dữ liệu TKB ---
                for (GiaoVienLichDayData gvData : danhSachLichDayGV) {
                    Row dataRow = sheet.createRow(currentRowIndex++);
                    dataRow.setHeightInPoints((short) (3 * sheet.getDefaultRowHeightInPoints() / 2)); // Tăng chiều cao dòng một chút

                    createCellWithFormattedData(dataRow, 0, gvData.getHoTenGV(), gvNameStyle);

                    currentCellIndexInExcel = 1;
                    for (int thuDbVal : THU_TRONG_TUAN_DBVAL) {
                        for (int tiet = 1; tiet <= SO_TIET_MOI_BUOI; tiet++) {
                            ChiTietTKB chiTiet = gvData.getChiTiet(thuDbVal, tiet);
                            String cellText = (chiTiet != null) ? chiTiet.toString() : ""; // flag = 1 cho TKBTCM sẽ là MaLop
                            createCellWithFormattedData(dataRow, currentCellIndexInExcel++, cellText, dataCellStyle);
                        }
                    }
                }

                // --- Điều chỉnh độ rộng cột ---
                sheet.setColumnWidth(0, 22 * 256); // Cột tên GV (khoảng 22 ký tự)
                for (int i = 1; i <= THU_TRONG_TUAN_LABEL.length * SO_TIET_MOI_BUOI; i++) {
                    sheet.setColumnWidth(i, 9 * 256); // Các cột tiết (khoảng 9 ký tự)
                }

                // Lưu file
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }
                showAlert("Thành công", "Đã xuất TKB Tổ Chuyên Môn ra file Excel thành công!\n" + file.getAbsolutePath(), Alert.AlertType.INFORMATION);

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi xuất Excel", "Có lỗi xảy ra khi ghi file Excel: " + e.getMessage(), Alert.AlertType.ERROR);
            } finally {
                if (workbook != null) {
                    try {
                        workbook.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
