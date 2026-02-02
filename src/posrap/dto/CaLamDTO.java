package posrap.dto;

import java.sql.Time;

public class CaLamDTO {
    private int caLamId;
    private Time gioBatDau;
    private Time gioKetThuc;

    public CaLamDTO() {}

    public int getCaLamId() {
        return caLamId;
    }

    public void setCaLamId(int caLamId) {
        this.caLamId = caLamId;
    }

    public Time getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(Time gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public Time getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(Time gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }
}
