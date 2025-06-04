package controllers.XepTKBTuDong;

import dao.XepTKBDAO;
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

import java.util.*;
import java.util.stream.Collectors;

public class XepTKBTuDongController {

    // ... (Các @FXML và khai báo biến thành viên giữ nguyên như code bạn gửi) ...
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
    private XepTKBDAO XepTKBDAO;

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
    private List<GiaoVien> giaoVienHopLe;

    // Key: MaLop + "-" + MaMH, Value: List<GiaoVien> đã được "khóa" cho môn/lớp đó
    // Sẽ được cập nhật nếu có sự thay đổi GV cho các lớp "linh hoạt"
    private Map<String, List<GiaoVien>> currentLockedTeachersForClassSubject;

    // Key: MaLop + "-" + MaMH, Value: MaGV (đại diện) từ TKB cơ sở
    private Map<String, String> pinnedTeacherForSubjectClassFromBaseTKB;

    private String maTKBMoiDuocTao;
    private static final int MAX_PERIODS_PER_DAY_SETTING = 5;
    private static final double ROW_HEIGHT = 30.0;
    private static final double HEADER_HEIGHT = 30.0;
    private static final int MAX_CONSECUTIVE_PERIODS = 2;
    private static final String PREFIX_GDQPAN = "GDQP";
    private static final String PREFIX_GDTC = "GDTC";
    private int soTietDaXepTuCoSo = 0; // Biến tạm để log chính xác hơn

    public XepTKBTuDongController() {
        xepTKBDAO = new XepTKBDAO();
        XepTKBDAO = new XepTKBDAO();
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
        if (this.selectedHocKy != null) {
            infoLabel.setText("Đang chuẩn bị dữ liệu cho Học Kỳ: " + this.selectedHocKy + "...");
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
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Thiếu dữ liệu", "Không đủ dữ liệu nền để xếp TKB."));
            return this.generatedTimetable;
        }

        // Phân loại (Lớp, Môn)
        List<ClassSubjectPair> priorityQueue = new ArrayList<>();
        List<ClassSubjectPair> flexibleQueue = new ArrayList<>();

        if (tatCaLop != null) {
            for (Lop lop : tatCaLop) {
                List<MonHocHoc> monHocCuaLop = phanCongMonHocChoLop.get(lop.getMaLop());
                if (monHocCuaLop != null) {
                    for (MonHocHoc mhh : monHocCuaLop) {
                        if (isPrioritySubject(lop, mhh)) {
                            priorityQueue.add(new ClassSubjectPair(lop, mhh));
                        } else {
                            flexibleQueue.add(new ClassSubjectPair(lop, mhh));
                        }
                    }
                }
            }
        }

        // Xếp Nhóm 1: Ưu tiên GV Cố Định
        System.out.println("--- BẮT ĐẦU XẾP NHÓM ƯU TIÊN (GV CỐ ĐỊNH) ---");
        for (ClassSubjectPair csp : priorityQueue) {
            scheduleSingleClassSubjectEntry(csp, true); // true = isPriority (không đổi GV)
        }

        // Xếp Nhóm 2: GV Linh Hoạt
        System.out.println("--- BẮT ĐẦU XẾP NHÓM LINH HOẠT (CÓ THỂ ĐỔI GV) ---");
        for (ClassSubjectPair csp : flexibleQueue) {
            scheduleSingleClassSubjectEntry(csp, false); // false = isFlexible (có thể đổi GV)
        }

        return this.generatedTimetable;
    }

    /**
     * Xác định một (Lớp, Môn) có yêu cầu GV đặc biệt (chỉ định cứng) hay không.
     */
    private boolean isPrioritySubject(Lop lop, MonHocHoc mhh) {
        String classSubjectKey = lop.getMaLop() + "-" + mhh.getMaMH();
        // Có cài đặt từ người dùng (classTeacherAssignmentsInput)
        if (classTeacherAssignmentsInput != null && classTeacherAssignmentsInput.containsKey(lop.getMaLop())) {
            Map<String, List<String>> assignmentsForClass = classTeacherAssignmentsInput.get(lop.getMaLop());
            if (assignmentsForClass != null && assignmentsForClass.containsKey(mhh.getMaMH())) {
                List<String> assignedGvIds = assignmentsForClass.get(mhh.getMaMH());
                // Nếu có danh sách GV cụ thể (không rỗng) -> là ưu tiên
                return assignedGvIds != null && !assignedGvIds.isEmpty();
            }
        }
        // Có ghim từ TKB cơ sở
        return pinnedTeacherForSubjectClassFromBaseTKB.containsKey(classSubjectKey);
    }

