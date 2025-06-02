package entities;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ThoiKhoaBieu {

    /* ===== Property ===== */
    private final StringProperty            maTKB      = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> ngayLap    = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> ngayApDung = new SimpleObjectProperty<>();
    private final StringProperty            buoi       = new SimpleStringProperty();
    private final StringProperty            nguoiTao   = new SimpleStringProperty();
    private final StringProperty            maHK       = new SimpleStringProperty();

    /* ===== Constructor đầy đủ (6 tham số) ===== */
    public ThoiKhoaBieu(String maTKB,
                        LocalDate ngayLap,
                        LocalDate ngayApDung,
                        String buoi,
                        String nguoiTao,
                        String maHK) {
        this.maTKB.set(maTKB);
        this.ngayLap.set(ngayLap);
        this.ngayApDung.set(ngayApDung);
        this.buoi.set(buoi);
        this.nguoiTao.set(nguoiTao);
        this.maHK.set(maHK);
    }

    /* ===== Constructor rút gọn (4 tham số) – nếu DAO cũ dùng ===== */
    public ThoiKhoaBieu(String maTKB,
                        java.sql.Date ngayApDung,
                        String buoi,
                        String maHK) {
        this(maTKB,
                ngayApDung != null ? ngayApDung.toLocalDate() : null,
                ngayApDung != null ? ngayApDung.toLocalDate() : null,
                buoi,
                "",
                maHK);
    }

    /* ===== Getters ===== */
    public String getMaTKB()         { return maTKB.get(); }
    public LocalDate getNgayLap()    { return ngayLap.get(); }
    public LocalDate getNgayApDung() { return ngayApDung.get(); }
    public String getBuoi()          { return buoi.get(); }
    public String getNguoiTao()      { return nguoiTao.get(); }
    public String getMaHK()          { return maHK.get(); }

    /* ===== Property getters (TableView cần) ===== */
    public StringProperty            maTKBProperty()      { return maTKB; }
    public ObjectProperty<LocalDate> ngayLapProperty()    { return ngayLap; }
    public ObjectProperty<LocalDate> ngayApDungProperty() { return ngayApDung; }
    public StringProperty            buoiProperty()       { return buoi; }
    public StringProperty            nguoiTaoProperty()   { return nguoiTao; }
    public StringProperty            maHKProperty()       { return maHK; }

    /* ===== toString() cho ComboBox (nếu dùng) ===== */
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override public String toString() {
        String d = ngayApDung.get() != null ? FMT.format(ngayApDung.get()) : "N/A";
        return "TKB " + (buoi.get()==null? "":buoi.get().toUpperCase()) + " – áp dụng: " + d;
    }

    public void setMaTKB(String newVal) {
    }

    public void setBuoi(String newVal) {
    }

    public void setNguoiTao(String newVal) {
    }

    public void setMaHK(String newVal) {
    }

    public void setNgayLap(LocalDate newDate) {
    }

    public void setNgayApDung(LocalDate newDate) {
    }
}
