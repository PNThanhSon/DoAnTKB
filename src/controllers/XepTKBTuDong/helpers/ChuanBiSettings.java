package controllers.XepTKBTuDong.helpers; // Đặt trong package phù hợp

import entities.TeacherCustomSettings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChuanBiSettings {
    private String selectedMaHK;
    private String selectedMaTkbCoSo;
    private Map<String, Map<Integer, Integer>> soTietMoiThuTheoKhoi;
    private Map<String, TeacherCustomSettings> teacherCustomSettingsMap;
    private Map<String, Map<String, List<String>>> classTeacherAssignmentsMap;

    // Jackson cần một constructor không tham số
    public ChuanBiSettings() {
        this.soTietMoiThuTheoKhoi = new HashMap<>();
        this.teacherCustomSettingsMap = new HashMap<>();
        this.classTeacherAssignmentsMap = new HashMap<>();
    }

    // Getters and Setters
    public String getSelectedMaHK() {
        return selectedMaHK;
    }

    public void setSelectedMaHK(String selectedMaHK) {
        this.selectedMaHK = selectedMaHK;
    }

    public String getSelectedMaTkbCoSo() {
        return selectedMaTkbCoSo;
    }

    public void setSelectedMaTkbCoSo(String selectedMaTkbCoSo) {
        this.selectedMaTkbCoSo = selectedMaTkbCoSo;
    }

    public Map<String, Map<Integer, Integer>> getSoTietMoiThuTheoKhoi() {
        return soTietMoiThuTheoKhoi;
    }

    public void setSoTietMoiThuTheoKhoi(Map<String, Map<Integer, Integer>> soTietMoiThuTheoKhoi) {
        this.soTietMoiThuTheoKhoi = soTietMoiThuTheoKhoi;
    }

    public Map<String, TeacherCustomSettings> getTeacherCustomSettingsMap() {
        return teacherCustomSettingsMap;
    }

    public void setTeacherCustomSettingsMap(Map<String, TeacherCustomSettings> teacherCustomSettingsMap) {
        this.teacherCustomSettingsMap = teacherCustomSettingsMap;
    }

    public Map<String, Map<String, List<String>>> getClassTeacherAssignmentsMap() {
        return classTeacherAssignmentsMap;
    }

    public void setClassTeacherAssignmentsMap(Map<String, Map<String, List<String>>> classTeacherAssignmentsMap) {
        this.classTeacherAssignmentsMap = classTeacherAssignmentsMap;
    }
}
