package entities;

import javafx.beans.property.*;
import java.time.LocalDate;

public class ThoiKhoaBieu {
    private final StringProperty maTKB   = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> ngayLap    = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> ngayApDung = new SimpleObjectProperty<>();
    private final StringProperty buoi     = new SimpleStringProperty();
    private final StringProperty nguoiTao = new SimpleStringProperty();
    private final StringProperty maHK     = new SimpleStringProperty();

    /* constructor đầy đủ */
    public ThoiKhoaBieu(String ma, LocalDate lap, LocalDate ad, String buoi,
                        String nguoi, String hk) {
        this.maTKB.set(ma); this.ngayLap.set(lap); this.ngayApDung.set(ad);
        this.buoi.set(buoi); this.nguoiTao.set(nguoi); this.maHK.set(hk);
    }

    /* getter / property */
    public StringProperty maTKBProperty()        { return maTKB; }
    public ObjectProperty<LocalDate> ngayLapProperty()    { return ngayLap; }
    public ObjectProperty<LocalDate> ngayApDungProperty() { return ngayApDung; }
    public StringProperty buoiProperty()         { return buoi; }
    public StringProperty nguoiTaoProperty()     { return nguoiTao; }
    public StringProperty maHKProperty()         { return maHK; }

    /* getter đơn giản (dùng trong controller) */
    public String getMaTKB()        { return maTKB.get(); }
    public LocalDate getNgayLap()   { return ngayLap.get(); }
    public LocalDate getNgayApDung(){ return ngayApDung.get(); }
    public String getBuoi()         { return buoi.get(); }
    public String getNguoiTao()     { return nguoiTao.get(); }
    public String getMaHK()         { return maHK.get(); }
}
