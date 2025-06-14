package controllers.LopHoc; // Correct package

import dao.GiaoVienDAO;
import dao.LopHocDAO;
import entities.GiaoVien;
import entities.Lop;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuanLyLopHocController {

    @FXML private BorderPane QLLHBorderPane;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> comboKhoiFilter;
    @FXML private ComboBox<GiaoVien> comboGVCNFilter;
    @FXML private TableView<Lop> tableLopHoc;
    @FXML private Text txtThongBao;

    @FXML private TableColumn<Lop, String> colMaLop;
    @FXML private TableColumn<Lop, String> colTenLop;
    @FXML private TableColumn<Lop, String> colKhoi;
    @FXML private TableColumn<Lop, String> colTenGVCN;

    private final LopHocDAO lopHocDAO = new LopHocDAO();
    private final GiaoVienDAO giaoVienDAO = new GiaoVienDAO();
    private ObservableList<Lop> danhSachLopHocCurrent = FXCollections.observableArrayList();
    private Map<String, String> maGvToTenGvMap;

    @FXML
    public void initialize() {
        Platform.runLater(() -> QLLHBorderPane.requestFocus());
        setupTableColumns();
        loadGiaoVienDataForFilter();
        loadLopHocData();
        initContextMenu();

        comboKhoiFilter.setValue("Tất cả");

        searchField.textProperty().addListener((obs, oldV, newV) -> handleFilter());
        comboKhoiFilter.valueProperty().addListener((obs, oldV, newV) -> handleFilter());
        comboGVCNFilter.valueProperty().addListener((obs, oldV, newV) -> handleFilter());

        comboGVCNFilter.setConverter(new StringConverter<>() {
            @Override
            public String toString(GiaoVien gv) {
                // If gv is null, it represents the "Tất cả" option
                if (gv == null) return "Tất cả";

                return gv.getHoGV() + " " + gv.getTenGV() + " (" + gv.getMaGV() + ")";
            }

            @Override
            public GiaoVien fromString(String string) {
                // Not needed for a non-editable ComboBox
                return null;
            }
        });
    }

    private void setupTableColumns() {
        colMaLop.setCellValueFactory(new PropertyValueFactory<>("maLop"));
        colTenLop.setCellValueFactory(new PropertyValueFactory<>("tenLop"));
        colKhoi.setCellValueFactory(new PropertyValueFactory<>("khoi"));

        colTenGVCN.setCellValueFactory(cellData -> {
            String maGVCN = cellData.getValue().getGvcn();
            return new SimpleStringProperty(maGvToTenGvMap.getOrDefault(maGVCN, maGVCN != null ? maGVCN : "Chưa có"));
        });
    }

    private void loadGiaoVienDataForFilter() {
        try {
            List<GiaoVien> gvList = giaoVienDAO.TracuuGiaoVien(""); // Assuming this method exists and works
            maGvToTenGvMap = gvList.stream()
                    .collect(Collectors.toMap(GiaoVien::getMaGV,
                            gv -> gv.getHoGV() + " " + gv.getTenGV(),
                            (name1, name2) -> name1)); // Handle potential duplicate MaGV

            ObservableList<GiaoVien> gvObservableList = FXCollections.observableArrayList();
            // Add null as the first item to represent "Tất cả"
            // This line (or the one after addAll) is likely your line 101 if error persists with this code
            gvObservableList.add(null);
            gvObservableList.addAll(gvList);

            comboGVCNFilter.setItems(gvObservableList);
            // Set null as the default selected value, which the StringConverter will display as "Tất cả"
            comboGVCNFilter.setValue(null);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách giáo viên: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadLopHocData() {
        try {
            String keyword = searchField.getText();
            String khoi = comboKhoiFilter.getValue();
            GiaoVien selectedGVCN = comboGVCNFilter.getValue();
            // If selectedGVCN is null (meaning "Tất cả" is selected), maGVCNFilterValue will be null
            String maGVCNFilterValue = (selectedGVCN != null) ? selectedGVCN.getMaGV() : null;

            danhSachLopHocCurrent.setAll(lopHocDAO.tracuuLopHoc(keyword, khoi, maGVCNFilterValue));
            tableLopHoc.setItems(danhSachLopHocCurrent);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách lớp học: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem suaItem = new MenuItem("Sửa thông tin lớp");
        suaItem.setOnAction(event -> moCuaSoSuaLop());

        MenuItem xoaItem = new MenuItem("Xóa lớp học");
        xoaItem.setOnAction(event -> xuLyXoaLop());

        contextMenu.getItems().addAll(suaItem, xoaItem);
        tableLopHoc.setContextMenu(contextMenu);
    }

    @FXML
    private void handleSearch() {
        loadLopHocData();
    }

    @FXML
    private void handleFilter() {
        loadLopHocData();
    }

    @FXML
    private void handleLamMoi() {
        searchField.clear();
        comboKhoiFilter.setValue("Tất cả");
        comboGVCNFilter.setValue(null);

        loadLopHocData();
        txtThongBao.setText("");
    }

    @FXML
    private void moCuaSoThemLop() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/LHform/ThemLopHocForm.fxml"));
            Parent root = loader.load();

            ThemLopHocController controller = loader.getController();
            Stage stage = new Stage();
            controller.setDialogStage(stage);
            controller.setOnSuccess(() -> {
                loadLopHocData();
                showThongBao("Thêm lớp học thành công!");
            });

            stage.setTitle("Thêm Lớp Học Mới");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(QLLHBorderPane.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi UI", "Không thể tải form thêm lớp học: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void moCuaSoSuaLop() {
        Lop selectedLopHoc = tableLopHoc.getSelectionModel().getSelectedItem();
        if (selectedLopHoc == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một lớp học để sửa.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/LHform/SuaLopHocForm.fxml"));
            Parent root = loader.load();

            SuaLopHocController controller = loader.getController();
            Stage stage = new Stage();
            controller.setDialogStage(stage);
            controller.khoiTaoDuLieuSua(selectedLopHoc);
            controller.setOnSuccess(() -> {
                loadLopHocData();
                showThongBao("Cập nhật lớp học thành công!");
            });

            stage.setTitle("Sửa Thông Tin Lớp Học");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(QLLHBorderPane.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi UI", "Không thể tải form sửa lớp học: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void xuLyXoaLop() {
        Lop selectedLopHoc = tableLopHoc.getSelectionModel().getSelectedItem();
        if (selectedLopHoc == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một lớp học để xóa.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc chắn muốn xóa lớp học '" + selectedLopHoc.getTenLop() + "' (Mã: " + selectedLopHoc.getMaLop() + ")?",
                ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Xác nhận xóa");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                lopHocDAO.xoaLopHoc(selectedLopHoc.getMaLop());
                loadLopHocData();
                showThongBao("Xóa lớp học thành công!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể xóa lớp học: " + e.getMessage() + ". Có thể lớp này đang được tham chiếu ở nơi khác.");
                e.printStackTrace();
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showThongBao(String message) {
        txtThongBao.setText(message);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> txtThongBao.setText("")));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
