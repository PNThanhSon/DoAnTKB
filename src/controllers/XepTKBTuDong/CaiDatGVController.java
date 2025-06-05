package controllers.XepTKBTuDong;

import dao.XepTKBDAO;
import entities.GiaoVien;
import entities.HocKy;
import entities.MonHoc;
import entities.TeacherCustomSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class CaiDatGVController {

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

    private TeacherCustomSettings currentSettingsForGV; // Đối tượng settings hiện tại cho GV đang được cấu hình

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

    public void initialize() {
        chkParticipateInScheduling.selectedProperty().addListener((obs, oldVal, newVal) -> {
            subjectSettingsPane.setVisible(newVal);
            subjectSettingsPane.setManaged(newVal);
        });
    }

    public void initData(GiaoVien gv, HocKy hk, Map<String, TeacherCustomSettings> settingsMap, XepTKBDAO dao) {
        this.selectedGV = gv;
        this.currentHocKy = hk;
        // Đây là tham chiếu đến map của ChuanBiController
        this.xepTKBDAO = dao;

        titleLabel.setText("Cài Đặt Chi Tiết cho GV: " + selectedGV.getHoGV() + " " + selectedGV.getTenGV() + " (" + selectedGV.getMaGV() + ")");

        // Lấy hoặc tạo mới TeacherCustomSettings cho GV này từ map chung
        this.currentSettingsForGV = settingsMap.computeIfAbsent(selectedGV.getMaGV(), k -> {
            System.out.println("[CaiDatGVController.initData] Tạo mới TeacherCustomSettings cho GV: " + k);
            return new TeacherCustomSettings(k);
        });
        System.out.println("[CaiDatGVController.initData] For GV " + selectedGV.getMaGV() + ": Loaded preferences: " + currentSettingsForGV.getSubjectTeachingPreference());


        chkParticipateInScheduling.setSelected(currentSettingsForGV.isParticipateInScheduling());
        subjectSettingsPane.setVisible(currentSettingsForGV.isParticipateInScheduling());
        subjectSettingsPane.setManaged(currentSettingsForGV.isParticipateInScheduling());

        populateSubjectList();
    }

    private void populateSubjectList() {
        subjectListVBox.getChildren().clear();
        List<MonHoc> subjectsToShow = new ArrayList<>();

        if (selectedGV.getMaTCM() != null && !selectedGV.getMaTCM().isEmpty()) {
            System.out.println("[CaiDatGVController.populateSubjectList] GV " + selectedGV.getMaGV() + " thuộc TCM: " + selectedGV.getMaTCM());
            List<MonHoc> tcmSubjects = xepTKBDAO.getMonHocByTCM(selectedGV.getMaTCM());
            if (tcmSubjects != null) {
                subjectsToShow.addAll(tcmSubjects);
                System.out.println("[CaiDatGVController.populateSubjectList] Môn từ TCM: " + tcmSubjects.stream().map(MonHoc::getMaMH).toList());
            }
        } else {
            subjectListVBox.getChildren().add(new Label("Giáo viên này chưa được gán Tổ Chuyên Môn."));
            System.out.println("[CaiDatGVController.populateSubjectList] GV " + selectedGV.getMaGV() + " không có TCM.");
        }

        for (MonHocInfo specialInfo : SPECIAL_SUBJECTS_INFO) {
            if (subjectsToShow.stream().noneMatch(mh -> mh.getMaMH().equals(specialInfo.maMH))) {
                subjectsToShow.add(new MonHoc(specialInfo.maMH, specialInfo.tenMH, null, null));
            }
        }
        System.out.println("[CaiDatGVController.populateSubjectList] Tổng số môn sẽ hiển thị cho GV " + selectedGV.getMaGV() + ": " + subjectsToShow.size());
        subjectsToShow.forEach(mh -> System.out.println("  - " + mh.getMaMH() + " (" + mh.getTenMH() + ")"));


        if (subjectsToShow.isEmpty() && (selectedGV.getMaTCM() != null && !selectedGV.getMaTCM().isEmpty())) {
            subjectListVBox.getChildren().add(new Label("Không tìm thấy môn học nào cho TCM: " + selectedGV.getMaTCM() + "."));
        } else if (subjectsToShow.isEmpty()){
            subjectListVBox.getChildren().add(new Label("Không có môn học nào để hiển thị."));
        }

        for (MonHoc monHoc : subjectsToShow) {
            CheckBox chkSubject = new CheckBox(monHoc.getTenMH() + " (" + monHoc.getMaMH() + ")");
            chkSubject.setUserData(monHoc.getMaMH());

            boolean currentPreference = currentSettingsForGV.getTeachingPreferenceForSubject(monHoc.getMaMH());
            chkSubject.setSelected(currentPreference);
            // Log giá trị hiện tại khi populate
            if (selectedGV.getMaGV().equals("GV021") && monHoc.getMaMH().equals("VATLI10")) {
                System.out.println("[CaiDatGVController.populateSubjectList] For GV021, Mon VATLI10: Initial checkbox state from currentSettingsForGV = " + currentPreference);
            }

            subjectListVBox.getChildren().add(chkSubject);
        }
    }

    @FXML
    void handleSaveSettings(ActionEvent event) {
        if (selectedGV == null || currentSettingsForGV == null) {
            System.err.println("Lỗi: selectedGV hoặc currentSettingsForGV là null khi lưu.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Lưu Cài Đặt");
            alert.setHeaderText(null);
            alert.setContentText("Không thể lưu cài đặt do thiếu thông tin giáo viên. Vui lòng thử lại.");
            alert.showAndWait();
            return;
        }

        System.out.println("[CaiDatGVController.handleSaveSettings] Saving settings for GV: " + selectedGV.getMaGV());
        System.out.println("[CaiDatGVController.handleSaveSettings] Participate in scheduling: " + chkParticipateInScheduling.isSelected());
        System.out.println("[CaiDatGVController.handleSaveSettings] Preferences BEFORE clear: " + new HashMap<>(currentSettingsForGV.getSubjectTeachingPreference()));


        currentSettingsForGV.setParticipateInScheduling(chkParticipateInScheduling.isSelected());
        // Quan trọng: Không clear() nếu bạn muốn giữ lại các preferences cho các môn không hiển thị trong VBox hiện tại.
        // Thay vào đó, chỉ cập nhật những môn có trong VBox.
        // Hoặc, đảm bảo VBox luôn chứa tất cả các môn có thể có của GV.
        // Hiện tại, logic là clear và thêm lại từ VBox.
        Map<String, Boolean> oldPreferences = new HashMap<>(currentSettingsForGV.getSubjectTeachingPreference());
        currentSettingsForGV.getSubjectTeachingPreference().clear(); // Xóa các preference cũ
        System.out.println("[CaiDatGVController.handleSaveSettings] Preferences AFTER clear: " + currentSettingsForGV.getSubjectTeachingPreference());


        if (chkParticipateInScheduling.isSelected()) {
            for (javafx.scene.Node node : subjectListVBox.getChildren()) {
                if (node instanceof CheckBox chkSubject) {
                    String maMH = (String) chkSubject.getUserData();
                    if (maMH != null) {
                        boolean isSelected = chkSubject.isSelected();
                        currentSettingsForGV.setTeachingPreferenceForSubject(maMH, isSelected);
                        if (selectedGV.getMaGV().equals("GV021") && maMH.equals("VATLI10")) {
                            System.out.println("[CaiDatGVController.handleSaveSettings] *** For GV021, saving VATLI10 preference as: " + isSelected + " ***");
                        }
                    }
                }
            }
        } else {
            // Nếu không tham gia xếp lịch, tất cả các subjectTeachingPreference đã bị clear.
            // Có thể bạn muốn giữ lại chúng nhưng chỉ không dùng đến khi isParticipateInScheduling là false.
            // Hoặc, nếu không tham gia, thì không quan tâm đến subject preference nữa.
            // Hiện tại: nếu không tham gia, map subjectTeachingPreference sẽ rỗng.
            System.out.println("[CaiDatGVController.handleSaveSettings] GV " + selectedGV.getMaGV() + " is NOT participating. All subject preferences cleared.");
        }

        System.out.println("[CaiDatGVController.handleSaveSettings] For GV " + selectedGV.getMaGV() + ": Preferences AFTER saving loop: " + currentSettingsForGV.getSubjectTeachingPreference());
        if (currentSettingsForGV.getSubjectTeachingPreference().containsKey("VATLI10")) {
            System.out.println("[CaiDatGVController.handleSaveSettings] For GV " + selectedGV.getMaGV() + ": VATLI10 final preference in currentSettingsForGV: " + currentSettingsForGV.getSubjectTeachingPreference().get("VATLI10"));
        } else {
            System.out.println("[CaiDatGVController.handleSaveSettings] For GV " + selectedGV.getMaGV() + ": VATLI10 preference NOT in currentSettingsForGV after save.");
        }


        // allTeacherSettingsMap (tham chiếu từ ChuanBiController) đã được cập nhật trực tiếp
        // thông qua currentSettingsForGV vì currentSettingsForGV là một phần tử của allTeacherSettingsMap.
        Stage stage = (Stage) btnSaveSettings.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
