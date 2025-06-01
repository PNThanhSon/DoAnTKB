package controllers.XepTKBTuDong;

import dao.ThoiKhoaBieuDAO;
import dao.XepTKBDAO;
import entities.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import entities.TietHocData;

import java.util.*;
import java.util.stream.Collectors;

public class XepTKBTuDongController {

    @FXML private BorderPane mainBorderPane;
    @FXML private MenuBar menuBar;
    @FXML private Menu menuKhoi10;
    @FXML private Menu menuKhoi11;
    @FXML private Menu menuKhoi12;
    @FXML private Label infoLabel;
    @FXML private Label statusLabel;
    @FXML private Label lopTKBTitleLabel;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private TableView<TietHocData> tkbTableView;
    @FXML private TableColumn<TietHocData, String> tietColumn;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu2Column;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu3Column;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu4Column;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu5Column;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu6Column;
    @FXML private TableColumn<TietHocData, ChiTietTKB> thu7Column;

    private XepTKBDAO xepTKBDAO;
    private ThoiKhoaBieuDAO thoiKhoaBieuDAO;

    private HocKy selectedHocKy;
    private List<GiaoVien> excludedGiaoVienListInput; // Đổi tên để rõ ràng là input
    private Map<Integer, Integer> soTietMoiThu;
    private ThoiKhoaBieu tkbCoSo;
    private List<ChiTietTKB> chiTietTkbCoSo;

    private List<Lop> tatCaLop;
    private Map<String, List<MonHocHoc>> phanCongMonHocChoLop;
    private List<GiaoVien> tatCaGiaoVienDayDu; // Danh sách GV đầy đủ từ DAO
    private Map<String, MonHoc> danhMucMonHoc;

    // Input từ ChuanBiController: Giáo viên đã được phân công trước cho các môn linh hoạt
    private Map<String, Map<String, String>> preAssignedTeachersForFlexibleSubjectsInput;

    // Danh sách các MÃ MÔN HỌC (hoặc tiền tố) được coi là linh hoạt ở ChuanBiController
    // Cần có danh sách này ở đây để thuật toán biết môn nào là "linh hoạt" nếu không có lựa chọn trước
    private final List<String> MA_MON_HOC_PREFIX_LINH_HOAT = Arrays.asList("TIN", "GDTC");


    private Map<String, List<ChiTietTKB>> thoiKhoaBieuDaXep;
    private String maTKBMoiDuocTao;

    // Dùng để "ghim" giáo viên cho một môn của một lớp trong suốt quá trình xếp
    // Key: MaLop + "-" + MaMH, Value: MaGV đã được chọn để dạy môn đó cho lớp đó
    private Map<String, String> pinnedTeacherForSubjectClass;


    private static final int SO_TIET_HIEN_THI_MAC_DINH = 5;
    private static final double ROW_HEIGHT = 30.0;
    private static final double HEADER_HEIGHT = 30.0;
    private static final int MAX_CONSECUTIVE_PERIODS = 2;


    public XepTKBTuDongController() {
        xepTKBDAO = new XepTKBDAO();
        thoiKhoaBieuDAO = new ThoiKhoaBieuDAO();
        thoiKhoaBieuDaXep = new HashMap<>();
        danhMucMonHoc = new HashMap<>();
        pinnedTeacherForSubjectClass = new HashMap<>();
    }

    // Sửa initData để nhận thêm preAssignedTeachersForFlexibleSubjectsInput
    public void initData(HocKy selectedHocKy, ThoiKhoaBieu tkbCoSo,
                         List<GiaoVien> excludedGiaoVienList, Map<Integer, Integer> soTietMoiThu,
                         Map<String, Map<String, String>> preAssignedTeachers) {
        this.selectedHocKy = selectedHocKy;
        this.tkbCoSo = tkbCoSo;
        this.excludedGiaoVienListInput = excludedGiaoVienList;
        this.soTietMoiThu = soTietMoiThu;
        this.preAssignedTeachersForFlexibleSubjectsInput = preAssignedTeachers;

        infoLabel.setText("Đang chuẩn bị dữ liệu cho Học Kỳ: " + selectedHocKy.toString() + "...");
        runSchedulingTask();
    }

