package controllers.PhanHoiYKien;

import dao.YKienDAO;
import entities.GiaoVien;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import entities.YKien;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

public class PhanHoiYKienController {

    @FXML private BorderPane YKienBorderPane;

    @FXML private TextArea txtNoiDung;
    @FXML private CheckBox chkAnDanh;
    @FXML private ComboBox<String> cbxChonCheDo;
    @FXML private TableView<YKien> tableYKien;
    @FXML private TableColumn<YKien, String> colMaYK;
    @FXML private TableColumn<YKien, String> colNoiDung;
    @FXML private TableColumn<YKien, LocalDate> colNgayGui;
    @FXML private TableColumn<YKien, Boolean> colAnDanh;
    @FXML private TableColumn<YKien, String> colMaGV;
    @FXML private TableColumn<YKien, String> colTrangthai;
    @FXML private Text txtStatusND;
    @FXML private TextField txtSearch;
    @FXML private Button btnTim;

    private final ContextMenu contextMenu = new ContextMenu();
    private GiaoVien giaoviencurrent;

    private final YKienDAO ykienDAO = new YKienDAO(); // Có thể inject nếu dùng DI framework
    private final ObservableList<YKien> listYkienCurrent = FXCollections.observableArrayList();
    private FilteredList<YKien> filteredList;

    @FXML
    public void initialize() throws SQLException {
        Platform.runLater(() -> YKienBorderPane.requestFocus());  // tránh focus vào ô nhập liệu
        initTableColumns(); // khởi tạo bảng danh sách.
        setupContextMenu(); // tạo menu xóa, sửa khi click trái
        loadDataFromDB();

        // lắng nghe trực tiếp thay vì ấn thêm nút
        cbxChonCheDo.valueProperty().addListener((obs, oldVal, newVal) -> updateFilterPredicate());
    }


    private void initTableColumns() {
        colMaYK.setCellValueFactory(new PropertyValueFactory<>("maYK"));
        colNoiDung.setCellValueFactory(new PropertyValueFactory<>("noiDung"));
        colNgayGui.setCellValueFactory(new PropertyValueFactory<>("ngayGui"));
        colAnDanh.setCellValueFactory(new PropertyValueFactory<>("anDanh"));
        colMaGV.setCellValueFactory(cellData -> {
            YKien yk = cellData.getValue();
            String display = yk.getAnDanh() ? "Ẩn Danh" : yk.getMaGV();
            return new SimpleStringProperty(display);
        });

        colTrangthai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
    }

    // ẩn các chức năng không được quyền, k phải admin thì không tìm kiếm được và chế độ xem hạn chế
    public void ishideFuntion() {
        // làm trống rồi set lại cho chắc ))
        cbxChonCheDo.getItems().clear();

        if (!giaoviencurrent.isAdmin()) {
            txtSearch.setVisible(false);
            btnTim.setVisible(false);

            // setup cho chế độ xem
            cbxChonCheDo.getItems().add("Phản hồi của tôi");
            Platform.runLater(() -> cbxChonCheDo.getSelectionModel().select("Phản hồi của tôi"));

            //
        } else {
            cbxChonCheDo.getItems().add("Tất cả phản hồi");
            cbxChonCheDo.getItems().add("Phản hồi của tôi");
            Platform.runLater(() -> cbxChonCheDo.getSelectionModel().select("Tất cả phản hồi"));
            txtSearch.setVisible(true);
            btnTim.setVisible(true);
        }
    }
    private void loadDataFromDB() throws SQLException {
        if (giaoviencurrent == null) return;

        filteredList = new FilteredList<>(listYkienCurrent, p -> true);
        // phân quyền, bảo mật về mặt backend cho chế độ xem
        if (giaoviencurrent.isAdmin()) {
            listYkienCurrent.setAll(ykienDAO.TimKiemYKien("", giaoviencurrent));
        } else {
            String maGV = giaoviencurrent.getMaGV();
            listYkienCurrent.setAll(ykienDAO.TracuuYKien(maGV));
        }

        // sort theo ngày gửi
        FXCollections.sort(listYkienCurrent, Comparator.comparing(YKien::getNgayGui).reversed());
        tableYKien.setItems(filteredList); // truyen data vao bang
        // phân quyền, bảo mật về mặt fontend cho chức năng tìm kiếm
        ishideFuntion();
    }


    // truyền user tới
    public void getGiaoVienData(GiaoVien gv) throws SQLException {
        this.giaoviencurrent = gv;
        loadDataFromDB();
    }


    // reload data, nếu trước đó có tra cứu thì load lại data tra cứu đó nếu không sẽ load lại theo chế độ
    // dùng khi callback để cập nhật hoặc reload lại dữ liệu, có thế dùng Property để đồng bộ thay mã này
    private void reloadDATA() throws SQLException {
        loadDataFromDB();
        updateFilterPredicate();
    }

