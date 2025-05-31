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

//Import cho Apache POI
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Font; // Import Font
import org.apache.poi.ss.usermodel.BorderStyle; // Import BorderStyle
import org.apache.poi.ss.util.CellRangeAddress; // Để gộp ô (nếu cần cho thông tin chung)

public class TKBLopCNController {

    @FXML private Label tkbBuoiLabel; // Label mới để hiển thị buổi của TKB
    @FXML private Label tkbLopCNLabel; // Label mới để hiển thị lớp chủ nhiệm
    @FXML private ComboBox<HocKy> hocKyComboBox;
    @FXML private ComboBox<ThoiKhoaBieu> tkbComboBox;
    @FXML private ComboBox<Lop> lopComboBox;
    @FXML private HBox hBoxLop;
    @FXML private Button btnXuat; // Nút xuất TKB

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

    @FXML
    private void handleXuat(ActionEvent event) {
        if (selectedTKB == null) {
            showAlert("Chưa chọn TKB", "Vui lòng chọn một thời khóa biểu để xuất.", Alert.AlertType.WARNING);
            return;
        }
        if (maLop == null || maLop.isEmpty()) {
            showAlert("Chưa có thông tin Lớp", "Không thể xác định Lớp để xuất TKB.", Alert.AlertType.WARNING);
            return;
        }
        if (tkbDataList.isEmpty()) {
            showAlert("Không có dữ liệu", "Không có dữ liệu thời khóa biểu để xuất.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu TKB Lớp Chủ Nhiệm (.xls)");
        // Tên file theo dạng TKB_MaTKB_MaLop.xls
        String defaultFileName = "TKB_" + selectedTKB.getMaTKB() + "_" + maLop + ".xls";
        defaultFileName = defaultFileName.replaceAll("[^a-zA-Z0-9._-]", "_").replaceAll("_+", "_");
        fileChooser.setInitialFileName(defaultFileName);

        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel 97-2003 Files (*.xls)", "*.xls"));
        File file = fileChooser.showSaveDialog(btnXuat.getScene().getWindow());

        if (file != null) {
            Workbook workbook = null;
            try {
                workbook = new HSSFWorkbook(); // Tạo workbook cho file .xls
                Sheet sheet = workbook.createSheet("TKB_LopCN");

                // --- Định nghĩa Fonts ---
                Font timesNewRomanFont = workbook.createFont();
                timesNewRomanFont.setFontName("Times New Roman");
                timesNewRomanFont.setFontHeightInPoints((short) 11);

                Font timesNewRomanBoldFont = workbook.createFont();
                timesNewRomanBoldFont.setFontName("Times New Roman");
                timesNewRomanBoldFont.setFontHeightInPoints((short) 12);
                timesNewRomanBoldFont.setBold(true);

                Font timesNewRomanHeaderFont = workbook.createFont();
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

                // tkbLopCNLabel (Tên TKB Lớp)
                String lopCNLabelText = (tkbLopCNLabel != null && tkbLopCNLabel.getText() != null) ? tkbLopCNLabel.getText() : "Thời Khóa Biểu Lớp";
                createMergedInfoCell(sheet, sheet.createRow(currentRowIndex++), 0, lopCNLabelText, infoLabelStyle, 0, lastTableColumnIndex);

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
                    dataContentRow.setHeightInPoints((float) (ROW_HEIGHT * 1.5));

                    createCellWithFormattedData(dataContentRow, 0, rowData.getTiet(), dataCellStyle);

                    // Sử dụng toString() của ChiTietTKB (flag = 3)
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
                    sheet.setColumnWidth(i, 25 * 256); // Độ rộng lớn hơn cho nội dung (TenMH - HoTenGV)
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

    private void createCellWithData(Row row, int cellIndex, ChiTietTKB chiTiet, CellStyle style) {
        Cell cell = row.createCell(cellIndex);
        if (chiTiet != null) {
            cell.setCellValue(chiTiet.toString()); // Sử dụng toString() để lấy định dạng hiển thị
        } else {
            cell.setCellValue(""); // Hoặc để trống
        }
        cell.setCellStyle(style);
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

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}