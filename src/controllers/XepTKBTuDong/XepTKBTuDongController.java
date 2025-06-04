package controllers.XepTKBTuDong;

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
import javafx.scene.layout.Region;
import javafx.util.Callback;

import java.util.*;
import java.util.stream.Collectors;

public class XepTKBTuDongController {

    // ... (Các @FXML và khai báo biến thành viên giữ nguyên như phiên bản trước) ...
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

    private HocKy selectedHocKy;
    private Map<String, TeacherCustomSettings> teacherCustomSettingsInput;
    private Map<String, Map<Integer, Integer>> soTietMoiThuTheoKhoiInput;
    private ThoiKhoaBieu tkbCoSo;
    private List<ChiTietTKB> chiTietTkbCoSo;

    private List<Lop> tatCaLop;
    private Map<String, List<MonHocHoc>> phanCongMonHocChoLop;
    private List<GiaoVien> tatCaGiaoVienDayDu;
    private Map<String, MonHoc> danhMucMonHoc;
    private Map<String, Map<String, List<String>>> classTeacherAssignmentsInput;

    private Map<String, List<ChiTietTKB>> generatedTimetable;
    private Map<String, boolean[][]> lopBusySlots;
    private Map<String, boolean[][]> teacherBusySlots;
    private List<GiaoVien> giaoVienHopLe; // Danh sách GV tham gia xếp TKB và hợp lệ

    private Map<String, List<GiaoVien>> currentLockedTeachersForClassSubject;
    private Map<String, String> pinnedTeacherForSubjectClassFromBaseTKB;

    private String maTKBMoiDuocTao;
    private static final int MAX_PERIODS_PER_DAY_SETTING = 5;
    private static final double ROW_HEIGHT = 30.0; // Nên để TableView tự tính toán hoặc làm nó lớn hơn nếu nội dung dài
    private static final double HEADER_HEIGHT = 30.0;
    private static final int MAX_CONSECUTIVE_PERIODS = 2;
    private static final String PREFIX_GDQPAN = "GDQP";
    private static final String PREFIX_GDTC = "GDTC";
    private int soTietDaXepTuCoSoGlobal = 0;


    public XepTKBTuDongController() {
        xepTKBDAO = new XepTKBDAO();
        danhMucMonHoc = new HashMap<>();
        currentLockedTeachersForClassSubject = new HashMap<>();
        pinnedTeacherForSubjectClassFromBaseTKB = new HashMap<>();
    }

    public void initData(HocKy selectedHocKy, ThoiKhoaBieu tkbCoSo,
                         Map<String, TeacherCustomSettings> teacherSettings,
                         Map<String, Map<Integer, Integer>> soTietMoiThuTheoKhoi,
                         Map<String, Map<String, List<String>>> classAssignments) {
        this.selectedHocKy = selectedHocKy;
        this.tkbCoSo = tkbCoSo;
        this.teacherCustomSettingsInput = teacherSettings;
        this.soTietMoiThuTheoKhoiInput = soTietMoiThuTheoKhoi;
        this.classTeacherAssignmentsInput = classAssignments;

        System.out.println("--- DỮ LIỆU ĐẦU VÀO CHO XếpTKBTuDongController ---");
        System.out.println("Học kỳ: " + (this.selectedHocKy != null ? this.selectedHocKy.toString() : "null")); // Sử dụng toString()
        System.out.println("TKB Cơ sở: " + (this.tkbCoSo != null ? this.tkbCoSo.getMaTKB() : "null"));
        System.out.println("Cài đặt GV (teacherCustomSettingsInput): " + (this.teacherCustomSettingsInput != null ? this.teacherCustomSettingsInput.size() + " mục" : "null"));
        System.out.println("Số tiết mỗi thứ (soTietMoiThuTheoKhoiInput): " + (this.soTietMoiThuTheoKhoiInput != null ? this.soTietMoiThuTheoKhoiInput.size() + " khối" : "null"));
        System.out.println("Phân công GV cho lớp (classTeacherAssignmentsInput): " + (this.classTeacherAssignmentsInput != null ? this.classTeacherAssignmentsInput.size() + " lớp có cài đặt" : "null"));
        System.out.println("--------------------------------------------------");

        if (this.selectedHocKy != null) {
            infoLabel.setText("Đang chuẩn bị dữ liệu cho Học Kỳ: " + this.selectedHocKy.toString() + "..."); // Sử dụng toString()
            runSchedulingTask();
        } else {
            infoLabel.setText("Lỗi: Học kỳ không được chọn.");
            showAlert(Alert.AlertType.ERROR, "Lỗi Khởi Tạo", "Học kỳ không hợp lệ.");
            Platform.runLater(this::displayEmptyTKB);
        }
    }

    private void runSchedulingTask() {
        Task<Void> schedulingTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> { progressIndicator.setVisible(true); statusLabel.setText("Đang tải dữ liệu nền..."); });
                loadBaseData();

                Platform.runLater(() -> statusLabel.setText("Đang khởi tạo trạng thái xếp lịch..."));
                initializeSchedulingState();

                Platform.runLater(() -> statusLabel.setText("Bắt đầu xếp TKB..."));
                generatedTimetable = scheduleTimetable();

