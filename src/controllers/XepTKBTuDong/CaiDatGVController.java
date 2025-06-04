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
import javafx.scene.control.Alert; // Thêm import Alert
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    private Map<String, TeacherCustomSettings> allTeacherSettingsMap;
    private XepTKBDAO xepTKBDAO;

    private TeacherCustomSettings currentSettingsForGV;

    // THAY ĐỔI: Chuyển thành public static final
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

    // THAY ĐỔI: Lớp nội bộ thành public static, các trường thành public final
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
        this.allTeacherSettingsMap = settingsMap;
        this.xepTKBDAO = dao;

        titleLabel.setText("Cài Đặt Chi Tiết cho GV: " + selectedGV.getHoGV() + " " + selectedGV.getTenGV() + " (" + selectedGV.getMaGV() + ")");

        this.currentSettingsForGV = allTeacherSettingsMap.computeIfAbsent(selectedGV.getMaGV(), k -> new TeacherCustomSettings(selectedGV.getMaGV()));

        chkParticipateInScheduling.setSelected(currentSettingsForGV.isParticipateInScheduling());
        subjectSettingsPane.setVisible(currentSettingsForGV.isParticipateInScheduling());
        subjectSettingsPane.setManaged(currentSettingsForGV.isParticipateInScheduling());

        populateSubjectList();
    }

    private void populateSubjectList() {
        subjectListVBox.getChildren().clear();
        List<MonHoc> subjectsToShow = new ArrayList<>();

        if (selectedGV.getMaTCM() != null && !selectedGV.getMaTCM().isEmpty()) {
            List<MonHoc> tcmSubjects = xepTKBDAO.getMonHocByTCM(selectedGV.getMaTCM());
            if (tcmSubjects != null) {
                subjectsToShow.addAll(tcmSubjects);
            }
        } else {
            subjectListVBox.getChildren().add(new Label("Giáo viên này chưa được gán Tổ Chuyên Môn."));
        }

        for (MonHocInfo specialInfo : SPECIAL_SUBJECTS_INFO) {
            if (subjectsToShow.stream().noneMatch(mh -> mh.getMaMH().equals(specialInfo.maMH))) {
                subjectsToShow.add(new MonHoc(specialInfo.maMH, specialInfo.tenMH, null, null));
            }
        }

        if (subjectsToShow.isEmpty() && (selectedGV.getMaTCM() != null && !selectedGV.getMaTCM().isEmpty())) {
            subjectListVBox.getChildren().add(new Label("Không tìm thấy môn học nào cho TCM: " + selectedGV.getMaTCM() + "."));
        } else if (subjectsToShow.isEmpty()){
            subjectListVBox.getChildren().add(new Label("Không có môn học nào để hiển thị."));
        }

        for (MonHoc monHoc : subjectsToShow) {
            CheckBox chkSubject = new CheckBox(monHoc.getTenMH() + " (" + monHoc.getMaMH() + ")");
            chkSubject.setUserData(monHoc.getMaMH());

            boolean isSpecialSubject = SPECIAL_SUBJECTS_INFO.stream()
                    .anyMatch(info -> info.maMH.equals(monHoc.getMaMH()));

            if (isSpecialSubject) {
                chkSubject.setSelected(currentSettingsForGV.getSubjectTeachingPreference().getOrDefault(monHoc.getMaMH(), false));
            } else {
                chkSubject.setSelected(currentSettingsForGV.getTeachingPreferenceForSubject(monHoc.getMaMH()));
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

        currentSettingsForGV.setParticipateInScheduling(chkParticipateInScheduling.isSelected());
        currentSettingsForGV.getSubjectTeachingPreference().clear();

        if (chkParticipateInScheduling.isSelected()) {
            for (javafx.scene.Node node : subjectListVBox.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox chkSubject = (CheckBox) node;
                    String maMH = (String) chkSubject.getUserData();
                    if (maMH != null) {
                        currentSettingsForGV.setTeachingPreferenceForSubject(maMH, chkSubject.isSelected());
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
