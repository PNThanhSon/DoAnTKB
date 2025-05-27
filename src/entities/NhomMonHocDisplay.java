package entities;

import java.util.List;
import java.util.Objects;

/**
 * Đại diện cho một nhóm các môn học có cùng tên nhưng có thể khác mã hoặc khối,
 * dùng để hiển thị trong ComboBox chọn môn học của Tổ Chuyên Môn.
 */
public class NhomMonHocDisplay {
    private final String tenMonHocChung; // Tên môn học duy nhất để hiển thị
    private final List<String> danhSachMaMH; // Danh sách các MaMH thuộc nhóm này
    private final String maTCM; // Mã tổ chuyên môn quản lý nhóm môn học này

    public NhomMonHocDisplay(String tenMonHocChung, List<String> danhSachMaMH, String maTCM) {
        this.tenMonHocChung = tenMonHocChung;
        this.danhSachMaMH = danhSachMaMH;
        this.maTCM = maTCM;
    }

    public String getTenMonHocChung() {
        return tenMonHocChung;
    }

    public List<String> getDanhSachMaMH() {
        return danhSachMaMH;
    }

    public String getMaTCM() {
        return maTCM;
    }

    @Override
    public String toString() {
        return tenMonHocChung; // Chỉ hiển thị tên chung trong ComboBox
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhomMonHocDisplay that = (NhomMonHocDisplay) o;
        return Objects.equals(tenMonHocChung, that.tenMonHocChung) &&
                Objects.equals(maTCM, that.maTCM);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenMonHocChung, maTCM);
    }
}
