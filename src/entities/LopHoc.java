package entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class LopHoc {
    private String maLop;
    private String tenLop;
    private String khoi;
    private String maGVCN; // Mã Giáo viên chủ nhiệm (tương ứng cột GVCN trong bảng LOP)

    // Constructors
    public LopHoc() {
    }

    public LopHoc(String maLop, String tenLop, String khoi, String maGVCN) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.khoi = khoi;
        this.maGVCN = maGVCN;
    }

    // Getters
    public String getMaLop() { return maLop;}
    public String getTenLop() { return tenLop;}
    public String getKhoi() { return khoi;}
    public String getMaGVCN() { return maGVCN;}

    // Setters
    public void setMaLop(String maLop) { this.maLop = maLop;}
    public void setTenLop(String tenLop) { this.tenLop = tenLop;}
    public void setKhoi(String khoi) { this.khoi = khoi;}
    public void setMaGVCN(String maGVCN) { this.maGVCN = maGVCN;}

    // JavaFX properties for TableView
    public ObservableValue<String> maLopProperty() {
        return new SimpleStringProperty(maLop);
    }

    public ObservableValue<String> tenLopProperty() {
        return new SimpleStringProperty(tenLop);
    }

    public ObservableValue<String> khoiProperty() {
        return new SimpleStringProperty(khoi);
    }

    public ObservableValue<String> maGVCNProperty() {
        return new SimpleStringProperty(maGVCN);
    }

    @Override
    public String toString() {
        return tenLop != null && !tenLop.isEmpty() ? tenLop : maLop;
    }
}
