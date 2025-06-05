package controllers; // Hoặc package Controller bạn đang dùng

import dao.BCTKDAO;
import entities.HocKy;
import entities.ThoiKhoaBieu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import javafx.stage.FileChooser;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFDrawing; // Để vẽ biểu đồ
import org.apache.poi.xssf.usermodel.XSSFChart;  // Đối tượng biểu đồ
import org.openxmlformats.schemas.drawingml.x2006.chart.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import entities.GiaoVien;

public class BCTKController implements Initializable {

    @FXML
    private ComboBox<HocKy> hocKyComboBox;

    @FXML
    private ComboBox<ThoiKhoaBieu> tkbComboBox;

    @FXML
    private PieChart soTietDuThieuPieChart;

    @FXML
    private Label messageLabel;

    @FXML
    private Button btnXuat;

    private BCTKDAO bctkDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bctkDAO = new BCTKDAO();
        soTietDuThieuPieChart.setTitle("Thống Kê Số Tiết Dư/Thiếu Của Giáo Viên");

        loadHocKyComboBox();

        hocKyComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                loadTkbComboBox(newValue.getMaHK());
                soTietDuThieuPieChart.setData(FXCollections.observableArrayList());
                if (messageLabel != null) messageLabel.setText("Vui lòng chọn Thời Khóa Biểu.");
            } else {
                tkbComboBox.getItems().clear();
                tkbComboBox.setDisable(true);
                soTietDuThieuPieChart.setData(FXCollections.observableArrayList());
            }
        });

        tkbComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null && hocKyComboBox.getSelectionModel().getSelectedItem() != null) {
                updatePieChartData(hocKyComboBox.getSelectionModel().getSelectedItem().getMaHK(), newValue.getMaTKB());
            } else {
                soTietDuThieuPieChart.setData(FXCollections.observableArrayList());
            }
        });

        tkbComboBox.setDisable(true);
    }

    private void loadHocKyComboBox() {
        ObservableList<HocKy> hocKyList = FXCollections.observableArrayList(bctkDAO.getAllHocKy());
        hocKyComboBox.setItems(hocKyList);
        if (hocKyList.isEmpty() && messageLabel != null) {
            messageLabel.setText("Không tìm thấy dữ liệu Học Kỳ.");
        }
    }

    private void loadTkbComboBox(String maHK) {
        ObservableList<ThoiKhoaBieu> tkbList = FXCollections.observableArrayList(bctkDAO.getThoiKhoaBieuByMaHK(maHK));
        tkbComboBox.setItems(tkbList);
        tkbComboBox.setDisable(tkbList.isEmpty());
        if (tkbList.isEmpty() && messageLabel != null) {
            messageLabel.setText("Không có Thời Khóa Biểu nào cho Học Kỳ đã chọn.");
        } else if (messageLabel != null) {
            messageLabel.setText("Vui lòng chọn Thời Khóa Biểu.");
        }
    }

    private void updatePieChartData(String maHK, String maTKB) {
        if (maHK == null || maHK.isEmpty() || maTKB == null || maTKB.isEmpty()) {
            if (messageLabel != null) messageLabel.setText("Vui lòng chọn đầy đủ Học Kỳ và Thời Khóa Biểu.");
            soTietDuThieuPieChart.setData(FXCollections.observableArrayList());
            return;
        }

        Map<String, Integer> stats = bctkDAO.getSoTietDuThieuStats(maHK, maTKB);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        int totalTeachersWithValue = 0;

        // Cập nhật tên các mục PieChart.Data để khớp với key mới và ý nghĩa
        if (stats.getOrDefault("ThieuNhieu", 0) > 0) {
            pieChartData.add(new PieChart.Data("Thiếu nhiều (<= -4 tiết)", stats.get("ThieuNhieu")));
            totalTeachersWithValue += stats.get("ThieuNhieu");
        }
        if (stats.getOrDefault("Thieu", 0) > 0) {
            pieChartData.add(new PieChart.Data("Thiếu (-1, -2 hoặc -3 tiết)", stats.get("Thieu")));
            totalTeachersWithValue += stats.get("Thieu");
        }
        if (stats.getOrDefault("Du", 0) > 0) {
            pieChartData.add(new PieChart.Data("Đủ tiết", stats.get("Du")));
            totalTeachersWithValue += stats.get("Du");
        }
        if (stats.getOrDefault("DuVua", 0) > 0) {
            pieChartData.add(new PieChart.Data("Dư vừa (1, 2 hoặc 3 tiết)", stats.get("DuVua")));
            totalTeachersWithValue += stats.get("DuVua");
        }
        if (stats.getOrDefault("DuNhieu", 0) > 0) {
            pieChartData.add(new PieChart.Data("Dư nhiều (>= 4 tiết)", stats.get("DuNhieu")));
            totalTeachersWithValue += stats.get("DuNhieu");
        }

        soTietDuThieuPieChart.setData(pieChartData);

        if (totalTeachersWithValue == 0) {
            if (messageLabel != null) messageLabel.setText("Không có dữ liệu thống kê cho lựa chọn này.");
            soTietDuThieuPieChart.setTitle("Thống Kê Số Tiết Dư/Thiếu (Không có dữ liệu)");
        } else {
            if (messageLabel != null) messageLabel.setText("Hiển thị thống kê.");
            soTietDuThieuPieChart.setTitle("Thống Kê Số Tiết Dư/Thiếu Của Giáo Viên");
        }

        final int finalTotalTeachers = totalTeachersWithValue;
        if (finalTotalTeachers > 0) {
            pieChartData.forEach(data -> {
                String percentage = String.format("%.1f%%", (data.getPieValue() / finalTotalTeachers) * 100);
                String tooltipText = String.format("%s: %d GV (%s)", data.getName(), (int)data.getPieValue(), percentage);

                Tooltip tooltip = new Tooltip(tooltipText);
                Tooltip.install(data.getNode(), tooltip);

                data.getNode().setOnMouseEntered(event -> data.getNode().setStyle("-fx-cursor: hand; -fx-opacity: 0.8;"));
                data.getNode().setOnMouseExited(event -> data.getNode().setStyle("-fx-cursor: default; -fx-opacity: 1.0;"));
            });
        }

        soTietDuThieuPieChart.setLabelLineLength(20);
        soTietDuThieuPieChart.setLabelsVisible(true);
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

    /**
     * Hàm tiện ích để tạo và định dạng một ô dữ liệu đơn giản.
     */
    private void createCell(Row row, int cellIndex, String text, CellStyle style) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(text);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }
    private void createCell(Row row, int cellIndex, double value, CellStyle style) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(value);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }


    /**
     * Hàm tiện ích để tạo và định dạng một ô thông tin, hỗ trợ gộp ô ngang.
     */
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
        }
    }

    @FXML
    private void handleXuat(ActionEvent event) { // Đổi lại tên hàm nếu bạn đã sửa thành handleXuatBaoCaoExcel
        HocKy selectedHK = hocKyComboBox.getSelectionModel().getSelectedItem();
        ThoiKhoaBieu selectedTKBValue = tkbComboBox.getSelectionModel().getSelectedItem();

        if (selectedHK == null || selectedTKBValue == null) {
            showAlert("Thiếu thông tin", "Vui lòng chọn đầy đủ Học Kỳ và Thời Khóa Biểu để xuất báo cáo.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu Báo Cáo Thống Kê (.xlsx)");
        String defaultFileName = "BCTK_SoTiet_" + selectedHK.getMaHK() + "_" + selectedTKBValue.getMaTKB() + ".xlsx";
        defaultFileName = defaultFileName.replaceAll("[^a-zA-Z0-9._-]", "_").replaceAll("_+", "_");
        fileChooser.setInitialFileName(defaultFileName);

        fileChooser.getExtensionFilters().clear();
        // Đảm bảo bạn đang xuất .xlsx
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));
        File file = fileChooser.showSaveDialog(btnXuat.getScene().getWindow()); // Sử dụng fx:id của nút Xuất Excel

        if (file != null) {
            Workbook workbook = null;
            try {
                workbook = new XSSFWorkbook(); // Sử dụng XSSFWorkbook cho .xlsx

                // --- Định nghĩa Fonts và CellStyles (giữ nguyên code này của bạn) ---
                Font timesNewRomanFont = workbook.createFont();
                timesNewRomanFont.setFontName("Times New Roman");
                timesNewRomanFont.setFontHeightInPoints((short) 11);

                Font timesNewRomanBoldFont = workbook.createFont();
                timesNewRomanBoldFont.setFontName("Times New Roman");
                timesNewRomanBoldFont.setFontHeightInPoints((short) 12);
                timesNewRomanBoldFont.setBold(true);

                Font timesNewRomanHeaderFont = workbook.createFont();
                timesNewRomanHeaderFont.setFontName("Times New Roman");
                timesNewRomanHeaderFont.setFontHeightInPoints((short) 11);
                timesNewRomanHeaderFont.setBold(true);

                CellStyle titleStyle = createCellStyle(workbook, timesNewRomanBoldFont, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, false, false);
                CellStyle infoStyle = createCellStyle(workbook, timesNewRomanFont, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, false, false);
                CellStyle tableHeaderStyle = createCellStyle(workbook, timesNewRomanHeaderFont, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true, true);
                CellStyle dataCellStyle = createCellStyle(workbook, timesNewRomanFont, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true, true);
                CellStyle textDataCellStyle = createCellStyle(workbook, timesNewRomanFont, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, true, true);


                // == Sheet 1: Chi tiết Giáo viên ==
                // (Giữ nguyên code tạo Sheet "ChiTietGiaoVien" của bạn)
                Sheet sheetChiTiet = workbook.createSheet("Chi tiết giáo viên");
                int currentRowIndexChiTiet = 0;
                int lastColChiTiet = 4; // STT, HoTen, STQD, STTH, STDT

                createMergedInfoCell(sheetChiTiet, sheetChiTiet.createRow(currentRowIndexChiTiet++), 0, "BÁO CÁO CHI TIẾT SỐ TIẾT GIÁO VIÊN", titleStyle, 0, lastColChiTiet);
                createMergedInfoCell(sheetChiTiet, sheetChiTiet.createRow(currentRowIndexChiTiet++), 0, "Học kỳ: " + selectedHK, infoStyle, 0, lastColChiTiet);
                createMergedInfoCell(sheetChiTiet, sheetChiTiet.createRow(currentRowIndexChiTiet++), 0, "Thời khóa biểu áp dụng: " + selectedTKBValue, infoStyle, 0, lastColChiTiet);
                currentRowIndexChiTiet++;

                Row headerRowChiTiet = sheetChiTiet.createRow(currentRowIndexChiTiet++);
                String[] titlesChiTiet = {"STT", "Họ và Tên", "Số Tiết QĐ", "Số Tiết TH", "Số Tiết Dư/Thiếu"};
                for (int i = 0; i < titlesChiTiet.length; i++) {
                    createCell(headerRowChiTiet, i, titlesChiTiet[i], tableHeaderStyle);
                }

                List<GiaoVien> danhSachGVChiTiet = bctkDAO.getDanhSachGiaoVienVoiSoTietChiTiet(selectedHK.getMaHK(), selectedTKBValue.getMaTKB());
                int stt = 1;
                for (GiaoVien gv : danhSachGVChiTiet) {
                    Row dataRow = sheetChiTiet.createRow(currentRowIndexChiTiet++);
                    createCell(dataRow, 0, stt++, dataCellStyle);
                    createCell(dataRow, 1, gv.getHoGV() + " " + gv.getTenGV(), textDataCellStyle);
                    createCell(dataRow, 2, gv.getSoTietQuyDinh() != null ? gv.getSoTietQuyDinh() : 0, dataCellStyle);
                    createCell(dataRow, 3, gv.getSoTietThucHien() != null ? gv.getSoTietThucHien() : 0, dataCellStyle);
                    createCell(dataRow, 4, gv.getSoTietDuThieu() != null ? gv.getSoTietDuThieu() : 0, dataCellStyle);
                }
                for (int i = 0; i < titlesChiTiet.length; i++) {
                    if (i == 1) sheetChiTiet.setColumnWidth(i, 25 * 256);
                    else sheetChiTiet.autoSizeColumn(i);
                }


                // == Sheet 2: Tóm tắt Thống kê ==
                Sheet sheetTomTat = workbook.createSheet("Tóm tắt thống kê");
                int currentRowIndexTomTat = 0;
                int lastColTomTat = 2;

                createMergedInfoCell(sheetTomTat, sheetTomTat.createRow(currentRowIndexTomTat++), 0, "BÁO CÁO TÓM TẮT THỐNG KÊ SỐ TIẾT", titleStyle, 0, lastColTomTat);
                createMergedInfoCell(sheetTomTat, sheetTomTat.createRow(currentRowIndexTomTat++), 0, "Học kỳ: " + selectedHK, infoStyle, 0, lastColTomTat);
                createMergedInfoCell(sheetTomTat, sheetTomTat.createRow(currentRowIndexTomTat++), 0, "Thời khóa biểu áp dụng: " + selectedTKBValue, infoStyle, 0, lastColTomTat);
                currentRowIndexTomTat++;

                Row headerRowTomTat = sheetTomTat.createRow(currentRowIndexTomTat++);
                String[] titlesTomTat = {"Trạng Thái", "Số Lượng GV", "Tỷ Lệ (%)"};
                for (int i = 0; i < titlesTomTat.length; i++) {
                    createCell(headerRowTomTat, i, titlesTomTat[i], tableHeaderStyle);
                }

                Map<String, Integer> stats = bctkDAO.getSoTietDuThieuStats(selectedHK.getMaHK(), selectedTKBValue.getMaTKB());
                double totalTeachers = danhSachGVChiTiet.size();
                if (totalTeachers == 0) totalTeachers = 1;

                DecimalFormat df = new DecimalFormat("#0.00");

                String[] categories = {"ThieuNhieu", "Thieu", "Du", "DuVua", "DuNhieu"};
                String[] categoryLabels = {
                        "Thiếu nhiều (<= -4 tiết)",
                        "Thiếu (-1, -2 hoặc -3 tiết)",
                        "Đủ tiết (0 tiết)",
                        "Dư vừa (1, 2 hoặc 3 tiết)",
                        "Dư nhiều (>= 4 tiết)"
                };

                // Chuẩn bị dữ liệu cho biểu đồ
                List<String> chartCategoryList = new ArrayList<>();
                List<Number> chartValueList = new ArrayList<>();

                for (int i = 0; i < categories.length; i++) {
                    String categoryKey = categories[i];
                    String categoryLabel = categoryLabels[i];
                    int count = stats.getOrDefault(categoryKey, 0);
                    double percentage = (count / totalTeachers) * 100;

                    Row dataRow = sheetTomTat.createRow(currentRowIndexTomTat++);
                    createCell(dataRow, 0, categoryLabel, textDataCellStyle);
                    createCell(dataRow, 1, count, dataCellStyle);
                    createCell(dataRow, 2, df.format(percentage) + "%", dataCellStyle);

                    // Thêm vào danh sách cho biểu đồ chỉ khi count > 0 để biểu đồ không bị rối
                    if (count > 0) {
                        chartCategoryList.add(categoryLabel);
                        chartValueList.add(count);
                    }
                }
                for (int i = 0; i < titlesTomTat.length; i++) {
                    if (i == 0) sheetTomTat.setColumnWidth(i, 30 * 256);
                    else sheetTomTat.autoSizeColumn(i);
                }

                // --- THÊM BIỂU ĐỒ TRÒN VÀO ĐÂY ---
                if (!chartCategoryList.isEmpty()) {
                    // Vị trí và kích thước của biểu đồ (tùy chỉnh theo ý bạn)
                    // Ví dụ: đặt biểu đồ bên phải bảng tóm tắt
                    int chartStartRow = headerRowTomTat.getRowNum() -1; // Bắt đầu từ gần tiêu đề bảng tóm tắt
                    int chartStartCol = titlesTomTat.length + 1; // Cách bảng 1 cột
                    int chartEndRow = chartStartRow + 15; // Cao khoảng 15 dòng
                    int chartEndCol = chartStartCol + 8;  // Rộng khoảng 8 cột

                    createPieChartInExcel(sheetTomTat,
                            "Biểu Đồ Tỷ Lệ Số Tiết Dư/Thiếu",
                            chartCategoryList.toArray(new String[0]),
                            chartValueList.toArray(new Number[0]),
                            chartStartRow, chartStartCol, chartEndRow, chartEndCol);
                }
                // --- KẾT THÚC PHẦN THÊM BIỂU ĐỒ ---


                // Lưu file
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }
                showAlert("Xuất Excel thành công!", "Đã lưu báo cáo thống kê vào file:\n" + file.getAbsolutePath(), Alert.AlertType.INFORMATION);

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi xuất Excel", "Có lỗi xảy ra khi ghi file Excel: " + e.getMessage(), Alert.AlertType.ERROR);
            } catch (Exception e) { // Bắt thêm Exception chung cho các lỗi POI khác
                e.printStackTrace();
                showAlert("Lỗi không xác định", "Có lỗi không xác định xảy ra khi tạo file Excel: " + e.getMessage(), Alert.AlertType.ERROR);
            }
            finally {
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

    /**
     * Tạo biểu đồ tròn trong sheet Excel dựa trên dữ liệu thống kê.
     *
     * @param sheet        Sheet để vẽ biểu đồ.
     * @param title        Tiêu đề của biểu đồ.
     * @param categoryData Mảng chứa tên các danh mục (ví dụ: "Đủ tiết", "Thiếu",...).
     * @param valuesData   Mảng chứa giá trị số lượng tương ứng với mỗi danh mục.
     * @param row1         Chỉ số hàng bắt đầu của vùng neo biểu đồ.
     * @param col1         Chỉ số cột bắt đầu của vùng neo biểu đồ.
     * @param row2         Chỉ số hàng kết thúc của vùng neo biểu đồ.
     * @param col2         Chỉ số cột kết thúc của vùng neo biểu đồ.
     */
    private void createPieChartInExcel(Sheet sheet, String title,
                                       String[] categoryData, Number[] valuesData,
                                       int row1, int col1, int row2, int col2) {
        if (categoryData.length == 0 || valuesData.length == 0 || categoryData.length != valuesData.length) {
            System.out.println("Dữ liệu không hợp lệ để tạo biểu đồ tròn.");
            return;
        }

        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, row1, col2, row2);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText(title);
        chart.setTitleOverlay(false);

        // Tạo dữ liệu cho biểu đồ tròn
        CTPieChart pieChart = chart.getCTChart().getPlotArea().addNewPieChart();
        CTBoolean ctBoolean = pieChart.addNewVaryColors(); // Để mỗi slice có màu khác nhau
        ctBoolean.setVal(true);

        CTPieSer ser = pieChart.addNewSer();

        ser.addNewIdx().setVal(0); // Thứ tự series

        // Categories (tên các danh mục)
        CTAxDataSource cat = ser.addNewCat();
        CTStrData strData = cat.addNewStrLit();
        for (int i = 0; i < categoryData.length; i++) {
            CTStrVal ctStrVal = strData.addNewPt();
            ctStrVal.setIdx(i);
            ctStrVal.setV(categoryData[i]);
        }
        CTUnsignedInt catPtCount = strData.addNewPtCount();
        catPtCount.setVal(categoryData.length);

        // Giá trị (Values)
        CTNumDataSource val = ser.addNewVal();
        CTNumData numData = val.addNewNumLit();
        for (int i = 0; i < valuesData.length; i++) {
            CTNumVal ctNumVal = numData.addNewPt();
            ctNumVal.setIdx(i);
            ctNumVal.setV(valuesData[i].toString());
        }
        CTUnsignedInt valPtCount = numData.addNewPtCount();
        valPtCount.setVal(valuesData.length);

        // Thêm Data Labels - CHỈNH SỬA PHẦN NÀY
        CTDLbls dLbls = ser.addNewDLbls();
        dLbls.addNewShowVal().setVal(false);          // KHÔNG hiển thị giá trị
        dLbls.addNewShowPercent().setVal(true);       // Hiển thị phần trăm
        dLbls.addNewShowCatName().setVal(false);      // KHÔNG hiển thị tên danh mục
        dLbls.addNewShowLegendKey().setVal(false);    // KHÔNG hiển thị legend key
        dLbls.addNewShowSerName().setVal(false);      // KHÔNG hiển thị series name - QUAN TRỌNG!

        // Định dạng số cho data labels
        CTNumFmt numFmt = dLbls.addNewNumFmt();
        if (numFmt != null) {
            numFmt.setFormatCode("0%"); // Hiển thị phần trăm không có số thập phân (45%)
            // Hoặc dùng "0.0%" để có 45.0%
            numFmt.setSourceLinked(false); // Không liên kết với định dạng nguồn
        }

        // Thiết lập vị trí data labels (tùy chọn)
        CTDLblPos dLblPos = dLbls.addNewDLblPos();
        dLblPos.setVal(STDLblPos.BEST_FIT); // Hoặc STDLblPos.CTR (center), STDLblPos.OUT_END, etc.

        // Hiển thị Legend (chú giải)
        if (chart.getCTChart().getLegend() == null) chart.getCTChart().addNewLegend();
        chart.getCTChart().getLegend().addNewLegendPos().setVal(STLegendPos.R); // Vị trí chú giải (Right)
        chart.getCTChart().getLegend().addNewOverlay().setVal(false);
    }

    // Hàm showAlert đã được bạn định nghĩa (giữ nguyên hoặc dùng bản dưới nếu muốn)
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
