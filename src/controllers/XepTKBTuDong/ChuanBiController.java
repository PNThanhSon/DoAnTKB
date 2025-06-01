package controllers.XepTKBTuDong;

import dao.ThoiKhoaBieuDAO;
import dao.XepTKBDAO; // Cần DAO này để lấy danh sách lớp và phân công môn học
import entities.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.util.stream.Collectors;

public class ChuanBiController {

    @FXML private ComboBox<HocKy> hocKyComboBox;
    @FXML private ComboBox<ThoiKhoaBieu> tkbCoSoComboBox;
    @FXML private TextField searchGiaoVienTextField;
    @FXML private ListView<GiaoVien> giaoVienExcludeListView;
    @FXML private Button btnTiepTuc;

    @FXML private Spinner<Integer> thu2Spinner;
    @FXML private Spinner<Integer> thu3Spinner;
    @FXML private Spinner<Integer> thu4Spinner;
    @FXML private Spinner<Integer> thu5Spinner;
    @FXML private Spinner<Integer> thu6Spinner;
    @FXML private Spinner<Integer> thu7Spinner;

    @FXML private TitledPane flexibleSubjectsPane; // TitledPane mới
    @FXML private VBox flexibleSubjectTeacherSelectionVBox; // VBox bên trong ScrollPane

    private ThoiKhoaBieuDAO thoiKhoaBieuDAO;
    private XepTKBDAO xepTKBDAO; // Thêm DAO này

    private ObservableList<GiaoVien> allGiaoVienList; // Danh sách tất cả GV (trừ admin)
    private ObservableList<GiaoVien> excludedGiaoVienList; // GV bị loại trừ chung

    // Danh sách các MÃ MÔN HỌC (hoặc tiền tố mã môn học) được coi là linh hoạt
    // Ví dụ: "TIN" cho Tin học, "GDTC" cho Giáo dục thể chất
    // Bạn cần điều chỉnh danh sách này cho phù hợp với mã môn học thực tế của bạn
    private final List<String> MA_MON_HOC_PREFIX_LINH_HOAT = Arrays.asList("TIN", "GDTC");

    // Lưu trữ lựa chọn giáo viên cho các môn linh hoạt: Map<MaLop, Map<MaMH, MaGV_DaChon>>
    private Map<String, Map<String, String>> preAssignedTeachersForFlexibleSubjects;


    public ChuanBiController() {
        thoiKhoaBieuDAO = new ThoiKhoaBieuDAO();
        xepTKBDAO = new XepTKBDAO(); // Khởi tạo
        allGiaoVienList = FXCollections.observableArrayList();
        excludedGiaoVienList = FXCollections.observableArrayList();
        preAssignedTeachersForFlexibleSubjects = new HashMap<>();
    }

    @FXML
    public void initialize() {
        setupHocKyComboBox();
        setupTkbCoSoComboBox();
        loadAllGiaoVienList(); // Tải danh sách GV một lần khi khởi tạo
        setupGiaoVienExcludeListView();
        setupSearchGiaoVienTextField();
        setupDayPeriodSpinners();

        tkbCoSoComboBox.setDisable(true);
        flexibleSubjectsPane.setExpanded(false); // Mặc định đóng TitledPane
    }

