package posrap.dto;

public class GheDTO {
    private int gheId;
    private int phongChieuId;
    private String hangGhe;
    private int soGhe;
    private String loaiGhe;

    public GheDTO() {}

    public int getGheId() {
        return gheId;
    }

    public void setGheId(int gheId) {
        this.gheId = gheId;
    }

    public int getPhongChieuId() {
        return phongChieuId;
    }

    public void setPhongChieuId(int phongChieuId) {
        this.phongChieuId = phongChieuId;
    }

    public String getHangGhe() {
        return hangGhe;
    }

    public void setHangGhe(String hangGhe) {
        this.hangGhe = hangGhe;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public String getLoaiGhe() {
        return loaiGhe;
    }

    public void setLoaiGhe(String loaiGhe) {
        this.loaiGhe = loaiGhe;
    }
}
