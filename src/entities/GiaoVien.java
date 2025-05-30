package entities;// Giả sử các file nằm trực tiếp trong thư mục src (default package)

public class GiaoVien {
    private String maGV;
    private String hoGV;
    private String tenGV;
    private String gioiTinh;
    private String chuyenMon;
    private String maTCM;
    private Integer soTietQuyDinh;
    private Integer soTietThucHien;
    private Integer soTietDuThieu;
    private String email;
    private String sdt;
    private String matKhau;
    private String ghiChu;
    private VaiTroGV vaiTro;

    // Constructor cho đăng nhập
    public GiaoVien(String maGV, String hoGV, String tenGV, String gioiTinh,
                    String chuyenMon, String maTCM, Integer soTietQuyDinh,
                    Integer soTietThucHien, Integer soTietDuThieu, String email,
                    String sdt, String matKhau, String ghiChu, VaiTroGV vaiTro) {
        this.maGV = maGV;
        this.hoGV = hoGV;
        this.tenGV = tenGV;
        this.gioiTinh = gioiTinh;
        this.chuyenMon = chuyenMon;
        this.maTCM = maTCM;
        this.soTietQuyDinh = soTietQuyDinh;
        this.soTietThucHien = soTietThucHien;
        this.soTietDuThieu = soTietDuThieu;
        this.email = email;
        this.sdt = sdt;
        this.matKhau = matKhau;
        this.ghiChu = ghiChu;
        this.vaiTro = vaiTro; //QUAN TRỌNG ĐỂ PHÂN QUYỀN!!!
    }

    // Constructor cho việc thêm mới giáo viên
    public GiaoVien(String maGV, String hoGV, String tenGV, String gioiTinh,
                    String chuyenMon, String maTCM, Integer soTietQuyDinh,
                    Integer soTietThucHien, Integer soTietDuThieu, String email,
                    String sdt, String matKhau, String ghiChu) {
        this.maGV = maGV;
        this.hoGV = hoGV;
        this.tenGV = tenGV;
        this.gioiTinh = gioiTinh;
        this.chuyenMon = chuyenMon;
        this.maTCM = maTCM;
        this.soTietQuyDinh = soTietQuyDinh;
        this.soTietThucHien = soTietThucHien;
        this.soTietDuThieu = soTietDuThieu;
        this.email = email;
        this.sdt = sdt;
        this.matKhau = matKhau;
        this.ghiChu = ghiChu;
    }
    public GiaoVien(){}

    //
    // Getters
    public String getMaGV() { return maGV; }
    public String getHoGV() { return hoGV; }
    public String getTenGV() { return tenGV; }
    public String getGioiTinh() { return gioiTinh; }
    public String getChuyenMon() { return chuyenMon; }
    public String getMaTCM() { return maTCM; }
    public Integer getSoTietQuyDinh() { return soTietQuyDinh; }
    public Integer getSoTietThucHien() { return soTietThucHien; }
    public Integer getSoTietDuThieu() { return soTietDuThieu; }
    public String getEmail() { return email; }
    public String getSdt() { return sdt; }
    public String getMatKhau() { return matKhau; } // Cần cho việc xác thực
    public String getGhiChu() { return ghiChu; }
    public VaiTroGV getVaiTro() { return vaiTro; }

    // Setters (Thêm nếu bạn cần cập nhật thông tin đối tượng sau khi tạo)
//    public void setMaGV(String maGV) { this.maGV = maGV; }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setHoGV(String hoGV) { this.hoGV = hoGV; }
    public void setTenGV(String tenGV) { this.tenGV = tenGV; }

    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }
    public void setMaTCM(String maTCM) { this.maTCM = maTCM; }
    public void setSoTietQuyDinh(Integer soTietQuyDinh) {
        this.soTietQuyDinh = soTietQuyDinh;
    }
    public void setSoTietThucHien(Integer soTietThucHien) {
        this.soTietThucHien = soTietThucHien;
    }
    public void setSoTietDuThieu(Integer soTietDuThieu) {
        this.soTietDuThieu = soTietDuThieu;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return hoGV + " " + tenGV;
    }

    public boolean isAdmin() {
        return this.getVaiTro() == VaiTroGV.ADMIN;
    }

    public boolean isToChuyenMon() {
        return this.getVaiTro() == VaiTroGV.TCM;
    }

    public boolean isGVCN() {
        return this.getVaiTro() == VaiTroGV.GVCN;
    }

    public boolean isGVThuong() {
        return this.getVaiTro() == VaiTroGV.GV;
    }
}
