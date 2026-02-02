    package posrap.dto;

    import java.util.Date;

    public class HoaDonVeDTO {
        private int hoaDonVeId;
        private int nhanVienId;
        private Date ngayLap;
        private double tongTien;

        public HoaDonVeDTO() {}

        public int getHoaDonVeId() {
            return hoaDonVeId;
        }

        public void setHoaDonVeId(int hoaDonVeId) {
            this.hoaDonVeId = hoaDonVeId;
        }

        public int getNhanVienId() {
            return nhanVienId;
        }

        public void setNhanVienId(int nhanVienId) {
            this.nhanVienId = nhanVienId;
        }

        public Date getNgayLap() {
            return ngayLap;
        }

        public void setNgayLap(Date ngayLap) {
            this.ngayLap = ngayLap;
        }

        public double getTongTien() {
            return tongTien;
        }

        public void setTongTien(double tongTien) {
            this.tongTien = tongTien;
        }
    }
