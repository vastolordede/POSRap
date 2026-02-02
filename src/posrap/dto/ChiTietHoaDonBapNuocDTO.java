package posrap.dto;

public class ChiTietHoaDonBapNuocDTO {
    private int hoaDonId;
    private int bienTheId;
    private int soLuong;

    public ChiTietHoaDonBapNuocDTO() {}

    public int getHoaDonId() {
        return hoaDonId;
    }

    public void setHoaDonId(int hoaDonId) {
        this.hoaDonId = hoaDonId;
    }

    public int getBienTheId() {
        return bienTheId;
    }

    public void setBienTheId(int bienTheId) {
        this.bienTheId = bienTheId;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
