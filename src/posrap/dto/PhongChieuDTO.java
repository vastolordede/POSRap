package posrap.dto;

public class PhongChieuDTO {
    private int phongChieuId;
    private String tenPhong;
    private int sucChua;
    private String trangThai;

    public PhongChieuDTO() {}

    public int getPhongChieuId() {
        return phongChieuId;
    }

    public void setPhongChieuId(int phongChieuId) {
        this.phongChieuId = phongChieuId;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
