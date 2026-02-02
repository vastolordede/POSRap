package posrap.dto;

public class BienTheMonDTO {
    private int bienTheId;
    private int monId;
    private int sizeId;
    private double gia;

    public BienTheMonDTO() {}

    public int getBienTheId() {
        return bienTheId;
    }

    public void setBienTheId(int bienTheId) {
        this.bienTheId = bienTheId;
    }

    public int getMonId() {
        return monId;
    }

    public void setMonId(int monId) {
        this.monId = monId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }
}
