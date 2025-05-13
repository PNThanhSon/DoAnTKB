// Giả sử các file nằm trực tiếp trong thư mục src (default package)

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController {

    @FXML
    private Label maGvLabel;

    @FXML
    private Label hoTenLabel;

    @FXML
    private Label gioiTinhLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label sdtLabel;

    @FXML
    private Label chuyenMonLabel;

    @FXML
    private Label ghiChuLabel;

    private GiaoVien currentGiaoVien;

    @FXML
    public void initialize() {
        // Có thể thực hiện các thiết lập ban đầu nếu cần
    }

    /**
     * Nhận đối tượng GiaoVien và hiển thị thông tin lên các Label.
     * @param giaoVien Đối tượng GiaoVien chứa thông tin người dùng.
     */
    public void setGiaoVienData(GiaoVien giaoVien) {
        this.currentGiaoVien = giaoVien;
        if (giaoVien != null) {
            maGvLabel.setText(giaoVien.getMaGV() != null ? giaoVien.getMaGV() : "N/A");
            hoTenLabel.setText((giaoVien.getHoGV() != null ? giaoVien.getHoGV() : "") + " " +
                    (giaoVien.getTenGV() != null ? giaoVien.getTenGV() : ""));
            gioiTinhLabel.setText(giaoVien.getGioiTinh() != null ? giaoVien.getGioiTinh() : "N/A");
            emailLabel.setText(giaoVien.getEmail() != null ? giaoVien.getEmail() : "N/A");
            sdtLabel.setText(giaoVien.getSdt() != null ? giaoVien.getSdt() : "N/A");
            chuyenMonLabel.setText(giaoVien.getChuyenMon() != null ? giaoVien.getChuyenMon() : "N/A");
            ghiChuLabel.setText(giaoVien.getGhiChu() != null ? giaoVien.getGhiChu() : "N/A");
        } else {
            // Xử lý trường hợp không có thông tin giáo viên (ví dụ: đặt tất cả là "N/A")
            maGvLabel.setText("N/A");
            hoTenLabel.setText("N/A");
            gioiTinhLabel.setText("N/A");
            emailLabel.setText("N/A");
            sdtLabel.setText("N/A");
            chuyenMonLabel.setText("N/A");
            ghiChuLabel.setText("N/A");
        }
    }

    // TODO: Thêm hàm xử lý cho nút "Sửa Thông Tin Cá Nhân" sau này
    // @FXML
    // private void handleSuaThongTin() {
    //     System.out.println("Nút Sửa Thông Tin được nhấn. Giáo viên hiện tại: " + (currentGiaoVien != null ? currentGiaoVien.getMaGV() : "null"));
    //     // Mở một form/dialog khác để cho phép sửa thông tin
    // }
}
