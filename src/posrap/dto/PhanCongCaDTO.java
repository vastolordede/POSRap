    package posrap.dto;

    public class PhanCongCaDTO {
        private int nhanVienId;
        private int caLamId;
        private String ngayLam;

        public PhanCongCaDTO() {}

        public int getNhanVienId() {
            return nhanVienId;
        }

        public void setNhanVienId(int nhanVienId) {
            this.nhanVienId = nhanVienId;
        }

        public int getCaLamId() {
            return caLamId;
        }

        public void setCaLamId(int caLamId) {
            this.caLamId = caLamId;
        }

        public String getNgayLam() {
            return ngayLam;
        }

        public void setNgayLam(String ngayLam) {
            this.ngayLam = ngayLam;
        }
    }
