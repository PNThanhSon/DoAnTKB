package controllers; // Hoặc package Controller bạn đang dùng

import dao.BCTKDAO;
import entities.HocKy;
import entities.ThoiKhoaBieu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class BCTKController implements Initializable {

    @FXML
    private ComboBox<HocKy> hocKyComboBox;

    @FXML
    private ComboBox<ThoiKhoaBieu> tkbComboBox;

    @FXML
    private PieChart soTietDuThieuPieChart;

    @FXML
    private Label messageLabel;

    private BCTKDAO bctkDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bctkDAO = new BCTKDAO();
        soTietDuThieuPieChart.setTitle("Thống Kê Số Tiết Dư/Thiếu Của Giáo Viên");

        loadHocKyComboBox();

        hocKyComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                loadTkbComboBox(newValue.getMaHK());
                soTietDuThieuPieChart.setData(FXCollections.observableArrayList());
                if (messageLabel != null) messageLabel.setText("Vui lòng chọn Thời Khóa Biểu.");
            } else {
                tkbComboBox.getItems().clear();
                tkbComboBox.setDisable(true);
                soTietDuThieuPieChart.setData(FXCollections.observableArrayList());
            }
        });

        tkbComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null && hocKyComboBox.getSelectionModel().getSelectedItem() != null) {
                updatePieChartData(hocKyComboBox.getSelectionModel().getSelectedItem().getMaHK(), newValue.getMaTKB());
            } else {
                soTietDuThieuPieChart.setData(FXCollections.observableArrayList());
            }
        });

        tkbComboBox.setDisable(true);
    }

    private void loadHocKyComboBox() {
        ObservableList<HocKy> hocKyList = FXCollections.observableArrayList(bctkDAO.getAllHocKy());
        hocKyComboBox.setItems(hocKyList);
        if (hocKyList.isEmpty() && messageLabel != null) {
            messageLabel.setText("Không tìm thấy dữ liệu Học Kỳ.");
        }
    }

    private void loadTkbComboBox(String maHK) {
        ObservableList<ThoiKhoaBieu> tkbList = FXCollections.observableArrayList(bctkDAO.getThoiKhoaBieuByMaHK(maHK));
        tkbComboBox.setItems(tkbList);
        tkbComboBox.setDisable(tkbList.isEmpty());
        if (tkbList.isEmpty() && messageLabel != null) {
            messageLabel.setText("Không có Thời Khóa Biểu nào cho Học Kỳ đã chọn.");
        } else if (messageLabel != null) {
            messageLabel.setText("Vui lòng chọn Thời Khóa Biểu.");
        }
    }

    private void updatePieChartData(String maHK, String maTKB) {
        if (maHK == null || maHK.isEmpty() || maTKB == null || maTKB.isEmpty()) {
            if (messageLabel != null) messageLabel.setText("Vui lòng chọn đầy đủ Học Kỳ và Thời Khóa Biểu.");
            soTietDuThieuPieChart.setData(FXCollections.observableArrayList());
            return;
        }

        Map<String, Integer> stats = bctkDAO.getSoTietDuThieuStats(maHK, maTKB);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        int totalTeachersWithValue = 0;

        // Cập nhật tên các mục PieChart.Data để khớp với key mới và ý nghĩa
        if (stats.getOrDefault("ThieuNhieu", 0) > 0) {
            pieChartData.add(new PieChart.Data("Thiếu nhiều (<= -3 tiết)", stats.get("ThieuNhieu")));
            totalTeachersWithValue += stats.get("ThieuNhieu");
        }
        if (stats.getOrDefault("Thieu", 0) > 0) {
            pieChartData.add(new PieChart.Data("Thiếu (-1 hoặc -2 tiết)", stats.get("Thieu")));
            totalTeachersWithValue += stats.get("Thieu");
        }
        if (stats.getOrDefault("Du", 0) > 0) {
            pieChartData.add(new PieChart.Data("Đủ tiết", stats.get("Du")));
            totalTeachersWithValue += stats.get("Du");
        }
        if (stats.getOrDefault("DuVua", 0) > 0) {
            pieChartData.add(new PieChart.Data("Dư vừa (1 hoặc 2 tiết)", stats.get("DuVua")));
            totalTeachersWithValue += stats.get("DuVua");
        }
        if (stats.getOrDefault("DuNhieu", 0) > 0) {
            pieChartData.add(new PieChart.Data("Dư nhiều (>= 3 tiết)", stats.get("DuNhieu")));
            totalTeachersWithValue += stats.get("DuNhieu");
        }

        soTietDuThieuPieChart.setData(pieChartData);

        if (totalTeachersWithValue == 0) {
            if (messageLabel != null) messageLabel.setText("Không có dữ liệu thống kê cho lựa chọn này.");
            soTietDuThieuPieChart.setTitle("Thống Kê Số Tiết Dư/Thiếu (Không có dữ liệu)");
        } else {
            if (messageLabel != null) messageLabel.setText("Hiển thị thống kê.");
            soTietDuThieuPieChart.setTitle("Thống Kê Số Tiết Dư/Thiếu Của Giáo Viên");
        }

        final int finalTotalTeachers = totalTeachersWithValue;
        if (finalTotalTeachers > 0) {
            pieChartData.forEach(data -> {
                String percentage = String.format("%.1f%%", (data.getPieValue() / finalTotalTeachers) * 100);
                String tooltipText = String.format("%s: %d GV (%s)", data.getName(), (int)data.getPieValue(), percentage);

                Tooltip tooltip = new Tooltip(tooltipText);
                Tooltip.install(data.getNode(), tooltip);

                data.getNode().setOnMouseEntered(event -> data.getNode().setStyle("-fx-cursor: hand; -fx-opacity: 0.8;"));
                data.getNode().setOnMouseExited(event -> data.getNode().setStyle("-fx-cursor: default; -fx-opacity: 1.0;"));
            });
        }

        soTietDuThieuPieChart.setLabelLineLength(20);
        soTietDuThieuPieChart.setLabelsVisible(true);
    }
}
