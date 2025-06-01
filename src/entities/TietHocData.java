package entities;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TietHocData {
    private final StringProperty tiet; // Sẽ chứa "Tiết 1", "Tiết 2",...
    // Sử dụng ObjectProperty<ChiTietTKB> để TableView có thể hiển thị đối tượng này
    // (thông qua phương thức toString() của ChiTietTKB hoặc CellFactory tùy chỉnh)
    private final ObjectProperty<ChiTietTKB> thu2;
    private final ObjectProperty<ChiTietTKB> thu3;
    private final ObjectProperty<ChiTietTKB> thu4;
    private final ObjectProperty<ChiTietTKB> thu5;
    private final ObjectProperty<ChiTietTKB> thu6;
    private final ObjectProperty<ChiTietTKB> thu7;

    public TietHocData(String tietValue) {
        this.tiet = new SimpleStringProperty(tietValue);
        this.thu2 = new SimpleObjectProperty<>(null); // Khởi tạo là null
        this.thu3 = new SimpleObjectProperty<>(null);
        this.thu4 = new SimpleObjectProperty<>(null);
        this.thu5 = new SimpleObjectProperty<>(null);
        this.thu6 = new SimpleObjectProperty<>(null);
        this.thu7 = new SimpleObjectProperty<>(null);
    }

    // Getters cho JavaFX Properties (cần thiết cho TableView)
    public StringProperty tietProperty() { return tiet; }
    public ObjectProperty<ChiTietTKB> thu2Property() { return thu2; }
    public ObjectProperty<ChiTietTKB> thu3Property() { return thu3; }
    public ObjectProperty<ChiTietTKB> thu4Property() { return thu4; }
    public ObjectProperty<ChiTietTKB> thu5Property() { return thu5; }
    public ObjectProperty<ChiTietTKB> thu6Property() { return thu6; }
    public ObjectProperty<ChiTietTKB> thu7Property() { return thu7; }

    // Setters để cập nhật thông tin môn học
    public void setThu2(ChiTietTKB value) { this.thu2.set(value); }
    public void setThu3(ChiTietTKB value) { this.thu3.set(value); }
    public void setThu4(ChiTietTKB value) { this.thu4.set(value); }
    public void setThu5(ChiTietTKB value) { this.thu5.set(value); }
    public void setThu6(ChiTietTKB value) { this.thu6.set(value); }
    public void setThu7(ChiTietTKB value) { this.thu7.set(value); }

    // Getters cho giá trị (tùy chọn)
    public String getTiet() { return tiet.get(); }
    public ChiTietTKB getThu2() { return thu2.get(); }

    public ChiTietTKB getThu3() { return thu3.get(); }
    public ChiTietTKB getThu4() { return thu4.get(); }
    public ChiTietTKB getThu5() { return thu5.get(); }
    public ChiTietTKB getThu6() { return thu6.get(); }
    public ChiTietTKB getThu7() { return thu7.get(); }
    // ...
    public void clearDayData() {
        setThu2(null);
        setThu3(null);
        setThu4(null);
        setThu5(null);
        setThu6(null);
        setThu7(null);
    }
}