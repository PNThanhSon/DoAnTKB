package entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TeacherCustomSettings implements Serializable {
    private static final long serialVersionUID = 1L; // For potential serialization later

    private String maGV;
    private boolean participateInScheduling;
    // Key: MaMH (Mã Môn Học), Value: true nếu giáo viên được chọn dạy môn này, false nếu không
    private Map<String, Boolean> subjectTeachingPreference;

    public TeacherCustomSettings(String maGV) {
        this.maGV = maGV;
        this.participateInScheduling = true; // Mặc định là tham gia xếp TKB
        this.subjectTeachingPreference = new HashMap<>();
    }

    public String getMaGV() {
        return maGV;
    }

    public boolean isParticipateInScheduling() {
        return participateInScheduling;
    }

    public void setParticipateInScheduling(boolean participateInScheduling) {
        this.participateInScheduling = participateInScheduling;
    }

    public Map<String, Boolean> getSubjectTeachingPreference() {
        return subjectTeachingPreference;
    }

    public void setSubjectTeachingPreference(Map<String, Boolean> subjectTeachingPreference) {
        this.subjectTeachingPreference = subjectTeachingPreference;
    }

    // Tiện ích để lấy hoặc đặt tùy chọn cho một môn học cụ thể
    public boolean getTeachingPreferenceForSubject(String maMH) {
        // Mặc định là true nếu không có cài đặt cụ thể (nghĩa là GV sẽ dạy môn đó nếu thuộc TCM của họ)
        return subjectTeachingPreference.getOrDefault(maMH, true);
    }

    public void setTeachingPreferenceForSubject(String maMH, boolean teaches) {
        subjectTeachingPreference.put(maMH, teaches);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherCustomSettings that = (TeacherCustomSettings) o;
        return Objects.equals(maGV, that.maGV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maGV);
    }

    @Override
    public String toString() {
        return "TeacherCustomSettings{" +
                "maGV='" + maGV + '\'' +
                ", participateInScheduling=" + participateInScheduling +
                ", subjectPreferences=" + subjectTeachingPreference.size() + " môn" +
                '}';
    }
}
