package controllers.XemTKB;

import dao.ThoiKhoaBieuDAO;
import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

// Import cho Apache POI
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Font; // Import Font
import org.apache.poi.ss.usermodel.BorderStyle; // Import BorderStyle
import org.apache.poi.ss.util.CellRangeAddress; // Để gộp ô (nếu cần cho thông tin chung)


public class TKBCaNhanController {

    @FXML private Label tkbBuoiLabel; // Label mới để hiển thị buổi của TKB
    @FXML private ComboBox<HocKy> hocKyComboBox;
    @FXML private ComboBox<ThoiKhoaBieu> tkbComboBox;
    @FXML private HBox hBoxGV;
    @FXML private ComboBox<GiaoVien> GVComboBox;
    @FXML private Label mainLabel;
    @FXML private Button btnXuat;

    @FXML private TableView<TietHocData> tkbTableView;
    @FXML private TableColumn<TietHocData, String> tietColumn;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu2Column;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu3Column;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu4Column;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu5Column;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu6Column;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu7Column;

    private GiaoVien currentGiaoVien;
    private ThoiKhoaBieuDAO thoiKhoaBieuDAO;
    private ObservableList<TietHocData> tkbDataList;

    private static final int SO_TIET_MOI_BUOI = 5;
    private static final double ROW_HEIGHT = 28.0; // Giả sử mỗi dòng là 28
    private static final double ESTIMATED_HEADER_HEIGHT = 30.0; // Chiều cao ước tính của header
    String maGV = "";
    ThoiKhoaBieu selectedTKB;


    public TKBCaNhanController() {
        thoiKhoaBieuDAO = new ThoiKhoaBieuDAO();
    }

    @FXML
    public void initialize() {
        setupTableColumns(tietColumn, thu2Column, thu3Column, thu4Column, thu5Column, thu6Column, thu7Column);
        tkbDataList = FXCollections.observableArrayList();
        initializeTableRows(tkbDataList);
        tkbTableView.setItems(tkbDataList);
        tkbBuoiLabel.setText("Buổi: (chưa chọn TKB)");

        tkbTableView.setFixedCellSize(ROW_HEIGHT);
        double calculatedTableHeight = ESTIMATED_HEADER_HEIGHT + (SO_TIET_MOI_BUOI * ROW_HEIGHT) + 2.0; // +2 for padding
        tkbTableView.setPrefHeight(calculatedTableHeight);
        tkbTableView.setMinHeight(calculatedTableHeight);
        tkbTableView.setMaxHeight(calculatedTableHeight);

        loadHocKyOptions();
        tkbComboBox.setDisable(true);
        if (GVComboBox != null) { // GVComboBox có thể không tồn tại trong mọi FXML
            GVComboBox.setDisable(true);
        }
    }

    public void initData(GiaoVien giaoVien) {
        this.currentGiaoVien = giaoVien;
        if (giaoVien != null) {
            maGV = giaoVien.getMaGV();
        }
        if (hBoxGV != null) {
            hBoxGV.setVisible(false);
            hBoxGV.setManaged(false);
        }
    }

    public void initDataAdmin() {
        mainLabel.setText("THỜI KHÓA BIỂU GIÁO VIÊN");
        loadGVOptions();
    }

    private void setupTableColumns(TableColumn<TietHocData, String> tietCol,
                                   TableColumn<TietHocData, ChiTietTKB> colThu2,
                                   TableColumn<TietHocData, ChiTietTKB> colThu3,
                                   TableColumn<TietHocData, ChiTietTKB> colThu4,
                                   TableColumn<TietHocData, ChiTietTKB> colThu5,
                                   TableColumn<TietHocData, ChiTietTKB> colThu6,
                                   TableColumn<TietHocData, ChiTietTKB> colThu7) {
        tietCol.setCellValueFactory(new PropertyValueFactory<>("tiet")); //
        colThu2.setCellValueFactory(new PropertyValueFactory<>("thu2")); //
        colThu3.setCellValueFactory(new PropertyValueFactory<>("thu3")); //
        colThu4.setCellValueFactory(new PropertyValueFactory<>("thu4")); //
        colThu5.setCellValueFactory(new PropertyValueFactory<>("thu5")); //
        colThu6.setCellValueFactory(new PropertyValueFactory<>("thu6")); //
        colThu7.setCellValueFactory(new PropertyValueFactory<>("thu7")); //

        // Custom cell factory để hiển thị toString() của ChiTietTKB
        // Hoặc bạn có thể định nghĩa một cách hiển thị khác ở đây nếu cần
        javafx.util.Callback<TableColumn<TietHocData, ChiTietTKB>, TableCell<TietHocData, ChiTietTKB>> cellFactory =
                param -> new TableCell<>() {
                    @Override
                    protected void updateItem(ChiTietTKB item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setStyle("-fx-alignment: CENTER;"); // Căn giữa ô trống
                        } else {
                            setText(item.toString()); // Sử dụng toString() của ChiTietTKB
                            setStyle("-fx-alignment: CENTER; -fx-wrap-text: true;"); // Căn giữa và wrap text
                        }
                    }
                };

