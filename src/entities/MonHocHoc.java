package entities;

/**
 * Data Transfer Object (DTO) để lưu trữ thông tin môn học mà một lớp cần học
 * và tổng số tiết tương ứng trong một học kỳ.
 * Thông tin này thường được lấy từ bảng HOC.
 */
public class MonHocHoc {
    private String maMH;
    private String tenMH; // Có thể lấy thêm tên môn học để hiển thị
    private int tongSoTiet;
    private String maTCM; // Mã tổ chuyên môn quản lý môn học này

    public MonHocHoc(String maMH, String tenMH, int tongSoTiet, String maTCM) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.tongSoTiet = tongSoTiet;
        this.maTCM = maTCM;
    }

    // Getters
    public String getMaMH() {
        return maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public int getTongSoTiet() {
        return tongSoTiet;
    }

    public String getMaTCM() {
        return maTCM;
    }

    // Setters (nếu cần)
    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public void setTongSoTiet(int tongSoTiet) {
        this.tongSoTiet = tongSoTiet;
    }

    public void setMaTCM(String maTCM) {
        this.maTCM = maTCM;
    }

    @Override
    public String toString() {
        return "MonHocHoc{" +
                "maMH='" + maMH + '\'' +
                ", tenMH='" + tenMH + '\'' +
                ", tongSoTiet=" + tongSoTiet +
                ", maTCM='" + maTCM + '\'' +
                '}';
    }
}
