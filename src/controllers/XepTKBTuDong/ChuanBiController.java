package controllers.XepTKBTuDong;

// Bỏ import của Jackson
import controllers.XepTKBTuDong.helpers.ChuanBiSettings; // Đảm bảo đường dẫn này đúng
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChuanBiController {

    @FXML private ComboBox<HocKy> hocKyComboBox;
    @FXML private ComboBox<ThoiKhoaBieu> tkbCoSoComboBox;
    @FXML private TitledPane giaoVienCustomSettingsPane;
    @FXML private TextField searchGiaoVienTextField;
    @FXML private ListView<GiaoVien> giaoVienListView;
    @FXML private Button btnTiepTuc;
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
    @FXML private TitledPane classCustomSettingsPane;
    @FXML private TextField searchLopTextField;
    @FXML private ListView<Lop> lopListView;

    private ThoiKhoaBieuDAO thoiKhoaBieuDAO;
    private XepTKBDAO xepTKBDAO;

    private ObservableList<GiaoVien> allGiaoVienList;
    private ObservableList<Lop> allLopList;
    private Map<String, TeacherCustomSettings> teacherCustomSettingsMap;
    private Map<String, Map<String, List<String>>> classTeacherAssignmentsMap;
    private Map<String, Map<Integer, Integer>> soTietMoiThuTheoKhoi;
    private final List<String> KHOI_LIST = Arrays.asList("10", "11", "12");
    private Map<String, List<MonHocHoc>> phanCongMonHocChoTatCaLopTrongHK;

    // Đường dẫn file cài đặt
    private final String SETTINGS_DIR_PATH = System.getProperty("user.home") + File.separator + ".tkb_scheduler_settings";
    private final String SETTINGS_FILE_NAME = "chuan_bi_settings.txt";
    private final String SETTINGS_FILE_PATH = SETTINGS_DIR_PATH + File.separator + SETTINGS_FILE_NAME;

    // Định nghĩa các tiền tố cho file TXT
    private static final String PREFIX_HOC_KY = "HOC_KY=";
    private static final String PREFIX_TKB_CO_SO = "TKB_CO_SO=";
    private static final String PREFIX_KHOI_TIET = "KHOI_TIET;";
    private static final String PREFIX_GV_SETTING = "GV_SETTING;";
    private static final String PREFIX_LOP_ASSIGN = "LOP_ASSIGN;";


    public ChuanBiController() {
        thoiKhoaBieuDAO = new ThoiKhoaBieuDAO();
        xepTKBDAO = new XepTKBDAO();
        allGiaoVienList = FXCollections.observableArrayList();
        allLopList = FXCollections.observableArrayList();
        teacherCustomSettingsMap = new HashMap<>();
        classTeacherAssignmentsMap = new HashMap<>();
        phanCongMonHocChoTatCaLopTrongHK = new HashMap<>();
        soTietMoiThuTheoKhoi = new HashMap<>();

        // Khởi tạo giá trị mặc định cho soTietMoiThuTheoKhoi
        // Sẽ bị ghi đè nếu có file settings
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
        loadAllGiaoVienList();
        loadAllLopList();

        Optional<ChuanBiSettings> loadedSettingsOpt = loadSettingsFromFile();
        String savedMaHK = null;
        // String savedMaTkbCoSo = null; // Sẽ được xử lý trong handleHocKySelection

        if (loadedSettingsOpt.isPresent()) {
            ChuanBiSettings settings = loadedSettingsOpt.get();
            savedMaHK = settings.getSelectedMaHK();
            // savedMaTkbCoSo = settings.getSelectedMaTkbCoSo(); // Sẽ được dùng sau

            if (settings.getSoTietMoiThuTheoKhoi() != null && !settings.getSoTietMoiThuTheoKhoi().isEmpty()) {
                this.soTietMoiThuTheoKhoi.putAll(settings.getSoTietMoiThuTheoKhoi());
            }
            if (settings.getTeacherCustomSettingsMap() != null) {
                this.teacherCustomSettingsMap.putAll(settings.getTeacherCustomSettingsMap());
            }
            if (settings.getClassTeacherAssignmentsMap() != null) {
                this.classTeacherAssignmentsMap.putAll(settings.getClassTeacherAssignmentsMap());
            }
            System.out.println("Đã tải cài đặt từ file: " + SETTINGS_FILE_PATH);
        } else {
            System.out.println("Không tìm thấy file cài đặt hoặc có lỗi khi tải. Sử dụng cài đặt mặc định.");
        }

        setupHocKyComboBox(savedMaHK); // Truyền MaHK đã lưu
        setupTkbCoSoComboBox(); // Chỉ thiết lập converter, giá trị sẽ được set trong handleHocKySelection

        setupGiaoVienCustomSettingsListView();
        setupSearchGiaoVienTextField();
        setupLopCustomSettingsListView();
        setupSearchLopTextField();
        setupKhoiComboBoxSoTiet();
        setupDayPeriodSpinnersForKhoi();

        // Nếu có học kỳ được chọn (từ file), kích hoạt handleHocKySelection
        // để tải TKB cơ sở và các thông tin liên quan.
        // Nếu không, UI sẽ chờ người dùng chọn.
        if (hocKyComboBox.getValue() != null) {
            handleHocKySelection(null);
        } else {
            tkbCoSoComboBox.setDisable(true);
            tkbCoSoComboBox.setPromptText("Chọn học kỳ trước");
            phanPhoiTietLabel.setText("Phân phối tiết (CSDL): Chọn học kỳ");
            tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: Chọn học kỳ");
        }

        // Cập nhật spinners cho khối được chọn (hoặc khối đầu tiên)
        String initialKhoiForSpinners = khoiComboBoxSoTiet.getSelectionModel().getSelectedItem();
        if (initialKhoiForSpinners == null && !KHOI_LIST.isEmpty()) {
            khoiComboBoxSoTiet.getSelectionModel().selectFirst();
            initialKhoiForSpinners = khoiComboBoxSoTiet.getSelectionModel().getSelectedItem();
        }
        if (initialKhoiForSpinners != null) {
            updateSpinnersAndLabelsForSelectedKhoi(initialKhoiForSpinners);
        }

        soTietTheoKhoiPane.setExpanded(true);
    }

    private void setupHocKyComboBox(String savedMaHK) {
        List<HocKy> hocKyList = thoiKhoaBieuDAO.getDanhSachHocKy();
        if (hocKyList != null && !hocKyList.isEmpty()) {
            hocKyComboBox.setItems(FXCollections.observableArrayList(hocKyList));
            hocKyComboBox.setConverter(new StringConverter<>() {
                @Override public String toString(HocKy hocKy) { return hocKy == null ? "" : hocKy.toString(); }
                @Override public HocKy fromString(String string) {
                    return hocKyComboBox.getItems().stream().filter(hk -> hk.toString().equals(string)).findFirst().orElse(null);
                }
            });

            if (savedMaHK != null) {
                hocKyList.stream()
                        .filter(hk -> hk.getMaHK().equals(savedMaHK))
                        .findFirst()
                        .ifPresent(hocKyComboBox::setValue);
            }
            // Nếu không có savedMaHK hoặc không tìm thấy, không chọn gì cả, để prompt text hiển thị
            if (hocKyComboBox.getValue() == null) {
                hocKyComboBox.setPromptText("Chọn học kỳ");
            }
        } else {
            hocKyComboBox.setPromptText("Không có học kỳ");
            hocKyComboBox.setDisable(true);
        }
    }

    @FXML
    void handleTiepTuc(ActionEvent event) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        if (selectedHocKy == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn Học Kỳ.");
            return;
        }
        saveSettingsToFile(); // Lưu cài đặt hiện tại

        ThoiKhoaBieu selectedTkbCoSo = tkbCoSoComboBox.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/XepTKBTuDong/XepTKBTuDong.fxml"));
            Parent root = loader.load();
            XepTKBTuDongController controller = loader.getController();
            controller.initData(selectedHocKy, selectedTkbCoSo, teacherCustomSettingsMap, soTietMoiThuTheoKhoi, classTeacherAssignmentsMap);
            Stage stage = new Stage();
            stage.setTitle("Kết Quả Xếp Thời Khóa Biểu Tự Động");
            stage.setScene(new Scene(root));
            stage.setMinWidth(1000); stage.setMinHeight(700);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Mở Form", "Không thể mở cửa sổ hiển thị kết quả xếp TKB.\n" + e.getMessage());
        }
    }

    private void saveSettingsToFile() {
        String currentSelectedKhoiInSpinner = khoiComboBoxSoTiet.getValue();
        if (currentSelectedKhoiInSpinner != null) {
            updateSettingsFromSpinners(currentSelectedKhoiInSpinner);
        }

        File settingsDir = new File(SETTINGS_DIR_PATH);
        if (!settingsDir.exists()) {
            if (!settingsDir.mkdirs()) {
                System.err.println("Không thể tạo thư mục cài đặt: " + SETTINGS_DIR_PATH);
                showAlert(Alert.AlertType.ERROR, "Lỗi Lưu Cài Đặt", "Không thể tạo thư mục lưu cài đặt.");
                return;
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SETTINGS_FILE_PATH), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write("# ChuanBiController Settings File");
            writer.newLine();

            HocKy selectedHocKy = hocKyComboBox.getValue();
            if (selectedHocKy != null) {
                writer.write(PREFIX_HOC_KY + selectedHocKy.getMaHK());
                writer.newLine();
            }

            ThoiKhoaBieu selectedTkbCoSo = tkbCoSoComboBox.getValue();
            if (selectedTkbCoSo != null && selectedTkbCoSo.getMaTKB() != null) { // Chỉ lưu nếu có TKB cơ sở thực sự được chọn
                writer.write(PREFIX_TKB_CO_SO + selectedTkbCoSo.getMaTKB());
                writer.newLine();
            }

            for (Map.Entry<String, Map<Integer, Integer>> entryKhoi : soTietMoiThuTheoKhoi.entrySet()) {
                String khoi = entryKhoi.getKey();
                Map<Integer, Integer> tietTheoThu = entryKhoi.getValue();
                String tietTheoThuStr = tietTheoThu.entrySet().stream()
                        .map(e -> e.getKey() + "=" + e.getValue())
                        .collect(Collectors.joining(","));
                writer.write(PREFIX_KHOI_TIET + khoi + ";" + tietTheoThuStr);
                writer.newLine();
            }

            for (Map.Entry<String, TeacherCustomSettings> entryGV : teacherCustomSettingsMap.entrySet()) {
                TeacherCustomSettings gvSetting = entryGV.getValue();
                String prefsStr = gvSetting.getSubjectTeachingPreference().entrySet().stream()
                        .map(e -> e.getKey() + "=" + e.getValue())
                        .collect(Collectors.joining(","));
                writer.write(PREFIX_GV_SETTING + gvSetting.getMaGV() + ";" + gvSetting.isParticipateInScheduling() + ";" + prefsStr);
                writer.newLine();
            }

            for (Map.Entry<String, Map<String, List<String>>> entryLop : classTeacherAssignmentsMap.entrySet()) {
                String maLop = entryLop.getKey();
                Map<String, List<String>> assignments = entryLop.getValue();
                String assignmentsStr = assignments.entrySet().stream()
                        .filter(e -> e.getValue() != null && !e.getValue().isEmpty()) // Chỉ lưu nếu có GV được gán
                        .map(e -> e.getKey() + "=" + String.join("~", e.getValue())) // Dùng dấu ~ để nối các MaGV
                        .collect(Collectors.joining("|"));
                if (!assignmentsStr.isEmpty()) { // Chỉ ghi nếu có assignment thực sự
                    writer.write(PREFIX_LOP_ASSIGN + maLop + ";" + assignmentsStr);
                    writer.newLine();
                }
            }
            System.out.println("Đã lưu cài đặt vào: " + SETTINGS_FILE_PATH);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Lưu Cài Đặt", "Không thể lưu file cài đặt.\n" + e.getMessage());
        }
    }

    private Optional<ChuanBiSettings> loadSettingsFromFile() {
        File settingsFile = new File(SETTINGS_FILE_PATH);
        if (!settingsFile.exists()) {
            return Optional.empty();
        }

        ChuanBiSettings settings = new ChuanBiSettings();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(SETTINGS_FILE_PATH), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty()) continue;

                if (line.startsWith(PREFIX_HOC_KY)) {
                    settings.setSelectedMaHK(line.substring(PREFIX_HOC_KY.length()));
                } else if (line.startsWith(PREFIX_TKB_CO_SO)) {
                    settings.setSelectedMaTkbCoSo(line.substring(PREFIX_TKB_CO_SO.length()));
                } else if (line.startsWith(PREFIX_KHOI_TIET)) {
                    String[] parts = line.substring(PREFIX_KHOI_TIET.length()).split(";", 2);
                    if (parts.length == 2) {
                        String khoi = parts[0];
                        Map<Integer, Integer> tietTheoThu = new HashMap<>();
                        if (!parts[1].isEmpty()) {
                            for (String partTiet : parts[1].split(",")) {
                                String[] kv = partTiet.split("=");
                                if (kv.length == 2) {
                                    try { tietTheoThu.put(Integer.parseInt(kv[0]), Integer.parseInt(kv[1])); }
                                    catch (NumberFormatException e) { System.err.println("Lỗi parse số tiết khi tải: " + partTiet + " cho dòng: " + line); }
                                }
                            }
                        }
                        settings.getSoTietMoiThuTheoKhoi().put(khoi, tietTheoThu);
                    }
                } else if (line.startsWith(PREFIX_GV_SETTING)) {
                    String[] parts = line.substring(PREFIX_GV_SETTING.length()).split(";", 3);
                    if (parts.length >= 2) {
                        String maGV = parts[0];
                        boolean participate = Boolean.parseBoolean(parts[1]);
                        TeacherCustomSettings gvSetting = new TeacherCustomSettings(maGV);
                        gvSetting.setParticipateInScheduling(participate);
                        if (parts.length == 3 && !parts[2].isEmpty()) {
                            for (String partPref : parts[2].split(",")) {
                                String[] kv = partPref.split("=");
                                if (kv.length == 2) {
                                    gvSetting.getSubjectTeachingPreference().put(kv[0], Boolean.parseBoolean(kv[1]));
                                }
                            }
                        }
                        settings.getTeacherCustomSettingsMap().put(maGV, gvSetting);
                    }
                } else if (line.startsWith(PREFIX_LOP_ASSIGN)) {
                    String[] parts = line.substring(PREFIX_LOP_ASSIGN.length()).split(";", 2);
                    if (parts.length == 2) {
                        String maLop = parts[0];
                        Map<String, List<String>> assignments = new HashMap<>();
                        if (!parts[1].isEmpty()) {
                            for (String partAssign : parts[1].split("\\|")) {
                                String[] kv = partAssign.split("=", 2);
                                if (kv.length == 2 && !kv[1].isEmpty()) { // Đảm bảo có MaGV
                                    assignments.put(kv[0], Arrays.asList(kv[1].split("~")));
                                }
                            }
                        }
                        if (!assignments.isEmpty()) {
                            settings.getClassTeacherAssignmentsMap().put(maLop, assignments);
                        }
                    }
                }
            }
            return Optional.of(settings);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Lỗi Tải Cài Đặt", "Không thể đọc file cài đặt. Sử dụng giá trị mặc định.\n" + e.getMessage());
        }
        return Optional.empty();
    }

    private void updateSettingsFromSpinners(String khoi) {
        if (khoi == null) return;
        Map<Integer, Integer> settingsForKhoi = soTietMoiThuTheoKhoi.computeIfAbsent(khoi, k -> new HashMap<>());
        settingsForKhoi.put(2, thu2Spinner.getValue());
        settingsForKhoi.put(3, thu3Spinner.getValue());
        settingsForKhoi.put(4, thu4Spinner.getValue());
        settingsForKhoi.put(5, thu5Spinner.getValue());
        settingsForKhoi.put(6, thu6Spinner.getValue());
        settingsForKhoi.put(7, thu7Spinner.getValue());
    }

    @FXML
    void handleHocKySelection(ActionEvent event) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        tkbCoSoComboBox.getItems().clear();
        tkbCoSoComboBox.getItems().add(null); // "Không chọn"

        String savedMaTkbCoSo = null;
        Optional<ChuanBiSettings> loadedSettingsOpt = loadSettingsFromFile();
        if(loadedSettingsOpt.isPresent() && selectedHocKy != null &&
                selectedHocKy.getMaHK().equals(loadedSettingsOpt.get().getSelectedMaHK())) {
            savedMaTkbCoSo = loadedSettingsOpt.get().getSelectedMaTkbCoSo();
        }

        if (selectedHocKy != null) {
            phanCongMonHocChoTatCaLopTrongHK = xepTKBDAO.getPhanCongMonHocChoTatCaLop(selectedHocKy.getMaHK());
            tkbCoSoComboBox.setDisable(false);
            List<ThoiKhoaBieu> tkbList = thoiKhoaBieuDAO.getDanhSachThoiKhoaBieuTheoHocKy(selectedHocKy.getMaHK());
            if (tkbList != null && !tkbList.isEmpty()) {
                tkbCoSoComboBox.getItems().addAll(tkbList);
                final String finalSavedMaTkbCoSo = savedMaTkbCoSo;
                if (finalSavedMaTkbCoSo != null) {
                    tkbList.stream()
                            .filter(tkb -> tkb.getMaTKB().equals(finalSavedMaTkbCoSo))
                            .findFirst()
                            .ifPresentOrElse(
                                    tkbCoSoComboBox::setValue,
                                    () -> tkbCoSoComboBox.getSelectionModel().selectFirst()
                            );
                } else {
                    tkbCoSoComboBox.getSelectionModel().selectFirst(); // Chọn "Không chọn"
                }
            } else {
                tkbCoSoComboBox.setPromptText("Không có TKB cho học kỳ này");
                tkbCoSoComboBox.getSelectionModel().selectFirst();
            }

            String currentSelectedKhoi = khoiComboBoxSoTiet.getSelectionModel().getSelectedItem();
            if (currentSelectedKhoi == null && !KHOI_LIST.isEmpty()) {
                currentSelectedKhoi = KHOI_LIST.get(0); // Hoặc KHOI_LIST.getFirst() nếu dùng Java 21+
                khoiComboBoxSoTiet.getSelectionModel().select(currentSelectedKhoi);
            } else if (currentSelectedKhoi != null) {
                updateSpinnersAndLabelsForSelectedKhoi(currentSelectedKhoi);
            }
        } else {
            phanCongMonHocChoTatCaLopTrongHK.clear();
            tkbCoSoComboBox.setDisable(true);
            tkbCoSoComboBox.setPromptText("Chọn TKB cơ sở (nếu có)");
            tkbCoSoComboBox.getSelectionModel().selectFirst();
            phanPhoiTietLabel.setText("Phân phối tiết (CSDL): Chọn học kỳ");
            tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: Chọn học kỳ");
        }
    }

    // Các phương thức còn lại giữ nguyên
    private void loadAllLopList() {
        List<Lop> lopListDB = xepTKBDAO.getDanhSachLop();
        if (lopListDB != null) {
            allLopList.setAll(lopListDB);
        } else {
            allLopList.clear();
        }
    }

    private void loadAllGiaoVienList() {
        List<GiaoVien> gvList = thoiKhoaBieuDAO.getDanhSachGiaoVien();
        if (gvList != null) {
            allGiaoVienList.setAll(gvList);
        } else {
            allGiaoVienList.clear();
        }
    }

    private void setupTkbCoSoComboBox() {
        tkbCoSoComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(ThoiKhoaBieu tkb) { return tkb == null || tkb.getMaTKB() == null ? "Không chọn TKB cơ sở" : tkb.toString(); }
            @Override public ThoiKhoaBieu fromString(String string) {
                if ("Không chọn TKB cơ sở".equals(string)) return null;
                return tkbCoSoComboBox.getItems().stream().filter(tkb -> tkb != null && tkb.toString().equals(string)).findFirst().orElse(null);
            }
        });
    }

    private void setupGiaoVienCustomSettingsListView() {
        ObservableList<GiaoVien> displayableGiaoVienList = allGiaoVienList.stream()
                .filter(gv -> !"ADMIN".equalsIgnoreCase(gv.getMaGV()))
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);

        FilteredList<GiaoVien> filteredData = new FilteredList<>(displayableGiaoVienList, p -> true);
        giaoVienListView.setItems(filteredData);

        giaoVienListView.setCellFactory(lv -> new ListCell<>() {
            private final HBox hbox = new HBox(10);
            private final Label lblName = new Label();
            private final Button btnConfigure = new Button("Cài đặt GV");
            private final Region spacer = new Region();
            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.getChildren().addAll(lblName, spacer, btnConfigure);
                hbox.setAlignment(Pos.CENTER_LEFT);
                btnConfigure.setOnAction(event -> {
                    GiaoVien gv = getItem();
                    if (gv != null) openGiaoVienSettingsWindow(gv);
                });
            }
            @Override
            protected void updateItem(GiaoVien gv, boolean empty) {
                super.updateItem(gv, empty);
                if (empty || gv == null) {
                    setText(null); setGraphic(null);
                } else {
                    lblName.setText(gv.getHoGV() + " " + gv.getTenGV() + " (" + gv.getMaGV() + ")");
                    TeacherCustomSettings settings = teacherCustomSettingsMap.get(gv.getMaGV());
                    if (settings != null && !settings.isParticipateInScheduling()) {
                        lblName.setStyle("-fx-strikethrough: true; -fx-opacity: 0.7;");
                    } else {
                        lblName.setStyle("");
                    }
                    setGraphic(hbox);
                }
            }
        });
    }

    private void openGiaoVienSettingsWindow(GiaoVien gv) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        if (selectedHocKy == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn Học Kỳ trước khi cài đặt cho giáo viên.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/XepTKBTuDong/CaiDatGV.fxml"));
            Parent root = loader.load();
            CaiDatGVController controller = loader.getController();
            controller.initData(gv, selectedHocKy, teacherCustomSettingsMap, xepTKBDAO);
            Stage stage = new Stage();
            stage.setTitle("Cài đặt cho GV: " + gv.getHoGV() + " " + gv.getTenGV());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(giaoVienListView.getScene().getWindow());
            stage.showAndWait();
            giaoVienListView.refresh();
            lopListView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Mở Cửa Sổ", "Không thể mở cửa sổ cài đặt giáo viên.\n" + e.getMessage());
        }
    }

    private void setupSearchGiaoVienTextField() {
        searchGiaoVienTextField.textProperty().addListener((obs, oldV, newV) -> {
            if (giaoVienListView.getItems() instanceof FilteredList) {
                FilteredList<GiaoVien> filteredData = (FilteredList<GiaoVien>) giaoVienListView.getItems();
                String lowerCaseFilter = (newV == null) ? "" : newV.toLowerCase().trim();
                filteredData.setPredicate(gv -> lowerCaseFilter.isEmpty() ||
                        (gv.getTenGV() != null && gv.getTenGV().toLowerCase().contains(lowerCaseFilter)) ||
                        (gv.getHoGV() != null && gv.getHoGV().toLowerCase().contains(lowerCaseFilter)) ||
                        (gv.getMaGV() != null && gv.getMaGV().toLowerCase().contains(lowerCaseFilter))
                );
            }
        });
    }

    private void setupLopCustomSettingsListView() {
        FilteredList<Lop> filteredLopData = new FilteredList<>(allLopList, p -> true);
        lopListView.setItems(filteredLopData);
        lopListView.setCellFactory(lv -> new ListCell<Lop>() {
            private final HBox hbox = new HBox(10);
            private final Label lblLopName = new Label();
            private final Button btnConfigureLop = new Button("Cài đặt Lớp");
            private final Region spacer = new Region();
            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.getChildren().addAll(lblLopName, spacer, btnConfigureLop);
                hbox.setAlignment(Pos.CENTER_LEFT);
                btnConfigureLop.setOnAction(event -> {
                    Lop lop = getItem();
                    if (lop != null) openLopSettingsWindow(lop);
                });
            }
            @Override
            protected void updateItem(Lop lop, boolean empty) {
                super.updateItem(lop, empty);
                if (empty || lop == null) {
                    setText(null); setGraphic(null);
                } else {
                    lblLopName.setText(lop.getTenLop() + " (" + lop.getMaLop() + ")");
                    setGraphic(hbox);
                }
            }
        });
    }

    private void openLopSettingsWindow(Lop lop) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        if (selectedHocKy == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn Học Kỳ trước.");
            return;
        }
        if (phanCongMonHocChoTatCaLopTrongHK == null || phanCongMonHocChoTatCaLopTrongHK.isEmpty()) {
            phanCongMonHocChoTatCaLopTrongHK = xepTKBDAO.getPhanCongMonHocChoTatCaLop(selectedHocKy.getMaHK());
            if (phanCongMonHocChoTatCaLopTrongHK == null || phanCongMonHocChoTatCaLopTrongHK.isEmpty()){
                showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Chưa có dữ liệu phân công môn học cho các lớp trong học kỳ này.");
                return;
            }
        }

        List<MonHocHoc> subjectsOfThisClass = phanCongMonHocChoTatCaLopTrongHK.get(lop.getMaLop());
        if (subjectsOfThisClass == null || subjectsOfThisClass.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Lớp " + lop.getTenLop() + " không có môn học nào được phân công trong học kỳ này.");
            return;
        }

        List<GiaoVien> availableTeachersForScheduling = allGiaoVienList.stream()
                .filter(gv -> {
                    if ("ADMIN".equalsIgnoreCase(gv.getMaGV())) return false;
                    TeacherCustomSettings settings = teacherCustomSettingsMap.get(gv.getMaGV());
                    return (settings == null) || settings.isParticipateInScheduling();
                })
                .collect(Collectors.toList());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/XepTKBTuDong/CaiDatLop.fxml"));
            Parent root = loader.load();
            CaiDatLopController controller = loader.getController();
            controller.initData(lop, selectedHocKy, classTeacherAssignmentsMap, subjectsOfThisClass, availableTeachersForScheduling, teacherCustomSettingsMap, xepTKBDAO);
            Stage stage = new Stage();
            stage.setTitle("Cài đặt Phân Công GV cho Lớp: " + lop.getTenLop());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(lopListView.getScene().getWindow());
            stage.showAndWait();
            lopListView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Mở Cửa Sổ", "Không thể mở cửa sổ cài đặt lớp.\n" + e.getMessage());
        }
    }

    private void setupSearchLopTextField() {
        searchLopTextField.textProperty().addListener((obs, oldV, newV) -> {
            if (lopListView.getItems() instanceof FilteredList) {
                FilteredList<Lop> filteredData = (FilteredList<Lop>) lopListView.getItems();
                String lowerCaseFilter = (newV == null) ? "" : newV.toLowerCase().trim();
                filteredData.setPredicate(lop -> lowerCaseFilter.isEmpty() ||
                        (lop.getTenLop() != null && lop.getTenLop().toLowerCase().contains(lowerCaseFilter)) ||
                        (lop.getMaLop() != null && lop.getMaLop().toLowerCase().contains(lowerCaseFilter))
                );
            }
        });
    }

    private void setupKhoiComboBoxSoTiet() {
        khoiComboBoxSoTiet.setItems(FXCollections.observableArrayList(KHOI_LIST));
    }

    private void setupDayPeriodSpinnersForKhoi() {
        int minPeriods = 0; int maxPeriods = 7; // Cho phép spinner chọn tới 7, logic giới hạn 5 sẽ ở chỗ khác nếu cần
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
                    soTietMoiThuTheoKhoi.computeIfAbsent(selectedKhoi, k -> {
                        Map<Integer, Integer> newMap = new HashMap<>();
                        for(int day=2; day<=7; day++) newMap.put(day, 0);
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
        Map<Integer, Integer> settingsForKhoi = soTietMoiThuTheoKhoi.get(khoi);
        if (settingsForKhoi == null) { // Nếu chưa có trong map (ví dụ, sau khi load file mà khoi này mới)
            settingsForKhoi = new HashMap<>();
            int defaultPeriods = 5;
            for(int day=2; day<=7; day++) settingsForKhoi.put(day, defaultPeriods);
            soTietMoiThuTheoKhoi.put(khoi, settingsForKhoi);
        }

        thu2Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(2, 0));
        thu3Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(3, 0));
        thu4Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(4, 0));
        thu5Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(5, 0));
        thu6Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(6, 0));
        thu7Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(7, 0));

        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        if (selectedHocKy != null) {
            Task<Integer> loadPhanPhoiTask = new Task<>() {
                @Override protected Integer call() throws Exception {
                    return xepTKBDAO.getTongPhanPhoiTietTheoKhoi(khoi, selectedHocKy.getMaHK());
                }
                @Override protected void succeeded() {
                    Platform.runLater(() -> phanPhoiTietLabel.setText("Phân phối tiết (CSDL): " + getValue() + " tiết/lớp/tuần (TB)"));
                }
                @Override protected void failed() {
                    Platform.runLater(() -> phanPhoiTietLabel.setText("Phân phối tiết (CSDL): Lỗi tải"));
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
            tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: --"); return;
        }
        Map<Integer, Integer> settings = soTietMoiThuTheoKhoi.get(khoi);
        int tongTietCaiDat = settings.values().stream().mapToInt(Integer::intValue).sum();
        tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: " + tongTietCaiDat + " tiết/tuần");
    }

    @FXML
    void handleTkbCoSoSelection(ActionEvent event) { /* Hiện tại không cần làm gì thêm */ }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
