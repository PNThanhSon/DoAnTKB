package controllers.XepTKBTuDong;

import controllers.XepTKBTuDong.helpers.ChuanBiSettings;
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

    private final String SETTINGS_DIR_PATH = System.getProperty("user.home") + File.separator + ".tkb_scheduler_settings";
    private final String SETTINGS_FILE_NAME = "chuan_bi_settings.txt";
    private final String SETTINGS_FILE_PATH = SETTINGS_DIR_PATH + File.separator + SETTINGS_FILE_NAME;

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

        initializeDefaultSettings();
    }

    private void initializeDefaultSettings() {
        // Khởi tạo giá trị mặc định ban đầu cho soTietMoiThuTheoKhoi
        int defaultPeriods = 5;
        for (String khoi : KHOI_LIST) {
            Map<Integer, Integer> defaultSettingsForKhoi = new HashMap<>();
            for (int i = 2; i <= 7; i++) { // Thứ 2 đến Thứ 7
                defaultSettingsForKhoi.put(i, defaultPeriods);
            }
            soTietMoiThuTheoKhoi.put(khoi, defaultSettingsForKhoi);
        }
        System.out.println("[ChuanBiController.constructor] Initialized default soTietMoiThuTheoKhoi: " + soTietMoiThuTheoKhoi);
    }


    @FXML
    public void initialize() {
        loadAllGiaoVienList(); // Phải load GV trước để đảm bảo có danh sách GV cho việc tạo settings mặc định
        loadAllLopList();

        Optional<ChuanBiSettings> loadedSettingsOpt = loadSettingsFromFile();
        String savedMaHK = null;
        String savedMaTkbCoSo = null;

        if (loadedSettingsOpt.isPresent()) {
            ChuanBiSettings settings = loadedSettingsOpt.get();
            savedMaHK = settings.getSelectedMaHK();
            savedMaTkbCoSo = settings.getSelectedMaTkbCoSo();

            // Nạp soTietMoiThuTheoKhoi: Clear map hiện tại và nạp từ file nếu có
            if (settings.getSoTietMoiThuTheoKhoi() != null && !settings.getSoTietMoiThuTheoKhoi().isEmpty()) {
                this.soTietMoiThuTheoKhoi.clear();
                this.soTietMoiThuTheoKhoi.putAll(settings.getSoTietMoiThuTheoKhoi());
                System.out.println("[ChuanBiController.initialize] Loaded soTietMoiThuTheoKhoi from file: " + this.soTietMoiThuTheoKhoi);
            } else {
                System.out.println("[ChuanBiController.initialize] No soTietMoiThuTheoKhoi found in file. Retaining defaults or current state.");
                // Nếu file không có, map soTietMoiThuTheoKhoi vẫn giữ giá trị mặc định từ constructor
            }

            // Nạp teacherCustomSettingsMap
            if (settings.getTeacherCustomSettingsMap() != null) {
                this.teacherCustomSettingsMap.clear();
                this.teacherCustomSettingsMap.putAll(settings.getTeacherCustomSettingsMap());
                System.out.println("[ChuanBiController.initialize] Loaded teacherCustomSettingsMap from file. Size: " + this.teacherCustomSettingsMap.size());
            }

            // Nạp classTeacherAssignmentsMap
            if (settings.getClassTeacherAssignmentsMap() != null) {
                this.classTeacherAssignmentsMap.clear();
                this.classTeacherAssignmentsMap.putAll(settings.getClassTeacherAssignmentsMap());
                System.out.println("[ChuanBiController.initialize] Loaded classTeacherAssignmentsMap from file: " + this.classTeacherAssignmentsMap);
            } else {
                System.out.println("[ChuanBiController.initialize] No classTeacherAssignmentsMap found in file.");
            }
            System.out.println("Đã tải cài đặt từ file: " + SETTINGS_FILE_PATH);
        } else {
            System.out.println("Không tìm thấy file cài đặt hoặc có lỗi khi tải. Sử dụng cài đặt mặc định đã khởi tạo.");
        }

        // Đảm bảo tất cả GV (trừ ADMIN) đều có một đối tượng TeacherCustomSettings
        if (allGiaoVienList != null) {
            for (GiaoVien gv : allGiaoVienList) {
                if (!"ADMIN".equalsIgnoreCase(gv.getMaGV())) {
                    teacherCustomSettingsMap.computeIfAbsent(gv.getMaGV(), k -> {
                        System.out.println("[ChuanBiController.initialize] Creating default TeacherCustomSettings for GV: " + k + " (not found in file/map)");
                        return new TeacherCustomSettings(k); // Mặc định isParticipateInScheduling là true
                    });
                }
            }
        }
        System.out.println("[ChuanBiController.initialize] Final teacherCustomSettingsMap size after ensuring all GVs: " + teacherCustomSettingsMap.size());


        setupHocKyComboBox(savedMaHK);
        setupTkbCoSoComboBox(savedMaTkbCoSo);

        setupGiaoVienCustomSettingsListView();
        setupSearchGiaoVienTextField();
        setupLopCustomSettingsListView();
        setupSearchLopTextField();
        setupKhoiComboBoxSoTiet();
        setupDayPeriodSpinnersForKhoi();

        String initialKhoiForSpinners = khoiComboBoxSoTiet.getSelectionModel().getSelectedItem();
        if (initialKhoiForSpinners == null && !KHOI_LIST.isEmpty()) {
            khoiComboBoxSoTiet.getSelectionModel().selectFirst();
            initialKhoiForSpinners = khoiComboBoxSoTiet.getSelectionModel().getSelectedItem();
        }
        if (initialKhoiForSpinners != null) {
            updateSpinnersAndLabelsForSelectedKhoi(initialKhoiForSpinners);
        } else {
            System.out.println("[ChuanBiController.initialize] KHOI_LIST is empty or no khoi selected for spinners.");
        }

        if (hocKyComboBox.getValue() != null) {
            handleHocKySelection(null);
        } else {
            tkbCoSoComboBox.setDisable(true);
            tkbCoSoComboBox.setPromptText("Chọn học kỳ trước");
            phanPhoiTietLabel.setText("Phân phối tiết (CSDL): Chọn học kỳ");
            tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: Chọn học kỳ");
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
            if (hocKyComboBox.getValue() == null && !hocKyList.isEmpty()) {
                hocKyComboBox.setPromptText("Chọn học kỳ");
            } else if (hocKyComboBox.getValue() == null) {
                hocKyComboBox.setPromptText("Không có học kỳ nào");
            }
        } else {
            hocKyComboBox.setPromptText("Không có học kỳ");
            hocKyComboBox.setDisable(true);
        }
    }

    private void setupTkbCoSoComboBox(String savedMaTkbCoSo) {
        tkbCoSoComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(ThoiKhoaBieu tkb) { return tkb == null || tkb.getMaTKB() == null ? "Không chọn TKB cơ sở" : tkb.toString(); }
            @Override public ThoiKhoaBieu fromString(String string) {
                if ("Không chọn TKB cơ sở".equals(string)) return null;
                return tkbCoSoComboBox.getItems().stream().filter(tkb -> tkb != null && tkb.toString().equals(string)).findFirst().orElse(null);
            }
        });
        tkbCoSoComboBox.getItems().clear(); // Xóa item cũ trước khi thêm
        tkbCoSoComboBox.getItems().add(null); // Luôn có lựa chọn "Không chọn"

        HocKy selectedHocKy = hocKyComboBox.getValue();
        if (selectedHocKy != null) {
            List<ThoiKhoaBieu> tkbList = thoiKhoaBieuDAO.getDanhSachThoiKhoaBieuTheoHocKy(selectedHocKy.getMaHK());
            if (tkbList != null && !tkbList.isEmpty()) {
                tkbCoSoComboBox.getItems().addAll(tkbList);
                if (savedMaTkbCoSo != null) {
                    tkbList.stream()
                            .filter(tkb -> tkb.getMaTKB().equals(savedMaTkbCoSo))
                            .findFirst()
                            .ifPresentOrElse(
                                    tkbCoSoComboBox::setValue,
                                    () -> tkbCoSoComboBox.getSelectionModel().selectFirst()
                            );
                } else {
                    tkbCoSoComboBox.getSelectionModel().selectFirst();
                }
            } else {
                tkbCoSoComboBox.getSelectionModel().selectFirst();
            }
            tkbCoSoComboBox.setDisable(false);
        } else {
            tkbCoSoComboBox.setDisable(true);
            tkbCoSoComboBox.setPromptText("Chọn học kỳ trước");
            tkbCoSoComboBox.getSelectionModel().selectFirst();
        }
    }


    @FXML
    void handleTiepTuc(ActionEvent event) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        if (selectedHocKy == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn Học Kỳ.");
            return;
        }
        saveSettingsToFile();

        ThoiKhoaBieu selectedTkbCoSo = tkbCoSoComboBox.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/XepTKBTuDong/XepTKBTuDong.fxml"));
            Parent root = loader.load();
            XepTKBTuDongController controller = loader.getController();

            System.out.println("[ChuanBiController.handleTiepTuc] Passing teacherCustomSettingsMap to XepTKB: " + teacherCustomSettingsMap);
            System.out.println("[ChuanBiController.handleTiepTuc] Passing soTietMoiThuTheoKhoi to XepTKB: " + soTietMoiThuTheoKhoi);
            System.out.println("[ChuanBiController.handleTiepTuc] Passing classTeacherAssignmentsMap to XepTKB: " + classTeacherAssignmentsMap);

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
            updateSettingsFromSpinners(currentSelectedKhoiInSpinner); // Đảm bảo giá trị spinner hiện tại được cập nhật
        }

        File settingsDir = new File(SETTINGS_DIR_PATH);
        if (!settingsDir.exists()) {
            if (!settingsDir.mkdirs()) {
                System.err.println("Không thể tạo thư mục cài đặt: " + SETTINGS_DIR_PATH);
                showAlert(Alert.AlertType.ERROR, "Lỗi Lưu Cài Đặt", "Không thể tạo thư mục lưu cài đặt.");
                return;
            }
        }

        System.out.println("[ChuanBiController.saveSettingsToFile] Saving soTietMoiThuTheoKhoi: " + this.soTietMoiThuTheoKhoi);
        System.out.println("[ChuanBiController.saveSettingsToFile] Saving classTeacherAssignmentsMap: " + this.classTeacherAssignmentsMap);
        System.out.println("[ChuanBiController.saveSettingsToFile] Saving teacherCustomSettingsMap (size: " + this.teacherCustomSettingsMap.size() + "): " + this.teacherCustomSettingsMap);


        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SETTINGS_FILE_PATH), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write("# ChuanBiController Settings File");
            writer.newLine();

            HocKy selectedHocKy = hocKyComboBox.getValue();
            if (selectedHocKy != null) {
                writer.write(PREFIX_HOC_KY + selectedHocKy.getMaHK());
                writer.newLine();
            }

            ThoiKhoaBieu selectedTkbCoSo = tkbCoSoComboBox.getValue();
            if (selectedTkbCoSo != null && selectedTkbCoSo.getMaTKB() != null) {
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
                        .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                        .map(e -> e.getKey() + "=" + String.join("~", e.getValue()))
                        .collect(Collectors.joining("|"));
                if (!assignmentsStr.isEmpty()) {
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
            System.out.println("[ChuanBiController.loadSettingsFromFile] File cài đặt không tồn tại: " + SETTINGS_FILE_PATH);
            return Optional.empty();
        }
        System.out.println("[ChuanBiController.loadSettingsFromFile] Loading settings from: " + SETTINGS_FILE_PATH);
        ChuanBiSettings settings = new ChuanBiSettings(); // Tạo đối tượng settings mới để nạp
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
                        Map<Integer, Integer> tietSettings = new HashMap<>();
                        if (!parts[1].isEmpty()) {
                            for (String partTiet : parts[1].split(",")) {
                                String[] kv = partTiet.split("=");
                                if (kv.length == 2) {
                                    try {
                                        tietSettings.put(Integer.parseInt(kv[0]), Integer.parseInt(kv[1]));
                                    } catch (NumberFormatException e) {
                                        System.err.println("Lỗi parse số khi tải cài đặt tiết cho khối: " + khoi + " - " + partTiet + ". Lỗi: " + e.getMessage());
                                    }
                                }
                            }
                        }
                        settings.getSoTietMoiThuTheoKhoi().put(khoi, tietSettings);
                        System.out.println("[ChuanBiController.loadSettingsFromFile] Parsed KHOI_TIET for " + khoi + ": " + tietSettings);
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
                                if (kv.length == 2) {
                                    String maMH = kv[0];
                                    List<String> gvList = new ArrayList<>(Arrays.asList(kv[1].split("~")));
                                    assignments.put(maMH, gvList);
                                }
                            }
                        }
                        settings.getClassTeacherAssignmentsMap().put(maLop, assignments);
                        System.out.println("[ChuanBiController.loadSettingsFromFile] Parsed LOP_ASSIGN for " + maLop + ": " + assignments);
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
        Map<Integer, Integer> settingsForKhoi = soTietMoiThuTheoKhoi.computeIfAbsent(khoi, k -> {
            System.out.println("[ChuanBiController.updateSettingsFromSpinners] Khoi " + k + " not found in map, creating new default entry.");
            Map<Integer, Integer> newMap = new HashMap<>();
            for(int day=2; day<=7; day++) newMap.put(day, 5); // Default to 5 if not found
            return newMap;
        });
        settingsForKhoi.put(2, thu2Spinner.getValue());
        settingsForKhoi.put(3, thu3Spinner.getValue());
        settingsForKhoi.put(4, thu4Spinner.getValue());
        settingsForKhoi.put(5, thu5Spinner.getValue());
        settingsForKhoi.put(6, thu6Spinner.getValue());
        settingsForKhoi.put(7, thu7Spinner.getValue());
        System.out.println("[ChuanBiController.updateSettingsFromSpinners] Updated soTietMoiThuTheoKhoi for " + khoi + ": " + settingsForKhoi);
    }

    @FXML
    void handleHocKySelection(ActionEvent event) {
        HocKy selectedHocKy = hocKyComboBox.getSelectionModel().getSelectedItem();
        // Không cần load lại file settings ở đây nữa.
        // Chỉ cần cập nhật TKB cơ sở và các label liên quan đến học kỳ.
        setupTkbCoSoComboBox(null); // Truyền null để nó không cố gắng chọn TKB đã lưu từ file (vì có thể đã thay đổi)

        if (selectedHocKy != null) {
            phanCongMonHocChoTatCaLopTrongHK = xepTKBDAO.getPhanCongMonHocChoTatCaLop(selectedHocKy.getMaHK());
            String currentSelectedKhoi = khoiComboBoxSoTiet.getSelectionModel().getSelectedItem();
            if (currentSelectedKhoi != null) {
                updateSpinnersAndLabelsForSelectedKhoi(currentSelectedKhoi);
            } else if (!KHOI_LIST.isEmpty()) {
                khoiComboBoxSoTiet.getSelectionModel().selectFirst();
                updateSpinnersAndLabelsForSelectedKhoi(KHOI_LIST.getFirst());
            }
        } else {
            phanCongMonHocChoTatCaLopTrongHK.clear();
            tkbCoSoComboBox.setDisable(true);
            tkbCoSoComboBox.setPromptText("Chọn TKB cơ sở (nếu có)");
            if (!tkbCoSoComboBox.getItems().isEmpty()) tkbCoSoComboBox.getSelectionModel().selectFirst();
            phanPhoiTietLabel.setText("Phân phối tiết (CSDL): Chọn học kỳ");
            tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: Chọn học kỳ");
        }
    }


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

            // Lưu toàn bộ settings ra file sau khi cửa sổ CaiDatGV đóng
            saveSettingsToFile();
            System.out.println("[ChuanBiController.openGiaoVienSettingsWindow] Settings saved to file after closing CaiDatGV window for GV: " + gv.getMaGV());

            giaoVienListView.refresh();
            lopListView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Mở Cửa Sổ", "Không thể mở cửa sổ cài đặt giáo viên.\n" + e.getMessage());
        }
    }

    private void setupSearchGiaoVienTextField() {
        searchGiaoVienTextField.textProperty().addListener((obs, oldV, newV) -> {
            if (giaoVienListView.getItems() instanceof FilteredList<GiaoVien> filteredData) {
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
        lopListView.setCellFactory(lv -> new ListCell<>() {
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
                    setText(null);
                    setGraphic(null);
                } else {
                    lblLopName.setText(lop.getMaLop());
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
            showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Lớp " + lop.getMaLop() + " không có môn học nào được phân công trong học kỳ này.");
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
            stage.setTitle("Cài đặt Phân Công GV cho Lớp: " + lop.getMaLop());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(lopListView.getScene().getWindow());
            stage.showAndWait();

            // Lưu toàn bộ settings ra file sau khi cửa sổ CaiDatLop đóng
            saveSettingsToFile();
            System.out.println("[ChuanBiController.openLopSettingsWindow] Settings saved to file after closing CaiDatLop window for Lop: " + lop.getMaLop());

            lopListView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Mở Cửa Sổ", "Không thể mở cửa sổ cài đặt lớp.\n" + e.getMessage());
        }
    }

    private void setupSearchLopTextField() {
        searchLopTextField.textProperty().addListener((obs, oldV, newV) -> {
            if (lopListView.getItems() instanceof FilteredList<Lop> filteredData) {
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
        int minPeriods = 0; int maxPeriods = 7;
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
                    Map<Integer, Integer> settingsForKhoi = soTietMoiThuTheoKhoi.computeIfAbsent(selectedKhoi, k -> {
                        Map<Integer, Integer> newMap = new HashMap<>();
                        for(int day=2; day<=7; day++) newMap.put(day, 5); // Mặc định là 5 nếu chưa có
                        return newMap;
                    });
                    settingsForKhoi.put(dayOfWeek, (Integer) newValue);
                    System.out.println("[SpinnerListener] Updated soTietMoiThuTheoKhoi for " + selectedKhoi + ", Day " + dayOfWeek + ": " + newValue);
                    updateTongSoTietTuanLabel(selectedKhoi);
                    // Không tự động lưu file ở đây để tránh ghi file quá thường xuyên
                    // Việc lưu sẽ diễn ra khi đổi Khối hoặc nhấn Tiếp Tục
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
            // Trước khi cập nhật spinner cho khối mới, lưu lại giá trị spinner hiện tại cho khối cũ (nếu có)
            // Việc này đã được spinner listener thực hiện (cập nhật vào map)
            updateSpinnersAndLabelsForSelectedKhoi(selectedKhoi);
            saveSettingsToFile(); // Lưu file khi đổi khối
            System.out.println("[ChuanBiController.handleKhoiSoTietSelection] Settings saved to file after changing Khoi to: " + selectedKhoi);
        }
    }

    private void updateSpinnersAndLabelsForSelectedKhoi(String khoi) {
        if (khoi == null) {
            phanPhoiTietLabel.setText("Phân phối tiết (CSDL): Chọn khối");
            tongSoTietTuanLabel.setText("Tổng số tiết theo cài đặt: Chọn khối");
            int defaultSpinnerVal = 5; // Hoặc giá trị mặc định bạn muốn
            thu2Spinner.getValueFactory().setValue(defaultSpinnerVal);
            thu3Spinner.getValueFactory().setValue(defaultSpinnerVal);
            thu4Spinner.getValueFactory().setValue(defaultSpinnerVal);
            thu5Spinner.getValueFactory().setValue(defaultSpinnerVal);
            thu6Spinner.getValueFactory().setValue(defaultSpinnerVal);
            thu7Spinner.getValueFactory().setValue(defaultSpinnerVal);
            return;
        }
        System.out.println("[ChuanBiController.updateSpinnersAndLabels] Updating for Khoi: " + khoi);
        Map<Integer, Integer> settingsForKhoi = soTietMoiThuTheoKhoi.get(khoi);
        if (settingsForKhoi == null || settingsForKhoi.isEmpty()) { // Thêm kiểm tra isEmpty
            System.out.println("[ChuanBiController.updateSpinnersAndLabels] No settings found for Khoi " + khoi + " in map, or map is empty. Using/Creating default.");
            settingsForKhoi = new HashMap<>();
            int defaultPeriods = 5;
            for(int day=2; day<=7; day++) settingsForKhoi.put(day, defaultPeriods);
            soTietMoiThuTheoKhoi.put(khoi, settingsForKhoi); // Đảm bảo map có entry cho khối này
        }
        System.out.println("[ChuanBiController.updateSpinnersAndLabels] Settings for Khoi " + khoi + ": " + settingsForKhoi);


        thu2Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(2, 5)); // Mặc định 5 nếu không có
        thu3Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(3, 5));
        thu4Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(4, 5));
        thu5Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(5, 5));
        thu6Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(6, 5));
        thu7Spinner.getValueFactory().setValue(settingsForKhoi.getOrDefault(7, 5));

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
