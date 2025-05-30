package entities;

public class Lop {
    private String maLop;
    private String tenLop;
    private String khoi;
    private String gvcn; // MaGV của giáo viên chủ nhiệm

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

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public void setKhoi(String khoi) {
        this.khoi = khoi;
    }

    public void setGvcn(String gvcn) {
        this.gvcn = gvcn;
    }

    @Override
    public String toString() {
        return maLop; // Hiển thị trong ComboBox
    }
}
