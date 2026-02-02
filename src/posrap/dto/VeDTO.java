package posrap.dto;

public class VeDTO {
    private int veId;
    private int suatChieuId;
    private int gheId;
    private double giaVe;
    private String trangThai;

    public VeDTO() {}

    public int getVeId() {
        return veId;
    }

    public void setVeId(int veId) {
        this.veId = veId;
    }

    public int getSuatChieuId() {
        return suatChieuId;
    }

    public void setSuatChieuId(int suatChieuId) {
        this.suatChieuId = suatChieuId;
    }

    public int getGheId() {
        return gheId;
    }

    public void setGheId(int gheId) {
        this.gheId = gheId;
    }

    public double getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(double giaVe) {
        this.giaVe = giaVe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
