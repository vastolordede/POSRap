package posrap.dto;

import java.sql.Timestamp;

public class SuatChieuDTO {
    private int suatChieuId;
    private int phimId;
    private int phongChieuId;
    private Timestamp batDau;
    private Timestamp ketThuc;
    private double gia;

    public SuatChieuDTO() {}

    public int getSuatChieuId() {
        return suatChieuId;
    }

    public void setSuatChieuId(int suatChieuId) {
        this.suatChieuId = suatChieuId;
    }

    public int getPhimId() {
        return phimId;
    }

    public void setPhimId(int phimId) {
        this.phimId = phimId;
    }

    public int getPhongChieuId() {
        return phongChieuId;
    }

    public void setPhongChieuId(int phongChieuId) {
        this.phongChieuId = phongChieuId;
    }

    public Timestamp getBatDau() { return batDau; }
    public void setBatDau(Timestamp batDau) { this.batDau = batDau; }

    public Timestamp getKetThuc() { return ketThuc; }
    public void setKetThuc(Timestamp ketThuc) { this.ketThuc = ketThuc; }


    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }
}