    private void setupContextMenu() {

        MenuItem suaItem = new MenuItem("Sửa Nội Dung"); // chỉ xài được khi người sửa là chính người đăng nhập
        MenuItem xoaItem = new MenuItem("Gỡ"); // admin hoặc chính người đăng nhập

        suaItem.setOnAction(e -> handle_Sua());
        xoaItem.setOnAction(e -> handle_Xoa());

        contextMenu.getItems().addAll(suaItem, xoaItem);

        tableYKien.setOnContextMenuRequested(e ->
                contextMenu.show(tableYKien, e.getScreenX(), e.getScreenY())
        );
    }

    private void updateFilterPredicate() {
        String keyword = txtSearch.getText().trim().toUpperCase();
        String cheDo = cbxChonCheDo.getValue();
        filteredList.setPredicate(yk -> {
            boolean matchSearch = keyword.isEmpty() ||
                    yk.getMaGV().toUpperCase().contains(keyword) ||
                    yk.getMaYK().toUpperCase().contains(keyword) ||
                    yk.getNoiDung().toUpperCase().contains(keyword);

            boolean matchChedo = cheDo.equals("Tất cả phản hồi") || yk.getMaGV().equals(giaoviencurrent.getMaGV());
            boolean matchAnDanh = !keyword.equals("") && yk.getAnDanh() && !(yk.getMaGV().equals(giaoviencurrent.getMaGV()));

            return matchSearch && matchChedo && !matchAnDanh;
        });
    }


    // Phương thức gọi khi người dùng gửi ý kiến (gắn event handler ở FXML hoặc code).
    @FXML
    private void handleGuiYKien() {
        String noiDung = txtNoiDung.getText().trim();
        boolean anDanh = chkAnDanh.isSelected();
        String maGV = giaoviencurrent.getMaGV();

        if (noiDung.isEmpty()) {
            showStatusNoidung("Nội dung ý kiến không được để trống!");
            return;
        }

        YKien ykien = new YKien();
        ykien.setNoiDung(noiDung);
        ykien.setAnDanh(anDanh);
        ykien.setMaGV(maGV);
        ykien.setNgayGui(LocalDate.now()); // ngày gửi lấy ngày hiện tại

        try {
            // goi DAO
            ykienDAO.themYKien(ykien);
            showStatusNoidung("Đã gửi ý kiến thành công!");

            // Xóa trắng form
            txtNoiDung.clear();
            chkAnDanh.setSelected(false);

            // Reload lại danh sách theo chế độ xem hiện tại
            reloadDATA();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể update data" + e.getMessage());
        }
    }

    @FXML
    private void handleTim() {
       updateFilterPredicate();
    }

//    private void XuLyTim() {
//        // Phân quyền chức năng tìm kiếm (backend)
//        if (!giaoviencurrent.isAdmin()) {
//            showAlert(Alert.AlertType.WARNING, "Không có quyền", "Chỉ dành cho ADMIN: ");
//            return;
//        }
//        updateFilterPredicate();
//    }

    @FXML
    private void handleLamMoi() throws SQLException {
        loadDataFromDB();
        txtSearch.clear();
        updateFilterPredicate();
    }



    // nút được tạo trong controller
    private void handle_Sua() {
        YKien selected = tableYKien.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn giáo viên để sửa.");
            return;
        }

        // xử lý quyền
        if (!selected.getMaGV().equals(giaoviencurrent.getMaGV())) {
            showAlert(Alert.AlertType.WARNING, "Không có quyền", "Chỉ chính người phản hồi mới được thực hiện chức năng này");
            return;
        }
        // điều phối giao diện mới
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/PhanHoiYKien/SuaYKienForm.fxml"));
            Parent root = loader.load();

            SuaYKienController controller = loader.getController();
            controller.getYKientoForm(selected);


            controller.setOnSuccess(() -> {
                try {
                    reloadDATA();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            Stage stage = new Stage();
            stage.setTitle("Sửa thông tin giáo viên");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // chặn tương tác cửa sổ chính
            controller.setDialogStage(stage); // truyền stage vào controller
            stage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải form sửa: " + e.getMessage());
        }
    }

    // nút được tạo trong controller
    private void handle_Xoa() {
        YKien selected = tableYKien.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn phản hồi để xóa.");
            return;
        }
        if (!giaoviencurrent.isAdmin() && !selected.getMaGV().equals(giaoviencurrent.getMaGV())) {
            showAlert(Alert.AlertType.WARNING, "Không có quyền", "Chỉ ADMIN hoặc chính người phản hồi mới được thực hiện chức năng này");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                ykienDAO.xoaYKien(selected); // DAO
                reloadDATA(); // load lai
                showAlert(Alert.AlertType.INFORMATION, "Đã xóa", "Phản hồi giáo viên đã bị xóa.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa: " + e.getMessage());
            }
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showStatusNoidung(String status) {
        txtStatusND.setText(status);
        // doi mau =)
        if (status == "Đã gửi ý kiến thành công!") {
            txtStatusND.setFill(Color.GREEN);
        }
        txtStatusND.setFill(Color.RED);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> txtStatusND.setText(""))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

}
