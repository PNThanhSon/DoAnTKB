package entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class ToChuyenMon {
    private String maTCM;
    private String tenTCM;
    private String toTruong; // Assuming MaGV
    private String toPho;    // Assuming MaGV

    public ToChuyenMon() {
    }

    public ToChuyenMon(String maTCM, String tenTCM, String toTruong, String toPho) {
        this.maTCM = maTCM;
        this.tenTCM = tenTCM;
        this.toTruong = toTruong;
        this.toPho = toPho;
    }

    // Getters
    public String getMaTCM() {
        return maTCM;
    }

    public String getTenTCM() {
        return tenTCM;
    }

    public String getToTruong() {
        return toTruong;
    }

    public String getToPho() {
        return toPho;
    }

    // Setters
    public void setMaTCM(String maTCM) {
        this.maTCM = maTCM;
    }

    public void setTenTCM(String tenTCM) {
        this.tenTCM = tenTCM;
    }

    public void setToTruong(String toTruong) {
        this.toTruong = toTruong;
    }

    public void setToPho(String toPho) {
        this.toPho = toPho;
    }

    // JavaFX properties (optional, but good practice if used directly in TableView)
    public ObservableValue<String> maTCMProperty() {
        return new SimpleStringProperty(maTCM);
    }

    public ObservableValue<String> tenTCMProperty() {
        return new SimpleStringProperty(tenTCM);
    }

    @Override
    public String toString() {
        // This is important for ComboBox display
        // It will show TenTCM instead of the object's memory address
        return tenTCM != null ? tenTCM : maTCM;
    }
}
