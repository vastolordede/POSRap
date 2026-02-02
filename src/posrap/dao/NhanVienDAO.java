package posrap.dao;

import java.sql.*;
import java.util.*;
import posrap.dto.NhanVienDTO;

public class NhanVienDAO extends BaseDAO {

    public List<NhanVienDTO> getAllNhanVien() throws Exception {
        String sql = "SELECT nhan_vien_id, ho_ten, ngay_sinh, so_dien_thoai, email, dia_chi, ma_nv, sdt, trang_thai FROM NhanVien";
        List<NhanVienDTO> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setNhanVienId(rs.getInt("nhan_vien_id"));
                nv.setHoTen(rs.getString("ho_ten"));
                nv.setNgaySinh(rs.getDate("ngay_sinh")); // hoặc rs.getTimestamp nếu DB dùng datetime
                nv.setSoDienThoai(rs.getString("so_dien_thoai"));
                nv.setEmail(rs.getString("email"));
                nv.setDiaChi(rs.getString("dia_chi"));
                nv.setMaNv(rs.getString("ma_nv"));
                nv.setSdt(rs.getString("sdt"));
                nv.setTrangThai(rs.getString("trang_thai"));
                list.add(nv);
            }
        }
        return list;
    }

    // Nếu thầy không yêu cầu module lương thì có thể bỏ luôn method này.
    public Map<Integer, Double> tinhTongGioTheoThang(int thang, int nam) throws Exception {
    // trả về: nhan_vien_id -> tổng giờ
    // DATEDIFF(MINUTE) / 60.0 để ra giờ
    String sql =
        "SELECT p.nhan_vien_id, " +
        "       SUM(DATEDIFF(MINUTE, c.gio_bat_dau, c.gio_ket_thuc)) / 60.0 AS tong_gio " +
        "FROM PhanCongCa p " +
        "JOIN CaLam c ON c.ca_lam_id = p.ca_lam_id " +
        "WHERE MONTH(p.ngay_lam)=? AND YEAR(p.ngay_lam)=? " +
        "GROUP BY p.nhan_vien_id";

    Map<Integer, Double> map = new HashMap<>();

    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, thang);
        ps.setInt(2, nam);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getInt("nhan_vien_id"), rs.getDouble("tong_gio"));
            }
        }
    }
    return map;
}

}
