package entities;

import javafx.beans.property.SimpleStringProperty;

public class ChucVu {
    private final SimpleStringProperty maCV;
    private final SimpleStringProperty tenCV;

    public ChucVu(String maCV, String tenCV) {
        this.maCV = new SimpleStringProperty(maCV);
        this.tenCV = new SimpleStringProperty(tenCV);
    }
    public String getMaCV()  { return maCV.get();  }
    public String getTenCV() { return tenCV.get(); }
}

