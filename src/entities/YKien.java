package entities;

import java.time.LocalDate;

public class YKien {
    private String maYK;
    private String noiDung;
    private LocalDate ngayGui;
    private Boolean anDanh;
    private String maGV;
    private String trangThai; // <-- Thêm thuộc tính mới

    public YKien() {
    }

    public YKien(String maYK, String noiDung, LocalDate ngayGui, Boolean anDanh, String maGV, String trangThai) {
        this.maYK = maYK;
        this.noiDung = noiDung;
        this.ngayGui = ngayGui;
        this.anDanh = anDanh;
        this.maGV = maGV;
        this.trangThai = trangThai; // <-- Gán giá trị
    }

    public String getMaYK() {
        return maYK;
    }

    public void setMaYK(String maYK) {
        this.maYK = maYK;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public LocalDate getNgayGui() {
        return ngayGui;
    }

    public void setNgayGui(LocalDate ngayGui) {
        this.ngayGui = ngayGui;
    }

    public Boolean getAnDanh() {
        return anDanh;
    }

    public void setAnDanh(Boolean anDanh) {
        this.anDanh = anDanh;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