    private void loadAllGiaoVienList() {
        List<GiaoVien> gvList = thoiKhoaBieuDAO.getDanhSachGiaoVien(); // Giả sử hàm này lấy tất cả GV (trừ ADMIN)
        if (gvList != null) {
            allGiaoVienList.setAll(gvList);
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
        tkbCoSoComboBox.getItems().add(null);
    }

    private void setupGiaoVienExcludeListView() {
        // allGiaoVienList đã được tải trong initialize
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

    private void setupDayPeriodSpinners() {
        // ... (giữ nguyên)
        int minPeriods = 0;
        int maxPeriods = 5;
        int defaultPeriods = 5;

        configureSpinner(thu2Spinner, minPeriods, maxPeriods, defaultPeriods);
        configureSpinner(thu3Spinner, minPeriods, maxPeriods, defaultPeriods);
        configureSpinner(thu4Spinner, minPeriods, maxPeriods, defaultPeriods);
        configureSpinner(thu5Spinner, minPeriods, maxPeriods, defaultPeriods);
        configureSpinner(thu6Spinner, minPeriods, maxPeriods, defaultPeriods);
        configureSpinner(thu7Spinner, minPeriods, maxPeriods, defaultPeriods);
    }

    private void configureSpinner(Spinner<Integer> spinner, int min, int max, int initial) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial));
    }

    private void filterGiaoVienList(String searchText) {
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

    @FXML
    void handleHocKySelection(ActionEvent event) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        tkbCoSoComboBox.getItems().clear();
        tkbCoSoComboBox.getItems().add(null);

        if (selectedHocKy != null) {
            tkbCoSoComboBox.setDisable(false);
            List<ThoiKhoaBieu> tkbList = thoiKhoaBieuDAO.getDanhSachThoiKhoaBieuTheoHocKy(selectedHocKy.getMaHK());
            if (tkbList != null && !tkbList.isEmpty()) {
                tkbCoSoComboBox.getItems().addAll(tkbList);
            } else {
                tkbCoSoComboBox.setPromptText("Không có TKB cho học kỳ này");
            }
            // Tải và hiển thị UI chọn GV cho môn linh hoạt
            populateFlexibleSubjectTeacherSelectionUI(selectedHocKy);

        } else {
            tkbCoSoComboBox.setDisable(true);
            tkbCoSoComboBox.setPromptText("Chọn TKB cơ sở (nếu có)");
            flexibleSubjectTeacherSelectionVBox.getChildren().clear(); // Xóa lựa chọn cũ
            flexibleSubjectTeacherSelectionVBox.getChildren().add(new Label("Chọn Học Kỳ để tải danh sách môn học."));
            preAssignedTeachersForFlexibleSubjects.clear();
        }
        tkbCoSoComboBox.getSelectionModel().selectFirst();
    }

    private void populateFlexibleSubjectTeacherSelectionUI(HocKy selectedHocKy) {
        flexibleSubjectTeacherSelectionVBox.getChildren().clear();
        preAssignedTeachersForFlexibleSubjects.clear();

        if (selectedHocKy == null) {
            flexibleSubjectTeacherSelectionVBox.getChildren().add(new Label("Vui lòng chọn Học Kỳ trước."));
            return;
        }

        List<Lop> tatCaLopTrongHK = xepTKBDAO.getDanhSachLop(); // Lấy tất cả lớp
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
                    gvComboBox.setPrefWidth(300); // Đặt chiều rộng cho ComboBox

                    // Lọc giáo viên hợp lệ (chưa bị loại trừ chung) và thuộc TCM của môn học
                    List<GiaoVien> qualifiedTeachers = allGiaoVienList.stream()
                            .filter(gv -> !excludedGiaoVienList.contains(gv)) // Loại trừ GV đã chọn ở ListView trên
                            .filter(gv -> gv.getMaTCM() != null && gv.getMaTCM().equals(mhh.getMaTCM()))
                            .collect(Collectors.toList());

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
    void handleTkbCoSoSelection(ActionEvent event) {
    }

    @FXML
    void handleTiepTuc(ActionEvent event) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        ThoiKhoaBieu selectedTkbCoSo = tkbCoSoComboBox.getSelectionModel().getSelectedItem();

        if (selectedHocKy == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn Học Kỳ.");
            return;
        }

        Map<Integer, Integer> soTietMoiThu = new HashMap<>();
        soTietMoiThu.put(2, thu2Spinner.getValue());
        soTietMoiThu.put(3, thu3Spinner.getValue());
        soTietMoiThu.put(4, thu4Spinner.getValue());
        soTietMoiThu.put(5, thu5Spinner.getValue());
        soTietMoiThu.put(6, thu6Spinner.getValue());
        soTietMoiThu.put(7, thu7Spinner.getValue());

        List<GiaoVien> finalExcludedGiaoVienList = new ArrayList<>(excludedGiaoVienList);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/XepTKBTuDong/XepTKBTuDong.fxml"));
            Parent root = loader.load();

            XepTKBTuDongController controller = loader.getController();
            // Truyền thêm preAssignedTeachersForFlexibleSubjects
            controller.initData(selectedHocKy, selectedTkbCoSo, finalExcludedGiaoVienList, soTietMoiThu, preAssignedTeachersForFlexibleSubjects);

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
