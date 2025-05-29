
package controllers.QuanLyGiaoVien;

import DAO.GiaoVienDAO;
import entities.GiaoVien;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Comparator;


public class QuanLyGiaoVienController {

    @FXML private BorderPane QLGVBorderPane;

    @FXML private TextField searchField;
    @FXML private TextField fieldQuydinh;
    @FXML private ComboBox<String> ListGioiTinh;
    @FXML private TableView<GiaoVien> tableGiaoVien;
    @FXML private Text txtThongBao;


    @FXML private TableColumn<GiaoVien, String> colMaGV;
    @FXML private TableColumn<GiaoVien, String> colHoGV;
    @FXML private TableColumn<GiaoVien, String> colTenGV;
    @FXML private TableColumn<GiaoVien, String> colGioiTinh;
    @FXML private TableColumn<GiaoVien, String> colSDT;
    @FXML private TableColumn<GiaoVien, String> colEmail;
    @FXML private TableColumn<GiaoVien, String> colChuyenMon;
    @FXML private TableColumn<GiaoVien, String> colMaTCM;
    @FXML private TableColumn<GiaoVien, Integer> colSoTietQuyDinh;
    @FXML private TableColumn<GiaoVien, Integer> colSoTietThucHien;
    @FXML private TableColumn<GiaoVien, Integer> colSoTietDuThieu;
    @FXML private TableColumn<GiaoVien, String> colMatKhau;
    @FXML private TableColumn<GiaoVien, String> colGhiChu;

    private final ContextMenu contextMenu = new ContextMenu();
    private final ObservableList<GiaoVien> danhSachGiaoVienCurrent = FXCollections.observableArrayList();
    private final GiaoVienDAO giaoVienDAO = new GiaoVienDAO();
    private FilteredList<GiaoVien> filteredList;

    @FXML
    public void initialize() {
        Platform.runLater(() -> QLGVBorderPane.requestFocus()); // tránh focus vào ô nhập liệu
        setupTableColumns(); // property để tự động update dữ liệu vào bảng
        setupComboBox();
        loadDataFromDB();
        initContextMenu();

        // Gắn bộ lọc khi người dùng nhập
//        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilterPredicate());
        ListGioiTinh.valueProperty().addListener((obs, oldVal, newVal) -> updateFilterPredicate());
        fieldQuydinh.textProperty().addListener((obs, oldVal, newVal) -> updateFilterPredicate());
    }

    private void initContextMenu() {
        MenuItem suaItem = new MenuItem("Sửa thông tin");
        suaItem.setOnAction(event -> handle_Sua());

        MenuItem xoaItem = new MenuItem("Xóa giáo viên");
        xoaItem.setOnAction(event -> handle_Xoa());

        contextMenu.getItems().addAll(suaItem, xoaItem);

        tableGiaoVien.setOnContextMenuRequested(event ->
                contextMenu.show(tableGiaoVien, event.getScreenX(), event.getScreenY()));
    }

