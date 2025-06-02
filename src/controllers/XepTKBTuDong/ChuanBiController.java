package controllers.XepTKBTuDong;

import dao.ThoiKhoaBieuDAO;
import dao.XepTKBDAO;
import entities.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.*;

public class ChuanBiController {

    @FXML private ComboBox<HocKy> hocKyComboBox;
    @FXML private ComboBox<ThoiKhoaBieu> tkbCoSoComboBox;
    // @FXML private Label tkbCoSoBuoiLabel; // Đã xóa khỏi FXML

    @FXML private TitledPane giaoVienLoaiTruPane; // Đảm bảo có fx:id này trong FXML
    @FXML private TextField searchGiaoVienTextField;
    @FXML private ListView<GiaoVien> giaoVienExcludeListView;

    @FXML private Button btnTiepTuc; // Đảm bảo có fx:id này trong FXML

    @FXML private Spinner<Integer> thu2Spinner;
    @FXML private Spinner<Integer> thu3Spinner;
    @FXML private Spinner<Integer> thu4Spinner;
    @FXML private Spinner<Integer> thu5Spinner;
    @FXML private Spinner<Integer> thu6Spinner;
    @FXML private Spinner<Integer> thu7Spinner;

    @FXML private TitledPane soTietTheoKhoiPane;
    @FXML private ComboBox<String> khoiComboBoxSoTiet;
    @FXML private Label phanPhoiTietLabel;
    @FXML private Label tongSoTietTuanLabel;

    @FXML private TitledPane flexibleSubjectsPane;
    @FXML private VBox flexibleSubjectTeacherSelectionVBox;

    private ThoiKhoaBieuDAO thoiKhoaBieuDAO;
    private XepTKBDAO xepTKBDAO;

    private ObservableList<GiaoVien> allGiaoVienList;
    private ObservableList<GiaoVien> excludedGiaoVienList;
    private final List<String> MA_MON_HOC_PREFIX_LINH_HOAT = Arrays.asList("TIN", "GDTC");
    private Map<String, Map<String, String>> preAssignedTeachersForFlexibleSubjects;

    private Map<String, Map<Integer, Integer>> soTietMoiThuTheoKhoi;
    private final List<String> KHOI_LIST = Arrays.asList("10", "11", "12");


    public ChuanBiController() {
        thoiKhoaBieuDAO = new ThoiKhoaBieuDAO();
        xepTKBDAO = new XepTKBDAO();
        allGiaoVienList = FXCollections.observableArrayList();
        excludedGiaoVienList = FXCollections.observableArrayList();
        preAssignedTeachersForFlexibleSubjects = new HashMap<>();

        soTietMoiThuTheoKhoi = new HashMap<>();
        int defaultPeriods = 5;
        for (String khoi : KHOI_LIST) {
            Map<Integer, Integer> defaultSettingsForKhoi = new HashMap<>();
            for (int i = 2; i <= 7; i++) {
                defaultSettingsForKhoi.put(i, defaultPeriods);
            }
            soTietMoiThuTheoKhoi.put(khoi, defaultSettingsForKhoi);
        }
    }

    @FXML
    public void initialize() {
        setupHocKyComboBox();
        setupTkbCoSoComboBox();
        loadAllGiaoVienList();
        setupGiaoVienExcludeListView(); // Cần được gọi sau loadAllGiaoVienList
        setupSearchGiaoVienTextField(); // Cần được gọi sau setupGiaoVienExcludeListView

        setupKhoiComboBoxSoTiet();
        setupDayPeriodSpinnersForKhoi();

        if (!KHOI_LIST.isEmpty()) {
            khoiComboBoxSoTiet.getSelectionModel().selectFirst();
            if (khoiComboBoxSoTiet.getSelectionModel().getSelectedItem() != null) {
                updateSpinnersAndLabelsForSelectedKhoi(khoiComboBoxSoTiet.getSelectionModel().getSelectedItem());
            }
        } else {
            phanPhoiTietLabel.setText("Phân phối tiết (CSDL): --");
            tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: --");
        }

        tkbCoSoComboBox.setDisable(true);
        // tkbCoSoBuoiLabel.setText("Buổi: --"); // Đã xóa
        flexibleSubjectsPane.setExpanded(false);
        giaoVienLoaiTruPane.setExpanded(false); // Có thể đặt mặc định đóng
        soTietTheoKhoiPane.setExpanded(true);
    }

