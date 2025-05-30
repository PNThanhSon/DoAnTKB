package entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class MonHoc {
    private String maMH;
    private String tenMH;
    private String khoi;
    private String maTCM;

    public MonHoc(String maMH, String tenMH, String khoi, String maTCM) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.khoi = khoi;
        this.maTCM = maTCM;
    }

    // Getters
    public String getMaMH() {
        return maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public String getKhoi() {
        return khoi;
    }

    public String getMaTCM() {
        return maTCM;
    }
    // set
    public void setMaMH(String maMH) { this.maMH = maMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }
    public void setKhoi(String khoi) { this.khoi = khoi; }
    public void setMaTCM(String maTCM) { this.maTCM = maTCM; }

    @Override
    public String toString() {
        // Hiển thị trong ComboBox nếu không dùng NhomMonHocDisplay
        return tenMH + (khoi != null && !khoi.isEmpty() ? " (" + khoi + ")" : "");
    }

    public ObservableValue<String> maMHProperty() {
        return new SimpleStringProperty(maMH);
    }

    public ObservableValue<String> tenMHProperty() {
        return new SimpleStringProperty(tenMH);
    }

    public ObservableValue<String> khoiProperty() {
        return new SimpleStringProperty(khoi);
    }

    public ObservableValue<String> maTCMProperty() {
        return new SimpleStringProperty(maTCM);
    }
}
