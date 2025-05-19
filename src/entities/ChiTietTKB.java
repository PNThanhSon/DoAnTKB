package entities;

/**
 * Đại diện cho một chi tiết cụ thể trong Thời Khóa Biểu,
 * ví dụ: một tiết học của một môn, tại một lớp, do một giáo viên dạy, vào một thứ và tiết nhất định.
 */
public class ChiTietTKB {
    private int thu;        // Ngày trong tuần, ví dụ: 2 (Thứ Hai) đến 7 (Thứ Bảy)
    private int tiet;       // Tiết học trong ngày, ví dụ: 1 đến 5 (cho mỗi buổi)
    private String tenMonHoc; // Tên của môn học
    private String maLop;     // Mã của lớp học
    // Bạn có thể thêm các thuộc tính khác như maPhongHoc nếu cần

    /**
     * Constructor để tạo một đối tượng ChiTietTKB.
     * @param thu Ngày trong tuần (2-7).
     * @param tiet Tiết học (ví dụ: 1-5).
     * @param tenMonHoc Tên của môn học.
     * @param maLop Mã của lớp học.
     */
    public ChiTietTKB(int thu, int tiet, String tenMonHoc, String maLop) {
        this.thu = thu;
        this.tiet = tiet;
        this.tenMonHoc = tenMonHoc;
        this.maLop = maLop;
    }

    // Getters
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

    // Setters (nếu bạn cần thay đổi giá trị sau khi đối tượng được tạo)
    public void setThu(int thu) {
        this.thu = thu;
    }

    public void setTiet(int tiet) {
        this.tiet = tiet;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    /**
     * Phương thức này giúp hiển thị thông tin của ChiTietTKB trong các ô của TableView.
     * Định dạng hiển thị có thể là tên môn học và mã lớp.
     * @return Chuỗi đại diện cho đối tượng ChiTietTKB.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean hasMonHoc = tenMonHoc != null && !tenMonHoc.isEmpty();
        boolean hasLop = maLop != null && !maLop.isEmpty();

        if (hasMonHoc) {
            sb.append(tenMonHoc);
        }

        if (hasLop) {
            if (hasMonHoc) {
                sb.append("\n"); // Xuống dòng nếu có cả tên môn và mã lớp
            }
            sb.append("Lớp: ").append(maLop);
        }

        if (!hasMonHoc && !hasLop) {
            return ""; // Trả về chuỗi rỗng nếu không có thông tin
        }
        return sb.toString();
    }
}
