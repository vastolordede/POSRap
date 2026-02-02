package posrap.bus;

import java.util.List;
import java.util.Map;

import posrap.dao.BangLuongDAO;
import posrap.dao.NhanVienDAO;
import posrap.dto.NhanVienDTO;

public class NhanSuBUS {

    private final NhanVienDAO dao = new NhanVienDAO();
    private final BangLuongDAO bangLuongDAO = new BangLuongDAO();

    // fix cứng lương/giờ cho đồ án
    private static final double LUONG_MOT_GIO = 30000;

    public List<NhanVienDTO> getAllNhanVien() throws Exception {
        return dao.getAllNhanVien();
    }

    public void tinhLuong(int thang, int nam) throws Exception {
        // ✅ gọi instance method
        Map<Integer, Double> tongGioTheoNV = dao.tinhTongGioTheoThang(thang, nam);

        String thangYYYYMM = String.format("%04d-%02d", nam, thang);

        for (Map.Entry<Integer, Double> e : tongGioTheoNV.entrySet()) {
            int nhanVienId = e.getKey();
            double tongGio = e.getValue();
            double tongLuong = tongGio * LUONG_MOT_GIO;

            bangLuongDAO.upsertBangLuong(nhanVienId, thangYYYYMM, tongLuong);
        }
    }
}
