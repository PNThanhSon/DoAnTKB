package entities;

import java.util.Objects;

public class ChiTietTKB {
    private final int thu;
    private final int tiet;
    private final String tenMonHoc; // Tên môn học thực tế được dạy trong TKB
    private final String maLop;
    private final String hoTenGV;   // Họ tên GV dạy tiết này
    private final String maGV;      // Mã GV dạy tiết này

    // Constructor private, sử dụng static factory methods
    private ChiTietTKB(int thu, int tiet, String tenMonHoc, String maLop, String hoTenGV, String maGV) {
        this.thu = thu;
        this.tiet = tiet;
        this.tenMonHoc = tenMonHoc; // Tên môn từ TKB (có thể khác tên nhóm môn)
        this.maLop = maLop;
        this.hoTenGV = hoTenGV;
        this.maGV = maGV;
    }

    /**
     * Factory method để tạo ChiTietTKB với đầy đủ thông tin cần thiết cho TKB Tổ Chuyên Môn.
     */
    public static ChiTietTKB taoChoTKBTCM(int thu, int tiet, String tenMonHocThucTe, String maLop, String hoTenGV, String maGV) {
        return new ChiTietTKB(thu, tiet, tenMonHocThucTe, maLop, hoTenGV, maGV);
    }

    /**
     * Factory method dùng cho TKB Cá Nhân (chỉ cần MaLop).
     */
    public static ChiTietTKB taoChoTKBCaNhan(int thu, int tiet, String tenMonHoc, String maLop) {
        return new ChiTietTKB(thu, tiet, tenMonHoc, maLop, null, null);
    }

    /**
     * Factory method dùng cho TKB Lớp Chủ Nhiệm (cần HoTenGV dạy).
     */
    public static ChiTietTKB taoChoTKBLopCN(int thu, int tiet, String tenMonHoc, String hoTenGV, String maGV) {
        return new ChiTietTKB(thu, tiet, tenMonHoc, null, hoTenGV, maGV);
    }


    // Getters
    public int getThu() { return thu; }
    public int getTiet() { return tiet; }
    public String getTenMonHoc() { return tenMonHoc; } // Tên môn học cụ thể trong TKB
    public String getMaLop() { return maLop; }
    public String getHoTenGV() { return hoTenGV; }
    public String getMaGV() { return maGV; }


    /**
     * Đối với TKB Tổ Chuyên Môn, chúng ta muốn hiển thị MaLop trong ô của bảng.
     * Tên GV sẽ là tiêu đề của hàng.
     * @return Mã lớp nếu có, ngược lại là chuỗi rỗng.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean hasLop = maLop != null && !maLop.isEmpty();
        boolean hasMonHoc = tenMonHoc != null && !tenMonHoc.isEmpty();
        boolean hasGV = hoTenGV != null && !hoTenGV.isEmpty();

        if (hasLop) {
            sb.append(maLop);
        }
        if (hasMonHoc) {
            if (hasLop) {
                sb.append(" - ");
            }
            sb.append(tenMonHoc);
        }
        if (hasGV) {
            if (hasLop || hasMonHoc) {
                sb.append(" - ");
            }
            sb.append(hoTenGV);
        }
        if (!hasLop && !hasMonHoc || !hasGV && !hasMonHoc) {
            return ""; // Trả về chuỗi rỗng nếu không có thông tin
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