        colThu2.setCellFactory(cellFactory);
        colThu3.setCellFactory(cellFactory);
        colThu4.setCellFactory(cellFactory);
        colThu5.setCellFactory(cellFactory);
        colThu6.setCellFactory(cellFactory);
        colThu7.setCellFactory(cellFactory);
    }

    private void initializeTableRows(ObservableList<TietHocData> dataList) {
        dataList.clear();
        for (int i = 1; i <= SO_TIET_MOI_BUOI; i++) {
            dataList.add(new TietHocData("Tiết " + i)); //
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
            if (GVComboBox != null) GVComboBox.setDisable(true);
        }
    }

    private void loadGVOptions() {
        if (GVComboBox == null) return; // Kiểm tra nếu GVComboBox không được inject
        List<GiaoVien> GVList = thoiKhoaBieuDAO.getDanhSachGiaoVien();
        GVComboBox.getItems().clear();
        if (GVList != null && !GVList.isEmpty()) {
            GVComboBox.setItems(FXCollections.observableArrayList(GVList));
        } else {
            GVComboBox.setPromptText("Không có GV");
        }
    }

    @FXML
    private void handleGVSelection(ActionEvent event) {
        if (GVComboBox == null) return;
        GiaoVien selectedGVEntity = GVComboBox.getSelectionModel().getSelectedItem();
        if (selectedGVEntity != null) {
            maGV = selectedGVEntity.getMaGV();
            mainLabel.setText("THỜI KHÓA BIỂU CỦA " + selectedGVEntity.getHoGV() + " " + selectedGVEntity.getTenGV());
            if (selectedTKB != null) {
                loadThoiKhoaBieuData(selectedTKB, maGV);
            }
        }
    }

    @FXML
    private void handleHocKySelection(ActionEvent event) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        tkbComboBox.getItems().clear();
        tkbComboBox.setDisable(true);
        if (GVComboBox != null) GVComboBox.setDisable(true);
        tkbBuoiLabel.setText("Buổi: (chưa chọn TKB)");
        clearTableData();

        if (selectedHocKy != null) {
            List<ThoiKhoaBieu> tkbList = thoiKhoaBieuDAO.getDanhSachThoiKhoaBieuTheoHocKy(selectedHocKy.getMaHK());
            if (tkbList != null && !tkbList.isEmpty()) {
                tkbComboBox.setItems(FXCollections.observableArrayList(tkbList));
                tkbComboBox.setDisable(false);
                if (GVComboBox != null && hBoxGV != null && hBoxGV.isVisible()) { // Chỉ bật GVComboBox nếu là admin view
                    GVComboBox.setDisable(false);
                }
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
        clearTableData();

        if (selectedTKB != null) {
            if (selectedTKB.getBuoi() != null && !selectedTKB.getBuoi().isEmpty()) {
                tkbBuoiLabel.setText("Buổi: " + selectedTKB.getBuoi().toUpperCase());
            } else {
                tkbBuoiLabel.setText("Buổi: (Không xác định)");
            }
            // Chỉ tải TKB nếu maGV đã được thiết lập (hoặc là admin view đang xem một GV cụ thể)
            if (maGV != null && !maGV.isEmpty()) {
                loadThoiKhoaBieuData(selectedTKB, maGV);
            }
        } else {
            tkbBuoiLabel.setText("Buổi: (chưa chọn TKB)");
        }
    }

    private void loadThoiKhoaBieuData(ThoiKhoaBieu tkb, String maGiaoVien) {
        if (tkb == null || maGiaoVien == null || maGiaoVien.isEmpty()) {
            System.out.println("Không thể tải TKB: selectedTKB hoặc maGV là null/rỗng.");
            clearTableData();
            return;
        }
        initializeTableRows(tkbDataList);
        List<ChiTietTKB> chiTietList = thoiKhoaBieuDAO.getChiTietTKBForGiaoVien(tkb.getMaTKB(), maGiaoVien);

        if (chiTietList != null && !chiTietList.isEmpty()) {
            for (ChiTietTKB ct : chiTietList) {
                int tietDbValue = ct.getTiet(); // Giả sử tiết từ 1-5
                int tietIndex = tietDbValue - 1; // Chuyển sang index 0-4

                if (tietIndex >= 0 && tietIndex < SO_TIET_MOI_BUOI) {
                    TietHocData rowData = tkbDataList.get(tietIndex);
                    switch (ct.getThu()) { // getThu() trả về 2-7
                        case 2: rowData.setThu2(ct); break;
                        case 3: rowData.setThu3(ct); break;
                        case 4: rowData.setThu4(ct); break;
                        case 5: rowData.setThu5(ct); break;
                        case 6: rowData.setThu6(ct); break;
                        case 7: rowData.setThu7(ct); break;
                        // No default needed as other 'thu' values are not expected for this table structure
                    }
                } else {
                    System.err.println("Tiết học không hợp lệ (ngoài khoảng 1-" + SO_TIET_MOI_BUOI + "): Tiết " + tietDbValue + " cho TKB " + tkb.getMaTKB());
                }
            }
        } else {
            System.out.println("Không có chi tiết TKB nào cho GV " + maGiaoVien + " với TKB " + tkb.getMaTKB());
        }
        tkbTableView.refresh();
    }

    @FXML
    private void handleXuat(ActionEvent event) {
        if (selectedTKB == null) {
            showAlert("Chưa chọn TKB", "Vui lòng chọn một thời khóa biểu để xuất.", Alert.AlertType.WARNING);
            return;
        }
        if (maGV == null || maGV.isEmpty()) {
            showAlert("Chưa có thông tin giáo viên", "Không thể xác định giáo viên để xuất TKB.", Alert.AlertType.WARNING);
            return;
        }
        if (tkbDataList.isEmpty()) {
            showAlert("Không có dữ liệu", "Không có dữ liệu thời khóa biểu để xuất.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu TKB Cá Nhân (.xlsx)");
        // Tên file theo dạng TKB_MaTKB_MaGV.xls
        String defaultFileName = "TKB_" + selectedTKB.getMaTKB() + "_" + maGV + ".xlsx";
        defaultFileName = defaultFileName.replaceAll("[^a-zA-Z0-9._-]", "_").replaceAll("_+", "_");
        fileChooser.setInitialFileName(defaultFileName);

        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));
        File file = fileChooser.showSaveDialog(btnXuat.getScene().getWindow());

        if (file != null) {
            Workbook workbook = null;
            try {
                workbook = new XSSFWorkbook(); // Tạo workbook cho file .xls
                Sheet sheet = workbook.createSheet("TKB_CaNhan");

                // --- Định nghĩa Fonts ---
                Font timesNewRomanFont = workbook.createFont();
                timesNewRomanFont.setFontName("Times New Roman");
                timesNewRomanFont.setFontHeightInPoints((short) 11);

                Font timesNewRomanBoldFont = workbook.createFont();
                timesNewRomanBoldFont.setFontName("Times New Roman");
                timesNewRomanBoldFont.setFontHeightInPoints((short) 12); // Size lớn hơn cho tiêu đề chính
                timesNewRomanBoldFont.setBold(true);

                Font timesNewRomanHeaderFont = workbook.createFont(); // Font cho header bảng
                timesNewRomanHeaderFont.setFontName("Times New Roman");
                timesNewRomanHeaderFont.setFontHeightInPoints((short) 10);
                timesNewRomanHeaderFont.setBold(true);

                // --- Định nghĩa CellStyles ---
                CellStyle infoLabelStyle = createCellStyle(workbook, timesNewRomanBoldFont, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, false, false);
                CellStyle tableHeaderStyle = createCellStyle(workbook, timesNewRomanHeaderFont, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true, true);
                CellStyle dataCellStyle = createCellStyle(workbook, timesNewRomanFont, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true, true);

                // --- Ghi thông tin chung ---
                int currentRowIndex = 0;
                int lastTableColumnIndex = 6; // "Tiết" (0) + 6 cột "Thứ" (1-6)

                // mainLabel (Tên TKB)
                String mainLabelText = (mainLabel != null && mainLabel.getText() != null) ? mainLabel.getText() : "Thời Khóa Biểu Cá Nhân";
                createMergedInfoCell(sheet, sheet.createRow(currentRowIndex++), 0, mainLabelText, infoLabelStyle, 0, lastTableColumnIndex);

                // tkbBuoiLabel (Buổi)
                String buoiLabelText = (tkbBuoiLabel != null && tkbBuoiLabel.getText() != null) ? tkbBuoiLabel.getText() : "Buổi: (Không xác định)";
                createMergedInfoCell(sheet, sheet.createRow(currentRowIndex++), 0, buoiLabelText, infoLabelStyle, 0, lastTableColumnIndex);

                currentRowIndex++; // Dòng trống

                // --- Tạo hàng tiêu đề cho bảng TKB ---
                Row headerRow = sheet.createRow(currentRowIndex++);
                headerRow.setHeightInPoints(20f);
                String[] columnTitles = {"Tiết", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
                for (int i = 0; i < columnTitles.length; i++) {
                    createCellWithFormattedData(headerRow, i, columnTitles[i], tableHeaderStyle);
                }

                // --- Ghi dữ liệu từ tkbDataList ---
                for (TietHocData rowData : tkbDataList) {
                    Row dataContentRow = sheet.createRow(currentRowIndex++);
                    dataContentRow.setHeightInPoints((float) (ROW_HEIGHT * 1.5)); // ROW_HEIGHT từ class của bạn

                    createCellWithFormattedData(dataContentRow, 0, rowData.getTiet(), dataCellStyle);

                    // Sử dụng toString() của ChiTietTKB (flag = 2)
                    createCellWithFormattedData(dataContentRow, 1, (rowData.getThu2() != null ? rowData.getThu2().toString() : ""), dataCellStyle);
                    createCellWithFormattedData(dataContentRow, 2, (rowData.getThu3() != null ? rowData.getThu3().toString() : ""), dataCellStyle);
                    createCellWithFormattedData(dataContentRow, 3, (rowData.getThu4() != null ? rowData.getThu4().toString() : ""), dataCellStyle);
                    createCellWithFormattedData(dataContentRow, 4, (rowData.getThu5() != null ? rowData.getThu5().toString() : ""), dataCellStyle);
                    createCellWithFormattedData(dataContentRow, 5, (rowData.getThu6() != null ? rowData.getThu6().toString() : ""), dataCellStyle);
                    createCellWithFormattedData(dataContentRow, 6, (rowData.getThu7() != null ? rowData.getThu7().toString() : ""), dataCellStyle);
                }

                // --- Điều chỉnh độ rộng cột ---
                sheet.setColumnWidth(0, 10 * 256); // Cột "Tiết"
                for (int i = 1; i <= 6; i++) { // Cột Thứ 2 đến Thứ 7
                    sheet.setColumnWidth(i, 20 * 256); // Độ rộng lớn hơn cho nội dung (MaLop - TenMH)
                }

                // Lưu file
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }
                showAlert("Xuất Excel (.xls) thành công!", "Đã lưu thời khóa biểu vào file:\n" + file.getAbsolutePath(), Alert.AlertType.INFORMATION);

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi xuất Excel (.xls)", "Có lỗi xảy ra khi ghi file Excel: " + e.getMessage(), Alert.AlertType.ERROR);
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

    private CellStyle createCellStyle(Workbook workbook, Font font,
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
            // Đối với các ô thông tin này, nếu style đã có border thì không cần RegionUtil
            // Hoặc nếu muốn viền chỉ bao quanh vùng gộp thì dùng RegionUtil.setBorder...
        }
    }


    private void clearAllUIData() {
        hocKyComboBox.getItems().clear();
        hocKyComboBox.setPromptText("Chọn học kỳ");
        tkbComboBox.getItems().clear();
        tkbComboBox.setPromptText("Chọn TKB để xem");
        tkbComboBox.setDisable(true);
        if (GVComboBox != null) {
            GVComboBox.getItems().clear();
            GVComboBox.setPromptText("Chọn giáo viên");
            GVComboBox.setDisable(true);
        }
        tkbBuoiLabel.setText("Buổi: (chưa chọn TKB)");
        clearTableData();
    }

    private void clearTableData() {
        initializeTableRows(tkbDataList);
        if (tkbTableView != null) {
            tkbTableView.refresh();
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