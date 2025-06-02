package controllers.XepTKBTuDong;

import dao.XepTKBDAO;
import entities.GiaoVien;
import entities.HocKy;
import entities.MonHoc;
import entities.TeacherCustomSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    // Định nghĩa mã môn và tên môn cho các môn đặc biệt theo khối
    private static final MonHocInfo GDDP10_INFO = new MonHocInfo("GDDP10", "Giáo dục địa phương 10");
    private static final MonHocInfo GDDP11_INFO = new MonHocInfo("GDDP11", "Giáo dục địa phương 11");
    private static final MonHocInfo GDDP12_INFO = new MonHocInfo("GDDP12", "Giáo dục địa phương 12");
    private static final MonHocInfo HDTNHN10_INFO = new MonHocInfo("HDTNHN10", "Hoạt động TN, HN 10");
    private static final MonHocInfo HDTNHN11_INFO = new MonHocInfo("HDTNHN11", "Hoạt động TN, HN 11");
    private static final MonHocInfo HDTNHN12_INFO = new MonHocInfo("HDTNHN12", "Hoạt động TN, HN 12");

    private static final List<MonHocInfo> SPECIAL_SUBJECTS_INFO = Arrays.asList(
            GDDP10_INFO, GDDP11_INFO, GDDP12_INFO,
            HDTNHN10_INFO, HDTNHN11_INFO, HDTNHN12_INFO
    );

    // Lớp nội bộ tiện ích để lưu thông tin môn học đặc biệt
    private static class MonHocInfo {
        final String maMH;
        final String tenMH;
        MonHocInfo(String maMH, String tenMH) {
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

        // 1. Lấy các môn học từ TCM của giáo viên (nếu có)
        if (selectedGV.getMaTCM() != null && !selectedGV.getMaTCM().isEmpty()) {
            List<MonHoc> tcmSubjects = xepTKBDAO.getMonHocByTCM(selectedGV.getMaTCM());
            if (tcmSubjects != null) {
                subjectsToShow.addAll(tcmSubjects);
            }
        } else {
            subjectListVBox.getChildren().add(new Label("Giáo viên này chưa được gán Tổ Chuyên Môn."));
        }

        // 2. Thêm các môn đặc biệt (GDDP, HDTNHN cho các khối) vào danh sách hiển thị
        // Đảm bảo không thêm trùng nếu mã môn đặc biệt có thể trùng với mã môn từ TCM (ít khả năng)
        for (MonHocInfo specialInfo : SPECIAL_SUBJECTS_INFO) {
            if (subjectsToShow.stream().noneMatch(mh -> mh.getMaMH().equals(specialInfo.maMH))) {
                // Tạo đối tượng MonHoc "ảo" cho các môn đặc biệt
                subjectsToShow.add(new MonHoc(specialInfo.maMH, specialInfo.tenMH, null, null)); // Khối và MaTCM có thể là null
            }
        }


        if (subjectsToShow.isEmpty() && (selectedGV.getMaTCM() != null && !selectedGV.getMaTCM().isEmpty())) {
            subjectListVBox.getChildren().add(new Label("Không tìm thấy môn học nào cho TCM: " + selectedGV.getMaTCM() + "."));
        } else if (subjectsToShow.isEmpty()){
            subjectListVBox.getChildren().add(new Label("Không có môn học nào để hiển thị."));
        }


        for (MonHoc monHoc : subjectsToShow) {
            CheckBox chkSubject = new CheckBox(monHoc.getTenMH() + " (" + monHoc.getMaMH() + ")");
            chkSubject.setUserData(monHoc.getMaMH()); // Lưu mã môn học

            // Kiểm tra xem có phải là một trong các môn đặc biệt không
            boolean isSpecialSubject = SPECIAL_SUBJECTS_INFO.stream()
                    .anyMatch(info -> info.maMH.equals(monHoc.getMaMH()));

            if (isSpecialSubject) {
                // Mặc định bỏ tick cho các môn đặc biệt, trừ khi đã có cài đặt trước đó
                chkSubject.setSelected(currentSettingsForGV.getSubjectTeachingPreference().getOrDefault(monHoc.getMaMH(), false));
            } else {
                // Các môn khác từ TCM: mặc định tick, trừ khi có cài đặt trước đó
                // (getTeachingPreferenceForSubject mặc định trả về true nếu key không tồn tại trong TeacherCustomSettings)
                chkSubject.setSelected(currentSettingsForGV.getTeachingPreferenceForSubject(monHoc.getMaMH()));
            }
            subjectListVBox.getChildren().add(chkSubject);
        }
    }

    @FXML
    void handleSaveSettings(ActionEvent event) {
        if (selectedGV == null || currentSettingsForGV == null) {
            System.err.println("Lỗi: selectedGV hoặc currentSettingsForGV là null khi lưu.");
            // Hiển thị Alert cho người dùng
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
