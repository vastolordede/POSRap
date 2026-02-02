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

            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public NhanVienDTO getById(int nhanVienId) throws Exception {
        String sql = "SELECT nhan_vien_id, ho_ten, ngay_sinh, so_dien_thoai, email, dia_chi, ma_nv, sdt, trang_thai FROM NhanVien WHERE nhan_vien_id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nhanVienId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapRow(rs);
            }
        }
    }

    public int insert(NhanVienDTO nv) throws Exception {
        String sql =
            "INSERT INTO NhanVien(ho_ten, ngay_sinh, so_dien_thoai, email, dia_chi, ma_nv, sdt, trang_thai) " +
            "VALUES(?,?,?,?,?,?,?,?)";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nv.getHoTen());
            ps.setDate(2, nv.getNgaySinh() == null ? null : new java.sql.Date(nv.getNgaySinh().getTime()));
            ps.setString(3, nv.getSoDienThoai());
            ps.setString(4, nv.getEmail());
            ps.setString(5, nv.getDiaChi());
            ps.setString(6, nv.getMaNv());
            ps.setString(7, nv.getSdt());
            ps.setString(8, nv.getTrangThai());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    public void update(NhanVienDTO nv) throws Exception {
        String sql =
            "UPDATE NhanVien SET ho_ten=?, ngay_sinh=?, so_dien_thoai=?, email=?, dia_chi=?, ma_nv=?, sdt=?, trang_thai=? " +
            "WHERE nhan_vien_id=?";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nv.getHoTen());
            ps.setDate(2, nv.getNgaySinh() == null ? null : new java.sql.Date(nv.getNgaySinh().getTime()));
            ps.setString(3, nv.getSoDienThoai());
            ps.setString(4, nv.getEmail());
            ps.setString(5, nv.getDiaChi());
            ps.setString(6, nv.getMaNv());
            ps.setString(7, nv.getSdt());
            ps.setString(8, nv.getTrangThai());
            ps.setInt(9, nv.getNhanVienId());

            ps.executeUpdate();
        }
    }

    public void delete(int nhanVienId) throws Exception {
        // nếu có FK: PhanCongCa / HoaDon / ... -> cần xóa con hoặc dùng cascade
        String delPcc = "DELETE FROM PhanCongCa WHERE nhan_vien_id=?";
        String delHdVe = "DELETE FROM HoaDonVe WHERE nhan_vien_id=?";
        String delHdBn = "DELETE FROM HoaDonBapNuoc WHERE nhan_vien_id=?";
        String delNv = "DELETE FROM NhanVien WHERE nhan_vien_id=?";

        try (Connection c = getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps1 = c.prepareStatement(delPcc);
                 PreparedStatement ps2 = c.prepareStatement(delHdVe);
                 PreparedStatement ps3 = c.prepareStatement(delHdBn);
                 PreparedStatement ps4 = c.prepareStatement(delNv)) {

                ps1.setInt(1, nhanVienId); ps1.executeUpdate();
                ps2.setInt(1, nhanVienId); ps2.executeUpdate();
                ps3.setInt(1, nhanVienId); ps3.executeUpdate();
                ps4.setInt(1, nhanVienId); ps4.executeUpdate();

                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<NhanVienDTO> search(String keyword) throws Exception {
        String sql =
            "SELECT nhan_vien_id, ho_ten, ngay_sinh, so_dien_thoai, email, dia_chi, ma_nv, sdt, trang_thai " +
            "FROM NhanVien " +
            "WHERE ho_ten LIKE ? OR ma_nv LIKE ? OR sdt LIKE ? OR email LIKE ? " +
            "ORDER BY nhan_vien_id";

        String k = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        List<NhanVienDTO> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);
            ps.setString(4, k);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    private NhanVienDTO mapRow(ResultSet rs) throws Exception {
        NhanVienDTO nv = new NhanVienDTO();
        nv.setNhanVienId(rs.getInt("nhan_vien_id"));
        nv.setHoTen(rs.getString("ho_ten"));
        nv.setNgaySinh(rs.getDate("ngay_sinh"));
        nv.setSoDienThoai(rs.getString("so_dien_thoai"));
        nv.setEmail(rs.getString("email"));
        nv.setDiaChi(rs.getString("dia_chi"));
        nv.setMaNv(rs.getString("ma_nv"));
        nv.setSdt(rs.getString("sdt"));
        nv.setTrangThai(rs.getString("trang_thai"));
        return nv;
    }

    // giữ hàm tính giờ bạn có
    public Map<Integer, Double> tinhTongGioTheoThang(int thang, int nam) throws Exception {
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
