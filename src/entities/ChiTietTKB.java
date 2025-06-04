package entities;

import java.util.Objects;

public class ChiTietTKB {
    private final int thu;
    private final int tiet;
    private final String tenMonHoc; // Tên môn học thực tế được dạy trong TKB
    private String maMH;
    private final String maLop;
    private final String hoTenGV;   // Họ tên GV dạy tiết này
    private final String maGV;      // Mã GV dạy tiết này
    private final int flag;

    // Constructor private, sử dụng static factory methods
    private ChiTietTKB(int thu, int tiet, String tenMonHoc, String maMH, String maLop, String hoTenGV, String maGV, int flag) {
        this.thu = thu;
        this.tiet = tiet;
        this.tenMonHoc = tenMonHoc; // Tên môn từ TKB (có thể khác tên nhóm môn)
        this.maMH = maMH;
        this.maLop = maLop;
        this.hoTenGV = hoTenGV;
        this.maGV = maGV;
        this.flag = flag; //1: TKB Tổ Chuyên Môn, 2: TKB Cá Nhân, 3: TKB Lớp Chủ Nhiệm
    }

    /**
     * Factory method để tạo ChiTietTKB với đầy đủ thông tin cần thiết cho TKB Tổ Chuyên Môn.
     */
    public static ChiTietTKB taoChoTKBTCM(int thu, int tiet, String tenMonHocThucTe, String maLop, String hoTenGV, String maGV, int flag) {
        return new ChiTietTKB(thu, tiet, tenMonHocThucTe, null, maLop, hoTenGV, maGV, flag);
    }

    /**
     * Factory method dùng cho TKB Cá Nhân (chỉ cần MaLop).
     */
    public static ChiTietTKB taoChoTKBCaNhan(int thu, int tiet, String tenMonHoc, String maLop, int flag) {
        return new ChiTietTKB(thu, tiet, tenMonHoc, null, maLop, null, null, flag);
    }

    /**
     * Factory method dùng cho TKB Lớp Chủ Nhiệm (cần HoTenGV dạy).
     */
    public static ChiTietTKB taoChoTKBLopCN(int thu, int tiet, String tenMonHoc, String hoTenGV, String maGV, int flag) {
        return new ChiTietTKB(thu, tiet, tenMonHoc, null, null, hoTenGV, maGV, flag);
    }

    /**
     * Factory method dùng riêng cho việc xếp thời khóa biểu tự động,
     * bao gồm cả mã môn học.
     */
    public static ChiTietTKB taoChoXepLichTuDong(int thu, int tiet, String tenMonHoc, String maLop, String hoTenGV, String maGV, String maMH, int flag) {
        return new ChiTietTKB(thu, tiet, tenMonHoc, maMH, maLop, hoTenGV, maGV, flag);
    }


    // Getters
    public int getThu() { return thu; }
    public int getTiet() { return tiet; }
    public String getTenMonHoc() { return tenMonHoc; } // Tên môn học cụ thể trong TKB
    public String getMaLop() { return maLop; }
    public String getHoTenGV() { return hoTenGV; }
    public String getMaGV() { return maGV; }
    public String getMaMH() {
        return maMH;
    }
    public int getFlag() { return flag; }

    /**
     * Đối với TKB Tổ Chuyên Môn, chúng ta muốn hiển thị MaLop trong ô của bảng.
     * Tên GV sẽ là tiêu đề của hàng.
     * @return Mã lớp nếu có, ngược lại là chuỗi rỗng.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        switch (flag) {
            case 1:
                sb.append(maLop);
                break;
            case 2:
                sb.append(maLop);
                sb.append(" - ");
                sb.append(tenMonHoc);
                break;
            case 3:
                sb.append(tenMonHoc);
                sb.append(" - ");
                sb.append(hoTenGV);
                break;
            default:
                return "";
        }
        return sb.toString();
    }

    // Equals và HashCode nếu bạn dùng trong Set hoặc Map (dựa trên các trường chính)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietTKB that = (ChiTietTKB) o;
        return thu == that.thu &&
                tiet == that.tiet &&
                Objects.equals(maLop, that.maLop) &&
                Objects.equals(maGV, that.maGV) && // So sánh cả maGV
                Objects.equals(tenMonHoc, that.tenMonHoc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thu, tiet, tenMonHoc, maLop, maGV);
    }


}
