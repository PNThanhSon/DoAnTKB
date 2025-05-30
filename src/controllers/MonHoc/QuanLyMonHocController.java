package controllers.MonHoc;

import dao.MonHocDAO;
import dao.ToChuyenMonDAO;
import entities.MonHoc;
import entities.ToChuyenMon;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuanLyMonHocController {

    @FXML private BorderPane QLMHBorderPane;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> comboKhoiFilter;
    @FXML private ComboBox<ToChuyenMon> comboTCMFilter; // Use ToChuyenMon object
    @FXML private TableView<MonHoc> tableMonHoc;
    @FXML private Text txtThongBao;

    @FXML private TableColumn<MonHoc, String> colMaMH;
    @FXML private TableColumn<MonHoc, String> colTenMH;
    @FXML private TableColumn<MonHoc, String> colKhoi;
    @FXML private TableColumn<MonHoc, String> colTenTCM; // For displaying TenTCM

    private final MonHocDAO monHocDAO = new MonHocDAO();
    private final ToChuyenMonDAO toChuyenMonDAO = new ToChuyenMonDAO();
    private ObservableList<MonHoc> danhSachMonHocCurrent = FXCollections.observableArrayList();
    private Map<String, String> maTcmToTenTcmMap; // To map MaTCM to TenTCM

    @FXML
    public void initialize() {
        Platform.runLater(() -> QLMHBorderPane.requestFocus());
        setupTableColumns();
        loadToChuyenMonData(); // Load TCM data for filters and display
        loadMonHocData();
        initContextMenu();

        // Add listeners for dynamic filtering (optional, handleFilter button can also be used)
        searchField.textProperty().addListener((obs, oldVal, newVal) -> handleFilter());
        comboKhoiFilter.valueProperty().addListener((obs, oldVal, newVal) -> handleFilter());
        comboTCMFilter.valueProperty().addListener((obs, oldVal, newVal) -> handleFilter());

        // Set default value for ComboBoxes
        comboKhoiFilter.setValue("Tất cả");

    }

    private void setupTableColumns() {
        colMaMH.setCellValueFactory(new PropertyValueFactory<>("maMH"));
        colTenMH.setCellValueFactory(new PropertyValueFactory<>("tenMH"));
        colKhoi.setCellValueFactory(new PropertyValueFactory<>("khoi"));

        // For displaying TenTCM based on MaTCM
        colTenTCM.setCellValueFactory(cellData -> {
            String maTCM = cellData.getValue().getMaTCM();
            return new SimpleStringProperty(maTcmToTenTcmMap.getOrDefault(maTCM, maTCM));
        });
    }

    private void loadToChuyenMonData() {
        try {
            List<ToChuyenMon> tcmList = toChuyenMonDAO.getDanhSachTCM();
            maTcmToTenTcmMap = tcmList.stream()
                    .collect(Collectors.toMap(ToChuyenMon::getMaTCM, ToChuyenMon::getTenTCM));

            ObservableList<ToChuyenMon> tcmObservableList = FXCollections.observableArrayList();
            // Add a "Tất cả" option. Create a dummy ToChuyenMon object for this.
            ToChuyenMon tatCaTCM = new ToChuyenMon(null, "Tất cả", null, null);
            tcmObservableList.add(tatCaTCM); // For "All" filter
            tcmObservableList.addAll(tcmList);
            comboTCMFilter.setItems(tcmObservableList);
            comboTCMFilter.setValue(tatCaTCM); // Set "Tất cả" as default

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách tổ chuyên môn: " + e.getMessage());
        }
    }


    private void loadMonHocData() {
        try {
            String keyword = searchField.getText();
            String khoi = comboKhoiFilter.getValue();
            ToChuyenMon selectedTCM = comboTCMFilter.getValue();
            String maTCM = (selectedTCM != null && selectedTCM.getMaTCM() != null) ? selectedTCM.getMaTCM() : null;


            danhSachMonHocCurrent.setAll(monHocDAO.tracuuMonHoc(keyword, khoi, maTCM));
            // FXCollections.sort(danhSachMonHocCurrent, Comparator.comparing(MonHoc::getMaMH)); // Already sorted by DAO
            tableMonHoc.setItems(danhSachMonHocCurrent);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách môn học: " + e.getMessage());
        }
    }

    private void initContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem suaItem = new MenuItem("Sửa thông tin");
        suaItem.setOnAction(event -> handleSua());

        MenuItem xoaItem = new MenuItem("Xóa môn học");
        xoaItem.setOnAction(event -> handleXoa());

        contextMenu.getItems().addAll(suaItem, xoaItem);
        tableMonHoc.setContextMenu(contextMenu);
    }

    @FXML
    private void handleSearch() {
        loadMonHocData();
    }

    @FXML
    private void handleFilter() {
        loadMonHocData();
    }

    @FXML
    private void handleLamMoi() {
        searchField.clear();
        comboKhoiFilter.setValue("Tất cả");
        if (!comboTCMFilter.getItems().isEmpty()) {
            // Find the "Tất cả" ToChuyenMon object to set as default
            Optional<ToChuyenMon> tatCaOption = comboTCMFilter.getItems().stream()
                    .filter(tcm -> "Tất cả".equals(tcm.getTenTCM()) && tcm.getMaTCM() == null)
                    .findFirst();
            tatCaOption.ifPresent(comboTCMFilter::setValue);
        }
        loadMonHocData();
        txtThongBao.setText("");
    }

    @FXML
    private void handleThem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/MHform/ThemMonHocForm.fxml"));
            Parent root = loader.load();

            ThemMonHocController controller = loader.getController();
            Stage stage = new Stage();
            controller.setDialogStage(stage);
            controller.setOnSuccess(() -> {
                loadMonHocData();
                showThongBao("Thêm môn học thành công!");
            });

            stage.setTitle("Thêm Môn Học Mới");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(QLMHBorderPane.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi UI", "Không thể tải form thêm môn học: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleSua() {
        MonHoc selectedMonHoc = tableMonHoc.getSelectionModel().getSelectedItem();
        if (selectedMonHoc == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một môn học để sửa.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/MHform/SuaMonHocForm.fxml"));
            Parent root = loader.load();

            SuaMonHocController controller = loader.getController();
            Stage stage = new Stage();
            controller.setDialogStage(stage);
            controller.setMonHocToEdit(selectedMonHoc); // Pass the selected subject
            controller.setOnSuccess(() -> {
                loadMonHocData();
                showThongBao("Cập nhật môn học thành công!");
            });

            stage.setTitle("Sửa Thông Tin Môn Học");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(QLMHBorderPane.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi UI", "Không thể tải form sửa môn học: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleXoa() {
        MonHoc selectedMonHoc = tableMonHoc.getSelectionModel().getSelectedItem();
        if (selectedMonHoc == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một môn học để xóa.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc chắn muốn xóa môn học '" + selectedMonHoc.getTenMH() + "' (Mã: " + selectedMonHoc.getMaMH() + ")?",
                ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Xác nhận xóa");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                monHocDAO.xoaMonHoc(selectedMonHoc.getMaMH());
                loadMonHocData();
                showThongBao("Xóa môn học thành công!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể xóa môn học: " + e.getMessage() + ". Có thể môn học này đang được sử dụng ở nơi khác.");
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
