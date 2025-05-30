package entities;

public class Lop {
    private final String maLop;
    private final String tenLop;
    private final String khoi;
    private final String gvcn; // MaGV của giáo viên chủ nhiệm

    public Lop(String maLop, String tenLop, String khoi, String gvcn) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.khoi = khoi;
        this.gvcn = gvcn;
    }

    public String getMaLop() { return maLop; }
    public String getTenLop() { return tenLop; }
    public String getKhoi() { return khoi; }
    public String getGvcn() { return gvcn; }

    @Override
    public String toString() {
        return maLop; // Hiển thị trong ComboBox
    }
}
