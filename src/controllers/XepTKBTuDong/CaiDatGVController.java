package controllers.XepTKBTuDong;

import dao.XepTKBDAO;
import entities.GiaoVien;
import entities.HocKy;
import entities.MonHoc;
import entities.TeacherCustomSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField; // Import thêm TextField
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class CaiDatGVController {

    // Thêm FXML cho ô tìm kiếm
    @FXML private TextField searchMonHocTextField;

    @FXML private Label titleLabel;
    @FXML private CheckBox chkParticipateInScheduling;
    @FXML private VBox subjectSettingsPane;
    @FXML private ScrollPane subjectScrollPane;
    @FXML private VBox subjectListVBox;
    @FXML private Button btnSaveSettings;
    @FXML private Button btnCancel;

    private GiaoVien selectedGV;
    private HocKy currentHocKy;
    private XepTKBDAO xepTKBDAO;
    private TeacherCustomSettings currentSettingsForGV;

    public static final MonHocInfo GDDP10_INFO = new MonHocInfo("GDDP10", "Giáo dục địa phương 10");
    public static final MonHocInfo GDDP11_INFO = new MonHocInfo("GDDP11", "Giáo dục địa phương 11");
    public static final MonHocInfo GDDP12_INFO = new MonHocInfo("GDDP12", "Giáo dục địa phương 12");
    public static final MonHocInfo HDTNHN10_INFO = new MonHocInfo("HDTNHN10", "Hoạt động TN, HN 10");
    public static final MonHocInfo HDTNHN11_INFO = new MonHocInfo("HDTNHN11", "Hoạt động TN, HN 11");
    public static final MonHocInfo HDTNHN12_INFO = new MonHocInfo("HDTNHN12", "Hoạt động TN, HN 12");

    public static final List<MonHocInfo> SPECIAL_SUBJECTS_INFO = Arrays.asList(
            GDDP10_INFO, GDDP11_INFO, GDDP12_INFO,
            HDTNHN10_INFO, HDTNHN11_INFO, HDTNHN12_INFO
    );

    public static class MonHocInfo {
        public final String maMH;
        public final String tenMH;
        public MonHocInfo(String maMH, String tenMH) {
            this.maMH = maMH;
            this.tenMH = tenMH;
        }
    }

    /**
     * Cập nhật hàm initialize để thêm listener cho ô tìm kiếm.
     */
    public void initialize() {
        // Listener để ẩn/hiện phần cài đặt môn học
        chkParticipateInScheduling.selectedProperty().addListener((obs, oldVal, newVal) -> {
            subjectSettingsPane.setVisible(newVal);
            subjectSettingsPane.setManaged(newVal);
        });

        // **LOGIC MỚI TẠI ĐÂY**
        // Listener cho ô tìm kiếm môn học
        searchMonHocTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterSubjectList(newVal);
        });
    }

    /**
     * Hàm mới để lọc danh sách CheckBox môn học dựa trên nội dung tìm kiếm.
     * @param searchText Nội dung người dùng nhập vào ô tìm kiếm.
     */
    private void filterSubjectList(String searchText) {
        String lowerCaseSearchText = (searchText == null) ? "" : searchText.toLowerCase().trim();

        for (Node node : subjectListVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox chkSubject = (CheckBox) node;
                // Kiểm tra xem text của CheckBox (tên môn + mã môn) có chứa nội dung tìm kiếm không
                boolean isVisible = lowerCaseSearchText.isEmpty() || chkSubject.getText().toLowerCase().contains(lowerCaseSearchText);

                // Ẩn hoặc hiện CheckBox tương ứng
                chkSubject.setVisible(isVisible);
                chkSubject.setManaged(isVisible);
            }
        }
    }


    public void initData(GiaoVien gv, HocKy hk, Map<String, TeacherCustomSettings> settingsMap, XepTKBDAO dao) {
        this.selectedGV = gv;
        this.currentHocKy = hk;
        this.xepTKBDAO = dao;

        titleLabel.setText("Cài Đặt Chi Tiết cho GV: " + selectedGV.getHoGV() + " " + selectedGV.getTenGV() + " (" + selectedGV.getMaGV() + ")");

        this.currentSettingsForGV = settingsMap.computeIfAbsent(selectedGV.getMaGV(), k -> new TeacherCustomSettings(k));

        chkParticipateInScheduling.setSelected(currentSettingsForGV.isParticipateInScheduling());
        subjectSettingsPane.setVisible(currentSettingsForGV.isParticipateInScheduling());
        subjectSettingsPane.setManaged(currentSettingsForGV.isParticipateInScheduling());

        populateSubjectList();
    }

    /**
     * Hiển thị danh sách các môn học và trạng thái lựa chọn (có dạy/không dạy)
     * cho giáo viên đang được cấu hình.
     * LOGIC ĐÃ ĐƯỢC CẬP NHẬT: Mặc định các môn sẽ không được chọn (bỏ tick).
     * Dấu tick sẽ chỉ được hiển thị nếu môn đó đã được lưu với giá trị 'true'.
     */
    private void populateSubjectList() {
        subjectListVBox.getChildren().clear();

        // Lấy tất cả môn học từ CSDL để hiển thị đầy đủ lựa chọn.
        List<MonHoc> allSubjects = xepTKBDAO.getAllMonHoc();

        List<MonHoc> subjectsToShow = new ArrayList<>();
        if (allSubjects != null) {
            subjectsToShow.addAll(allSubjects);
        }

        // Đảm bảo các môn đặc biệt (GDDP, HDTNHN) luôn có trong danh sách để cấu hình.
        for (MonHocInfo specialInfo : SPECIAL_SUBJECTS_INFO) {
            if (subjectsToShow.stream().noneMatch(mh -> mh.getMaMH().equals(specialInfo.maMH))) {
                subjectsToShow.add(new MonHoc(specialInfo.maMH, specialInfo.tenMH, null, null));
            }
        }

        // Sắp xếp danh sách theo tên môn học để dễ dàng tìm kiếm.
        subjectsToShow.sort(Comparator.comparing(MonHoc::getTenMH, String.CASE_INSENSITIVE_ORDER));

        if (subjectsToShow.isEmpty()) {
            subjectListVBox.getChildren().add(new Label("Không tìm thấy môn học nào trong cơ sở dữ liệu."));
        } else {
            // Lấy ra bản đồ các môn học mà giáo viên này đã được cấu hình để dạy.
            Map<String, Boolean> preferences = currentSettingsForGV.getSubjectTeachingPreference();

            for (MonHoc monHoc : subjectsToShow) {
                CheckBox chkSubject = new CheckBox(monHoc.getTenMH() + " (" + monHoc.getMaMH() + ")");
                chkSubject.setUserData(monHoc.getMaMH());

                // Lấy giá trị từ bản đồ cài đặt.
                // Nếu môn học này có trong bản đồ và giá trị là 'true', isTicked sẽ là true.
                // Nếu môn học không có trong bản đồ (trường hợp GV mới) hoặc giá trị là 'false',
                // getOrDefault sẽ trả về 'false', và ô sẽ không được tick.
                boolean isTicked = preferences.getOrDefault(monHoc.getMaMH(), false);
                chkSubject.setSelected(isTicked);

                subjectListVBox.getChildren().add(chkSubject);
            }
        }
    }


    @FXML
    void handleSaveSettings(ActionEvent event) {
        if (selectedGV == null || currentSettingsForGV == null) {
            System.err.println("Lỗi: selectedGV hoặc currentSettingsForGV là null khi lưu.");
            return;
        }

        currentSettingsForGV.setParticipateInScheduling(chkParticipateInScheduling.isSelected());

        // Xóa các cài đặt cũ để cập nhật lại từ các CheckBox hiện tại
        currentSettingsForGV.getSubjectTeachingPreference().clear();

        if (chkParticipateInScheduling.isSelected()) {
            for (javafx.scene.Node node : subjectListVBox.getChildren()) {
                if (node instanceof CheckBox chkSubject) {
                    String maMH = (String) chkSubject.getUserData();
                    if (maMH != null) {
                        // Chỉ lưu những môn được tick để tiết kiệm không gian
                        if (chkSubject.isSelected()) {
                            currentSettingsForGV.setTeachingPreferenceForSubject(maMH, true);
                        }
                    }
                }
            }
        }

        Stage stage = (Stage) btnSaveSettings.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
