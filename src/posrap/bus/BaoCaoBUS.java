package posrap.bus;

import java.sql.Date;
import posrap.dao.BaoCaoDAO;
import posrap.dto.BaoCaoDTO;

public class BaoCaoBUS {

    private final BaoCaoDAO dao = new BaoCaoDAO();

    public BaoCaoDTO xemBaoCao(String tuNgay, String denNgay) throws Exception {
        if (tuNgay == null || tuNgay.trim().isEmpty() || denNgay == null || denNgay.trim().isEmpty()) {
            throw new Exception("Vui long nhap tu ngay va den ngay (yyyy-MM-dd).");
        }

        Date tu;
        Date den;
        try {
            tu = Date.valueOf(tuNgay.trim());
            den = Date.valueOf(denNgay.trim());
        } catch (IllegalArgumentException ex) {
            throw new Exception("Sai dinh dang ngay. Dung: yyyy-MM-dd (vd: 2026-01-31).");
        }

        if (tu.after(den)) {
            throw new Exception("Tu ngay khong duoc lon hon den ngay.");
        }

        BaoCaoDTO dto = dao.getBaoCao(tu, den);
        dto.setTuNgay(tuNgay.trim());
        dto.setDenNgay(denNgay.trim());
        return dto;
    }
}
