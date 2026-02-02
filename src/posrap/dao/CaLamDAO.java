package posrap.dao;

import java.sql.*;
import java.util.*;
import posrap.dto.CaLamDTO;

public class CaLamDAO extends BaseDAO {

    public List<CaLamDTO> getAll() throws Exception {
        String sql = "SELECT ca_lam_id, gio_bat_dau, gio_ket_thuc FROM CaLam ORDER BY ca_lam_id";
        List<CaLamDTO> list = new ArrayList<>();

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CaLamDTO ca = new CaLamDTO();
                ca.setCaLamId(rs.getInt("ca_lam_id"));
                ca.setGioBatDau(rs.getTime("gio_bat_dau"));
                ca.setGioKetThuc(rs.getTime("gio_ket_thuc"));
                list.add(ca);
            }
        }
        return list;
    }

    public int insert(CaLamDTO ca) throws Exception {
        String sql = "INSERT INTO CaLam(gio_bat_dau, gio_ket_thuc) VALUES(?,?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setTime(1, ca.getGioBatDau());
            ps.setTime(2, ca.getGioKetThuc());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    public void update(CaLamDTO ca) throws Exception {
        String sql = "UPDATE CaLam SET gio_bat_dau=?, gio_ket_thuc=? WHERE ca_lam_id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setTime(1, ca.getGioBatDau());
            ps.setTime(2, ca.getGioKetThuc());
            ps.setInt(3, ca.getCaLamId());
            ps.executeUpdate();
        }
    }

    public void delete(int caLamId) throws Exception {
        // phải xóa phân công trước nếu còn
        String delPcc = "DELETE FROM PhanCongCa WHERE ca_lam_id=?";
        String delCa  = "DELETE FROM CaLam WHERE ca_lam_id=?";

        try (Connection c = getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps1 = c.prepareStatement(delPcc);
                 PreparedStatement ps2 = c.prepareStatement(delCa)) {
                ps1.setInt(1, caLamId);
                ps1.executeUpdate();

                ps2.setInt(1, caLamId);
                ps2.executeUpdate();

                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }
}
