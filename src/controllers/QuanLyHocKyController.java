package controllers;

import dao.HocKyDAO;
import entities.HocKy;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.Optional;
import java.util.List;

public class QuanLyHocKyController {

    @FXML private TableView<HocKy> tableHocKy;
    @FXML private TableColumn<HocKy, String> colSTT;
    @FXML private TableColumn<HocKy, String> colMaHK;
    @FXML private TableColumn<HocKy, String> colHK;
    @FXML private TableColumn<HocKy, String> colNH;
    @FXML private TextField txtMaHk;
    @FXML private TextField txtHK;
    @FXML private TextField txtNamHoc;
    @FXML private TextField txtSearch;
    @FXML private Button btnThem;

    private final ObservableList<HocKy> dsHocKy = FXCollections.observableArrayList();
    private FilteredList<HocKy> filteredHocKyList;
    private HocKyDAO hocKyDAO;

    @FXML
    public void initialize() {
        hocKyDAO = new HocKyDAO();

        // Cấu hình cột
        colSTT.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(String.valueOf(tableHocKy.getItems().indexOf(cellData.getValue()) + 1)).asString()
        );
        colMaHK.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaHK()));
        colHK.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTenHocKy()));
        colNH.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamHoc()));

        // Cho phép chỉnh sửa trên bảng cho cột Học Kỳ và Năm Học
        tableHocKy.setEditable(true);
        // KHÔNG cho phép sửa MaHK trực tiếp trong bảng để đảm bảo tính toàn vẹn dữ liệu
        // initEditableColumn(colMaHK, "MaHK"); // << Dòng này đã bị loại bỏ/comment
        initEditableColumn(colHK, "TenHocKy");
        initEditableColumn(colNH, "NamHoc");

        // Context menu cho việc xoá hàng
        tableHocKy.setRowFactory(tv -> {
            TableRow<HocKy> row = new TableRow<>();
            MenuItem delMenuItem = new MenuItem("🗑 Xoá học kỳ này");
            delMenuItem.setOnAction(e -> {
                if (row.getItem() != null) {
                    xuLyXoa(row.getItem());
                }
            });
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(new ContextMenu(delMenuItem))
            );
            return row;
        });

        taiDuLieu();

        // Cấu hình tìm kiếm
        filteredHocKyList = new FilteredList<>(dsHocKy, p -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            String filter = newValue == null ? "" : newValue.toLowerCase().trim();
            filteredHocKyList.setPredicate(hocKy -> {
                if (filter.isEmpty()) {
                    return true;
                }
                return hocKy.getMaHK().toLowerCase().contains(filter) ||
                        hocKy.getTenHocKy().toLowerCase().contains(filter) ||
                        hocKy.getNamHoc().toLowerCase().contains(filter);
            });
        });

        SortedList<HocKy> sortedData = new SortedList<>(filteredHocKyList);
        sortedData.comparatorProperty().bind(tableHocKy.comparatorProperty());
        tableHocKy.setItems(sortedData);

        // Đặt prompt text
        txtMaHk.setPromptText("Ví dụ: HK1_2425");
        txtHK.setPromptText("Ví dụ: Học Kỳ 1");
        txtNamHoc.setPromptText("Ví dụ: 2024-2025");
    }

    private void taiDuLieu() {
        dsHocKy.clear();
        List<HocKy> hocKyListFromDB = hocKyDAO.getAllHocKy();
        if (hocKyListFromDB != null && !hocKyListFromDB.isEmpty()) {
            dsHocKy.addAll(hocKyListFromDB);
        } else {
            // Có thể hiển thị thông báo nếu không có dữ liệu
            System.out.println("Không có dữ liệu học kỳ nào trong cơ sở dữ liệu hoặc có lỗi khi tải.");
        }
        System.out.println("Đã tải " + dsHocKy.size() + " học kỳ từ cơ sở dữ liệu.");
    }

    private void initEditableColumn(TableColumn<HocKy, String> column, String propertyName) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());

        column.setOnEditCommit(event -> {
            HocKy hocKyRow = event.getRowValue();
            String newValue = event.getNewValue();

            // Kiểm tra giá trị mới
            if (newValue == null) {
                tableHocKy.refresh(); // Hủy nếu giá trị mới là null
                return;
            }
            newValue = newValue.trim();

            // Lấy giá trị hiện tại để so sánh
            String currentValueInObject = "";
            switch (propertyName) {
                case "TenHocKy": currentValueInObject = hocKyRow.getTenHocKy(); break;
                case "NamHoc": currentValueInObject = hocKyRow.getNamHoc(); break;
                default:
                    tableHocKy.refresh();
                    return; // Không nên xảy ra nếu chỉ cấu hình cho TenHocKy và NamHoc
            }

            if (newValue.equals(currentValueInObject)) {
                tableHocKy.refresh(); // Hủy nếu không có thay đổi
                return;
            }

            // Validation
            if (newValue.isEmpty()) {
                warn(propertyName.equals("TenHocKy") ? "Tên học kỳ không được để trống." : "Năm học không được để trống.");
                tableHocKy.refresh();
                return;
            }
            if (propertyName.equals("NamHoc") && !newValue.matches("\\d{4}-\\d{4}")) {
                warn("Định dạng năm học không đúng (ví dụ: 2024-2025).");
                tableHocKy.refresh();
                return;
            }

            // Tạo một bản sao và cập nhật giá trị mới
            HocKy hocKyToUpdate = new HocKy(hocKyRow.getMaHK(), hocKyRow.getTenHocKy(), hocKyRow.getNamHoc());

            switch (propertyName) {
                case "TenHocKy":
                    hocKyToUpdate.setTenHocKy(newValue);
                    break;
                case "NamHoc":
                    hocKyToUpdate.setNamHoc(newValue);
                    break;
            }

            // Gọi DAO để cập nhật CSDL
            if (hocKyDAO.updateHocKy(hocKyToUpdate)) {
                // Cập nhật đối tượng trong dsHocKy để UI phản ánh thay đổi
                hocKyRow.setTenHocKy(hocKyToUpdate.getTenHocKy());
                hocKyRow.setNamHoc(hocKyToUpdate.getNamHoc());
                info("Đã cập nhật học kỳ '" + hocKyRow.getMaHK() + "' thành công.");
            } else {
                warn("Cập nhật học kỳ '" + hocKyRow.getMaHK() + "' thất bại. Vui lòng kiểm tra log hoặc thông báo lỗi từ CSDL.");
            }
            tableHocKy.refresh(); // Cập nhật lại bảng (quan trọng để hiển thị đúng nếu có lỗi hoặc để bỏ chế độ edit)
        });
    }

    @FXML
    private void xuLyThem() {
        String maHK = txtMaHk.getText().trim();
        String tenHK = txtHK.getText().trim();
        String namHoc = txtNamHoc.getText().trim();

        if (maHK.isEmpty() || tenHK.isEmpty() || namHoc.isEmpty()) {
            warn("Vui lòng nhập đủ Mã HK, Tên Học Kỳ và Năm học.");
            return;
        }

        if (hocKyDAO.maHKExists(maHK)) {
            warn("Mã học kỳ '" + maHK + "' đã tồn tại trong cơ sở dữ liệu. Vui lòng chọn mã khác.");
            txtMaHk.requestFocus(); // Focus vào trường MaHK để người dùng sửa
            return;
        }

        if (!namHoc.matches("\\d{4}-\\d{4}")) {
            warn("Định dạng năm học không đúng (ví dụ: 2024-2025).");
            txtNamHoc.requestFocus();
            return;
        }

        HocKy hocKyMoi = new HocKy(maHK, tenHK, namHoc);
        if (hocKyDAO.addHocKy(hocKyMoi)) {
            dsHocKy.add(hocKyMoi);
            info("Đã thêm học kỳ mới: " + maHK);
            txtMaHk.clear();
            txtHK.clear();
            txtNamHoc.clear();
            tableHocKy.scrollTo(hocKyMoi);
            tableHocKy.getSelectionModel().select(hocKyMoi);
        } else {
            // Lỗi đã được log trong DAO, có thể hiển thị thông báo chung hơn
            error("Thêm học kỳ thất bại. Vui lòng kiểm tra lại thông tin hoặc xem log để biết chi tiết.");
        }
    }

    private void xuLyXoa(HocKy selectedHocKy) {
        if (selectedHocKy == null) {
            // Trường hợp này không nên xảy ra nếu gọi từ context menu của một hàng có item
            warn("Không có học kỳ nào được chọn để xoá.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xoá");
        alert.setHeaderText("Bạn có chắc chắn muốn xoá học kỳ: " + selectedHocKy.getMaHK() + " (" + selectedHocKy.getTenHocKy() + " - " + selectedHocKy.getNamHoc() + ")?");
        alert.setContentText("Hành động này sẽ xoá dữ liệu khỏi cơ sở dữ liệu và không thể hoàn tác.");
        ButtonType btnDongY = new ButtonType("Đồng ý xoá", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnDongY, btnHuy);


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnDongY) {
            if (hocKyDAO.deleteHocKy(selectedHocKy.getMaHK())) {
                dsHocKy.remove(selectedHocKy);
                info("Đã xoá học kỳ: " + selectedHocKy.getMaHK());
            } else {
                // Thông báo lỗi chi tiết hơn đã được DAO log, ở đây chỉ cần thông báo chung
                error("Xoá học kỳ '" + selectedHocKy.getMaHK() + "' thất bại. Học kỳ có thể đang được sử dụng hoặc có lỗi CSDL.");
            }
        }
    }

    // Các phương thức helper cho Alert
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // Không dùng header
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void info(String message) {
        showAlert(Alert.AlertType.INFORMATION, "Thông báo", message);
    }

    private void warn(String message) {
        showAlert(Alert.AlertType.WARNING, "Cảnh báo", message);
    }

    private void error(String message) {
        showAlert(Alert.AlertType.ERROR, "Lỗi", message);
    }
}
