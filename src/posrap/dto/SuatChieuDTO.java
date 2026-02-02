package posrap.dto;

import java.util.Date;

public class SuatChieuDTO {
    private int suatChieuId;
    private int phimId;
    private int phongChieuId;
    private Date batDau;
    private Date ketThuc;
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

    public Date getBatDau() {
        return batDau;
    }

    public void setBatDau(Date batDau) {
        this.batDau = batDau;
    }

    public Date getKetThuc() {
        return ketThuc;
    }

    public void setKetThuc(Date ketThuc) {
        this.ketThuc = ketThuc;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }
}
