package entities;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Đại diện cho một hàng trong GridPane TKB của Tổ Chuyên Môn.
 * Mỗi hàng là một giáo viên, và chứa lịch dạy (ChiTietTKB) cho mỗi tiết.
 */
public class GiaoVienLichDayData {
    private final StringProperty maGVProperty;
    private final StringProperty hoTenGVProperty;

    // Mảng 2 chiều để lưu ChiTietTKB: [thứ_index][tiết_index]
    // Thứ 2 -> index 0 (thuDbValue 2), ..., Thứ 7 -> index 5 (thuDbValue 7)
    // Tiết 1 -> index 0 (tietDbValue 1), ..., Tiết 5 -> index 4 (tietDbValue 5)
    private final ObjectProperty<ChiTietTKB>[][] lichDayTuan;

    private static final int SO_NGAY_TRONG_TUAN = 6; // Thứ 2 đến Thứ 7
    private static final int SO_TIET_MOI_BUOI = 5;  // 5 tiết mỗi buổi

    @SuppressWarnings("unchecked") // Cần thiết cho việc khởi tạo mảng generic
    public GiaoVienLichDayData(String maGV, String hoTenGV) {
        this.maGVProperty = new SimpleStringProperty(maGV);
        this.hoTenGVProperty = new SimpleStringProperty(hoTenGV);

        this.lichDayTuan = (ObjectProperty<ChiTietTKB>[][]) new ObjectProperty[SO_NGAY_TRONG_TUAN][SO_TIET_MOI_BUOI];
        for (int i = 0; i < SO_NGAY_TRONG_TUAN; i++) {
            for (int j = 0; j < SO_TIET_MOI_BUOI; j++) {
                this.lichDayTuan[i][j] = new SimpleObjectProperty<>(null);
            }
        }
    }

    // Property Getters
    public StringProperty maGVProperty() { return maGVProperty; }
    public StringProperty hoTenGVProperty() { return hoTenGVProperty; }

    // Setter để đặt ChiTietTKB vào đúng ô
    public void setChiTiet(int thuDbValue, int tietDbValue, ChiTietTKB chiTiet) {
        // Chuyển đổi giá trị từ CSDL (Thứ 2-7, Tiết 1-5) sang chỉ số mảng (0-5, 0-4)
        int thuIndex = thuDbValue - 2; // Thứ 2 (value 2) -> index 0
        int tietIndex = tietDbValue - 1; // Tiết 1 (value 1) -> index 0

        if (thuIndex >= 0 && thuIndex < SO_NGAY_TRONG_TUAN &&
                tietIndex >= 0 && tietIndex < SO_TIET_MOI_BUOI) {
            this.lichDayTuan[thuIndex][tietIndex].set(chiTiet);
        } else {
            System.err.println("Lỗi setChiTiet: Thứ hoặc Tiết không hợp lệ. Thu: " + thuDbValue + ", Tiết: " + tietDbValue);
        }
    }

    // Getter để lấy ChiTietTKB từ ô cụ thể
    public ChiTietTKB getChiTiet(int thuDbValue, int tietDbValue) {
        int thuIndex = thuDbValue - 2;
        int tietIndex = tietDbValue - 1;

        if (thuIndex >= 0 && thuIndex < SO_NGAY_TRONG_TUAN &&
                tietIndex >= 0 && tietIndex < SO_TIET_MOI_BUOI) {
            return this.lichDayTuan[thuIndex][tietIndex].get();
        }
        return null;
    }

    // Getter cho ObjectProperty (nếu cần bind trực tiếp trong trường hợp phức tạp hơn)
    public ObjectProperty<ChiTietTKB> chiTietProperty(int thuDbValue, int tietDbValue) {
        int thuIndex = thuDbValue - 2;
        int tietIndex = tietDbValue - 1;

        if (thuIndex >= 0 && thuIndex < SO_NGAY_TRONG_TUAN &&
                tietIndex >= 0 && tietIndex < SO_TIET_MOI_BUOI) {
            return this.lichDayTuan[thuIndex][tietIndex];
        }
        return null; // Hoặc ném lỗi
    }


    // Getters thông thường
    public String getMaGV() { return maGVProperty.get(); }
    public String getHoTenGV() { return hoTenGVProperty.get(); }
}
