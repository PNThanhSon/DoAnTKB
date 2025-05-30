package entities;

public class ToChuyenMon {
    private String maTCM;
    private String tenTCM;
    private String toTruong;
    private String toPho;

    // Constructor
    public ToChuyenMon() {}

    public ToChuyenMon(String maTCM, String tenTCM, String toTruong, String toPho) {
        this.maTCM = maTCM;
        this.tenTCM = tenTCM;
        this.toTruong = toTruong;
        this.toPho = toPho;
    }

    // Getters và Setters
    public String getMaTCM() {
        return maTCM;
    }

    public void setMaTCM(String maTCM) {
        this.maTCM = maTCM;
    }

    public String getTenTCM() {
        return tenTCM;
    }

    public void setTenTCM(String tenTCM) {
        this.tenTCM = tenTCM;
    }

    public String getToTruong() {
        return toTruong;
    }

    public void setToTruong(String toTruong) {
        this.toTruong = toTruong;
    }

    public String getToPho() {
        return toPho;
    }

    public void setToPho(String toPho) {
        this.toPho = toPho;
    }

    @Override
    public String toString() {
        return tenTCM; // Dùng khi set ComboBox hiển thị tên tổ
    }
}
