package entities;

public class ToChuyenMon {
    private final String maTCM;
    private final String tenTCM;
    private final String toTruong; // MaGV
    private final String toPho;    // MaGV

    public ToChuyenMon(String maTCM, String tenTCM, String toTruong, String toPho) {
        this.maTCM = maTCM;
        this.tenTCM = tenTCM;
        this.toTruong = toTruong;
        this.toPho = toPho;
    }

    public String getMaTCM() { return maTCM; }
    public String getTenTCM() { return tenTCM; }
    public String getToTruong() { return toTruong; }
    public String getToPho() { return toPho; }

    @Override
    public String toString() {
        return tenTCM; // Hiển thị trong ComboBox
    }
}
