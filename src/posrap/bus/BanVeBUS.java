package posrap.bus;

import java.util.List;
import posrap.dao.VeDAO;
import posrap.dto.*;

public class BanVeBUS {

    private final VeDAO veDAO = new VeDAO();

    public List<GheDTO> laySoDoGhe(int suatChieuId) throws Exception {
        return veDAO.getGheTheoSuatChieu(suatChieuId);
    }

    public boolean gheConTrong(int suatChieuId, int gheId) throws Exception {
        return veDAO.isGheTrong(suatChieuId, gheId);
    }

    public HoaDonVeDTO lapHoaDonVe(
            int suatChieuId,
            List<GheDTO> dsGhe
    ) throws Exception {
        return veDAO.taoHoaDonVe(suatChieuId, dsGhe);
    }
    public java.util.Set<Integer> getGheDaBan(int suatChieuId) throws Exception {
    return veDAO.getGheDaBan(suatChieuId);
}

}
