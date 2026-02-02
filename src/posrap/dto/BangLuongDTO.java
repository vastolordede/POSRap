package posrap.dto;

public class BangLuongDTO {
    private int bangLuongId;
    private int nhanVienId;
    private double tongLuong;
    private String thang;

    public BangLuongDTO() {}

    public int getBangLuongId() {
        return bangLuongId;
    }

    public void setBangLuongId(int bangLuongId) {
        this.bangLuongId = bangLuongId;
    }

    public int getNhanVienId() {
        return nhanVienId;
    }

    public void setNhanVienId(int nhanVienId) {
        this.nhanVienId = nhanVienId;
    }

    public double getTongLuong() {
        return tongLuong;
    }

    public void setTongLuong(double tongLuong) {
        this.tongLuong = tongLuong;
    }

    public String getThang() {
        return thang;
    }

    public void setThang(String thang) {
        this.thang = thang;
    }

    
}
