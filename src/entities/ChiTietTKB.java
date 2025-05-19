// Trong file entities/ChiTietTKB.java

package entities;

public class ChiTietTKB {
    private int thu;        // 2-7
    private int tiet;       // 1-5 (hoặc 1-10 nếu bạn không chia buổi)
    private String tenMonHoc; // Sẽ được lấy từ JOIN với bảng MONHOC
    private String maLop;
    private String hoTenGV;

    // Constructor này sẽ được sử dụng trong DAO
    public ChiTietTKB(int thu, int tiet, String tenMonHoc, String maLop, String hoTenGV) {
        this.thu = thu;
        this.tiet = tiet;
        this.tenMonHoc = tenMonHoc;
        this.maLop = maLop;
        this.hoTenGV = hoTenGV;
    }


    public int getThu() {
        return thu;
    }
    public int getTiet() {
        return tiet;
    }
    public String getTenMonHoc() {
        return tenMonHoc;
    }
    public String getMaLop() {
        return maLop;
    }

    // Phương thức này giúp hiển thị thông tin trong ô TableView theo yêu cầu
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
}