package controllers.XepTKBTuDong;

import dao.XepTKBDAO;
import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CaiDatLopController {

    @FXML private Label titleLabel;
    @FXML private TextField searchMonHocTextField;
    @FXML private ScrollPane subjectsScrollPane;
    @FXML private VBox subjectAssignmentsVBox;
    @FXML private Button btnSaveSettings;
    @FXML private Button btnCancel;

    private Lop selectedLop;
    private Map<String, Map<String, List<String>>> classTeacherAssignmentsMap; // Tham chiếu đến map chính
    private List<MonHocHoc> subjectsForClass; // Danh sách môn lớp này học (từ HOC)
    private List<GiaoVien> allAvailableTeachers; // Danh sách GV đã lọc (tham gia TKB)
    private Map<String, TeacherCustomSettings> teacherCustomSettings; // Cài đặt riêng của từng GV

    private final GiaoVien AUTO_SELECT_GV_PLACEHOLDER = new GiaoVien(); // Placeholder cho "Để thuật toán tự chọn"

    public void initialize() {
        AUTO_SELECT_GV_PLACEHOLDER.setMaGV("__AUTO__"); // Mã đặc biệt
        AUTO_SELECT_GV_PLACEHOLDER.setHoGV("Để thuật toán");
        AUTO_SELECT_GV_PLACEHOLDER.setTenGV("tự chọn");

        searchMonHocTextField.textProperty().addListener((obs, oldVal, newVal) -> filterSubjectDisplay(newVal));
    }

    public void initData(Lop lop, HocKy hk,
                         Map<String, Map<String, List<String>>> assignmentsMap,
                         List<MonHocHoc> subjectsOfThisClass,
                         List<GiaoVien> allTeachers,
                         Map<String, TeacherCustomSettings> teacherSettings,
                         XepTKBDAO dao) {
        this.selectedLop = lop;
        this.classTeacherAssignmentsMap = assignmentsMap;
        this.subjectsForClass = subjectsOfThisClass;
        this.allAvailableTeachers = allTeachers; // Đây là danh sách GV đã được lọc bởi ChuanBiController
        this.teacherCustomSettings = teacherSettings;

        titleLabel.setText("Cài Đặt Phân Công GV cho Lớp: " + selectedLop.getTenLop() + " (" + selectedLop.getMaLop() + ")");
        populateSubjectAssignments();
    }

    private void filterSubjectDisplay(String searchText) {
        String lowerCaseSearchText = searchText != null ? searchText.toLowerCase().trim() : "";
        for (Node node : subjectAssignmentsVBox.getChildren()) {
            if (node instanceof VBox subjectPane) { // Mỗi môn là một VBox
                Label subjectLabel = (Label) subjectPane.getChildren().getFirst(); // Giả sử Label là con đầu tiên
                boolean matches = lowerCaseSearchText.isEmpty() ||
                        subjectLabel.getText().toLowerCase().contains(lowerCaseSearchText);
                subjectPane.setVisible(matches);
                subjectPane.setManaged(matches);
            }
        }
    }

    private void populateSubjectAssignments() {
        subjectAssignmentsVBox.getChildren().clear();
        if (subjectsForClass == null || subjectsForClass.isEmpty()) {
            subjectAssignmentsVBox.getChildren().add(new Label("Không có môn học nào được phân công cho lớp này."));
            return;
        }

        // Lấy cài đặt hiện tại cho lớp này (nếu có)
        Map<String, List<String>> currentAssignmentsForClass = classTeacherAssignmentsMap.getOrDefault(selectedLop.getMaLop(), new HashMap<>());

        for (MonHocHoc monHocHoc : subjectsForClass) {
            VBox subjectPane = new VBox(5);
            subjectPane.setStyle("-fx-padding: 8px; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
            subjectPane.setUserData(monHocHoc.getMaMH()); // Lưu MaMH để tìm kiếm

            Label subjectLabel = new Label(monHocHoc.getTenMH() + " (" + monHocHoc.getMaMH() + ")");
            subjectLabel.setStyle("-fx-font-weight: bold;");
            subjectPane.getChildren().add(subjectLabel);

            // Danh sách GV đã được gán cho môn này của lớp này
            List<String> assignedGvIds = currentAssignmentsForClass.getOrDefault(monHocHoc.getMaMH(), new ArrayList<>());

            // VBox để chứa các ComboBox chọn giáo viên
            VBox teacherSelectorsVBox = new VBox(5);
            subjectPane.getChildren().add(teacherSelectorsVBox);

            if (assignedGvIds.isEmpty()) {
                // Nếu chưa có GV nào được gán (hoặc là "Để thuật toán tự chọn"), hiển thị 1 ComboBox
                addTeacherSelector(teacherSelectorsVBox, monHocHoc, null);
            } else {
                // Hiển thị ComboBox cho mỗi GV đã được gán
                for (String maGV : assignedGvIds) {
                    addTeacherSelector(teacherSelectorsVBox, monHocHoc, maGV);
                }
            }

            Button btnAddTeacher = new Button("+ Thêm GV");
            btnAddTeacher.setOnAction(e -> addTeacherSelector(teacherSelectorsVBox, monHocHoc, null));
            subjectPane.getChildren().add(btnAddTeacher);

            subjectAssignmentsVBox.getChildren().add(subjectPane);
        }
    }

    private void addTeacherSelector(VBox parentVBox, MonHocHoc monHocHoc, String selectedMaGV) {
        HBox selectorHBox = new HBox(5);
        selectorHBox.setAlignment(Pos.CENTER_LEFT);

        ComboBox<GiaoVien> gvComboBox = new ComboBox<>();
        gvComboBox.setPrefWidth(300);

        // Lọc danh sách giáo viên:
        // 1. Thuộc TCM của môn học
        // 2. Được phép dạy môn này (theo TeacherCustomSettings)
        ObservableList<GiaoVien> teacherOptions = allAvailableTeachers.stream()
                .filter(gv -> {
                    if (gv.getMaTCM() == null || !gv.getMaTCM().equals(monHocHoc.getMaTCM())) {
                        return false; // Không thuộc TCM
                    }
                    TeacherCustomSettings settings = teacherCustomSettings.get(gv.getMaGV());
                    // Nếu không có setting riêng, hoặc setting cho phép dạy môn này
                    return (settings == null) || settings.getTeachingPreferenceForSubject(monHocHoc.getMaMH());
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        teacherOptions.addFirst(AUTO_SELECT_GV_PLACEHOLDER); // Luôn có lựa chọn "Để thuật toán"
        gvComboBox.setItems(teacherOptions);

        gvComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(GiaoVien gv) {
                if (gv == null) return null;
                if (AUTO_SELECT_GV_PLACEHOLDER.getMaGV().equals(gv.getMaGV())) {
                    return AUTO_SELECT_GV_PLACEHOLDER.getHoGV() + " " + AUTO_SELECT_GV_PLACEHOLDER.getTenGV();
                }
                return gv.getHoGV() + " " + gv.getTenGV() + " (" + gv.getMaGV() + ")";
            }

            @Override
            public GiaoVien fromString(String string) {
                return null;
            }
        });

        // Chọn giáo viên đã được gán trước (nếu có)
        if (selectedMaGV != null) {
            teacherOptions.stream()
                    .filter(gv -> selectedMaGV.equals(gv.getMaGV()))
                    .findFirst()
                    .ifPresent(gvComboBox::setValue);
        } else {
            gvComboBox.setValue(AUTO_SELECT_GV_PLACEHOLDER);
        }

        Button btnRemoveTeacher = new Button("-");
        btnRemoveTeacher.setOnAction(e -> {
            // Chỉ xóa nếu còn nhiều hơn 1 selector, hoặc selector hiện tại không phải là "Để thuật toán tự chọn"
            // và parentVBox có nhiều hơn 1 HBox (selector)
            if (parentVBox.getChildren().size() > 1) {
                parentVBox.getChildren().remove(selectorHBox);
            } else { // Nếu chỉ còn 1, reset nó về "Để thuật toán"
                gvComboBox.setValue(AUTO_SELECT_GV_PLACEHOLDER);
            }
        });

        selectorHBox.getChildren().addAll(gvComboBox, btnRemoveTeacher);
        parentVBox.getChildren().add(selectorHBox);
    }


    @FXML
    void handleSaveSettings(ActionEvent event) {
        Map<String, List<String>> assignmentsForThisClass = new HashMap<>();

        for (Node subjectPaneNode : subjectAssignmentsVBox.getChildren()) {
            if (subjectPaneNode instanceof VBox subjectPane) {
                String maMH = (String) subjectPane.getUserData();
                if (maMH == null) continue;

                List<String> assignedGvsForSubject = new ArrayList<>();
                VBox teacherSelectorsVBox = (VBox) subjectPane.getChildren().get(1); // VBox chứa các HBox selector

                for (Node selectorNode : teacherSelectorsVBox.getChildren()) {
                    if (selectorNode instanceof HBox selectorHBox) {
                        ComboBox<GiaoVien> gvComboBox = (ComboBox<GiaoVien>) selectorHBox.getChildren().getFirst();
                        GiaoVien selectedGv = gvComboBox.getValue();

                        if (selectedGv != null && !AUTO_SELECT_GV_PLACEHOLDER.getMaGV().equals(selectedGv.getMaGV())) {
                            if (!assignedGvsForSubject.contains(selectedGv.getMaGV())) { // Tránh trùng lặp
                                assignedGvsForSubject.add(selectedGv.getMaGV());
                            }
                        }
                    }
                }
                // Nếu có giáo viên được chọn, lưu danh sách đó.
                // Nếu không có ai được chọn (tất cả là "Để thuật toán"), danh sách sẽ rỗng,
                // có nghĩa là để thuật toán tự chọn cho môn này.
                if (!assignedGvsForSubject.isEmpty()) {
                    assignmentsForThisClass.put(maMH, assignedGvsForSubject);
                } else {
                    // Nếu muốn xóa hẳn key khi không có GV nào được chọn (để thuật toán quyết)
                    assignmentsForThisClass.remove(maMH);
                }
            }
        }

        // Cập nhật map chính
        if (assignmentsForThisClass.isEmpty()) {
            classTeacherAssignmentsMap.remove(selectedLop.getMaLop());
        } else {
            classTeacherAssignmentsMap.put(selectedLop.getMaLop(), assignmentsForThisClass);
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
    