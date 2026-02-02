package posrap.dto;

public class PhimDTO {
    private int phimId;
    private String tenPhim;
    private String theLoai;
    private int thoiLuong;
    private String doTuoi;
    private String trangThai;

    public PhimDTO() {}

    public int getPhimId() {
        return phimId;
    }

    public void setPhimId(int phimId) {
        this.phimId = phimId;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getDoTuoi() {
        return doTuoi;
    }

    public void setDoTuoi(String doTuoi) {
        this.doTuoi = doTuoi;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