                Platform.runLater(() -> {
                    statusLabel.setText("Đã xếp xong! Xây dựng Menu chọn lớp...");
                    setupLopMenu();
                    if (tatCaLop != null && !tatCaLop.isEmpty()) {
                        displayTKBForLop(tatCaLop.getFirst().getMaLop());
                    } else {
                        infoLabel.setText("Không có lớp nào để hiển thị TKB.");
                        statusLabel.setText("Hoàn thành (không có lớp).");
                        if (lopTKBTitleLabel != null) lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP");
                        displayEmptyTKB();
                    }
                });
                return null;
            }
            @Override protected void succeeded() {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    if (generatedTimetable != null && !generatedTimetable.isEmpty() && tatCaLop != null && !tatCaLop.isEmpty()) {
                        statusLabel.setText("Hoàn thành! Chọn lớp từ menu để xem.");
                    } else if (tatCaLop == null || tatCaLop.isEmpty()){
                        statusLabel.setText("Hoàn thành (không có lớp).");
                    } else {
                        statusLabel.setText("Hoàn thành (có thể không xếp được đủ tiết hoặc không có dữ liệu).");
                    }
                    printScheduleStatistics();
                });
            }
            @Override protected void failed() {
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
                    displayEmptyTKB();
                });
            }
        };
        new Thread(schedulingTask).start();
    }

    private Map<String, List<ChiTietTKB>> scheduleTimetable() {
        if (!isBaseDataSufficient()) {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Thiếu dữ liệu", "Không đủ dữ liệu nền để xếp TKB. Vui lòng kiểm tra lại cài đặt Học Kỳ và các dữ liệu liên quan (Phân công giảng dạy, danh sách lớp, GV...)."));
            return this.generatedTimetable;
        }

        List<ClassSubjectPair> priorityQueue = new ArrayList<>();
        List<ClassSubjectPair> flexibleQueue = new ArrayList<>();

        if (tatCaLop != null) {
            tatCaLop.sort(Comparator.comparing(Lop::getKhoi, Comparator.nullsLast(String::compareTo))
                    .thenComparing(Lop::getMaLop, Comparator.nullsLast(String::compareTo)));
        }

        if (tatCaLop != null) {
            for (Lop lop : tatCaLop) {
                List<MonHocHoc> monHocCuaLop = phanCongMonHocChoLop.get(lop.getMaLop());
                if (monHocCuaLop != null) {
                    for (MonHocHoc mhh : monHocCuaLop) {
                        if (isPriorityAssignmentForClassSubject(lop, mhh)) {
                            priorityQueue.add(new ClassSubjectPair(lop, mhh));
                        } else {
                            flexibleQueue.add(new ClassSubjectPair(lop, mhh));
                        }
                    }
                } else {
                    System.out.println("Cảnh báo: Lớp " + lop.getMaLop() + " không có phân công môn học nào cho HK: " + (selectedHocKy != null ? selectedHocKy.toString() : "N/A"));
                }
            }
        }

        System.out.println("--- BẮT ĐẦU XẾP NHÓM ƯU TIÊN (GV CỐ ĐỊNH TỪ CÀI ĐẶT LỚP HOẶC TKB CƠ SỞ) ---");
        System.out.println("Số lượng mục trong hàng đợi ưu tiên: " + priorityQueue.size());
        for (ClassSubjectPair csp : priorityQueue) {
            System.out.println("Đang xử lý (Ưu tiên): Lớp " + csp.lop.getMaLop() + " - Môn " + csp.mhh.getMaMH());
            scheduleSingleClassSubjectEntry(csp, true);
        }

        System.out.println("--- BẮT ĐẦU XẾP NHÓM LINH HOẠT (THUẬT TOÁN TỰ CHỌN GV) ---");
        System.out.println("Số lượng mục trong hàng đợi linh hoạt: " + flexibleQueue.size());
        for (ClassSubjectPair csp : flexibleQueue) {
            System.out.println("Đang xử lý (Linh hoạt): Lớp " + csp.lop.getMaLop() + " - Môn " + csp.mhh.getMaMH());
            scheduleSingleClassSubjectEntry(csp, false);
        }

        return this.generatedTimetable;
    }

    private boolean isPriorityAssignmentForClassSubject(Lop lop, MonHocHoc mhh) {
        if (classTeacherAssignmentsInput != null && classTeacherAssignmentsInput.containsKey(lop.getMaLop())) {
            Map<String, List<String>> assignmentsForClass = classTeacherAssignmentsInput.get(lop.getMaLop());
            if (assignmentsForClass != null && assignmentsForClass.containsKey(mhh.getMaMH())) {
                List<String> assignedGvIds = assignmentsForClass.get(mhh.getMaMH());
                if (assignedGvIds != null && !assignedGvIds.isEmpty()) {
                    return true;
                }
            }
        }
        String classSubjectKeyBaseTKB = lop.getMaLop() + "-" + mhh.getMaMH();
        return pinnedTeacherForSubjectClassFromBaseTKB.containsKey(classSubjectKeyBaseTKB);
    }

    private void scheduleSingleClassSubjectEntry(ClassSubjectPair csp, boolean isPriorityRule) {
        Lop lop = csp.lop;
        MonHocHoc mhh = csp.mhh;
        String classSubjectIdentifier = lop.getMaLop() + " - " + mhh.getMaMH() + " (" + mhh.getTenMH() + ")";

        Map<Integer, Integer> currentSoTietMoiThuForThisKhoi = soTietMoiThuTheoKhoiInput.get(lop.getKhoi());
        if (currentSoTietMoiThuForThisKhoi == null) {
            System.err.println("Cảnh báo: Lớp " + lop.getMaLop() + " (Khối " + lop.getKhoi() + ") không có cài đặt số tiết mỗi thứ. Sử dụng mặc định " + MAX_PERIODS_PER_DAY_SETTING + " tiết/ngày.");
            currentSoTietMoiThuForThisKhoi = new HashMap<>();
            for (int d = 2; d <= 7; d++) currentSoTietMoiThuForThisKhoi.put(d, MAX_PERIODS_PER_DAY_SETTING);
        }

        int soTietCanXepBanDau = mhh.getTongSoTiet();
        this.soTietDaXepTuCoSoGlobal = 0;
        if (generatedTimetable.containsKey(lop.getMaLop())) {
            this.soTietDaXepTuCoSoGlobal = (int) generatedTimetable.get(lop.getMaLop()).stream()
                    .filter(ct -> ct.getMaMH() != null && ct.getMaMH().equals(mhh.getMaMH()))
                    .count();
        }
        int soTietConLaiCanXep = soTietCanXepBanDau - this.soTietDaXepTuCoSoGlobal;

        System.out.println("  " + classSubjectIdentifier + ": Tổng tiết theo PPCT: " + soTietCanXepBanDau +
                ", Đã xếp từ TKB cơ sở: " + this.soTietDaXepTuCoSoGlobal +
                ", Cần xếp thêm: " + soTietConLaiCanXep);

        if (soTietConLaiCanXep <= 0) {
            System.out.println("  Đã đủ tiết (từ TKB cơ sở hoặc không cần xếp thêm) cho: " + classSubjectIdentifier);
            return;
        }

        List<GiaoVien> initialTeam = getAndLockTeachingTeamForSubject(mhh, lop, Collections.emptyList(), isPriorityRule, true);

        if (initialTeam.isEmpty()) {
            System.err.println("  KHÔNG TÌM ĐƯỢC GV BAN ĐẦU cho: " + classSubjectIdentifier +
                    (isPriorityRule ? " (ƯU TIÊN)" : " (LINH HOẠT)") +
                    ". Sẽ bị thiếu " + soTietConLaiCanXep + " tiết.");
            return;
        }
        System.out.println("  Đội GV ban đầu cho " + classSubjectIdentifier + ": " + initialTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));

        int scheduledPeriods = scheduleAllPeriodsForSubject(mhh, lop, initialTeam, soTietConLaiCanXep, currentSoTietMoiThuForThisKhoi, true);

        if (scheduledPeriods < soTietConLaiCanXep) {
            System.out.println("  THIẾU " + (soTietConLaiCanXep - scheduledPeriods) + " tiết cho " + classSubjectIdentifier +
                    " với đội GV: " + initialTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")) +
                    " (sau lần thử ngẫu nhiên). Gỡ các tiết đã xếp của lần thử này.");
            undoScheduledPeriodsForSubject(mhh, lop, initialTeam, scheduledPeriods);

            System.out.println("  Thử xếp lại (slot xác định) cho " + classSubjectIdentifier + " với đội GV ban đầu.");
            scheduledPeriods = scheduleAllPeriodsForSubject(mhh, lop, initialTeam, soTietConLaiCanXep, currentSoTietMoiThuForThisKhoi, false);

            if (scheduledPeriods < soTietConLaiCanXep) {
                System.out.println("  VẪN THIẾU " + (soTietConLaiCanXep - scheduledPeriods) + " tiết cho " + classSubjectIdentifier +
                        " với đội GV ban đầu (sau lần thử slot xác định). Gỡ các tiết đã xếp của lần thử này.");
                undoScheduledPeriodsForSubject(mhh, lop, initialTeam, scheduledPeriods);

                if (!isPriorityRule) {
                    System.out.println("  Môn linh hoạt " + classSubjectIdentifier + " bị thiếu tiết. Thử tìm đội GV MỚI (loại trừ đội GV ban đầu).");
                    List<GiaoVien> excludedTeachers = new ArrayList<>(initialTeam);
                    List<GiaoVien> newTeam = getAndLockTeachingTeamForSubject(mhh, lop, excludedTeachers, false, false);

                    if (!newTeam.isEmpty() && !listEqualsIgnoreOrder(newTeam, initialTeam)) {
                        System.out.println("  Đã tìm thấy đội GV MỚI cho " + classSubjectIdentifier + ": " + newTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));
                        System.out.println("  Thử xếp (slot ngẫu nhiên) cho " + classSubjectIdentifier + " với đội GV MỚI.");
                        scheduledPeriods = scheduleAllPeriodsForSubject(mhh, lop, newTeam, soTietConLaiCanXep, currentSoTietMoiThuForThisKhoi, true);

                        if (scheduledPeriods < soTietConLaiCanXep) {
                            System.out.println("  THIẾU " + (soTietConLaiCanXep - scheduledPeriods) + " tiết cho " + classSubjectIdentifier +
                                    " với đội GV MỚI (sau lần thử ngẫu nhiên). Gỡ các tiết đã xếp của lần thử này.");
                            undoScheduledPeriodsForSubject(mhh, lop, newTeam, scheduledPeriods);

                            System.out.println("  Thử xếp lại (slot xác định) cho " + classSubjectIdentifier + " với đội GV MỚI.");
                            scheduledPeriods = scheduleAllPeriodsForSubject(mhh, lop, newTeam, soTietConLaiCanXep, currentSoTietMoiThuForThisKhoi, false);

                            if (scheduledPeriods < soTietConLaiCanXep) {
                                System.err.println("  VẪN THIẾU " + (soTietConLaiCanXep - scheduledPeriods) + " tiết cho " + classSubjectIdentifier +
                                        " ngay cả với đội GV MỚI và thử slot xác định. Chấp nhận thiếu.");
                            }
                        }
                    } else {
                        System.err.println("  KHÔNG TÌM ĐƯỢC ĐỘI GV MỚI hoặc đội GV mới giống đội cũ cho " + classSubjectIdentifier + ". Chấp nhận thiếu.");
                    }
                } else {
                    System.err.println("  Môn ƯU TIÊN " + classSubjectIdentifier + " VẪN THIẾU " + (soTietConLaiCanXep - scheduledPeriods) + " tiết sau khi thử slot xác định. Chấp nhận thiếu.");
                }
            }
        }
        if (scheduledPeriods > 0) {
            System.out.println("  Đã xếp được " + scheduledPeriods + "/" + soTietConLaiCanXep + " tiết còn lại cho " + classSubjectIdentifier);
        }
    }

    private boolean listEqualsIgnoreOrder(List<GiaoVien> list1, List<GiaoVien> list2) {
        if (list1 == null && list2 == null) return true;
        if (list1 == null || list2 == null || list1.size() != list2.size()) return false;
        return new HashSet<>(list1.stream().map(GiaoVien::getMaGV).collect(Collectors.toList()))
                .equals(new HashSet<>(list2.stream().map(GiaoVien::getMaGV).collect(Collectors.toList())));
    }

    private void undoScheduledPeriodsForSubject(MonHocHoc mhh, Lop lop, List<GiaoVien> teamToUndo, int periodsToUndo) {
        if (periodsToUndo <= 0 || !generatedTimetable.containsKey(lop.getMaLop()) || teamToUndo == null || teamToUndo.isEmpty()) {
            return;
        }
        System.out.println("    GỠ TIẾT: Đang gỡ " + periodsToUndo + " tiết của môn " + mhh.getMaMH() + " lớp " + lop.getMaLop() + " do đội GV " + teamToUndo.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")) + " dạy.");

        List<ChiTietTKB> scheduleForClass = generatedTimetable.get(lop.getMaLop());
        List<ChiTietTKB> toRemoveFromSchedule = new ArrayList<>();
        int removedCount = 0;

        for (int i = scheduleForClass.size() - 1; i >= 0; i--) {
            if (removedCount >= periodsToUndo) break;
            ChiTietTKB ct = scheduleForClass.get(i);

            if (ct.getFlag() != 0) continue;

            if (ct.getMaMH().equals(mhh.getMaMH())) {
                boolean taughtByThisTeamMember = teamToUndo.stream().anyMatch(gv -> gv.getMaGV().equals(ct.getMaGV()));
                if (taughtByThisTeamMember) {
                    toRemoveFromSchedule.add(ct);
                    removedCount++;
                }
            }
        }

        if (removedCount < periodsToUndo && periodsToUndo > 0){
            System.out.println("    CẢNH BÁO GỠ TIẾT: Chỉ tìm thấy " + removedCount + "/" + periodsToUndo + " tiết (flag=0) để gỡ cho " + mhh.getMaMH() + " lớp " + lop.getMaLop() + " với đội GV " + teamToUndo.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));
        }

        for (ChiTietTKB ctToRemove : toRemoveFromSchedule) {
            scheduleForClass.remove(ctToRemove);
            if (lopBusySlots.containsKey(lop.getMaLop())) {
                lopBusySlots.get(lop.getMaLop())[ctToRemove.getThu()][ctToRemove.getTiet()] = false;
            }
            for (GiaoVien gvInTeam : teamToUndo) {
                if (teacherBusySlots.containsKey(gvInTeam.getMaGV())) {
                    teacherBusySlots.get(gvInTeam.getMaGV())[ctToRemove.getThu()][ctToRemove.getTiet()] = false;
                }
            }
        }
        if (!toRemoveFromSchedule.isEmpty()) {
            System.out.println("    GỠ TIẾT: Đã gỡ thành công " + toRemoveFromSchedule.size() + " tiết.");
        }
    }

    private List<GiaoVien> getAndLockTeachingTeamForSubject(MonHocHoc mhh, Lop lop, List<GiaoVien> excludedTeachers, boolean isPriorityRule, boolean isInitialAttemptForThisClassSubject) {
        String classSubjectKey = lop.getMaLop() + "-" + mhh.getMaMH();
        String logPrefix = "  GET_GV [" + classSubjectKey + "]: ";
        System.out.println(logPrefix + "Bắt đầu tìm GV. Ưu tiên: " + isPriorityRule + ", Lần đầu cho Lớp-Môn: " + isInitialAttemptForThisClassSubject +
                ", GV loại trừ: " + excludedTeachers.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));

        if (isInitialAttemptForThisClassSubject && currentLockedTeachersForClassSubject.containsKey(classSubjectKey)) {
            List<GiaoVien> previouslyLockedTeam = currentLockedTeachersForClassSubject.get(classSubjectKey);
            boolean anyExcluded = !excludedTeachers.isEmpty() && previouslyLockedTeam.stream().anyMatch(excludedTeachers::contains);
            if (!anyExcluded) {
                System.out.println(logPrefix + "Sử dụng đội GV đã khóa trước đó: " + previouslyLockedTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));
                return previouslyLockedTeam;
            } else {
                System.out.println(logPrefix + "Đội GV đã khóa trước đó (" + previouslyLockedTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")) + ") chứa GV bị loại trừ. Tìm mới.");
            }
        }

        List<GiaoVien> determinedTeam = new ArrayList<>();
        List<String> finalExcludedMaGVs = excludedTeachers.stream().map(GiaoVien::getMaGV).collect(Collectors.toList());

        if (isPriorityRule || isInitialAttemptForThisClassSubject) {
            if (classTeacherAssignmentsInput != null && classTeacherAssignmentsInput.containsKey(lop.getMaLop())) {
                Map<String, List<String>> assignmentsForClass = classTeacherAssignmentsInput.get(lop.getMaLop());
                if (assignmentsForClass != null && assignmentsForClass.containsKey(mhh.getMaMH())) {
                    List<String> assignedGvIdsFromSettings = assignmentsForClass.get(mhh.getMaMH());
                    if (assignedGvIdsFromSettings != null && !assignedGvIdsFromSettings.isEmpty()) {
                        System.out.println(logPrefix + "Tìm thấy cài đặt GV từ người dùng: " + assignedGvIdsFromSettings);
                        for (String maGV : assignedGvIdsFromSettings) {
                            if (finalExcludedMaGVs.contains(maGV)) {
                                System.out.println(logPrefix + "GV " + maGV + " từ cài đặt người dùng bị loại trừ.");
                                continue;
                            }
                            GiaoVien gv = giaoVienHopLe.stream().filter(g -> g.getMaGV().equals(maGV)).findFirst().orElse(null);
                            if (gv != null) {
                                TeacherCustomSettings settings = teacherCustomSettingsInput.get(gv.getMaGV());
                                boolean canTeachThisSubject = (settings != null && settings.getTeachingPreferenceForSubject(mhh.getMaMH()));
                                boolean generallyAvailable = isTeacherGenerallyAvailable(gv, mhh.getTongSoTiet());

                                System.out.println(logPrefix + "  Kiểm tra GV " + maGV + " từ cài đặt: Có thể dạy môn này? " + canTeachThisSubject + ". Còn khả năng nhận tiết? " + generallyAvailable);
                                if (canTeachThisSubject && generallyAvailable) {
                                    determinedTeam.add(gv);
                                } else {
                                    System.out.println(logPrefix + "  GV " + maGV + " từ cài đặt không thể dạy môn này hoặc không còn khả năng nhận thêm tiết.");
                                }
                            } else {
                                System.out.println(logPrefix + "  GV " + maGV + " từ cài đặt không nằm trong danh sách GV hợp lệ.");
                            }
                        }
                        if (!determinedTeam.isEmpty()) {
                            System.out.println(logPrefix + "Đã chọn đội GV từ cài đặt người dùng: " + determinedTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));
                            currentLockedTeachersForClassSubject.put(classSubjectKey, determinedTeam);
                            return determinedTeam;
                        } else if (isPriorityRule) {
                            System.err.println(logPrefix + "LỖI (ƯU TIÊN): Không tìm được GV hợp lệ nào từ cài đặt người dùng cho " + classSubjectKey + " sau khi lọc.");
                            currentLockedTeachersForClassSubject.put(classSubjectKey, Collections.emptyList());
                            return Collections.emptyList();
                        }
                    }
                }
            }

            if (pinnedTeacherForSubjectClassFromBaseTKB.containsKey(classSubjectKey)) {
                String pinnedMaGV = pinnedTeacherForSubjectClassFromBaseTKB.get(classSubjectKey);
                System.out.println(logPrefix + "Tìm thấy GV từ TKB cơ sở: " + pinnedMaGV);
                if (!finalExcludedMaGVs.contains(pinnedMaGV)) {
                    GiaoVien gvFromBase = giaoVienHopLe.stream().filter(gv -> gv.getMaGV().equals(pinnedMaGV)).findFirst().orElse(null);
                    if (gvFromBase != null) {
                        TeacherCustomSettings settings = teacherCustomSettingsInput.get(gvFromBase.getMaGV());
                        boolean canTeachThisSubject = (settings != null && settings.getTeachingPreferenceForSubject(mhh.getMaMH()));
                        boolean generallyAvailable = isTeacherGenerallyAvailable(gvFromBase, mhh.getTongSoTiet());

                        System.out.println(logPrefix + "  Kiểm tra GV " + pinnedMaGV + " từ TKB cơ sở: Có thể dạy môn này? " + canTeachThisSubject + ". Còn khả năng nhận tiết? " + generallyAvailable);
                        if (canTeachThisSubject && generallyAvailable) {
                            determinedTeam.add(gvFromBase);
                            System.out.println(logPrefix + "Đã chọn GV từ TKB cơ sở: " + determinedTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));
                            currentLockedTeachersForClassSubject.put(classSubjectKey, determinedTeam);
                            return determinedTeam;
                        } else if (isPriorityRule) {
                            System.err.println(logPrefix + "LỖI (ƯU TIÊN): GV " + pinnedMaGV + " từ TKB cơ sở không thể dạy môn này hoặc không còn khả năng nhận thêm tiết.");
                            currentLockedTeachersForClassSubject.put(classSubjectKey, Collections.emptyList());
                            return Collections.emptyList();
                        }
                    } else {
                        System.out.println(logPrefix + "  GV " + pinnedMaGV + " từ TKB cơ sở không nằm trong danh sách GV hợp lệ.");
                    }
                } else {
                    System.out.println(logPrefix + "GV " + pinnedMaGV + " từ TKB cơ sở bị loại trừ.");
                }
            }
        }

        System.out.println(logPrefix + "Thực hiện 'Để thuật toán tự chọn'...");
        List<GiaoVien> potentialTeachers;
        boolean isSpecialSubject = mhh.getMaMH().startsWith("GDDP") || mhh.getMaMH().startsWith("HDTNHN");
        String targetMaTCM = mhh.getMaTCM();

        potentialTeachers = giaoVienHopLe.stream()
                .filter(gv -> !finalExcludedMaGVs.contains(gv.getMaGV()))
                .filter(gv -> {
                    TeacherCustomSettings settings = teacherCustomSettingsInput.get(gv.getMaGV());
                    return settings != null && settings.getTeachingPreferenceForSubject(mhh.getMaMH());
                })
                .filter(gv -> {
                    if (targetMaTCM != null && !targetMaTCM.isBlank()) {
                        return gv.getMaTCM() != null && gv.getMaTCM().equals(targetMaTCM);
                    } else if (isSpecialSubject) {
                        return true; // Môn đặc biệt không có TCM chuẩn, chỉ dựa vào preference
                    }
                    return false; // Môn không đặc biệt, không có TCM -> không hợp lệ
                })
                .filter(gv -> isTeacherGenerallyAvailable(gv, mhh.getTongSoTiet()))
                .sorted(Comparator.comparingLong(this::countCurrentPeriodsForTeacher))
                .collect(Collectors.toList());

        if (!potentialTeachers.isEmpty()) {
            System.out.println(logPrefix + "Danh sách GV tiềm năng sau khi lọc ('tự chọn'): " + potentialTeachers.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));
            GiaoVien selectedTeacher = potentialTeachers.stream()
                    .filter(gv -> lop.getGvcn() != null && gv.getMaGV().equals(lop.getGvcn()))
                    .findFirst()
                    .orElse(potentialTeachers.get(0));

            determinedTeam.add(selectedTeacher);
            System.out.println(logPrefix + "Đã chọn GV bởi thuật toán 'tự chọn': " + selectedTeacher.getMaGV());
            currentLockedTeachersForClassSubject.put(classSubjectKey, determinedTeam);
            return determinedTeam;
        } else {
            System.out.println(logPrefix + "KHÔNG tìm thấy GV tiềm năng nào sau khi lọc ('tự chọn').");
        }

        if (isInitialAttemptForThisClassSubject && !currentLockedTeachersForClassSubject.containsKey(classSubjectKey)) {
            System.out.println(logPrefix + "Không tìm được GV nào, khóa danh sách rỗng cho " + classSubjectKey);
            currentLockedTeachersForClassSubject.put(classSubjectKey, Collections.emptyList());
        }
        return Collections.emptyList();
    }

    private boolean isTeacherGenerallyAvailable(GiaoVien gv, int periodsToAdd) {
        if (gv.getSoTietQuyDinh() == null || gv.getSoTietQuyDinh() == 0) return true;
        long soTietDaDayHienTai = countCurrentPeriodsForTeacher(gv);
        int soTietChucVu = xepTKBDAO.getSoTietChucVuDaSuDung(gv.getMaGV(), (selectedHocKy != null ? selectedHocKy.getMaHK() : "")); // Cần MaHK
        return (soTietDaDayHienTai + soTietChucVu + periodsToAdd) <= gv.getSoTietQuyDinh();
    }

    private int scheduleAllPeriodsForSubject(MonHocHoc mhh, Lop lop, List<GiaoVien> lockedInTeam, int soTietCanXep, Map<Integer, Integer> currentSoTietMoiThuForThisKhoi, boolean useRandomSlots) {
        int soTietDaXepThucTeChoDotNay = 0;
        String classSubjectIdentifier = lop.getMaLop() + "-" + mhh.getMaMH();
        String teamString = lockedInTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(","));

        if (lockedInTeam == null || lockedInTeam.isEmpty()) {
            System.err.println("    SCHEDULE_ALL: Không có GV nào được khóa cho " + classSubjectIdentifier + ". Không thể xếp.");
            return 0;
        }
        System.out.println("    SCHEDULE_ALL ["+ classSubjectIdentifier + " với GV " + teamString + "]: Cần xếp " + soTietCanXep + " tiết. Dùng slot " + (useRandomSlots ? "ngẫu nhiên." : "xác định."));

        for (int i = 0; i < soTietCanXep; i++) {
            boolean teamStillAvailable = true;
            for(GiaoVien gv : lockedInTeam) {
                if (!isTeacherGenerallyAvailable(gv, 1)) {
                    System.out.println("    SCHEDULE_ALL: GV " + gv.getMaGV() + " trong đội không còn khả năng nhận thêm tiết cho " + classSubjectIdentifier);
                    teamStillAvailable = false;
                    break;
                }
            }
            if (!teamStillAvailable) {
                System.err.println("    SCHEDULE_ALL: Một GV trong đội " + teamString + " không còn khả năng nhận thêm tiết cho " + classSubjectIdentifier + ". Dừng xếp cho môn này với đội này.");
                break;
            }

            if (tryScheduleSinglePeriodForSubject(mhh, lop, lockedInTeam, currentSoTietMoiThuForThisKhoi, useRandomSlots)) {
                soTietDaXepThucTeChoDotNay++;
            } else {
                long tongSoTietDaXepChoMonNayCuaLopNay = this.soTietDaXepTuCoSoGlobal + soTietDaXepThucTeChoDotNay;
                System.err.println("    SCHEDULE_ALL: Không thể xếp tiết thứ " + (tongSoTietDaXepChoMonNayCuaLopNay + 1) +
                        " (thứ " + (i + 1) + " trong " + soTietCanXep + " tiết cần xếp của đợt này)" +
                        " cho môn " + mhh.getTenMH() + " (" + mhh.getMaMH() + ") của lớp " + lop.getMaLop() +
                        " với đội GV: " + teamString +
                        (useRandomSlots ? " (thử ngẫu nhiên)" : " (thử xác định)"));
            }
        }
        System.out.println("    SCHEDULE_ALL ["+ classSubjectIdentifier + "]: Đã xếp được thực tế " + soTietDaXepThucTeChoDotNay + " tiết trong đợt này.");
        return soTietDaXepThucTeChoDotNay;
    }

    private boolean tryScheduleSinglePeriodForSubject(MonHocHoc mhh, Lop lop, List<GiaoVien> lockedInTeam, Map<Integer, Integer> currentSoTietMoiThuForThisKhoi, boolean useRandomSlots) {
        List<Integer> thuList = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7));
        if (useRandomSlots) {
            Collections.shuffle(thuList);
        }

        for (int thu : thuList) {
            int maxTietHomNayTheoCaiDatLop = Math.min(currentSoTietMoiThuForThisKhoi.getOrDefault(thu, MAX_PERIODS_PER_DAY_SETTING), MAX_PERIODS_PER_DAY_SETTING);
            if (maxTietHomNayTheoCaiDatLop == 0) continue;

            List<Integer> tietList = new ArrayList<>();
            for (int t = 1; t <= maxTietHomNayTheoCaiDatLop; t++) tietList.add(t);
            if (useRandomSlots) {
                Collections.shuffle(tietList);
            }

            for (int tiet : tietList) {
                boolean classSlotAvailable = !lopBusySlots.get(lop.getMaLop())[thu][tiet];
                if (!classSlotAvailable) continue;

                boolean allLockedInTeachersAvailableInThisSlot = true;
                for (GiaoVien gv : lockedInTeam) {
                    if (!teacherBusySlots.containsKey(gv.getMaGV()) || teacherBusySlots.get(gv.getMaGV())[thu][tiet]) {
                        allLockedInTeachersAvailableInThisSlot = false;
                        break;
                    }
                }
                if (!allLockedInTeachersAvailableInThisSlot) continue;

                if (checkSpecificConstraints(mhh, lop, thu, tiet, lockedInTeam)) {
                    String hoTenGVDisplay;
                    if (lockedInTeam.isEmpty()) {
                        hoTenGVDisplay = "(GV không xác định)";
                    } else {
                        hoTenGVDisplay = lockedInTeam.stream()
                                .map(gv -> {
                                    String ho = gv.getHoGV() != null ? gv.getHoGV().trim() : "";
                                    String ten = gv.getTenGV() != null ? gv.getTenGV().trim() : "";
                                    String fullName = (ho + " " + ten).trim();
                                    return fullName.isEmpty() ? gv.getMaGV() : fullName;
                                })
                                .collect(Collectors.joining(", "));
                    }

                    String maGVForChiTiet = lockedInTeam.isEmpty() ? null : lockedInTeam.get(0).getMaGV();

                    ChiTietTKB chiTietMoi = ChiTietTKB.taoChoXepLichTuDong(
                            thu, tiet, mhh.getTenMH(),
                            lop.getMaLop(),
                            hoTenGVDisplay,
                            maGVForChiTiet,
                            mhh.getMaMH(),
                            0);

                    generatedTimetable.computeIfAbsent(lop.getMaLop(), k -> new ArrayList<>()).add(chiTietMoi);
                    lopBusySlots.get(lop.getMaLop())[thu][tiet] = true;
                    for (GiaoVien gv : lockedInTeam) {
                        if (teacherBusySlots.containsKey(gv.getMaGV())) {
                            teacherBusySlots.get(gv.getMaGV())[thu][tiet] = true;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private static class ClassSubjectPair {
        Lop lop;
        MonHocHoc mhh;
        ClassSubjectPair(Lop lop, MonHocHoc mhh) { this.lop = lop; this.mhh = mhh; }
        @Override public String toString() { return "CSP[" + lop.getMaLop() + "-" + mhh.getMaMH() + "]"; }
    }

    private void initializeSchedulingState() {
        this.generatedTimetable = new HashMap<>();
        this.lopBusySlots = new HashMap<>();
        this.teacherBusySlots = new HashMap<>();
        this.currentLockedTeachersForClassSubject.clear();
        this.pinnedTeacherForSubjectClassFromBaseTKB.clear();

        this.giaoVienHopLe = tatCaGiaoVienDayDu.stream()
                .filter(gv -> {
                    if ("ADMIN".equalsIgnoreCase(gv.getMaGV())) return false;
                    TeacherCustomSettings settings = teacherCustomSettingsInput.get(gv.getMaGV());
                    return (settings == null) || settings.isParticipateInScheduling();
                })
                .collect(Collectors.toList());
        System.out.println("Khởi tạo trạng thái: Số GV hợp lệ (tham gia TKB): " + this.giaoVienHopLe.size() + "/" + this.tatCaGiaoVienDayDu.size());

        if (tatCaLop != null) {
            for (Lop lop : tatCaLop) {
                lopBusySlots.put(lop.getMaLop(), new boolean[8][MAX_PERIODS_PER_DAY_SETTING + 1]);
            }
        }
        for (GiaoVien gv : giaoVienHopLe) {
            teacherBusySlots.put(gv.getMaGV(), new boolean[8][MAX_PERIODS_PER_DAY_SETTING + 1]);
        }

        if (tkbCoSo != null && chiTietTkbCoSo != null) {
            System.out.println("Khởi tạo trạng thái: Đang xử lý " + chiTietTkbCoSo.size() + " chi tiết từ TKB cơ sở " + tkbCoSo.getMaTKB());
            int countBaseSlotsAdded = 0;
            for (ChiTietTKB ctCoSo : chiTietTkbCoSo) {
                if (ctCoSo.getThu() >= 2 && ctCoSo.getThu() <= 7 &&
                        ctCoSo.getTiet() >= 1 && ctCoSo.getTiet() <= MAX_PERIODS_PER_DAY_SETTING) {
                    generatedTimetable.computeIfAbsent(ctCoSo.getMaLop(), k -> new ArrayList<>()).add(ctCoSo);
                    countBaseSlotsAdded++;
                    if (lopBusySlots.containsKey(ctCoSo.getMaLop())) {
                        lopBusySlots.get(ctCoSo.getMaLop())[ctCoSo.getThu()][ctCoSo.getTiet()] = true;
                    } else {
                        System.out.println("Cảnh báo: Lớp " + ctCoSo.getMaLop() + " trong TKB cơ sở không có trong danh sách lớp hiện tại.");
                    }
                    if (ctCoSo.getMaGV() != null && giaoVienHopLe.stream().anyMatch(gv -> gv.getMaGV().equals(ctCoSo.getMaGV()))) {
                        if (teacherBusySlots.containsKey(ctCoSo.getMaGV())) {
                            teacherBusySlots.get(ctCoSo.getMaGV())[ctCoSo.getThu()][ctCoSo.getTiet()] = true;
                        }
                        if (ctCoSo.getMaMH() != null) {
                            String pinnedKey = ctCoSo.getMaLop() + "-" + ctCoSo.getMaMH();
                            pinnedTeacherForSubjectClassFromBaseTKB.put(pinnedKey, ctCoSo.getMaGV());
                        }
                    } else if (ctCoSo.getMaGV() != null) {
                        System.out.println("Cảnh báo: GV " + ctCoSo.getMaGV() + " trong TKB cơ sở không hợp lệ hoặc không tham gia xếp TKB.");
                    }
                }
            }
            System.out.println("Khởi tạo trạng thái: Đã thêm " + countBaseSlotsAdded + " tiết từ TKB cơ sở vào generatedTimetable và busy_slots.");
        } else {
            System.out.println("Khởi tạo trạng thái: Không có TKB cơ sở hoặc không có chi tiết TKB cơ sở.");
            chiTietTkbCoSo = null;
        }
    }

    private boolean isBaseDataSufficient() {
        String currentHocKyId = (selectedHocKy != null) ? selectedHocKy.getMaHK() : "N/A";
        if (selectedHocKy == null) { System.err.println("Thiếu dữ liệu: Học kỳ chưa chọn."); return false; }
        if (tatCaLop == null || tatCaLop.isEmpty()) { System.err.println("Thiếu dữ liệu: Không có danh sách lớp."); return false; }
        if (phanCongMonHocChoLop == null || phanCongMonHocChoLop.isEmpty()) { System.err.println("Thiếu dữ liệu: Không có phân công môn học cho lớp nào trong học kỳ " + currentHocKyId); return false; }
        if (tatCaGiaoVienDayDu == null || tatCaGiaoVienDayDu.isEmpty()) { System.err.println("Thiếu dữ liệu: Không có danh sách giáo viên."); return false; }
        if (giaoVienHopLe == null || giaoVienHopLe.isEmpty()) { System.err.println("Thiếu dữ liệu: Không có giáo viên nào hợp lệ tham gia xếp TKB (sau khi lọc theo cài đặt)."); return false; }
        if (soTietMoiThuTheoKhoiInput == null || soTietMoiThuTheoKhoiInput.isEmpty()) { System.err.println("Thiếu dữ liệu: Không có cài đặt số tiết mỗi thứ theo khối."); return false; }
        return true;
    }

    private long countCurrentPeriodsForTeacher(GiaoVien gv) {
        if (gv == null || generatedTimetable == null) return 0;
        return generatedTimetable.values().stream()
                .flatMap(List::stream)
                .filter(ct -> ct.getMaGV() != null && ct.getMaGV().equals(gv.getMaGV()))
                .count();
    }

    private boolean checkSpecificConstraints(MonHocHoc mhh, Lop lop, int thu, int tiet, List<GiaoVien> teamOfTeachers) {
        List<ChiTietTKB> tkbCuaLopTrongNgay = generatedTimetable.getOrDefault(lop.getMaLop(), Collections.emptyList())
                .stream().filter(ct -> ct.getThu() == thu).collect(Collectors.toList());

        String maMHCurrentUpper = mhh.getMaMH().toUpperCase();
        if (maMHCurrentUpper.startsWith(PREFIX_GDQPAN) || maMHCurrentUpper.startsWith(PREFIX_GDTC)) {
            String otherConflictPrefix = maMHCurrentUpper.startsWith(PREFIX_GDQPAN) ? PREFIX_GDTC : PREFIX_GDQPAN;
            if (tkbCuaLopTrongNgay.stream().anyMatch(ct -> ct.getMaMH() != null && ct.getMaMH().toUpperCase().startsWith(otherConflictPrefix))) {
                return false;
            }
        }

        for(GiaoVien currentGiaoVienInTeam : teamOfTeachers) {
            int consecutiveCountForThisGV = 0;
            for (int i = 1; i < MAX_CONSECUTIVE_PERIODS; i++) {
                int tietTruocDo = tiet - i;
                if (tietTruocDo < 1) break;
                boolean matchFoundForPrevPeriod = false;
                for (ChiTietTKB ctDaXep : tkbCuaLopTrongNgay) {
                    if (ctDaXep.getTiet() == tietTruocDo) {
                        if (ctDaXep.getMaMH() != null && ctDaXep.getMaMH().equals(mhh.getMaMH()) &&
                                ctDaXep.getMaGV() != null && ctDaXep.getMaGV().equals(currentGiaoVienInTeam.getMaGV())) {
                            consecutiveCountForThisGV++;
                            matchFoundForPrevPeriod = true;
                        }
                        break;
                    }
                }
                if (!matchFoundForPrevPeriod) break;
            }
            if (consecutiveCountForThisGV >= MAX_CONSECUTIVE_PERIODS) {
                return false;
            }
        }
        return true;
    }

    @FXML public void initialize() {
        setupTableColumns();
        tkbTableView.setFixedCellSize(ROW_HEIGHT);
        double calculatedTableHeight = HEADER_HEIGHT + (MAX_PERIODS_PER_DAY_SETTING * ROW_HEIGHT) + 2.0;
        tkbTableView.setPrefHeight(calculatedTableHeight);
        tkbTableView.setMinHeight(calculatedTableHeight);
        tkbTableView.setMaxHeight(calculatedTableHeight);
        if (lopTKBTitleLabel != null) lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP");
        progressIndicator.setVisible(false);
    }

    private void loadBaseData() {
        System.out.println("Bắt đầu loadBaseData...");
        tatCaLop = xepTKBDAO.getDanhSachLop();
        System.out.println("  Số lớp tải được: " + (tatCaLop != null ? tatCaLop.size() : "null"));

        if (selectedHocKy != null) {
            phanCongMonHocChoLop = xepTKBDAO.getPhanCongMonHocChoTatCaLop(selectedHocKy.getMaHK());
            System.out.println("  Phân công môn học cho các lớp (HK: " + selectedHocKy.toString() + "): " + (phanCongMonHocChoLop != null ? phanCongMonHocChoLop.size() + " lớp có PC" : "null"));

            if (phanCongMonHocChoLop != null && tatCaLop != null) {
                for (Lop lop : tatCaLop) {
                    List<MonHocHoc> monCuaLop = phanCongMonHocChoLop.computeIfAbsent(lop.getMaLop(), k -> new ArrayList<>());
                    String khoi = lop.getKhoi();
                    List<CaiDatGVController.MonHocInfo> specialSubjectsForKhoi = new ArrayList<>();
                    if ("10".equals(khoi)) { specialSubjectsForKhoi.add(CaiDatGVController.GDDP10_INFO); specialSubjectsForKhoi.add(CaiDatGVController.HDTNHN10_INFO); }
                    else if ("11".equals(khoi)) { specialSubjectsForKhoi.add(CaiDatGVController.GDDP11_INFO); specialSubjectsForKhoi.add(CaiDatGVController.HDTNHN11_INFO); }
                    else if ("12".equals(khoi)) { specialSubjectsForKhoi.add(CaiDatGVController.GDDP12_INFO); specialSubjectsForKhoi.add(CaiDatGVController.HDTNHN12_INFO); }

                    for (CaiDatGVController.MonHocInfo specialInfo : specialSubjectsForKhoi) {
                        if (monCuaLop.stream().noneMatch(m -> m.getMaMH().equals(specialInfo.maMH))) {
                            int soTietMonDacBiet = 1;
                            monCuaLop.add(new MonHocHoc(specialInfo.maMH, specialInfo.tenMH, soTietMonDacBiet, null));
                            System.out.println("    Đã thêm môn đặc biệt " + specialInfo.maMH + " cho lớp " + lop.getMaLop());
                        }
                    }
                }
            }
        } else {
            phanCongMonHocChoLop = new HashMap<>();
            System.out.println("  Không có học kỳ được chọn, phanCongMonHocChoLop được khởi tạo rỗng.");
        }

        tatCaGiaoVienDayDu = xepTKBDAO.getDanhSachGiaoVienDayDu();
        System.out.println("  Số GV đầy đủ tải được: " + (tatCaGiaoVienDayDu != null ? tatCaGiaoVienDayDu.size() : "null"));

        List<MonHoc> allMonHocDB = xepTKBDAO.getAllMonHoc();
        danhMucMonHoc.clear();
        if (allMonHocDB != null) {
            allMonHocDB.forEach(mh -> danhMucMonHoc.put(mh.getMaMH(), mh));
        }
        CaiDatGVController.SPECIAL_SUBJECTS_INFO.forEach(info -> {
            danhMucMonHoc.putIfAbsent(info.maMH, new MonHoc(info.maMH, info.tenMH, null, null));
        });
        System.out.println("  Số môn học trong danh mục (danhMucMonHoc): " + danhMucMonHoc.size());

        if (tkbCoSo != null) {
            chiTietTkbCoSo = xepTKBDAO.getChiTietTKBByMaTKB(tkbCoSo.getMaTKB());
            System.out.println("  Số chi tiết TKB cơ sở (" + tkbCoSo.getMaTKB() + ") tải được: " + (chiTietTkbCoSo != null ? chiTietTkbCoSo.size() : "null"));
        } else {
            chiTietTkbCoSo = null;
            System.out.println("  Không có TKB cơ sở được chọn.");
        }
        System.out.println("Kết thúc loadBaseData.");
    }

    private void setupTableColumns() {
        tietColumn.setCellValueFactory(new PropertyValueFactory<>("tiet"));
        tietColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle(empty ? "" : "-fx-alignment: CENTER; -fx-font-weight: bold;");
            }
        });

        final Callback<TableColumn<TietHocData, ChiTietTKB>, TableCell<TietHocData, ChiTietTKB>> cellFactory = param -> new TableCell<>() {
            @Override
            protected void updateItem(ChiTietTKB item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getMaMH() == null) {
                    setText(null);
                    setTooltip(null);
                    setStyle("");
                } else {
                    String tenMHDisplay = item.getTenMonHoc();
                    if ((tenMHDisplay == null || tenMHDisplay.isBlank()) && danhMucMonHoc.containsKey(item.getMaMH())) {
                        MonHoc monHocRef = danhMucMonHoc.get(item.getMaMH());
                        if (monHocRef != null) tenMHDisplay = monHocRef.getTenMH();
                    }
                    if (tenMHDisplay == null || tenMHDisplay.isBlank()) tenMHDisplay = "MH: " + item.getMaMH();

                    String displayText = tenMHDisplay;
                    String gvDisplay = item.getHoTenGV(); // Đã được định dạng khi tạo ChiTietTKB

                    // Chỉ nối tên GV nếu gvDisplay có ý nghĩa
                    if (gvDisplay != null && !gvDisplay.isBlank() &&
                            !gvDisplay.equalsIgnoreCase("(Chưa phân công GV)") &&
                            !gvDisplay.equalsIgnoreCase("(Lỗi tên GV)") &&
                            !gvDisplay.equalsIgnoreCase("(GV không xác định)") &&
                            !gvDisplay.equalsIgnoreCase("(Chưa có GV)") ) {
                        displayText += " - " + gvDisplay; // Sử dụng " - " thay vì "\n"
                    }
                    setText(displayText);
                    setTooltip(new Tooltip(displayText)); // Tooltip sẽ hiển thị giống như text
                    setStyle("-fx-alignment: CENTER-LEFT; -fx-text-alignment: LEFT; -fx-wrap-text: true; -fx-padding: 3 5 3 5;");
                }
            }
        };

        thu2Column.setCellValueFactory(new PropertyValueFactory<>("thu2")); thu2Column.setCellFactory(cellFactory);
        thu3Column.setCellValueFactory(new PropertyValueFactory<>("thu3")); thu3Column.setCellFactory(cellFactory);
        thu4Column.setCellValueFactory(new PropertyValueFactory<>("thu4")); thu4Column.setCellFactory(cellFactory);
        thu5Column.setCellValueFactory(new PropertyValueFactory<>("thu5")); thu5Column.setCellFactory(cellFactory);
        thu6Column.setCellValueFactory(new PropertyValueFactory<>("thu6")); thu6Column.setCellFactory(cellFactory);
        thu7Column.setCellValueFactory(new PropertyValueFactory<>("thu7")); thu7Column.setCellFactory(cellFactory);
    }

    private void setupLopMenu() {
        menuKhoi10.getItems().clear();
        menuKhoi11.getItems().clear();
        menuKhoi12.getItems().clear();

        if (tatCaLop == null || tatCaLop.isEmpty()) {
            System.out.println("Không có lớp nào để tạo menu.");
            return;
        }

        Map<String, List<Lop>> lopTheoKhoi = tatCaLop.stream()
                .filter(lop -> lop.getKhoi() != null)
                .collect(Collectors.groupingBy(Lop::getKhoi, TreeMap::new, Collectors.toList()));

        lopTheoKhoi.forEach((khoi, listLopTrongKhoi) -> {
            listLopTrongKhoi.sort(Comparator.comparing(Lop::getTenLop, Comparator.nullsLast(String::compareToIgnoreCase)));
            Menu menuForThisKhoi = null;
            if ("10".equals(khoi)) menuForThisKhoi = menuKhoi10;
            else if ("11".equals(khoi)) menuForThisKhoi = menuKhoi11;
            else if ("12".equals(khoi)) menuForThisKhoi = menuKhoi12;

            if (menuForThisKhoi != null) {
                final Menu finalMenuForThisKhoi = menuForThisKhoi;
                listLopTrongKhoi.forEach(lop -> {
                    String tenLopHienThi = (lop.getTenLop() != null && !lop.getTenLop().isBlank()) ? lop.getTenLop() : lop.getMaLop();
                    if (!tenLopHienThi.equals(lop.getMaLop())) {
                        tenLopHienThi += " (" + lop.getMaLop() + ")";
                    }
                    MenuItem item = new MenuItem(tenLopHienThi);
                    item.setOnAction(e -> displayTKBForLop(lop.getMaLop()));
                    finalMenuForThisKhoi.getItems().add(item);
                });
            }
        });
    }

    private void displayTKBForLop(String maLop) {
        ObservableList<TietHocData> tkbDataList = FXCollections.observableArrayList();
        Lop currentLop = (tatCaLop != null) ? tatCaLop.stream().filter(l -> l.getMaLop().equals(maLop)).findFirst().orElse(null) : null;

        String khoiCuaLop = "";
        if (currentLop != null && currentLop.getKhoi() != null) {
            khoiCuaLop = currentLop.getKhoi();
        } else if (maLop != null && maLop.length() >= 2) {
            String khoiTuMaLop = maLop.substring(0, 2);
            if (Arrays.asList("10", "11", "12").contains(khoiTuMaLop)) {
                khoiCuaLop = khoiTuMaLop;
            }
        }

        Map<Integer, Integer> soTietMoiThuCuaKhoiNay = soTietMoiThuTheoKhoiInput.getOrDefault(khoiCuaLop, Collections.emptyMap());
        int maxTietDisplayTrongNgay = MAX_PERIODS_PER_DAY_SETTING;
        if (!soTietMoiThuCuaKhoiNay.isEmpty()) {
            maxTietDisplayTrongNgay = Math.min(
                    soTietMoiThuCuaKhoiNay.values().stream().max(Integer::compareTo).orElse(MAX_PERIODS_PER_DAY_SETTING),
                    MAX_PERIODS_PER_DAY_SETTING
            );
        }
        if (maxTietDisplayTrongNgay == 0 && MAX_PERIODS_PER_DAY_SETTING > 0) maxTietDisplayTrongNgay = MAX_PERIODS_PER_DAY_SETTING;

        for (int i = 1; i <= MAX_PERIODS_PER_DAY_SETTING; i++) {
            tkbDataList.add(new TietHocData("Tiết " + i));
        }

        List<ChiTietTKB> chiTietsCuaLop = (generatedTimetable != null) ? generatedTimetable.get(maLop) : null;
        if (chiTietsCuaLop != null) {
            for (ChiTietTKB ct : chiTietsCuaLop) {
                if (ct.getTiet() > 0 && ct.getTiet() <= MAX_PERIODS_PER_DAY_SETTING) {
                    TietHocData rowData = tkbDataList.get(ct.getTiet() - 1);
                    int gioiHanTietChoNgayNay = Math.min(soTietMoiThuCuaKhoiNay.getOrDefault(ct.getThu(), maxTietDisplayTrongNgay), maxTietDisplayTrongNgay);
                    if (ct.getTiet() <= gioiHanTietChoNgayNay || gioiHanTietChoNgayNay == 0) {
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
        double newHeight = HEADER_HEIGHT + (MAX_PERIODS_PER_DAY_SETTING * ROW_HEIGHT) + 2.0;
        tkbTableView.setPrefHeight(newHeight);
        tkbTableView.setMinHeight(newHeight);
        tkbTableView.setMaxHeight(newHeight);
        tkbTableView.refresh();

        String tenLopDisplay = maLop;
        if (currentLop != null && currentLop.getTenLop() != null && !currentLop.getTenLop().isBlank()) {
            tenLopDisplay = currentLop.getTenLop() + " (" + maLop + ")";
        }
        lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP " + tenLopDisplay.toUpperCase());
        infoLabel.setText("Hiển thị TKB lớp: " + maLop + (selectedHocKy != null ? " (HK: " + selectedHocKy.toString() + ")" : "")); // Sử dụng toString()
    }

    private void displayEmptyTKB() {
        ObservableList<TietHocData> list = FXCollections.observableArrayList();
        for (int i = 1; i <= MAX_PERIODS_PER_DAY_SETTING; i++) {
            list.add(new TietHocData("Tiết " + i));
        }
        tkbTableView.setItems(list);
        double height = HEADER_HEIGHT + (MAX_PERIODS_PER_DAY_SETTING * ROW_HEIGHT) + 2.0;
        tkbTableView.setPrefHeight(height); tkbTableView.setMinHeight(height); tkbTableView.setMaxHeight(height);
        tkbTableView.refresh();
        if (lopTKBTitleLabel != null) lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP");
    }

    @FXML void handleXemTatCaLop(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Tính năng xem TKB tất cả các lớp chưa được triển khai chi tiết trong giao diện này. Vui lòng chọn từng lớp từ menu.");
        printScheduleStatistics();
    }

    private void printScheduleStatistics() {
        if (generatedTimetable == null || phanCongMonHocChoLop == null || tatCaLop == null) {
            System.out.println("Không đủ dữ liệu để in thống kê TKB.");
            return;
        }
        System.out.println("\n--- THỐNG KÊ XẾP LỊCH ---");
        // Sắp xếp danh sách lớp để thống kê có thứ tự
        List<Lop> sortedLops = new ArrayList<>(tatCaLop);
        sortedLops.sort(Comparator.comparing(Lop::getKhoi, Comparator.nullsLast(String::compareTo))
                .thenComparing(Lop::getMaLop, Comparator.nullsLast(String::compareTo)));

        for (Lop lop : sortedLops) {
            List<MonHocHoc> monCanDay = phanCongMonHocChoLop.get(lop.getMaLop());
            List<ChiTietTKB> monDaXep = generatedTimetable.get(lop.getMaLop());

            if (monCanDay == null) {
                System.out.println("Lớp " + lop.getMaLop() + ": Không có phân công môn học.");
                continue;
            }
            // Sắp xếp môn cần dạy để thống kê có thứ tự
            monCanDay.sort(Comparator.comparing(MonHocHoc::getTenMH, Comparator.nullsLast(String::compareToIgnoreCase)));

            System.out.println("Lớp: " + lop.getMaLop() + " (" + lop.getTenLop() + ")");
            for (MonHocHoc mhh : monCanDay) {
                long soTietDaXepChoMonNay = 0;
                if (monDaXep != null) {
                    soTietDaXepChoMonNay = monDaXep.stream().filter(ct -> ct.getMaMH().equals(mhh.getMaMH())).count();
                }
                System.out.print("  - Môn: " + mhh.getTenMH() + " (" + mhh.getMaMH() + "): Cần " + mhh.getTongSoTiet() + " tiết, Đã xếp: " + soTietDaXepChoMonNay);
                if (soTietDaXepChoMonNay < mhh.getTongSoTiet()) {
                    System.out.println(" (THIẾU " + (mhh.getTongSoTiet() - soTietDaXepChoMonNay) + ")");
                } else {
                    System.out.println(" (Đủ)");
                }
            }
        }
        System.out.println("--- KẾT THÚC THỐNG KÊ ---\n");
    }

    @FXML void handleLuuThoiKhoaBieu(ActionEvent event) {
        if (generatedTimetable == null || generatedTimetable.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Không có dữ liệu", "Chưa có TKB để lưu.");
            return;
        }
        if (selectedHocKy == null) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Không xác định được học kỳ để lưu TKB.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn lưu thời khóa biểu này?\nMột mã TKB mới sẽ được tạo cho Học kỳ: " + selectedHocKy.toString() + ".\nLƯU Ý: Các TKB cũ của học kỳ này sẽ không bị ảnh hưởng trực tiếp, nhưng bạn nên quản lý các phiên bản TKB cẩn thận.", // Sử dụng toString()
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Xác nhận lưu Thời Khóa Biểu");
        confirm.setHeaderText(null);
        confirm.getDialogPane().setMinWidth(400);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            progressIndicator.setVisible(true);
            statusLabel.setText("Đang chuẩn bị lưu TKB...");

            String nguoiTaoMaGV = "ADMIN";
            String buoiHoc = (tkbCoSo != null && tkbCoSo.getBuoi() != null && !tkbCoSo.getBuoi().isBlank()) ? tkbCoSo.getBuoi() : "CHUNG";

            String newMaTKB = xepTKBDAO.taoThoiKhoaBieuMoi(selectedHocKy.getMaHK(), buoiHoc, nguoiTaoMaGV);

            if (newMaTKB != null) {
                maTKBMoiDuocTao = newMaTKB;
                final int[] successCount = {0};
                final int[] failureCount = {0};

                List<ChiTietTKB> allChiTietToSave = generatedTimetable.values().stream()
                        .flatMap(List::stream)
                        .filter(ct -> ct.getMaMH() != null && !ct.getMaMH().isBlank())
                        .collect(Collectors.toList());

                if (allChiTietToSave.isEmpty()) {
                    progressIndicator.setVisible(false);
                    statusLabel.setText("Không có chi tiết TKB hợp lệ nào để lưu.");
                    showAlert(Alert.AlertType.INFORMATION, "Không có dữ liệu", "Không có tiết học nào được xếp để lưu.");
                    return;
                }

                Task<Void> saveTask = new Task<>() {
                    @Override protected Void call() throws Exception {
                        int totalToSave = allChiTietToSave.size();
                        int processedCount = 0;
                        for (ChiTietTKB ct : allChiTietToSave) {
                            if (ct.getMaGV() == null || ct.getMaGV().isBlank()) {
                                System.err.println("LƯU THẤT BẠI (MaGV rỗng): Lớp " + ct.getMaLop() + ", Môn " + ct.getMaMH() + ", Thứ " + ct.getThu() + ", Tiết " + ct.getTiet());
                                failureCount[0]++;
                            } else if (xepTKBDAO.luuChiTietTKB(ct, newMaTKB)) {
                                successCount[0]++;
                            } else {
                                failureCount[0]++;
                            }
                            processedCount++;
                            updateMessage("Đang lưu: " + processedCount + "/" + totalToSave +
                                    ". Thành công: " + successCount[0] + ", Thất bại: " + failureCount[0]);
                            updateProgress(processedCount, totalToSave);
                        }
                        return null;
                    }

                    @Override protected void succeeded() {
                        Platform.runLater(() -> {
                            progressIndicator.setVisible(false);
                            statusLabel.setText("Lưu hoàn tất. Thành công: " + successCount[0] + ", Thất bại: " + failureCount[0]);
                            showAlert(Alert.AlertType.INFORMATION, "Lưu Thời Khóa Biểu Thành Công",
                                    "Thời khóa biểu mới đã được lưu với Mã TKB: " + newMaTKB +
                                            "\nSố tiết lưu thành công: " + successCount[0] +
                                            "\nSố tiết lưu thất bại: " + failureCount[0]);
                        });
                    }
                    @Override protected void failed() {
                        Platform.runLater(() -> {
                            progressIndicator.setVisible(false);
                            statusLabel.setText("Lỗi nghiêm trọng trong quá trình lưu TKB.");
                            Throwable ex = getException();
                            if (ex != null) ex.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Lỗi Lưu TKB",
                                    "Có lỗi xảy ra trong quá trình lưu chi tiết TKB." + (ex != null ? "\n" + ex.getMessage() : ""));
                        });
                    }
                };
                progressIndicator.progressProperty().bind(saveTask.progressProperty());
                statusLabel.textProperty().bind(saveTask.messageProperty());
                new Thread(saveTask).start();

            } else {
                progressIndicator.setVisible(false);
                statusLabel.setText("Lỗi: Không thể tạo bản ghi TKB mới trong CSDL.");
                showAlert(Alert.AlertType.ERROR, "Lỗi Tạo TKB", "Không thể tạo bản ghi Thời Khóa Biểu mới. Vui lòng kiểm tra kết nối CSDL hoặc quyền hạn.");
            }
        }
    }

    @FXML void handleXepLaiTKB(ActionEvent event) {
        if (selectedHocKy == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn Học Kỳ ở màn hình Chuẩn Bị trước.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn chạy lại thuật toán xếp TKB cho Học Kỳ: " + selectedHocKy.toString() + "?\n" + // Sử dụng toString()
                        "Tất cả các thay đổi hoặc TKB hiện tại (nếu chưa lưu) sẽ bị mất và được tạo lại từ đầu.",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Xác nhận Xếp Lại TKB");
        confirm.setHeaderText(null);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            infoLabel.setText("Chuẩn bị xếp lại TKB cho HK: " + selectedHocKy.toString() + "..."); // Sử dụng toString()
            tkbTableView.getItems().clear();
            maTKBMoiDuocTao = null;
            runSchedulingTask();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}
