package entities;

public class MonHoc {
    private final String maMH; // Nên là final nếu không thay đổi sau khi tạo
    private final String tenMH;
    private final String khoi;
    private final String maTCM;

    public MonHoc(String maMH, String tenMH, String khoi, String maTCM) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.khoi = khoi;
        this.maTCM = maTCM;
    }

    // Getters
    public String getMaMH() {
        return maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public String getKhoi() {
        return khoi;
    }

    public String getMaTCM() {
        return maTCM;
    }

    @Override
    public String toString() {
        // Hiển thị trong ComboBox nếu không dùng NhomMonHocDisplay
        return tenMH + (khoi != null && !khoi.isEmpty() ? " (" + khoi + ")" : "");
    }
}
