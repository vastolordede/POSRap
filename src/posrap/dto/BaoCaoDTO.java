package posrap.dto;

public class BaoCaoDTO {
    private String tuNgay;        // yyyy-MM-dd
    private String denNgay;       // yyyy-MM-dd
    private long doanhThuVe;
    private long doanhThuBapNuoc;
    private String topPhim;
    private String topMon;

    public BaoCaoDTO() {}

    public String getTuNgay() {
        return tuNgay;
    }

    public void setTuNgay(String tuNgay) {
        this.tuNgay = tuNgay;
    }

    public String getDenNgay() {
        return denNgay;
    }

    public void setDenNgay(String denNgay) {
        this.denNgay = denNgay;
    }

    public long getDoanhThuVe() {
        return doanhThuVe;
    }

    public void setDoanhThuVe(long doanhThuVe) {
        this.doanhThuVe = doanhThuVe;
    }

    public long getDoanhThuBapNuoc() {
        return doanhThuBapNuoc;
    }

    public void setDoanhThuBapNuoc(long doanhThuBapNuoc) {
        this.doanhThuBapNuoc = doanhThuBapNuoc;
    }

    public String getTopPhim() {
        return topPhim;
    }

    public void setTopPhim(String topPhim) {
        this.topPhim = topPhim;
    }

    public String getTopMon() {
        return topMon;
    }

    public void setTopMon(String topMon) {
        this.topMon = topMon;
    }
}
