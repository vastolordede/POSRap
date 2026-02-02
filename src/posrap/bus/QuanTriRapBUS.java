package posrap.bus;

import java.util.List;
import posrap.dao.RapChieuDAO;
import posrap.dto.*;

public class QuanTriRapBUS {

    private final RapChieuDAO dao = new RapChieuDAO();

    public List<PhimDTO> getAllPhim() throws Exception {
        return dao.getAllPhim();
    }

    public void themPhim(PhimDTO p) throws Exception {
        dao.insertPhim(p);
    }

    public void xoaPhim(int id) throws Exception {
        dao.deletePhim(id);
    }
}
