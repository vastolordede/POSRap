package posrap.dto;

import java.util.Date;

public class NhanVienDTO {
    private int nhanVienId;
    private String hoTen;
    private Date ngaySinh;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private String maNv;       // DB có ma_nv
    private String sdt;        // DB có sdt (nếu bạn tách so_dien_thoai và sdt thì giữ cả 2, còn không thì bỏ 1)
    private String trangThai;  // DB có trang_thai

    public NhanVienDTO() {}

    public int getNhanVienId() { return nhanVienId; }
    public void setNhanVienId(int nhanVienId) { this.nhanVienId = nhanVienId; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getMaNv() { return maNv; }
    public void setMaNv(String maNv) { this.maNv = maNv; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
