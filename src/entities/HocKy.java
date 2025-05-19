package entities;

/**
 * Đại diện cho một Học Kỳ trong năm học.
 */
public class HocKy {
    private String maHK;     // Mã định danh duy nhất cho học kỳ
    private String tenHocKy; // Tên của học kỳ, ví dụ: "Học Kỳ 1", "Học Kỳ Hè"
    private String namHoc;   // Năm học, ví dụ: "2024-2025"

    /**
     * Constructor để tạo một đối tượng HocKy.
     * @param maHK Mã học kỳ.
     * @param tenHocKy Tên học kỳ.
     * @param namHoc Năm học.
     */
    public HocKy(String maHK, String tenHocKy, String namHoc) {
        this.maHK = maHK;
        this.tenHocKy = tenHocKy;
        this.namHoc = namHoc;
    }

    // Getters
    public String getMaHK() {
        return maHK;
    }

    public String getTenHocKy() {
        return tenHocKy;
    }

    public String getNamHoc() {
        return namHoc;
    }

    // Setters (nếu cần)
    public void setMaHK(String maHK) {
        this.maHK = maHK;
    }

    public void setTenHocKy(String tenHocKy) {
        this.tenHocKy = tenHocKy;
    }

    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }

    /**
     * Phương thức này sẽ được ComboBox sử dụng để hiển thị thông tin Học Kỳ.
     * @return Chuỗi đại diện cho đối tượng HocKy, thường là tên học kỳ và năm học.
     */
    @Override
    public String toString() {
        // Đảm bảo không có giá trị null được hiển thị là "null"
        String displayTenHK = (tenHocKy != null ? tenHocKy : "");
        String displayNamHoc = (namHoc != null ? namHoc : "");

        if (!displayTenHK.isEmpty() && !displayNamHoc.isEmpty()) {
            return displayTenHK + " - " + displayNamHoc;
        } else if (!displayTenHK.isEmpty()) {
            return displayTenHK;
        } else if (!displayNamHoc.isEmpty()) {
            return displayNamHoc;
        } else {
            return maHK; // Hoặc một giá trị mặc định khác nếu cả hai đều rỗng
        }
    }
}
