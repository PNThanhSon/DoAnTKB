package entities;

import java.text.SimpleDateFormat;
import java.util.Date; // Sử dụng java.util.Date cho ngày tháng

/**
 * Đại diện cho một Thời Khóa Biểu cụ thể, áp dụng từ một ngày nhất định,
 * cho một buổi học và thuộc một học kỳ.
 */
public class ThoiKhoaBieu {
    private String maTKB;        // Mã định danh duy nhất cho thời khóa biểu
    private Date ngayApDung;   // Ngày bắt đầu áp dụng thời khóa biểu này (java.util.Date)
    private String buoi;         // Buổi học, ví dụ: "SANG" hoặc "CHIEU"
    private String maHK;         // Mã học kỳ mà thời khóa biểu này thuộc về

    /**
     * Constructor để tạo một đối tượng ThoiKhoaBieu.
     * @param maTKB Mã thời khóa biểu.
     * @param ngayApDung Ngày áp dụng (java.util.Date).
     * @param buoi Buổi học ("SANG" hoặc "CHIEU").
     * @param maHK Mã học kỳ.
     */
    public ThoiKhoaBieu(String maTKB, Date ngayApDung, String buoi, String maHK) {
        this.maTKB = maTKB;
        this.ngayApDung = ngayApDung;
        this.buoi = buoi;
        this.maHK = maHK;
    }

    // Getters
    public String getMaTKB() {
        return maTKB;
    }

    public Date getNgayApDung() {
        return ngayApDung;
    }

    public String getBuoi() {
        return buoi;
    }

    public String getMaHK() {
        return maHK;
    }

    // Setters (nếu cần)
    public void setMaTKB(String maTKB) {
        this.maTKB = maTKB;
    }

    public void setNgayApDung(Date ngayApDung) {
        this.ngayApDung = ngayApDung;
    }

    public void setBuoi(String buoi) {
        this.buoi = buoi;
    }

    public void setMaHK(String maHK) {
        this.maHK = maHK;
    }

    /**
     * Phương thức này sẽ được ComboBox sử dụng để hiển thị thông tin Thời Khóa Biểu.
     * @return Chuỗi đại diện cho đối tượng ThoiKhoaBieu.
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ngayApDungStr = (ngayApDung != null) ? sdf.format(ngayApDung) : "N/A";
        String buoiStr = (buoi != null && !buoi.isEmpty()) ? (" " + buoi.toUpperCase() + " ") : "";

        // Ví dụ hiển thị: TKB SANG - Áp dụng từ: 19/05/2025
        return  buoiStr + " - Áp dụng từ: " + ngayApDungStr;
    }
}
