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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;


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
        // Kiểm tra điều kiện trước khi xuất
        if (selectedTKB == null) {
            showAlert("Chưa chọn TKB", "Vui lòng chọn một thời khóa biểu để xuất.");
            return;
        }
        if (maGV == null || maGV.isEmpty()) {
            showAlert("Chưa có thông tin giáo viên", "Không thể xác định giáo viên để xuất TKB.");
            return;
        }

        // Tạo FileChooser để chọn vị trí lưu file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file Excel (.xls)"); // Sửa tiêu đề
        String defaultFileName = "TKB_" + selectedTKB.getMaTKB() + "_" + maGV + ".xls"; // Đổi đuôi file mặc định
        defaultFileName = defaultFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        fileChooser.setInitialFileName(defaultFileName);
        // Sửa ExtensionFilter
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel 97-2003 Files (*.xls)", "*.xls"));
        File file = fileChooser.showSaveDialog(btnXuat.getScene().getWindow());

        if (file != null) {
            Workbook workbook = null; // Interface chung
            try {
                workbook = new HSSFWorkbook(); // Sử dụng HSSFWorkbook cho định dạng .xls
                Sheet sheet = workbook.createSheet("ThoiKhoaBieu");

                // (Phần còn lại của code tạo CellStyle, Row, Cell, ghi dữ liệu giữ nguyên)
                // ...
                // Style cho cell (wrap text, căn giữa)
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setWrapText(true);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                // Style cho header
                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                // (Bạn có thể thêm Font, Fill color,... cho headerStyle ở đây nếu muốn)

                // 0. Thông tin chung của TKB
                Row infoRow1 = sheet.createRow(0);
                Cell infoCellLabel = infoRow1.createCell(0);
                infoCellLabel.setCellValue(mainLabel.getText()); // Tên TKB đang hiển thị

                Row infoRow2 = sheet.createRow(1);
                Cell infoCellBuoi = infoRow2.createCell(0);
                infoCellBuoi.setCellValue(tkbBuoiLabel.getText()); // Buổi

                // 1. Tạo hàng tiêu đề cho bảng TKB
                Row headerRow = sheet.createRow(3); // Bắt đầu bảng TKB từ hàng thứ 4 (index 3)
                String[] columnTitles = {"Tiết", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
                for (int i = 0; i < columnTitles.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnTitles[i]);
                    cell.setCellStyle(headerStyle);
                }

                // 2. Ghi dữ liệu từ tkbDataList
                int rowIndex = 4; // Dữ liệu bắt đầu từ hàng thứ 5 (index 4)
                for (TietHocData rowData : tkbDataList) {
                    Row row = sheet.createRow(rowIndex++);
                    row.setHeightInPoints((float) (ROW_HEIGHT * 1.5)); // Tăng chiều cao dòng một chút

                    Cell tietCell = row.createCell(0);
                    tietCell.setCellValue(rowData.getTiet());
                    tietCell.setCellStyle(cellStyle);

                    // Sử dụng toString() của ChiTietTKB vì nó đã có logic hiển thị theo flag
                    createCellWithData(row, 1, rowData.getThu2(), cellStyle);
                    createCellWithData(row, 2, rowData.getThu3(), cellStyle);
                    createCellWithData(row, 3, rowData.getThu4(), cellStyle);
                    createCellWithData(row, 4, rowData.getThu5(), cellStyle);
                    createCellWithData(row, 5, rowData.getThu6(), cellStyle);
                    createCellWithData(row, 6, rowData.getThu7(), cellStyle);
                }

                // Tự động điều chỉnh kích thước cột
                for (int i = 0; i < columnTitles.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                // Đặt độ rộng cột cố định nếu muốn (ví dụ cột Tiết)
                sheet.setColumnWidth(0, 2000); // Cột "Tiết" rộng khoảng 2000 units
                // ...

                // Lưu file
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }
                showInfo("Xuất Excel (.xls) thành công!", "Đã lưu thời khóa biểu vào file:\n" + file.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                showError("Lỗi xuất Excel (.xls)", "Có lỗi xảy ra khi ghi file Excel: " + e.getMessage());
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

    private void showInfo(String title, String message) { // Đảm bảo hàm này tồn tại và đúng
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showError(String title, String message) { // Đảm bảo hàm này tồn tại và đúng
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}