    /**
     * Xếp lịch cho một cặp (Lớp, Môn) cụ thể.
     * @param csp Cặp (Lớp, Môn).
     * @param isPriority True nếu đây là môn ưu tiên (GV cố định, không được đổi).
     * False nếu là môn linh hoạt (có thể đổi GV nếu cần).
     */
    private void scheduleSingleClassSubjectEntry(ClassSubjectPair csp, boolean isPriority) {
        Lop lop = csp.lop;
        MonHocHoc mhh = csp.mhh;

        Map<Integer, Integer> currentSoTietMoiThuForThisKhoi = soTietMoiThuTheoKhoiInput.get(lop.getKhoi());
        if (currentSoTietMoiThuForThisKhoi == null) {
            currentSoTietMoiThuForThisKhoi = new HashMap<>();
            for (int d = 2; d <= 7; d++) currentSoTietMoiThuForThisKhoi.put(d, MAX_PERIODS_PER_DAY_SETTING);
        }

        int soTietCanXepBanDau = mhh.getTongSoTiet();
        this.soTietDaXepTuCoSo = 0; // Reset cho mỗi môn/lớp
        if (generatedTimetable.containsKey(lop.getMaLop())) {
            this.soTietDaXepTuCoSo = (int) generatedTimetable.get(lop.getMaLop()).stream()
                    .filter(ct -> ct.getMaMH() != null && ct.getMaMH().equals(mhh.getMaMH()))
                    .count();
        }
        int soTietConLaiCanXep = soTietCanXepBanDau - this.soTietDaXepTuCoSo;

        if (soTietConLaiCanXep <= 0) {
            System.out.println("Đã đủ tiết (từ TKB cơ sở): " + lop.getMaLop() + " - " + mhh.getMaMH());
            return;
        }

        List<GiaoVien> initialTeam = getAndLockTeachingTeamForSubject(mhh, lop, Collections.emptyList(), isPriority);

        if (initialTeam.isEmpty()) {
            System.err.println("KHÔNG TÌM ĐƯỢC GV BAN ĐẦU: " + lop.getMaLop() + " - " + mhh.getMaMH() + ". Sẽ bị thiếu " + soTietConLaiCanXep + " tiết.");
            return;
        }

        int scheduledPeriods = scheduleAllPeriodsForSubject(mhh, lop, initialTeam, soTietConLaiCanXep, currentSoTietMoiThuForThisKhoi, true); // true = firstAttempt

        if (scheduledPeriods < soTietConLaiCanXep) { // Bị thiếu tiết
            System.out.println("Thiếu " + (soTietConLaiCanXep - scheduledPeriods) + " tiết cho " + lop.getMaLop() + " - " + mhh.getMaMH() + " với GV: " + initialTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));

            // Gỡ các tiết vừa xếp (nếu có) của môn này với initialTeam
            undoScheduledPeriodsForSubject(mhh, lop, initialTeam, scheduledPeriods);

            if (isPriority) {
                // Với môn ưu tiên, thử xếp lại với thứ tự duyệt slot khác (deterministic)
                System.out.println("Thử xếp lại (deterministic) cho môn ƯU TIÊN: " + lop.getMaLop() + " - " + mhh.getMaMH() + " với GV: " + initialTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));
                scheduledPeriods = scheduleAllPeriodsForSubject(mhh, lop, initialTeam, soTietConLaiCanXep, currentSoTietMoiThuForThisKhoi, false); // false = not firstAttempt (use deterministic slots)
                if (scheduledPeriods < soTietConLaiCanXep) {
                    System.err.println("VẪN THIẾU TIẾT (ƯU TIÊN): " + lop.getMaLop() + " - " + mhh.getMaMH() + ". Thiếu " + (soTietConLaiCanXep - scheduledPeriods) + " tiết.");
                }
            } else {
                // Với môn linh hoạt, thử đổi GV mới
                System.out.println("Thử ĐỔI GV cho môn LINH HOẠT: " + lop.getMaLop() + " - " + mhh.getMaMH());
                List<GiaoVien> excludedTeachers = new ArrayList<>(initialTeam);
                List<GiaoVien> newTeam = getAndLockTeachingTeamForSubject(mhh, lop, excludedTeachers, false); // false = not priority, can pick new

                if (!newTeam.isEmpty() && !listEqualsIgnoreOrder(newTeam, initialTeam)) {
                    System.out.println("Đã đổi GV cho " + lop.getMaLop() + " - " + mhh.getMaMH() + " từ " +
                            initialTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")) + " sang " +
                            newTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));
                    // Xếp lại từ đầu với GV mới
                    scheduledPeriods = scheduleAllPeriodsForSubject(mhh, lop, newTeam, soTietConLaiCanXep, currentSoTietMoiThuForThisKhoi, true); // firstAttempt with new teacher
                    if (scheduledPeriods < soTietConLaiCanXep) {
                        System.err.println("VẪN THIẾU TIẾT (SAU KHI ĐỔI GV): " + lop.getMaLop() + " - " + mhh.getMaMH() + ". Thiếu " + (soTietConLaiCanXep - scheduledPeriods) + " tiết.");
                    }
                } else {
                    System.err.println("KHÔNG TÌM ĐƯỢC GV MỚI hoặc GV mới giống GV cũ cho " + lop.getMaLop() + " - " + mhh.getMaMH() + ". Chấp nhận thiếu " + (soTietConLaiCanXep - scheduledPeriods) + " tiết.");
                    // Nếu muốn, có thể thử xếp lại số tiết đã xếp được với initialTeam (nếu scheduledPeriods > 0)
                    // và nếu chúng ta muốn đảm bảo ít nhất những gì đã xếp được vẫn còn đó.
                    // Tuy nhiên, logic hiện tại là gỡ hết rồi thử lại với newTeam, hoặc chấp nhận thiếu.
                    // Nếu không tìm được newTeam, và initialTeam đã xếp được một số tiết,
                    // có thể xem xét việc xếp lại những tiết đó của initialTeam.
                    // Hiện tại, nếu không đổi được GV, các tiết đã gỡ sẽ không được xếp lại tự động.
                }
            }
        }
    }
    private boolean listEqualsIgnoreOrder(List<GiaoVien> list1, List<GiaoVien> list2) {
        if (list1 == null && list2 == null) return true;
        if (list1 == null || list2 == null || list1.size() != list2.size()) return false;
        return new HashSet<>(list1.stream().map(GiaoVien::getMaGV).collect(Collectors.toList()))
                .equals(new HashSet<>(list2.stream().map(GiaoVien::getMaGV).collect(Collectors.toList())));
    }


    /**
     * Gỡ bỏ các tiết đã được xếp cho một môn học của một lớp với một nhóm giáo viên cụ thể.
     * @param mhh Môn học.
     * @param lop Lớp học.
     * @param teamToUndo Nhóm giáo viên đã dạy.
     * @param periodsToUndo Số lượng tiết cần gỡ (thường là số tiết đã xếp được trước đó).
     */
    private void undoScheduledPeriodsForSubject(MonHocHoc mhh, Lop lop, List<GiaoVien> teamToUndo, int periodsToUndo) {
        if (periodsToUndo <= 0 || !generatedTimetable.containsKey(lop.getMaLop()) || teamToUndo == null || teamToUndo.isEmpty()) {
            return;
        }
        System.out.println("Đang gỡ " + periodsToUndo + " tiết của môn " + mhh.getMaMH() + " lớp " + lop.getMaLop() + " do GV " + teamToUndo.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")) + " dạy.");

        List<ChiTietTKB> scheduleForClass = generatedTimetable.get(lop.getMaLop());
        List<ChiTietTKB> toRemoveFromSchedule = new ArrayList<>();
        int removedCount = 0;

        // Duyệt ngược để dễ xóa và chỉ xóa đúng số lượng periodsToUndo
        for (int i = scheduleForClass.size() - 1; i >= 0; i--) {
            if (removedCount >= periodsToUndo) break;
            ChiTietTKB ct = scheduleForClass.get(i);
            if (ct.getMaMH().equals(mhh.getMaMH())) {
                // Kiểm tra xem tiết này có phải do teamToUndo dạy không (dựa vào MaGV đại diện)
                if (ct.getMaGV() != null && ct.getMaGV().equals(teamToUndo.get(0).getMaGV())) {
                    // Giả định MaGV đại diện là đủ để xác định cho việc gỡ bỏ
                    toRemoveFromSchedule.add(ct);
                    removedCount++;
                }
            }
        }

        if (removedCount < periodsToUndo && periodsToUndo > 0) {
            System.out.println("CẢNH BÁO GỠ TIẾT: Chỉ tìm thấy " + removedCount + "/" + periodsToUndo + " tiết để gỡ cho " + mhh.getMaMH() + " lớp " + lop.getMaLop() + " với GV " + teamToUndo.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")));
        }


        for (ChiTietTKB ctToRemove : toRemoveFromSchedule) {
            scheduleForClass.remove(ctToRemove); // Xóa khỏi TKB đã tạo
            // Cập nhật slot bận
            lopBusySlots.get(lop.getMaLop())[ctToRemove.getThu()][ctToRemove.getTiet()] = false;
            for (GiaoVien gv : teamToUndo) { // Giải phóng slot cho tất cả GV trong nhóm
                if (teacherBusySlots.containsKey(gv.getMaGV())) {
                    teacherBusySlots.get(gv.getMaGV())[ctToRemove.getThu()][ctToRemove.getTiet()] = false;
                }
            }
        }
    }


    /**
     * Xác định và "khóa" giáo viên cho (Môn, Lớp).
     * @param excludedTeachers Danh sách GV cần loại trừ khi tìm kiếm (cho trường hợp đổi GV).
     * @param isPriorityAssignment True nếu đây là phân công ưu tiên (GV chỉ định cứng từ đầu).
     */
    private List<GiaoVien> getAndLockTeachingTeamForSubject(MonHocHoc mhh, Lop lop, List<GiaoVien> excludedTeachers, boolean isPriorityAssignment) {
        String classSubjectKey = lop.getMaLop() + "-" + mhh.getMaMH();

        // Nếu không phải là ưu tiên VÀ đã có GV khóa cho môn này (tức là đang thử đổi GV),
        // thì không cần tìm lại từ cài đặt/TKB cơ sở nữa, mà trực tiếp tìm GV mới.
        if (!isPriorityAssignment && currentLockedTeachersForClassSubject.containsKey(classSubjectKey) && !excludedTeachers.isEmpty()) {
            // Bỏ qua, sẽ đi thẳng tới logic tìm GV mới ở bước 3
        }
        // Nếu là phân công ưu tiên và đã có GV khóa từ trước, thì dùng luôn
        else if (isPriorityAssignment && currentLockedTeachersForClassSubject.containsKey(classSubjectKey)) {
            return currentLockedTeachersForClassSubject.get(classSubjectKey);
        }
        // Nếu là lần đầu (chưa có ai khóa) hoặc là ưu tiên nhưng chưa khóa -> tìm mới
        else {
            // 1. Ưu tiên cài đặt của người dùng
            if (classTeacherAssignmentsInput != null && classTeacherAssignmentsInput.containsKey(lop.getMaLop())) {
                Map<String, List<String>> assignmentsForClass = classTeacherAssignmentsInput.get(lop.getMaLop());
                if (assignmentsForClass != null && assignmentsForClass.containsKey(mhh.getMaMH())) {
                    List<String> assignedGvIds = assignmentsForClass.get(mhh.getMaMH());
                    if (assignedGvIds != null && !assignedGvIds.isEmpty()) {
                        List<GiaoVien> teamFromSettings = new ArrayList<>();
                        for (String maGV : assignedGvIds) {
                            if (excludedTeachers.stream().anyMatch(ex -> ex.getMaGV().equals(maGV))) continue;
                            GiaoVien gv = giaoVienHopLe.stream().filter(g -> g.getMaGV().equals(maGV)).findFirst().orElse(null);
                            if (gv != null) {
                                TeacherCustomSettings settings = teacherCustomSettingsInput.get(gv.getMaGV());
                                boolean canTeach = (settings == null) || settings.getTeachingPreferenceForSubject(mhh.getMaMH());
                                if (canTeach) teamFromSettings.add(gv);
                            }
                        }
                        if (!teamFromSettings.isEmpty()) {
                            currentLockedTeachersForClassSubject.put(classSubjectKey, teamFromSettings);
                            return teamFromSettings;
                        } else if (isPriorityAssignment) { // GV ưu tiên từ cài đặt không hợp lệ
                            System.err.println("GV ưu tiên từ cài đặt cho " + classSubjectKey + " không hợp lệ/đã bị loại trừ.");
                            currentLockedTeachersForClassSubject.put(classSubjectKey, Collections.emptyList());
                            return Collections.emptyList();
                        }
                        // Nếu không phải priority và không tìm được từ settings (do excluded), thử bước tiếp theo
                    }
                    // Nếu assignedGvIds rỗng => "Để thuật toán tự chọn" -> đi tiếp
                }
            }

            // 2. Ưu tiên TKB Cơ Sở
            if (pinnedTeacherForSubjectClassFromBaseTKB.containsKey(classSubjectKey)) {
                String pinnedMaGV = pinnedTeacherForSubjectClassFromBaseTKB.get(classSubjectKey);
                if (excludedTeachers.stream().noneMatch(ex -> ex.getMaGV().equals(pinnedMaGV))) {
                    GiaoVien gvFromBase = giaoVienHopLe.stream().filter(gv -> gv.getMaGV().equals(pinnedMaGV)).findFirst().orElse(null);
                    if (gvFromBase != null) {
                        TeacherCustomSettings settings = teacherCustomSettingsInput.get(gvFromBase.getMaGV());
                        boolean canTeach = (settings == null) || settings.getTeachingPreferenceForSubject(mhh.getMaMH());
                        if (canTeach) {
                            List<GiaoVien> teamFromBase = Collections.singletonList(gvFromBase);
                            currentLockedTeachersForClassSubject.put(classSubjectKey, teamFromBase);
                            return teamFromBase;
                        }
                    }
                }
            }
        }


        // 3. "Để thuật toán tự chọn" - Tìm GV (mới hoặc lần đầu)
        List<GiaoVien> potentialTeachers;
        boolean isSpecial = mhh.getMaMH().startsWith("GDDP") || mhh.getMaMH().startsWith("HDTNHN");
        boolean noStandardTCM = (mhh.getMaTCM() == null || mhh.getMaTCM().isBlank());

        List<String> finalExcludedMaGVs = excludedTeachers.stream().map(GiaoVien::getMaGV).collect(Collectors.toList());

        if (isSpecial && noStandardTCM) {
            potentialTeachers = giaoVienHopLe.stream()
                    .filter(gv -> !finalExcludedMaGVs.contains(gv.getMaGV()))
                    .filter(gv -> teacherCustomSettingsInput.get(gv.getMaGV()) != null &&
                            teacherCustomSettingsInput.get(gv.getMaGV()).getTeachingPreferenceForSubject(mhh.getMaMH()))
                    .filter(gv -> canTeacherTeachMore(gv, mhh)) // Chỉ chọn GV còn đủ tiết
                    .collect(Collectors.toList());
        } else {
            potentialTeachers = giaoVienHopLe.stream()
                    .filter(gv -> !finalExcludedMaGVs.contains(gv.getMaGV()))
                    .filter(gv -> gv.getMaTCM() != null && gv.getMaTCM().equals(mhh.getMaTCM()))
                    .filter(gv -> {
                        TeacherCustomSettings settings = teacherCustomSettingsInput.get(gv.getMaGV());
                        return (settings == null) || settings.getTeachingPreferenceForSubject(mhh.getMaMH());
                    })
                    .filter(gv -> canTeacherTeachMore(gv, mhh)) // Chỉ chọn GV còn đủ tiết
                    .collect(Collectors.toList());
        }

        if (!potentialTeachers.isEmpty()) {
            // Ưu tiên GVCN nếu có trong danh sách và chưa bị loại trừ
            GiaoVien selectedTeacher = potentialTeachers.stream()
                    .filter(gv -> lop.getGvcn() != null && gv.getMaGV().equals(lop.getGvcn()))
                    .findFirst()
                    .orElse(potentialTeachers.get(0)); // Lấy người đầu tiên nếu GVCN không có/không phù hợp
            List<GiaoVien> newTeam = Collections.singletonList(selectedTeacher);
            currentLockedTeachersForClassSubject.put(classSubjectKey, newTeam);
            return newTeam;
        }

        // Nếu không tìm được ai và đây là lần đầu tiên (chưa có ai khóa cho môn này)
        if (!currentLockedTeachersForClassSubject.containsKey(classSubjectKey)) {
            currentLockedTeachersForClassSubject.put(classSubjectKey, Collections.emptyList());
        }
        return Collections.emptyList(); // Không tìm được GV mới/ban đầu phù hợp
    }

    /**
     * Xếp tất cả các tiết cần thiết.
     * @param firstAttempt True nếu đây là lần thử đầu tiên (dùng slot ngẫu nhiên),
     * False nếu là lần thử lại (dùng slot xác định).
     * @return Số tiết đã xếp được.
     */
    private int scheduleAllPeriodsForSubject(MonHocHoc mhh, Lop lop, List<GiaoVien> lockedInTeam, int soTietCanXep, Map<Integer, Integer> currentSoTietMoiThuForThisKhoi, boolean firstAttempt) {
        int soTietDaXepThucTe = 0;
        if (lockedInTeam == null || lockedInTeam.isEmpty()) {
            System.err.println("Không có GV nào được khóa cho " + lop.getMaLop() + " - " + mhh.getMaMH() + ". Không thể xếp.");
            return 0;
        }

        for (int i = 0; i < soTietCanXep; i++) {
            if (tryScheduleSinglePeriodForSubject(mhh, lop, lockedInTeam, currentSoTietMoiThuForThisKhoi, firstAttempt)) {
                soTietDaXepThucTe++;
            } else {
                System.err.println("Không thể xếp tiết thứ " + (this.soTietDaXepTuCoSo + soTietDaXepThucTe + 1) +
                        " cho môn " + mhh.getTenMH() + " (" + mhh.getMaMH() + ") của lớp " + lop.getMaLop() +
                        " với GV: " + lockedInTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.joining(",")) +
                        (firstAttempt ? " (thử ngẫu nhiên)" : " (thử xác định)"));
            }
        }
        return soTietDaXepThucTe;
    }

    private boolean tryScheduleSinglePeriodForSubject(MonHocHoc mhh, Lop lop, List<GiaoVien> lockedInTeam, Map<Integer, Integer> currentSoTietMoiThuForThisKhoi, boolean useRandomSlots) {
        List<Integer> thuList = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7));
        if (useRandomSlots) {
            Collections.shuffle(thuList);
        }

        for (int thu : thuList) {
            int maxTietHomNayTheoCaiDat = Math.min(currentSoTietMoiThuForThisKhoi.getOrDefault(thu, MAX_PERIODS_PER_DAY_SETTING), MAX_PERIODS_PER_DAY_SETTING);
            List<Integer> tietList = new ArrayList<>();
            for (int t = 1; t <= maxTietHomNayTheoCaiDat; t++) tietList.add(t);
            if (useRandomSlots) {
                Collections.shuffle(tietList);
            }

            for (int tiet : tietList) {
                boolean classSlotAvailable = !lopBusySlots.get(lop.getMaLop())[thu][tiet];
                boolean allLockedInTeachersAvailable = true;
                for (GiaoVien gv : lockedInTeam) {
                    if (!teacherBusySlots.containsKey(gv.getMaGV()) || teacherBusySlots.get(gv.getMaGV())[thu][tiet]) {
                        allLockedInTeachersAvailable = false;
                        break;
                    }
                }

                if (classSlotAvailable && allLockedInTeachersAvailable) {
                    List<String> maGVTeamDangXep = lockedInTeam.stream().map(GiaoVien::getMaGV).collect(Collectors.toList());
                    if (checkSpecificConstraints(mhh, lop, thu, tiet, maGVTeamDangXep)) {
                        String hoTenGVDisplay = lockedInTeam.stream()
                                .map(gv -> (gv.getHoGV() + " " + gv.getTenGV()).trim())
                                .filter(name -> !name.isEmpty())
                                .collect(Collectors.joining(", "));
                        if (hoTenGVDisplay.isEmpty()) hoTenGVDisplay = "Nhóm GV (" + lockedInTeam.size() + ")";
                        String maGVForChiTiet = lockedInTeam.get(0).getMaGV();

                        ChiTietTKB chiTietMoi = ChiTietTKB.taoChoXepLichTuDong(
                                thu, tiet, mhh.getTenMH(), lop.getMaLop(),
                                hoTenGVDisplay, maGVForChiTiet, mhh.getMaMH(), 0);

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
        }
        return false;
    }

    private static class ClassSubjectPair {
        Lop lop;
        MonHocHoc mhh;
        ClassSubjectPair(Lop lop, MonHocHoc mhh) {
            this.lop = lop;
            this.mhh = mhh;
        }
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

        if (tatCaLop != null) {
            for (Lop lop : tatCaLop) {
                lopBusySlots.put(lop.getMaLop(), new boolean[8][MAX_PERIODS_PER_DAY_SETTING + 1]);
            }
        }
        for (GiaoVien gv : giaoVienHopLe) {
            teacherBusySlots.put(gv.getMaGV(), new boolean[8][MAX_PERIODS_PER_DAY_SETTING + 1]);
        }

        if (tkbCoSo != null) {
            chiTietTkbCoSo = xepTKBDAO.getChiTietTKBByMaTKB(tkbCoSo.getMaTKB());
            if (chiTietTkbCoSo != null) {
                for (ChiTietTKB ctCoSo : chiTietTkbCoSo) {
                    if (ctCoSo.getThu() >= 2 && ctCoSo.getThu() <= 7 &&
                            ctCoSo.getTiet() >= 1 && ctCoSo.getTiet() <= MAX_PERIODS_PER_DAY_SETTING) {
                        generatedTimetable.computeIfAbsent(ctCoSo.getMaLop(), k -> new ArrayList<>()).add(ctCoSo);
                        if (lopBusySlots.containsKey(ctCoSo.getMaLop())) {
                            lopBusySlots.get(ctCoSo.getMaLop())[ctCoSo.getThu()][ctCoSo.getTiet()] = true;
                        }
                        if (ctCoSo.getMaGV() != null && giaoVienHopLe.stream().anyMatch(gv -> gv.getMaGV().equals(ctCoSo.getMaGV()))) {
                            if (teacherBusySlots.containsKey(ctCoSo.getMaGV())) {
                                teacherBusySlots.get(ctCoSo.getMaGV())[ctCoSo.getThu()][ctCoSo.getTiet()] = true;
                            }
                            String pinnedKey = ctCoSo.getMaLop() + "-" + ctCoSo.getMaMH();
                            pinnedTeacherForSubjectClassFromBaseTKB.put(pinnedKey, ctCoSo.getMaGV());
                        }
                    }
                }
            }
        } else {
            chiTietTkbCoSo = null;
        }
    }

    private boolean isBaseDataSufficient() {
        return tatCaLop != null && !tatCaLop.isEmpty() &&
                phanCongMonHocChoLop != null &&
                tatCaGiaoVienDayDu != null &&
                selectedHocKy != null &&
                soTietMoiThuTheoKhoiInput != null &&
                teacherCustomSettingsInput != null &&
                classTeacherAssignmentsInput != null;
    }


    private boolean canTeacherTeachMore(GiaoVien gv, MonHocHoc mhhToPotentiallyAssign) {
        if (gv.getSoTietQuyDinh() == null || gv.getSoTietQuyDinh() == 0) return true;
        long soTietDaDayHienTai = countCurrentPeriodsForTeacher(gv);
        int soTietChucVu = xepTKBDAO.getSoTietChucVuDaSuDung(gv.getMaGV(), selectedHocKy.getMaHK());
        // Khi chọn GV ban đầu, kiểm tra xem có thể nhận thêm TOÀN BỘ số tiết của môn này không
        return (soTietDaDayHienTai + soTietChucVu + mhhToPotentiallyAssign.getTongSoTiet()) <= gv.getSoTietQuyDinh();
    }

    private long countCurrentPeriodsForTeacher(GiaoVien gv) {
        return generatedTimetable.values().stream()
                .flatMap(List::stream)
                .filter(ct -> {
                    if (ct.getMaGV() != null && ct.getMaGV().equals(gv.getMaGV())) return true;
                    if (ct.getHoTenGV() != null && ct.getHoTenGV().contains(gv.getTenGV())) { // Cần cẩn thận với tên trùng
                        return true;
                    }
                    return false;
                })
                .count();
    }
    private boolean checkSpecificConstraints(MonHocHoc mhh, Lop lop, int thu, int tiet, List<String> maGVTeamDangXep) {
        List<ChiTietTKB> tkbCuaLopTrongNgay = generatedTimetable.getOrDefault(lop.getMaLop(), Collections.emptyList())
                .stream().filter(ct -> ct.getThu() == thu).collect(Collectors.toList());
        String maMHCurrentUpper = mhh.getMaMH().toUpperCase();
        if (maMHCurrentUpper.startsWith(PREFIX_GDQPAN) || maMHCurrentUpper.startsWith(PREFIX_GDTC)) {
            String otherConflictPrefix = maMHCurrentUpper.startsWith(PREFIX_GDQPAN) ? PREFIX_GDTC : PREFIX_GDQPAN;
            if (tkbCuaLopTrongNgay.stream().anyMatch(ct -> ct.getMaMH() != null && ct.getMaMH().toUpperCase().startsWith(otherConflictPrefix))) {
                return false;
            }
        }
        if (maGVTeamDangXep != null && !maGVTeamDangXep.isEmpty()) {
            for (String currentMaGVTrongNhom : maGVTeamDangXep) {
                int consecutiveCountForThisGV = 0;
                for (int i = 1; i < MAX_CONSECUTIVE_PERIODS; i++) {
                    int tietTruocDo = tiet - i;
                    if (tietTruocDo < 1) break;
                    boolean match = false;
                    for (ChiTietTKB ctDaXep : tkbCuaLopTrongNgay) {
                        if (ctDaXep.getTiet() == tietTruocDo) {
                            boolean isCurrentGVInvolved = false;
                            if (ctDaXep.getMaGV() != null && ctDaXep.getMaGV().equals(currentMaGVTrongNhom)) {
                                isCurrentGVInvolved = true;
                            } else if (ctDaXep.getHoTenGV() != null) {
                                GiaoVien gvObj = tatCaGiaoVienDayDu.stream().filter(g -> g.getMaGV().equals(currentMaGVTrongNhom)).findFirst().orElse(null);
                                if (gvObj != null && gvObj.getTenGV() != null && ctDaXep.getHoTenGV().contains(gvObj.getTenGV())) {
                                    isCurrentGVInvolved = true;
                                }
                            }
                            if (ctDaXep.getMaMH() != null && ctDaXep.getMaMH().equals(mhh.getMaMH()) && isCurrentGVInvolved) {
                                consecutiveCountForThisGV++;
                                match = true;
                            }
                            break;
                        }
                    }
                    if (!match) break;
                }
                if (consecutiveCountForThisGV >= MAX_CONSECUTIVE_PERIODS) return false;
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
    }
    private void loadBaseData() {
        tatCaLop = xepTKBDAO.getDanhSachLop();
        if (selectedHocKy != null) {
            phanCongMonHocChoLop = xepTKBDAO.getPhanCongMonHocChoTatCaLop(selectedHocKy.getMaHK());
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
                        }
                    }
                }
            }
        } else {
            phanCongMonHocChoLop = new HashMap<>();
        }
        tatCaGiaoVienDayDu = xepTKBDAO.getDanhSachGiaoVienDayDu();
        List<MonHoc> allMonHocDB = XepTKBDAO.getAllMonHoc();
        danhMucMonHoc.clear();
        if (allMonHocDB != null) allMonHocDB.forEach(mh -> danhMucMonHoc.put(mh.getMaMH(), mh));
        CaiDatGVController.SPECIAL_SUBJECTS_INFO.forEach(info -> danhMucMonHoc.putIfAbsent(info.maMH, new MonHoc(info.maMH, info.tenMH, null, null)));
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
        // SỬA ĐỔI Ở ĐÂY: Tạo biến final cho cellFactory để tránh lỗi lambda
        final Callback<TableColumn<TietHocData, ChiTietTKB>, TableCell<TietHocData, ChiTietTKB>> cellFactory = param -> new TableCell<>() {
            @Override
            protected void updateItem(ChiTietTKB item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getMaMH() == null) {
                    setText(null); setTooltip(null); setStyle("");
                } else {
                    String tenMH = item.getTenMonHoc();
                    if ((tenMH == null || tenMH.isBlank()) && danhMucMonHoc.containsKey(item.getMaMH())) {
                        tenMH = danhMucMonHoc.get(item.getMaMH()).getTenMH();
                    }
                    if (tenMH == null || tenMH.isBlank()) tenMH = "MH: " + item.getMaMH();

                    String displayText = tenMH;
                    String gvDisplay = item.getHoTenGV();

                    if (gvDisplay != null && !gvDisplay.isBlank() && !gvDisplay.equalsIgnoreCase("(Chưa phân công GV)")) {
                        displayText += " - " + gvDisplay;
                    } else if (item.getMaGV() != null && !item.getMaGV().isBlank() && (gvDisplay == null || gvDisplay.isBlank())) {
                        GiaoVien gv = tatCaGiaoVienDayDu.stream().filter(g -> g.getMaGV().equals(item.getMaGV())).findFirst().orElse(null);
                        if (gv != null) displayText += " - " + (gv.getHoGV() + " " + gv.getTenGV()).trim();
                        else displayText += " - GV:" + item.getMaGV();
                    }
                    setText(displayText);
                    setTooltip(new Tooltip(displayText));
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
            System.out.println("Không có danh sách lớp để tạo menu.");
            return;
        }

        Comparator<Lop> lopComparator = Comparator
                .comparing((Lop lop) -> lop.getKhoi() != null ? lop.getKhoi() : "", Comparator.nullsLast(String::compareTo))
                .thenComparing((Lop lop) -> {
                    String tenLop = (lop.getTenLop() != null && !lop.getTenLop().isBlank()) ? lop.getTenLop() : lop.getMaLop();
                    return tenLop.replaceAll("\\d+$", ""); // Phần chữ
                }, Comparator.nullsLast(String::compareToIgnoreCase))
                .thenComparingInt((Lop lop) -> {
                    String tenLop = (lop.getTenLop() != null && !lop.getTenLop().isBlank()) ? lop.getTenLop() : lop.getMaLop();
                    String prefix = tenLop.replaceAll("\\d+$", "");
                    String suffixStr = tenLop.substring(prefix.length());
                    try {
                        return suffixStr.isEmpty() ? 0 : Integer.parseInt(suffixStr);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                });

        Map<String, List<Lop>> lopTheoKhoi = tatCaLop.stream()
                .sorted(lopComparator)
                .collect(Collectors.groupingBy(
                        lop -> lop.getKhoi() != null ? lop.getKhoi() : "Khác",
                        TreeMap::new,
                        Collectors.toList()
                ));

        lopTheoKhoi.forEach((khoi, listLop) -> {
            Menu menuForThisKhoi = null; // Sử dụng biến cục bộ trong lambda
            if ("10".equals(khoi)) {
                menuForThisKhoi = menuKhoi10;
            } else if ("11".equals(khoi)) {
                menuForThisKhoi = menuKhoi11;
            } else if ("12".equals(khoi)) {
                menuForThisKhoi = menuKhoi12;
            }

            if (menuForThisKhoi != null) {
                // Tạo biến final để sử dụng trong lambda của listLop.forEach
                final Menu finalMenuForThisKhoi = menuForThisKhoi;
                listLop.forEach(lop -> {
                    String text = (lop.getTenLop() != null && !lop.getTenLop().isBlank()) ? lop.getTenLop() : lop.getMaLop();
                    if (!text.equals(lop.getMaLop())) { // Thêm mã lớp nếu tên lớp khác mã lớp
                        text += " (" + lop.getMaLop() + ")";
                    }
                    MenuItem item = new MenuItem(text);
                    item.setOnAction(e -> displayTKBForLop(lop.getMaLop()));
                    finalMenuForThisKhoi.getItems().add(item);
                });
            }
        });
    }

    private void displayTKBForLop(String maLop) {
        ObservableList<TietHocData> tkbDataList = FXCollections.observableArrayList();
        Lop currentLop = tatCaLop.stream().filter(l -> l.getMaLop().equals(maLop)).findFirst().orElse(null);
        String khoi = (currentLop != null && currentLop.getKhoi() != null) ? currentLop.getKhoi() : "";

        Map<Integer, Integer> soTietNgay = soTietMoiThuTheoKhoiInput.getOrDefault(khoi, Collections.emptyMap());
        int maxTietDisplay = MAX_PERIODS_PER_DAY_SETTING;
        if (!soTietNgay.isEmpty()) {
            maxTietDisplay = Math.min(soTietNgay.values().stream().max(Integer::compareTo).orElse(MAX_PERIODS_PER_DAY_SETTING), MAX_PERIODS_PER_DAY_SETTING);
        }
        for (int i = 1; i <= maxTietDisplay; i++) tkbDataList.add(new TietHocData("Tiết " + i));

        List<ChiTietTKB> chiTiets = generatedTimetable.get(maLop);
        if (chiTiets != null) {
            for (ChiTietTKB ct : chiTiets) {
                if (ct.getTiet() > 0 && ct.getTiet() <= maxTietDisplay) {
                    TietHocData row = tkbDataList.get(ct.getTiet() - 1);
                    int limitTietNgay = Math.min(soTietNgay.getOrDefault(ct.getThu(), maxTietDisplay), maxTietDisplay);
                    if (ct.getTiet() <= limitTietNgay) {
                        switch (ct.getThu()) {
                            case 2: row.setThu2(ct); break;
                            case 3: row.setThu3(ct); break;
                            case 4: row.setThu4(ct); break;
                            case 5: row.setThu5(ct); break;
                            case 6: row.setThu6(ct); break;
                            case 7: row.setThu7(ct); break;
                        }
                    }
                }
            }
        }
        tkbTableView.setItems(tkbDataList);
        double newHeight = HEADER_HEIGHT + (maxTietDisplay * ROW_HEIGHT) + 2.0;
        tkbTableView.setPrefHeight(newHeight); tkbTableView.setMinHeight(newHeight); tkbTableView.setMaxHeight(newHeight);
        tkbTableView.refresh();

        String tenLopDisplay = maLop;
        if (currentLop != null && currentLop.getTenLop() != null && !currentLop.getTenLop().isBlank()) {
            tenLopDisplay = currentLop.getTenLop() + " (" + maLop + ")";
        }
        lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP " + tenLopDisplay.toUpperCase());
        infoLabel.setText("Hiển thị TKB lớp: " + maLop + (selectedHocKy != null ? " (HK: " + selectedHocKy + ")" : ""));
    }

    private void displayEmptyTKB() {
        ObservableList<TietHocData> list = FXCollections.observableArrayList();
        for (int i = 1; i <= MAX_PERIODS_PER_DAY_SETTING; i++) list.add(new TietHocData("Tiết " + i));
        tkbTableView.setItems(list);
        double height = HEADER_HEIGHT + (MAX_PERIODS_PER_DAY_SETTING * ROW_HEIGHT) + 2.0;
        tkbTableView.setPrefHeight(height); tkbTableView.setMinHeight(height); tkbTableView.setMaxHeight(height);
        tkbTableView.refresh();
        if (lopTKBTitleLabel != null) lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP");
    }

    @FXML void handleXemTatCaLop(ActionEvent event) {
        infoLabel.setText("Chức năng xem tổng quan toàn trường chưa được triển khai.");
        displayEmptyTKB();
        if (lopTKBTitleLabel != null) {
            lopTKBTitleLabel.setText("THỜI KHÓA BIỂU TOÀN TRƯỜNG (TỔNG QUAN)");
        }
    }

    @FXML void handleLuuThoiKhoaBieu(ActionEvent event) {
        if (generatedTimetable == null || generatedTimetable.isEmpty()) { showAlert(Alert.AlertType.WARNING, "Không có dữ liệu", "Chưa có TKB để lưu."); return; }
        if (selectedHocKy == null) { showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Không xác định được học kỳ."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn lưu TKB này?\nMã TKB mới sẽ được tạo.", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Xác nhận lưu"); confirm.setHeaderText(null);
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.YES) {
            progressIndicator.setVisible(true); statusLabel.setText("Đang lưu TKB...");
            String nguoiTao = "ADMIN";
            String buoi = (tkbCoSo != null && tkbCoSo.getBuoi() != null && !tkbCoSo.getBuoi().isBlank()) ? tkbCoSo.getBuoi() : "CHUNG";
            String newMaTKB = xepTKBDAO.taoThoiKhoaBieuMoi(selectedHocKy.getMaHK(), buoi, nguoiTao);

            if (newMaTKB != null) {
                maTKBMoiDuocTao = newMaTKB;
                final int[] success = {0}; // final for lambda
                final int[] fail = {0};    // final for lambda
                List<ChiTietTKB> toSave = generatedTimetable.values().stream().flatMap(List::stream).collect(Collectors.toList());
                List<ChiTietTKB> validToSave = new ArrayList<>();

                for (ChiTietTKB ct : toSave) {
                    if (ct.getMaMH() == null || ct.getMaMH().isBlank() || !danhMucMonHoc.containsKey(ct.getMaMH())) {
                        System.err.println("LƯU THẤT BẠI (MaMH không hợp lệ/không tồn tại trong danhMucMonHoc đã load): " + ct.getMaMH() + " cho Lớp " + ct.getMaLop());
                        fail[0]++;
                    } else if (ct.getMaGV() == null || ct.getMaGV().isBlank()){
                        System.err.println("LƯU THẤT BẠI (MaGV rỗng không hợp lệ): " + ct.getMaMH() + " cho Lớp " + ct.getMaLop());
                        fail[0]++;
                    }
                    else {
                        validToSave.add(ct);
                    }
                }
                if (validToSave.isEmpty() && !toSave.isEmpty()){
                    progressIndicator.setVisible(false);
                    statusLabel.setText("Lưu thất bại hoàn toàn do MaMH/MaGV không hợp lệ.");
                    showAlert(Alert.AlertType.ERROR, "Lỗi Lưu", "Không có chi tiết TKB nào hợp lệ để lưu.");
                    return;
                }

                Task<Void> saveTask = new Task<>() {
                    @Override protected Void call() throws Exception {
                        int total = validToSave.size(); int done = 0;
                        int taskFailCount = 0; // Local fail count for this task
                        for (ChiTietTKB ct : validToSave) {
                            if (xepTKBDAO.luuChiTietTKB(ct, newMaTKB)) {
                                success[0]++;
                            } else {
                                taskFailCount++;
                            }
                            done++;
                            // Use local taskFailCount for immediate UI update, add to outer fail[0] at the end
                            updateMessage("Đang lưu: " + done + "/" + total + ". Thành công: " + success[0] + ", Thất bại: " + (fail[0] + taskFailCount));
                            updateProgress(done, total);
                        }
                        fail[0] += taskFailCount; // Update the outer fail count
                        return null;
                    }
                    @Override protected void succeeded() {
                        Platform.runLater(() -> {
                            progressIndicator.setVisible(false);
                            statusLabel.setText("Lưu hoàn tất. Thành công: " + success[0] + ", Thất bại: " + fail[0]);
                            showAlert(Alert.AlertType.INFORMATION, "Lưu TKB", "Mã TKB mới: " + newMaTKB + "\nThành công: " + success[0] + ", Thất bại: " + fail[0]);
                        });
                    }
                    @Override protected void failed() {
                        Platform.runLater(()->{
                            progressIndicator.setVisible(false);
                            statusLabel.setText("Lỗi khi đang lưu TKB (Task failed).");
                            Throwable ex = getException();
                            if (ex != null) ex.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Lỗi Lưu TKB", "Có lỗi xảy ra trong quá trình lưu chi tiết TKB." + (ex != null ? "\n" + ex.getMessage() : ""));
                        });
                    }
                };
                progressIndicator.progressProperty().bind(saveTask.progressProperty());
                statusLabel.textProperty().bind(saveTask.messageProperty());
                new Thread(saveTask).start();
            } else {
                progressIndicator.setVisible(false); statusLabel.setText("Lỗi tạo TKB mới.");
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo bản ghi TKB mới trong CSDL.");
            }
        }
    }
    @FXML void handleXepLaiTKB(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Chạy lại thuật toán xếp TKB?\nThay đổi chưa lưu sẽ mất.", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Xác nhận"); confirm.setHeaderText(null);
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.YES) {
            if (selectedHocKy == null) { showAlert(Alert.AlertType.ERROR, "Lỗi", "Chưa chọn học kỳ."); return; }
            infoLabel.setText("Chuẩn bị xếp lại TKB cho HK: " + selectedHocKy + "...");
            tkbTableView.getItems().clear();
            maTKBMoiDuocTao = null;
            runSchedulingTask();
        }
    }
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
