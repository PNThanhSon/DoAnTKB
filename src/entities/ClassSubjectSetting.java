package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClassSubjectSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maLop;
    private String maMH;
    // Danh sách mã giáo viên được chỉ định cho môn này của lớp này.
    // Nếu rỗng, nghĩa là "Để thuật toán tự chọn".
    private List<String> assignedTeacherIds;
    private String tenMHDisplay; // Chỉ dùng cho hiển thị, không lưu trữ cố định

    public ClassSubjectSetting(String maLop, String maMH) {
        this.maLop = maLop;
        this.maMH = maMH;
        this.assignedTeacherIds = new ArrayList<>();
    }

    public ClassSubjectSetting(String maLop, String maMH, String tenMHDisplay) {
        this(maLop, maMH);
        this.tenMHDisplay = tenMHDisplay;
    }

    public String getMaLop() {
        return maLop;
    }

    public String getMaMH() {
        return maMH;
    }

    public List<String> getAssignedTeacherIds() {
        return assignedTeacherIds;
    }

    public void setAssignedTeacherIds(List<String> assignedTeacherIds) {
        this.assignedTeacherIds = assignedTeacherIds != null ? new ArrayList<>(assignedTeacherIds) : new ArrayList<>();
    }

    public void addAssignedTeacherId(String maGV) {
        if (maGV != null && !maGV.isEmpty() && !this.assignedTeacherIds.contains(maGV)) {
            this.assignedTeacherIds.add(maGV);
        }
    }

    public void removeAssignedTeacherId(String maGV) {
        this.assignedTeacherIds.remove(maGV);
    }

    public boolean isAlgorithmChoice() {
        return assignedTeacherIds == null || assignedTeacherIds.isEmpty();
    }

    public String getTenMHDisplay() {
        return tenMHDisplay;
    }

    public void setTenMHDisplay(String tenMHDisplay) {
        this.tenMHDisplay = tenMHDisplay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassSubjectSetting that = (ClassSubjectSetting) o;
        return Objects.equals(maLop, that.maLop) && Objects.equals(maMH, that.maMH);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maLop, maMH);
    }

    @Override
    public String toString() {
        return "ClassSubjectSetting{" +
                "maLop='" + maLop + '\'' +
                ", maMH='" + maMH + '\'' +
                ", assignedTeacherIds=" + assignedTeacherIds.stream().collect(Collectors.joining(", ")) +
                '}';
    }
}
    