    private void setupTableColumns() {
        colMaGV.setCellValueFactory(new PropertyValueFactory<>("maGV"));
        colHoGV.setCellValueFactory(new PropertyValueFactory<>("hoGV"));
        colTenGV.setCellValueFactory(new PropertyValueFactory<>("tenGV"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colChuyenMon.setCellValueFactory(new PropertyValueFactory<>("chuyenMon"));
        colMaTCM.setCellValueFactory(new PropertyValueFactory<>("maTCM"));
        colSoTietQuyDinh.setCellValueFactory(new PropertyValueFactory<>("soTietQuyDinh"));
        colSoTietThucHien.setCellValueFactory(new PropertyValueFactory<>("soTietThucHien"));
        colSoTietDuThieu.setCellValueFactory(new PropertyValueFactory<>("soTietDuThieu"));
        colMatKhau.setCellValueFactory(new PropertyValueFactory<>("matKhau"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
    }

    private void setupComboBox() {
        ListGioiTinh.setItems(FXCollections.observableArrayList("Tất cả", "Nam", "Nữ", "Khác"));
        ListGioiTinh.setValue("Tất cả");
    }


    private void loadDataFromDB() {
        try {
            danhSachGiaoVienCurrent.setAll(giaoVienDAO.TracuuGiaoVien(""));
            FXCollections.sort(danhSachGiaoVienCurrent, Comparator.comparing(GiaoVien::getMaGV));
            filteredList = new FilteredList<>(danhSachGiaoVienCurrent, p -> true);
            tableGiaoVien.setItems(filteredList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể lấy danh sách giáo viên: " + e.getMessage());
        }
    }

    private void updateFilterPredicate() {
        String keyword = searchField.getText().trim().toUpperCase();
        String gioiTinh = ListGioiTinh.getValue();
        String tietQD = fieldQuydinh.getText().trim();

        filteredList.setPredicate(gv -> {
            boolean matchSearch = keyword.isEmpty() ||
                    gv.getMaGV().toUpperCase().contains(keyword) ||
                    gv.getHoGV().toUpperCase().contains(keyword) ||
                    gv.getTenGV().toUpperCase().contains(keyword);

            boolean matchGender = gioiTinh.equals("Tất cả") || gv.getGioiTinh().equals(gioiTinh);
            boolean matchTiet = tietQD.isEmpty();
            if (tietQD.matches(".*[^0-9].*")) {
                return false;
            }
            if (!matchTiet && gv.getSoTietQuyDinh() != null) {
                if (gv.getSoTietQuyDinh().intValue() < Integer.parseInt(tietQD) + 1) {
                    matchTiet = true;
                }
            }
            return matchSearch && matchGender && matchTiet;

        });
    }

    @FXML
    private void handleSearch() {
        updateFilterPredicate();
    }

    @FXML
    private void handleLoc() {
        updateFilterPredicate();
    }

    @FXML
    private void handleLamMoi() {
        searchField.clear();
        fieldQuydinh.clear();
        ListGioiTinh.setValue("Tất cả");
        loadDataFromDB();
    }
    @FXML
    private void handleThem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/QuanLyGiaoVien/ThemGVForm.fxml"));
            Parent root = loader.load();

            ThemGVController controller = loader.getController();

            // Gửi callback để cập nhật và reload lại dữ liệu
            controller.setOnSuccess(() -> {
                loadDataFromDB();
                updateFilterPredicate();
                showThongBao("Thêm thành công!");
            });

            Stage stage = new Stage();
            stage.setTitle("Thêm giáo viên");
            stage.setScene(new Scene(root, 500, 580));
            stage.initModality(Modality.APPLICATION_MODAL); // chặn tương tác cửa sổ chính
            controller.setDialogStage(stage); // truyền stage vào controller
            stage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải form thêm: " + e.getMessage());
        }
    }

    // nút được tạo trong controller
    private void handle_Sua() {
        GiaoVien selected = tableGiaoVien.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn giáo viên để sửa.");
            return;
        }
        // điều phối giao diện sửa
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/QuanLyGiaoVien/SuaThongtinGVForm.fxml"));
            Parent root = loader.load();

            SuaThongtinGVController controller = loader.getController();
            controller.getGiaoVien(selected);

            // Gửi callback để cập nhật và reload lại dữ liệu
            controller.setOnSuccess(() -> {
                loadDataFromDB();
                updateFilterPredicate();
                showThongBao("Sửa thành công!");
            });

            Stage stage = new Stage();
            stage.setTitle("Sửa thông tin giáo viên");
            stage.setScene(new Scene(root, 500, 580));
            stage.initModality(Modality.APPLICATION_MODAL); // chặn tương tác cửa sổ chính
            controller.setDialogStage(stage); // truyền stage vào controller
            stage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải form sửa: " + e.getMessage());
        }
    }

    // nút được tạo trong controller
    private void handle_Xoa() {
        GiaoVien selected = tableGiaoVien.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn giáo viên để xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                giaoVienDAO.xoaGiaoVien(selected);
                loadDataFromDB();
                updateFilterPredicate();
                showThongBao("Xóa thành công!");
                showAlert(Alert.AlertType.INFORMATION, "Đã xóa", "Giáo viên đã bị xóa.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa: Lỗi ràng buộc khóa ngoại với YKien" + e.getMessage());
            }
        }
    }


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
}
