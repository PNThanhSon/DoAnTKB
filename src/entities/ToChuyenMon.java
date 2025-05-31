package entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class ToChuyenMon {
    private String maTCM;
    private String tenTCM;
    private String toTruong;
    private String toPho;
    private Integer slGV;


    public ToChuyenMon(String maTCM, String tenTCM, String toTruong, String toPho) {
        this.maTCM = maTCM;
        this.tenTCM = tenTCM;
        this.toTruong = toTruong;
        this.toPho = toPho;
    }
    public ToChuyenMon(String maTCM, String tenTCM, String toTruong, String toPho, Integer slGV) {
        this.maTCM = maTCM;
        this.tenTCM = tenTCM;
        this.toTruong = toTruong;
        this.toPho = toPho;
        this.slGV = slGV;
    }

    public String getMaTCM() {
        return maTCM;
    }

    public void setMaTCM(String maTCM) {
        this.maTCM = maTCM;
    }

    public String getTenTCM() {
        return tenTCM;
    }

    public void setTenTCM(String tenTCM) {
        this.tenTCM = tenTCM;
    }

    public String getToTruong() {
        return toTruong;
    }

    public void setToTruong(String toTruong) {
        this.toTruong = toTruong;
    }

    public String getToPho() {
        return toPho;
    }

    public void setToPho(String toPho) {
        this.toPho = toPho;
    }

    public Integer getSlGV() {
        return slGV;
    }
    public void setSlGV(Integer slGV) {
        this.slGV = slGV;
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
        return tenTCM; // Dùng khi set ComboBox hiển thị tên tổ
    }
}
