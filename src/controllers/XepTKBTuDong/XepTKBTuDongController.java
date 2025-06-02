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
    private List<GiaoVien> excludedGiaoVienListInput;
    private Map<String, Map<Integer, Integer>> soTietMoiThuTheoKhoiInput;
    private ThoiKhoaBieu tkbCoSo;
    private List<ChiTietTKB> chiTietTkbCoSo;

    private List<Lop> tatCaLop;
    private Map<String, List<MonHocHoc>> phanCongMonHocChoLop;
    private List<GiaoVien> tatCaGiaoVienDayDu;
    private Map<String, MonHoc> danhMucMonHoc;

    private Map<String, Map<String, String>> preAssignedTeachersForFlexibleSubjectsInput;
    private final List<String> MA_MON_HOC_PREFIX_LINH_HOAT = Arrays.asList("TIN", "GDTC");

    // Các biến thành viên được sử dụng và cập nhật trong quá trình xếp TKB
    private Map<String, List<ChiTietTKB>> generatedTimetable; // TKB được tạo ra
    private Map<String, boolean[][]> lopBusySlots; // [MaLop][Thu][Tiet] -> true nếu bận
    private Map<String, boolean[][]> teacherBusySlots; // [MaGV][Thu][Tiet] -> true nếu bận
    private List<GiaoVien> giaoVienHopLe; // Danh sách GV có thể tham gia xếp TKB
    private Map<String, String> pinnedTeacherForSubjectClass; // Ghim GV cho một môn của một lớp: Key "MaLop-MaMH", Value "MaGV"


    private String maTKBMoiDuocTao;


    private static final int SO_TIET_HIEN_THI_MAC_DINH = 5; // Số tiết tối đa hiển thị trên bảng, cũng là giới hạn của mảng busy slots
    private static final double ROW_HEIGHT = 30.0;
    private static final double HEADER_HEIGHT = 30.0;
    private static final int MAX_CONSECUTIVE_PERIODS = 2; // Số tiết liên tục tối đa cho phép của một môn

    private static final String PREFIX_GDQPAN = "GDQP";
    private static final String PREFIX_GDTC = "GDTC";


    public XepTKBTuDongController() {
        xepTKBDAO = new XepTKBDAO();
        thoiKhoaBieuDAO = new ThoiKhoaBieuDAO();
        danhMucMonHoc = new HashMap<>();
    }

    public void initData(HocKy selectedHocKy, ThoiKhoaBieu tkbCoSo,
                         List<GiaoVien> excludedGiaoVienList,
                         Map<String, Map<Integer, Integer>> soTietMoiThuTheoKhoi,
                         Map<String, Map<String, String>> preAssignedTeachers) {
        this.selectedHocKy = selectedHocKy;
        this.tkbCoSo = tkbCoSo;
        this.excludedGiaoVienListInput = excludedGiaoVienList;
        this.soTietMoiThuTheoKhoiInput = soTietMoiThuTheoKhoi;
        this.preAssignedTeachersForFlexibleSubjectsInput = preAssignedTeachers;

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
                Platform.runLater(() -> {
                    progressIndicator.setVisible(true);
                    statusLabel.setText("Đang tải dữ liệu nền...");
                });
                loadBaseData();

                if (tkbCoSo != null) {
                    chiTietTkbCoSo = xepTKBDAO.getChiTietTKBByMaTKB(tkbCoSo.getMaTKB());
                    Platform.runLater(() -> statusLabel.setText("Đã tải TKB cơ sở. Bắt đầu xếp TKB..."));
                } else {
                    chiTietTkbCoSo = null;
                    Platform.runLater(() -> statusLabel.setText("Không có TKB cơ sở. Bắt đầu xếp TKB mới..."));
                }

                generatedTimetable = scheduleTimetable();

                Platform.runLater(() -> {
                    statusLabel.setText("Đã xếp xong! Xây dựng Menu chọn lớp...");
                    setupLopMenu();
                    if (tatCaLop != null && !tatCaLop.isEmpty()) {
                        String maLopDauTien = tatCaLop.getFirst().getMaLop();
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
            @Override
            protected void succeeded() {
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
                    displayEmptyTKB();
                });
            }
        };
        new Thread(schedulingTask).start();
    }

    private Map<String, List<ChiTietTKB>> scheduleTimetable() {
        if (!isBaseDataSufficient()) {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Thiếu dữ liệu", "Không đủ dữ liệu nền hoặc cài đặt số tiết để xếp TKB."));
            return new HashMap<>();
        }
        initializeSchedulingState();
        if (tatCaLop != null) {
            for (Lop lop : tatCaLop) {
                scheduleSubjectsForClass(lop);
            }
        }
        return this.generatedTimetable;
    }

    private boolean isBaseDataSufficient() {
        return tatCaLop != null &&
                phanCongMonHocChoLop != null &&
                tatCaGiaoVienDayDu != null &&
                selectedHocKy != null &&
                soTietMoiThuTheoKhoiInput != null;
    }

    private void initializeSchedulingState() {
        this.generatedTimetable = new HashMap<>();
        this.lopBusySlots = new HashMap<>();
        this.teacherBusySlots = new HashMap<>();
        this.pinnedTeacherForSubjectClass = new HashMap<>();

        this.giaoVienHopLe = tatCaGiaoVienDayDu.stream()
                .filter(gv -> excludedGiaoVienListInput == null || excludedGiaoVienListInput.stream().noneMatch(ex -> ex.getMaGV().equals(gv.getMaGV())))
                .toList();

        int maxPeriodsArrayBound = SO_TIET_HIEN_THI_MAC_DINH;

        if (tatCaLop != null) {
            for (Lop lop : tatCaLop) {
                lopBusySlots.put(lop.getMaLop(), new boolean[8][maxPeriodsArrayBound + 1]);
            }
        }
        for (GiaoVien gv : giaoVienHopLe) {
            teacherBusySlots.put(gv.getMaGV(), new boolean[8][maxPeriodsArrayBound + 1]);
        }

        if (chiTietTkbCoSo != null) {
            for (ChiTietTKB ctCoSo : chiTietTkbCoSo) {
                if (ctCoSo.getThu() >= 2 && ctCoSo.getThu() <= 7 && ctCoSo.getTiet() >= 1 && ctCoSo.getTiet() <= maxPeriodsArrayBound) {
                    if (lopBusySlots.containsKey(ctCoSo.getMaLop())) {
                        lopBusySlots.get(ctCoSo.getMaLop())[ctCoSo.getThu()][ctCoSo.getTiet()] = true;
                    }
                    if (ctCoSo.getMaGV() != null && teacherBusySlots.containsKey(ctCoSo.getMaGV())) {
                        teacherBusySlots.get(ctCoSo.getMaGV())[ctCoSo.getThu()][ctCoSo.getTiet()] = true;
                    }
                    generatedTimetable.computeIfAbsent(ctCoSo.getMaLop(), k -> new ArrayList<>()).add(ctCoSo);
                    if (ctCoSo.getMaLop() != null && ctCoSo.getMaMH() != null && ctCoSo.getMaGV() != null) {
                        String pinnedKey = ctCoSo.getMaLop() + "-" + ctCoSo.getMaMH();
                        pinnedTeacherForSubjectClass.put(pinnedKey, ctCoSo.getMaGV());
                    }
                }
            }
        }
    }

    private void scheduleSubjectsForClass(Lop lop) {
        List<MonHocHoc> monHocCuaLop = phanCongMonHocChoLop.get(lop.getMaLop());
        if (monHocCuaLop == null) return;

        Map<Integer, Integer> currentSoTietMoiThuForThisKhoi = soTietMoiThuTheoKhoiInput.get(lop.getKhoi());
        if (currentSoTietMoiThuForThisKhoi == null) {
            System.err.println("Cảnh báo: Lớp " + lop.getMaLop() + " thuộc khối " + lop.getKhoi() + " không có cài đặt số tiết riêng. Dùng mặc định " + SO_TIET_HIEN_THI_MAC_DINH + " tiết/ngày.");
            currentSoTietMoiThuForThisKhoi = new HashMap<>();
            for (int d = 2; d <= 7; d++) currentSoTietMoiThuForThisKhoi.put(d, SO_TIET_HIEN_THI_MAC_DINH);
        }

        for (MonHocHoc mhh : monHocCuaLop) {
            int soTietCanXep = mhh.getTongSoTiet();
            List<ChiTietTKB> tkbCuaLopHienTai = generatedTimetable.getOrDefault(lop.getMaLop(), Collections.emptyList());

            if (chiTietTkbCoSo != null) {
                long soTietDaXepTuCoSo = tkbCuaLopHienTai.stream()
                        .filter(ct -> ct.getMaMH() != null && ct.getMaMH().equals(mhh.getMaMH()))
                        .count();
                soTietCanXep -= (int) soTietDaXepTuCoSo;
            }

            if (soTietCanXep <= 0) continue;

            GiaoVien gvFixedForThisSubjectClass = determineTeacherForSubject(mhh, lop);
            if (gvFixedForThisSubjectClass == null) {
                continue;
            }
            scheduleAllPeriodsForSubject(mhh, lop, gvFixedForThisSubjectClass, soTietCanXep, currentSoTietMoiThuForThisKhoi);
        }
    }

    private GiaoVien determineTeacherForSubject(MonHocHoc mhh, Lop lop) {
        String pinnedTeacherKey = lop.getMaLop() + "-" + mhh.getMaMH();
        GiaoVien gvFixed = null;

        String preSelectedMaGV = null;
        if (preAssignedTeachersForFlexibleSubjectsInput != null &&
                preAssignedTeachersForFlexibleSubjectsInput.containsKey(lop.getMaLop()) &&
                preAssignedTeachersForFlexibleSubjectsInput.get(lop.getMaLop()).containsKey(mhh.getMaMH())) {
            preSelectedMaGV = preAssignedTeachersForFlexibleSubjectsInput.get(lop.getMaLop()).get(mhh.getMaMH());
        }

        if (preSelectedMaGV != null) {
            final String finalPreSelectedMaGV = preSelectedMaGV;
            gvFixed = giaoVienHopLe.stream().filter(gv -> gv.getMaGV().equals(finalPreSelectedMaGV)).findFirst().orElse(null);
            if (gvFixed != null) {
                // THAY ĐỔI: Kiểm tra số tiết quy định KHI chọn GV được gán trước
                if (canTeacherTeachMore(gvFixed) || isTeacherAlreadyTeachingSubjectInClass(gvFixed, mhh, lop)) {
                    pinnedTeacherForSubjectClass.put(pinnedTeacherKey, gvFixed.getMaGV());
                } else {
                    System.err.println("CẢNH BÁO: GV " + finalPreSelectedMaGV + " (chọn trước) cho môn " + mhh.getMaMH() + " lớp " + lop.getMaLop() + " đã vượt quá số tiết quy định và không phải đang dạy dở môn này. Thuật toán sẽ cố chọn GV khác từ TCM.");
                    gvFixed = null; // Reset để logic bên dưới chọn GV khác
                }
            } else {
                System.err.println("CẢNH BÁO: GV " + finalPreSelectedMaGV + " được chọn trước cho môn " + mhh.getMaMH() + " của lớp " + lop.getMaLop() + " không hợp lệ. Thuật toán sẽ cố chọn GV khác từ TCM.");
            }
        }

        if (gvFixed == null && pinnedTeacherForSubjectClass.containsKey(pinnedTeacherKey)) {
            String pinnedMaGV = pinnedTeacherForSubjectClass.get(pinnedTeacherKey);
            gvFixed = giaoVienHopLe.stream().filter(gv -> gv.getMaGV().equals(pinnedMaGV)).findFirst().orElse(null);
            if (gvFixed == null) {
                System.err.println("CẢNH BÁO: GV " + pinnedMaGV + " đã ghim cho môn " + mhh.getMaMH() + " của lớp " + lop.getMaLop() + " không còn hợp lệ. Xóa ghim và chọn lại.");
                pinnedTeacherForSubjectClass.remove(pinnedTeacherKey);
            }
            // Không cần kiểm tra canTeacherTeachMore ở đây nữa vì nếu đã ghim, nghĩa là họ đang dạy dở hoặc đã được chấp nhận trước đó.
        }

        if (gvFixed == null) {
            List<GiaoVien> potentialTeachers = giaoVienHopLe.stream()
                    .filter(gv -> gv.getMaTCM() != null && gv.getMaTCM().equals(mhh.getMaTCM()))
                    .collect(Collectors.toList());

            if (potentialTeachers.isEmpty()) {
                System.err.println("KHÔNG CÓ GV TRONG TCM: Không tìm thấy GV nào từ TCM " + mhh.getMaTCM() + " cho môn " + mhh.getMaMH() + " lớp " + lop.getMaLop());
                return null;
            }

            if (lop.getGvcn() != null) {
                potentialTeachers.sort((g1, g2) -> {
                    boolean g1IsGvcn = g1.getMaGV().equals(lop.getGvcn());
                    boolean g2IsGvcn = g2.getMaGV().equals(lop.getGvcn());
                    if (g1IsGvcn && !g2IsGvcn) return -1;
                    if (!g1IsGvcn && g2IsGvcn) return 1;
                    return 0;
                });
            }

            for (GiaoVien potentialGv : potentialTeachers) {
                // Chỉ chọn GV mới nếu họ còn khả năng dạy thêm (chưa vượt ngưỡng quy định)
                if (canTeacherTeachMore(potentialGv)) {
                    gvFixed = potentialGv;
                    if (preSelectedMaGV == null) { // Chỉ ghim nếu không có lựa chọn trước nào (môn không linh hoạt hoặc linh hoạt nhưng không chọn GV)
                        pinnedTeacherForSubjectClass.put(pinnedTeacherKey, gvFixed.getMaGV());
                    }
                    break;
                }
            }

            if (gvFixed == null) {
                System.err.println("KHÔNG CHỌN ĐƯỢC GV BAN ĐẦU: Không GV nào trong TCM " + mhh.getMaTCM() + " còn đủ điều kiện (số tiết quy định) cho môn " + mhh.getMaMH() + " lớp " + lop.getMaLop());
                return null;
            }
        }
        return gvFixed;
    }

    /**
     * Kiểm tra xem một giáo viên có đang dạy dở dang môn học này cho lớp này hay không
     * (dựa trên việc đã có tiết nào của môn này do GV này dạy cho lớp này trong TKB cơ sở hay chưa).
     * @param gv Giáo viên
     * @param mhh Môn học học
     * @param lop Lớp
     * @return true nếu giáo viên đã có tiết dạy môn này cho lớp này, false nếu không.
     */
    private boolean isTeacherAlreadyTeachingSubjectInClass(GiaoVien gv, MonHocHoc mhh, Lop lop) {
        if (chiTietTkbCoSo == null) return false;
        return chiTietTkbCoSo.stream()
                .anyMatch(ct -> ct.getMaLop().equals(lop.getMaLop()) &&
                        ct.getMaMH().equals(mhh.getMaMH()) &&
                        ct.getMaGV().equals(gv.getMaGV()));
    }


    private boolean canTeacherTeachMore(GiaoVien gv) {
        if (gv.getSoTietQuyDinh() == null || gv.getSoTietQuyDinh() == 0) {
            return true;
        }
        long soTietDaDayHienTai = generatedTimetable.values().stream()
                .flatMap(List::stream)
                .filter(ct -> ct.getMaGV() != null && ct.getMaGV().equals(gv.getMaGV()))
                .count();
        int soTietChucVu = xepTKBDAO.getSoTietChucVuDaSuDung(gv.getMaGV(), selectedHocKy.getMaHK());
        return (soTietDaDayHienTai + soTietChucVu) < gv.getSoTietQuyDinh();
    }

    /**
     * Xếp lịch cho tất cả các tiết cần thiết của một môn học, với một giáo viên đã được xác định trước.
     * THAY ĐỔI: Giáo viên sẽ hoàn thành tất cả các tiết cho môn/lớp này ngay cả khi vượt quá số tiết quy định.
     * Việc kiểm tra số tiết quy định chỉ thực hiện khi chọn giáo viên ban đầu cho cặp (môn, lớp).
     * @param mhh Môn học cần xếp.
     * @param lop Lớp học.
     * @param gvFixed Giáo viên đã được cố định cho môn học này.
     * @param soTietCanXep Tổng số tiết cần xếp cho môn học này.
     * @param currentSoTietMoiThuForThisKhoi Map cài đặt số tiết học mỗi thứ cho khối của lớp này.
     */
    private void scheduleAllPeriodsForSubject(MonHocHoc mhh, Lop lop, GiaoVien gvFixed, int soTietCanXep, Map<Integer, Integer> currentSoTietMoiThuForThisKhoi) {
        int soTietDaXepThucTe = 0;
        for (int i = 0; i < soTietCanXep; i++) {
            // THAY ĐỔI: Không kiểm tra canTeacherTeachMore(gvFixed) ở đây nữa.
            // Giáo viên sẽ hoàn thành tất cả các tiết cho môn/lớp này.

            boolean daXepTietNayThanhCong = tryScheduleSinglePeriodForSubject(mhh, lop, gvFixed, currentSoTietMoiThuForThisKhoi);
            if (!daXepTietNayThanhCong) {
                System.err.println("Không thể xếp tiết thứ " + (i + 1) + " (trong số " + soTietCanXep + " tiết cần) cho môn " + mhh.getTenMH() + " (" + mhh.getMaMH() + ") của lớp " + lop.getMaLop() + " với GV " + gvFixed.getMaGV());
                // Quyết định dừng lại hay cố gắng tiếp tục là tùy thuộc vào yêu cầu. Hiện tại là tiếp tục cố gắng xếp các tiết còn lại.
            } else {
                soTietDaXepThucTe++;
            }
        }

        // Sau khi đã xếp xong tất cả các tiết cho môn/lớp này, có thể kiểm tra và ghi log nếu GV vượt ngưỡng
        if (gvFixed.getSoTietQuyDinh() != null && gvFixed.getSoTietQuyDinh() > 0) {
            long tongSoTietGVDaySauKhiXepXongMonNay = generatedTimetable.values().stream()
                    .flatMap(List::stream)
                    .filter(ct -> ct.getMaGV() != null && ct.getMaGV().equals(gvFixed.getMaGV()))
                    .count();
            int soTietChucVu = xepTKBDAO.getSoTietChucVuDaSuDung(gvFixed.getMaGV(), selectedHocKy.getMaHK());

            if ((tongSoTietGVDaySauKhiXepXongMonNay + soTietChucVu) > gvFixed.getSoTietQuyDinh()) {
                System.out.println("LƯU Ý: GV " + gvFixed.getMaGV() + " (" + gvFixed.getHoGV() + " " + gvFixed.getTenGV() +
                        ") đã dạy " + tongSoTietGVDaySauKhiXepXongMonNay + " tiết (+ " + soTietChucVu + " CV) sau khi hoàn thành môn " +
                        mhh.getMaMH() + " cho lớp " + lop.getMaLop() +
                        ", vượt quá số tiết quy định là " + gvFixed.getSoTietQuyDinh() + ".");
            }
        }
        if (soTietDaXepThucTe < soTietCanXep) {
            System.err.println("CẢNH BÁO THIẾU TIẾT: Lớp " + lop.getMaLop() + " môn " + mhh.getMaMH() +
                    " chỉ xếp được " + soTietDaXepThucTe + "/" + soTietCanXep + " tiết với GV " + gvFixed.getMaGV());
        }
    }

    private boolean tryScheduleSinglePeriodForSubject(MonHocHoc mhh, Lop lop, GiaoVien gvFixed, Map<Integer, Integer> currentSoTietMoiThuForThisKhoi) {
        List<Integer> thuList = Arrays.asList(2, 3, 4, 5, 6, 7);
        Collections.shuffle(thuList);

        for (int thu : thuList) {
            int maxTietHomNayTheoCaiDat = currentSoTietMoiThuForThisKhoi.getOrDefault(thu, SO_TIET_HIEN_THI_MAC_DINH);
            List<Integer> tietList = new ArrayList<>();
            for (int t = 1; t <= maxTietHomNayTheoCaiDat; t++) {
                tietList.add(t);
            }
            Collections.shuffle(tietList);

            for (int tiet : tietList) {
                if (tiet > SO_TIET_HIEN_THI_MAC_DINH) continue; // SO_TIET_HIEN_THI_MAC_DINH là giới hạn của mảng busy

                if (!lopBusySlots.get(lop.getMaLop())[thu][tiet] &&
                        !teacherBusySlots.get(gvFixed.getMaGV())[thu][tiet]) {

                    if (checkSpecificConstraints(mhh, lop, thu, tiet, gvFixed.getMaGV())) {
                        String hoGV = gvFixed.getHoGV() != null ? gvFixed.getHoGV().trim() : "";
                        String tenGV = gvFixed.getTenGV() != null ? gvFixed.getTenGV().trim() : "";
                        String hoTenGVChoTKB = (hoGV + " " + tenGV).trim();
                        if (hoTenGVChoTKB.isEmpty() && gvFixed.getMaGV() != null) {
                            hoTenGVChoTKB = "GV:" + gvFixed.getMaGV();
                        }

                        ChiTietTKB chiTietMoi = ChiTietTKB.taoChoXepLichTuDong(thu, tiet, mhh.getTenMH(), lop.getMaLop(), hoTenGVChoTKB, gvFixed.getMaGV(), mhh.getMaMH(), 0);

                        generatedTimetable.computeIfAbsent(lop.getMaLop(), k -> new ArrayList<>()).add(chiTietMoi);
                        lopBusySlots.get(lop.getMaLop())[thu][tiet] = true;
                        teacherBusySlots.get(gvFixed.getMaGV())[thu][tiet] = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkSpecificConstraints(MonHocHoc mhh, Lop lop, int thu, int tiet, String maGVDangXep) {
        List<ChiTietTKB> tkbCuaLopTrongNgay = generatedTimetable.getOrDefault(lop.getMaLop(), Collections.emptyList())
                .stream()
                .filter(ct -> ct.getThu() == thu)
                .toList();

        String maMHCurrentUpper = mhh.getMaMH().toUpperCase();
        if (maMHCurrentUpper.startsWith(PREFIX_GDQPAN) || maMHCurrentUpper.startsWith(PREFIX_GDTC)) {
            String otherConflictPrefix = maMHCurrentUpper.startsWith(PREFIX_GDQPAN) ? PREFIX_GDTC : PREFIX_GDQPAN;
            for (ChiTietTKB ctDaXep : tkbCuaLopTrongNgay) {
                if (ctDaXep.getMaMH() != null && ctDaXep.getMaMH().toUpperCase().startsWith(otherConflictPrefix)) {
                    return false;
                }
            }
        }

        if (tiet > 1) {
            int consecutiveCount = 0;
            for (int i = 1; i < MAX_CONSECUTIVE_PERIODS; i++) { // Kiểm tra MAX_CONSECUTIVE_PERIODS - 1 tiết trước
                int tietTruocDo = tiet - i;
                if (tietTruocDo < 1) break;

                boolean foundMatchingPreviousPeriod = false;
                for (ChiTietTKB ctDaXep : tkbCuaLopTrongNgay) {
                    if (ctDaXep.getTiet() == tietTruocDo) {
                        if (ctDaXep.getMaMH() != null && ctDaXep.getMaMH().equals(mhh.getMaMH()) &&
                                ctDaXep.getMaGV() != null && ctDaXep.getMaGV().equals(maGVDangXep)) {
                            consecutiveCount++;
                            foundMatchingPreviousPeriod = true;
                        }
                        break;
                    }
                }
                if (!foundMatchingPreviousPeriod) {
                    break;
                }
            }
            // Nếu đã có MAX_CONSECUTIVE_PERIODS tiết giống hệt xếp liền trước rồi thì không được thêm
            return consecutiveCount < MAX_CONSECUTIVE_PERIODS;
        }
        return true;
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        tkbTableView.setFixedCellSize(ROW_HEIGHT);
        // Tính toán chiều cao dựa trên số tiết hiển thị mặc định
        // Nếu muốn linh hoạt hơn, bạn có thể cập nhật chiều cao này khi chọn lớp dựa trên số tiết tối đa của khối đó
        double calculatedTableHeight = HEADER_HEIGHT + (SO_TIET_HIEN_THI_MAC_DINH * ROW_HEIGHT) + 2.0; // +2.0 cho border
        tkbTableView.setPrefHeight(calculatedTableHeight);
        tkbTableView.setMinHeight(calculatedTableHeight); // Giữ chiều cao cố định
        tkbTableView.setMaxHeight(calculatedTableHeight); // Giữ chiều cao cố định

        if (lopTKBTitleLabel != null) {
            lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP");
        }
    }

    private void loadBaseData() {
        tatCaLop = xepTKBDAO.getDanhSachLop();
        if (selectedHocKy != null) {
            phanCongMonHocChoLop = xepTKBDAO.getPhanCongMonHocChoTatCaLop(selectedHocKy.getMaHK());
        } else {
            phanCongMonHocChoLop = new HashMap<>(); // Khởi tạo rỗng nếu không có học kỳ
            Platform.runLater(() -> showAlert(Alert.AlertType.WARNING, "Thiếu Học Kỳ", "Không thể tải phân công môn học do học kỳ không được chọn."));
        }
        tatCaGiaoVienDayDu = xepTKBDAO.getDanhSachGiaoVienDayDu();
        List<MonHoc> allMonHoc = thoiKhoaBieuDAO.getDanhSachMonHocTheoTCM(null); // Lấy tất cả môn học
        if (allMonHoc != null) {
            danhMucMonHoc.clear(); // Xóa dữ liệu cũ nếu có
            allMonHoc.forEach(mh -> danhMucMonHoc.put(mh.getMaMH(), mh));
        }
    }

    private void setupTableColumns() {
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

                            // Lấy tên môn học từ danh mục nếu tên trong ChiTietTKB rỗng nhưng có MaMH
                            if ((tenMHDisplay == null || tenMHDisplay.trim().isEmpty()) && item.getMaMH() != null) {
                                MonHoc mh = danhMucMonHoc.get(item.getMaMH());
                                if (mh != null) tenMHDisplay = mh.getTenMH();
                            }
                            // Nếu vẫn rỗng, hiển thị MaMH
                            if (tenMHDisplay == null || tenMHDisplay.trim().isEmpty()) {
                                tenMHDisplay = (item.getMaMH() != null) ? "MH: " + item.getMaMH() : "(Môn?)";
                            }

                            // Tương tự cho tên GV
                            boolean needLookupGVName = (hoTenGVDisplay == null || hoTenGVDisplay.trim().isEmpty() || hoTenGVDisplay.trim().toUpperCase().startsWith("GV:"));
                            if (needLookupGVName && item.getMaGV() != null) {
                                if (tatCaGiaoVienDayDu != null) { // Đảm bảo tatCaGiaoVienDayDu đã được tải
                                    GiaoVien gv = tatCaGiaoVienDayDu.stream()
                                            .filter(g -> item.getMaGV().equals(g.getMaGV()))
                                            .findFirst().orElse(null);
                                    if (gv != null) {
                                        String ho = gv.getHoGV() != null ? gv.getHoGV().trim() : "";
                                        String ten = gv.getTenGV() != null ? gv.getTenGV().trim() : "";
                                        if (!ho.isEmpty() || !ten.isEmpty()) {
                                            hoTenGVDisplay = (ho + " " + ten).trim();
                                        }
                                    }
                                }
                            }
                            if (hoTenGVDisplay == null || hoTenGVDisplay.trim().isEmpty() || hoTenGVDisplay.trim().toUpperCase().startsWith("GV:")) {
                                hoTenGVDisplay = (item.getMaGV() != null) ? "GV: " + item.getMaGV() : "(GV?)";
                            }


                            String displayText = tenMHDisplay;
                            if (!hoTenGVDisplay.isEmpty() && !hoTenGVDisplay.equalsIgnoreCase("(GV?)") && !hoTenGVDisplay.toUpperCase().startsWith("GV:")) {
                                displayText += " - " + hoTenGVDisplay;
                            } else if (!hoTenGVDisplay.isEmpty() && hoTenGVDisplay.toUpperCase().startsWith("GV:")) {
                                // Nếu chỉ có dạng "GV:xxx", hiển thị trong ngoặc đơn
                                displayText += " (" + hoTenGVDisplay + ")";
                            }


                            setText(displayText);
                            setTooltip(new Tooltip(displayText)); // Tooltip để xem đầy đủ nếu text dài
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
        menuKhoi10.getItems().clear();
        menuKhoi11.getItems().clear();
        menuKhoi12.getItems().clear();

        if (tatCaLop == null) return;

        // Sắp xếp lớp theo kiểu "natural sort" (ví dụ: 10A1, 10A2, ..., 10A10 thay vì 10A1, 10A10, 10A2)
        Comparator<Lop> naturalSortLopComparator = (lop1, lop2) -> {
            String s1 = lop1.getMaLop();
            String s2 = lop2.getMaLop();

            // Tách phần chữ và phần số
            String prefix1 = s1.replaceAll("\\d+$", "");
            String suffix1Str = s1.substring(prefix1.length());

            String prefix2 = s2.replaceAll("\\d+$", "");
            String suffix2Str = s2.substring(prefix2.length());

            // So sánh phần chữ trước
            int prefixCompare = prefix1.compareToIgnoreCase(prefix2);
            if (prefixCompare != 0) {
                return prefixCompare;
            }

            // Nếu phần chữ giống nhau, so sánh phần số
            try {
                int num1 = suffix1Str.isEmpty() ? 0 : Integer.parseInt(suffix1Str);
                int num2 = suffix2Str.isEmpty() ? 0 : Integer.parseInt(suffix2Str);
                return Integer.compare(num1, num2);
            } catch (NumberFormatException e) {
                // Nếu không phải số, so sánh như chuỗi
                return suffix1Str.compareToIgnoreCase(suffix2Str);
            }
        };

        Map<String, List<Lop>> lopTheoKhoi = tatCaLop.stream()
                .sorted(naturalSortLopComparator) // Áp dụng comparator đã tạo
                .collect(Collectors.groupingBy(Lop::getKhoi, TreeMap::new, Collectors.toList()));


        lopTheoKhoi.forEach((khoi, danhSachLopTrongKhoi) -> {
            Menu menuKhoi = null;
            if ("10".equals(khoi)) menuKhoi = menuKhoi10;
            else if ("11".equals(khoi)) menuKhoi = menuKhoi11;
            else if ("12".equals(khoi)) menuKhoi = menuKhoi12;

            if (menuKhoi != null) {
                for (Lop lop : danhSachLopTrongKhoi) {
                    String menuItemText = lop.getMaLop();
                    // Chỉ thêm tên lớp vào nếu nó khác mã lớp và không rỗng
                    if (lop.getTenLop() != null && !lop.getTenLop().isEmpty() && !lop.getTenLop().equals(lop.getMaLop())) {
                        menuItemText += " (" + lop.getTenLop() + ")";
                    }
                    MenuItem lopItem = new MenuItem(menuItemText);
                    lopItem.setOnAction(event -> displayTKBForLop(lop.getMaLop()));
                    menuKhoi.getItems().add(lopItem);
                }
            }
        });
    }

    private void displayTKBForLop(String maLop) {
        ObservableList<TietHocData> tkbDataList = FXCollections.observableArrayList();
        // Xác định số tiết tối đa cần hiển thị cho lớp này dựa trên cài đặt khối
        String khoiCuaLop = "";
        if (tatCaLop != null) {
            Optional<Lop> lopOptional = tatCaLop.stream().filter(l -> l.getMaLop().equals(maLop)).findFirst();
            if (lopOptional.isPresent()) {
                khoiCuaLop = lopOptional.get().getKhoi();
            }
        }

        Map<Integer, Integer> soTietTungNgayCuaKhoi = soTietMoiThuTheoKhoiInput.get(khoiCuaLop);
        int maxTietHienThiChoLopNay = SO_TIET_HIEN_THI_MAC_DINH; // Mặc định

        if (soTietTungNgayCuaKhoi != null && !soTietTungNgayCuaKhoi.isEmpty()) {
            maxTietHienThiChoLopNay = soTietTungNgayCuaKhoi.values().stream().max(Integer::compareTo).orElse(SO_TIET_HIEN_THI_MAC_DINH);
        }
        // Đảm bảo không hiển thị nhiều hơn giới hạn của mảng busy slot (SO_TIET_HIEN_THI_MAC_DINH)
        maxTietHienThiChoLopNay = Math.min(maxTietHienThiChoLopNay, SO_TIET_HIEN_THI_MAC_DINH);


        for (int i = 1; i <= maxTietHienThiChoLopNay; i++) {
            tkbDataList.add(new TietHocData("Tiết " + i));
        }

        List<ChiTietTKB> chiTietCuaLop = generatedTimetable.get(maLop);

        if (chiTietCuaLop != null) {
            for (ChiTietTKB ct : chiTietCuaLop) {
                if (ct.getTiet() > 0 && ct.getTiet() <= maxTietHienThiChoLopNay) {
                    TietHocData rowData = tkbDataList.get(ct.getTiet() - 1);
                    // Kiểm tra xem tiết học có nằm trong giới hạn số tiết của ngày đó cho khối không
                    int gioiHanTietTrongNgay = (soTietTungNgayCuaKhoi != null) ? soTietTungNgayCuaKhoi.getOrDefault(ct.getThu(), maxTietHienThiChoLopNay) : maxTietHienThiChoLopNay;
                    if (ct.getTiet() <= gioiHanTietTrongNgay) {
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

        // Cập nhật chiều cao TableView dựa trên số tiết thực tế hiển thị cho lớp này
        double newTableHeight = HEADER_HEIGHT + (maxTietHienThiChoLopNay * ROW_HEIGHT) + 2.0;
        tkbTableView.setPrefHeight(newTableHeight);
        tkbTableView.setMinHeight(newTableHeight);
        tkbTableView.setMaxHeight(newTableHeight);
        tkbTableView.refresh();


        if (lopTKBTitleLabel != null) {
            String tenLopHienThi = maLop;
            if (tatCaLop != null) {
                Optional<Lop> lopOptional = tatCaLop.stream().filter(l -> l.getMaLop().equals(maLop)).findFirst();
                if (lopOptional.isPresent() && lopOptional.get().getTenLop() != null && !lopOptional.get().getTenLop().isEmpty()) {
                    tenLopHienThi = lopOptional.get().getMaLop() + (lopOptional.get().getTenLop().equals(lopOptional.get().getMaLop()) ? "" : " - " + lopOptional.get().getTenLop());
                }
            }
            lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP " + tenLopHienThi.toUpperCase());
        }
        infoLabel.setText("Đang hiển thị TKB lớp: " + maLop +
                (selectedHocKy != null ? " (Học kỳ: " + selectedHocKy + ")" : ""));
    }


    private void displayEmptyTKB() {
        ObservableList<TietHocData> tkbDataList = FXCollections.observableArrayList();
        for (int i = 1; i <= SO_TIET_HIEN_THI_MAC_DINH; i++) {
            tkbDataList.add(new TietHocData("Tiết " + i));
        }
        tkbTableView.setItems(tkbDataList);
        // Reset chiều cao TableView về mặc định
        double defaultTableHeight = HEADER_HEIGHT + (SO_TIET_HIEN_THI_MAC_DINH * ROW_HEIGHT) + 2.0;
        tkbTableView.setPrefHeight(defaultTableHeight);
        tkbTableView.setMinHeight(defaultTableHeight);
        tkbTableView.setMaxHeight(defaultTableHeight);
        tkbTableView.refresh();

        if (lopTKBTitleLabel != null) {
            lopTKBTitleLabel.setText("THỜI KHÓA BIỂU LỚP");
        }
    }


    @FXML
    void handleXemTatCaLop(ActionEvent event) {
        infoLabel.setText("Chức năng xem tổng quan toàn trường chưa được triển khai.");
        displayEmptyTKB();
        if (lopTKBTitleLabel != null) {
            lopTKBTitleLabel.setText("THỜI KHÓA BIỂU TOÀN TRƯỜNG (TỔNG QUAN)");
        }
    }

    @FXML
    void handleLuuThoiKhoaBieu(ActionEvent event) {
        if (generatedTimetable == null || generatedTimetable.isEmpty()) {
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
                List<ChiTietTKB> allChiTietToSave = generatedTimetable.values().stream()
                        .flatMap(List::stream)
                        .toList();

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
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn chạy lại thuật toán xếp TKB không?\n" +
                        "Mọi thay đổi chưa lưu của TKB hiện tại (nếu có) sẽ bị mất.", ButtonType.YES, ButtonType.NO);
        confirmAlert.setTitle("Xác nhận Xếp Lại TKB");
        confirmAlert.setHeaderText(null);
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (selectedHocKy == null) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Chưa chọn học kỳ để xếp lại TKB.");
                return;
            }
            infoLabel.setText("Đang chuẩn bị xếp lại TKB cho Học Kỳ: " + selectedHocKy + "...");
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
        alert.showAndWait();
    }
}