    private void runSchedulingTask() {
        Task<Void> schedulingTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(true);
                    statusLabel.setText("Đang tải dữ liệu nền...");
                });
                loadBaseData(); // Tải dữ liệu nền
                pinnedTeacherForSubjectClass.clear(); // Reset pinned teachers cho mỗi lần chạy

                if (tkbCoSo != null) {
                    chiTietTkbCoSo = xepTKBDAO.getChiTietTKBByMaTKB(tkbCoSo.getMaTKB());
                    Platform.runLater(() -> statusLabel.setText("Đã tải TKB cơ sở. Bắt đầu xếp TKB..."));
                } else {
                    Platform.runLater(() -> statusLabel.setText("Không có TKB cơ sở. Bắt đầu xếp TKB mới..."));
                }
                thoiKhoaBieuDaXep = scheduleTimetable(); // Chạy thuật toán
                Platform.runLater(() -> { // Cập nhật UI sau khi hoàn thành
                    statusLabel.setText("Đã xếp xong! Xây dựng Menu chọn lớp...");
                    setupLopMenu();
                    if (tatCaLop != null && !tatCaLop.isEmpty()) {
                        String maLopDauTien = tatCaLop.get(0).getMaLop();
                        displayTKBForLop(maLopDauTien);
                    } else {
                        infoLabel.setText("Không có lớp nào để hiển thị TKB.");
                        statusLabel.setText("Hoàn thành (không có lớp).");
                        if (lopTKBTitleLabel != null) lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP");
                        displayEmptyTKB();
                    }
                });
                return null;
            }
            // ... (succeeded, failed giữ nguyên) ...
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    if (thoiKhoaBieuDaXep != null && !thoiKhoaBieuDaXep.isEmpty() && tatCaLop != null && !tatCaLop.isEmpty()) {
                        statusLabel.setText("Hoàn thành! Chọn lớp từ menu để xem.");
                    } else if (tatCaLop == null || tatCaLop.isEmpty()){
                        statusLabel.setText("Hoàn thành (không có lớp).");
                    } else {
                        statusLabel.setText("Hoàn thành (không xếp được TKB hoặc không có dữ liệu).");
                    }
                });
            }
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    statusLabel.setText("Lỗi trong quá trình xếp TKB!");
                    Throwable ex = getException();
                    if (ex != null) {
                        ex.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Lỗi Xếp TKB", "Đã xảy ra lỗi: " + ex.getMessage());
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Lỗi Xếp TKB", "Đã xảy ra lỗi không xác định.");
                    }
                });
            }
        };
        new Thread(schedulingTask).start();
    }

    private void displayEmptyTKB() {
        // ... (giữ nguyên)
        ObservableList<TietHocData> tkbDataList = FXCollections.observableArrayList();
        for (int i = 1; i <= SO_TIET_HIEN_THI_MAC_DINH; i++) {
            tkbDataList.add(new TietHocData("Tiết " + i));
        }
        tkbTableView.setItems(tkbDataList);
        tkbTableView.refresh();
        if (lopTKBTitleLabel != null) {
            lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP");
        }
    }

    @FXML
    public void initialize() {
        // ... (giữ nguyên)
        setupTableColumns();
        tkbTableView.setFixedCellSize(ROW_HEIGHT);
        double calculatedTableHeight = HEADER_HEIGHT + (SO_TIET_HIEN_THI_MAC_DINH * ROW_HEIGHT) + 2.0;
        tkbTableView.setPrefHeight(calculatedTableHeight);
        tkbTableView.setMinHeight(calculatedTableHeight);
        tkbTableView.setMaxHeight(calculatedTableHeight);
        if (lopTKBTitleLabel != null) {
            lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP");
        }
    }

    private void loadBaseData() {
        // ... (giữ nguyên)
        tatCaLop = xepTKBDAO.getDanhSachLop();
        if (selectedHocKy != null) {
            phanCongMonHocChoLop = xepTKBDAO.getPhanCongMonHocChoTatCaLop(selectedHocKy.getMaHK());
        } else {
            phanCongMonHocChoLop = new HashMap<>();
            Platform.runLater(() -> showAlert(Alert.AlertType.WARNING, "Thiếu Học Kỳ", "Không thể tải phân công môn học."));
        }
        tatCaGiaoVienDayDu = xepTKBDAO.getDanhSachGiaoVienDayDu();
        List<MonHoc> allMonHoc = thoiKhoaBieuDAO.getDanhSachMonHocTheoTCM(null);
        if (allMonHoc != null) {
            allMonHoc.forEach(mh -> danhMucMonHoc.put(mh.getMaMH(), mh));
        }
    }

    private void setupTableColumns() {
        // ... (giữ nguyên như phiên bản trước đã sửa lỗi hiển thị tên GV và style cột Tiết)
        tietColumn.setCellValueFactory(new PropertyValueFactory<>("tiet"));
        tietColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;");
                }
            }
        });

        Callback<TableColumn<TietHocData, ChiTietTKB>, TableCell<TietHocData, ChiTietTKB>> cellFactoryMonHocGV =
                (TableColumn<TietHocData, ChiTietTKB> param) -> new TableCell<>() {
                    @Override
                    protected void updateItem(ChiTietTKB item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null); setTooltip(null); setStyle("");
                        } else {
                            String tenMHDisplay = item.getTenMonHoc();
                            String hoTenGVDisplay = item.getHoTenGV();

                            if ((tenMHDisplay == null || tenMHDisplay.trim().isEmpty()) && item.getMaMH() != null) {
                                MonHoc mh = danhMucMonHoc.get(item.getMaMH());
                                if (mh != null) tenMHDisplay = mh.getTenMH();
                            }
                            if (tenMHDisplay == null || tenMHDisplay.trim().isEmpty()) {
                                tenMHDisplay = (item.getMaMH() != null) ? "MH: " + item.getMaMH() : "(Môn?)";
                            }

                            if ((hoTenGVDisplay == null || hoTenGVDisplay.trim().isEmpty()) && item.getMaGV() != null) {
                                if (tatCaGiaoVienDayDu != null) {
                                    GiaoVien gv = tatCaGiaoVienDayDu.stream()
                                            .filter(g -> item.getMaGV().equals(g.getMaGV()))
                                            .findFirst().orElse(null);
                                    if (gv != null) {
                                        hoTenGVDisplay = (gv.getHoGV() != null ? gv.getHoGV().trim() : "") +
                                                (gv.getTenGV() != null ? " " + gv.getTenGV().trim() : "");
                                        hoTenGVDisplay = hoTenGVDisplay.trim();
                                    }
                                }
                            }
                            if (hoTenGVDisplay == null || hoTenGVDisplay.trim().isEmpty()) {
                                hoTenGVDisplay = (item.getMaGV() != null) ? "GV: " + item.getMaGV() : "";
                            }

                            String displayText = tenMHDisplay;
                            if (!hoTenGVDisplay.isEmpty() && !hoTenGVDisplay.startsWith("GV:")) {
                                displayText += " - " + hoTenGVDisplay;
                            } else if (!hoTenGVDisplay.isEmpty() && hoTenGVDisplay.startsWith("GV:")) {
                                displayText += " (" + hoTenGVDisplay + ")";
                            }

                            setText(displayText);
                            setTooltip(new Tooltip(displayText));
                            setStyle("-fx-alignment: CENTER-LEFT; -fx-text-alignment: LEFT; -fx-wrap-text: true; -fx-padding: 3 5 3 5;");
                        }
                    }
                };

        thu2Column.setCellValueFactory(new PropertyValueFactory<>("thu2"));
        thu2Column.setCellFactory(cellFactoryMonHocGV);
        thu3Column.setCellValueFactory(new PropertyValueFactory<>("thu3"));
        thu3Column.setCellFactory(cellFactoryMonHocGV);
        thu4Column.setCellValueFactory(new PropertyValueFactory<>("thu4"));
        thu4Column.setCellFactory(cellFactoryMonHocGV);
        thu5Column.setCellValueFactory(new PropertyValueFactory<>("thu5"));
        thu5Column.setCellFactory(cellFactoryMonHocGV);
        thu6Column.setCellValueFactory(new PropertyValueFactory<>("thu6"));
        thu6Column.setCellFactory(cellFactoryMonHocGV);
        thu7Column.setCellValueFactory(new PropertyValueFactory<>("thu7"));
        thu7Column.setCellFactory(cellFactoryMonHocGV);
    }

    private void setupLopMenu() {
        // ... (giữ nguyên như phiên bản trước đã sửa lỗi sắp xếp tự nhiên)
        menuKhoi10.getItems().clear();
        menuKhoi11.getItems().clear();
        menuKhoi12.getItems().clear();

        if (tatCaLop == null) return;

        Comparator<Lop> naturalSortLopComparator = (lop1, lop2) -> {
            String s1 = lop1.getMaLop();
            String s2 = lop2.getMaLop();
            String prefix1 = ""; String suffix1Str = ""; int i1 = s1.length() - 1;
            while (i1 >= 0 && Character.isDigit(s1.charAt(i1))) i1--;
            prefix1 = s1.substring(0, i1 + 1); suffix1Str = s1.substring(i1 + 1);
            String prefix2 = ""; String suffix2Str = ""; int i2 = s2.length() - 1;
            while (i2 >= 0 && Character.isDigit(s2.charAt(i2))) i2--;
            prefix2 = s2.substring(0, i2 + 1); suffix2Str = s2.substring(i2 + 1);
            int prefixCompare = prefix1.compareToIgnoreCase(prefix2);
            if (prefixCompare != 0) return prefixCompare;
            if (suffix1Str.isEmpty() && suffix2Str.isEmpty()) return 0;
            if (suffix1Str.isEmpty()) return -1;
            if (suffix2Str.isEmpty()) return 1;
            try {
                return Integer.compare(Integer.parseInt(suffix1Str), Integer.parseInt(suffix2Str));
            } catch (NumberFormatException e) {
                return suffix1Str.compareToIgnoreCase(suffix2Str);
            }
        };

        Map<String, List<Lop>> lopTheoKhoi = tatCaLop.stream()
                .sorted(naturalSortLopComparator) // Sắp xếp toàn bộ danh sách trước khi nhóm
                .collect(Collectors.groupingBy(Lop::getKhoi, TreeMap::new, Collectors.toList()));


        lopTheoKhoi.forEach((khoi, danhSachLopTrongKhoi) -> {
            Menu menuKhoi = null;
            if ("10".equals(khoi)) menuKhoi = menuKhoi10;
            else if ("11".equals(khoi)) menuKhoi = menuKhoi11;
            else if ("12".equals(khoi)) menuKhoi = menuKhoi12;

            if (menuKhoi != null) {
                // danhSachLopTrongKhoi.sort(Comparator.comparing(Lop::getMaLop)); // Không cần sort lại ở đây nếu đã sort trước khi group
                for (Lop lop : danhSachLopTrongKhoi) {
                    String menuItemText = lop.getMaLop();
                    MenuItem lopItem = new MenuItem(menuItemText);
                    lopItem.setOnAction(event -> {
                        displayTKBForLop(lop.getMaLop());
                    });
                    menuKhoi.getItems().add(lopItem);
                }
            }
        });
    }

    private void displayTKBForLop(String maLop) {
        // ... (giữ nguyên như phiên bản trước)
        ObservableList<TietHocData> tkbDataList = FXCollections.observableArrayList();
        for (int i = 1; i <= SO_TIET_HIEN_THI_MAC_DINH; i++) {
            tkbDataList.add(new TietHocData("Tiết " + i));
        }

        List<ChiTietTKB> chiTietCuaLop = thoiKhoaBieuDaXep.get(maLop);

        if (chiTietCuaLop != null) {
            for (ChiTietTKB ct : chiTietCuaLop) {
                if (ct.getTiet() > 0 && ct.getTiet() <= SO_TIET_HIEN_THI_MAC_DINH) {
                    TietHocData rowData = tkbDataList.get(ct.getTiet() - 1);
                    if (ct.getTiet() <= soTietMoiThu.getOrDefault(ct.getThu(), SO_TIET_HIEN_THI_MAC_DINH)) {
                        switch (ct.getThu()) {
                            case 2: rowData.setThu2(ct); break;
                            case 3: rowData.setThu3(ct); break;
                            case 4: rowData.setThu4(ct); break;
                            case 5: rowData.setThu5(ct); break;
                            case 6: rowData.setThu6(ct); break;
                            case 7: rowData.setThu7(ct); break;
                        }
                    }
                }
            }
        }
        tkbTableView.setItems(tkbDataList);
        tkbTableView.refresh();

        if (lopTKBTitleLabel != null) {
            String tenLopHienThi = maLop;
            if (tatCaLop != null) {
                Optional<Lop> lopOptional = tatCaLop.stream().filter(l -> l.getMaLop().equals(maLop)).findFirst();
                if (lopOptional.isPresent()) {
                    tenLopHienThi = lopOptional.get().getMaLop();
                }
            }
            lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP " + tenLopHienThi.toUpperCase());
        }
        infoLabel.setText("Đang hiển thị TKB lớp: " + maLop +
                (selectedHocKy != null ? " (Học kỳ: " + selectedHocKy.toString() + ")" : ""));
    }

    private Map<String, List<ChiTietTKB>> scheduleTimetable() {
        Map<String, List<ChiTietTKB>> generatedTimetable = new HashMap<>();
        Random random = new Random();

        if (tatCaLop == null || phanCongMonHocChoLop == null || tatCaGiaoVienDayDu == null || selectedHocKy == null) {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Thiếu dữ liệu", "Không đủ dữ liệu nền để xếp TKB."));
            return generatedTimetable;
        }

        List<GiaoVien> giaoVienHopLe = tatCaGiaoVienDayDu.stream()
                .filter(gv -> excludedGiaoVienListInput == null || excludedGiaoVienListInput.stream().noneMatch(ex -> ex.getMaGV().equals(gv.getMaGV())))
                .collect(Collectors.toList());

        Map<String, boolean[][]> lopBusySlots = new HashMap<>();
        Map<String, boolean[][]> teacherBusySlots = new HashMap<>();
        int maxPossiblePeriods = SO_TIET_HIEN_THI_MAC_DINH;

        for (Lop lop : tatCaLop) {
            lopBusySlots.put(lop.getMaLop(), new boolean[8][maxPossiblePeriods + 1]);
        }
        for (GiaoVien gv : giaoVienHopLe) {
            teacherBusySlots.put(gv.getMaGV(), new boolean[8][maxPossiblePeriods + 1]);
        }

        if (chiTietTkbCoSo != null) {
            for (ChiTietTKB ctCoSo : chiTietTkbCoSo) {
                if (ctCoSo.getThu() >=2 && ctCoSo.getThu() <=7 && ctCoSo.getTiet() >=1 && ctCoSo.getTiet() <= maxPossiblePeriods) {
                    if (lopBusySlots.containsKey(ctCoSo.getMaLop())) {
                        lopBusySlots.get(ctCoSo.getMaLop())[ctCoSo.getThu()][ctCoSo.getTiet()] = true;
                    }
                    if (teacherBusySlots.containsKey(ctCoSo.getMaGV())) {
                        teacherBusySlots.get(ctCoSo.getMaGV())[ctCoSo.getThu()][ctCoSo.getTiet()] = true;
                    }
                    generatedTimetable.computeIfAbsent(ctCoSo.getMaLop(), k -> new ArrayList<>()).add(ctCoSo);
                }
            }
        }

        for (Lop lop : tatCaLop) {
            List<ChiTietTKB> tkbCuaLop = generatedTimetable.computeIfAbsent(lop.getMaLop(), k -> new ArrayList<>());
            List<MonHocHoc> monHocCuaLop = phanCongMonHocChoLop.get(lop.getMaLop());

            if (monHocCuaLop == null) continue;

            for (MonHocHoc mhh : monHocCuaLop) {
                int soTietCanXep = mhh.getTongSoTiet();

                if (chiTietTkbCoSo != null) {
                    long soTietDaXepTuCoSo = tkbCuaLop.stream()
                            .filter(ct -> ct.getMaMH() != null && ct.getMaMH().equals(mhh.getMaMH()))
                            .count();
                    soTietCanXep -= (int) soTietDaXepTuCoSo;
                }

                List<GiaoVien> candidateTeachers;
                String pinnedTeacherKey = lop.getMaLop() + "-" + mhh.getMaMH();

                // Bước 1: Kiểm tra xem GV đã được chọn trước từ ChuanBiController chưa
                String preSelectedMaGV = null;
                if (preAssignedTeachersForFlexibleSubjectsInput != null &&
                        preAssignedTeachersForFlexibleSubjectsInput.containsKey(lop.getMaLop()) &&
                        preAssignedTeachersForFlexibleSubjectsInput.get(lop.getMaLop()).containsKey(mhh.getMaMH())) {
                    preSelectedMaGV = preAssignedTeachersForFlexibleSubjectsInput.get(lop.getMaLop()).get(mhh.getMaMH());
                }

                if (preSelectedMaGV != null) {
                    final String finalPreSelectedMaGV = preSelectedMaGV;
                    GiaoVien selectedTeacher = giaoVienHopLe.stream()
                            .filter(gv -> gv.getMaGV().equals(finalPreSelectedMaGV))
                            .findFirst().orElse(null);
                    if (selectedTeacher != null) {
                        candidateTeachers = Arrays.asList(selectedTeacher);
                        pinnedTeacherForSubjectClass.put(pinnedTeacherKey, selectedTeacher.getMaGV()); // Ghim GV này
                        // System.out.println("INFO: Lớp " + lop.getMaLop() + ", Môn " + mhh.getMaMH() + " sẽ do GV (chọn trước): " + selectedTeacher.getMaGV() + " dạy.");
                    } else {
                        // System.err.println("CẢNH BÁO: GV " + finalPreSelectedMaGV + " được chọn trước cho môn " + mhh.getMaMH() + " của lớp " + lop.getMaLop() + " không hợp lệ. Thuật toán sẽ tự chọn.");
                        candidateTeachers = giaoVienHopLe.stream()
                                .filter(gv -> gv.getMaTCM() != null && gv.getMaTCM().equals(mhh.getMaTCM()))
                                .collect(Collectors.toList());
                    }
                } else {
                    // Bước 2: Nếu không có lựa chọn trước, kiểm tra xem GV đã được "ghim" cho môn/lớp này chưa
                    if (pinnedTeacherForSubjectClass.containsKey(pinnedTeacherKey)) {
                        String pinnedMaGV = pinnedTeacherForSubjectClass.get(pinnedTeacherKey);
                        GiaoVien pinnedGV = giaoVienHopLe.stream().filter(gv -> gv.getMaGV().equals(pinnedMaGV)).findFirst().orElse(null);
                        if (pinnedGV != null) {
                            candidateTeachers = Arrays.asList(pinnedGV);
                        } else { // GV đã ghim không còn hợp lệ (ví dụ bị exclude), chọn lại từ TCM
                            candidateTeachers = giaoVienHopLe.stream()
                                    .filter(gv -> gv.getMaTCM() != null && gv.getMaTCM().equals(mhh.getMaTCM()))
                                    .collect(Collectors.toList());
                        }
                    } else {
                        // Bước 3: Nếu chưa ghim, lấy tất cả GV từ TCM
                        candidateTeachers = giaoVienHopLe.stream()
                                .filter(gv -> gv.getMaTCM() != null && gv.getMaTCM().equals(mhh.getMaTCM()))
                                .collect(Collectors.toList());

                        // Ưu tiên GVCN nếu có thể và danh sách ứng viên > 1
                        if (candidateTeachers.size() > 1 && lop.getGvcn() != null) {
                            GiaoVien gvcn = giaoVienHopLe.stream().filter(g -> g.getMaGV().equals(lop.getGvcn())).findFirst().orElse(null);
                            if (gvcn != null && candidateTeachers.contains(gvcn)) {
                                candidateTeachers.sort((g1, g2) -> g1.getMaGV().equals(gvcn.getMaGV()) ? -1 : (g2.getMaGV().equals(gvcn.getMaGV()) ? 1 : 0));
                            }
                        }
                    }
                }


                if (candidateTeachers.isEmpty()) {
                    // System.err.println("Không tìm thấy GV cho môn " + mhh.getTenMH() + " (" + mhh.getMaMH() + ") của lớp " + lop.getTenLop() + " (TCM: " + mhh.getMaTCM() + ")");
                    continue;
                }

                for (int i = 0; i < soTietCanXep; i++) {
                    boolean daXepTietNay = false;
                    // Không shuffle candidateTeachers nữa nếu đã ghim hoặc chọn trước
                    // Collections.shuffle(candidateTeachers);

                    for (GiaoVien gvChon : candidateTeachers) {
                        int soTietDaDayTrongTuanCuaGV = (int) generatedTimetable.values().stream().flatMap(List::stream)
                                .filter(ct -> ct.getMaGV() != null && ct.getMaGV().equals(gvChon.getMaGV())).count();
                        int soTietChucVu = xepTKBDAO.getSoTietChucVuDaSuDung(gvChon.getMaGV(), selectedHocKy.getMaHK());

                        boolean vuotSoTietQuyDinh = false;
                        if (gvChon.getSoTietQuyDinh() != null && gvChon.getSoTietQuyDinh() > 0) {
                            if ((soTietDaDayTrongTuanCuaGV + soTietChucVu) >= gvChon.getSoTietQuyDinh()) {
                                vuotSoTietQuyDinh = true;
                            }
                        }
                        if (vuotSoTietQuyDinh) continue;

                        List<Integer> thuList = Arrays.asList(2, 3, 4, 5, 6, 7);
                        Collections.shuffle(thuList);

                        for (int thu : thuList) {
                            int maxTietHomNay = soTietMoiThu.getOrDefault(thu, maxPossiblePeriods);
                            List<Integer> tietList = new ArrayList<>();
                            for(int t=1; t<=maxTietHomNay; t++) tietList.add(t);
                            Collections.shuffle(tietList);

                            for (int tiet : tietList) {
                                if (tiet > maxPossiblePeriods) continue;

                                if (!lopBusySlots.get(lop.getMaLop())[thu][tiet] &&
                                        !teacherBusySlots.get(gvChon.getMaGV())[thu][tiet]) {

                                    boolean consecutiveViolation = false;
                                    if (tiet > 1) {
                                        int consecutiveCount = 0;
                                        for (int k = 1; k < MAX_CONSECUTIVE_PERIODS; k++) {
                                            if (tiet - k >= 1) {
                                                ChiTietTKB tietTruoc = null;
                                                for(ChiTietTKB ct : tkbCuaLop){
                                                    if(ct.getThu() == thu && ct.getTiet() == (tiet-k)){
                                                        tietTruoc = ct;
                                                        break;
                                                    }
                                                }
                                                if (tietTruoc != null &&
                                                        tietTruoc.getMaMH() != null && tietTruoc.getMaMH().equals(mhh.getMaMH()) &&
                                                        tietTruoc.getMaGV() != null && tietTruoc.getMaGV().equals(gvChon.getMaGV())) {
                                                    consecutiveCount++;
                                                } else {
                                                    break;
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        if (consecutiveCount >= MAX_CONSECUTIVE_PERIODS -1) {
                                            consecutiveViolation = true;
                                        }
                                    }

                                    if (consecutiveViolation) {
                                        continue;
                                    }

                                    String hoTenGVChoTKB = (gvChon.getHoGV() != null ? gvChon.getHoGV().trim() : "") +
                                            (gvChon.getTenGV() != null ? " " + gvChon.getTenGV().trim() : "");
                                    hoTenGVChoTKB = hoTenGVChoTKB.trim();
                                    if (hoTenGVChoTKB.isEmpty() && gvChon.getMaGV() != null) {
                                        hoTenGVChoTKB = "GV:" + gvChon.getMaGV();
                                    }

                                    ChiTietTKB chiTietMoi = ChiTietTKB.taoChoXepLichTuDong(
                                            thu, tiet, mhh.getTenMH(), lop.getMaLop(),
                                            hoTenGVChoTKB,
                                            gvChon.getMaGV(), mhh.getMaMH(), 0);

                                    tkbCuaLop.add(chiTietMoi);
                                    lopBusySlots.get(lop.getMaLop())[thu][tiet] = true;
                                    teacherBusySlots.get(gvChon.getMaGV())[thu][tiet] = true;
                                    daXepTietNay = true;

                                    // Nếu chưa có GV nào được ghim cho môn/lớp này, và môn này không phải là môn linh hoạt đã được chọn trước
                                    // (hoặc là môn linh hoạt nhưng người dùng để thuật toán tự chọn)
                                    // thì ghim GV vừa chọn.
                                    boolean isFlexibleAndPreAssigned = preSelectedMaGV != null;
                                    boolean isTrulyFlexibleSubject = MA_MON_HOC_PREFIX_LINH_HOAT.stream().anyMatch(prefix -> mhh.getMaMH().startsWith(prefix));

                                    if (!pinnedTeacherForSubjectClass.containsKey(pinnedTeacherKey) && !isFlexibleAndPreAssigned) {
                                        // Chỉ ghim nếu đây là lần đầu xếp cho môn/lớp này HOẶC nếu là môn linh hoạt mà người dùng không chọn trước
                                        // (để đảm bảo môn không linh hoạt luôn có 1 GV, và môn linh hoạt nếu không chọn trước cũng có 1 GV)
                                        pinnedTeacherForSubjectClass.put(pinnedTeacherKey, gvChon.getMaGV());
                                        // System.out.println("PINNED: L" + lop.getMaLop() + "-M" + mhh.getMaMH() + " -> GV " + gvChon.getMaGV());
                                    }
                                    break;
                                }
                            }
                            if (daXepTietNay) break;
                        }
                        if (daXepTietNay) break;
                    }
                    if (!daXepTietNay) {
                        // System.err.println("Không thể xếp tiết thứ " + (i + 1) + " cho môn " + mhh.getTenMH() + " của lớp " + lop.getTenLop());
                    }
                }
            }
        }
        return generatedTimetable;
    }

    @FXML
    void handleXemTatCaLop(ActionEvent event) {
        // ... (giữ nguyên)
        infoLabel.setText("Chức năng xem tổng quan toàn trường chưa được triển khai.");
        displayEmptyTKB();
        if (lopTKBTitleLabel != null) {
            lopTKBTitleLabel.setText("THỜI KHÓA BIỂU TOÀN TRƯỜNG (TỔNG QUAN)");
        }
    }

    @FXML
    void handleLuuThoiKhoaBieu(ActionEvent event) {
        // ... (giữ nguyên)
        if (thoiKhoaBieuDaXep == null || thoiKhoaBieuDaXep.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Không có dữ liệu", "Chưa có thời khóa biểu nào được xếp để lưu.");
            return;
        }
        if (selectedHocKy == null) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Không xác định được học kỳ để lưu TKB.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc chắn muốn lưu thời khóa biểu này vào CSDL không?\n" +
                        "Một mã TKB mới sẽ được tạo.", ButtonType.YES, ButtonType.NO);
        confirmAlert.setTitle("Xác nhận lưu TKB");
        confirmAlert.setHeaderText(null);
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            progressIndicator.setVisible(true);
            statusLabel.setText("Đang lưu TKB...");
            String nguoiTaoMaGV = "ADMIN";
            String buoiHocDeNghi = "CHUNG";
            if (tkbCoSo != null && tkbCoSo.getBuoi() != null && !tkbCoSo.getBuoi().isBlank()) {
                buoiHocDeNghi = tkbCoSo.getBuoi();
            }

            String newMaTKB = xepTKBDAO.taoThoiKhoaBieuMoi(selectedHocKy.getMaHK(), buoiHocDeNghi, nguoiTaoMaGV);

            if (newMaTKB != null) {
                maTKBMoiDuocTao = newMaTKB;
                final int[] successCount = {0};
                final int[] failCount = {0};
                List<ChiTietTKB> allChiTietToSave = thoiKhoaBieuDaXep.values().stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                Task<Void> saveTask = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        for (ChiTietTKB ct : allChiTietToSave) {
                            if (ct.getMaMH() == null || ct.getMaMH().trim().isEmpty()) {
                                System.err.println("Lỗi: ChiTietTKB thiếu MaMH, không thể lưu: " + ct.getTenMonHoc() + " lớp " + ct.getMaLop() + " GV " + ct.getMaGV());
                                failCount[0]++;
                                continue;
                            }
                            if (xepTKBDAO.luuChiTietTKB(ct, newMaTKB)) {
                                successCount[0]++;
                            } else {
                                failCount[0]++;
                            }
                            updateMessage("Đã lưu " + successCount[0] + " chi tiết. Thất bại: " + failCount[0]);
                        }
                        return null;
                    }
                    @Override
                    protected void succeeded() {
                        Platform.runLater(()->{
                            progressIndicator.setVisible(false);
                            statusLabel.setText("Lưu hoàn tất. Thành công: " + successCount[0] + ", Thất bại: " + failCount[0]);
                            showAlert(Alert.AlertType.INFORMATION, "Lưu Thành Công",
                                    "Thời khóa biểu đã được lưu với Mã TKB: " + newMaTKB +
                                            "\nSố chi tiết lưu thành công: " + successCount[0] +
                                            "\nSố chi tiết lưu thất bại: " + failCount[0]);
                        });
                    }
                    @Override
                    protected void failed() {
                        Platform.runLater(()->{
                            progressIndicator.setVisible(false);
                            statusLabel.setText("Lỗi khi đang lưu TKB.");
                            Throwable ex = getException();
                            if (ex != null) ex.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Lỗi Lưu TKB", "Có lỗi xảy ra trong quá trình lưu chi tiết TKB." + (ex != null ? "\n" + ex.getMessage() : ""));
                        });
                    }
                };
                statusLabel.textProperty().bind(saveTask.messageProperty());
                new Thread(saveTask).start();

            } else {
                progressIndicator.setVisible(false);
                statusLabel.setText("Lỗi khi tạo record TKB mới.");
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo bản ghi Thời Khóa Biểu mới trong CSDL.");
            }
        }
    }

    @FXML
    void handleXepLaiTKB(ActionEvent event) {
        // ... (giữ nguyên)
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn chạy lại thuật toán xếp TKB không?\n" +
                        "Mọi thay đổi chưa lưu của TKB hiện tại (nếu có) sẽ bị mất.", ButtonType.YES, ButtonType.NO);
        confirmAlert.setTitle("Xác nhận Xếp Lại TKB");
        confirmAlert.setHeaderText(null);
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            infoLabel.setText("Đang chuẩn bị xếp lại TKB cho Học Kỳ: " + selectedHocKy.toString() + "...");
            tkbTableView.getItems().clear();
            thoiKhoaBieuDaXep.clear();
            maTKBMoiDuocTao = null;
            runSchedulingTask();
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