    private void loadAllGiaoVienList() {
        List<GiaoVien> gvList = thoiKhoaBieuDAO.getDanhSachGiaoVien();
        if (gvList != null) {
            allGiaoVienList.setAll(gvList);
        } else {
            allGiaoVienList.clear(); // Đảm bảo rỗng nếu DAO trả về null
        }
    }

    private void setupHocKyComboBox() {
        List<HocKy> hocKyList = thoiKhoaBieuDAO.getDanhSachHocKy();
        if (hocKyList != null && !hocKyList.isEmpty()) {
            hocKyComboBox.setItems(FXCollections.observableArrayList(hocKyList));
            hocKyComboBox.setConverter(new StringConverter<>() {
                @Override public String toString(HocKy hocKy) { return hocKy == null ? "" : hocKy.toString(); }
                @Override public HocKy fromString(String string) {
                    return hocKyComboBox.getItems().stream().filter(hk -> hk.toString().equals(string)).findFirst().orElse(null);
                }
            });
        } else {
            hocKyComboBox.setPromptText("Không có học kỳ");
        }
    }

    private void setupTkbCoSoComboBox() {
        tkbCoSoComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(ThoiKhoaBieu tkb) { return tkb == null ? "Không chọn TKB cơ sở" : tkb.toString(); }
            @Override public ThoiKhoaBieu fromString(String string) {
                if ("Không chọn TKB cơ sở".equals(string)) return null;
                return tkbCoSoComboBox.getItems().stream().filter(tkb -> tkb != null && tkb.toString().equals(string)).findFirst().orElse(null);
            }
        });
        tkbCoSoComboBox.getItems().add(null); // Luôn có lựa chọn "Không chọn"
        tkbCoSoComboBox.getSelectionModel().selectFirst(); // Chọn mặc định
    }

    private void setupGiaoVienExcludeListView() {
        // Đảm bảo allGiaoVienList đã được tải
        if (allGiaoVienList.isEmpty() && giaoVienExcludeListView.getItems() != null) {
            // Nếu allGiaoVienList rỗng nhưng ListView có item (từ lần chạy trước), thì clear nó
            if (giaoVienExcludeListView.getItems() instanceof FilteredList) {
                ((FilteredList<GiaoVien>)giaoVienExcludeListView.getItems()).getSource().clear();
            } else {
                giaoVienExcludeListView.getItems().clear();
            }
        }
        FilteredList<GiaoVien> filteredData = new FilteredList<>(allGiaoVienList, p -> true);
        giaoVienExcludeListView.setItems(filteredData);

        giaoVienExcludeListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(GiaoVien gv, boolean empty) {
                super.updateItem(gv, empty);
                if (empty || gv == null) {
                    setText(null); setGraphic(null);
                } else {
                    CheckBox checkBox = new CheckBox(gv.getHoGV() + " " + gv.getTenGV() + " (" + gv.getMaGV() + ")");
                    checkBox.setSelected(excludedGiaoVienList.contains(gv));
                    checkBox.setOnAction(event -> {
                        if (checkBox.isSelected()) {
                            if (!excludedGiaoVienList.contains(gv)) excludedGiaoVienList.add(gv);
                        } else {
                            excludedGiaoVienList.remove(gv);
                        }
                    });
                    setGraphic(checkBox);
                }
            }
        });
    }

    private void setupSearchGiaoVienTextField() {
        searchGiaoVienTextField.textProperty().addListener((obs, oldV, newV) -> filterGiaoVienList(newV));
    }

    private void setupKhoiComboBoxSoTiet() {
        khoiComboBoxSoTiet.setItems(FXCollections.observableArrayList(KHOI_LIST));
    }

    private void setupDayPeriodSpinnersForKhoi() {
        int minPeriods = 0;
        int maxPeriods = 5;

        configureSpinner(thu2Spinner, minPeriods, maxPeriods);
        configureSpinner(thu3Spinner, minPeriods, maxPeriods);
        configureSpinner(thu4Spinner, minPeriods, maxPeriods);
        configureSpinner(thu5Spinner, minPeriods, maxPeriods);
        configureSpinner(thu6Spinner, minPeriods, maxPeriods);
        configureSpinner(thu7Spinner, minPeriods, maxPeriods);

        Spinner<?>[] spinners = {thu2Spinner, thu3Spinner, thu4Spinner, thu5Spinner, thu6Spinner, thu7Spinner};
        for (int i = 0; i < spinners.length; i++) {
            final int dayOfWeek = i + 2;
            spinners[i].valueProperty().addListener((obs, oldValue, newValue) -> {
                String selectedKhoi = khoiComboBoxSoTiet.getSelectionModel().getSelectedItem();
                if (selectedKhoi != null && newValue instanceof Integer) {
                    // Đảm bảo map cho khối tồn tại
                    soTietMoiThuTheoKhoi.computeIfAbsent(selectedKhoi, k -> {
                        Map<Integer, Integer> newMap = new HashMap<>();
                        for(int day=2; day<=7; day++) newMap.put(day, 0); // Khởi tạo với 0 nếu chưa có
                        return newMap;
                    });
                    soTietMoiThuTheoKhoi.get(selectedKhoi).put(dayOfWeek, (Integer) newValue);
                    updateTongSoTietTuanLabel(selectedKhoi);
                }
            });
        }
    }

    private void configureSpinner(Spinner<Integer> spinner, int min, int max) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, min);
        spinner.setValueFactory(valueFactory);
    }

    @FXML
    void handleKhoiSoTietSelection(ActionEvent event) {
        String selectedKhoi = khoiComboBoxSoTiet.getSelectionModel().getSelectedItem();
        if (selectedKhoi != null) {
            updateSpinnersAndLabelsForSelectedKhoi(selectedKhoi);
        }
    }

    private void updateSpinnersAndLabelsForSelectedKhoi(String khoi) {
        if (khoi == null) {
            phanPhoiTietLabel.setText("Phân phối tiết (CSDL): Chọn khối");
            tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: Chọn khối");
            return;
        }
        // Đảm bảo map cho khối tồn tại và có giá trị mặc định nếu chưa có
        Map<Integer, Integer> settingsForKhoi = soTietMoiThuTheoKhoi.computeIfAbsent(khoi, k -> {
            Map<Integer, Integer> newMap = new HashMap<>();
            int defaultPeriods = 5; // Hoặc giá trị mặc định bạn muốn
            for(int day=2; day<=7; day++) newMap.put(day, defaultPeriods);
            return newMap;
        });


        thu2Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(2, 0));
        thu3Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(3, 0));
        thu4Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(4, 0));
        thu5Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(5, 0));
        thu6Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(6, 0));
        thu7Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(7, 0));

        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        if (selectedHocKy != null) {
            Task<Integer> loadPhanPhoiTask = new Task<>() {
                @Override
                protected Integer call() throws Exception {
                    return xepTKBDAO.getTongPhanPhoiTietTheoKhoi(khoi, selectedHocKy.getMaHK());
                }
                @Override
                protected void succeeded() {
                    Platform.runLater(() -> phanPhoiTietLabel.setText("Phân phối tiết (CSDL): " + getValue() + " tiết"));
                }
                @Override
                protected void failed() {
                    Platform.runLater(() -> phanPhoiTietLabel.setText("Phân phối tiết (CSDL): Lỗi tải"));
                    getException().printStackTrace();
                }
            };
            new Thread(loadPhanPhoiTask).start();
        } else {
            phanPhoiTietLabel.setText("Phân phối tiết (CSDL): Chọn học kỳ");
        }
        updateTongSoTietTuanLabel(khoi);
    }

    private void updateTongSoTietTuanLabel(String khoi) {
        if (khoi == null || !soTietMoiThuTheoKhoi.containsKey(khoi)) {
            tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: --");
            return;
        }
        Map<Integer, Integer> settings = soTietMoiThuTheoKhoi.get(khoi);
        int tongTietCaiDat = settings.values().stream().mapToInt(Integer::intValue).sum();
        tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: " + tongTietCaiDat + " tiết/tuần");
    }

    private void filterGiaoVienList(String searchText) {
        // ... (giữ nguyên)
        FilteredList<GiaoVien> filteredData = (FilteredList<GiaoVien>) giaoVienExcludeListView.getItems();
        if (searchText == null || searchText.isEmpty()) {
            filteredData.setPredicate(s -> true);
        } else {
            String lowerCaseFilter = searchText.toLowerCase();
            filteredData.setPredicate(gv ->
                    (gv.getTenGV() != null && gv.getTenGV().toLowerCase().contains(lowerCaseFilter)) ||
                            (gv.getHoGV() != null && gv.getHoGV().toLowerCase().contains(lowerCaseFilter)) ||
                            (gv.getMaGV() != null && gv.getMaGV().toLowerCase().contains(lowerCaseFilter))
            );
        }
        giaoVienExcludeListView.refresh();
    }

    private void populateFlexibleSubjectTeacherSelectionUI(HocKy selectedHocKy) {
        // ... (giữ nguyên)
        flexibleSubjectTeacherSelectionVBox.getChildren().clear();
        preAssignedTeachersForFlexibleSubjects.clear();

        if (selectedHocKy == null) {
            flexibleSubjectTeacherSelectionVBox.getChildren().add(new Label("Vui lòng chọn Học Kỳ trước."));
            return;
        }

        List<Lop> tatCaLopTrongHK = xepTKBDAO.getDanhSachLop();
        Map<String, List<MonHocHoc>> phanCongTrongHK = xepTKBDAO.getPhanCongMonHocChoTatCaLop(selectedHocKy.getMaHK());

        if (tatCaLopTrongHK == null || tatCaLopTrongHK.isEmpty() || phanCongTrongHK == null || phanCongTrongHK.isEmpty()) {
            flexibleSubjectTeacherSelectionVBox.getChildren().add(new Label("Không có dữ liệu lớp hoặc phân công môn học cho học kỳ này."));
            return;
        }

        boolean foundFlexible = false;
        for (Lop lop : tatCaLopTrongHK) {
            List<MonHocHoc> monHocCuaLop = phanCongTrongHK.get(lop.getMaLop());
            if (monHocCuaLop == null) continue;

            for (MonHocHoc mhh : monHocCuaLop) {
                boolean isFlexible = MA_MON_HOC_PREFIX_LINH_HOAT.stream()
                        .anyMatch(prefix -> mhh.getMaMH().toUpperCase().startsWith(prefix.toUpperCase()));

                if (isFlexible) {
                    foundFlexible = true;
                    Label lbl = new Label("Lớp " + lop.getMaLop() + " - Môn " + mhh.getTenMH() + " ("+mhh.getMaMH()+"):");
                    ComboBox<GiaoVien> gvComboBox = new ComboBox<>();
                    gvComboBox.setPrefWidth(300);

                    List<GiaoVien> qualifiedTeachers = allGiaoVienList.stream()
                            .filter(gv -> !excludedGiaoVienList.contains(gv))
                            .filter(gv -> gv.getMaTCM() != null && gv.getMaTCM().equals(mhh.getMaTCM()))
                            .toList();

                    ObservableList<GiaoVien> gvOptions = FXCollections.observableArrayList();
                    GiaoVien autoSelectOption = new GiaoVien(null, "Để thuật toán", "tự chọn", null, null, null, null, null, null, null, null, null, null);
                    gvOptions.add(autoSelectOption);
                    gvOptions.addAll(qualifiedTeachers);
                    gvComboBox.setItems(gvOptions);
                    gvComboBox.getSelectionModel().select(autoSelectOption);

                    final String currentMaLop = lop.getMaLop();
                    final String currentMaMH = mhh.getMaMH();
                    gvComboBox.setOnAction(e -> {
                        GiaoVien selectedGV = gvComboBox.getSelectionModel().getSelectedItem();
                        preAssignedTeachersForFlexibleSubjects.computeIfAbsent(currentMaLop, k -> new HashMap<>());
                        if (selectedGV != null && selectedGV.getMaGV() != null) {
                            preAssignedTeachersForFlexibleSubjects.get(currentMaLop).put(currentMaMH, selectedGV.getMaGV());
                        } else {
                            preAssignedTeachersForFlexibleSubjects.get(currentMaLop).remove(currentMaMH);
                        }
                    });

                    gvComboBox.setConverter(new StringConverter<>() {
                        @Override public String toString(GiaoVien gv) {
                            if (gv == null || gv.getMaGV() == null) return "[ Để thuật toán tự chọn ]";
                            return gv.getHoGV() + " " + gv.getTenGV() + " (" + gv.getMaGV() +")";
                        }
                        @Override public GiaoVien fromString(String string) { return null; }
                    });

                    HBox hbox = new HBox(10, lbl, gvComboBox);
                    hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    flexibleSubjectTeacherSelectionVBox.getChildren().add(hbox);
                }
            }
        }
        if (!foundFlexible) {
            flexibleSubjectTeacherSelectionVBox.getChildren().add(new Label("Không có môn học linh hoạt (Tin, GDTC,...) trong học kỳ này."));
        }
    }

    @FXML
    void handleHocKySelection(ActionEvent event) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        tkbCoSoComboBox.getItems().clear();
        tkbCoSoComboBox.getItems().add(null);
        // tkbCoSoBuoiLabel.setText("Buổi: --"); // Đã xóa

        if (selectedHocKy != null) {
            tkbCoSoComboBox.setDisable(false);
            List<ThoiKhoaBieu> tkbList = thoiKhoaBieuDAO.getDanhSachThoiKhoaBieuTheoHocKy(selectedHocKy.getMaHK());
            if (tkbList != null && !tkbList.isEmpty()) {
                tkbCoSoComboBox.getItems().addAll(tkbList);
            } else {
                tkbCoSoComboBox.setPromptText("Không có TKB cho học kỳ này");
            }
            String currentSelectedKhoi = khoiComboBoxSoTiet.getSelectionModel().getSelectedItem();
            if (currentSelectedKhoi == null && !KHOI_LIST.isEmpty()) {
                currentSelectedKhoi = KHOI_LIST.getFirst();
                khoiComboBoxSoTiet.getSelectionModel().select(currentSelectedKhoi);
            } else if (currentSelectedKhoi != null) { // Nếu đã có khối được chọn, cập nhật lại label cho nó
                updateSpinnersAndLabelsForSelectedKhoi(currentSelectedKhoi);
            }


            populateFlexibleSubjectTeacherSelectionUI(selectedHocKy);

        } else {
            tkbCoSoComboBox.setDisable(true);
            tkbCoSoComboBox.setPromptText("Chọn TKB cơ sở (nếu có)");
            flexibleSubjectTeacherSelectionVBox.getChildren().clear();
            flexibleSubjectTeacherSelectionVBox.getChildren().add(new Label("Chọn Học Kỳ để tải danh sách môn học."));
            preAssignedTeachersForFlexibleSubjects.clear();
            phanPhoiTietLabel.setText("Phân phối tiết (CSDL): Chọn học kỳ");
            tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: Chọn học kỳ");
        }
        tkbCoSoComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    void handleTkbCoSoSelection(ActionEvent event) {
        // ThoiKhoaBieu selectedTKB = tkbCoSoComboBox.getSelectionModel().getSelectedItem();
        // tkbCoSoBuoiLabel.setText((selectedTKB != null && selectedTKB.getBuoi() != null) ? "Buổi: " + selectedTKB.getBuoi().toUpperCase() : "Buổi: --"); // Đã xóa
    }


    @FXML
    void handleTiepTuc(ActionEvent event) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        ThoiKhoaBieu selectedTkbCoSo = tkbCoSoComboBox.getSelectionModel().getSelectedItem();

        if (selectedHocKy == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn Học Kỳ.");
            return;
        }

        // soTietMoiThuTheoKhoi đã được cập nhật tự động bởi listeners của Spinners
        List<GiaoVien> finalExcludedGiaoVienList = new ArrayList<>(excludedGiaoVienList);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/XepTKBTuDong/XepTKBTuDong.fxml"));
            Parent root = loader.load();

            XepTKBTuDongController controller = loader.getController();
            controller.initData(selectedHocKy, selectedTkbCoSo, finalExcludedGiaoVienList, soTietMoiThuTheoKhoi, preAssignedTeachersForFlexibleSubjects);

            Stage stage = new Stage();
            stage.setTitle("Kết Quả Xếp Thời Khóa Biểu Tự Động");
            stage.setScene(new Scene(root));
            stage.setMinWidth(1000);
            stage.setMinHeight(700);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Mở Form", "Không thể mở cửa sổ hiển thị kết quả xếp TKB.\n" + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
