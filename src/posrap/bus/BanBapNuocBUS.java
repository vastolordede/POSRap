package posrap.bus;

import java.util.List;
import posrap.dao.BapNuocDAO;
import posrap.dto.*;

public class BanBapNuocBUS {

    private final BapNuocDAO dao = new BapNuocDAO();

    public List<MonBapNuocDTO> layDanhSachMon() throws Exception {
        return dao.getAllMon();
    }

    public List<SizeDTO> layDanhSachSize() throws Exception {
        return dao.getAllSize();
    }

    public BienTheMonDTO layBienThe(int monId, int sizeId) throws Exception {
        return dao.getBienThe(monId, sizeId);
    }

    // --- thay hàm lapHoaDon bằng bản này ---
public HoaDonBapNuocDTO lapHoaDon(List<ChiTietHoaDonBapNuocDTO> ct) throws Exception {
    return dao.taoHoaDon(ct);
}
}
